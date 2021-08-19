package kr.ispark.common.util;

import java.sql.Connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class ContextHolderInfo {

	private static final ThreadLocal<BasicDataSource> contextHolderUser = new ThreadLocal<BasicDataSource>();
	
	
	public static void setDataSourceInfo(String userId, String userNm){
	    
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		BasicDataSource bds = (BasicDataSource)context.getBean("egov.dataSource");
		bds.setUsername(userId);
		bds.setPassword(userNm);
		
		contextHolderUser.set(bds);
	}
	
	public static String getDataSourceInfo(){
		
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		BasicDataSource bds = (BasicDataSource)context.getBean("egov.dataSource");
		
		return bds == null ? "" : bds.getUsername();
	}
	
	public static BasicDataSource getDataSource(){
		
		return contextHolderUser.get();
	}
	
	
}
