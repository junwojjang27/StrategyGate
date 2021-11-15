<!-- 
*************************************************************************
* CLASS 명	: ChaList
* 작 업 자	: 하성준
* 작 업 일	: 2021-11-09
* 기	능	: 문화재청 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-11-09				최 초 작 업
**************************************************************************
-->

<!--
필독 : 다국어 지원기능에 의해서 화면에 나타나는 한글명칭은 message properties파일을 이용하여 수정할 것. 
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="chaVO" staticJavascript="false" xhtml="true" cdata="false"/>
<style>
    .test1 {margin:0px 0px 0px; text-align:right;}
    .test1 > a {display:inline-block; padding:10px 15px; color:#fff; font-size:13px; -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; background:#fb7a52}

    .test2 {margin:0px 0px 0px; text-align:center;}
    .test2 > a {display:inline-block; padding:10px 15px; color:#fff; font-size:13px; -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; background:#555555}
</style>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/menu/cha/chaList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}-50",
		height		:	"250",
		colModel	:	[
						{name:"kpiGbnId",	    index:"kpiGbnId",	width:15,	align:"center",	label:"체계구분"},
						{name:"straNo",	        index:"straNo",	    width:15,	align:"center",	label:"번호"},
						{name:"kpiNm",	        index:"kpiNm",	    width:100,	align:"center",	label:"체계",
							formatter: function (cellvalue, options, rowObject) {
								return "<a href='#' onclick='showDetail2(\"" + removeNull(rowObject.straTgtId) + "\",\"" + removeNull(rowObject.resultTgtId) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},//formatter : 색깔이나 액션을 주는 (데이터 가공) / 제목에 링크, 돋보기 표시 등
							unformat: linkUnformatter
						},
						{name:"temp",	        index:"temp",	        width:15,	align:"center",	label:"성과지표"},
						{name:"atchFileId",	    index:"atchFileId",	    width:15,	align:"center",	label:"첨부파일"},
						{name:"year",	        index:"year",	        width:100,	align:"center",	label:"null", hidden:true},
						{name:"kpiId",	        index:"kpiId",	        width:100,	align:"center",	label:"null", hidden:true},
						{name:"straTgtId",	    index:"straTgtId",	    width:100,	align:"center",	label:"null", hidden:true},
						{name:"resultTgtId",	index:"resultTgtId",	width:100,	align:"center",	label:"null", hidden:true},
						{name:"subjectTgtId",	index:"subjectTgtId",	width:100,	align:"center",	label:"null", hidden:true},
						{name:"unitId",	        index:"unitId",	        width:100,	align:"center",	label:"null", hidden:true},
						{name:"kpiPoolId",	    index:"kpiPoolId",	    width:100,	align:"center",	label:"null", hidden:true},
						{name:"year1Actual",	index:"year1Actual",	width:100,	align:"center",	label:"null", hidden:true},
						{name:"year2Actual",	index:"year2Actual",	width:100,	align:"center",	label:"null", hidden:true},
						{name:"year3Actual",	index:"year3Actual",	width:100,	align:"center",	label:"null", hidden:true},
						{name:"year0Target",	index:"year0Target",	width:100,	align:"center",	label:"null", hidden:true},
						{name:"year1Target",	index:"year1Target",	width:100,	align:"center",	label:"null", hidden:true},
						{name:"year2Target",	index:"year2Target",	width:100,	align:"center",	label:"null", hidden:true},
						{name:"year3Target",	index:"year3Target",	width:100,	align:"center",	label:"null", hidden:true},
						{name:"year4Target",	index:"year4Target",	width:100,	align:"center",	label:"null", hidden:true},
						{name:"basisContent",	index:"basisContent",	width:100,	align:"center",	label:"null", hidden:true},
						{name:"calContent",	    index:"calContent",	    width:100,	align:"center",	label:"null", hidden:true},
						{name:"dataContent",	index:"dataContent",	width:100,	align:"center",	label:"null", hidden:true},
						{name:"keyword",	    index:"keyword",	    width:100,	align:"center",	label:"null", hidden:true},
						{name:"sortOrder",	    index:"sortOrder",	    width:100,	align:"center",	label:"null", hidden:true},
						{name:"poolYn",	        index:"poolYn",	        width:100,	align:"center",	label:"null", hidden:true},
						{name:"createDt",	    index:"createDt",	    width:100,	align:"center",	label:"null", hidden:true},
						{name:"modifyDt",	    index:"modifyDt",	    width:100,	align:"center",	label:"null", hidden:true},
						{name:"deleteDt",	    index:"deleteDt",	    width:100,	align:"center",	label:"null", hidden:true},
						{name:"straTgtNm",	    index:"straTgtNm",	    width:100,	align:"center",	label:"null", hidden:true},
						{name:"straNo",	        index:"straNo",	        width:100,	align:"center",	label:"null", hidden:true},
						{name:"atchFileId",	    index:"atchFileId",	    width:100,	align:"center",	label:"null", hidden:true}
						],
		rowNum		: ${jqgrid_rownum_max},
		sortname	: "sortOrder",
		sortorder	: "asc",
		cellEdit	: true,
		multiselect	: true,
		loadComplete : function() {
			showDetail();
		}
	});
});

//성과목표 조회
function showDetail2(straTgtId, resultTgtId) {

	var f = document.form;
	f.straTgtId.value = straTgtId;
	f.resultTgtId.value = resultTgtId;

	if(isNotEmpty(resultTgtId)) { //성과목표일때
		sendAjax({
			"url" : "${context_path}/system/system/menu/cha/selectDetail7.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "setDetail2"
		});
	}
	/*else {	//전략목표일때
		sendAjax({
			"url" : "${context_path}/system/system/menu/cha/selectDetail77.do",
			"data" : getFormData("form"),
			"doneCallbackFunc" : "setDetail22"
		});
	}*/
}

// 성과목표 상세 조회 값 세팅
function setDetail2(data) {

	$("#addForm2").show();
	var dataVO = data.dataVO;

	alert(dataVO.resultTgtNm);

	voToForm(dataVO, "form", ["year", "resultTgtNm", "straTgtNm", "resultTgtNo"]);	//VO의 값을 form에 세팅
	// $("#userNm").text(dataVO.userNm);
	// $("#deptNm").text(dataVO.deptNm);
	// $("#useYn").val($("#findUseYn").val());
	// $("#title").val(dataVO.title);

	alert(dataVO.atchFileKey);

	var modifyYn = "Y";
	//첨부파일 ajax
	$.ajax({
		url: "${context_path}/system/system/menu/cha/chaAtchFile.do",
		data: {
			"modifyYn": modifyYn,
			//"atchFileId": dataVO.atchFileKey,
			"_csrf": getCsrf("form")
		},
		method: "POST",
		cache: false,
		dataType: "html"
	}).done(function (html) {
		// $("#spanAttachFile2").empty();
		// $("#spanAttachFile1").empty();
		$("#spanAttachFile2").html(html);
	}).fail(function (jqXHR, textStatus) {
		try {
			var json = JSON.parse(jqXHR.responseText);
			if (!isEmpty(json.msg)) {
				$.showMsgBox(json.msg);
			} else {
				$.showMsgBox(getMessage("errors.processing"));
			}
		} catch (e) {
			$.showMsgBox(getMessage("errors.processing"));
		}
	});
}

// 전략목표 상세 조회 값 세팅
function setDetail22(data) {

	$("#newForm").show();
	var dataVO = data.dataVO;

	voToForm(dataVO, "form", ["year", "category", "title", "userNm", "deptNm", "content"]);	//VO의 값을 form에 세팅
	$("#userNm").text(dataVO.userNm);
	$("#deptNm").text(dataVO.deptNm);
	$("#useYn").val($("#findUseYn").val());
	// $("#title").val(dataVO.title);

	var modifyYn = "Y";
	//첨부파일 ajax
	$.ajax({
		url: "${context_path}/system/system/menu/cha/chaAtchFile.do",
		data: {
			"modifyYn": modifyYn,
			"atchFileId": "${dataVO.atchFileKey}",
			"_csrf": getCsrf("form")
		},
		method: "POST",
		cache: false,
		dataType: "html"
	}).done(function (html) {
		$("#spanAttachFile2").empty();
		$("#spanAttachFile1").empty();
		$("#spanAttachFile2").html(html);
	}).fail(function (jqXHR, textStatus) {
		try {
			var json = JSON.parse(jqXHR.responseText);
			if (!isEmpty(json.msg)) {
				$.showMsgBox(json.msg);
			} else {
				$.showMsgBox(getMessage("errors.processing"));
			}
		} catch (e) {
			$.showMsgBox(getMessage("errors.processing"));
		}
	});
}

$(function(){	//전략목표@@@@@@@@@@@@@@@@@@@@@@
	var nowYear = parseInt($("#findYear").val());
	$("#list2").jqGrid({
		//url			:	"${context_path}/system/system/menu/cha/chaList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}-50",
		height		:	"250",
		colModel	:	[
			{name:"kpiNm",	index:"kpiNm",	width:20,	align:"center",	label:"성과지표", 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 30}},
			{name:"unitId",	index:"unitId",	width:10,	align:"center",	label:"단위", editable: true,  edittype: "select",
				formatter: 'select', editrules: {required: true}, editoptions: {value: getUnitSelect()}},
			{name:"year1Actual",	index:"year1Actual",	width:10,	align:"center",	label:nowYear-1, 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 20}},
			{name:"year0Target",	index:"year0Target",	width:10,	align:"center",	label:nowYear, 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 20}},
			{name:"year1Target",	index:"year1Target",	width:10,	align:"center",	label:nowYear+1, 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 20}},
			{name:"year2Target",	index:"year2Target",	width:10,	align:"center",	label:nowYear+2, 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 20}},
			{name:"year3Target",	index:"year3Target",	width:10,	align:"center",	label:nowYear+3, 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 20}},
			{name:"year4Target",	index:"year4Target",	width:10,	align:"center",	label:nowYear+4, 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 20}},
			{name:"basisContent",	index:"basisContent",	width:30,	align:"center",	label:"목표치 산출근거", 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 300}},
			{name:"calContent",	index:"calContent",	width:30,	align:"center",	label:"측정산식", 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 300}},
			{name:"dataContent",	index:"dataContent",	width:20,	align:"center",	label:"자료수집방법", 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 300}},

			{name:"atchFileId",	    index:"atchFileId",	width:15,	align:"center",	label:"첨부파일", hidden:true},
			{name:"kpiGbnId",	    index:"kpiGbnId",	width:15,	align:"center",	label:"체계구분", hidden:true},
			{name:"straNo",	        index:"straNo",	    width:15,	align:"center",	label:"번호", hidden:true},
			{name:"year",	        index:"year",	    width:100,	align:"center",	label:"null", hidden:true},
			{name:"kpiId",	        index:"kpiId",	    width:100,	align:"center",	label:"null", hidden:true},
			{name:"straTgtId",	    index:"straTgtId",	width:100,	align:"center",	label:"null", hidden:true},
			{name:"resultTgtId",	index:"resultTgtId",	width:100,	align:"center",	label:"null", hidden:true},
			{name:"subjectTgtId",	index:"subjectTgtId",	width:100,	align:"center",	label:"null", hidden:true},
			{name:"kpiPoolId",	    index:"kpiPoolId",	width:100,	align:"center",	label:"null", hidden:true},
			{name:"year2Actual",	index:"year2Actual",	width:100,	align:"center",	label:"null", hidden:true},
			{name:"year3Actual",	index:"year3Actual",	width:100,	align:"center",	label:"null", hidden:true},
			{name:"keyword",	    index:"keyword",	width:100,	align:"center",	label:"null", hidden:true},
			{name:"sortOrder",	    index:"sortOrder",	width:100,	align:"center",	label:"null", hidden:true},
			{name:"poolYn",	        index:"poolYn",	    width:100,	align:"center",	label:"null", hidden:true},
			{name:"createDt",	    index:"createDt",	width:100,	align:"center",	label:"null", hidden:true},
			{name:"modifyDt",   	index:"modifyDt",	width:100,	align:"center",	label:"null", hidden:true},
			{name:"deleteDt",	    index:"deleteDt",	width:100,	align:"center",	label:"null", hidden:true}
		],
		rowNum		: ${jqgrid_rownum_max},
		sortname	: "sortOrder",
		sortorder	: "asc",
		cellEdit	: true,
		multiselect	: true,
		loadComplete : function() {
		}
	});
	$("#addForm").hide();
});
jQuery("#list2").jqGrid('setGroupHeaders', {
	useColSpanStyle: true,
	groupHeaders:[
		{startColumnName: 'year1Actual', numberOfColumns: 1, titleText: '실적'},
		{startColumnName: 'year0Target', numberOfColumns: 5, titleText: '목표치'}
	]
});

//단위 선택
function getUnitSelect() {
	var selectStr = "";
	<c:forEach var="unitList" items="${codeUtil:getCodeList('013')}" varStatus="status">
	selectStr += "<c:out value="${unitList.codeId}"/>" + ":" + "<c:out value="${unitList.codeNm}"/>";
	<c:if test="${not status.last}">
	selectStr += ";";
	</c:if>
	</c:forEach>

	return selectStr;
}

$(function(){	//성과목표@@@@@@@@@@@@@@@@@@@@@@
	$("#list3").jqGrid({
		//url			:	"${context_path}/system/system/menu/cha/chaList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"250",
		colModel	:	[
			{name:"kpiNm",	index:"kpiNm",	width:20,	align:"center",	label:"성과지표", 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 30}},
			{name:"unitId",	index:"unitId",	width:10,	align:"center",	label:"단위",         editable: true,  edittype: "select",
				formatter: 'select', editrules: {required: true}, editoptions: {value: getUnitSelect()}},
			{name:"year3Actual",	index:"year2Actual",	width:10,	align:"center",	label:$("#findYear").val()-3, 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 30}},
			{name:"year2Actual",	index:"year3Actual",	width:10,	align:"center",	label:$("#findYear").val()-2, 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 30}},
			{name:"year1Actual",	index:"year1Actual",	width:10,	align:"center",	label:$("#findYear").val()-1, 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 30}},
			{name:"year0Target",	index:"year0Target",	width:10,	align:"center",	label:$("#findYear").val(), 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 30}},
			{name:"basisContent",	index:"basisContent",	width:30,	align:"center",	label:"목표치 산출근거", 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 30}},
			{name:"calContent",	index:"calContent",	width:30,	align:"center",	label:"측정산식", 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 30}},
			{name:"dataContent",	index:"dataContent",	width:20,	align:"center",	label:"자료수집방법", 	editable: true,  edittype: "text",
				editrules: {required: true, custom: true, custom_func: jqGridChkBytes}, editoptions: {maxlength: 30}},

			{name:"year1Target",	index:"year1Target",	width:10,	align:"center",	label:"2022", hidden:true},
			{name:"year2Target",	index:"year2Target",	width:10,	align:"center",	label:"2023", hidden:true},
			{name:"year3Target",	index:"year3Target",	width:10,	align:"center",	label:"2024", hidden:true},
			{name:"year4Target",	index:"year4Target",	width:10,	align:"center",	label:"2025", hidden:true},
			{name:"atchFileId",	    index:"atchFileId",	    width:15,	align:"center",	label:"첨부파일", hidden:true},
			{name:"kpiGbnId",	    index:"kpiGbnId",	    width:15,	align:"center",	label:"체계구분", hidden:true},
			{name:"straNo",	        index:"straNo",	        width:15,	align:"center",	label:"번호", hidden:true},
			{name:"year",	        index:"year",	        width:100,	align:"center",	label:"null", hidden:true},
			{name:"kpiId",	        index:"kpiId",	        width:100,	align:"center",	label:"null", hidden:true},
			{name:"straTgtId",	    index:"straTgtId",	    width:100,	align:"center",	label:"null", hidden:true},
			{name:"resultTgtId",	index:"resultTgtId",	width:100,	align:"center",	label:"null", hidden:true},
			{name:"subjectTgtId",	index:"subjectTgtId",	width:100,	align:"center",	label:"null", hidden:true},
			{name:"kpiPoolId",	    index:"kpiPoolId",	    width:100,	align:"center",	label:"null", hidden:true},
			{name:"keyword",	    index:"keyword",	    width:100,	align:"center",	label:"null", hidden:true},
			{name:"sortOrder",	    index:"sortOrder",	    width:100,	align:"center",	label:"null", hidden:true},
			{name:"poolYn",	        index:"poolYn",	        width:100,	align:"center",	label:"null", hidden:true},
			{name:"createDt",	    index:"createDt",	    width:100,	align:"center",	label:"null", hidden:true},
			{name:"modifyDt",	    index:"modifyDt",	    width:100,	align:"center",	label:"null", hidden:true},
            {name:"deleteDt",	    index:"deleteDt",	width:100,	align:"center",	label:"null", hidden:true}
		],
		rowNum		: ${jqgrid_rownum_max},
		sortname	: "sortOrder",
		sortorder	: "asc",
		cellEdit	: true,
		multiselect	: true,
		autowidth	: true,
		loadComplete : function() {
		}
	});
	$("#addForm2").hide();
	$(window).resize(function(){
		$("jqGridList").setGridWidth($(this).width() * .100);
	});
});
jQuery("#list3").jqGrid('setGroupHeaders', {
	useColSpanStyle: true,
	groupHeaders:[
		{startColumnName: 'year3Actual', numberOfColumns: 3, titleText: '실적'},
		{startColumnName: 'year0Target', numberOfColumns: 1, titleText: '목표치'}
	]
});

// 목록 조회
function searchList() {
	$("#addForm").hide();
 	reloadGrid("list", "form");
}

// 엑셀 다운로드
function excelDownload() {
	var f = document.form;
	f.action = "${context_path}/system/system/menu/cha/excelDownload.do";
	f.submit();
}

// 상세 조회
function showDetail() {
	var f = document.form;
	f.year.value = year;
	//f.kpiId.value = kpiId;

	sendAjax({
		"url" : "${context_path}/system/system/menu/cha/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	var dataVO = data.dataVO;
	
	voToForm(dataVO, "form", ["mission", "vision"]);

    /**********첨부파일 시작**********/
    var modifyYn = "N";
    //첨부파일 ajax
    $.ajax({
        url: "${context_path}/system/system/menu/cha/popAtchFile.do",
        data: {
            "modifyYn": modifyYn,
            "atchFileId": dataVO.atchFileKey,
            "_csrf": getCsrf("form")
        },
        method: "POST",
        cache: false,
        dataType: "html"
    }).done(function (html) {
        $("#matchFileId").html(html);
    }).fail(function (jqXHR, textStatus) {
        try {
            var json = JSON.parse(jqXHR.responseText);
            if (!isEmpty(json.msg)) {
                $.showMsgBox(json.msg);
            } else {
                $.showMsgBox(getMessage("errors.processing"));
            }
        } catch (e) {
            $.showMsgBox(getMessage("errors.processing"));
        }
    });
    /**********첨부파일 끝**********/
	/**********첨부파일 시작**********/
	var modifyYn = "N";
	//첨부파일 ajax
	$.ajax({
		url: "${context_path}/system/system/menu/cha/popAtchFile2.do",
		data: {
			"modifyYn": modifyYn,
			"atchFileId": dataVO.atchFileKey2,
			"_csrf": getCsrf("form")
		},
		method: "POST",
		cache: false,
		dataType: "html"
	}).done(function (html) {
		$("#vatchFileId").html(html);
	}).fail(function (jqXHR, textStatus) {
		try {
			var json = JSON.parse(jqXHR.responseText);
			if (!isEmpty(json.msg)) {
				$.showMsgBox(json.msg);
			} else {
				$.showMsgBox(getMessage("errors.processing"));
			}
		} catch (e) {
			$.showMsgBox(getMessage("errors.processing"));
		}
	});
	/**********첨부파일 끝**********/
}

// 정렬순서저장
function saveSortOrder() {
	if(!gridToForm("list", "form", true)) return false;
	
	sendAjax({
		"url" : "${context_path}/system/system/menu/cha/saveSortOrder.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}

// 등록(성과목표추가)
function addData() {
    $("#addForm").hide();
	$("#addForm2").show();

	resetForm("form", ["straTgtNm", "straNo", "resultTgtNm"]);

	$("#year").val($("#findYear").val());

	var modifyYn = "Y";
	//첨부파일 ajax
	$.ajax({
		url: "${context_path}/system/system/menu/cha/chaAtchFile.do",
		data: {
			"modifyYn": modifyYn,
			"_csrf": getCsrf("form")
		},
		method: "POST",
		cache: false,
		dataType: "html"
	}).done(function (html) {
		$("#spanAttachFile2").empty();
		$("#spanAttachFile1").empty();
		$("#spanAttachFile2").html(html);
	}).fail(function (jqXHR, textStatus) {
		try {
			var json = JSON.parse(jqXHR.responseText);
			if (!isEmpty(json.msg)) {
				$.showMsgBox(json.msg);
			} else {
				$.showMsgBox(getMessage("errors.processing"));
			}
		} catch (e) {
			$.showMsgBox(getMessage("errors.processing"));
		}
	});
}

// 등록2(전략목표추가)
function addData2() {
    $("#addForm2").hide(); // 성과목표
    $("#addForm").show();	// 전략목표

    resetForm("form", ["straTgtNm", "straNo"]);
    $("#year").val($("#findYear").val());

	var modifyYn = "Y";
	//첨부파일 ajax
	$.ajax({
		url: "${context_path}/system/system/menu/cha/chaAtchFile.do",
		data: {
			"modifyYn": modifyYn,
			"_csrf": getCsrf("form")
		},
		method: "POST",
		cache: false,
		dataType: "html"
	}).done(function (html) {
		$("#spanAttachFile1").empty();
		$("#spanAttachFile2").empty();		//다 지워야 정상적으로 뜸.

		$("#spanAttachFile1").html(html);
	}).fail(function (jqXHR, textStatus) {
		try {
			var json = JSON.parse(jqXHR.responseText);
			if (!isEmpty(json.msg)) {
				$.showMsgBox(json.msg);
			} else {
				$.showMsgBox(getMessage("errors.processing"));
			}
		} catch (e) {
			$.showMsgBox(getMessage("errors.processing"));
		}
	});
}

//행 추가 (전략목표 행)
function addRow() {
	//var rowId = "newRow"+$.jgrid.guid++;
	var rowId = $("#list2").getGridParam("reccount"); // 페이징 처리 시 현 페이지의 Max RowId 값

	/*조회된 데이터 없을시 나타나는 문구 없앰.*/
	if($("#list2").find(".noGridResult").length > 0){
		$("#list2").find(".noGridResult").closest("tr").hide();
	}

	var rowData = {
		year: $("#findYear").val()
	};
	$("#list2").jqGrid("addRowData", rowId + 1, rowData, 'last');
	$('#list2 tr:last').focus();
}

//행 추가 (전략목표 행)
function addRow2() {
	//var rowId = "newRow"+$.jgrid.guid++;
	var rowId = $("#list3").getGridParam("reccount"); // 페이징 처리 시 현 페이지의 Max RowId 값

	/*조회된 데이터 없을시 나타나는 문구 없앰.*/
	if($("#list3").find(".noGridResult").length > 0){
		$("#list3").find(".noGridResult").closest("tr").hide();
	}

	var rowData = {
		year: $("#findYear").val()
	};
	$("#list3").jqGrid("addRowData", rowId + 1, rowData, 'last');
	$('#list3 tr:last').focus();
}

// 저장 (임무)
function saveData() {
	var f = document.form;

	sendAjax({
		"url" : "${context_path}/system/system/menu/cha/saveCha.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});

}

// 저장 (비전)
function saveData2() {
	var f = document.form;

	sendAjax({
		"url" : "${context_path}/system/system/menu/cha/saveCha2.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}

// 저장 (전략목표)
function saveData3() {
	var f = document.form;

	gridToFormChanged("list2", "form");

	sendMultipartForm({
		"url": "${context_path}/system/system/menu/cha/saveCha3.do",
		"formId": "form",
		"fileModules": [upload1],
		"doneCallbackFunc": "saveData33"
	});

}

function saveData33() {
	var f = document.form;

	gridToFormChanged("list2", "form");

	sendAjax({
		"url": "${context_path}/system/system/menu/cha/saveCha33.do",
		"data": getFormData("form"),
		"doneCallbackFunc": "searchList"
	});

}

// 저장 (성과목표)
function saveData4() {
	var f = document.form;

	gridToFormChanged("list3", "form");

	sendMultipartForm({
		"url": "${context_path}/system/system/menu/cha/saveCha4.do",
		"formId": "form",
		"fileModules": [upload1],
		"doneCallbackFunc": "saveData44"
	});

}

function saveData44() {
	var f = document.form;

	gridToFormChanged("list3", "form");

	sendAjax({
		"url": "${context_path}/system/system/menu/cha/saveCha44.do",
		"data": getFormData("form"),
		"doneCallbackFunc": "searchList"
	});

}


//파일 첨부(임무)
function popAtchFile() {
	var f = document.form;
	openFancybox({
		"url" : "${context_path}/system/system/menu/cha/chaListDetail.do",
		"data" : getFormData("form")
	});

}

//파일 첨부(비전)
function popAtchFile2() {
	var f = document.form;

	openFancybox({
		"url" : "${context_path}/system/system/menu/cha/chaListDetail2.do",
		"data" : getFormData("form")
	});

}

// 삭제
function deleteData() {
	if(deleteDataToForm("list", "kpiId", "form")) {
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
		"url" : "${context_path}/system/system/menu/cha/deleteCha.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="year"/>
	<form:hidden path="kpiId"/>
    <form:hidden path="atchFileKey" value="${dataVO.atchFileKey}"/>
	<form:hidden path="straTgtId"/>
	<form:hidden path="resultTgtId"/>

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

	<div id="newForm">
		<div class="ptitle" id="titleChaNm"></div>
		<div class="tbl-type02">
			<table summary="" id="table1">
				<caption></caption>
				<colgroup>
					<col width="14%"/>
					<col width="50%"/>
					<col width="12%"/>
					<col width="12%"/>
					<col width="12%"/>
				</colgroup>
				<tbody>
				<tr>
					<th >구분</th>
					<th >내용</th>
					<th >첨부파일</th>
					<th >파일첨부</th>
					<th >저장</th>
				</tr>
				<tr>
					<th scope="row"><label for="mission">임무(*)</label></th>
					<td ><form:input path="mission" class="t-box01" maxlength="300" id="mission"/></td>
                    <td ><span id="matchFileId"></span></td>
                    <td >
                        <div class="test2" >
                            <a href="#" class="save" onclick="popAtchFile();return false;">첨부</a>
                        </div>
                    </td>
                    <td >
                        <div class="test2" id="save1">
                            <a href="#" class="save" onclick="saveData();return false;">저장</a>
                        </div>
                    </td>
				</tr>
				<tr>
					<th scope="row"><label for="vision">비전(*)</label></th>
					<td ><form:input path="vision" class="t-box01" maxlength="300" id="vision"/></td>
					<td ><span id="vatchFileId"></span></td>
					<td >
                        <div class="test2" >
                        <a href="#" class="save" onclick="popAtchFile2();return false;">첨부</a>
                        </div>
                    </td>
                    <td >
                        <div class="test2" >
                            <a href="#" class="save" onclick="saveData2();return false;">저장</a>
                        </div>
                    </td>
				</tr>
				</tbody>
			</table>
		</div>
		<div class="tbl-bottom">
			<div class="tbl-wbtn">
			</div>
		</div>
	</div>

	<div class="gridContainer">
		<table id="list"></table>
	</div>
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="addData2();return false;">전략목표 추가</a>
			<a href="#" class="new" onclick="addData();return false;">성과목표 추가</a>
			<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>

	<div id="addForm">		<%--전략목표츄가@@@@@@@@--%>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="20%"/>
					<col width="80%"/>
				</colgroup>
				<tbody>
				<tr>
					<th scope="row"><label for="straTgtNm">전략목표명(*)</label></th>
					<td ><form:input path="straTgtNm" class="t-box01" maxlength="300" id="straTgtNm"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="straNo">번호(*)</label></th>
					<td ><form:input path="straNo" class="t-box01" maxlength="300" id="straNo"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="atchFileId">첨부파일(*)</label></th>
					<td ><span id="spanAttachFile1"></span></td>
				</tr>
				</tbody>
			</table>
		</div>
		<div class="btn-dw"></div>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="20%"/>
					<col width="80%"/>
				</colgroup>
				<tbody>
				<tr>
					<th >성과지표(*)</th>
					<td >
                        <div class="test1" >
                            <a href="#" class="save" onclick="addRow();return false;">성과지표 추가</a>
                            <a href="#" class="new" onclick="addData();return false;">성과지표 삭제</a>
                        </div>
                   </td>
				</tr>
				</tbody>
			</table>
		</div>
		<div class="tbl-bottom">
			<div class="tbl-wbtn">
			</div>
				<%--<div class="tbl-btn">
                    <a href="#" class="save" onclick="saveData();return false;"><spring:message code="button.save"/></a>
                </div>--%>
		</div>




		<div class="gridContainer">
			<table id="list2"></table>
			<%--<div id="pager"></div>--%>
		</div>

        <div class="tbl-bottom tbl-bottom2">
            <div class="tbl-btn">
                <a href="#" class="save" onclick="saveData3();return false;">일괄저장</a>
            </div>
        </div>
	</div>

	<div id="addForm2">		<%--성과목표츄가@@@@@@@@--%>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="20%"/>
					<col width="80%"/>
				</colgroup>
				<tbody>
				<tr>
					<th scope="row"><label for="straTgtNm2">전략목표명(*)</label></th>
					<td >
						<form:select path="straTgtNm2" class="select wx400" >
							<form:options items="${straList}"  itemLabel="straTgtNm" itemValue="straTgtId" />
						</form:select>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="resultTgtNm">성과목표명(*)</label></th>
					<td ><form:input path="resultTgtNm" class="t-box01" maxlength="300" id="resultTgtNm"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="resultTgtNo">번호(*)</label></th>
					<td ><form:input path="resultTgtNo" class="t-box01" maxlength="300" id="resultTgtNo"/></td>
				</tr>
				<tr>
					<th scope="row"><label for="atchFileId2">첨부파일(*)</label></th>
					<td ><span id="spanAttachFile2"></span></td>
				</tr>
				</tbody>
			</table>
		</div>
		<div class="btn-dw"></div>
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="20%"/>
					<col width="80%"/>
				</colgroup>
				<tbody>
				<tr>
					<th >성과지표(*)</th>
					<td >
                        <div class="test1" >
                            <a href="#" class="save" onclick="addRow2();return false;">성과지표 추가</a>
                            <a href="#" class="new" onclick="addData();return false;">성과지표 삭제</a>
                        </div>
                    </td>
				</tr>
				</tbody>
			</table>
		</div>
		<div class="tbl-bottom">
			<div class="tbl-wbtn">
			</div>
				<%--<div class="tbl-btn">
                    <a href="#" class="save" onclick="saveData();return false;"><spring:message code="button.save"/></a>
                </div>--%>
		</div>



		<div class="gridContainer">
			<table id="list3"></table>
				<%--<div id="pager"></div>--%>
		</div>

        <div class="tbl-bottom tbl-bottom2">
            <div class="tbl-btn">
                <a href="#" class="save" onclick="saveData4();return false;">일괄저장</a>
            </div>
        </div>
	</div>
</form:form>

