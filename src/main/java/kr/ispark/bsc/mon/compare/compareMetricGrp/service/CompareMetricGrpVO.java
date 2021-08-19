/*************************************************************************
* CLASS 명	: CompareMetricGrpVO
* 작 업 자	: kimyh
* 작 업 일	: 2018-05-02
* 기	능	: 평가군별 비교 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018-05-02
**************************************************************************/
package kr.ispark.bsc.mon.compare.compareMetricGrp.service;

import java.math.BigDecimal;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CompareMetricGrpVO extends CommonVO {
	private static final long serialVersionUID = -6752206906332527076L;
	
	private int rnk = 0;
	private String metricGrpId;
	private String metricGrpNm;
	private String scDeptId;
	private String scDeptNm;
	private String metricNm;
	private String status;
	private String color;
	private String unit;
	private String unitNm;
	
	private String findMetricGrpId;
	
	private String actual;
	private String score;
	private String weightScore;
	private String finalScore;
	
	private String deptScore;
	private String stdScore;
	private String avgScore;
	private String deptAvgScore;
	private String maxScore;
	private String deptMaxScore;
	private String minScore;
	private String deptMinScore;
	private String maxMinScore;

	private String type;
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
	
	/*
	public BigDecimal getActual() {return getValue(this.actual);}
	public BigDecimal getScore() {return getValue(this.score);}
	public BigDecimal getWeightScore() {return getValue(this.weightScore);}
	public BigDecimal getFinalScore() {return getValue(this.finalScore);}
	
	public BigDecimal getDeptScore() {return getValue(this.deptScore);}
	public BigDecimal getStdScore() {return getValue(this.stdScore);}
	public BigDecimal getAvgScore() {return getValue(this.avgScore);}
	public BigDecimal getDeptAvgScore() {return getValue(this.deptAvgScore);}
	public BigDecimal getMaxScore() {return getValue(this.maxScore);}
	public BigDecimal getDeptMaxScore() {return getValue(this.deptMaxScore);}
	public BigDecimal getMinScore() {return getValue(this.minScore);}
	public BigDecimal getDeptMinScore() {return getValue(this.deptMinScore);}
	public BigDecimal getMaxMinScore() {return getValue(this.maxMinScore);}
	
	public BigDecimal getMon01() {return getValue(this.mon01);}
	public BigDecimal getMon02() {return getValue(this.mon02);}
	public BigDecimal getMon03() {return getValue(this.mon03);}
	public BigDecimal getMon04() {return getValue(this.mon04);}
	public BigDecimal getMon05() {return getValue(this.mon05);}
	public BigDecimal getMon06() {return getValue(this.mon06);}
	public BigDecimal getMon07() {return getValue(this.mon07);}
	public BigDecimal getMon08() {return getValue(this.mon08);}
	public BigDecimal getMon09() {return getValue(this.mon09);}
	public BigDecimal getMon10() {return getValue(this.mon10);}
	public BigDecimal getMon11() {return getValue(this.mon11);}
	public BigDecimal getMon12() {return getValue(this.mon12);}
	*/
}

