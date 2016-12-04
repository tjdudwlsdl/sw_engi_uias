package remote_control;

import java.io.IOException;
import java.io.PrintWriter;
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
	private static final Object getlock = new Object();
	private static final Object postlock = new Object();
	
	private Connection conn;
	private PreparedStatement getdv_pstmt;
	private PreparedStatement getcomm_pstmt;
	private PreparedStatement incomm_pstmt;
	private PreparedStatement delcomm_pstmt;
       
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
    				"SELECT (%s,%s) FROM %s m INNER %s d ON m.%s=d.%s WHERE m.%s=?",
    				Meta_DB.col_mbKey, Meta_DB.col_dvKey, Meta_DB.tb_member, Meta_DB.tb_device,
    				Meta_DB.col_mbKey, Meta_DB.col_mbKey, Meta_DB.col_mbID));
    		getcomm_pstmt = conn.prepareStatement(String.format(
    				"SELECT * FROM %s WHERE %s=?", Meta_DB.tb_schedule, Meta_DB.col_dvID));
    		incomm_pstmt = conn.prepareStatement(String.format(
    				"INSERT INTO %s VALUES (?,?,?,?,?)", Meta_DB.tb_schedule));
    		delcomm_pstmt = conn.prepareStatement(String.format(
    				"DELETE FROM %s WHERE %s=?", Meta_DB.tb_schedule, Meta_DB.col_dvID));
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
			synchronized(getlock) {
				getcomm_pstmt.clearParameters();
				getcomm_pstmt.setString(1, dv_id);
				ResultSet rs = getcomm_pstmt.executeQuery();
				if(rs.next()) {
					int act = rs.getInt(Meta_DB.col_scAct);
					pw.println(String.valueOf(act));
					
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

		try {
			
			HttpSession session = request.getSession(true);
	    	
	    	String userID = (String)session.getAttribute("Logon.isDone");
	    	if(userID==null) {
	    		response.sendRedirect(String.format("%s://%s:%d%s", 
	    				request.getScheme(), request.getServerName(), request.getServerPort(), Meta_Page.LOGINPAGE));
	    		return;
	    	}
			int act;
			if(request.getParameter("act").equals("open"))
				act = 100;
			else
				act = 0;
			DateTime dt = new DateTime("Asia/Seoul");
			
			
			synchronized (postlock) {
				getdv_pstmt.clearParameters();
				getdv_pstmt.setString(1, userID);
				ResultSet rs = getdv_pstmt.executeQuery();
					
				if(rs.next()) {
					int mb_no = rs.getInt(Meta_DB.col_mbKey);
					int dv_no = rs.getInt(Meta_DB.col_dvKey);
						
					incomm_pstmt.clearParameters();
					incomm_pstmt.setDate(1, dt.getDate());
					incomm_pstmt.setTime(2, dt.getTime());
					incomm_pstmt.setInt(3, mb_no);
					incomm_pstmt.setInt(4, dv_no);
					incomm_pstmt.setInt(5, act);
					incomm_pstmt.executeUpdate();
				}
			}
		}
		catch(Exception e) { }
		
		RequestDispatcher rd = request.getRequestDispatcher(Meta_Page.REMOTEPAGE);
		rd.forward(request, response);
		
		
	}

}
