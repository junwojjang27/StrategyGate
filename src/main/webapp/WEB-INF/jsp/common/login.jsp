<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>Strategy Gate</title>
<style type="text/css">
*{padding:0;margin:0}
body,html{height:100%;}
body{background-color:#000; background-image: url("./images/common/login/login_bg.jpg"); color:#222;font-family: 'Nanum Gothic',Dotum,'dotum',Gulim,'gulim';}
#floater{float:left;height:50%;margin-bottom:-340px}
.loginLayout{clear:both;position:relative; background-image: url("./images/common/login/login_area.png");margin:0 auto;width:560px;height:634px;}
.loginLayout>div{width:100%;box-sizing:border-box;display:block}
.loginLayout .logoDiv{text-align:center;padding:5.5em 0 .5em 0;}
.loginLayout .formDiv{padding:1em;text-align:center}
.loginLayout .formDiv label{width:150px;display:inline-block;font-size:1em;line-height:1em;text-align:left}
.loginLayout .formDiv input{width:21.1em; height:1.5em;padding:0.7em; display:inline-block;font-size:1em; font-weight:bold; font-family: 'Nanum Gothic',Dotum,'dotum',Gulim,'gulim'; margin-bottom:12px;border:1px solid #b7b7b7;}
.loginLayout .formDiv button{width:22.5em;height:3.2em;font-size:1em;font-weight:bold;font-family:'Nanum Gothic',Dotum,'dotum',Gulim,'gulim';color:#fff;background-image: url( "./images/common/login/login_btn_bg.png" );border-width:0px;}
.loginLayout .formDiv .chkDiv{text-align:right;margin-right:1.3em;}
.loginLayout .formDiv .chkDiv input{width:inherit;margin-right:.5em;margin-top:0.5em;}
.loginLayout .formDiv .chkDiv label{width:110px;font-size:0.8em;}
.loginLayout .lineDiv{width:27.2em; border-top:1px solid #d2d2d2;margin-bottom:1.5em;margin-left:3.9em;}
.loginLayout .noticeDiv{font-size:0.7em;margin-left:8em;font-family: 'Nanum Gothic',Dotum,'dotum',Gulim,'gulim';}
.loginLayout .noticeDiv h4{font-weight:bold;margin: 0 0 0 1em;font-size:1.2em !important;}
.loginLayout .noticeDiv ul{margin:.2em 1em 0 2.2em}
.loginLayout .noticeDiv ul li {line-height:1.4em;}
.loginLayout .noticeDiv ul li a{color:#4975ac; display:inline-block;vertical-align:middle;width:25em;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;-o-text-overflow:ellipsis}
input[type='radio'] {margin:5px 5px 5px 10px}
</style>
<script>

if(document.location.href.indexOf("main.do") > -1) {
	document.location.href = "<c:url value="/"/>";
}

function login(id, pw) {
	var f = document.f;
	f.userId.value = id;
	f.password.value = pw;
	f.submit();
}

function saveid(form) {
	var expdate = new Date();
	if (form.checkId.checked)
		expdate.setTime(expdate.getTime() + 1000 * 3600 * 24 * 30); // 30일
	else
		expdate.setTime(expdate.getTime() - 1); // 쿠키 삭제조건
	setCookie("saveid", form.userId.value, expdate);
}

function setCookie (name, value, expires) {
	document.cookie = name + "=" + escape (value) + "; path=/; expires=" + expires.toGMTString();
}

function getid(form) {
	if(getCookie("saveid")!= ""){
		form.userId.value = getCookie("saveid")
	}
	form.checkId.checked = (getCookie("saveid") != "");
}

function getCookie(cname){
	var name = cname + "=";
	var decodedCookie = document.cookie;
	var ca = decodedCookie.split(';');
	for(var i = 0; i <ca.length; i++) {
	    var c = ca[i];
		while (c.charAt(0) == ' ') {
	      	c = c.substring(1);
	    }
	    if (c.indexOf(name) == 0) {
	      return c.substring(name.length, c.length);
	    }
	}
	return "";
}

/*조회조건 cookiereset
 */
function resetParamCookie(){

	var cookie = document.cookie;
	var cookieArray = cookie.split(";");
	var cookieObjArray = [];

	for(var index in cookieArray){
		var fullCookie = cookieArray[index].trim();
		var endIndex = 0;
		var replaceCookie = "";
		if(fullCookie.indexOf("find") > -1){
			endIndex = fullCookie.indexOf("=");
			replaceCookie = fullCookie.substring(0,endIndex+1);
			document.cookie = replaceCookie+";";
		}
	}
	//document.cookie = "findYear=;";
}


//자동으로 공지사항 조회
/*
function popNotice(id, width, height) {
	var w=width, h=height;
	var left = (screen.width-w)/2;
	var top = (screen.height-h)/2;
	var newWin = window.open("", "popNotice", "width="+w+",height="+h+",left="+left+",top="+top+", scrollbars=auto, toolbar=no");
	newWin.focus();
}
*/


function popShowNotice(id, width, height) {
	//var w=700, h=500;
	var w=width, h=height;
	var left = (screen.width-w)/2;
	var top = (screen.height-h)/2;

	left += window.screenX;//듀얼모니터 사용시 팝업위치 중앙으로
	var newWin = window.open("", "popNotice_"+id, "width="+w+",height="+h+",left="+left+",top="+top+", scrollbars=auto, toolbar=no");
	newWin.focus();
}

window.onload = function() {
	getid(document.f);
	<c:if test="${not empty msg}">alert("${msg}");</c:if>

	resetParamCookie();

	/*자동팝업*/
	var atag;
	<c:forEach items="${popNoticeList}" var="popNotice">
		var atag = document.createElement("a");
		atag.href = "${context_path}/comPop/notice/popNoticeDetail.do?id=${popNotice.id}&layout=popup";
		atag.target = "popNotice_${popNotice.id}";
		atag.addEventListener("click",function(){
			popShowNotice('${popNotice.id}', '${popNotice.width}', '${popNotice.height}');
		});
		document.getElementById("autoPopup").appendChild(atag);
	</c:forEach>

	/*
	document.querySelectorAll("#autoPopup > a").forEach(function(e){
		e.click();
	});
	*/

	var popList = document.querySelectorAll("#autoPopup > a");
	var node = document.getElementById("autoPopup");
	if(popList.length > 0){

		for(var i=0 ; i<popList.length ; i++){
			popList[i].click();
		}

		for(var i=0 ; i<popList.length ; i++){
			node.removeChild(popList[i]);
		}
	}


}
</script>
</head>
<body id="loginbg">
<div id="floater"></div>
<div class="loginLayout">
	<div class="logoDiv">
		<img src="./images/common/login/img-logo.png" alt="Logo" style="padding:15px; border-radius:5px;"/>
	</div>
	<div class="formDiv">
		<c:url var="loginUrl" value="/loginProcess.do"/>
		<form:form name="f" action="${loginUrl}" method="POST">
			<div>
				<input type="text" name="userId" id="userId" placeholder="<spring:message code="word.id"/>" value=""/>
				<input type="password" name="password" id="password" value="" placeholder="<spring:message code="word.password"/>"/>
			</div>
			<div>
				<c:if test="${param.fail != null}">
					<p><spring:message code="fail.common.login"/></p>
				</c:if>
				<c:if test="${param.noService != null}">
					<p><spring:message code="fail.common.login.useYn" htmlEscape="false"/></p>
				</c:if>
				<c:if test="${param.secure != null}">
					<p><spring:message code="errors.secureException"/></p>
				</c:if>
				<c:if test="${param.error != null}">
					<p><spring:message code="errors.errorOccurred"/></p>
				</c:if>
				<c:if test="${param.sessionExpired != null}">
					<p><spring:message code="errors.sessionExipred"/></p>
				</c:if>
				<c:if test="${param.noConnection != null}">
					<p><spring:message code="errors.undefinedConnection"/></p>
				</c:if>
			</div>
			<div class="chkDiv">
				<input type="checkbox" name="checkId" class="check2" onclick="saveid(document.f);" id="checkId" style="vertical-align:middle;"/><label for="checkId">Save ID</label>
			</div>
			<div>
				<button>Login</button>
			</div>
		</form:form>
	</div>
	<div class="lineDiv"></div>
	<div class="noticeDiv">
		<h4>Notice</h4>
		<ul>
			<c:forEach items="${noticeList}" var="notice">
				<li>
					<a href="${context_path}/comPop/notice/popNoticeDetail.do?id=${notice.id}&layout=popup" target="popNotice_${notice.id}" onclick="popShowNotice('${notice.id}', '${notice.width}', '${notice.height}');"><c:out value="${notice.subject}" escapeXml="true"/></a> <label>${notice.createDtStr}</label>
				</li>
			</c:forEach>
		</ul>
	</div>
	<div id="autoPopup" style="display:none;">
	</div>
</div>
<div style="width:700px; margin:0 auto; color:white">
	<input type="radio" name="login" id="radio11" onclick="login('super', 'super_admin', 'isparkenc_admin1!')"/><label for="radio11">super_admin</label>
	<br />
	<input type="radio" name="login" id="radio1" onclick="login('gwhyun', 'isparkenc_admin1!')"/><label for="radio1">ispark_admin</label>
</div>
</body>
</html>
