/*************************************************************************
* CLASS 명	: SurvProgStatVO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-18
* 기	능	: 설문진행현황 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-18
**************************************************************************/
package kr.ispark.system.system.survey.survProgStat.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SurvProgStatVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	// [TABLE] SUR_SURVEY
	private String surveyId;
	private String surveyNm;
	private String surveyTypeId;
	private String closeYn;
	private String surveyYear;

	private String findSurveyId;
	private String surveyTypeNm;
	private String surveyDate;
	private String targetCnt;
	private String answerCnt;
	private String answerRate;
	private String closeNm;
	private String keySurveyId;
	private String min;
	private String max;


	private List<SurvProgStatVO> gridDataList;
}

