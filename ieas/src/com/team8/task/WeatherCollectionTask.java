package com.team8.task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.TimerTask;

import com.team8.Meta_DB;
import com.team8.environment_data_collector.Weather;
import com.team8.environment_data_collector.WeatherIntelligenceCollector;

// 기상 데이터를 수집하는 Task 클래스 입니다.
public class WeatherCollectionTask extends TimerTask {
	public static final int INTERVAL = 3*3600*1000;
	
	public WeatherCollectionTask() {
		try {
			Class.forName(Meta_DB.driver);
		} catch (Exception ignored) { }
	}
	
	@Override
	public void run() {
		Connection conn = null;
		try{
			conn = DriverManager.getConnection(
					Meta_DB.db_url, Meta_DB.db_user, Meta_DB.db_password);
			
			
		}
		catch(Exception e) {
			
		}
		finally {
			if(conn != null) {
				try {
					conn.close();
				} catch(Exception ignored) { }
			}
		}
		
	}

}
