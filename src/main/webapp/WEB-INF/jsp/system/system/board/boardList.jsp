<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
		url			:	"${context_path}/system/system/board/boardList_json.do",
		postData	:	getFormData("form"),
		width		:	"${jqgrid_width}",
		height		:	"${jqgrid_height}",
		colModel	:	[
							{name:"seq",			index:"seq",			hidden:true},
							{name:"groupOrder",		index:"groupOrder",		hidden:true},
							{name:"groupLevel",		index:"groupLevel",		hidden:true},
							{name:"insertUserId",	index:"insertUserId",	hidden:true},
							{name:"replyCnt",		index:"replyCnt",		hidden:true},
							<c:if test="${boardSetting.useCommentYn eq 'Y'}">
								{name:"commentCnt",		index:"commentCnt",		hidden:true},
							</c:if>
							<c:choose>
								<c:when test="${boardSetting.privateYn eq 'Y'}">
									{name:"groupSeq",		index:"groupSeq",		hidden:true},
								</c:when>
								<c:otherwise>
									{name:"groupSeq",		index:"groupSeq",		width:30,		align:"center",	label:"<spring:message code="word.number"/>",	sortable:false,
										formatter:function(cellvalue, options, rowObject) {
											if(rowObject.groupOrder == 0) {
												return cellvalue;
											} else {
												return "";
											}
										}
									},
								</c:otherwise>
							</c:choose>
							{name:"title",	index:"title",	width:200,		align:"left",	title:true,	label:"<spring:message code="word.title"/>",	sortable:false,
								formatter:function(cellvalue, options, rowObject) {
									var padding = "";
									var value = escapeHTML(removeNull(cellvalue));
									if(rowObject.groupLevel > 0) {
										padding = "<img src='${img_path}/tree.gif' class='mr5' style='margin-left:" + ((rowObject.groupLevel-1) * 16) + "px'/>";
									}
									<c:if test="${boardSetting.useCommentYn eq 'Y'}">
										if(rowObject.commentCnt > 0) {
											value += " [" + rowObject.commentCnt + "]";
										}
									</c:if>
									return padding + "<a href='#' onclick='showDetail(\"" + removeNull(rowObject.seq) + "\");clickGridRow(this);return false;'>" + value + "</a>";
								}
							},
							{name:"userNm",		index:"userNm",		width:50,		align:"center",	title:true,	label:"<spring:message code="word.insertUser"/>",	sortable:false},
							{name:"hit",		index:"hit",		width:30,		align:"center",	title:true,	label:"<spring:message code="word.hit"/>",	sortable:false},
							{name:"createDt",	index:"createDt",	width:50,		align:"center",	title:true,	label:"<spring:message code="word.insertDT"/>",	sortable:false}
						],
		rowNum		: ${jqgrid_rownum},
		pager		: "pager",
		onPaging	: function (pgButton) {
					setGridPaging($(this).attr("id"), pgButton, "searchList");
		},
		loadonce	: false
		<sec:authorize access="hasRole('01')">
			, multiselect	: true
			, loadComplete : function() {
				hideGridCheckbox("list", "replyCnt", 0, false);
			}
		</sec:authorize>
	});
	
	$("#searchKeyword, #userNm").keyup(function(e) {
		if(e.keyCode == 13) searchList();
	});
});

// 목록 조회
function searchList(pageNum) {
	if(isEmpty(pageNum)) {
		pageNum = parseInt($("#page").val(), 10);
	}
 	reloadGrid("list", "form", pageNum);
}

// 상세 조회
function showDetail(seq) {
	var f = document.form;
	f.seq.value = seq;
	loadPage("${context_path}/system/system/board/boardDetail.do", "form");
}

<c:if test="${boardSetting.writable}">
// 등록 폼으로 이동
function goForm() {
	var f = document.form;
	f.seq.value = -1;
	loadPage("${context_path}/system/system/board/boardForm.do", "form");
}
</c:if>

<sec:authorize access="hasRole('01')">
// 삭제
function deleteData() {
	if(deleteDataToForm("list", "seq", "form")) {
		$.showConfirmBox("<spring:message code="common.delete.msg"/>", function() {
			sendAjax({
				"url" : "${context_path}/system/system/board/deleteBoardAll.do",
				"data" : getFormData("form"),
				"doneCallbackFunc" : "reloadGrid",
				"doneCallbackArgs" : ["list", "form", 1]
			});
		});
	}
}
</sec:authorize>
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="page"/>
	<form:hidden path="rows" value="10"/>
	<form:hidden path="boardId"/>
	<form:hidden path="seq"/>
	<div class="sch-bx">
		<ul>
			<li>
				<label for="searchKeyword"><spring:message code="word.title"/></label>
				<form:input path="searchKeyword" class="t-box02 wx200"/>
			</li>
			<li>
				<label for="userNm"><spring:message code="word.insertUser"/></label>
				<form:input path="userNm" class="t-box02 wx100"/>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="reloadGrid('list', 'form');return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
	</div>
	<div class="gridContainer">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
	<div class="tbl-bottom tbl-bottom2">
		<div class="tbl-btn">
			<c:if test="${boardSetting.writable}">
				<a href="#" class="new" onclick="goForm();return false;"><spring:message code="button.add"/></a>
			</c:if>
			<sec:authorize access="hasRole('01')">
				<a href="#" class="delete" onclick="deleteData();return false;"><spring:message code="button.delete"/></a>
			</sec:authorize>
		</div>
	</div>
</form:form>
