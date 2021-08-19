/*************************************************************************
* CLASS 명	: SurvQuesPoolVO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-04
* 기	능	: 설문질문pool VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-04
**************************************************************************/
package kr.ispark.system.system.survey.survQuesPool.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SurvQuesPoolVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	// [TABLE] SUR_QUES_POOL
	private String quesPoolId;
	private String quesPoolNm;
	private String quesGbnId;
	private String itemCntId;
	private String itemCheckGbnId;
	private String sortOrder;
	private String createDt;
	private String modifyDt;
	private String deleteDt;

	// [TABLE] SUR_QUES_POOL_ITEM
	private String quesItemId;
	private int itemNum = 1;
	private String itemContent;

	// [TABLE] SUR_ITEM_POOL
	private String itemPoolId;

	private String quesGbnNm;
	private String itemCntNm;
	private String itemCheckGbnNm;

	private List<SurvQuesPoolVO> gridDataList;
}

