/*************************************************************************
* CLASS 명	: AuthGrpDAO
* 작 업 자	: joey
* 작 업 일	: 2018-1-12
* 기	능	: 그룹별권한 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018-1-12
**************************************************************************/
package kr.ispark.system.system.menu.authGrp.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.menu.authGrp.service.AuthGrpVO;

@Repository
public class AuthGrpDAO extends EgovComAbstractDAO {
	/**
	 * 그룹별권한 조회
	 */
	public List<AuthGrpVO> selectList(AuthGrpVO searchVO) throws Exception {
		return selectList("system.menu.authGrp.selectList", searchVO);
	}
	
	/**
	 * 그룹별권한 조회
	 */
	public List<AuthGrpVO> selectMenuList(AuthGrpVO searchVO) throws Exception {
		return selectList("system.menu.authGrp.selectMenuList", searchVO);
	}
	/**
	 * 그룹별권한 조회
	 */
	public List<AuthGrpVO> selectMenuSelectedList(AuthGrpVO searchVO) throws Exception {
		return selectList("system.menu.authGrp.selectMenuSelectedList", searchVO);
	}
	
	/**
	 * 그룹별권한 상세 조회
	 */
	public AuthGrpVO selectDetail(AuthGrpVO searchVO) throws Exception {
		return (AuthGrpVO)selectOne("system.menu.authGrp.selectDetail", searchVO);
	}
	
	/**
	 * 그룹별권한 정렬순서저장
	 */
	public int updateSortOrder(AuthGrpVO searchVO) {
		return update("system.menu.authGrp.updateSortOrder", searchVO);
	}

	/**
	 * 그룹별권한삭제
	 */
	public int deleteAuthGrp(AuthGrpVO searchVO) {
		return update("system.menu.authGrp.deleteAuthGrp", searchVO);
	}
	
	/**
	 * 그룹별권한삭제
	 */
	public int deleteData(AuthGrpVO searchVO) {
		return update("system.menu.authGrp.deleteData", searchVO);
	}
	
	/**
	 * 그룹별권한 저장
	 */
	public int insertData(AuthGrpVO searchVO) {
		return update("system.menu.authGrp.insertData", searchVO);
	}

	/**
	 * 그룹별권한 수정
	 */
	public int updateData(AuthGrpVO searchVO) {
		return update("system.menu.authGrp.updateData", searchVO);
	}
}

