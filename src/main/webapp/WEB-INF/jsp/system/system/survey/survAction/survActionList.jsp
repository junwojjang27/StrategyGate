<!--
*************************************************************************
* CLASS 명	: SurvActionList
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-16
* 기	능	: 설문실시 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-16				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
var globalVariableForId = "";
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/survey/survAction/survActionList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"145",
		colModel	:	[
						{name:"surveyId",			index:"surveyId",			hidden:true},
						{name:"surveyNm",			index:"surveyNm",			width:300,	align:"left",	label:"<spring:message code="word.survNm"/>",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.surveyId) + "\",\""+removeNull(rowObject.surveyTypeId) + "\",\""+removeNull(rowObject.surveyEndYn) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"surveyTypeNm",	index:"surveyTypeNm",	width:100,	align:"center",	label:"<spring:message code="word.survType" />"},
						{name:"startDt",				index:"startDt",				width:100,	align:"center",	label:"<spring:message code="word.survStartDt" />"},
						{name:"endDt",				index:"endDt",				width:100,	align:"center",	label:"<spring:message code="word.survEndDt" />"},
						{name:"surveyEndYnNm",	index:"surveyEndYnNm",	width:100,	align:"center",	label:"<spring:message code="word.survEndYn" />"}

						],
		rowNum		: ${jqgrid_rownum_max},
		loadComplete : function() {
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
				emptyData();
			}
			<%-- 신규등록 및 수정시 해당 id로 조회 되도록 프로세스 추가 end --%>
		}
	});

});

// 목록 조회
function searchList() {
 	reloadGrid("list", "form");
}

// 상세 조회
function showDetail(surveyId) {
	$("#newForm").show();
	var f = document.form;
	f.surveyId.value = surveyId;

	sendAjax({
		"url" : "${context_path}/system/system/survey/survAction/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	var surveyVO = data.surveyVO;
	var grpList; // 질문그룹
	var quesList; // 질문
	var itemList; // 답변
	var linkList; // 연계질문
	var linkItemList; // 연계답변
	var bonbuList; // 본부목록 selectbox
	var str = "";
	var essayQuestionId = "QI00000"; // 주관식 답변항목코드
	var id = "";

	var now = new Date();
	var year = now.getFullYear()+"";
	var month = (now.getMonth()+1)+"";
	var mlength = month.length;
	month = mlength == 1 ? "0"+month : month;
	var day = now.getDate()+"";
	var dlength = day.length;
	day = dlength == 1 ? "0"+day : day;
	var realTime = year+month+day;

	var survEndFlag = false, survDateFlag = false, survCloseFlag = false, pageNotiFlag = false;
	$(".page-noti").find("li").remove();
	// 설문완료 여부 msg
	if(surveyVO.surveyEndYn == "Y") {
		$(".page-noti > ul").append('<li><spring:message code="system.system.survey.survAction.info1"/></li>');
		survEndFlag = true;
		pageNotiFlag = true;
	}
	// 설문기간 여부 msg
	if(surveyVO.startDt.replaceAll(".","") > realTime || surveyVO.endDt.replaceAll(".","") < realTime) {
		$(".page-noti > ul").append('<li><spring:message code="system.system.survey.survAction.info2"/></li>');
		survDateFlag = true;
		pageNotiFlag = true;
	}
	// 설문마감 여부 msg
	if(surveyVO.closeYn == "Y") {
		$(".page-noti > ul").append('<li><spring:message code="system.system.survey.survAction.info3"/></li>');
		survCloseFlag = true;
		pageNotiFlag = true;
	}
	
	if(pageNotiFlag){
		$(".page-noti").show();
		$(".gridContainer").css("marginBottom", "20px");
	}else{
		$(".page-noti").hide();
		$(".gridContainer").css("marginBottom", "30px");
	}

	if(surveyVO.surveyTypeId == "001") { // 기명
		$("#btnsDiv").find("a:first").removeClass("hide");
		$("#infoDiv").addClass("hide");

		if(survEndFlag || survDateFlag || survCloseFlag) {
			$("#btnsDiv").addClass("hide");
		}else{
			$("#btnsDiv").removeClass("hide");
		}
	}else{ // 무기명
		$("#btnsDiv").find("a:first").addClass("hide");

		$("#infoSpan1").html(surveyVO.surveyNm);
		$("#infoSpan2").html(surveyVO.surveyTypeNm);
		if(survEndFlag || survDateFlag || survCloseFlag) {
			$("#newForm").hide();
			$("#infoDiv").removeClass("hide");
			$("#btnsDiv").addClass("hide");
			if(survEndFlag) {
				$("#infoSpan3").removeClass("hide");
			}else{
				$("#infoSpan3").addClass("hide");
			}
			if(survDateFlag) {
				$("#infoSpan4").removeClass("hide");
			}else{
				$("#infoSpan4").addClass("hide");
			}
			if(survCloseFlag) {
				$("#infoSpan5").removeClass("hide");
			}else{
				$("#infoSpan5").addClass("hide");
			}
		}else{
			$("#newForm").show();
			$("#infoDiv").addClass("hide");
			$("#btnsDiv").removeClass("hide");
		}
	}

	voToForm(surveyVO, "form", ["surveyTypeId","surveyEndYn"]);
	voToFormForText(surveyVO, "form", ["surveyNm","surveyTypeNm","startContent","endContent","surveyYearNm"]);

	<%-- 설문조사 그리기 --%>
	$("#loopDiv").empty();
	for(var i in data.grpList) {
		grpList = data.grpList[i];
		str = '<div class="pdt30 pl5 fs17" name="quesGrpDiv">'+grpList.quesGrpNm+'</div>';

		for(var j in data.quesList) { // 질문생성로직
			quesList = data.quesList[j];
			if(grpList.quesGrpId == quesList.quesGrpId) {
				str+= '<div class="pdt30 pl20">'+quesList.quesNm+'</div>';
				if(quesList.quesGbnId == "002") { // 주관식
					str+= '<div class="pdt5 pl30"><input type="text" id="quesItemId_'+quesList.quesId+'_'+essayQuestionId+'_'+quesList.quesGbnId+'" name="quesItemId_'+quesList.quesId+'" class="t-box01" maxlength="300"></div>';
				}else{ // 객관식 or 객관식+주관식

					for(var k in data.itemList) { // 답변생성로직
						itemList = data.itemList[k];
						if(quesList.quesId == itemList.quesId) {
							if(quesList.quesGbnId == "001") { // 객관식
								if(quesList.itemCheckGbnId == "001") { // 단일선택(radio)
									str+= '<div class="chkGrp pdt5 pl30"><input id="quesItemId_'+quesList.quesId+'_'+itemList.quesItemId+'_'+quesList.quesGbnId+'" name="quesItemId_'+quesList.quesId+'" type="radio" value="'+itemList.quesItemId+'"><label for="quesItemId_'+quesList.quesId+'_'+itemList.quesItemId+'_'+quesList.quesGbnId+'"><span></span>'+itemList.itemContent+'</label></div>'
								}else if(quesList.itemCheckGbnId == "002") { // 다중선택(checkbox)
									str+= '<div class="chkGrp pdt5 pl30"><input id="quesItemId_'+quesList.quesId+'_'+itemList.quesItemId+'_'+quesList.quesGbnId+'" name="quesItemId_'+quesList.quesId+'" type="checkbox" value="'+itemList.quesItemId+'"><input type="hidden"><label for="quesItemId_'+quesList.quesId+'_'+itemList.quesItemId+'_'+quesList.quesGbnId+'"><span></span>'+itemList.itemContent+'</label></div>'
								}
							}else if(quesList.quesGbnId == "003") { // 객관식+주관식
								if(quesList.itemCheckGbnId == "001") { // 단일선택(radio)
									str+= '<div class="chkGrp pdt5 pl30"><input id="quesItemId_'+quesList.quesId+'_'+itemList.quesItemId+'_'+quesList.quesGbnId+'" name="quesItemId_'+quesList.quesId+'" type="radio" value="'+itemList.quesItemId+'"><label for="quesItemId_'+quesList.quesId+'_'+itemList.quesItemId+'_'+quesList.quesGbnId+'"><span></span>'+itemList.itemContent+'</label></div>'
								}else if(quesList.itemCheckGbnId == "002") { // 다중선택(checkbox)
									str+= '<div class="chkGrp pdt5 pl30"><input id="quesItemId_'+quesList.quesId+'_'+itemList.quesItemId+'_'+quesList.quesGbnId+'" name="quesItemId_'+quesList.quesId+'" type="checkbox" value="'+itemList.quesItemId+'"><input type="hidden"><label for="quesItemId_'+quesList.quesId+'_'+itemList.quesItemId+'_'+quesList.quesGbnId+'"><span></span>'+itemList.itemContent+'</label></div>'
								}
							}

							if(isNotEmpty(itemList.linkQuesId)) { // 연계질문생성로직
								for(var l in data.linkList) {
									linkList = data.linkList[l];
									if(itemList.linkQuesId == linkList.quesId){
										str+= '<div class="pdt30 pl20">'+linkList.quesNm+'</div>';
										if(linkList.quesGbnId == "002") { // 주관식
											str+= '<div class="pdt10 pl30"><input type="text" id="quesItemId_'+linkList.quesId+'_'+essayQuestionId+'_'+linkList.quesGbnId+'" name="quesItemId_'+linkList.quesId+'" class="t-box01" maxlength="300"></div>';
										}else{
											for(var m in data.itemList) {
												linkItemList = data.itemList[m];
												if(linkList.quesId == linkItemList.quesId) {
													if(linkList.quesGbnId == "001") { // 객관식
														if(linkList.itemCheckGbnId == "001") { // 단일선택(radio)
															str+= '<div class="chkGrp pdt5 pl30"><input id="quesItemId_'+linkList.quesId+'_'+linkItemList.quesItemId+'_'+linkList.quesGbnId+'" name="quesItemId_'+linkList.quesId+'" type="radio" value="'+linkItemList.quesItemId+'"><label for="quesItemId_'+linkList.quesId+'_'+linkItemList.quesItemId+'_'+linkList.quesGbnId+'"><span></span>'+linkItemList.itemContent+'</label></div>'
														}else if(linkList.itemCheckGbnId == "002") { // 다중선택(checkbox)
															str+= '<div class="chkGrp pdt5 pl30"><input id="quesItemId_'+linkList.quesId+'_'+linkItemList.quesItemId+'_'+linkList.quesGbnId+'" name="quesItemId_'+linkList.quesId+'" type="checkbox" value="'+linkItemList.quesItemId+'"><input type="hidden"><label for="quesItemId_'+linkList.quesId+'_'+linkItemList.quesItemId+'_'+linkList.quesGbnId+'"><span></span>'+linkItemList.itemContent+'</label></div>'
														}
													}else if(linkList.quesGbnId == "003") { // 객관식+주관식
														if(linkList.itemCheckGbnId == "001") { // 단일선택(radio)
															str+= '<div class="chkGrp pdt5 pl30"><input id="quesItemId_'+linkList.quesId+'_'+linkItemList.quesItemId+'_'+linkList.quesGbnId+'" name="quesItemId_'+linkList.quesId+'" type="radio" value="'+linkItemList.quesItemId+'"><label for="quesItemId_'+linkList.quesId+'_'+linkItemList.quesItemId+'_'+linkList.quesGbnId+'"><span></span>'+linkItemList.itemContent+'</label></div>'
														}else if(linkList.itemCheckGbnId == "002") { // 다중선택(checkbox)
															str+= '<div class="chkGrp pdt5 pl30"><input id="quesItemId_'+linkList.quesId+'_'+linkItemList.quesItemId+'_'+linkList.quesGbnId+'" name="quesItemId_'+linkList.quesId+'" type="checkbox" value="'+linkItemList.quesItemId+'"><input type="hidden"><label for="quesItemId_'+linkList.quesId+'_'+linkItemList.quesItemId+'_'+linkList.quesGbnId+'"><span></span>'+linkItemList.itemContent+'</label></div>'
														}
													}
												}
											} // 연계답변생성로직 end
											if(linkList.quesGbnId == "003") {
												str+= '<div class="pdt5 pl30"><input type="text" id="quesItemId_'+linkList.quesId+'_'+essayQuestionId+'_'+linkList.quesGbnId+'" name="quesItemId_'+linkList.quesId+'" class="t-box01" maxlength="300"></div>';
											}
										} // 연계질문 - 객관식 or 객관식+주관식 end
									}
								}
							}  // 연계질문생성로직 end
						}
					} // 답변생성로직 end
					if(quesList.quesGbnId == "003") {
						str+= '<div class="pdt5 pl30"><input type="text" id="quesItemId_'+quesList.quesId+'_'+essayQuestionId+'_'+quesList.quesGbnId+'" name="quesItemId_'+quesList.quesId+'" class="t-box01" maxlength="300"></div>';
					}
				}  // 객관식 or 객관식+주관식 end
			}
		} // 질문생성로직 end

		$("#loopDiv").append(str);
	}
	$("div[name=quesGrpDiv]").next().removeClass("pdt30").addClass("pdt20");
	<%-- 설문조사 그리기 end --%>

	// 소속본부 selectbox set
	var selected = "", $option;
	$("#newForm #findScDeptId").empty();
	$(data.bonbuList).each(function(i,el){
		selected = surveyVO.scDeptId == el.scDeptId ? "selected" : "";
		$option += '<option value="'+el.scDeptId+'" '+selected+'>'+el.scDeptNm+'</option>';
	});
	$("#newForm #findScDeptId").append($option);

	// 설문결과 값 set
	$(data.resultList).each(function(i,el) {
		id = "quesItemId_"+el.quesId+"_"+el.quesItemId+"_"+el.quesGbnId;
		if(el.quesItemId == "QI00000") { // 주관식
			$("#form").find("#" + id).val(el.answerContent);
		}else{ // 객관식
			$("#form").find("#" + id).attr("checked",true);
		}
	});
}

function emptyData() {
	$("#newForm").hide();
	$("#infoDiv").hide();
}

// 저장
function saveData(gbn) {
	var f = document.form;
	var quesIds = [];
	var quesItemIds = [];
	var answerVals = [];
	var flag = true;
	
	if(!isUndefined(gbn) && gbn == "temp") {
		$("#saveGbn").val(gbn);
	}else{
		$("#saveGbn").val("");
		$("#loopDiv").find("input").not("input[type=hidden]").each(function(i,el) {
			id = $(el).attr("id");
			name = $(el).attr("name");

			if(id.split("_")[3] == "002") { // 주관식일 경우
				if($("input[name="+name+"]").val() == "") {
					$.showMsgBox($(el).parent().prev().html().split(".")[0]+"<spring:message code="system.system.survey.survAction.valid2"/>");
				}
			}else{
				if($("input[name="+name+"]:checked").length < 1) {
					$.showMsgBox($(el).parent().prev().html().split(".")[0]+"<spring:message code="system.system.survey.survAction.valid1"/>");
					flag = false;
					return false;
				}
			}
		});
	}

	// values set
	$("#loopDiv").find("input[type=radio]:checked, input[type=checkbox]:checked, input[type=text]").each(function(i,el) {
		quesIds.push($(el).attr("id").split("_")[1]);
		quesItemIds.push($(el).attr("id").split("_")[2]);
		if(isEmpty($(el).val())) {
			answerVals.push("empty");
		}else{
			answerVals.push($(el).val().replaceAll(",","|뚫훑"));
		}
	});
	$("#quesIds").val(quesIds);
	$("#quesItemIds").val(quesItemIds);
	$("#answerVals").val(answerVals);

	if(flag) {
		if(!isUndefined(gbn) && gbn == "temp") {
			$.showConfirmBox("<c:set var="messageArg"><spring:message code="button.temporarySave"/></c:set><spring:message code="common.confirm.msg" arguments="${messageArg}"/>", "saveDataProc");
		}else{
			$.showConfirmBox("<c:set var="messageArg"><spring:message code="button.survEnd"/></c:set><spring:message code="common.confirm.msg" arguments="${messageArg}"/>", "saveDataProc");
		}
	}
}

function saveDataProc() {
	sendAjax({
		"url" : "${context_path}/system/system/survey/survAction/saveSurvAction.do",
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
 	<form:hidden path="surveyId"/>
 	<form:hidden path="surveyTypeId"/>
	<form:hidden path="surveyEndYn"/>
	<form:hidden path="quesIds"/>
	<form:hidden path="quesItemIds"/>
	<form:hidden path="answerVals"/>
	<form:hidden path="saveGbn"/>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.survUsr"/> : <c:out value="${searchVO.userNm}" /></label>
			</li>
		</ul>
	</div>
	<div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>

	<div class="page-noti">
		<ul>
		</ul>
	</div>
	<div id="newForm">
		<div class="sch-bx bdr-t1">
			<ul>
				<li>
					<label for="surveyYearNm"><spring:message code="word.survYear"/> : </label>
					<span id="surveyYearNm"></span>
				</li>
			</ul>
		</div>
		<div class="btn-dw"></div>
		<div class="tbl-type02 bdr-a1">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="20%"/>
					<col width="80%"/>
				</colgroup>
				<tbody>
					<tr>
						<td>
							<div class="txt-c mt10 mb10 fs20"><span id="surveyNm"></span>(<span id="surveyTypeNm"></span>)</div>
							<div class="txt-c bdr-a1 ml300 mr300 pdt10 pdr20 pdb10 pl20"><span id="startContent"></span></div>
						</td>
					</tr>
					<tr>
						<td><div id="loopDiv" class="pdb10"></div></td>
					</tr>
					<tr>
						<td><div class="txt-c ml300 mr300 pdt10 pdr20 pdb10 pl20"><span id="endContent"></span></div></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="tbl-bottom">
			<div class="tbl-wbtn">
			</div>
			<div class="tbl-btn" id="btnsDiv">
				<a href="#" class="save" onclick="saveData('temp');return false;"><spring:message code="button.temporarySave"/></a>
				<a href="#" class="save" onclick="saveData();return false;"><spring:message code="button.survEnd"/></a>
			</div>
		</div>
	</div>
	<div id="infoDiv">
		<div class="tbl-type02 bdr-a1 hx300">
			<div class="txt-c mt90 mb10 fs20"><span id="infoSpan1"></span>(<span id="infoSpan2"></span>)</div>
			<div class="txt-c bdr-a1 ml300 mr300 pdt30 pdr20 pdb30 pl20 fs15">
				<div id="infoSpan3"><spring:message code="system.system.survey.survAction.info1"/></div>
				<div id="infoSpan4"><spring:message code="system.system.survey.survAction.info2"/></div>
				<div id="infoSpan5"><spring:message code="system.system.survey.survAction.info3"/></div>
			</div>
		</div>
	</div>
</form:form>

