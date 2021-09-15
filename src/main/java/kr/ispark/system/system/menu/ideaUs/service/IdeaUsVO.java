package kr.ispark.system.system.menu.ideaUs.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IdeaUsVO extends CommonVO {
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
	private String ideaGbnCd;
	private String degree;
	private String evalState;
	private String atchFileKey;
	private String atchFileId;
	private String modifyYn;

	private String searchKeyword;

	private String deptNm;

	private List<IdeaUsVO> gridDataList;
}
