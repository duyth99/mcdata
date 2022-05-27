package mcdata.api.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import mcdata.api.server.impl.PlatformImpl;

public class Utils {

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

}
