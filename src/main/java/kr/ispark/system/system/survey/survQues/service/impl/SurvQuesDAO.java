/*************************************************************************
* CLASS 명	: SurvQuesDAO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-10
* 기	능	: 설문질문등록 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-10
**************************************************************************/
package kr.ispark.system.system.survey.survQues.service.impl;


import java.util.List;

import kr.ispark.system.system.survey.survQues.service.SurvQuesVO;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository
public class SurvQuesDAO extends EgovComAbstractDAO {
	/**
	 * 설문질문등록 목록 조회
	 * @param	SurvQuesVO searchVO
	 * @return	List<SurvQuesVO>
	 * @throws	Exception
	 */
	public List<SurvQuesVO> selectList(SurvQuesVO searchVO) throws Exception {
		return selectList("system.survey.survQues.selectList", searchVO);
	}

	/**
	 * 설문질문등록 상세 조회
	 * @param	SurvQuesVO searchVO
	 * @return	SurvQuesVO
	 * @throws	Exception
	 */
	public SurvQuesVO selectDetail(SurvQuesVO searchVO) throws Exception {
		return (SurvQuesVO)selectOne("system.survey.survQues.selectDetail", searchVO);
	}

	/**
	 * 설문질문답변 목록조회
	 */
	public List<SurvQuesVO> selectItemList(SurvQuesVO searchVO) throws Exception {
		return selectList("system.survey.survQues.selectItemList", searchVO);
	}

	/**
	 * 설문질문등록 삭제
	 * @param	SurvQuesVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteSurvQues(SurvQuesVO searchVO) throws Exception {
		return update("system.survey.survQues.deleteSurvQues", searchVO);
	}

	/**
	 * 설문질문등록 저장
	 * @param	SurvQuesVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(SurvQuesVO searchVO) throws Exception {
		return insert("system.survey.survQues.insertData", searchVO);
	}

	/**
	 * 설문질문등록 수정
	 * @param	SurvQuesVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(SurvQuesVO searchVO) throws Exception {
		return update("system.survey.survQues.updateData", searchVO);
	}

	/**
	 * 설문질문답변 삭제
	 */
	public int deleteSurveyItem(SurvQuesVO searchVO) throws Exception {
		return update("system.survey.survQues.deleteSurveyItem", searchVO);
	}

	/**
	 * 설문질문답변 저장
	 */
	public int insertSurveyItem(SurvQuesVO searchVO) throws Exception {
		return insert("system.survey.survQues.insertSurveyItem", searchVO);
	}
}

