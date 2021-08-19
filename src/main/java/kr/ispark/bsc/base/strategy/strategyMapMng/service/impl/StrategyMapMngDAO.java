/*************************************************************************
* CLASS 명	: StrategyMapMngDAO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 2. 2.
* 기	능	: 전략체계도 관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 2. 2.			최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.strategy.strategyMapMng.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.bsc.base.strategy.strategyMapMng.service.StrategyMapVO;
import kr.ispark.common.util.CommonUtil;

@Repository
public class StrategyMapMngDAO extends EgovComAbstractDAO {
	/**
	 * 비전 & 미션 조회
	 * @param	StrategyMapVO searchVO
	 * @return	StrategyMapVO
	 * @throws	Exception
	 */
	public StrategyMapVO selectVisionMission(StrategyMapVO searchVO) throws Exception {
		return selectOne("base.strategy.strategyMapMng.selectVisionMission", searchVO);
	}

	/**
	 * 전략체계도 - 관점 목록 조회
	 * @param	StrategyMapVO searchVO
	 * @return	List<StrategyMapVO>
	 * @throws	Exception
	 */
	public List<StrategyMapVO> selectPerspectiveList(StrategyMapVO searchVO) throws Exception {
		return selectList("base.strategy.strategyMapMng.selectPerspectiveList", searchVO);
	}

	/**
	 * 전략체계도 목록 조회
	 * @param	StrategyMapVO searchVO
	 * @return	List<StrategyMapVO>
	 * @throws	Exception
	 */
	public List<StrategyMapVO> selectList(StrategyMapVO searchVO) throws Exception {
		return selectList("base.strategy.strategyMapMng.selectList", searchVO);
	}

	/**
	 * 전략체계도관리 지표 목록 조회
	 * @param	StrategyMapVO searchVO
	 * @return	List<StrategyMapVO>
	 * @throws	Exception
	 */
	public List<StrategyMapVO> selectMetricList(StrategyMapVO searchVO) throws Exception {
		return selectList("base.strategy.strategyMapMng.selectMetricList", searchVO);
	}

	/**
	 * 전략체계도관리 화살표 목록 조회
	 * @param	StrategyMapVO searchVO
	 * @return	List<StrategyMapVO>
	 * @throws	Exception
	 */
	public List<StrategyMapVO> selectArrowList(StrategyMapVO searchVO) throws Exception {
		return selectList("base.strategy.strategyMapMng.selectArrowList", searchVO);
	}

	/**
	 * 전략체계도 지표 표시 여부 조회
	 * @param	StrategyMapVO searchVO
	 * @return	List<StrategyMapVO>
	 * @throws	Exception
	 */
	public String selectShowMetricYn(StrategyMapVO searchVO) throws Exception {
		return CommonUtil.removeNull((String)selectOne("base.strategy.strategyMapMng.selectShowMetricYn", searchVO), "Y");
	}

	/**
	 * 비전 & 미션 저장
	 * @param	StrategyMapVO dataVO
	 * @return	int
	 */
	public int insertVisionMission(StrategyMapVO dataVO) {
		return update("base.strategy.strategyMapMng.insertVisionMission", dataVO);
	}

	/**
	 * 비전 & 미션 삭제
	 * @param	StrategyMapVO dataVO
	 * @return	int
	 */
	public int deleteVisionMission(StrategyMapVO dataVO) {
		return delete("base.strategy.strategyMapMng.deleteVisionMission", dataVO);
	}

	/**
	 * 전략체계도 삭제
	 * @param	StrategyMapVO dataVO
	 * @return	int
	 */
	public int deleteData(StrategyMapVO dataVO) throws Exception {
		return delete("base.strategy.strategyMapMng.deleteData", dataVO);
	}

	/**
	 * 전략체계도 저장
	 * @param	StrategyMapVO dataVO
	 * @return	int
	 */
	public int insertData(StrategyMapVO dataVO) {
		return update("base.strategy.strategyMapMng.insertData", dataVO);
	}
}
