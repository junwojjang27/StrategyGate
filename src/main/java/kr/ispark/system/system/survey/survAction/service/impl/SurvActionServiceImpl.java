/*************************************************************************
* CLASS 명	: SurvActionServiceIpml
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-16
* 기	능	: 설문실시 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-16
**************************************************************************/
package kr.ispark.system.system.survey.survAction.service.impl;

import java.util.List;

import javax.annotation.Resource;

import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.ScDeptVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.system.system.survey.survAction.service.SurvActionVO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service
public class SurvActionServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;

	@Resource
	private SurvActionDAO survActionDAO;

	/**
	 * 설문실시 목록 조회
	 * @param	SurvActionVO searchVO
	 * @return	List<SurvActionVO>
	 * @throws	Exception
	 */
	public List<SurvActionVO> selectList(SurvActionVO searchVO) throws Exception {
		UserVO uservo = (UserVO)SessionUtil.getAttribute("loginVO");
		searchVO.setSurveyUserId(uservo.getUserId());
		return survActionDAO.selectList(searchVO);
	}

	/**
	 * 설문실시 상세 조회
	 * @param	SurvActionVO searchVO
	 * @return	SurvActionVO
	 * @throws	Exception
	 */
	public SurvActionVO selectDetail(SurvActionVO searchVO) throws Exception {
		return survActionDAO.selectDetail(searchVO);
	}

	/**
	 * 설문결과 목록 조회
	 */
	public List<SurvActionVO> selectResultList(SurvActionVO searchVO) throws Exception {
		UserVO uservo = (UserVO)SessionUtil.getAttribute("loginVO");
		searchVO.setSurveyUserId(uservo.getUserId());
		return survActionDAO.selectResultList(searchVO);
	}

	/**
	 * 성과조직 본부 목록 조회
	 */
	public List<ScDeptVO> selectBonbuList(SurvActionVO searchVO) throws Exception {
		return survActionDAO.selectBonbuList(searchVO);
	}

	/**
	 * 설문실시 저장
	 * @param	SurvActionVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String saveData(SurvActionVO dataVO) throws Exception {
		String key = "";
		String[] quesArr = dataVO.getQuesIds().split("\\,", 0);
		String[] quesItemArr = dataVO.getQuesItemIds().split("\\,", 0);
		String[] answerArr = dataVO.getAnswerVals().split("\\,", 0);

		UserVO uservo = (UserVO)SessionUtil.getAttribute("loginVO");
		dataVO.setScDeptId(uservo.getScDeptId());
		if("001".equals(dataVO.getSurveyTypeId())) {
			dataVO.setSurveyUserId(uservo.getUserId());

			survActionDAO.deleteSurResult(dataVO); // 설문답변결과 삭제
			for(int i=0; i<quesArr.length; i++) {
				if(quesArr.length > 0) {
					dataVO.setQuesId(quesArr[i]);
					dataVO.setQuesItemId(quesItemArr[i]);
					dataVO.setAnswerContent(answerArr[i].replace("|뚫훑", ","));
				}
				if(!"empty".equals(dataVO.getAnswerContent())) {
					survActionDAO.insertData(dataVO); // 설문답변결과
				}
			}
			if(!"temp".equals(dataVO.getSaveGbn())) {
				survActionDAO.insertSurTargetUserState(dataVO); // 설문차수별대상자완료여부
			}
		}else{
			key = idgenService.selectNextSeq("SUR_RESULT", "SU", 5, "0");
			dataVO.setSurveyUserId(key);

			for(int i=0; i<quesArr.length; i++) {
				if(quesArr.length > 0) {
					dataVO.setQuesId(quesArr[i]);
					dataVO.setQuesItemId(quesItemArr[i]);
					dataVO.setAnswerContent(answerArr[i]);
				}
				if(!"empty".equals(dataVO.getAnswerContent())) {
					survActionDAO.insertData(dataVO); // 설문답변결과
				}
			}
			dataVO.setSurveyUserId(uservo.getUserId());
			survActionDAO.insertSurTargetUserState(dataVO); // 설문차수별대상자완료여부
		}

		return dataVO.getSurveyId();
	}
}

