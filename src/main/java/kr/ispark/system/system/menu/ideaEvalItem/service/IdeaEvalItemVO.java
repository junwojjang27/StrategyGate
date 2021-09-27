/*************************************************************************
* CLASS 명	: IdeaEvalItemVO
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-27
* 기	능	: 평가항목관리 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-27
**************************************************************************/
package kr.ispark.system.system.menu.ideaEvalItem.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IdeaEvalItemVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String evalItemCd;
	private String evalDegreeId;
	private String evalItemTitle;
	private String particalTypeId;
	private String weightId;
	private String createDt;
	private String updateDt;
	private String deleteDt;
	private String evalItemContent;

	
	private List<IdeaEvalItemVO> gridDataList;
}

