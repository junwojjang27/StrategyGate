/*************************************************************************
* CLASS 명	: DeptVO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 1. 25.
* 기	능	: 조직관리 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 1. 25.		최 초 작 업
**************************************************************************/
package kr.ispark.common.system.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeptVO extends CommonVO {
	private static final long serialVersionUID = 123820195267602839L;
	
	private String deptId;
	
	private String deptNm;
	
	private String deptFNm;
	
	private String upDeptId;
	
	private String upDeptNm;
	
	private String sortOrder;
	
	private String deptLevel;
	
	private String bonsaYn;
	
	private String chiefId;
	
	private String chiefNm;
	
	private String beingYn;
	
	private String deptLevelNm;
	
	private String isLeaf;
	
	private String levelId;
	
	private List<DeptVO> gridDataList;
}
