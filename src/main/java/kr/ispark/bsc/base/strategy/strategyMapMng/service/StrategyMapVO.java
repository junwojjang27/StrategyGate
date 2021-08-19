/*************************************************************************
* CLASS 명	: StrategyMapVO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 2. 1.
* 기	능	: 전략체계도 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 2. 1.			최 초 작 업
**************************************************************************/

package kr.ispark.bsc.base.strategy.strategyMapMng.service;

import java.math.BigDecimal;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StrategyMapVO extends CommonVO {
	private static final long serialVersionUID = 5337394647181968162L;

	private String strategyId;

	private String strategyNm;

	private String metricId;

	private String metricNm;

	private String scDeptId;

	private String kind;

	private String status;

	private BigDecimal x1;

	private BigDecimal y1;

	private BigDecimal x2;

	private BigDecimal y2;

	private BigDecimal x3;

	private BigDecimal y3;

	private BigDecimal x4;

	private BigDecimal y4;

	private BigDecimal x1Pos;
	private BigDecimal y1Pos;

	private BigDecimal x2Pos;
	private BigDecimal y2Pos;

	private BigDecimal x3Pos;
	private BigDecimal y3Pos;

	private BigDecimal x4Pos;
	private BigDecimal y4Pos;

	private String vision;

	private String mission;

	private String strategy;

	private String line;

	private String showMetricYn;
}
