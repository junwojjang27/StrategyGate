/*************************************************************************
* CLASS 명	: SurvProgStatDAO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-18
* 기	능	: 설문진행현황 DAO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-18
**************************************************************************/
package kr.ispark.system.system.survey.survProgStat.service.impl;


import java.util.ArrayList;
import java.util.List;

import kr.ispark.common.security.service.UserVO;
import kr.ispark.system.system.survey.survProgStat.service.SurvProgStatVO;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository
public class SurvProgStatDAO extends EgovComAbstractDAO {
	/**
	 * 설문진행현황 목록 조회
	 * @param	SurvProgStatVO searchVO
	 * @return	List<SurvProgStatVO>
	 * @throws	Exception
	 */
	public List<SurvProgStatVO> selectList(SurvProgStatVO searchVO) throws Exception {
		return selectList("system.survey.survProgStat.selectList", searchVO);
	}

	/**
	 * 설문대상자 목록 조회
	 * @param	SurvProgStatVO searchVO
	 * @return	List<SurvProgStatVO>
	 * @throws	Exception
	 */
	public List<UserVO> selectSendMail(SurvProgStatVO searchVO) throws Exception {
		return selectList("system.survey.survProgStat.selectSendMail", searchVO);
	}

	/**
	 * 설문질문답변별종합점수 삭제
	 */
	public int deleteItemTotalData(SurvProgStatVO dataVO) throws Exception {
		return update("system.survey.survProgStat.deleteItemTotalData", dataVO);
	}

	/**
	 * 설문질문답변별종합점수 입력
	 */
	public int insertItemTotalForAll(SurvProgStatVO dataVO) throws Exception {
		return insert("system.survey.survProgStat.insertItemTotalForAll", dataVO);
	}

	/**
	 * 설문진행현황 마감
	 * @param	SurvProgStatVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateData(SurvProgStatVO dataVO) throws Exception {
		return update("system.survey.survProgStat.updateData", dataVO);
	}
}

