/*************************************************************************
* CLASS 명	: CodeGrpDeployVO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-07-04
* 기	능	: 슈퍼관리자 공통코드 배포관리 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-07-04
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.codeGrpDeploy.service;

import java.util.List;

import kr.ispark.system.system.comp.compCodeGrp.service.CompCodeGrpCommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CodeGrpDeployVO extends CompCodeGrpCommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String findDeployTargetYn;

	private String deployTargetYn;

	private String deployCnt;
	private String compCnt;
	private String compNm;
	private String deployYn;
	private String modifyYn;

	private String compIds;
	private String templateCompId;
	private String tempCompId;
	private String compareCodeId;

	private List<CodeGrpDeployVO> LangList;
	private List<CodeGrpDeployVO> gridDataList;
}

