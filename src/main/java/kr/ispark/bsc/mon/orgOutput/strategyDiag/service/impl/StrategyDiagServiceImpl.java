/*************************************************************************
* CLASS 명	: StrategyDiagServiceIpml
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-03
* 기	능	: 전략연계도 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-03
**************************************************************************/
package kr.ispark.bsc.mon.orgOutput.strategyDiag.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.bsc.mon.orgOutput.strategyDiag.service.StrategyDiagVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class StrategyDiagServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private StrategyDiagDAO strategyDiagDAO;

	/**
	 * 전략연계도 목록 조회
	 * @param	StrategyDiagVO searchVO
	 * @return	List<StrategyDiagVO>
	 * @throws	Exception
	 */
	public List<StrategyDiagVO> selectList(StrategyDiagVO searchVO) throws Exception {
		return strategyDiagDAO.selectList(searchVO);
	}
	
	/**
	 * 전략연계도 목록 조회
	 * @param	StrategyDiagVO searchVO
	 * @return	List<StrategyDiagVO>
	 * @throws	Exception
	 */
	public List<StrategyDiagVO> selectStrategyList(StrategyDiagVO searchVO) throws Exception {
		return strategyDiagDAO.selectStrategyList(searchVO);
	}
	
	/**
	 * 전략연계도 목록 조회
	 * @param	StrategyDiagVO searchVO
	 * @return	List<StrategyDiagVO>
	 * @throws	Exception
	 */
	public List<StrategyDiagVO> selectSignalList(StrategyDiagVO searchVO) throws Exception {
		return strategyDiagDAO.selectSignalList(searchVO);
	}
	
	/**
	 * 전략연계도 상세 조회
	 * @param	StrategyDiagVO searchVO
	 * @return	StrategyDiagVO
	 * @throws	Exception
	 */
	public StrategyDiagVO selectDetail(StrategyDiagVO searchVO) throws Exception {
		return strategyDiagDAO.selectDetail(searchVO);
	}
	
	/**
	 * 전략연계도 정렬순서저장
	 * @param	StrategyDiagVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(StrategyDiagVO dataVO) throws Exception {
		int resultCnt = 0;
		for(StrategyDiagVO paramVO : dataVO.getGridDataList()) {
			resultCnt += strategyDiagDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	/**
	 * 전략연계도 삭제
	 * @param	StrategyDiagVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteStrategyDiag(StrategyDiagVO dataVO) throws Exception {
		return strategyDiagDAO.deleteStrategyDiag(dataVO);
	}
	
	/**
	 * 전략연계도 저장
	 * @param	StrategyDiagVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(StrategyDiagVO dataVO) throws Exception {
		String key = "";
		if(CommonUtil.isEmpty(dataVO.getStrategyId())) {
			key = idgenService.selectNextSeqByYear("originalTableName", dataVO.getYear(), "S", 6, "0");
			dataVO.setStrategyId(key);
			return strategyDiagDAO.insertData(dataVO);
		} else {
			return strategyDiagDAO.updateData(dataVO);
		}
	}
}

