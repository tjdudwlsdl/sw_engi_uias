package environment_data_analyzer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.TimerTask;

import util_core.Meta_DB;

public class AnalysisTask extends TimerTask{
	public static final int INTERVAL = 60 * 1000;
	
	private Connection conn;

	public AnalysisTask() {
		try {
			Class.forName(Meta_DB.driver);
			conn = DriverManager.getConnection(
					Meta_DB.db_url, Meta_DB.db_user, Meta_DB.db_password);
		} catch (Exception ignored) { }
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
