/*************************************************************************
* CLASS 명	: MenuVO
* 작 업 자	: kimyh
* 작 업 일	: 2017년 11월 23일
* 기	능	: 메뉴 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2017년 11월 23일	최 초 작 업
**************************************************************************/
package kr.ispark.common.system.service;

import org.codehaus.jackson.annotate.JsonIgnore;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MenuVO extends CommonVO {
	private static final long serialVersionUID = 6985078694123877792L;

	private String pgmId;

	private String pgmNm;

	private String upPgmId;

	private String url;

	private String urlPattern;

	private String sortOrder;

	private String levelId;

	private String isLeaf;

	private String siblingOrder;

	private String siblingCnt;

	private String childCnt;

	private String logId;	// 메뉴 접근 로그용 ID

	private String guideComment;
	
	private String urlPage;

	// 로그용
	private String userId;
	private String scDeptId;
	@JsonIgnore	private String ip;
	
	private String serviceId;
	private String serviceSubId;
	private String processNm;
	private String serviceNm;
	private String endYn;
	private String subCnt;
	private String serviceCnt;
}
