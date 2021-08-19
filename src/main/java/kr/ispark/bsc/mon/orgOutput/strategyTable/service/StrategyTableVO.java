/*************************************************************************
* CLASS 명	: StrategyTableVO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-03
* 기	능	: 전략연계표 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-03
**************************************************************************/
package kr.ispark.bsc.mon.orgOutput.strategyTable.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StrategyTableVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String mon;
	private String analCycle;
	private String scDeptId;

	private String strategyId;
	private String strategyNm;
	private String score;
	private String status;
	private String conversionScore;
	private String createDt;

	private String findStrategyId;
	private String scDeptNm;
	private String signalId;
	private String color;
	private String firColor;
	private String secColor;
	private String thiColor;
	private String firScDeptId;
	private String firScDeptNm;
	private String SecScDeptId;
	private String SecScDeptNm;
	private String thiScDeptId;
	private String thiScDeptNm;
	private String firStrategyId;
	private String firStrategyNm;
	private String secStrategyId;
	private String secStrategyNm;
	private String thiStrategyId;
	private String thiStrategyNm;

	private String findScDeptId;

	private List<StrategyTableVO> gridDataList;
}

