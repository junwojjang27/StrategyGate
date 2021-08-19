/*************************************************************************
* CLASS 명	: MetricMapDAO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 05. 08.
* 기	능	: 지표연계도 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 05. 08.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.mon.org.metricMap.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.CommonVO;

@Repository
public class MetricMapDAO extends EgovComAbstractDAO {
	/**
	 * 관점 목록 조회
	 * @param	CommonVO searchVO
	 * @return	List<EgovMap>
	 * @throws	Exception
	 */
	public List<EgovMap> selectPerspectiveList(CommonVO searchVO) throws Exception {
		return selectList("mon.org.metricMap.selectPerspectiveList", searchVO);
	}
	
	/**
	 * 전략 목록 조회
	 * @param	CommonVO searchVO
	 * @return	List<EgovMap>
	 * @throws	Exception
	 */
	public List<EgovMap> selectStrategyList(CommonVO searchVO) throws Exception {
		return selectList("mon.org.metricMap.selectStrategyList", searchVO);
	}
	
	/**
	 * 지표 목록 조회
	 * @param	CommonVO searchVO
	 * @return	List<EgovMap>
	 * @throws	Exception
	 */
	public List<EgovMap> selectMetricList(CommonVO searchVO) throws Exception {
		return selectList("mon.org.metricMap.selectMetricList", searchVO);
	}
}
