/*************************************************************************
* CLASS 명	: ScDeptMappingServiceImpl
* 작 업 자	: kimyh
* 작 업 일	: 2018. 1. 30.
* 기	능	: 성과조직매핑 Service
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 1. 30.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.scDept.scDeptMapping.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.common.system.service.ScDeptVO;

@Service
public class ScDeptMappingServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private ScDeptMappingDAO scDeptMappingDAO;

	/**
	 * 매핑용 성과조직 목록 조회
	 * @param	ScDeptVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectScDeptList(ScDeptVO searchVO) throws Exception {
		return scDeptMappingDAO.selectScDeptList(searchVO);
	}
	
	/**
	 * 매핑용 인사조직 목록 조회
	 * @param	ScDeptVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectDeptList(ScDeptVO searchVO) throws Exception {
		return scDeptMappingDAO.selectDeptList(searchVO);
	}
	
	/**
	 * 매핑 목록 조회
	 * @param	ScDeptVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectMappingList(ScDeptVO searchVO) throws Exception {
		return scDeptMappingDAO.selectMappingList(searchVO);
	}
	
	/**
	 * 엑셀다운로드용 매핑 목록 조회 (성과조직 기준)
	 * @param	ScDeptVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectMappingListForExcel(ScDeptVO searchVO) throws Exception {
		return scDeptMappingDAO.selectMappingListForExcel(searchVO);
	}
	
	/**
	 * 엑셀다운로드용 매핑 목록 조회 (인사조직 기준)
	 * @param	ScDeptVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectMappingListForExcel2(ScDeptVO searchVO) throws Exception {
		return scDeptMappingDAO.selectMappingListForExcel2(searchVO);
	}
	
	/**
	 * 매핑 정보 저장
	 * @param	ScDeptVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveMapping(ScDeptVO dataVO) throws Exception {
		int result = scDeptMappingDAO.deleteMapping(dataVO);
		if(dataVO.getDeptIds() != null && dataVO.getDeptIds().size() > 0) {
			result = scDeptMappingDAO.insertMapping(dataVO);
		}
		return result;
	}
}
