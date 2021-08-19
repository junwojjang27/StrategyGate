<!--
*************************************************************************
* CLASS 명	: SurvRegList
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-05
* 기	능	: 설문등록 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-05				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="survRegVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
var globalVariableForId = "";
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/survey/survReg/survRegList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height2}",
		colModel	:	[
						{name:"surveyId",			index:"surveyId",		hidden:true},
						{name:"surveyNm",			index:"surveyNm",		width:300,	align:"left",	label:"<spring:message code="word.survNm" />",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.surveyId) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"surveyTypeNm",	index:"surveyTypeNm",width:100,	align:"center",	label:"<spring:message code="word.survType" />"},
						{name:"startDt",				index:"startDt",			width:100,	align:"center",	label:"<spring:message code="word.survStartDt" />"},
						{name:"endDt",				index:"endDt",			width:100,	align:"center",	label:"<spring:message code="word.survEndDt" />"},
						{name:"userCnt",			index:"userCnt",			hidden:true},
						{name:"quesCnt",			index:"quesCnt",			hidden:true},
						{name:"closeYn",			index:"closeYn",			hidden:true}
						],
		rowNum		: ${jqgrid_rownum_max},
		cellEdit	: true,
		multiselect	: true,
		loadComplete : function() {
			$("#hiddenUseYn").val($("#findUseYn").val());
			if($("#findUseYn").val() == "N") {
				$(".tbl-btn").find("a:not(.save):not(.edit)").addClass("disabled").css("cursor","default");
				$(".tbl-btn").find("a#addData").removeClass("new").attr("onclick","return false;");
				$(".tbl-btn").find("a#deleteData").removeClass("delete").attr("onclick","return false;");
			}else{
				$(".tbl-btn").find("a:not(.save):not(.edit)").removeClass("disabled").css("cursor","pointer");
				$(".tbl-btn").find("a#addData").addClass("new").attr("onclick","addData();return false;");
				$(".tbl-btn").find("a#deleteData").addClass("delete").attr("onclick","deleteData();return false;");
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
						id = rowdata.surveyId;
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

	<%-- date선택 팝업화면 --%>
	$(".datepicker").datepicker();

	<%-- byte check --%>
	showBytes("startContent", "startContentBytes");
	showBytes("endContent", "endContentBytes");
});

// 목록 조회
function searchList() {
 	reloadGrid("list", "form");
}

// 상세 조회
function showDetail(key) {
	var f = document.form;
	f.surveyId.value = key;

	sendAjax({
		"url" : "${context_path}/system/system/survey/survReg/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	var dataVO = data.dataVO;

	voToForm(dataVO, "form", ["surveyId","surveyNm","surveyTypeId","startContent","endContent","startDt","endDt","surveyYear","useYn"]);
	$("#startContentBytes").text(lengthb($("#startContent").val()));
	$("#endContentBytes").text(lengthb($("#endContent").val()));
	$("#surveyId").focus();

	if($("#hiddenUseYn" == "Y")) {
		//마감처리
		if(dataVO.closeYn == "Y") {
			$(".tbl-btn").find("a#saveData").addClass("disabled").css("cursor","default");
			$(".tbl-btn").find("a#saveData").removeClass("save").attr("onclick","return false;");
			$(".page-noti").removeClass("hide");
		}else{
			$(".tbl-btn").find("a#saveData").removeClass("disabled").css("cursor","pointer");
			$(".tbl-btn").find("a#saveData").addClass("save").attr("onclick","saveData();return false;");
			$(".page-noti").addClass("hide");
		}
	}
}

// 설문복사
function copyData() {
	var $grid = $("#list");
	var ids = $grid.jqGrid("getDataIDs");
	var rowObj = "";
	var chkYn = "";
	var chkCnt = 0;
	$(ids).each(function(i,el){
		rowObj = $grid.jqGrid("getRowData",el);
		chkYn = $grid.find("input.cbox#jqg_list_"+el).is(':checked');
		if(chkYn) {
			chkCnt++;
			$("#copySurveyId").val(rowObj.surveyId);
		}
	});

	if(chkCnt == 0) {
		$.showMsgBox("<spring:message code="errors.noSelectedData"/>");
		return false;
	}else if(chkCnt > 1) {
		$.showMsgBox("<spring:message code="system.system.survey.survReg.error1"/>");
		return false;
	}

	$.showConfirmBox("<c:set var="messageArg"><spring:message code="word.survCopy"/></c:set><spring:message code="common.confirm.msg" arguments="${messageArg}"/>", function() {
		sendAjax({
			"url" : "${context_path}/system/system/survey/survReg/copySurvReg.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
	});
}

// 등록
function addData() {
	resetForm("form", ["surveyId","surveyNm","surveyTypeId","startContent","endContent","startDt","endDt","useYn"]);
	$("#startContentBytes").text(lengthb($("#startContent").val()));
	$("#endContentBytes").text(lengthb($("#endContent").val()));
	$("#surveyId").focus();

	$(".tbl-btn").find("a#saveData").addClass("save").css("cursor","pointer").attr("onclick","saveData();return false;");
	$(".page-noti").addClass("hide");
}

// 저장
function saveData() {
	if($("#hiddenUseYn").val()=="N" && $("#surveyId").val() == "") {
		$.showMsgBox("<spring:message code="errors.noUseNoSave"/>");
		return false;
	}

	var f = document.form;

	var oriStartDt = $("#startDt").val();
	var oriEndDt = $("#endDt").val();
	$("#startDt").val($("#startDt").val().replace(/\./g, ''));
	$("#endDt").val($("#endDt").val().replace(/\./g, ''));
	if(!validateSurvRegVO(f)) {
		$("#startDt").val(oriStartDt);
		$("#endDt").val(oriEndDt);
		return false;
	}

	if($("#startDt").val() > $("#endDt").val()) {
		$("#startDt").val(oriStartDt);
		$("#endDt").val(oriEndDt);
		$.showMsgBox("<spring:message code="errors.wrongPeriod"/>");
		return false;
	}

	$.showConfirmBox("<spring:message code="common.save.msg"/>", function() {
		sendAjax({
			"url" : "${context_path}/system/system/survey/survReg/saveSurvReg.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
	});
}

// 삭제
function deleteData() {
	var $grid = $("#list");
	var ids = $grid.jqGrid("getDataIDs");
	var rowObj = "";
	var chkYn = "";
	var closeYn = 0, userChk = 0, quesChk = 0;
	$(ids).each(function(i,el){
		rowObj = $grid.jqGrid("getRowData",el);
		chkYn = $grid.find("input.cbox#jqg_list_"+el).is(':checked');
		if(chkYn) {
			if(rowObj.closeYn == "Y") {
				closeYn++;
			}
			if(rowObj.userCnt > 0) {
				userChk++;
			}
			if(rowObj.quesCnt > 0) {
				quesChk++;
			}
		}
	});


	if(closeYn > 0) {
		$.showMsgBox("<spring:message code="system.system.survey.closeY"/>");
		return false;
	}

	if(userChk > 0 && quesChk > 0) {
		$.showMsgBox("<spring:message code="system.system.survey.survReg.info1" htmlEscape="false"/>");
		return false;
	}
	if(userChk > 0) {
		$.showMsgBox("<spring:message code="system.system.survey.survReg.info2"/>");
		return false;
	}
	if(quesChk > 0) {
		$.showMsgBox("<spring:message code="system.system.survey.survReg.info3"/>");
		return false;
	}

	if(deleteDataToForm("list", "surveyId", "form")) {
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
		"url" : "${context_path}/system/system/survey/survReg/deleteSurvReg.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

function popDatePicker(obj) {
	$('#'+obj).datepicker().datepicker("show");
}

<%-- 요청처리  callback --%>
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
 	<form:hidden path="copySurveyId"/>
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
		<div class="tbl-btn" id="gridBtns">
			<a href="#" class="edit" id="copyData" onclick="copyData();return false;"><spring:message code="button.survCopy"/></a>
			<a href="#" class="new" id="addData" onclick="addData();return false;"><spring:message code="button.create"/></a>
			<a href="#" class="delete" id="deleteData" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	<div class="page-noti">
		<ul>
			<li><spring:message code="system.system.survey.closeY"/></li>
		</ul>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleSurvRegNm"><spring:message code="word.survReg" /></div>
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
						<th scope="row"><label for="surveyNm"><spring:message code="word.survNm" /></label><span class="red">(*)</span></th>
						<td colspan="3"><form:input path="surveyNm" class="t-box01" maxlength="300"/></td>
					</tr>
					<tr>
						<th scope="row"><label for="surveyTypeId"><spring:message code="word.survType" /></label></th>
						<td >
							<form:select path="surveyTypeId" class="select t-box01" items="${codeUtil:getCodeList('373')}" itemLabel="codeNm" itemValue="codeId">
							</form:select>
						</td>
						<th scope="row">
							<label for="startDt"><spring:message code="word.survPeriod" /><span class="red">(*)</span></label>
						</th>
						<td>
							<form:input path="startDt" class="wx165 datepicker" readonly="true" maxlength="10" />
							<a href="#" class="btn-search btn-absolute" style="margin-left:-40px;" onclick="popDatePicker('startDt');return false;"><spring:message code="word.search"/></a>
							<a href="#" class="close ml0" onclick="resetInput(['startDt']);return false;"><spring:message code="word.delete"/></a>
							~
							<form:input path="endDt" class="wx165 datepicker" readonly="true" maxlength="10" />
							<a href="#" class="btn-search btn-absolute" style="margin-left:-40px;" onclick="popDatePicker('endDt');return false;"><spring:message code="word.search"/></a>
							<a href="#" class="close ml0" onclick="resetInput(['endDt']);return false;"><spring:message code="word.delete"/></a>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="startContent"><spring:message code="word.survOutline" /></label><span class="red">(*)</span></th>
						<td colspan="3">
							<p><form:textarea path="startContent" maxlength="4000"/></p>
							<p class="byte"><label id="startContentBytes">0</label><label> / 4000byte</label></p>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="endContent"><spring:message code="word.survClosingMent" /></label><span class="red">(*)</span></th>
						<td colspan="3">
							<p><form:textarea path="endContent" maxlength="4000"/></p>
							<p class="byte"><label id="endContentBytes">0</label><label> / 4000byte</label></p>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="surveyYear"><spring:message code="word.survYear" /></label></th>
						<td>
							<form:select path="surveyYear" class="select wx422" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
							</form:select>
						</td>
						<th scope="row"><label for="useYn"><spring:message code="word.useYn" /></label></th>
						<td>
							<form:select path="useYn" class="select wx422" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
							</form:select>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="tbl-bottom">
			<div class="tbl-wbtn">
			</div>
			<div class="tbl-btn">
				<a href="#" class="save" id="saveData" onclick="saveData(); return false;"><spring:message code="button.save"/></a>
			</div>
		</div>
	</div>
</form:form>

