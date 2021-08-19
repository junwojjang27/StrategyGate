/*************************************************************************
* CLASS 명	: SignalMngVO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-2-6
* 기	능	: 신호등관리 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-2-6
**************************************************************************/
package kr.ispark.system.system.code.signalMng.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignalMngVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String statusId;
	private String statusCodeId;
	private String statusNm;
	private String fromValue;
	private String toValue;
	private String color;
	private String colorVal;
	private String createDt;

	
	private List<SignalMngVO> gridDataList;
}

