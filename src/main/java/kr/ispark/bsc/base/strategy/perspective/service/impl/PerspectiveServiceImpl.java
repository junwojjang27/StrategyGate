/*************************************************************************
* CLASS 명	: PerspectiveServiceImpl
* 작 업 자	: joey
* 작 업 일	: 2018. 1. 29.
* 기	능	:  관점 ServiceImpl
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018. 1. 29.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.strategy.perspective.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.bsc.base.strategy.perspective.service.PerspectiveVO;
import kr.ispark.bsc.base.strategy.strategy.service.StrategyVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class PerspectiveServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private PerspectiveDAO perspectiveDAO;

	// 성과조직관리 목록 조회
	public List<PerspectiveVO> selectList(PerspectiveVO searchVO) throws Exception {
		return perspectiveDAO.selectList(searchVO);
	}
	
	// 성과조직 상세 조회
	public PerspectiveVO selectDetail(PerspectiveVO searchVO) throws Exception {
		return perspectiveDAO.selectDetail(searchVO);
	}
	
	// 정렬순서저장
	public int updateSortOrder(PerspectiveVO dataVO) throws Exception {
		int resultCnt = 0;
		for(PerspectiveVO paramVO : dataVO.getGridDataList()) {
			resultCnt += perspectiveDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	// 성과조직 삭제
	public int deletePerspective(PerspectiveVO dataVO) throws Exception {
		return perspectiveDAO.deletePerspective(dataVO);
	}
	
	// 성과조직 저장
	public int saveData(PerspectiveVO dataVO) throws Exception {
		List<PerspectiveVO> gridDataList = dataVO.getGridDataList();
		int resultCnt = 0;
		String key = "";
		
		if(gridDataList != null && 0 < gridDataList.size()){
			for(PerspectiveVO vo: gridDataList ){
				
				if(CommonUtil.isEmpty(vo.getPerspectiveId())) {
					key = idgenService.selectNextSeqByYear("BSC_PERSPECTIVE", vo.getYear(), "P", 6, "0");
					vo.setPerspectiveId(key);
					
					resultCnt +=  perspectiveDAO.insertData(vo);
				} else {
					resultCnt += perspectiveDAO.updateData(vo);
				}
				
			}
		}
		
		return resultCnt;
	}
}
