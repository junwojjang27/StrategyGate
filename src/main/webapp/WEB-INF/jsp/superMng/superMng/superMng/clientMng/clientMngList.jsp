<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<c:set var="superCompId"><spring:eval expression="@globals.getProperty('Super.CompId')"></spring:eval></c:set>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/superMng/superMng/superMng/clientMng/clientMngList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
							{name:"compNm",			index:"compNm",			width:150,	align:"left",		label:"<spring:message code="word.compNm"/>",
								formatter:function(cellvalue, options, rowObject) {
									return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.compId) + "\");clickGridRow(this);return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
								}, unformat:linkUnformatter
							},
							{name:"compId",			index:"compId",			width:100,	align:"center",		label:"<spring:message code="word.compId"/>"},
							{name:"serviceStatus",	index:"serviceStatus",	width:100,	align:"center",		label:"<spring:message code="word.serviceStatus"/>"},
							{name:"payInfo",		index:"payInfo",		width:100,	align:"center",		label:"<spring:message code="word.payStatus"/>"},
							{name:"serviceDt",		index:"serviceDt",		width:150,	align:"center",		label:"<spring:message code="word.serviceDt"/>"},
							{name:"chargeNm",		index:"chargeNm",		width:100,	align:"center",		label:"<spring:message code="word.inCharge"/>"},
							{name:"chargeTel",		index:"chargeTel",		width:120,	align:"center",		label:"<spring:message code="word.inChargeTel"/>"},
							{name:"useYn",			index:"useYn",			width:120,	align:"center",		label:"<spring:message code="word.useYn"/>"},
							{name:"tableCnt",		index:"tableCnt",		hidden:true },
							{name:"serviceN",		index:"serviceN",		width:110,	align:"center",		title:false,	sortable:false,	label:"<spring:message code="word.useYn"/>",
								formatter:function(cellvalue, options, rowObject) {
									if(rowObject.compId == "${superCompId}") {
										return "";
									}
									
									if($("#findUseYn").val() == "Y") {
										return "<input type='button' class='pButton3_grid' value='<spring:message code="word.serviceN"/>' onclick='updateService(\""+options.rowId+"\");clickGridRow(this);return false;'/>";
									} else if($('#findUseYn').val() == "N") {
										return "<input type='button' class='pButton3_grid' value='<spring:message code="word.serviceY"/>' onclick='updateService(\""+options.rowId+"\");clickGridRow(this);return false;'/>";
									}
								}
							},
							{name:"createScheme",	index:"createScheme",	width:110,	align:"center",		label:"<spring:message code="word.createSchema"/>",
								formatter:function(cellvalue, options, rowObject) {
									if(rowObject.compId == "${superCompId}" || rowObject.tableCnt > 0) {
										return "";
									}
									
									return "<input type='button' class='pButton3_grid' value='<spring:message code="word.createSchema"/>' onclick='setScheme(\""+options.rowId+"\");clickGridRow(this);return false;'/>";
								}
							},
							{name:"reset",			index:"reset",			width:180,	align:"center",		title:false,	sortable:false,	label:"<spring:message code="word.reset"/>",
								formatter:function(cellvalue, options, rowObject) {
									if(rowObject.compId == "${superCompId}") {
										return "";
									}
									
									var str = "";
									
									if(rowObject.tableCnt > 0){
										if(rowObject.hasDefaultDataYn == "N") {
											str =  "<input type='button' class='pButton3_grid' value='<spring:message code="word.insertBasicData"/>' onclick='updateReset(\""+options.rowId+"\");clickGridRow(this);return false;'/>";
										} else {
											str =  "<input type='button' class='pButton3_grid' value='<spring:message code="button.reset"/>' onclick='popUpdateReset(\""+options.rowId+"\");clickGridRow(this);return false;'/>";
										}
										
										str += "<input type='button' class='pButton3_grid' value='<spring:message code="button.demoReset"/>' onclick='popDemoReset(\""+options.rowId+"\");clickGridRow(this);return false;'/>";
									}else{
										str = "no schema";
									}
									
									return str;
									
								}
							}
						],
		rowNum		: ${jqgrid_rownum},
		loadonce    : false,
		pager		: "pager",
		sortname	: "compNm",
		sortorder	: "asc",
		onSortCol	: function(sidx, column, sortOrder) {
					$("#sidx").val(sidx);
					$("#sord").val(sortOrder);
					searchList(1);
					return "stop";
		},
		onPaging	: function (pgButton) {
					console.log("pgButton",pgButton);
					setGridPaging($(this).attr("id"), pgButton, "searchList");
		}
	});
	
	$("#searchKeyword").keyup(function(e) {
		if(e.keyCode == 13) searchList();
	});
});

// 목록 조회
function searchList(pageNum) {
	if(isEmpty(pageNum)) {
		pageNum = parseInt($("#page").val(), 10);
	}
 	reloadGrid("list", "form", pageNum);
}

// 수정
function showDetail(compId) {
	$("#newCompId").val(compId);
	loadPage("${context_path}/superMng/superMng/superMng/clientMng/clientMngUpdateForm.do", "form");
}

// 신규
function goForm() {
	$("#newCompId").val("");
	loadPage("${context_path}/superMng/superMng/superMng/clientMng/clientMngForm.do", "form");
}

// 서비스 중지
function updateService(rowId) {
	var f = document.form;
	var rowData = $("#list").jqGrid("getRowData", rowId);
	var useYn = rowData.useYn;
	var msg = "<spring:message code="superMng.superMng.clientMng.clientMngList.confirm1"/>";
	
	if(useYn == "Y") {
		useYn = "N";
	} else {
		useYn = "Y";
		msg = "<spring:message code="superMng.superMng.clientMng.clientMngList.confirm2"/>";
	}
	$("#newCompId").val(rowData.compId);
	$("#useYn").val(useYn);
	
	$.showConfirmBox(msg, function() {
		sendAjax({
			"url" : "${context_path}/superMng/superMng/superMng/clientMng/updateService.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
	});
}

//DB정보 초기화
function updateDbInfo() {
	var msg = "<spring:message code="superMng.superMng.clientMng.clientMngList.confirm4"/>";
	
	$.showConfirmBox(msg, function() {
		sendAjax({
			"url" : "${context_path}/superMng/superMng/superMng/clientMng/resetDbInfo.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
	});
}

function setScheme(rowId){
	
	var f = document.form;
	var rowData = $("#list").jqGrid("getRowData", rowId);
	$("#newCompId").val(rowData.compId);
	
	var msg = "Do you want to create database scheme?";
	
	$.showConfirmBox(msg, function() {
		sendAjax({
			"url" : "${context_path}/superMng/superMng/superMng/clientMng/createScheme.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
	});
}

// 기초데이터 생성
function updateReset(rowId) {
	var f = document.form;
	var rowData = $("#list").jqGrid("getRowData", rowId);
	$("#newCompId").val(rowData.compId);
	
	$.showConfirmBox("<spring:message code="superMng.superMng.clientMng.clientMngList.confirm3"/>", function() {
		sendAjax({
			"url" : "${context_path}/superMng/superMng/superMng/clientMng/updateReset.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
	});
}

// 서비스 초기화
function popUpdateReset(rowId) {
	var rowData = $("#list").jqGrid("getRowData", rowId);
	$("#newCompId").val(rowData.compId);
	$("#compNm").val(rowData.compNm);
	
	openFancybox({
		"url" : "${context_path}/superMng/superMng/superMng/clientMng/popUpdateReset.do",
		"data" : getFormData("form")
	});
}

function popDemoReset(rowId) {
	var rowData = $("#list").jqGrid("getRowData", rowId);
	$("#newCompId").val(rowData.compId);
	$("#compNm").val(rowData.compNm);
	
	openFancybox({
		"url" : "${context_path}/superMng/superMng/superMng/clientMng/popDemoReset.do",
		"data" : getFormData("form")
	});
}

function checkResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		searchList();
	}
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="page"/>
	<form:hidden path="rows" value="${jqgrid_rownum}"/>
	<form:hidden path="sidx"/>
	<form:hidden path="sord"/>
	
	<form:hidden path="newCompId"/>
	<form:hidden path="copyTargetCompId"/>
	<form:hidden path="compNm"/>
	<form:hidden path="useYn"/>
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findUseYn"><spring:message code="word.useYn"/></label>
				<form:select path="findUseYn" class="select wx80" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findServiceStatus"><spring:message code="word.serviceStatus"/></label>
				<form:select path="findServiceStatus" class="select wx80">
					<form:option value=""><spring:message code="bsc.common.msg.all"/></form:option>
			    	<form:options items="${codeUtil:getCodeList('347')}" itemLabel="codeNm" itemValue="codeId"/>
				</form:select>
			</li>
			<li>
				<label for="findPayStatus"><spring:message code="word.payStatus"/></label>
				<form:select path="findPayStatus" class="select wx80">
					<form:option value=""><spring:message code="bsc.common.msg.all"/></form:option>
			    	<form:options items="${codeUtil:getCodeList('348')}" itemLabel="codeNm" itemValue="codeId"/>
				</form:select>
			</li>
			<li>
				<label class="mr0">&nbsp;</label>
				<form:select path="searchCondition" class="select wx80">
					<form:option value="01"><spring:message code="word.compNm"/></form:option>
					<form:option value="02"><spring:message code="word.compId"/></form:option>
				</form:select>
				<form:input path="searchKeyword" class="t-box02 wx100"/>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="reloadGrid('list', 'form');return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
		    <a href="#" class="new" onclick="updateDbInfo();return false;"><spring:message code="word.dbInfoReset"/></a>
			<a href="#" class="new" onclick="goForm();return false;"><spring:message code="button.addClient"/></a>
		</div>
	</div>
</form:form>
