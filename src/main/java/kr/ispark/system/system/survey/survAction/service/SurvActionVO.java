/*************************************************************************
* CLASS 명	: SurvActionVO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-16
* 기	능	: 설문실시 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-16
**************************************************************************/
package kr.ispark.system.system.survey.survAction.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SurvActionVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	// [TABLE] SUR_SURVEY
	private String surveyId;
	private String surveyNm;
	private String surveyTypeId;
	private String startContent;
	private String endContent;
	private String startDt;
	private String endDt;
	private String closeYn;
	private String surveyYear;
	private String createDt;
	private String modifyDt;
	private String deleteDt;

	// [TABLE] SUR_SURVEY_QUES
	private String quesGbnId;

	// [TABLE] SUR_RESULT
	private String quesId;
	private String quesItemId;
	private String scDeptId;
	private String answerContent;

	// [TABLE] SUR_TARGET_USER_STATE
	private String surveyUserId;
	private String surveyEndYn;

	private String surveyTypeNm;
	private String surveyEndYnNm;
	private String quesIds;
	private String quesItemIds;
	private String answerVals;
	private String saveGbn;
	private String surveyYearNm;

	private List<SurvActionVO> gridDataList;
}

