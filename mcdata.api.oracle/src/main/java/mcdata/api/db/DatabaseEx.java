package mcdata.api.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import mcdata.api.server.impl.PlatformImpl;

public class DatabaseEx {
	
	public static List<String> ping() throws SQLException{
		Connection connection = PlatformImpl.getPlatform().getDbUtils().getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> result = new ArrayList<>();
		try {
			
			String sql = "select record from bpi where ROWNUM <= 10";
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			rs.next();
			while (rs.next()) {
				result.add(rs.getString(1));
			}
			
			return result;
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		} finally {
			connection.commit();
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			PlatformImpl.getPlatform().getDbUtils().releaseConnection(connection);
		}
	}
	
	public static List<String> selectBPI(String[] msisdns, String sqlCloseDateBefore) throws SQLException{
		Connection connection = PlatformImpl.getPlatform().getDbUtils().getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> result = new ArrayList<>();
		try {
			String inCondition = "";
			for(int i=0;i<msisdns.length;i+=1000) {
				inCondition+="OR t.MSISDN IN(";
				if(i+1000>msisdns.length) {
					inCondition += Arrays.asList(Arrays.copyOfRange(msisdns, i, msisdns.length)).stream().collect(Collectors.joining("','", "'", "'"));
					inCondition+=" )";
					break;
				}
				inCondition += Arrays.asList(Arrays.copyOfRange(msisdns, i, i+1000)).stream().collect(Collectors.joining("','", "'", "'"));
				inCondition+=" )";
			}
			
			String sql = "select t.RECORD from BPI t"
						+ " where t.snap_date <= date '"+sqlCloseDateBefore+"'"
						+ " and not exists ("
						+ " select 1"
						+ " from BPI t1"
						+ " where t1.msisdn = t.msisdn and t1.snap_date <= date '"+sqlCloseDateBefore+"' and t1.snap_date > t.snap_date"
						+ " ) and (0=1 "+inCondition+")";
			
//			System.out.println(sql);
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				result.add(rs.getString(1));
			}

			rs.close();
			ps.close();
			
			return result;
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		} finally {
			connection.commit();
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			PlatformImpl.getPlatform().getDbUtils().releaseConnection(connection);
		}
	}
	
	public static List<String> selectARPU(String[] msisdns, String sqlStartDate, String sqlEndDate) throws SQLException{
		Connection connection = PlatformImpl.getPlatform().getDbUtils().getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> result = new ArrayList<>();
		try {
			
			String inCondition = "";
			for(int i=0;i<msisdns.length;i+=1000) {
				inCondition+=" OR MSISDN IN(";
				if(i+1000>msisdns.length) {
					inCondition += Arrays.asList(Arrays.copyOfRange(msisdns, i, msisdns.length)).stream().collect(Collectors.joining("','", "'", "'"));
					inCondition+=" )";
					break;
				}
				inCondition += Arrays.asList(Arrays.copyOfRange(msisdns, i, i+1000)).stream().collect(Collectors.joining("','", "'", "'"));
				inCondition+=" )";
			}
			String sql = "SELECT RECORD FROM ARPU WHERE (0=1"+inCondition+") AND SNAP_DATE>=? AND SNAP_DATE<=?";
			
			
//			String sql = "SELECT * FROM ARPU_09_2021 WHERE MSISDN IN("+Arrays.asList(msisdns).stream().collect(Collectors.joining("','", "'", "'"))+")";
			ps = connection.prepareStatement(sql);
			ps.setDate(1, java.sql.Date.valueOf(sqlStartDate));
			ps.setDate(2, java.sql.Date.valueOf(sqlEndDate));
			rs = ps.executeQuery();
			while (rs.next()) {
				result.add(rs.getString(1));
			}

			rs.close();
			ps.close();
			
			return result;
		} catch (SQLException e) {
			connection.rollback();
//			if(!e.getMessage().split("\n")[0].contains("ORA-00942")) {
//				throw e;
//			}
			throw e;
		} finally {
			connection.commit();
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			PlatformImpl.getPlatform().getDbUtils().releaseConnection(connection);
		}
//		return result;
	}
	
	
	public static List<String> selectCH(String[] msisdns, String sqlStartDate, String sqlEndDate, String zz) throws SQLException{
		Connection connection = PlatformImpl.getPlatform().getDbUtils().getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> result = new ArrayList<>();
		try {
			
			String inCondition = "";
			for(int i=0;i<msisdns.length;i+=1000) {
				inCondition+=" OR MSISDN IN(";
				if(i+1000>msisdns.length) {
					inCondition += Arrays.asList(Arrays.copyOfRange(msisdns, i, msisdns.length)).stream().collect(Collectors.joining("','", "'", "'"));
					inCondition+=" )";
					break;
				}
				inCondition += Arrays.asList(Arrays.copyOfRange(msisdns, i, i+1000)).stream().collect(Collectors.joining("','", "'", "'"));
				inCondition+=" )";
			}
			String sql = "SELECT RECORD FROM CH_"+zz+" WHERE (0=1"+inCondition+") AND SNAP_DATE>=? AND SNAP_DATE<?";
			
			
//			String sql = "SELECT * FROM ARPU_09_2021 WHERE MSISDN IN("+Arrays.asList(msisdns).stream().collect(Collectors.joining("','", "'", "'"))+")";
//			System.out.println(sql);
			ps = connection.prepareStatement(sql);
			ps.setDate(1, java.sql.Date.valueOf(sqlStartDate));
			ps.setDate(2, java.sql.Date.valueOf(sqlEndDate));
			rs = ps.executeQuery();
			while (rs.next()) {
				result.add(rs.getString(1));
			}

			rs.close();
			ps.close();
			
			return result;
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		} finally {
			connection.commit();
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			PlatformImpl.getPlatform().getDbUtils().releaseConnection(connection);
		}
	}
	
	public static List<String> selectSH(String[] msisdns, String sqlStartDate, String sqlEndDate, String zz) throws SQLException{
		Connection connection = PlatformImpl.getPlatform().getDbUtils().getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> result = new ArrayList<>();
		try {
			
			String inCondition = "";
			for(int i=0;i<msisdns.length;i+=1000) {
				inCondition+=" OR MSISDN IN(";
				if(i+1000>msisdns.length) {
					inCondition += Arrays.asList(Arrays.copyOfRange(msisdns, i, msisdns.length)).stream().collect(Collectors.joining("','", "'", "'"));
					inCondition+=" )";
					break;
				}
				inCondition += Arrays.asList(Arrays.copyOfRange(msisdns, i, i+1000)).stream().collect(Collectors.joining("','", "'", "'"));
				inCondition+=" )";
			}
			String sql = "SELECT RECORD FROM SH_"+zz+" WHERE (0=1"+inCondition+") AND SNAP_DATE>=? AND SNAP_DATE<?";
			
			
			ps = connection.prepareStatement(sql);
			ps.setDate(1, java.sql.Date.valueOf(sqlStartDate));
			ps.setDate(2, java.sql.Date.valueOf(sqlEndDate));
			rs = ps.executeQuery();
			while (rs.next()) {
				result.add(rs.getString(1));
			}

			rs.close();
			ps.close();
			
			return result;
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		} finally {
			connection.commit();
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			PlatformImpl.getPlatform().getDbUtils().releaseConnection(connection);
		}
	}
	
	public static List<String> selectTH(String[] msisdns, String sqlStartDate, String sqlEndDate) throws SQLException{
		Connection connection = PlatformImpl.getPlatform().getDbUtils().getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> result = new ArrayList<>();
		try {
			
			String inCondition = "";
			for(int i=0;i<msisdns.length;i+=1000) {
				inCondition+=" OR MSISDN IN(";
				if(i+1000>msisdns.length) {
					inCondition += Arrays.asList(Arrays.copyOfRange(msisdns, i, msisdns.length)).stream().collect(Collectors.joining("','", "'", "'"));
					inCondition+=" )";
					break;
				}
				inCondition += Arrays.asList(Arrays.copyOfRange(msisdns, i, i+1000)).stream().collect(Collectors.joining("','", "'", "'"));
				inCondition+=" )";
			}
			String sql = "SELECT RECORD FROM TH WHERE (0=1"+inCondition+") AND SNAP_DATE>=? AND SNAP_DATE<?";
			
			
			ps = connection.prepareStatement(sql);
			ps.setDate(1, java.sql.Date.valueOf(sqlStartDate));
			ps.setDate(2, java.sql.Date.valueOf(sqlEndDate));
			rs = ps.executeQuery();
			while (rs.next()) {
				result.add(rs.getString(1));
			}

			rs.close();
			ps.close();
			
			return result;
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		} finally {
			connection.commit();
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			PlatformImpl.getPlatform().getDbUtils().releaseConnection(connection);
		}
	}
}
