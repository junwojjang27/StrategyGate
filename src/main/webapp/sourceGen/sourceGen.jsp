<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
<title>Source Code Generator</title>
<script type="text/javascript" src="./setting/js/angular.min.js"></script>
<script type="text/javascript" src="./setting/js/angular-route.js"></script>
<script type="text/javascript" src="./setting/js/jquery-1.11.0.min.js"></script>
<link rel="stylesheet" type="text/css" href="./setting/css/sourceGen.css"/>
<%
	String context= request.getContextPath();
	String url= request.getRequestURL().toString();
	String uri= request.getRequestURI();
	String contextUrl = "";

	contextUrl = url.replace(uri, "")+context;
%>
<script type="text/javascript">

	var app = angular.module('genApp',["ngRoute"]);
	app.controller('genCtrl',function($scope,$http){

		$scope.pathCheckYn = "N";

		$scope.loadData = function(){

			$scope.param = $.param({tbNm:$("#tbNm").val()});
			$http({
				method:'POST',
				url:'<%=contextUrl%>/sourceGen/setting/src/dbconn.jsp',
				data:$scope.param,
				headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
			}).then(function success(response) {

				if(response.data.columnGridData == undefined){
					alert("데이터 가져오기가 실패하였습니다. 테이블명이 올바른지 확인 하세요.");
				}else{
					$scope.datas = response.data.columnGridData;
					$scope.columnData = response.data.columnData;
					$scope.columnKeyData = response.data.columnKeyData;
				}

			},function error(response) {
				alert("데이터 가져오기가 실패하였습니다. db접속정보를 확인해 주세요.");
			});
		}

		$scope.checkFile = function(){

			if($("#pathLev1").val().trim() == ""){alert("분류를 입력해 주세요."); $("#pathLev1").focus(); return;}
			if($("#pathLev2").val().trim() == ""){alert("대메뉴를 입력해 주세요."); $("#pathLev2").focus(); return;}
			if($("#pathLev3").val().trim() == ""){alert("중메뉴를 입력해 주세요."); $("#pathLev3").focus(); return;}
			if($("#pathLev4").val().trim() == ""){alert("소메뉴를 입력해 주세요."); $("#pathLev4").focus(); return;}

			$scope.param = $.param({pathLev1:$("#pathLev1").val(),pathLev2:$("#pathLev2").val(),pathLev3:$("#pathLev3").val(),pathLev4:$("#pathLev4").val()});
			$http({
				method:'POST',
				url:'<%=contextUrl%>/sourceGen/setting/src/fileCheck.jsp',
				data:$scope.param,
				headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
			}).then(function success(response) {

				if(response.data.existYn == undefined){
					alert("데이터 가져오기가 실패하였습니다. 기본 파일 path 를 확인해 주세요.");
				}else{
					$scope.pathCheckYn = "Y";
					$scope.existYn = response.data.existYn;
					if($scope.existYn == 'Y'){
						$("#spanExistYn").empty();
						$("#spanExistYn").html("<font color=\"red\">해당path의 폴더가 존재 합니다.");
					}else{
						$("#spanExistYn").empty();
						$("#spanExistYn").html("<font color=\"blue\">해당path의 폴더가 존재하지 않습니다.");
					}
				}

			},function error(response) {
				alert("데이터 가져오기가 실패하였습니다. 기본 파일 path 를 확인해 주세요.");
			});
		}
	});


	function loadFileCheck(){

		if($("#devName").val().trim() == ""){alert("개발자명을 입력해 주세요."); $("#devName").focus(); return;}
		if($("#pathLev1").val().trim() == ""){alert("분류를 입력해 주세요."); $("#pathLev1").focus(); return;}
		if($("#pathLev2").val().trim() == ""){alert("대메뉴를 입력해 주세요."); $("#pathLev2").focus(); return;}
		if($("#pathLev3").val().trim() == ""){alert("중메뉴를 입력해 주세요."); $("#pathLev3").focus(); return;}
		if($("#pathLev4").val().trim() == ""){alert("소메뉴를 입력해 주세요."); $("#pathLev4").focus(); return;}
		if($("#pageNm").val().trim() == ""){alert("화면명을 입력해 주세요."); $("#pageNm").focus(); return;}
		if($("#tbNm").val().trim() == ""){alert("테이블명을 입력 후 검색해 주세요."); $("#tbNm").focus(); return;}

		if($("input[name='columnName']").size() == 0){alert("테이블명을 입력 후 검색해서 컬럼정보가 나와야 합니다."); $("#tbNm").focus(); return;}

		//alert($("#pathCheckYnStr").val());
		//alert($("#existYnStr").val());

		if($("#pathCheckYnStr").val() == "N"){alert("path check 버튼은 선택하여 존재유무를 확인하세요."); return;}
		if($("#existYnStr").val()=="Y"){
			if(confirm("해당path 가 존재합니다. 재생성 하시겠습니까?")){
				loadFile();
			}else{
				return;
			}
		}else{
			loadFile();
		}
	}

	function loadFile(){
		var form = document.getElementsByName("formGen")[0];
		form.submit();
	}


</script>

</head>
<body ng-app="genApp" class="genBodyArea" id="genBodyArea">
<h2>SourceCode Generator</h2>
<form id="formGen" name="formGen" method="post" action="<%=contextUrl%>/sourceGen/setting/src/createFile.jsp">
<div ng-controller="genCtrl">
    <div style="width:100%;height:20px;">
		<input type="button" class="positionRight" id="srcGen" alt="SourceCode Generator" value="execute" onclick="loadFileCheck();"/>
	</div>
	<div style="background-color:yellow;">
	    <input type="hidden" id="columnData" name="columnData" value="{{columnData}}" />
		<input type="hidden" id="columnKeyData" name="columnKeyData" value="{{columnKeyData}}"/>
		<input type="hidden" id="pathCheckYnStr" name="pathCheckYnStr" value="{{pathCheckYn}}"/>
		<input type="hidden" id="existYnStr" name="existYnStr" value="{{existYn}}"/>
		<div class="genSrcArea" >
		    <p>SOURCE:</p>
			<ul>
				<li><p>개발자 이름<input type="text" id="devName" name="devName" class="w100p" placeholder="너의이름을  한글로 입력하세요."/></p></li>
				<li>
					<p>package:</p>
					<div>
					<p>ex> package: kr.ispark.<font color="yellow"><b>bsc.scDept.scDept.scDeptMng</b></font></p>
					</div>
					분류:조직성과(bsc)/개인성과(prs)
					<select id="pathLev1" name="pathLev1" class="w80px" ng-model="pathLev1">
					    <option value="">--선택--</option>
						<option value="bsc">bsc</option>
						<option value="prs">prs</option>
						<option value="gov">gov</option>
						<option value="system">system</option>
						<option value="superMng">superMng</option>
					</select>
					<br />
					대매뉴
					<select id="pathLev2" name="pathLev2" class="w200px" ng-model="pathLev2">
						<option value="">--선택--</option>
						<option value="mon">mon</option>
						<option value="base">base</option>
						<option value="actual">actual</option>
						<option value="eval">eval</option>
						<option value="orgEval">orgEval</option>
						<option value="gov">gov</option>
						<option value="system">system</option>
						<option value="pae">pae</option>
						<option value="pce">pce</option>
						<option value="prsTot">prsTot</option>
						<option value="superMng">superMng</option>
					</select>
					<br />
					중메뉴
					<select id="pathLev3" name="pathLev3" class="w200px" ng-model="pathLev3">
					    <option value="">--선택--</option>
						<option value="dashboard">dashboard</option>
						<option value="orgOutput">orgOutput</option>
						<option value="divOutput">divOutput</option>
						<option value="compare">compare</option>
						<option value="scDept">scDept</option>
						<option value="strategy">strategy</option>
						<option value="metric">metric</option>
						<option value="period">period</option>
						<option value="actual">actual</option>
						<option value="planActual">planActual</option>
						<option value="evaluation">evaluation</option>
						<option value="menu">menu</option>
						<option value="code">code</option>
						<option value="comun">comun</option>
						<option value="tot">tot</option>
						<option value="system">system</option>
						<option value="exec">exec</option>
						<option value="batch">batch</option>
						<option value="base">base</option>
						<option value="coach">coach</option>
						<option value="eval">eval</option>
						<option value="measAct">measAct</option>
						<option value="result">result</option>
						<option value="comp">comp</option>
						<option value="superMng">superMng</option>
					</select>
					<br />
					소메뉴
					<input type="text" class="w300px" id="pathLev4" name="pathLev4" ng-model="pathLev4" />
					<input type="button" alt="searchPath" value="check" ng-click="checkFile();"/>
					<br />
					<span id="spanExistYn"><font color="yellow">구분/대메뉴/중메뉴/소메뉴 선택 및 입력 후 check 눌러주세요.</font></span>
					<div>
						<p>
							package: kr.ispark.{{pathLev1}}.{{pathLev2}}.{{pathLev3}}.{{pathLev4}} <br />
							jsp: {{pathLev1}}.{{pathLev2}}.{{pathLev3}}.{{pathLev4}}
						</p>
					</div>
				</li>
				<li><p>페이지명</p> <input type="text" id="pageNm" name="pageNm" class="w100p" /></li>
				<li><p>페이지설명 </p><textarea id="pageDesc" name="pageDesc" rows="5" class="w100p" ></textarea></li>
			</ul>
		</div>
		<div class="genDbArea">
			<p>DATABASE:</p>
			<ul>
				<li>
				<p>테이블명 &nbsp;<input type="text" id="tbNm" name="tbNm" class="w300px" placeholder="테이블명을 하나만 작성해 주세요."/>
				&nbsp;<input type="button" id="schTb" alt="테이블  meta 정보 검색" value="search" ng-click="loadData();"/></p>
				</li>
			</ul>
			<table ng-table="tableList">
				<tr>
					<th>컬럼</th>
					<th>컬럼명</th>
					<th>타입</th>
					<th>길이</th>
					<th>pk여부</th>
				</tr>
				<!-- for loop -->
				<tr ng-repeat="data in datas track by $index">
					<td>{{data.columnName}}
						<input type="hidden" name="columnName" value="{{data.columnName}}"/>
						<input type="hidden" name="tableName" value="{{data.tableName}}"/>
						<input type="hidden" name="tableNameKor" value="{{data.tableNameKor}}"/>
						<input type="hidden" name="dataPrecision" value="{{data.dataPrecision}}"/>
						<input type="hidden" name="dataScale" value="{{data.dataScale}}"/>
						<input type="hidden" name="columnNameKor" value="{{data.columnNameKor}}"/>
						<input type="hidden" name="dataType" value="{{data.dataType}}"/>
						<input type="hidden" name="dataLength" value="{{data.dataLength}}"/>
						<input type="hidden" name="pkYn" value="{{data.pkYn}}"/>
					</td>
					<td>{{data.columnNameKor}}</td>
					<td>{{data.dataType}}</td>
					<td>{{data.dataLength}}</td>
					<td>{{data.pkYn}}</td>
				</tr>
			</table>
		</div>
	</div>
</div>
</form>
</body>
</html>