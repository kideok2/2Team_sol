<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
<!-- 배민 폰트 -->
<link href="style.css" rel="stylesheet" type="text/css">

<meta charset="UTF-8">
<title>마이 페이지</title>
</head>
<body>
<div id="wrapper" style="position: absolute; width: 100%; height: auto;"><!-- 전체 div 시작 -->
<div>
<jsp:include page="header_bar.jsp" />
</div>
<div>
<jsp:include page="menu_bar.jsp" />
</div>
			<div id="myPro" style="width: 100%; height:1200px; clear:both;">	
<div>
	<jsp:include page="side_bar.jsp" />
</div>
			<!-- content 영역 시작 -->
			<div style="float:left; width:80%; height:1200px; background-color:yellow;">
			<table>
			<tr><th>예약</th></tr>
				<tr>
					<td>예약 번호</td>
					<td>업체명</td>
					<td>예약 일자</td>
					<td>주문 금액</td>
					<td>기타</td>
					<td>결제 일자</td>
					<td>1대1 문의</td>
					<td>취소</td>
				</tr>
				<c:forEach var="bdto" items="${bdto}">
					<tr> 
						<td>${bdto.no}</td>
						<td>${bdto.res_no}</td>
				  		<td>${bdto.mem_no}</td>
				  		<td>${bdto.date1}</td>
					</tr>
 				</c:forEach>
			</table>
			</div>
			<!-- content 영역 끝 -->
	</div><!-- 첫 번째 큰 단락 끝 -->	<!--두 번째 큰 단락 끝 -->
	</div><!-- 전체 div 끝 -->

</body>
</html>