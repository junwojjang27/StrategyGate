<!-- 
*************************************************************************
* CLASS 명	: SystemSettingList
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-11-01
* 기	능	: 시스템설정 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-11-01				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="systemSettingVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	//$("#newForm").hide();
	showDetail();
	$("label[for*='ScoreYn']").click(function(e){
		
		if($(e.target.parentNode).attr("for") == undefined){
			e.preventDefault();
			return false;
		}
		
		console.log(e.target.parentNode);
		console.log($(e.target.parentNode).attr("for"));
		//$("#"+$(e.target.parentNode).attr("for")).prop("checked",true);
		var inputId = $(e.target.parentNode).attr("for").replace("YnY","").replace("YnN","");
		if($(e.target.parentNode).attr("for").indexOf("YnN") > -1){
			$("#"+inputId).val("");
			$("#"+inputId).attr("disabled",true);
		}else{
			$("#"+inputId).attr("disabled",false);
		}
		
	});
	
});

function checkResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		searchList();
	}
}

// 목록 조회
function searchList() {
	showDetail();
}

// 상세 조회
function showDetail() {
	
	sendAjax({
		"url" : "${context_path}/system/system/comp/systemSetting/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 정렬순서저장
function setDetail(data) {
	
	var approve = data.approve;
	var score = data.score;
	
	$("#metricApproveUseYn"+approve.metricApproveUseYn).prop("checked",true);
	$("#actApproveUseYn"+approve.actApproveUseYn).prop("checked",true);
	$("#maxScoreYn"+score.maxScoreYn).prop("checked",true);
	$("#minScoreYn"+score.minScoreYn).prop("checked",true);
	$("#maxScore").val(score.maxScore);
	$("#minScore").val(score.minScore);
}

// 저장
function saveData() {
	var f = document.form;
	f.year.value  = f.findYear.value;
	if(!validateSystemSettingVO(f)) {
		return;
	}
	
	sendAjax({
		"url" : "${context_path}/system/system/comp/systemSetting/saveSystemSetting.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="year"/>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
	</div>

	<div id="newForm">
		<%--	
		<div class="ptitle"><spring:message code="word.deptSc"/><spring:message code="word.approval"/><spring:message code="word.setting"/></div>
		<div class="tbl-type01">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="30%"/>
					<col width="70%"/>
				</colgroup>
				<tbody>
					<tr> 
						<th scope="row"><label for="metricApproveUseYn"><spring:message code="word.metricApprovalUseYn"/></label></th> 
						<td>
							<c:forEach items="${codeUtil:getCodeList('011')}" var="item" varStatus="status">
								<form:radiobutton path="metricApproveUseYn" id="metricApproveUseYn${item.codeId}" value="${item.codeId}"/><label for="metricApproveUseYn${item.codeId}"><span></span>${item.codeNm}</label>
							</c:forEach>
						</td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="actApproveUseYn"><spring:message code="word.actApprovalUseYn"/></label></th> 
						<td>
							<c:forEach items="${codeUtil:getCodeList('011')}" var="item" varStatus="status">
								<form:radiobutton path="actApproveUseYn" id="actApproveUseYn${item.codeId}" value="${item.codeId}"/><label for="actApproveUseYn${item.codeId}"><span></span>${item.codeNm}</label>
							</c:forEach>
						</td> 
					</tr> 
				</tbody>
			</table>
		</div>
		--%>
		<div class="ptitle">조직지표 점수구간 설정</div>
		<div class="tbl-type01">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="30%"/>
					<col width="35%"/>
					<col width="35%"/>
				</colgroup>
				<tbody>
					<tr> 
						<th scope="row"><label for="maxScore"><spring:message code="word.orgMetric"/>&nbsp;<spring:message code="word.maxScore"/></label></th> 
						<td >
							<c:forEach items="${codeUtil:getCodeList('011')}" var="item" varStatus="status">
								<form:radiobutton path="maxScoreYn" id="maxScoreYn${item.codeId}" value="${item.codeId}"/><label for="maxScoreYn${item.codeId}"><span></span>${item.codeNm}</label>
							</c:forEach>
						</td>
						<td ><form:input path="maxScore" class="t-box01 txt-r"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="minScore"><spring:message code="word.orgMetric"/>&nbsp;<spring:message code="word.minScore"/></label></th> 
						<td >
							<c:forEach items="${codeUtil:getCodeList('011')}" var="item" varStatus="status">
								<form:radiobutton path="minScoreYn" id="minScoreYn${item.codeId}" value="${item.codeId}"/><label for="minScoreYn${item.codeId}"><span></span>${item.codeNm}</label>
							</c:forEach>
						</td>
						<td ><form:input path="minScore" class="t-box01 txt-r"/></td> 
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

