/*************************************************************************
* CLASS 명	: IdeaUsVO
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-08
* 기	능	: 혁신제안 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-08
**************************************************************************/
package kr.ispark.system.system.menu.ideaUs.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IdeaUsVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String ideaCd;
	private String userId;

	private String userNm;


	private String category;

	private String findCategory;

	private String title;
	private String content;
	private String state;
	private String createDt;
	private String updateDt;
	private String deleteDt;
	private String startDt;
	private String endDt;
	private String atchFileId;
	private String ideaGbnCd;
	private String degree;
	private String evalState;

	private String findUseYn;

	private String deptNm;

	private String categoryNm;

	private String findSearch;
	
	private List<IdeaUsVO> gridDataList;
}

