package kr.ispark.common.calculation.service.cal;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer{

	private static int TASK_CORE_POOL_SIZE = 3;
	private static int TASK_MAX_POOL_SIZE = 3;
	private static int TASK_QUEUE_CAPA = 100;
	private static String EXECUTE_TASK_NAME = "asyncExecuter";
	
	@Bean(name="asyncExecuter")
	public Executor getAsyncExecutor() {
		
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(TASK_CORE_POOL_SIZE);
		taskExecutor.setMaxPoolSize(TASK_MAX_POOL_SIZE);
		taskExecutor.setQueueCapacity(TASK_QUEUE_CAPA);
		taskExecutor.initialize();
		
		// TODO Auto-generated method stub
		return taskExecutor;
	}
	
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
