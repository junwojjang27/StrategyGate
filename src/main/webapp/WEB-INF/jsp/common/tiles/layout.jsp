<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/tiles/common-params.jsp"%>
<!doctype html>
<html lang="ko">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>bizGATE BSC+</title>
<link rel="stylesheet" type="text/css" href="${css_path}/smoothness/jquery-ui.min.css"/>
<link rel="stylesheet" type="text/css" href="${css_path}/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" href="${css_path}/jquery.fancybox-1.3.4.css"/>
<link rel="stylesheet" type="text/css" href="${css_path}/theme/${theme}/common.css"/>
<%-- select검색 추가 --%>
<link rel="stylesheet" type="text/css" href="${css_path}/select2.min.css"/>
<script type="text/javascript" src="${js_path}/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${js_path}/jquery-ui.min.js"></script>
<script type="text/javascript" src="${js_path}/jquery.i18n.properties.min.js"></script>
<script type="text/javascript" src="${js_path}/i18n/grid.locale-${pageContext.response.locale}.js"></script>
<script type="text/javascript" src="${js_path}/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="${js_path}/jquery.cookie.js"></script>
<script type="text/javascript" src="${js_path}/common.js"></script>
<script type="text/javascript" src="${js_path}/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="${js_path}/Chart.bundle.min.js"></script>
<%-- select검색 추가 --%>
<script type="text/javascript" src="${js_path}/select2.min.js"></script>

<!--[if lte IE 8]>
<script type="text/javascript" src="${js_path}/html5.js"></script>
<![endif]-->
<script type="text/javascript">
<validator:javascript dynamicJavascript="false" staticJavascript="true"/>
// 검색조건용 전역 변수
setFindValue("findYear", "${findYear}");
setFindValue("findMon", "${findMon}");
setFindValue("findAnalCycle", "${findAnalCycle}");
setFindValue("findScDeptId", "${findScDeptId}");
setFindValue("findDeptId", "${findDeptId}");
setFindValue("findEvalDegreeId", "${findEvalDegreeId}");
$.cookie("theme", "${theme}");

var currentLocale = "${pageContext.response.locale}";
var contextPath = "${root}";
var imagePath = "${img_path}";
var monitoringPgmId = "<spring:eval expression="@globals.getProperty('MONITORING_PGM_ID')"></spring:eval>";
<c:set var="defaultPgmId" scope="request"><spring:eval expression="@globals.getProperty('DEFAULT_PGM_ID')"></spring:eval></c:set>
<c:set var="superAuthCodeId"><spring:eval expression="@globals.getProperty('Super.AuthCodeId')"></spring:eval></c:set>
<sec:authorize access="hasRole('${superAuthCodeId}')">
	<c:set var="defaultPgmId" scope="request"><spring:eval expression="@globals.getProperty('Super.MainPgmId')"></spring:eval></c:set>
</sec:authorize>
var defaultPgmId = "${defaultPgmId}";
var gPopNoticeList = [];
<c:forEach items="${popNoticeList}" var="popNotice">gPopNoticeList.push({"id":"${popNotice.id}", "width":"${popNotice.width}", "height":"${popNotice.height}"});</c:forEach>

$.i18n.properties({
	name:"Messages", 
	path:"${context_path}/properties/",
	language:"${pageContext.response.locale}",
	mode:"both",
	async:false,
	cache:true,
	callback:function() {
		i18nLoaded = true;
	}
});

function setSideMenu(pgmId) {
	<sec:authorize access="not hasRole('01')">
		if($("#menu_" + pgmId).closest("#menu_" + monitoringPgmId).length == 0) {
			$("#scDeptTree").hide();
			$("#gnb .depth2.menu03").addClass("moveUp");
		} else {
			$("#scDeptTree").show();
			$("#gnb .depth2.menu03").removeClass("moveUp");
		}
	</sec:authorize>
}

function popNotice(){
	openFancybox({
		"url" : "${context_path}/comPop/bsc/system/system/notice/popNoticeList.do",
		"data" : getFormData("layoutPopForm")
	});
}

function popQnA(){
	openFancybox({
		"url" : "${context_path}/comPop/system/system/board/boardList.do?boardId=QNA",
		"data" : getFormData("layoutPopForm")
	});
}

function popSignal(){
	$("#layoutPopForm input[name=findYear]").val(getFindValue("findYear"));
	openFancybox({
		"url" : "${context_path}/comPop/system/system/code/signalMng/signalMngList.do",
		"data" : getFormData("layoutPopForm")
	});
}

var time = 0;
var refreshIntervalId = window.setInterval(chkeckSessionTime,1000);
var maxInactiveInterval = Number("<c:out value="${pageContext.session.maxInactiveInterval}"></c:out>");
var sessionTime;

function chkeckSessionTime(){
      time++;
      sessionTime = maxInactiveInterval - time;
      var result = "";
      if(sessionTime <= 0){
            window.clearInterval(refreshIntervalId);
            $("#sessionTimeCheck").empty();
            result = "<spring:message code="errors.sessionExipred"/>";
            doLogout();
      }else{
            //console.log(sessionTime);
            result = parseInt(sessionTime/(60*60))+" : "+parseInt((sessionTime%(60*60))/60)+" : "+parseInt(sessionTime%60);
            $("#sessionTimeCheck").empty();
      }
      
      $("#sessionTimeCheck").text(result);
}  

function initSessionTime(){
	window.clearInterval(refreshIntervalId);
	refreshIntervalId = window.setInterval(chkeckSessionTime,1000);
	time=0;
}

</script>
</head>
<body>
<div id="wrap">
	<!-- 상단 시작 -->
	<div id="hd">
		<div>
			<h1 id="hd_h1"><spring:message code="bsc.title"/></h1>
			<div class="head">
				<div class="logo">
					<a href="#" onclick="loadPageByPgmId('MAIN');return false;"><img src="${img_path}/img-logo.png" alt="bizGATE BSC+"/></a>
				</div>
				<div class="country">
					<ul>
						<c:forEach items="${sessionScope.langList}" var="item" varStatus="status">
							<li><a href="#" onclick="changeLang('${item.lang}');return false;" <c:if test="${item.lang eq sessionScope.lang}">class="active"</c:if>>${item.langNm}</a></li>
						</c:forEach>
					</ul>
				</div>
				<div class="tnb">
					<ul>
						<li><span id="sessionTimeCheck"></span></li>
						<li><a href="#" class="fir" onclick="manualFileDownload();"><spring:message code="word.manual"/></a></li>
						<%--
						<li><a href="#" class="two"><spring:message code="word.systemWordDic"/></a></li>
						--%>
						<li><a href="#" class="mem-modify mr10"><img src="${img_path}/ico-tnb03.png" class="mr10" alt='<spring:message code="word.editInfo"/>'/>${sessionScope.loginVO.userNm}</a>
							<a href="#" class="mem-logout ml10" onclick="doLogout();return false;"><img src="${img_path}/ico-tnb04.png" alt="<spring:message code="button.logout"/>"/></a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<!-- 상단 끝 -->
	<!-- 콘텐츠 시작 -->
	<div id="container">
		<div class="containerCellL">
		<div id="gnb">
			<ul>
				<li><a href="#" class="gnb01" data-target-menu="menu01" onclick="showMenu(this);return false;"><spring:message code="button.allMenu"/></a></li>
				<li id="scDeptTree"><a href="#" class="gnb02" data-target-menu="menu02" onclick="showMenu(this);return false;"><spring:message code="word.deptDiagram"/></a></li>
				<li><a href="#" class="gnb03" data-target-menu="menu03" onclick="showMenu(this);return false;"><spring:message code="button.bookmark"/></a></li>
				<li>
				<div class="fniconbg">
					<div class="fnicon">
					<ul>
						<li><a href="#" onclick="popNotice();return false;" class="gnb04"><spring:message code="button.notice"/></a>
							<!-- <div class="menu04 depth2">
								<div class="menu" style="height: 944px;">
								</div>
							</div> -->
						</li>
					</ul>
					</div>
					<div class="fnicon">
					<ul>
						<li><a href="#" onclick="popQnA();return false;" class="gnb05"><spring:message code="button.qna"/></a>
							<!-- <div class="menu05 depth2">
								<div class="menu" style="height: 944px;">
								</div>
							</div> -->
						</li>
					</ul>
					</div>
					<div class="fnicon">
						<ul>
							<li><a href="#" onclick="popSignal();return false;" class="gnb06"><spring:message code="button.signal"/></a>
								<!-- <div class="menu06 depth2">
									<div class="menu" style="height: 944px;">
									</div>
								</div> -->
							</li>
						</ul>
					</div>
					
					<div class="fnicon">
						<ul>
							<li><a href="#" class="gnb07">흐름도</a>
								<div class="fwtable-bx">
									<div id="allProcess" class="fwtable-bx2">
										
										<%--
										<div class="fwtable-tbl">
											<table>
												<tbody>
													<tr>
														<th scope="rowgroup">조직성과평가</th>
																<td>
																	<div class="list">
																		<div class="cancel-bg">
																			<div class="cancel-icn">완료<br/>취소</div>
																			<p class="txt">관점 설정</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="cancel-bg">
																			<div class="cancel-icn">완료<br/>취소</div>
																			<p class="txt">전략목표 설정</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="cancel-bg">
																			<div class="cancel-icn">완료<br/>취소</div>
																			<p class="txt">지표등록 기한 설정</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">지표POOL 설정 및 배포</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">조직별 지표 설정 및 승인</p>
																		</div>
																	</div>
																	
																	<div class="arrow-down"></div>
																	
																	<div class="list">
																		<div class="bg-empty">
																		</div>
																		<div class="arrow-empty"></div>
																		<div class="bg-empty">
																		</div>
																		<div class="arrow-empty"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">주기 별 실적등록 및 승인, 마감</p>
																		</div>
																		<div class="arrow-left"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">실적등록 기한 설정</p>
																		</div>
																		<div class="arrow-left"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">지표마감</p>
																		</div>	
																	</div>
																	
																	<div class="arrow-down-empty"></div>
																	
																	<div class="list">
																		<div class="cancel-bg">
																			<div class="cancel-icn">완료<br/>취소</div>
																			<p class="txt">비계량 평가 차수 등록</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="cancel-bg">
																			<div class="cancel-icn">완료<br/>취소</div>
																			<p class="txt">평가단 및 평가자, 평가대상지표 설정</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">평가항목 설정</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">비계량지표 평가마감</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">평가결과산출/평가결과 확정</p>
																		</div>	
																	</div>
																	
																	<div class="arrow-down-empty"></div>

																	<div class="list">
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">기타평가항목 등록</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">기타평가항목별 점수 등록</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">평가종합 비율 및 결과산출방법 설정</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">최종조직점수 산출 및 마감</p>
																		</div>

																	</div>				
														</td>
													</tr>
												</tbody>
											</table>
										</div>
										
										<div class="fwtable-tbl">
											<table>
												<tbody>
													<tr>
														<th>개인업적평가</th>
																<td>
																	<div class="list">
																		<div class="cancel-bg">
																			<div class="cancel-icn">완료<br/>취소</div>
																			<p class="txt">평가차수 등록</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="cancel-bg">
																			<div class="cancel-icn">완료<br/>취소</div>
																			<p class="txt">평가그룹설정</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="cancel-bg">
																			<div class="cancel-icn">완료<br/>취소</div>
																			<p class="txt">평가대상자 및 평가자 설정</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">지표 등록 및 승인</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">실적마감</p>
																		</div>
																	</div>
																	
																	<div class="arrow-down"></div>
																	
																	<div class="list">
																		<div class="bg-empty">
																		</div>
																		<div class="arrow-empty"></div>
																		<div class="bg-empty">
																		</div>
																		<div class="arrow-empty"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">차수별 결과산출 및 확정</p>
																		</div>
																		<div class="arrow-left"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">업적평가 현황조회 및 마감</p>
																		</div>
																		<div class="arrow-left"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">자기평가 현황조회 및 마감</p>
																		</div>	
																	</div>
																	
																	<div class="arrow-down-empty"></div>
																	
																	<div class="list">
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">평가항목 설정</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">평가대상자 등록</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">업적평가 최종결과 산출 및 확정</p>
																		</div>
																	</div>
															</td>
													</tr>
												</tbody>
											</table>
										</div>
										
										<div class="fwtable-tbl">
											<table>
												<tbody>
													<tr>
														<th>개인역량평가</th>
																<td>
																	<div class="list">
																		<div class="cancel-bg">
																			<div class="cancel-icn">완료<br/>취소</div>
																			<p class="txt">평가일정 설정</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="cancel-bg">
																			<div class="cancel-icn">완료<br/>취소</div>
																			<p class="txt">역량구분 및 역량항목 등록</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="cancel-bg">
																			<div class="cancel-icn">완료<br/>취소</div>
																			<p class="txt">역량구분 및 역량항목 등록</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">평가대상자 설정</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">개인역량목표 등록 및 승인</p>
																		</div>
																	</div>
																	
																	<div class="arrow-down"></div>
																	
																	<div class="list">
																		<div class="bg-empty">
																		</div>
																		<div class="arrow-empty"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">평가결과 산출 및 확정</p>
																		</div>		
																		<div class="arrow-left"></div>																	
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">역량평가 및 마감</p>
																		</div>
																		<div class="arrow-left"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">평가자 설정</p>
																		</div>
																		<div class="arrow-left"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">평가자 설정</p>
																		</div>																
																	</div>
															</td>
													</tr>
												</tbody>
											</table>
										</div>
										
										<div class="fwtable-tbl">
											<table>
												<tbody>
													<tr>
														<th>평가종합</th>
																<td>
																	<div class="list">
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">기타평가항목 등록</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">평가군 설정</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">평가대상자 설정</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">조직점수 월 할 계산 점수 산출</p>
																		</div>
																		<div class="arrow-right"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">기타평가항목 점수 등록</p>
																		</div>
																	</div>
																	
																	<div class="arrow-down"></div>
																	
																	<div class="list">
																		<div class="bg-empty">
																		</div>
																		<div class="arrow-empty"></div>
																		<div class="bg-empty">
																		</div>
																		<div class="arrow-empty"></div>																
																		<div class="bg-empty">
																		</div>
																		<div class="arrow-empty"></div>
																		<div class="bg-empty">
																		</div>
																		<div class="arrow-empty"></div>
																		<div class="complete-bg">
																			<div class="complete-icn">완료</div>
																			<p class="txt">개인 최종결과 산출 및 확정</p>
																		</div>																
																	</div>
															</td>
													</tr>
												</tbody>
											</table>
										</div>
										--%>
									</div>
								</div>
							</li>
						</ul>
					</div>
					
				</div>
				</li>
				
				
				<%-- 
				<li><a href="#" onclick="popNotice();return false;" class="gnb04">공지/자료</a>
				</li>
				<li><a href="#" onclick="popQnA();return false;" class="gnb05">질문답변</a>
				</li>
				<li><a href="#" class="gnb06">신호등</a>
					<div class="menu06 depth2">
						<div class="menu">
						</div>
					</div>
				</li>
				--%>
			</ul>
			
			<div id="menu01" class="menu01 depth2">
				<div id="menu" class="menu">
					<p class="allmenu"><a href="#"><span><spring:message code="button.allMenu"/></span></a></p>
					<div class="am-bx">
						<div id="allMenu" class="am-bx2">
							
							<c:forEach items="${sessionScope.menuList}" var="item" begin="1" end="${fn:length(sessionScope.menuList)}" varStatus="status">
								<c:if test="${item.levelId eq '2'}">
									<c:set var="startIdx" value="${status.index + 1}"/>
									<c:set var="endIdx" value="${startIdx}"/>
									<c:set var="rowspan" value="0"/>
									<c:set var="breakYN" value="N"/>
									<c:forEach items="${sessionScope.menuList}" var="item2" begin="${startIdx}" end="${fn:length(sessionScope.menuList)}" varStatus="status2">
										<c:if test="${breakYN eq 'N'}">
											<c:choose>
												<c:when test="${item2.levelId eq '2'}">
													<c:set var="breakYN" value="Y"/>
												</c:when>
												<c:otherwise>
													<c:if test="${item2.levelId eq '3'}">
														<c:set var="rowspan" value="${rowspan + 1}"/>
													</c:if>
													<c:set var="endIdx" value="${status2.index}"/>
												</c:otherwise>
											</c:choose>
										</c:if>
									</c:forEach>
									
									<div class="am-tbl">
										<table>
											<tbody>
												<tr>
													<th scope="rowgroup" <c:if test="${rowspan > 1}">rowspan="${rowspan}"</c:if>>${item.pgmNm}</th>
													<c:forEach items="${sessionScope.menuList}" var="item2" begin="${startIdx}" end="${endIdx}" varStatus="status2">
														<c:choose>
															<c:when test="${item2.levelId eq '3'}">
																<c:if test="${not status2.first}">
																		</td>
																	</tr>
																	<tr>
																</c:if>
																<th scope="row">${item2.pgmNm}</th><td>
															</c:when>
															<c:when test="${item2.levelId eq '4'}">
																<a href="${root}/ispark.do#${item2.pgmId}"><span>${item2.pgmNm}</span></a>
															</c:when>
														</c:choose>
													</c:forEach>
													</td>
												</tr>
											</tbody>
										</table>
									</div>
								</c:if>
							</c:forEach>
						</div>
					</div>
					<c:set var="menuLevel" value="1"/>
					<c:forEach items="${sessionScope.menuList}" var="item" varStatus="status">
						<c:if test="${item.levelId > 1}">
							<c:choose>
								<c:when test="${item.levelId > menuLevel}">
									<ul class="menu${item.levelId}
										<c:choose>
											<c:when test="${item.levelId == 2}">
												menuLvl2
											</c:when>
											<c:when test="${item.levelId == 3}">
												menuLvl3
											</c:when>
											<c:when test="${item.levelId == 4}">
												menuLvl4
											</c:when>
										</c:choose>
										"
									>
								</c:when>
								<c:when test="${item.levelId < menuLevel}">
									<c:forEach begin="${item.levelId}" end="${menuLevel}" step="1" varStatus="status">
											</li>
										<c:if test="${not status.last}">
											</ul>
										</c:if>
									</c:forEach>
								</c:when>
								<c:otherwise>
									</li>
								</c:otherwise>
							</c:choose>
							
							<c:set var="menuLevel" value="${item.levelId}"/>
							<li id="menu_${item.pgmId}">
								<c:choose>
									<c:when test="${empty item.url}">
										<a href="${root}/ispark.do" onclick="return false;"><span>${item.pgmNm}</span></a>
									</c:when>
									<c:otherwise>
										<a href="${root}/ispark.do#${item.pgmId}"><span>${item.pgmNm}</span></a>
									</c:otherwise>
								</c:choose>
						</c:if>
					</c:forEach>
					<c:forEach begin="2" end="${menuLevel}" step="1" varStatus="status">
							</li>
						</ul>
					</c:forEach>
				</div>
			</div>
			
			<div id="menu02" class="menu02 depth2">
				<div class="oga-menu">
					<p class="title"><spring:message code="word.deptDiagram"/></p>
					<div id="scDeptList" class="scDeptList oga-bx ogam"><ul></ul></div>
					<div id="scDeptListOrg" class="hide"><ul></ul></div>
					<div id="deptList" class="scDeptList oga-bx hide"><ul></ul></div>
				</div>
			</div>
			
			<div id="menu03" class="menu03 depth2">
				<form:form id="bookmarkForm" name="bookmarkForm" method="post">
					<div id="bookmark" class="menu">
						<p class="favo"><a href="#" onclick="manageBookmark();return false;"><span><spring:message code="button.manageBookmark"/></span></a></p>
						<p id="favoBtns" class="favo-btn">
							<a href="#" onclick="deleteAllBookmarks();return false;"><spring:message code="button.deleteAll"/></a>
							<a href="#" onclick="deleteBookmark();return false;"><spring:message code="button.deleteSelected"/></a>
						</p>
					</div>
				</form:form>
			</div>
			
			<a href="#" class="close" onclick="showMenu(null);return false;"><spring:message code="button.close"/></a>
		</div>
		</div>
		<div class="containerCellR">
		<div id="contents" class="hide">
			<div id="nav" class="nav">
				<ul>
					<li><a href="#" class="home"><spring:message code="button.home"/></a></li>
				</ul>
				<a id="bookmarkIcon" href="#" class="favo" onclick="addToBookmark();return false;"><spring:message code="button.bookmark"/></a>
			</div>
			<h3><span id="title"></span>
				<p class="help hide2" style="display:none">
					<span class="text hide2">
						<span class="close link" onclick="showGuideComment(false)"> </span>
						<span class="title"><spring:message code="word.help"/></span>
						<span id="helpSpan" style="display: block;">
						</span>
						<%--
						<span id="guideComment"></span>
						--%>
					</span>
				</p>
			</h3>
			<div class="contentsWrap">
				<div class="contents">
					<tiles:insertAttribute name="content"/>
				</div>
				<div class="hx30"></div>
			</div>
		</div>
		</div>
	</div>
	<!-- 콘텐츠 끝 -->
	<div id="sgDialog" title="<spring:message code="word.confirm"/>"><p></p></div> <!-- 다이어로그 -->
	<div class="loadingDiv">
		<div class="loadingImgDiv">
			<img src="${img_path}/loading.gif"/>
			<div class="loadingPercentDiv"></div>
		</div>
		<div class="loadingBGDiv"></div>
	</div>
	<form:form action="${root}/logout.do" name="logoutForm" method="POST"></form:form>
	<iframe name="hiddenFrame" id="hiddenFrame" frameborder="0"></iframe>
	<form:form id="layoutPopForm" name="layoutPopForm"><input type="hidden" name="findYear"/></form:form>
	<c:forEach items="${guideCommentList}" var="guide"><textarea id="PGM_${guide.pgmId}" class="guideComment hide"><c:out value="${guide.guideComment}" escapeXml="true"/></textarea></c:forEach>
	<c:forEach items="${manualImgNmList}" var="manual"><textarea class="manualImgNmList hide"><c:out value="${manual}" escapeXml="true"/></textarea></c:forEach>
	<c:forEach items="${sessionScope.menuList}" var="menuList"><textarea id="PGM_${menuList.pgmId}" class="urlPages hide"><c:out value="${menuList.urlPage}" escapeXml="true"/></textarea></c:forEach>
	<%--
	<textarea id="processTextarea" class="hide"><c:out value="${processList}" escapeXml="true"/></textarea>
	--%>
</div>
</body>
</html>