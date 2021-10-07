/*************************************************************************
* CLASS 명	: ScDeptMngDAO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 1. 23.
* 기	능	: 성과조직관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 1. 23.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.scDept.scDeptMng.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.common.CommonVO;
import kr.ispark.common.system.service.ScDeptVO;
import kr.ispark.common.util.CommonUtil;

@Repository
public class ScDeptMngDAO extends EgovComAbstractDAO {
	/**
	 * 성과조직 조회
	 */
	public List<ScDeptVO> selectList(ScDeptVO searchVO) throws Exception {
		return selectList("base.scDept.scDeptMng.selectList", searchVO);
	}
	
	/**
	 * 성과조직 조회
	 */
	public List<ScDeptVO> selectExcelList(ScDeptVO searchVO) throws Exception {
		System.out.println("DAO");
		System.out.println("searchVO : " + searchVO);
		return selectList("base.scDept.scDeptMng.selectExcelList", searchVO);
	}

	/**
	 * 하위 성과조직수 조회
	 */
	public int selectSubScDeptCount(ScDeptVO searchVO) {
		return (Integer)selectOne("base.scDept.scDeptMng.selectSubScDeptCount", searchVO);
	}
	
	/**
	 * 상위 성과조직ID 조회
	 */
	public String selectUpScDeptId(ScDeptVO searchVO) {
		return (String)selectOne("base.scDept.scDeptMng.selectUpScDeptId", searchVO);
	}
	
	/**
	 * 성과조직 상세 조회
	 */
	public ScDeptVO selectDetail(ScDeptVO searchVO) throws Exception {
		return (ScDeptVO)selectOne("base.scDept.scDeptMng.selectDetail", searchVO);
	}
	
	/**
	 * 성과조직 조회
	 */
	public List<ScDeptVO> selectReDefineData(ScDeptVO searchVO) throws Exception {
		return selectList("base.scDept.scDeptMng.selectReDefineData", searchVO);
	}
	
	/**
	 * 정렬순서저장
	 */
	public int updateSortOrder(ScDeptVO searchVO) {
		return update("base.scDept.scDeptMng.updateSortOrder", searchVO);
	}
	
	/**
	 * 성과조직별 지표수 조회
	 * @param	ScDeptVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectMetricCountList(ScDeptVO searchVO) throws Exception {
		return selectList("base.scDept.scDeptMng.selectMetricCountList", searchVO);
	}

	/**
	 * 성과조직삭제
	 */
	public int deleteScDeptMng(ScDeptVO searchVO) {
		return update("base.scDept.scDeptMng.deleteScDeptMng", searchVO);
	}
	
	/**
	 * 성과조직 저장
	 */
	public int insertData(ScDeptVO searchVO) {
		return update("base.scDept.scDeptMng.insertData", searchVO);
	}

	/**
	 * 성과조직 수정
	 */
	public int updateData(ScDeptVO searchVO) {
		return update("base.scDept.scDeptMng.updateData", searchVO);
	}
	
	/**
	 * 성과조직 재정의 데이터 수정
	 */
	public int updateReDefineData(ScDeptVO searchVO) {
		return update("base.scDept.scDeptMng.updateReDefineData", searchVO);
	}
	
	/**
	 * 성과조직 등록/수정 후 권한 등록
	 * @param	ScDeptVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertAuth(ScDeptVO dataVO) throws Exception {
		int resultCnt = 0;
		// 성과조직 저장, 수정시 모든 지표의 담당자를 초기화 할 것인지 여부
		if(CommonUtil.removeNull(dataVO.getResetAllYn(), "N").equals("Y")) {
			update("base.scDept.scDeptMng.updateMetricUserId", dataVO);
		}
		delete("base.scDept.scDeptMng.deleteAuth", dataVO);
		resultCnt = insert("base.scDept.scDeptMng.insertAuth", dataVO);
		return resultCnt;
	}
	
	/**
	 * 지표담당자용 성과조직 조회
	 * @param	CommonVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectScDeptListByBscUserId(CommonVO searchVO) throws Exception {
		return selectList("base.scDept.scDeptMng.selectScDeptListByBscUserId", searchVO);
	}
	
	/**
	 * 부서장용 성과조직 조회
	 * @param	CommonVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	Exception
	 */
	public List<ScDeptVO> selectScDeptListByManagerUserId(CommonVO searchVO) throws Exception {
		return selectList("base.scDept.scDeptMng.selectScDeptListByManagerUserId", searchVO);
	}
}
