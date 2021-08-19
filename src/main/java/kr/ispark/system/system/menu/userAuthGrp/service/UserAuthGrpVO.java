/*************************************************************************
* CLASS 명	: UserAuthGrpVO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-1-19
* 기	능	: 사용자권한그룹설정 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-1-19
**************************************************************************/
package kr.ispark.system.system.menu.userAuthGrp.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserAuthGrpVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;
	
	private String findAuthGubun;
	private String authGubun;
	private String userId;
	private String deptId;
	private String baseDeptId;
	private String deptNm;
	private String upDeptId;
	private String sortOrder;
	private String chiefId;
	private String chiefNm;
	private String deptLevel;
	private String isLeaf;
	
	private String userNm;
	private String posId;
	private String posNm;
	private String jikgubId;
	private String jikgubNm;
	
	private String userSelectedIds;
	private String[] userSelectedData;
	private String[] deptSelectedData;
	
	private List<UserAuthGrpVO> gridDataList;
}

