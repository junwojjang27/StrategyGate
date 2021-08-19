/*************************************************************************
* CLASS 명	: UserMngVO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-1-21
* 기	능	: 사용자관리 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-1-21
**************************************************************************/
package kr.ispark.system.system.menu.userMng.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserMngVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String findDeptId;
	private String findDeptNm;
	private String findSearchUserNm;
	private String findSearchDeptNm;
	
	private String userId;
	private String userNm;
	private String passwd;
	private String deptId;
	private String deptNm;
	private String jikgubId;
	private String jikgubNm;
	private String posId;
	private String posNm;
	private String email;
	private String beingYn;
	private String joinDt;
	private String retireDt;
	private String birthDt;
	private String lang;
	private String pwChangeDt;
	private String hiddenYn;
	private String createDt;
	private String modifyDt;
	
	private String authGubun;
	private String authGubunNm;
	private String fullDescDeptNm;
	private String fullAscDeptNm;
	
	
	private List<UserMngVO> gridDataList;
}

