/*************************************************************************
* CLASS 명	: SurvActionDAO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-16
* 기	능	: 설문실시 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-16
**************************************************************************/
package kr.ispark.system.system.survey.survAction.service.impl;


import java.util.List;

import kr.ispark.common.system.service.ScDeptVO;
import kr.ispark.system.system.survey.survAction.service.SurvActionVO;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository
public class SurvActionDAO extends EgovComAbstractDAO {
	/**
	 * 설문실시 목록 조회
	 * @param	SurvActionVO searchVO
	 * @return	List<SurvActionVO>
	 * @throws	Exception
	 */
	public List<SurvActionVO> selectList(SurvActionVO searchVO) throws Exception {
		return selectList("system.survey.survAction.selectList", searchVO);
	}

	/**
	 * 설문실시 상세 조회
	 * @param	SurvActionVO searchVO
	 * @return	SurvActionVO
	 * @throws	Exception
	 */
	public SurvActionVO selectDetail(SurvActionVO searchVO) throws Exception {
		return (SurvActionVO)selectOne("system.survey.survAction.selectDetail", searchVO);
	}

	/**
	 * 설문결과 목록 조회
	 */
	public List<SurvActionVO> selectResultList(SurvActionVO searchVO) throws Exception {
		return selectList("system.survey.survAction.selectResultList", searchVO);
	}

	/**
	 * 성과조직 본부 목록 조회
	 */
	public List<ScDeptVO> selectBonbuList(SurvActionVO searchVO) throws Exception {
		return selectList("system.survey.survAction.selectBonbuList", searchVO);
	}

	/**
	 * 설문답변결과 삭제
	 */
	public int deleteSurResult(SurvActionVO searchVO) throws Exception {
		return update("system.survey.survAction.deleteSurResult", searchVO);
	}

	/**
	 * 설문차수별대상자완료여부 삭제
	 */
	public int deleteSurTargetUserState(SurvActionVO searchVO) throws Exception {
		return update("system.survey.survAction.deleteSurTargetUserState", searchVO);
	}

	/**
	 * 설문실시 저장
	 * @param	SurvActionVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(SurvActionVO searchVO) throws Exception {
		return insert("system.survey.survAction.insertData", searchVO);
	}

	/**
	 * 설문차수별대상자완료여부 저장
	 */
	public int insertSurTargetUserState(SurvActionVO searchVO) throws Exception {
		return insert("system.survey.survAction.insertSurTargetUserState", searchVO);
	}
}

