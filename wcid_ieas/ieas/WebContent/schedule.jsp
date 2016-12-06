<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*" %>     <!-- JSP에서 JDBC의 객체를 사용하기 위해 java.sql 패키지를 import 한다 -->
<%@ page import = "java.io.InputStreamReader" %>
<%@	page import = "java.net.URL" %>
<%@ page import = "java.util.*" %>
<%@ page import = "org.json.simple.JSONArray" %>
<%@ page import = "org.json.simple.JSONObject"%>
<%@ page import = "org.json.simple.JSONValue" %>

<%@ page import = "auto_control_management.Reservation" %>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  <!-- jstl 사용 -->

<!DOCTYPE html>
<html>
<head>
	<title>SW-Project</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="project.css">
	<link href="bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
    <script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    
	
    
</head>
<body>
<%
String userID = (String)session.getAttribute("Logon.isDone");
if(userID==null) {
	response.sendRedirect(String.format("%s://%s:%d/ieas%s", 
			request.getScheme(), request.getServerName(), 
			request.getServerPort(), "/login.jsp"));
	return;
}

request.getRequestDispatcher("/ReservationManager").include(request, response);
Reservation[] list = (Reservation[])request.getAttribute("list");

%>
<!-- 상단 네비게이션 바 -->
<div class="navbar navbar-inverse navbar-fixed-top">
          <a class="navbar-brand" href="#">Ajou Univ SWE Project</a>    
</div>
<div class="container">
</div>
	
<section id="buttons" style="margin-top: 70px; text-align: center;">
	<div class="page-header"><h4>Check Schedule</h4></div>	
</section>

<table class="table table-bodered table-hover" style="width:100%; overflow:auto;">

<% 
if (list==null || list.length == 0) {
%> <tr><td>
	<h4>no data</h4></td></tr>
<%
} 
	String oper = null;
	for(int i=0;i<list.length;i++) { 
		if(list[i].getAct()==0)
			oper = "Close";
		else 
			oper = "Open";
			%>
		<tr style="vertical-align:middle;">
		<td><h4>
		<%=list[i].getDate().toString() %>
		</h4></td>
		<td><h4>
		<%=list[i].getTime().toString() %>
		</h4></td>
		<td><h4>
		<%=oper %>
		</h4></td>
			<td>
		 <form method="post" action="/ieas/Reserve" style="vertical-align:middle; text-align:right; margin-right:10px;">
		 	<input type="hidden" name="command" value="cancel">
		 	<input type="hidden" name="id" value="<%=String.valueOf(list[i].getID())%>">
		 	<input type="submit" class="btn btn-danger btn-sm" value="Cancel" style="float:right; margin-right:10px; vertical-align:0px;">
		 </form>
		 </td>
		 </tr>
	<%}%>
</table>



</body>
<footer>
<button class="btn btn-primary btn-lg" onclick="location.href='main.jsp'" 
	style="margin-left:10px;">Back</button>
</footer>
</html>