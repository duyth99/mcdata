package mcdata.process;

import java.sql.Connection;
import java.sql.SQLException;

import mcdata.process.command_id.ArpuProcess;
import mcdata.process.command_id.BaseInfoProcess;
import mcdata.process.command_id.CallHistProcess;
import mcdata.process.command_id.SmsHistProcess;
import mcdata.process.command_id.TopupHistProcess;
import mcdata.process.utils.DBUtils;
import mcdata.process.utils.DatabaseInfo;

public class App {
	private static App app;
	private DBUtils dbUtils;

	public App() throws Exception {
		dbUtils = new DBUtils(new DatabaseInfo());
	}
	public void startApp(String[] args) {

//		List<Data> listInsert = new ArrayList<>();
//		listInsert.add(new Data("0829600368",2022+"-"+02+"-"+02,"ok12345"));
//		try {
//			Database.insert(listInsert);
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		if(0==0)
//			System.exit(0);
		
		long now = System.currentTimeMillis();
		String commandId = args[0];
		Connection conn = null;
		try {
			conn = dbUtils.getConnection();
			switch(commandId) {
				
				case "BPI":
					BaseInfoProcess.getApp().startApp(args, conn);
					break;
				case "ARPU":
					ArpuProcess.getApp().startApp(args, conn);
					break;
				case "CH":
					CallHistProcess.getApp().startApp(args, conn);
					break;
				case "SH":
					SmsHistProcess.getApp().startApp(args, conn);
					break;
				case "TH":
					TopupHistProcess.getApp().startApp(args, conn);
					break;
				default:
					System.out.println("???");
					System.exit(-1);
		
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				dbUtils.releaseConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("\n"+(System.currentTimeMillis() - now)+"ms");
		
	}
	
	public static void main(String[] args) {
		App.getApp().startApp(args);
	}

	public DBUtils getDbUtils() {
		return dbUtils;
	}

	public void setDbUtils(DBUtils dbUtils) {
		this.dbUtils = dbUtils;
	}

	public synchronized static App getApp() {
		if (app == null) {
			try {
				app = new App();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return app;
	}
}
