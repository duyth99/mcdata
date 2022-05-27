package mcdata.process.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mcdata.process.model.Data;

public class Database {
	public static void insertBPI(Connection conn, List<Data> list) throws SQLException{
//		Connection conn = App.getApp().getDbUtils().getConnection();
		PreparedStatement ps = null;
		try {
			String sql = "INSERT INTO BPI VALUES(?,?,?)";
			ps = conn.prepareStatement(sql);
			for (Data data : list) {
				ps.setString(1, data.getMsisdn());
				ps.setDate(2, java.sql.Date.valueOf(data.getDate()));
				ps.setString(3, data.getRecord());
				ps.addBatch();
			}
			
			ps.executeBatch();
            
            ps.close();
			
		}catch(SQLException e){
			conn.rollback();
			throw e;
		} finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			conn.commit();
//			App.getApp().getDbUtils().releaseConnection(conn);
		}
	}
	
	public static void insertARPU(Connection conn, List<Data> list) throws SQLException{
//		Connection conn = App.getApp().getDbUtils().getConnection();
		PreparedStatement ps = null;
		try {
			String sql = "INSERT INTO ARPU VALUES(?,?,?)";
			ps = conn.prepareStatement(sql);
			for (Data data : list) {
				ps.setString(1, data.getMsisdn());
				ps.setDate(2, java.sql.Date.valueOf(data.getDate()));
				ps.setString(3, data.getRecord());
				ps.addBatch();
			}
			
			ps.executeBatch();
            
            ps.close();
			
		}catch(SQLException e){
			conn.rollback();
			throw e;
		} finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			conn.commit();
//			App.getApp().getDbUtils().releaseConnection(conn);
		}
	}
	
	public static void insertCH(Connection conn, List<Data> list, String table) throws SQLException{
//		Connection conn = App.getApp().getDbUtils().getConnection();
		PreparedStatement ps = null;
		try {
			String sql = "INSERT INTO CH_" + table + " VALUES(?,?,?)";
			ps = conn.prepareStatement(sql);
			for (Data data : list) {
				ps.setString(1, data.getMsisdn());
				ps.setDate(2, java.sql.Date.valueOf(data.getDate()));
				ps.setString(3, data.getRecord());
				ps.addBatch();
			}
			
			ps.executeBatch();
            
            ps.close();
			
		}catch(SQLException e){
			conn.rollback();
			throw e;
		} finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			conn.commit();
//			App.getApp().getDbUtils().releaseConnection(conn);
		}
	}
	
	public static void insertSH(Connection conn, List<Data> list, String table) throws SQLException{
//		Connection conn = App.getApp().getDbUtils().getConnection();
		PreparedStatement ps = null;
		try {
			String sql = "INSERT INTO SH_" + table + " VALUES(?,?,?)";
			ps = conn.prepareStatement(sql);
			for (Data data : list) {
				ps.setString(1, data.getMsisdn());
				ps.setDate(2, java.sql.Date.valueOf(data.getDate()));
				ps.setString(3, data.getRecord());
				ps.addBatch();
			}
			
			ps.executeBatch();
            
            ps.close();
			
		}catch(SQLException e){
			conn.rollback();
			throw e;
		} finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			conn.commit();
//			App.getApp().getDbUtils().releaseConnection(conn);
		}
	}
	
	public static void insertTH(Connection conn, List<Data> list) throws SQLException{
//		Connection conn = App.getApp().getDbUtils().getConnection();
		PreparedStatement ps = null;
		try {
			String sql = "INSERT INTO TH VALUES(?,?,?)";
			ps = conn.prepareStatement(sql);
			for (Data data : list) {
				ps.setString(1, data.getMsisdn());
				ps.setDate(2, java.sql.Date.valueOf(data.getDate()));
				ps.setString(3, data.getRecord());
				ps.addBatch();
			}
			
			ps.executeBatch();
            
            ps.close();
			
		}catch(SQLException e){
			conn.rollback();
			throw e;
		} finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			conn.commit();
//			App.getApp().getDbUtils().releaseConnection(conn);
		}
	}
	
	public static List<String> ping(Connection connection) throws SQLException{
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
		}
	}
}
