/*************************************************************************
* CLASS 명	: StrategyDiagVO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-03
* 기	능	: 전략연계도 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-03
**************************************************************************/
package kr.ispark.bsc.mon.orgOutput.strategyDiag.service;

import java.math.BigDecimal;
import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StrategyDiagVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;
	
	private String findStrategyId;
	
	private String sortOrder1;
	private String sortOrder2;
	private String scDeptNm;
	private String scDeptFullNm;
	private String levelNum;
	private String levelId;
     
	private String mon;
	private String analCycle;
	private String scDeptId;
	private String strategyId;
	private String strategyNm;
	private String score;
	private String status;
	private String conversionScore;
	private String createDt;
	
	private String fullScDeptId;
	private String fullScDeptNm;
	private String upScDeptId;
	
	private int nodeRemoveCnt;
	
	private String codeId;
	private String codeNm;
	private String color;
	
	private List<StrategyDiagVO> gridDataList;
	
	/*
	public BigDecimal getScore(){
    	return getValue(this.score);
    }
	*/
}

