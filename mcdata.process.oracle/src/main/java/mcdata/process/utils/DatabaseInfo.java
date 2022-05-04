/**
 * 
 */
package mcdata.process.utils;

public class DatabaseInfo {

	private String db_host;
	private int db_port;
	private String db_sid;
	private String db_username;
	private String db_password;
	private int db_number_of_connections = 1;

	public String getURL() {
//		String dbConnectString = "jdbc:oracle:thin:@(description=(address=(protocol=tcp)(port=" + db_port + ")(host="
//				+ db_host + "))(connect_data=(SID=" + db_sid + ")))";
//		String dbConnectString = "jdbc:oracle:thin:@115.146.123.7:1521:cit7";
		String dbConnectString = "jdbc:oracle:thin:@" + db_host + ":" + db_port + ":" + db_sid;
		return dbConnectString;
	}

	public DatabaseInfo(String db_host, int db_port, String db_sid, String db_username, String db_password) {
		super();
		this.db_host = db_host;
		this.db_port = db_port;
		this.db_sid = db_sid;
		this.db_username = db_username;
		this.db_password = db_password;
	}

	public DatabaseInfo() {
		super();
//		this.db_host = "115.146.123.7";
//		this.db_port = 1521;
//		this.db_sid = "cit7";
//		this.db_username = "TEST";
//		this.db_password = "TEST2019";
//		this.db_server = "DEDICATED";

		this.db_host = Props.getProp("db_host");
		this.db_port = Integer.parseInt(Props.getProp("db_port"));
		this.db_sid = Props.getProp("db_sid");
		this.db_username = Props.getProp("db_username");
		this.db_password = Props.getProp("db_password");
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
