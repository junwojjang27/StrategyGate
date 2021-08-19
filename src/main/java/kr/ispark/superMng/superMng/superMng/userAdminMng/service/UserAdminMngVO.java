/*************************************************************************
* CLASS 명	: UserAdminMngVO
* 작 업 자	: 박정현
* 작 업 일	: 2018-07-04
* 기	능	: 사용자관리 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-07-04
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.userAdminMng.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserAdminMngVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String findCompId;
	private String findCompNm;
	private String findDeptId;
	private String findDeptNm;
	private String findAuthId;
	private String findAuthNm;
	private String findUserId;
	private String findUserNm;

	private String compId;
	private String compNm;
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
	private String jobId;
	private String jobNm;

	private String authGubun;
	private String authGubunNm;
	
	private String targetDbId;
	
	private List<UserAdminMngVO> gridDataList;
}

