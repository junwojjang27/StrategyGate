/*************************************************************************
* CLASS 명	: SurvTgtUsrDAO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-12
* 기	능	: 설문대상자 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-12
**************************************************************************/
package kr.ispark.system.system.survey.survTgtUsr.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.survey.survTgtUsr.service.SurvTgtUsrVO;

@Repository
public class SurvTgtUsrDAO extends EgovComAbstractDAO {
	/**
	 * 설문대상자 목록 조회
	 * @param	SurvTgtUsrVO searchVO
	 * @return	List<SurvTgtUsrVO>
	 * @throws	Exception
	 */
	public List<SurvTgtUsrVO> selectList(SurvTgtUsrVO searchVO) throws Exception {
		return selectList("system.survey.survTgtUsr.selectList", searchVO);
	}

	/**
	 * 설문대상자 삭제
	 * @param	SurvTgtUsrVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteSurvTgtUsr(SurvTgtUsrVO searchVO) throws Exception {
		return update("system.survey.survTgtUsr.deleteSurvTgtUsr", searchVO);
	}
	/**
	 * 설문대상자 전체삭제
	 */
	public int deleteAllSurvTgtUsr(SurvTgtUsrVO searchVO) throws Exception {
		return update("system.survey.survTgtUsr.deleteAllSurvTgtUsr", searchVO);
	}

	/**
	 * 설문대상자 저장
	 * @param	SurvTgtUsrVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(SurvTgtUsrVO searchVO) throws Exception {
		return insert("system.survey.survTgtUsr.insertData", searchVO);
	}

	/**
	 * 전직원삭제
	 */
	public int deleteAllUser(SurvTgtUsrVO searchVO) throws Exception {
		return update("system.survey.survTgtUsr.deleteAllUser", searchVO);
	}

	/**
	 * 전직원추가
	 */
	public int insertAllUser(SurvTgtUsrVO searchVO) throws Exception {
		return insert("system.survey.survTgtUsr.insertAllUser", searchVO);
	}
}

