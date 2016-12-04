package auto_control_management;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
 * Servlet implementation class AutoControlManager
 * Auto Control 사용 여부를 제어합니다.
 */
@WebServlet("/AutoControlManager")
public class AutoControlManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Object lock = new Object();
	
	private Connection conn;
	private PreparedStatement get_pstmt;
	private PreparedStatement update_pstmt;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutoControlManager() {
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
    		get_pstmt = conn.prepareStatement(String.format(
    				"SELECT %s FROM %s WHERE %s=?", 
    				Meta_DB.col_mbAuto, Meta_DB.tb_member, Meta_DB.col_mbID));
    		update_pstmt = conn.prepareStatement(String.format(
    				"UPDATE %s SET %s=? WHERE %s=?",
    				Meta_DB.tb_member, Meta_DB.col_mbAuto, Meta_DB.col_mbID));
    		
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
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
    	String userID = (String)session.getAttribute("Logon.isDone");
    	if(userID==null) {
    		response.sendRedirect(String.format("%s://%s:%d%s", 
    				request.getScheme(), request.getServerName(), request.getServerPort(), Meta_Page.LOGINPAGE));
    		return;
    	}
		
		String auto = request.getParameter("auto");
		int mb_auto = -1;
		try {
			switch(auto) {
			case "on":
				mb_auto = 1;
				break;
				
			case "off":
				mb_auto = 0;
				break;
				
			default:
				throw new Exception();
			}
			
			synchronized (lock) {
				update_pstmt.clearParameters();
				update_pstmt.setInt(1, mb_auto);
				update_pstmt.setString(2, userID);
				update_pstmt.executeUpdate();
			}
		}
		catch (Exception e) {
			try {
				synchronized (lock) {
					get_pstmt.clearParameters();
					get_pstmt.setString(1, userID);
					ResultSet rs = get_pstmt.executeQuery();
					if(rs.next())
						mb_auto = rs.getInt(Meta_DB.col_mbAuto);
				}
			} catch(Exception ignored) { }
		}
		
		RequestDispatcher rd = request.getRequestDispatcher(Meta_Page.AUTOPAGE);
		request.setAttribute("autoflag", String.valueOf(mb_auto));
		rd.forward(request, response);
	}

}
