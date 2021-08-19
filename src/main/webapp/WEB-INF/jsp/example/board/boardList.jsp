<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	/******************************************
	* 데이터 그리드 출력
	******************************************/
	$("#list").jqGrid({
		url			:	"${context_path}/example/board/boardList_json.do",
		postData	:	getFormData("formFind"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
							{name:"id",			index:"id",			width:100,		align:"center",	label:"<spring:message code="word.number"/>",
								formatter:showDetailLinkFormatter, unformat:linkUnformatter},
							{name:"subject",	index:"subject",	width:200,		align:"left",	title:true,	label:"<spring:message code="word.title"/>",
								formatter:function(cellvalue, options, rowObject) {
									return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.id) + "\");clickGridRow(this);return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
								}
							},
							{name:"userNm",		index:"userNm",		width:100,		align:"center",	title:true,	label:"<spring:message code="word.insertUser"/>"},
							{name:"createDt",	index:"createDt",	width:100,		align:"center",	title:true,	label:"<spring:message code="word.insertDT"/>"}
						],
		rowNum		: ${jqgrid_rownum},
		pager		: "pager",
		sortname	: "id",
		sortorder	: "desc",
		onSortCol	: function(sidx, column, sortOrder) {
					$("#formFind input[name=sidx]").val(sidx);
					$("#formFind input[name=sord]").val(sortOrder);
					searchList(1);
					return "stop";
		},
		onPaging	: function (pgButton) {
					setGridPaging($(this).attr("id"), pgButton, "searchList");
		},
		loadonce	: false,
		multiselect	: true
	});
});

// 목록 조회
function searchList(pageNum) {
	if(isEmpty(pageNum)) {
		pageNum = parseInt($("#page").val(), 10);
	}
 	reloadGrid("list", "formFind", pageNum);
}

// 상세 조회
function showDetail(id) {
	var f = document.formFind;
	f.id.value = id;
	loadPage("${context_path}/example/board/boardDetail.do", "formFind");
}

// 등록 폼으로 이동
function goForm() {
	document.formFind.id.value = "";
	loadPage("${context_path}/example/board/boardForm.do", "formFind");
}

// 삭제
function deleteData() {
	if(deleteDataToForm("list", "id", "formFind")) {
		$.showConfirmBox("<spring:message code="common.delete.msg"/>", "doDeleteData");
	}
}

// 삭제 처리
function doDeleteData() {
	sendAjax({
		"url" : "${context_path}/example/board/deleteBoard.do",
		"data" : getFormData("formFind"),
		"doneCallbackFunc" : "reloadGrid",
		"doneCallbackArgs" : ["list", "formFind", 1]
	});
}
</script>

<form:form commandName="searchVO" id="formFind" name="formFind" method="post">
	<form:input path="page" class="formHiddenData"/>
	<form:input path="rows" class="formHiddenData"/>
	<form:input path="sidx" class="formHiddenData"/>
	<form:input path="sord" class="formHiddenData"/>
	<form:input path="id" class="formHiddenData"/>
	<div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
			<a href="#" class="new" onclick="goForm();return false;">등록</a>
			<a href="#" class="delete" onclick="deleteData();return false;">삭제</a>
		</div>
	</div>
</form:form>
