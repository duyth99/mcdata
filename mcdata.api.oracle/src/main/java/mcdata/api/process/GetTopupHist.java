package mcdata.api.process;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import mcdata.api.common.Utils;
import mcdata.api.exception.ValidateException;
import mcdata.api.model.RequestObject;
import mcdata.api.model.ResponseObject;
import mcdata.api.model.TopupHistRequest;
import mcdata.api.process.flow.TopupHistFlow;
import mcdata.api.provider.ApiProcessingInterface;

public class GetTopupHist {
	private static Logger logger = LoggerFactory.getLogger("api_logger");
	public static final ApiProcessingInterface<ResponseObject> PROCESS = (RequestObject requestObject, ResponseObject responseObject, int maxMsisdn) -> {


		try {
//			long now = System.currentTimeMillis();
//	
//			String responseID = RandomStringUtils.randomAlphanumeric(10);
			
			
			String request_type = requestObject.getRequest_type();
			if(!request_type.equals("topup_hist")) {
				throw new ValidateException();
			}
			Object request_body = requestObject.getRequest_body();
			Gson gson = new Gson();
			TopupHistRequest bodyObj = gson.fromJson(gson.toJson(request_body), TopupHistRequest.class);
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
//			if(!PlatformImpl.getPlatform().getServerProp().getTelco().equals("all")) {
//				bodyObj.setMsisdns(Utils.filterByTelco(bodyObj.getMsisdns()));
//			}
			
			responseObject = TopupHistFlow.run(bodyObj, responseObject);
	
//			responseObject.setRequest_id(requestObject.getRequest_id());
//			responseObject.setResponse_id(responseID);
//			
//			if(responseObject.getBatch() == 0) {
//				responseObject.setResponse_status_code(Constants.Code.NO_DATA);
//				responseObject.setResponse_status_dsc(Constants.Description.NO_DATA);
//				responseObject.setResponse_body(null);
//			}
//			
//	
//			String cdr = PlatformImpl.getPlatform().getCdrFolder();
//			
//			Calendar cal = Calendar.getInstance();
//			String year = cal.get(Calendar.YEAR) + "";
//			String month = (cal.get(Calendar.MONTH) + 1) > 9 ? ("" + (cal.get(Calendar.MONTH) + 1)): ("0" + (cal.get(Calendar.MONTH) + 1));
//			String day = (cal.get(Calendar.DAY_OF_MONTH)) > 9 ? ("" + (cal.get(Calendar.DAY_OF_MONTH))): ("0" + (cal.get(Calendar.DAY_OF_MONTH)));
//			
//			Path path = Paths.get(cdr, requestObject.getUser_id(), year, month, "TH-"+year+month+day+".cdr");
//			
//			List<String> list = new ArrayList<>();
//			list.add(responseObject.getRequest_id());
//			list.add(responseObject.getResponse_id());
//			list.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
//			list.add((System.currentTimeMillis() - now)/1000.0+"");
//			list.add(bodyObj.getMsisdns().length+"");
//			list.add(bodyObj.getStart_date());
//			list.add(bodyObj.getEnd_date());
//			list.add(responseObject.getBatch()+"");
//	
//			if(!Files.exists(path.getParent())) {
//				Files.createDirectories(path.getParent());
//			}
//			if(!Files.exists(path)) {
//				Files.createFile(path);
//			}
//			Files.write(path, (String.join(",", list) + "\n").getBytes(), StandardOpenOption.APPEND);
//			
//			responseObject.setBatch(null);
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return responseObject;
	};
}
