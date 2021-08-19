package kr.ispark.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import kr.ispark.common.CommonVO;

/**
* @author sidnancy
*/

public class RoutingDataSource extends AbstractRoutingDataSource {
	
   private HashMap<String,String> connMap; 
   
   /*
   private String compId = "";
   private String connYn = "";
   private String connType = "";
   */
   
	
   @Override
   protected Object determineCurrentLookupKey() {
		// TODO Auto-generated method stub
	   //connType = connMap.get(SessionUtil.getAttribute("connectionId"));
	   
	   /*
	   compId = CommonUtil.nullToBlank(SessionUtil.getAttribute("compId"));
	   connYn = CommonUtil.nullToBlank(connMap.get(compId));
	    
	    if("".equals(compId)){
	    	connType = "super";
	    }else if(!"".equals(compId) && !"Y".equals(connYn)){
	    	
	    	//super.afterPropertiesSet();
	    	setTargetDataSources(new HashMap<Object, Object>());
	    	super.afterPropertiesSet();
	    	connType = compId;
	    	
	    }else{
	    	connType = compId;
	    }
	    
	    
	    System.out.println("################target datainfo ##################");
	    System.out.println(" connType ==> "+ connType); 
	    System.out.println("################target datainfo end###############");
		
		return connType;
		*/
		
		return ContextHolder.getDataSourceType();
   }
   
   
   @Override
	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
	   // TODO Auto-generated method stub
	   
	   //database for문
	   String fileRoot = PropertyUtil.getProperty("Globals.dbInfoPath");
	   FileReader freader = null;
	   BufferedReader breader = null;
	   
	   connMap = new HashMap<String,String>(); 
	   
	   System.out.println("################target datainfo read start###############");
	   
	   try{
		   File file  = new File(fileRoot+"dbInfo.txt");
		   DataSourceType[] types = DataSourceType.values();
		   
		   System.out.println(" isFile ==> "+ file.isFile());
		   
		   freader = new FileReader(file);
		   breader = new BufferedReader(freader);
		   String line = "";
		   int typeCount = 0;
		   while((line = breader.readLine()) != null){
			   
			   if(!"".equals(line.trim())){
			   
				   String[] dbInfos = line.split("&&");
				   System.out.println(" dbInfos.length ==> "+ dbInfos.length);
				   
				   if(dbInfos != null){
					   System.out.println(" dbInfos[0] ==> "+ dbInfos[0]);
					   System.out.println(" dbInfos[1] ==> "+ dbInfos[1]);
					   System.out.println(" dbInfos[2] ==> "+ dbInfos[2]);
					   System.out.println(" dbInfos[3] ==> "+ dbInfos[3]);
					   System.out.println(" dbInfos[4] ==> "+ dbInfos[4]);
					   System.out.println(" dbInfos[5] ==> "+ dbInfos[5]);
					   System.out.println(" dbInfos[6] ==> "+ dbInfos[6]);
					   
					   
					   BasicDataSource md = new BasicDataSource();
					   md.setDriverClassName(dbInfos[3].trim());
					   md.setUrl(dbInfos[4].trim());
					   md.setUsername(dbInfos[5].trim());
					   md.setPassword(dbInfos[6].trim());
					   md.setValidationQuery("select 1 ");
					   
					   targetDataSources.put(DataSourceType.valueOf(dbInfos[0].toString()),md);
					   //targetDataSources.put(dbInfos[1].toString(),md);
					   //targetDataSources.put(types[typeCount],md);
					   //typeCount++;
					   
					   connMap.put(dbInfos[1], "Y");
					   
				   }
			   } 
		   }
		   
	   }catch(FileNotFoundException e){
		   e.getCause();
	   } catch (IOException e) {
		// TODO Auto-generated catch block
		   e.getCause();
	   }finally{
		   try{
			   breader.close();
		   }catch(IOException ie){
			   ie.getCause();
		   }
		   
		   try{
			   freader.close();
		   }catch(IOException ie){
			   ie.getCause();
		   }
	   }
	   
	   System.out.println("################target datainfo read end###############");
	   
	   super.setTargetDataSources(targetDataSources);
	}
   
   
   public void resetTargetDataSources(){
	   	setTargetDataSources(new HashMap<Object, Object>());
   		super.afterPropertiesSet();
   }
   
   public boolean checkIsTargetDataSource(String compId){
	    System.out.println("[datasource check ==> "+connMap.get(compId)+"]");
	    if(null == connMap.get(compId)){
	    	return false;
	    }
  		return true;
   }
   
   public void makeTargetDataSourcesFile(List<CommonVO> paramList){
	   
	   if(paramList != null && paramList.size() > 0){
		   BufferedWriter fbw1 = null;
		   FileWriter fw1 = null;
		   BufferedWriter fbw2 = null;
		   FileWriter fw2 = null;
		   BufferedReader fbr = null;
		   FileReader fr = null;

		   String info = "";
		   String backupInfo = "";
		   
		   String fileRoot = PropertyUtil.getProperty("Globals.dbInfoPath");
		   
		   System.out.println("################tresetTargetDataSourcesIncludeFile start###############");
		   
		   File orgfile  = new File(fileRoot+"dbInfo.txt");
		   File backupfile  = new File(fileRoot+"dbInfo_backup.txt");
		   
		   System.out.println("orgfile isFile ==> "+ orgfile.isFile());
		   System.out.println("backupfile isFile ==> "+ backupfile.isFile());
		   
		   try{
			   
			   fw2 = new FileWriter(backupfile);
			   fbw2 = new BufferedWriter(fw2);
			   
			   for(CommonVO pvo : paramList){
				   info += pvo.getDbInfoText()+"\r\n";
			   }
			   
			   System.out.println(info);
			   
			   if(info != null && info.trim().length() > 0){
				   fr = new FileReader(orgfile);
				   fbr = new BufferedReader(fr);
				   String line = "";
				   while((line = fbr.readLine()) != null){
					   if(line.trim().length() > 0){
						   backupInfo += line+"\r\n";
					   }
				   }
				   
				   fbw2.write(backupInfo);
			   }
			   
			   fw1 = new FileWriter(orgfile);
			   fbw1 = new BufferedWriter(fw1);
			   fbw1.write(info);
			   
			   
		   }catch(FileNotFoundException e){
			   e.getCause();
		   } catch (IOException e) {
			// TODO Auto-generated catch block
			   e.getCause();
		   }finally{
			   
			   try{
				   fbw1.close();
			   }catch(IOException ie){
				   ie.getCause();
			   }
			   try{
				   fbw2.close();
			   }catch(IOException ie){
				   ie.getCause();
			   }
			   try{
				   fw1.close();
			   }catch(IOException ie){
				   ie.getCause();
			   }
			   try{
				   fw2.close();
			   }catch(IOException ie){
				   ie.getCause();
			   }
			   try{
				   fbr.close();
			   }catch(IOException ie){
				   ie.getCause();
			   }
			   try{
				   fr.close();
			   }catch(IOException ie){
				   ie.getCause();
			   }
		   }
		   
		   System.out.println("################tresetTargetDataSourcesIncludeFile end#################");
	   }
   }
 
   
	  /*
	  @Override
	  public Connection getConnection(String username, String password) throws SQLException {
		 // TODO Auto-generated method stub
		 
		 String compId = SessionUtil.getCompId();
		 if("".equals(compId)){
		 	 compId = "BSCV6";
		 }
		 
		 System.out.println("#########comp_id ==>"+compId);
		 
		 return super.getConnection(compId, compId);
	  }
	  */
  
}