package kr.ispark.common;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import egovframework.com.cmm.ComDefaultVO;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter
@JsonIgnoreProperties({
	"lang",				"findYear",		"findMon",		"findAnalCycle",
	"findScDeptId",		"findUseYn",	"findEvalMethodId",	"findMonitoringRootScDeptId",
	"_csrf",			"layout",		"keys",
	"modifyDt",			"deleteDt",		"atchFileId",	"chkAttachFiles",
	"chkAttachFiles2",	"doneCallbackFunc",	"failCallbackFunc",	"alwaysCallbackFunc",	"startRow",
	"endRow",
	"_id_",	"page",	"rows",	"sidx",	"sord",
	"gridData", "gridDataList",

	"searchCondition", "searchKeyword", "searchUseYn", "pageIndex", "pageUnit",
	"pageSize",	"firstIndex",	"lastIndex",	"recordCountPerPage",	"searchKeywordFrom",
	"searchKeywordTo",

	"decimal_SCALE",	"decimal_ROUND_MODE",	"decimal_TYPE",
	"sendMailProcessUrl",	"userNmList",	"userIdList"
})	// json 데이터 생성시 제외할 변수명
public class CommonVO extends ComDefaultVO {
	private static final long serialVersionUID = 924024280774492177L;

	private String dbId;
	private String masterDbId;
	private String targetDbId;
	private String paramCompId;
	private String userId;
	private String userNm;
	private String lang;
	private String year;
	private String useYn;
	private String isNew = "N";	// 신규 데이터 여부

	// 검색조건들
	private String findYear;
	private String findMon;
	private String findAnalCycle;
	private String findScDeptId;
	private String findUseYn;
	private String findJbGrade;
	private String colName;//검색조건들(회원)
	private String findCircle;
	// 모니터링 권한 제한시 최상위 scDeptId
	private String findMonitoringRootScDeptId;
	private String findEvalMethodId;

	private String _csrf;
	private String layout = "simple";
	private List<String> keys;	// 일괄 삭제용
	private String createDt;
	private String modifyDt;
	private String deleteDt;
	private String atchFileId;
	private String sortOrder;
	private String dbInfoText;
	private String dbUserId;
	private String dbUserPasswd;
	private String dbDriver;
	private String dbUrl;
	
	private List<CommonVO> dbList;

	/*
	 * 첨부파일 삭제용.
	 * 첨부파일란이 세 개 이상일 경우 각 VO에 chkAttachFiles3, chkAttachFiles4 등을 추가해줘야 함.
	 */
	private List<String> chkAttachFiles;
	private List<String> chkAttachFiles2;

	// 첨부파일 처리후 콜백 함수명 관련 변수
	private String doneCallbackFunc;
	private String failCallbackFunc;
	private String alwaysCallbackFunc;

	// jqgrid 관련
	private String _id_;	// jqgrid의 rowId
	private int page = 1;
	private int rows = 20;
	private String sidx;	// 정렬 컬럼명
	private String sord;	// 정렬 순서
	private String gridData;

	// 메일 발송 관련
	@JsonIgnore private String title;
	@JsonIgnore private String contents;
	private String sendMailProcessUrl = "/common/sendMail.do";	// 메일 발송을 처리할 url. 메일 발송을 별도로 처리해야할 경우 해당 url을 지정하면 됨
	private List<String> userNmList;
	private List<String> userIdList;

	// 소수점 자릿수, 반올림
	protected final int DECIMAL_SCALE = 2;
	protected final int DECIMAL_ROUND_MODE = BigDecimal.ROUND_HALF_UP;
	protected final String DECIMAL_TYPE = "###,###,###,###,##0.00";

	// BigDecimal 값의 소수점 처리
	public BigDecimal getValue(BigDecimal value) {
		return getValue(value, false);
	}
	public BigDecimal getValue(BigDecimal value, boolean removeTrailingZeros) {
		if(value == null) return null;

		if(removeTrailingZeros) {
			value = new BigDecimal(value.setScale(DECIMAL_SCALE, DECIMAL_ROUND_MODE).stripTrailingZeros().toPlainString());
			return value;
		} else {
			return value.setScale(DECIMAL_SCALE, DECIMAL_ROUND_MODE);
		}
	}

	// BigDecimal 값의 소수점 처리
	public String getCommaValue(String value) {
		return getCommaValue(value, false);
	}
	public String getCommaValue(String value, boolean removeTrailingZeros) {
		if(value == null) return null;

		BigDecimal  b = new BigDecimal(value);

		if(removeTrailingZeros) {
			return String.format("%,.2f", b.setScale(DECIMAL_SCALE, DECIMAL_ROUND_MODE).stripTrailingZeros().toPlainString());
		} else {
			return String.format("%,.2f", b.setScale(DECIMAL_SCALE, DECIMAL_ROUND_MODE));
		}
	}

	public int getStartRow(){
		return (page-1)*rows+1;
	}

	public int getEndRow(){
		return page*rows;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getLayout() {
		if("main".equals(layout) ||
				"popup".equals(layout) ||
				"simple".equals(layout) ||
				"monitor".equals(layout)
				){
		return layout;
		}
		return "main";
	}
	
	public String getYear() {
		if(year != null){
			if(Pattern.matches("^[0-9]{4}$", year)){
				return year;
			}else{
				return null;
			}
		}else{
			return year;
		}
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	public String getFindYear() {
		if(findYear != null){
			if(Pattern.matches("^[0-9]{4}$", findYear)){
				return findYear;
			}else{
				return null;
			}
		}else{
			return findYear;
		}
	}
	public void setFindYear(String findYear) {
		this.findYear = findYear;
	}
}
