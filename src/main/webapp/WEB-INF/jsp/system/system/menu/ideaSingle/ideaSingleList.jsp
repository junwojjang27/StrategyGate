<!-- 
*************************************************************************
* CLASS 명	: IdeaSingleList
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-07
* 기	능	: 간단제안 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-07				최 초 작 업
**************************************************************************
-->

<!--
필독 : 다국어 지원기능에 의해서 화면에 나타나는 한글명칭은 message properties파일을 이용하여 수정할 것. 
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="ideaSingleVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/menu/ideaSingle/ideaSingleList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
						{name:"ideaCd",	index:"ideaCd",	width:100,	align:"center",	label:"제안코드",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.ideaCd) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"year",	index:"year",	width:100,	align:"center",	label:"기준년도",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.year) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"userId",	index:"userId",	width:100,	align:"center",	label:"아이디"},
						{name:"category",	index:"category",	width:100,	align:"center",	label:"카테고리"},
						{name:"title",	index:"title",	width:100,	align:"center",	label:"제목"},
						{name:"content",	index:"content",	width:100,	align:"center",	label:"내용"},
						{name:"state",	index:"state",	width:100,	align:"center",	label:"상태(접수/승인/반려)"},
						{name:"createDt",	index:"createDt",	width:100,	align:"center",	label:"생성일자",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.createDt) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"updateDt",	index:"updateDt",	width:100,	align:"center",	label:"수정일자"},
						{name:"deleteDt",	index:"deleteDt",	width:100,	align:"center",	label:"삭제일자"},
						{name:"startDt",	index:"startDt",	width:100,	align:"center",	label:"평가시작일자"},
						{name:"endDt",	index:"endDt",	width:100,	align:"center",	label:"평가종료일자"},
						{name:"atchFileId",	index:"atchFileId",	width:100,	align:"center",	label:"첨부파일ID"},
						{name:"ideaGbnCd",	index:"ideaGbnCd",	width:100,	align:"center",	label:"평가구분코드"},
						{name:"degree",	index:"degree",	width:100,	align:"center",	label:"차수"},
						{name:"evalState",	index:"evalState",	width:100,	align:"center",	label:"평가상태(대기/진행/종료)"}

						],
		rowNum		: ${jqgrid_rownum_max},
		pager		: "pager",
		sortname	: "sortOrder",
		sortorder	: "asc",
		cellEdit	: true,
		multiselect	: true,
		loadComplete : function() {
		}
	});
	
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
	f.action = "${context_path}/system/system/menu/ideaSingle/excelDownload.do";
	f.submit();
}

// 상세 조회
function showDetail(parameter) {
	var f = document.form;
	f.ideaCd.value = ideaCd;
	f.year.value = year;
	f.createDt.value = createDt;

	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaSingle/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	$("#newForm").show();
	var dataVO = data.dataVO;
	
	$("#titleIdeaSingleNm").text("간단제안 : " + dataVO.userId);
	
	voToForm(dataVO, "form", ["ideaCd","userId","category","title","content","state","createDt","updateDt","deleteDt","startDt","endDt","atchFileId","ideaGbnCd","degree","evalState"]);
	$("#userId").focus();
}

// 정렬순서저장
function saveSortOrder() {
	if(!gridToForm("list", "form", true)) return false;
	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaSingle/saveSortOrder.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}

// 등록
function addData() {
	$("#newForm").show();
	
	$("#titleIdeaSingleNm").text("간단제안");
	
	resetForm("form", ["ideaCd","userId","category","title","content","state","createDt","updateDt","deleteDt","startDt","endDt","atchFileId","ideaGbnCd","degree","evalState"]);
	$("#year").val($("#findYear").val());
	$("#userId").focus();
}

// 저장
function saveData() {
	var f = document.form;
	if(!validatePerspectiveVO(f)) {
		return;
	}
	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaSingle/saveIdeaSingle.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}

// 삭제
function deleteData() {
	if(deleteDataToForm("list", "userId", "form")) {
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
		"url" : "${context_path}/system/system/menu/ideaSingle/deleteIdeaSingle.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="ideaCd"/>
	<form:hidden path="year"/>
	<form:hidden path="createDt"/>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
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
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="saveSortOrder();return false;"><spring:message code="button.saveAll"/></a>
			<a href="#" class="new" onclick="addData();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	<div class="page-noti">
		<ul>
			<li></li>
			<li></li>
		</ul>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleIdeaSingleNm"></div>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="20%"/>
					<col width="80%"/>
				</colgroup>
				<tbody>
					<tr> 
						<th scope="row"><label for="ideaCd">제안코드</label><span class="red">(*)</span></th> 
						<td ><form:input path="ideaCd" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="userId">아이디</label></th> 
						<td ><form:input path="userId" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="category">카테고리</label></th> 
						<td ><form:input path="category" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="title">제목</label></th> 
						<td ><form:input path="title" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="content">내용</label></th> 
						<td ><form:input path="content" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="state">상태(접수/승인/반려)</label></th> 
						<td ><form:input path="state" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="createDt">생성일자</label><span class="red">(*)</span></th> 
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
					<tr> 
						<th scope="row"><label for="startDt">평가시작일자</label></th> 
						<td ><form:input path="startDt" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="endDt">평가종료일자</label></th> 
						<td ><form:input path="endDt" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="atchFileId">첨부파일ID</label></th> 
						<td ><form:input path="atchFileId" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="ideaGbnCd">평가구분코드</label></th> 
						<td ><form:input path="ideaGbnCd" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="degree">차수</label></th> 
						<td ><form:input path="degree" class="t-box01"/></td> 
					</tr> 
					<tr> 
						<th scope="row"><label for="evalState">평가상태(대기/진행/종료)</label></th> 
						<td ><form:input path="evalState" class="t-box01"/></td> 
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

