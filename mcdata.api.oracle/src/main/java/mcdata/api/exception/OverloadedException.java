package mcdata.api.exception;

import mcdata.api.common.Constants;

@SuppressWarnings("serial")
public class OverloadedException extends ApiException {
	
	public OverloadedException() {
		super(Constants.Code.EOVERLOADED, Constants.Error.EOVERLOADED 
				+ "/" + Constants.Description.EOVERLOADED);
	}
	
	public OverloadedException(String description) {
		super(Constants.Code.EOVERLOADED, Constants.Error.EOVERLOADED
				+ "/" + (description.isEmpty() ? Constants.Description.EOVERLOADED : description));
	}
}
