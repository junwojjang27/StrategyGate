/*************************************************************************
* CLASS 명	: DashboardServiceIpml
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-08
* 기	능	: dashboard ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-08
**************************************************************************/
package kr.ispark.bsc.mon.dashboard.dashboard.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.bsc.mon.dashboard.dashboard.service.DashboardVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class DashboardServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private DashboardDAO dashboardDAO;

	/**
	 * dashboard 목록 조회
	 * @param	DashboardVO searchVO
	 * @return	List<DashboardVO>
	 * @throws	Exception
	 */
	public List<DashboardVO> selectList(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectList(searchVO);
	}
	
	public List<DashboardVO> selectItemList(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectItemList(searchVO);
	}
	
	public List<DashboardVO> selectItemUserList(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectItemUserList(searchVO);
	}
	
	public List<DashboardVO> selectMetricTop5List(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectMetricTop5List(searchVO);
	}
	
	public DashboardVO selectOrgScore(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectOrgScore(searchVO);
	}
	
	public List<DashboardVO> selectPerspectiveScoreList(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectPerspectiveScoreList(searchVO);
	}
	
	public List<DashboardVO> selectMainMetricList(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectMainMetricList(searchVO);
	}
	
	public List<DashboardVO> selectEvalGrpList(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectEvalGrpList(searchVO);
	}
	
	public List<DashboardVO> selectGovList(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectGovList(searchVO);
	}
	
	public List<DashboardVO> selectMetricList(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectMetricList(searchVO);
	}
	
	public DashboardVO selectMetricDetail(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectMetricDetail(searchVO);
	}
	
	public DashboardVO selectChartTarget(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectChartTarget(searchVO);
	}
	
	public DashboardVO selectChartActual(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectChartActual(searchVO);
	}
	
	public DashboardVO selectChartScore(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectChartScore(searchVO);
	}
	
	public DashboardVO selectScoreAnalysis(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectScoreAnalysis(searchVO);
	}
	
	public List<DashboardVO> selectUserMetricList(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectUserMetricList(searchVO);
	}
	
	public int saveItemCData(DashboardVO searchVO) throws Exception {
		int resultCnt = 0;
		String[] selectedMetricIds = searchVO.getSelectedMetricIds().split("\\|", 0);
		String[] selectedSortOrders = searchVO.getSelectedSortOrders().split("\\|", 0);
		
		dashboardDAO.deleteItemCData(searchVO);
		
		if(null != selectedMetricIds && selectedMetricIds.length > 0){
			for(int i=0 ; i<selectedMetricIds.length ; i++){
				searchVO.setMetricId(selectedMetricIds[i]);
				searchVO.setSortOrder(selectedSortOrders[i]);
				
				resultCnt += dashboardDAO.insertItemCData(searchVO);
			}
		}
		
		return resultCnt;
	}
	
	/**
	 * dashboard 상세 조회
	 * @param	DashboardVO searchVO
	 * @return	DashboardVO
	 * @throws	Exception
	 */
	public DashboardVO selectDetail(DashboardVO searchVO) throws Exception {
		return dashboardDAO.selectDetail(searchVO);
	}
	
	/**
	 * dashboard 정렬순서저장
	 * @param	DashboardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(DashboardVO dataVO) throws Exception {
		int resultCnt = 0;
		for(DashboardVO paramVO : dataVO.getGridDataList()) {
			resultCnt += dashboardDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	/**
	 * dashboard 삭제
	 * @param	DashboardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteDashboard(DashboardVO dataVO) throws Exception {
		return dashboardDAO.deleteDashboard(dataVO);
	}
	
	/**
	 * dashboard 저장
	 * @param	DashboardVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(DashboardVO dataVO) throws Exception {
		
		int resultCnt = 0;
		int idx = 1;
		
		dashboardDAO.deleteDashboard(dataVO);
		String[] itemIds = dataVO.getItemIds().split("\\|");
		if(itemIds != null && itemIds.length > 0){
			for(String itemId : itemIds){
				dataVO.setItemId(itemId);
				dataVO.setSortOrder(String.valueOf(idx));
				resultCnt += dashboardDAO.insertData(dataVO);
				idx++;
			}
		}
		
		return resultCnt;
	}
}

