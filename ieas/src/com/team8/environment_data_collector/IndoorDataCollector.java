package com.team8.environment_data_collector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.team8.DateTime;
import com.team8.Meta_DB;

/**
 * Servlet implementation class IndoorDataCollector
 * 실내의 센서 데이터를 수집합니다.
 * !! 미완성 !!
 */
@WebServlet("/IndoorDataCollector")
public class IndoorDataCollector extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Object sensor_lock = new Object();
	private static final Object controller_lock = new Object();

	private Connection conn;
	private PreparedStatement sensor_pstmt;
	private PreparedStatement controller_pstmt;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndoorDataCollector() {
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
    		sensor_pstmt = conn.prepareStatement(String.format(
    				"INSERT INTO %s VALUES (?,?,?,?,?,?)", Meta_DB.tb_SensorData));
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
		
		String dv_id = request.getParameter("id");
		String type = request.getParameter("type");
		DateTime dateTime = new DateTime("Asia/seoul");
		
		switch(type) {
		
		// 센서 데이터
		case "sensor":
			try {
				Double temp = Double.parseDouble(request.getParameter("temp"));
				Double reh = Double.parseDouble(request.getParameter("reh"));
				Double co2 = Double.parseDouble(request.getParameter("co2"));
				
				synchronized (sensor_lock) {
					sensor_pstmt.clearParameters();
					sensor_pstmt.setDate(1, dateTime.getDate());
					sensor_pstmt.setTime(2, dateTime.getTime());
					sensor_pstmt.setString(3, dv_id);
					sensor_pstmt.setDouble(4, temp);
					sensor_pstmt.setDouble(5, reh);
					sensor_pstmt.setDouble(6, co2);
					sensor_pstmt.executeUpdate();
				}
			}
			catch(Exception e) {
				
			}
			
			break;
			
		// 컨트롤러 데이터
		case "controller":
			try {
				int state = Integer.parseInt(request.getParameter("state"));
				
				synchronized (controller_lock) {
					controller_pstmt.clearParameters();
					controller_pstmt.setDate(1, dateTime.getDate());
					controller_pstmt.setTime(2, dateTime.getTime());
					controller_pstmt.setString(3, dv_id);
					controller_pstmt.setInt(4, state);
					controller_pstmt.executeUpdate();
				}
			}
			catch(Exception e) {
				
			}
			break;
			
		default:
				
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
