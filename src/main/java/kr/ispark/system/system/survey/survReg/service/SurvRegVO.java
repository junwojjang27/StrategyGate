/*************************************************************************
* CLASS 명	: SurvRegVO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-05
* 기	능	: 설문등록 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-05
**************************************************************************/
package kr.ispark.system.system.survey.survReg.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SurvRegVO extends CommonVO {
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

	private String surveyTypeNm;
	private String userCnt;
	private String quesCnt;
	private String newSurveyId;
	private String copySurveyId;

	private List<SurvRegVO> gridDataList;
}

