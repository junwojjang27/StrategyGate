/*************************************************************************
* CLASS 명	: MenuMngDAO
* 작 업 자	: joey
* 작 업 일	: 2018-1-7
* 기	능	: 메뉴관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018-1-7
**************************************************************************/
package kr.ispark.system.system.menu.menuMng.service.impl;


import java.util.List;

import kr.ispark.system.system.menu.menuMng.service.MenuMngVO;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository
public class MenuMngDAO extends EgovComAbstractDAO {
	/**
	 * 메뉴관리 조회
	 */
	public List<MenuMngVO> selectList(MenuMngVO searchVO) throws Exception {
		return selectList("system.menu.menuMng.selectList", searchVO);
	}
	
	/**
	 * 메뉴관리 조회
	 */
	public List<MenuMngVO> selectRoleList(MenuMngVO searchVO) throws Exception {
		return selectList("system.menu.menuMng.selectRoleList", searchVO);
	}
	
	/**
	 * 메뉴관리 조회
	 */
	public List<MenuMngVO> selectReSortList(MenuMngVO searchVO) throws Exception {
		return selectList("system.menu.menuMng.selectReSortList", searchVO);
	}
	
	/**
	 * 메뉴관리 조회
	 */
	public List<MenuMngVO> selectFullNameList(MenuMngVO searchVO) throws Exception {
		return selectList("system.menu.menuMng.selectFullNameList", searchVO);
	}
	
	/**
	 * 메뉴권한 조회
	 */
	public List<MenuMngVO> selectAuthPgmList(MenuMngVO searchVO) throws Exception {
		return selectList("system.menu.menuMng.selectAuthPgmList", searchVO);
	}
	
	/**
	 * 표준언어  조회
	 */
	public List<MenuMngVO> selectLangList(MenuMngVO searchVO) throws Exception {
		return selectList("system.menu.menuMng.selectLangList", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int deletePgmNmData(MenuMngVO searchVO) {
		return delete("system.menu.menuMng.deletePgmNmData", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int deletePgmAuthData(MenuMngVO searchVO) {
		return delete("system.menu.menuMng.deletePgmAuthData", searchVO);
	}
	
	/**
	 * 메뉴관리 저장
	 */
	public int insertData(MenuMngVO searchVO) {
		return update("system.menu.menuMng.insertData", searchVO);
	}
	
	/**
	 * 메뉴관리 저장
	 */
	public int insertPgmNmData(MenuMngVO searchVO) {
		return update("system.menu.menuMng.insertPgmNmData", searchVO);
	}
	
	/**
	 * 메뉴관리 저장
	 */
	public int insertPgmAuthData(MenuMngVO searchVO) {
		return update("system.menu.menuMng.insertPgmAuthData", searchVO);
	}

	/**
	 * 메뉴관리 수정
	 */
	public int updateData(MenuMngVO searchVO) {
		return update("system.menu.menuMng.updateData", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int updateReSortOrder(MenuMngVO searchVO) {
		return update("system.menu.menuMng.updateReSortOrder", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int updateFullName(MenuMngVO searchVO) {
		return update("system.menu.menuMng.updateFullName", searchVO);
	}
	
	/**
	 * 메뉴관리 상세 조회
	 */
	public MenuMngVO selectDetail(MenuMngVO searchVO) throws Exception {
		return (MenuMngVO)selectOne("system.menu.menuMng.selectDetail", searchVO);
	}
	
	/**
	 * 메뉴 도움말 저장
	 */
	public int updateGuideComment(MenuMngVO searchVO) {
		return update("system.menu.menuMng.updateGuideComment", searchVO);
	}
}

