package mcdata.api.model;

public class ArpuRequest{
	
	private String[] msisdns;
	private String start_date;
	private String end_date;
	private int data_duration_month;
	
	public ArpuRequest() {
		super();
	}
	public ArpuRequest(String[] msisdns, String start_date, String end_date, int data_duration_month) {
		super();
		this.msisdns = msisdns;
		this.start_date = start_date;
		this.end_date = end_date;
		this.data_duration_month = data_duration_month;
	}
	public String[] getMsisdns() {
		return msisdns;
	}
	public void setMsisdns(String[] msisdns) {
		this.msisdns = msisdns;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public int getData_duration_month() {
		return data_duration_month;
	}
	public void setData_duration_month(int data_duration_month) {
		this.data_duration_month = data_duration_month;
	}
}
