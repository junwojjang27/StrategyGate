/*************************************************************************
* CLASS 명	: SurvQuesVO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-10
* 기	능	: 설문질문등록 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-10
**************************************************************************/
package kr.ispark.system.system.survey.survQues.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SurvQuesVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	// [TABLE] SUR_SURVEY_QUES
	private String surveyId;
	private String quesId;
	private String quesSeq;
	private String quesNm;
	private String quesGbnId;
	private String itemCntId;
	private String itemCheckGbnId;
	private String quesLinkYn;
	private String quesGrpId;
	private String createDt;

	// [TABLE] SUR_SURVEY_ITEM
	private String quesItemId;
	private int itemNum = 1;
	private String itemContent;
	private String linkQuesId;

	// [TABLE] SUR_ITEM_POOL
	private String itemPoolId;

	// [TABLE] SUR_QUES_POOL
	private String quesPoolId;

	// [TABLE] SUR_SURVEY
	private String closeYn;

	private String findSurveyId;
	private String quesGbnNm;
	private String itemCntNm;
	private String itemCheckGbnNm;
	private String selectQuesLinkYn;


	private List<SurvQuesVO> gridDataList;
}

