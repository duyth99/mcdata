package mcdata.api.model;

public class ResponseObject {
	
	private String request_id;
	private String response_id;
	private Integer response_status_code;
	private String response_status_dsc;
	private Object response_body;
	private Integer batch;
	
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public String getResponse_id() {
		return response_id;
	}
	public void setResponse_id(String response_id) {
		this.response_id = response_id;
	}
	public Integer getResponse_status_code() {
		return response_status_code;
	}
	public void setResponse_status_code(Integer response_status_code) {
		this.response_status_code = response_status_code;
	}
	public String getResponse_status_dsc() {
		return response_status_dsc;
	}
	public void setResponse_status_dsc(String response_status_dsc) {
		this.response_status_dsc = response_status_dsc;
	}
	public Object getResponse_body() {
		return response_body;
	}
	public void setResponse_body(Object response_body) {
		this.response_body = response_body;
	}
	public Integer getBatch() {
		return batch;
	}
	public void setBatch(Integer batch) {
		this.batch = batch;
	}
	
	
}
