<!-- 
*************************************************************************
* CLASS 명	: SignalMngList
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-2-6
* 기	능	: 신호등관리 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-2-6				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="signalMngVO" staticJavascript="false" xhtml="true" cdata="false"/>
<link href="${css_path}/color_picker_farbtastic.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" charset="utf-8" src="${js_path}/color_picker_farbtastic.js"></script>
<script type="text/javascript">
$(function(){
	$("#popSignalList").jqGrid({
		url			:	"${context_path}/comPop/system/system/code/signalMng/signalMngList_json.do",
		postData	:	getFormData("popSignalForm"),
		width		:	"550",
		height		:	"250",
		colModel	:	[
						{name:"year",			index:"year",	hidden:true},
						{name:"statusCodeId",	index:"statusCodeId",	hidden:true},
						{name:"statusId",		index:"statusId",	hidden:true},
						{name:"statusNm",		index:"statusNm",	width:80,	align:"center",	label:"<spring:message code="word.signalLamp"/>"},
						{name:"color",			index:"color",	width:100,	align:"center",	label:"<spring:message code="word.color"/>",
							formatter:function(cellvalue,options,rowObj){
								return "<div class='signalTable' style='background-color:"+cellvalue+";' >&nbsp;</div>";
							}
						},
						{name:"fromValue",	index:"fromValue",	hidden:true},
						{name:"toValue",	index:"toValue",	hidden:true},
						{name:"value",	index:"value",	width:100, align:"center", label:"<spring:message code="word.legend"/>(<spring:message code="word.more"/>~<spring:message code="word.below"/>)",
							formatter:function(cellvalue,options,rowObj){
								return rowObj.fromValue+"~"+rowObj.toValue;
							}
						},
						{name:"colorVal",	index:"colorVal", hidden:true}
						],
		rowNum		: ${jqgrid_rownum_max},
		multiselect	: false
	});
	
	/*popup인 경우 gridContainerdp width를 정의하고 gridResize() 호출 해야 함.*/
	gridResize("popSignalList");
	
});

// 목록 조회
function searchList() {
 	reloadGrid("popSignalList", "popSignalForm");
}

// 저장
function saveData() {
	if(gridToFormChanged("popSignalList", "popSignalForm", true)){
		sendAjax({
			"url" : "${context_path}/system/system/code/signalMng/saveSignalMng.do",
			"data" : getFormData("popSignalForm"),
			"doneCallbackFunc" : "checkResult"
		});
	}else{
		return false;
	}	
}

function checkResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		searchList();
	}
}

</script>
<div class="popup wx550">
	<p class="title"><spring:message code="word.signalLamp"/></p>
	<form:form commandName="searchVO" id="popSignalForm" name="popSignalForm" method="post">
	 	<form:hidden path="year"/>
		<form:hidden path="statusId"/>
	
		<div class="sch-bx">
			<ul>
				<li>
					<label for="findYear"><spring:message code="word.year"/></label>
					<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
					</form:select>
				</li>
			</ul>
			<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
		</div>
		<div class="btn-dw">
			<%--
			<a href="#" class="excel" onclick="excelDownload();return false;"><span><spring:message code="button.excelDownload"/></span></a>
			--%>
		</div>
		<div class="gridContainer" style="width:550px;">
			<table id="popSignalList"></table>
			<div id="pager"></div>
		</div>
		<div class="tbl-bottom">
			<div class="tbl-btn">
				<a href="#" class="new" onclick="$.fancybox.close();return false;"><spring:message code="button.close"/></a>
			</div>
		</div>
	</form:form>
</div>	