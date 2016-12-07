package remote_control;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

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
 * Servlet implementation class RemoteControlHandler
 * 창문의 원격 제어를 핸들링합니다.
 * !! 미완성 !!
 */
@WebServlet("/RemoteControlHandler")
public class RemoteControlHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Object lock = new Object();
	
	private Connection conn;
	private PreparedStatement getdv_pstmt;
	private PreparedStatement getcomm_pstmt;
	private PreparedStatement incomm_pstmt;
	private PreparedStatement delcomm_pstmt;
	private PreparedStatement controller_pstmt;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoteControlHandler() {
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
    		getdv_pstmt = conn.prepareStatement(String.format(
    				"SELECT %s FROM %s d JOIN %s m ON d.%s=m.%s WHERE d.%s=? AND %s=?",
    				Meta_DB.col_dvID, Meta_DB.tb_device, Meta_DB.tb_member,
    				Meta_DB.col_mbID, Meta_DB.col_mbID, Meta_DB.col_mbID, Meta_DB.col_dvType));
    		getcomm_pstmt = conn.prepareStatement(String.format(
    				"SELECT * FROM %s WHERE %s=? ORDER BY %s asc, %s desc, %s desc", 
    				Meta_DB.tb_schedule, Meta_DB.col_dvID,
    				Meta_DB.col_scPriority, Meta_DB.col_scDate, Meta_DB.col_scTime));
    		incomm_pstmt = conn.prepareStatement(String.format(
    				"INSERT INTO %s VALUES (?,?,?,?,?,?)", Meta_DB.tb_schedule));
    		delcomm_pstmt = conn.prepareStatement(String.format(
    				"DELETE FROM %s WHERE %s=?", Meta_DB.tb_schedule, Meta_DB.col_dvID));
    		controller_pstmt = conn.prepareStatement(String.format(
    				"INSERT INTO %s VALUES (?,?,?,?,?)", Meta_DB.tb_ControllerData));
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

	// 원격 제어 명령이 있을 경우 컨트롤러에 전달합니다.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String dv_id = request.getParameter("id");
			PrintWriter pw = response.getWriter();
			synchronized(lock) {
				getcomm_pstmt.clearParameters();
				getcomm_pstmt.setString(1, dv_id);
				ResultSet rs = getcomm_pstmt.executeQuery();
				if(rs.next()) {
					DateTime dt = new DateTime("Asia/Seoul");
					int act = rs.getInt(Meta_DB.col_scAct);
					int priority = rs.getInt(Meta_DB.col_scPriority);
					Date sc_date = rs.getDate(Meta_DB.col_scDate);
					Time sc_time = rs.getTime(Meta_DB.col_scTime);
					
					if(dt.getDateTimeDiff(sc_date, sc_time) > 180000l)
						pw.println("none");
					else {
						if(act!=100)
							pw.println("close");
						else
							pw.println("open");
					
						controller_pstmt.clearParameters();
						controller_pstmt.setDate(1, dt.getDate());
						controller_pstmt.setTime(2, dt.getTime());
						controller_pstmt.setString(3, dv_id);
						controller_pstmt.setInt(4, act);
						controller_pstmt.setInt(5, priority);
						controller_pstmt.executeUpdate();
					}
					delcomm_pstmt.clearParameters();
					delcomm_pstmt.setString(1, dv_id);
					delcomm_pstmt.executeUpdate();
				}
				else {
					pw.println("none");
				}
			}
		}
		catch(Exception e) {}
	}

	// 원격 제어 명령을 입력합니다.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
    	String userID = (String)session.getAttribute("Logon.isDone");
    	if(userID==null) {
    		response.sendRedirect(String.format("%s://%s:%d/ieas%s", 
    				request.getScheme(), request.getServerName(), request.getServerPort(), Meta_Page.LOGINPAGE));
    		return;
    	}
    	
    	// 입력된 명령을 가져와 schedule 테이블에 저장
		try {
			int act;
			if(request.getParameter("act").equals("Open"))
				act = 100;
			else
				act = 0;
			DateTime dt = new DateTime("Asia/Seoul");
			
			
			synchronized (lock) {
				getdv_pstmt.clearParameters();
				getdv_pstmt.setString(1, userID);
				getdv_pstmt.setString(2, "controller");
				ResultSet rs = getdv_pstmt.executeQuery();
					
				if(rs.next()) {
					String dv_id = rs.getString(Meta_DB.col_dvID);
						
					incomm_pstmt.clearParameters();
					incomm_pstmt.setDate(1, dt.getDate());
					incomm_pstmt.setTime(2, dt.getTime());
					incomm_pstmt.setString(3, userID);
					incomm_pstmt.setString(4, dv_id);
					incomm_pstmt.setInt(5, act);
					incomm_pstmt.setInt(6, 0);
					incomm_pstmt.executeUpdate();
				}
			}
		}
		catch(Exception e) { }
		
		RequestDispatcher rd = request.getRequestDispatcher(Meta_Page.MAINPAGE);
		rd.forward(request, response);
		
		
	}

}
