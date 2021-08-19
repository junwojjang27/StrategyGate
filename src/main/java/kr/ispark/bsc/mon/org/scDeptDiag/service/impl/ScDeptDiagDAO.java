/*************************************************************************
* CLASS 명	: ScDeptDiagDAO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 05. 03.
* 기	능	: 성과조직도 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 05. 03.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.mon.org.scDeptDiag.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.bsc.mon.org.scDeptDiag.service.ScDeptDiagVO;

@Repository
public class ScDeptDiagDAO extends EgovComAbstractDAO {
	/**
	 * 성과조직도 지표 목록 조회
	 * @param	ScDeptDiagVO searchVO
	 * @return	List<ScDeptDiagVO>
	 * @throws	Exception
	 */
	public List<ScDeptDiagVO> selectList(ScDeptDiagVO searchVO) throws Exception {
		return selectList("mon.org.scDeptDiag.selectList", searchVO);
	}
	
	/**
	 * 성과조직도 차트 조회
	 * @param	ScDeptDiagVO searchVO
	 * @return	List<ScDeptDiagVO>
	 * @throws	Exception
	 */
	public List<ScDeptDiagVO> selectChartData(ScDeptDiagVO searchVO) throws Exception {
		return selectList("mon.org.scDeptDiag.selectChartData", searchVO);
	}
}
