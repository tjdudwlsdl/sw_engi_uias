<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*" %>     <!-- JSP에서 JDBC의 객체를 사용하기 위해 java.sql 패키지를 import 한다 -->
<%@ page import = "java.io.InputStreamReader" %>
<%@	page import = "java.net.URL" %>
<%@ page import = "java.util.*" %>
<%@ page import = "org.json.simple.JSONArray" %>
<%@ page import = "org.json.simple.JSONObject"%>
<%@ page import = "org.json.simple.JSONValue" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  <!-- jstl 사용 -->

<!-- WEB-INF/classes/util_core/ 폴더에 *.class 파일 이동 -->
<%@ page import = "util_core.LocationUtil" %>

<!-- classes 하위 폴더의 이름은 사용하려는 패키지 이름이어야 한다.
	임포트 하는 클래스에도 패키지 이름을 넣어야 한다.
 -->

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

	<div class="page-header"><h4>Membership Registration</h4></div>
	<div class="loginForm">
 		<form  method="post" action="/ieas/localselect.jsp">
  			<div class="form-group">
		    	<label for="exampleInputEmail1" style="margin-left:10px;">ID</label>
    			<input type="text" class="form-control" id="exampleInputEmail1" placeholder="Enter new id" name="id">
  			</div>
  			<div class="form-group">
    			<label for="exampleInputPassword1" style="margin-left:10px;">Password</label>
    			<input type="password" class="form-control" id="exampleInputPassword1" placeholder="Enter new password" name="password"> 		
  			</div>
  			<section id="buttons" style="margin-top: 50px; margin-bottom: 130px; text-align: center;">
  				<div class="form-group">
    				<div class="col-sm-offset-2 col-sm-10">
      					<input type="submit" class="btn btn-default btn-lg" value="Next" style="vertical-align:center;">
      					
				    </div>
  				</div>
  			</section>	
  		</form>
    </div>
</section>
</body>

</html>