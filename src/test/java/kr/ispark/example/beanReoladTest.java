package kr.ispark.example;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import kr.ispark.common.calculation.service.CalculationVO;
import kr.ispark.common.calculation.service.cal.CalculationDAO;
import kr.ispark.common.calculation.service.cal.CalculationServiceImpl;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.SessionUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"file:src/main/resources/config/context-idgen.xml",
		"file:src/main/resources/config/root-context.xml",
		"file:src/main/resources/config/servlet-context.xml",
		"file:src/main/resources/config/context-datasource.xml",
		"file:src/main/resources/config/context-password.xml",
		"file:src/main/resources/config/context-validator.xml",
		"file:src/main/resources/config/context-sqlMap.xml"
})
@WebAppConfiguration
public class beanReoladTest {

	//@Autowired
	//CalculationServiceImpl calService;
	
	@Test
	@Transactional
	public void test1() throws Exception{
		
		System.out.println("##############테스트 시작#############");
		
		System.out.println("1.서버올라온 후 30초 후 시작");
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){
			@Override
			public void run() {
				ConfigurableApplicationContext context = new GenericXmlApplicationContext("classpath:config/context-datasource.xml");
				context.refresh();
				
			}
		};
		timer.schedule(task, 10000);
	}
	
}
