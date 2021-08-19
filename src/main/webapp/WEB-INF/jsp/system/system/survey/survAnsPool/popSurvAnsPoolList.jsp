<!--
*************************************************************************
* CLASS 명	: popSurvAnsPoolList
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-05
* 기	능	: 설문답변Pool List 팝업
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-05				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	$("#popSurvAnsPoolList").jqGrid({
		url			:	"${context_path}/system/system/survey/survAnsPool/popSurvAnsPoolList_json.do",
		postData	:	getFormData("popSurvAnsPoolform"),
		width		:	"${jqgrid_size800}",
		height		:	"${jqgrid_dual}",
		colModel	:	[
						{name:"itemPoolId",	index:"itemPoolId",		hidden:true},
						{name:"itemPoolNm",	index:"itemPoolNm",		width:250,	align:"left",	label:"<spring:message code="word.survAnsItemNm" />",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='setData(\"" + removeNull(rowObject.itemPoolId) + "\",\""+removeNull(rowObject.itemCntId)+"\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"itemCntId",	index:"itemCntId",		hidden:true},
						{name:"itemCntNm",	index:"itemCntNm",		width:100,	align:"center",	label:"<spring:message code="word.ansItemCnt" />"},
						{name:"itemContent",index:"itemContent",	width:300,	align:"left",	label:"<spring:message code="word.ansContent" />"},
						],
		rowNum		: ${jqgrid_rownum_max},
		multiselect	: false
	});

	/*popup인 경우 gridContainer  popGridResize() 호출 해야 함.*/
	popGridResize("popSurvAnsPoolList");
});

// 목록 조회
function searchPopList() {
 	reloadGrid("popSurvAnsPoolList", "popSurvAnsPoolform");
}

// 상세 조회
function setData(itemPoolId,itemCntId) {
	$("#itemPoolId").val(itemPoolId);
	$("#itemCntId").val(itemCntId);
	setItemCntDiv("pop");

	$.fancybox.close();
}

</script>
<div class="popup wx800">
	<p class="title"><spring:message code="word.survAnsPool" /></p>
	<form:form commandName="searchVO" id="popSurvAnsPoolform" name="popSurvAnsPoolform" method="post" onsubmit="searchPopList();return false;">
	<div class="sch-bx">
		<a href="#" class="btn-sch" onclick="searchPopList();return false;"><spring:message code="button.search"/></a>
	</div>
    <div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="popSurvAnsPoolList"></table>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-wbtn">
		</div>
		<div class="tbl-btn">
			<a href="#" class="save" onclick="$.fancybox.close();return false;"><spring:message code="button.close"/></a>
		</div>
	</div>
	<div class="page-noti">
		<ul>
			<li><spring:message code="system.system.survey.survAnsPool.info1"/></li>
		</ul>
	</div>
</form:form>
</div>
