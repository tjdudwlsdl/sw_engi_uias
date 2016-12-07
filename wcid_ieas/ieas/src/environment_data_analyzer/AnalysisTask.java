package environment_data_analyzer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import environment_data_collector.ControllerData;
import environment_data_collector.SensorData;
import environment_data_collector.Weather;
import util_core.DateTime;
import util_core.Meta_DB;

/**
 * Servlet implementation class AnalysisTask
 */
@WebServlet("/AnalysisTask")
public class AnalysisTask extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn;
	private PreparedStatement mb_pstmt;
	private PreparedStatement dv_pstmt;
	private PreparedStatement comm_pstmt;
	private PreparedStatement res_pstmt;
	private PreparedStatement delres_pstmt;
	private PreparedStatement we_pstmt;
	private PreparedStatement se_pstmt;
	private PreparedStatement co_pstmt;

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnalysisTask() {
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
    		mb_pstmt = conn.prepareStatement(String.format(
					"SELECT * FROM %s", Meta_DB.tb_member));
			dv_pstmt = conn.prepareStatement(String.format(
					"SELECT * FROM %s WHERE %s=?",
					Meta_DB.tb_device, Meta_DB.col_mbID));
			comm_pstmt = conn.prepareStatement(String.format(
					"INSERT INTO %s VALUES (?,?,?,?,?,?)", Meta_DB.tb_schedule));
			res_pstmt = conn.prepareStatement(String.format(
					"SELECT * FROM %s WHERE %s=? ORDER BY %s asc, %s asc", 
					Meta_DB.tb_reservation, Meta_DB.col_mbID,
					Meta_DB.col_rsActDate, Meta_DB.col_rsActTime));
			delres_pstmt = conn.prepareStatement(String.format(
    				"DELETE FROM %s WHERE %s=?",
    				Meta_DB.tb_reservation, Meta_DB.col_rsID));
			we_pstmt = conn.prepareStatement(String.format(
					"SELECT * FROM %s WHERE %s=? ORDER BY %s desc, %s desc",
					Meta_DB.tb_weather, Meta_DB.col_weLocalcode,
					Meta_DB.col_weDate, Meta_DB.col_weTime));
			se_pstmt = conn.prepareStatement(String.format(
					"SELECT * FROM %s WHERE %s=? ORDER BY %s desc, %s desc",
					Meta_DB.tb_SensorData, Meta_DB.col_dvID,
					Meta_DB.col_seDate, Meta_DB.col_seTime));
			co_pstmt = conn.prepareStatement(String.format(
					"SELECT * FROM %s WHERE %s=? ORDER BY %s desc, %s desc",
					Meta_DB.tb_ControllerData, Meta_DB.col_dvID,
					Meta_DB.col_coDate, Meta_DB.col_coTime));
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
		ResultSet mbrs;
		DateTime dt = new DateTime("Asia/Seoul");
		try {
			// 등록된 모든 멤버 순회
			mb_pstmt.clearParameters();
			mbrs = mb_pstmt.executeQuery();
			while(mbrs.next()) {
				try {
					// 유저 및 장치 데이터 가져옴
					String mb_id = mbrs.getString(Meta_DB.col_mbID);
					int mb_auto = mbrs.getInt(Meta_DB.col_mbAuto);
					String localcode = mbrs.getString(Meta_DB.col_mbLocalcode);
					String sensorID=null;
					String controllerID=null;
					ResultSet rs;
					
					// 디바이스 정보 가져옴
					dv_pstmt.clearParameters();
					dv_pstmt.setString(1, mb_id);
					rs = dv_pstmt.executeQuery();
					while(rs.next()) {
						String dvtype = rs.getString(Meta_DB.col_dvType);
						switch(dvtype) {
						case "sencor":
							sensorID = rs.getString(Meta_DB.col_dvID); break;
						case "controller":
							controllerID = rs.getString(Meta_DB.col_dvID); break;
						}
					}
					
					// 기상데이터, 실내 정보들을 가져옴
					Weather we = getRecentWeather(localcode);
					SensorData se = getRecentSensorData(sensorID);
					ControllerData co = getRecentControllerData(controllerID);
					
					// 예약 처리
					try {
						res_pstmt.clearParameters();
						res_pstmt.setString(1, mb_id);
						rs = res_pstmt.executeQuery();
						while(rs.next()) {
							Date rs_d = rs.getDate(Meta_DB.col_rsActDate);
							Time rs_t = rs.getTime(Meta_DB.col_rsActTime);
							int rs_id = rs.getInt(Meta_DB.col_rsID);
							long timediff = dt.getDateTimeDiff(rs_d, rs_t);
							if(timediff > -10000l) {	// 10초 전부터 3분 후까지
								if(timediff < 180000l && (co==null || co.getPriority()>=1
										|| dt.getDateTimeDiff(co.getDate(), co.getTime()) > 600000l)) {	
									int rs_act = rs.getInt(Meta_DB.col_rsAct);
									command(mb_id, controllerID, rs_act, 1);
								}
								
								delres_pstmt.clearParameters();
								delres_pstmt.setInt(1, rs_id);
								delres_pstmt.executeUpdate();
							}
						}
					} catch(Exception ignored) { }
					
					
					// 데이터 분석
					AnalysisResult ar = EnvironmentAnalyzer.AnalyzeEnvironment(we, se, co);
					if(mb_auto != 0) {
						// auto controll 설정 시 실행
						if(ar.HasAction()) {
							command(mb_id, controllerID, ar.getAct(), 2);
						}
					}
				}
				catch(Exception ignored) { }
			}
		}
		catch(Exception ignored) { }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void command(String mb_id, String dv_id, int act, int priority) {
		DateTime dt = new DateTime("Asia/Seoul");
		try {
			comm_pstmt.clearParameters();
			comm_pstmt.setDate(1, dt.getDate());
			comm_pstmt.setTime(2, dt.getTime());
			comm_pstmt.setString(3, mb_id);
			comm_pstmt.setString(4, dv_id);
			comm_pstmt.setInt(5, act);
			comm_pstmt.setInt(6, priority);
			comm_pstmt.executeUpdate();
		}
		catch(Exception ignored) { }
	}
	
	private Weather getRecentWeather(String localcode) {
		Weather weather = null;
		try {
			we_pstmt.clearParameters();
			we_pstmt.setString(1, localcode);
			ResultSet rs = we_pstmt.executeQuery();
			if(rs.next()) {
					weather = new Weather(localcode, 
							rs.getDate(Meta_DB.col_weDate),
							rs.getTime(Meta_DB.col_weTime),
							rs.getDouble(Meta_DB.col_weTemp),
							rs.getDouble(Meta_DB.col_weReh),
							rs.getInt(Meta_DB.col_wePty),
							rs.getInt(Meta_DB.col_weSky),
							rs.getInt(Meta_DB.col_wePop));
			}
		}
		catch(Exception ignored) { }
		return weather;
	}
	
	private SensorData getRecentSensorData(String sensorID) {
		SensorData data = null;
		try {
			se_pstmt.clearParameters();
			se_pstmt.setString(1, sensorID);
			ResultSet rs = se_pstmt.executeQuery();
			if(rs.next()) {
				data = new SensorData(
						rs.getDate(Meta_DB.col_seDate),
						rs.getTime(Meta_DB.col_seTime),
						rs.getString(Meta_DB.col_dvID),
						rs.getDouble(Meta_DB.col_seTmp),
						rs.getDouble(Meta_DB.col_seReh),
						rs.getDouble(Meta_DB.col_seCo2));
			}
		}
		catch(Exception ignored) { }
		return data;
	}
	
	private ControllerData getRecentControllerData(String controllerID) {
		ControllerData data = null;
		try {
			co_pstmt.clearParameters();
			co_pstmt.setString(1, controllerID);
			ResultSet rs = co_pstmt.executeQuery();
			if(rs.next()) {
				data = new ControllerData(
						rs.getDate(Meta_DB.col_coDate),
						rs.getTime(Meta_DB.col_coTime),
						rs.getString(Meta_DB.col_dvID),
						rs.getInt(Meta_DB.col_coState),
						rs.getInt(Meta_DB.col_scPriority));
			}
		}
		catch(Exception ignored) { }
		return data;
	}
}
