<!--
*************************************************************************
* CLASS 명	: popSurvQuesPoolList
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-05
* 기	능	: 설문질문Pool List 팝업
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
	$("#popSurvQuesPoolList").jqGrid({
		url			:	"${context_path}/system/system/survey/survQuesPool/popSurvQuesPoolList_json.do",
		postData	:	getFormData("popSurvQuesPoolform"),
		width		:	"${jqgrid_size800}",
		height		:	"${jqgrid_dual}",
		colModel	:	[
						{name:"quesPoolId",		index:"quesPoolId",		hidden:true},
						{name:"quesPoolNm",		index:"quesPoolNm",		width:300,	align:"left",	label:"<spring:message code="word.question" />",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='setData(\"" + removeNull(rowObject.quesPoolId) + "\",\""+removeNull(rowObject.quesPoolNm)+"\",\""+removeNull(rowObject.quesGbnId)+"\",\""+removeNull(rowObject.itemCntId)+"\",\""+removeNull(rowObject.itemCheckGbnId)+"\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"quesGbnId",		index:"quesGbnId",		hidden:true},
						{name:"quesGbnNm",		index:"quesGbnNm",		width:100,	align:"center",	label:"<spring:message code="word.questionGbn" />"},
						{name:"itemCntId",		index:"itemCntId",		hidden:true},
						{name:"itemCntNm",		index:"itemCntNm",		width:100,	align:"center",	label:"<spring:message code="word.itemCnt" />"},
						{name:"itemCheckGbnId",	index:"itemCheckGbnId",	hidden:true},
						{name:"itemCheckGbnNm",	index:"itemCheckGbnNm",	width:100,	align:"center",	label:"<spring:message code="word.selectGbn" />"}
						],
		rowNum		: ${jqgrid_rownum_max},
		multiselect	: false
	});

	/*popup인 경우 gridContainer  popGridResize() 호출 해야 함.*/
	popGridResize("popSurvQuesPoolList");
});

// 목록 조회
function searchPopList() {
 	reloadGrid("popSurvQuesPoolList", "popSurvQuesPoolform");
}

// 상세 조회
function setData(quesPoolId,quesPoolNm,quesGbnId,itemCntId,itemCheckGbnId) {
	$("#quesPoolId").val(quesPoolId);
	$("#quesNm").val(quesPoolNm);
	$("#quesGbnId").val(quesGbnId);
	$("#itemCntId").val(itemCntId);
	$("#itemCheckGbnId").val(itemCheckGbnId);

	if($("#quesGbnId").val() == "002") {
		$("#itemCntId").prop("disabled",true);
		$("#itemCheckGbnId").prop("disabled",true);
		$("#trHeader").nextAll().remove();	//항목 행 삭제(invalid 걸림문제)
		$("#trTemplate").remove();	//복사용 행 삭제(invalid 걸림문제)
		$("#itemCheckGbnDIv").addClass("hide");	//설문답변Pool 버튼, 항목 입력폼 숨김
		$("#poolBtn1").addClass("hide");
		$("#poolBtn2").removeClass("hide");
	}else{
		$("#itemCntId").prop("disabled",false);
		$("#itemCheckGbnId").prop("disabled",false);
		$("#itemCheckGbnDIv").removeClass("hide");
		$("#poolBtn1").removeClass("hide");
		$("#poolBtn2").addClass("hide");
	}
	setItemCntDivForQuesPool();
	$.fancybox.close();
}

</script>
<div class="popup wx800">
	<p class="title"><spring:message code="word.survQuesPool" /></p>
	<form:form commandName="searchVO" id="popSurvQuesPoolform" name="popSurvQuesPoolform" method="post" onsubmit="searchPopList();return false;">
	<div class="sch-bx">
		<a href="#" class="btn-sch" onclick="searchPopList();return false;"><spring:message code="button.search"/></a>
	</div>
    <div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="popSurvQuesPoolList"></table>
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
			<li><spring:message code="system.system.survey.survQuesPool.info1"/></li>
		</ul>
	</div>
</form:form>
</div>
