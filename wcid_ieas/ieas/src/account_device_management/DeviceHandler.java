package account_device_management;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
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
 * Servlet implementation class DeviceHandler
 */
@WebServlet("/DeviceHandler")
public class DeviceHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Object lock = new Object();
	
	private static Connection conn;
	PreparedStatement regdev_pstmt;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeviceHandler() {
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
    		regdev_pstmt = conn.prepareStatement(String.format(
    				"INSERT INTO %s (%s, %s, %s) VALUES (?,?,?)",
    				Meta_DB.tb_device, Meta_DB.col_dvID, Meta_DB.col_mbID, Meta_DB.col_dvType));
    		
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
    	
    	String id = request.getParameter("id");
    	String type = request.getParameter("type");
    	try {
    		synchronized (lock) {
				regdev_pstmt.clearParameters();
				regdev_pstmt.setString(1, id);
				regdev_pstmt.setString(2, userID);
				regdev_pstmt.setString(3, type);
				regdev_pstmt.executeUpdate();
			}
    	}
    	catch(Exception ignored) { }
    	
    	RequestDispatcher rd = request.getRequestDispatcher("/main.jsp");
    	rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
