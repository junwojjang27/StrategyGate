/*************************************************************************
* CLASS 명	: PastYearCopyVO
* 작 업 자	: 박정현
* 작 업 일	: 2018-06-29
* 기	능	: 전년데이터일괄적용 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-06-29
**************************************************************************/
package kr.ispark.system.system.batch.pastYearCopy.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PastYearCopyVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String systemGubun;

	private String pastCnt;
	private String newCnt;
	private String tableNm;
	private String tableId;
	private String tableIds;
	private String pastYear;
	private String targetCompId;

	private List<PastYearCopyVO> gridDataList;
}

