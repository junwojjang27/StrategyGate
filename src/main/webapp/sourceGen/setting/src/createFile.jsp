<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.io.*" %>
<%@page import="java.util.Calendar" %>
<%@page import="java.nio.file.NotDirectoryException"%>
<%@page import="com.google.common.io.Resources"%>
<%@ include file="config.jsp" %>
<%!
public String toUtf8(String str){
	String param = str;
	if(param != null) try{param = new String(param.getBytes("iso-8859-1"), "UTF-8");}catch(Exception e){e.getStackTrace();};
	return param;
}

public String toCamel(String str){
	
	String camelCase = "";
	String orginal = str;
	String allLowcase = orginal.toLowerCase();
	
	String[] allLowcaseArray;
	if(-1 < allLowcase.indexOf('_')){
		allLowcaseArray = allLowcase.split("_",-1);
		int size = allLowcaseArray.length;
		String upperChar = "";
		if(allLowcaseArray != null && 0 < size){
			for(int a=0 ; a<size ; a++){
				if(allLowcaseArray[a] != null && 0 < allLowcaseArray[a].length()){
					if(0<camelCase.trim().length()){
						upperChar = allLowcaseArray[a].substring(0,1).toUpperCase();
						camelCase += upperChar+allLowcaseArray[a].substring(1);
					}else{
						camelCase += allLowcaseArray[a];
					}
				}
			}			
		}
	}else{
		camelCase = allLowcase;
	}
	return camelCase;
}

//앞글자만 UPPER 로 변경
public String toNonCamel(String str){
	
	String nonCamelCase = "";
	String upperChar = "";
	String orginal = str;
	
	upperChar = orginal.substring(0,1).toUpperCase();
	nonCamelCase = upperChar+orginal.substring(1);
	
	return nonCamelCase;
}
%>
<%
	
	Calendar calendar = Calendar.getInstance();
	int mon = calendar.get(Calendar.MONTH)+1;
	int year = calendar.get(Calendar.YEAR);
	int day = calendar.get(Calendar.DATE);
	
	/*파라미터 전달받음*/	
	String tableNameParam = request.getParameter("tbNm"); 	
	String devNameParam = request.getParameter("devName");
	String pageNmParam = request.getParameter("pageNm");
	String pageDescParam = request.getParameter("pageDesc");
	String pathLev1Param = request.getParameter("pathLev1");
	String pathLev2Param = request.getParameter("pathLev2");
	String pathLev3Param = request.getParameter("pathLev3");
	String pathLev4Param = request.getParameter("pathLev4");
	
	String[] tableNames = request.getParameterValues("tableName");
	String[] tableNameKors = request.getParameterValues("tableNameKor");
	String[] columnNames = request.getParameterValues("columnName");
	String[] columnNameKors = request.getParameterValues("columnNameKor");
	String[] dataLengths = request.getParameterValues("dataLength");
	String[] dataPrecisions = request.getParameterValues("dataPrecision");
	String[] dataScales = request.getParameterValues("dataScale");
	String[] dataTypes = request.getParameterValues("dataType");
	String[] pkYns = request.getParameterValues("pkYn");
	String[] camelColumnNames = null;
	if(columnNames != null && 0<columnNames.length){
		camelColumnNames = new String[columnNames.length];
		for(int i=0 ; i<columnNames.length ; i++){
			camelColumnNames[i] = toCamel(columnNames[i]);	
		}
	}	
	String[] columnNameUtf8s = null;
	if(columnNameKors != null && 0<columnNameKors.length){
		columnNameUtf8s = new String[columnNameKors.length];
		for(int i=0 ; i<columnNameKors.length ; i++){
			columnNameUtf8s[i] = toUtf8(columnNameKors[i]);	
		}
	}
	
	/*필요변수 세팅*/
	String packageBarPath = "/"+pathLev1Param+"/"+pathLev2Param+"/"+pathLev3Param+"/"+pathLev4Param;
    String packageDotPath = "."+pathLev1Param+"."+pathLev2Param+"."+pathLev3Param+"."+pathLev4Param;
    String queryNamePath = pathLev2Param+"."+pathLev3Param+"."+pathLev4Param;
	String fullPackageBarPath = "kr/ispark"+packageBarPath;
	String fullPackageDotPath = "kr.ispark"+packageDotPath;
	String camelPageNm = pathLev4Param;
	String nonCamelPageNm = toNonCamel(pathLev4Param);
	String koPageNm = toUtf8(pageNmParam);
	String devName = toUtf8(devNameParam);
	String pageDesc = toUtf8(pageDescParam);
	String devDate = String.valueOf(year)+"-" + (mon<10?"0":"") + String.valueOf(mon)+"-" + (day<10?"0":"") +String.valueOf(day);
	
	/*ㅋ필요쿼리변수*/
	StringBuffer selectQueryBuffer = new StringBuffer();
	StringBuffer selectDetailQueryBuffer = new StringBuffer();
	StringBuffer insertColQueryBuffer = new StringBuffer();
	StringBuffer insertValQueryBuffer = new StringBuffer();
	StringBuffer updateQueryBuffer = new StringBuffer();
	StringBuffer deleteQueryBuffer = new StringBuffer();
	
	if(columnNames != null && 0<columnNames.length){
		
		boolean isDeleteDt = false;
		for(String str:columnNames){if(("DELETE_DT").equals(str))isDeleteDt=true;}
		
		selectQueryBuffer.append("\t\tSELECT\n");
		selectDetailQueryBuffer.append("\t\tSELECT\n");
		insertColQueryBuffer.append("\t\tINSERT INTO "+tableNameParam+"(\n");
		insertValQueryBuffer.append("\t\t\nVALUES (\n");
		updateQueryBuffer.append("\t\tUPDATE "+tableNameParam+"\n"+"\t\tSET\n");
		if(isDeleteDt){deleteQueryBuffer.append("\t\tUPDATE "+tableNameParam+"\n"+"\t\tSET\n");}else{deleteQueryBuffer.append("\t\tDELETE FROM "+tableNameParam+"\n");}
		
		//SELECT, UPDATE, DELETE 컬럼절
		for(int i=0 ; i<columnNames.length ; i++){
			selectQueryBuffer.append("\t\t\t"+columnNames[i]);
			selectDetailQueryBuffer.append("\t\t\t"+columnNames[i]);
			insertColQueryBuffer.append("\t\t\t"+columnNames[i]);
			insertValQueryBuffer.append("\t\t\t#{"+camelColumnNames[i]+"}");
			if("N".equals(pkYns[i]))updateQueryBuffer.append("\t\t\t"+columnNames[i]+"=#{"+camelColumnNames[i]+"}");
			if(isDeleteDt&&"DELETE_DT".equals(columnNames[i]))deleteQueryBuffer.append("\t\t\tDELETE_DT = SYSDATE\n");
			
			if(i+1 != columnNames.length){
				selectQueryBuffer.append(",\n");
				selectDetailQueryBuffer.append(",\n");
				insertColQueryBuffer.append(",\n");
				insertValQueryBuffer.append(",\n");
				if("N".equals(pkYns[i]))updateQueryBuffer.append(",\n");
			}else{
				selectQueryBuffer.append("\n");
				selectDetailQueryBuffer.append("\n");
				insertColQueryBuffer.append(")\n");
				insertValQueryBuffer.append(")\n");
				if("N".equals(pkYns[i]))updateQueryBuffer.append("\n");
			}
		}
		
		//FROM 절
		selectQueryBuffer.append("\t\tFROM "+tableNameParam+"\n");
		selectDetailQueryBuffer.append("\t\tFROM "+tableNameParam+"\n");
		
		//WEHRE 절
		selectQueryBuffer.append("\t\tWHERE 1=1\n");
		selectDetailQueryBuffer.append("\t\tWHERE 1=1\n");
		updateQueryBuffer.append("\t\tWHERE 1=1\n");
		deleteQueryBuffer.append("\t\tWHERE 1=1\n");
		if(pkYns != null && 0<pkYns.length){
			for(int j=0 ; j<pkYns.length ; j++){
				if(pkYns[j] != null && "Y".equals(pkYns[j])){
					selectDetailQueryBuffer.append("\t\t\tAND "+columnNames[j]+"=#{"+toCamel(columnNames[j])+"}\n");
					updateQueryBuffer.append("\t\t\tAND "+columnNames[j]+"=#{"+toCamel(columnNames[j])+"}\n");
					deleteQueryBuffer.append("\t\t\tAND "+columnNames[j]+"=#{"+toCamel(columnNames[j])+"}\n");
				}	
			}
		}
	}
		
	String selectQuery = selectQueryBuffer.toString();
	String selectDetailQuery = selectDetailQueryBuffer.toString();
	String insertQuery = insertColQueryBuffer.append(insertValQueryBuffer).toString();
	String updateQuery = updateQueryBuffer.toString();
	String deleteQuery = deleteQueryBuffer.toString();
	
	//그리드 변수
	String lastKey = "";
	int index = 0;
	if(pkYns != null && 0<pkYns.length){
		for(String str:pkYns){if("Y".equals(str))index++;}	
		lastKey = toCamel(columnNames[index-1]);
	}
	String nonCamelLastKey = toNonCamel(lastKey);
	StringBuffer gridModelBuffer = new StringBuffer();
	StringBuffer bindKeysBuffer = new StringBuffer();
	
	StringBuffer voToFormKeyBuffer = new StringBuffer();
	StringBuffer resetFormColumnBuffer = new StringBuffer();
	StringBuffer enrollmentHtmlBuffer = new StringBuffer();
	
	StringBuffer voColumnsBuffer = new StringBuffer();
	StringBuffer valiatorColumnsBuffer = new StringBuffer();
	
	StringBuffer hiddenKeyBuffer = new StringBuffer();
	
	String dot = "";
	String requiredDot = "";
	String requiredStr = "";
	if(columnNames != null && 0<columnNames.length){
		for(int i=0 ; i<columnNames.length ; i++){
			if(i == columnNames.length-1){dot = "";}else{dot = ",";}
			if("SORT_ORDER".equals(columnNames[i])){
				gridModelBuffer.append("\t\t\t\t\t\t{name:\""+camelColumnNames[i]+"\",	index:\""+camelColumnNames[i]+"\",	width:100,	align:\"center\",	label:\""+columnNameUtf8s[i]+"\",\n");
				gridModelBuffer.append("\t\t\t\t\t\t\t" + "editable:true, edittype:\"text\", editrules:{integer:true}, editoptions:{maxlength:3}\n");
				gridModelBuffer.append("\t\t\t\t\t\t}"+dot+"\n");
			}else{
				if(pkYns != null && 0<pkYns.length){
					if("Y".equals(pkYns[i])){
						gridModelBuffer.append("\t\t\t\t\t\t{name:\""+camelColumnNames[i]+"\",	index:\""+camelColumnNames[i]+"\",	width:100,	align:\"center\",	label:\""+columnNameUtf8s[i]+"\""+dot+"\n");
						gridModelBuffer.append("\t\t\t\t\t\t\tformatter:function(cellvalue, options, rowObject) {\n");
						gridModelBuffer.append("\t\t\t\t\t\t\t\treturn \"<a href=\'#\' onclick='showDetail(\\\\\"\" + removeNull(rowObject."+camelColumnNames[i]+") + \"\\\\\");return false;'>\" + escapeHTML(removeNull(cellvalue)) + \"</a>\";\n");
						gridModelBuffer.append("\t\t\t\t\t\t\t},\n");
						gridModelBuffer.append("\t\t\t\t\t\t\tunformat:linkUnformatter\n");
						gridModelBuffer.append("\t\t\t\t\t\t}"+dot+"\n");
					}else{
						gridModelBuffer.append("\t\t\t\t\t\t{name:\""+camelColumnNames[i]+"\",	index:\""+camelColumnNames[i]+"\",	width:100,	align:\"center\",	label:\""+columnNameUtf8s[i]+"\"}"+dot+"\n");
					}	
				}	
			}
			
			if(pkYns != null && 0<pkYns.length){
				if("Y".equals(pkYns[i]) && !"COMP_ID".equals(columnNames[i])){
					bindKeysBuffer.append("\tf."+camelColumnNames[i]+".value = "+camelColumnNames[i]+";\n");
					hiddenKeyBuffer.append("\t<form:hidden path=\""+camelColumnNames[i]+"\"/>\n");
				}
			}
			
			if(!"COMP_ID".equals(columnNames[i]) && !"YEAR".equals(columnNames[i])){
				if(pkYns != null && 0<pkYns.length){
					if("Y".equals(pkYns[i])){requiredDot="<span class=\"red\">(*)</span>";}else{requiredDot="";}
				}

				
				enrollmentHtmlBuffer.append("\t\t\t\t\t<tr> \n");
				enrollmentHtmlBuffer.append("\t\t\t\t\t\t<th scope=\"row\"><label for=\""+camelColumnNames[i]+"\">"+columnNameUtf8s[i]+"</label>"+requiredDot+"</th> \n");
				enrollmentHtmlBuffer.append("\t\t\t\t\t\t<td ><form:input path=\""+camelColumnNames[i]+"\" class=\"t-box01\"/></td> \n");
				enrollmentHtmlBuffer.append("\t\t\t\t\t</tr> \n");
				
				voToFormKeyBuffer.append("\""+camelColumnNames[i]+"\""+dot);
				resetFormColumnBuffer.append("\""+camelColumnNames[i]+"\""+dot);
				
				voColumnsBuffer.append("\tprivate String "+camelColumnNames[i]+";\n");
			}
			
			if(pkYns != null && 0<pkYns.length){
				if(!"COMP_ID".equals(columnNames[i]) && !"YEAR".equals(columnNames[i])){
					/*
					if("Y".equals(pkYns[i])){requiredStr=",required";}else{requiredStr="";}
					}
					*/
					valiatorColumnsBuffer.append("\t\t\t<field property=\""+camelColumnNames[i]+"\" depends=\"maxlength\">\n");
					valiatorColumnsBuffer.append("\t\t\t\t<arg0 key=\""+columnNameUtf8s[i]+"\" resource=\"true\"/>\n");
					valiatorColumnsBuffer.append("\t\t\t\t<arg1 key=\""+dataLengths[i]+"\" resource=\"false\"/>\n");
					valiatorColumnsBuffer.append("\t\t\t\t<var>\n");
					valiatorColumnsBuffer.append("\t\t\t\t\t<var-name>maxlength</var-name>\n");
					valiatorColumnsBuffer.append("\t\t\t\t\t<var-value>"+dataLengths[i]+"</var-value>\n");
					valiatorColumnsBuffer.append("\t\t\t\t</var>\n");
					valiatorColumnsBuffer.append("\t\t\t</field>\n");
				}
			}
		}
	}
	
	String gridModel = gridModelBuffer.toString();
	String bindKeys = bindKeysBuffer.toString();
	String voToFormKey = voToFormKeyBuffer.toString();
	String resetFormColumn = resetFormColumnBuffer.toString();
	String enrollmentHtml = enrollmentHtmlBuffer.toString();
	String hiddenKey = hiddenKeyBuffer.toString();
	
	//vo변수
	String voColumns = voColumnsBuffer.toString();
	
	//valiator 변수
	String valiatorColumns = valiatorColumnsBuffer.toString();
	
	/*----------------------------------파일 처리-----------------------------------------------------------------------*/	
	//file을 먼저 읽어서 내용을 변경하고 수정된 이름으로 template 폴더에 저장.
	//1.copy하고 이름변경
	
	//각 소스 루트 start
	String srcRoot = baseRoot+"/src/main/webapp/sourceGen/setting/template/onepage";
	
	String sqlRoot = baseRoot+"/src/main/resources/sql";
	String validatorRoot = baseRoot+"/src/main/resources/validator";
	
	String javaBaseRoot = baseRoot+"/src/main/java/";
	String voRoot = javaBaseRoot+fullPackageBarPath+"/service";
	String daoImplRoot = javaBaseRoot+fullPackageBarPath+"/service/impl";
	String controllerRoot = javaBaseRoot+fullPackageBarPath+"/web";
	String jspRoot = baseRoot+"/src/main/webapp/WEB-INF/jsp"+packageBarPath;
	//각 소스 루트 end
	
	File inFile = new File(srcRoot);
	
	File[] inFiles = inFile.listFiles();
	
	System.out.println("-----------inFiles.length() : "+inFiles.length);
	
	String copyName = "";
	String targetRoot;
	String error = "";
	if(inFiles != null && 0<inFiles.length){
		for(File f:inFiles){

			if(f.getName().contains(".java")||f.getName().contains(".xml")){
				copyName = f.getName().replace("PAGENM",nonCamelPageNm);
			}else{
				copyName = f.getName().replace("PAGENM",camelPageNm);
			}
			
			targetRoot = "";
			
			if(f.getName().contains("VO.java")){
				targetRoot = voRoot;
			}else if(f.getName().contains("DAO.java") || f.getName().contains("Impl.java")){
				targetRoot = daoImplRoot;
			}else if(f.getName().contains("Controller.java")){
				targetRoot = controllerRoot;
			}else if(f.getName().contains("_SQL.xml")){
				targetRoot = sqlRoot;
			}else if(f.getName().contains("Validator.xml")){
				targetRoot = validatorRoot;
			}else if(f.getName().contains(".jsp")){
				targetRoot = jspRoot;
			}
			
			File outFile = new File(targetRoot);
			
			/*
			if(f.getName().contains("VO.java")){
				outFile = new File(voRoot);
			}else if(f.getName().contains("DAO.java") || f.getName().contains("Impl.java")){
				outFile = new File(daoImplRoot);
			}else if(f.getName().contains("Controller.java")){
				outFile = new File(controllerRoot);
			}else if(f.getName().contains("_SQL.xml")){
				outFile = new File(sqlRoot);
			}else if(f.getName().contains("Validator.xml")){
				outFile = new File(validatorRoot);
			}else if(f.getName().contains(".jsp")){
				outFile = new File(jspRoot);
			}
			*/
			
			if(!outFile.exists())outFile.mkdirs();
			if(!outFile.exists()){
				throw new NotDirectoryException("해당 폴더가 존재하지 않습니다.");
			}
				
			System.out.println("-----------copyName : "+copyName+"---size---"+f.length());
			
			BufferedReader fbr = null;
			BufferedWriter fbw = null;
			
			try{
				
				fbr = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
				fbw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile.getAbsolutePath()+File.separator+copyName),"UTF-8"));
				
				String orgLine = "";
				String newLine = "";
				String line = "";
				while((line=fbr.readLine()) != null){
					orgLine += line+"\r\n";
					System.out.println(line);
				}
				
				newLine = orgLine.replaceAll("packageBarPath", packageBarPath)
								 .replaceAll("packageDotPath", packageDotPath)
								 .replaceAll("fullPackageBarPath", fullPackageBarPath)
				                 .replaceAll("fullPackageDotPath", fullPackageDotPath)
				                 .replaceAll("camelPageNm", camelPageNm)
				                 .replaceAll("nonCamelPageNm", nonCamelPageNm)
				                 .replaceAll("koPageNm", koPageNm)
				                 .replaceAll("selectQuery", selectQuery)
				                 .replaceAll("selectDetailQuery", selectDetailQuery)
				                 .replaceAll("updateQuery", updateQuery)
				                 .replaceAll("deleteQuery", deleteQuery)
				                 .replaceAll("devNm", devName)
				                 .replaceAll("devDate", devDate)
				                 .replaceAll("lastKey", lastKey)
				                 .replaceAll("gridModel", gridModel)
				                 .replaceAll("enrollmentHtml", enrollmentHtml)
				                 .replaceAll("bindKeys", bindKeys)
				                 .replaceAll("voToFormKey", voToFormKey)
				                 .replaceAll("resetFormColumn", resetFormColumn)
				                 .replaceAll("voColumns", voColumns)
				                 .replaceAll("valiatorColumns", valiatorColumns)
				                 .replaceAll("hiddenKey", hiddenKey)
				                 .replaceAll("insertQuery", insertQuery)
				                 .replaceAll("nonCamelLastKey", nonCamelLastKey)
				                 .replaceAll("queryNamePath", queryNamePath)
				                 ;
				fbw.write(newLine+"\r\n");
					
			}catch(Exception e){
				e.getStackTrace();
			}finally{
				fbr.close();
				fbw.close();
			}
		}		
	}
	
	//mybatis 파일 마지막 줄에 typeAilas 추가
	
	File mybatisConfig = new File(sqlRoot+"/config/mybatis-config.xml");
	File mybatisConfigTarget = new File(sqlRoot+"/config/mybatis-config-temp.xml");
	
	BufferedReader fbr = null;
	BufferedWriter fbw = null;
	
	if(mybatisConfig.exists()){
		try{
			
			fbr = new BufferedReader(new InputStreamReader(new FileInputStream(mybatisConfig),"UTF-8"));
			
			String newLine = "";
			String line = "";	
			while((line=fbr.readLine()) != null){
				if("</typeAliases>".equals(line.trim())){
					newLine += "\t\t<typeAlias alias=\""+camelPageNm+"VO\"	type=\""+fullPackageDotPath+".service."+nonCamelPageNm+"VO\"/>"+"\r\n";
				}
				newLine += line+"\r\n";
				System.out.println(line);
			}
			
			try{
				fbw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mybatisConfig),"UTF-8"));
				
				fbw.write(newLine+"\r\n");
				
			}catch(Exception e){
				e.getStackTrace();
			}finally{
				fbw.close();
			}
			
			/*
			File finalFile = new File(sqlRoot+"/config/mybatis-config-temp.xml");
			File oldFile = new File(sqlRoot+"/config/mybatis-config.xml");
			boolean isOk = finalFile.renameTo(oldFile);
			FileOutputStream finalOut = new FileOutputStream(finalFile);
			finalOut.flush();
			System.out.println("********************************"+isOk);
			
			finalOut.close();
			*/
		}catch(Exception e){
			e.getStackTrace();
		}finally{
			fbr.close();
		}
	}
	
%>

<script type="text/javascript">
	
	alert("완료되었습니다.");
	history.back(-1);
	
</script>
</head>
<body>
	<%--
	<p><%=selectQuery%></p>
	<p><%=selectDetailQuery%></p>
	<p><%=updateQuery%></p>
	<p><%=deleteQuery%></p>
	--%>
	
</body>	
</html>