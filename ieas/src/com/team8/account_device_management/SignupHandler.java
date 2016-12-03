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

import com.team8.LocationUtil;
import com.team8.Meta_DB;
import com.team8.SecurityUtil;

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
    				"INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?,?,m?,?,?)",
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
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userID = request.getParameter("userID");		// mb_id
		String password = request.getParameter("password");
		String salt = SecurityUtil.createSalt(); 	// mb_salt
		String hashedpswd = SecurityUtil.encryptSHA256(password+salt);	//mb_password
		
		String location = request.getParameter("location");	// mb_location
		String[] locals = location.split(" ");
		String localCode = LocationUtil.getLocationCode(locals[0], locals[1], locals[2]);	// mb_localcode
		
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
	}

}
