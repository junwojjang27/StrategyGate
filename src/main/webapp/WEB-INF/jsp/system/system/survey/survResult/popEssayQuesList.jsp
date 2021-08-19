<!--
*************************************************************************
* CLASS 명	: SurvResultList
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-22
* 기	능	: 설문결과 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-22				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	$("#popList").jqGrid({
		url			:	"${context_path}/system/system/survey/survResult/essayQuesList_json.do",
		postData	:	getFormData("popForm"),
		width		:	"${jqgrid_size800}",
		height		:	"${jqgrid_dual}",
		colModel	:	[
						{name:"surveyId",			index:"surveyId",		hidden:true},
						{name:"surveyUserId",		index:"surveyUserId",	hidden:true},
						{name:"surveyUserNm",	index:"surveyUserNm",	width:100,		align:"center",	label:"<spring:message code="word.answerUsr"/>"},
						{name:"quesId",				index:"quesId",			hidden:true},
						{name:"quesItemId",		index:"quesItemId",		hidden:true},
						{name:"scDeptId",			index:"scDeptId",		hidden:true},
						{name:"answerContent",	index:"answerContent",width:300,	align:"left",	label:"<spring:message code="word.ansContent"/>"}
						],
		rowNum		: ${jqgrid_rownum_max}
	});

	/*popup인 경우 gridContainerdp width를 정의하고 gridResize() 호출 해야 함.*/
	gridResize("popList");

});

// 목록 조회
function popSearchList() {
 	reloadGrid("popList", "popForm");
}

</script>
<div class="popup wx800">
	<p class="title"><spring:message code="word.essayAnsList"/></p>
	<form:form commandName="searchVO" id="popForm" name="popForm" method="post">
	 	<form:hidden path="surveyId"/>
	 	<form:hidden path="quesId"/>
	 	<form:hidden path="scDeptId"/>

		<div class="sch-bx">
			<ul>
				<li>
					<label class=""><c:out value="${searchVO.quesNm}" /></label>
				</li>
			</ul>
			<a href="#" class="btn-sch" onclick="popSearchList();return false;"><spring:message code="button.search"/></a>
		</div>
		<div class="btn-dw"></div>
		<div class="gridContainer wx800">
			<table id="popList"></table>
			<div id="pager"></div>
		</div>
		<div class="tbl-bottom">
			<div class="tbl-btn">
				<a href="#" class="new" onclick="$.fancybox.close();return false;"><spring:message code="button.close"/></a>
			</div>
		</div>
	</form:form>
</div>