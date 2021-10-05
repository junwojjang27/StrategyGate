/*************************************************************************
* CLASS 명	: IdeaReviewVO
* 작 업 자	: 하성준
* 작 업 일	: 2021-10-05
* 기	능	: IDEA+검토 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-10-05
**************************************************************************/
package kr.ispark.system.system.menu.ideaReview.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IdeaReviewVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String ideaExecutionCd;
	private String ideaCd;
	private String result;
	private String excutionDt;
	private String createDt;
	private String updateDt;
	private String deleteDt;

	
	private List<IdeaReviewVO> gridDataList;
}

