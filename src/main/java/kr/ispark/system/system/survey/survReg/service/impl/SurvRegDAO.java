/*************************************************************************
* CLASS 명	: SurvRegDAO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-05
* 기	능	: 설문등록 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-05
**************************************************************************/
package kr.ispark.system.system.survey.survReg.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.survey.survReg.service.SurvRegVO;

@Repository
public class SurvRegDAO extends EgovComAbstractDAO {
	/**
	 * 설문등록 목록 조회
	 * @param	SurvRegVO searchVO
	 * @return	List<SurvRegVO>
	 * @throws	Exception
	 */
	public List<SurvRegVO> selectList(SurvRegVO searchVO) throws Exception {
		return selectList("system.survey.survReg.selectList", searchVO);
	}

	/**
	 * 설문등록 상세 조회
	 * @param	SurvRegVO searchVO
	 * @return	SurvRegVO
	 * @throws	Exception
	 */
	public SurvRegVO selectDetail(SurvRegVO searchVO) throws Exception {
		return (SurvRegVO)selectOne("system.survey.survReg.selectDetail", searchVO);
	}

	/**
	 * 설문등록 삭제
	 * @param	SurvRegVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteSurvReg(SurvRegVO searchVO) throws Exception {
		return update("system.survey.survReg.deleteSurvReg", searchVO);
	}

	/**
	 * 설문등록 저장
	 * @param	SurvRegVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(SurvRegVO searchVO) throws Exception {
		return insert("system.survey.survReg.insertData", searchVO);
	}

	/**
	 * 설문등록 수정
	 * @param	SurvRegVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(SurvRegVO searchVO) throws Exception {
		return update("system.survey.survReg.updateData", searchVO);
	}

	/**
	 * 설문등록 설문복사
	 */
	public int insertSurveyCopy(SurvRegVO searchVO) throws Exception {
		return insert("system.survey.survReg.insertSurveyCopy", searchVO);
	}
	/**
	 * 설문등록 설문질문복사
	 */
	public int insertQuesCopy(SurvRegVO searchVO) throws Exception {
		return insert("system.survey.survReg.insertQuesCopy", searchVO);
	}
	/**
	 * 설문등록 설문답변복사
	 */
	public int insertAnsCopy(SurvRegVO searchVO) throws Exception {
		return insert("system.survey.survReg.insertAnsCopy", searchVO);
	}
	/**
	 * 설문등록 설문질문그룹복사
	 */
	public int insertGrpCopy(SurvRegVO searchVO) throws Exception {
		return insert("system.survey.survReg.insertGrpCopy", searchVO);
	}
}

