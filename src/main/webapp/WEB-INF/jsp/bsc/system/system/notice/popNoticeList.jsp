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
	$("#popNoticeList").jqGrid({
		url			:	"${context_path}/comPop/bsc/system/system/notice/popNoticeList_json.do",
		postData	:	getFormData("popNoticeform"),
		width		:	"760",
		height		:	"250",
		colModel	:	[
						{name:"id",	index:"id",	width:80,	align:"center",	label:"<spring:message code="word.number"/>"	},
						{name:"subject",	index:"subject",	width:200,	align:"left",	label:"<spring:message code="word.title"/>",
							formatter:function(cellvalue, options, rowObject) {
								return "<a href='#' onclick='showPopNoticeDetail(\"" + removeNull(rowObject.id) + "\");return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
							},
							unformat:linkUnformatter
						},
						{name:"createDtStr",index:"createDtStr",width:150,	align:"center",					label:"<spring:message code="word.insertDT2"/>"}
						],
		rowNum		: ${jqgrid_rownum_max},
		sortname	: "createDtStr",
		sortorder	: "desc",
		multiselect	: true
	});
	
	$("#newPopNoticeForm").hide();
	/*popup인 경우 gridContainer  popGridResize() 호출 해야 함.*/
	popGridResize("popNoticeList");
});

// 목록 조회
function searchPopNoticeList() {
	$("#newForm").hide();
 	reloadGrid("popNoticeList", "popNoticeform");
}

// 상세 조회
function showPopNoticeDetail(id) {
	var f = document.popNoticeform;
	f.id.value = id;
	
	sendAjax({
		"url" : "${context_path}/comPop/bsc/system/system/notice/popSelectNoticeDetail.do",
		"data" : getFormData("popNoticeform"),
		"doneCallbackFunc" : "setPopNoticeDetail"
	});
}

// 상세 조회 값 세팅
function setPopNoticeDetail(data) {
	$("#newPopNoticeForm").show();

	var dataVO = data.dataVO;
	$("#titlePopNoticeNm").text("<spring:message code="word.title"/> : " + dataVO.subject);
	
	$("#popSubject").empty();
	$("#popContent").empty();
	$("#popSubject").append(dataVO.subject);
	$("#popContent").append(nl2br(dataVO.content));
	var modifyYn = "N";
	//첨부파일 ajax
	$.ajax({
		url : "${context_path}/comPop/bsc/system/system/notice/popNoticeDetail.do",
		data : {
					"modifyYn" : modifyYn,
					"atchFileId" : dataVO.atchFileKey,
					"_csrf" : getCsrf("popNoticeform")
				},
		method : "POST",
		cache : false,
		dataType : "html"
	}).done(function(html) {
		$("#popSpanAttachFile").html(html);
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
}

</script>
<div class="popup wx800">
	<p class="title"><spring:message code="word.notice" /></p>
	<form:form commandName="searchVO" id="popNoticeform" name="popNoticeform" method="post" onsubmit="searchPopNoticeList();return false;">
 	<form:hidden path="id"/>
	<div class="sch-bx">
		<ul>
			<li>
				<label for="colName"><spring:message  code="bsc.common.msg.searchCondition" /></label>
				<form:select path="colName" class="select wx150">
					<form:option value="subject"><spring:message code="word.title"/></form:option>
					<form:option value="content"><spring:message code="word.content"/></form:option>
				</form:select>
				<span class="searchBar"><form:input path="searchKeyword" class="t-box01 wx300" maxlength="20" title="검색어" /> </span>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchPopNoticeList();return false;"><spring:message code="button.search"/></a>
	</div>
    <div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="popNoticeList"></table>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-wbtn">
		</div>
		<div class="tbl-btn">
			<a href="#" class="save" onclick="$.fancybox.close();return false;"><spring:message code="button.close"/></a>
		</div>
	</div>
	<div id="newPopNoticeForm">
		<div class="ptitle" id="titlePopNoticeNm"></div>
		<div class="tbl-type02 mb20">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="20%"/>
					<col width="80%"/>
				</colgroup>
				<tbody>
					<tr>
						<th scope="row"><label for=popSubject><spring:message code="word.title"/></label><span class="red">(*)</span></th>
						<td><span id="popSubject"></span></td>
					</tr>
					<tr>
						<th scope="row"><label for="popContent"><spring:message code="word.noticeContent" /></label><span class="red">(*)</span></th> 
						<td class="pd10">
							<span id="popContent"></span>
						</td>
					</tr>
					<tr>
						<th scope="row"><label><spring:message code="word.atchFile"/></label></th>
						<td>
							<span id="popSpanAttachFile"></span>
						</td>
					</tr> 
				</tbody>
			</table>
		</div>
	</div>
</form:form>
</div>
