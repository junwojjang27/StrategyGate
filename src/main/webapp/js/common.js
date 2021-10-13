// 검색조건용 전역 변수
var gFindValues = {
	"findYear" : "",
	"findMon" : "",
	"findAnalCycle" : "",
	"findScDeptId" : "",
	"findDeptId" : "",
	"findUseYn" : "",
	"findEvalGrpId" : "",
	"findEvalDegreeId" : ""
};

// 모니터링 성과조직 관련
var gMonitoringRootScDeptId = null;
var gIsMonitoringMenu = null;
var gIsMonitoringScDeptTree = false;
var gMonitoringScDeptIdArr = [];

// 성과조직 정보 배열
var gScDeptIdArr = [];
var gScDeptNmArr = [];
var gScDeptFullNmArr = [];

// 조직 정보 배열
var gDeptIdArr = [];
var gDeptNmArr = [];
var gDeptFullNmArr = [];

// 조직도, 성과조직도 검색조건 ID
var FIND_DEPT_ID = "findDeptId";
var FIND_DEPT_LIST_ID = "findDeptList";
var FIND_SC_DEPT_ID = "findScDeptId";
var FIND_SC_DEPT_LIST_ID = "findScDeptList";
var POP_FIND_SC_DEPT_LIST_ID = "popFindScDeptList";

// 소수점 자릿수
var DECIMAL_SCALE = 2;
var REMOVE_POINT_ZEROS = false;

// 자동완성 실행을 위한 최소 바이트수
var AUTO_COMPLETE_ACT_MIN_BYTE = 1;

// 즐겨찾기 목록
var bookmarks = [];

var href, pgmId, url;
var currPgmId;

// AJAX 응답 관련 코드
var AJAX_SUCCESS = "SUCCESS";
var AJAX_FAIL = "FAIL";

// grid 너비 계산용
var gNewGridWidth = 0;

// 도움말
var guideCommentMap = {};
var helpImagesMap = {};
var existHelpImages = [];
var isGuideCommentVisible = false

// replaceAll
String.prototype.replaceAll = function(target, replacement) {
	return this.split(target).join(replacement);
};

// lpad
String.prototype.lpad = function(len, fillChar) {
	var chars = "";
	for(var i=0; i<len; i++) {
		chars += fillChar;
	}
	return (chars + this).slice(-len);
}

// undefined 여부
function isUndefined(val) {
	return typeof val == "undefined";
}

// null 여부
function isNull(val) {
	return val == null;
}

// undefined 또는 null 여부
function isEmpty(val) {
	return (typeof val == "undefined" || val == null || $.trim(val) == "");
}

// 값이 존재하는지 여부
function isNotEmpty(val) {
	return (typeof val != "undefined" && val != null && $.trim(val) != "");
}

function removeNull(val) {
	return isEmpty(val) ? "" : val;
}

function removeEmpty(val) {
	return isEmpty(val) ? "" : val;
}

function nvl(val, altVal) {
	return isEmpty(val) ? altVal : val;
}

// 줄바꿈 문자를 <br/> 태그로 변경
function nl2br(str) {
	return removeNull(str).replace(/\n/g, "<br/>");
}

function addComma(val) {
	if(isEmpty(val)) return "";
	var arr = val.toString().split(".");
	arr[0] = arr[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	return arr.join(".");
}

function removeComma(val) {
	return val.split(",").join("");
}
												/********************************************/
function getNumberFormat(val, decimal, removeZero) {
	if(isEmpty(val)) return "";
	if(isEmpty(decimal)){decimal = DECIMAL_SCALE;}
	if(isEmpty(removeZero)){removeZero = REMOVE_POINT_ZEROS;}

	var result;
	if(removeZero){
		result = removePointZeros(parseFloat(val).toFixed(decimal),0,removeZero);
	}else{
		result = parseFloat(val).toFixed(decimal);
	}

	var arr = result.toString().split(".");
	arr[0] = arr[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");

	return arr.join(".");
}

// 소수점 이하 0 제거
function removePointZeros(val, replaceIsNaN, removeZero) {
	if(isEmpty(removeZero)){removeZero = REMOVE_POINT_ZEROS;}
	if(isNaN(val)) {
		return replaceIsNaN;
	} else {
		if(removeZero) {
			return val.toString().replace(/\.0+$/, "").replace(/(\.\d*[1-9])0*/, "$1");
		} else {
			return val;
		}
	}
}

// email 패턴 체크
function checkEmail(email) {
    var regex = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
    return regex.test(email);
}

/*
 * vo의 값을 form에 세팅
 * - vo : dataVO 같은 json 값
 * - formId : form ID
 * - keys (배열) : vo에서 form에 세팅할 key들. (생략시 vo의 모든 key값을 조회해서 form에 해당 name이 있으면 세팅)
 */
function voToForm(vo, formId, keys) {
	var $f = $("#" + formId);
	if(isEmpty(keys)) {
		keys = [];
		for(var k in vo) {
			if(k == "_csrf" || k.substring(0,4) == "find") continue;
			keys.push(k);
		}
	}
	$(keys).each(function(i, e) {
		$f.find(":input[name=" + e + "]:not(':radio'):not(':checkbox')").val(vo[e]);
	});
}

/*
 * vo의 값을 form에 세팅
 * - vo : dataVO 같은 json 값
 * - formId : form ID
 * - keys (배열) : vo에서 form에 세팅할 key들. (생략시 vo의 모든 key값을 조회해서 form에 해당 name이 있으면 세팅)
 * - .val()이 아닌 .html()로 값을 set
 */
function voToFormForText(vo, formId, keys) {
	var $f = $("#" + formId);
	if(isEmpty(keys)) {
		keys = [];
		for(var k in vo) {
			if(k == "_csrf" || k.substring(0,4) == "find") continue;
			keys.push(k);
		}
	}
	$(keys).each(function(i, e) {
		$f.find("#" + e).html(vo[e]);
	});
}

/*
 * form의 값들을 지움에 세팅
 * - formId : form ID
 * - keys (배열) : form에서 값을 지울 name들. (생략시 form을 reset함. 이 경우에 검색조건의 값들도 사라지므로 주의할 것.)
 */
function resetForm(formId, keys) {
	var $f = $("#" + formId);
	if(isEmpty(keys)) {
		$f.reset();
	} else {
		$(keys).each(function(i, e) {
			$f.find("input[name=" + e + "]:not(':radio'):not(':checkbox'), textarea[name=" + e + "]").val("");
			$f.find("select[name=" + e + "]").val($f.find("select[name=" + e + "] option:first").val());
		});
	}
}

// 전역 검색조건 변수 세팅
function setFindValue(varNm, value) {
	gFindValues[varNm] = value;
	$.cookie(varNm, value);
}

// 전역 검색조건 get
function getFindValue(varNm) {
	return gFindValues[varNm];
}

/*
 * form에서 find로 시작되는 값들을 전역 검색조건 변수에 세팅
 * gFindValues에 선언된 값만 저장됨
 */
function formToFindValue(formId) {
	if(formId.indexOf("pop") == 0) return false;

	var $f = $("#" + formId);
	var $e;
	var varNm;
	$f.find(":input[name^=find]:not(':radio'), :radio[name^=find]:checked").each(function(i, e) {
		if(isUndefined(gFindValues[$(e).attr("name")])) return true;
		setFindValue($(e).attr("name"), $(e).val());
	});
}

// form 데이타 serialize
function getFormData(formId) {
	formToFindValue(formId);
	return $("#" + formId).serialize();
}

/*
 * 해당 id의 값을 삭제
 * - ids : 값을 삭제할 id 또는 id 배열
 */
function resetInput(id) {
	if(Array.isArray(id)) {
		$(id).each(function(i, e) {
			$("#" + e).val("");
		});
	} else {
		$("#" + id).val("");
	}
}

// _csrf값 가져옴
function getCsrf(formId) {
	if(isEmpty(formId)) {
		return $("[name=_csrf]").eq(0).val();
	} else {
		return $("#" + formId).find("[name=_csrf]").eq(0).val();
	}
}

// 언어 변경
function changeLang(lang) {
	var location = document.location;
	url = [location.protocol, '//', location.host, location.pathname].join("") + "?lang=" + lang + "#" + currPgmId;
	document.location.href = url;
}

/*
 * pageId로 페이지 이동 (메뉴 클릭으로 이동)
 * - pgmId : 이동할 pgmId
 * - callbackMsg : 페이지 이동 후 보여줄 메시지 (생략 가능
 */
function loadPageByPgmId(pgmId, callbackMsg) {
	if(isEmpty(pgmId)) {
		$.showMsgBox(getMessage("errors.noUrl"));
		return;
	} else if(pgmId == "MAIN") {
		pgmId = $(".menuLvl4 > li:first").attr("id").replace("menu_", "");
	}

	if(getFindValue("findScDeptId") == "") {
		setFindValue("findScDeptId", removeNull($("#scDeptList > ul").find(".scDeptRoot").data("scDeptId")));
	}
	gFindValues["pgmId"] = pgmId;

	$.ajax({
		url : url,
		method : "GET",
		cache : false,
		dataType : "html",
		data : gFindValues,
		beforeSend : function(xhr, settings) {
			$(".contents").css({
				"opacity"	:	0,
			});

			//console.log(settings);
		}
	}).done(function(html) {
		deleteCallbackFunctions();

		currPgmId = pgmId;
		$(".currentMenu").removeClass("currentMenu");
		$("#menu_" + currPgmId).addClass("currentMenu");

		$("#menu .active").removeClass("active");
		$("#menu_" + currPgmId).addClass("active").children("a").addClass("active");
		$("#menu_" + currPgmId).parents(".menu li").children("a").addClass("active");
		$("#menu_" + currPgmId).parents(".menu li").children("ul").addClass("active");

		checkBookmarkIcon();

		var location = document.location;
		window.location.hash = "#" + currPgmId;
		$(".contents").html(html.replace(/\u200B/g,''));
		$(".contents").find("form").on("submit", function(e) {
			e.preventDefault();
			return false;
		});

		$(window).scrollTop(0);
		$(".ui-dialog").css({"top" : 300});
		setNavi(pgmId);
		setSideMenu(pgmId);
		setGuideComment(pgmId);

		// 모니터링 메뉴인 경우 권한에 맞는 조직도 생성
		gIsMonitoringMenu = ($("#menu_" + pgmId).closest("#menu_" + monitoringPgmId).length > 0);
		if(gIsMonitoringMenu == null || gIsMonitoringMenu != gIsMonitoringScDeptTree) {
			gIsMonitoringScDeptTree = gIsMonitoringMenu;

			$("#scDeptList").empty().append($("#scDeptListOrg > ul").clone());

			if(gIsMonitoringMenu && !$("#scDeptList .scDept_" + gMonitoringRootScDeptId).hasClass("scDeptRoot")) {
				$("#scDeptList .scDept_" + gMonitoringRootScDeptId).appendTo($("#scDeptList .scDeptRoot").parent());
				$("#scDeptList .scDeptRoot").remove();
				$("#scDeptList .scDept_" + gMonitoringRootScDeptId).addClass("scDeptRoot");
			}

			$("#scDeptList .scDeptRoot").data("scDeptId", $("#scDeptListOrg .scDept_" + gMonitoringRootScDeptId).data("scDeptId"));

			$("#scDeptList").find("a").addClass("active");
		}

		if($(".sch-oga:not(.forDeptList)").length > 0) {
			// 검색조건의 성과조직도 생성
			makeFindScDeptList(FIND_SC_DEPT_LIST_ID, FIND_SC_DEPT_ID);
		}
		if($(".sch-oga.forDeptList").length > 0) {
			// 검색조건의 성과조직도 생성
			makeFindDeptList(FIND_DEPT_LIST_ID, FIND_DEPT_ID);
		}

		// 전체메뉴 닫음
		$("#gnb .depth2 .menu .allmenu a").removeClass("active");
		$(".am-bx").hide();
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
	}).always(function() {
		$(".contents").animate({
			opacity	:	1
		}, "fast", function() {
			if(isNotEmpty(callbackMsg)) {
				$.showMsgBox(callbackMsg);
			}
		});
	});
}

// 네비게이션 표시
function setNavi(pgmId) {
	$("#nav > ul > li:gt(0)").remove();
	$($("#menu_" + pgmId).parents("li[id^=menu]").get().reverse()).each(function(i, e) {
		$("<li>").text($.trim($(e).children("a").text())).appendTo("#nav > ul");
	});
	var title = $.trim($("#menu_" + pgmId + " > a").text());
	$("<li>").addClass("active").text(title).appendTo("#nav > ul");
	$("#title").text(title);
}

// 도움말 표시
function setGuideComment(pgmId,pageNm) {
	//$("#contents p.help > span.text").show();
	/*
	var imgpath = "http://"+window.location.host+contextPath+"/menual/"+pgmId+".jpg";

	if(isImage(imgpath)){
		$("p.help").show();
		$("#helpSpan").html("<input type=\"image\" id=\"helpImages\" src=\"${img_path}/menual/"+helpImagesMap[pgmId]+".jpg\"/>")
	}else{
		$("p.help").hide();
		$("#helpSpan").empty();
	}
	*/

	var imageNm = "";
	if(!isEmpty(pgmId)){
		imageNm = helpImagesMap[pgmId];
	}else if(!isEmpty(pageNm)){
		imageNm = pageNm;
	}

	//console.log("existHelpImages",existHelpImages);
	//console.log("pageNm",imageNm);

	if(existHelpImages.indexOf(imageNm) > -1){
		$("p.help").show();
		$("#contents p.help > span.text").hide();
		$("#helpSpan").empty();
		$("#helpSpan").html("<input type=\"image\" id=\"helpImages\" src=\""+contextPath+"/images/manual/"+imageNm+".JPG\"/>");
	}else{
		$("p.help").hide();
		$("#contents p.help > span.text").hide();
		$("#helpSpan").empty();
	}

	/*
	$.ajax({
		url : imagePath+"/manual/"+imageNm+".JPG",
		data : gFindValues,
		method : "POST",
		cache : false
	}).done(function(data) {

		$("p.help").show();
		$("#contents p.help > span.text").hide();
		$("#helpSpan").empty();
		if(!isEmpty(pgmId)){
			$("#helpSpan").html("<input type=\"image\" id=\"helpImages\" src=\""+imagePath+"/manual/"+helpImagesMap[pgmId]+".JPG\"/>");
		}else if(!isEmpty(pageNm)){
			$("#helpSpan").html("<input type=\"image\" id=\"helpImages\" src=\""+imagePath+"/manual/"+pageNm+".JPG\"/>");
		}

		isGuideCommentVisible = false;

	}).error(function(xhr, ajaxOptions, thrownError) {
		$("p.help").hide();
		$("#contents p.help > span.text").hide();
		$("#helpSpan").empty();
	});
	*/

	//$("p.help").show();
	/*
	if(isNotEmpty(guideCommentMap[pgmId])) {
		$("p.help").show();
		$("#guideComment").html(nl2br(guideCommentMap[pgmId]));
	} else {
		$("p.help").hide();
		$("#guideComment").html("");
	}
	*/
	//console.log(url);
	//$("#helpSpan").html("<input type=\"image\" id=\"helpImages\" src=\"${img_path}/menual/"+pgmId+".jpg\"/>")
	//isGuideCommentVisible = false;

}

function showGuideComment(sw) {
	var $guideComment = $("#contents p.help > span.text");

	if(isEmpty(sw)) {
		if(isGuideCommentVisible) {
			$guideComment.fadeOut(500);
			isGuideCommentVisible = false;
		} else {
			$guideComment.fadeIn(500);
			isGuideCommentVisible = true;
		}
	} else {
		if(sw) {
			$guideComment.fadeIn(500);
			isGuideCommentVisible = true;
		} else {
			$guideComment.fadeOut(500);
			isGuideCommentVisible = false;
		}
	}

	resizeGuideComment();
}

function resizeGuideComment(){

	var minWidth = 800;
	var minHeight = 400;
	var winWidth = $(window).width();
	var winHeight = $(window).height();
	var helpWidth = $("#contents p.help > span.text > #helpSpan").width();
	var helpHeight = $("#contents p.help > span.text > #helpSpan").height();
	var helpPosibleWidth = winWidth-350;
	var helpPosibleHeight = winHeight-150;

	if(helpWidth > helpPosibleWidth){
		$("#contents p.help > span.text").width(helpPosibleWidth);
		$("#contents p.help > span.text > .close").css("left",helpPosibleWidth-10);
	}else if(minWidth > helpWidth){
		$("#contents p.help > span.text").width(minWidth);
		$("#contents p.help > span.text > .close").css("left",minWidth-10);
	}else{
		$("#contents p.help > span.text").css("width","");
		$("#contents p.help > span.text > .close").css("left",helpWidth-10);
	}
	if(helpHeight > helpPosibleHeight){
		$("#contents p.help > span.text").height(helpPosibleHeight);
	}else if(minHeight > helpHeight){
		$("#contents p.help > span.text").height(minHeight);
	}else{
		$("#contents p.help > span.text").css("height","");
	}

	if(helpWidth > helpPosibleWidth || helpHeight > helpPosibleHeight){
		$("#contents p.help > span.text").css("overflow","auto");
	}
}

// 로그아웃 처리
function doLogout() {
	document.logoutForm.submit();
}

// 페이지 이동시 특정 페이지에서만 사용하는 callbackFunction들 삭제
function deleteCallbackFunctions() {
	if(typeof resizeCallback != "undefined" && typeof resizeCallback == "function") {
		delete resizeCallback;
	}
	if(typeof setFindDeptNmCallback != "undefined" && typeof setFindDeptNmCallback == "function") {
		delete setFindDeptNmCallback;
	}
}

/*
 * 다중 파일 업로드 모듈
 * config : 옵션
	- targetId : 모듈이 위치할 dom id
	- inputName : 파일을 서버로 전송할 때 사용할 파라미터명
	- canUpload : 업로드 가능 여부 (기본값 : true), false일 경우 다운로드만 가능
 	- width : 너비 (기본값 : 600, 최소값:300)
 	- height : 높이 (기본값 : 자동)
 	- maxFileCnt : 최대 첨부 파일 수 (기본값 : 1)
 	- maxTotalSize : 최대 첨부 파일 용량(업로드한 모든 파일들의 용량 합계를 기준으로 함, 기본값 : 500M)
 	- allowFileExts : 허용할 파일 확장자, 생략할 경우 제한 없음, comma로 구분한 문자열 또는 문자열 배열로 선언할 수 있음
 	- delChkIdx : 첨부파일 삭제 그룹 idx (기본값 : 파일모듈 순서대로 자동으로 채번)
 */
function SGFileUploader(config) {
	var sgf = this;
	if(isEmpty(isAdvancedUpload)) {
		isAdvancedUpload = function() {
			var div = document.createElement("div");
			return (("draggable" in div) || ("ondragstart" in div && "ondrop" in div)) && "FormData" in window && "FileReader" in window;
		}();
	}
	sgf.config = config;
	sgf.name = removeNull(sgf.config.inputName);
	sgf.fileList = new Array();
	sgf.fileCount = 0;
	sgf.totalSize = 0;
	if(isEmpty(config)) {
		config = {};
	};

	if(isEmpty(sgf.config.canUpload)) sgf.config.canUpload = true;
	if(isEmpty(sgf.config.width)) sgf.config.width = 600;
	//if(isEmpty(sgf.config.height)) sgf.config.height = "inherit";
	if(isEmpty(sgf.config.maxFileCnt)) sgf.config.maxFileCnt = 5;
	if(isEmpty(sgf.config.maxTotalSize)) sgf.config.maxTotalSize = "500MB";
	if(isEmpty(sgf.config.allowFileExts)) sgf.config.allowFileExts = [];
	if(typeof sgf.config.allowFileExts == "string") sgf.config.allowFileExts = sgf.config.allowFileExts.toLowerCase().split(",");
	for(var i in sgf.config.allowFileExts) {
		sgf.config.allowFileExts[i] = sgf.config.allowFileExts[i].toLowerCase();
	}
	if(isEmpty(sgf.config.delChkIdx)) sgf.config.delChkIdx = ($(".sgFileUploader").length == 0 ? "" : ($(".sgFileUploader").length + 1).toString());

	if(isNaN(sgf.config.maxTotalSize) && sgf.config.maxTotalSize.indexOf("MB") > -1) {
		sgf.config.maxTotalSize = parseInt(sgf.config.maxTotalSize.replaceAll("MB", ""), 10) * 1024 * 1024;
	}

	var $fileBoxLayout = $("#" + sgf.config.targetId).addClass("sgFileUploader").css({"width":sgf.config.width, "height":sgf.config.height});

	var $fileBox, $divInputFile, $divStatus;
	var $divNewFileList, $newFileListUl, $form;
	if(sgf.config.canUpload) {
		$fileBox = $("<div class='fileBox'>").appendTo($fileBoxLayout);
		$divInputFile = $("<div class='divInputFile'>").appendTo($fileBox);

		// 상태 영역
		$divStatus = $("<div class='divStatus'>").appendTo($fileBoxLayout);
		$divStatus.append("<span><label>Files : </label><label id='fileCnt_" + sgf.name + "'>0</label><label> / " + sgf.config.maxFileCnt + "</label></span>");
		if(isAdvancedUpload) {
			$divStatus.append("<span class='ml10'><label>Total Size : </label><label id='totalSize_" + sgf.name + "'>0</label><label> / " + addComma(sgf.config.maxTotalSize) + " bytes</label></span>");
		}
	}

	// 기존에 업로드한 파일들 목록
	var $divFileList = $("<div class='divFileList" + (sgf.config.canUpload?" mt5":"") + "'>").appendTo($fileBoxLayout);
	var $fileListUl = $("<ul class='fileList'>").appendTo($divFileList);

	if(sgf.config.canUpload) {
		// 새로 업로드한 파일들 목록
		$divNewFileList = $("<div class='divNewFileList mt10'>").appendTo($fileBoxLayout);
		$newFileListUl = $("<ul class='newFileList'>").appendTo($divNewFileList);

		$form = $fileBox.parents("form:first");
		if(isAdvancedUpload) {
			$fileBox.addClass("has-advanced-upload");
		}
	}

	// 파일 목록 표시
	sgf.showFile = function(fileId, fileSn, fileName, fileSize, canDelete) {
		sgf.fileCount++;
		sgf.totalSize += fileSize;

		var $fileItem = $("<li>").data("fileSize", fileSize);
		var $fileLink = $("<a href='#' onclick='fileDownload(\"" + fileId + "\", \"" + fileSn + "\");return false;'>");
		$fileLink.append("<label>" + fileName + " (" + addComma(fileSize) + " bytes)" + "</label>");
		$fileItem.append($fileLink);
		if(canDelete) {
			$("<input type='checkbox' class='ml5' name='chkAttachFiles" + sgf.config.delChkIdx + "' id='delChk_" + sgf.name + "_" + fileSn + "' value='" + fileSn + "'>")
				.click(function(){sgf.chkRemoveFile(this)})
				.appendTo($fileItem);
			$fileItem.append("<label for='delChk_" + sgf.name + "_" + fileSn + "'><span></span>" + getMessage("word.atchFileDelete") + "</label>");
		}
		$fileListUl.append($fileItem);
		sgf.updateStatus();
	}

	// 다중 파일 추가
	sgf.addFileList = function(files) {
		$.each(files, function(i, file) {
			if(sgf.config.maxFileCnt < sgf.fileCount + 1) {
				$.showMsgBox(getMessage("errors.exceededMaximumFiles", sgf.config.maxFileCnt));
				return false;
			}

			if(sgf.config.maxTotalSize < sgf.totalSize + file.size) {
				$.showMsgBox(getMessage("errors.exceededMaximumFileSize", addComma(sgf.config.maxTotalSize)));
				return false;
			}

			if(sgf.config.allowFileExts.length > 0
					&& $.inArray(getFileExtension(file.name).toLowerCase(), sgf.config.allowFileExts) == -1) {
				$.showMsgBox(getMessage("errors.nowAllowedFormat2"));
				return false;
			}

			var $fileItem = $("<li>").data("fileSize", file.size);

			$fileItem.append("<label>").text(file.name + " (" + addComma(file.size) + " bytes)");
			$("<input type='button' value='x' class='ml5 close'>").on("click", function(e) {
				sgf.removeFile(this);
			}).appendTo($fileItem);

			$newFileListUl.append($fileItem);

			sgf.fileList.push(file);
			sgf.totalSize += file.size;
			sgf.fileCount++;
		});
		sgf.updateStatus();
	}

	// 파일 1개 추가 (IE9용)
	sgf.addFile = function(fileName) {
		var $fileItem = $("<li>");
		$fileItem.append("<label>").text(fileName);
		$("<input type='button' value='x' class='ml5 close'>").on("click", function(e) {
			sgf.removeFile(this);
		}).appendTo($fileItem);
		$newFileListUl.append($fileItem);

		$fileBox.find("input[type=file]").removeAttr("id");
		var $file = ("<input class='inputFile' type='file' name='" + sgf.name + "' id='file_" + sgf.name + "'>");
		$file.on("change", function() {
			sgf.addFile(this.value);
		});
		$divInputFile.append($file);
		sgf.fileCount++;
		sgf.updateStatus();

		if(sgf.config.maxFileCnt < sgf.fileCount) {
			sgf.removeLastFile();
			$.showMsgBox(getMessage("errors.exceededMaximumFiles", sgf.config.maxFileCnt));
			return false;
		}

		fileName = fileName.substring(fileName.lastIndexOf("/")).substring(fileName.lastIndexOf("\\") + 1);
		if(sgf.config.allowFileExts.length > 0
				&& $.inArray(getFileExtension(fileName).toLowerCase(), sgf.config.allowFileExts) == -1) {
			sgf.removeLastFile();
			$.showMsgBox(getMessage("errors.nowAllowedFormat2"));
			return false;
		}
	}

	// 파일 삭제 체크
	sgf.chkRemoveFile = function(obj) {
		if($(obj).is(":checked")) {
			sgf.fileCount--;
			if(isAdvancedUpload) {
				sgf.totalSize -= $(obj).parents("li:first").data("fileSize");
			}
		} else {
			sgf.fileCount++;
			if(isAdvancedUpload) {
				sgf.totalSize += $(obj).parents("li:first").data("fileSize");

				while(sgf.fileCount > 0
					&& (sgf.fileCount > sgf.config.maxFileCnt || sgf.totalSize > sgf.config.maxTotalSize)) {
					sgf.removeLastFile();
				}
			} else {
				if(sgf.fileCount > sgf.config.maxFileCnt) {
					sgf.removeLastFile();
				}
			}
		}
		sgf.updateStatus();
	}

	// 파일 삭제
	sgf.removeFile = function(obj) {
		var $li = $(obj).parents("li:first");
		var fileIdx = $newFileListUl.find("li").index($li);
		if(isAdvancedUpload) {
			sgf.fileList.splice(fileIdx, 1);
			sgf.totalSize -= $li.data("fileSize");
		} else {
			$form.find("input[name=" + sgf.name + "]").eq(fileIdx).remove();
		}
		$li.remove();
		sgf.fileCount--;
		sgf.updateStatus();
	}

	// 마지막 파일 삭제
	sgf.removeLastFile = function() {
		if($newFileListUl.find("li").length > 0) {
			sgf.removeFile($newFileListUl.find("li:last input")[0]);
		}
	}

	// 새로 업로드한 첨부파일 정보 초기화
	sgf.reset = function() {
		if(isNotEmpty($newFileListUl)) {
			while($newFileListUl.find("li").length > 0) {
				sgf.removeFile($newFileListUl.find("li:last input")[0]);
			}
		}
	}

	sgf.resetAll = function() {
		$fileListUl.empty();
	}

	// 파일 정보 저장
	sgf.saveFiles = function(data) {
		if(isAdvancedUpload) {
			$.each(sgf.fileList, function(i, file) {
				data.append(sgf.name, file);
			});
		}
	}

	sgf.updateStatus = function() {
		$("#fileCnt_" + sgf.name).text(sgf.fileCount);
		if(isAdvancedUpload) {
			$("#totalSize_" + sgf.name).text(addComma(sgf.totalSize));
		}

		if(sgf.config.maxFileCnt <= sgf.fileCount
				|| (isAdvancedUpload && sgf.config.maxTotalSize <= sgf.totalSize)) {
			$("#file_" + sgf.name).attr("disabled", "disabled");
		} else {
			$("#file_" + sgf.name).removeAttr("disabled");
		}
	}

	sgf.init = function() {
		$divInputFile.append("<label for='file_" + sgf.name + "'><strong>Choose files</strong><span class='boxDragndrop'> or drag it here</span>.</label>");
		var $file;
		if(isAdvancedUpload) {
			$file = $("<input class='inputFile' type='file' id='file_" + sgf.name + "' " + (sgf.config.maxFileCnt > 1 ? "multiple" : "") + ">");
			$file.on("change", function() {
				sgf.addFileList($(this)[0].files);
				$(this).val("");
			});
		} else {
			$file = $("<input class='inputFile' type='file' name='" + sgf.name + "' id='file_" + sgf.name + "' " + (sgf.config.maxFileCnt > 1 ? "multiple" : "") + ">");
			$file.on("change", function() {
				sgf.addFile(this.value);
			});
		}
		$divInputFile.append($file);

		if(isAdvancedUpload) {
			$form.on("drag dragstart dragend dragover dragenter dragleave drop", function(e) {
				e.preventDefault();
				e.stopPropagation();
			});
			$fileBox.on("dragover dragenter", function() {
				$fileBox.addClass("is-dragover");
			})
			.on("dragleave dragend drop", function() {
				$fileBox.removeClass("is-dragover");
			})
			.on("drop", function(e) {
				sgf.addFileList(e.originalEvent.dataTransfer.files);
			});
		}

		/*
		$form.on("change", "#file_" + sgf.name, function(e) {
			if(isEmpty(sgf)) return false;
			if(isAdvancedUpload) {
				sgf.addFileList($(this)[0].files);
				$(this).val("");
			} else {
				sgf.addFile(this.value);
			}
		});
		*/
	}

	sgf.destroy = function() {
		sgf.reset();
		$fileBoxLayout.empty().removeClass("sgFileUploader");
		sgf = null;
	}

	if(sgf.config.canUpload) {
		sgf.init();
	}

	return sgf;
}

// url로 페이지 이동
function loadPage(url, formId) {
	if(isUndefined(url)) {
		$.showMsgBox(getMessage("errors.noUrl"));
		return;
	}

	var data;
	if(isUndefined(formId)) {
		gFindValues["_csrf"] = getCsrf();
		data = gFindValues;
	} else {
		data = getFormData(formId)
	}

	$.ajax({
		url : url,
		method : "POST",
		cache : false,
		dataType : "html",
		data : data,
		beforeSend : function(xhr) {
			$(".contents").css({
				"opacity" : 0
			});
		}
	}).done(function(html) {
		deleteCallbackFunctions();

		try{
			$(".contents").html(html.replace(/\u200B/g,''));
			if($(".sch-oga:not(.forDeptList)").length > 0) {
				// 검색조건의 성과조직도 생성
				makeFindScDeptList(FIND_SC_DEPT_LIST_ID, FIND_SC_DEPT_ID);
			}
			if($(".sch-oga.forDeptList").length > 0) {
				// 검색조건의 성과조직도 생성
				makeFindDeptList(FIND_DEPT_LIST_ID, FIND_DEPT_ID);
			}

			$(window).scrollTop(0);
			$(".ui-dialog").css({"top" : 300});
		} catch(e) {
			$.showMsgBox(getMessage("errors.processing"));
		}
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
	}).always(function() {
		$(".contents").animate({
			opacity	:	1
		}, "fast");
	});

	//"/bsc/base/metric/metric/metricList.do".substring("/bsc/base/metric/metric/metricList.do".lastIndexOf("/")+1,"/bsc/base/metric/metric/metricList.do".indexOf("."))
	setGuideComment(null, url.substring(url.lastIndexOf("/")+1,url.indexOf(".")));
}

/*
 * ajax 호출용 공통함수
 * 	url : 호출할 url
 * 	data : 전송할 data
 * 	doneCallbackFunc : ajax 호출 성공 후 실행할 함수/함수명 (생략 가능)
 * 	doneCallbackArgs : ajax 호출 성공 후 실행할 함수의 인자값들. 배열로 선언할 것. (생략 가능)
 * 	failCallbackFunc : ajax 호출 실패 후 실행할 함수/함수명 (생략 가능)
 * 	failCallbackArgs : ajax 호출 실패 후 실행할 함수의 인자값들. 배열로 선언할 것. (생략 가능)
 * 	alwaysCallbackFunc : ajax 호출 완료 후(성공/실패 함수 처리 후) 실행할 함수/함수명 (생략 가능)
 * 	alwaysCallbackArgs : ajax 호출 완료 후 실행할 함수의 인자값들. 배열로 선언할 것. (생략 가능)
 */
function sendAjax(option) {
	if(isEmpty(option) || isEmpty(option.url)) {
		$.showMsgBox(getMessage("errors.noUrl"));
		return false;
	}
	if(!isEmpty(option.async) && typeof option.async != "boolean"){
		$.showMsgBox(getMessage("errors.processing"));
		return false;
	}
	if(isEmpty(option.async)){
		option.async = true;
	}
	$.ajax({
		url : option.url,
		data : option.data,
		async : option.async,
		method : "POST",
		cache : false,
		dataType : "json"
	}).done(function(json) {
		if(isNotEmpty(option.doneCallbackFunc)) {
			if(isEmpty(option.doneCallbackArgs)) {
				option.doneCallbackArgs = [];
			} else if(!Array.isArray(option.doneCallbackArgs)) {
				option.doneCallbackArgs = [option.doneCallbackArgs];
			}
			option.doneCallbackArgs.push(json);

			if(typeof option.doneCallbackFunc == "function") {
				option.doneCallbackFunc.apply(this, option.doneCallbackArgs);
			} else {
				window[option.doneCallbackFunc].apply(window, option.doneCallbackArgs);
			}
		}
	}).fail(function(jqXHR, textStatus) {
		if(isNotEmpty(option.failCallbackFunc)) {
			if(isEmpty(option.failCallbackArgs)) {
				option.failCallbackArgs = [];
			} else if(!Array.isArray(option.failCallbackArgs)) {
				option.failCallbackArgs = [option.failCallbackArgs];
			}
			option.failCallbackArgs.push(jqXHR);
			option.failCallbackArgs.push(textStatus);

			if(typeof option.failCallbackFunc == "function") {
				option.failCallbackFunc.apply(this, option.failCallbackArgs);
			} else {
				window[option.failCallbackFunc].apply(window, option.failCallbackArgs);
			}
		} else {
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
		}
	}).always(function() {
		if(isNotEmpty(option.alwaysCallbackFunc)) {
			if(isEmpty(option.alwaysCallbackArgs)) {
				option.alwaysCallbackArgs = [];
			} else if(!Array.isArray(option.alwaysCallbackArgs)) {
				option.alwaysCallbackArgs = [option.alwaysCallbackArgs];
			}

			if(typeof option.alwaysCallbackFunc == "function") {
				option.alwaysCallbackFunc.apply(this, option.alwaysCallbackArgs);
			} else {
				window[option.alwaysCallbackFunc].apply(window, option.alwaysCallbackArgs);
			}
		}
	});
}

/*
 * multipart form submit용 공통함수
 * 	url : 호출할 url
 * 	formId : 전송할 form id
 * 	fileModules : 파일모듈 객체 (배열 선언 가능)
 * 	doneCallbackFunc : ajax 호출 성공 후 실행할 함수명 (생략 가능)
 * 	failCallbackFunc : ajax 호출 실패 후 실행할 함수명 (생략 가능)
 * 	alwaysCallbackFunc : ajax 호출 완료 후(성공/실패 함수 처리 후) 실행할 함수명 (생략 가능)
 */
function sendMultipartForm(option) {
	if(isEmpty(option) || isEmpty(option.url)) {
		$.showMsgBox(getMessage("errors.noUrl"));
		return false;
	} else if(isEmpty(option.formId)) {
		$.showMsgBox(getMessage("errors.required", "formId"));
		return false;
	}

	if(isAdvancedUpload) {
		var data = new FormData($("#" + option.formId)[0]);
		if(isNotEmpty(option.fileModules)) {
			if(Array.isArray(option.fileModules)) {
				for(var i in option.fileModules) {
					option.fileModules[i].saveFiles(data);
				}
			} else {
				option.fileModules.saveFiles(data);
			}
		}

		$.ajax({
			url : option.url,
			data : data,
			method : "POST",
			enctype	: "multipart/form-data",
			cache : false,
			dataType : "json",
			contentType: false,
			processData : false,
			xhr : function() {
				var xhr = $.ajaxSettings.xhr();
				xhr.upload.onprogress = function(e) {
					if(e.lengthComputable) {
						$(".loadingPercentDiv").show().text(parseInt((e.loaded / e.total) * 100, 10) + "%");
					}
				};
				return xhr;
			}
		}).done(function(json) {
			$(".loadingPercentDiv").hide();
			if(isNotEmpty(option.doneCallbackFunc)) {
				window[option.doneCallbackFunc].apply(window, [json]);
			}
		}).fail(function(jqXHR, textStatus) {
			$(".loadingPercentDiv").hide();
			if(isNotEmpty(option.failCallbackFunc)) {
				window[option.failCallbackFunc].apply(window, [jqXHR, textStatus]);
			} else {
				try {
					var json = JSON.parse(jqXHR.responseText);
					if(!isEmpty(json.msg)) {
						$.showMsgBox(json.msg);
					} else {
						$.showMsgBox(getMessage("errors.processing"));
					}
				} catch(e) {
					$.showMsgBox(getMessage("errors.processing"));
				}
			}
		}).always(function() {
			if(isNotEmpty(option.alwaysCallbackFunc)) {
				window[option.alwaysCallbackFunc].apply(window, []);
			}
		});
	} else {
		var $f = $("#" + option.formId);
		if($f.find("[name=doneCallbackFunc]").length == 0) {
			$("<input type='hidden' name='doneCallbackFunc'>").val(removeNull(option.doneCallbackFunc)).appendTo($f);
		} else {
			$f.find("[name=doneCallbackFunc]").val(removeNull(option.doneCallbackFunc));
		}
		if($f.find("[name=failCallbackFunc]").length == 0) {
			$("<input type='hidden' name='failCallbackFunc'>").val(removeNull(option.failCallbackFunc)).appendTo($f);
		} else {
			$f.find("[name=failCallbackFunc]").val(removeNull(option.failCallbackFunc));
		}
		if($f.find("[name=alwaysCallbackFunc]").length == 0) {
			$("<input type='hidden' name='alwaysCallbackFunc'>").val(removeNull(option.alwaysCallbackFunc)).appendTo($f);
		} else {
			$f.find("[name=alwaysCallbackFunc]").val(removeNull(option.alwaysCallbackFunc));
		}

		var f = $f[0];
		f.target = "hiddenFrame";
		f.action = option.url;
		showLoading(true);
		f.submit();
	}
}

/*
 * multipart ajax 호출용 공통함수 (IE9에서 실행 안됨)
 * 	url : 호출할 url
 * 	formId : 전송할 form id
 * 	doneCallbackFunc : ajax 호출 성공 후 실행할 함수명
 * 	doneCallbackArgs : ajax 호출 성공 후 실행할 함수의 인자값들. 배열로 선언할 것. 없으면 null 선언
 * 	failCallbackFunc : ajax 호출 실패 후 실행할 함수명
 * 	failCallbackArgs : ajax 호출 실패 후 실행할 함수의 인자값들. 배열로 선언할 것. 없으면 null 선언
 * 	alwaysCallbackFunc : ajax 호출 완료 후(성공/실패 함수 처리 후) 실행할 함수명
 * 	alwaysCallbackArgs : ajax 호출 완료 후 실행할 함수의 인자값들. 배열로 선언할 것. 없으면 null 선언
 */
function sendMultipartAjax(option) {
	if(isEmpty(option) || isEmpty(option.url)) {
		$.showMsgBox(getMessage("errors.noUrl"));
		return false;
	}

	$.ajax({
		url : option.url,
		data : new FormData($("#" + option.formId)[0]),
		method : "POST",
		enctype	: "multipart/form-data",
		cache : false,
		dataType : "json",
		contentType: false,
		processData : false
	}).done(function(json) {
		if(isNotEmpty(option.doneCallbackFunc)) {
			if(isEmpty(option.doneCallbackArgs)) {
				option.doneCallbackArgs = [];
			}
			option.doneCallbackArgs.push(json);
			window[option.doneCallbackFunc].apply(window, option.doneCallbackArgs);
		}
	}).fail(function(jqXHR, textStatus) {
		if(isNotEmpty(option.failCallbackFunc)) {
			if(isEmpty(option.failCallbackArgs)) {
				option.failCallbackArgs = [];
			}
			option.failCallbackArgs.push(jqXHR);
			option.failCallbackArgs.push(textStatus);
			window[option.failCallbackFunc].apply(window, option.failCallbackArgs);
		} else {
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
		}
	}).always(function() {
		if(isNotEmpty(option.alwaysCallbackFunc)) {
			window[option.alwaysCallbackFunc].apply(window, option.alwaysCallbackArgs);
		}
	});
}

/******************************************
 * 파일다운로드
 * 기존의 파일다운로드 방식이 IE에서 제대로 작동하지 않아서(window.open으로 다운로드시 referer가 없어서 접근 에러 발생)
 * iframe을 사용하는 방식으로 변경
 ******************************************/
function fileDownload(atchFileId, fileSn, isPublic) {
	if(isEmpty(isPublic)) isPublic = "N";
	var url = contextPath + "/cmm/fms/FileDown.do?atchFileId="+encodeURIComponent(atchFileId)+"&fileSn="+fileSn;
	if(isPublic == "Y") {
		url = contextPath + "/common/publicFileDown.do?atchFileId="+encodeURIComponent(atchFileId)+"&fileSn="+fileSn;
	}

	var $ifrm = $("#fileDownloadIFrame");
	var $f = $("#fileDownloadForm");
	if($ifrm.length == 0) {
		$ifrm = $("<iframe name='fileDownloadIFrame' id='fileDownloadIFrame' style='display:none;' frameborder='0'>").appendTo("body");
	}
	if($f.length == 0) {
		$f = $("<form name='fileDownloadForm' id='fileDownloadForm' style='display:none;' method='post' target='fileDownloadIFrame'>").appendTo("body");
	}
	if($f.find("[name=_csrf]").length == 0) {
		$f.append("<input type='hidden' name='_csrf' value='" + getCsrf() + "'>");
	}
	$f.attr("action", url).submit();
}

/******************************************
 * 파일다운로드
 * 기존의 파일다운로드 방식이 IE에서 제대로 작동하지 않아서(window.open으로 다운로드시 referer가 없어서 접근 에러 발생)
 * iframe을 사용하는 방식으로 변경
 ******************************************/
function manualFileDownload() {
	var url = contextPath + "/common/ManualFileDown.do";
	var $ifrm = $("#fileDownloadIFrame");
	var $f = $("#fileDownloadForm");

	if($ifrm.length == 0) {
		$ifrm = $("<iframe name='fileDownloadIFrame' id='fileDownloadIFrame' style='display:none;' frameborder='0'>").appendTo("body");
	}
	if($f.length == 0) {
		$f = $("<form name='fileDownloadForm' id='fileDownloadForm' style='display:none;' method='post' target='fileDownloadIFrame'>").appendTo("body");
	}
	if($f.find("[name=_csrf]").length == 0) {
		$f.append("<input type='hidden' name='_csrf' value='" + getCsrf() + "'>");
	}

	$f.attr("action", url).submit();
}

/*
 * message.properties의 메시지 호출
 * 바인딩할 변수가 여러개인 경우 ,로 구분해서 지정하면 됨
 * 예) getMessage("errors.required", "아이디");
 */
function getMessage(msg) {
	var args = "\""+ msg + "\"";
	for(var i=1; i<arguments.length; i++) {
		args += ", \"" + arguments[i] + "\"";
	}
	return eval("jQuery.i18n.prop(" + args + ")");
}

/*
 * 문자열 바이트 계산 (한글 : 3바이트)
 * - 사용방법 : lengthb('문자열')
 */
function lengthb(s,b,i,c) {
	if(s == null || s == undefined || s == ""){
		b = 0;
	}else{
		for(b=i=0;c=s.charCodeAt(i++);b+=c>>11?3:c>>7?2:1);
	}
	return b;
}

/*
 * 문자열 바이트로 자르기 (한글 : 3바이트)
 * - 사용방법 : lengthbCut('문자열', 10)
 */
function lengthbCut(s,m,b,i,c){
	for(b=i=0;b<=m?(c=s.charCodeAt(i++)):false;b+=c>>11?3:c>>7?2:1);
	return s.substring(0, i-1);
}

/**
 * 파일확장자 추출
 * @param str
 * @return
 */
function getFileExtension(str){
	var fileNm	= "";
	var startIdx = 0;
	var endIdx = 0;

	var extName = "";

	if(str != null && str != "") {
		var filePath = str.split("\\");

		fileNm = filePath[filePath.length - 1];
		for(var i=fileNm.length-1; i>-1; i--) {
			if(fileNm.charAt(i) == ".") {
				endIdx = i;
				break;
			}
		}

		if(endIdx == 0){
			extName = "";
		}else{
			extName = fileNm.substr(endIdx + 1);
		}
	}

	return extName;
}

/*
 * maxLength 설정
 * - 사용방법 : maxLength('form name')
 */
function setMaxLength(formNm) {
	$("form[name=" + formNm + "]").find("[maxlength]").each(function(i, e) {
		$(e).on("keyup blur", function() {
			var maxlength = $(this).attr("maxlength");
			if(maxlength == "" || isNaN(maxlength)) return false;
			if(lengthb($(this).val()) > maxlength) {
				$(this).val(lengthbCut($(this).val(), maxlength));
			}
		});
	});
}

function setMaxLengthById(id) {
	$("#" + id).on("keyup blur", function() {
		var maxlength = $(this).attr("maxlength");
		if(maxlength == "" || isNaN(maxlength)) return false;
		if(lengthb($(this).val()) > maxlength) {
			$(this).val(lengthbCut($(this).val(), maxlength));
		}
	});
}

function setMaxLengthByElement($e) {
	$e.on("keyup blur", function() {
		var maxlength = $(this).attr("maxlength");
		if(maxlength == "" || isNaN(maxlength)) return false;
		if(lengthb($(this).val()) > maxlength) {
			$(this).val(lengthbCut($(this).val(), maxlength));
		}
	});
}

// textarea의 bytes 표시
function showBytes(textareaId, labelId) {
	$("#" + labelId).text(lengthb($("#" + textareaId).val()));
	$("#" + textareaId).on("keyup blur", function() {
		$("#" + labelId).text(lengthb($(this).val()));
	});
}

// textarea의 bytes 표시 (키 이벤트 생략)
function showBytesUpdate(textareaId, labelId) {
	$("#" + labelId).text(lengthb($("#" + textareaId).val()));
}

/* 숫자 입력만 허용
 * 사용법 : $('#'+elementId).numericOnly() or $('#'+elementId).numericOnly({allow:"."}) 후자의 경우 . 를 허용하기 위한것 추가적인 허용을 해야할 경우가 있다면
 * 이 함수를 수정할여 추가할것 단 정규식때문에 아무거나 넣는 방식은 안된다
 * 이 함수는 jquery의 numeric가 한글에 대해서 적절한 반응을 하지 못하는 것때문에 만들었으며 keyup이벤트와 change이벤트에 반응한다.
 * 현재 허용 allow 목록 : {allow:".-"}
 * 2018.02.22	kimyh	음수 입력 허용 ({allow:"-"}), 로직 개선
 */
$.fn.numericOnly = function(param,autoComma) {
	var allowList = "\\d+";
	if(param && param.allow && param.allow.length > 0) {

		if(param.allow.indexOf(".") > -1) {
			allowList += "(\\.\\d+)?";
		}
		if(param.allow.indexOf("-") > -1) {
			allowList = "-?" + allowList;
		}
	}

	return this.each(function() {
		var c = $(this);
		var regExp = new RegExp(allowList);
		var resultArr;
		c.on("keyup change", function() {
			$(this).val($(this).val().replace(/[^\d\-\.\,]/g, ""));
		})
		.on("blur", function() {
			resultArr = regExp.exec($(this).val());

			if(isEmpty(resultArr)) {
				$(this).val("");
			} else {
				$(this).val(resultArr[0]);
			}

			if(isNaN($(this).val())) {
				$(this).val("");
			}

		});
	});
};

$.fn.numericCommaOnly = function() {
	var allowList = "-?\\d+(\\,\\d+)*(\\.\\d+)?";

	return this.each(function() {
		var c = $(this);
		var regExp = new RegExp(allowList);
		var resultArr;
		c.on("keyup change", function() {
			$(this).val(addComma($(this).val().replace(/[^\d\-\.]/g, "")));
		})
		.on("blur", function() {
			$(this).val($(this).val().replace(/[^\d\-\.\,]/g, ""));
			resultArr = regExp.exec($(this).val());
			if(isEmpty(resultArr)) {
				$(this).val("");
			} else {
				$(this).val(resultArr[0]);
			}
			if(isNaN($(this).val().replace(/,/gi,""))) {
				$(this).val("");
			}

		});
	});
};

/********************************************************
 * Confirm Dialog 설정
 * parameter (메세지, 콜백함수, 콜백함수용 인자값들)
 * 2018.03.06	kimyh	callBackFunc에 직접 함수를 선언할 수 있도록 수정
 ********************************************************/
$.showConfirmBox = function(msg, callBackFunc) {
	msg = msg.replace(/(&lt;br\/&gt;|\r\n|\\\\n|\\r\\n|\\n|\n)/gi, "<br/>");

	var args = arguments;
	$("#sgDialog").dialog({
		autoOpen: false,
		width: 300,
		modal: true,
		buttons: {
			"Ok": function() {
				$(this).dialog("close");
				if(typeof callBackFunc == "function") {
					callBackFunc();
				} else {
					window[callBackFunc].apply(window, [Array.apply(null, args).slice(2)]); //CallBack 함수 호출
				}
			},
			"Cancel": function() {
				$(this).dialog("close");
			}
		}
	});
	$("#sgDialog p").html(msg);
	$("#sgDialog").dialog("open");
};

/********************************************************
 * Alert Dialog 설정
 * parameter (메세지, 콜백함수, 확인후 포커스 객체)
 ********************************************************/
$.showMsgBox = function(msg, callBackFunc, obj) {
	msg = msg.replace(/(&lt;br\/&gt;|\r\n|\\\\n|\\r\\n|\\n|\n)/gi, "<br/>");

	$("#sgDialog").dialog({
		autoOpen: false,
		width: 300,
		modal: true,
		buttons: {
			"Ok": function() {
				$(this).dialog("close");
				if(callBackFunc != null) {
					if(typeof callBackFunc == "function") {
						callBackFunc.apply(this);
					} else {
						window[callBackFunc].apply(window, arguments);
					}

				}
				if(obj != null) {
					try {
						$("#" + obj).focus(); //focus 설정
					} catch(err) {
						$(obj).focus(); //focus 설정
					}
				}
			}
		}
	});
	$("#sgDialog p").html(msg);
	$("#sgDialog").dialog("open");
};

// html escape
var escapeHTMLMap = {
		"&": "&amp;",
		"<" : "&lt;",
		">" : "&gt;",
		'"' : "&quot;",
		"'" : "&#39;",
		"&amp;#160;" : ""
}

function escapeHTML(s) {
	if(isNotEmpty(s)) {
		if(typeof s != "string") {
			s = s.toString();
		}

		var s2 = s;
		for(var k in escapeHTMLMap) {
			s = s.replaceAll(k, escapeHTMLMap[k]);
		}
	}
	return s;
}

// html unescape
var unescapeHTMLMap = {
		"&amp;" : "&",
		"&lt;" : "<",
		"&gt;" : ">",
		"&quot;" : '"',
		"&#39;" : "'"
}
function unescapeHTML(s) {
	var s2 = s;
	for(var k in unescapeHTMLMap) {
		s = s.replaceAll(k, unescapeHTMLMap[k]);
	}
	return s;
}

/*
 * fancybox 호출용 공통함수
 *	1) openFancybox(url)
 *		해당 url을 호출
 *	2) openFancybox({"url":url, "data":data})
 * 		url : 호출할 url
 * 		data : 전송할 data
 */
function openFancybox(option) {
	if(typeof option == "string") {
		option = {"url" : option}
	}
	if(isEmpty(option.data)) {
		option.data = {};
	}
	if($("[name=_csrf]").length > 0) {
		option.data["_csrf"] = getCsrf();
	}
	option.data["layout"] = "simple";

	$(":focus").blur();

	$.ajax({
		url : option.url,
		method : "POST",
		cache : false,
		dataType : "html",
		data : option.data
	}).done(function(html) {
		$.fancybox(html, {
			"transitionIn"	: "elastic",
			"transitionOut"	: "elastic",	// or "fade"
			"speedIn"	: 100,
			"speedOut"	: 100
		});
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

// 첨부파일 화면컨트롤
function fn_egov_check_file(flag) {
	if(flag == "Y") {
		$(".file_upload_posbl").show();
		$(".file_upload_imposbl").hide();
	} else {
		$(".file_upload_posbl").hide();
		$(".file_upload_imposbl").show();
	}
}

// 로딩 이미지 표시/숨김
function showLoading(flag) {
	if(flag) {
		$(".loadingPercentDiv").hide();
		$(".loadingImgDiv").css({"top" : parseInt($(window).height()/2 + $(window).scrollTop(), 10) + "px"});
		$(".loadingDiv").height($("body").prop("scrollHeight")).show();
		$(":focus").blur();
	} else {
		$(".loadingDiv").hide();
	}
}

// 성과조직도 조회
function reloadScDeptList(year) {
	sendAjax({
		"url" : contextPath + "/common/scDeptList_json.do",
		"data" : {
			"findYear"	: isEmpty(year) ? getFindValue("findYear") : year,
			"_csrf"		: getCsrf()
		},
		"doneCallbackFunc"	: "makeScDeptList"
	});
}

// 성과조직도 생성
function makeScDeptList(data) {

	var $list = $("#scDeptListOrg > ul").empty();
	var $scDept;

	gMonitoringRootScDeptId = data.monitoringRootScDeptId;

	// 성과조직 정보 배열
	gScDeptIdArr = [];
	gScDeptNmArr = [];
	gScDeptFullNmArr = [];

	$(data.scDeptList).each(function(i, e) {
		gScDeptIdArr.push(e.scDeptId);
		gScDeptNmArr.push(e.scDeptNm);
		gScDeptFullNmArr.push(e.scDeptFullNm);

		$scDept = $("<li class='scDept_" + e.scDeptId + "'>").data("scDeptId", e.scDeptId);
		// root
		if(isEmpty(e.upScDeptId)) {
			$scDept.addClass("scDeptRoot").appendTo($list);
		} else {
			$scDept.appendTo($list.find(".upScDept_" + e.upScDeptId));
			$list.find(".upScDept_" + e.upScDeptId).parent("li").addClass("hasChild");
		}
		$scDept.append("<a href='#' onclick='return false;'><span data-sc_dept_id='" + e.scDeptId + "'>" + e.scDeptNm + "</span></a>");
		if(e.isLeaf == "N") {
			$scDept.append("<ul class='upScDept_" + e.scDeptId + "'>");
		}
	});

	// 모니터링 성과조직
	gMonitoringScDeptIdArr.push(gMonitoringRootScDeptId);
	$("#scDeptListOrg .scDept_" + gMonitoringRootScDeptId).find("li").each(function(i,e){
		gMonitoringScDeptIdArr.push($(e).data("scDeptId"));
	});

	// 모니터링 메뉴인 경우 권한에 맞는 조직도 생성
	$("#scDeptList").empty().append($("#scDeptListOrg > ul").clone());

	if(gIsMonitoringMenu && !$("#scDeptList .scDept_" + gMonitoringRootScDeptId).hasClass("scDeptRoot")) {
		$("#scDeptList .scDept_" + gMonitoringRootScDeptId).appendTo($("#scDeptList .scDeptRoot").parent());
		$("#scDeptList .scDeptRoot").remove();
		$("#scDeptList .scDept_" + gMonitoringRootScDeptId).addClass("scDeptRoot");
	}
	$("#scDeptList .scDeptRoot").data("scDeptId", $("#scDeptListOrg .scDept_" + gMonitoringRootScDeptId).data("scDeptId"));

	// 최초 로딩시 조직도 전체 펼침
	$("#scDeptList").find("a").addClass("active");

	// 검색조건의 성과조직도 생성
	makeFindScDeptList(FIND_SC_DEPT_LIST_ID, FIND_SC_DEPT_ID);

	// 조직도 생성
	$list = $("#deptList > ul").empty();
	var $dept;
	// 조직 정보 배열
	gDeptIdArr = [];
	gDeptNmArr = [];
	gDeptFullNmArr = [];

	$(data.deptList).each(function(i, e) {
		gDeptIdArr.push(e.deptId);
		gDeptNmArr.push(e.deptNm);
		gDeptFullNmArr.push(e.deptNm);

		$dept = $("<li class='dept_" + e.deptId + "'>");

		// root
		if(isEmpty(e.upDeptId)) {
			$dept.addClass("deptRoot").data("deptId", e.deptId).appendTo($list);
		} else {
			$dept.appendTo($list.find(".upDept_" + e.upDeptId));
			$list.find(".upDept_" + e.upDeptId).parent("li").addClass("hasChild");
		}
		$dept.append("<a href='#' onclick='return false;'><span data-sc_dept_id='" + e.deptId + "'>" + e.deptNm + "</span></a>");
		if(e.isLeaf == "N") {
			$dept.append("<ul class='upDept_" + e.deptId + "'>");
		}
	});

	// 최초 로딩시 조직도 전체 펼침
	$list.find("a").addClass("active");
	// 검색조건의 조직도 생성
	makeFindDeptList(FIND_DEPT_LIST_ID, FIND_DEPT_ID);
}

var acScDeptIdx = -1;	// 자동완성 선택 index
/*
 * 검색조건의 성과조직도 생성
 * - id : 검색조건의 성과조직도 div의 ID
 * - findScDeptId : 검색한 scDeptId를 세팅할 ID
 */
function makeFindScDeptList(id, findScDeptId) {
	if(isEmpty(id)) {
		id = FIND_SC_DEPT_LIST_ID;
	}
	if(isEmpty(findScDeptId)) {
		findScDeptId = FIND_SC_DEPT_ID;
	}

	if($("#" + id).length == 0) return;

	var $div = $("#" + id).empty().data("target", findScDeptId).append("<ul>");
	var $ul = $div.children("ul");
	$ul.append("<li class='liScDeptNm'><p></p></li>");
	$ul.append("<li class='liScDeptList'><div class='scDeptList oga-bx'><ul></ul></div></li>");
	$ul.append("<li class='liAc'><input type='text' class='acInput'/></li>");
	$ul.append("<li class='liAcList'><div class='acList'><ul></ul></div></li>");

	$ul.find(".scDeptList > ul").remove();
	$ul.find(".scDeptList").append($("#scDeptList > ul").clone());

	// 전체 조회 사용시
	if($div.hasClass("useFindAll")) {
		var $li = $("<li class='scDept_all'>").data("scDeptId", "");
		$li.append("<a href='#' onclick='return false;'><span data-sc_dept_id=''>" + getMessage("word.all") + "</span></a>");
		$ul.find(".scDeptList > ul").prepend($li);
	}

	if(id == FIND_SC_DEPT_LIST_ID) {
		setScDept(getFindValue("findScDeptId"));
	}

	// 성과조직 검색조건 자동완성 검색 기능 추가
	makeAcScDept($div);
}

/*
 * 검색조건의 조직도 생성
 * - id : 검색조건의 조직도 div의 ID
 * - findDeptId : 검색한 deptId를 세팅할 ID
 */
function makeFindDeptList(id, findDeptId) {
	if(isEmpty(id)) {
		id = FIND_DEPT_LIST_ID;
	}
	if(isEmpty(findDeptId)) {
		findDeptId = FIND_DEPT_ID;
	}

	if($("#" + id).length == 0) return;

	var $div = $("#" + id).empty().data("target", findDeptId).append("<ul>");
	var $ul = $div.children("ul");
	$ul.append("<li class='liScDeptNm'><p></p></li>");
	$ul.append("<li class='liScDeptList'><div class='scDeptList oga-bx'><ul></ul></div></li>");
	$ul.append("<li class='liAc'><input type='text' class='acInput'/></li>");
	$ul.append("<li class='liAcList'><div class='acList'><ul></ul></div></li>");

	var $list = $div.find(".scDeptList > ul").remove();
	var $deptList = $("#deptList > ul").clone();
	$deptList.removeAttr("id").removeClass("hide");
	$ul.find(".scDeptList").append($deptList);

	// 전체 조회 사용시
	if($div.hasClass("useFindAll")) {
		var $li = $("<li class='scDept_all'>").data("deptId", "");
		$li.append("<a href='#' onclick='return false;'><span data-sc_dept_id=''>" + getMessage("word.all") + "</span></a>");
		$ul.find(".scDeptList > ul").prepend($li);
	}

	// 최초 로딩시 조직도 전체 펼침
	$list.find("a").addClass("active");

	showScDeptList(false);

	if(id == FIND_DEPT_LIST_ID) {
		setFindDeptNm(nvl(getFindValue("findDeptId"), gDeptIdArr[0]), id);
	}

	// 조직도 검색조건 자동완성 검색 기능 추가
	makeAcScDept($div);
}

// 성과조직 검색조건 자동완성 검색 기능 추가
var isScDeptListOnFocus = false;
function makeAcScDept($div) {
	$div.find(".acInput").on("click", function(e) {
		e.stopPropagation();
	}).on("blur", function(e) {
		isScDeptListOnFocus = false;
		if(isScDeptListVisible) {
			setTimeout(function() {
				if(!isScDeptListOnFocus) {
					showScDeptList(false);
				}
			}, 300);
		}
	}).on("keydown", function(e) {
		switch(e.keyCode) {
			case 38:	// up
				acScDeptIdx--;
				selectAcScDept($div);
				return false;
			case 40:	// down
				acScDeptIdx++;
				selectAcScDept($div);
				return false;
		}
	}).on("keyup", function(e) {
		switch(e.keyCode) {
			case 27:	// ESC
				showScDeptList(false);
				return false;
			case 37:	// left
			case 39:	// right
			case 38:	// up
			case 40:	// down
				return false;
			case 13:	// enter
				if(acScDeptIdx > -1) {	// 선택된 것이 있으면
					setScDept($div.find(".acList li").eq(acScDeptIdx).data("sc_dept_id"), $div.attr("id"));
				}
				return false;
		}

		var $result = $div.find(".acList > ul").empty();
		$result.on("click", function(e) {
			e.stopPropagation();
		});
		acScDeptIdx = -1;	// 자동완성 선택 index 초기화

		var fullNmArr = [];
		var idArr = [];
		var keyword = $.trim($(this).val()).toLowerCase();
		if(lengthb(keyword) >= AUTO_COMPLETE_ACT_MIN_BYTE) {
			$div.find(".liAcList").show();
			$div.find(".liScDeptList").hide();

			// 조직도
			if($div.hasClass("forDeptList")) {
				for(var i in gDeptNmArr) {
					if(gDeptNmArr[i].toLowerCase().indexOf(keyword) > -1) {
						fullNmArr.push(gDeptFullNmArr[i]);
						idArr.push(gDeptIdArr[i]);
					}
				}
			} else {	// 성과조직도
				for(var i in gScDeptNmArr) {
					if(gScDeptNmArr[i].toLowerCase().indexOf(keyword) > -1) {
						// 모니터링 메뉴가 아니거나 모니터링 대상 부서인 경우
						if(!gIsMonitoringScDeptTree
							|| (gIsMonitoringScDeptTree && $.inArray(gScDeptIdArr[i], gMonitoringScDeptIdArr) > -1)) {
							fullNmArr.push(gScDeptFullNmArr[i]);
							idArr.push(gScDeptIdArr[i]);
						}
					}
				}
			}

			var nmArr = [], mNm, mIdx;
			$(fullNmArr).each(function(i, e) {
				try {
					// 검색 매칭되는 부분 강조
					nmArr = e.split(">");
					mNm = nmArr.pop();
					mIdx = mNm.toLowerCase().lastIndexOf(keyword);
					nmArr.push(mNm.substring(0, mIdx) + "<span class='acMatchedStr'>" + mNm.substring(mIdx, mIdx+keyword.length) + "</span>" + mNm.substring(mIdx+keyword.length));
					e = nmArr.join(">");
				} catch(e) {}
				$("<li>" + e + "</li>").data("sc_dept_id", idArr[i])
				.on("mouseover", function(evt) {
					$(this).addClass("active2Over");
				}).on("mouseout", function(evt) {
					$(this).removeClass("active2Over");
				}).on("click", function(evt) {
					setScDept(idArr[i], $div.attr("id"));
				}).appendTo($result);
			});

			if(fullNmArr.length == 1) {
				acScDeptIdx = 0;
				selectAcScDept($div);
			}
		} else {
			$div.find(".liAcList").hide();
			$div.find(".liScDeptList").show();
		}
	});
}

// 성과조직 검색조건 자동완성 선택 효과
function selectAcScDept($div) {
	var $acList = $div.find(".acList");
	$acList.find(".active2").removeClass("active2");

	var resultCnt = $acList.find("li").length;
	if(acScDeptIdx < 0) {
		acScDeptIdx = 0;
	} else if(acScDeptIdx >= resultCnt) {
		acScDeptIdx = resultCnt-1;
	}

	if(resultCnt == 0) {
		acScDeptIdx = -1;
		return false;
	} else {
		var $target = $acList.find("li").eq(acScDeptIdx);
		var $targetUl = $target.parent();
		$target.addClass("active2");
		// 스크롤 보정
		$targetUl.scrollTop($target.offset().top + $target.outerHeight() - $targetUl.offset().top + $targetUl.scrollTop() - $targetUl.height());
	}
}

// 성과조직도 보임/숨김
var isScDeptListVisible = false;
function showScDeptList(id, sw) {
	// 모든 성과조직도 검색조건을 숨긴 뒤 sw에 따라 보임 처리
	$(".liScDeptNm").show();
	$(".liAc, .liAcList, .liScDeptList").hide();
	isScDeptListVisible = false;
	if(id == false) {
		return false;
	}

	if(isEmpty(id)) {
		id = ($(".sch-oga").hasClass("forDeptList") ? FIND_DEPT_LIST_ID : FIND_SC_DEPT_LIST_ID);
	}
	if(isEmpty(sw)) {
		sw = !$("#" + id).find(".liScDeptList").is(":visible");
	}

	if(sw) {
		$div = $("#" + id);
		if($div.find(".scDeptList > ul").find("li").length == 0) {
			makeFindScDeptList(id, $div.data("target"));
		}
		$div.find(".liAc, .liScDeptList").show();
		$div.find(".liScDeptNm, .liAcList").hide();
		$div.find(".acInput").val("").focus();

		isScDeptListVisible = true;
	}
}

// 검색조건의 성과조직 세팅
function setScDept(scDeptId, findScDeptListId) {
	if(isEmpty(findScDeptListId)) {
		findScDeptListId = FIND_SC_DEPT_LIST_ID;
	}

	// 조직도 선택시에는 별도로 처리
	if(findScDeptListId == FIND_DEPT_LIST_ID) {
		var $deptList = $("#" + findScDeptListId);
		var findScDeptId = $deptList.data("target");
		$("#" + findScDeptId).val(scDeptId);

		setFindDeptNm(scDeptId, findScDeptListId);

		showScDeptList(false);
		return false;
	}

	var $list;

	var findScDeptId = $("#" + findScDeptListId).data("target");
	if(scDeptId == "" && $("#" + findScDeptListId).hasClass("useFindAll")) {
		$("#" + findScDeptId).val("");
		setFindScDeptNm(scDeptId, findScDeptListId);

		if(findScDeptListId == FIND_SC_DEPT_LIST_ID) {
			setFindValue("findScDeptId", "");
			$list = $("#" + findScDeptListId);
			$list.find(".active2").removeClass("active2");
			$list.find(".scDept_all > a").addClass("active2");
		}
	} else {
		$list = $("#scDeptList > ul");

		// scDeptId가 존재하는지 확인, 없으면 최상위 성과조직의 scDeptId를 세팅
		if($list.find(".scDept_" + scDeptId).length == 0) {
			scDeptId = removeNull($list.find(".scDeptRoot").data("scDeptId"));
		}

		// 검색조건에 세팅
		$("#" + findScDeptId).val(scDeptId);
		setFindScDeptNm(scDeptId, findScDeptListId);

		if(findScDeptListId == FIND_SC_DEPT_LIST_ID) {
			// 공통 검색조건에 세팅
			setFindValue("findScDeptId", scDeptId);

			// 해당 성과조직의 상위 조직들 펼침
			$list = $("#scDeptList > ul, #" + FIND_SC_DEPT_LIST_ID + " .scDeptList > ul");
			$list.find(".active2").removeClass("active2");
			$list.find(".scDept_" + scDeptId + " > a").addClass("active2");
			$list.find(".scDept_" + scDeptId).parentsUntil("#scDeptList", "li").children("a").addClass("active");
		}
	}

	showScDeptList(false);
}

// 검색조건에 성과조직 Full name 세팅
var tScDeptNmArr = [];
function setFindScDeptNm(scDeptId, findScDeptListId) {
	var scDeptFullNm = getScDeptFullNm(scDeptId);
	var $scDeptNmTarget = $("#" + findScDeptListId).parents("ul:first").find(".liScDeptNm p").text("");

	tScDeptNmArr = scDeptFullNm.split(">");
	if(tScDeptNmArr.length > 0) {
		var currScDeptNm = tScDeptNmArr.pop();
		$scDeptNmTarget.text(tScDeptNmArr.join(">") + (tScDeptNmArr.length > 0 ? ">" : ""));
		$scDeptNmTarget.append("<span>" + currScDeptNm + "</span>");
	}
}

function setFindDeptNm(deptId, findDeptListId) {
	var $list = $("#" + findDeptListId);
	var $deptNmTarget = $list.closest("ul").find(".liScDeptNm p").text("");
	$deptNmTarget.append("<span>" + getDeptNm(deptId) + "</span>");

	$list.find(".active2").removeClass("active2");
	$list.find(".dept_" + deptId + " > a").addClass("active2");

	if(typeof setFindDeptNmCallback != "undefined" && typeof setFindDeptNmCallback == "function") {
		setFindDeptNmCallback.apply(this);
	}
}

// 조직ID로 조직명 가져오기
function getDeptNm(deptId) {
	if(isEmpty(deptId)) return getMessage("word.all");
	return removeNull(gDeptNmArr[$.inArray(deptId, gDeptIdArr)]);
}

// 성과조직ID로 성과조직명 Full name 가져오기
function getScDeptFullNm(scDeptId) {
	if(scDeptId == "") {
		return getMessage("word.all");
	}

	var idx = $.inArray(scDeptId, gScDeptIdArr);
	if(idx != -1) {
		return gScDeptFullNmArr[idx];
	} else {
		return "";
	}
}

// 성과조직ID로 성과조직명 가져오기
function getScDeptNm(scDeptId) {
	var idx = $.inArray(scDeptId, gScDeptIdArr);
	if(idx != -1) {
		return gScDeptNmArr[idx];
	} else {
		return "";
	}
}

// 최상위 성과조직 ID 조회
function getScDeptRootId() {
	return removeNull($("#scDeptList > ul .scDeptRoot").data("scDeptId"));
}

// 즐겨찾기 조회
function reloadBookmark() {
	sendAjax({
		"url" : contextPath + "/common/bookmarkList.do",
		"data" : {
			"_csrf"		: getCsrf()
		},
		"doneCallbackFunc"	: "makeBookmark"
	});
}

// 즐겨찾기 생성
function makeBookmark(data) {
	bookmarks = [];
	$("#bookmark > ul").remove();

	var prefix = "bookmark_";
	var $upNode, $ul, $li;
	$(data.list).each(function(i, e) {
		if(e.levelId == "1") {
			return true;
		} else if(e.isLeaf == 1) {
			bookmarks.push(e.pgmId);
		}

		$upNode = $("#" + prefix + e.upPgmId);
		if($upNode.length == 0) $upNode = $("#bookmark");

		$ul = $upNode.children("ul");
		if($ul.length == 0) $ul = $("<ul>").addClass("active").appendTo($upNode);
		$li = $("<li id='" + prefix + e.pgmId + "'>").appendTo($ul);
		if(e.isLeaf == 1) {
			$li.append("<span><input type='checkbox' name='keys' value='" + e.pgmId + "'/> <a href='" + contextPath + "/main.do#" + e.pgmId + "'>" + e.pgmNm + "</a></span>");
		} else {
			$li.append("<a href='" + contextPath + "' onclick='return false;'><span>" + e.pgmNm + "</span></a>");
		}
	});

	checkBookmarkIcon();
	showBookmarkMngBtns = true;
	manageBookmark();
}

// 즐겨찾기 아이콘 표시
function checkBookmarkIcon() {
	if($.inArray(currPgmId, bookmarks) > -1) {
		$("#bookmarkIcon").addClass("active");
	} else {
		$("#bookmarkIcon").removeClass("active");
	}
}

// 즐겨찾기 추가/해제
function addToBookmark() {
	sendAjax({
		"url" : contextPath + ($.inArray(currPgmId, bookmarks) == -1 ? "/common/insertBookmark.do" : "/common/deleteBookmark.do"),
		"data" : {
			"pgmId"	: currPgmId,
			"keys"	: currPgmId,
			"_csrf"		: getCsrf()
		},
		"doneCallbackFunc"	: "addToBookmarkCallback"
	});
}

function addToBookmarkCallback(data) {
	if(data.result == AJAX_SUCCESS) {
		reloadBookmark();
	} else {
		$.showMsgBox(data.msg);
	}
}

// 즐겨찾기 관리
var showBookmarkMngBtns = false;
function manageBookmark() {
	if(!showBookmarkMngBtns) {
		$("#bookmark").addClass("showEdit");
	} else {
		$("#bookmark").removeClass("showEdit");
		$("#bookmark :checkbox").prop("checked", false);
	}
	showBookmarkMngBtns = !showBookmarkMngBtns;
}

// 즐겨찾기 일괄 삭제
function deleteAllBookmarks() {
	$("#bookmark :checkbox").prop("checked", true);
	deleteBookmark();
}

// 즐겨찾기 선택 삭제
function deleteBookmark() {
	if($("#bookmark :checkbox:checked").length == 0) {
		$.showMsgBox(getMessage("errors.noDataToDelete"));
		return false;
	}
	$.showConfirmBox(getMessage("common.confirm.msg", getMessage("word.delete")), "deleteBookmarkProcess");
}

function deleteBookmarkProcess() {
	sendAjax({
		"url" : contextPath + "/common/deleteBookmark.do",
		"data" : getFormData("bookmarkForm"),
		"doneCallbackFunc"	: "addToBookmarkCallback"
	});
}

//성과조직도 조회
function reloadProcessList(year) {
	sendAjax({
		"url" : contextPath + "/common/processList_json.do",
		"async" : false,
		"data" : {
			"findYear"	: isEmpty(year) ? getFindValue("findYear") : year,
			"_csrf"		: getCsrf()
		},
		"doneCallbackFunc"	: "makeProcess"
	});
}

/*
 * 프로세스 흐름도 생성
 * */
function makeProcess(data){

	var processObj = data.processList;

	if(!isEmpty(processObj) && $(processObj).size() > 0){

		/*구분자*/
		var serviceId = "";
		var serviceSubId = "";
		var subCnt = 0;
		var count = 1;
		var rowIdx = 1;

		var $mainDiv = $("#allProcess").empty();
		/*i는 0부터 시작*/
		processObj.forEach(function(e,i){
			/*service div*/
			if(serviceId != e.serviceId){
				$mainDiv.append("<div class='fwtable-tbl'><table><tbody><tr><th scope='rowgroup'>"+e.serviceNm+"</th><td id=prc_"+e.serviceId+"></td></tr></tbody></table></div>");
				subCnt = 0;
			}

			if(subCnt == 0 || serviceSubId != e.serviceSubId){

				if(subCnt != 0 && serviceSubId != e.serviceSubId){
					$("#prc_"+e.serviceId).append("<div class='arrow-down-empty'></div>");
				}

				subCnt = e.subCnt;
				rowIdx = 1;
				$("#prc_"+e.serviceId).append("<div class='list' id='item_"+e.serviceId+"_"+e.serviceSubId+"_"+rowIdx+"'></div>");
				count = 1;
			}else if(count != 1 && count%5 == 1){

				$("#prc_"+e.serviceId).append("<div class='arrow-down'></div>");

				rowIdx++;
				if(rowIdx%2 == 0){
					$("#prc_"+e.serviceId).append("<div class='list' id='item_"+e.serviceId+"_"+e.serviceSubId+"_"+rowIdx+"'><div id='temp_"+e.serviceId+"_"+e.serviceSubId+"_"+rowIdx+"' class='display:none'></div></div>");
				}else{
					$("#prc_"+e.serviceId).append("<div class='list' id='item_"+e.serviceId+"_"+e.serviceSubId+"_"+rowIdx+"'></div>");
				}

				if(rowIdx%2 == 0){
					if(0 < (rowIdx*5)-subCnt ){
						var emptyBoxCnt = (rowIdx*5)-subCnt;
						while(emptyBoxCnt > 0){
							$("<div class='bg-empty'></div>").insertBefore("#temp_"+e.serviceId+"_"+e.serviceSubId+"_"+rowIdx);
							$("<div class='arrow-empty'></div>").insertBefore("#temp_"+e.serviceId+"_"+e.serviceSubId+"_"+rowIdx);
							emptyBoxCnt--;
						}
					}
				}
			}

			if(rowIdx%2 == 1){
				if(e.endYn == "Y"){
					$("#item_"+e.serviceId+"_"+e.serviceSubId+"_"+rowIdx).append("<div class='cancel-bg'><div  style='cursor:pointer;' class='cancel-icn'>"+getMessage("word.complete")+"<br/>"+getMessage("word.cancel")+"</div><p class='txt' style='cursor:pointer;' id='processJob_"+e.serviceId+"_"+e.serviceSubId+"_"+count+"_"+e.pgmId+"'>"+e.processNm+"</p></div>");
				}else{
					$("#item_"+e.serviceId+"_"+e.serviceSubId+"_"+rowIdx).append("<div class='complete-bg'><div  style='cursor:pointer;' class='complete-icn'>"+getMessage("word.complete")+"</div><p class='txt' style='cursor:pointer;' id='processJob_"+e.serviceId+"_"+e.serviceSubId+"_"+count+"_"+e.pgmId+"'>"+e.processNm+"</p></div>");
				}
			}else{
				if(e.endYn == "Y"){
					$("<div class='cancel-bg'><div style='cursor:pointer;' class='cancel-icn'>"+getMessage("word.complete")+"<br/>"+getMessage("word.cancel")+"</div><p class='txt' style='cursor:pointer;' id='processJob_"+e.serviceId+"_"+e.serviceSubId+"_"+count+"_"+e.pgmId+"'>"+e.processNm+"</p></div>").insertAfter("#temp_"+e.serviceId+"_"+e.serviceSubId+"_"+rowIdx);
				}else{
					$("<div class='complete-bg'><div style='cursor:pointer;' class='complete-icn'>"+getMessage("word.complete")+"</div><p class='txt' style='cursor:pointer;' id='processJob_"+e.serviceId+"_"+e.serviceSubId+"_"+count+"_"+e.pgmId+"'>"+e.processNm+"</p></div>").insertAfter("#temp_"+e.serviceId+"_"+e.serviceSubId+"_"+rowIdx);
				}
			}

			if(rowIdx%2 == 1 && !(count%5 == 0 || count == subCnt)){
				$("#item_"+e.serviceId+"_"+e.serviceSubId+"_"+rowIdx).append("<div class='arrow-right'></div>");
			}else if(rowIdx%2 == 0 && !(count%5 == 0 || count == subCnt)){
				$("<div class='arrow-left'></div>").insertAfter("#temp_"+e.serviceId+"_"+e.serviceSubId+"_"+rowIdx);
			}

			count++;
			serviceSubId = e.serviceSubId;
			serviceId = e.serviceId;
		});
	}
}

/*
 * 성과조직 팝업
 * - targetScDeptId : 성과조직 선택시 성과조직ID를 세팅할 대상의 ID
 * - targetScDeptNm : 성과조직 선택시 성과조직명을 세팅할 대상의 ID (생략가능)
 * - targetScDeptFullNm : 성과조직 선택시 성과조직 Full name을 세팅할 대상의 ID (생략가능)
 * - callbackFunc : 성과조직 선택 후 저장 버튼 클릭시 실행할 함수명 (생략 가능)
 */
function popScDeptList(targetScDeptId, targetScDeptNm, targetScDeptFullNm, callbackFunc) {

	var data = {
		"findYear" : $("#findYear").val(),
		"targetScDeptId" : targetScDeptId
	}
	if(isNotEmpty(targetScDeptNm)) {
		data["targetScDeptNm"] = targetScDeptNm;
	}
	if(isNotEmpty(targetScDeptFullNm)) {
		data["targetScDeptFullNm"] = targetScDeptFullNm;
	}
	if(isNotEmpty(callbackFunc)) {
		data["callbackFunc"] = callbackFunc;
	}
	openFancybox({
		"url" : contextPath + "/common/popScDeptList.do",
		"data" : data
	});
}

/*
 * 실조직 팝업
 * - targetDeptId : 성과조직 선택시 성과조직ID를 세팅할 대상의 ID
 * - targetDeptNm : 성과조직 선택시 성과조직명을 세팅할 대상의 ID (생략가능)
 * - targetDeptFullNm : 성과조직 선택시 성과조직 Full name을 세팅할 대상의 ID (생략가능)
 * - callbackFunc : 성과조직 선택 후 저장 버튼 클릭시 실행할 함수명 (생략 가능)
 */
function popDeptList(targetDeptId, targetDeptNm, targetDeptFullNm, callbackFunc) {

	var data = {
		"findYear" : $("#findYear").val(),
		"targetDeptId" : targetDeptId
	}
	if(isNotEmpty(targetDeptNm)) {
		data["targetDeptNm"] = targetDeptNm;
	}
	if(isNotEmpty(targetDeptFullNm)) {
		data["targetDeptFullNm"] = targetDeptFullNm;
	}
	if(isNotEmpty(callbackFunc)) {
		data["callbackFunc"] = callbackFunc;
	}
	openFancybox({
		"url" : contextPath + "/common/popDeptList.do",
		"data" : data
	});
}

function popScDeptListForGrid(gridCallYn, targetGridId, targetRowId, targetScDeptId, targetScDeptNm, targetScDeptFullNm, callbackFunc) {
	var data = {
		"findYear" : $("#findYear").val(),
		"gridCallYn" : gridCallYn,
		"targetGridId" : targetGridId,
		"targetRowId" : targetRowId,
		"targetScDeptId" : targetScDeptId
	}
	if(isNotEmpty(targetScDeptNm)) {
		data["targetScDeptNm"] = targetScDeptNm;
	}
	if(isNotEmpty(targetScDeptFullNm)) {
		data["targetScDeptFullNm"] = targetScDeptFullNm;
	}
	if(isNotEmpty(callbackFunc)) {
		data["callbackFunc"] = callbackFunc;
	}
	openFancybox({
		"url" : contextPath + "/common/popScDeptList.do",
		"data" : data
	});
}

/*
 * 성과조직 팝업
 * - targetScDeptId : 성과조직 선택시 성과조직ID를 세팅할 대상의 ID
 * - targetScDeptNm : 성과조직 선택시 성과조직명을 세팅할 대상의 ID (생략가능)
 * - targetScDeptFullNm : 성과조직 선택시 성과조직 Full name을 세팅할 대상의 ID (생략가능)
 * - callbackFunc : 성과조직 선택 후 저장 버튼 클릭시 실행할 함수명 (생략 가능)
 */
function popDeptListForGrid(gridCallYn, targetGridId, targetRowId, targetDeptId, targetDeptNm, targetDeptFullNm, callbackFunc) {

	var data = {
		"findYear" : $("#findYear").val(),
		"gridCallYn" : gridCallYn,
		"targetGridId" : targetGridId,
		"targetRowId" : targetRowId,
		"targetDeptId" : targetDeptId
	}
	if(isNotEmpty(targetDeptNm)) {
		data["targetDeptNm"] = targetDeptNm;
	}
	if(isNotEmpty(targetDeptFullNm)) {
		data["targetDeptFullNm"] = targetDeptFullNm;
	}
	if(isNotEmpty(callbackFunc)) {
		data["callbackFunc"] = callbackFunc;
	}
	openFancybox({
		"url" : contextPath + "/common/popDeptList.do",
		"data" : data
	});
}

/*
 * 사용자 조회 팝업
 * - targetUserId : 사용자 선택시 사용자ID를 세팅할 대상의 ID
 * - targetUserNm : 사용자 선택시 사용자명을 세팅할 대상의 ID
 */
function popSearchUser(targetUserId, targetUserNm) {
	openFancybox({
		"url" : contextPath + "/common/popSearchUser.do",
		"data" : {
			"findYear" : removeNull($("#findYear").eq(0).val()),
			"targetUserId" : targetUserId,
			"targetUserNm" : targetUserNm
		}
	});
}

/*
 * 사용자 조회 팝업 (jqgrid에 값 세팅)
 * - targetGridId : 사용자 선택시 사용자 정보를 세팅할 grid의 ID
 * - targetRowId : 사용자 선택시 사용자 정보를 세팅할 row의 ID
 * - targetUserId : 사용자 선택시 사용자ID를 세팅할 column의 name
 * - targetUserNm : 사용자 선택시 사용자명을 세팅할 column의 name
 */
function popSearchUserForGrid(targetGridId, targetRowId, targetUserId, targetUserNm) {
	var data = {
		"gridCall" : "Y",
		"findYear" : removeNull($("#findYear").eq(0).val()),
		"targetGridId" : targetGridId,
		"targetRowId" : targetRowId,
		"targetUserId" : targetUserId,
		"targetUserNm" : targetUserNm
	};

	openFancybox({
		"url" : contextPath + "/common/popSearchUser.do",
		"data" : data
	});
}

/*
 * 사용자 정보 조회 팝업 (jqgrid에 값 세팅)
 * - targetGridId : 사용자 선택시 사용자 정보를 세팅할 grid의 ID
 * - targetRowId : 사용자 선택시 사용자 정보를 세팅할 row의 ID
 * - targetData : 사용자 선택시 사용자 정보를 세팅할 target 정보들
 */
function popSearchUserInfoForGrid(targetGridId, targetRowId, targetData) {
	var data = {
			"gridCall" : "Y",
			"findYear" : removeNull($("#findYear").eq(0).val()),
			"targetGridId" : targetGridId,
			"targetRowId" : targetRowId,
			"targetUserId" : targetData.targetUserId,
			"targetUserNm" : targetData.targetUserNm
	};
	if(isNotEmpty(targetData.targetDeptId)) {
		data["targetDeptId"] = targetData.targetDeptId;
	}
	if(isNotEmpty(targetData.targetDeptNm)) {
		data["targetDeptNm"] = targetData.targetDeptNm;
	}
	if(isNotEmpty(targetData.targetJikgubId)) {
		data["targetJikgubId"] = targetData.targetJikgubId;
	}
	if(isNotEmpty(targetData.targetPosId)) {
		data["targetPosId"] = targetData.targetPosId;
	}
	if(isNotEmpty(targetData.targetJobId)) {
		data["targetJobId"] = targetData.targetJobId;
	}
	if(isNotEmpty(targetData.callback)) {
		data["callback"] = targetData.callback;
	}

	openFancybox({
		"url" : contextPath + "/common/popSearchUser.do",
		"data" : data
	});
}

//////////// jqGrid 관련 ////////////
// 공통 설정
$.jgrid.defaults = $.extend($.jgrid.defaults, {
	mtype		: "POST",
	autowidth	: true,
	datatype	: "json",
	viewrecords	: false,
	loadonce	: true,
	gridview	: true,
	//autoencode	: true,	// 20171214 kimyh autoencode:true 일 경우 cell 수정시 특수문자가 태그로 변환되는 문제가 있어서 사용하지 않음
	//shrinkToFit	: false,	// false일 경우 각 열의 너비가 지정한 픽셀로 적용됨
	editurl		: "clientArray",
	cellsubmit	: "clientArray",
	gridComplete : function() {
		// 조회된 내용이 없을 경우의 처리
		if($(this).jqGrid("getGridParam", "records") == 0) {
			$(this).find("tbody").append("<tr><td class='noGridResult' colspan='" + $(this).find("tr.jqgfirstrow").find("td").length + "'>" + getMessage("errors.noResult") + "<td></tr>");
		}

		// jqgrid 열 리사이즈 선 위치 보정
		$(".ui-jqgrid-resize-mark").css("margin-left", "-287px");

		gridResize($(this).attr("id"));
	},
	onInitGrid : function() {
		// editable column 헤더에 class 적용
		if($(this).jqGrid("getGridParam", "cellEdit")) {
			var gridId = $(this).attr("id");
			var $gridHeader = $("#gbox_" + gridId).find(".ui-jqgrid-htable");
			var colModel = $(this).jqGrid("getGridParam", "colModel");
			$(colModel).each(function(i, e) {
				if(!isUndefined(e.editable) && e.editable) {
					$gridHeader.find("#" + gridId + "_" + e.name).addClass("editableColumn");
				}
			});
		}
	}
});

function makeSearchObj(selectId){

	var obj = this;

	obj.selectId = selectId;
	obj.selectObj = document.getElementById(obj.selectId);
	obj.pNode = document.getElementById(obj.selectId).parentNode;
	obj.selectObj.style.display = 'none';

	if(document.getElementById(obj.selectId+"_nm") != undefined){
		document.getElementById(obj.selectId+"_nm").parentNode.removeChild(document.getElementById(obj.selectId+"_nm"));
		document.getElementById(obj.selectId+"_childDiv").parentNode.removeChild(document.getElementById(obj.selectId+"_childDiv"));
	}

	//바탕 DIV
	obj.backDiv = document.createElement("DIV");
	obj.backDiv.id = "customSelector2";
	obj.backDiv.className = "customSelector";
	obj.pNode.insertBefore(obj.backDiv,obj.selectObj);

	//SELECTED TEXT DIV
	obj.mainDiv = document.createElement("DIV");
	obj.mainDiv.id = selectId+"_nm";
	obj.mainDiv.className = "customSelectorSelectedTxt";
	if(obj.selectObj.options.length > 0){
		obj.mainDiv.innerHTML = obj.selectObj.options[obj.selectObj.selectedIndex].text;
	}
	obj.backDiv.insertBefore(obj.mainDiv, obj.mainDiv.nextSibling);
	//obj.backDiv.innerHTML=obj.mainDiv;

	obj.childDiv = document.createElement("DIV");
	obj.childDiv.id=obj.selectId+"_childDiv";
	obj.childDiv.className = "customSelectorLists";

	obj.childDiv.style.width = obj.mainDiv.offsetWidth+"px";
	obj.childDiv.style.height = "200px";

	/*
	console.log("obj.mainDiv.offsetLeft",obj.mainDiv.offsetLeft);
	console.log("obj.mainDiv.offsetTop",obj.mainDiv.offsetTop);
	console.log("obj.mainDiv.offsetLeft",$(obj.mainDiv).position().left);
	console.log("obj.mainDiv.offsetTop",$(obj.mainDiv).position().top);
	console.log("obj.mainDiv.offsetLeft",$(obj.mainDiv).offset().left);
	console.log("obj.mainDiv.offsetTop",$(obj.mainDiv).offset().top);
	*/

	//obj.childDiv.style.left = eval($(obj.mainDiv).position().left-1)+"px";
	//obj.childDiv.style.top = eval($(obj.mainDiv).position().top+24)+"px";

	obj.childDiv.style.position="absolute";
	obj.childDiv.style.zIndex="10";
	obj.childDiv.style.display="none";
	obj.childDiv.style.backgroundColor="white";
	obj.childDiv.innerHTML="<input type='text' id='"+obj.selectId+"searchNm' placeholder='"+getMessage("common.search.word")+"'/>"+"<ul id='"+obj.selectId+"optionList'><ul>";

	obj.backDiv.insertBefore(obj.childDiv, obj.mainDiv.nextSibling);

	//obj.backDiv.insertAfter(obj.childDiv,obj.mainDiv);
	document.getElementById(obj.selectId+"searchNm").style.width=obj.mainDiv.offsetWidth+"px";

	obj.datas = obj.selectObj.options;
	obj.nms = [];
	obj.ids = [];
	obj.indexes = [];

	for(var i=0 ; i<obj.datas.length ; i++){
		obj.nms.push(obj.datas.item(i).text);
		obj.ids.push(obj.datas.item(i).value);
		obj.indexes.push(i);
	}

	obj.mainDiv.addEventListener("click", function(e){

		if(obj.childDiv.style.display == "none"){

			/*
			console.log("obj.mainDiv.offsetLeft",obj.mainDiv.offsetLeft);
			console.log("obj.mainDiv.offsetTop",obj.mainDiv.offsetTop);
			console.log("obj.mainDiv.offsetLeft",$(obj.mainDiv).position().left);
			console.log("obj.mainDiv.offsetTop",$(obj.mainDiv).position().top);
			console.log("obj.mainDiv.offsetLeft",$(obj.mainDiv).offset().left);
			console.log("obj.mainDiv.offsetTop",$(obj.mainDiv).offset().top);
			*/

			setTimeout(function(){
				obj.childDiv.style.left = eval($(obj.mainDiv).position().left-1)+"px";
				obj.childDiv.style.top = eval($(obj.mainDiv).position().top+24)+"px";

				obj.childDiv.style.display="block";
				document.getElementById(obj.selectId+"searchNm").value="";
				obj.makeOptions();
			}, 100);

		}else{
			obj.childDiv.style.display="none";
		}

	},false);


	document.addEventListener("click", function(e){
		//console.log(e.target);
		//console.log(obj.childDiv.style.display);
		//e.preventDefault();
		//e.stopPropagation();

		//obj.childDiv.style.display="none";

		if(obj.childDiv.style.display != "none"){
			if(e.target.id != obj.selectId+"_childDiv" && e.target.id != obj.selectId+"optionList" && e.target.id != obj.selectId+"searchNm" && e.target.id != obj.selectId+"_nm"){
				obj.childDiv.style.display="none";
			}
		}

		e.stopPropagation();
		//this.childDiv.style.display="none";
	},false);

	document.addEventListener("focusin", function(e){
		//console.log(e.target);
		//console.log(obj.childDiv.style.display);
		//e.preventDefault();
		//e.stopPropagation();

		//obj.childDiv.style.display="none";

		if(obj.childDiv.style.display != "none"){
			if(e.target.id != obj.selectId+"_childDiv" && e.target.id != obj.selectId+"optionList" && e.target.id != obj.selectId+"searchNm" && e.target.id != obj.selectId+"_nm"){
				obj.childDiv.style.display="none";
			}
		}

		e.stopPropagation();
		//this.childDiv.style.display="none";
	},false);


	document.getElementById(obj.selectId+"searchNm").addEventListener("blur", function(e){
		e.preventDefault();
		e.stopPropagation();
	},false);

	document.getElementById(obj.selectId+"searchNm").addEventListener("keyup",function(e){
		obj.makeOptions();
	},false);

	obj.makeOptions = function(){
		var selectText = "";
		var selectIds = [];

		for(var i=0 ; i<obj.datas.length ; i++){
			if(obj.datas.item(i).text.indexOf(document.getElementById(obj.selectId+"searchNm").value) > -1){
				selectText +="<li id='"+obj.selectId+"optionList_"+obj.indexes[i]+"''>"+obj.nms[i]+"</li>";
				selectIds.push(obj.indexes[i]);
			}
		}

		document.getElementById(obj.selectId+"optionList").innerHTML="";
		document.getElementById(obj.selectId+"optionList").innerHTML=selectText;

		selectIds.forEach(function(el){
			document.getElementById(obj.selectId+"optionList_"+el).addEventListener("click",function(e){
				e.stopPropagation();
				//console.log("e.target",e.target);
				document.getElementById(obj.selectId+"_nm").innerHTML=obj.nms[e.target.id.split("_")[1]];
				obj.selectObj.selectedIndex=e.target.id.split("_")[1]+"";
				$("#"+obj.selectId).change();
				obj.childDiv.style.display="none";
			},false);
		});
	};

	obj.reload = function(){

		obj.datas = obj.selectObj.options;
		obj.nms = [];
		obj.ids = [];
		obj.indexes = [];

		for(var i=0 ; i<obj.datas.length ; i++){
			obj.nms.push(obj.datas.item(i).text);
			obj.ids.push(obj.datas.item(i).value);
			obj.indexes.push(i);
		}

		if(obj.selectObj.options.length > 0){
			obj.mainDiv.innerHTML = obj.selectObj.options[obj.selectObj.selectedIndex].text;
		}else{
			obj.mainDiv.innerHTML = "";
		}
	};
}

// dialog 대신 showMsgBox 사용
$.jgrid.info_dialog = function(caption, content, c_b, modalopt) {
	$.showMsgBox(content);
};

// jqGrid checkbox 선택여부 확인
function gridSelectChk(gridId) {
	var id = $("#" + gridId).getGridParam("selarrrow");
	return (id.length > 0);
};

// 그리드 입력값 바이트 체크
function jqGridChkBytes(value, colname, idx, colModel) {
	if(isNotEmpty(colModel.editoptions) && isNotEmpty(colModel.editoptions.maxlength)
			&& lengthb(value) > colModel.editoptions.maxlength) {
		return [false, getMessage("errors.exceededMaximumChars3", colModel.editoptions.maxlength)];
	}
	return [true, ""];
}

//그리드 입력값 바이트 체크(숫자 소수점 자릿수 포함 체크)
function jqGridChkNumDotBytes(value, colname, idx, colModel) {
	if(-1 < value.indexOf(".")){
		var varr = value.split(".");
		if(isNotEmpty(colModel.editoptions) && isNotEmpty(colModel.editoptions.maxDot)
				&& varr[1].length > colModel.editoptions.maxDot) {
			return [false, getMessage("errors.exceededMaximumDot", colModel.editoptions.maxDot)];
		}
	}

	if(isNotEmpty(colModel.editoptions) && isNotEmpty(colModel.editoptions.maxlength)
			&& value.length > colModel.editoptions.maxlength) {
		return [false, getMessage("errors.exceededMaximumChars3", colModel.editoptions.maxlength)];
	}
	return [true, ""];
}

//그리드 입력값 바이트 체크(숫자 소수점 자릿수 포함 체크 정수:maxlength-1-maxDot, 소수:maxDot 허용)
function jqGridChkNumDotSepBytes(value, colname, idx, colModel) {
	if(-1 < value.indexOf(".")){
		var varr = value.split(".");
		if(isNotEmpty(colModel.editoptions) && isNotEmpty(colModel.editoptions.maxDot)
				&& varr[1].length > colModel.editoptions.maxDot) {
			return [false, getMessage("errors.exceededMaximumDot", colModel.editoptions.maxDot)];
		}
		if(isNotEmpty(colModel.editoptions) && isNotEmpty(colModel.editoptions.maxDot) && isNotEmpty(colModel.editoptions.maxlength)
				&& varr[0].length > Number(colModel.editoptions.maxlength)-1-Number(colModel.editoptions.maxDot)) {
			return [false, getMessage("errors.exceededMaximumDot2", colModel.editoptions.maxlength-1-colModel.editoptions.maxDot, colModel.editoptions.maxDot)];
		}
	}else{
		//정수는 maxlength에서 정수와 '.'을 제외한 길이 체크
		if(isNotEmpty(colModel.editoptions) && isNotEmpty(colModel.editoptions.maxlength)
				&& value.length > Number(colModel.editoptions.maxlength)-1-Number(colModel.editoptions.maxDot)) {
			return [false, getMessage("errors.exceededMaximumChars3", Number(colModel.editoptions.maxlength)-1-Number(colModel.editoptions.maxDot))];
		}
	}

	if(isNotEmpty(colModel.editoptions) && isNotEmpty(colModel.editoptions.maxlength)
			&& value.length > colModel.editoptions.maxlength) {
		return [false, getMessage("errors.exceededMaximumChars3", colModel.editoptions.maxlength)];
	}
	return [true, ""];
}

// 그리드 새로고침
function reloadGrid(gridId, formId, pageNum, json) {
	var $grid = $("#" + gridId);
	if(isEmpty(formId)) {
		formId = $grid.parents("form:first").attr("id");
	}
	if(isEmpty(pageNum)) {
		pageNum = 1;
	}
	$("#" + formId).find("input[name=page]").val(pageNum);

	$grid.jqGrid("setGridParam", {datatype:"json", postData:getFormData(formId)}).trigger("reloadGrid");

	// grid내에서 페이징 처리를 하는 경우, 페이지와 정렬 적용
	if($grid.jqGrid("getGridParam", "loadonce") && pageNum > 1) {
		var sidx = $grid.jqGrid("getGridParam", "sortname");
		var sord = $grid.jqGrid("getGridParam", "sortorder");
		setTimeout(function () {
			$grid.trigger("reloadGrid", [{page:pageNum, sortname:sidx, sortorder:sord}]);
		}, 500);
	}

	if(isNotEmpty(json) && isNotEmpty(json.msg)) {
		$.showMsgBox(json.msg);
	}
}

/*
 * grid의 데이터를 form에 넣음
 * useValidation : 유효성 체크 여부(기본값은 false)
 * 	- true일 경우 유효성 체크 결과를 true/false로 리턴함
 * lengthChkYn : jqgrid 길이 체크 여부(기본값은 true)
 * mapYn : 리스트 내 map으로 파라미터 넘길시 true
 */
function gridToForm(gridId, formId, useValidation, lengthChkYn, mapYn) {
	var $grid = $("#" + gridId);
	if(isEmpty(useValidation)) useValidation = false;
	if(isEmpty(lengthChkYn)) lengthChkYn = true;
	if(isEmpty(mapYn)) mapYn = false;
	var validationMap = {};

	// colModel에서 editoptions와 editrules 정보를 취합해서 validationMap에 넣음
	var colModel = $grid.jqGrid("getGridParam", "colModel");
	for(var i in colModel) {
		validationMap[colModel[i].name] = {};
		if(isNotEmpty(colModel[i].editoptions) && isNotEmpty(colModel[i].editoptions.maxlength)) {
			validationMap[colModel[i].name]["maxlength"] = colModel[i].editoptions.maxlength;
			validationMap[colModel[i].name]["label"] = colModel[i].label;
		}
		if(isNotEmpty(colModel[i].editrules)) {
			validationMap[colModel[i].name]["editrules"] = colModel[i].editrules;
			validationMap[colModel[i].name]["label"] = colModel[i].label;
		}
		if(isNotEmpty(colModel[i].edittype)) {
			validationMap[colModel[i].name]["edittype"] = colModel[i].edittype;
		}

		if($.isEmptyObject(validationMap[colModel[i].name])) {
			delete validationMap[colModel[i].name];
		}
	}

	$("#" + formId).find("div.gridData").remove();
	var $divGridData = $("<div class='gridData'>").appendTo("#" + formId);
	var gridData = $grid.jqGrid("getRowData");

	if(lengthChkYn == true && gridData.length == 0) {
		$.showMsgBox(getMessage("errors.noDataToSave"));
		return false;
	}

	var errCnt = 0;
	var val;
	$(gridData).each(function(i, e) {
		for(var k in e) {
			val = e[k];
			if(useValidation && isNotEmpty(validationMap[k])) {
				// 글자 길이 byte 체크
				if(isNotEmpty(validationMap[k]["maxlength"])
					&& lengthb(val) > validationMap[k]["maxlength"]) {
					errCnt++;
					$.showMsgBox(getMessage("errors.validation.byteLimit4",	validationMap[k]["label"], validationMap[k]["maxlength"], 3));
					return false;
				}

				// editrules 체크
				if(isNotEmpty(validationMap[k]["editrules"])) {
					// required 체크
					if(isNotEmpty(validationMap[k]["editrules"]["required"])
						&& isEmpty(val)) {
						errCnt++;
						$.showMsgBox(getMessage("errors.required", validationMap[k]["label"]));
						return false;
					}
					// integer 체크
					if(isNotEmpty(validationMap[k]["editrules"]["integer"])
							&& isNotEmpty(val)
							&& parseInt(val, 10) != val) {
						errCnt++;
						$.showMsgBox(getMessage("errors.validation.integerOnly", validationMap[k]["label"]));
						return false;
					}
					// number 체크
					if(isNotEmpty(validationMap[k]["editrules"]["number"])
							&& isNotEmpty(val)
							&& isNaN(val)) {
						errCnt++;
						$.showMsgBox(getMessage("errors.validation.numberOnly", validationMap[k]["label"]));
						return false;
					}
					// minValue 체크
					if(isNotEmpty(validationMap[k]["editrules"]["minValue"])
							&& isNotEmpty(val)
							&& (isNaN(val) || val < validationMap[k]["editrules"]["minValue"])) {
						errCnt++;
						$.showMsgBox(getMessage("errors.validation.minValue", validationMap[k]["label"], validationMap[k]["editrules"]["minValue"]));
						return false;
					}
					// maxValue 체크
					if(isNotEmpty(validationMap[k]["editrules"]["maxValue"])
							&& isNotEmpty(val)
							&& (isNaN(val) || val > validationMap[k]["editrules"]["maxValue"])) {
						errCnt++;
						$.showMsgBox(getMessage("errors.validation.maxValue", validationMap[k]["label"], validationMap[k]["editrules"]["maxValue"]));
						return false;
					}
				}
			}

			if(mapYn){
				if(isNotEmpty(validationMap[k]) && isNotEmpty(validationMap[k]["edittype"]) && validationMap[k]["edittype"] == "textarea") {
					$("<textarea name='gridDataList[" + i + "][" + k + "]'>").val(val).appendTo($divGridData);
				} else {
					$("<input name='gridDataList[" + i + "][" + k + "]'>").val(val).appendTo($divGridData);
				}
			}else{
				if(isNotEmpty(validationMap[k]) && isNotEmpty(validationMap[k]["edittype"]) && validationMap[k]["edittype"] == "textarea") {
					$("<textarea name='gridDataList[" + i + "]." + k + "'>").val(val).appendTo($divGridData);
				} else {
					$("<input name='gridDataList[" + i + "]." + k + "'>").val(val).appendTo($divGridData);
				}
			}
		}
	});

	if(useValidation) {
		return errCnt == 0 ? true : false;
	}
}

/*
 * grid의 데이터를 form에 넣음
 * useValidation : 유효성 체크 여부(기본값은 false)
 * 	- true일 경우 유효성 체크 결과를 true/false로 리턴함
 * lengthChkYn : jqgrid 길이 체크 여부(기본값은 true)
 * mapYn : 리스트 내 map으로 파라미터 넘길시 true
 */
function gridToFormChanged(gridId, formId, useValidation, lengthChkYn, mapYn) {
	var $grid = $("#" + gridId);
	if(isEmpty(useValidation)) useValidation = false;
	if(isEmpty(lengthChkYn)) lengthChkYn = true;
	if(isEmpty(mapYn)) mapYn = false;
	var validationMap = {};
	var colModel = $grid.jqGrid("getGridParam", "colModel");

	for(var i in colModel) {
		validationMap[colModel[i].name] = {};
		if(isNotEmpty(colModel[i].editoptions) && isNotEmpty(colModel[i].editoptions.maxlength)) {
			validationMap[colModel[i].name]["maxlength"] = colModel[i].editoptions.maxlength;
			validationMap[colModel[i].name]["label"] = colModel[i].label;
		}
		if(isNotEmpty(colModel[i].editrules)) {
			validationMap[colModel[i].name]["editrules"] = colModel[i].editrules;
			validationMap[colModel[i].name]["label"] = colModel[i].label;
		}
		if(isNotEmpty(colModel[i].edittype)) {
			validationMap[colModel[i].name]["edittype"] = colModel[i].edittype;
		}

		if($.isEmptyObject(validationMap[colModel[i].name])) {
			delete validationMap[colModel[i].name];
		}
	}

	$("#" + formId).find("div.gridData").remove();
	var $divGridData = $("<div class='gridData'>").appendTo("#" + formId);
	var gridData = $grid.getChangedCells('all');

	if(lengthChkYn == true && gridData.length == 0) {
		$.showMsgBox(getMessage("errors.noDataToUpdate"));
		return false;
	}

	var errCnt = 0;
	var val;
	$(gridData).each(function(i, e) {
		for(var k in e) {
			val = e[k];
			if(useValidation && isNotEmpty(validationMap[k])) {
				// 글자 길이 byte 체크
				if(isNotEmpty(validationMap[k]["maxlength"])
					&& lengthb(val) > validationMap[k]["maxlength"]) {
					errCnt++;
					$.showMsgBox(getMessage("errors.validation.byteLimit4",	validationMap[k]["label"], validationMap[k]["maxlength"], 3));
					return false;
				}

				// editrules 체크
				if(isNotEmpty(validationMap[k]["editrules"])) {
					// required 체크
					if(isNotEmpty(validationMap[k]["editrules"]["required"])
						&& isEmpty(val)) {
						errCnt++;
						$.showMsgBox(getMessage("errors.required", validationMap[k]["label"]));
						return false;
					}
					// integer 체크
					if(isNotEmpty(validationMap[k]["editrules"]["integer"])
							&& isNotEmpty(val)
							&& parseInt(val, 10) != val) {
						errCnt++;
						$.showMsgBox(getMessage("errors.validation.integerOnly", validationMap[k]["label"]));
						return false;
					}
					// number 체크
					if(isNotEmpty(validationMap[k]["editrules"]["number"])
							&& isNotEmpty(val)
							&& isNaN(val)) {
						errCnt++;
						$.showMsgBox(getMessage("errors.validation.numberOnly", validationMap[k]["label"]));
						return false;
					}
				}
			}

			if(mapYn){
				if(isNotEmpty(validationMap[k]) && isNotEmpty(validationMap[k]["edittype"]) && validationMap[k]["edittype"] == "textarea") {
					$("<textarea name='gridDataList[" + i + "][" + k + "]'>").val(val).appendTo($divGridData);
				} else {
					$("<input name='gridDataList[" + i + "][" + k + "]'>").val(val).appendTo($divGridData);
				}
			}else{
				if(isNotEmpty(validationMap[k]) && isNotEmpty(validationMap[k]["edittype"]) && validationMap[k]["edittype"] == "textarea") {
					$("<textarea name='gridDataList[" + i + "]." + k + "'>").val(val).appendTo($divGridData);
				} else {
					$("<input name='gridDataList[" + i + "]." + k + "'>").val(val).appendTo($divGridData);
				}
			}
		}
	});

	if(useValidation) {
		return errCnt == 0 ? true : false;
	}

}

// 삭제할 데이터를 form에 넣음
function deleteDataToForm(gridId, keyColName, formId) {
	var $grid = $("#" + gridId);
	var ids = $grid.jqGrid("getGridParam", "selarrrow");
	if(ids.length == 0) {
		$.showMsgBox(getMessage("errors.noSelectedData"));
		return false;
	}

	var $f = $("#" + formId);
	var val;
	$f.find("[name=keys]").remove();
	$(ids).each(function(i, e) {
		val = $grid.jqGrid("getRowData", e)[keyColName];
		$("<input type='hidden' name='keys'>").val(val).appendTo($f);
	});
	return true;
}

/*
 * 삭제할 Grid데이터를 form에 넣음 + 신규 row는 바로 삭제
 * 모두 신규일 경우 row 만 삭제됨.
 * 기존 데이터가 포함되어있으면 return false
 */
function deleteGridToForm(gridId, keyColName, formId, newRowId){
	var $grid = $("#" + gridId);
	var ids = $grid.jqGrid("getGridParam", "selarrrow");
	var isStoredData = false;
	if(ids.length == 0) {
		$.showMsgBox(getMessage("errors.noSelectedData"));
		return false;
	}

	var $f = $("#" + formId);
	var val, rowData;
	$f.find("[name=keys]").remove();

	$(ids).each(function(i, e) {
		rowData = $grid.jqGrid("getRowData", e);
		val = rowData[keyColName];

		if(isNotEmpty(val) && removeNull(rowData["isNew"]) != "Y"){
			isStoredData = true;
			$("<input type='hidden' name='keys'>").val(val).appendTo($f);
		}else{
			$grid.jqGrid('delRowData',e);
		}
	});

	return isStoredData;
}

// 페이징 이벤트 처리
function setGridPaging(gridId, pgButton, callbackFunc, callbackArgs) {
	var $grid = $("#" + gridId);
	var $pager = $($grid.getGridParam("pager"));
	var pageNum = $grid.getGridParam("page");
	var lastPage = $grid.getGridParam("lastpage");

	if(pgButton.indexOf("next") != -1) {
		pageNum++;
	} else if(pgButton.indexOf("prev") != -1) {
		pageNum--;
	} else if(pgButton.indexOf("first") != -1) {
		pageNum = 1;
	} else if(pgButton.indexOf("last") != -1) {
		pageNum = lastPage;
	} else if(pgButton.indexOf("user") != -1) {
		pageNum = $pager.find(".ui-pg-input").val();
		if(isNaN(pageNum)) {
			pageNum = 1;
		} else {
			pageNum = parseInt(pageNum, 10);
		}
	}

	if(pageNum < 1 || isNaN(pageNum)) {
		pageNum = 1;
	} else if(pageNum > lastPage) {
		pageNum = lastPage;
	}

	if(isNotEmpty(callbackFunc)) {
		if(isEmpty(callbackArgs)) {
			callbackArgs = [];
		}
		callbackArgs.push(pageNum);
		window[callbackFunc].apply(window, callbackArgs);
	}
}

// jqGrid에서 datepicker를 적용할 열의 editoptions
var jqGridDatepickerEditoptions = {
					dataInit:function(el) {
						$(el).datepicker({
							setDate : $(el).val(),
							onClose : function(text, obj) {
								var $input = $(obj.input);
								var $grid = $("#" + $input.parents("table:first").attr("id"));
								var $tr = $input.parents("tr:first");
								var iRow = $grid.find("#" + $tr.attr("id"))[0].rowIndex;
								var iCol = $tr.find("td").index($(el).parents("td:first"));
								$grid.jqGrid("saveCell", iRow, iCol);
							}
						});
					}
				}

// link formatter - showDetail 함수 선언 필요
function showDetailLinkFormatter(cellvalue, options, rowObject) {
	return "<a href='#' onclick='showDetail(\"" + removeNull(cellvalue) + "\");clickGridRow(this);return false;'>" + escapeHTML(removeNull(cellvalue)) + "</a>";
}
function linkUnformatter(cellvalue, options) {
	return (isUndefined(cellvalue)?"":$.trim(cellvalue));
}
// textarea 속성이 있을 경우 높이를 제한하는 formatter
function textareaFormatter(cellvalue, options) {
	return "<div class='jqgridTextareaDiv'>" + $.jgrid.htmlEncode(removeNull(cellvalue)) + "</div>";
}
// input formatter - 정렬순서 등 입력란 표시용
function inputFormatter(cellvalue, options, rowObject) {
	return "<input type=\"text\" class=\"wp85 txt-r pl5 pdr5\" value=\"" + escapeHTML(removeNull(cellvalue)) + "\"/>";
}
// input unformatter - 정렬순서 등 입력란 표시용
function inputUnformatter(cellvalue, options, cell) {
	return $(cell).find("input").val();
}

// 날짜용 formatter
function dateFormatter(cellvalue, options, rowObject) {
	if(isNotEmpty(cellvalue) && cellvalue.length >= 8) {
		cellvalue = cellvalue.replaceAll(/[^0-9]/g, "");
		return cellvalue.substring(0,4) + "." + cellvalue.substring(4,6) + "." + cellvalue.substring(6,8);
	} else {
		return removeNull(cellvalue);
	}
}
// 날짜용 unformatter
function dateUnformatter(cellvalue, options) {
	return cellvalue.replaceAll(/[^0-9]/g, "");
}
// formatter 적용된 링크 클릭시 해당 열을 클릭 처리 (해당 row의 배경색 변경 또는 이벤트 발생)
function clickGridRow(obj) {
	$(obj).closest("td").click();
}

function clickGridRowByRowId(gridId, rowId) {
	$("#" + gridId + " tr#" + rowId).click();
}

// escape 문자 표시 방지용 포매터
function unescapeFormatter(cellvalue, options) {
	return unescapeHTML(cellvalue);
}

// number, currency 포매터용 unformatter
function numberUnformatter(cellvalue, options) {
	return isEmpty(cellvalue) ? 0 : ("" + cellvalue).replace(/[^(\d|\.|\-)]/, "");
}

// 정렬순서(sortOrder)용 sorttype
function sortTypeForSortOrder(cellValue) {
	var num = parseInt(cellValue, 10);
	return isNaN(num) ? 99999 : num;
}

/*
 * 해당 rowId의 cell 값들을 삭제
 * - gridId : grid의 id
 * - rowId : 값을 삭제할 rowId
 * - colNames : 값을 삭제할 col의 name 또는 배열
 */
function resetGridCellValue(gridId, rowId, colNames) {
	var $grid = $("#" + gridId);
	var rowData = $grid.jqGrid("getRowData", rowId);
	if(Array.isArray(colNames)) {
		$(colNames).each(function(i, e) {
			rowData[e] = "";
		});
	} else {
		rowData[colNames] = "";
	}
	$grid.jqGrid("setRowData", rowId, rowData);
}

/*
 * grid 체크박스 숨김 처리
 * - gridId : grid의 id
 * - colname : 비교할 열의 name
 * - compareValue : 비교할 값
 * - isEqual : true이면 일치할 경우 체크박스 숨김, false면 일치하지 않을 경우 숨김
 * 예) hideGridCheckbox("list", "useYn", "N", true);
 * - id가 list인 그리드에서 useYn == N이면 해당 row의 체크박스를 숨김
 */
function hideGridCheckbox(gridId, colname, compareValue, isEqual) {
	var $grid = $("#" + gridId);
	var ids = $grid.jqGrid("getDataIDs");
	$(ids).each(function(i, e) {
		var rowdata = $grid.jqGrid("getRowData", e);
		if((isEqual && rowdata[colname] == compareValue)
				|| (!isEqual && rowdata[colname] != compareValue)) {
			$grid.find(":checkbox[name=jqg_" + gridId + "_" + (ids[i]) + "]").hide();
		}
	});
}

/*
 * grid 셀병합(rowspan)
 * - gridId : grid의 id
 * - chkColNm : 같은 값인지 비교할 열의 name
 * - targetColNm : chkColNm의 값이 같은 경우 병합할 열의 name
 * - removeTd : true - 병합된 나머지 셀을 삭제, false - 숨김
 */
function gridRowSpan(gridId, chkColNm, targetColNm, removeTd) {
	var rowSpanArr = [], idx = -1, lastVal, $td;
	var $grid = $("#" + gridId);
	var gridData = $grid.jqGrid("getRowData");
	if(isEmpty(gridData)) {
		return false;
	}

	$(gridData).each(function(i, e) {
		if(idx == -1 || lastVal != e[chkColNm]) {
			idx = i;
			lastVal = e[chkColNm];
			rowSpanArr[i] = 1;
		} else {
			rowSpanArr[idx] += 1;
			rowSpanArr[i] = 0;
		}
	});

	$grid.find("tr.jqgrow").each(function(i,e) {
		$td = $(e).find("[aria-describedby=" + gridId + "_" + targetColNm + "]");
		if(rowSpanArr[i] > 1) {
			$td.attr("rowspan", rowSpanArr[i]);
		} else if(rowSpanArr[i] == 0) {
			if(isEmpty(removeTd) || !removeTd) {
				$td.hide();
			} else {
				$td.remove();
			}
		}
	});
}

// 그리드의 데이터가 있는지 여부
function isGridEmpty(gridId) {
	return $("#" + gridId).jqGrid("getRowData").length == 0;
}
function isEmptyGrid(gridId) {
	return isGridEmpty(gridId);
}

// jqGrid 리사이즈
function gridResize(gridId) {
	var $grid = $("#" + gridId);
	gNewGridWidth = $grid.closest(".gridContainer").width() - 40;	// 40 : grid 여백

	//console.log("gNewGridWidth",gNewGridWidth);

	if(gNewGridWidth > 0) {
		$grid.setGridWidth(gNewGridWidth);
	}

	// grid resize시 colspan header의 열너비가 어긋나는 것을 방지하기 위한 처리
	var groupHeader = $grid.jqGrid("getGridParam", "groupHeader");
	if(isNotEmpty(groupHeader)) {
		$grid.jqGrid("destroyGroupHeader");
		$grid.jqGrid("setGroupHeaders", groupHeader);
	}
}

//jqGrid 리사이즈
function popGridResize(gridId) {
	gNewGridWidth = $("#" + gridId).closest(".popup").width() - 40;	// 40 : grid 여백
	if(gNewGridWidth > 0) {
		$("#" + gridId).setGridWidth(gNewGridWidth);
	}
}

//////////// jqGrid 관련 함수 끝 ////////////

//////////// chartJS 관련 함수 시작 ////////////
var chartJsCustom = {
	colors : [
				/*	red,	green,	blue,
					yellow,	orange,	purple,
					grey	*/
				"rgb(255, 99, 132)",	"rgb(75, 192, 192)",	"rgb(54, 162, 235)",
				"rgb(255, 205, 86)",	"rgb(255, 159, 64)",	"rgb(153, 102, 255)",
				"rgb(201, 203, 207)"
			],
	getColor : function(i) {
		return this.colors[i%this.colors.length];
	},
	getColors : function(n) {
		var colorArr = [];
		if(typeof n == "number") {
			for(var i=0; i<n; i++) {
				colorArr.push(this.getColor(i));
			}
		} else {
			for(var i=0; i<n.length; i++) {
				colorArr.push(this.getColor(n[i]));
			}
		}
		return colorArr;
	},
	getBGColor : function(i) {
		return Chart.helpers.color(this.getColor(i)).alpha(0.5).rgbString();
	},
	getBGColors : function(n) {
		var colorArr = [];
		if(typeof n == "number") {
			for(var i=0; i<n; i++) {
				colorArr.push(this.getBGColor(i));
			}
		} else {
			for(var i=0; i<n.length; i++) {
				colorArr.push(this.getBGColor(n[i]));
			}
		}
		return colorArr;
	},
	defaultChartOptions : {
		bar : {
			responsive: true,
			responsiveAnimationDuration:0,
			maintainAspectRatio : false,
			legend: {
				display: true
			},
			scales : {
				xAxes : [{
					gridLines : {
						display : false
					}
				}],
				yAxes : [{
					ticks: {
						min : 0,
						max : 100,
						maxTicksLimit : 5
					}
				}]
			},
			//events : ["click"],
			animation : {
				duration : 500,
				onComplete: function(animation) {
					var chartInstance = this.chart;
					if(isNotEmpty(chartInstance.config.showValue) && !chartInstance.config.showValue) {
						return false;
					}
					ctx = chartInstance.ctx;
					ctx.font = Chart.helpers.fontString(Chart.defaults.global.defaultFontSize, Chart.defaults.global.defaultFontStyle, Chart.defaults.global.defaultFontFamily);
					ctx.textAlign = "center";
					ctx.fillStyle = "black";
					ctx.textBaseline = "bottom";

					this.data.datasets.forEach(function(dataset, i) {
						var meta = chartInstance.controller.getDatasetMeta(i);
						meta.data.forEach(function(bar, index) {
							var data = dataset.data[index];
							ctx.fillText(data, bar._model.x, bar._model.y - 5);
						});
					});
				}
			}
		},
		line : {
			responsive: true,
			responsiveAnimationDuration:0,
			maintainAspectRatio : false,
			legend: {
				position: "right"
			},
			scales : {
				xAxes : [{
					gridLines : {
						display : false
					}
				}],
				yAxes : [{
					ticks: {
						min : 0,
						maxTicksLimit : 5
					}
				}]
			}
		}
	},
	noValueChartOptions : {
		bar : {
			responsive: true,
			responsiveAnimationDuration:0,
			maintainAspectRatio : false,
			legend: {
				display: true,
				position: "bottom"
			},
			scales : {
				xAxes : [{
					gridLines : {
						display : false
					}
				}],
				yAxes : [{
					ticks: {
						min : 0,
						max : 100,
						maxTicksLimit : 5
					}
				}]
			},
			//events : ["click"],
			animation : {
				duration : 500,
			}
		},
		line : {
			responsive: true,
			responsiveAnimationDuration:0,
			maintainAspectRatio : false,
			legend: {
				position: "right"
			},
			scales : {
				xAxes : [{
					gridLines : {
						display : false
					}
				}],
				yAxes : [{
					ticks: {
						min : 0,
						maxTicksLimit : 5
					}
				}]
			}
		}
	}
}
//////////// chartJS 관련 함수 끝 ////////////

// jquery browser 변수 호환용
jQuery.browser = {};
(function () {
	jQuery.browser.msie = false;
	jQuery.browser.version = 0;
	if (navigator.userAgent.match(/MSIE ([0-9]+)\./)) {
		jQuery.browser.msie = true;
		jQuery.browser.version = RegExp.$1;
	}
})();

// 파일 모듈 사용 가능 여부
var isAdvancedUpload = function() {
	var div = document.createElement("div");
	return (("draggable" in div) || ("ondragstart" in div && "ondrop" in div)) && "FormData" in window && "FileReader" in window;
}();

// window resize
var wrapMinW, wrapMaxW, bodyW;
function windowResize() {
	bodyW = $("body").width();
	if(bodyW > wrapMaxW) bodyW = wrapMaxW;
	if(bodyW < wrapMinW) bodyW = wrapMinW;
	bodyW -= 20;

	$(".gridContainer").addClass("gridContainerForResize");
	$("#wrap").css("width", bodyW + "px");

	// chart overflow-x 방지
	$(".chart100").each(function(i, e) {
		var margin = 0;
		$(e).parentsUntil(".contents").each(function(i2, e2) {
			margin += parseInt($(e2).css("margin-left").replace("px", ""), 10);
			margin += parseInt($(e2).css("margin-right").replace("px", ""), 10);
		});
		$(e).hide().css("width", ($(".contents").width() - margin) + "px").show();
	});

	$(".gridContainer").removeClass("gridContainerForResize");

	// jqGrid 동적 리사이즈
	$(".ui-jqgrid-btable").each(function(i, e) {
		// autowidth가 true인 grid만 리사이즈 적용
		if($("#" + $(e).attr("id")).jqGrid("getGridParam", "autowidth")) {
			gridResize($(e).attr("id"));
		}
	});

	// jqgrid 열 리사이즈 선 위치 보정
	$(".ui-jqgrid-resize-mark").css("margin-left", (($("div.depth2.active").length == 0 ? $("#menu").width(): 0)-289) + "px");

	if(typeof resizeCallback != "undefined" && typeof resizeCallback == "function") {
		resizeCallback.apply(this);
	}
}

/********************************************************
 * window open 설정
 * parameter : url, width, height, name, scroll, useForm, method
 ********************************************************/
$.openWinByName = function(obj) {
	var left = (screen.width-obj.width)/2;
	var top = (screen.height-obj.height)/2;
	var name = nvl(obj.name, "popWin");
	left += window.screenX;//듀얼모니터 사용시 팝업위치 중앙으로
	var newWin = window.open('',name,'width='+obj.width+',height='+obj.height+',left='+left+',top='+top+',scrollbars='+obj.scroll+',toolbar=no');
	if(isNotEmpty(obj.useForm) && obj.useForm) {
		var f = document.layoutPopForm;
		f.action = obj.url;
		f.target = name;
		f.method = nvl(obj.method, "POST");
		f.submit();
	} else {
		newWin.location.href = obj.url;
	}
	return newWin;
};

/*
 * 사용법 : $('#테이블 ID').rowspan('tableId','병합할 셀순번','병합할 셀의 판단근거가 되는 셀순번');
 */
$.fn.rowspan = function(table, colIdx, nextColIdx, isStats) {
	return this.each(function(){
		var that;
		var nextThis;
		var nextThat;
		var i = 0;

		$('tr', this).each(function(row) {
			$('td:eq('+colIdx+')', this).each(function(col) {
				var nextThis = $("#"+table + " tbody tr:eq(" + i + ") td:eq(" + nextColIdx + ")");

				if ($(nextThis).html() == $(nextThat).html()
					&& (!isStats
							|| isStats && $(nextThis).prev().html() == $(nextThat).prev().html()
							)
					) {
					rowspan = $(that).attr("rowspan") || 1;
					rowspan = Number(rowspan)+1;

					$(that).attr("rowspan",rowspan);

					// do your action for the colspan cell here
					$(this).hide();
					$(this).html("");

					//$(this).remove();
					// do your action for the old cell here

				} else {
					that = this;
					nextThat = nextThis;
				}

				// set the that if not already set
				that = (that == null) ? this : that;
				nextThat = (nextThat == null) ? nextThis : nextThat;
				i ++;
			});
		});
	});
};

function textareaToHtml(str){
	var newStr = "";
	if(str != null && str != '' && str != undefined){
		newStr = str.replaceAll('\u0020', '&nbsp;').replace(/(?:\r\n|\r|\n)/g, '<br />');
	}
	return newStr;
}

function getPgmId() {
	var arr = document.location.href.split("#");
	if(arr.length > 1) {
		return arr[1].split("?")[0];
	} else {
		return "";
	}
}

// 메뉴 접음/펼침
function showMenu(obj) {
	var $targetMenu = null;

	// 닫음
	if(obj == null) {
		if($(".depth2#menu02").hasClass("activeMenu")) {
			$targetMenu = $(".depth2#menu02");
		} else {
			$targetMenu = $(".depth2[class^=menu].activeMenu");
		}
	} else {
		$targetMenu = $("#" + $(obj).data("target-menu"));
	}

	$(".depth2[class^=menu]").hide();
	$("#gnb > ul > li > a").removeClass("active");

	// 펼쳐져 있으면 닫음
	if($targetMenu.hasClass("activeMenu")) {
		$targetMenu.removeClass("activeMenu");

		// 조직도 닫음 & 이전에 펼친 메뉴가 있는 경우 이전 메뉴 펼침
		if($targetMenu.attr("id") == "menu02" && $(".depth2[class^=menu].activeMenu").length > 0) {
			$targetMenu = $(".depth2[class^=menu].activeMenu");
		} else {
			$targetMenu = null;
		}
	} else {
		if($targetMenu.attr("id") != "menu02") {
			$(".depth2[class^=menu]").removeClass("activeMenu");
		}

		$targetMenu.addClass("activeMenu");
	}

	if($targetMenu != null) {
		$targetMenu.show();
		$("#gnb > ul > li > a[data-target-menu=" + $targetMenu.attr("id") + "]").addClass("active");

		$("#gnb .close").show();
		$("#gnb").css("width", "270px");
	} else {
		$("#gnb .close").hide();
		$("#gnb").css("width", "60px");
	}

	windowResize();
	return false;
}

// 팝업 공지사항
function showPopNotice(o) {
	$.openWinByName({
		url:contextPath + "/common/popNoticeDetail.do?id="+o.id+"&layout=popup",
		width:o.width,
		height:o.height,
		name:"popWinNotice" + o.id,
		scroll:"auto",
		useForm:true
	});
}

$(function() {
	$.datepicker.setDefaults($.datepicker.regional[currentLocale]);
	$.datepicker.setDefaults({
		changeMonth: true,
		changeYear: true,
		dateFormat : "yy.mm.dd"
	});

	$(document).ajaxStart(function() {
		showLoading(true);
	}).ajaxStop(function() {
		showLoading(false);
	});

	if($("#wrap").length){
		wrapMinW = parseInt($("#wrap").css("min-width").replace("px", ""), 10);
		wrapMaxW = parseInt($("#wrap").css("max-width").replace("px", ""), 10);
	}

	$(window).on("resize", function() {
		$(".loadingDiv").height($("body").prop("scrollHeight"));
		windowResize();
		resizeGuideComment();
	}).on("hashchange", function(e) {
		var urlPgmId = getPgmId();
		if(isNotEmpty(urlPgmId) && urlPgmId != currPgmId) {
			loadPageByPgmId(urlPgmId);
		}
	});

	$("a[href='#']").click(function(event) {
		event.preventDefault();
	});

	if($("#gnb").length > 0) {
		// 성과조직도 조회
		reloadScDeptList();

		// 즐겨찾기 조회
		reloadBookmark();

		// 메뉴 관련
		$(".menu01.depth2 .menu > ul > li > a").click(function(){
			$(".menu01.depth2 .menu > ul > li > a").removeClass("active");
			$(".menu01.depth2 .menu > ul > li > ul").removeClass("active");
			$(this).addClass("active");
			$(this).parent("li").children("ul").addClass("active");

			/*
			 * kimyh custom
			 * 대메뉴 클릭시 펼쳐진 소메뉴가 없으면 첫번째 소메뉴 펼침
			 */
			if($(this).parent("li").find(".menu4.active").length == 0) {
				$(this).parent("li").find(".menu3 > li > a:first").addClass("active");
				$(this).parent("li").find(".menu4:first").addClass("active");
			}
		});

		$(".menu01.depth2 .menu > ul > li > ul > li > a").click(function(){
			$(".menu01.depth2 .menu > ul > li > ul > li > a").removeClass("active");
			$(".menu01.depth2 .menu > ul > li > ul > li > ul").removeClass("active");
			$(this).addClass("active");
			$(this).parent("li").children("ul").addClass("active");
		});

		$("#gnb .depth2 .menu .allmenu a").click(function(){
			if ($(this).hasClass("active")) {
				$(this).removeClass("active");
				$(".am-bx").hide();
			} else {
				$(this).addClass("active");
				$(".am-bx").show();
			}
		});

		$(".gnb01").click();
		$("#contents").removeClass("hide");

		// 도움말
		$("textarea.guideComment").each(function(i, e) {
			guideCommentMap[$(e).attr("id").replace("PGM_", "")] = $(e).text();
		}).remove();

		$("textarea.urlPages").each(function(i, e) {
			helpImagesMap[$(e).attr("id").replace("PGM_", "")] = $(e).text();
		}).remove();

		$("textarea.manualImgNmList").each(function(i, e) {
			existHelpImages.push($(e).text());
		}).remove();

		$("#contents p.help").on("click", function(e) {
			showGuideComment();
			e.stopPropagation();
		});
		$("#contents p.help .text").on("click", function(e) {
			e.stopPropagation();
		});

		$("#menu, #bookmark").on("click", "li[id^=menu_] a, li[id^=bookmark_] a, #allMenu a", function(e) {

			/*세션 초기화*/
			initSessionTime();

			href = $(this).attr("href");
			if(href.split("#").length == 2) {
				pgmId = href.split("#")[1];
				loadPageByPgmId(pgmId);
			}
			e.preventDefault();
			return false;
		});

		// 팝업 공지사항
		if(isNotEmpty(gPopNoticeList)) {
			for(var i in gPopNoticeList) {
				if(removeNull($.cookie("popNoticeClose" + gPopNoticeList[i].id)) != "Y") {
					showPopNotice(gPopNoticeList[i]);
				}
			}
		}

		$(".gnb07").click(function(e){
			if($(this).hasClass("active")){
				$(this).removeClass("active");
				$(".fwtable-bx").css("display","none");
			}else{
				$(this).addClass("active");
				$(".fwtable-bx").css("display","block");
			}
		});

		/*
		 * 프로세스 흐름도 생성
		 * 완료/완료취소
		 * 메뉴이동 이벤트 추가
		 * */

		reloadProcessList();

		$("#allProcess").on("click","p[id^='processJob_']",function(e){
			//console.log("e.target",e.target);
			var id = e.target.id;
			loadPageByPgmId(id.split("_")[4]);
			$(".gnb07").click();
		});

		$("#allProcess").on("click","div[class$='-icn']",function(e){
			//console.log("e.target save",e.target);
			e.stopPropagation();
			var id = e.target.nextSibling.id;

			var preIdsArray = e.target.nextSibling.id.split("_");
			var nextIdsArray = e.target.nextSibling.id.split("_");
			var preNum = Number(preIdsArray[3])-1;
			var nextNum = Number(nextIdsArray[3])+1;
			preIdsArray.splice(3,2,preNum+"");
			nextIdsArray.splice(3,2,nextNum+"");

			var preId = preIdsArray.join("_");
			var nextId = nextIdsArray.join("_");

			/*완료인 경우*/
			if(e.target.className == "complete-icn"){
				//console.log("obj",$("[id^='"+preId+"']"));
				if($("[id^='"+preId+"']").size() > 0 && $("[id^='"+preId+"']").prev().attr("class") == "complete-icn"){
					$.showMsgBox(getMessage("errors.process.notAllowComplete"));
				}else{
					sendAjax({
						"url":contextPath+"/common/processCompleteData.do",
						"data":{"findYear":getFindValue("findYear"),"pgmId":id.split("_")[4],"endYn":"Y","_csrf":getCsrf()},
						"doneCallbackFunc":function(data){
							if(data.result != AJAX_SUCCESS) {
								$.showMsgBox(data.msg);
							}else{
								e.target.className = "cancel-icn";
								e.target.innerHTML = getMessage("word.complete")+"<br/>"+getMessage("word.cancel");
								e.target.parentNode.className = "cancel-bg";
							}
						}
					});
				}
			}

			/*완료취소인 경우*/
			else if(e.target.className == "cancel-icn"){
				//console.log("obj",$("[id^='"+nextId+"']"));
				if($("[id^='"+nextId+"']").size() > 0 && $("[id^='"+nextId+"']").prev().attr("class") == "cancel-icn"){
					$.showMsgBox(getMessage("errors.process.notAllowCancel"));
				}else{

					sendAjax({
						"url":contextPath+"/common/processCompleteData.do",
						"data":{"findYear":getFindValue("findYear"),"pgmId":id.split("_")[4],"endYn":"N","_csrf":getCsrf()},
						"doneCallbackFunc":function(data){
							if(data.result != AJAX_SUCCESS) {
								$.showMsgBox(data.msg);
							}else{
								e.target.className = "complete-icn";
								e.target.innerHTML = getMessage("word.complete");
								e.target.parentNode.className = "complete-bg";
							}
						}
					});
				}
			}
		});

	}

	/*
	 * 성과조직도 보임/숨김
	 * & 기준연도 변경시 성과조직도 새로고침
	 */
	$("body").on("click", ".sch-oga", function(e) {
		showScDeptList($(this).attr("id"));
		e.stopPropagation();
	}).on("click", ".scDeptList > ul a", function(e) {	// 성과조직도 접음/펼침 처리
		isScDeptListOnFocus = true;	// blur 이벤트 발생으로 tree 숨김을 방지하기 위한 변수
		if($(this).hasClass("active")) {
			$(this).removeClass("active");
		} else {
			$(this).addClass("active");
		}
		e.stopPropagation();
	}).on("click", ".scDeptList > ul span", function(e) {	// 성과조직도 선택
		setScDept($(this).data("sc_dept_id"), $(this).parents(".sch-oga:first").attr("id"));
		e.stopPropagation();
	}).on("click", function() {	// 성과조직도 외의 부분을 클릭한 경우 성과조직도를 닫음
		if(isScDeptListVisible) {
			showScDeptList(false);
		}
		if(isGuideCommentVisible) {
			showGuideComment(false);
		}
	}).on("change", "#findYear", function(e) {
		setFindValue("findYear", $(this).val());
		reloadScDeptList($(this).val());
		reloadProcessList($(this).val());
	});
});
