package mcdata.api.exception;

import mcdata.api.common.Constants;

@SuppressWarnings("serial")
public class NotStartException extends ApiException {
	
	public NotStartException() {
		super(Constants.Code.ENOT_START, Constants.Error.ENOT_START 
				+ "/" + Constants.Description.ENOT_START);
	}
	
	public NotStartException(String month, String year) {
		super(Constants.Code.ENOT_START, Constants.Error.ENOT_START
				+ "/" + "Thang "+month+"-"+year+" chua co du lieu");
	}
}
