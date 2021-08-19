<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="clientMngVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
var compIdPattern = /^[A-Za-z]{1}[A-Za-z0-9]{1,30}$/;
var dupCount = 0;
var oldId = "";

$(function() {
	$(".datepicker").datepicker();
	setMaxLength("form");
	showBytes("memo", "memoBytes");
	
	if("${dataVO.country}" == "") {
		$("#country").val("KOR");
	}
	
	<c:if test="${dataVO.isNew eq 'N'}">
		oldId = "${dataVO.newCompId}";
		dupCount = 1;
	</c:if>
	
	<c:if test="${not empty dataVO.newCompId}">
		$("#detailForm").show();
	</c:if>
	
	<c:if test="${not empty dataVO.contractDt}">
		$("#renewYn").show();
		$("input[name='renewYn']").prop("checked", false); 
	</c:if>
	
	<c:if test="${dataVO.hasDefaultDataYn eq 'Y'}">
		$("#newCompId, #btnChk").hide();
	</c:if>
	
	$("#renewYn1").prop("checked", true);
});

// 회사아이디 체크
function checkCompId(newCompId) {
    var regex=/^[A-Za-z]{1}[A-Za-z0-9]{1,30}$/;
    return regex.test(newCompId);
}

// 중복체크
function idDuplicationCheck() {
	oldId = $("#newCompId").val();
	var newCompId = $("#newCompId").val();
	
	if(isEmpty(newCompId)) {
		$.showMsgBox("<spring:message code="superMng.superMng.clientMng.clientMngList.inputId"/>", null, $("#newCompId"));
		dupCount = 0;
	} else if(!compIdPattern.test(newCompId)) {
		$.showMsgBox("<spring:message code="superMng.superMng.clientMng.clientMngList.compIdPattern"/>", null, $("#newCompId"));
		dupCount = 0;
	} else {
		sendAjax({
			"url" : "${context_path}/superMng/superMng/superMng/clientMng/idDuplicationCheck.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : function(data) {
				$.showMsgBox(data.msg);
				if(data.result == AJAX_SUCCESS) {
					dupCount = 1;
				} else {
					$("#newCompId").select();
					dupCount = 0;
				}
			}
		});
	}
}

// 이력보기 팝업
function showHistory() {
	openFancybox({
		"url" : "${context_path}/superMng/superMng/superMng/clientMng/popClientHistory.do",
		"data" : getFormData("form")
	});
}

// 저장
function doSave() {
	if(!validateClientMngVO(document.form)) {
		return;
	}
	
	<c:if test="${dataVO.isNew eq 'Y' or dataVO.hasDefaultDataYn eq 'N'}">
		if(!checkCompId($("#newCompId").val())) {
			$.showMsgBox("<spring:message code="superMng.superMng.clientMng.clientMngList.compIdPattern"/>", null, $("#newCompId"));
			return false;
		}
	</c:if>
	
	if(oldId != $("#newCompId").val() || dupCount == 0) {
		$.showMsgBox("<spring:message code="errors.mustCheckDuplication"/>", null, $("#newCompId"));
		return false;
	}
	
	if(!checkEmail($("#chargeEmail").val())) {
		$.showMsgBox("<spring:message code="errors.email2"/>", null, $("#chargeEmail"));
		return false;
	}
	
	if($("input[name=useYn]:checked").length < 1) {
		$.showMsgBox("<c:set var="messageArg"><spring:message code="word.useYn"/></c:set><spring:message code="common.required.msg" arguments="${messageArg}"/>");
		return;
	}
	
	$("input[name=langChk][value=" + $("#compLang").val() + "]").prop("checked", true);
	
	$.showConfirmBox("<spring:message code="common.save.msg"/>", function() {
		sendAjax({
			"url" : "${context_path}/superMng/superMng/superMng/clientMng/saveData.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
	});
}

// 계약 내용 저장
function doSaveContract() {
	if(isEmpty($("#contractDt").val())) {
		$.showMsgBox("<c:set var="messageArg"><spring:message code="word.contractDt"/></c:set><spring:message code="common.required.msg" arguments="${messageArg}"/>", null, $("#contractDt"));
		return;
	}
	
	if(isEmpty($("#serviceStartDt").val())) {
		$.showMsgBox("<c:set var="messageArg"><spring:message code="word.serviceDt"/></c:set><spring:message code="common.required.msg" arguments="${messageArg}"/>", null, $("#serviceStartDt"));
		return;
	}
	
	if(isEmpty($("#serviceEndDt").val())) {
		$.showMsgBox("<c:set var="messageArg"><spring:message code="word.serviceDt"/></c:set><spring:message code="common.required.msg" arguments="${messageArg}"/>", null, $("#serviceEndDt"));
		return;
	}
	
	if($("#serviceStartDt").val() > $("#serviceEndDt").val()) {
		$.showMsgBox("<c:set var="messageArg"><spring:message code="word.serviceDt"/></c:set><spring:message code="errors.validation.check" arguments="${messageArg}"/>");
		return;
	}
	
	if($("input[name=serviceTypes]:checked").length < 1) {
		$.showMsgBox("<c:set var="messageArg"><spring:message code="word.serviceType"/></c:set><spring:message code="common.required.msg" arguments="${messageArg}"/>");
		return;
	}
	
	if($("input[name=payInfo]:checked").length < 1) {
		$.showMsgBox("<c:set var="messageArg"><spring:message code="word.paymentInfo"/></c:set><spring:message code="common.required.msg" arguments="${messageArg}"/>");
		return;
	}
	
	<c:if test="${not empty dataVO.contractDt}">
		if($("input[name=renewYn]:checked").length < 1) {
			$.showMsgBox("<c:set var="messageArg"><spring:message code="word.renewYn"/></c:set><spring:message code="common.required.msg" arguments="${messageArg}"/>");
			return;
		}
	</c:if>
	
	var orgContractDt = $("#contractDt").val();
	var orgServiceStartDt = $("#serviceStartDt").val();
	var orgServiceEndDt = $("#serviceEndDt").val();
	
	$("#contractDt").val(dateUnformatter($("#contractDt").val()));
	$("#serviceStartDt").val(dateUnformatter($("#serviceStartDt").val()));
	$("#serviceEndDt").val(dateUnformatter($("#serviceEndDt").val()));
	
	var formData = getFormData("form");
	
	$("#contractDt").val(orgContractDt);
	$("#serviceStartDt").val(orgServiceStartDt);
	$("#serviceEndDt").val(orgServiceEndDt);
	
	$.showConfirmBox("<spring:message code="common.save.msg"/>", function() {
		sendAjax({
			"url" : "${context_path}/superMng/superMng/superMng/clientMng/saveContractData.do",
			"data" : formData,
			"doneCallbackFunc" : "checkResult"
		});
	});
}

// 저장 callback
function checkResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		goBack();
	}
}

// 이전
function goBack() {
	loadPage("${context_path}/superMng/superMng/superMng/clientMng/clientMngList.do", "form");
}

</script>

<form:form commandName="dataVO" id="form" name="form" method="post">
	<form:hidden path="findUseYn" value="${searchVO.findUseYn}"/>
	<form:hidden path="findServiceStatus" value="${searchVO.findServiceStatus}"/>
	<form:hidden path="findPayStatus" value="${searchVO.findPayStatus}"/>
	<form:hidden path="searchCondition" value="${searchVO.searchCondition}"/>
	<form:hidden path="searchKeyword" value="${searchVO.searchKeyword}"/>
	
	<form:hidden path="compId"/>
	<form:hidden path="oldCompId"/>
	<form:hidden path="isNew"/>
	<form:hidden path="pwChangeCycle"/>

	<div class="ptitle"><spring:message code="word.basicInfo"/></div>
	<div class="tbl-type02">
		<table>
			<colgroup>
				<col width="140">
				<col width="">
				<col width="140">
				<col width="">
			</colgroup>
			<tbody>
				<tr>
					<th scope="row"><label for="compNm"><spring:message code="word.compNm"/></label><span class="red">(*)</span></th>
					<td><form:input path="compNm" class="t-box01" maxlength="100"/></td>
					<th scope="row"><label for="newCompId"><spring:message code="word.compId"/></label><span class="red">(*)</span></th>
					<td>
						<form:input path="newCompId" class="t-box01 fl wx200" maxlength="30"/>
						<a id="btnChk" href="#" class="btn-as fl mt0 ml5" onclick="idDuplicationCheck();return false;"><spring:message code="word.dupCheck"/></a>
						<c:if test="${dataVO.hasDefaultDataYn eq 'Y'}">${dataVO.newCompId}</c:if>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="bizNo"><spring:message code="word.bizNo"/></label><span class="red">(*)</span></th>
					<td><form:input path="bizNo" class="t-box01" maxlength="20"/></td>
					<th scope="row"><label for="ceoNm"><spring:message code="word.agent"/></label><span class="red">(*)</span></th>
					<td><form:input path="ceoNm" class="t-box01" maxlength="100"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="bizCondition"><spring:message code="word.business"/></label><span class="red">(*)</span></th>
					<td><form:input path="bizCondition" class="t-box01" maxlength="100"/></td>
					<th scope="row"><label for="bizType"><spring:message code="word.sectors"/></label><span class="red">(*)</span></th>
					<td><form:input path="bizType" class="t-box01" maxlength="100"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="compTel"><spring:message code="word.tel"/></label><span class="red">(*)</span></th>
					<td><form:input path="compTel" class="t-box01" maxlength="100"/></td>
					<th scope="row"><label for="compFax"><spring:message code="word.fax"/></label></th>
					<td><form:input path="compFax" class="t-box01" maxlength="50"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="country"><spring:message code="word.country"/></label><span class="red">(*)</span></th>
					<td colspan="3">
						<form:select path="country" class="select wx300" items="${codeUtil:getCountryList()}" itemLabel="codeNm" itemValue="codeId"/>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="compLang"><spring:message code="word.lang"/></label><span class="red">(*)</span></th>
					<td>
						<c:if test="${not empty lang}">
							<form:select path="compLang" class="select wx300">
 								<form:option value=""><spring:message code="word.selectDefaultLang"/></form:option>
 								<form:options items="${lang}" itemLabel="langNm" itemValue="lang"/>
							</form:select>
						</c:if>
					</td>
					<th scope="row"><label for="langChk0"><spring:message code="word.selectOtherLang"/></label></th>
					<td>
						<c:if test="${not empty lang}">
							<c:forEach items="${lang}" var="item" varStatus="status">
								<c:set var="chkClass" value=""/>
								<c:if test="${status.index == 0}"><c:set var="chkClass" value="ml0"/></c:if>
								<input type="checkbox" name="langChk" id="langChk${status.index}" value="${item.lang}" <c:if test="${item.langUseYn eq 'Y'}">checked="checked"</c:if>/><label for="langChk${status.index}" class="${chkClass}"><span></span>${item.langNm}</label>
							</c:forEach>
						</c:if>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="compAddr"><spring:message code="word.addr"/></label><span class="red">(*)</span></th>
					<td colspan="3"><form:input path="compAddr" class="t-box01" maxlength="1000"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="chargeNm"><spring:message code="word.inCharge"/></label><span class="red">(*)</span></th>
					<td><form:input path="chargeNm" class="t-box01" maxlength="100"/></td>
					<th scope="row"><label for="chargeTel"><spring:message code="word.inChargeTel"/></label><span class="red">(*)</span></th>
					<td><form:input path="chargeTel" class="t-box01" maxlength="100"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="chargeEmail"><spring:message code="word.inChargeEmail"/></label><span class="red">(*)</span></th>
					<td colspan="3"><form:input path="chargeEmail" class="t-box01" maxlength="50"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="theme"><spring:message code="word.selectTheme"/></label><span class="red">(*)</span></th>
					<td>
						<form:select path="theme" class="select wx300" items="${codeUtil:getCodeList('350')}" itemLabel="codeNm" itemValue="codeId">
						</form:select>
					</td>
					<th scope="row"><label for="commYearBatchYn"><spring:message code="word.commYearBatchYn"/></label></th>
					<td>
						<c:forEach items="${codeUtil:getCodeList('367')}" var="item" varStatus="status">
							<form:radiobutton path="commYearBatchYn" id="commYearBatchYn${status.index}" value="${item.codeId}"/><label for="commYearBatchYn${status.index}"><span></span>${item.codeNm}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="memo"><spring:message code="word.memo"/></label></th>
					<td colspan="3">
						<p><form:textarea path="memo" maxlength="4000" cssClass="wp100 ml0"/></p>
						<p class="byte"><label id="memoBytes">0</label><label> / 4000byte</label></p>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="useYn0"><spring:message code="word.useYn"/></label><span class="red">(*)</span></th>
					<td colspan="3">
						<c:forEach items="${codeUtil:getCodeList('011')}" var="item" varStatus="status">
							<form:radiobutton path="useYn" id="useYn${status.index}" value="${item.codeId}"/><label for="useYn${status.index}"><span></span>${item.codeNm}</label>
						</c:forEach>
					</td>
				</tr>
				<%-- databse 접속정보 --%>
				<tr>
					<th scope="row"><label for="dbDriver"><spring:message code="word.compDbDriverName"/></label><span class="red">(*)</span></th>
					<td><form:input path="dbDriver" class="t-box01" maxlength="100"/></td>
					<th scope="row"><label for="dbUrl"><spring:message code="word.compDbUrl"/></label><span class="red">(*)</span></th>
					<td><form:input path="dbUrl" class="t-box01" maxlength="100"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="connectionId"><spring:message code="word.compDbConnId"/></label><span class="red">(*)</span></th>
					<td><form:input path="connectionId" class="t-box01" maxlength="100"/></td>
					<th scope="row"><label for="dbId"><spring:message code="word.compDbDatabaseId"/></label><span class="red">(*)</span></th>
					<td><form:input path="dbId" class="t-box01" maxlength="100"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="dbUserId"><spring:message code="word.compDbUserId"/></label><span class="red">(*)</span></th>
					<td><form:input path="dbUserId" class="t-box01" maxlength="100"/></td>
					<th scope="row"><label for="dbUserPasswd"><spring:message code="word.compDbPassword"/></label><span class="red">(*)</span></th>
					<td><form:input path="dbUserPasswd" class="t-box01" maxlength="100"/></td>
				</tr>
				<%-- databse 접속정보 end --%>
			</tbody>
		</table>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-wbtn">
			<a href="#" class="prev" onclick="goBack();return false;"><span><spring:message code="button.before"/></span></a>
		</div>
		<div class="tbl-btn">
			<a href="#" class="save" onclick="doSave();return false;"><spring:message code="button.save"/></a>
		</div>
	</div>
	
	<div id="detailForm" class="hide2">
		<div class="ptitle"><spring:message code="word.contract"/></div>
		<div class="tbl-type02">
			<table>
				<colgroup>
					<col width="140">
					<col width="">
					<col width="140">
					<col width="">
				</colgroup>
				<tbody>
					<tr>
						<th scope="row"><label for="contractYn"><spring:message code="word.contractYn"/></label></th>
						<td colspan="3">
							<c:choose>
								<c:when test="${dataVO.contractStatus eq 'Y'}"><label class="floatLabel"><spring:message code="word.contractY"/></label></c:when>
								<c:when test="${dataVO.contractStatus eq 'C'}"><label class="floatLabel"><spring:message code="word.contractComplete"/></label></c:when>
								<c:when test="${dataVO.contractStatus eq 'E'}"><label class="floatLabel"><spring:message code="word.contractExpiration"/></label></c:when>
							</c:choose>
							<a href="#" class="btn-as fl mt0 ml5" onclick="showHistory();return false;"><spring:message code="word.viewHistory"/></a>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="contractDt"><spring:message code="word.contractDt"/></label><span class="red">(*)</span></th>
						<td><form:input path="contractDt" class="t-box01 wx100 datepicker" readonly="true"/></td>
						<th scope="row"><label for="serviceStartDt"><spring:message code="word.serviceDt"/></label><span class="red">(*)</span></th>
						<td>
							<form:input path="serviceStartDt" class="t-box01 wx100 datepicker" readonly="true"/>
							~
							<form:input path="serviceEndDt" class="t-box01 wx100 datepicker" readonly="true"/>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="payType"><spring:message code="word.deposit"/></label><span class="red">(*)</span></th>
						<td>
							<form:select path="payType" class="select wx300">
								<form:option value="1"><spring:message code="word.bankTransfer"/></form:option>
								<form:option value="2"><spring:message code="word.creditcard"/></form:option>
								<form:option value="3"><spring:message code="word.mobilePayment"/></form:option>
							</form:select>
						</td>
						<th scope="row"><label for="serviceType0"><spring:message code="word.serviceType"/></label><span class="red">(*)</span></th>
						<td>
							<%--
							<c:forEach items="${codeUtil:getCodeList('351')}" var="item" varStatus="status">
								<form:radiobutton path="serviceType" id="serviceType${status.index}" value="${item.codeId}"/><label for="serviceType${status.index}"><span></span>${item.codeNm}</label>
							</c:forEach>
							--%>
							<c:forEach items="${codeUtil:getCodeList('002')}" var="item" varStatus="status">
								<c:set var="chkClass" value=""/>
								<c:if test="${status.index == 0}"><c:set var="chkClass" value="ml0"/></c:if>
								<span class="chkGrp"><form:checkbox path="serviceTypes" id="serviceTypes${status.index}" value="${item.codeId}"/><label for="serviceTypes${status.index}" class="${chkClass}"><span></span>${item.codeNm}</label></span>
							</c:forEach>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="payInfo0"><spring:message code="word.paymentInfo"/></label><span class="red">(*)</span></th>
						<td colspan="3">
							<c:forEach items="${codeUtil:getCodeList('348')}" var="item" varStatus="status">
								<form:radiobutton path="payInfo" id="payInfo${status.index}" value="${item.codeId}"/><label for="payInfo${status.index}"><span></span>${item.codeNm}</label>
							</c:forEach>
						</td>
					</tr>
					<tr id="renewYn">
						<th scope="row"><label for="renewYn0"><spring:message code="word.renewYn"/></label><span class="red">(*)</span></th>
						<td colspan="3">
							<form:radiobutton path="renewYn" id="renewYn0" value="01"/><label for="renewYn0"><span></span><spring:message code="word.simpleUpdate"/></label>
							<form:radiobutton path="renewYn" id="renewYn1" value="02"/><label for="renewYn1"><span></span><spring:message code="word.contractRenewal"/></label>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="tbl-bottom">
			<div class="tbl-wbtn">
				<a href="#" class="prev" onclick="goBack();return false;"><span><spring:message code="button.before"/></span></a>
			</div>
			<div class="tbl-btn">
				<a href="#" class="save" onclick="doSaveContract();return false;"><spring:message code="button.save"/></a>
			</div>
		</div>
	</div>
</form:form>
