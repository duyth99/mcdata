package mcdata.api.process;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.gson.Gson;

import mcdata.api.common.Constants;
import mcdata.api.common.Utils;
import mcdata.api.exception.ValidateException;
import mcdata.api.model.AllRequest;
import mcdata.api.model.ArpuRequest;
import mcdata.api.model.BaseInfoRequest;
import mcdata.api.model.CallHistRequest;
import mcdata.api.model.RequestObject;
import mcdata.api.model.ResponseObject;
import mcdata.api.model.SmsHistRequest;
import mcdata.api.model.TopupHistRequest;
import mcdata.api.process.flow.ArpuFlow;
import mcdata.api.process.flow.BaseInfoFlow;
import mcdata.api.process.flow.CallHistFlow;
import mcdata.api.process.flow.SmsHistFlow;
import mcdata.api.process.flow.TopupHistFlow;
import mcdata.api.provider.ApiProcessingInterface;
import mcdata.api.server.impl.PlatformImpl;

public class GetAll {
	public static final ApiProcessingInterface<ResponseObject> PROCESS = (RequestObject requestObject, int maxMsisdn) -> {

		long now = System.currentTimeMillis();
		int batch = 0;

		String request_type = requestObject.getRequest_type();
		if(!request_type.equals("all")) {
			throw new ValidateException();
		}
		
		Object request_body = requestObject.getRequest_body();
		
		Gson gson = new Gson();
		AllRequest allRequest = gson.fromJson(gson.toJson(request_body), AllRequest.class);
		allRequest.setMsisdns(Arrays.stream(allRequest.getMsisdns()).distinct().toArray(String[]::new));
		
		if(!Utils.isThisDateValid(allRequest.getStart_date(),"yyyyMMdd") || !Utils.isThisDateValid(allRequest.getEnd_date(),"yyyyMMdd")) {
			throw new ValidateException("sai yyyyMMdd");
		}
//		if(allRequest.getMsisdns().length > PlatformImpl.getPlatform().getServerProp().getMax_msisdns()) {
//			throw new ValidateException("sai msisdns[]");
//		}
		if(allRequest.getMsisdns().length > maxMsisdn) {
			throw new ValidateException("sai msisdns[]");
		}
		for(String msisdn: allRequest.getMsisdns()) {
			if(!msisdn.matches("84[1-9][0-9]{8}")) {
				throw new ValidateException("sai msisdns[]");
			}
		}
				
		HashMap<String, Object> responseBody = new HashMap<>();
		
		ResponseObject responseObject;
		
		
		BaseInfoRequest baseInfoRequest = new BaseInfoRequest();
		baseInfoRequest.setMsisdns(allRequest.getMsisdns());
		baseInfoRequest.setSnapshot_date(allRequest.getEnd_date());
		responseObject = BaseInfoFlow.run(baseInfoRequest);
		batch += responseObject.getBatch();
		responseObject.setBatch(null);
		responseBody.put("base_info", responseObject.getResponse_body());
			
		
		ArpuRequest arpuRequest = new ArpuRequest(allRequest.getMsisdns(),allRequest.getStart_date(),allRequest.getEnd_date(),allRequest.getData_duration_month());
		responseObject = ArpuFlow.run(arpuRequest);
		batch += responseObject.getBatch();
		responseObject.setBatch(null);
		responseBody.put("arpu", responseObject.getResponse_body());
		
		
		CallHistRequest callHistRequest = new CallHistRequest(allRequest.getMsisdns(),allRequest.getStart_date(),allRequest.getEnd_date(),allRequest.getData_duration_month());
		responseObject = CallHistFlow.run(callHistRequest);
		batch += responseObject.getBatch();
		responseObject.setBatch(null);
		responseBody.put("call_hist", responseObject.getResponse_body());
		
		SmsHistRequest smsHistRequest = new SmsHistRequest(allRequest.getMsisdns(),allRequest.getStart_date(),allRequest.getEnd_date(),allRequest.getData_duration_month());
		responseObject = SmsHistFlow.run(smsHistRequest);
		batch += responseObject.getBatch();
		responseObject.setBatch(null);
		responseBody.put("sms_hist", responseObject.getResponse_body());
		
		TopupHistRequest topupHistRequest = new TopupHistRequest(allRequest.getMsisdns(),allRequest.getStart_date(),allRequest.getEnd_date(),allRequest.getData_duration_month());
		responseObject = TopupHistFlow.run(topupHistRequest);
		batch += responseObject.getBatch();
		responseObject.setBatch(null);
		responseBody.put("topup_hist", responseObject.getResponse_body());
		

		String responseID = RandomStringUtils.randomAlphanumeric(10);
		responseObject.setRequest_id(requestObject.getRequest_id());
		responseObject.setResponse_id(responseID);
		
		
		if(batch == 0) {
			responseObject.setResponse_status_code(Constants.Code.NO_DATA);
			responseObject.setResponse_status_dsc(Constants.Description.NO_DATA);
			responseObject.setResponse_body(null);
		}
		else {
			responseObject.setResponse_body(responseBody);
		}
		
		String cdr = PlatformImpl.getPlatform().getCdrFolder();
		
		Calendar cal = Calendar.getInstance();
		String year = cal.get(Calendar.YEAR) + "";
		String month = (cal.get(Calendar.MONTH) + 1) > 9 ? ("" + (cal.get(Calendar.MONTH) + 1)): ("0" + (cal.get(Calendar.MONTH) + 1));
		String day = (cal.get(Calendar.DAY_OF_MONTH)) > 9 ? ("" + (cal.get(Calendar.DAY_OF_MONTH))): ("0" + (cal.get(Calendar.DAY_OF_MONTH)));
		
		Path path = Paths.get(cdr, requestObject.getRequest_id(), year, month, "ALL-"+year+month+day+".cdr");
		
		List<String> list = new ArrayList<>();
		list.add(responseID);
		list.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
		list.add((System.currentTimeMillis() - now)/1000.0+"");
		list.add(allRequest.getMsisdns().length+"");
		list.add(allRequest.getStart_date());
		list.add(allRequest.getEnd_date());
		list.add(batch+"");
		
		if(!Files.exists(path.getParent())) {
			Files.createDirectories(path.getParent());
		}
		if(!Files.exists(path)) {
			Files.createFile(path);
		}
		Files.write(path, (String.join(",", list) + "\n").getBytes(), StandardOpenOption.APPEND);
		
		return responseObject;
	};
}
