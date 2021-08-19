/*************************************************************************
* CLASS 명	: StrategyDAO
* 작 업 자	: joey
* 작 업 일	: 2018. 1. 29.
* 기	능	:  전략목표 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018. 1. 29.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.strategy.strategy.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.bsc.base.strategy.strategy.service.StrategyVO;

@Repository
public class StrategyDAO extends EgovComAbstractDAO {
	/**
	 * 전략목표 조회
	 */
	public List<StrategyVO> selectList(StrategyVO searchVO) throws Exception {
		return selectList("base.strategy.strategy.selectList", searchVO);
	}
	
	/**
	 * 전략목표 조회
	 */
	public List<StrategyVO> selectUpStrategyList(StrategyVO searchVO) throws Exception {
		return selectList("base.strategy.strategy.selectUpStrategyList", searchVO);
	}	
	
	/**
	 * 전략목표 조회
	 */
	public List<StrategyVO> selectPerspectiveList(StrategyVO searchVO) throws Exception {
		return selectList("base.strategy.strategy.selectPerspectiveList", searchVO);
	}	

	/**
	 * 전략목표삭제
	 */
	public int deleteStrategy(StrategyVO searchVO) {
		return update("base.strategy.strategy.deleteStrategy", searchVO);
	}
	
	/**
	 * 전략목표 저장
	 */
	public int insertData(StrategyVO searchVO) {
		return update("base.strategy.strategy.insertData", searchVO);
	}

	/**
	 * 전략목표 수정
	 */
	public int updateData(StrategyVO searchVO) {
		return update("base.strategy.strategy.updateData", searchVO);
	}
}
