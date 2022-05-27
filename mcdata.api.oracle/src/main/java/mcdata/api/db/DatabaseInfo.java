/**
 * 
 */
package mcdata.api.db;

import mcdata.api.common.PropertiesReader;

public class DatabaseInfo {

	private String db_host;
	private int db_port;
	private String db_sid;
	private String db_username;
	private String db_password;
	private String db_server;
	private int db_number_of_connections;
	private int db_reconnecting_interval_in_second;

	public String getURL() {
		String dbConnectString = "jdbc:oracle:thin:@(description=(address=(protocol=tcp)(port=" + db_port + ")(host="
				+ db_host + "))(connect_data=(SID=" + db_sid + ")(SERVER=" + db_server + ")))";
		return dbConnectString;
	}

	public DatabaseInfo(PropertiesReader prop) {
		super();
		this.db_host = prop.getValue("db_host");
		this.db_port = Integer.parseInt(prop.getValue("db_port"));
		this.db_sid = prop.getValue("db_sid");
		this.db_username = prop.getValue("db_username");
		this.db_password = prop.getValue("db_password");
		this.db_server = prop.getValue("db_server");
		this.db_number_of_connections = Integer.parseInt(prop.getValue("db_number_of_connections"));
		this.db_reconnecting_interval_in_second = Integer.parseInt(prop.getValue("db_reconnecting_interval_in_second"));
	}

	/**
	 * @return the db_reconnecting_interval_in_second
	 */
	public int getDb_reconnecting_interval_in_second() {
		return db_reconnecting_interval_in_second;
	}

	/**
	 * @param db_reconnecting_interval_in_second the
	 *                                           db_reconnecting_interval_in_second
	 *                                           to set
	 */
	public void setDb_reconnecting_interval_in_second(int db_reconnecting_interval_in_second) {
		this.db_reconnecting_interval_in_second = db_reconnecting_interval_in_second;
	}

	/**
	 * @return the db_host
	 */
	public String getDb_host() {
		return db_host;
	}

	/**
	 * @param db_host the db_host to set
	 */
	public void setDb_host(String db_host) {
		this.db_host = db_host;
	}

	/**
	 * @return the db_port
	 */
	public int getDb_port() {
		return db_port;
	}

	/**
	 * @param db_port the db_port to set
	 */
	public void setDb_port(int db_port) {
		this.db_port = db_port;
	}

	/**
	 * @return the db_sid
	 */
	public String getDb_sid() {
		return db_sid;
	}

	/**
	 * @param db_sid the db_sid to set
	 */
	public void setDb_sid(String db_sid) {
		this.db_sid = db_sid;
	}

	/**
	 * @return the db_username
	 */
	public String getDb_username() {
		return db_username;
	}

	/**
	 * @param db_username the db_username to set
	 */
	public void setDb_username(String db_username) {
		this.db_username = db_username;
	}

	/**
	 * @return the db_password
	 */
	public String getDb_password() {
		return db_password;
	}

	/**
	 * @param db_password the db_password to set
	 */
	public void setDb_password(String db_password) {
		this.db_password = db_password;
	}

	/**
	 * @return the db_server
	 */
	public String getDb_server() {
		return db_server;
	}

	/**
	 * @param db_server the db_server to set
	 */
	public void setDb_server(String db_server) {
		this.db_server = db_server;
	}

	/**
	 * @return the db_number_of_connections
	 */
	public int getDb_number_of_connections() {
		return db_number_of_connections;
	}

	/**
	 * @param db_number_of_connections the db_number_of_connections to set
	 */
	public void setDb_number_of_connections(int db_number_of_connections) {
		this.db_number_of_connections = db_number_of_connections;
	}

}
