<!--
*************************************************************************
* CLASS 명	: PerspectiveList
* 작 업 자	: joey
* 작 업 일	: 2018. 1. 29.
* 기	능	:  관점 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018. 1. 29.		최 초 작 업
**************************************************************************
-->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="perspectiveVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/bsc/base/strategy/perspective/perspectiveList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
							{name:"year",			index:"year",				hidden:true},
							{name:"perspectiveId",	index:"perspectiveId",		hidden:true},
							{name:"perspectiveNm",	index:"perspectiveNm",	width:200,	align:"left",	label:"<spring:message code="word.perspective"/>",
								editable:true, edittype:"text", editrules:{required:true, custom:true, custom_func:jqGridChkBytes},	editoptions:{maxlength:150}
							},
							{name:"cntYn",	index:"cntYn",		hidden:true},
							{name:"useYn",			index:"useYn",				width:100,	align:"center",		label:"<spring:message code="word.useYn"/>",
								editable:true, edittype:"select", formatter:'select', editrules:{required:true}, editoptions:{value:getUseYnSelect()}
							},
							{name:"sortOrder",	    index:"sortOrder",			width:50,	align:"center",		label:"<spring:message code="word.sortOrder"/>",
								editable:true, edittype:"text", editrules:{required:true, integer:true}, editoptions:{maxlength:3}
							}
						],
		rowNum		: ${jqgrid_rownum_max},
		multiselect	: true,
		cellEdit	: true,
		cellsubmit  :'clientArray',
		afterSaveCell: function (rowid, name, val, iRow, iCol) {
			if(name=="useYn"){
				var useCnt = $("#list").jqGrid("getCell", iRow, "cntYn");
				if(useCnt>0){
					$.showMsgBox("<spring:message code="info.nonDelete.msg"/>");
					$("#list").jqGrid("setCell", iRow, "useYn", "Y");
				}
				
			}
		},
		loadComplete : function() {
			hideGridCheckbox("list", "cntYn", 0, false);
			if($("#findUseYn").val()=="N"){
				$(".delete").hide();
			}else{
				$(".delete").show();
			}
		}
	});

	/***** 사용여부 미사용시 삭제 버튼 숨김 *****/
	<c:choose>
		<c:when test="${searchVO.findUseYn == 'N'}">
			$(".delete").hide();
		</c:when>
		<c:otherwise>
			$(".delete").show();
		</c:otherwise>
	</c:choose>
	$("#findUseYn").on("change", function() {
		if($(this).val() == "N"){
			$(".delete").hide();
		}else{
			$(".delete").show();
		}
	});
	/***** 사용여부 미사용시 삭제 버튼 숨김 end *****/
});

function getUseYnSelect(){

	var selectStr="";
	<c:forEach var="useYnList" items="${codeUtil:getCodeList('011')}" varStatus="status">
		selectStr += "<c:out value="${useYnList.codeId}"/>"+":"+"<c:out value="${useYnList.codeNm}"/>";
		<c:if test="${not status.last}">
			selectStr += ";";
		</c:if>
	</c:forEach>

	return selectStr;
}

// 엑셀 다운로드
function excelDownload() {
	var f = document.form;
	f.action = "${context_path}/bsc/base/strategy/perspective/excelDownload.do";
	f.submit();
}

function addRow(){

	var rowId = "newRow"+$.jgrid.guid++;
	/*조회된 데이터 없을시 나타나는 문구 없앰.*/
	if($("#list").find(".noGridResult").length > 0){
		$("#list").find(".noGridResult").closest("tr").hide();
	}

	var rowData = {year:$("#findYear").val(),
			       useYn:'Y'}

	$("#list").jqGrid("addRowData", rowId, rowData,'last');
	$('#list tr:last').focus();

	//수정한 것만 저장할 경우 추가시 아래구문이 추가되어야 함. (필수체크목록)
	$("#list").jqGrid("setRowData", rowId, rowData,'edited');
}

//그리드 조회
function searchList() {
 	reloadGrid("list", "form");
}

function checkResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		searchList();
	}
}

//저장
function saveData() {
	if(!gridToFormChanged("list", "form", true)) return false;

	sendAjax({
		"url" : "${context_path}/bsc/base/strategy/perspective/savePerspective.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

//삭제
function deleteData() {
	if(deleteGridToForm("list", "perspectiveId", "form")) {
		$.showConfirmBox("<spring:message code="common.beforeDelete.msg"/>", "doDeleteData");
	}
}

// 삭제 처리
function doDeleteData() {
	sendAjax({
		"url" : "${context_path}/bsc/base/strategy/perspective/deletePerspective.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

<sec:authorize access="hasRole('01')">
// 전년도 데이터 복사
function copyData() {
	var findYear = parseInt($("#findYear").val(), 10);
	var lastYear = findYear - 1;
	
	$.showConfirmBox(getMessage("common.confirm.copyPastData.msg", lastYear, findYear, findYear), function() {
		sendAjax({
			"url" : "${context_path}/common/copyPastData.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
	});
}
</sec:authorize>
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<sec:authorize access="hasRole('01')">
		<input type="hidden" id="tableId" name="tableId" value="BSC_PERSPECTIVE"/>
	</sec:authorize>
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findUseYn"><spring:message code="word.useYn"/></label>
				<form:select path="findUseYn" class="select" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
		<a href="#" class="excel" onclick="excelDownload();return false;"><span><spring:message code="button.excelDownload"/></span></a>
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom">
		<sec:authorize access="hasRole('01')">
			<div class="tbl-wbtn">
				<a href="#" class="delete" onclick="copyData();return false;"><span><spring:message code="button.copyPastData"/></span></a>
			</div>
		</sec:authorize>
		<div class="tbl-btn">
			<a href="#" class="save" onclick="saveData();return false;"><spring:message code="button.saveAll"/></a>
			<a href="#" class="save" onclick="addRow();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
</form:form>
