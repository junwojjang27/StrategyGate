/*************************************************************************
* CLASS 명	: ScDeptDiagVO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 05. 03.
* 기	능	: 성과조직도 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 05. 03.		최 초 작 업
**************************************************************************/

package kr.ispark.bsc.mon.org.scDeptDiag.service;

import java.math.BigDecimal;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ScDeptDiagVO extends CommonVO {
	private static final long serialVersionUID = -7696519383084319534L;

    private String findMetricId;

    private String metricId;
    private String metricNm;
    private String evalCycle;
    private String weight;
    private String unit;
    private String status;
    private String color;
    
    private String mon;
    private String target;
    private String actual;
    private String finalScore;
    
    /*
    public BigDecimal getTarget() {return getValue(this.target);}
    public BigDecimal getActual() {return getValue(this.actual);}
    public BigDecimal getFinalScore() {return getValue(this.finalScore);}
    */
}
