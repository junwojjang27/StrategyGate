/*************************************************************************
* CLASS 명	: MetricMapServiceImpl
* 작 업 자	: kimyh
* 작 업 일	: 2018. 05. 03.
* 기	능	: 지표연계도 Service
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 05. 03.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.mon.org.metricMap.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.CommonVO;

@Service
public class MetricMapServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private MetricMapDAO metricMapDAO;

	/**
	 * 관점 목록 조회
	 * @param	CommonVO searchVO
	 * @return	List<EgovMap>
	 * @throws	Exception
	 */
	public List<EgovMap> selectPerspectiveList(CommonVO searchVO) throws Exception {
		return metricMapDAO.selectPerspectiveList(searchVO);
	}
	
	/**
	 * 전략 목록 조회
	 * @param	CommonVO searchVO
	 * @return	List<EgovMap>
	 * @throws	Exception
	 */
	public List<EgovMap> selectStrategyList(CommonVO searchVO) throws Exception {
		return metricMapDAO.selectStrategyList(searchVO);
	}
	
	/**
	 * 지표 목록 조회
	 * @param	CommonVO searchVO
	 * @return	List<EgovMap>
	 * @throws	Exception
	 */
	public List<EgovMap> selectMetricList(CommonVO searchVO) throws Exception {
		return metricMapDAO.selectMetricList(searchVO);
	}
}
