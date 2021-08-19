<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	$("#popBoardList").jqGrid({
		url			:	"${context_path}/comPop/system/system/board/boardList_json.do",
		postData	:	getFormData("popBoardform"),
		width		:	"800",
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
									return padding + "<a href='#' onclick='popShowDetail(\"" + removeNull(rowObject.seq) + "\");clickGridRow(this);return false;'>" + value + "</a>";
								}
							},
							{name:"userNm",		index:"userNm",		width:50,		align:"center",	title:true,	label:"<spring:message code="word.insertUser"/>",	sortable:false},
							{name:"hit",		index:"hit",		width:30,		align:"center",	title:true,	label:"<spring:message code="word.hit"/>",	sortable:false},
							{name:"createDt",	index:"createDt",	width:50,		align:"center",	title:true,	label:"<spring:message code="word.insertDT"/>",	sortable:false}
						],
		rowNum		: ${jqgrid_rownum},
		pager		: "popPager",
		onPaging	: function (pgButton) {
					setGridPaging($(this).attr("id"), pgButton, "popSearchList");
		},
		loadonce	: false
		<sec:authorize access="hasRole('01')">
			, multiselect	: true
			, loadComplete : function() {
				hideGridCheckbox("popBoardList", "replyCnt", 0, false);
			}
		</sec:authorize>
	});
	
	$("#popSearchKeyword, #popUserNm").keyup(function(e) {
		if(e.keyCode == 13) popSearchList();
	});
	
	/*popup인 경우 gridContainerdp width를 정의하고 gridResize() 호출 해야 함.*/
	popGridResize("popBoardList");
});

// 목록 조회
function popSearchList(pageNum) {
	if(isEmpty(pageNum)) {
		pageNum = parseInt(document.popBoardform.page.value, 10);
	}
 	reloadGrid("popBoardList", "popBoardform", pageNum);
}

// 상세 조회
function popShowDetail(seq) {
	var f = document.popBoardform;
	f.seq.value = seq;
	
	openFancybox({
		"url" : "${context_path}/comPop/system/system/board/boardDetail.do",
		"data" : getFormData("popBoardform")
	});
	
	//loadPage("${context_path}/comPop/system/system/board/boardDetail.do", "popBoardform");
}

<c:if test="${boardSetting.writable}">
// 등록 폼으로 이동
function popGoForm() {
	var f = document.popBoardform;
	f.seq.value = -1;
	openFancybox({
		"url" : "${context_path}/comPop/system/system/board/boardForm.do",
		"data" : getFormData("popBoardform")
	});
	//loadPage("${context_path}/comPop/system/system/board/boardForm.do", "popBoardform");
}
</c:if>

<sec:authorize access="hasRole('01')">
// 삭제
function popDeleteData() {
	if(deleteDataToForm("popBoardList", "seq", "popBoardform")) {
		$.showConfirmBox("<spring:message code="common.delete.msg"/>", function() {
			sendAjax({
				"url" : "${context_path}/comPop/system/system/board/deleteBoardAll.do",
				"data" : getFormData("popBoardform"),
				"doneCallbackFunc" : "reloadGrid",
				"doneCallbackArgs" : ["popBoardList", "popBoardform", 1]
			});
		});
	}
}
</sec:authorize>
</script>

<div class="popup wx800">
	<p class="title">QnA</p>
	<form:form commandName="searchVO" id="popBoardform" name="popBoardform" method="post">
		<form:hidden path="page"/>
		<form:hidden path="rows" value="10"/>
		<form:hidden path="boardId"/>
		<form:hidden path="seq"/>
		<div class="sch-bx">
			<ul>
				<li>
					<label for="searchKeyword"><spring:message code="word.title"/></label>
					<form:input path="searchKeyword" id="popSearchKeyword" class="t-box02 wx200"/>
				</li>
				<li>
					<label for="userNm"><spring:message code="word.insertUser"/></label>
					<form:input path="userNm" id="popUserNm" class="t-box02 wx100"/>
				</li>
			</ul>
			<a href="#" class="btn-sch" onclick="reloadGrid('popBoardList', 'popBoardform');return false;"><spring:message code="button.search"/></a>
		</div>
		<div class="btn-dw">
		</div>
		<div class="gridContainer">
			<table id="popBoardList"></table>
			<div id="popPager"></div>
		</div>
		<div class="tbl-bottom tbl-bottom2">
			<div class="tbl-btn">
				<c:if test="${boardSetting.writable}">
					<a href="#" class="new" onclick="popGoForm();return false;"><spring:message code="button.add"/></a>
				</c:if>
				<sec:authorize access="hasRole('01')">
					<a href="#" class="delete" onclick="popDeleteData();return false;"><spring:message code="button.delete"/></a>
				</sec:authorize>
			</div>
		</div>
	</form:form>
</div>	
