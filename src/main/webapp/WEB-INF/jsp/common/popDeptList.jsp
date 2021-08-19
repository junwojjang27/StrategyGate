<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
var popDeptId = "";
var popDeptNm = "";
function setValue(deptId, deptNm, event) {
	$(".oga-bx .active2").removeClass("active2");
	popDeptId = deptId;
	popDeptNm = deptNm;
	$("#popDept_" + popDeptId + " > a").addClass("active2");
	event.stopPropagation();
}

function expandTree(deptId, event) {
	var $a = $("#popDept_" + deptId + " > a");
	if($a.hasClass("active")) {
		$a.removeClass("active");
	} else {
		$a.addClass("active");
	}
	event.stopPropagation();
}

// 전체닫음
function openPopDeptAll(flag) {
	if(flag) {
		$("#popDeptList").find("a").addClass("active");
	} else {
		$("#popDeptList").find(".active").removeClass("active");
	}
}

function popSave() {
	if(isEmpty(popDeptId)) {
		$.showMsgBox("<c:set var="messageArg"><spring:message code="word.deptNm"/></c:set><spring:message code="errors.select" arguments="${messageArg}"/>", null, null);
		return false;
	}
	
	<c:choose>
		<c:when test="${empty param.gridCallYn or param.gridCallYn eq 'N'}">
			$("#${param.targetDeptId}").val(popDeptId);
			
			<c:if test="${not empty param.targetDeptNm}">
				if($("#${param.targetDeptNm}").length > 0) {
					$("#${param.targetDeptNm}").val(popDeptNm).focus();
				}
			</c:if>
			
			<c:if test="${not empty param.targetDeptFullNm}">
				if($("#${param.targetDeptFullNm}").length > 0) {
					var deptFullNm = [];
					$($("#popDept_" + popDeptId).parents("li[id^=popDept_]").get().reverse()).each(function(i, e) {
						deptFullNm.push($.trim($(e).children("a").children("span").text()));
					});
					deptFullNm.push($.trim($("#popDept_" + popDeptId).children("a").children("span").text()));
					$("#${param.targetDeptFullNm}").val(deptFullNm.join(">"));
				}
			</c:if>
			
			<c:if test="${not empty param.callbackFunc}">
				${param.callbackFunc}();
			</c:if>
		</c:when>
		<c:otherwise>
			var $grid = $("#${param.targetGridId}");
			var rowId = "${param.targetRowId}";
			var rowData = $grid.jqGrid("getRowData", rowId);
			rowData["${param.targetDeptId}"] = popDeptId;
			<c:if test="${not empty param.targetDeptNm}">
				rowData["${param.targetDeptNm}"] = popDeptNm;
			</c:if>
			<c:if test="${not empty param.targetDeptFullNm}">
				if($("#${param.targetDeptFullNm}").length > 0) {
					var deptFullNm = [];
					$($("#popDept_" + popDeptId).parents("li[id^=popDept_]").get().reverse()).each(function(i, e) {
						deptFullNm.push($.trim($(e).children("a").children("span").text()));
					});
					deptFullNm.push($.trim($("#popDept_" + popDeptId).children("a").children("span").text()));
					rowData["${param.targetDeptFullNm}"] = deptFullNm.join(">");
				}
			</c:if>
			
			$grid.jqGrid("setRowData", rowId, rowData,'edited');
			
			<c:if test="${not empty param.callbackFunc}">
			window["${param.callback}"].apply(window, []);
			</c:if>
			
		</c:otherwise>
	</c:choose>
	
	$.fancybox.close();
}

$(function() {
	openPopDeptAll(true);
	$("#popDeptList li span").on("dblclick", function(e) {
		popSave();
	});
});
</script>
<div class="popup wx400">
	<p class="title"><spring:message code="word.deptNm"/></p>
	<div class="btn-dw fr mt10">
		<a href="#" class="aopen" onclick="openPopDeptAll(true);return false;"><span><spring:message code="button.allOpen"/></span></a>
		<a href="#" class="aclose" onclick="openPopDeptAll(false);return false;"><span><spring:message code="button.allClose"/></span></a>
	</div>
	<div id="popDeptList" class="oga-bx ogam wx360 cboth">
		<c:set var="deptLevel" value="0"/>
		<c:forEach items="${deptList}" var="item" varStatus="status">
			<c:choose>
				<c:when test="${item.deptLevel > deptLevel}">
					<ul <c:if test="${item.deptLevel == 1}"> id='popDeptRoot'</c:if>>
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
				<a href="#" onclick="expandTree('${item.deptId}', event);return false;"><span onclick="setValue('${item.deptId}', '${item.deptNm}', event);return false;">${item.deptNm}</span></a>
		</c:forEach>
		<c:forEach begin="1" end="${deptLevel}" step="1" varStatus="status">
				</li>
			</ul>
		</c:forEach>
	</div>
	<div class="tbl-bottom">
		<div class="tbl-btn">
			<a href="#" class="save" onclick="popSave();return false;"><spring:message code="button.select"/></a>
		</div>
	</div>
</div>
