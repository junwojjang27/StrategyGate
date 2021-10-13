<!-- 
*************************************************************************
* CLASS 명	: IdeaEvalList
* 작 업 자	: 하성준
* 작 업 일	: 2021-10-13
* 기	능	: 평가하기 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-10-13				최 초 작 업
**************************************************************************
-->

<!--
필독 : 다국어 지원기능에 의해서 화면에 나타나는 한글명칭은 message properties파일을 이용하여 수정할 것. 
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="ideaEvalVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/menu/ideaEval/ideaEvalList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"(count%10)*30",		//jqGrid크기를 제안 수에 맞게 변경
		colModel	:	[
						{name:"ideaGbnCd",	index:"ideaGbnCd",	width:20,	align:"center",	label:"제안구분"},
						{name:"category",	index:"category",	width:20,	align:"center",	label:"카테고리"},
						{name:"title",		index:"title",		width:100,	align:"left",	label:"제목",
							formatter: function (cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.ideaCd) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
								//escapeHTML뜻 : DB에 이거 없이 넣으면 digit가 나옴?....
							},							//formatter : 색깔이나 액션을 주는 (데이터 가공) / 제목에 링크, 돋보기 표시 등
							unformat: linkUnformatter	//포메터로 포맷된 데이터가 저장이 될 떄 언포맷 된 상태에서 나가야 한다.. (순수 데이터만 데이터에 날라가기(포매터에서 가공된 데이터를 다시 본래의 데이터로 저장))
						},
						{name:"userNm",		index:"userNm",		width:20,	align:"center",	label:"등록자"},
						{name:"degree",		index:"degree",		width:20,	align:"center",	label:"평가차수"},
						{name:"evalState",	index:"evalState",	width:20,	align:"center",	label:"평가상태"},
						{name:"startDt",	index:"startDt",	width:20,	align:"center",	label:"평가시작일"},
						{name:"endDt",		index:"endDt",		width:20,	align:"center",	label:"평가종료일"},
						{name:"state",		index:"state",		width:20,	align:"center",	label:"상태(접수/승인/반려)", 	hidden: true},
						{name:"ideaCd",		index:"ideaCd",		width:100,	align:"center",	label:"제안코드", 	hidden: true},
						{name:"year",		index:"year",		width:100,	align:"center",	label:"기준년도",	hidden: true},
						{name:"userId",		index:"userId",		width:100,	align:"center",	label:"아이디",	 	hidden: true},
						{name:"content",	index:"content",	width:100,	align:"center",	label:"내용", 	 	hidden: true},
						{name:"createDt",	index:"createDt",	width:100,	align:"center",	label:"생성일자", 	hidden: true},
						{name:"updateDt",	index:"updateDt",	width:100,	align:"center",	label:"수정일자", 	hidden: true},
						{name:"deleteDt",	index:"deleteDt",	width:100,	align:"center",	label:"삭제일자", 	hidden: true},
						{name:"atchFileId",	index:"atchFileId",	width:100,	align:"center",	label:"첨부파일ID", 	hidden: true},
						],
		pager: "pager",
		rowNum: 10,
		sortname: "createDt",
		sortorder: "asc",
		loadComplete : function() {
			var count = $('#list').getGridParam('records'); //jqGrid크기 조절하기 위함. (서버에서 받은 실제 레코드 수)
		}
	});

	$("#newForm").hide();

	$("#searchKeyword").keyup(function (e) {		//enter눌렀을 때 searchList()로 이동
		if (e.keyCode == 13) searchList();
	});
});

// 목록 조회
function searchList() {
	$("#newForm").hide();
 	reloadGrid("list", "form");
}

// 엑셀 다운로드
function excelDownload() {
	var f = document.form;
	f.action = "${context_path}/system/system/menu/ideaEval/excelDownload.do";
	f.submit();
}

/*// 상세 조회
function showDetail(parameter) {
	var f = document.form;
	f.ideaCd.value = ideaCd;
	f.year.value = year;
	f.createDt.value = createDt;

	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaEval/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}*/

// 상세 조회
function showDetail(parameter) {
	var f = document.form;
	f.ideaCd.value = ideaCd;
	f.year.value = year;

	loadPage("${context_path}/system/system/menu/ideaEval/ideaEvalDetail.do", "form");
}

// 상세 조회 값 세팅
function setDetail(data) {
	$("#newForm").show();
	var dataVO = data.dataVO;
	
	$("#titleIdeaEvalNm").text("평가하기 : " + dataVO.userId);
	
	voToForm(dataVO, "form", ["ideaCd","userId","category","title","content","state","createDt","updateDt","deleteDt","startDt","endDt","atchFileId","ideaGbnCd","degree","evalState"]);
	$("#userId").focus();
}

// 정렬순서저장
function saveSortOrder() {
	if(!gridToForm("list", "form", true)) return false;
	
	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaEval/saveSortOrder.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}

// 등록
function addData() {
	$("#newForm").show();
	
	$("#titleIdeaEvalNm").text("평가하기");
	
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
		"url" : "${context_path}/system/system/menu/ideaEval/saveIdeaEval.do",
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
		"url" : "${context_path}/system/system/menu/ideaEval/deleteIdeaEval.do",
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
				<label>평가자 : ${sessionScope.loginVO.userNm}</label>
			</li>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<%--<li>
				<label for="findEvalState"><spring:message code="word.evalStatus"/></label>
				<form:select path="findEvalState" class="select wx80">
					<option value=""><spring:message code="word.all"/></option>
					<form:options items="${codeUtil:getCodeList('391')}" itemLabel="codeNm" itemValue="codeId"/>
				</form:select>
			</li>--%>
			<li>
				<label for="findDegree"><spring:message code="word.evalDegree"/></label>
				<form:select path="findDegree" class="select wx100">
					<option value=""><spring:message code="word.all"/></option>
					<form:options items="${codeUtil:getCodeList('387')}" itemLabel="codeNm" itemValue="codeId"/>
				</form:select>
			</li>
			<li>
				<label for="findCategory"><spring:message code="word.category"/></label>
				<form:select path="findCategory" class="select wx100">
					<option value=""><spring:message code="word.all"/></option>
					<!--카테고리 라벨에서 전체 를 추가-->
					<form:options items="${codeUtil:getCodeList('385')}" itemLabel="codeNm" itemValue="codeId"/>
				</form:select>
			</li>
			<li>
			<label>등록자</label>
			<span class="searchBar"><form:input path="findUserNm" class="t-box01 wx100" maxlength="20"/> </span>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw"></div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn"></div>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleIdeaEvalNm"></div>
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

