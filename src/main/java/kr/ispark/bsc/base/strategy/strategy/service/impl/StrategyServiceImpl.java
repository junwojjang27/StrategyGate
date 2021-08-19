/*************************************************************************
* CLASS 명	: StrategyServiceImpl
* 작 업 자	: joey
* 작 업 일	: 2018. 1. 29.
* 기	능	:  전략목표 ServiceImpl
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018. 1. 29.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.strategy.strategy.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.bsc.base.strategy.strategy.service.StrategyVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class StrategyServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private StrategyDAO strategyDAO;

	// 전략목표 목록 조회
	public List<StrategyVO> selectList(StrategyVO searchVO) throws Exception {
		return strategyDAO.selectList(searchVO);
	}
	
	// 전략목표 목록 조회
	public List<StrategyVO> selectUpStrategyList(StrategyVO searchVO) throws Exception {
		return strategyDAO.selectUpStrategyList(searchVO);
	}
	
	// 전략목표 목록 조회
	public List<StrategyVO> selectPerspectiveList(StrategyVO searchVO) throws Exception {
		return strategyDAO.selectPerspectiveList(searchVO);
	}

	// 전략목표 삭제
	public int deleteStrategy(StrategyVO dataVO) throws Exception {
		return strategyDAO.deleteStrategy(dataVO);
	}
	
	// 전략목표 저장
	public int saveData(StrategyVO dataVO) throws Exception {
		
		List<StrategyVO> gridDataList = dataVO.getGridDataList();
		int resultCnt = 0;
		String key = "";
		
		if(gridDataList != null && 0 < gridDataList.size()){
			for(StrategyVO vo: gridDataList ){
				
				if(CommonUtil.isEmpty(vo.getStrategyId())) {
					key = idgenService.selectNextSeqByYear("BSC_STRATEGY", vo.getYear(), "S", 6, "0");
					vo.setStrategyId(key);
					
					resultCnt +=  strategyDAO.insertData(vo);
				} else {
					resultCnt += strategyDAO.updateData(vo);
				}
				
			}
		}
		
		return resultCnt;
	}
}
