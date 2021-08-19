/*************************************************************************
* CLASS 명	: SurvQuesGrpVO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-11
* 기	능	: 설문질문그룹 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-11
**************************************************************************/
package kr.ispark.system.system.survey.survQuesGrp.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SurvQuesGrpVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	// [TABLE] SUR_QUES_GRP
	private String surveyId;
	private String quesGrpId;
	private String quesGrpNm;
	private String sortOrder;
	private String createDt;

	// [TABLE] SUR_SURVEY_QUES
	private String quesSeq;
	private String quesId;
	private String quesNm;

	// [TABLE] SUR_TARGET_USER
	private String surveyUserId;

	// [TABLE] SUR_SURVEY
	private String closeYn;

	private String findSurveyId;
	private String mapQuesCnt;


	private List<SurvQuesGrpVO> gridDataList;
}

