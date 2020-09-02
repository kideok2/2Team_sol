<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.rawgit.com/moonspam/NanumSquare/master/nanumsquare.css">
<link href="../resources/css/custom/index/base.css" rel="stylesheet" type="text/css" />
<link href="../resources/css/custom/index/common.css" rel="stylesheet" type="text/css" />
<link href="../resources/css/custom/index/index.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="../resources/css/custom/sub1/sub1.css">

<script src="../resources/js/common.js"></script>

<style type="text/css">

 .slide_wrap { position: relative; width: 900px; margin: auto; padding-bottom: 30px; } 
 .slide_box { width: 100%; margin: auto; overflow-x: hidden; } 
 .slide_content { display: table; float: left; width: 300px; height: 400px; }
 .slide_list div p {background-color: red;} 
</style>

<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=62d3ab0d1faddf540c257e322ccce48e&libraries=services,clusterer,drawing"></script>

<script type="text/javascript" >
window.onload = function(){
	const slideList = document.querySelector('.slide_list'); // Slide parent dom
	const slideContents = document.querySelectorAll('.slide_content'); // each slide dom
	const slideBtnNext = document.querySelector('.slide_btn_next'); // next button
	const slideBtnPrev = document.querySelector('.slide_btn_prev'); // prev button
	const slideLen = slideContents.length; // slide length
	const slideWidth = 300; // slide width
	const slideSpeed = 300; // slide speed
	const startNum = 0; // initial slide index (0 ~ 4)
	slideList.style.width = slideWidth * (slideLen + 2) + "px";
	// Copy first and last slide
	let firstChild = slideList.firstElementChild;
	let lastChild = slideList.lastElementChild;
	let clonedFirst = firstChild.cloneNode(true);
	let clonedLast = lastChild.cloneNode(true);
	// Add copied Slides
	slideList.appendChild(clonedFirst);
	slideList.insertBefore(clonedLast, slideList.firstElementChild);
	slideList.style.transform = "translate3d(-" + (slideWidth * (startNum + 1)) + "px, 0px, 0px)";
	let curIndex = startNum; // current slide index (except copied slide)
	let curSlide = slideContents[curIndex]; // current slide dom
	curSlide.classList.add('slide_active');
	
	/** Next Button Event */
	slideBtnNext.addEventListener('click', function() {
		// 1, 2, 3 -> 0,1,2
	if (curIndex <= slideLen - 3) {
	slideList.style.transition = slideSpeed + "ms";
	slideList.style.transform = "translate3d(-" + (slideWidth * (curIndex + 2)) + "px, 0px, 0px)";
	}
		// 4면
	if (curIndex === slideLen - 2) {
	setTimeout(function() {
	slideList.style.transition = "0ms";
	slideList.style.transform = "translate3d(-" + slideWidth + "px, 0px, 0px)";
	}, slideSpeed);
	curIndex = -1;
	}
	curSlide.classList.remove('slide_active');
	curSlide = slideContents[++curIndex];
	curSlide.classList.add('slide_active');
	});
	/** Prev Button Event */
	slideBtnPrev.addEventListener('click', function() {
		if (curIndex === 0) {
		curIndex = slideLen-1;
		}
		if (curIndex >= 0) {
		slideList.style.transition = slideSpeed + "ms";
		slideList.style.transform = "translate3d(-" + (slideWidth * curIndex) + "px, 0px, 0px)";
		}
	curSlide.classList.remove('slide_active');
	curSlide = slideContents[curIndex];
	curSlide.classList.add('slide_active');
	--curIndex;
	});
	
}

$(document).ready(function(){// 문서전체가 로딩되면 실행. 그래야 문서에 있는 요소들을 지정해서 가져올 수 있음.
//문서가 로딩 되지 않은 상태에서 #id 를 하면 아직 해당 id가 생성되지 않아 읽어올 수가 없다.
	
	
});


// 음식점 리스트 밑 페이징 눌렀을 때 form 전송 
function paging_form(currentPage,currPageBlock){

	$('#paging_form [name = "currentPage"]').val(currentPage);
	$('#paging_form [name = "currPageBlock"]').val(currPageBlock);
	
	var keyword = $('#keyword1').val();
	var category = $('#category1').val();
	
	var url = "/custom/sub1?keyword="+keyword+"&category="+category;
	$('#paging_form').attr("action",url);
	$('#paging_form').submit();
}



</script>

</head>
<body id="main">

<div id="wrap">

	<!-- top 영역 시작-->
	<c:import url="top.jsp"/>
	<!-- top 영역 끝 -->
		
	<form action="#" id="paging_form" method="post">
		<input type="text" name="currentPage" value="${pdto.currentPage}" />
		<input type="text" name="currPageBlock" value="${pdto.currPageBlock}" />
		<input type="text" id="keyword1" value="${keyword}"/>
		<input type="text" id="category1" value="${category}"/>
	</form>
	
	<div id="content-wrapper">
		<div id="content" >
		
			<!--  1번째 줄 새로시작 -->
			<div id="list2" style="width: auto; height: 350px; text-align: center;">
			
				<div id="list2(0)" style="width:100%; height:100%;">
			
					<div id="list" style=" float:left; height :10%; width:25%;">카테고리별 음식점 리스트</div>
					
					<!-- 지도 wrap-->
					<div class="map_wrap">
						<!-- 지도 영역  --> 
						<div id="map" style="float:right; width:74.8%;height:100%;position:relative;overflow:hidden;">
						 	<ul id="category">
					       	 	<li id="c_entire" > 
					            <!-- <span class="category_bg bank"></span>  -->
					            	전체
					         	</li>       
					         	<li id="c_korean" > 
					           <!--  <span class="category_bg mart"></span>  -->
					            	한식
					         	</li>  
					         	<li id="c_chinese" > 
					           <!--  <span class="category_bg pharmacy"></span>  -->
					            	중식
					         	</li>  
					         	<li id="c_japanese" > 
					           <!--  <span class="category_bg oil"></span>  -->
					            	일식
					         	</li>  
					         	<li id="c_american" > 
					           <!--  <span class="category_bg oil"></span>  -->
					            	양식
					         	</li>  
					         	<li id="c_cafe" > 
 					           <!--  <span class="category_bg cafe"></span>  -->
					            	카페
					         	</li>     
					     	</ul>
						</div>
						
						<!-- 지도 음식점 리스트 영역 -->
						<div id="list2-1" style="float: left; display: inline; height: 89.7%; width: 25%;">
							
							<ul>
								<c:forEach var="resdto" items="${reslist}">
									<li>
										<div id="store${resdto.no}">
											<span class="alphabet"></span>
											<a id="res_name" href="#" data-no="${resdto.no}">${resdto.name}</a>
											
											<input type="hidden" class="res_address" value="${resdto.address1}" />
											<input type="hidden" class="res_c_no" value="${resdto.c_no}" />
										</div>
									 
									</li>
								</c:forEach>
							</ul>

							
							<div class="res_paging">

								
								<c:if test="${pdto.startPage > pdto.pageBlock}">
         							<a href="javascript:void(0);" onclick="javascript:paging_form(${pdto.startPage - pdto.pageBlock},${pdto.currPageBlock-1});" class="btn_prev">
         								<span class="ico_comm ico_prev"></span>
         							</a>
         						</c:if>
         			
         						<c:forEach var = "i" begin="${pdto.startPage}" end="${pdto.endPage}">
         			
         							<a href="javascript:void(0);"  onclick="javascript:paging_form(${i},${pdto.currPageBlock});" class="link_page">
         								<c:if test="${pdto.currentPage == i}"> 
         									<em><c:out value="${i}"/></em>
         								</c:if>
         								<c:if test="${pdto.currentPage != i}">
         									<c:out value="${i}"/>
         								</c:if>
         							</a>
         						</c:forEach>
         			
         						<c:if test="${pdto.endPage < pdto.allPage}">
         							<a href="javascript:void(0);" onclick="javascript:paging_form(${pdto.endPage+1},${pdto.currPageBlock+1});"  class="btn_next">
         								<span class="ico_comm ico_next"></span>
         							</a>
         						</c:if>
         					</div> <!-- review-paging end -->
					    
					    </div>
					
					</div>
				</div>
			<!-- 카드 형태로 3개만 구현하고 나머지는 슬라이드 -->
			</div>


			<!--  2번째 줄 새로시작 -->
				
			<div id ="restart"style="width: auto; height: 350px; " >
				<div id="recom">
					<div style="height: 10%; text-align: center;">선택된 음식점 </div>
				</div>
				
				<div id="list2-2" style="float: left; display: inline; height: 90%; width: 40%; "> 					
					<a href="http://duckbap.com/detail?res_no=${resdto.get(0).no}" target='_blank'>
						<img src="../resources/image/custom/sub1/don200.jpg" style="width: 100%; height: 100%; vertical-align: middle;"  >
					</a><!-- target='_blank' 새창띄우기 -->
				</div>
				
				<div id="list2-3" 	style="float: right; display: inline; height: 90%; width: 59.8%; text-align:left;">
					<p id="selected_name">음식점 : <span>  </span></p>
					<p id="selected_address">주소 : <span>  </span></p>
					<p id="selected_tel">연락처 : <span> </span></p>
					<p id="selected_hour">운영시간 : <span> </span></p> 
					<button>상세보기</button>
				</div> 		<!-- list2-3 끝 -->			

		
			<!--  2번째 줄 끝 -->		
			</div>	
		
		<!-- 3번째 줄 시작  -->				
			<div id="content-wrap">
						<div class="slide_wrap">
							<div class="slide_box">
								<div class="slide_list clearfix" style="text-align: center;">
									<div class="slide_content slide01">
										<div style="float: left; width: 100%; height: 80%">
											<img src="../resources/image/custom/sub1/han300.jpg">
										</div>
									<div style="float: left; width: 100%; height: 20%"> 
										<%-- <p>${cdto.get(0).name}</p> --%>
									</div>
									</div>
					<!-- 한식 글귀(제목)-->
					<div class="slide_content slide02" >
						<div style="float: left; width: 100%; height: 80%">
							<img src="../resources/image/custom/sub1/han300.jpg">
						</div>
							<div style="float: left; width: 100%; height: 20%">
								<%-- <p>${cdto.get(1).name}</p> --%>
							
							</div>
						</div>
						<div class="slide_content slide03">
							<div style="float: left; width: 100%; height: 80%">
								<img src="../resources/image/custom/sub1/han300.jpg">
							</div>
							<div style="float: left; width: 100%; height: 20%">분식</div>

						</div>

						<div class="slide_content slide04">
						<div style="float: left; width: 100%; height: 80%">
							<img src="../resources/image/custom/sub1/han300.jpg">
						</div>
						<div style="float: left; width: 100%; height: 20%">좝</div>

						</div>
					
					
						<div class="slide_content slide05">
						<div style="float: left; width: 100%; height: 80%">
							<img src="../resources/image/custom/sub1/han300.jpg">
						</div>
						<div style="float: left; width: 100%; height: 20%">좝</div>
						</div>

					</div>
					<!-- // .slide_list -->
					</div>
					<!-- // .slide_box -->
				<div class="slide_btn_box" style="text-align: center;">
				<button type="button" class="slide_btn_prev">이전</button>
				<button type="button" class="slide_btn_next">다음</button>
				</div>
					
				<!-- // .slide_wrap -->
					<!-- // .container -->
					
				<!-- // .slide_btn_box -->
				<ul class="slide_pagination"></ul>
				
				</div>
				
				<!-- 3번째줄 끝 -->
				</div>
				
				
				<!-- // .slide_pagination -->
				<ul class="slide_pagination"></ul>
				</div>
				<!-- 	content 끝 -->
			</div>
			<!-- // .slide_list -->
	
	<!-- container 끝 -->

	</div>
		<c:import url="bottom.jsp"/>
<script src="../resources/js/custom/sub1/sub1.js"></script>

</body>
</html>