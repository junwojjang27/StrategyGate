/*************************************************************************
* CLASS 명	: IdeaReviewServiceIpml
* 작 업 자	: 하성준
* 작 업 일	: 2021-10-05
* 기	능	: IDEA+검토 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-10-05
**************************************************************************/
package kr.ispark.system.system.menu.ideaReview.service.impl;

import java.util.List;

import javax.annotation.Resource;

import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.util.SessionUtil;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.menu.ideaReview.service.IdeaReviewVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class IdeaReviewServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private IdeaReviewDAO ideaReviewDAO;

	/**
	 * IDEA+검토 목록 조회
	 * @param	IdeaReviewVO searchVO
	 * @return	List<IdeaReviewVO>
	 * @throws	Exception
	 */
	public List<IdeaReviewVO> selectList(IdeaReviewVO searchVO) throws Exception {
		return ideaReviewDAO.selectList(searchVO);
	}
	
	/**
	 * IDEA+검토 상세 조회
	 * @param	IdeaReviewVO searchVO
	 * @return	IdeaReviewVO
	 * @throws	Exception
	 */
	public IdeaReviewVO selectDetail(IdeaReviewVO searchVO) throws Exception {
		return ideaReviewDAO.selectDetail(searchVO);
	}
	
	/**
	 * IDEA+검토 정렬순서저장
	 * @param	IdeaReviewVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(IdeaReviewVO dataVO) throws Exception {
		int resultCnt = 0;
		for(IdeaReviewVO paramVO : dataVO.getGridDataList()) {
			resultCnt += ideaReviewDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	/**
	 * IDEA+검토 삭제
	 * @param	IdeaReviewVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaReview(IdeaReviewVO dataVO) throws Exception {
		return ideaReviewDAO.deleteIdeaReview(dataVO);
	}
	
	/**
	 * IDEA+검토 저장
	 * @param	IdeaReviewVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(IdeaReviewVO dataVO) throws Exception {

		int resultCnt = 0;
		String key = "";
		System.out.println("저장 : 서비스");



		if(dataVO.getState().equals("001")) {
			dataVO.setEvalState("001");
		}
		else if(dataVO.getState().equals("002")) {
			dataVO.setEvalState("002");
		}
		else {
			dataVO.setEvalState("005");
		}

		if(CommonUtil.isEmpty(dataVO.getIdeaCd())) {
			key = idgenService.selectNextSeqByYear("IDEA_INFO", dataVO.getYear(), "S", 6, "0");
			dataVO.setIdeaCd(key);
			resultCnt +=  ideaReviewDAO.insertData(dataVO);
		} else {
			resultCnt += ideaReviewDAO.updateData(dataVO);
		}
		return resultCnt;
	}
}

