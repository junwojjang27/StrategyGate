<%@ page language="java" contentType="application/json" pageEncoding="utf-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Enumeration" %>
<%
	//테이블 명 파라미터 정보
	String surl = request.getParameter("surl");
	String sdriver = request.getParameter("sdriver");
	String suser = request.getParameter("suser");
	String spasswd = request.getParameter("spasswd");
	String sdbtype = request.getParameter("sdbtype");
	String scompId = request.getParameter("scompId");
	
	String turl = request.getParameter("turl");
	String tdriver = request.getParameter("tdriver");
	String tuser = request.getParameter("tuser");
	String tpasswd = request.getParameter("tpasswd");
	String tdbtype = request.getParameter("tdbtype");
	String tcompId = request.getParameter("tcompId");
	
	String tables = request.getParameter("tables");
	StringBuffer logBuf = new StringBuffer();
	
	System.out.println(surl);
	System.out.println(sdriver);
	System.out.println(suser);
	System.out.println(spasswd);
	System.out.println(sdbtype);
	System.out.println(scompId);
	System.out.println(turl);
	System.out.println(tdriver);
	System.out.println(tuser);
	System.out.println(tpasswd);
	System.out.println(tdbtype);
	System.out.println(tcompId);
	System.out.println(tables);
	
	if(tables != null && tables != "" && tables.length() > 0){
		String[] tableArray = tables.split("\\|");
		if(tableArray != null && tableArray.length > 0){
			
			Connection sconn = null;
			PreparedStatement spstmt1 = null;
			PreparedStatement spstmt2 = null;
			PreparedStatement spstmt3 = null;
			PreparedStatement spstmt4 = null;
			PreparedStatement spstmt5 = null;
			ResultSet srs1 = null;
			ResultSet srs2 = null;
			ResultSet srs3 = null;
			ResultSet srs4 = null;
			ResultSet srs5 = null;
			ResultSetMetaData srsm = null;
			
			Connection tconn = null;
			PreparedStatement tpstmt1 = null;
			PreparedStatement tpstmt2 = null;
			PreparedStatement tpstmt3 = null;
			PreparedStatement tpstmt4 = null;
			PreparedStatement tpstmt5 = null;
			ResultSet trs = null;
			//driver class 를 로딩
			try{
				Class.forName(sdriver);
				sconn = DriverManager.getConnection(surl, suser, spasswd);
				Class.forName(tdriver);
				tconn = DriverManager.getConnection(turl, tuser, tpasswd);
			
			
				System.out.println(sconn.toString());
				System.out.println(tconn.toString());
				
				StringBuffer srcColumnSql = new StringBuffer();
				StringBuffer targetDeleteSql = new StringBuffer();
				StringBuffer targetInsertSql = new StringBuffer();
				
				ArrayList<String> srcColumn = new ArrayList<String>(0);
				ArrayList<Integer> srcColumnType = new ArrayList<Integer>(0);
				ArrayList<String[]> srcColumnData = new ArrayList<String[]>(0);
				String srcColumnString = "";
				String srcColumnQuestionMarkString = "";
				int resultCnt = 0;
				boolean compIdExiest = false;
				
				SimpleDateFormat sdate = new SimpleDateFormat("yyyy-MM-dd");
				
				String error1="";
				String error2="";
				
				//autocommit false
				sconn.setAutoCommit(false);
				tconn.setAutoCommit(false);
				
				for(String tableId : tableArray){
					if(null != tableId && !"".equals(tableId)){
						srcColumnSql.append("SELECT *				   			\n");
						srcColumnSql.append("FROM "+tableId+"					\n");
						if(scompId != null && !"".equals(scompId)){
							srcColumnSql.append("WHERE COMP_ID='" +scompId+"'	\n");
						}
						
						try{
						
							spstmt1 = sconn.prepareStatement(srcColumnSql.toString());
							srs1 = spstmt1.executeQuery();
							srsm = srs1.getMetaData();
							
							//컬럼정보를 리스트에 저장
							int colCnt = srsm.getColumnCount();
							
							for(int i=1 ; i<=colCnt ; i++){
								
								srcColumn.add(srsm.getColumnName(i));
								srcColumnType.add(srsm.getColumnType(i));
								srcColumnString += srsm.getColumnName(i);
								srcColumnQuestionMarkString += "?";
								if(i != colCnt){
									srcColumnString+=",";
									srcColumnQuestionMarkString+=",";
								}
							}
							
							//row별로 데이터를 String array에 담고 list에 add
							while(srs1.next()){
								String[] srcColumnDataByRow = new String[colCnt];
								for(int j=0 ; j<colCnt ; j++){
									if(scompId != null && scompId.trim() != "" && tcompId != null && tcompId.trim() != "" && "COMP_ID".equals(srcColumn.get(j))){
										srcColumnDataByRow[j] = tcompId;
									}else{
										srcColumnDataByRow[j] = srs1.getString(srcColumn.get(j));	
									}
									
									//System.out.println("데이터 추출 : "+srcColumn.get(j)+":"+srcColumnDataByRow[j]);
								}
								srcColumnData.add(srcColumnDataByRow);
								srcColumnDataByRow = null;
							}	
							
							if(srcColumnData != null && srcColumnData.size() > 0){
								String error = "";
								try{
									//데이터삭제
									targetDeleteSql.append("DELETE FROM "+tableId+"				\n");
									if(scompId != null && scompId.trim() != "" && tcompId != null && tcompId.trim() != "" ){
										targetDeleteSql.append("WHERE COMP_ID='"+tcompId+"'	\n");
									}
									
									//데이터 저장
									targetInsertSql.append("INSERT INTO "+tableId+" ("+srcColumnString+") \n");
									targetInsertSql.append("VALUES ("+srcColumnQuestionMarkString+") \n");
									
									tpstmt1 = tconn.prepareStatement(targetDeleteSql.toString());
									if("oracle".equals(tdbtype)){
										tpstmt1.executeQuery();
									}else{
										tpstmt1.executeUpdate();	
									}
									
									tpstmt2 = tconn.prepareStatement(targetInsertSql.toString());
									int index = 0;
									for(int k=0 ; k<srcColumnData.size() ; k++){
										String[] dataArray = (String[])srcColumnData.get(k);
										if(dataArray.length > 0){
											for(int idx=0 ; idx<dataArray.length ; idx++){
												if(dataArray[idx] != null && dataArray[idx] != "" && (srcColumnType.get(idx) == java.sql.Types.DATE || srcColumnType.get(idx) == java.sql.Types.TIMESTAMP)){
													tpstmt2.setString(++index,sdate.format(sdate.parse(dataArray[idx])));
													//tpstmt2.setString(++index,"2018-01-01");
												}else{
													tpstmt2.setString(++index,dataArray[idx]);
												}
											}
											tpstmt2.addBatch();
											tpstmt2.clearParameters();
										}
										index = 0;
									}
									
									int[] resultCnts = tpstmt2.executeBatch();
									tpstmt2.clearBatch();
									tconn.commit();
									
									if(resultCnts!=null && resultCnts.length>0){resultCnt = resultCnts.length;}
									
								}catch(Exception e){
									e.getStackTrace();
									error1 = e.getMessage();
									tconn.rollback();
								}finally{
									tpstmt1.close();
									tpstmt2.close();
								}
							}
							
						}catch(Exception e){
							e.getStackTrace();
							error2 = e.getMessage();
						}finally{
							spstmt1.close();
							srs1.close();
							System.out.println("[테이블명  "+tableId+" 작업건수   "+resultCnt+"] "+error1+"|"+error2);
							logBuf.append("[테이블명  "+tableId+" 작업건수   "+resultCnt+"] "+error1+"|"+error2+"<br />");
						}
						
						//객체 초기화  할것
						srcColumnSql.setLength(0);
						targetDeleteSql.setLength(0);
						targetInsertSql.setLength(0);
						
						srcColumn.clear();
						srcColumnType.clear();
						srcColumnData.clear();
						srcColumnString = "";
						srcColumnQuestionMarkString = "";
						compIdExiest = false;
						error1="";
						error2="";
						
						resultCnt = 0 ;
						
					}
				}
				System.out.print("{"+"\"logBuf\":\""+logBuf.toString()+"\"}");
				out.print("{"+"\"logBuf\":\""+logBuf.toString()+"\",\"test\":\"good\"}");
				//out.print("{"+"\"logBuf\":\"GOOD\",\"test\":\"good\"}");
				
			}catch(ClassNotFoundException cnfe){
				cnfe.printStackTrace();
			}finally{
				sconn.close();
				tconn.close();
			}
		}
	}
	
%>
