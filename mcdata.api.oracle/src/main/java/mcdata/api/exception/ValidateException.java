package mcdata.api.exception;

import mcdata.api.common.Constants;

@SuppressWarnings("serial")
public class ValidateException extends ApiException {
	
	public ValidateException() {
		super(Constants.Code.EVALIDATION, Constants.Error.EVALIDATION 
				+ "/" + Constants.Description.EVALIDATION);
	}
	
	public ValidateException(String description) {
		super(Constants.Code.EVALIDATION, Constants.Error.EVALIDATION
				+ "/" + (description.isEmpty() ? Constants.Description.EVALIDATION : description));
	}
}
