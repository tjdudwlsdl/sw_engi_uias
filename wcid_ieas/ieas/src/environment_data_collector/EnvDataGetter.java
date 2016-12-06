package environment_data_collector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
		
    	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
