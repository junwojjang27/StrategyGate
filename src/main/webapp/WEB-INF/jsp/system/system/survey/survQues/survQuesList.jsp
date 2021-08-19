<!--
*************************************************************************
* CLASS 명	: SurvQuesList
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-10
* 기	능	: 설문질문등록 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-10				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="survQuesVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
var globalVariableForId = "";
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/survey/survQues/survQuesList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"290",
		colModel	:	[
						{name:"quesSeq",				index:"quesSeq",			width:100,	align:"center",	label:"<spring:message code="word.questionNum" />"},
						{name:"quesId",					index:"quesId",				hidden:true},
						{name:"quesNm",				index:"quesNm",			width:300,	align:"left",		label:"<spring:message code="word.question" />",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.quesId) + "\",\"" + removeNull(rowObject.surveyId) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"quesGbnId",			index:"quesGbnId",			hidden:true},
						{name:"quesGbnNm",			index:"quesGbnNm",		width:100,	align:"center",	label:"<spring:message code="word.questionGbn" />"},
						{name:"itemCntId",				index:"itemCntId",			hidden:true},
						{name:"itemCntNm",			index:"itemCntNm",			width:100,	align:"center",	label:"<spring:message code="word.itemCnt" />"},
						{name:"itemCheckGbnId",		index:"itemCheckGbnId",	hidden:true},
						{name:"itemCheckGbnNm",	index:"itemCheckGbnNm",width:100,	align:"center",	label:"<spring:message code="word.selectGbn" />"},
						{name:"quesLinkYn",			index:"quesLinkYn",			width:100,	align:"center",	label:"<spring:message code="word.linkYn" />"}
						],
		rowNum		: ${jqgrid_rownum_max},
		cellEdit	: true,
		multiselect	: true,
		loadComplete : function() {
			$("#surveyId").val($('#findSurveyId').val());
			selectCloseYn(); <%-- 마감정보 set --%>
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
						id = rowdata.quesId;
						break;
					}
				}
				showDetail(id,$("#surveyId").val());
			}else{
				addData();
			}
			<%-- 신규등록 및 수정시 해당 id로 조회 되도록 프로세스 추가 end --%>
		}
	});
	setMaxLength("form");
	$("#quesSeq").numericOnly();

	$("#quesGbnId").change(function(){
		setQuesGbnId(this);
	});
	$("#itemCntId").change(function(){
		setItemCntDiv("chg");
	});

	$(".tbl-btn").find("a").bind('click', function(event){
		if($("#closeYn").val() == "Y") {
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
		$("#poolBtn1").addClass("hide");
		$("#poolBtn2").removeClass("hide");
	}else{
		$("#itemCntId").prop("disabled",false);
		$("#itemCheckGbnId").prop("disabled",false);
		$("#itemCheckGbnDIv").removeClass("hide");
		$("#poolBtn1").removeClass("hide");
		$("#poolBtn2").addClass("hide");

		if(crud == "r") {
			setItemCntDiv();	<%-- 조회정보 set --%>
		}else{
			$("#itemCntId option:eq(0)").prop("selected",true);
			$("#itemCheckGbnId option:eq(0)").prop("selected",true);
			setItemCntDiv("chg");	<%-- 신규등록 항목 폼set --%>
		}
	}
}

<%--  항목수 리스트 입력폼 생성 --%>
function setItemCntDiv(gbn) {
	if(gbn == "chg") {	<%-- 신규등록 항목 폼set --%>
		setItemCnt();
	}else if(gbn == "pop") {	<%-- 팝업에서 itemPoolId set 후 호출 값set --%>
		sendAjax({
			"url" : "${context_path}/system/system/survey/survAnsPool/selectItemList.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "setItemCnt"
		});
	}else{	<%-- 상세조회 값set --%>
		sendAjax({
			"url" : "${context_path}/system/system/survey/survQues/selectItemList.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "setItemCnt"
		});
	}
}

<%--  항목 입력폼 생성 및 값 set--%>
function setItemCnt(itemData) {
	sendAjax({
		"url" : "${context_path}/system/system/survey/survQues/selectLinkQuesList_json.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : function(data){
			$("#trHeader").nextAll().remove();
			var $tr = "", item = "";
			if(typeof itemData == "undefined") {
				var itemCntSel = parseInt($("#itemCntId").val());
				for(var i=1; i<=itemCntSel; i++) {
					$tr = '<tr>';
					$tr += '<td class="txt-c"><label class="itemNumLabel">'+i+'</label></td>';
					$tr += '<td><input type="text" name="itemContent" id="itemContent" class="wp97 pl5 pdr5" maxlength="300" value=""/></td>';
					$tr += '<td><select name="linkQuesId" id="linkQuesId" class="wp100">';
					$tr += '<option value="empty"><spring:message code="word.noData" /></option>';
					$(data.list).each(function(i,e){
							$tr += '<option value="'+e.quesId+'">'+e.quesNm+'</option>';
					});
					$tr += '</select></td>';

					$("#itemCntDiv tbody").append($tr);
				}
			}else{
				for(var i in itemData.list) {
					item = itemData.list[i];
					$tr = '<tr>';
					$tr += '<td class="txt-c"><label class="itemNumLabel">'+item.itemNum+'</label></td>';
					$tr += '<td><input type="text" name="itemContent" id="itemContent" class="wp97 pl5 pdr5" maxlength="300" value="'+item.itemContent+'"/></td>';
					$tr += '<td><select name="linkQuesId" id="linkQuesId" class="wp100">';
					$tr += '<option value="empty"><spring:message code="word.noData" /></option>';
					$(data.list).each(function(i,e){
						if(item.linkQuesId == e.quesId) {
							$tr += '<option value="'+e.quesId+'" selected>'+e.quesNm+'</option>';
						}else{
							$tr += '<option value="'+e.quesId+'">'+e.quesNm+'</option>';
						}
					});
					$tr += '</select></td>';

					$("#itemCntDiv tbody").append($tr);
				}
			}
		}
	});
}

function selectCloseYn() {
	sendAjax({
		"url" : "${context_path}/system/system/survey/survReg/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setCloseYn"
	});
}

function setCloseYn(data) {
	$("#closeYn").val(data.dataVO.closeYn);
	if(data.dataVO.closeYn == "Y") {
		$(".tbl-btn").find("a").addClass("disabled").css("cursor","default");
		$(".tbl-btn").find("a#addData").removeClass("new");
		$(".tbl-btn").find("a#deleteData").removeClass("delete");
		$(".tbl-btn").find("a#popSurvAnsPool").removeClass("view");
		$(".tbl-btn").find("a#popSurvQuesPool").removeClass("view");
		$(".tbl-btn").find("a#saveData").removeClass("save");
		$(".page-noti").removeClass("hide");
	}else{
		$(".tbl-btn").find("a").removeClass("disabled").css("cursor","pointer");
		$(".tbl-btn").find("a#addData").addClass("new");
		$(".tbl-btn").find("a#deleteData").addClass("delete");
		$(".tbl-btn").find("a#popSurvAnsPool").addClass("view");
		$(".tbl-btn").find("a#popSurvQuesPool").addClass("view");
		$(".tbl-btn").find("a#saveData").addClass("save");
		$(".page-noti").addClass("hide");
	}
}

// 목록 조회
function searchList() {
 	reloadGrid("list", "form");
}

// 상세 조회
function showDetail(quesId, surveyId) {
	var f = document.form;
	f.quesId.value = quesId;

	sendAjax({
		"url" : "${context_path}/system/system/survey/survQues/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	var dataVO = data.dataVO;

	voToForm(dataVO, "form", ["quesId","quesSeq","quesNm","quesGbnId","itemCntId","itemCheckGbnId","quesLinkYn","quesGrpId"]);
	$("#quesNum").focus();

	setQuesGbnId($("#quesGbnId"),"r");
}

// 등록
function addData() {
	resetForm("form", ["quesId","quesSeq","quesNm","quesGbnId","itemCntId","itemCheckGbnId","quesLinkYn","quesGrpId"]);
	$("#quesNum").focus();
	setQuesGbnId($("#quesGbnId"));
}

// 저장
function saveData() {
	var f = document.form;
	if(!validateSurvQuesVO(f)) {
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
				"url" : "${context_path}/system/system/survey/survQues/saveSurvQues.do",
				"data" : getFormData("form"),
				"doneCallbackFunc" : "checkResult"
			});
		});
	}
}

// 삭제
function deleteData() {
	if(deleteDataToForm("list", "quesId", "form")) {
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
		"url" : "${context_path}/system/system/survey/survQues/deleteSurvQues.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

<%-- 설문답변POOL --%>
function popSurvAnsPool() {
	openFancybox({
		"url" : "${context_path}/system/system/survey/survAnsPool/popSurvAnsPoolList.do",
		"data" : getFormData("form")
	});
}
<%-- 설문질문POOL --%>
function popSurvQuesPool() {
	openFancybox({
		"url" : "${context_path}/system/system/survey/survQuesPool/popSurvQuesPoolList.do",
		"data" : getFormData("form")
	});
}

function setItemCntDivForQuesPool() {
	sendAjax({
		"url" : "${context_path}/system/system/survey/survQuesPool/selectItemList.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setItemCntForQuesPool"
	});
}

function setItemCntForQuesPool(itemData) {
	sendAjax({
		"url" : "${context_path}/system/system/survey/survQues/selectLinkQuesList_json.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : function(data){
			$("#trHeader").nextAll().remove();
			var $tr = "", item = "";
			if(typeof itemData == "undefined") {
				var itemCntSel = parseInt($("#itemCntId").val());
				for(var i=1; i<=itemCntSel; i++) {
					$tr = '<tr>';
					$tr += '<td class="txt-c"><label class="itemNumLabel">'+i+'</label></td>';
					$tr += '<td><input type="text" name="itemContent" id="itemContent" class="wp97 pl5 pdr5" maxlength="300" value=""/></td>';
					$tr += '<td><select name="linkQuesId" id="linkQuesId" class="wp100">';
					$tr += '<option value="empty"><spring:message code="word.noData" /></option>';
					$(data.list).each(function(i,e){
							$tr += '<option value="'+e.quesId+'">'+e.quesNm+'</option>';
					});
					$tr += '</select></td>';

					$("#itemCntDiv tbody").append($tr);
				}
			}else{
				for(var i in itemData.list) {
					item = itemData.list[i];
					$tr = '<tr>';
					$tr += '<td class="txt-c"><label class="itemNumLabel">'+item.itemNum+'</label></td>';
					$tr += '<td><input type="text" name="itemContent" id="itemContent" class="wp97 pl5 pdr5" maxlength="300" value="'+item.itemContent+'"/></td>';
					$tr += '<td><select name="linkQuesId" id="linkQuesId" class="wp100">';
					$tr += '<option value="empty"><spring:message code="word.noData" /></option>';
					$(data.list).each(function(i,e){
						if(item.linkQuesId == e.quesId) {
							$tr += '<option value="'+e.quesId+'" selected>'+e.quesNm+'</option>';
						}else{
							$tr += '<option value="'+e.quesId+'">'+e.quesNm+'</option>';
						}
					});
					$tr += '</select></td>';

					$("#itemCntDiv tbody").append($tr);
				}
			}
		}
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
 	<form:hidden path="surveyId"/>
	<form:hidden path="quesId"/>
	<form:hidden path="itemPoolId"/><%-- 팝업에서사용 --%>
	<form:hidden path="quesPoolId"/><%-- 팝업에서사용 --%>
	<form:hidden path="closeYn"/><%-- 마감체크용 --%>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findSurveyId"><spring:message code="word.survNm"/></label>
				<form:select path="findSurveyId" class="select wx400" >
					<form:options items="${surveyList}"  itemLabel="surveyNm" itemValue="surveyId" />
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
	<div class="page-noti">
		<ul>
			<li><spring:message code="system.system.survey.closeY"/></li>
		</ul>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleSurvQuesNm"><spring:message code="word.survQuesReg" /></div>
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
						<th scope="row"><label for="quesSeq"><spring:message code="word.questionNum" /><span class="red">(*)</span></label></th>
						<td colspan="3"><form:input path="quesSeq" class="wx422" maxlength="5"/></td>
					</tr>
					<tr>
						<th scope="row"><label for="quesNm"><spring:message code="word.question" /><span class="red">(*)</span></label></th>
						<td colspan="3"><form:input path="quesNm" class="t-box01" maxlength="1000"/></td>
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
						<th scope="row"><label for="quesLinkYn"><spring:message code="word.linkYn" /></label></th>
						<td >
							<form:select path="quesLinkYn" class="select t-box01">
								<form:option value="N">N</form:option>
								<form:option value="Y">Y</form:option>
							</form:select>
						</td>
					</tr>

				</tbody>
			</table>
		</div>
		<div class="tbl-bottom" id="poolBtn1">
			<div class="tbl-btn">
				<a href="#" class="view" id="popSurvAnsPool" onclick="return false;"><spring:message code="word.survAnsPool"/></a>
				<a href="#" class="view" id="popSurvQuesPool" onclick="return false;"><spring:message code="word.survQuesPool"/></a>
			</div>
		</div>
		<div class="tbl-bottom hide" id="poolBtn2">
			<div class="tbl-btn">
				<a href="#" class="view" onclick="popSurvQuesPool();return false;"><spring:message code="word.survQuesPool"/></a>
			</div>
		</div>
		<div id="itemCheckGbnDIv">
			<div class="tbl-type01 mt10" id="itemCntDiv">
				<table summary="">
					<caption></caption>
					<colgroup>
						<col width="15%"/>
						<col width="42.5%"/>
						<col width="42.5%"/>
					</colgroup>
					<tbody>
						<tr id="trHeader">
							<th class="txt-c p0"><spring:message code="word.orderNum" /></th>
							<th class="txt-c p0"><spring:message code="word.ansContent" /><span class="red">(*)</span></th>
							<th class="txt-c p0"><spring:message code="word.linkQues" /></th>
						</tr>
					</tbody>
				</table>
			</div>
		</div>

		<div class="tbl-bottom">
			<div class="tbl-wbtn">
			</div>
			<div class="tbl-btn">
				<a href="#" class="save" onclick="return false;" id="saveData"><spring:message code="button.save"/></a>
			</div>
		</div>
	</div>
</form:form>

