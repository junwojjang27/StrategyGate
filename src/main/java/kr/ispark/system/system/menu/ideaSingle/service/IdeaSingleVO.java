/*************************************************************************
* CLASS 명	: IdeaSingleVO
* 작 업 자	: 하성준
* 작 업 일	: 2021-09-07
* 기	능	: 간단제안 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-09-07
**************************************************************************/
package kr.ispark.system.system.menu.ideaSingle.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IdeaSingleVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String ideaCd;
	private String userId;
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

	private String findSearch;

	
	private List<IdeaSingleVO> gridDataList;
}

