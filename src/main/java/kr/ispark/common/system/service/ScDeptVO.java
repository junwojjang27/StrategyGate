/*************************************************************************
* CLASS 명	: ScDeptVO
* 작 업 자	: kimyh
* 작 업 일	: 2018. 1. 23.
* 기	능	: 성과조직 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 1. 23.		최 초 작 업
**************************************************************************/
package kr.ispark.common.system.service;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ScDeptVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String scDeptId;
	private String upScDeptId;
	private String scDeptNm;
	private String orgScDeptNm;
	private String upScDeptNm;
	private String deptId;
	private String metricCnt;
	
	@JsonIgnore
	private List<String> deptIds;
	
	private String upDeptId;
	private String deptNm;
	private String scDeptFullNm;
	private String scDeptGrpId;
	private String scDeptGrpNm;
	private String bscUserId;
	private String bscUserNm;
	private String bscUserEmail;
	private String managerUserId;
	private String managerUserNm;
	private String managerUserEmail;
	
	private String deptKind;
	private String sortOrder;
	private String level;
	private String isLeaf;
	private String findMappingBase;
	private String baseDeptId;

	private int mappingCnt = 0;
	private int realSortOrder;
	private String levelId;
	private String fullDescScDeptNm;
	private String fullAscScDeptNm;
	private String fullScDeptId;
	
	private String approveStatusId;
	private String approveStatusNm;

	@JsonIgnore
	private String resetAllYn = "N";	// 성과조직 저장, 수정시 모든 지표의 담당자를 초기화 할 것인지 여부
	
	private List<ScDeptVO> gridDataList;
}
