/*************************************************************************
* CLASS 명	: IdeaEvalItemServiceIpml
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-27
* 기	능	: 평가항목관리 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-27
**************************************************************************/
package kr.ispark.system.system.menu.ideaEvalItem.service.impl;

import java.util.List;

import javax.annotation.Resource;

import kr.ispark.bsc.base.strategy.perspective.service.PerspectiveVO;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.menu.ideaEvalItem.service.IdeaEvalItemVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class IdeaEvalItemServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private IdeaEvalItemDAO ideaEvalItemDAO;

	/**
	 * 평가항목관리 목록 조회
	 * @param	IdeaEvalItemVO searchVO
	 * @return	List<IdeaEvalItemVO>
	 * @throws	Exception
	 */
	public List<IdeaEvalItemVO> selectList(IdeaEvalItemVO searchVO) throws Exception {
		return ideaEvalItemDAO.selectList(searchVO);
	}

	/**
	 * 평가항목관리 정렬순서저장
	 * @param	IdeaEvalItemVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(IdeaEvalItemVO dataVO) throws Exception {
		int resultCnt = 0;
		for(IdeaEvalItemVO paramVO : dataVO.getGridDataList()) {
			resultCnt += ideaEvalItemDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}
	
	/**
	 * 평가항목관리 상세 조회
	 * @param	IdeaEvalItemVO searchVO
	 * @return	IdeaEvalItemVO
	 * @throws	Exception
	 */
	public IdeaEvalItemVO selectDetail(IdeaEvalItemVO searchVO) throws Exception {
		return ideaEvalItemDAO.selectDetail(searchVO);
	}

	/**
	 * 평가항목관리 삭제
	 * @param	IdeaEvalItemVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaEvalItem(IdeaEvalItemVO dataVO) throws Exception {
		return ideaEvalItemDAO.deleteIdeaEvalItem(dataVO);
	}
	
	/**
	 * 평가항목관리 저장
	 * @param	IdeaEvalItemVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(IdeaEvalItemVO dataVO) throws Exception {
		List<IdeaEvalItemVO> gridDataList = dataVO.getGridDataList();
		int resultCnt = 0;
		String key = "";

		if(gridDataList != null && 0 < gridDataList.size()){
			for(IdeaEvalItemVO vo: gridDataList ){

				if(CommonUtil.isEmpty(vo.getEvalItemCd())) {
					key = idgenService.selectNextSeqByYear("IDEA_EVAL_ITEM", vo.getYear(), "P", 6, "0");
					vo.setEvalItemCd(key);

					resultCnt +=  ideaEvalItemDAO.insertData(vo);
				} else {
					resultCnt += ideaEvalItemDAO.updateData(vo);
				}

			}
		}

		return resultCnt;
	}
}

