package kr.ispark.example;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class dbSessionTest {

	@Autowired
	CalculationServiceImpl calService;
	
	@Test
	@Transactional
	public void test1() throws Exception{
		
		try {
			calService.setAllParam("2018","12");
			calService.execAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
}
