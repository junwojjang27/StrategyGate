<!--
*************************************************************************
* CLASS 명	: SurvQuesPoolList
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-04
* 기	능	: 설문질문pool List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-04				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="survQuesPoolVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
var globalVariableForId = "";
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/survey/survQuesPool/survQuesPoolList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height2}",
		colModel	:	[
						{name:"quesPoolId",		index:"quesPoolId",		hidden:true},
						{name:"quesPoolNm",		index:"quesPoolNm",		width:300,	align:"left",	label:"<spring:message code="word.question" />",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.quesPoolId) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
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
		cellEdit	: true,
		multiselect	: true,
		loadComplete : function() {
			$("#hiddenUseYn").val($("#findUseYn").val());
			if($("#findUseYn").val() == "N") {
				$(".tbl-btn").find("a:not(.save)").addClass("disabled").css("cursor","default");
				$(".tbl-btn").find("a#addData").removeClass("new");
				$(".tbl-btn").find("a#deleteData").removeClass("delete");
				$(".tbl-btn").find("a#popSurvAnsPool").removeClass("view");
			}else{
				$(".tbl-btn").find("a:not(.save)").removeClass("disabled").css("cursor","pointer");
				$(".tbl-btn").find("a#addData").addClass("new");
				$(".tbl-btn").find("a#deleteData").addClass("delete");
				$(".tbl-btn").find("a#popSurvAnsPool").addClass("view");
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
						id = rowdata.quesPoolId;
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
	$("#sortOrder").numericOnly();

	$("#quesGbnId").change(function(){
		setQuesGbnId(this);
	});
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

function setQuesGbnId(obj, crud) {
	if($(obj).val() == "002") { <%-- 주관식일 경우 --%>
		$("#itemCntId").prop("disabled",true);
		$("#itemCheckGbnId").prop("disabled",true);
		$("#trHeader").nextAll().remove();	<%-- 항목 행 삭제(invalid 걸림문제) --%>
		$("#trTemplate").remove();	<%-- 복사용 행 삭제(invalid 걸림문제) --%>
		$("#itemCheckGbnDIv").addClass("hide");	<%-- 설문답변Pool 버튼, 항목 입력폼 숨김 --%>
	}else{
		$("#itemCntId").prop("disabled",false);
		$("#itemCheckGbnId").prop("disabled",false);
		$("#itemCheckGbnDIv").removeClass("hide");

		if(crud == "r") {
			setItemCntDiv();	<%-- 조회정보 set --%>
		}else{
			setItemCntDiv("chg");	<%-- 항목 입력폼 set --%>
		}
	}
}
<%--  항목수 리스트 입력폼 생성 --%>
function setItemCntDiv(gbn) {
	if(gbn == "chg") {
		setItemCnt();
	}else if(gbn == "pop") {	<%-- 팝업에서 itemPoolId set 후 호출 값set --%>
		sendAjax({
			"url" : "${context_path}/system/system/survey/survAnsPool/selectItemList.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "setItemCnt"
		});
	}else{	<%-- 상세조회 값set --%>
		sendAjax({
			"url" : "${context_path}/system/system/survey/survQuesPool/selectItemList.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "setItemCnt"
		});
	}
}

<%--  항목 입력폼 생성 처리 & 값 set --%>
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

<%--  항목 입력폼 생성 처리 --%>
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
	f.quesPoolId.value = key;


	sendAjax({
		"url" : "${context_path}/system/system/survey/survQuesPool/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	var dataVO = data.dataVO;

	voToForm(dataVO, "form", ["quesPoolId","quesPoolNm","quesGbnId","itemCntId","itemCheckGbnId","sortOrder","useYn"]);
	$("#quesPoolNm").focus();

	setQuesGbnId($("#quesGbnId"),"r");
}

// 등록
function addData() {
	resetForm("form", ["quesPoolId","quesPoolNm","quesGbnId","itemCntId","itemCheckGbnId","sortOrder","useYn"]);
	$("#quesPoolNm").focus();
	setQuesGbnId($("#quesGbnId"));
}

// 저장
function saveData() {
	if($("#hiddenUseYn").val()=="N" && $("#quesPoolId").val() == "") {
		$.showMsgBox("<spring:message code="errors.noUseNoSave"/>");
		return false;
	}

	var f = document.form;
	if(!validateSurvQuesPoolVO(f)) {
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
	if(isNotEmpty(validMsg)) {
		$.showMsgBox(validMsg);
		return false;
	}else{
		$.showConfirmBox("<spring:message code="common.save.msg"/>", function() {
			sendAjax({
				"url" : "${context_path}/system/system/survey/survQuesPool/saveSurvQuesPool.do",
				"data" : getFormData("form"),
				"doneCallbackFunc" : "checkResult"
			});
		});
	}
}

// 삭제
function deleteData() {
	if(deleteDataToForm("list", "quesPoolId", "form")) {
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
		"url" : "${context_path}/system/system/survey/survQuesPool/deleteSurvQuesPool.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

function popSurvAnsPool() {
	openFancybox({
		"url" : "${context_path}/system/system/survey/survAnsPool/popSurvAnsPoolList.do",
		"data" : getFormData("form")
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
 	<form:hidden path="quesPoolId"/>
 	<form:hidden path="itemPoolId"/><%-- 팝업에서사용 --%>
 	<input type="hidden" id="hiddenUseYn"/><%-- 미사용시 체크에 사용 --%>

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
			<a href="#" class="new" id="addData" onclick="return false;"><spring:message code="button.create"/></a>
			<a href="#" class="delete" id="deleteData" onclick="return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleSurvQuesPoolNm"><spring:message code="word.survQuesPool" /></div>
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
						<th scope="row"><label for="quesPoolNm"><spring:message code="word.question" /></label><span class="red">(*)</span></th>
						<td colspan="3"><form:input path="quesPoolNm" class="t-box01" maxlength="1000"/></td>
					</tr>
					<tr>
						<th scope="row"><label for="quesGbnId"><spring:message code="word.questionGbn" /></label></th>
						<td >
							<form:select path="quesGbnId" class="select t-box01" items="${codeUtil:getCodeList('371')}" itemLabel="codeNm" itemValue="codeId">
							</form:select>
						</td>
						<th scope="row"><label for="itemCntId"><spring:message code="word.itemCnt" /></label></th>
						<td >
							<form:select path="itemCntId" class="select t-box01" items="${codeUtil:getCodeList('370')}" itemLabel="codeNm" itemValue="codeId">
							</form:select>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="itemCheckGbnId"><spring:message code="word.selectGbn" /></label></th>
						<td >
							<form:select path="itemCheckGbnId" class="select t-box01" items="${codeUtil:getCodeList('372')}" itemLabel="codeNm" itemValue="codeId">
							</form:select>
						</td>
						<th scope="row"><label for="sortOrder"><spring:message code="word.sortOrder" /></label></th>
						<td ><form:input path="sortOrder" class="t-box01" maxlength="5"/></td>
					</tr>
					<tr>
						<th scope="row"><label for="useYn"><spring:message code="word.useYn" /></label></th>
						<td colspan="3">
							<form:select path="useYn" class="select wx422" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
							</form:select>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div id="itemCheckGbnDIv">
			<div class="tbl-bottom">
				<div class="tbl-btn">
					<a href="#" class="view" id="popSurvAnsPool" onclick="return false;"><spring:message code="word.survAnsPool"/></a>
				</div>
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

