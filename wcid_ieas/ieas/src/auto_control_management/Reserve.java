package auto_control_management;

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

import util_core.DateTime;
import util_core.Meta_DB;
import util_core.Meta_Page;

/**
 * Servlet implementation class Reserve
 * 예약을 추가하거나 취소합니다.
 * !! 미완성 !!
 */
@WebServlet("/Reserve")
public class Reserve extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Object lock = new Object();
	
	private Connection conn;
	private PreparedStatement reserve_pstmt;
	private PreparedStatement cancel_pstmt;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Reserve() {
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
    		reserve_pstmt = conn.prepareStatement(String.format(
    				"INSERT INTO %s (%s,%s,%s,%s) VALUES (?,?,?,?)",
    				Meta_DB.tb_reservation, Meta_DB.col_rsActDate, Meta_DB.col_rsActTime,
    				Meta_DB.col_mbID, Meta_DB.col_rsAct));
    		cancel_pstmt = conn.prepareStatement(String.format(
    				"DELETE FROM %s WHERE %s=?",
    				Meta_DB.tb_reservation, Meta_DB.col_rsID));
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
    	
    	String command = request.getParameter("command");
    	if(command == null)
    		return;
    	
    	switch(command) {
    	case "reserve":
    		String year = request.getParameter("FirstSelectYear");
        	String month = request.getParameter("FirstSelectMonth");
        	String day = request.getParameter("FirstSelectDay");
        	String hour = request.getParameter("FirstSelectHour");
        	String minute = request.getParameter("FirstSelectMinute");
        	try {
        		DateTime dt = new DateTime(year, month, day, hour, minute);
        		int rs_act = Integer.parseInt(request.getParameter("act"));
        		synchronized (lock) {
					reserve_pstmt.clearParameters();
					reserve_pstmt.setDate(1, dt.getDate());
					reserve_pstmt.setTime(2, dt.getTime());
					reserve_pstmt.setString(3, userID);
					reserve_pstmt.setInt(4, rs_act);
					reserve_pstmt.executeUpdate();
				}
        	} catch(Exception ignored) { }
        	request.getRequestDispatcher(Meta_Page.RESERVERESPAGE).forward(request, response);
    		break;
    		
    	case "cancel":
    		try {
    			int rs_id = Integer.parseInt(request.getParameter("id"));
    			synchronized (lock) {
    				cancel_pstmt.clearParameters();
    				cancel_pstmt.setInt(1, rs_id);
    				cancel_pstmt.executeUpdate();
				}
    		}
    		catch(Exception ignored) { }
    		request.getRequestDispatcher(Meta_Page.SCHEDULEPAGE).forward(request, response);
    		break;
    		
    	default:
    		return;
    	}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
