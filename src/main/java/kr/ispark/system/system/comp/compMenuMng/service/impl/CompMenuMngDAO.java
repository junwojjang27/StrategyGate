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
package kr.ispark.system.system.comp.compMenuMng.service.impl;


import java.util.List;

import kr.ispark.system.system.comp.compMenuMng.service.CompMenuMngVO;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository
public class CompMenuMngDAO extends EgovComAbstractDAO {
	/**
	 * 메뉴관리 조회
	 */
	public List<CompMenuMngVO> selectList(CompMenuMngVO searchVO) throws Exception {
		return selectList("system.comp.compMenuMng.selectList", searchVO);
	}
	
	/**
	 * 메뉴관리 조회
	 */
	public List<CompMenuMngVO> selectRoleList(CompMenuMngVO searchVO) throws Exception {
		return selectList("system.comp.compMenuMng.selectRoleList", searchVO);
	}
	
	/**
	 * 메뉴관리 조회
	 */
	public List<CompMenuMngVO> selectReSortList(CompMenuMngVO searchVO) throws Exception {
		return selectList("system.comp.compMenuMng.selectReSortList", searchVO);
	}
	
	/**
	 * 메뉴관리 조회
	 */
	public List<CompMenuMngVO> selectFullNameList(CompMenuMngVO searchVO) throws Exception {
		return selectList("system.comp.compMenuMng.selectFullNameList", searchVO);
	}
	
	/**
	 * 메뉴권한 조회
	 */
	public List<CompMenuMngVO> selectAuthPgmList(CompMenuMngVO searchVO) throws Exception {
		return selectList("system.comp.compMenuMng.selectAuthPgmList", searchVO);
	}
	
	/**
	 * 표준언어  조회
	 */
	public List<CompMenuMngVO> selectLangList(CompMenuMngVO searchVO) throws Exception {
		return selectList("system.comp.compMenuMng.selectLangList", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int deletePgmNmData(CompMenuMngVO searchVO) {
		return delete("system.comp.compMenuMng.deletePgmNmData", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int deletePgmAuthData(CompMenuMngVO searchVO) {
		return delete("system.comp.compMenuMng.deletePgmAuthData", searchVO);
	}
	
	/**
	 * 메뉴관리 저장
	 */
	public int insertData(CompMenuMngVO searchVO) {
		return update("system.comp.compMenuMng.insertData", searchVO);
	}
	
	/**
	 * 메뉴관리 저장
	 */
	public int insertPgmNmData(CompMenuMngVO searchVO) {
		return update("system.comp.compMenuMng.insertPgmNmData", searchVO);
	}
	
	/**
	 * 메뉴관리 저장
	 */
	public int insertPgmAuthData(CompMenuMngVO searchVO) {
		return update("system.comp.compMenuMng.insertPgmAuthData", searchVO);
	}

	/**
	 * 메뉴관리 수정
	 */
	public int updateData(CompMenuMngVO searchVO) {
		return update("system.comp.compMenuMng.updateData", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int updateReSortOrder(CompMenuMngVO searchVO) {
		return update("system.comp.compMenuMng.updateReSortOrder", searchVO);
	}
	
	/**
	 * 메뉴관리 수정
	 */
	public int updateFullName(CompMenuMngVO searchVO) {
		return update("system.comp.compMenuMng.updateFullName", searchVO);
	}
	
	/**
	 * 메뉴관리 상세 조회
	 */
	public CompMenuMngVO selectDetail(CompMenuMngVO searchVO) throws Exception {
		return (CompMenuMngVO)selectOne("system.comp.compMenuMng.selectDetail", searchVO);
	}
	
	/**
	 * 메뉴 도움말 저장
	 */
	public int updateGuideComment(CompMenuMngVO searchVO) {
		return update("system.comp.compMenuMng.updateGuideComment", searchVO);
	}
}

