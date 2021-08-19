/*************************************************************************
* CLASS 명	: DashboardVO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-08
* 기	능	: dashboard VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-08
**************************************************************************/
package kr.ispark.bsc.mon.dashboard.dashboard.service;

import java.util.List;
import java.math.BigDecimal;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DashboardVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String findMetricId;
	private String findScDeptGrpId;
	
	private String loginUserId;
	private String url;
	private String itemId;
	private String itemNm;
	private String checkYn;
	private String itemIds;
	private String width;
	private String height;
	private String classNm;
	private String selectedItemIds;

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
	private String evalCycle;
	private String evalCycleNm;
	private String deptRollup;
	private String deptRollupNm;
	private String propertyId;
	private String propertyNm;
	private String typeId;
	private String typeNm;
	private String weight;
	private String actCalType;
	private String scoreCalTypeGubun;
	private String scoreCalTypeId;
	private String scoreCalTypeNm;
	private String description;
	private String gubun;
	private String gubunNm;
	private String upMetricId;
	private String upMetricNm;
	private String tamShareYn;
	private String sortOrder;
	private String content;
	private String kpiInsertUserId;
	private String kpiInsertUserNm;
	private String actInsertUserId;
	private String actInsertUserNm;
	private String actApproveUserId;
	private String actApproveUserNm;
	private String upScDeptId;
	private String scDeptGrpId;
	private String scDeptGrpNm;
	private String levelId;
	private String createDt;

	private String userScDeptId;
	private String color;
    private String statusId;
	private String restScore;

	private String direction;

	private String mon01;
	private String mon02;
	private String mon03;
	private String mon04;
	private String mon05;
	private String mon06;
	private String mon07;
	private String mon08;
	private String mon09;
	private String mon10;
	private String mon11;
	private String mon12;

	private String govScore;
	private String conversionScore;

	private String deptAvgScore;
	private String deptMinScore;
	private String deptMaxScore;
	private String deptScore;
	private String minScore;
	private String avgScore;
	private String maxScore;

	private String selectedMetricIds;
	private String selectedSortOrders;

	private List<DashboardVO> gridDataList;

	// 소숫점 자리수 처리
	/*
	public String getTarget() {return getValue(this.target);}
	public String getScore() {return getCommaValue(this.score);}
	public String getFinalScore() {return getCommaValue(this.finalScore);}
	public String getRestScore() {return getCommaValue(this.restScore);}
	public String getActual() {return getCommaValue(this.actual);}
	*/
}

