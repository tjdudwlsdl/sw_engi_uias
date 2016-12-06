package account_device_management;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util_core.Meta_Page;

/**
 * Servlet implementation class LogoutHandler
 * 로그아웃을 수행합니다.
 */
@WebServlet("/LogoutHandler")
public class LogoutHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutHandler() {
        super();
        // TODO Auto-generated constructor stub
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

    	session.invalidate();
    	response.sendRedirect(String.format("%s://%s:%d/ieas%s", 
				request.getScheme(), request.getServerName(), 
				request.getServerPort(), Meta_Page.LOGINPAGE));
	}

}
