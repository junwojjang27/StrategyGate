/*************************************************************************
* CLASS 명	: ScDeptDiagMngVO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 1. 31.
* 기	능	: 성과조직도 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 1. 31.		최 초 작 업
**************************************************************************/

package kr.ispark.bsc.base.scDept.scDeptDiagMng.service;

import java.math.BigDecimal;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ScDeptDiagMngVO extends CommonVO {
	private static final long serialVersionUID = -7696519383084319534L;

    // 성과조직코드
    private String scDeptId;
    // 성과조직명
    private String scDeptNm;
    // 상위성과조직코드
    private String upScDeptId;
    // 조직레벨
    private int levelId;
    // 조직평가그룹
    private String scDeptGrpId;
    // 조직평가그룹
    private String scDeptGrpNm;
    // 조직도형
    private String deptKind;
    // 조직도형명
    private String deptKindNm;
    
    private BigDecimal subDeptXPos;
    private BigDecimal subDeptYPos;

    private String score;
    private String status;
    private String statusNm;

    // 데이터 저장 관련
    private String code;
    private String dwDeptId;
    
    // 신호등 관련 
    private String codeId;
    private String fromValue;
    private String toValue;
    private String codeNm;
    private String color;
}
