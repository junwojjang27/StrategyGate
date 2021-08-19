/*************************************************************************
* CLASS 명	: nonCamelPageNmVO
* 작 업 자	: devNm
* 작 업 일	: devDate
* 기	능	: koPageNm VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	devNm		devDate
**************************************************************************/
package fullPackageDotPath.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class nonCamelPageNmVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

voColumns
	
	private List<nonCamelPageNmVO> gridDataList;
}
