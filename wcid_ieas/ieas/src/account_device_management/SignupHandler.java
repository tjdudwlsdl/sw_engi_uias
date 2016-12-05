package account_device_management;

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
import javax.servlet.http.HttpSession;

import util_core.LocationUtil;
import util_core.Meta_DB;
import util_core.Meta_Page;
import util_core.SecurityUtil;

/**
 * Servlet implementation class SignupHandler
 * 유저 등록을 수행합니다.
 * !! 미완성 !!
 */
@WebServlet("/SignupHandler")
public class SignupHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Object lock = new Object();
	
	private Connection conn;
	private PreparedStatement pstmt;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignupHandler() {
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
    		pstmt = conn.prepareStatement(String.format(
    				"INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?,?,?,?,?)",
    				Meta_DB.tb_member, Meta_DB.col_mbID, Meta_DB.col_mbHashPasswd,
    				Meta_DB.col_mbSalt, Meta_DB.col_mbLocation, Meta_DB.col_mbLocalcode));
    		
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
		String localCode = (String)session.getAttribute("localcode");	// mb_localcode
		if(localCode == null) {
			return;
		}
		String location = (String)session.getAttribute("location");	// mb_location
		
		String userID = (String)session.getAttribute("id");		// mb_id
		String password = (String)session.getAttribute("password");
		String salt = SecurityUtil.createSalt(); 	// mb_salt
		String hashedpswd = SecurityUtil.encryptSHA256(password+salt);	//mb_password
		
		try {
			synchronized (lock) {
				pstmt.clearParameters();
				pstmt.setString(1, userID);
				pstmt.setString(2, hashedpswd);
				pstmt.setString(3, salt);
				pstmt.setString(4, location);
				pstmt.setString(5, localCode);
				pstmt.executeUpdate();
			}
		}
		catch(Exception e) {
			
		}
	
		session.invalidate();
		response.sendRedirect(String.format("%s://%s:%d/ieas%s",
				request.getScheme(), request.getServerName(), request.getServerPort(),
				Meta_Page.LOGINPAGE));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

}
