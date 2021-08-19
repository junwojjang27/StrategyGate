/*************************************************************************
* CLASS 명	: ExampleVO
* 작 업 자	: kimyh
* 작 업 일	: 2017. 12. 15.
* 기	능	: 예제 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2017. 12. 15.		최 초 작 업
**************************************************************************/
package kr.ispark.example.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExampleVO extends CommonVO {
	private static final long serialVersionUID = 4590554382546464033L;
	
    private String perspectiveId;	// 관점ID
    private String perspectiveNm;	// 관점명
    private String sortOrder;		// 정렬ID
    private String etc;		// 기타
    private String amt;		// 금액
    
    private List<ExampleVO> gridDataList;
    
    private String userId;
    private String userNm;
    
    private String compId;		// 회사코드
	private String scDeptId;	// 성과조직코드
    private String scDeptNm;	// 성과조직명
    private String upScDeptId;	// 상위성과조직코드
    private String scDeptGrpId;	// 평가군
    private String bscUserId;	// 지표담당자 ID
    private String bscUserNm;	// 지표담당자
    private String managerUserId;	// 부서장 ID
    private String managerUserNm;	// 부서장
    private String content;		// 비고
    private String level;		// 레벨
    private String isLeaf;		// 말단 노드 여부 ("true"/"false")
    private String expandYn;	// 노드 펼침 여부 ("true"/"false")
    
}
