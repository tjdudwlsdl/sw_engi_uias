package environment_data_collector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.Condition;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util_core.Meta_DB;
import util_core.Meta_Page;

/**
 * Servlet implementation class EnvDataGetter
 * 유저의 환경 데이터 정보를 가져옵니다.
 */
@WebServlet("/EnvDataGetter")
public class EnvDataGetter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Object lock = new Object();
	
	private Connection conn;
	private PreparedStatement we_pstmt;
	private PreparedStatement se_pstmt;
	private PreparedStatement co_pstmt;
	private PreparedStatement dv_pstmt;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EnvDataGetter() {
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
    		dv_pstmt = conn.prepareStatement(String.format(
    				"SELECT (%s,%s) FROM %s WHERE %s=?",
    				Meta_DB.col_dvID, Meta_DB.col_dvType,
    				Meta_DB.tb_device, Meta_DB.col_mbID));
    		we_pstmt = conn.prepareStatement(String.format(
    				"SELECT * FROM %s w INNER %s m ON w.%s=m.%s WHERE %s=?",
    				Meta_DB.tb_weather, Meta_DB.tb_member,
    				Meta_DB.col_weLocalcode, Meta_DB.col_mbLocalcode, Meta_DB.col_mbID));
    		se_pstmt = conn.prepareStatement(String.format(
    				"SELECT * FROM %s WHERE %s=?",
    				Meta_DB.tb_SensorData, Meta_DB.col_dvID));
    		co_pstmt = conn.prepareStatement(String.format(
    				"SELECT * FROM %s WHERE %s=?",
    				Meta_DB.tb_ControllerData, Meta_DB.col_dvID));				
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
		HttpSession session = request.getSession(true);
    	String userID = (String)session.getAttribute("Logon.isDone");
    	if(userID==null) {
    		response.sendRedirect(String.format("%s://%s:%d/ieas%s", 
    				request.getScheme(), request.getServerName(), request.getServerPort(), Meta_Page.LOGINPAGE));
    		return;
    	}
		
    	String location = "";
    	String condition = "";
    	String tmp = "";
    	String reh = "";
    	String co2 = "";
    	String state = "";
    	ResultSet rs;
    	// 유저가 등록된 위치의 기상 정보를 가져옴
    	try {
    		synchronized (lock) {
				we_pstmt.clearParameters();
				we_pstmt.setString(1, userID);
				rs = we_pstmt.executeQuery();
			}
    		if (rs.next()) {
    			location = rs.getString(Meta_DB.col_mbLocation);
    			int sky = rs.getInt(Meta_DB.col_weSky);
    			int pty = rs.getInt(Meta_DB.col_wePty);
    			if(pty==0) {
    				
    			}
    		}
    	}
    	catch(Exception ignored) { }
    	
    	// 각 디바이스에서 수집된 정보를 가져옴
    	try {
    		synchronized (lock) {
				dv_pstmt.clearParameters();
				dv_pstmt.setString(1, userID);
				rs = dv_pstmt.executeQuery();
			}
    		while (rs.next()) {
    			String dv_id = rs.getString(Meta_DB.col_dvID);
    			String dv_type = rs.getString(Meta_DB.col_dvType);
    			ResultSet envRs;
    			switch(dv_type) {
    			case "sensor":
    				synchronized (lock) {
        				se_pstmt.clearParameters();
        				se_pstmt.setString(1, dv_id);
        				envRs = se_pstmt.executeQuery();
    				}
    				if(envRs.next()) {
    					
    				}
    				break;
    				
    			case "controller":
    				synchronized (lock) {
        				co_pstmt.clearParameters();
        				co_pstmt.setString(1, dv_id);
        				envRs = co_pstmt.executeQuery();
    				}
    				if(envRs.next()) {
    					
    				}
    				
    				break;
    			}
    			
    		}
    	}
    	catch(Exception ignored) { }
    	
    	request.setAttribute("location", location);
    	request.setAttribute("condition", condition);
    	request.setAttribute("temperature", tmp);
    	request.setAttribute("humidity", reh);
    	request.setAttribute("co2", co2);
    	request.setAttribute("state", state);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
