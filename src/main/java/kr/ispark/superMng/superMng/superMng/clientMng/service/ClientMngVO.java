package kr.ispark.superMng.superMng.superMng.clientMng.service;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClientMngVO extends CommonVO {
	private static final long serialVersionUID = -5691904252643683375L;
	
	private String compId;

	private String newCompId;

	private String oldCompId;

	private String compNm;

	private String bizNo;

	private String ceoNm;

	private String bizCondition;

	private String bizType;

	private String compTel;

	private String compFax;

	private String country;

	private String compLang;

	private String compAddr;

	private String chargeNm;

	private String chargeTel;

	private String chargeEmail;

	private String memo;

	private String contractStatus;

	private String useYn;

	private String contractDt;

	private String serviceStartDt;

	private String serviceEndDt;

	private String serviceDt;

	private String payType;

	private String serviceType;

	private List<String> serviceTypes;
	private List<String> menuServiceTypes;
	private List<String> codeServiceTypes;

	private String payInfo;

	private String renewYn;

	private String theme;
	
	private String commYearBatchYn;

	private String logo;

	private String countryCode;

	private String countryName;

	private String lang;

	private String langNm;

	private String newLang;

	private String[] langChk;

	private String serviceStatus;

	private String langUseYn;

	private String monitoringYn;

	private String seq;

	private String currentSeq;

	private String hasDefaultDataYn;
	
	private String targetDbId;
	
	private String serviceTypeNm;
	
	private String tableCnt;
	
	private String copyTargetCompId;
	private String copyTargetDbId;
	
	private String tableName;
	
	@JsonIgnore private String pwChangeCycle;	// 패스워드 변경 주기
	@JsonIgnore private String useMonitoringYn;

	@JsonIgnore private String isSuper = "N";
	@JsonIgnore private String adminPassword;
	@JsonIgnore private String templateId;
	@JsonIgnore private String defaultDeptId;
	@JsonIgnore private String maxYear;

	@JsonIgnore private String searchCondition;
	@JsonIgnore private String findUseYn;
	@JsonIgnore private String findServiceStatus;
	@JsonIgnore private String findPayStatus;
	
	@JsonIgnore private String dbDriver;
	@JsonIgnore private String dbUrl;
	@JsonIgnore private String connectionId;
	@JsonIgnore private String dbId;
	@JsonIgnore private String dbUserId;
	@JsonIgnore private String dbUserPasswd;
}
