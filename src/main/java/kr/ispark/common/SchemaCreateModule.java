package kr.ispark.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("SchemaCreateModule")
public class SchemaCreateModule {
	
	String msg = "";
	private Connection sconn = null;
	private Connection tconn = null;
	
	public final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public String createScheme(HashMap<String,String> smap, HashMap<String,String> tmap, String dbType){
		
		if("mysql".equals(dbType) || "mariadb".equals(dbType)){
			mysqlCreateScheme(smap,tmap);
		}else if ("oracle".equals(dbType)){
			oracleCreateScheme(smap,tmap);
		}
		return msg;
	}
	
	private Connection dbConnection(HashMap<String,String> map) throws Exception {
		
		String url = map.get("DB_URL");
		String driver = map.get("DB_DRIVER");
		String user = map.get("DB_USER_ID");
		String password = map.get("DB_USER_PASSWD");
		
		Class.forName(driver);
		return DriverManager.getConnection(url, user, password);
	}
	
	private void mysqlCreateScheme(HashMap<String,String> smap, HashMap<String,String> tmap) {
		
		PreparedStatement stmtTableSearch = null;
		PreparedStatement stmtTableScriptSearch = null;
		PreparedStatement stmtViewSearch = null;
		Statement stmtTableCreate = null;
		Statement stmtViewCreate = null;
		ResultSet rsTableSearch = null;
		ResultSet rsTableScriptSearch = null;
		ResultSet rsViewSearch = null;
		
		try{
			sconn = dbConnection(smap);
			tconn = dbConnection(tmap);
			
			
			StringBuffer sSearchQuery  = new StringBuffer();
			sSearchQuery.append(" SELECT TABLE_NAME 				\n");
			sSearchQuery.append(" FROM Information_schema.TABLES 	\n");
			sSearchQuery.append(" WHERE TABLE_SCHEMA = ? 			\n");
			//sSearchQuery.append(" AND TABLE_NAME = 'BSC_METRIC' 	\n");
			sSearchQuery.append(" AND TABLE_NAME NOT LIKE 'v_%' 	\n");
			
			stmtTableSearch = sconn.prepareStatement(sSearchQuery.toString());
			stmtTableSearch.setString(1, smap.get("DB_ID"));
			rsTableSearch = stmtTableSearch.executeQuery();
			
			ArrayList<String> tableNames = new ArrayList<String>();
			
			while(rsTableSearch.next()){
				log.debug("#### tableNames ===== "+rsTableSearch.getString(1));
				tableNames.add(rsTableSearch.getString(1));
			}
			
			StringBuffer sViewSearchQuery  = new StringBuffer();
			sViewSearchQuery.append(" SELECT TABLE_NAME, VIEW_DEFINITION	\n");
			sViewSearchQuery.append(" FROM Information_schema.VIEWS 		\n");
			sViewSearchQuery.append(" WHERE TABLE_SCHEMA = ? 				\n");
			
			stmtViewSearch = sconn.prepareStatement(sViewSearchQuery.toString());
			stmtViewSearch.setString(1, smap.get("DB_ID"));
			rsViewSearch = stmtViewSearch.executeQuery();
			
			ArrayList<String> viewNames = new ArrayList<String>();
			
			while(rsViewSearch.next()){
				log.debug("#### viewNames ===== "+rsViewSearch.getString(1));
				viewNames.add("CREATE VIEW "+rsViewSearch.getString(1)+" AS "+rsViewSearch.getString(2).replace(smap.get("DB_ID").toUpperCase(), tmap.get("DB_ID").toUpperCase())
																									   .replace(smap.get("DB_ID").toLowerCase(), tmap.get("DB_ID").toLowerCase()));
			}
			
			if(tableNames.size() > 0){
				StringBuffer sShowQuery  = new StringBuffer();
				StringBuffer tCreateQuery  = new StringBuffer();
				String query = "";
				for(String tableName : tableNames){
					sShowQuery.append(" SHOW CREATE TABLE "+tableName+"	\n");
					stmtTableScriptSearch = sconn.prepareStatement(sShowQuery.toString());
					rsTableScriptSearch = stmtTableScriptSearch.executeQuery();
					while(rsTableScriptSearch.next()){
						query = rsTableScriptSearch.getString(2);
					}
					
					tCreateQuery.append(query);
					stmtTableCreate = tconn.createStatement();
					
					stmtTableCreate.executeUpdate(tCreateQuery.toString());
					
					sShowQuery.setLength(0);
					tCreateQuery.setLength(0);
					
					stmtTableScriptSearch.close();
					stmtTableCreate.close();
					rsTableScriptSearch.close();
				}
			}
			
			if(viewNames.size() > 0){
				StringBuffer tViewCreateQuery  = new StringBuffer();
				for(String viewQuery : viewNames){

					tViewCreateQuery.append(viewQuery);
					stmtViewCreate = tconn.createStatement();
					stmtViewCreate.executeUpdate(tViewCreateQuery.toString());
					
					tViewCreateQuery.setLength(0);
					stmtViewCreate.close();
				}
			}
			
		}catch(Exception e){
			msg = "problem occur!";
			e.printStackTrace();
		}finally{
			try {
				sconn.close();
				tconn.close();
				stmtTableSearch.close();
				stmtTableScriptSearch.close();
				stmtViewSearch.close();
				stmtTableCreate.close();
				stmtViewCreate.close();
				rsTableSearch.close();
				rsTableScriptSearch.close();
				rsViewSearch.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				msg = "problem occur!";
				e.printStackTrace();
			}
		}
		
	}
	
	private void oracleCreateScheme(HashMap<String,String> smap, HashMap<String,String> tmap){
		/*추후 개발*/
		PreparedStatement stmtTableSearch = null;
		PreparedStatement stmtTableScriptSearch = null;
		PreparedStatement stmtViewSearch = null;
		Statement stmtTableCreate = null;
		Statement stmtViewCreate = null;
		ResultSet rsTableSearch = null;
		ResultSet rsTableScriptSearch = null;
		ResultSet rsViewSearch = null;
		
		try{
			sconn = dbConnection(smap);
			tconn = dbConnection(tmap);
			
			
			StringBuffer sSearchQuery  = new StringBuffer();
			sSearchQuery.append(" SELECT TABLE_NAME 				\n");
			sSearchQuery.append("   FROM DBA_TABLES 	\n");
			sSearchQuery.append("  WHERE OWNER = ? 			\n");
			
			stmtTableSearch = sconn.prepareStatement(sSearchQuery.toString());
			stmtTableSearch.setString(1, smap.get("DB_ID"));
			rsTableSearch = stmtTableSearch.executeQuery();
			
			ArrayList<String> tableNames = new ArrayList<String>();
			
			while(rsTableSearch.next()){
				log.debug("#### tableNames ===== "+rsTableSearch.getString(1));
				tableNames.add(rsTableSearch.getString(1));
			}
			
			StringBuffer sViewSearchQuery  = new StringBuffer();
			sViewSearchQuery.append(" SELECT VIEW_NAME, TEXT				\n");
			sViewSearchQuery.append(" FROM DBA_VIEWS						\n");
			sViewSearchQuery.append(" WHERE OWNER = ? 						\n");
			
			stmtViewSearch = sconn.prepareStatement(sViewSearchQuery.toString());
			stmtViewSearch.setString(1, smap.get("DB_ID"));
			rsViewSearch = stmtViewSearch.executeQuery();
			
			ArrayList<String> viewNames = new ArrayList<String>();
			
			while(rsViewSearch.next()){
				log.debug("#### viewNames ===== "+rsViewSearch.getString(1));
				viewNames.add("CREATE VIEW "+rsViewSearch.getString(1)+" AS "+rsViewSearch.getString(2).replace("\"",""));
			}
			
			if(tableNames.size() > 0){
				StringBuffer sShowQuery  = new StringBuffer();
				StringBuffer tCreateQuery  = new StringBuffer();
				String query = "";
				for(String tableName : tableNames){
					sShowQuery.append(" SELECT DBMS_METADATA.GET_DDL('TABLE',?) FROM DUAL \n");
					stmtTableScriptSearch = sconn.prepareStatement(sShowQuery.toString());
					stmtTableScriptSearch.setString(1, tableName);
					rsTableScriptSearch = stmtTableScriptSearch.executeQuery();
					while(rsTableScriptSearch.next()){
						query = rsTableScriptSearch.getString(1).replace("\"", "").replace(smap.get("DB_ID").toUpperCase(), tmap.get("DB_ID").toUpperCase())
								   												  .replace(smap.get("DB_ID").toLowerCase(), tmap.get("DB_ID").toLowerCase());
					}
					
					tCreateQuery.append(query);
					stmtTableCreate = tconn.createStatement();
					
					stmtTableCreate.executeUpdate(tCreateQuery.toString());
					
					sShowQuery.setLength(0);
					tCreateQuery.setLength(0);
					
					stmtTableScriptSearch.close();
					stmtTableCreate.close();
					rsTableScriptSearch.close();
				}
			}
			
			if(viewNames.size() > 0){
				StringBuffer tViewCreateQuery  = new StringBuffer();
				for(String viewQuery : viewNames){

					tViewCreateQuery.append(viewQuery);
					stmtViewCreate = tconn.createStatement();
					stmtViewCreate.executeUpdate(tViewCreateQuery.toString());
					
					tViewCreateQuery.setLength(0);
					stmtViewCreate.close();
				}
			}
			
		}catch(Exception e){
			msg = "problem occur!";
			e.printStackTrace();
		}finally{
			try {
				sconn.close();
				tconn.close();
				stmtTableSearch.close();
				stmtTableScriptSearch.close();
				stmtViewSearch.close();
				stmtTableCreate.close();
				stmtViewCreate.close();
				rsTableSearch.close();
				rsTableScriptSearch.close();
				rsViewSearch.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				msg = "problem occur!";
				e.printStackTrace();
			}
		}
	}
	
}
