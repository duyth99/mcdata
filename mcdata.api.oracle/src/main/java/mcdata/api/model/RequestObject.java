package mcdata.api.model;

public class RequestObject {
	
	private String user_id;
	private String token;
	private String request_id;
	private String request_type;
	private Object request_body;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public String getRequest_type() {
		return request_type;
	}
	public void setRequest_type(String request_type) {
		this.request_type = request_type;
	}
	public Object getRequest_body() {
		return request_body;
	}
	public void setRequest_body(Object request_body) {
		this.request_body = request_body;
	}
}
