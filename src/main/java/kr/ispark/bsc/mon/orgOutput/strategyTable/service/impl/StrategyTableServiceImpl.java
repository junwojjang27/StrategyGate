/*************************************************************************
* CLASS 명	: StrategyTableServiceIpml
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-03
* 기	능	: 전략연계표 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-03
**************************************************************************/
package kr.ispark.bsc.mon.orgOutput.strategyTable.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.bsc.mon.orgOutput.strategyTable.service.StrategyTableVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class StrategyTableServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private StrategyTableDAO strategyTableDAO;

	/**
	 * 전략연계표 목록 조회
	 * @param	StrategyTableVO searchVO
	 * @return	List<StrategyTableVO>
	 * @throws	Exception
	 */
	public List<StrategyTableVO> selectList(StrategyTableVO searchVO) throws Exception {
		return strategyTableDAO.selectList(searchVO);
	}
	
	/**
	 * 전략연계표 목록 조회
	 * @param	StrategyTableVO searchVO
	 * @return	List<StrategyTableVO>
	 * @throws	Exception
	 */
	public List<StrategyTableVO> selectStrategyList(StrategyTableVO searchVO) throws Exception {
		return strategyTableDAO.selectStrategyList(searchVO);
	}
	
	/**
	 * 전략연계표 상세 조회
	 * @param	StrategyTableVO searchVO
	 * @return	StrategyTableVO
	 * @throws	Exception
	 */
	public StrategyTableVO selectDetail(StrategyTableVO searchVO) throws Exception {
		return strategyTableDAO.selectDetail(searchVO);
	}
	
	/**
	 * 전략연계표 정렬순서저장
	 * @param	StrategyTableVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(StrategyTableVO dataVO) throws Exception {
		int resultCnt = 0;
		for(StrategyTableVO paramVO : dataVO.getGridDataList()) {
			resultCnt += strategyTableDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	/**
	 * 전략연계표 삭제
	 * @param	StrategyTableVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteStrategyTable(StrategyTableVO dataVO) throws Exception {
		return strategyTableDAO.deleteStrategyTable(dataVO);
	}
	
	/**
	 * 전략연계표 저장
	 * @param	StrategyTableVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(StrategyTableVO dataVO) throws Exception {
		String key = "";
		if(CommonUtil.isEmpty(dataVO.getStrategyId())) {
			key = idgenService.selectNextSeqByYear("originalTableName", dataVO.getYear(), "S", 6, "0");
			dataVO.setStrategyId(key);
			return strategyTableDAO.insertData(dataVO);
		} else {
			return strategyTableDAO.updateData(dataVO);
		}
	}
}

