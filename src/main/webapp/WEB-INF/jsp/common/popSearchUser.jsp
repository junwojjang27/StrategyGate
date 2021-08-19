<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
function popExpandTree(deptId, event) {
	var $a = $("#popDept_" + deptId + " > a");
	if($a.hasClass("active")) {
		$a.removeClass("active");
	} else {
		$a.addClass("active");
	}
	event.stopPropagation();
}

function popSetValue() {
	var $option = $("#userList > option:selected");
	if($option.length == 0) {
		return false;
	} else if($option.length > 1) {
		$option = $option.eq(0);
	}
	var userId = $option.data("userId");
	var userNm = $option.data("userNm");
	var deptId = $option.data("deptId");
	var deptNm = $option.data("deptNm");
	var jikgubId = $option.data("jikgubId");
	var posId = $option.data("posId");
	var jobId = $option.data("jobId");
	
	<c:choose>
		<c:when test="${empty param.gridCall or gridCall eq 'N'}">
			$("#${param.targetUserId}").val(userId);
			$("#${param.targetUserNm}").val(userNm).focus();
			<c:if test="${not empty param.targetDeptId}">
				$("#${param.targetDeptId}").val(deptId);
			</c:if>
			<c:if test="${not empty param.targetDeptNm}">
				$("#${param.targetDeptNm}").val(deptNm);
			</c:if>
			<c:if test="${not empty param.targetJikgubId}">
				$("#${param.targetJikgubId}").val(jikgubId);
			</c:if>
			<c:if test="${not empty param.targetPosId}">
				$("#${param.targetPosId}").val(posId);
			</c:if>
			<c:if test="${not empty param.targetJobId}">
				$("#${param.targetJobId}").val(jobId);
			</c:if>
		</c:when>
		<c:otherwise>
			var $grid = $("#${param.targetGridId}");
			var rowId = "${param.targetRowId}";
			var rowData = $grid.jqGrid("getRowData", rowId);
			rowData["${param.targetUserId}"] = userId;
			rowData["${param.targetUserNm}"] = userNm;
			<c:if test="${not empty param.targetDeptId}">
				rowData["${param.targetDeptId}"] = deptId;
			</c:if>
			<c:if test="${not empty param.targetDeptNm}">
				rowData["${param.targetDeptNm}"] = deptNm;
			</c:if>
			<c:if test="${not empty param.targetJikgubId}">
				rowData["${param.targetJikgubId}"] = jikgubId;
			</c:if>
			<c:if test="${not empty param.targetPosId}">
				rowData["${param.targetPosId}"] = posId;
			</c:if>
			<c:if test="${not empty param.targetJobId}">
				rowData["${param.targetJobId}"] = jobId;
			</c:if>
			$grid.jqGrid("setRowData", rowId, rowData);
			<c:if test="${not empty param.callback}">
				window["${param.callback}"].apply(window, []);
			</c:if>
		</c:otherwise>
	</c:choose>
	$.fancybox.close();
}

function popSearchUserByDeptId(deptId) {
	$("#popDeptList li.active").removeClass("active");
	$("#popDeptList .active2").removeClass("active2");
	$("#popDept_" + deptId).addClass("active").children("a").addClass("active2");

	var f = document.popForm;
	f.deptId.value = deptId;
	f.userNm.value = "";
	sendAjax({
		"url" : "${context_path}/common/userList_json.do",
		"data" : getFormData("popForm"),
		"doneCallbackFunc" : "showUserList",
		"doneCallbackArgs" : "deptId"
	});
	event.stopPropagation();
}

function popSearchUserByUserNm() {
	$("#popDeptList li.active").removeClass("active");
	$("#popDeptList .active2").removeClass("active2");
	
	var f = document.popForm;
	f.deptId.value = "";
	sendAjax({
		"url" : "${context_path}/common/userList_json.do",
		"data" : getFormData("popForm"),
		"doneCallbackFunc" : "showUserList",
		"doneCallbackArgs" : "userNm"
	});
	event.stopPropagation();
}

function showUserList(searchType, json) {
	var $userList = $("#userList").empty();
	var $option;
	$(json.list).each(function(i, e) {
		$option = $("<option value='" + e.userId + "'>" + (searchType == "userNm" ? "[" + e.deptNm + "] " : " ") + e.jikgubNm + " " + e.userNm + "</option>")
			.appendTo($userList);
		$option.data({
			"userId" : e.userId,		"userNm" : e.userNm,	"deptId" : e.deptId,	"deptNm" : e.deptNm,
			"jikgubId" : e.jikgubId,	"posId" : e.posId,		"jobId" : e.jobId
		});
	});
}

// 전체닫음
function closeAll() {
	$("#popDeptList").find(".active").removeClass("active");
}

// 전체펼침
function openAll() {
	$("#popDeptList").find("a").addClass("active");
}

$(function() {
	openAll();
	
	$("#userList").on("click", function(e) {
		popSetValue();
	});
})
</script>
<div class="popup wx750">
	<p class="title"><spring:message code="word.selectInsertUser"/></p>
	<form:form commandName="searchVO" id="popForm" name="popForm" method="post" onsubmit="popSearchUserByUserNm();return false;">
		<form:hidden path="findYear"/>
		<form:hidden path="deptId"/>
		<div class="sch-bx">
			<ul class="sch-bx1">
				<li>
					<label><spring:message code="word.name"/></label>
					<form:input path="userNm" class="t-box02 wx150"/>
				</li>
				<li>
					<label><spring:message code="word.pos"/></label>
					<form:select path="findPosId" class="select">
						<option value=""><spring:message code="bsc.common.msg.all"/></option>
						<form:options items="${codeUtil:getCodeList('344')}" itemLabel="codeNm" itemValue="codeId" />
					</form:select>
				</li>
				<li>
					<label><spring:message code="word.jikgub"/></label>
					<form:select path="findJikgubId" class="select">
						<option value=""><spring:message code="bsc.common.msg.all"/></option>
						<form:options items="${codeUtil:getCodeList('345')}" itemLabel="codeNm" itemValue="codeId" />
					</form:select>
				</li>
			</ul>
			<a href="#" class="btn-sch" onclick="popSearchUserByUserNm();return false;"><spring:message code="button.search"/></a>
		</div>
	</form:form>
	<div class="table-bx2 mt10 mb10">
		<div id="popDeptList" class="mapp fl wx350 mr0">
			<p><spring:message code="word.dept"/></p>
			<c:set var="deptLevel" value="0"/>
			<c:forEach items="${deptList}" var="item" varStatus="status">
				<c:choose>
					<c:when test="${item.deptLevel > deptLevel}">
						<ul <c:if test="${item.deptLevel == 1}"> id='popDeptRoot' class='hx400'</c:if>>
					</c:when>
					<c:when test="${item.deptLevel < deptLevel}">
						<c:forEach begin="${item.deptLevel}" end="${deptLevel}" step="1" varStatus="status">
								</li>
							<c:if test="${not status.last}">
								</ul>
							</c:if>
						</c:forEach>
					</c:when>
					<c:otherwise>
						</li>
					</c:otherwise>
				</c:choose>
				
				<c:set var="deptLevel" value="${item.deptLevel}"/>
				<c:set var="hasChildClass" value=""/>
				<c:if test="${item.isLeaf eq 'N'}">
					<c:set var="hasChildClass" value="hasChild"/>
				</c:if>
				<li id="popDept_${item.deptId}" class="${hasChildClass}">
					<a href="#" onclick="popExpandTree('${item.deptId}', event);return false;"><span onclick="popSearchUserByDeptId('${item.deptId}', event);return false;">${item.deptNm}</span></a>
			</c:forEach>
			<c:forEach begin="1" end="${deptLevel}" step="1" varStatus="status">
					</li>
				</ul>
			</c:forEach>
			<a href="#" class="aopen" onclick="openAll();return false;"><span><spring:message code="button.allOpen"/></span></a>
			<a href="#" class="aclose" onclick="closeAll();return false;"><span><spring:message code="button.allClose"/></span></a>
		</div>
		<div class="mapp wx350 ml10">
			<p><spring:message code="word.user"/></p>
			<div class="hx400">
				<select id="userList" class="wp100 multiselect" multiple="multiple"></select>
			</div>
		</div>
	</div>
</div>
