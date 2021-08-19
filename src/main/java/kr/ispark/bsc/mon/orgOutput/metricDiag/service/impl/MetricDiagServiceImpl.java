/*************************************************************************
* CLASS 명	: MetricDiagServiceIpml
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-03
* 기	능	: 지표연계도 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-03
**************************************************************************/
package kr.ispark.bsc.mon.orgOutput.metricDiag.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.bsc.mon.orgOutput.metricDiag.service.MetricDiagVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class MetricDiagServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private MetricDiagDAO metricDiagDAO;

	/**
	 * 지표연계도 목록 조회
	 * @param	MetricDiagVO searchVO
	 * @return	List<MetricDiagVO>
	 * @throws	Exception
	 */
	public List<MetricDiagVO> selectList(MetricDiagVO searchVO) throws Exception {
		return metricDiagDAO.selectList(searchVO);
	}
	
	/**
	 * 지표연계도 목록 조회
	 * @param	MetricDiagVO searchVO
	 * @return	List<MetricDiagVO>
	 * @throws	Exception
	 */
	public List<MetricDiagVO> selectMetricList(MetricDiagVO searchVO) throws Exception {
		return metricDiagDAO.selectMetricList(searchVO);
	}
	
	/**
	 * 지표연계도 상세 조회
	 * @param	MetricDiagVO searchVO
	 * @return	MetricDiagVO
	 * @throws	Exception
	 */
	public MetricDiagVO selectDetail(MetricDiagVO searchVO) throws Exception {
		return metricDiagDAO.selectDetail(searchVO);
	}
	
	/**
	 * 지표연계도 정렬순서저장
	 * @param	MetricDiagVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(MetricDiagVO dataVO) throws Exception {
		int resultCnt = 0;
		for(MetricDiagVO paramVO : dataVO.getGridDataList()) {
			resultCnt += metricDiagDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	/**
	 * 지표연계도 삭제
	 * @param	MetricDiagVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteMetricDiag(MetricDiagVO dataVO) throws Exception {
		return metricDiagDAO.deleteMetricDiag(dataVO);
	}
	
	/**
	 * 지표연계도 저장
	 * @param	MetricDiagVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(MetricDiagVO dataVO) throws Exception {
		String key = "";
		if(CommonUtil.isEmpty(dataVO.getMetricId())) {
			key = idgenService.selectNextSeqByYear("originalTableName", dataVO.getYear(), "S", 6, "0");
			dataVO.setMetricId(key);
			return metricDiagDAO.insertData(dataVO);
		} else {
			return metricDiagDAO.updateData(dataVO);
		}
	}
}

