<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*" %>     <!-- JSP에서 JDBC의 객체를 사용하기 위해 java.sql 패키지를 import 한다 -->


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

request.getRequestDispatcher("/EnvDataGetter").include(request, response);
String location = (String)request.getAttribute("location");
String condition = (String)request.getAttribute("condition");
String temperature = (String)request.getAttribute("temperature");
String humidity = (String)request.getAttribute("humidity");
String co2 = (String)request.getAttribute("co2");
String state = (String)request.getAttribute("state");
%>
<!-- 상단 네비게이션 바 -->
<div class="navbar navbar-inverse navbar-fixed-top">
          <a class="navbar-brand" href="#">Ajou Univ SWE Project</a>    
</div>
<div class="container">
</div>
<section id="buttons" style="margin-top: 70px; text-align: center; margin-bottom:150px">
	<div class="page-header"><h4>Checking Information</h4></div>
	<div id="info_table" style=" padding-left:10px; text-align:left;">
	<p>Registered location: <%=location%></p>
	<p>Weather condition: <%=condition%></p>
	<p>Temperature: <%=temperature%> ℃</p>
	<p>Humidity: <%=humidity%> %</p>
	<p>CO2 density: <%=co2%> ppm</p>
	<p>Window state: <%=state%></p>
	</div>
</section>
</body>
<footer>
<button class="btn btn-primary btn-lg" onclick="location.href='main.jsp'" 
	style="margin-left:10px;">Back</button>
</footer>
</html>