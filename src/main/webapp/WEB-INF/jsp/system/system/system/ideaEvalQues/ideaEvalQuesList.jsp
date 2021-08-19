<!-- 
*************************************************************************
* CLASS 명	: IdeaEvalQuesList
* 작 업 자	: 문은경
* 작 업 일	: 2019-05-21
* 기	능	: 평가 질문 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	문은경		2019-05-21				최 초 작 업
**************************************************************************
-->

<!--
필독 : 다국어 지원기능에 의해서 화면에 나타나는 한글명칭은 message properties파일을 이용하여 수정할 것. 
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="ideaEvalQuesVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/system/ideaEvalQues/ideaEvalQuesList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
						{name:"evalFormCd",	index:"evalFormCd",	width:100,	align:"center",	label:"평가항목코드", hidden:true							
						},
						{name:"evalQuesId",	index:"evalQuesId",	width:100,	align:"center",	label:"평가질문ID",hidden:true
						},
						{name:"evalQuesSeq",	index:"evalQuesSeq",	width:100,	align:"center",	label:"질문순번"},
						{name:"evalQuesNm",	index:"evalQuesNm",	width:100,	align:"center",	label:"평가질문",
							formatter:function(cellvalue, options, rowObject) {
							return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.evalQuesNm) + "\",\"" + removeNull(rowObject.surveyId) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
						},
						unformat:linkUnformatter},
						{name:"evalItemCnt",	index:"evalItemCnt",	width:100,	align:"center",	label:"항목수코드"},
						{name:"createDt",	index:"createDt",	width:100,	align:"center",	label:"생성일자"},
						{name:"updateDt",	index:"updateDt",	width:100,	align:"center",	label:"수정일자",hidden:true},
						{name:"deleteDt",	index:"deleteDt",	width:100,	align:"center",	label:"삭제일자",hidden:true},
						{name:"year",	index:"year",	width:100,	align:"center",	label:"기준년도",hidden:true}

						],
		rowNum		: ${jqgrid_rownum_max},
		pager		: "pager",
		sortname	: "sortOrder",
		sortorder	: "asc",
		cellEdit	: true,
		multiselect	: true,
		loadComplete : function() {
			$("#evalFormCd").val($('#findevalFormCd').val());
		}
	});

	$("#newForm").show();
	//$("#newForm").hide();
});

// 목록 조회
function searchList() {
	$("#newForm").hide();
 	reloadGrid("list", "form");
}

// 엑셀 다운로드
function excelDownload() {
	var f = document.form;
	f.action = "${context_path}/system/system/system/ideaEvalQues/excelDownload.do";
	f.submit();
}

// 상세 조회
function showDetail(evalFormCd) {
	var f = document.form;
	f.evalFormCd.value = evalFormCd;
	f.evalQuesId.value = evalQuesId;

	
	sendAjax({
		"url" : "${context_path}/system/system/system/ideaEvalQues/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	$("#newForm").show();
	var dataVO = data.dataVO;
	
	$("#titleIdeaEvalQuesNm").text("평가 질문 : " + dataVO.evalQuesId);
	
	voToForm(dataVO, "form", ["evalFormCd","evalQuesId","evalQuesSeq","evalQuesNm","evalItemCnt","createDt","updateDt","deleteDt",]);
	$("#evalQuesId").focus();
}


// 등록
function addData() {
	$("#newForm").show();
	
	$("#titleIdeaEvalQuesNm").text("평가 질문");
	
	resetForm("form", ["evalFormCd","evalQuesId","evalQuesSeq","evalQuesNm","evalItemCnt","createDt","updateDt","deleteDt",]);
	$("#year").val($("#findYear").val());
	$("#evalQuesId").focus();
}

// 저장
function saveData() {
	var f = document.form;
	if(!validatePerspectiveVO(f)) {
		return;
	}
	
	sendAjax({
		"url" : "${context_path}/system/system/system/ideaEvalQues/saveIdeaEvalQues.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}

// 삭제
function deleteData() {
	if(deleteDataToForm("list", "evalQuesId", "form")) {
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
		"url" : "${context_path}/system/system/system/ideaEvalQues/deleteIdeaEvalQues.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="evalFormCd"/>
	<form:hidden path="evalQuesId"/>

	<div class="sch-bx">
		<ul>
			<%-- <li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findUseYn"><spring:message code="word.useYn"/></label>
				<form:select path="findUseYn" class="select wx80" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li> --%>
			<li>
				<label for="findevalFormCd"><spring:message code="word.evalQues"/></label>
				<form:select path="findevalFormCd" class="select wx400" >
					<form:options items="${evalList}"  itemLabel="evalFormNm" itemValue="evalFormNm" />
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
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
			<a href="#" class="new" id="addData" onclick="return false;"><spring:message code="button.create"/></a>
			<a href="#" class="delete" id="deleteData" onclick="return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	<div class="page-noti">
		<ul>
			<li></li>
			<li></li>
		</ul>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleIdeaEvalQuesNm"><spring:message code="word.evalQuesReg" /></div>
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
						<th scope="row"><label for="evalQuesSeq"><spring:message code="word.evalQuesSeq" /><span class="red">(*)</span></label></th>
						<td colspan="3"><form:input path="evalQuesSeq" class="wx422" maxlength="5"/></td>
					</tr>
					<tr>
						<th scope="row"><label for="evalQuesNm"><spring:message code="word.evalQues" /><span class="red">(*)</span></label></th>
						<td colspan="3"><form:input path="evalQuesNm" class="t-box01" maxlength="1000"/></td>
					</tr>
					<tr>
						<th scope="row"><label for="evalItemCnt"><spring:message code="word.itemCnt" /></label></th>
						<td >
							<form:select path="evalItemCnt" class="select t-box01" items="${codeUtil:getCodeList('370')}" itemLabel="codeNm" itemValue="codeId">
							</form:select>
						</td>
					</tr>
				</tbody>
				<%-- <tbody>
					<tr> 
						<th scope="row"><label for="evalFormCd">평가항목코드</label><span class="red">(*)</span></th> 
						<td ><form:input path="evalFormCd" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="evalQuesId">평가질문ID</label><span class="red">(*)</span></th> 
						<td ><form:input path="evalQuesId" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="evalQuesSeq">질문순번</label></th> 
						<td ><form:input path="evalQuesSeq" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="evalQuesNm">평가질문</label></th> 
						<td ><form:input path="evalQuesNm" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="evalItemCnt">항목수코드</label></th> 
						<td ><form:input path="evalItemCnt" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="createDt">생성일자</label></th> 
						<td ><form:input path="createDt" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="updateDt">수정일자</label></th> 
						<td ><form:input path="updateDt" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="deleteDt">삭제일자</label></th> 
						<td ><form:input path="deleteDt" class="t-box01"/></td> 
					</tr> 

				</tbody> --%>
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
				<a href="#" class="save" onclick="saveData();return false;"><spring:message code="button.save"/></a>
			</div>
		</div>
	</div>
</form:form>

