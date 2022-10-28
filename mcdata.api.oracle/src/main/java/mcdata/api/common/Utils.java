package mcdata.api.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import mcdata.api.exception.CertificateException;
import mcdata.api.exception.OverloadedException;
import mcdata.api.model.AllRequest;
import mcdata.api.model.ArpuRequest;
import mcdata.api.model.BaseInfoRequest;
import mcdata.api.model.CallHistRequest;
import mcdata.api.model.RequestObject;
import mcdata.api.model.ResponseObject;
import mcdata.api.model.SmsHistRequest;
import mcdata.api.model.TopupHistRequest;
import mcdata.api.server.impl.PlatformImpl;

public class Utils {
	private static Logger logger = LoggerFactory.getLogger("api_logger");
	public static String getProperties(String key) {
		return PlatformImpl.getPlatform().getPropertiesReader().getValue(key);
	}

	public static ServerProperties getServerProperties() {
		return PlatformImpl.getPlatform().getServerProp();
	}

	public static String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:MM:ss");
		String timeNow = format.format(Calendar.getInstance().getTime());

		return timeNow;
	}

	public static boolean validateName(String name) {
		return Pattern.matches("^[A-Za-z0-9_]+$", name);
	}

	public static boolean validateAddress(String address) {
		return Pattern.matches("^[A-Za-z0-9ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠ-ỹ\\s,-./_]+$", address);
	}

	public static boolean validatePhoneNumber(String number) {
		return Pattern.matches("[0-9]++", number);
	}

	// validate id_no
	public static boolean validateIDNo(String idNo) {
		return Pattern.matches("[A-Z0-9a-z\\s{0,1}]++", idNo);
	}

//	public static boolean isThisDateValid(String dateToValidate, String dateFromat) {
//
//		if (dateToValidate == null) {
//			return false;
//		}
//
//		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
//		sdf.setLenient(false);
//
//		try {
//
//			sdf.parse(dateToValidate);
//
//		} catch (ParseException e) {
//
//			e.printStackTrace();
//			return false;
//		}
//
//		return true;
//	}

	public static String processPhoneNumber(String phoneNumber) {
		String phone = phoneNumber;
		if (phone.startsWith("84") && phone.length() > 10) {
			phone = "0" + phone.substring(2);
		} else if (phone.startsWith("+840")) {
			phone = "0" + phone.substring(4);
		} else if (phone.startsWith("+84")) {
			phone = "0" + phone.substring(3);
		} else if (phone.startsWith("0084")) {
			phone = "0" + phone.substring(4);
		} else if (phone.startsWith("(+84)")) {
			phone = "0" + phone.substring(5);
		}

		return phone;
	}

	public static String removeAccent(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");
	}
	
	public static void writeToFile(String fileName, List<String> list) throws IOException {
		Files.write(Paths.get(fileName), String.join(",", list).getBytes());
	}
	
	public static boolean isThisDateValid(String dateToValidate, String dateFromat) {

		if (dateToValidate == null) {
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);

		try {

			sdf.parse(dateToValidate);

		} catch (ParseException e) {

			// e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public static void tryAcquireTrain(String request_id) throws CertificateException, OverloadedException, InterruptedException {
		if (!PlatformImpl.getPlatform().tryAcquireTrain(request_id)) {
			PlatformImpl.getPlatform().releaseSemaphoreTrain(request_id);
			
			for (int i = 0; i < Utils.getServerProperties().getApi_number_of_retries(); i++) {
				if (!PlatformImpl.getPlatform().tryAcquireTrain(request_id)) {
					PlatformImpl.getPlatform().releaseSemaphoreTrain(request_id);
					
					TimeUnit.MILLISECONDS.sleep(Integer.valueOf(Utils.getServerProperties().getApi_retry_delay_in_milliseconds()));
					if(i == (Utils.getServerProperties().getApi_number_of_retries() - 1)) {
						PlatformImpl.getPlatform().tryAcquireTrain(request_id);
						throw new OverloadedException();
					}
				}else {
					break;
				}
			}
		}
	}
	
//	public static void tryAcquireFetch(String request_id) throws CertificateException, OverloadedException, InterruptedException {
//		if (!PlatformImpl.getPlatform().tryAcquireFetch(request_id)) {
//			PlatformImpl.getPlatform().releaseSemaphoreFetch(request_id);
//			
//			for (int i = 0; i < Utils.getServerProperties().getApi_number_of_retries(); i++) {
//				if (!PlatformImpl.getPlatform().tryAcquireFetch(request_id)) {
//					PlatformImpl.getPlatform().releaseSemaphoreFetch(request_id);
//					
//					TimeUnit.MILLISECONDS.sleep(Integer.valueOf(Utils.getServerProperties().getApi_retry_delay_in_milliseconds()));
//					if(i == (Utils.getServerProperties().getApi_number_of_retries() - 1)) {
//						PlatformImpl.getPlatform().tryAcquireFetch(request_id);
//						throw new OverloadedException();
//					}
//				}else {
//					break;
//				}
//			}
//		}
//	}
	
	public static void exportCDR(String type, long start, RequestObject requestObject, ResponseObject responseObject, int batch, int status_code) {
		try {
			String user_id;
			if(requestObject.getUser_id()==null||requestObject.getUser_id().trim().isEmpty()) {
				user_id = "null";
			}else {
				user_id = requestObject.getUser_id();
			}
			Gson gson = new Gson();
			String cdr = PlatformImpl.getPlatform().getCdrFolder();
			Calendar cal = Calendar.getInstance();
			String year = cal.get(Calendar.YEAR) + "";
			String month = (cal.get(Calendar.MONTH) + 1) > 9 ? ("" + (cal.get(Calendar.MONTH) + 1)): ("0" + (cal.get(Calendar.MONTH) + 1));
			String day = (cal.get(Calendar.DAY_OF_MONTH)) > 9 ? ("" + (cal.get(Calendar.DAY_OF_MONTH))): ("0" + (cal.get(Calendar.DAY_OF_MONTH)));
			Path path = Paths.get(cdr, user_id, year, month, type+"-"+year+month+day+".cdr");
			
			List<String> list = new ArrayList<>();
			list.add(status_code+"");
			list.add(responseObject.getRequest_id());
			list.add(responseObject.getResponse_id());
			list.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
			list.add((System.currentTimeMillis() - start)/1000.0+"");
			
			Object request_body = requestObject.getRequest_body();
			
			if(type.equals("BPI")) {
				BaseInfoRequest bodyObj = gson.fromJson(gson.toJson(request_body), BaseInfoRequest.class);
				list.add(bodyObj.getMsisdns().length+"");
				list.add("");
				list.add(bodyObj.getSnapshot_date());
			}else if(type.equals("ARPU")) {
				ArpuRequest bodyObj = gson.fromJson(gson.toJson(request_body), ArpuRequest.class);
				list.add(bodyObj.getMsisdns().length+"");
				list.add(bodyObj.getStart_date());
				list.add(bodyObj.getEnd_date());
			}else if(type.equals("CH")) {
				CallHistRequest bodyObj = gson.fromJson(gson.toJson(request_body), CallHistRequest.class);
				list.add(bodyObj.getMsisdns().length+"");
				list.add(bodyObj.getStart_date());
				list.add(bodyObj.getEnd_date());
			}else if(type.equals("SH")) {
				SmsHistRequest bodyObj = gson.fromJson(gson.toJson(request_body), SmsHistRequest.class);
				list.add(bodyObj.getMsisdns().length+"");
				list.add(bodyObj.getStart_date());
				list.add(bodyObj.getEnd_date());
			}else if(type.equals("TH")) {
				TopupHistRequest bodyObj = gson.fromJson(gson.toJson(request_body), TopupHistRequest.class);
				list.add(bodyObj.getMsisdns().length+"");
				list.add(bodyObj.getStart_date());
				list.add(bodyObj.getEnd_date());
			}else {
				AllRequest bodyObj = gson.fromJson(gson.toJson(request_body), AllRequest.class);
				list.add(bodyObj.getMsisdns().length+"");
				list.add(bodyObj.getStart_date());
				list.add(bodyObj.getEnd_date());
			}
			
			
			
			list.add(batch+"");
			
			
			if(!Files.exists(path.getParent())) {
				Files.createDirectories(path.getParent());
			}
			if(!Files.exists(path)) {
				Files.createFile(path);
			}
			Files.write(path, (String.join(",", list) + "\n").getBytes(), StandardOpenOption.APPEND);
			
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

//	public static String[] filterByTelco(String[] msisdns) throws SQLException {
//		List<String> result = new ArrayList<>();
//		List<String> prefix;
//		String telco = PlatformImpl.getPlatform().getServerProp().getTelco();
//		if(telco.equals("vnp")) {
//			prefix = Arrays.asList(PlatformImpl.getPlatform().getServerProp().getVnp_prefix().split(","));
//		}else {
//			prefix = Arrays.asList(PlatformImpl.getPlatform().getServerProp().getVtt_prefix().split(","));
//		}
//		
//		HashMap<String, String> mnp = DatabaseEx.selectMNP(msisdns);
//		
//		for (String sdt : msisdns) {
//			if(mnp.get(sdt) != null && mnp.get(sdt).equals(telco)) {
//				result.add(sdt);
//			}else if(mnp.get(sdt) == null && prefix.contains(sdt.substring(0,4))) {
//				result.add(sdt);
//			}
//		}
//		
//		return result.stream().toArray(String[]::new);
//	}
	
}
