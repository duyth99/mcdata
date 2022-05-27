package mcdata.api.exception;

import mcdata.api.common.Constants;

@SuppressWarnings("serial")
public class CertificateException extends ApiException {
	
	public CertificateException() {
		super(Constants.Code.ECERTIFICATE, Constants.Error.ECERTIFICATE 
				+ "/" + Constants.Description.ECERTIFICATE);
	}
	
	public CertificateException(String description) {
		super(Constants.Code.ECERTIFICATE, Constants.Error.ECERTIFICATE
				+ "/" + (description.isEmpty() ? Constants.Description.ECERTIFICATE : description));
	}
}
