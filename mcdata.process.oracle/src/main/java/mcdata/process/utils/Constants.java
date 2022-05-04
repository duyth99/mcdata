package mcdata.process.utils;

public interface Constants {
	public final static String FORMAT_DATE = "yyyyMMdd";
	public final static String FORMAT_DATETIME = "yyyyMMddHHmmss";
	
	public interface Error {
		public final static String ERROR = "ERROR";
		public final static String EVALIDATION = "EVALIDATION";
		public final static String ELICENSE_KEY = "ELICENSE_KEY";
		public final static String ELICENSE_LOCKED = "ELICENSE_LOCKED";
		public final static String ELICENSE_EXPIRED = "ELICENSE_EXPIRED";
		public final static String ELICENSE_FPDM_NODE = "ELICENSE_FPDM_NODE";
		public final static String EAUTHENTICATION = "EAUTHENTICATION";
		public final static String EPERMISSTION = "EPERMISSTION";
		public final static String EMAXIMUM_PARTICIPANTS = "EMAXIMUM_PARTICIPANTS";
		public final static String ELICENSEID = "ELICENSEID";
		public final static String EGROUPEXISTED = "EGROUPEXISTED";
		public final static String EGROUPNOTFOUND = "EGROUPNOTFOUND";
		public final static String EPRODUCTEXISTED = "EPRODUCTEXISTED";
		public final static String EPRODUCTNOTFOUND = "EPRODUCTNOTFOUND";
		public final static String WRENEWLICENSE = "WRENEWLICENSE";
	}

	public interface Description {
		public final static String ERROR = "General error";
		public final static String EVALIDATION = "One of parameter is not valid";
		public final static String ELICENSE_KEY = "Wrong license key";
		public final static String ELICENSE_LOCKED = "The license is locked";
		public final static String ELICENSE_EXPIRED = "The license is expired";
		public final static String ELICENSE_FPDM_NODE = "Wrong FPDM node";
		public final static String EAUTHENTICATION = "Wrong group member or administrator key";
		public final static String EPERMISSTION = "The operator is not allowed";
		public final static String EMAXIMUM_PARTICIPANTS = "The number of paticipants has reached the limit";
		public final static String ELICENSEID = "Not found the License_Id";
		public final static String EGROUPEXISTED = "Group already exists in the system";
		public final static String EGROUPNOTFOUND = "Group does not exist in the system";
		public final static String EPRODUCTEXISTED = "Product already exists in the system";
		public final static String EPRODUCTNOTFOUND = "Product does not found in the system";
		public final static String WRENEWLICENSE = "License does not need to be renewed ";
	}

	public interface Code {
		public final static int ERROR = -1;
		public final static int EVALIDATION = -2;
		public final static int ELICENSE_KEY = -3;
		public final static int ELICENSE_LOCKED = -4;
		public final static int ELICENSE_EXPIRED = -5;
		public final static int ELICENSE_FPDM_NODE = -6;
		public final static int EAUTHENTICATION = -7;
		public final static int EPERMISSTION = -8;
		public final static int EMAXIMUM_PARTICIPANTS = -9;
		public final static int ELICENSEID = -10;
		public final static int EGROUPEXISTED = -11;
		public final static int EGROUPNOTFOUND = -12;
		public final static int EPRODUCTEXISTED = -13;
		public final static int EPRODUCTNOTFOUND = -14;
		public final static int WRENEWLICENSE = -15;
	}

	public final static String SP = "\\u0000";
	public static final String LICENSE_PRIVATE_KEY = "/FinGo/FinGo License System/License_Private_Key.txt";
//	public static final String LICENSE_PRIVATE_KEY = "E:\\License_Private_Key.txt";
//	public static final String LICENSE_PRIVATE_KEY = "/Users/tronghoang/project/finGlobal/FinGo/deploy/process_Test/License_Private_Key.txt";
}
