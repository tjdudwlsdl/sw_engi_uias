package com.team8;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUtils;

/**
 * Servlet implementation class ProtectedServlet
 * 보호되는 리소스의 추상클래스 입니다.
 */
public abstract class ProtectedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProtectedServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private boolean isLogonSession(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    	HttpSession session = request.getSession(true);
    	
    	Object done = session.getAttribute("Logon.isDone");
    	if(done==null) {
    		session.setAttribute("login.target", request.getRequestURL().toString());
    		response.sendRedirect(String.format("%s://%s:%d%s", 
    				request.getScheme(), request.getServerName(), request.getServerPort(), Meta_Page.LOGINPAGE));
    		return false;
    	}
    	return true;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(isLogonSession(request, response))
			doGetProc(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(isLogonSession(request, response))
			doPostProc(request, response);
	}

	protected abstract void doGetProc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	protected abstract void doPostProc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
