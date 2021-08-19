/*************************************************************************
* CLASS 명	: UserAuthGrpDAO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-1-19
* 기	능	: 사용자권한그룹설정 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-1-19
**************************************************************************/
package kr.ispark.system.system.menu.userAuthGrp.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.menu.userAuthGrp.service.UserAuthGrpVO;

@Repository
public class UserAuthGrpDAO extends EgovComAbstractDAO {
	/**
	 * 사용자권한그룹설정 조회
	 */
	public List<UserAuthGrpVO> selectList(UserAuthGrpVO searchVO) throws Exception {
		return selectList("system.menu.userAuthGrp.selectList", searchVO);
	}
	
	/**
	 * 사용자권한그룹설정 조회
	 */
	public List<UserAuthGrpVO> selectDeptList(UserAuthGrpVO searchVO) throws Exception {
		return selectList("system.menu.userAuthGrp.selectDeptList", searchVO);
	}
	
	/**
	 * 사용자권한그룹설정 조회
	 */
	public List<UserAuthGrpVO> selectUserList(UserAuthGrpVO searchVO) throws Exception {
		return selectList("system.menu.userAuthGrp.selectUserList", searchVO);
	}
	
	/**
	 * 사용자권한그룹설정 조회
	 */
	public List<UserAuthGrpVO> selectSelectedUserList(UserAuthGrpVO searchVO) throws Exception {
		return selectList("system.menu.userAuthGrp.selectSelectedUserList", searchVO);
	}
	
	/**
	 * 사용자권한그룹설정 상세 조회
	 */
	public UserAuthGrpVO selectDetail(UserAuthGrpVO searchVO) throws Exception {
		return (UserAuthGrpVO)selectOne("system.menu.userAuthGrp.selectDetail", searchVO);
	}
	
	/**
	 * 사용자권한그룹설정 정렬순서저장
	 */
	public int updateSortOrder(UserAuthGrpVO searchVO) {
		return update("system.menu.userAuthGrp.updateSortOrder", searchVO);
	}

	/**
	 * 사용자권한그룹설정삭제
	 */
	public int deleteUserAuthGrp(UserAuthGrpVO searchVO) {
		return update("system.menu.userAuthGrp.deleteUserAuthGrp", searchVO);
	}
	
	/**
	 * 사용자권한그룹설정 저장
	 */
	public int insertData(UserAuthGrpVO searchVO) {
		return update("system.menu.userAuthGrp.insertData", searchVO);
	}

	/**
	 * 사용자권한그룹설정 수정
	 */
	public int updateData(UserAuthGrpVO searchVO) {
		return update("system.menu.userAuthGrp.updateData", searchVO);
	}
}

