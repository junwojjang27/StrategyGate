/*************************************************************************
* CLASS 명	: SurvResultVO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-22
* 기	능	: 설문결과 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-22
**************************************************************************/
package kr.ispark.system.system.survey.survResult.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SurvResultVO extends CommonVO {
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
	private String quesId;
	private String quesSeq;
	private String quesNm;
	private String quesGbnId;
	private String itemCntId;
	private String itemCheckGbnId;
	private String quesLinkYn;

	// [TABLE] SUR_SURVEY_ITEM
	private String quesItemId;
	private String itemNum;
	private String itemContent;
	private String linkQuesId;

	// [TABLE] SUR_QUES_GRP
	private String quesGrpId;
	private String quesGrpNm;

	// [TABLE] SUR_TARGET_USER
	private String surveyUserId;
	private String surveyUserNm;

	// [TABLE] SUR_RESULT
	private String answerContent;

	// [TABLE] SUR_ITEM_SUMMARY
	private String answerCnt;
	private String answerRate;

	private String findSurveyId;
	private String findChartGbn;
	private String findQuesGbnId;
	private String scDeptId;
	private String allUsrCnt;
	private String ansUsrCnt;
	private String particpationRate;
	private String selectQuesLinkYn;
	private String idx;
	private String subIdx;

	private List<SurvResultVO> gridDataList;
}

