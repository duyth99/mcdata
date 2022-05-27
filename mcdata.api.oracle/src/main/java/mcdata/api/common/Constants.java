package mcdata.api.common;

public interface Constants {

	public interface Properties {
		public static final String APP_PROP_FILE = "cf/mcdata_api.properties";
		public static final String DATA_PROP_FILE = "cf/data.properties";
	}
	
	public final static String YYYYMMDD = "yyyy-MM-dd";
	public final static String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	
	public final static String SPLIT = ":";
	
	public final static String SP = "\\u0000";

//	public interface Error {
//		public final static String OK = "OK";
//		public final static String ERROR = "ERROR";
//		public final static int EOVERLOAD = 2;
//		public final static String EWRONG_USERNAME_OR_PASSWORD = "EWRONG_USERNAME_OR_PASSWORD";
//		public final static String ECAN_NOT_CREATED_ACCOUNT = "ECAN_NOT_CREATED_ACCOUNT";
//		public final static String EAUTHENTICATE_FAILURE = "EAUTHENTICATE_FAILURE";
//		public final static String EPERMISSION = "EPERMISSION";
//		public final static String EVALIDATION = "EVALIDATION";
//		public final static String EWRONG_PASSWORD = "EWRONG_PASSWORD";
//		public final static String ESESSION_DEACTIVETED = "ESESSION_DEACTIVETED";
//		public final static String NO_DATA = "NO_DATA";
//		public static final String EDATA = "EDATA";
//		public static final String EREGISTRATION = "EREGISTRATION";
//		public static final String EGETPROFILE = "EGETPROFILE";
//		public static final String EVERIFYPROFILE = "EVERIFYPROFILE";
//		public static final String ESCOPE = "ESCOPE";
//		public static final String ERPSOVERLOAD = "ERPSOVERLOAD";
//		public static final String ERPMOVERLOAD = "ERPMOVERLOAD";
//	}
	
	public final static String DATA_UPDATING = "Du lieu dang duoc cap nhat";
	
	public interface Error {
		public final static String ERROR = "ERROR";
		public final static String EVALIDATION = "EVALIDATION";
		public final static String ECERTIFICATE = "ECERTIFICATE";
		public final static String NO_DATA = "ENO_DATA";
		public final static String EOVERLOADED = "EOVERLOADED";
		public final static String ENOT_START = "ENOT_START";
		public final static String EPROCESSING = "EPROCESSING";
		
//		public final static String ELICENSE_LOCKED = "ELICENSE_LOCKED";
//		public final static String ELICENSE_EXPIRED = "ELICENSE_EXPIRED";
//		public final static String ELICENSE_FPDM_NODE = "ELICENSE_FPDM_NODE";
//		public final static String EAUTHENTICATION = "EAUTHENTICATION";
//		public final static String EPERMISSTION = "EPERMISSTION";
//		public final static String EMAXIMUM_PARTICIPANTS = "EMAXIMUM_PARTICIPANTS";
//		public final static String ELICENSEID = "ELICENSEID";
//		public final static String EGROUPEXISTED = "EGROUPEXISTED";
//		public final static String EGROUPNOTFOUND = "EGROUPNOTFOUND";
//		public final static String EPRODUCTEXISTED = "EPRODUCTEXISTED";
//		public final static String EPRODUCTNOTFOUND = "EPRODUCTNOTFOUND";
//		public final static String WRENEWLICENSE = "WRENEWLICENSE";
	}

	public interface Description {
		public final static String OK = "OK";
		public final static String ERROR = "General error";
		public final static String EVALIDATION = "One of parameter is not vaild";
		public final static String ECERTIFICATE = "Certificate error";
		public final static String NO_DATA = "Empty";
		public final static String EOVERLOADED = "The system is overloaded";
		public final static String ENOT_START = "Chua update du lieu trong khoang thoi gian input";
		public final static String EPROCESSING = "Du lieu dang cap nhat";
		
//		public final static String ELICENSE_LOCKED = "The license is locked";
//		public final static String ELICENSE_EXPIRED = "The license is expired";
//		public final static String ELICENSE_FPDM_NODE = "Wrong FPDM node";
//		public final static String EAUTHENTICATION = "Wrong group member or administrator key";
//		public final static String EPERMISSTION = "The operator is not allowed";
//		public final static String EMAXIMUM_PARTICIPANTS = "The number of paticipants has reached the limit";
//		public final static String ELICENSEID = "Not found the License_Id";
//		public final static String EGROUPEXISTED = "Group already exists in the system";
//		public final static String EGROUPNOTFOUND = "Group does not exist in the system";
//		public final static String EPRODUCTEXISTED = "Product already exists in the system";
//		public final static String EPRODUCTNOTFOUND = "Product does not found in the system";
//		public final static String WRENEWLICENSE = "License does not need to be renewed ";
	}

	public interface Code {
		public final static int OK = 0;
		public final static int ERROR = -1;
		public final static int EVALIDATION = -2;
		public final static int ECERTIFICATE = -3;
		public final static int NO_DATA = -4;
		public final static int EOVERLOADED = -5;
		public final static int ENOT_START = -6;
		public final static int EPROCESSING = -7;
		
//		public final static int ELICENSE_LOCKED = -4;
//		public final static int ELICENSE_EXPIRED = -5;
//		public final static int ELICENSE_FPDM_NODE = -6;
//		public final static int EAUTHENTICATION = -7;
//		public final static int EPERMISSTION = -8;
//		public final static int EMAXIMUM_PARTICIPANTS = -9;
//		public final static int ELICENSEID = -10;
//		public final static int EGROUPEXISTED = -11;
//		public final static int EGROUPNOTFOUND = -12;
//		public final static int EPRODUCTEXISTED = -13;
//		public final static int EPRODUCTNOTFOUND = -14;
//		public final static int WRENEWLICENSE = -15;
	}

}
