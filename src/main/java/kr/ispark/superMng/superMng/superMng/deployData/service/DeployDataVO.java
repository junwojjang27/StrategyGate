/*************************************************************************
* CLASS 명	: DeployDataVO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-07-09
* 기	능	: 고객사별 전년데이터 일괄적용 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-07-09
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.deployData.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeployDataVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String compId; 
	private String compNm;
	private String hasPastDataYn;
	private String hasNewDataYn;
	
	private String compIds;
	private String targetCompId;
	private String pastYear;

	private List<DeployDataVO> gridDataList;
}

