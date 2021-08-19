/*************************************************************************
* CLASS 명	: SystemSettingVO
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-11-01
* 기	능	: 시스템설정 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-11-01
**************************************************************************/
package kr.ispark.system.system.comp.systemSetting.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SystemSettingVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String metricApproveUseYn;
	private String actApproveUseYn;
	private String maxScore;
	private String maxScoreYn;
	private String minScore;
	private String minScoreYn;
	private String createDt;

	private List<SystemSettingVO> gridDataList;
}

