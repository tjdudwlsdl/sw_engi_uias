<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*" %>     <!-- JSP에서 JDBC의 객체를 사용하기 위해 java.sql 패키지를 import 한다 -->
<%@ page import = "java.io.InputStreamReader" %>
<%@   page import = "java.net.URL" %>
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

<%
response.setContentType("text/html; charset=utf-8");
request.setCharacterEncoding("UTF-8");

String id = request.getParameter("id");
String password = request.getParameter("password");
if(id != null) session.setAttribute("id", id);
if(password != null) session.setAttribute("password", password);

JSONArray arr=null;
String code = "";
String value = null;
String code_value = request.getParameter("code_value");
if(code_value==null) {
   arr = LocationUtil.getTopLocationList();
} else {
   String[] cv = code_value.split("_");
   code = cv[0];
   value = cv[1];
   
   if(code.length()==2) {
      arr = LocationUtil.getMdlLocationList(code);
   } else if(code.length()==5){
      arr = LocationUtil.getLeafLocationList(code);
   } else {
      session.setAttribute("localcode", code);
      session.setAttribute("location", value);
      response.sendRedirect(String.format("%s://%s:%d/ieas%s",
         request.getScheme(), request.getServerName(), request.getServerPort(),
         "/SignupHandler"));
      return;
   }
}
%>
<div class="navbar navbar-inverse navbar-fixed-top">
          <a class="navbar-brand" href="#">Ajou Univ SWE Project</a>    
</div>
<div class="container">
</div>
<section id="buttons" style="margin-top: 70px; text-align: center;">

   <div class="page-header"><h4>Select location</h4></div>

<form method="post" action="/ieas/localselect.jsp" accept-charset="UTF-8">
   
   <select name="code_value">
      <%
      JSONObject obj;
      String c,v;
      for(int i=0; i<arr.size(); i++) {
         obj = (JSONObject)arr.get(i); 
         c = obj.get("code").toString();
         v = obj.get("value").toString();%>
      <option value="<%=c+"_"+(value==null?v:value+" "+v)%>"><%=v%> 
      <% } %>
   </select>
   <input type="submit">
</form>
</section>
</body>
</html>