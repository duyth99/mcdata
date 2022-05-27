package mcdata.api.exception;

@SuppressWarnings("serial")
public class ApiException extends Exception{
	private int code;
	private String dsc;
	
	public int getCode() {
		return code;
	}

	public String getDsc() {
		return dsc;
	}

	public ApiException(final int code, final String dsc) {
    	super(dsc);
        this.code = code;
        this.dsc = dsc;
    }
    
}
