<!--
*************************************************************************
* CLASS 명	: SurvResultList
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-22
* 기	능	: 설문결과 List
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-22				최 초 작 업
**************************************************************************
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/common-taglibs.jsp"%>
<script type="text/javascript">
var ObjectSet = "";
var horizontalBar ="labels: [],";
	horizontalBar +="datasets: [{";
	horizontalBar +="label: 'Dataset 1',";
	horizontalBar +="backgroundColor : chartJsCustom.getBGColor(1),";
	horizontalBar +="borderColor: chartJsCustom.getColor(1),";
	horizontalBar +="borderWidth: 1,";
	horizontalBar +="data: []";
	horizontalBar +="}]";
	horizontalBar +="}";
var pie ="labels: [],";
	pie +="datasets: [{";
	pie +="label: 'Dataset 1',";
	pie +="backgroundColor : [";
	pie +="chartJsCustom.colors[1],";
	pie +="chartJsCustom.colors[2],";
	pie +="chartJsCustom.colors[3],";
	pie +="chartJsCustom.colors[4],";
	pie +="chartJsCustom.colors[5],";
	pie +="chartJsCustom.colors[6],";
	pie +="chartJsCustom.colors[7],";
	pie +="],";
	pie +="borderColor: [";
	pie +="chartJsCustom.colors[1],";
	pie +="chartJsCustom.colors[2],";
	pie +="chartJsCustom.colors[3],";
	pie +="chartJsCustom.colors[4],";
	pie +="chartJsCustom.colors[5],";
	pie +="chartJsCustom.colors[6],";
	pie +="chartJsCustom.colors[7],";
	pie +="],";
	pie +="borderWidth: 1,";
	pie +="data: []";
	pie +="}]";
	pie +="}";

<%-- 객관식 질문만큼 차트 객체생성 --%>
<c:forEach items="${quesList}" var="qList" varStatus="status">
if("${qList.quesGbnId}" != "002") {
	ObjectSet = "var horizontalBarChartData${status.index} = {"
	ObjectSet += horizontalBar;
	eval(ObjectSet);
	
	ObjectSet = "var pieChartData${status.index} = {"
	ObjectSet += pie;
	eval(ObjectSet);
	
	<c:forEach items="${itemList}" var="iList" varStatus="status2">
		if("${qList.quesId}" == "${iList.quesId}") {
			
			<c:forEach items="${linkList}" var="lList" varStatus="status3">
				if("${iList.linkQuesId}" == "${lList.quesId}") {
					if("${lList.quesGbnId}" != "002") {
						ObjectSet = "var horizontalBarChartData${status.index}_${status3.index} = {"
						ObjectSet += horizontalBar;
						eval(ObjectSet);
								
						ObjectSet = "var pieChartData${status.index}_${status3.index} = {"
						ObjectSet += pie;
						eval(ObjectSet);
					}
				}
			</c:forEach>
			
		}
	</c:forEach>
}
</c:forEach>

$(function(){
	$("#surveyId").val($("#findSurveyId").val());
	$("#scDeptId").val($("#findScDeptId").val());

	sendAjax({
		"url" : "${context_path}/system/system/survey/survResult/selectDetail.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setDetail"
	});
});

function drowChart(i, quesId){
	var ctx = $("#canvas_"+i)[0].getContext("2d");
	if($("#findChartGbn").val() == "001") {
		window['myHorizontalBar'+i] = new Chart(ctx, {
			type: 'horizontalBar',
			data: eval("horizontalBarChartData"+i),
			options: {
				// Elements options apply to all of the options unless overridden in a dataset
				// In this case, we are setting the border of each horizontal bar to be 2px wide
				elements: {
					rectangle: {
						borderWidth: 2,
					}
				},
				responsive: true,
				legend: {
					display: false,
					position: 'right',
				},
				title: {
					display: true,
					text: ''
				}
			}
		});
	}else{
		window['myHorizontalBar'+i] = new Chart(ctx, {
			type: 'pie',
			data: eval("pieChartData"+i),
			options: {
				// Elements options apply to all of the options unless overridden in a dataset
				// In this case, we are setting the border of each horizontal bar to be 2px wide
				elements: {
					rectangle: {
						borderWidth: 2,
					}
				},
				responsive: true,
				legend: {
					display: true,
					position: 'right',
				},
				title: {
					display: false,
					text: ''
				},
				scaleShowLabelBackdrop : true
			}
		});
	}

	pushArray(i, quesId);
}


//차트 그리기
function pushArray(i, quesId){
	document.form.idx.value = i;
	document.form.quesId.value = quesId;
	sendAjax({
		"url" : "${context_path}/system/system/survey/survResult/selectChartData.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setChartData"
	});
}

//차트에 들어갈 데이터 array로 만들어 넣어주기
function setChartData(data) {
	setEmptyChart(data.idx);
	if(isNotEmpty(data.list)) {
		for (var i in data.list) {
			if(isNotEmpty(data.list[i])){
				if($("#findChartGbn").val() == "001") {
					eval("horizontalBarChartData"+data.idx+".datasets[0].data.push(data.list[i].answerCnt)");
					eval("horizontalBarChartData"+data.idx+".labels.push(data.list[i].itemContent)");
				}else{
					eval("pieChartData"+data.idx+".datasets[0].label = data.list[i].quesNm");
					eval("pieChartData"+data.idx+".datasets[0].data.push(data.list[i].answerCnt)");
					eval("pieChartData"+data.idx+".labels.push(data.list[i].itemContent)");
				}
			}else{
			}
		}

		window['myHorizontalBar'+data.idx].update();
	}
}

//데이터 없을때 차트 empty처리
function setEmptyChart(idx) {
	if($("#findChartGbn").val() == "001") {
		eval("horizontalBarChartData"+idx+".datasets[0].data = []");
		eval("horizontalBarChartData"+idx+".labels = []");
	}else{
		eval("pieChartData"+idx+".datasets[0].data = []");
		eval("pieChartData"+idx+".labels = []");
	}
	window['myHorizontalBar'+idx].update();
}

function drowChartForLink(i, k, quesId) {
	var ctx = $("#canvas_"+i+"_"+k)[0].getContext("2d");
	if($("#findChartGbn").val() == "001") {
		window['myHorizontalBar'+i+"_"+k] = new Chart(ctx, {
			type: 'horizontalBar',
			data: eval("horizontalBarChartData"+i+"_"+k),
			options: {
				// Elements options apply to all of the options unless overridden in a dataset
				// In this case, we are setting the border of each horizontal bar to be 2px wide
				elements: {
					rectangle: {
						borderWidth: 2,
					}
				},
				responsive: true,
				legend: {
					display: false,
					position: 'right',
				},
				title: {
					display: true,
					text: ''
				}
			}
		});
	}else{
		window['myHorizontalBar'+i+"_"+k] = new Chart(ctx, {
			type: 'pie',
			data: eval("pieChartData"+i+"_"+k),
			options: {
				// Elements options apply to all of the options unless overridden in a dataset
				// In this case, we are setting the border of each horizontal bar to be 2px wide
				elements: {
					rectangle: {
						borderWidth: 2,
					}
				},
				responsive: true,
				legend: {
					display: true,
					position: 'right',
				},
				title: {
					display: false,
					text: ''
				},
				scaleShowLabelBackdrop : true
			}
		});
	}

	pushArrayForLink(i, k, quesId);
}


//차트 그리기
function pushArrayForLink(i, k, quesId){
	document.form.idx.value = i;
	document.form.subIdx.value = k;
	document.form.quesId.value = quesId;
	sendAjax({
		"url" : "${context_path}/system/system/survey/survResult/selectChartData.do",
		"data" : getFormData("form"),
		"doneCallbackFunc" : "setChartDataForLink"
	});
}

//차트에 들어갈 데이터 array로 만들어 넣어주기
function setChartDataForLink(data) {
	setEmptyChartForLink(data.idx, data.subIdx);
	if(isNotEmpty(data.list)) {
		for (var i in data.list) {
			if(isNotEmpty(data.list[i])){
				if($("#findChartGbn").val() == "001") {
					eval("horizontalBarChartData"+data.idx+"_"+data.subIdx+".datasets[0].data.push(data.list[i].answerCnt)");
					eval("horizontalBarChartData"+data.idx+"_"+data.subIdx+".labels.push(data.list[i].itemContent)");
				}else{
					eval("pieChartData"+data.idx+"_"+data.subIdx+".datasets[0].label = data.list[i].quesNm");
					eval("pieChartData"+data.idx+"_"+data.subIdx+".datasets[0].data.push(data.list[i].answerCnt)");
					eval("pieChartData"+data.idx+"_"+data.subIdx+".labels.push(data.list[i].itemContent)");
				}
			}else{
			}
		}

		window['myHorizontalBar'+data.idx+'_'+data.subIdx].update();
	}
}

//데이터 없을때 차트 empty처리
function setEmptyChartForLink(idx, subIdx) {
	if($("#findChartGbn").val() == "001") {
		eval("horizontalBarChartData"+idx+"_"+subIdx+".datasets[0].data = []");
		eval("horizontalBarChartData"+idx+"_"+subIdx+".labels = []");
	}else{
		eval("pieChartData"+idx+"_"+subIdx+".datasets[0].data = []");
		eval("pieChartData"+idx+"_"+subIdx+".labels = []");
	}
	window['myHorizontalBar'+idx+'_'+subIdx].update();
}

// 목록 조회
function searchList() {
 	loadPage("${context_path}/system/system/survey/survResult/survResultList.do", "form");
}
 
// 상세 조회 값 세팅
function setDetail(data) {
	var dataVO = data.selectDetail;
	voToFormForText(dataVO, "form", ["surveyNm","allUsrCnt","ansUsrCnt","particpationRate"]);

	if(dataVO.closeYn == "N") {
		$(".page-noti").show();
		$("#bodyDiv").hide();
	}else{
		$(".page-noti").hide();
		$("#bodyDiv").show();

		var quesList;
		var itemList;
		var linkList;
		var linkItemList;
		var str, titleStr, $table;

		$("#tempDiv").nextAll().remove();
		for(var i in data.quesList) {
			quesList = data.quesList[i];
			$table = $("#tempDiv").clone().removeAttr("id").removeClass("hide").attr("id","tempDiv_"+i);

			titleStr = '<div class="tbl-btn txt-l"><span class="txt-l">'+quesList.quesNm+'</span>';
			if(quesList.quesGbnId != "001") { // 주관식 or 객관식+주관식 답변보기 버튼 생성
				titleStr += '&nbsp;<a href="#" class="save pdt5 pdr10 pdb5 pl10" onclick="popEssayQues(\''+quesList.quesId+'\',\''+quesList.quesNm+'\');return false;"><spring:message code="word.ansView"/></a>';
			}
			titleStr += '</div>';
			$table.find(".ptitle").html(titleStr); // 질문 set
			if(quesList.quesGbnId == "002") { // 주관식
				$table.find(".divFloatLeft").hide();
				$table.removeClass("hx410").addClass("hx60");
			}
			$("#bodyDiv").append($table);
			if(quesList.quesGbnId != "002") {
				$table.find("canvas").attr("id","canvas_"+i);
				drowChart(i, quesList.quesId); // 차트그리기
			}

			// 답변 set
			for(var j in data.itemList) {
				itemList = data.itemList[j];
				if(quesList.quesId == itemList.quesId) {
					str = '<tr>';
					str += '<td class=""><span>'+itemList.itemContent+'</span></td>';
					str += '<td class="txt-r"><span>'+itemList.answerCnt+'</span></td>';
					str += '<td class="txt-r"><span>'+itemList.answerRate+'</span></td>';
					str += '</tr>';
					$("#tempDiv_"+i+" table tbody").append(str);

					// 연계질문 set
					for(var k in data.linkList) {
						linkList = data.linkList[k];
						if(itemList.linkQuesId == linkList.quesId) {
							$table = $("#tempDiv").clone().removeAttr("id").removeClass("hide").attr("id","tempDiv_"+i+"_"+k);
							titleStr = '<div class="tbl-btn txt-l"><span class="txt-l">'+linkList.quesNm+'</span>';
							if(linkList.quesGbnId != "001") { // 주관식 or 객관식+주관식 답변보기 버튼 생성
								titleStr += '&nbsp;<a href="#" class="save pdt5 pdr10 pdb5 pl10" onclick="popEssayQues(\''+linkList.quesId+'\',\''+linkList.quesNm+'\');return false;"><spring:message code="word.ansView"/></a>';
							}
							titleStr += '</div>';
							$table.find(".ptitle").html(titleStr);
							if(linkList.quesGbnId == "002") { // 주관식
								$table.find(".divFloatLeft").hide();
								$table.removeClass("hx410").addClass("hx60");
							}
							$("#bodyDiv").append($table);

							if(linkList.quesGbnId != "002") { // 객관식이 있을때
								$table.find("canvas").attr("id","canvas_"+i+"_"+k);
								drowChartForLink(i, k, linkList.quesId); // 차트그리기
							}

							for(var l in data.itemList) {
								linkItemList = data.itemList[l];
								if(linkList.quesId == linkItemList.quesId) {
									str = '<tr>';
									str += '<td class=""><span>'+linkItemList.itemContent+'</span></td>';
									str += '<td class="txt-r"><span>'+linkItemList.answerCnt+'</span></td>';
									str += '<td class="txt-r"><span>'+linkItemList.answerRate+'</span></td>';
									str += '</tr>';
									$("#tempDiv_"+i+"_"+k+" table tbody").append(str);
								}
							}
						}
					}
				}
			}
		}

	}
}

// 답변보기 팝업
function popEssayQues(quesId, quesNm) {
	document.form.quesId.value = quesId;
	document.form.quesNm.value = quesNm;

	openFancybox({
		"url" : "${context_path}/system/system/survey/survResult/popEssayQuesList.do",
		"data" : getFormData("form")
	});
}
</script>

<form:form commandName="searchVO" id="form" name="form" method="post">
 	<form:hidden path="surveyId"/>
 	<form:hidden path="scDeptId"/>
 	<form:hidden path="quesId"/>
 	<form:hidden path="quesNm"/>
 	<form:hidden path="idx"/>
 	<form:hidden path="subIdx"/>

	<div class="sch-bx">
		<ul>
			<li>
				<label for="findSurveyId"><spring:message code="word.survNm"/></label>
				<form:select path="findSurveyId" class="select wx400" >
					<form:options items="${surveyList}"  itemLabel="surveyNm" itemValue="surveyId" />
				</form:select>
			</li>
			<li>
				<label for="findChartGbn"><spring:message code="word.chartGbn"/></label>
				<form:select path="findChartGbn" class="select wx100" items="${codeUtil:getCodeList('374')}" itemLabel="codeNm" itemValue="codeId">
				</form:select>
			</li>
			<li>
				<label for="findQuesGbnId"><spring:message code="word.questionGbn"/></label>
				<form:select path="findQuesGbnId" class="select wx100">
					<option value=""><spring:message code="word.all" /></option>
					<form:options items="${codeUtil:getCodeList('371')}" itemLabel="codeNm" itemValue="codeId"/>
				</form:select>
			</li>
		</ul>
		<a href="#" class="btn-sch" onclick="searchList();return false;"><spring:message code="button.search"/></a>
	</div>
	<div class="btn-dw"></div>
	<div id="newForm">
		<div class="tbl-type02">
			<table summary="">
				<caption></caption>
				<colgroup>
					<col width="15%"/>
					<col width="35%"/>
					<col width="15%"/>
					<col width="35%"/>
				</colgroup>
				<tbody>
					<tr>
						<th scope="row"><label for="survNm"><spring:message code="word.survNm"/></label></th>
						<td ><span id="surveyNm"></span></td>
						<th scope="row"><label for="surveyId"><spring:message code="word.allSurvTgtCnt"/></label></th>
						<td ><span id="allUsrCnt"></span></td>
					</tr>
					<tr>
						<th scope="row"><label for="survNm"><spring:message code="word.allSurvAnsCnt"/></label></th>
						<td ><span id="ansUsrCnt"></span></td>
						<th scope="row"><label for="surveyId"><spring:message code="word.allContrastParticipationRate"/></label></th>
						<td ><span id="particpationRate"></span></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="mt10"></div>
		<div class="page-noti">
			<ul>
				<li><spring:message code="system.system.survey.survResult.info1"/></li>
				<li><spring:message code="system.system.survey.survResult.info2"/></li>
			</ul>
		</div>
	</div>

	<div id="bodyDiv">
		<div class="divDualBox hx410 mt30 hide" id="tempDiv">
			<div class="ptitle"></div>
			<div class="divFloatLeft">
				<div class="tbl-type02">
					<table summary="">
						<caption></caption>
						<colgroup>
							<col width="40%"/>
							<col width="20%"/>
							<col width="20%"/>
							<col width="20%"/>
						</colgroup>
						<tbody>
							<tr class="trTemp">
								<th scope="row" class="txt-c pl0"><label for=""><spring:message code="word.ansItem"/></label></th>
								<th scope="row" class="txt-c pl0"><label for=""><spring:message code="word.ansUsrCnt"/></label></th>
								<th scope="row" class="txt-c pl0"><label for=""><spring:message code="word.ansUsrRate"/></label></th>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="divFloatLeft">
				<div id="typeChart" class="hx340 bdr-a1">
					<div id="actDetailChart" class="hx200">
						<div id="lineChart" class="wx600 hx300 pdt10">
							<canvas class=""></canvas>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</form:form>

