/*************************************************************************
* CLASS 명	: CompUserMngDAO
* 작 업 자	: 박정현
* 작 업 일	: 2018-07-02
* 기	능	: 사용자관리 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-07-02
**************************************************************************/
package kr.ispark.system.system.comp.compUserMng.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.system.system.comp.compUserMng.service.CompUserMngVO;

@Repository
public class CompUserMngDAO extends EgovComAbstractDAO {
	/**
	 * 사용자관리 목록 조회
	 * @param	CompUserMngVO searchVO
	 * @return	List<CompUserMngVO>
	 * @throws	Exception
	 */
	public List<CompUserMngVO> selectList(CompUserMngVO searchVO) throws Exception {
		return selectList("system.comp.compUserMng.selectList", searchVO);
	}

	/**
	 * 사용자관리 상세 조회
	 * @param	CompUserMngVO searchVO
	 * @return	CompUserMngVO
	 * @throws	Exception
	 */
	public CompUserMngVO selectDetail(CompUserMngVO searchVO) throws Exception {
		return (CompUserMngVO)selectOne("system.comp.compUserMng.selectDetail", searchVO);
	}

	/**
	 * 사용자관리 삭제
	 * @param	CompUserMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteCompUserMng(CompUserMngVO dataVO) throws Exception {
		int resultCnt = update("system.comp.compUserMng.deleteCompUserMng", dataVO);
		// 일반사용자 권한 삭제
		delete("system.comp.compUserMng.deleteAuth", dataVO);
		return resultCnt;
	}

	/**
	 * 사용자관리 저장
	 * @param	CompUserMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(CompUserMngVO dataVO) throws Exception {
		int resultCnt = insert("system.comp.compUserMng.insertData", dataVO);
		// 일반사용자 권한 부여
		insert("system.comp.compUserMng.insertAuth", dataVO);
		return resultCnt;
	}

	/**
	 * 사용자관리 수정
	 * @param	CompUserMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(CompUserMngVO dataVO) throws Exception {
		return update("system.comp.compUserMng.updateData", dataVO);
	}

	/**
	 * 패스워드 초기화
	 * @param	CompUserMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updatePasswordReset(CompUserMngVO dataVO) throws Exception {
		return update("system.comp.compUserMng.updatePasswordReset", dataVO);
	}

	/**
	 * 패스워드 변경
	 * @param	CompUserMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updatePassword(CompUserMngVO dataVO) throws Exception {
		return update("system.comp.compUserMng.updatePassword", dataVO);
	}

	/**
	 * ID 중복 체크
	 * @param	CompUserMngVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectIdCnt(CompUserMngVO searchVO) throws Exception{
		return selectOne("system.comp.compUserMng.selectIdCnt", searchVO);
	}

	/**
	 * 사용자 언어 설정 수정
	 * @param	UserVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateUserLang(UserVO dataVO) throws Exception{
		return update("system.comp.compUserMng.updateUserLang", dataVO);
	}

	/**
	 * 사용자관리 목록 조회 (엑셀 양식용)
	 * @param	CompUserMngVO searchVO
	 * @return	List<CompUserMngVO>
	 * @throws	Exception
	 */
	public List<CompUserMngVO> selectListForExcelForm(CompUserMngVO searchVO) throws Exception {
		return selectList("system.comp.compUserMng.selectListForExcelForm", searchVO);
	}

	/**
	 * 사용자관리 저장 (Excel)
	 * @param	CompUserMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertExcelData(CompUserMngVO dataVO) throws Exception {
		int resultCnt = insert("system.comp.compUserMng.insertExcelData", dataVO);
		// 일반사용자 권한 부여
		insert("system.comp.compUserMng.insertAuth", dataVO);
		return resultCnt;
	}

	/**
	 * 사용자관리 수정 (Excel)
	 * @param	CompUserMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateExcelData(CompUserMngVO dataVO) throws Exception {
		return update("system.comp.compUserMng.updateExcelData", dataVO);
	}
}
