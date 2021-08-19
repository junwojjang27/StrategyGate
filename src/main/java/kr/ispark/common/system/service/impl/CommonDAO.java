/*************************************************************************
* CLASS 명	: CommonDAO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 01. 24.
* 기	능	: 공통모듈 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 01. 24.		최 초 작 업
**************************************************************************/

package kr.ispark.common.system.service.impl;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.CommonVO;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.DeptVO;
import kr.ispark.common.system.service.MenuVO;
import kr.ispark.common.system.service.ScDeptVO;

@Repository
public class CommonDAO extends EgovComAbstractDAO {
	/**
	 * 모니터링 권한에 따른 최상위 성과조직ID 조회
	 * @param	EgovMap egovMap
	 * @return	String
	 * @throws	SQLException
	 */
	public String selectMonitoringRootScDeptId(EgovMap egovMap) throws SQLException {
		return selectOne("common.selectMonitoringRootScDeptId", egovMap);
	}

	/**
	 * 성과조직 목록 조회
	 * @param	CommonVO searchVO
	 * @return	List<ScDeptVO>
	 * @throws	SQLException
	 */
	public List<ScDeptVO> selectScDeptList(CommonVO searchVO) throws SQLException {
		return selectList("common.selectScDeptList", searchVO);
	}

	/**
	 * 최상위 성과조직 자동 생성
	 * @param	ScDeptVO dataVO
	 * @return	int
	 * @throws	SQLException
	 */
	public int insertRootScDeptBySystem(ScDeptVO dataVO) throws SQLException {
		return insert("common.insertRootScDeptBySystem", dataVO);
	}

	/**
	 * 조직 목록 조회
	 * @param	CommonVO searchVO
	 * @return	List<DeptVO>
	 * @throws	SQLException
	 */
	public List<DeptVO> selectDeptList(CommonVO searchVO) throws SQLException {
		return selectList("common.selectDeptList", searchVO);
	}

	/**
	 * 최상위 조직 자동 생성
	 * @param	CommonVO searchVO
	 * @return	int
	 * @throws	SQLException
	 */
	public int insertRootDeptBySystem(CommonVO searchVO) throws SQLException {
		return insert("common.insertRootDeptBySystem", searchVO);
	}

	/**
	 * 사용자 목록 조회
	 * @param	UserVO searchVO
	 * @return	List<UserVO>
	 * @throws	SQLException
	 */
	public List<UserVO> selectUserList(UserVO searchVO) throws SQLException {
		return selectList("common.selectUserList", searchVO);
	}

	/**
	 * 즐겨찾기 목록 조회
	 * @param	MenuVO searchVO
	 * @return	List<MenuVO>
	 * @throws	SQLException
	 */
	public List<MenuVO> selectBookmarkList(MenuVO searchVO) throws SQLException {
		return selectList("common.selectBookmarkList", searchVO);
	}

	/**
	 * 즐겨찾기 설정 가능한 메뉴인지 확인
	 * @param	MenuVO searchVO
	 * @return	int
	 * @throws	SQLException
	 */
	public int selectCheckPgmId(MenuVO searchVO) throws SQLException {
		return selectOne("common.selectCheckPgmId", searchVO);
	}

	/**
	 * 즐겨찾기 삭제
	 * @param	MenuVO dataVO
	 * @return	int
	 * @throws	SQLException
	 */
	public int deleteBookmark(MenuVO dataVO) throws SQLException {
		return delete("common.deleteBookmark", dataVO);
	}

	/**
	 * 즐겨찾기 등록
	 * @param	MenuVO dataVO
	 * @return	int
	 * @throws	SQLException
	 */
	public int insertBookmark(MenuVO dataVO) throws SQLException {
		return insert("common.insertBookmark", dataVO);
	}

	/**
	 * 승인사용설정 확인
	 * @param	CommonVO searchVO
	 * @return	EgovMap
	 * @throws	SQLException
	 */
	public EgovMap selectApproveUse(CommonVO searchVO) throws SQLException {
		return selectOne("common.selectApproveUse", searchVO);
	}

	/**
	 * 사용자의 성과조직 정보 조회
	 * @param	CommonVO searchVO
	 * @return	ScDeptVO
	 * @throws	SQLException
	 */
	public ScDeptVO selectScDeptByUser(CommonVO searchVO) throws SQLException {
		return selectOne("common.selectScDeptByUser", searchVO);
	}

	/**
	 * 성과조직 평가군ID 조회
	 * @param	CommonVO searchVO
	 * @return	String
	 * @throws	SQLException
	 */
	public String selectScDeptGrpId(CommonVO searchVO) throws SQLException {
		return selectOne("common.selectScDeptGrpId", searchVO);
	}

	/**
	 * 실적월마감 조회
	 * @param	EgovMap emap
	 * @return	String
	 * @throws	Exception
	 */
	public String selectActualCloseYn(CommonVO dataVO) throws Exception {
		return selectOne("common.selectActualCloseYn", dataVO);
	}

	/**
	 * 최상위조직 정보 조회
	 * @param	String findYear
	 * @return	ScDeptVO
	 * @throws	Exception
	 */
	public ScDeptVO selectTopScDeptInfo(String findYear) throws Exception {
		return selectOne("common.selectTopScDeptInfo", findYear);
	}

	/**
	 * 사용자 email 조회
	 * @param	CommonVO searchVO
	 * @return	String
	 * @throws	Exception
	 */
	public String selectUserEmail(CommonVO searchVO) throws Exception {
		return selectOne("common.selectUserEmail", searchVO);
	}

	/**
	 * 사용자 email 조회
	 * @param	Map	map
	 * @return	List<String>
	 * @throws	Exception
	 */
	public List<String> selectUserEmailList(Map<String, Object> map) throws Exception {
		return selectList("common.selectUserEmailList", map);
	}

	/**
	 * 디자인 테마 조회
	 * @param	CommonVO searchVO
	 * @return	String
	 * @throws	Exception
	 */
	public String selectTheme(CommonVO searchVO) throws Exception {
		return selectOne("common.selectTheme", searchVO);
	}
	
	public List<MenuVO> selectProcessList(CommonVO searchVO) throws Exception {
		return selectList("common.selectProcessList", searchVO);
	}
	
	public int updateProcessData(MenuVO dataVO) throws Exception{
		return update("common.updateProcessData", dataVO);	
	}
	
	public int deleteProcessData(MenuVO dataVO) throws Exception{
		return delete("common.deleteProcessData", dataVO);	
	}
	
	public int insertProcessData(MenuVO dataVO) throws Exception{
		return insert("common.insertProcessData", dataVO);	
	}
	
	public List<CommonVO> selectDbList() throws Exception{
		return selectList("common.selectDbList");
	}
	
	public List<CommonVO> selectDbResetList(EgovMap emap) throws Exception{
		return selectList("common.selectDbResetList", emap);
	}
	
	public String selectDbId(EgovMap emap) throws Exception{
		return selectOne("common.selectDbId", emap);
	}
}
