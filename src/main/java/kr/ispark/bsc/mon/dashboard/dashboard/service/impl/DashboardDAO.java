/*************************************************************************
* CLASS 명	: DashboardDAO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-08
* 기	능	: dashboard DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-08
**************************************************************************/
package kr.ispark.bsc.mon.dashboard.dashboard.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.bsc.mon.dashboard.dashboard.service.DashboardVO;

@Repository
public class DashboardDAO extends EgovComAbstractDAO {
	/**
	 * dashboard 목록 조회
	 * @param	DashboardVO searchVO
	 * @return	List<DashboardVO>
	 * @throws	Exception
	 */
	public List<DashboardVO> selectList(DashboardVO searchVO) throws Exception {
		return selectList("mon.dashboard.dashboard.selectList", searchVO);
	}
	
	public List<DashboardVO> selectItemList(DashboardVO searchVO) throws Exception {
		return selectList("mon.dashboard.dashboard.selectItemList", searchVO);
	}
	
	public List<DashboardVO> selectItemUserList(DashboardVO searchVO) throws Exception {
		return selectList("mon.dashboard.dashboard.selectItemUserList", searchVO);
	}
	
	public List<DashboardVO> selectMetricTop5List(DashboardVO searchVO) throws Exception {
		return selectList("mon.dashboard.dashboard.selectMetricTop5List", searchVO);
	}
	
	public DashboardVO selectOrgScore(DashboardVO searchVO) throws Exception {
		return (DashboardVO)selectOne("mon.dashboard.dashboard.selectOrgScore", searchVO);
	}
	
	public List<DashboardVO> selectPerspectiveScoreList(DashboardVO searchVO) throws Exception {
		return selectList("mon.dashboard.dashboard.selectPerspectiveScoreList", searchVO);
	}
	
	public List<DashboardVO> selectMainMetricList(DashboardVO searchVO) throws Exception {
		return selectList("mon.dashboard.dashboard.selectMainMetricList", searchVO);
	}
	
	public List<DashboardVO> selectEvalGrpList(DashboardVO searchVO) throws Exception {
		return selectList("mon.dashboard.dashboard.selectEvalGrpList", searchVO);
	}
	
	public List<DashboardVO> selectGovList(DashboardVO searchVO) throws Exception {
		return selectList("mon.dashboard.dashboard.selectGovList", searchVO);
	}
	
	public List<DashboardVO> selectMetricList(DashboardVO searchVO) throws Exception {
		return selectList("mon.dashboard.dashboard.selectMetricList", searchVO);
	}
	
	public DashboardVO selectMetricDetail(DashboardVO searchVO) throws Exception {
		return (DashboardVO)selectOne("mon.dashboard.dashboard.selectMetricDetail", searchVO);
	}
	
	public DashboardVO selectChartTarget(DashboardVO searchVO) throws Exception {
		return (DashboardVO)selectOne("mon.dashboard.dashboard.selectChartTarget", searchVO);
	}
	
	public DashboardVO selectChartActual(DashboardVO searchVO) throws Exception {
		return (DashboardVO)selectOne("mon.dashboard.dashboard.selectChartActual", searchVO);
	}
	
	public DashboardVO selectChartScore(DashboardVO searchVO) throws Exception {
		return (DashboardVO)selectOne("mon.dashboard.dashboard.selectChartScore", searchVO);
	}
	
	public DashboardVO selectScoreAnalysis(DashboardVO searchVO) throws Exception {
		return (DashboardVO)selectOne("mon.dashboard.dashboard.selectScoreAnalysis", searchVO);
	}
	
	public List<DashboardVO> selectUserMetricList(DashboardVO searchVO) throws Exception{
		return selectList("mon.dashboard.dashboard.selectUserMetricList", searchVO);
	}
	
	/**
	 * dashboard 삭제
	 * @param	DashboardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteItemCData(DashboardVO searchVO) throws Exception {
		return update("mon.dashboard.dashboard.deleteItemCData", searchVO);
	}
	
	/**
	 * dashboard 저장
	 * @param	DashboardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertItemCData(DashboardVO searchVO) throws Exception {
		return insert("mon.dashboard.dashboard.insertItemCData", searchVO);
	}
	
	/**
	 * dashboard 상세 조회
	 * @param	DashboardVO searchVO
	 * @return	DashboardVO
	 * @throws	Exception
	 */
	public DashboardVO selectDetail(DashboardVO searchVO) throws Exception {
		return (DashboardVO)selectOne("mon.dashboard.dashboard.selectDetail", searchVO);
	}
	
	/**
	 * dashboard 정렬순서저장
	 * @param	DashboardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(DashboardVO searchVO) throws Exception {
		return update("mon.dashboard.dashboard.updateSortOrder", searchVO);
	}

	/**
	 * dashboard 삭제
	 * @param	DashboardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteDashboard(DashboardVO searchVO) throws Exception {
		return update("mon.dashboard.dashboard.deleteDashboard", searchVO);
	}
	
	/**
	 * dashboard 저장
	 * @param	DashboardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(DashboardVO searchVO) throws Exception {
		return insert("mon.dashboard.dashboard.insertData", searchVO);
	}

	/**
	 * dashboard 수정
	 * @param	DashboardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(DashboardVO searchVO) throws Exception {
		return update("mon.dashboard.dashboard.updateData", searchVO);
	}
}

