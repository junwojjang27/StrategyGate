<!-- 
*************************************************************************
* CLASS 명	: NoticeList
* 작 업 자	: 박정현
* 작 업 일	: 2018-03-29
* 기	능	: 공지사항 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-03-29				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="noticeVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function(){
	//- 검색조건 준비 완료된상태
	$("#list").jqGrid({
		url			:	"${context_path}/bsc/system/system/notice/noticeList_json.do",//서버로 요청 //system이후부터 수정되어야함
		postData	:	getFormData("form"),//폼에있는 검색조건들의 데이터 반환
		width		:	"${jqgrid_width}",
		height		:	"250",
		colModel	:	[// 화면에 뿌려지는 컬럼명들 id => vo변수명 , label : 화면에 표시되는 글자들,,?
						{name:"id",	index:"id",	width:80,	align:"center",	label:"<spring:message code="word.number"/>"	},
						{name:"subject",	index:"subject",	width:200,	align:"left",	label:"<spring:message code="word.title"/>",
							formatter:function(cellvalue, options, rowObject) {//(넘어온값, )
								return "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.id) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},//formatter : 색깔이나 액션을 주는 (데이터 가공) / 제목에 링크, 돋보기 표시 등
							unformat:linkUnformatter
						},
						{name:"popPreview",		index:"popPreview",	width:100,	align:"center",	label:"<spring:message code="word.preview"/>",
							formatter:function(cellvalue, options, rowObject) {
								if(rowObject.popupGbnId == "Y"){
									return "<a href='#' onclick='popPreview(\"" + options.rowId + "\");clickGridRow(this);return false;'><img src='${img_path}/icon_search.gif'/></a>";
								}else{
									return "";
								}
							}
						},
						{name:"createDtStr",index:"createDtStr",width:150,	align:"center",					label:"<spring:message code="word.insertDT2"/>"},
						{name:"popupGbnNm",	index:"popupGbnNm",	width:80,	align:"center",					label:"<spring:message code="word.popupYn"/>"},
						{name:"fromDt",		index:"fromDt",		width:100,	align:"center",	hidden:true,	label:"<spring:message code="word.popupStartDt"/>"},
						{name:"toDt",		index:"toDt",		width:100,	align:"center",	hidden:true,	label:"<spring:message code="word.popupEndDt"/>"},
						{name:"width",		index:"width",		width:150,	align:"center",					label:"<spring:message code="word.width"/>"},
						{name:"height",		index:"height",		width:150,	align:"center",					label:"<spring:message code="word.height"/>"},
						{name:"popupGbnId",	index:"popupGbnId",	width:80,	align:"center",	hidden:true,	label:"<spring:message code="word.popupYn"/>"},
						],
		rowNum		: ${jqgrid_rownum_max},
		sortname	: "createDtStr",
		sortorder	: "desc",
		multiselect	: true
	});
	
	$("#newForm").hide();// 제이쿼리 선택자 /선택해서 액션취하게하는것 #:id, .:class 
	
	//date선택 팝업화면
	$(".datepicker").datepicker();//날짜 선택
	
	//숫자만 가능
	$("#width, #height").numericOnly();

	/******************************************
	 * 팝업변경시 disabled
	 *********************************ㅋ*********/
	$("#popupGbnId").change(function() {
		if($(this).val()=='N'){
			$("#fromDt, #toDt").prop("disabled", true);
			$("#fromDt, #toDt").val("");
		}else{
			$("#fromDt, #toDt").prop("disabled", false);
		}
	});
	
	$("#searchKeyword").keyup(function(e) {
		if(e.keyCode == 13) searchList();
	});
});

// 미리보기 팝업
function popPreview(id) {
	var rowData = $("#list").jqGrid("getRowData", id);

	$.openWinByName({
		url:"${context_path}/bsc/system/system/notice/popNoticeForm.do?id="+rowData.id+"&layout=popup",
		width:rowData.width,
		height:rowData.height,
		scroll:"no",
		useForm:true
	});
}

// 목록 조회
function searchList() {
	$("#newForm").hide();
	reloadGrid("list", "form");
}

// 상세 조회
function showDetail(id) {
	var f = document.form;
	f.id.value = id;
	
	sendAjax({
		"url" : "${context_path}/bsc/system/system/notice/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
}

// 상세 조회 값 세팅
function setDetail(data) {
	$("#newForm").show();

	var dataVO = data.dataVO;
	
	$("#titleNoticeNm").text("<spring:message code="word.title"/> : " + dataVO.subject);
	voToForm(dataVO, "form", ["id","popupGbnId","fromDt","toDt","width","height","createDt"]);
	$("#subject").val(dataVO.subject);
	$("#content").val(dataVO.content);
	$("#subject").focus();

	if(dataVO.popupGbnId == 'N'){
		$("#fromDt, #toDt").prop("disabled", true);
		$("#fromDt, #toDt").val("");
	}else{
		$("#fromDt, #toDt").prop("disabled", false);
	}
	
	var modifyYn = "Y";
	//첨부파일 ajax
	$.ajax({
		url : "${context_path}/bsc/system/system/notice/noticeDetail.do",
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

// 등록
function addData() {
	$("#newForm").show();
	$("#titleNoticeNm").text("<spring:message code="word.notice"/>");
	resetForm("form", ["id","subject","content","popupGbnId","fromDt","toDt","width","height","createDt","atchFileId"]);
	$("#subject").focus();

	if($("#popupGbnId").val() == 'N'){
		$("#fromDt, #toDt").prop("disabled", true);
		$("#fromDt, #toDt").val("");
	}else{
		$("#fromDt, #toDt").prop("disabled", false);
	}
	
	var modifyYn = "Y";
	//첨부파일 ajax
	$.ajax({
		url : "${context_path}/bsc/system/system/notice/noticeDetail.do",
		data : {
					"modifyYn" : modifyYn,
					"_csrf" : getCsrf("form")
				},
		method : "POST",
		cache : false,
		dataType : "html"
	}).done(function(html) {
		$("#spanAttachFile").empty();
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

// 저장
function saveData() {
	var f = document.form;
	
	var oriToDt = $("#toDt").val();
	var oriFromDt = $("#fromDt").val();
	$("#toDt").val($("#toDt").val().replace(/\./g, ''));
	$("#fromDt").val($("#fromDt").val().replace(/\./g, ''));

	if(!validateNoticeVO(f)) {
		$("#toDt").val(oriToDt);
		$("#fromDt").val(oriFromDt);
		return;
	}

	if( $("#popupGbnId").val() == "Y" ){
		if(isEmpty($("#width").val())) {
			$.showMsgBox("<spring:message code="bsc.system.notice.noticeList.width"/>");
			return;
		}
		if(isEmpty($("#height").val())) {
			$.showMsgBox("<spring:message code="bsc.system.notice.noticeList.height"/>");
			return;
		}
		if(isEmpty($("#fromDt").val())) {
			$.showMsgBox("<spring:message code="bsc.system.notice.noticeList.fromDt"/>");
			return;
		}
		if(isEmpty($("#toDt").val())) {
			$.showMsgBox("<spring:message code="bsc.system.notice.noticeList.toDt"/>");
			return;
		}
		
		if($("#fromDt").val() > $("#toDt").val()) {
			$("#toDt").val(oriToDt);
			$("#fromDt").val(oriFromDt);
			$.showMsgBox("<spring:message code="bsc.system.notice.noticeList.error"/>");
			return;
		}
	}
	
	sendMultipartForm({
		"url" : "${context_path}/bsc/system/system/notice/saveNotice.do",
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
	if(deleteDataToForm("list", "id", "form")) {
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
		"url" : "${context_path}/bsc/system/system/notice/deleteNotice.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "checkResult"
	});
}
</script>



<form:form commandName="searchVO" id="form" name="form" method="post" enctype="multipart/form-data">
	<form:hidden path="id"/>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="colName"><spring:message  code="bsc.common.msg.searchCondition" /></label>
				<form:select path="colName" class="select wx100">
					<form:option value="subject"><spring:message code="word.title"/></form:option>
					<form:option value="content"><spring:message code="word.content"/></form:option>
				</form:select>
				<span class="searchBar"><form:input path="searchKeyword" class="t-box01 wx200" maxlength="20"/> </span>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	
	
	<div class="btn-dw">
	</div>
	
	
	<div class="gridContainer">
		<table id="list"></table>
	</div>
	
	
	<div class="tbl-bottom mt0"> 
		<div class="tbl-btn">
			<a href="#" class="new" onclick="addData();return false;"><spring:message code="button.add"/></a>
			<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
		</div>
	</div>
	
	<div id="newForm">
		<div class="ptitle" id="titleNoticeNm"></div>
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
						<th scope="row"><label for=subject><spring:message code="word.title"/></label><span class="red">(*)</span></th>
						<td colspan="3"><form:input path="subject" class="t-box01" maxlength="100"/></td>
					</tr>
					<tr>
						<th scope="row"><label for="content"><spring:message code="word.noticeContent" /></label><span class="red">(*)</span></th> 
						<td colspan="3">
							<p><form:textarea path="content" maxlength="4000"/></p>
							<p class="byte"><label id="contentBytes">0</label><label> / 4000byte</label></p>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="popupGbnId"><spring:message code="word.popupYn"/></label></th>
						<td colspan="3">
							<form:select path="popupGbnId" class="select" items="${codeUtil:getCodeList('011')}" itemLabel="codeNm" itemValue="codeId">
							</form:select>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="width"><spring:message code="word.popupWidth"/></label></th>
						<td><form:input path="width" class="t-box01" maxlength="5"/></td>
						<th scope="row"><label for="height"><spring:message code="word.popupHeight"/></label></th>
						<td><form:input path="height" class="t-box01" maxlength="5"/></td>
					</tr>
					<tr>
							<th scope="row"><label for=fromDt><spring:message code="word.popupStartDt"/></label></th>
							<td>
							<form:input path="fromDt" class="inputbox datepicker" readonly="true" maxlength="10" />
<%--							<a href="#" onclick="cancelDate('fromDt');return false;"><img src="${img_path}/common/board/ic_cancel.gif" class="valign_middle" alt="시작일자 삭제"/></a> --%>
						</td>
							<th scope="row"><label for=toDt><spring:message code="word.popupEndDt"/></label></th>
							<td>
							<form:input path="toDt" class="inputbox datepicker" readonly="true" maxlength="10" />
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

