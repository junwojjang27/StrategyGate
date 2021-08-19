/*************************************************************************
* CLASS 명	: CompDeptMngDAO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 6. 29.
* 기	능	: 조직관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 6. 29.		최 초 작 업
**************************************************************************/
package kr.ispark.system.system.comp.compDeptMng.service.impl;


import java.util.List;

import kr.ispark.common.CommonVO;
import kr.ispark.common.system.service.ScDeptVO;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository
public class CompDeptMngDAO extends EgovComAbstractDAO {
	/**
	 * 조직 조회
	 */
	public List<ScDeptVO> selectList(ScDeptVO searchVO) throws Exception {
		return selectList("system.system.comp.compDeptMng.selectList", searchVO);
	}
	
	/**
	 * 조직 조회
	 */
	public List<ScDeptVO> selectExcelList(ScDeptVO searchVO) throws Exception {
		return selectList("system.system.comp.compDeptMng.selectExcelList", searchVO);
	}

	/**
	 * 하위 조직수 조회
	 */
	public int selectSubScDeptCount(ScDeptVO searchVO) {
		return (Integer)selectOne("system.system.comp.compDeptMng.selectSubScDeptCount", searchVO);
	}
	
	/**
	 * 상위 조직ID 조회
	 */
	public String selectUpScDeptId(ScDeptVO searchVO) {
		return (String)selectOne("system.system.comp.compDeptMng.selectUpScDeptId", searchVO);
	}
	
	/**
	 * 조직 상세 조회
	 */
	public ScDeptVO selectDetail(ScDeptVO searchVO) throws Exception {
		return (ScDeptVO)selectOne("system.system.comp.compDeptMng.selectDetail", searchVO);
	}
	
	/**
	 * 조직 전체 삭제
	 * @param	ScDeptVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteScDeptAll(ScDeptVO dataVO) throws Exception {
		return delete("system.system.comp.compDeptMng.deleteScDeptAll", dataVO);
	}
	
	/**
	 * 조직 조회
	 */
	public List<ScDeptVO> selectReDefineData(ScDeptVO searchVO) throws Exception {
		return selectList("system.system.comp.compDeptMng.selectReDefineData", searchVO);
	}
	
	/**
	 * 조직별 지표수 조회
	 * @param	ScDeptVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectMetricCountList(ScDeptVO searchVO) throws Exception {
		return selectList("system.system.comp.compDeptMng.selectMetricCountList", searchVO);
	}
	
	/**
	 * 성과조직 채번
	 * @param	ScDeptVO searchVO
	 * @return	String
	 * @throws	Exception
	 */
	public String selectNewScDeptId(ScDeptVO searchVO) throws Exception {
		return selectOne("system.system.comp.compDeptMng.selectNewScDeptId", searchVO);
	}

	/**
	 * 조직삭제
	 */
	public int deleteCompDeptMng(ScDeptVO searchVO) {
		return update("system.system.comp.compDeptMng.deleteCompDeptMng", searchVO);
	}
	
	/**
	 * 조직 저장
	 */
	public int insertData(ScDeptVO searchVO) {
		return update("system.system.comp.compDeptMng.insertData", searchVO);
	}

	/**
	 * 조직 수정
	 */
	public int updateData(ScDeptVO dataVO) {
		return update("system.system.comp.compDeptMng.updateData", dataVO);
	}
	
	/**
	 * 조직 재정의 데이터 수정
	 */
	public int updateReDefineData(ScDeptVO dataVO) {
		return update("system.system.comp.compDeptMng.updateReDefineData", dataVO);
	}
	
	/**
	 * 조직 등록/수정 후 권한 등록
	 * @param	ScDeptVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertDeptinfoAndMapping(ScDeptVO dataVO) {
		int resultCnt = 0;
		delete("system.system.comp.compDeptMng.deleteDeptinfo", dataVO);
		resultCnt += insert("system.system.comp.compDeptMng.insertDeptinfoFromScDeptList", dataVO);
		delete("system.system.comp.compDeptMng.deleteScDeptMapping", dataVO);
		resultCnt += insert("system.system.comp.compDeptMng.insertScDeptMapping", dataVO);
		
		return resultCnt;
	}
	
	/**
	 * 조직 등록/수정 후 권한 등록
	 * @param	ScDeptVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertAuth(ScDeptVO dataVO) throws Exception {
		int resultCnt = 0;
		delete("system.system.comp.compDeptMng.deleteAuth", dataVO);
		resultCnt = insert("system.system.comp.compDeptMng.insertAuth", dataVO);
		return resultCnt;
	}
	
	/**
	 * 성과지표 담당자 갱신
	 * @param	ScDeptVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateMetricUserId(ScDeptVO dataVO) throws Exception {
		return update("system.system.comp.compDeptMng.updateMetricUserId", dataVO);
	}
	
	/**
	 * 지표담당자용 조직 조회
	 * @param	CommonVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectScDeptListByBscUserId(CommonVO searchVO) throws Exception {
		return selectList("system.system.comp.compDeptMng.selectScDeptListByBscUserId", searchVO);
	}
	
	/**
	 * 부서장용 조직 조회
	 * @param	CommonVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectScDeptListByManagerUserId(CommonVO searchVO) throws Exception {
		return selectList("system.system.comp.compDeptMng.selectScDeptListByManagerUserId", searchVO);
	}
	
	/**
	 * 조직 중복 체크
	 * @param	ScDeptVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectDeptCnt(ScDeptVO searchVO) throws Exception{
		return selectOne("system.system.comp.compDeptMng.selectDeptCnt", searchVO);
	}
	
	/**
	 * 조직 수정
	 */
	public int updateOrinData(ScDeptVO dataVO) {
		return update("system.system.comp.compDeptMng.updateOrinData", dataVO);
	}
}
