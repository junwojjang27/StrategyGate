/*************************************************************************
* CLASS 명	: IdeaEvalQuesVO
* 작 업 자	: 문은경
* 작 업 일	: 2019-05-21
* 기	능	: 평가 질문 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	문은경		2019-05-21
**************************************************************************/
package kr.ispark.system.system.system.ideaEvalQues.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IdeaEvalQuesVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String evalFormCd;
	private String evalQuesId;
	private String evalQuesSeq;
	private String evalQuesNm;
	private String evalItemCnt;
	private String createDt;
	private String updateDt;
	private String deleteDt;

	private String findevalFormCd;
	
	private List<IdeaEvalQuesVO> gridDataList;
}

