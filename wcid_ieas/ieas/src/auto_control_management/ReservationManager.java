package auto_control_management;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
 * Servlet implementation class ReservationManager
 * 사용자에 의한 예약을 관리합니다.
 * !! 미완성 !!
 */
@WebServlet("/ReservationManager")
public class ReservationManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Object lock = new Object();
	
	private Connection conn;
	private PreparedStatement get_pstmt;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReservationManager() {
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
    				"SELECT * FROM %s WHERE %s=?", Meta_DB.tb_reservation, Meta_DB.col_mbID));
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


    // 유저의 Reservation 리스트를 request로 반환합니다.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
    	String userID = (String)session.getAttribute("Logon.isDone");
    	if(userID==null) {
    		response.sendRedirect(String.format("%s://%s:%d/ieas%s", 
    				request.getScheme(), request.getServerName(), request.getServerPort(), Meta_Page.LOGINPAGE));
    		return;
    	}
    	// 해당 사용자의 예약 리스트를 가져옴
    	ArrayList<Reservation> list = new ArrayList<Reservation>();
    	try {
    		ResultSet rs;
    		synchronized (lock) {
    			get_pstmt.clearParameters();
    			get_pstmt.setString(1, userID);
    			rs = get_pstmt.executeQuery();
    		}
    		while(rs.next()) {
    			list.add(new Reservation(
    			rs.getInt(Meta_DB.col_rsID),
    			rs.getDate(Meta_DB.col_rsActDate),
   				rs.getTime(Meta_DB.col_rsActTime),
    			rs.getString(Meta_DB.col_mbID),
    			rs.getInt(Meta_DB.col_rsAct)));
    		}
    	} catch(Exception ignored) { }
    	
    	request.setAttribute("list", list.toArray(new Reservation[0]));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
