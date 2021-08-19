/*************************************************************************
* CLASS 명	: MetricDiagDAO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-03
* 기	능	: 지표연계도 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-03
**************************************************************************/
package kr.ispark.bsc.mon.orgOutput.metricDiag.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.bsc.mon.orgOutput.metricDiag.service.MetricDiagVO;

@Repository
public class MetricDiagDAO extends EgovComAbstractDAO {
	/**
	 * 지표연계도 목록 조회
	 * @param	MetricDiagVO searchVO
	 * @return	List<MetricDiagVO>
	 * @throws	Exception
	 */
	public List<MetricDiagVO> selectList(MetricDiagVO searchVO) throws Exception {
		return selectList("mon.orgOutput.metricDiag.selectList", searchVO);
	}
	
	/**
	 * 지표연계도 목록 조회
	 * @param	MetricDiagVO searchVO
	 * @return	List<MetricDiagVO>
	 * @throws	Exception
	 */
	public List<MetricDiagVO> selectMetricList(MetricDiagVO searchVO) throws Exception {
		return selectList("mon.orgOutput.metricDiag.selectMetricList", searchVO);
	}
	
	/**
	 * 지표연계도 상세 조회
	 * @param	MetricDiagVO searchVO
	 * @return	MetricDiagVO
	 * @throws	Exception
	 */
	public MetricDiagVO selectDetail(MetricDiagVO searchVO) throws Exception {
		return (MetricDiagVO)selectOne("mon.orgOutput.metricDiag.selectDetail", searchVO);
	}
	
	/**
	 * 지표연계도 정렬순서저장
	 * @param	MetricDiagVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(MetricDiagVO searchVO) throws Exception {
		return update("mon.orgOutput.metricDiag.updateSortOrder", searchVO);
	}

	/**
	 * 지표연계도 삭제
	 * @param	MetricDiagVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteMetricDiag(MetricDiagVO searchVO) throws Exception {
		return update("mon.orgOutput.metricDiag.deleteMetricDiag", searchVO);
	}
	
	/**
	 * 지표연계도 저장
	 * @param	MetricDiagVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(MetricDiagVO searchVO) throws Exception {
		return insert("mon.orgOutput.metricDiag.insertData", searchVO);
	}

	/**
	 * 지표연계도 수정
	 * @param	MetricDiagVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(MetricDiagVO searchVO) throws Exception {
		return update("mon.orgOutput.metricDiag.updateData", searchVO);
	}
}

