package mcdata.process;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import mcdata.process.utils.DBUtils;
import mcdata.process.utils.DatabaseInfo;



public class Test {
	static DBUtils dbUtils;
	public static void main(String[] args) {
		
		try {
			dbUtils = new DBUtils(new DatabaseInfo());
			Set<String> listR = new HashSet<>();
			List<String> list = selectCH();
			String result="";
			for (String str : list) {
				String sdt = str.substring(str.indexOf("called_party")+15, str.indexOf("called_party")+26);
				listR.add(sdt);
//				System.out.println(sdt);
			}
			
			result+=(selectBPI(listR.toArray(new String[0]))+"\n");
			Files.write(Paths.get("D:\\looking\\sdt.txt"), result.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<String> selectCH() throws SQLException{
		Connection connection = dbUtils.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> result = new ArrayList<>();
		try {
			
			String sql = "select RECORD from ch_36 where msisdn in('84343932136') and snap_date < date '2022-02-01'and snap_date >=date '2021-08-01'";
			
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
			dbUtils.releaseConnection(connection);
		}
		
	}
	
	public static List<String> selectBPI(String[] msisdns) throws SQLException{
		Connection connection = dbUtils.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> result = new ArrayList<>();
		try {
			String inCondition = "";
			for(int i=0;i<msisdns.length;i+=1000) {
				inCondition+="MSISDN IN(";
				if(i+1000>msisdns.length) {
					inCondition += Arrays.asList(Arrays.copyOfRange(msisdns, i, msisdns.length)).stream().collect(Collectors.joining("','", "'", "'"));
					inCondition+=" )";
					break;
				}
				inCondition += Arrays.asList(Arrays.copyOfRange(msisdns, i, i+1000)).stream().collect(Collectors.joining("','", "'", "'"));
				inCondition+=" )";
			}
//			String inCondition = "0=1";
//			for(int i=0;i<msisdns.length;i++) {
//				inCondition+=" OR MSISDN ='"+msisdns[i]+"'";
//
//			}
			String sql = "select RECORD from bpi where "+inCondition;
			System.out.println(sql);
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
			dbUtils.releaseConnection(connection);
		}
	}
}
