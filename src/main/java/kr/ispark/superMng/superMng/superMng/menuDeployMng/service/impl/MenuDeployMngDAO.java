/*************************************************************************
* CLASS 명	: MenuDeployMngDAO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-07-06
* 기	능	: 메뉴배포관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-07-06
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.menuDeployMng.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.superMng.superMng.superMng.menuDeployMng.service.MenuDeployMngVO;

@Repository
public class MenuDeployMngDAO extends EgovComAbstractDAO {
	
	/**
	 * 메뉴배포관리 목록 조회
	 * @param	MenuDeployMngVO searchVO
	 * @return	List<MenuDeployMngVO>
	 * @throws	Exception
	 */
	public List<MenuDeployMngVO> selectDeployList(MenuDeployMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.menuDeployMng.selectDeployList", searchVO);
	}
	/**
	 * 메뉴배포관리 목록 조회
	 * @param	MenuDeployMngVO searchVO
	 * @return	List<MenuDeployMngVO>
	 * @throws	Exception
	 */
	public List<MenuDeployMngVO> selectDeployCompList(MenuDeployMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.menuDeployMng.selectDeployCompList", searchVO);
	}
	
	/**
	 * 메뉴배포관리 수정
	 */
	public int updateDeployData(MenuDeployMngVO searchVO) {
		return update("superMng.superMng.menuDeployMng.updateDeployData", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int deleteDeployedMenu(MenuDeployMngVO searchVO) {
		return delete("superMng.superMng.menuDeployMng.deleteDeployedMenu", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int deleteDeployedMenuNm(MenuDeployMngVO searchVO) {
		return delete("superMng.superMng.menuDeployMng.deleteDeployedMenuNm", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int deleteDeployedMenuAuth(MenuDeployMngVO searchVO) {
		return delete("superMng.superMng.menuDeployMng.deleteDeployedMenuAuth", searchVO);
	}
	
	/**
	 * 메뉴관리 저장
	 */
	public int insertDeployMenu(MenuDeployMngVO searchVO) {
		return update("superMng.superMng.menuDeployMng.insertDeployMenu", searchVO);
	}
	
	/**
	 * 메뉴관리 저장
	 */
	public int insertDeployMenuNm(MenuDeployMngVO searchVO) {
		return update("superMng.superMng.menuDeployMng.insertDeployMenuNm", searchVO);
	}
	
	/**
	 * 메뉴관리 저장
	 */
	public int insertDeployMenuAuth(MenuDeployMngVO searchVO) {
		return update("superMng.superMng.menuDeployMng.insertDeployMenuAuth", searchVO);
	}
	
	/*메뉴등록시작 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
	
	/**
	 * 메뉴관리 조회
	 */
	public List<MenuDeployMngVO> selectList(MenuDeployMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.menuDeployMng.selectList", searchVO);
	}
	
	/**
	 * 메뉴관리 조회
	 */
	public List<MenuDeployMngVO> selectRoleList(MenuDeployMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.menuDeployMng.selectRoleList", searchVO);
	}
	
	/**
	 * 메뉴관리 조회
	 */
	public List<MenuDeployMngVO> selectReSortList(MenuDeployMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.menuDeployMng.selectReSortList", searchVO);
	}
	
	/**
	 * 메뉴관리 조회
	 */
	public List<MenuDeployMngVO> selectFullNameList(MenuDeployMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.menuDeployMng.selectFullNameList", searchVO);
	}
	
	/**
	 * 메뉴관리 조회
	 */
	public List<MenuDeployMngVO> selectCompReSortList(MenuDeployMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.menuDeployMng.selectCompReSortList", searchVO);
	}
	
	/**
	 * 메뉴관리 조회
	 */
	public List<MenuDeployMngVO> selectCompFullNameList(MenuDeployMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.menuDeployMng.selectCompFullNameList", searchVO);
	}
	
	/**
	 * 메뉴권한 조회
	 */
	public List<MenuDeployMngVO> selectAuthPgmList(MenuDeployMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.menuDeployMng.selectAuthPgmList", searchVO);
	}
	
	/**
	 * 표준언어  조회
	 */
	public List<MenuDeployMngVO> selectLangList(MenuDeployMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.menuDeployMng.selectLangList", searchVO);
	}
	
	/**
	 * 표준언어  조회
	 */
	public List<MenuDeployMngVO> selectCompLangList(MenuDeployMngVO searchVO) throws Exception {
		return selectList("superMng.superMng.menuDeployMng.selectCompLangList", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int deletePgmNmData(MenuDeployMngVO searchVO) {
		return delete("superMng.superMng.menuDeployMng.deletePgmNmData", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int deletePgmAuthData(MenuDeployMngVO searchVO) {
		return delete("superMng.superMng.menuDeployMng.deletePgmAuthData", searchVO);
	}
	
	/**
	 * 메뉴관리 저장
	 */
	public int insertData(MenuDeployMngVO searchVO) {
		return update("superMng.superMng.menuDeployMng.insertData", searchVO);
	}
	
	/**
	 * 메뉴관리 저장
	 */
	public int insertPgmNmData(MenuDeployMngVO searchVO) {
		return update("superMng.superMng.menuDeployMng.insertPgmNmData", searchVO);
	}
	
	/**
	 * 메뉴관리 저장
	 */
	public int insertPgmAuthData(MenuDeployMngVO searchVO) {
		return update("superMng.superMng.menuDeployMng.insertPgmAuthData", searchVO);
	}

	/**
	 * 메뉴관리 수정
	 */
	public int updateData(MenuDeployMngVO searchVO) {
		return update("superMng.superMng.menuDeployMng.updateData", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int updateReSortOrder(MenuDeployMngVO searchVO) {
		return update("superMng.superMng.menuDeployMng.updateReSortOrder", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int updateFullName(MenuDeployMngVO searchVO) {
		return update("superMng.superMng.menuDeployMng.updateFullName", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int updateCompReSortOrder(MenuDeployMngVO searchVO) {
		return update("superMng.superMng.menuDeployMng.updateCompReSortOrder", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int updateCompFullName(MenuDeployMngVO searchVO) {
		return update("superMng.superMng.menuDeployMng.updateCompFullName", searchVO);
	}
}

