/*************************************************************************
* CLASS 명	: SurvQuesPoolDAO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-04
* 기	능	: 설문질문pool DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-04
**************************************************************************/
package kr.ispark.system.system.survey.survQuesPool.service.impl;


import java.util.List;

import kr.ispark.system.system.survey.survQuesPool.service.SurvQuesPoolVO;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository
public class SurvQuesPoolDAO extends EgovComAbstractDAO {
	/**
	 * 설문질문pool 목록 조회
	 * @param	SurvQuesPoolVO searchVO
	 * @return	List<SurvQuesPoolVO>
	 * @throws	Exception
	 */
	public List<SurvQuesPoolVO> selectList(SurvQuesPoolVO searchVO) throws Exception {
		return selectList("system.survey.survQuesPool.selectList", searchVO);
	}

	/**
	 * 설문질문pool 상세 조회
	 * @param	SurvQuesPoolVO searchVO
	 * @return	SurvQuesPoolVO
	 * @throws	Exception
	 */
	public SurvQuesPoolVO selectDetail(SurvQuesPoolVO searchVO) throws Exception {
		return (SurvQuesPoolVO)selectOne("system.survey.survQuesPool.selectDetail", searchVO);
	}

	/**
	 * 설문질문pool답변항목 목록 조회
	 */
	public List<SurvQuesPoolVO> selectItemList(SurvQuesPoolVO searchVO) throws Exception {
		return selectList("system.survey.survQuesPool.selectItemList", searchVO);
	}

	/**
	 * 설문질문pool 정렬순서저장
	 * @param	SurvQuesPoolVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(SurvQuesPoolVO searchVO) throws Exception {
		return update("system.survey.survQuesPool.updateSortOrder", searchVO);
	}

	/**
	 * 설문질문pool 삭제
	 * @param	SurvQuesPoolVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteSurvQuesPool(SurvQuesPoolVO searchVO) throws Exception {
		return update("system.survey.survQuesPool.deleteSurvQuesPool", searchVO);
	}

	/**
	 * 설문질문pool 저장
	 * @param	SurvQuesPoolVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(SurvQuesPoolVO searchVO) throws Exception {
		return insert("system.survey.survQuesPool.insertData", searchVO);
	}

	/**
	 * 설문질문pool 수정
	 * @param	SurvQuesPoolVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(SurvQuesPoolVO searchVO) throws Exception {
		return update("system.survey.survQuesPool.updateData", searchVO);
	}

	/**
	 * 설문질문pool답변항목 삭제
	 */
	public int deleteQuesPoolItem(SurvQuesPoolVO searchVO) throws Exception {
		return update("system.survey.survQuesPool.deleteQuesPoolItem", searchVO);
	}

	/**
	 * 설문질문pool답변항목 저장
	 */
	public int insertQuesPoolItem(SurvQuesPoolVO searchVO) throws Exception {
		return insert("system.survey.survQuesPool.insertQuesPoolItem", searchVO);
	}
}

