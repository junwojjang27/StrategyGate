/*************************************************************************
* CLASS 명	: ChaVO
* 작 업 자	: 하성준
* 작 업 일	: 2021-11-09
* 기	능	: 문화재청 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-11-09
**************************************************************************/
package kr.ispark.system.system.menu.cha.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

@Getter @Setter
public class ChaVO extends CommonVO {
	private static final long serialVersionUID = -7555616568320156540L;

	private String kpiId;
	private String kpiGbnId;
	private String straTgtId;
	private String resultTgtId;
	private String subjectTgtId;
	private String unitId;
	private String kpiNm;
	private String kpiPoolId;
	private String year1Actual;
	private String year2Actual;
	private String year3Actual;
	private String year0Target;
	private String year1Target;
	private String year2Target;
	private String year3Target;
	private String year4Target;
	private String basisContent;
	private String calContent;
	private String dataContent;
	private String keyword;
	private String sortOrder;
	private String poolYn;
	private String createDt;
	private String modifyDt;
	private String deleteDt;

	private String mission;
	private String vision;
	private String straTgtNm;
	private String straNo;
	private String atchFileKey;
	private String atchFileKey2;
	private String atchFileId;

	private String resultTgtNm;
	private String resultTgtNo;

	private String matchFileId;
	private String vatchFileId;

	private String modifyYn;

	private String straTgtNm2;

	private String straTgtId2;

	private String resultCnt;


	private int seq = -1;
	@JsonIgnore
	private int upSeq = -1;
	private int groupSeq = -1;
	private int groupOrder = 0;
	private int groupLevel = 0;
	
	private List<ChaVO> gridDataList;
}

