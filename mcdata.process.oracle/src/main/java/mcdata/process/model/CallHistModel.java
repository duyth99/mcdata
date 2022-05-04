package mcdata.process.model;

public class CallHistModel {
	
	private String msisdn;
	private String service_provider;
	private String datetime;
	private int duration_in_seconds;
	private String called_party;
	private String call_direction;
	private String clp;
	private String cld;
	private String clv;
	private String cell_id;
	
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getService_provider() {
		return service_provider;
	}
	public void setService_provider(String service_provider) {
		this.service_provider = service_provider;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public int getDuration_in_seconds() {
		return duration_in_seconds;
	}
	public void setDuration_in_seconds(int duration_in_seconds) {
		this.duration_in_seconds = duration_in_seconds;
	}
	public String getCalled_party() {
		return called_party;
	}
	public void setCalled_party(String called_party) {
		this.called_party = called_party;
	}
	public String getCall_direction() {
		return call_direction;
	}
	public void setCall_direction(String call_direction) {
		this.call_direction = call_direction;
	}
	public String getClp() {
		return clp;
	}
	public void setClp(String clp) {
		this.clp = clp;
	}
	public String getCld() {
		return cld;
	}
	public void setCld(String cld) {
		this.cld = cld;
	}
	public String getClv() {
		return clv;
	}
	public void setClv(String clv) {
		this.clv = clv;
	}
	public String getCell_id() {
		return cell_id;
	}
	public void setCell_id(String cell_id) {
		this.cell_id = cell_id;
	}
}
