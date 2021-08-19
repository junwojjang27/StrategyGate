/*************************************************************************
* CLASS 명	: IdeaEvalQuesServiceIpml
* 작 업 자	: 문은경
* 작 업 일	: 2019-05-21
* 기	능	: 평가 질문 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	문은경		2019-05-21
**************************************************************************/
package kr.ispark.system.system.system.ideaEvalQues.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.system.ideaEvalQues.service.IdeaEvalQuesVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class IdeaEvalQuesServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private IdeaEvalQuesDAO ideaEvalQuesDAO;

	/**
	 * 평가 질문 목록 조회
	 * @param	IdeaEvalQuesVO searchVO
	 * @return	List<IdeaEvalQuesVO>
	 * @throws	Exception
	 */
	public List<IdeaEvalQuesVO> selectList(IdeaEvalQuesVO searchVO) throws Exception {
		return ideaEvalQuesDAO.selectList(searchVO);
	}
	
	/**
	 * 평가 질문 상세 조회
	 * @param	IdeaEvalQuesVO searchVO
	 * @return	IdeaEvalQuesVO
	 * @throws	Exception
	 */
	public IdeaEvalQuesVO selectDetail(IdeaEvalQuesVO searchVO) throws Exception {
		return ideaEvalQuesDAO.selectDetail(searchVO);
	}
	
	/**
	 * 평가 질문 정렬순서저장
	 * @param	IdeaEvalQuesVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(IdeaEvalQuesVO dataVO) throws Exception {
		int resultCnt = 0;
		for(IdeaEvalQuesVO paramVO : dataVO.getGridDataList()) {
			resultCnt += ideaEvalQuesDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	/**
	 * 평가 질문 삭제
	 * @param	IdeaEvalQuesVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaEvalQues(IdeaEvalQuesVO dataVO) throws Exception {
		return ideaEvalQuesDAO.deleteIdeaEvalQues(dataVO);
	}
	
	/**
	 * 평가 질문 저장
	 * @param	IdeaEvalQuesVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(IdeaEvalQuesVO dataVO) throws Exception {
		String key = "";
		if(CommonUtil.isEmpty(dataVO.getEvalQuesId())) {
			key = idgenService.selectNextSeqByYear("IDEA_EVAL_QUES", dataVO.getYear(), "S", 6, "0");
			dataVO.setEvalQuesId(key);
			return ideaEvalQuesDAO.insertData(dataVO);
		} else {
			return ideaEvalQuesDAO.updateData(dataVO);
		}
	}
	/*public String saveData(IdeaEvalQuesVO dataVO) throws Exception {
		int resultCnt = 0;
		String key = "";
		String rtnVal = "";
		if(CommonUtil.isEmpty(dataVO.getEvalQuesId())) {
			key = idgenService.selectNextSeq("IDEA_EVAL_QUES", "Q", 6, "0");
			dataVO.setEvalQuesId(key);
			resultCnt = ideaEvalQuesDAO.insertData(dataVO);
			surveyItemProc(dataVO, resultCnt);
			rtnVal = key;
		} else {
			resultCnt = ideaEvalQuesDAO.updateData(dataVO);
			surveyItemProc(dataVO, resultCnt);
			rtnVal = dataVO.getEvalQuesId();
		}
		return rtnVal;
	}
	public int surveyItemProc(IdeaEvalQuesVO dataVO, int resultCnt) throws Exception {
		String subKey = "";
		int subResultCnt = 0;
		if(resultCnt > 0) {
			subResultCnt += ideaEvalQuesDAO.deleteSurveyItem(dataVO);
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
	}*/
}

