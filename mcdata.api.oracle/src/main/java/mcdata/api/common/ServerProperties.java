/**
 * 
 */
package mcdata.api.common;

public class ServerProperties {
	private int listening_port;
	private int maximum_number_of_concurrent_requests;
	private int api_executing_delay_in_milliseconds;

	private String linkCertificate;
	private String fileLock;
	private int max_msisdns_train;
	private int max_msisdns_fetch;
	private int max_thread;
	private int max_request_per_client_train;
	private int max_request_per_client_fetch;
	
	

	public ServerProperties() {
		// app properties
		setListening_port(Integer.parseInt(Utils.getProperties("listening_port")));
		setLinkCertificate(Utils.getProperties("link_certificate"));
		setApi_executing_delay_in_milliseconds(
				Integer.valueOf(Utils.getProperties("api_executing_delay_in_milliseconds")));
		setMaximum_number_of_concurrent_requests(
				Integer.valueOf(Utils.getProperties("maximum_number_of_concurrent_requests")));
		setFileLock(Utils.getProperties("file_lock"));
		setMax_msisdns_train(Integer.valueOf(Utils.getProperties("max_msisdns_train")));
		setMax_msisdns_fetch(Integer.valueOf(Utils.getProperties("max_msisdns_fetch")));
		setMax_thread(Integer.valueOf(Utils.getProperties("max_thread")));
		setMax_request_per_client_train(Integer.valueOf(Utils.getProperties("max_request_per_client_train")));
		setMax_request_per_client_fetch(Integer.valueOf(Utils.getProperties("max_request_per_client_fetch")));
	}


	/**
	 * @return the listening_port
	 */
	public int getListening_port() {
		return listening_port;
	}

	/**
	 * @return the maximum_number_of_concurrent_requests
	 */
	public int getMaximum_number_of_concurrent_requests() {
		return maximum_number_of_concurrent_requests;
	}

	/**
	 * @param listening_port the listening_port to set
	 */
	public void setListening_port(int listening_port) {
		this.listening_port = listening_port;
	}

	/**
	 * @param maximum_number_of_concurrent_requests the
	 *                                              maximum_number_of_concurrent_requests
	 *                                              to set
	 */
	public void setMaximum_number_of_concurrent_requests(int maximum_number_of_concurrent_requests) {
		this.maximum_number_of_concurrent_requests = maximum_number_of_concurrent_requests;
	}

	/**
	 * @param api_executing_delay_in_milliseconds the
	 *                                            api_executing_delay_in_milliseconds
	 *                                            to set
	 */
	public void setApi_executing_delay_in_milliseconds(int api_executing_delay_in_milliseconds) {
		this.api_executing_delay_in_milliseconds = api_executing_delay_in_milliseconds;
	}

	/**
	 * @return the fileLock
	 */
	public String getFileLock() {
		return fileLock;
	}

	/**
	 * @param fileLock the fileLock to set
	 */
	public void setFileLock(String fileLock) {
		this.fileLock = fileLock;
	}

	/**
	 * @return the linkCertificate
	 */
	public String getLinkCertificate() {
		return linkCertificate;
	}

	/**
	 * @param linkCertificate the linkCertificate to set
	 */
	public void setLinkCertificate(String linkCertificate) {
		this.linkCertificate = linkCertificate;
	}

	/**
	 * @return the api_executing_delay_in_milliseconds
	 */
	public int getApi_executing_delay_in_milliseconds() {
		return api_executing_delay_in_milliseconds;
	}


	public int getMax_thread() {
		return max_thread;
	}


	public void setMax_thread(int max_thread) {
		this.max_thread = max_thread;
	}


	public int getMax_msisdns_train() {
		return max_msisdns_train;
	}


	public void setMax_msisdns_train(int max_msisdns_train) {
		this.max_msisdns_train = max_msisdns_train;
	}


	public int getMax_msisdns_fetch() {
		return max_msisdns_fetch;
	}


	public void setMax_msisdns_fetch(int max_msisdns_fetch) {
		this.max_msisdns_fetch = max_msisdns_fetch;
	}


	public int getMax_request_per_client_train() {
		return max_request_per_client_train;
	}


	public void setMax_request_per_client_train(int max_request_per_client_train) {
		this.max_request_per_client_train = max_request_per_client_train;
	}


	public int getMax_request_per_client_fetch() {
		return max_request_per_client_fetch;
	}


	public void setMax_request_per_client_fetch(int max_request_per_client_fetch) {
		this.max_request_per_client_fetch = max_request_per_client_fetch;
	}


	


	


}
