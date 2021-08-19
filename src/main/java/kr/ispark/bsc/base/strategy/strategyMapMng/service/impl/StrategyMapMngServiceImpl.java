/*************************************************************************
* CLASS 명	: StrategyMapMngServiceImpl
* 작 업 자	: kimyh
* 작 업 일	: 2018. 2. 2.
* 기	능	: 전략체계도 관리 Service
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 2. 2.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.strategy.strategyMapMng.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.bsc.base.strategy.strategyMapMng.service.StrategyMapVO;
import kr.ispark.common.util.CommonUtil;

@Service
public class StrategyMapMngServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private StrategyMapMngDAO strategyMapMngDAO;

	/**
	 * 비전 & 미션 조회
	 * @param	StrategyMapVO searchVO
	 * @return	StrategyMapVO
	 * @throws	Exception
	 */
	public StrategyMapVO selectVisionMission(StrategyMapVO searchVO) throws Exception {
		return strategyMapMngDAO.selectVisionMission(searchVO);
	}

	/**
	 * 전략체계도 - 관점 목록 조회
	 * @param	StrategyMapVO searchVO
	 * @return	List<StrategyMapVO>
	 * @throws	Exception
	 */
	public List<StrategyMapVO> selectPerspectiveList(StrategyMapVO searchVO) throws Exception {
		return strategyMapMngDAO.selectPerspectiveList(searchVO);
	}

	/**
	 * 전략체계도 목록 조회
	 * @param	StrategyMapVO searchVO
	 * @return	List<StrategyMapVO>
	 * @throws	Exception
	 */
	public List<StrategyMapVO> selectList(StrategyMapVO searchVO) throws Exception {
		return strategyMapMngDAO.selectList(searchVO);
	}

	/**
	 * 전략체계도관리 지표 목록 조회
	 * @param	StrategyMapVO searchVO
	 * @return	List<StrategyMapVO>
	 * @throws	Exception
	 */
	public List<StrategyMapVO> selectMetricList(StrategyMapVO searchVO) throws Exception {
		return strategyMapMngDAO.selectMetricList(searchVO);
	}

	/**
	 * 전략체계도관리 화살표 목록 조회
	 * @param	StrategyMapVO searchVO
	 * @return	List<StrategyMapVO>
	 * @throws	Exception
	 */
	public List<StrategyMapVO> selectArrowList(StrategyMapVO searchVO) throws Exception {
		return strategyMapMngDAO.selectArrowList(searchVO);
	}

	/**
	 * 전략체계도 지표 표시 여부 조회
	 * @param	StrategyMapVO searchVO
	 * @return	List<StrategyMapVO>
	 * @throws	Exception
	 */
	public String selectShowMetricYn(StrategyMapVO searchVO) throws Exception {
		return strategyMapMngDAO.selectShowMetricYn(searchVO);
	}

	/**
	 * 전략체계도 저장
	 * @param	StrategyMapVO dataVO
	 * @throws	Exception
	 */
	public void saveData(StrategyMapVO dataVO) throws Exception {
		// 비전 & 미션
		strategyMapMngDAO.deleteVisionMission(dataVO);
		strategyMapMngDAO.insertVisionMission(dataVO);

		String strategy = dataVO.getStrategy();
		String line = dataVO.getLine();
		String arr_strategy[] = strategy.split("/");
		String arr_line[] = line.split("/");

		strategyMapMngDAO.deleteData(dataVO);

		// 지표 표시 여부
		dataVO.setStrategyId("CHKBOX1");
		dataVO.setKind("checkBox");
		dataVO.setX1Pos(new BigDecimal(CommonUtil.removeNull(dataVO.getShowMetricYn()).equals("N") ? 0 : 1));
		strategyMapMngDAO.insertData(dataVO);

		if(arr_strategy.length > 0) {
			for (int i = 0; i < arr_strategy.length; i++) {
				if (arr_strategy[i].trim().length() > 0 ) {
					String arr_values[] = arr_strategy[i].trim().split(",");
					String strategy_id	= arr_values[0].trim();
					String kind	 	= arr_values[1].trim();
					String x1_pos	 	= arr_values[2].trim();
					String y1_pos	 	= arr_values[3].trim();

					strategy_id	= strategy_id.substring(strategy_id.indexOf(':')+1);
					kind 		= kind.substring(kind.indexOf(':')+1);
					x1_pos 		= x1_pos.substring(x1_pos.indexOf(':')+1);
					y1_pos 		= y1_pos.substring(y1_pos.indexOf(':')+1);

					dataVO.setStrategyId(strategy_id);
					dataVO.setKind(kind);
					dataVO.setX1Pos( new BigDecimal(x1_pos));
					dataVO.setY1Pos( new BigDecimal(y1_pos));

					strategyMapMngDAO.insertData(dataVO);
				}
			}
		 }

		if(arr_line.length > 0) {
			for (int i = 0; i < arr_line.length; i++) {
				if (arr_line[i].trim().length() > 0) {
					String arr_values[] = arr_line[i].trim().split(",");
					String strategy_id	= arr_values[0].trim();
					String kind		 	= arr_values[1].trim();
					String x1_pos	 	= arr_values[2].trim();
					String y1_pos	 	= arr_values[3].trim();
					String x2_pos	 	= arr_values[4].trim();
					String y2_pos	 	= arr_values[5].trim();

					strategy_id	= strategy_id.substring(strategy_id.indexOf(':')+1);
					kind 		= kind.substring(kind.indexOf(':')+1);
					x1_pos 		= x1_pos.substring(x1_pos.indexOf(':')+1);
					y1_pos 		= y1_pos.substring(y1_pos.indexOf(':')+1);
					x2_pos 		= x2_pos.substring(x2_pos.indexOf(':')+1);
					y2_pos 		= y2_pos.substring(y2_pos.indexOf(':')+1);

					dataVO.setStrategyId(strategy_id);
					dataVO.setKind(kind);
					dataVO.setX1Pos(new BigDecimal(x1_pos));
					dataVO.setY1Pos(new BigDecimal(y1_pos));
					dataVO.setX2Pos(new BigDecimal(x2_pos));
					dataVO.setY2Pos(new BigDecimal(y2_pos));

					String x3_pos = null, y3_pos = null, x4_pos = null, y4_pos = null;
					if(arr_values.length > 6) {
						x3_pos	 	= arr_values[6].trim();
						y3_pos	 	= arr_values[7].trim();
						x4_pos	 	= arr_values[8].trim();
						y4_pos	 	= arr_values[9].trim();

						x3_pos 		= x3_pos.substring(x3_pos.indexOf(':')+1);
						y3_pos 		= y3_pos.substring(y3_pos.indexOf(':')+1);
						x4_pos 		= x4_pos.substring(x4_pos.indexOf(':')+1);
						y4_pos 		= y4_pos.substring(y4_pos.indexOf(':')+1);

						dataVO.setX3Pos(new BigDecimal(x3_pos));
						dataVO.setY3Pos(new BigDecimal(y3_pos));
						dataVO.setX4Pos(new BigDecimal(x4_pos));
						dataVO.setY4Pos(new BigDecimal(y4_pos));
					} else {
						dataVO.setX3Pos(null);
						dataVO.setY3Pos(null);
						dataVO.setX4Pos(null);
						dataVO.setY4Pos(null);
					}

					strategyMapMngDAO.insertData(dataVO);
				}
			}
		}
	}

}
