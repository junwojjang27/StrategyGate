<!--
*************************************************************************
* CLASS 명	: SurvAnsPoolList
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-02
* 기	능	: 설문답변pool List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-02				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="survAnsPoolVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
var globalVariableForId = "";
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/survey/survAnsPool/survAnsPoolList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height2}",
		colModel	:	[
						{name:"itemPoolId",	index:"itemPoolId",		width:100,	hidden:true},
						{name:"itemPoolNm",	index:"itemPoolNm",	width:300,	align:"left",	label:"<spring:message code="word.survAnsItemNm" />",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.itemPoolId) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"itemCntId",		index:"itemCntId",		width:100,	hidden:true},
						{name:"itemCntNm",	index:"itemCntNm",		width:100,	align:"center",	label:"<spring:message code="word.ansItemCnt" />"},
						{name:"mainItemYn",	index:"mainItemYn",	width:100,	align:"center",	label:"<spring:message code="word.ansItemYn" />",
							editable:true, edittype:"select", formatter:"select", editroles:{required:true},
							editoptions:{value:getMainItemYnSelect()}
						},
						{name:"sortOrder",		index:"sortOrder",		width:100,	align:"center",	label:"<spring:message code="word.sortOrder" />",
							editable:true, edittype:"text", editrules:{required:true, integer:true}, editoptions:{maxlength:5}
						}
						],
		rowNum		: ${jqgrid_rownum_max},
		sortname	: "sortOrder",
		sortorder	: "asc",
		cellEdit	: true,
		multiselect	: true,
		loadComplete : function() {
			$("#hiddenUseYn").val($("#findUseYn").val());
			if($("#findUseYn").val() == "N") {
				$(".tbl-btn").find("a:not(.save)").addClass("disabled").css("cursor","default");
				$(".tbl-btn").find("a#addData").removeClass("new");
				$(".tbl-btn").find("a#deleteData").removeClass("delete");
			}else{
				$(".tbl-btn").find("a:not(.save)").removeClass("disabled").css("cursor","pointer");
				$(".tbl-btn").find("a#addData").addClass("new");
				$(".tbl-btn").find("a#deleteData").addClass("delete");
			}

			<%-- 신규등록 및 수정시 해당 id로 조회 되도록 프로세스 추가 --%>
		 	var id = "";
			var rowdata;

			var ids = $("#list").jqGrid('getDataIDs');
			if(0 < ids.length){
				if(isNotEmpty(globalVariableForId)) {
					id = globalVariableForId;
				}else{
					for(var i=0 ; i<ids.length ; i++){
						rowdata = $("#list").getRowData(ids[i]);
						id = rowdata.itemPoolId;
						break;
					}
				}
				showDetail(id);
			}else{
				addData();
			}
			<%-- 신규등록 및 수정시 해당 id로 조회 되도록 프로세스 추가 end --%>
		}
	});
	setMaxLength("form");

	$("#itemCntId").change(function(){
		setItemCntDiv("chg");
	});

	$(".tbl-btn").find("a:not(.save)").bind('click', function(event){
		if($("#findUseYn").val() == "N") {
			event.preventDefault();
			return false;
		}else{
			eval($(this).attr("id")+"()");
		}
	});
});

<%--  set --%>
function getMainItemYnSelect(){
	var selectStr= "Y:Y;N:N";
	return selectStr;
}

<%--  항목수 리스트 입력폼 생성 --%>
function setItemCntDiv(gb){
	if(gb == "chg") {
		setItemCnt();
	}else{
		sendAjax({
			"url" : "${context_path}/system/system/survey/survAnsPool/selectItemList.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "setItemCnt"
		});
	}
}

<%--  항목수 리스트 입력폼 생성 처리 --%>
function setItemCnt(data) {
	$("#trHeader").nextAll().remove();
	var $trTemp, $tr;
	$trTemp = '<tr id="trTemplate" class="hide">';
	$trTemp += '<td class="txt-c"><label class="itemNumLabel"></label></td>';
	$trTemp += '<td><input type="text" name="itemContent" id="itemContent" class="wp99 pl5 pdr5" maxlength="300"/></td>';
	$trTemp += '</tr>';
	$("#itemCntDiv tbody").append($trTemp);
	if(typeof data == "undefined") {
		createItemDetailTr();
	}else{
		var listCnt = data.list.length;
		if(listCnt == 0) {
			createItemDetailTr();
		}else{
			var item;
			for(var i in data.list) {
				item = data.list[i];
				$tr = $("#trTemplate").clone().removeAttr("id").removeClass("hide");
				$tr.find(".itemNumLabel").text(item.itemNum);
				$tr.find("input[name=itemContent]").val(item.itemContent);

				$("#itemCntDiv tbody").append($tr);
			}
		}
	}
	$("#trTemplate").remove();
}

function createItemDetailTr() {
	var $tr;
	var itemCntSel = parseInt($("#itemCntId").val());
	for(var i=1; i<=itemCntSel; i++) {
		$tr = $("#trTemplate").clone().removeAttr("id").removeClass("hide");
		$tr.find(".itemNumLabel").text(i);
		$tr.find("input[name=itemContent]").val("");

		$("#itemCntDiv tbody").append($tr);
	}
}

// 목록 조회
function searchList() {
 	reloadGrid("list", "form");
}

// 상세 조회
function showDetail(key) {
	var f = document.form;
	f.itemPoolId.value = key;

	sendAjax({
		"url" : "${context_path}/system/system/survey/survAnsPool/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	var dataVO = data.dataVO;

	voToForm(dataVO, "form", ["itemPoolId","itemPoolNm","itemCntId","useYn","mainItemCnt"]);
	$("#itemPoolNm").focus();
	// 항목수 정보 세팅
	setItemCntDiv();
}

// 일괄저장
function saveAllData() {
	var $grid = $("#list");
	var ids = $grid.jqGrid("getDataIDs");
	if(ids.length == 0) {
		$.showMsgBox("<spring:message code="errors.noDataToSaveAll"/>");
		return false;
	}
	
	if(!gridToFormChanged("list", "form", true)) return false;

	var rowObj,  rowObj2;
	var duplicationCnt = 0;
	var dupMsg = "";
	$(ids).each(function(i,el) {
		duplicationCnt = 0;
		$(ids).each(function(j,el2) {
			rowObj = $grid.jqGrid("getRowData",el);
			rowObj2 = $grid.jqGrid("getRowData",el2);
			if(rowObj.itemCntId == rowObj2.itemCntId) {
				if(rowObj2.mainItemYn == "Y") {
					duplicationCnt += 1;
				}
			}
		});
		if(duplicationCnt > 1) {
			dupMsg = "<spring:message code="system.system.survey.survAnsPool.error1"/>";
			return false;
		}
	});

	if(isNotEmpty(dupMsg)) {
		$.showMsgBox(dupMsg);
		return false;
	}else{
		$.showConfirmBox("<spring:message code="common.saveAll.msg"/>", function() {
			sendAjax({
				"url" : "${context_path}/system/system/survey/survAnsPool/saveAllSurvAnsPool.do",
				"data" : getFormData("form"),
				"doneCallbackFunc" : "checkResult"
			});
		});
	}
}

// 등록
function addData() {
	resetForm("form", ["itemPoolId","itemPoolNm","itemCntId"]);
	$("#itemPoolNm").focus();
	setItemCntDiv("chg");
}

// 저장
function saveData() {
	if($("#hiddenUseYn").val()=="N" && $("#itemPoolId").val() == "") {
		$.showMsgBox("<spring:message code="errors.noUseNoSave"/>");
		return false;
	}

	var f = document.form;
	if(!validateSurvAnsPoolVO(f)) {
		return;
	}

	var validMsg = "";
	var maxlength = 0;
	$("input[name=itemContent]").each(function(i,el){
		if(isEmpty($(el).val())) {
			validMsg += "<c:set var="messageArg"><spring:message code="word.ansContent"/></c:set><spring:message code="errors.required" arguments="${messageArg}"/>";
			return false;
		}
		if(lengthb($(el).val()) > $(el).attr("maxlength")) {

			validMsg += "<c:set var="messageArg"><spring:message code="word.ansContent"/></c:set><c:set var="messageArg2">300</c:set><spring:message code="errors.maxByteLength" arguments="${messageArg}, ${messageArg2}" htmlEscape="false"/>";
			return false;
		}
	});
	
	if($("#hiddenUseYn").val()=="N" && $("#useYn").val()=="Y"){
		sendAjax({
			"url" : "${context_path}/system/system/survey/survAnsPool/mainItemCnt.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "setMainItemCnt"
		});
	}
	
	if(isNotEmpty(validMsg)) {
		$.showMsgBox(validMsg);
		return false;
	}else{
		$.showConfirmBox("<spring:message code="common.save.msg"/>", function() {
			sendAjax({
				"url" : "${context_path}/system/system/survey/survAnsPool/saveSurvAnsPool.do",
				"data" : getFormData("form"),
				"doneCallbackFunc" : "checkResult"
			});
		});
	}
}

// 미사용에서 사용으로 변경 시 답변항목대표 수 검사
function setMainItemCnt(data) {
	$("#mainItemCnt").val(data.dataVO.mainItemCnt);
	
	if($("#mainItemCnt").val()!="0"){
		$.showMsgBox("<spring:message code="system.system.survey.survAnsPool.error1"/>");
		return false;
	}
}

// 삭제
function deleteData() {
	if(deleteDataToForm("list", "itemPoolId", "form")) {
		$.showConfirmBox("<spring:message code="common.delete.msg"/>", "doDeleteData");
	}
}

// 삭제 처리
function doDeleteData() {
	var delList = [];
	$("#form").find("[name=keys]").each(function(i, e) {
		delList.push($(this).val());
	});

	sendAjax({
		"url" : "${context_path}/system/system/survey/survAnsPool/deleteSurvAnsPool.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

<%-- 요청처리 callback --%>
function checkResult(data) {
	$.showMsgBox(data.msg);
	globalVariableForId = data.key;
	if(data.result == AJAX_SUCCESS) {
		searchList();
	}
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="itemPoolId"/>
 	<input type="hidden" id="hiddenUseYn"/><%-- 미사용시 체크에 사용 --%>
	<input type="hidden" id="mainItemCnt"/><%-- 미사용에서 사용 전환 시 답변항목 대표 수 체크에 사용 --%>
	
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findUseYn"><spring:message code="word.useYn"/></label>
				<form:select path="findUseYn" class="select wx100" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
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
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="saveAllData();return false;"><spring:message code="button.saveAll"/></a>
			<a href="#" class="new" id="addData" onclick="return false;"><spring:message code="button.create"/></a>
			<a href="#" class="delete" id="deleteData" onclick="return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleSurvAnsPoolNm"><spring:message code="word.survAnsPool" /></div>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="15%"/>
					<col width="35%"/>
					<col width="15%"/>
					<col width="35%"/>
				</colgroup>
				<tbody>
					<tr>
						<th scope="row"><label for="itemPoolNm"><spring:message code="word.survAnsItemNm" /></label><span class="red">(*)</span></th>
						<td colspan="3"><form:input path="itemPoolNm" class="t-box01" maxlength="300"/></td>
					</tr>
					<tr>
						<th scope="row"><label for="itemCntId"><spring:message code="word.ansItemCnt" /></label></th>
						<td >
							<form:select path="itemCntId" class="select t-box01" items="${codeUtil:getCodeList('370')}" itemLabel="codeNm" itemValue="codeId">
							</form:select>
						</td>
						<th scope="row"><label for="useYn"><spring:message code="word.useYn" /></label></th>
						<td >
							<form:select path="useYn" class="select t-box01" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
							</form:select>
						</td>
					</tr>

				</tbody>
			</table>
		</div>
		<div class="tbl-type01 mt10" id="itemCntDiv">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="15%"/>
					<col width="85%"/>
				</colgroup>
				<tbody>
					<tr id="trHeader">
						<th class="txt-c p0"><spring:message code="word.orderNum" /></th>
						<th class="txt-c p0"><spring:message code="word.ansContent" /><span class="red">(*)</span></th>
					</tr>
				</tbody>
			</table>
		</div>

		<div class="tbl-bottom">
			<div class="tbl-wbtn">
			</div>
			<div class="tbl-btn">
				<a href="#" class="save" onclick="saveData();return false;"><spring:message code="button.save"/></a>
			</div>
		</div>
	</div>
</form:form>

