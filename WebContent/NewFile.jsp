<%@ page
language="java"
contentType="text/html; charset=utf-8"
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
    <script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</head>
<body>
<!-- 상단 네비게이션 바 -->
<div class="navbar navbar-inverse navbar-fixed-top">
          <div class="container">
            <div class="navbar-header">
              <!-- 브라우저가 좁아졋을때 나오는 버튼(클릭시 메뉴출력) -->
              <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
              </button>
              <a class="navbar-brand" href="#">Ajou Univ Data Mining Project</a>
            </div>
            <div class="collapse navbar-collapse">
              <ul class="nav navbar-nav">
                <!-- <li class="active"><a href="#">홈으로</a></li>
                <li><a href="#about">부트스트랩이란</a></li>
                <li><a href="#contact">문의하기</a></li> -->
              </ul>
            </div>
          </div>
</div>
<div class="container">
          <div style="margin-top: 100px;">
            <h1><center>감정 사전 만들기</center></h1>
            <p class="lead"><center>아래 보이는 단어에 대해 참가자 분이 느끼시는 감정을 선택해 주세요</center></p>
          </div>
</div>
<div id="word">
    <p><center>단어 자리</center></p>
</div>

<div id="selection">
    <center>
    <button class="btn btn-primary">긍정!</button>
    <button class="btn btn-danger">부정!</button>
    <button class="btn btn-warning">의미없음</button>
    </center>
</div>
</body>
</html>