<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<validator:javascript formName="metricCommentVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
$(function() {
	loadMCList();
	setMaxLengthById("mcContents");
	showBytes("mcContents", "mcContentsBytes");
	
	$("#mcTable").on("mouseenter", "tr", function(e) {
		$(this).addClass("hover");
	}).on("mouseleave", "tr", function(e) {
		$(this).removeClass("hover");
	});
});

function loadMCList() {
	hideMCEditFormAll();
	document.mcForm.contents.value = "";
	$("#mcContentsBytes").text(0);
	sendAjax({
		"url" : "${context_path}/bsc/mon/common/metricComment/metricCommentList_json.do",
		"data" : getFormData("mcForm"),
		"doneCallbackFunc" : "showMCList"
	});
}

function showMCList(data) {
	var listCnt = data.list.length;
	$("#mcCnt").text("(" + listCnt + ")");
	
	$("#mcTable .trMC").remove();
	
	if(listCnt == 0) {
		$("#trNoMC").show();
	} else {
		$("#trNoMC").hide();
		var cmt, $tr;
		for(var i in data.list) {
			cmt = data.list[i];
			$tr = $("#trMCTemplate").clone().removeAttr("id").removeClass("hide");
			$tr.addClass("trMC").attr("id", "mc" + cmt.seq)
				.data({
					"seq":cmt.seq,
					"lvl":cmt.groupLevel
				});
			$tr.find(".cmtUserNm").text(cmt.userNm);
			if(cmt.insertUserId == "${sessionScope.userId}") {
				$tr.find(".cmtUserNm").addClass("fs-bold");
			}
			
			if(cmt.groupLevel > 0) {
				$tr.find(".cmtContents").addClass("mcReply").closest("td").css("padding-left", ((20 * (cmt.groupLevel-1)) + 10) + "px")
			}
			
			$tr.find(".cmtContents").html(nl2br(escapeHTML(removeNull(cmt.contents))));
			$tr.find(".cmtDeptNm").text(isNotEmpty(cmt.deptNm) ? "(" + cmt.deptNm + ")" : "");
			$tr.find(".cmtCreateDt").text(cmt.createDt);
			
			<sec:authorize access="hasRole('01')">
				$tr.find("a.edit").on("click", {"seq" : cmt.seq}, function(e) {
					showMCEditForm(e.data.seq);
					e.preventDefault();
				});
				$tr.find("a.delete").on("click", {"seq" : cmt.seq}, function(e) {
					deleteMC(e.data.seq);
					e.preventDefault();
				});
				
				$tr.find("a.save").on("click", {"seq" : cmt.seq}, function(e) {
					saveMC(e.data.seq);
					e.preventDefault();
				});
			</sec:authorize>
			<sec:authorize access="not hasRole('01')">
				if(cmt.insertUserId == "${sessionScope.userId}") {
					$tr.find("a.edit").on("click", {"seq" : cmt.seq}, function(e) {
						showMCEditForm(e.data.seq);
						e.preventDefault();
					});
					$tr.find("a.delete").on("click", {"seq" : cmt.seq}, function(e) {
						deleteMC(e.data.seq);
						e.preventDefault();
					});
					
					$tr.find("a.save").on("click", {"seq" : cmt.seq}, function(e) {
						saveMC(e.data.seq);
						e.preventDefault();
					});
				} else {
					$tr.find("a.edit").remove();
					$tr.find("a.delete").remove();
				}
			</sec:authorize>
			
			if(cmt.replyCnt > 0) {
				$tr.find("a.delete").remove();
			}
			
			$tr.find("a.add").on("click", {"seq" : cmt.seq}, function(e) {
				showMCReplyForm(e.data.seq);
				e.preventDefault();
			});
			
			$tr.find("a.close").on("click", {"seq" : cmt.seq}, function(e) {
				hideMCEditForm(e.data.seq);
				e.preventDefault();
			});
			
			$("#mcTable tbody").append($tr);
		}
	}
}

// 저장
function saveMC(seq) {
	// 신규
	if(isEmpty(seq) && document.mcForm.upSeq.value == -1) {
		if(!validateMetricCommentVO(document.mcForm)) return;
	} else {	// 수정 or 대댓글
		if(isEmpty(document.mcForm.editContents.value)) {
			$.showMsgBox("<spring:message code="errors.noDataToUpdate"/>", null, document.mcForm.editContents);
			return false;
		}
	}
	
	document.mcForm.seq.value = seq;

	sendAjax({
		"url" : "${context_path}/bsc/mon/common/metricComment/saveMetricComment.do",
		"data" : getFormData("mcForm"),
		"doneCallbackFunc" : "checkMCResult"
	});
}

// 답글 양식 표시
function showMCReplyForm(seq) {
	hideMCEditFormAll();
	
	document.mcForm.upSeq.value = seq;
	var $upTr = $("#mc" + seq);
	var $tr = $("#trMCReplyTemplate").clone().removeAttr("id").removeClass("hide").insertAfter($upTr);
	$tr.attr("id", "trMCReply");
	
	var $txt = $tr.find("textarea").attr("name", "editContents").focus();
	$txt.closest("td").css("padding-left", ((20 * $upTr.data("lvl"))+10) + "px");
	setMaxLengthByElement($txt);
}

// 수정 양식 표시
function showMCEditForm(seq) {
	hideMCEditFormAll();

	var $tr = $("#mc" + seq);
	var $txt = $("<textarea name='editContents' maxlength='1000'>");
	$txt.addClass("wp100").addClass("hx80").addClass("hx80").val(unescapeHTML($tr.find(".cmtContents").text())).insertAfter($tr.find(".cmtContents")).focus();
	$txt.closest("td").css("padding-left", ((20 * ($tr.data("lvl")-1))+10) + "px");
	$txt.wrap("<div class='mcReply'></div>");
	setMaxLengthByElement($txt);
	$tr.find(".cmtContents").hide();
	$tr.find(".mcBtns1").hide();
	$tr.find(".mcBtns2").show();
}

// 수정 양식 삭제
function hideMCEditForm(seq) {
	var $tr = $("#mc" + seq);
	$tr.find(".cmtContents").show();
	$tr.find("textarea, div.mcReply").remove();
	$tr.find(".mcBtns1").show();
	$tr.find(".mcBtns2").hide();
}

// 수정, 답글 양식 삭제
function hideMCEditFormAll() {
	document.mcForm.upSeq.value = -1;
	$("#trMCReply").remove();
	$(".trMC textarea").each(function(i, e) {
		hideMCEditForm($(e).closest(".trMC").data("seq"));
	});
}

// 삭제
function deleteMC(seq) {
	document.mcForm.seq.value = seq;
	$.showConfirmBox("<spring:message code="common.delete.msg"/>", function() {
		sendAjax({
			"url" : "${context_path}/bsc/mon/common/metricComment/deleteMetricComment.do",
			"data" : getFormData("mcForm"),
			"doneCallbackFunc" : "checkMCResult"
		});
	});
}

function checkMCResult(data) {
	$.showMsgBox(data.msg);
	if(data.result == AJAX_SUCCESS) {
		loadMCList();
	}
}
</script>
<form:form commandName="searchVO" id="mcForm" name="mcForm" method="post">
	<form:hidden path="findScDeptId" value="${searchVO.findScDeptId}"/>
	<form:hidden path="metricId" value="${searchVO.metricId}"/>
	<form:hidden path="year" value="${searchVO.findYear}"/>
	<form:hidden path="upSeq"/>
	<form:hidden path="seq"/>

	<div class="ptitle"><spring:message code="word.opinion"/> <label id="mcCnt">(0)</label></div>
	<div class="tbl-type01">
		<table id="mcTable">
			<colgroup>
				<col width="80%"/>
				<col width="10%"/>
				<col width="10%"/>
			</colgroup>
			<thead>
				<tr>
					<th scope="col" class="pl0 txt-c"><spring:message code="word.content"/></th>
					<th scope="col" class="pl0 txt-c"><spring:message code="word.writeUser"/></th>
					<th scope="col" class="pl0 txt-c"><spring:message code="word.modify"/> / <spring:message code="word.delete"/></th>
				</tr>
			</thead>
			<tbody>
				<tr id="trNoMC" class="hide2">
					<td colspan="3" class="txt-c"><spring:message code="errors.noResult"/></td>
				</tr>
				<tr id="trMCTemplate" class="hide">
					<td>
						<p class="cmtContents txt-l text-type01"></p>
					</td>
					<td class="txt-c">
						<div class="mb5">
							<label class="cmtUserNm"></label> <label class="cmtDeptNm"></label>
						</div>
						<label class="cmtCreateDt"></label><br/>
					</td>
					<td class="pd5">
						<div class="mcBtns1 txt-c tbl-btn mt0">
							<a href="#" class="add pd5" title="<spring:message code="button.reply2"/>"><spring:message code="button.reply2"/></a>
							<a href="#" class="edit pd5" title="<spring:message code="button.modify"/>"><spring:message code="button.modify"/></a>
							<a href="#" class="delete pd5" title="<spring:message code="button.delete"/>"><spring:message code="button.delete"/></a>
						</div>
						<div class="mcBtns2 txt-c tbl-btn mt0 hide2">
							<a href="#" class="save pd5" title="<spring:message code="button.save"/>"><spring:message code="button.save"/></a>
							<a href="#" class="close pd5" title="<spring:message code="button.cancel"/>"><spring:message code="button.cancel"/></a>
						</div>
					</td>
				</tr>
				<tr id="trMCReplyTemplate" class="hide">
					<td>
						<div class="mcReply">
							<textarea maxlength="1000" class="wp100"></textarea>
						</div>
					</td>
					<td class="txt-c">
						<div class="mb5">
							<label class="cmtUserNm">${sessionScope.loginVO.userNm}</label>
						</div>
					</td>
					<td class="pd5">
						<div class="mcBtns2 txt-c tbl-btn mt0">
							<a href="#" class="save pd5" onclick="saveMC('');return false;" title="<spring:message code="button.save"/>"><spring:message code="button.save"/></a>
							<a href="#" class="close pd5" onclick="hideMCEditFormAll();return false;" title="<spring:message code="button.cancel"/>"><spring:message code="button.cancel"/></a>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="tbl-type02 mt10">
		<table>
			<colgroup>
				<col width="140"/>
				<col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th scope="row"><label for="mcContents"><spring:message code="word.opinion"/></label></th>
					<td>
						<p><form:textarea path="contents" id="mcContents" maxlength="1000" cssClass="wp100 hx60 ml0"/></p>
						<p class="byte"><label id="mcContentsBytes">0</label><label> / 1000byte</label></p>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="hideMCEditFormAll();saveMC('');return false;"><spring:message code="button.save"/></a>
		</div>
	</div>
</form:form>
