/*************************************************************************
* CLASS 명	: StrategyVO
* 작 업 자	: joey
* 작 업 일	: 2018. 1. 29.
* 기	능	: 전략목표 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018. 1. 29.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.strategy.strategy.service;

import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StrategyVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String strategyId;

	private String strategyNm;

	private String perspectiveId;

	private String perspectiveNm;

	private String upStrategyId;

	private String upStrategyNm;

	private String cntStrategy;

	private List<StrategyVO> gridDataList;
}
