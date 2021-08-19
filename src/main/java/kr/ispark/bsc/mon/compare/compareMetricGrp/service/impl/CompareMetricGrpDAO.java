/*************************************************************************
* CLASS 명	: CompareMetricGrpDAO
* 작 업 자	: kimyh
* 작 업 일	: 2018-05-02
* 기	능	: 평가군별 비교 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018-05-02
**************************************************************************/
package kr.ispark.bsc.mon.compare.compareMetricGrp.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.bsc.mon.compare.compareMetricGrp.service.CompareMetricGrpVO;

@Repository
public class CompareMetricGrpDAO extends EgovComAbstractDAO {
	/**
	 * 지표POOL 조회
	 * @param	CompareMetricGrpVO searchVO
	 * @return	List<CompareMetricGrpVO>
	 * @throws	Exception
	 */
	public List<CompareMetricGrpVO> selectMetricGrpList(CompareMetricGrpVO searchVO) throws Exception {
		return selectList("mon.compare.compareMetricGrp.selectMetricGrpList", searchVO);
	}

	/**
	 * 지표POOL별 비교 목록 조회
	 * @param	CompareMetricGrpVO searchVO
	 * @return	List<CompareMetricGrpVO>
	 * @throws	Exception
	 */
	public List<CompareMetricGrpVO> selectList(CompareMetricGrpVO searchVO) throws Exception {
		return selectList("mon.compare.compareMetricGrp.selectList", searchVO);
	}
	
	/**
	 * 선택한 부서 점수 분석
	 * @param	CompareMetricGrpVO searchVO
	 * @return	CompareMetricGrpVO
	 * @throws	Exception
	 */
	public CompareMetricGrpVO selectScoreAnalysis(CompareMetricGrpVO searchVO) throws Exception {
		return selectOne("mon.compare.compareMetricGrp.selectScoreAnalysis", searchVO);
	}
	
	/**
	 * 조직별 월별 추이
	 * @param	CompareMetricGrpVO searchVO
	 * @return	List<CompareMetricGrpVO>
	 * @throws	Exception
	 */
	public List<CompareMetricGrpVO> selectMonthlyTrend(CompareMetricGrpVO searchVO) throws Exception {
		return selectList("mon.compare.compareMetricGrp.selectMonthlyTrend", searchVO);
	}
}

