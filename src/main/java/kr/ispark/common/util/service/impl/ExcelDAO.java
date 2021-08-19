package kr.ispark.common.util.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import kr.ispark.common.CommonVO;

@Repository
public class ExcelDAO {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	private SqlSessionFactory sqlSessionFactory;

	@Resource(name = "sqlSession")
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public SqlSessionFactory getSqlSessionFactory(){
		return this.sqlSessionFactory;
	}

	public int batchInsert(final String queryId, final List<? extends CommonVO> list) {
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
		
		int cnt = 0;
		for(Object targetObj : list) {
			sqlSession.insert(queryId, targetObj);
			cnt ++;
		}
		sqlSession.flushStatements();

		return cnt;
	}
	
	public int batchUpdate(final String queryId, final List<? extends CommonVO> list) {
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
		
		int cnt = 0;
		for(Object targetObj : list) {
			sqlSession.update(queryId, targetObj);
			cnt ++;
		}
		sqlSession.flushStatements();
		
		return cnt;
	}
}
