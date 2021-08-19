package kr.ispark.common.aspect;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.com.cmm.EgovMessageSource;
import kr.ispark.common.exception.SecureException;
import kr.ispark.common.util.ProcessQueue;

@Aspect
public class DoubleClickAspect {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public Object processMethod(ProceedingJoinPoint jp) throws Throwable {
		
		String targetName = jp.getTarget().getClass().getName();
		String targetMethod = jp.getSignature().getName();
		Object obj;
		
		if(!ProcessQueue.isProcessQueue(targetMethod)){
			ProcessQueue.setProcessQueue(targetMethod);
			logger.debug("############# "+targetName+" :: "+targetMethod+" is running");
			
			obj = jp.proceed();
			ProcessQueue.removeProcessQueue(targetMethod);
			logger.debug("############# "+targetName+" :: "+targetMethod+" is end");
			logger.debug("############# jp :: "+jp.getSignature().getDeclaringType().getName()+"");
			logger.debug("############# jp :: "+((MethodSignature) jp.getSignature()).getReturnType()+"");
			logger.debug("############# ProcessQueue.isProcessQueue(targetMethod) :: "+ProcessQueue.isProcessQueue(targetMethod));
		}else{
			obj = null; 
			logger.debug("############# "+targetName+" :: "+targetMethod+" already used sorry!!!!");
			logger.debug("############# ProcessQueue.isProcessQueue(targetMethod) :: "+ProcessQueue.isProcessQueue(targetMethod));
		}
		
		return obj;
	}

}