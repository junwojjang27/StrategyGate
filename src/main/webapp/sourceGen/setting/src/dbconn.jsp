<%@ page language="java" contentType="application/json" pageEncoding="utf-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@page import="java.util.Enumeration"%>
<%@ include file="config.jsp" %>
<%
	//테이블 명 파라미터 정보
	String tableName = request.getParameter("tbNm");
	Enumeration enu = request.getAttributeNames();
	Enumeration en = request.getParameterNames();
	while(enu.hasMoreElements()){
		System.out.println("리퀘스트 속성명 : "+enu.nextElement().toString());
	}
	while(en.hasMoreElements()){
		System.out.println("리케스트 파라미터명 : "+en.nextElement().toString());
	}
	//for test
	//tableName = "BSC_STRATEGY";
	//--------
	String columnGridData = "";
	String columnData = "";
	String columnKeyData = "";
	ArrayList<HashMap<String,String>> dataList = new ArrayList<HashMap<String,String>>(0);
	
	StringBuffer columnGridDataBuffer = new StringBuffer();
	StringBuffer columnDataBuffer = new StringBuffer();
	StringBuffer columnKeyDataBuffer = new StringBuffer();
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	StringBuffer testSql = new StringBuffer();
	//driver class 를 로딩
	try{
		Class.forName(DB_DRIVER);
		conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}catch(ClassNotFoundException cnfe){
		cnfe.printStackTrace();
	}
	
	testSql.append("SELECT A.TABLE_NAME,C.COMMENTS AS TABLE_NAME_KOR,B.COLUMN_NAME,D.COMMENTS AS COLUMN_NAME_KOR,											");
	testSql.append("       B.DATA_TYPE,B.DATA_LENGTH,B.DATA_PRECISION,B.DATA_SCALE,CASE WHEN NVL(E.COLUMN_NAME,'N') = 'N' THEN 'N' ELSE 'Y' END AS PK_YN	"); 
	testSql.append("  FROM USER_TABLES A                                                                                                                    ");
	testSql.append("       INNER JOIN USER_TAB_COLUMNS B                                                                                                    ");
	testSql.append("       ON A.TABLE_NAME = B.TABLE_NAME                                                                                                   ");
	testSql.append("       LEFT OUTER JOIN USER_TAB_COMMENTS C                                                                                              ");
	testSql.append("       ON A.TABLE_NAME = C.TABLE_NAME                                                                                                   ");
	testSql.append("       LEFT OUTER JOIN USER_COL_COMMENTS D                                                                                              ");
	testSql.append("       ON B.TABLE_NAME = D.TABLE_NAME                                                                                                   ");
	testSql.append("       AND B.COLUMN_NAME = D.COLUMN_NAME                                                                                                ");
	testSql.append("       LEFT OUTER JOIN USER_CONS_COLUMNS E                                                                                              ");
	testSql.append("       ON B.TABLE_NAME = E.TABLE_NAME                                                                                                   ");
	testSql.append("       AND B.COLUMN_NAME = E.COLUMN_NAME                                                                                                ");
	testSql.append("       AND E.POSITION IS NOT NULL                                                                                                       ");
	testSql.append(" WHERE A.TABLE_NAME = ?				                                                                                                    ");
	testSql.append(" ORDER BY B.COLUMN_ID ASC                                                                                                               ");
 
	try{
		pstmt = conn.prepareStatement(testSql.toString());
		pstmt.setString(1, tableName);
		rs = pstmt.executeQuery();
		while(rs.next()){
			HashMap<String,String> dataMap = new HashMap<String,String>();
			dataMap.put("tableName",rs.getString("TABLE_NAME"));
			dataMap.put("tableNameKor",rs.getString("TABLE_NAME_KOR"));
			dataMap.put("columnName",rs.getString("COLUMN_NAME"));
			dataMap.put("columnNameKor",rs.getString("COLUMN_NAME_KOR"));
			dataMap.put("dataType",rs.getString("DATA_TYPE"));
			dataMap.put("dataLength",rs.getString("DATA_LENGTH"));
			dataMap.put("dataPrecision",rs.getString("DATA_PRECISION"));
			dataMap.put("dataScale",rs.getString("DATA_SCALE"));
			dataMap.put("pkYn",rs.getString("PK_YN"));
			
			dataList.add(dataMap);
			dataMap = null;
		}
		
		//columnGridData 만들기
		if(dataList != null && 0<dataList.size()){
			columnGridDataBuffer.append("[");
			columnDataBuffer.append("\"");
			columnKeyDataBuffer.append("\"");
			
			for(int idx=0 ; idx<dataList.size() ; idx++){
				HashMap hm = (HashMap)dataList.get(idx);
				columnGridDataBuffer.append("{\"tableName\":"+"\""+hm.get("tableName")+"\",");
				columnGridDataBuffer.append("\"tableNameKor\":"+"\""+hm.get("tableNameKor")+"\",");
				columnGridDataBuffer.append("\"columnName\":"+"\""+hm.get("columnName")+"\",");
				columnGridDataBuffer.append("\"columnNameKor\":"+"\""+hm.get("columnNameKor")+"\",");
				columnGridDataBuffer.append("\"dataType\":"+"\""+hm.get("dataType")+"\",");
				columnGridDataBuffer.append("\"dataLength\":"+"\""+hm.get("dataLength")+"\",");
				columnGridDataBuffer.append("\"dataPrecision\":"+"\""+hm.get("dataPrecision")+"\",");
				columnGridDataBuffer.append("\"dataScale\":"+"\""+hm.get("dataScale")+"\",");
				columnGridDataBuffer.append("\"pkYn\":"+"\""+hm.get("pkYn")+"\"}");
				
				if(idx != dataList.size()-1){
					columnGridDataBuffer.append(",");
				}
				
				columnDataBuffer.append(hm.get("columnName")+"|");
				
				if("Y".equals(hm.get("pkYn")))columnKeyDataBuffer.append(hm.get("columnName")+"|");

			}
			
			columnGridDataBuffer.append("]");
			columnGridData = columnGridDataBuffer.toString();
			
			columnDataBuffer.append("\"");
			columnData = columnDataBuffer.toString();
			
			columnKeyDataBuffer.append("\"");
			columnKeyData = columnKeyDataBuffer.toString();
			
			out.print("{"+"\"columnGridData\":"+columnGridData+","+"\"columnData\":"+columnData+","+"\"columnKeyData\":"+columnKeyData+"}");
			//out.print(columnGridData);
		}
	}catch(Exception e){
		e.getStackTrace();
	}finally{
		conn.close();
		pstmt.close();
		rs.close();
	}
%>
