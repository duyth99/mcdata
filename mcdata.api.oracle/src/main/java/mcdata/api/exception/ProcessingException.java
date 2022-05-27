package mcdata.api.exception;

import mcdata.api.common.Constants;

@SuppressWarnings("serial")
public class ProcessingException extends ApiException {
	
	public ProcessingException() {
		super(Constants.Code.EPROCESSING, Constants.Error.EPROCESSING 
				+ "/" + Constants.Description.EPROCESSING);
	}
	
	public ProcessingException(String month, String year) {
		super(Constants.Code.EPROCESSING, Constants.Error.EPROCESSING
				+ "/" + "Du lieu thang "+month+"-"+year+" dang cap nhat");
	}
}
