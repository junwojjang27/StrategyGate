<!-- 
*************************************************************************
* CLASS 명	: IdeaUsList
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-08
* 기	능	: 혁신제안 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-08				최 초 작 업
**************************************************************************
-->

<!--
필독 : 다국어 지원기능에 의해서 화면에 나타나는 한글명칭은 message properties파일을 이용하여 수정할 것. 
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="ideaUsVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/menu/ideaUs/ideaUsList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
			/*{name:"ideaCd",	index:"ideaCd",	width:100,	align:"center",	label:"제안코드",
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
            {name:"userId",	index:"userId",	width:100,	align:"center",	label:"아이디"},		*/
			{name:"userId",	index:"userId",	width:100,	align:"center",	label:"아이디", hidden:true},
			{name:"categoryNm",	index:"categoryNm",	width:20,	align:"center",	label:"<spring:message code="word.category"/>"},
			{name:"title",	index:"title",	width:100,	align:"center",	label:"<spring:message code="word.title"/>",
				formatter:function(cellvalue, options, rowObject) {
					return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.ideaCd) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
				},
				unformat:linkUnformatter
			},
			//{name:"content",	index:"content",	width:100,	align:"center",	label:"내용"},
			//{name:"state",	index:"state",	width:100,	align:"center",	label:"상태(접수/승인/반려)"},
			/*{name:"createDt",	index:"createDt",	width:100,	align:"center",	label:"생성일자",
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
            {name:"evalState",	index:"evalState",	width:100,	align:"center",	label:"평가상태(대기/진행/종료)"}	*/
			{name:"userNm",	index:"userNm",	width:20,	align:"center",	label:"<spring:message code="word.insertUser"/>"},
			{name:"deptNm",	index:"deptNm",	width:20,	align:"center",	label:"<spring:message code="word.deptNm"/>"},
			{name:"createDt",	index:"createDt",	width:20,	align:"center",	label:"<spring:message code="word.insertDT"/>"}

		],
		rowNum		: ${jqgrid_rownum_max},
		pager		: "pager",
		rowNum		: 10,
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
/*
// 엑셀 다운로드
function excelDownload() {
	var f = document.form;
	f.action = "${context_path}/system/system/menu/ideaUs/excelDownload.do";
	f.submit();
}
*/
// 상세 조회
function showDetail(ideaCd, year) {
	var f = document.form;
	f.ideaCd.value = ideaCd;
	f.year.value = year;
	//f.createDt.value = createDt;


	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaUs/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	$("#newForm").show();
	var dataVO = data.dataVO;

	//var userId = "${sessionScope.loginVO.userId}";

	//document.getElementById("userNm").innerHTML = dataVO.userNm;
	//document.getElementById("deptNm").innerHTML = dataVO.deptNm;

	//$("#titleIdeaUsNm").text("창의제안 : " + dataVO.userId);

	//voToForm(dataVO, "form", ["ideaCd","userId","category","title","content","state","createDt","updateDt","deleteDt","startDt","endDt","atchFileId","ideaGbnCd","degree","evalState"]);
    voToForm(dataVO, "form", ["ideaCd","category","title"]);

    $("#title").val(dataVO.title);
    $("#content").val(dataVO.content);
    $("#title").focus();

    if(isNotEmpty(upload1)) {
        upload1.destroy();
    }
    upload1 = new SGFileUploader({"targetId" : "spanAttachFile", "inputName" : "upFile1"});
    <c:import url="/common/fileList.do" charEncoding="utf-8">
    <c:param name="moduleName" value="upload1"/>
    <%--<c:param name="param_atchFileId" value= "${dataVO["atchFileKey"]}"/>--%>
    </c:import>

	var modifyYn = "Y";
	//첨부파일 ajax
	$.ajax({
		url : "${context_path}/system/system/menu/ideaUs/ideaUsDetail.do",
		data : {
			"modifyYn" : modifyYn,
			"atchFileId" : dataVO.atchFileKey,
			"_csrf" : getCsrf("form")
		},
		method : "POST",
		cache : false,
		dataType : "html"
	}).done(function(html) {
		$("#spanAttachFile").html(html);
	}).fail(function(jqXHR, textStatus) {
		try{
			var json = JSON.parse(jqXHR.responseText);
			if(!isEmpty(json.msg)) {
				$.showMsgBox(json.msg);
			} else {
				$.showMsgBox(getMessage("errors.processing"));
			}
		}catch(e) {
			$.showMsgBox(getMessage("errors.processing"));
		}
	});

	//byte check
	showBytes("content", "contentBytes");
}

// 정렬순서저장
function saveSortOrder() {
	if(!gridToForm("list", "form", true)) return false;

	sendAjax({
		"url" : "${context_path}/system/system/menu/ideaUs/saveSortOrder.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}

// 등록
function addData() {
	$("#newForm").show();

    //var userId = "${sessionScope.loginVO.userId}";
    //System.out.println(userId + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

    //document.getElementById("userNm").innerHTML = userId;

	//$("#titleIdeaUsNm").text("혁신제안");

	//resetForm("form", ["ideaCd","userId","category","title","content","state","createDt","updateDt","deleteDt","startDt","endDt","atchFileId","ideaGbnCd","degree","evalState"]);
	resetForm("form", ["category","title", "atchFileId", "useYn", "content"]);
	$("#year").val($("#findYear").val());
	$("#title").focus();
	//$("#userId").focus();

    upload1 = new SGFileUploader({"targetId" : "spanAttachFile", "inputName" : "upFile1", "maxFileCnt" : 5, "maxTotalSize" : "5MB"});
    <c:import url="/common/fileList.do" charEncoding="utf-8">
    <c:param name="moduleName" value="upload1"/>
    <c:param name="param_atchFileId" value="${searchVO.atchFileId}"/>
    </c:import>

    //byte check
    showBytes("content", "contentBytes");


}

// 저장
function saveData() {
	var f = document.form;
	if(!validateIdeaUsVO(f)) {
		return;
	}

	/*sendAjax({
		"url" : "${context_path}/system/system/menu/ideaUs/saveIdeaUs.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult",
		"failCallbackFunc" : "checkResult"
	});*/
    sendMultipartForm({
        "url" : "${context_path}/system/system/menu/ideaUs/saveIdeaUs.do",
        "formId" : "form",
        "fileModules" : [upload1],
        "doneCallbackFunc" : "checkResult",
        "failCallbackFunc" : "checkResult"
    });

}

//저장 callback
function checkResult(data) {
	$(window).scrollTop(0);
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		searchList();
	}
}

// 삭제
function deleteData() {
	/*if(deleteDataToForm("list", "userId", "form")) {
		$.showConfirmBox("<spring:message code="common.delete.msg"/>", "doDeleteData");
	}*/
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
	if(deleteDataToForm("list", "ideaCd", "form")) {
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
		"url" : "${context_path}/system/system/menu/ideaUs/deleteIdeaUs.do",
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
			<li>
				<label for="category"><spring:message code="word.category"/></label>
				<form:select path="findCategory" class="select wx100" items="${codeUtil:getCodeList('385')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findSearch"><spring:message code="word.search"/></label>
				<form:select path="findSearch" class="select wx100">
					<form:option value="subject"><spring:message code="word.title"/></form:option>
					<form:option value="content"><spring:message code="word.content"/></form:option>
				</form:select>
				<span class="searchBar"><form:input path="searchKeyword" class="t-box01 wx200" maxlength="20"/> </span>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
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
			<li>개인,부서별,동아리별 프로젝트 단위의 아이디어를 제안하는 페이지 입니다.</li>
			<li>공모전에 참가하실 팀은 게시판에서 관련 폼을 다운받아 제출하시기 바랍니다.</li>
			<li>개인으로 제안하실 분은 카테고리를 개인으로 설정하여 제출하시기 바랍니다.</li>
		</ul>
	</div>

	<div id="newForm">
		<div class="ptitle" id="titleIdeaUsNm"></div>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="20%"/>
					<col width="30%"/>
					<col width="20%"/>
					<col width="30%"/>
				</colgroup>
				<tbody>
					<tr>
						<th scope="row"><label for="userNm">등록자</label></th>
                        <td ><form:input path="userNm" class="t-box01"/></td>
						<th scope="row"><label for="deptNm">부서</label></th>
                        <td ><form:input path="deptNm" class="t-box01"/></td>
					</tr>
					<tr>
						<th scope="row"><label for="category">카테고리</label><span class="red">(*)</span></th>
						<td>
							<form:select path="category" class="select wx80" items="${codeUtil:getCodeList('385')}" itemLabel="codeNm" itemValue="codeId">
							</form:select>
						</td>
						<th scope="row"><label for="state"><spring:message code="word.useYn"/></label><span class="red">(*)</span></th>
						<td ><form:select path="state" class="select wx80" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
						</form:select></td>
					</tr>
					<tr>
						<th scope="row"><label for="title">제목</label><span class="red">(*)</span></th>
						<td colspan="3"><form:input path="title" class="t-box01"/></td>
					</tr>
					<tr>
						<th scope="row"><label for="content">내용</label><span class="red">(*)</span><br>ex) 배경, 현황 및 문제점, 개선방안, 기대효과</th>
						<td colspan="3">
							<p><form:textarea path="content" maxlength="4000"/></p>
							<p class="byte"><label id="contentBytes">0</label><label> / 4000byte</label></p>
						</td>
					</tr>
					<tr>
						<th scope="row"><label><spring:message code="word.atchFile"/></label></th>
						<td colspan="3">
							<span id="spanAttachFile"></span>
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

