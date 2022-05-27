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
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.gson.Gson;

import mcdata.api.common.Constants;
import mcdata.api.common.Utils;
import mcdata.api.exception.ValidateException;
import mcdata.api.model.RequestObject;
import mcdata.api.model.ResponseObject;
import mcdata.api.model.SmsHistRequest;
import mcdata.api.process.flow.SmsHistFlow;
import mcdata.api.provider.ApiProcessingInterface;
import mcdata.api.server.impl.PlatformImpl;

public class GetSmsHist {
	public static final ApiProcessingInterface<ResponseObject> PROCESS = (RequestObject requestObject, int maxMsisdn) -> {

		long now = System.currentTimeMillis();

		String responseID = RandomStringUtils.randomAlphanumeric(10);
		
		String request_type = requestObject.getRequest_type();
		if(!request_type.equals("sms_hist")) {
			throw new ValidateException();
		}
		Object request_body = requestObject.getRequest_body();
		Gson gson = new Gson();
		SmsHistRequest bodyObj = gson.fromJson(gson.toJson(request_body), SmsHistRequest.class);
		bodyObj.setMsisdns(Arrays.stream(bodyObj.getMsisdns()).distinct().toArray(String[]::new));
		
		if(!Utils.isThisDateValid(bodyObj.getStart_date(),"yyyyMMdd") || !Utils.isThisDateValid(bodyObj.getEnd_date(),"yyyyMMdd")) {
			throw new ValidateException("sai yyyyMMdd");
		}
//		if(bodyObj.getMsisdns().length > PlatformImpl.getPlatform().getServerProp().getMax_msisdns()) {
//			throw new ValidateException("sai msisdns[]");
//		}
		if(bodyObj.getMsisdns().length > maxMsisdn) {
			throw new ValidateException("sai msisdns[]");
		}
		for(String msisdn: bodyObj.getMsisdns()) {
			if(!msisdn.matches("84[1-9][0-9]{8}")) {
				throw new ValidateException("sai msisdns[]");
			}
		}
		
		

		ResponseObject responseObject = SmsHistFlow.run(bodyObj);

		responseObject.setRequest_id(requestObject.getRequest_id());
		responseObject.setResponse_id(responseID);
		
		if(responseObject.getBatch() == 0) {
			responseObject.setResponse_status_code(Constants.Code.NO_DATA);
			responseObject.setResponse_status_dsc(Constants.Description.NO_DATA);
			responseObject.setResponse_body(null);
		}
		

		String cdr = PlatformImpl.getPlatform().getCdrFolder();
		
		Calendar cal = Calendar.getInstance();
		String year = cal.get(Calendar.YEAR) + "";
		String month = (cal.get(Calendar.MONTH) + 1) > 9 ? ("" + (cal.get(Calendar.MONTH) + 1)): ("0" + (cal.get(Calendar.MONTH) + 1));
		String day = (cal.get(Calendar.DAY_OF_MONTH)) > 9 ? ("" + (cal.get(Calendar.DAY_OF_MONTH))): ("0" + (cal.get(Calendar.DAY_OF_MONTH)));
		
		Path path = Paths.get(cdr, requestObject.getRequest_id(), year, month, "SH-"+year+month+day+".cdr");
		
		List<String> list = new ArrayList<>();
		list.add(responseObject.getResponse_id());
		list.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
		list.add((System.currentTimeMillis() - now)/1000.0+"");
		list.add(bodyObj.getMsisdns().length+"");
		list.add(bodyObj.getStart_date());
		list.add(bodyObj.getEnd_date());
		list.add(responseObject.getBatch()+"");

		if(!Files.exists(path.getParent())) {
			Files.createDirectories(path.getParent());
		}
		if(!Files.exists(path)) {
			Files.createFile(path);
		}
		Files.write(path, (String.join(",", list) + "\n").getBytes(), StandardOpenOption.APPEND);
		
		responseObject.setBatch(null);
		return responseObject;
	};
}
