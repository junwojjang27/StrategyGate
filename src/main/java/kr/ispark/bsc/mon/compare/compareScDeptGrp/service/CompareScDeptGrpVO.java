/*************************************************************************
* CLASS 명	: CompareScDeptGrpVO
* 작 업 자	: kimyh
* 작 업 일	: 2018-04-27
* 기	능	: 평가군별 비교 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018-04-27
**************************************************************************/
package kr.ispark.bsc.mon.compare.compareScDeptGrp.service;

import java.math.BigDecimal;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CompareScDeptGrpVO extends CommonVO {
	private static final long serialVersionUID = -3006545051941672953L;
	
	private int rnk = 0;
	private String findEvalGrpId;
	private String scDeptId;
	private String scDeptNm;
	private String conversionScore;
	private String mesWeight;
	private String mesWeightScore;
	private String mesScoreRate;
	private String nonMesWeight;
	private String nonMesWeightScore;
	private String nonMesScoreRate;
	private String p1Weight;
	private String p1WeightScore;
	private String p1ScoreRate;
	private String p2Weight;
	private String p2WeightScore;
	private String p2ScoreRate;
	
	private String deptScore;
	private String stdScore;
	private String avgScore;
	private String deptAvgScore;
	private String maxScore;
	private String deptMaxScore;
	private String minScore;
	private String deptMinScore;

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
	public BigDecimal getConversionScore() {return getValue(this.conversionScore);}
	public BigDecimal getMesWeight() {return getValue(this.mesWeight);}
	public BigDecimal getMesWeightScore() {return getValue(this.mesWeightScore);}
	public BigDecimal getMesScoreRate() {return getValue(this.mesScoreRate);}
	public BigDecimal getNonMesWeight() {return getValue(this.nonMesWeight);}
	public BigDecimal getNonMesWeightScore() {return getValue(this.nonMesWeightScore);}
	public BigDecimal getNonMesScoreRate() {return getValue(this.nonMesScoreRate);}
	public BigDecimal getP1Weight() {return getValue(this.p1Weight);}
	public BigDecimal getP1WeightScore() {return getValue(this.p1WeightScore);}
	public BigDecimal getP1ScoreRate() {return getValue(this.p1ScoreRate);}
	public BigDecimal getP2Weight() {return getValue(this.p2Weight);}
	public BigDecimal getP2WeightScore() {return getValue(this.p2WeightScore);}
	public BigDecimal getP2ScoreRate() {return getValue(this.p2ScoreRate);}
	
	public BigDecimal getDeptScore() {return getValue(this.deptScore);}
	public BigDecimal getStdScore() {return getValue(this.stdScore);}
	public BigDecimal getAvgScore() {return getValue(this.avgScore);}
	public BigDecimal getDeptAvgScore() {return getValue(this.deptAvgScore);}
	public BigDecimal getMaxScore() {return getValue(this.maxScore);}
	public BigDecimal getDeptMaxScore() {return getValue(this.deptMaxScore);}
	public BigDecimal getMinScore() {return getValue(this.minScore);}
	public BigDecimal getDeptMinScore() {return getValue(this.deptMinScore);}
	
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

