package environment_data_collector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util_core.Meta_DB;

/**
 * Servlet implementation class WeatherCollectionTask
 */
@WebServlet("/WeatherCollectionTask")
public class WeatherCollectionTask extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection conn = null;
	PreparedStatement localList_pstmt;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeatherCollectionTask() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	try {
    		Class.forName(Meta_DB.driver);
    		conn = DriverManager.getConnection(
    				Meta_DB.db_url, Meta_DB.db_user, Meta_DB.db_password);
    		localList_pstmt = conn.prepareStatement(String.format(
					"SELECT %s FROM %s GROUP BY %s",
					Meta_DB.col_mbLocalcode, Meta_DB.tb_member, Meta_DB.col_mbLocalcode));
    	}
    	catch(ClassNotFoundException e) {
    		throw new UnavailableException("Couldn't load database driver");
    	}
    	catch(SQLException e){
    		throw new UnavailableException("Couldn't get db connection");
    	}
    }
    
    @Override
    public void destroy() {
    	super.destroy();
    	try {
    		if(conn != null)
    			conn.close();
    	}
    	catch (SQLException ignore) { }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			
			// 등록된 멤버들의 localcode 정보를 가져옴
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
