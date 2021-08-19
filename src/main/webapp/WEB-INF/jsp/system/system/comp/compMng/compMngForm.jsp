<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="clientMngVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function() {
	setMaxLength("form");
	$("#pwChangeCycle").numericOnly();
	
	if("${dataVO.country}" == "") {
		$("#country").val("KOR");
	}
});

// 저장
function doSave() {
	if(!validateClientMngVO(document.form)) {
		return;
	}
	
	if(!checkEmail($("#chargeEmail").val())) {
		$.showMsgBox("<spring:message code="errors.email2"/>", null, $("#chargeEmail"));
		return false;
	}
	
	$("input[name=langChk][value=" + $("#compLang").val() + "]").prop("checked", true);
	
	$.showConfirmBox("<spring:message code="common.save.msg"/>", function() {
		sendAjax({
			"url" : "${context_path}/system/system/comp/compMng/saveData.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "checkResult"
		});
	});
}

// 저장 callback
function checkResult(data) {
	$.showMsgBox(data.msg, function() {
		if(data.result == AJAX_SUCCESS) {
			$.cookie("theme", $("#theme").val());
			document.location.reload();
		}
	});
}
</script>

<form:form commandName="dataVO" id="form" name="form" method="post">
	<form:hidden path="compNm"/>
	<form:hidden path="newCompId" value="${dataVO.compId}"/>
	<form:hidden path="bizNo"/>
	<form:hidden path="country"/>
	<form:hidden path="memo"/>
	<form:hidden path="dbDriver" value="none"/>
	<form:hidden path="dbUrl" value="none"/>
	<form:hidden path="connectionId" value="none"/>
	<form:hidden path="dbId" value="none"/>
	<form:hidden path="dbUserId" value="none"/>
	<form:hidden path="dbUserPasswd" value="none"/>
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
					<th scope="row"><label><spring:message code="word.compNm"/></label></th>
					<td>
						${dataVO.compNm}
					</td>
					<th scope="row"><label><spring:message code="word.compId"/></label></th>
					<td>
						${dataVO.compId}
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="bizNo"><spring:message code="word.bizNo"/></label><span class="red">(*)</span></th>
					<td>${dataVO.bizNo}</td>
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
						<c:forEach items="${codeUtil:getCountryList()}" var="i"><c:if test="${i.codeId eq dataVO.country}"><c:out value="${i.codeNm}"></c:out></c:if></c:forEach>
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
					<td colspan="3">
						<form:select path="theme" class="select wx300" items="${codeUtil:getCodeList('350')}" itemLabel="codeNm" itemValue="codeId">
						</form:select>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="useMonitoringYn0"><spring:message code="word.monitoringYn"/></label></th>
					<td>
						<c:forEach items="${codeUtil:getCodeList('349')}" var="code" varStatus="status">
							<form:radiobutton path="useMonitoringYn" id="useMonitoringYn${status.index}" value="${code.codeId}"/><label for="useMonitoringYn${status.index}"><span></span>${code.codeNm}</label>
						</c:forEach>
					</td>
					<th scope="row"><label for="pwChangeCycle"><spring:message code="word.passwordChangeCycle"/></label></th>
					<td>
						<form:input path="pwChangeCycle" class="t-box01 wx50" maxlength="4"/> <label><spring:message code="unit.date"/></label>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="contractYn"><spring:message code="word.contractYn"/></label></th>
					<td colspan="3">
						<c:choose>
							<c:when test="${dataVO.contractStatus eq 'Y'}"><label class="floatLabel"><spring:message code="word.contractY"/></label></c:when>
							<c:when test="${dataVO.contractStatus eq 'C'}"><label class="floatLabel"><spring:message code="word.contractComplete"/></label></c:when>
							<c:when test="${dataVO.contractStatus eq 'E'}"><label class="floatLabel"><spring:message code="word.contractExpiration"/></label></c:when>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th scope="row"><label><spring:message code="word.serviceDt"/></label></th>
					<td>
						${dataVO.serviceStartDt} ~ ${dataVO.serviceEndDt}
					</td>
					<th scope="row"><label><spring:message code="word.serviceType"/></label></th>
					<td>
						${dataVO.serviceTypeNm}
					</td>
				</tr>
				<tr>
					<th scope="row"><label><spring:message code="word.deposit"/></label></th>
					<td>
						<c:if test="${dataVO.payType eq '1'}"><spring:message code="word.bankTransfer"/></c:if>
						<c:if test="${dataVO.payType eq '2'}"><spring:message code="word.creditcard"/></c:if>
						<c:if test="${dataVO.payType eq '3'}"><spring:message code="word.mobilePayment"/></c:if>
					</td>
					<th scope="row"><label><spring:message code="word.paymentInfo"/></label></th>
					<td>
						${codeUtil:getCodeName("348", dataVO.payInfo)}
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="doSave();return false;"><spring:message code="button.save"/></a>
		</div>
	</div>
</form:form>
