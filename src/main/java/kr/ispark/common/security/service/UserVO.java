/*************************************************************************
* CLASS 명	: UserVO
* 작 업 자	: kimyh
* 작 업 일	: 2017. 11. 23.
* 기	능	: 사용자 정보 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2017. 11. 23.		최 초 작 업
**************************************************************************/

package kr.ispark.common.security.service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserVO extends CommonVO {
	private static final long serialVersionUID = 6985078694123877792L;

	private String userId;
	private String userNm;
	private String deptId;
	private String deptNm;
	private String scDeptId;
	private String scDeptNm;
	private String jikgubId;
	private String jikgubNm;
	private String posId;
	private String posNm;
	private String jobId;
	private String jobNm;
	private String passwd;
	private String email;
	private String evalerStep;
	
	@JsonIgnore private String alertPwChangeYn;	// 패스워드 변경 알림 여부
	@JsonIgnore private String pwChangeCycle;	// 패스워드 변경 주기
	@JsonIgnore private String showPopNotice = "Y";	// 팝업 공지사항 표시 여부

	private String loginFlag;
	private String ip;

	@JsonIgnore private String findJikgubId;
	@JsonIgnore private String findPosId;

	private List<String> adminGubunList;
	private String connectionId;

	private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap;
}
