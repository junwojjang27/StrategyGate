package kr.ispark.example;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springmodules.validation.util.lang.ReflectionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"file:src/test/resources/config/test-context-sqlMap.xml"
	//"file:src/test/resources/config/test-context-sqlMap_mariadb.xml"
	//"file:src/test/resources/config/test-context-sqlMap_tibero.xml"
})
@TransactionConfiguration(transactionManager="txManager", defaultRollback = true)
@Transactional
@WebAppConfiguration
public class QueryTest {
	@Autowired
	private SqlSessionFactory sqlFactory;

	// 기본값들
	final String FILE_PATH = "C:\\workspace\\StrategyGate\\src\\test\\resources\\config\\";
	final String TEST_COMP_ID = "TEST_COMP_ID";
	final String TEST_USER_ID = "testUser";
	final String TEST_LANG = "ko";
	final String TEST_YEAR = "2018";
	final String TEST_MON = "12";
	final String TEST_ANAL_CYCLE = "Y";
	final String TEST_METRIC_ID = "M000001";
	final String TEST_SC_DEPT_ID = "D000001";
	final String TEST_DEPT_ID = "D000001";
	final String TEST_CAL_TYPE_COL = "A";
	final String TEST_SORT_ORDER = "10";
	final String TEST_EVAL_DEGREE_ID = "TEST";
	final boolean SHOW_ERROR_MSG = true;	// ERROR 표시 뒤에 에러메시지를 추가
	final int MAX_ERROR_CNT = 10;	// 에러가 몇 개 발생하면 테스트를 중단 시킬 것인지

	@Test
	public void run() throws Exception {
		// 실행할 메소드의 주석을 풀고 사용할 것

		// 쿼리 테스트 실행 (이전 테스트에서 에러가 발생했던 쿼리를 포함해서 실행)
		test();

		// 쿼리 테스트 실행 (이전 테스트에서 실행된 쿼리는 skip하고 테스트 진행)
		//test(false);

		// queryIds.txt 파일을 새로 생성
		//makeQueryIdsFile();

		// 모든 sql id를 출력
		//getAllSqlIds();
	}

	/**
	 * 쿼리 테스트 실행
	 * @param	boolean retryError	// 이전 테스트에서 에러가 발생한 쿼리로 테스트할 것인지 포함 여부
	 * @throws	Exception
	 */
	public void test() throws Exception {
		test(true);
	}
	public void test(boolean retryError) throws Exception {
		File in = new File(FILE_PATH + "queryIds.txt");
		File out = new File(FILE_PATH + "queryIds.temp");

		FileReader fr = new FileReader(in);
		FileWriter fw = new FileWriter(out);
		try(BufferedReader br = new BufferedReader(fr);
			BufferedWriter bw = new BufferedWriter(fw);) {
			String line, line2;
			int errCnt = 0;
			while((line = br.readLine()) != null) {
				line2 = line;
				if(errCnt < MAX_ERROR_CNT &&
						(line.split(" : ").length == 1
							|| (retryError && line.split(" : ").length > 1 && line.split(" : ")[1].startsWith("ERROR")))) {
					try {
						line2 = line.split(" : ")[0];
						testQuery(line2);
						line2 += " : PASS";
					} catch(Exception e) {
						e.printStackTrace();
						errCnt++;
						line2 += " : ERROR";
						if(SHOW_ERROR_MSG) line2 += " - " + e.getCause().getMessage();
					}
				}

				bw.write(line2);
				bw.newLine();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		in.delete();
		out.renameTo(new File(FILE_PATH + "queryIds.txt"));
	}

	// queryIds.txt 파일을 새로 생성
	public void makeQueryIdsFile() throws Exception {
		TreeSet<String> sqlIdSet = getAllSqlIds();

		File file = new File(FILE_PATH + "queryIds.txt");
		if(file.exists()) file.delete();

		FileWriter fw = new FileWriter(file);
		try(BufferedWriter bw = new BufferedWriter(fw);) {
			for(String id : sqlIdSet) {
				bw.write(id);
				bw.newLine();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	// 모든 sql id를 출력
	public TreeSet<String> getAllSqlIds() throws Exception {
		TreeSet<String> sqlIdSet = new TreeSet<String>();
		for(String sqlId : sqlFactory.getConfiguration().getMappedStatementNames()) {
			// namespace가 있는 sqlId만 sqlIdSet에 넣음
			if(sqlId.indexOf(".") > -1) {
				sqlIdSet.add(sqlId);
			}
		}

		int i=0;
		for(String id : sqlIdSet) {
			System.out.println(id);
			i++;
		}

		System.out.println("================");
		System.out.println(i);

		return sqlIdSet;
	}

	public void testQuery(String sqlId) throws Exception {
		System.out.println("\n# " + sqlId);

		Class cls;
		Class superClass;
		Object vo = null;
		Method m;
		List<Field> fieldList;

		Configuration config = sqlFactory.getConfiguration();
		MappedStatement ms = config.getMappedStatement(sqlId.trim());
		ParameterMap pMap = ms.getParameterMap();
		SqlCommandType type = ms.getSqlCommandType();

		if(pMap.getType() != null) {
			cls = Class.forName(pMap.getType().getName());
			vo = cls.newInstance();

			if(vo instanceof Map) {
				Map map = (Map)vo;
				map.put("compId", TEST_COMP_ID);
				map.put("userId", TEST_USER_ID);
				map.put("loginUserId", TEST_USER_ID);
				map.put("insertUserId", TEST_USER_ID);
				map.put("lang", TEST_LANG);
				map.put("year", TEST_YEAR);
				map.put("mon", TEST_MON);
				map.put("findYear", TEST_YEAR);
				map.put("findMon", TEST_MON);
				map.put("analCycle", TEST_ANAL_CYCLE);
				map.put("metricGrpId", TEST_ANAL_CYCLE);
				map.put("metricId", TEST_METRIC_ID);
				map.put("govMetricId", TEST_METRIC_ID);
				map.put("scDeptId", TEST_SC_DEPT_ID);
				map.put("deptId", TEST_DEPT_ID);
				map.put("calTypeCol", TEST_CAL_TYPE_COL);
				map.put("sortOrder", TEST_SORT_ORDER);
				map.put("evalDegreeId", TEST_EVAL_DEGREE_ID);
			} else if(vo instanceof String) {
				vo = "0/0*100";
			} else {
				fieldList = new ArrayList<Field>();
				superClass = cls;
				while((superClass = superClass.getSuperclass()) != null) {
					for(Field field : superClass.getDeclaredFields()) {
						fieldList.add(field);
					}
				}
				for(Field field : cls.getDeclaredFields()) {
					fieldList.add(field);
				}

				for(Field field : fieldList) {
					//System.out.println(field.getName() + "\t:\t" + field.getType());

					if(field.getType() == Class.forName("java.util.List")) {
						m = ReflectionUtils.findMethod(cls, "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), new Class[]{List.class});
						if(m != null) {
							m.invoke(vo, new ArrayList(){{add(null);}});
						}
					} else if(field.getType() == Class.forName("[L" + String.class.getName() + ";")) {
						m = ReflectionUtils.findMethod(cls, "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), new Class[]{String[].class});
						if(m != null) {
							m.invoke(vo, new Object[] {new String[] {"TEST"}});
						}
					}
				}

				m = ReflectionUtils.findMethod(cls, "setCompId", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_COMP_ID);
				m = ReflectionUtils.findMethod(cls, "setUserId", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_USER_ID);
				m = ReflectionUtils.findMethod(cls, "setLoginUserId", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_USER_ID);
				m = ReflectionUtils.findMethod(cls, "setInsertUserId", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_USER_ID);
				m = ReflectionUtils.findMethod(cls, "setLang", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_LANG);
				m = ReflectionUtils.findMethod(cls, "setYear", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_YEAR);
				m = ReflectionUtils.findMethod(cls, "setMon", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_MON);
				m = ReflectionUtils.findMethod(cls, "setFindYear", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_YEAR);
				m = ReflectionUtils.findMethod(cls, "setFindMon", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_MON);
				m = ReflectionUtils.findMethod(cls, "setAnalCycle", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_ANAL_CYCLE);
				m = ReflectionUtils.findMethod(cls, "setMetricId", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_METRIC_ID);
				m = ReflectionUtils.findMethod(cls, "setMetricGrpId", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_METRIC_ID);
				m = ReflectionUtils.findMethod(cls, "setGovMetricId", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_METRIC_ID);
				m = ReflectionUtils.findMethod(cls, "setScDeptId", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_SC_DEPT_ID);
				m = ReflectionUtils.findMethod(cls, "setDeptId", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_DEPT_ID);
				m = ReflectionUtils.findMethod(cls, "setCalTypeCol", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_CAL_TYPE_COL);
				m = ReflectionUtils.findMethod(cls, "setSortOrder", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_SORT_ORDER);
				m = ReflectionUtils.findMethod(cls, "setEvalDegreeId", new Class[]{String.class});
				if(m != null) m.invoke(vo, TEST_EVAL_DEGREE_ID);
			}
		}

		try(SqlSession session = sqlFactory.openSession()){
			if(type == SqlCommandType.SELECT) {
				System.out.println(session.selectList(sqlId, vo));
			} else if(type == SqlCommandType.INSERT) {
				session.insert(sqlId, vo);
			} else if(type == SqlCommandType.UPDATE) {
				session.update(sqlId, vo);
			} else if(type == SqlCommandType.DELETE) {
				session.delete(sqlId, vo);
			}
		} catch(Exception e) {
			if(e.getCause() instanceof SQLDataException
				&& ((SQLDataException)e.getCause()).getErrorCode() == 1476) {
				System.out.println("- 제수가 0인 경우 (ORACLE)");
				System.out.println(((SQLDataException)e.getCause()).getMessage());
			} else if(e.getCause() instanceof SQLException
				&& ((SQLException)e.getCause()).getErrorCode() == -5070) {
				System.out.println("- 제수가 0인 경우 (TIBERO)");
				System.out.println(((SQLException)e.getCause()).getMessage());
			}
			e.printStackTrace();
			throw e;
		}
	}

	public String getQuery(MappedStatement ms, Object vo) {
		return getQuery(ms, vo, false);
	}
	public String getQuery(MappedStatement ms, Object vo, boolean showParams) {
		String sql = ms.getBoundSql(vo).getSql();

		if(showParams) {
			System.out.println(ms.getBoundSql(vo).getParameterMappings());

			for(ParameterMapping param : ms.getBoundSql(vo).getParameterMappings()) {
				System.out.println(param);
				System.out.println(param.getProperty());
				sql = sql.replaceFirst("\\?", "#{" + param.getProperty() + "}");
			}
		}

		return sql;
	}

	public void getAllSqls2() throws Exception {
		TreeSet<String> sqlIdSet = new TreeSet<String>();
		for(String sqlId : sqlFactory.getConfiguration().getMappedStatementNames()) {
			if(sqlId.indexOf(".") > -1) {
				sqlIdSet.add(sqlId);
			}
		}

		MappedStatement ms;
		Configuration config = sqlFactory.getConfiguration();

		int i=0;
		for(String id : sqlIdSet) {
			System.out.println("---------------");
			System.out.println(id);

			ms = config.getMappedStatement(id);

			System.out.println(ms.getSqlCommandType());

			//System.out.println(ms.getSqlSource().getBoundSql(null).getSql());

			System.out.println(ms.getBoundSql(null).getSql());
			System.out.println(ms.getBoundSql(null).getParameterObject());
			System.out.println(ms.getBoundSql(null).getParameterMappings());

			ParameterMap map = ms.getParameterMap();
			System.out.println(map.getType());

			if(i>30) break;
			i++;
		}

		System.out.println("================");
		System.out.println(sqlIdSet.size());

		/*
		// include용 sql들 조회
		Map<String, XNode> sqlMap = sqlFactory.getConfiguration().getSqlFragments();
		for(String key : sqlMap.keySet()) {
			System.out.println("--------------------------");
			System.out.println(key);
			System.out.println(sqlMap.get(key));
		}
		try(SqlSession session = sqlFactory.openSession()){
			ExampleBoardVO searchVO = new ExampleBoardVO();
			searchVO.setCompId("isparkenc");

			System.out.println(" >>>>>>>>>> session 출력 : "+session+"\n");
			session.selectList("exampleBoard.selectList", searchVO);


			System.out.println(session.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}
}
