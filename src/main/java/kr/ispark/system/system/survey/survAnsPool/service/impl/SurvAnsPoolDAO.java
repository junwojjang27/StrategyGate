/*************************************************************************
* CLASS 명	: SurvAnsPoolDAO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-02
* 기	능	: 설문답변pool DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-02
**************************************************************************/
package kr.ispark.system.system.survey.survAnsPool.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.survey.survAnsPool.service.SurvAnsPoolVO;

@Repository
public class SurvAnsPoolDAO extends EgovComAbstractDAO {
	/**
	 * 설문답변pool 목록 조회
	 * @param	SurvAnsPoolVO searchVO
	 * @return	List<SurvAnsPoolVO>
	 * @throws	Exception
	 */
	public List<SurvAnsPoolVO> selectList(SurvAnsPoolVO searchVO) throws Exception {
		return selectList("system.survey.survAnsPool.selectList", searchVO);
	}

	/**
	 * 설문답변pool 상세 조회
	 * @param	SurvAnsPoolVO searchVO
	 * @return	SurvAnsPoolVO
	 * @throws	Exception
	 */
	public SurvAnsPoolVO selectDetail(SurvAnsPoolVO searchVO) throws Exception {
		return (SurvAnsPoolVO)selectOne("system.survey.survAnsPool.selectDetail", searchVO);
	}

	/**
	 * 설문답변pool상세 목록 조회
	 */
	public List<SurvAnsPoolVO> selectItemList(SurvAnsPoolVO searchVO) throws Exception {
		return selectList("system.survey.survAnsPool.selectItemList", searchVO);
	}

	/**
	 * 설문답변pool 팝업 목록 조회
	 */
	public List<SurvAnsPoolVO> selectPopList(SurvAnsPoolVO searchVO) throws Exception {
		return selectList("system.survey.survAnsPool.selectPopList", searchVO);
	}

	/**
	 * 설문답변pool 일괄저장
	 * @param	SurvAnsPoolVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveAllSurvAnsPool(SurvAnsPoolVO searchVO) throws Exception {
		return update("system.survey.survAnsPool.saveAllSurvAnsPool", searchVO);
	}

	/**
	 * 설문답변pool 삭제
	 * @param	SurvAnsPoolVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteSurvAnsPool(SurvAnsPoolVO searchVO) throws Exception {
		return update("system.survey.survAnsPool.deleteSurvAnsPool", searchVO);
	}

	/**
	 * 설문답변pool 저장
	 * @param	SurvAnsPoolVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(SurvAnsPoolVO searchVO) throws Exception {
		return insert("system.survey.survAnsPool.insertData", searchVO);
	}

	/**
	 * 설문답변pool 수정
	 * @param	SurvAnsPoolVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(SurvAnsPoolVO searchVO) throws Exception {
		return update("system.survey.survAnsPool.updateData", searchVO);
	}

	/**
	 * 설문답변pool상세 삭제
	 */
	public int deleteItemDetail(SurvAnsPoolVO searchVO) throws Exception {
		return update("system.survey.survAnsPool.deleteItemDetail", searchVO);
	}

	/**
	 * 설문답변pool상세 저장
	 */
	public int insertItemDetail(SurvAnsPoolVO searchVO) throws Exception {
		return insert("system.survey.survAnsPool.insertItemDetail", searchVO);
	}
	
	/**
	 * 답변항목대표여부 카운트
	 */
	public SurvAnsPoolVO mainItemCnt(SurvAnsPoolVO searchVO) throws Exception {
		return (SurvAnsPoolVO)selectOne("system.survey.survAnsPool.mainItemCnt", searchVO);
	}
}

