/*************************************************************************
* CLASS 명	: CodeGrpVO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-1-26
* 기	능	: 공통코그룹관리 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-1-26
**************************************************************************/
package kr.ispark.system.system.comp.compCodeGrp.service;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CompCodeGrpCommonVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String findUseYn;
	private String findCodeUseYn;
	private String findLang;
	private String inputLang;
	private String langNm;
	private String codeGrpId;
	private String codeGrpNm;
	private String codeGrpNmLang;
	private String codeDefId;
	private String codeDefNm;
	private String yearYn;
	private String content;
	private String createDt;
	private String deleteDt;
	private String useYn;
	private String modifyYn;
	private String modifyYnNm;
	private String codeCnt;
	private String codeAllCnt;

	private String codeId;
	private String codeNm;
	private String codeNmLang;
	private String sortOrder;
	private String etc1;
	private String etc2;

	private String codeGbnId;
	private String codeGbnNm;

}

