package mcdata.api.model;

public class BaseInfoRequest{
	
	private String[] msisdns;
	private String snapshot_date;
	
	public String[] getMsisdns() {
		return msisdns;
	}
	public void setMsisdns(String[] msisdns) {
		this.msisdns = msisdns;
	}
	public String getSnapshot_date() {
		return snapshot_date;
	}
	public void setSnapshot_date(String snapshot_date) {
		this.snapshot_date = snapshot_date;
	}
}
