/*************************************************************************
* CLASS 명	: CalTypeMngVO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-2-6
* 기	능	: 산식관리 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-2-6
**************************************************************************/
package kr.ispark.system.system.code.calTypeMng.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CalTypeMngVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;
	
	private String calTypeId;
	private String calTypeNm;
	private String calType;
	private String content;
	private String metricCnt;
	private String createDt;
	private String modifyDt;
	private String deleteDt;

	
	private List<CalTypeMngVO> gridDataList;
}

