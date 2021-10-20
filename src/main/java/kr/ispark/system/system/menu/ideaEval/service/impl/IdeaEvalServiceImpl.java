/*************************************************************************
* CLASS 명	: IdeaEvalServiceIpml
* 작 업 자	: 하성준
* 작 업 일	: 2021-10-13
* 기	능	: 평가하기 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-10-13
**************************************************************************/
package kr.ispark.system.system.menu.ideaEval.service.impl;

import java.util.List;

import javax.annotation.Resource;

import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.system.system.menu.ideaEvalItem.service.IdeaEvalItemVO;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.menu.ideaEval.service.IdeaEvalVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class IdeaEvalServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private IdeaEvalDAO ideaEvalDAO;

	/**
	 * 평가하기 목록 조회
	 * @param	IdeaEvalVO searchVO
	 * @return	List<IdeaEvalVO>
	 * @throws	Exception
	 */
	public List<IdeaEvalVO> selectList(IdeaEvalVO searchVO) throws Exception {
		return ideaEvalDAO.selectList(searchVO);
	}

	/**
	 * 평가하기 > 평가항목 목록 조회
	 * @param	IdeaEvalVO searchVO
	 * @return	List<IdeaEvalVO>
	 * @throws	Exception
	 */
	public List<IdeaEvalVO> selectItemList(IdeaEvalVO searchVO) throws Exception {
		return ideaEvalDAO.selectItemList(searchVO);
	}
	
	/**
	 * 평가하기 상세 조회
	 * @param	IdeaEvalVO searchVO
	 * @return	IdeaEvalVO
	 * @throws	Exception
	 */
	public IdeaEvalVO selectDetail(IdeaEvalVO searchVO) throws Exception {
		return ideaEvalDAO.selectDetail(searchVO);
	}
	
	/**
	 * 평가하기 정렬순서저장
	 * @param	IdeaEvalVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(IdeaEvalVO dataVO) throws Exception {
		int resultCnt = 0;
		for(IdeaEvalVO paramVO : dataVO.getGridDataList()) {
			resultCnt += ideaEvalDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	/**
	 * 평가하기 삭제
	 * @param	IdeaEvalVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaEval(IdeaEvalVO dataVO) throws Exception {
		return ideaEvalDAO.deleteIdeaEval(dataVO);
	}
	
	/**
	 * 평가하기 저장
	 * @param	IdeaEvalVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(IdeaEvalVO dataVO) throws Exception {
		System.out.println("평가하기 : 서비스");
		List<IdeaEvalVO> gridDataList = dataVO.getGridDataList();
		int resultCnt = 0;

		String key = "";

		if(gridDataList != null && 0 < gridDataList.size()){
			if(CommonUtil.isEmpty(dataVO.getEvalCd())) { //평가코드
				key = idgenService.selectNextSeqByYear("IDEA_EVAL_SCORE", dataVO.getYear(), "P", 6, "0");
			}
			else {
				key = dataVO.getEvalCd();
			}
			for(IdeaEvalVO vo: gridDataList ){
				vo.setIdeaCd(dataVO.getIdeaCd());
				vo.setEvalUserId(SessionUtil.getUserVO()!=null?SessionUtil.getUserVO().getUserId():null);
				vo.setEvalDegree(vo.getEvalDegreeId());
				if(CommonUtil.isEmpty(vo.getEvalCd())) { //평가코드
					//key = idgenService.selectNextSeqByYear("IDEA_EVAL_SCORE", vo.getYear(), "P", 6, "0");
					vo.setEvalCd(key);
					System.out.println("vo : " + vo);
					resultCnt +=  ideaEvalDAO.insertData(vo);
				} else {
					resultCnt += ideaEvalDAO.updateData(vo);
				}
			}
		}

		return resultCnt;
	}

	/**
	 * 평가하기 제출
	 * @param	IdeaEvalVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int submitData(IdeaEvalVO dataVO) throws Exception {
		System.out.println("평가 제출하기 : 서비스");
		int resultCnt = 0;

		String key = "";
		key = dataVO.getEvalCd();

		//key = idgenService.selectNextSeqByYear("IDEA_EVAL_INFO", dataVO.getYear(), "P", 6, "0");
		dataVO.setEvalCd(key);
		dataVO.setUserId(SessionUtil.getUserVO()!=null?SessionUtil.getUserVO().getUserId():null);
		dataVO.setEvalDegree(dataVO.getDegree());

		resultCnt +=  ideaEvalDAO.submitData(dataVO);



		return resultCnt;
	}
}

