<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
var popScDeptId = "";
var popScDeptNm = "";
function setValue(scDeptId, scDeptNm, event) {
	$(".oga-bx .active2").removeClass("active2");
	popScDeptId = scDeptId;
	popScDeptNm = scDeptNm;
	$("#popScDept_" + popScDeptId + " > a").addClass("active2");
	event.stopPropagation();
}

function expandTree(scDeptId, event) {
	var $a = $("#popScDept_" + scDeptId + " > a");
	if($a.hasClass("active")) {
		$a.removeClass("active");
	} else {
		$a.addClass("active");
	}
	event.stopPropagation();
}

// 전체닫음
function openPopScDeptAll(flag) {
	if(flag) {
		$("#popScDeptList").find("a").addClass("active");
	} else {
		$("#popScDeptList").find(".active").removeClass("active");
	}
}

function popSave() {
	if(isEmpty(popScDeptId)) {
		$.showMsgBox("<c:set var="messageArg"><spring:message code="word.scDeptNm"/></c:set><spring:message code="errors.select" arguments="${messageArg}"/>", null, null);
		return false;
	}
	
	<c:choose>
		<c:when test="${empty param.gridCallYn or param.gridCallYn eq 'N'}">
			$("#${param.targetScDeptId}").val(popScDeptId);
			
			<c:if test="${not empty param.targetScDeptNm}">
				if($("#${param.targetScDeptNm}").length > 0) {
					$("#${param.targetScDeptNm}").val(popScDeptNm).focus();
				}
			</c:if>
			
			<c:if test="${not empty param.targetScDeptFullNm}">
				if($("#${param.targetScDeptFullNm}").length > 0) {
					var scDeptFullNm = [];
					$($("#popScDept_" + popScDeptId).parents("li[id^=popScDept_]").get().reverse()).each(function(i, e) {
						scDeptFullNm.push($.trim($(e).children("a").children("span").text()));
					});
					scDeptFullNm.push($.trim($("#popScDept_" + popScDeptId).children("a").children("span").text()));
					$("#${param.targetScDeptFullNm}").val(scDeptFullNm.join(">"));
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
			rowData["${param.targetScDeptId}"] = popScDeptId;
			<c:if test="${not empty param.targetScDeptNm}">
				rowData["${param.targetScDeptNm}"] = popScDeptNm;
			</c:if>
			<c:if test="${not empty param.targetScDeptFullNm}">
				if($("#${param.targetScDeptFullNm}").length > 0) {
					var scDeptFullNm = [];
					$($("#popScDept_" + popScDeptId).parents("li[id^=popScDept_]").get().reverse()).each(function(i, e) {
						scDeptFullNm.push($.trim($(e).children("a").children("span").text()));
					});
					scDeptFullNm.push($.trim($("#popScDept_" + popScDeptId).children("a").children("span").text()));
					rowData["${param.targetScDeptFullNm}"] = scDeptFullNm.join(">");
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
	openPopScDeptAll(true);
	$("#popScDeptList li span").on("dblclick", function(e) {
		popSave();
	});
});
</script>
<div class="popup wx400">
	<p class="title"><spring:message code="word.scDeptNm"/></p>
	<div class="btn-dw fr mt10">
		<a href="#" class="aopen" onclick="openPopScDeptAll(true);return false;"><span><spring:message code="button.allOpen"/></span></a>
		<a href="#" class="aclose" onclick="openPopScDeptAll(false);return false;"><span><spring:message code="button.allClose"/></span></a>
	</div>
	<div id="popScDeptList" class="oga-bx ogam wx360 cboth">
		<c:set var="scDeptLevel" value="0"/>
		<c:forEach items="${scDeptList}" var="item" varStatus="status">
			<c:choose>
				<c:when test="${item.levelId > scDeptLevel}">
					<ul <c:if test="${item.levelId == 1}"> id='popScDeptRoot'</c:if>>
				</c:when>
				<c:when test="${item.levelId < scDeptLevel}">
					<c:forEach begin="${item.levelId}" end="${scDeptLevel}" step="1" varStatus="status">
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
			
			<c:set var="scDeptLevel" value="${item.levelId}"/>
			<c:set var="hasChildClass" value=""/>
			<c:if test="${item.isLeaf eq 'N'}">
				<c:set var="hasChildClass" value="hasChild"/>
			</c:if>
			<li id="popScDept_${item.scDeptId}" class="${hasChildClass}">
				<a href="#" onclick="expandTree('${item.scDeptId}', event);return false;"><span onclick="setValue('${item.scDeptId}', '${item.scDeptNm}', event);return false;">${item.scDeptNm}</span></a>
		</c:forEach>
		<c:forEach begin="1" end="${scDeptLevel}" step="1" varStatus="status">
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
