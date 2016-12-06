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
 
<!-- 상단 네비게이션 바 -->
<div class="navbar navbar-inverse navbar-fixed-top">
          <a class="navbar-brand" href="#">Ajou Univ SWE Project</a>    
</div>
<div class="container">
</div>
<section id="buttons" style="margin-top: 70px; text-align: center;">
	<div class="page-header"><h4>Reservation</h4></div>
	<h4 style="margin-bottom:200px;"> Your reservation has been completed.</h4>
</section>
</body>
<footer>
<section id="buttons" style="text-align: center;">
<button class="btn btn-primary btn-lg" onclick="location.href='main.jsp'" 
	style="margin-left:10px; margin-bottom:10px;">Back to main menu</button>
<button class="btn btn-primary btn-lg" onclick="location.href='reserve.jsp'" 
	style="margin-left:10px;">Back to reservation page</button>	
</section>
</footer>
</html>