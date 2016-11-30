package com.team8.account_device_management;

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

import com.team8.Meta_DB;

/**
 * Servlet implementation class SignupHandler
 * 유저 등록을 수행합니다.
 * !! 미완성 !!
 */
@WebServlet("/SignupHandler")
public class SignupHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
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
    	ServletContext sc = getServletContext();
    	try {
    		Class.forName(sc.getInitParameter("driver"));
    		conn = DriverManager.getConnection(
    				sc.getInitParameter("dbURL"), 
    				sc.getInitParameter("dbUser"), 
    				sc.getInitParameter("dbPassword"));
    		pstmt = conn.prepareStatement(String.format(
    				"INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,?)",
    				Meta_DB.tb_member, Meta_DB.col_mbID, Meta_DB.col_mbHashPasswd,
    				Meta_DB.col_mbLocation, Meta_DB.col_mbLocationCode));
    		
    	}
    	catch(ClassNotFoundException e) {
    		throw new UnavailableException("Couldn't load database driver");
    	}
    	catch(SQLException e){
    		throw new UnavailableException("Couldn't get db connection");
    	}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userID = request.getParameter("userID");
		String password = request.getParameter("password");
		String location = request.getParameter("location");
		
		
	}

}
