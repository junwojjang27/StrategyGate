<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<link href="${css_path}/metricMap.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript">
$(function() {
	reloadData();

	$(window).on("resize", function() {
		resizeVLine();
	});
});

function reloadData() {
	sendAjax({
		"url" : "${context_path}/bsc/mon/org/metricMap/metricMapList.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "makeMetricMap"
	});
}

function makeMetricMap(data) {
	$(".metricMap .metricMapLvl1").remove();
	$(data.perspectiveList).each(function(i, e) {
		if(i == 0) {
			$(".metricMap").append("<ul class='metricMapLvl1'>");
		}
		$(".metricMapLvl1").append("<li class='liNode" + " pId_" + e.perspectiveId + "'><div class='divNode'><a href='#' onclick='return false;' style='cursor:auto;' title='" +  escapeHTML(removeNull(e.perspectiveNm)) + "'>" + escapeHTML(removeNull(e.perspectiveNm)) + "</a></div></li>");
	});

	var $pNode;
	$(data.strategyList).each(function(i, e) {
		$pNode = $(".pId_" + e.perspectiveId).children(".metricMapLvl2");
		if($pNode.length == 0) {
			$pNode = $("<ul class='metricMapLvl2'>").appendTo(".pId_" + e.perspectiveId);
		}
		$pNode.append("<li class='liNode" + " sId_" + e.strategyId + "'><div class='divNode'><a href='#' onclick='popDetails(\""+e.strategyId+"\");return false;'>" + escapeHTML(removeNull(e.strategyNm)) + "</a></div></li>");
	});

	$pNode;
	$(data.metricList).each(function(i, e) {
		$pNode = $(".sId_" + e.strategyId).children(".metricMapLvl3");
		if($pNode.length == 0) {
			$pNode = $("<ul class='metricMapLvl3'>").appendTo(".sId_" + e.strategyId);
		}
		$pNode.append("<li class='liNode" + " mId_" + e.metricId + "'><div class='divNode'><a class='signal' style='background-color:" + e.color + "' onclick='return false;'>&nbsp;</a><a href='#' onclick='return false;' title='" +  escapeHTML(removeNull(e.metricNm)) + "'>" + escapeHTML(removeNull(e.metricNm)) + "</a></div></li>");
	});

	var $dummyNode = $("<li style='top:0;position:absolute;visibility:hidden'><div><a>dummy</a></div></li>").appendTo(".metricMapLvl1");
	var nodeHeight = $dummyNode.outerHeight(true);
	$dummyNode.remove();

	$(".metricMapLvl1, .metricMapLvl2").children(".liNode").each(function(i, e) {
		if($(e).children("ul[class^=metricMapLvl]").length > 0) {
			$(e).children(".divNode").addClass("expandOn").prepend("<a class='btnExpand' href='#' onclick='return false;'>-</a>");
		}
	});

	$(".metricMapLvl2, .metricMapLvl3").each(function(i, e) {
		var $lis = $(this).children("li");

		// 가로선
		$lis.each(function(i2, e2) {
			$("<span>").addClass("hLine").css({"top" : nodeHeight/2}).prependTo($(e2));
		});

		// 세로선
		var h = 1;
		if($lis.length > 1) {
			$lis.each(function(i2, e2) {
				if(i2+1 < $lis.length) {
					h += $(e2).outerHeight(true);
				}
			});
			$("<span>").addClass("vLine").css({"top" : nodeHeight/2, "height": h}).insertBefore($(this));
		}
	});

	$(".metricMap .btnExpand").on("click", function() {
		if($(this).closest(".divNode").hasClass("expandOn")) {
			$(this).text("+");
			$(this).closest(".divNode").removeClass("expandOn").addClass("expandOff");
		} else {
			$(this).text("-");
			$(this).closest(".divNode").removeClass("expandOff").addClass("expandOn");
		}
		resizeVLine();
	});

	resizeVLine();
}

function popDetails(val) {
	//var tmpScDeptNm = getScDeptNm(val);
	val = val.split("|")[0];
	var dualScreenLeft = window.screenLeft != undefined ? window.screenLeft : window.screenX;
    var dualScreenTop = window.screenTop != undefined ? window.screenTop : window.screenY;
	var width = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : screen.width;
    var height = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : screen.height;
	var left = ((width / 2) - 900) + dualScreenLeft;
    var top = ((height / 2) - 400) + dualScreenTop;
    var url = "${context_path}/bsc/mon/divOutput/scDeptDetail/popScDeptDetailList.do?findYear="+$("#findYear").val()+"&findMon="+$("#findMon").val()+"&findStrategyId="+val+"&findScDeptId="+$("#findScDeptId").val()+"&layout=popup";
    var title = "popScDeptDetailList"
	//$("#findScDeptId").val(val);
    var newWindow = window.open(url, title, 'scrollbars=no, width=' + 1300 + ', height=' + 600 + ', top=' + top + ', left=' + left);
    if (window.focus) {
        newWindow.focus();
    }
}

function resizeVLine() {
	$(".metricMapLvl2, .metricMapLvl3").each(function(i, e) {
		var $lis = $(this).children("li");

		// 세로선
		var h = 1;
		if($lis.length > 1) {
			$lis.each(function(i2, e2) {
				if(i2+1 < $lis.length) {
					h += $(e2).outerHeight(true);
				}
			});
			$(this).prev(".vLine").css({"height": h});
		}
	});
}
</script>
<form:form commandName="searchVO" id="form" name="form" method="post">
	<form:hidden path="findScDeptId"/>
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findYear"><spring:message code="word.year"/></label>
				<form:select path="findYear" class="select wx100" items="${codeUtil:getCodeList('017')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
				<form:select path="findMon" class="select wx80" items="${codeUtil:getCodeList('024')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<div class="sch-oga" id="findScDeptList"></div>
			</li>
			<li class="ml20">
				<form:radiobuttons path="findAnalCycle" items="${codeUtil:getCodeList('138')}" itemLabel="codeNm" itemValue="codeId" />
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="reloadData();return false;"><spring:message code="button.search"/></a>
	</div>
	<%--
	<div class="btn-dw">
	</div>
	--%>
	<div class="table-bx2">
		<div class="metricMap">
			<ul class="metricMapHeader">
				<li><label><spring:message code="word.perspective"/></label></li><li><label><spring:message code="word.strategy"/></label></li><li><label><spring:message code="word.metric"/></label></li>
			</ul>
		</div>
	</div>
</form:form>