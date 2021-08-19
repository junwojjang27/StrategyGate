<!-- 
*************************************************************************
* CLASS 명	: AuthGrpList
* 작 업 자	: joey
* 작 업 일	: 2018-1-12
* 기	능	: 그룹별권한 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018-1-12				최 초 작 업
**************************************************************************
-->

<!--
필독 : 다국어 지원기능에 의해서 화면에 나타나는 한글명칭은 message properties파일을 이용하여 수정할 것. 
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">

$(function(){
	searchList();
	
	/*
	$("#addRow").click(function(e){
		addRow();
	});
	
	$("#delRow").click(function(e){
		delRow();
	});
	*/
	
	/*
	$("#findAuthGubun").on("change",function(){
		if($("#findAuthGubun").val() != '01' && $("#findAuthGubun").val() != '99'){
			$("#saveBtn").hide();
		}else{
			$("#saveBtn").show();
		}
	});
	*/
	
})

function menuList(){
    
	//var authGubun = $("#findAuthGubun").val();
	/*CSRF protection*/
	//var _csrfCom = $("#form").find("[name=_csrf]").eq(0).val();
	
	$.ajax({url 		: "${context_path}/system/system/menu/authGrp/menuList_json.do", 
		    method 		: "POST",
		    dataType	: "json",
		    data 		: $("#form").serialize()
		   }).done(function(json){
		     	// Handle the complete event
			    var data = json.list;
			    
			    $("#menuList").empty();
			    
			    if(0 == $("#menuList").find("select").length){
	    			$("<select></select>").attr({
	    				id:"menuData",
	    				name:"menuData",
	    				multiple:"multiple",
	    				style:"width:100%;height:500px;"
	    			}).appendTo($("#menuList"));
	    			//$("#menuList").add("select").attr("id","menuTotList").attr("name","menuTotList").attr("multiple","multiple");
	    		}
			    
			    if(0<data.length){
			    	for(var i=0 ; i<data.length ; i++){
			    		$("<option></option>").val(data[i].pgmId).text(data[i].fullPgmNm).appendTo("#menuData");
			    	}
			    }
			    
			    //click event
			    
			    $("select[name='menuData']").dblclick(function(e){
			    	addRow();
				});
			    
		   }).error(function(json,textStatus){
		   });

}

function menuSelectedList(){
    
	//var authGubun = $("#findAuthGubun").val();
	/*CSRF protection*/
	//var _csrfCom = $("#form").find("[name=_csrf]").eq(0).val();
	
	$.ajax({url 		: "${context_path}/system/system/menu/authGrp/menuSelectedList_json.do", 
		    method 		: "POST",
		    dataType	: "json",
		    data 		: $("#form").serialize()
		   }).done(function(json){
		     	// Handle the complete event
			    var data = json.list;
				$("#menuSelectedList").empty();		
				
				if(0 == $("#menuSelectedList").find("select").length){
	    			$("<select></select>").attr({
	    				id:"menuSelectedData",
	    				name:"menuSelectedData",
	    				multiple:"multiple",
	    				style:"width:100%;height:500px;"
	    			}).appendTo($("#menuSelectedList"));
	    		}
				if(0<data.length){
				    for(var i=0 ; i<data.length ; i++){
			    		$("<option></option>").val(data[i].pgmId).text(data[i].fullPgmNm).appendTo("#menuSelectedData");
			    	}
				}
				
				$("select[name='menuSelectedData']").dblclick(function(e){
			    	delRow();
				});
				
		   }).error(function(json,textStatus){
		   });
}

// 목록 조회
function searchList() {
	menuList();
	menuSelectedList();
}

function addRow(){
	
	var isExist = false;
	
	$("select[name='menuData'] option:selected").each(function(i,v){
		if($("select[name='menuSelectedData'] option[value='"+$(this).val()+"']").length > 0){
			isExist = true;
		}else{
			$("<option></option>").val($(this).val()).text($(this).text()).appendTo("#menuSelectedData");
			$("select[name='menuData'] option[value='"+$(this).val()+"']").remove();
		}
	});
	
	if(isExist) $.showMsgBox(getMessage("common.alreadySelectedData.msg"),null); 
	
}

function delRow(){
	$("select[name='menuSelectedData'] option:selected").each(function(i,v){
		$("<option></option>").val($(this).val()).text($(this).text()).appendTo("#menuData");
		$("select[name='menuSelectedData'] option[value='"+$(this).val()+"']").remove();
	});
}

function save(){
	
	/*
	if(0 == $("select[name='menuSelectedData'] option").length){
		$.showMsgBox(getMessage("bsc.system.systemOriginalData.systemOriginalDataList.error"),null);
		return false;
	}
	*/
	
	var menuSelectedData = "";
	
	$("#authGubun").val($("#findAuthGubun").val());
	$("select[name='menuSelectedData'] option").each(function(i,v){
		menuSelectedData += $(this).val()+"|";
	});
	$("#menuSelectedDataList").val(menuSelectedData);
	
	sendAjax({
		"url" : "${context_path}/system/system/menu/authGrp/saveAuthGrp.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "searchList"
	});
	
}

</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
    <form:hidden path="authGubun"></form:hidden>
    <form:hidden path="menuSelectedDataList"></form:hidden>
	<div class="sch-bx">
		<ul>
			<li>
				<label for="findAuthGubun"><spring:message code="word.auth"/></label>
				<form:select path="findAuthGubun" class="select" items="${codeUtil:getCodeList('018')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw">
		<%--
		<a href="#" class="excel" onclick="excelDownload();return false;"><span><spring:message code="button.excelDownload"/></span></a>
		--%>
	</div>
	<div class="table-bx3">
		<div class="moveLeft">
			<p id="menuTitle1"><spring:message code="word.menuList"/>
			<ul id="menuList">
			</ul>
		</div>
		<div class="moveCenter">
			<div><input type="button" class="goRight" id="goRight" title="<spring:message code="button.add"/>" onclick="addRow();return false;"></div>
			<br />
			<div><input type="button" class="goLeft" id="goLeft" title="<spring:message code="button.delete"/>" onclick="delRow();return false;"></div>
		</div>
		<div class="moveRight">
			<p id="menuTitle2"><spring:message code="word.menuSelectedList"/></p>
			<ul id="menuSelectedList">
			</ul>
		</div>
	</div>
	<div class="tbl-bottom tbl-bottom2 mt0">
		<div class="tbl-btn">
			<a href="#" id="saveBtn" class="save" onclick="save();return false;"><spring:message code="button.save"/></a>
		</div>
	</div>
</form:form>

