/*************************************************************************
* CLASS 명	: StrategyTableDAO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-03
* 기	능	: 전략연계표 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-03
**************************************************************************/
package kr.ispark.bsc.mon.orgOutput.strategyTable.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.bsc.mon.orgOutput.strategyTable.service.StrategyTableVO;

@Repository
public class StrategyTableDAO extends EgovComAbstractDAO {
	/**
	 * 전략연계표 목록 조회
	 * @param	StrategyTableVO searchVO
	 * @return	List<StrategyTableVO>
	 * @throws	Exception
	 */
	public List<StrategyTableVO> selectList(StrategyTableVO searchVO) throws Exception {
		return selectList("mon.orgOutput.strategyTable.selectList", searchVO);
	}
	
	/**
	 * 전략연계표 목록 조회
	 * @param	StrategyTableVO searchVO
	 * @return	List<StrategyTableVO>
	 * @throws	Exception
	 */
	public List<StrategyTableVO> selectStrategyList(StrategyTableVO searchVO) throws Exception {
		return selectList("mon.orgOutput.strategyTable.selectStrategyList", searchVO);
	}
	
	/**
	 * 전략연계표 상세 조회
	 * @param	StrategyTableVO searchVO
	 * @return	StrategyTableVO
	 * @throws	Exception
	 */
	public StrategyTableVO selectDetail(StrategyTableVO searchVO) throws Exception {
		return (StrategyTableVO)selectOne("mon.orgOutput.strategyTable.selectDetail", searchVO);
	}
	
	/**
	 * 전략연계표 정렬순서저장
	 * @param	StrategyTableVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(StrategyTableVO searchVO) throws Exception {
		return update("mon.orgOutput.strategyTable.updateSortOrder", searchVO);
	}

	/**
	 * 전략연계표 삭제
	 * @param	StrategyTableVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteStrategyTable(StrategyTableVO searchVO) throws Exception {
		return update("mon.orgOutput.strategyTable.deleteStrategyTable", searchVO);
	}
	
	/**
	 * 전략연계표 저장
	 * @param	StrategyTableVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(StrategyTableVO searchVO) throws Exception {
		return insert("mon.orgOutput.strategyTable.insertData", searchVO);
	}

	/**
	 * 전략연계표 수정
	 * @param	StrategyTableVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(StrategyTableVO searchVO) throws Exception {
		return update("mon.orgOutput.strategyTable.updateData", searchVO);
	}
}

