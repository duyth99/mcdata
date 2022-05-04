/**
 * 
 */
package mcdata.process.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import oracle.jdbc.OracleConnection;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

public class DBUtils {
	private PoolDataSource dataSource = PoolDataSourceFactory.getPoolDataSource();
	private DatabaseInfo dbInfo;

	public DBUtils(DatabaseInfo db_info) throws SQLException {
		super();
		this.dbInfo = db_info;
		Properties connProps = new Properties();
		connProps.put(OracleConnection.CONNECTION_PROPERTY_AUTOCOMMIT, Boolean.FALSE.toString());
		dataSource.setConnectionProperties(connProps);
		dataSource.setConnectionFactoryClassName("oracle.jdbc.OracleDriver");
		dataSource.setURL(dbInfo.getURL());
		dataSource.setUser(dbInfo.getDb_username());
		dataSource.setPassword(dbInfo.getDb_password());
		dataSource.setConnectionPoolName("FLS");
		if (dbInfo.getDb_number_of_connections() != -1) {
			dataSource.setInitialPoolSize(dbInfo.getDb_number_of_connections());
			dataSource.setMaxPoolSize(dbInfo.getDb_number_of_connections());
			dataSource.setMinPoolSize(dbInfo.getDb_number_of_connections());
		}
//		if (dbInfo.getDb_reconnecting_interval_in_second() != -1) {
//			dataSource.setTimeoutCheckInterval((int) dbInfo.getDb_reconnecting_interval_in_second());
//			dataSource.setPropertyCycle((int) dbInfo.getDb_reconnecting_interval_in_second());
//		}
		dataSource.setConnectionWaitTimeout(Integer.MAX_VALUE);
		dataSource.setAbandonedConnectionTimeout(0);
		dataSource.setInactiveConnectionTimeout(0);
		dataSource.setValidateConnectionOnBorrow(false);
		dataSource.setTimeToLiveConnectionTimeout(0);

		dataSource.setMaxIdleTime(0);
		dataSource.setMaxConnectionReuseCount(0);
		dataSource.setMaxConnectionReuseTime(0);
		dataSource.setFastConnectionFailoverEnabled(false);
		dataSource.setMaxStatements(0);
		dataSource.setConnectionHarvestTriggerCount(Integer.MAX_VALUE);
	}

	public void releaseConnection(Connection conn) throws SQLException {
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}

	public Connection getConnection() throws SQLException {
		Connection conn = dataSource.getConnection();
		conn.setAutoCommit(false);
		return conn;
	}

	public PoolDataSource getDataSource() {
		return dataSource;
	}

}
