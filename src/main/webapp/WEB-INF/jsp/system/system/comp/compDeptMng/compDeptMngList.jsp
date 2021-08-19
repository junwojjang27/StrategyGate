<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<c:set var="defaultRootDeptId"><spring:eval expression="@globals.getProperty('default.rootDeptId')"></spring:eval></c:set>
<validator:javascript formName="scDeptVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
var scDeptGrpList = {
	"":"<spring:message code="word.notApplicable"/>",<c:forEach var="item" items="${codeUtil:getCodeListByYear('003', searchVO.findYear)}" varStatus="status">"${item.codeId}":"${item.codeNm}"<c:if test="${not status.last}">,</c:if></c:forEach>
}

if(Object.keys(scDeptGrpList).length == 0) {
	scDeptGrpList[""] = "<spring:message code="word.select"/>";
}

$(function(){
	
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/comp/compDeptMng/compDeptMngList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
							{name:"isNew",			index:"isNew",		hidden:true},
							{name:"year",			index:"year",		hidden:true},
							{name:"scDeptId",		index:"scDeptId",	width:50,		align:"center",	label:"<spring:message code="word.orgCode"/>"},
							{name:"metricCnt",		index:"metricCnt",	hidden:true},
							{name:"scDeptNm",		index:"scDeptNm",	width:200,		align:"left",	editable:true,	label:"<spring:message code="word.org"/>"},
							{name:"orgScDeptNm",	index:"orgScDeptNm",	hidden:true},
							{name:"scDeptFullNm",	index:"scDeptFullNm",	hidden:true},
							{name:"upScDeptId",		index:"upScDeptId",	hidden:true},
							{name:"upScDeptNm",		index:"upScDeptNm",	width:100,		align:"left",	label:"<spring:message code="word.upOrg"/>",
								formatter:function(cellvalue, options, rowObject) {
									if(rowObject.scDeptId == "${defaultRootDeptId}") {
										return "";	
									} else {
										return	"<a href=\"#\" class=\"btn-search-grid\" onclick=\"popScDeptListForGrid('Y','list','"+options.rowId+"','upScDeptId','upScDeptNm','','');return false;\">"
												+ "<spring:message code="button.search"/></a> "
												+ "<input type=\"hidden\" value=\""+rowObject.upScDeptNm+"\"/>"
												+ rowObject.upScDeptNm;
									}
								},
								unformat:inputUnformatter
							},
							{name:"scDeptGrpId",	index:"scDeptGrpId",	width:100,	align:"center", editable:true,	formatter:"select",	edittype:"select",
								editoptions:{value:scDeptGrpList},
								label:"<spring:message code="word.evalGrp"/>"
							},
							{name:"deptKind",	index:"deptKind",	width:80,	align:"center", editable:true,	formatter:"select",	edittype:"select",
								editoptions:{value:
									':<spring:message code="word.notApplicable"/>;<c:forEach var="item" items="${codeUtil:getCodeList('025')}" varStatus="status">${item.codeId}:${item.codeNm}<c:if test="${not status.last}">;</c:if></c:forEach>'
								},
								label:"<spring:message code="word.deptDiagramShape"/>"
							},
							{name:"managerUserId",	index:"managerUserId",	hidden:true},
							{name:"managerUserNm",	index:"managerUserNm",	width:100,		align:"right",	label:"<spring:message code="word.manager"/>",
								formatter:function(cellvalue, options, rowObject) {
									return escapeHTML(removeNull(cellvalue))
											+ "<input type=\"hidden\" value=\""+rowObject.managerUserNm+"\"/>"
									 		+ " <a href='#' onclick='popSearchUserForGrid(\"list\", \"" + options.rowId + "\", \"managerUserId\", \"managerUserNm\");return false;'><img src='${img_path}/icon_search.gif'/></a>"
									 		+ " <a href='#' onclick='resetGridCellValue(\"list\", \"" + options.rowId + "\", [\"managerUserId\", \"managerUserNm\"]);return false;'><img src='${img_path}/btn-close.png'/></a>";
								},
								unformat:inputUnformatter
							},
							{name:"bscUserId",		index:"bscUserId",	hidden:true},
							{name:"bscUserNm",		index:"bscUserNm",	width:100,	align:"right",	label:"<spring:message code="word.meticInCharge"/>",
								formatter:function(cellvalue, options, rowObject) {
									return escapeHTML(removeNull(cellvalue))
											+ "<input type=\"hidden\" value=\""+rowObject.bscUserNm+"\"/>"
									 		+ " <a href='#' onclick='popSearchUserForGrid(\"list\", \"" + options.rowId + "\", \"bscUserId\", \"bscUserNm\");return false;'><img src='${img_path}/icon_search.gif'/></a>"
									 		+ " <a href='#' onclick='resetGridCellValue(\"list\", \"" + options.rowId + "\", [\"bscUserId\", \"bscUserNm\"]);return false;'><img src='${img_path}/btn-close.png'/></a>";
								},
								unformat:inputUnformatter
							},
							{name:"resetAllYn",		index:"resetAllYn",	width:100,	align:"center", editable:true,	formatter:"select",	edittype:"select",
								editoptions:{value:{
									<c:forEach var="item" items="${codeUtil:getCodeList('011')}" varStatus="status">"${item.codeId}":"${item.codeNm}"<c:if test="${not status.last}">,</c:if></c:forEach>
								}},
								label:"<spring:message code="word.actUserReset"/>"
							},
							{name:"useYn",	index:"useYn",	width:70,	align:"center", editable:true,	formatter:"select",	edittype:"select",
								editoptions:{value:{
									<c:forEach var="item" items="${codeUtil:getCodeList('011')}" varStatus="status">"${item.codeId}":"${item.codeNm}"<c:if test="${not status.last}">,</c:if></c:forEach>
								}},
								label:"<spring:message code="word.useYn"/>"
							},
							{name:"sortOrder",		index:"sortOrder",	width:70,		align:"center",	label:"<spring:message code="word.sortOrder"/>", sorttype:"number",
								editable:true, formatter:"text", editrules:{required:true,number:true}, editoptions:{maxlength:5}
							}
						],
		rowNum		: ${jqgrid_rownum_max},
		pager		: "pager",
		sortname	: "sortOrder",
		sortorder	: "asc",
		multiselect	: true,
		cellEdit	: true,
		formatCell: function(rowId, cellname, value, iRow, iCol) {
			if(cellname == "scDeptNm") {
				return $("#list").jqGrid("getRowData", rowId).orgScDeptNm;
			} else return value;
		},
		afterSaveCell : function(rowId, cellname, value, iRow, iCol) {
			if(cellname == "scDeptNm") {
				$("#list").jqGrid("setCell", rowId, "orgScDeptNm", value);
				
				var valArr = $("#list").jqGrid("getRowData", rowId).scDeptFullNm.split(">");
				valArr[valArr.length-1] = value;
				
				$("#list").jqGrid("setCell", rowId, "scDeptNm", valArr.join(">"));
				$("#list").jqGrid("setCell", rowId, "scDeptFullNm", valArr.join(">"));
			}else if(cellname=="useYn"){
				var useCnt = $("#list").jqGrid("getCell", iRow, "metricCnt");
				if(useCnt>0){
					$.showMsgBox("<spring:message code="info.nonDelete.msg"/>");
					$("#list").jqGrid("setCell", iRow, "useYn", "Y");
				}
				
			}
		},
		loadComplete : function() {
			$("#year").val($("#findYear").val());
			hideGridCheckbox("list", "metricCnt", 0, false);
			
			var $grid = $("#list");
			var ids = $grid.jqGrid("getDataIDs");
			$(ids).each(function(i, e) {
				if($grid.jqGrid("getRowData", e).scDeptId == "${defaultRootDeptId}") {
					$grid.find("#" + e).find("[aria-describedby=list_useYn]").addClass("not-editable-cell");
				}
			});
		}
	});
	
	// 연도 변경시 조직평가군 새로 불러옴
	$("#findYear").on("change", function() {
		getScDeptGrpList();
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

// 목록 조회
function searchList() {
	var colModel = $("#list").jqGrid("getGridParam", "colModel");
	colModel[10].editoptions.value = scDeptGrpList;
	$("#list").jqGrid("setGridParam", {"colModel" : colModel});
	
 	reloadGrid("list", "form");
}

// 엑셀 다운로드
function excelDownload() {
	var f = document.form;
	f.action = "${context_path}/system/system/comp/compDeptMng/excelDownload.do";
	f.submit();
}

// 일괄저장
function saveAll() {
	$("#findYear").val($("#year").val());
	
	if(!gridToForm("list", "form", true)) return false;
	
	$.showConfirmBox("<spring:message code="common.saveAll.msg"/>", function() {
		sendAjax({
			"url" : "${context_path}/system/system/comp/compDeptMng/saveAll.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult",
			"doneCallbackArgs" : $("#findScDeptId").val()
		});
	});
}

function checkResult(findScDeptId, data) {
	if(data.result == AJAX_SUCCESS) {
		$(window).scrollTop(0);
		$.showMsgBox(data.msg);
		if(isNotEmpty(findScDeptId)) {
			setScDept(findScDeptId);
		}

		reloadScDeptList();
		searchList();
	} else {
		$.showMsgBox(data.msg);
	}
}

// 엑셀업로드
function popExcelUploadForm() {
	openFancybox({
		"url" : "${context_path}/system/system/comp/compDeptMng/popExcelUploadForm.do",
		"data" : getFormData("form")
	});
}

// 등록
function addData() {
	var $grid = $("#list");
	$grid.jqGrid("addRowData", ("newRow" + $.jgrid.guid++),
			{
				"isNew" : "Y",
				"year" : $("#year").val(),
				"scDeptId" : "",
				"scDeptNm" : "",
				"orgScDeptNm" : "",
				"scDeptFullNm" : "",
				"upScDeptId" : $("#findScDeptId").val(),
				"upScDeptNm" : getScDeptNm($("#findScDeptId").val()),
				"managerUserId" : "",
				"managerUserNm" : "",
				"bscUserId" : "",
				"bscUserNm" : "",
				"sortOrder" : ""
			}, "last");
	$grid.jqGrid("scrollToBottom");
}

// 삭제
function deleteData() {
	if(deleteGridToForm("list", "scDeptId", "form")) {
		$.showConfirmBox("<spring:message code="common.delete.msg"/>", function() {
			var delList = [];
			$("#form").find("[name=keys]").each(function(i, e) {
				delList.push($(this).val());
			});
			
			if($.inArray(getScDeptRootId(), delList) != -1) {
				$.showMsgBox("<spring:message code="system.system.comp.compDeptMng.error2"/>");
				return false;
			}
			
			sendAjax({
				"url" : "${context_path}/system/system/comp/compDeptMng/deleteCompDeptMng.do",
				"data" : getFormData("form"),
				"doneCallbackFunc" : "checkResult",
				"doneCallbackArgs" : ($.inArray($("#findScDeptId").val(), delList) != -1 ? getScDeptRootId() : $("#findScDeptId").val())
			});
		});
	}
}

// 조직평가군 코드 조회
function getScDeptGrpList() {
	sendAjax({
		"url" : "${context_path}/common/codeList_json.do",
		"data" : {
			"codeGrpId" : "003",
			"findYear"	: $("#findYear").val(),
			"_csrf"		: getCsrf()
		},
		"doneCallbackFunc" : "setScDeptGrpId"
	});
}

// 조직평가군 세팅
function setScDeptGrpId(data) {
	$("#scDeptGrpId").empty();
	scDeptGrpList = {};
	if(isNotEmpty(data.list)) {
		var list = data.list;
		for(var i in list) {
			$("<option value='" + list[i].codeId + "'>" + list[i].codeNm + "</option>").appendTo($("#scDeptGrpId"));
			scDeptGrpList[list[i].codeId] = escapeHTML(list[i].codeNm);
		}
	} else {
		scDeptGrpList[""] = "<spring:message code="word.select"/>";
	}
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="findScDeptId"/>
	<form:hidden path="year"/>
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<div class="sch-oga" id="findScDeptList"></div>
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
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom tbl-bottom2 wx600">
		<div class="tbl-btn">
			<a href="#" class="dw" onclick="excelDownload();return false;"><spring:message code="button.excelFormDownload"/></a>
			<a href="#" class="save" onclick="popExcelUploadForm();return false;"><spring:message code="button.excelUpload"/></a>
			<a href="#" class="save" onclick="saveAll();return false;"><spring:message code="button.saveAll"/></a>
			<a href="#" class="new" onclick="addData();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	<div class="page-noti">
		<ul>
			<li><span class="red"><spring:message code="word.actUserReset"/> : <spring:message code="bsc.base.scDeptMng.scDeptMngForm.info3"/></span></li>
			<li><spring:message code="bsc.base.scDeptMng.scDeptMngList.info"/></li>
			<li><spring:message code="bsc.base.scDeptMng.scDeptMngList.info2"/></li>
		</ul>
	</div>
</form:form>
