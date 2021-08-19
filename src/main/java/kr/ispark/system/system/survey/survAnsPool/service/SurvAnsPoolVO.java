/*************************************************************************
* CLASS 명	: SurvAnsPoolVO
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-02
* 기	능	: 설문답변pool VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-02
**************************************************************************/
package kr.ispark.system.system.survey.survAnsPool.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SurvAnsPoolVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	// [TABLE] SUR_ITEM_POOL
	private String itemPoolId;
	private String itemPoolNm;
	private String itemCntId;
	private String mainItemYn;
	private String sortOrder;
	private String createDt;
	private String modifyDt;
	private String deleteDt;

	// [TABLE] SUR_ITEM_DETAIL
	private String itemId;
	private int itemNum = 1;
	private String itemContent;

	private String itemCntNm;
	
	private String mainItemCnt;

	private List<SurvAnsPoolVO> gridDataList;
}

