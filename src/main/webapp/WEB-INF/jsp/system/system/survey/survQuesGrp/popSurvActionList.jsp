<!--
*************************************************************************
* CLASS 명	: popSurvQuesPoolList
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-05
* 기	능	: 설문질문Pool List 팝업
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-05				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	searchPopList()
});

// 목록 조회
function searchPopList() {
	sendAjax({
		"url" : "${context_path}/system/system/survey/survQuesGrp/popSelectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

//상세 조회 값 세팅
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

	//voToForm(surveyVO, "form", ["surveyTypeId","surveyEndYn"]);
	voToFormForText(surveyVO, "popForm", ["surveyNm","surveyTypeNm","startContent","endContent"]);

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
}

</script>
<div class="popup wx800">
	<p class="title"><spring:message code="word.survPreview" /></p>
	<form:form commandName="searchVO" id="popForm" name="popForm" method="post" onsubmit="searchPopList();return false;">

	<div id="newForm">
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
							<div class="txt-c bdr-a1 ml120 mr120 pdt10 pdr20 pdb10 pl20"><span id="startContent"></span></div>
						</td>
					</tr>
					<tr>
						<td><div id="loopDiv" class="pdb10"></div></td>
					</tr>
					<tr>
						<td><div class="txt-c ml120 mr120 pdt10 pdr20 pdb10 pl20"><span id="endContent"></span></div></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="tbl-bottom">
			<div class="tbl-wbtn">
			</div>
			<div class="tbl-btn">
				<a href="#" class="save" onclick="$.fancybox.close();return false;"><spring:message code="button.close"/></a>
			</div>
		</div>
	</div>
</form:form>
</div>
