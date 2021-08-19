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

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CompCodeGrpVO extends CompCodeGrpCommonVO {
	private static final long serialVersionUID = -7555616568320156540L;
	
	private List<CompCodeGrpVO> LangList;
	private List<CompCodeGrpVO> gridDataList;
}

