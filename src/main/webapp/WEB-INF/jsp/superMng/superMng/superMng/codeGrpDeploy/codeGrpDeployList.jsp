<!--
*************************************************************************
* CLASS 명	: CodeGrpDeployList
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-07-04
* 기	능	: 슈퍼관리자 공통코드 배포관리 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-07-04				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="codeGrpVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
var globalModifyYN;
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/superMng/superMng/superMng/codeGrpDeploy/codeGrpDeployList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"250",
		colModel	:	[
						{name:"codeGrpId",	index:"codeGrpId",	width:60, align:"center",	label:"<spring:message code="word.codeGroupId"/>"},
						/* 다국어 사용 안하는 일반 프로젝트에서는 주석부분을 사용할것.
						{name:"codeGrpNm",	index:"codeGrpNm",	width:250,	align:"left",	label:"<spring:message code="word.codeGrpNm"/>",
							//editable:true, edittype:"text", editrules:{required:true, custom:true, custom_func:jqGridChkBytes}, editoptions:{maxlength:500}
						},
						*/
						{name:"codeGrpNm",	index:"codeGrpNm",	width:200,	align:"left",	label:"<spring:message code="word.codeGrpNm"/>",
							//editable:true, edittype:"text", editrules:{required:true, custom:true, custom_func:jqGridChkBytes}, editoptions:{maxlength:500}
							editable:true, edittype:"custom",
							editrules:{required:true},
							editoptions:{custom_element:getMultiInput,
								         custom_value:getMultiValue
							}
						},
						{name:"codeGrpNmLang",	index:"codeGrpNmLang", hidden:true},
						{name:"modifyYn",	index:"modifyYn",	width:70,	align:"center",	label:"<spring:message code="word.modifyYn"/>",
							editable:true, edittype:"select", formatter:'select', editroles:{required:true},
							editoptions:{value:getModifySelect()}
						},
						{name:"codeGbnId",	index:"codeGbnId",	width:50,	align:"center",	label:"<spring:message code="word.division"/>",
							editable:true, edittype:"select", formatter:'select', editroles:{required:true},
							editoptions:{value:getGbnSelect()}
						},
						{name:"codeDefId",	index:"codeDefId",	width:80,	align:"center",	label:"<spring:message code="word.codeDefId"/>",
							editable:true, edittype:"select", formatter:'select', editroles:{required:true},
							editoptions:{value:getCodeDefSelect()}
						},
						{name:"yearYn",		index:"yearYn",		width:80,	align:"center",	label:"<spring:message code="word.yearlyManageYn"/>",
							editable:true, edittype:"select", formatter:"select", editroles:{required:true},
							editoptions:{value:getYearYnSelect()}
						},
						{name:"content",	index:"content",	hidden:true},
						/*
						{name:"useYn",		index:"useYn",		width:80,	align:"center",	label:"<spring:message code="word.useYn"/>",
							editable:true, edittype:"select", formatter:"select", editroles:{required:true},
							editoptions:{value:getUseYnSelect(),
										 dataEvents:[{type:"change",
													  fn: function(e){
														  var rowId = $(this).attr("id")[0];
														  var allCnt = $("#list").jqGrid("getRowData",rowId).codeAllCnt;

														  if($(this).val()=='N' && Number(allCnt)>0){
															  $(this).val("Y");
															  $.showMsgBox("<spring:message code="info.existChild.msg"/>");
														  }
													  }
												}]
										}
						},
						*/
						{name:"codeCnt",	index:"codeCnt",	width:50,	align:"center",	label:"<spring:message code="word.codeCnt"/>",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='goCodePage(\"" + removeNull(rowObject.codeGrpId) + "\",\"" + removeNull(rowObject.yearYn) + "\",\"" + removeNull(rowObject.codeGrpNm) + "\",\"" + removeNull(rowObject.codeDefId) + "\",\"" + removeNull(rowObject.modifyYn)+ "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"codeAllCnt",	index:"codeAllCnt",	hidden:true},
						{name:"deployTargetYn",		index:"deployTargetYn",		width:80,	align:"center",	label:"<spring:message code="word.deployObjectYn"/>",
							editable:true, edittype:"select", formatter:"select", editroles:{required:true},
							editoptions:{value:getDeployObjectYnSelect()}
						},
						{name:"deployCnt",		index:"deployCnt",		width:80,	align:"center",	label:"<spring:message code="word.deployObjectCnt"/>",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='popCodeGrpDeployPage(\"" + removeNull(rowObject.codeGrpId) + "\",\"" + removeNull(rowObject.deployTargetYn) + "\");return false;'>" + escapeHTML(removeNull(rowObject.deployCnt))+"/"+escapeHTML(removeNull(rowObject.compCnt)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"compCnt",		index:"compCnt", 	hidden:true }
						],
		rowNum		: ${jqgrid_rownum_max},
		//pager		: "pager",
		multiselect	: true,
		cellEdit    : true,
		cellSubmit  : "clientArray"
		/*
		loadComplete : function(){
			if($("#list").getDataIDs().length>0){
				if($("#list").getRowData(1).modifyYn=="N"){
					globalModifyYN = "N";
					$("#btns").hide();
				}else{
					globalModifyYN = "Y";
					$("#btns").show();
				}
			}
		}
		*/
	});

	$("#listChild").jqGrid({
		url			:	"${context_path}/superMng/superMng/superMng/codeGrpDeploy/codeDeployList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"250",
		colModel	:	[
						{name:"modifyYn",	index:"modifyYn",	hidden:true},
						{name:"codeGrpId",	index:"codeGrpId",	hidden:true},
						{name:"codeId",		index:"codeId",		width:100, align:"center", label:"<spring:message code="word.commCode"/>",
							editable:true, edittype:"text", editrules:{custom:true, custom_func:jqGridChkBytes},	editoptions:{maxlength:7}
						},
						/*
						{name:"codeNm",		index:"codeNm",	width:200,	align:"left",	label:"<spring:message code="word.codeNm"/>",
							editable:true, edittype:"text", editrules:{required:true, custom:true, custom_func:jqGridChkBytes}, editoptions:{maxlength:500}
						},
						*/
						{name:"codeNm",	index:"codeNm",	width:250,	align:"left",	label:"<spring:message code="word.commCodeNm"/>",
							//editable:true, edittype:"text",
							editable:true, edittype:"custom", editrules:{required:true, custom:true, custom_func:jqGridChkBytes}, editoptions:{maxlength:500},
							editoptions:{custom_element:getMultiCodeInput,
								         custom_value:getMultiCodeValue
							}
						},
						{name:"codeNmLang",	index:"codeNmLang", hidden:true},
						{name:"etc1",		index:"etc1",	width:150,	align:"left",	label:"<spring:message code="word.etc1"/>",
							editable:true, edittype:"text", editrules:{custom:true, custom_func:jqGridChkBytes}, editoptions:{maxlength:500}
						},
						{name:"etc2",		index:"etc2",	width:150,	align:"left",	label:"<spring:message code="word.etc2"/>",
							editable:true, edittype:"text", editrules:{custom:true, custom_func:jqGridChkBytes}, editoptions:{maxlength:500}
						},
						{name:"content",	index:"content",	hidden:true},
						{name:"sortOrder",	index:"sortOrder",	width:150,	align:"center",	label:"<spring:message code="word.sortOrder"/>",
							editable:true, edittype:"text", editrules:{required:true,integer:true},
							sorttype:sortTypeForSortOrder
						},
						{name:"useYn",		index:"useYn",		width:80,	align:"center",	label:"<spring:message code="word.useYn"/>",
							editable:true, edittype:"select", formatter:"select", editroles:{required:true},
							editoptions:{value:getUseYnSelect()}
						}
						],
		rowNum		: ${jqgrid_rownum_max},
		//pager		: "pager",
		sortname	: "sortOrder",
		sortorder	: "asc",
		multiselect	: true,
		cellEdit    : true,
		cellSubmit  : "clientArray"
		/*
		loadComplete: function(){
			
			if($("#codeDefId").val() == "01"){
				$("#listChild").jqGrid("setColProp","codeId",{editable:false});
			}else{
				$("#listChild").jqGrid("setColProp","codeId",{editable:true});
			}

			if(globalModifyYN=="N"){
				$("#gbox_listChild .ui-jqgrid-htable #listChild_codeId").removeClass("editableColumn");
				$("#gbox_listChild .ui-jqgrid-htable #listChild_codeNm").removeClass("editableColumn");
				$("#gbox_listChild .ui-jqgrid-htable #listChild_etc1").removeClass("editableColumn");
				$("#gbox_listChild .ui-jqgrid-htable #listChild_etc2").removeClass("editableColumn");
				$("#gbox_listChild .ui-jqgrid-htable #listChild_sortOrder").removeClass("editableColumn");
				$("#gbox_listChild .ui-jqgrid-htable #listChild_useYn").removeClass("editableColumn");
			}else{
				$("#gbox_listChild .ui-jqgrid-htable #listChild_codeId").addClass("editableColumn");
				$("#gbox_listChild .ui-jqgrid-htable #listChild_codeNm").addClass("editableColumn");
				$("#gbox_listChild .ui-jqgrid-htable #listChild_etc1").addClass("editableColumn");
				$("#gbox_listChild .ui-jqgrid-htable #listChild_etc2").addClass("editableColumn");
				$("#gbox_listChild .ui-jqgrid-htable #listChild_sortOrder").addClass("editableColumn");
				$("#gbox_listChild .ui-jqgrid-htable #listChild_useYn").addClass("editableColumn");
			}
		}
		*/
	});

	$("body").on("blur", ".customInput input, .customCodeInput input", function(e) {
		$(this).unbind("blur");
	}).on("click", "input[id$=_codeGrpNm], input[id$=_codeNm]", function(e) {
		e.stopPropagation();
	}).on("click", function(e) {
		// 코드명 수정 중 코드명 외의 다른 부분을 클릭했을 경우 해당 코드명 저장 처리
		var attr = removeNull($(e.target).attr("aria-describedby"));
		if(attr != "list_codeGrpNm" && attr != "listChild_codeNm") {
			if($("#list").find("input[id$=_codeGrpNm]").length > 0) {
				$("#list").find("input[id$=_codeGrpNm]").closest("tr").click();
			}
			if($("#listChild").find("input[id$=_codeNm]").length > 0) {
				$("#listChild").find("input[id$=_codeNm]").closest("tr").click();
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
	$("#findCodeUseYn").val("${searchVO.findUseYn}");
	$("#findUseYn").on("change", function() {
		if($(this).val() == "N"){
			$(".delete").hide();
		}else{
			$(".delete").show();
		}
		$("#findCodeUseYn").val($(this).val());
	});
	/***** 사용여부 미사용시 삭제 버튼 숨김 end *****/
});

function getMultiInput(value,options){

	var gridIds = options.id.split("_");
	var dataIds = $("#list").jqGrid("getDataIDs");

	var id = dataIds[Number(gridIds[0])-1];
	//var valuesText1 = $("#list").jqGrid('getCell', id, 'codeGrpNmLang');
	var rowObject = $("#list").jqGrid('getRowData', id);
	var valuesText = rowObject.codeGrpNmLang;

	//var valuesText = $("#list #"+id).find("td[aria-describedby='list_codeGrpNmLang']").text();

	var values = {};
	if(valuesText != ''){
		values = valuesText.split("$%^");
	}

	var inputtext = "";
	var langValues = {};
	var valueText;

	inputtext+="<div class=\"customInput\">";
	<c:forEach var="langList" items="${langList}" varStatus="status">
		valueText = "";
		if(values.length != undefined){
			for(var i=0 ; i<values.length ; i++){
				langValues = values[i].split("#$%");

				if(langValues[0] == "${langList.lang}"){
					valueText=langValues[1];
					break;
				}
			}
		}


		inputtext+="${langList.lang}:<input id=\"${langList.lang}_"+options.id+"\" type=\"text\" value=\""+valueText+"\" style=\"width:100%;height:30px;\"/>";
		<c:if test="${not status.last}">
			inputtext += "</br>";
		</c:if>
	</c:forEach>
	inputtext+="</div>";
	return inputtext;
}

function getMultiValue(elem, operation, value){

	var $elems = $(elem).find("input");
	var $tr = $(elem).parents("tr:first");
	var $trObj = $("#list").find("#"+$tr.attr("id"));
	var $tr_rowid = $trObj[0].rowIndex;

	var $td = $trObj.find("td[aria-describedby='list_codeGrpNmLang']");
	var $td_colid = $td[0].cellIndex;

	var result = "";
	var bindResultStr = "";
	if(0<$elems.length){
		for(var i=0 ; i<$elems.length ; i++){
			if("${searchVO.lang}" == $elems.eq(i).attr("id").split('_')[0]){
				result = $elems.eq(i).val();
			}
			//bindResult[$elems.eq(i).attr("id").split('_')[0]] = $elems.eq(i).val();
			bindResultStr += $elems.eq(i).attr("id").split('_')[0]+"#$%"+$elems.eq(i).val()+"$%^";
		}
	}

	//custom 에서 변경되지않는 것에 대해 강제로 변경처리하도록 설정.
	$tr.find("td[aria-describedby='list_codeGrpNmLang']").attr("title",bindResultStr).text(bindResultStr).addClass("dirty-cell");
	$tr.addClass("edited");

	return result;
}

function getMultiCodeInput(value,options){

	var gridIds = options.id.split("_");
	var dataIds = $("#listChild").jqGrid("getDataIDs");
	var id = dataIds[Number(gridIds[0])-1];
	//var valuesText = $("#listChild").jqGrid('getCell', id, 'codeNmLang');
	//var valuesText = $("#listChild #"+id).find("td[aria-describedby='listChild_codeNmLang']").text();
	var rowObject = $("#listChild").jqGrid('getRowData', id);
	var valuesText = rowObject.codeNmLang;

	var values = {};
	if(valuesText != ''){
		values = valuesText.split("$%^");
	}

	var inputtext = "";
	var langValues = {};
	var valueText;

	inputtext+="<div class=\"customCodeInput\">";
	<c:forEach var="langList" items="${langList}" varStatus="status">
		valueText = "";
		if(values.length != undefined){
			for(var i=0 ; i<values.length ; i++){
				langValues = values[i].split("#$%");

				if(langValues[0] == "${langList.lang}"){
					valueText=langValues[1];
					break;
				}
			}
		}

		inputtext+="${langList.lang}:<input id=\"${langList.lang}_"+options.id+"\" type=\"text\" value=\""+valueText+"\" style=\"width:100%;height:30px;\"/>";
		<c:if test="${not status.last}">
			inputtext += "</br>";
		</c:if>
	</c:forEach>
	inputtext+="</div>";
	return inputtext;
}

function getMultiCodeValue(elem, operation, value){

	var $elems = $(elem).find("input");
	var $tr = $(elem).parents("tr:first");
	var $trObj = $("#listChild").find("#"+$tr.attr("id"));
	var $tr_rowid = $trObj[0].rowIndex;

	var $td = $trObj.find("td[aria-describedby='listChild_codeNmLang']");
	var $tdNm = $trObj.find("td[aria-describedby='listChild_codeNm']");
	var $td_colid = $td[0].cellIndex;

	var result = "";
	var bindResultStr = "";
	if(0<$elems.length){
		for(var i=0 ; i<$elems.length ; i++){
			if("${searchVO.lang}" == $elems.eq(i).attr("id").split('_')[0]){
				result = $elems.eq(i).val();
			}
			//bindResult[$elems.eq(i).attr("id").split('_')[0]] = $elems.eq(i).val();
			bindResultStr += $elems.eq(i).attr("id").split('_')[0]+"#$%"+$elems.eq(i).val()+"$%^";
		}
	}

	//custom 에서 변경되지않는 것에 대해 강제로 변경처리하도록 설정.
	$tr.find("td[aria-describedby='listChild_codeNmLang']").attr("title",bindResultStr).text(bindResultStr).addClass("dirty-cell");
	$tr.addClass("edited");
	//$("#listChild").jqGrid('setCell', $tr_rowid, 'codeNmLang', bindResultStr, "dirty-cell");
	//$td.addClass("dirty-cell");

	return result;
}

function getCodeDefSelect(){
	var selectStr="";
	<c:forEach var="defList" items="${codeUtil:getCodeList('023')}" varStatus="status">
		selectStr += "<c:out value="${defList.codeId}"/>"+":"+"<c:out value="${defList.codeNm}"/>";
		<c:if test="${not status.last}">
			selectStr += ";";
		</c:if>
	</c:forEach>

	return selectStr;
}

function getGbnSelect(){
	var selectStr="";
	<c:forEach var="gbnList" items="${codeUtil:getCodeList('002')}" varStatus="status">
		selectStr += "<c:out value="${gbnList.codeId}"/>"+":"+"<c:out value="${gbnList.codeNm}"/>";
		<c:if test="${not status.last}">
			selectStr += ";";
		</c:if>
	</c:forEach>

	return selectStr;
}

function getModifySelect(){
	var selectStr="";
	<c:forEach var="modifyList" items="${codeUtil:getCodeList('366')}" varStatus="status">
		selectStr += "<c:out value="${modifyList.codeId}"/>"+":"+"<c:out value="${modifyList.codeNm}"/>";
		<c:if test="${not status.last}">
			selectStr += ";";
		</c:if>
	</c:forEach>

	return selectStr;
}

function getYearYnSelect(){
	var selectStr="";
	<c:forEach var="yearYnList" items="${codeUtil:getCodeList('026')}" varStatus="status">
		selectStr += "<c:out value="${yearYnList.codeId}"/>"+":"+"<c:out value="${yearYnList.codeNm}"/>";
		<c:if test="${not status.last}">
			selectStr += ";";
		</c:if>
	</c:forEach>
	return selectStr;
}

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

function getDeployObjectYnSelect(){
	var selectStr="";
	<c:forEach var="deployList" items="${codeUtil:getCodeList('001')}" varStatus="status">
		selectStr += "<c:out value="${deployList.codeId}"/>"+":"+"<c:out value="${deployList.codeNm}"/>";
		<c:if test="${not status.last}">
			selectStr += ";";
		</c:if>
	</c:forEach>

	return selectStr;
}

function goCodePage(codeGrpId,yearYn,codeGrpNm,codeDefId,modifyYn){
	globalModifyYN = modifyYn;
	$("#form #codeGrpId").val(codeGrpId);
	$("#form #codeDefId").val(codeDefId);
	$("#form #yearYn").val(yearYn);
	$("#codeGrpNmText").empty();
	$("#codeGrpNmText").html("<b>"+codeGrpNm+"</b>");
	/*
	if(modifyYn=="N"){
		$("#btns").hide();
	}else{
		$("#btns").show();
	}
	*/
	searchCodeList();

}


// 목록 조회
function searchList() {
 	$("#codeGrpId").val("");
 	$("#yearYn").val("");
	loadPage("${context_path}/superMng/superMng/superMng/codeGrpDeploy/codeGrpDeployList.do", "form");
}

//목록 조회
function searchCodeList() {
	//reloadGrid("list", "form");
 	reloadGrid("listChild", "form");
}

//목록 조회
function searchSaveCodeList() {
	reloadGrid("list", "form");
 	reloadGrid("listChild", "form");
}

//cell 추가함수
function addRow(){

	/*조회된 데이터 없을시 나타나는 문구 없앰.*/
	if($("#list").find(".noGridResult").length > 0){
		$("#list").find(".noGridResult").closest("tr").remove();
	}

	var rowData = {codeDefId:'02',
				   yearYn:'N',
			       useYn:'Y'}

	$("#list").jqGrid("addRowData", ("newRow"+$.jgrid.guid++), rowData,'last');
	$('#list tr:last').focus();

}


//저장
function saveData() {

	$("#listChild #codeGrpId").val($("#list #codeGrpId").val());

	if(gridToFormChanged("list", "form", true)){
		sendAjax({
			"url" : "${context_path}/superMng/superMng/superMng/codeGrpDeploy/saveCodeGrpDeploy.do",
			"data" : getFormData("form"),
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

//삭제
function deleteDataChk() {
	var isOk = true;
	var ids = $("#list").jqGrid("getDataIDs");
	var selIds = $("#list").jqGrid("getGridParam","selarrrow");

	if(selIds.length>0){
		for(var i in selIds){
			var obj = $("#list").jqGrid("getRowData",ids[selIds[i]-1]);
			if(Number(obj.codeAllCnt)>0){
				isOk = false;
			}
		}
	}

	if($(".customInput").length > 0){
		$.showMsgBox("<spring:message code="info.cellEditNotClose.msg"/>",null);
		return false;
	}

	if(!isOk){
		$.showMsgBox("<spring:message code="info.existChild.msg"/>",null);
        return false;
	}else if(deleteGridToForm("list", "codeGrpId", "form")){
		$.showConfirmBox("<spring:message code="common.beforeDelete.msg"/>", "doDeleteData");
	}

}

// 삭제 처리
function doDeleteData() {

	sendAjax({
		"url" : "${context_path}/superMng/superMng/superMng/codeGrpDeploy/deleteCodeGrpDeploy.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

//cell 추가함수
function addCodeRow(){

	/*조회된 데이터 없을시 나타나는 문구 없앰.*/
	if($("#listChild").find(".noGridResult").length > 0){
		$("#listChild").find(".noGridResult").closest("tr").remove();
	}

	var rowData = {useYn:'Y'}

	$("#listChild").jqGrid("addRowData", ("newRow"+$.jgrid.guid++), rowData,'last');
	$('#listChild tr:last').focus();

}


//저장
function saveCodeData() {

	if(gridToForm("listChild", "form", true)){
		sendAjax({
			"url" : "${context_path}/superMng/superMng/superMng/codeGrpDeploy/saveCodeDeploy.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "searchSaveCodeList"
		});
	}else{
		return false;
	}


}

function checkCodeResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		searchCodeList();
	}
}

//삭제
function deleteCodeDataChk() {

	if($(".customCodeInput").length > 0){
		$.showMsgBox("<spring:message code="info.cellEditNotClose.msg"/>",null);
		return false;
	}

	if(deleteGridToForm("listChild", "codeId", "form")){
		$.showConfirmBox("<spring:message code="common.beforeDelete.msg"/>", "doDeleteCodeData");
	}

}

function doDeleteCodeData() {
// 삭제 처리

	sendAjax({
		"url" : "${context_path}/superMng/superMng/superMng/codeGrpDeploy/deleteCodeDeploy.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkCodeResult"
	});
}

function popCodeGrpDeployPage(codeGrpId,deployTargetYn){

	document.form.codeGrpId.value=codeGrpId;
	document.form.deployTargetYn.value=deployTargetYn;

	openFancybox({
		"url" : "${context_path}/superMng/superMng/superMng/codeGrpDeploy/popCodeGrpDeployList.do",
		"data" : getFormData("form")
	});
}

function saveDeployDataDo(){
	sendAjax({
		"url" : "${context_path}/superMng/superMng/superMng/codeGrpDeploy/updateDeploy.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
    <form:hidden path="codeGrpId"/>
    <form:hidden path="deployTargetYn"/>
    <form:hidden path="codeDefId"/>
    <form:hidden path="yearYn"/>
    <form:hidden path="compIds"/>
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findDeployTargetYn"><spring:message code="word.deployObjectYn"/></label>
				<form:select path="findDeployTargetYn" class="select wx80" >
					<option value=""><spring:message code="word.all" /></option>
					<form:options items="${codeUtil:getCodeList('001')}" itemLabel="codeNm" itemValue="codeId"></form:options>
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw"></div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="saveData();return false;"><spring:message code="button.saveAll"/></a>
			<a href="#" class="new" onclick="addRow();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" onclick="deleteDataChk();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	<div class="sch-bx mt20">
		<ul>
			<li>
				<label for="codeGrpNmText"><spring:message code="word.codeGrpNm"/></label>
				<span id="codeGrpNmText"><b><c:out value="${searchVO.codeGrpNm}"></c:out></b></span>
			</li>
			<li>
				<label for="findCodeUseYn"><spring:message code="word.useYn"/></label>
				<form:select path="findCodeUseYn" class="select wx80" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchCodeList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw"></div>
	<div class="gridContainer">
		<table id="listChild"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-btn" id="btns">
			<a href="#" class="save" onclick="saveCodeData();return false;"><spring:message code="button.saveAll"/></a>
			<a href="#" class="new" onclick="addCodeRow();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" onclick="deleteCodeDataChk();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
</form:form>

