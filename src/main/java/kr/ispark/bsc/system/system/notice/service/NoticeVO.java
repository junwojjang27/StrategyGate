/*************************************************************************
* CLASS 명	: NoticeVO
* 작 업 자	: 박정현
* 작 업 일	: 2018-03-29
* 기	능	: 공지사항 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-03-29
**************************************************************************/
package kr.ispark.bsc.system.system.notice.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NoticeVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String id;
	private String subject;
	private String content;
	private String popupGbnId;
	private String popupGbnNm;
	private String fromDt;
	private String toDt;
	private String width;
	private String height;
	private String createDtStr;
	private String atchFileId;
	private String colName;
	private String atchFileKey;
	private String modifyYn;
	
	private List<NoticeVO> gridDataList;
}

