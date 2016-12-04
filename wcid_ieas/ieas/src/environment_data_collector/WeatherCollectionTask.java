package environment_data_collector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.TimerTask;

import util_core.Meta_DB;

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
			
			// 등록된 멤버들의 localcode 정보를 가져옴
			PreparedStatement localList_pstmt = conn.prepareStatement(String.format(
					"SELECT %s FROM %s GROUP BY %s",
					Meta_DB.col_mbLocalcode, Meta_DB.tb_member, Meta_DB.col_mbLocalcode));
			localList_pstmt.clearParameters();
			ResultSet rs = localList_pstmt.executeQuery();
			
			
			// localcode 마다 기상 정보를 가져와 저장
			PreparedStatement insert_pstmt = conn.prepareStatement(String.format(
					"INSERT INTO %s VALUES (?,?,?,?,?,?,?,?)", Meta_DB.tb_weather));
			while(rs.next()) {
				Weather weather = WeatherIntelligenceCollector.getRecentWeather(
						rs.getString(Meta_DB.col_mbLocalcode));
				
				if(weather == null) 
					continue;
				
				insert_pstmt.clearParameters();
				insert_pstmt.setString(1, weather.getLocalcode());
				insert_pstmt.setDate(2, weather.getDate());
				insert_pstmt.setTime(3, weather.getTime());
				insert_pstmt.setDouble(4, weather.getTemperature());
				insert_pstmt.setDouble(5, weather.getHumidity());
				insert_pstmt.setInt(6, weather.getPty());
				insert_pstmt.setInt(7, weather.getSky());
				insert_pstmt.setInt(8, weather.getPop());
				insert_pstmt.executeUpdate();
			}
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
