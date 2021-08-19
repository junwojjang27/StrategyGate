/*************************************************************************
* CLASS 명	: AuthGrpVO
* 작 업 자	: joey
* 작 업 일	: 2018-1-12
* 기	능	: 그룹별권한 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018-1-12
**************************************************************************/
package kr.ispark.system.system.menu.authGrp.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthGrpVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String findAuthGubun;
	private String authGubun;
	private String userId;
	private String deptId;
	
	private String pgmId;
	private String pgmNm;
	private String fullPgmNm;
	private String pgmLevelId;
	private String upPgmId;
	
	private String[] menuSelectedData;
	private String menuSelectedDataList;
	
	private List<AuthGrpVO> gridDataList;
}

