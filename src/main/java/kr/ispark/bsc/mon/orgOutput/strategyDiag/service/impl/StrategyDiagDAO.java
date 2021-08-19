/*************************************************************************
* CLASS 명	: StrategyDiagDAO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-03
* 기	능	: 전략연계도 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-03
**************************************************************************/
package kr.ispark.bsc.mon.orgOutput.strategyDiag.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.bsc.mon.orgOutput.strategyDiag.service.StrategyDiagVO;

@Repository
public class StrategyDiagDAO extends EgovComAbstractDAO {
	/**
	 * 전략연계도 목록 조회
	 * @param	StrategyDiagVO searchVO
	 * @return	List<StrategyDiagVO>
	 * @throws	Exception
	 */
	public List<StrategyDiagVO> selectList(StrategyDiagVO searchVO) throws Exception {
		return selectList("mon.orgOutput.strategyDiag.selectList", searchVO);
	}
	
	/**
	 * 전략연계도 목록 조회
	 * @param	StrategyDiagVO searchVO
	 * @return	List<StrategyDiagVO>
	 * @throws	Exception
	 */
	public List<StrategyDiagVO> selectStrategyList(StrategyDiagVO searchVO) throws Exception {
		return selectList("mon.orgOutput.strategyDiag.selectStrategyList", searchVO);
	}
	
	/**
	 * 전략연계도 목록 조회
	 * @param	StrategyDiagVO searchVO
	 * @return	List<StrategyDiagVO>
	 * @throws	Exception
	 */
	public List<StrategyDiagVO> selectSignalList(StrategyDiagVO searchVO) throws Exception {
		return selectList("mon.orgOutput.strategyDiag.selectSignalList", searchVO);
	}
	
	/**
	 * 전략연계도 상세 조회
	 * @param	StrategyDiagVO searchVO
	 * @return	StrategyDiagVO
	 * @throws	Exception
	 */
	public StrategyDiagVO selectDetail(StrategyDiagVO searchVO) throws Exception {
		return (StrategyDiagVO)selectOne("mon.orgOutput.strategyDiag.selectDetail", searchVO);
	}
	
	/**
	 * 전략연계도 정렬순서저장
	 * @param	StrategyDiagVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(StrategyDiagVO searchVO) throws Exception {
		return update("mon.orgOutput.strategyDiag.updateSortOrder", searchVO);
	}

	/**
	 * 전략연계도 삭제
	 * @param	StrategyDiagVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteStrategyDiag(StrategyDiagVO searchVO) throws Exception {
		return update("mon.orgOutput.strategyDiag.deleteStrategyDiag", searchVO);
	}
	
	/**
	 * 전략연계도 저장
	 * @param	StrategyDiagVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(StrategyDiagVO searchVO) throws Exception {
		return insert("mon.orgOutput.strategyDiag.insertData", searchVO);
	}

	/**
	 * 전략연계도 수정
	 * @param	StrategyDiagVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(StrategyDiagVO searchVO) throws Exception {
		return update("mon.orgOutput.strategyDiag.updateData", searchVO);
	}
}

