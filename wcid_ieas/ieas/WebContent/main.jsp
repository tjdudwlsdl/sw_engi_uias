<%@ page language="java" contentType="text/html; charset=UTF-8"%>
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
%>
<!-- 상단 네비게이션 바 -->
<div class="navbar navbar-inverse navbar-fixed-top">
          <a class="navbar-brand" href="#">Ajou Univ SWE Project</a>    
</div>
<div class="container">
</div>
<section id="buttons" style="margin-top: 70px; text-align: center;">
	<div class="page-header"><h4>Indoor Environment Administration System</h4></div>
	<form method='post' action='/DoServlet' name='submenu'>
		<div><input class="btn btn-lg btn-block btn-primary" type='submit' value='Checking Information' 
		onclick='this.form.action="check.jsp"' 
		style="margin-bottom: 15px;"></div>
		<div><input class="btn btn-lg btn-block btn-primary" type='submit' value='Window Remote Control' 
		onclick='this.form.action="remote.jsp"' 
		style="margin-bottom: 15px;"></div>
		<div><input class="btn btn-lg btn-block btn-primary" type='submit' value='Reservation' 
		onclick='this.form.action="reserve.jsp"' 
		style="margin-bottom: 15px;"></div>
		<div><input class="btn btn-lg btn-block btn-primary" type='submit' value='Check Schedule' 
		onclick='this.form.action="schedule.jsp"' 
		style="margin-bottom: 15px;"></div>
		<div><input class="btn btn-lg btn-block btn-primary" type='submit' value='Auto Control Option' 
		onclick='this.form.action="option.jsp"' 
		style="margin-bottom: 15px;"></div>
		<div><input class="btn btn-lg btn-block btn-primary" type='submit' value='User Change' 
		onclick='this.form.action="change.jsp"' 
		style="margin-bottom: 15px;"></div>
	</form>
	</section>
</body>
<footer>
<!--<p style="text-align: left;">user id: AAA</p>-->
</footer>
</html>