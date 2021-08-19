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
	
	$(function(){
		$("#loadingBar").hide();
		
		$(window).ajaxStart(function(){
			$("#loadingBar").show();
		});
		
		$(window).ajaxStop(function(){
			$("#loadingBar").hide();
		});
		
		if($("#sdbtype").val() === 'oracle'){
			$("#sdbName").val("");
			$("#sdbName").prop("disabled",true);
		}else if($("#sdbtype").val() === 'mysql'){
			$("#sdbName").prop("disabled",false);
		}
		
		if($("#tdbtype").val() === 'oracle'){
			$("#tdbName").val("");
			$("#tdbName").prop("disabled",true);
		}else if($("#tdbtype").val() === 'mysql'){
			$("#tdbName").prop("disabled",false);
		}
		
		$("#sdbtype").on("change",function(){
			if($("#sdbtype").val() === 'oracle'){
				$("#sdbName").val("");
				$("#sdbName").prop("disabled",true);
			}else if($("#sdbtype").val() === 'mysql'){
				$("#sdbName").prop("disabled",false);
			}
		});
		
		$("#tdbtype").on("change",function(){
			if($("#tdbtype").val() === 'oracle'){
				$("#tdbName").val("");
				$("#tdbName").prop("disabled",true);
			}else if($("#tdbtype").val() === 'mysql'){
				$("#tdbName").prop("disabled",false);
			}
		});
		
		$("#sql").hide();
	});
	
	var app = angular.module('genApp',["ngRoute"]);
	app.controller('genCtrl',function($scope,$http){

		$scope.sloadData = function(){

			$scope.sparam = $.param({url:$("#surl").val(),driver:$("#sdriver").val(),user:$("#suser").val(),passwd:$("#spasswd").val(),dbtype:$("#sdbtype").val(),dbName:$("#sdbName").val(),searchKey:$("#searchKey").val()});
			$http({
				method:'POST',
				url:'<%=contextUrl%>/sourceGen/setting/dbgenSrc/dbconn.jsp',
				data:$scope.sparam,
				headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
			}).then(function success(response) {

				if(response.data.columnGridData == undefined){
					alert("데이터 가져오기가 실패하였습니다. 테이블명이 올바른지 확인 하세요.");
				}else{
					$scope.sdatas = response.data.columnGridData;
				}

			},function error(response) {
				alert("데이터 가져오기가 실패하였습니다. db접속정보를 확인해 주세요.");
			});
		}
		
		$scope.tloadData = function(){

			$scope.tparam = $.param({url:$("#turl").val(),driver:$("#tdriver").val(),user:$("#tuser").val(),passwd:$("#tpasswd").val(),dbtype:$("#tdbtype").val(),dbName:$("#tdbName").val()});
			$http({
				method:'POST',
				url:'<%=contextUrl%>/sourceGen/setting/dbgenSrc/dbconn.jsp',
				data:$scope.tparam,
				headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
			}).then(function success(response) {

				if(response.data.columnGridData == undefined){
					alert("접속이 안됩니다. 다시 확인해 주세요.");
					$scope.tchk = "N";
				}else{
					alert("접속확인 되었습니다.");
					$scope.tchk = "Y";
				}

			},function error(response) {
				alert("데이터 가져오기가 실패하였습니다. db접속정보를 확인해 주세요.");
				$scope.tchk = "N";
			});
		}
		
		$scope.insertData = function(){
			
			if($("#tchk").val() != "Y"){
				alert("target db접속확인을 해주세요.");
			}
			
			if($("table td").size() == 0){
				alert("source db접속확인을 해주세요.");
			}
			
			var tableString = "";
			$(":input[name='chkTable']:checked").each(function(i,e){
				tableString += e.value+"|";
			});
			$scope.param = $.param({tables:tableString,scompId:$("#scompId").val(),tcompId:$("#tcompId").val(),
									turl:$("#turl").val(),tdriver:$("#tdriver").val(),tuser:$("#tuser").val(),tpasswd:$("#tpasswd").val(),tdbtype:$("#tdbtype").val(),
									surl:$("#surl").val(),sdriver:$("#sdriver").val(),suser:$("#suser").val(),spasswd:$("#spasswd").val(),sdbtype:$("#sdbtype").val(),
									sdbName:$("#sdbName").val(),tdbName:$("#tdbName").val()});
			
			$http({
				method:'POST',
				url:'<%=contextUrl%>/sourceGen/setting/dbgenSrc/dbInsert.jsp',
				data:$scope.param,
				headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
			}).then(function success(response) {
				var log = response.data.logBuf;
				$("#resultTest").html(log);
				alert("완료되었습니다.");

			},function error(response) {
				alert("데이터 이관이 문제가 발생하였습니다. 정보를 다시 확인해 주세요.");
			});
		}
	});
	
	function allCheck(){
		$(":input[name='chkTable']").prop("checked",true);
	}
	function allNoCheck(){
		$(":input[name='chkTable']").prop("checked",false);
	}
	function showsql(){$("#sql").slideToggle();$("#dataGen").slideToggle();}
	
</script>

</head>
<body ng-app="genApp" id="genBodyArea2">
<h2>
	DB DATA Move
	<input type="button" onclick="showsql();" value="oracle to mysql schema conversion sql(oracle version)"/>
</h2>
<form id="formGen" name="formGen" method="post" >
<div ng-controller="genCtrl" id="dataGen">
	<input type="hidden" id="tchk" name="tchk" value="{{tchk}}"/>
	<div style="background-color:yellow;">
		<div class="genSrcArea" >
		    <p><b>SOURCE:</b></p>
			<ul>
				<li>
					<span style="width:80px;display:inline-block;">dbtype</span><select id="sdbtype" name="sdbtype" ><option value="oracle">oracle</option><option value="mysql">mysql</option></select>
					<span style="width:80px;display:inline-block;">dbName</span><input type="text" id="sdbName" name="sdbName"/>
				</li>
				<li><span style="width:80px;display:inline-block;">URL</span><input type="text" id="surl" name="surl" class="w80p" placeholder="sourceUrl" value="jdbc:log4jdbc:oracle:thin:@192.168.10.120:1521:ispark"/></li>
				<li><span style="width:80px;display:inline-block;">Driver</span><input type="text" id="sdriver" name="sdriver" class="w80p" placeholder="sourceDriver" value="net.sf.log4jdbc.DriverSpy"/></li>
				<li><span style="width:80px;display:inline-block;">user</span><input type="text" id="suser" name="suser" class="w80p" placeholder="sourceUser" value="BSCV6"/></li>
				<li><span style="width:80px;display:inline-block;">passwd</span><input type="text" id="spasswd" name="spasswd" class="w80p" placeholder="sourcePasswd" value="BSCV6"/></li>
				<li><span style="width:80px;display:inline-block;">compId</span><input type="text" id="scompId" name="scompId" class="w80p" placeholder="sourceCompId"/></li>
			</ul>
			<input type="button" ng-click="sloadData();" value="테이블 목록 조회"/>
			<p><b>TARGET:</b></p>
			<ul>
				<li>
					<span style="width:80px;display:inline-block;">dbtype</span><select id="tdbtype" name="tdbtype" ><option value="oracle">oracle</option><option value="mysql">mysql</option></select>
					<span style="width:80px;display:inline-block;">dbName</span><input type="text" id="tdbName" name="tdbName"/>
				</li>
				<li><span style="width:80px;display:inline-block;">URL</span><input type="text" id="turl" name="turl" class="w80p" placeholder="targetUrl" value="jdbc:mariadb://192.168.10.120:3306/BSCV6_DB"/></li>
				<li><span style="width:80px;display:inline-block;">Driver</span><input type="text" id="tdriver" name="tdriver" class="w80p" placeholder="targetDriver" value="org.mariadb.jdbc.Driver"/></li>
				<li><span style="width:80px;display:inline-block;">user</span><input type="text" id="tuser" name="tuser" class="w80p" placeholder="targetUser" value="BSCV6"/></li>
				<li><span style="width:80px;display:inline-block;">passwd</span><input type="text" id="tpasswd" name="tpasswd" class="w80p" placeholder="targetPasswd" value="BSCV6"/></li>
				<li><span style="width:80px;display:inline-block;">compId</span><input type="text" id="tcompId" name="tcompId" class="w80p" placeholder="targetCompId"/></li>
			</ul>
			<input type="button" ng-click="tloadData();" value="접속확인"/>
		</div>
		<div class="genDbArea" style="width:400px;height:100%;">
			<p>SOURCE TABLES:</p>
			<input type="button" onclick="allCheck()" value="전체선택"/>&nbsp;<input type="button" onclick="allNoCheck()" value="전체선택해제"/>&nbsp;<input type="button" ng-click="insertData();" value="데이터이관(delete/insert)"/>
			<br /><input type="text" id="searchKey" name="searchKey" /><input type="button" ng-click="sloadData();" value="찾기"/>
			<table ng-table="stableList" >
				<tr>
					<th>테이블명</th>
				</tr>
				<!-- for loop -->
				<tr ng-repeat="data in sdatas track by $index" style="font-size:11px;">
					<td><input type="checkbox" id="chkTable" name="chkTable" value="{{data.tableName}}"/></td>
					<td>{{data.tableName}}</td>
				</tr>
			</table>
		</div>
		<div class="genDbArea" style="width:400px;height:100%;">
			<p>RESULT:</p>
			<div id="resultTest" style="font-size:10px;height:100%;">
			</div>
		</div>
	</div>
</div>
<div id="sql">
	<textarea style="width:1500px;height:100%;" rows="50">
		/* mysql 테이블 생성 쿼리 from oracle */
		SELECT CASE WHEN NUM = '1' THEN 'CREATE TABLE IF NOT EXISTS '||TABLE_NAME||'('||COLUMN_NAME||' '||COLDEFINE||','
		            WHEN NUM = CNT THEN COLUMN_NAME||' '||COLDEFINE||');'
		            ELSE COLUMN_NAME||' '||COLDEFINE ||',' END 
		FROM
		(
		SELECT TABLE_NAME,COLUMN_NAME, 
		       CASE WHEN DATA_TYPE IN ('CLOB') THEN 'TEXT'
		            WHEN DATA_TYPE IN ('VARCHAR2','CLOB') THEN 'VARCHAR('||CHAR_LENGTH||')'
		            WHEN DATA_TYPE  = 'NUMBER' AND DATA_PRECISION IS NULL THEN 'NUMERIC'
		            WHEN DATA_TYPE  = 'NUMBER' AND DATA_PRECISION IS NOT NULL THEN 'NUMERIC('||DATA_PRECISION||','||DATA_SCALE||')'
		            WHEN DATA_TYPE  = 'CHAR' THEN 'CHAR('||CHAR_LENGTH||')'
		            WHEN DATA_TYPE  = 'DATE' THEN 'DATETIME' END AS COLDEFINE,
		       DATA_TYPE, CHAR_LENGTH,DATA_LENGTH, 
		       DATA_PRECISION,DATA_SCALE,NULLABLE,
		       ROW_NUMBER () OVER (PARTITION BY TABLE_NAME ORDER BY COLUMN_ID) AS NUM,
		       COUNT(1) OVER (PARTITION BY TABLE_NAME) AS CNT
		  FROM USER_TAB_COLUMNS A
		 WHERE TABLE_NAME LIKE 'COM%'
		);
		
		/* mysql 테이블 comment 생성  쿼리 from oracle */
		SELECT 'ALTER TABLE '||TABLE_NAME||' COMMENT='||CHR(39)||COMMENTS||CHR(39)||';'
		  FROM USER_TAB_COMMENTS
		WHERE TABLE_TYPE = 'TABLE'
		  AND TABLE_NAME LIKE 'COM%';
		  
		/* mysql 컬럼 comment 생성  쿼리 from oracle */
		SELECT 'ALTER TABLE '||A.TABLE_NAME||' MODIFY COLUMN '||A.COLUMN_NAME||' '||COLDEFINE||' COMMENT '||CHR(39)||A.COMMENTS||CHR(39)||';'
		FROM
		(
		SELECT A.TABLE_NAME,A.COLUMN_NAME, 
		       CASE WHEN A.DATA_TYPE IN ('CLOB') THEN 'TEXT'
		            WHEN A.DATA_TYPE IN ('VARCHAR2','CLOB') THEN 'VARCHAR('||CHAR_LENGTH||')'
		            WHEN A.DATA_TYPE  = 'NUMBER' AND A.DATA_PRECISION IS NULL THEN 'NUMERIC'
		            WHEN A.DATA_TYPE  = 'NUMBER' AND A.DATA_PRECISION IS NOT NULL THEN 'NUMERIC('||A.DATA_PRECISION||','||A.DATA_SCALE||')'
		            WHEN A.DATA_TYPE  = 'CHAR' THEN 'CHAR('||A.CHAR_LENGTH||')'
		            WHEN A.DATA_TYPE  = 'DATE' THEN 'DATETIME' END AS COLDEFINE,
		       A.DATA_TYPE, A.CHAR_LENGTH,A.DATA_LENGTH, 
		       A.DATA_PRECISION,A.DATA_SCALE,A.NULLABLE, B.COMMENTS,
		       ROW_NUMBER () OVER (PARTITION BY A.TABLE_NAME ORDER BY A.COLUMN_ID) AS NUM,
		       COUNT(1) OVER (PARTITION BY A.TABLE_NAME) AS CNT
		  FROM USER_TAB_COLUMNS A
		       INNER JOIN USER_COL_COMMENTS B
		       ON A.TABLE_NAME = B.TABLE_NAME
		       AND A.COLUMN_NAME = B.COLUMN_NAME
		 WHERE A.TABLE_NAME LIKE 'COM%'
		) A;   
	
		/* PK 생성  쿼리 from oracle */
		SELECT 'ALTER TABLE '||A.TABLE_NAME||' ADD CONSTRAINT '||A.CONSTRAINT_NAME||' PRIMARY KEY ('||LISTAGG(A.COLUMN_NAME, ',') WITHIN GROUP (ORDER BY NUM)||');' 
		FROM
		(
		SELECT A.CONSTRAINT_NAME,
		       A.TABLE_NAME,
		       B.COLUMN_NAME,
		       ROW_NUMBER() OVER (PARTITION BY B.TABLE_NAME ORDER BY B.COLUMN_POSITION ) AS NUM,
		       COUNT(1) OVER (PARTITION BY B.TABLE_NAME) AS CNT
		  FROM USER_CONSTRAINTS A
		       INNER JOIN USER_IND_COLUMNS B
		       ON A.TABLE_NAME = B.TABLE_NAME
		       AND A.INDEX_NAME = B.INDEX_NAME
		 WHERE A.OWNER = 'BSCV6'
		   AND A.TABLE_NAME LIKE 'COM%'
		) A
		GROUP BY A.TABLE_NAME,A.CONSTRAINT_NAME;
	</textarea>
</div>
</form>
<div id="loadingBar" class="loading"><img id="loading_img" alt="loading" src="http://localhost:8888/StrategyGate/sourceGen/setting/image/genloadingbar.gif" /></div>
</body>
</html>