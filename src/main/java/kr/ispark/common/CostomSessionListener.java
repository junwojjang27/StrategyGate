package kr.ispark.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

public class CostomSessionListener implements HttpSessionListener{
	
	@Override
	public void sessionCreated(javax.servlet.http.HttpSessionEvent se) {
		/*
		System.out.println("************************************************************");
		System.out.println("sessionCreated session ID :" + se.getSession().getId());
		System.out.println("sessionCreated getCreationTime :" + se.getSession().getCreationTime());
		System.out.println("************************************************************");
		*/
	};
	
	@Override
	public void sessionDestroyed(javax.servlet.http.HttpSessionEvent se) {
		
		/*
		System.out.println("************************************************************");
		System.out.println("sessionDestroyed session ID :" + se.getSession().getId());
		System.out.println("sessionDestroyed getLastAccessedTime :" + se.getSession().getLastAccessedTime());
		System.out.println("sessionDestroyed getMaxInactiveInterval :" + se.getSession().getMaxInactiveInterval());
		System.out.println("************************************************************");
		*/
	};
	
	
	
}
