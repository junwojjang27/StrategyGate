/*************************************************************************
* CLASS 명	: SurvQuesGrpDAO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-11
* 기	능	: 설문질문그룹 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-11
**************************************************************************/
package kr.ispark.system.system.survey.survQuesGrp.service.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import kr.ispark.system.system.survey.survQuesGrp.service.SurvQuesGrpVO;

@Repository
public class SurvQuesGrpDAO extends EgovComAbstractDAO {
	/**
	 * 설문질문그룹 목록 조회
	 * @param	SurvQuesGrpVO searchVO
	 * @return	List<SurvQuesGrpVO>
	 * @throws	Exception
	 */
	public List<SurvQuesGrpVO> selectList(SurvQuesGrpVO searchVO) throws Exception {
		return selectList("system.survey.survQuesGrp.selectList", searchVO);
	}

	/**
	 * 설문질문그룹별 설문매핑 목록 조회
	 */
	public List<SurvQuesGrpVO> selectListForMap(SurvQuesGrpVO searchVO) throws Exception {
		return selectList("system.survey.survQuesGrp.selectListForMap", searchVO);
	}

	/**
	 * 설문질문그룹 삭제
	 * @param	SurvQuesGrpVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteSurvQuesGrp(SurvQuesGrpVO searchVO) throws Exception {
		return update("system.survey.survQuesGrp.deleteSurvQuesGrp", searchVO);
	}

	/**
	 * 설문차수별질문 질문그룹코드 삭제
	 */
	public int deleteSurvQues(SurvQuesGrpVO searchVO) throws Exception {
		return update("system.survey.survQuesGrp.deleteSurvQues", searchVO);
	}

	/**
	 * 설문질문그룹 저장
	 * @param	SurvQuesGrpVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int insertData(SurvQuesGrpVO searchVO) throws Exception {
		return insert("system.survey.survQuesGrp.insertData", searchVO);
	}

	/**
	 * 설문질문그룹 수정
	 * @param	SurvQuesGrpVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(SurvQuesGrpVO searchVO) throws Exception {
		return update("system.survey.survQuesGrp.updateData", searchVO);
	}

	/**
	 * 설문질문그룹별 설문매핑 수정
	 */
	public int updateDataForMap(SurvQuesGrpVO searchVO) throws Exception {
		return update("system.survey.survQuesGrp.updateDataForMap", searchVO);
	}
}

