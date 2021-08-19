/*************************************************************************
* CLASS 명	: MenuMngVO
* 작 업 자	: joey
* 작 업 일	: 2018-1-7
* 기	능	: 메뉴관리 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	joey		2018-1-7
**************************************************************************/
package kr.ispark.system.system.comp.compMenuMng.service;

import java.util.HashMap;
import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CompMenuMngVO extends CompMenuMngCommonVO {
	private static final long serialVersionUID = -7555616568320156540L;
	
	private List<CompMenuMngVO> langList;
	private List<CompMenuMngVO> gridDataList;
}

