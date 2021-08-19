/*************************************************************************
* CLASS 명	: MetricDiagVO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-03
* 기	능	: 지표연계도 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-03
**************************************************************************/
package kr.ispark.bsc.mon.orgOutput.metricDiag.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MetricDiagVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String findMetricId;
	private String levelNum;
	
	private String mon;
	private String analCycle;
	private String metricId;
	private String metricNm;
	private String scDeptId;
	private String scDeptNm;
	private String metricGrpId;
	private String metricGrpNm;
	private String perspectiveId;
	private String perspectiveNm;
	private String strategyId;
	private String strategyNm;
	private String target;
	private String actual;
	private String score;
	private String finalScore;
	private String weightScore;
	private String status;
	private String unit;
	private String unitNm;
	private String gubun;
	private String gubunNm;
	private String upMetricId;
	private String upMetricNm;
	private String tamShareYn;
	private String sortOrder;
	private String content;
	private String upScDeptId;
	private String scDeptGrpId;
	private String levelId;
	private String createDt;
	
	private List<MetricDiagVO> gridDataList;
}

