/*************************************************************************
* CLASS 명	: SurvQuesServiceIpml
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-10
* 기	능	: 설문질문등록 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-10
**************************************************************************/
package kr.ispark.system.system.survey.survQues.service.impl;

import java.util.List;

import javax.annotation.Resource;

import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.system.system.survey.survQues.service.SurvQuesVO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service
public class SurvQuesServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;

	@Resource
	private SurvQuesDAO survQuesDAO;

	/**
	 * 설문질문등록 목록 조회
	 * @param	SurvQuesVO searchVO
	 * @return	List<SurvQuesVO>
	 * @throws	Exception
	 */
	public List<SurvQuesVO> selectList(SurvQuesVO searchVO) throws Exception {
		return survQuesDAO.selectList(searchVO);
	}

	/**
	 * 설문질문등록 상세 조회
	 * @param	SurvQuesVO searchVO
	 * @return	SurvQuesVO
	 * @throws	Exception
	 */
	public SurvQuesVO selectDetail(SurvQuesVO searchVO) throws Exception {
		return survQuesDAO.selectDetail(searchVO);
	}

	/**
	 * 설문질문답변 목록조회
	 */
	public List<SurvQuesVO> selectItemList(SurvQuesVO searchVO) throws Exception {
		return survQuesDAO.selectItemList(searchVO);
	}

	/**
	 * 설문질문등록 삭제
	 * @param	SurvQuesVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteSurvQues(SurvQuesVO dataVO) throws Exception {
		int resultCnt = 0;
		for(String quesId: dataVO.getKeys()) {
			dataVO.setQuesId(quesId);
			resultCnt += survQuesDAO.deleteSurveyItem(dataVO);
		}
		resultCnt += survQuesDAO.deleteSurvQues(dataVO);
		return resultCnt;
	}

	/**
	 * 설문질문등록 저장
	 * @param	SurvQuesVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String saveData(SurvQuesVO dataVO) throws Exception {
		int resultCnt = 0;
		String key = "";
		String rtnVal = "";
		if(CommonUtil.isEmpty(dataVO.getQuesId())) {
			key = idgenService.selectNextSeq("SUR_SURVEY_QUES", "Q", 6, "0");
			dataVO.setQuesId(key);
			resultCnt = survQuesDAO.insertData(dataVO);
			surveyItemProc(dataVO, resultCnt);
			rtnVal = key;
		} else {
			resultCnt = survQuesDAO.updateData(dataVO);
			surveyItemProc(dataVO, resultCnt);
			rtnVal = dataVO.getQuesId();
		}
		return rtnVal;
	}

	public int surveyItemProc(SurvQuesVO dataVO, int resultCnt) throws Exception {
		String subKey = "";
		int subResultCnt = 0;
		if(resultCnt > 0) {
			subResultCnt += survQuesDAO.deleteSurveyItem(dataVO);
			if(!"002".equals(dataVO.getQuesGbnId())) {
				String[] itemContentArr = dataVO.getItemContent().split("\\,",0);
				String[] linkQuesIdArr = dataVO.getLinkQuesId().split("\\,",0);

				for(int i=0; i<Integer.parseInt(dataVO.getItemCntId()); i++) {
					subKey = idgenService.selectNextSeq("SUR_SURVEY_ITEM", "QI", 5, "0");
					dataVO.setQuesItemId(subKey);
					dataVO.setItemNum((i+1));
					dataVO.setItemContent(itemContentArr[i]);
					if(linkQuesIdArr.length > 0 && linkQuesIdArr.length>=i) {
						if(!"empty".equals(linkQuesIdArr[i])) {
							dataVO.setLinkQuesId(linkQuesIdArr[i]);
						}else{
							dataVO.setLinkQuesId(null);
						}
					}
					subResultCnt += survQuesDAO.insertSurveyItem(dataVO);
				}
			}
		}
		return subResultCnt;
	}
}

