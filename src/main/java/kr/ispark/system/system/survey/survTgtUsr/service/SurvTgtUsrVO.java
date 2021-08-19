/*************************************************************************
* CLASS 명	: SurvTgtUsrVO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-12
* 기	능	: 설문대상자 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-12
**************************************************************************/
package kr.ispark.system.system.survey.survTgtUsr.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SurvTgtUsrVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	// [TABLE] SUR_TARGET_USER
	private String surveyId;
	private String surveyUserId;
	private String surveyUserNm;
	private String deptNm;
	private String posNm;
	private String jikgubNm;

	// [TABLE] SUR_SURVEY
	private String closeYn;

	private String findSurveyId;


	private List<SurvTgtUsrVO> gridDataList;
}

