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
		String key = "";
		if(CommonUtil.isEmpty(dataVO.getUserId())) {
			key = idgenService.selectNextSeqByYear("originalTableName", dataVO.getYear(), "S", 6, "0");
			dataVO.setUserId(key);
			return ideaEvalDAO.insertData(dataVO);
		} else {
			return ideaEvalDAO.updateData(dataVO);
		}
	}
}

