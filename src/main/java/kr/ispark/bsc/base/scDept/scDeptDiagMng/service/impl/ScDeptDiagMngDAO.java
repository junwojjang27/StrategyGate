/*************************************************************************
* CLASS 명	: ScDeptDiagMngDAO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 1. 31.
* 기	능	: 성과조직도 관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 1. 31.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.scDept.scDeptDiagMng.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.bsc.base.scDept.scDeptDiagMng.service.ScDeptDiagMngVO;

@Repository
public class ScDeptDiagMngDAO extends EgovComAbstractDAO {
	/**
	 * 성과조직 목록 조회
	 * @param	ScDeptDiagMngVO searchVO
	 * @return	List<ScDeptDiagMngVO>
	 * @throws	Exception
	 */
	public List<ScDeptDiagMngVO> selectList(ScDeptDiagMngVO searchVO) throws Exception {
		return selectList("base.scDept.scDeptDiagMng.selectList", searchVO);
	}

	/**
	 * 신호등 조회
	 * @param	ScDeptDiagMngVO searchVO
	 * @return	List<ScDeptDiagMngVO>
	 * @throws	Exception
	 */
	public List<ScDeptDiagMngVO> selectSignalList(ScDeptDiagMngVO searchVO) throws Exception {
		return selectList("base.scDept.scDeptDiagMng.selectSignalList", searchVO);
	}

	/**
	 * 성과조직도 조회
	 * @param	ScDeptDiagMngVO searchVO
	 * @return	List<ScDeptDiagMngVO>
	 * @throws	Exception
	 */
	public List<ScDeptDiagMngVO> getList(ScDeptDiagMngVO searchVO) throws Exception {
		return selectList("base.scDept.scDeptDiagMng.getList", searchVO);
	}

	/**
	 * 신호등 조회
	 * @param	ScDeptDiagMngVO searchVO
	 * @return	List<ScDeptDiagMngVO>
	 * @throws	Exception
	 */
	public List<ScDeptDiagMngVO> getSignal(ScDeptDiagMngVO searchVO) throws Exception {
		return selectList("base.scDept.scDeptDiagMng.getSignal", searchVO);
	}

	/**
	 * 좌표 등록 여부 확인
	 * @param	ScDeptDiagMngVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectOrgChartCnt(ScDeptDiagMngVO searchVO) throws Exception {
		return selectOne("base.scDept.scDeptDiagMng.selectOrgChartCnt", searchVO);
	}

	/**
	 * 성과조직도 삭제
	 */
	public int deleteData(ScDeptDiagMngVO dataVO) throws Exception {
		return delete("base.scDept.scDeptDiagMng.deleteData", dataVO);
	}

	/**
	 * 성과조직도 등록
	 */
	public int insertData(ScDeptDiagMngVO dataVO) throws Exception {
		return insert("base.scDept.scDeptDiagMng.insertData", dataVO);
	}
}
