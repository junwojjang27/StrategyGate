<%@ page language="java" contentType="application/json" pageEncoding="utf-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@page import="java.util.Enumeration"%>
<%
	//테이블 명 파라미터 정보
	String url = request.getParameter("url");
	String driver = request.getParameter("driver");
	String user = request.getParameter("user");
	String passwd = request.getParameter("passwd");
	String dbtype = request.getParameter("dbtype");
	String searchKey = request.getParameter("searchKey");
	String dbName = request.getParameter("dbName");
	
	System.out.println(url);
	System.out.println(driver);
	System.out.println(user);
	System.out.println(passwd);
	System.out.println(dbtype);

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
		Class.forName(driver);
		conn = DriverManager.getConnection(url, user, passwd);
	}catch(ClassNotFoundException cnfe){
		cnfe.printStackTrace();
	}
	
	if("oracle".equals(dbtype)){
		testSql.append("SELECT A.TABLE_NAME											");
		testSql.append("  FROM USER_TABLES A                                        ");
		if(searchKey != null && !"".equals(searchKey)){
			testSql.append("  WHERE A.TABLE_NAME LIKE '%"+searchKey+"%'             ");
		}
		testSql.append(" ORDER BY A.TABLE_NAME ASC                                  ");
	}else if("mysql".equals(dbtype)){
		testSql.append("SELECT TABLE_NAME											");
		testSql.append("  FROM INFORMATION_SCHEMA.TABLES                            ");
		testSql.append(" WHERE TABLE_SCHEMA = '"+dbName+"'                          ");
		if(searchKey != null && !"".equals(searchKey)){
			testSql.append("  AND TABLE_NAME LIKE '%"+searchKey+"%'               ");
		}
	}
	
	try{
		pstmt = conn.prepareStatement(testSql.toString());
		rs = pstmt.executeQuery();
		while(rs.next()){
			HashMap<String,String> dataMap = new HashMap<String,String>();
			dataMap.put("tableName",rs.getString("TABLE_NAME"));
			
			dataList.add(dataMap);
			dataMap = null;
		}
		
		//columnGridData 만들기
		if(dataList != null && 0<dataList.size()){
			
			columnGridDataBuffer.append("[");
			
			for(int idx=0 ; idx<dataList.size() ; idx++){
				HashMap hm = (HashMap)dataList.get(idx);
				columnGridDataBuffer.append("{\"tableName\":"+"\""+hm.get("tableName")+"\"}");
				if(idx != dataList.size()-1){
					columnGridDataBuffer.append(",");
				}
			}
			
			columnGridDataBuffer.append("]");
			columnGridData = columnGridDataBuffer.toString();
			
			out.print("{"+"\"columnGridData\":"+columnGridData+"}");
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
