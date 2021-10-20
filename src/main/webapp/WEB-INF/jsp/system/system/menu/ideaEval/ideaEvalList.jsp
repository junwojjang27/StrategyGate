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
		height		:	"300",		//jqGrid크기를 제안 수에 맞게 변경
		colModel	:	[
						{name:"ideaGbnCd",	index:"ideaGbnCd",	width:20,	align:"center",	label:"제안구분"},
						{name:"category",	index:"category",	width:20,	align:"center",	label:"카테고리"},
						{name:"title",		index:"title",		width:100,	align:"left",	label:"제목",
							formatter: function (cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.ideaCd) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},//formatter : 색깔이나 액션을 주는 (데이터 가공) / 제목에 링크, 돋보기 표시 등
							unformat: linkUnformatter
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
		}
	});

	$("#newForm").hide();

	$("#findUserNm").keyup(function (e) {		//enter눌렀을 때 searchList()로 이동
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

// 상세 조회
function showDetail(ideaCd) {
	var f = document.form;
	f.ideaCd.value = ideaCd;

    var num = $("#list").getGridParam("reccount");		//현재 화면에 표시되고있는 레코드 숫자
    var rowData;
    for (var n = 1; n <= num; n++) {	//1 ~ (화면에 표시되고있는 레코드 숫자) 까지 반복하여 ideaCd를 비교해서 해당 제안을 찾음.
        rowData = $("#list").jqGrid("getRowData", n);
        if(rowData.ideaCd == $("#ideaCd").val()) {
            if (rowData.userId == "${sessionScope.loginVO.userId}") {
                $.showMsgBox("본인의 제안은 평가할 수 없습니다.");
                return false;
            }

            var startTemp = rowData.startDt.replace(/\./g, ''); //날짜 비교를 위함.
            var endTemp = rowData.endDt.replace(/\./g, '');
            var now = new Date(); //현재 시간 구하기

            year = now.getFullYear();
            month = now.getMonth() + 1;
            date = now.getDate();

            today = year * 10000 + month * 100 + date;

            if (today < startTemp) {
                $.showMsgBox("평가 시작일 이전입니다");
                return false;
            }

            if (today > endTemp) {
                $.showMsgBox("평가가 종료되었습니다.");
                return false;
            }
        }
    }

	loadPage("${context_path}/system/system/menu/ideaEval/ideaEvalDetail.do", "form");
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
			<li>
				<label for="findEvalDegreeId"><spring:message code="word.evalDegree"/></label>
				<form:select path="findEvalDegreeId" class="select wx100">
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
</form:form>

