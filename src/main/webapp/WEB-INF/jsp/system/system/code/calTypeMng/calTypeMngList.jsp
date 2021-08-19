<!-- 
*************************************************************************
* CLASS 명	: CalTypeMngList
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-2-6
* 기	능	: 산식관리 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-2-6				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="calTypeMngVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/code/calTypeMng/calTypeMngList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"300px",
		colModel	:	[
						{name:"calTypeId",	index:"calTypeId",	width:60,	align:"center",	label:"<spring:message code="word.addScoreCalTypeId"/>"},
						{name:"calTypeNm",	index:"calTypeNm",	width:300,	align:"left",	label:"<spring:message code="word.addScoreCalType"/>",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.calTypeId) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"metricCnt",	index:"metricCnt",	width:60,	align:"center",	label:"<spring:message code="word.metricCnt"/>"}
						],
		rowNum		: ${jqgrid_rownum_max},
		//pager		: "pager",
		sortname	: "calTypeId",
		sortorder	: "asc",
		multiselect	: true
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
	$("#findUseYn").on("change", function() {
		if($(this).val() == "N"){
			$(".delete").hide();
		}else{
			$(".delete").show();
		}
	});
	/***** 사용여부 미사용시 삭제 버튼 숨김 end *****/
	
	$("#newForm").hide();
});

// 목록 조회
function searchList() {
	$("#newForm").hide();
 	reloadGrid("list", "form");
}

// 엑셀 다운로드
function excelDownload() {
	var f = document.form;
	f.action = "${context_path}/system/system/code/calTypeMng/excelDownload.do";
	f.submit();
}

// 상세 조회
function showDetail(calTypeId) {
	var f = document.form;
	f.calTypeId.value = calTypeId;
	
	sendAjax({
		"url" : "${context_path}/system/system/code/calTypeMng/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	$("#newForm").show();
	var dataVO = data.dataVO;
	$("#calTypeIdText").empty();
	$("#calTypeIdText").text(dataVO.calTypeId);
	$("#titleCalTypeMngNm").text("<spring:message code="word.addScoreCalType" /> : " + dataVO.calTypeNm);
	
	voToForm(dataVO, "form", ["calTypeId","calTypeNm","calType","content","useYn"]);
	
	$("#newForm").find("input").each(function(i,v){
		$(v).val(unescapeHTML($(v).val()));
	});
	
	$("#calTypeNm").focus();
}

// 등록
function addData() {
	$("#newForm").show();
	
	$("#titleCalTypeMngNm").text("<spring:message code="word.addScoreCalType" />");
	
	resetForm("form", ["calTypeId","calTypeNm","calType","content","useYn"]);
	$("#year").val($("#findYear").val());
	$("#calTypeIdText").text("※ <spring:message code="common.autoCreate.msg" />");
	$("#calTypeNm").focus();
}

// 저장
function saveData() {
	var f = document.form;
	if(!validateCalTypeMngVO(f)) {
		return;
	}
	
	sendAjax({
		"url" : "${context_path}/system/system/code/calTypeMng/saveCalTypeMng.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}

// 삭제
function deleteData() {
	
	var ids = $("#list").jqGrid("getGridParam", "selarrrow");
	var rowData;
	var isUse = false;
	$(ids).each(function(i,v){
		rowData = $("#list").jqGrid("getRowData",v);
		if(Number(rowData.metricCnt) > 0){
			isUse = true;
		}
	});
	
	if(isUse){
		$.showMsgBox("<spring:message code="bsc.system.calTypeMng.noDelete"/>",null);
		return false;
	}
	
	if(deleteDataToForm("list", "calTypeId", "form")) {
		$.showConfirmBox("<spring:message code="common.delete.msg"/>", "doDeleteData");
	}
}

// 삭제 처리
function doDeleteData() {
	/*
	var delList = [];
	$("#form").find("[name=keys]").each(function(i, e) {
		delList.push($(this).val());
	});
	*/
	
	
	sendAjax({
		"url" : "${context_path}/system/system/code/calTypeMng/deleteCalTypeMng.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
    <form:hidden path="calTypeId"/>
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findUseYn"><spring:message code="word.useYn"/></label>
				<form:select path="findUseYn" class="select wx80" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
		<a href="#" class="excel" onclick="excelDownload();return false;"><span><spring:message code="button.excelDownload"/></span></a>
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="new" onclick="addData();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	<%--
	<div class="page-noti">
		<ul>
			<li></li>
			<li></li>
		</ul>
	</div>
	--%>
	<div id="newForm">
		<div class="ptitle" id="titleCalTypeMngNm"></div>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="20%"/>
					<col width="80%"/>
				</colgroup>
				<tbody>
					<tr> 
						<th scope="row"><label for="calTypeId"><spring:message code="word.addScoreCalTypeId" /></label></th> 
						<td>
							<span id="calTypeIdText"></span>
						</td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="calTypeNm"><spring:message code="word.addScoreCalType" /></label><span class="red">(*)</span></th> 
						<td><form:input path="calTypeNm" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="calType"><spring:message code="word.calPatternCalculus" /></label><span class="red">(*)</span></th> 
						<td><form:input path="calType" class="t-box01"/></td> 
					</tr>
					<tr> 
						<th scope="row"><label for="useYn"><spring:message code="word.useYn" /></label><span class="red">(*)</span></th> 
						<td>
							<form:select path="useYn" class="wx100">
								<form:options items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId" />
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
				<a href="#" class="save" onclick="saveData();return false;"><spring:message code="button.save"/></a>
			</div>
		</div>
	</div>
</form:form>

