/*************************************************************************
* CLASS 명	: CompareScDeptGrpServiceIpml
* 작 업 자	: kimyh
* 작 업 일	: 2018-04-27
* 기	능	: 평가군별 비교 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018-04-27
**************************************************************************/
package kr.ispark.bsc.mon.compare.compareScDeptGrp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.bsc.mon.compare.compareScDeptGrp.service.CompareScDeptGrpVO;

@Service
public class CompareScDeptGrpServiceImpl extends EgovAbstractServiceImpl {
	@Resource
	private CompareScDeptGrpDAO compareScDeptGrpDAO;

	/**
	 * 평가군별 비교 목록 조회
	 * @param	CompareScDeptGrpVO searchVO
	 * @return	List<CompareScDeptGrpVO>
	 * @throws	Exception
	 */
	public List<CompareScDeptGrpVO> selectList(CompareScDeptGrpVO searchVO) throws Exception {
		return compareScDeptGrpDAO.selectList(searchVO);
	}
	
	/**
	 * 선택한 부서 점수 분석
	 * @param	CompareScDeptGrpVO searchVO
	 * @return	CompareScDeptGrpVO
	 * @throws	Exception
	 */
	public CompareScDeptGrpVO selectScoreAnalysis(CompareScDeptGrpVO searchVO) throws Exception {
		return compareScDeptGrpDAO.selectScoreAnalysis(searchVO);
	}
	
	/**
	 * 조직별 월별 추이
	 * @param	CompareScDeptGrpVO searchVO
	 * @return	List<CompareScDeptGrpVO>
	 * @throws	Exception
	 */
	public List<CompareScDeptGrpVO> selectMonthlyTrend(CompareScDeptGrpVO searchVO) throws Exception {
		return compareScDeptGrpDAO.selectMonthlyTrend(searchVO);
	}
	
	/**
	 * 같은 평가군 내 성과분석
	 * @param	CompareScDeptGrpVO searchVO
	 * @return	List<CompareScDeptGrpVO>
	 * @throws	Exception
	 */
	public List<CompareScDeptGrpVO> selectPerformanceList(CompareScDeptGrpVO searchVO) throws Exception {
		return compareScDeptGrpDAO.selectPerformanceList(searchVO);
	}
}
