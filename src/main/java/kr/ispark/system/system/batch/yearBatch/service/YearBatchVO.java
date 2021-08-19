/*************************************************************************
* CLASS 명	: YearBatchVO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-04-23
* 기	능	: 년배치 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-04-23
**************************************************************************/
package kr.ispark.system.system.batch.yearBatch.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class YearBatchVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String procId;
	private String procNm;
	private String procGbn;
	private String seq;
	private String execYn;
	private String errorId;
	private String execDate;
	private String cnt;
	private String sortOrder;
	private String content;
	
	private List<YearBatchVO> gridDataList;
}

