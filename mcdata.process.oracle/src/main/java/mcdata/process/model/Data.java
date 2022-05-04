package mcdata.process.model;

public class Data {
	public String msisdn;
	public String date;
	public String record;
	
	public Data() {
		super();
	}
	public Data(String msisdn, String date, String record) {
		super();
		this.msisdn = msisdn;
		this.date = date;
		this.record = record;
	}
	
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRecord() {
		return record;
	}
	public void setRecord(String record) {
		this.record = record;
	}
}
