/*************************************************************************
* CLASS 명	: ScDeptMappingDAO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 1. 29.
* 기	능	: 성과조직매핑 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 1. 29.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.scDept.scDeptMapping.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.common.system.service.ScDeptVO;

@Repository
public class ScDeptMappingDAO extends EgovComAbstractDAO {
	/**
	 * 매핑용 성과조직 목록 조회
	 * @param	ScDeptVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectScDeptList(ScDeptVO searchVO) throws Exception {
		return selectList("base.scDept.scDeptMapping.selectScDeptList", searchVO);
	}
	
	/**
	 * 매핑용 인사조직 목록 조회
	 * @param	ScDeptVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectDeptList(ScDeptVO searchVO) throws Exception {
		return selectList("base.scDept.scDeptMapping.selectDeptList", searchVO);
	}
	
	/**
	 * 매핑 목록 조회
	 * @param	ScDeptVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectMappingList(ScDeptVO searchVO) throws Exception {
		return selectList("base.scDept.scDeptMapping.selectMappingList", searchVO);
	}
	
	/**
	 * 엑셀다운로드용 매핑 목록 조회 (성과조직 기준)
	 * @param	ScDeptVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectMappingListForExcel(ScDeptVO searchVO) throws Exception {
		return selectList("base.scDept.scDeptMapping.selectMappingListForExcel", searchVO);
	}

	/**
	 * 엑셀다운로드용 매핑 목록 조회 (인사조직 기준)
	 * @param	ScDeptVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectMappingListForExcel2(ScDeptVO searchVO) throws Exception {
		return selectList("base.scDept.scDeptMapping.selectMappingListForExcel2", searchVO);
	}
	
	/**
	 * 매핑 목록 삭제
	 * @param	ScDeptVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteMapping(ScDeptVO dataVO) throws Exception {
		return delete("base.scDept.scDeptMapping.deleteMapping", dataVO);
	}

	/**
	 * 매핑 목록 저장
	 * @param	ScDeptVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertMapping(ScDeptVO dataVO) throws Exception {
		return insert("base.scDept.scDeptMapping.insertMapping", dataVO);
	}
}
