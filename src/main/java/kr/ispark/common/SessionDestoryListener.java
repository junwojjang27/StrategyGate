package kr.ispark.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionDestoryListener implements ApplicationListener<ApplicationEvent>{
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		
		if(event instanceof HttpSessionCreatedEvent){
			
			//System.out.println("------------------------------------------------------------");
			//System.out.println("HttpSessionCreatedEvent session ID :" +((HttpSessionCreatedEvent) event).getSession().getId());
			//System.out.println("HttpSessionCreatedEvent sgetCreationTime :" +((HttpSessionCreatedEvent) event).getSession().getCreationTime());
			//System.out.println("------------------------------------------------------------");
		}else if(event instanceof HttpSessionDestroyedEvent){
			
			//System.out.println("------------------------------------------------------------");
			//System.out.println("HttpSessionDestroyedEvent session ID :" +((HttpSessionDestroyedEvent) event).getSession().getId());
			//System.out.println("HttpSessionDestroyedEvent getLastAccessedTime :" +((HttpSessionDestroyedEvent) event).getSession().getLastAccessedTime());
			//System.out.println("HttpSessionDestroyedEvent getMaxInactiveInterval :" +((HttpSessionDestroyedEvent) event).getSession().getMaxInactiveInterval());
			//System.out.println("------------------------------------------------------------");
			
			/*
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			request.getRequestDispatcher("/login.do?sessionExpired");
			*/
		}
	}
}
