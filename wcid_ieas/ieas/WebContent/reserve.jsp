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
    
    <!----- 1. 아래의 스크립트를 HEAD 부분에 붙여 넣으세요 -------->

<SCRIPT LANGUAGE="JavaScript">

Now = new Date();
NowDay = Now.getDate();
NowMonth = Now.getMonth();
NowYear = Now.getYear();
NowHour = Now.getHours();
NowMinute = Now.getMinutes();

if (NowYear < 2000) NowYear += 1900; 
function DaysInMonth(WhichMonth, WhichYear)
{
  var DaysInMonth = 31;
  if (WhichMonth == "Apr" || WhichMonth == "Jun" || WhichMonth == "Sep" || WhichMonth == "Nov") DaysInMonth = 30;
  if (WhichMonth == "Feb" && (WhichYear/4) != Math.floor(WhichYear/4))        DaysInMonth = 28;
  if (WhichMonth == "Feb" && (WhichYear/4) == Math.floor(WhichYear/4))        DaysInMonth = 29;
  return DaysInMonth;
}

function ChangeOptionDays(Which)
{
  DaysObject = eval("document.Form1." + Which + "Day");
  MonthObject = eval("document.Form1." + Which + "Month");
  YearObject = eval("document.Form1." + Which + "Year");
  HourObject = eval("document.Form1."+Which+"Hour");
  MinuteObject = eval("document.Form1."+Which+"Minute");
  
  Month = MonthObject[MonthObject.selectedIndex].text;
  Year = YearObject[YearObject.selectedIndex].text;

  DaysForThisSelection = DaysInMonth(Month, Year);
  CurrentDaysInSelection = DaysObject.length;
  
  if (CurrentDaysInSelection > DaysForThisSelection)
  {
    for (i=0; i<(CurrentDaysInSelection-DaysForThisSelection); i++)
    {
      DaysObject.options[DaysObject.options.length - 1] = null
    }
  }
  if (DaysForThisSelection > CurrentDaysInSelection)
  {
    for (i=0; i<(DaysForThisSelection-CurrentDaysInSelection); i++)
    {
      NewOption = new Option(DaysObject.options.length + 1);
      DaysObject.add(NewOption);
    }
  }
    if (DaysObject.selectedIndex < 0) DaysObject.selectedIndex == 0;
}

function SetToToday(Which)
{
  DaysObject = eval("document.Form1." + Which + "Day");
  MonthObject = eval("document.Form1." + Which + "Month");
  YearObject = eval("document.Form1." + Which + "Year");

  YearObject[0].selected = true;
  MonthObject[NowMonth].selected = true;

  ChangeOptionDays(Which);

  DaysObject[NowDay-1].selected = true;
}

function WriteYearOptions(YearsAhead)
{
  line = "";
  for (i=0; i<YearsAhead; i++)
  {
    line += "<OPTION>";
    line += NowYear + i;
  }
  return line;
}

</script>
</head>
<body onLoad="SetToToday('FirstSelect');">

<%! String window_state= "open"; %>
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

	<div class="page-header"><h4>Reservation</h4></div>


<div>
<p style="word-spacing:13px;">Year Month Day Hour Minute</p>
</div>

	
	<FORM name="Form1" method="post" action="/ieas/Reserve">
		<input type="hidden" name="command" value="reserve">
	
<SELECT name="FirstSelectYear" onchange="ChangeOptionDays('FirstSelect')">
        <SCRIPT language="JavaScript">
                document.write(WriteYearOptions(50));
        </SCRIPT>
</SELECT>


<SELECT name="FirstSelectMonth" onchange="ChangeOptionDays('FirstSelect')" >
<!------ 필요하면 아래 월 표시를 한글로 바꾸어 주세요 ------->
        <OPTION>Jan
        <OPTION>Feb
        <OPTION>Mar
        <OPTION>Apr
        <OPTION>May
        <OPTION>Jun
        <OPTION>Jul
        <OPTION>Aug
        <OPTION>Sep
        <OPTION>Oct
        <OPTION>Nov
        <OPTION>Dec
</SELECT>
<SELECT name="FirstSelectDay">
        <OPTION>1
        <OPTION>2
        <OPTION>3
        <OPTION>4
        <OPTION>5
        <OPTION>6
        <OPTION>7
        <OPTION>8
        <OPTION>9
        <OPTION>10
        <OPTION>11
        <OPTION>12
        <OPTION>13
        <OPTION>14
        <OPTION>15
        <OPTION>16
        <OPTION>17
        <OPTION>18
        <OPTION>19
        <OPTION>20
        <OPTION>21
        <OPTION>22
        <OPTION>23
        <OPTION>24
        <OPTION>25
        <OPTION>26
        <OPTION>27
        <OPTION>28
        <OPTION>29
        <OPTION>30
        <OPTION>31
</SELECT>
<SELECT name="FirstSelectHour">
        <OPTION>1
        <OPTION>2
        <OPTION>3
        <OPTION>4
        <OPTION>5
        <OPTION>6
        <OPTION>7
        <OPTION>8
        <OPTION>9
        <OPTION>10
        <OPTION>11
        <OPTION>12
        <OPTION>13
        <OPTION>14
        <OPTION>15
        <OPTION>16
        <OPTION>17
        <OPTION>18
        <OPTION>19
        <OPTION>20
        <OPTION>21
        <OPTION>22
        <OPTION>23
        </select>
<SELECT name="FirstSelectMinute">
		<OPTION>0
        <OPTION>1
        <OPTION>2
        <OPTION>3
        <OPTION>4
        <OPTION>5
        <OPTION>6
        <OPTION>7
        <OPTION>8
        <OPTION>9
        <OPTION>10
        <OPTION>11
        <OPTION>12
        <OPTION>13
        <OPTION>14
        <OPTION>15
        <OPTION>16
        <OPTION>17
        <OPTION>18
        <OPTION>19
        <OPTION>20
        <OPTION>21
        <OPTION>22
        <OPTION>23
        <OPTION>24
        <OPTION>25
        <OPTION>26
        <OPTION>27
        <OPTION>28
        <OPTION>29
        <OPTION>30
        <OPTION>31
        <OPTION>32
        <OPTION>33
        <OPTION>34
        <OPTION>35
        <OPTION>36
        <OPTION>37
        <OPTION>38
        <OPTION>39
        <OPTION>40
        <OPTION>41
        <OPTION>42
        <OPTION>43
        <OPTION>44
        <OPTION>45
        <OPTION>46
        <OPTION>47
        <OPTION>48
        <OPTION>49
        <OPTION>50
        <OPTION>51
        <OPTION>52
        <OPTION>53
        <OPTION>54
        <OPTION>55
        <OPTION>56
        <OPTION>57
        <OPTION>58
        <OPTION>59
</select>

<div class="radio">
  <label>
    <input type="radio" name="act" value="100">Open
  </label>
</div>
<div class="radio">
  <label>
    <input type="radio" name="act" value="0">Close
  </label>
</div>	


<div style="margin-top: 50px; margin-bottom:200px;">
<input class="btn btn-lg btn-success" type="submit" value="Reserve">
</div>



</FORM>
	
</section>

<!-- Parameter names -->
<!-- String year=request.getParameter("FirstSelectYear");
	String month=request.getParameter("FirstSelectMonth");
	String day=request.getParameter("FirstSelectDay");
	String hour=requset.getParameter("FirstSelectHour");
	String minute=request.getParameter("FirstSelectMinute");
 -->
</body>
<footer>
<!-- <p style="text-align: right;">user id: AAA</p> -->
<button class="btn btn-primary btn-lg" onclick="location.href='main.jsp'" 
	style="margin-left:10px;">Back</button>
</footer>
</html>