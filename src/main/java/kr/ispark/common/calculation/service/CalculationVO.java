package kr.ispark.common.calculation.service;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CalculationVO  extends CommonVO {

	private static final long serialVersionUID = -410028088542907493L;

	private String seq;
	private String finalSeq;
	private String mon;
	private String analCycle;
	private String metricId;
	private String approveYn;
	private int resultCnt;
	private String successYn;
	private String successFinalYn;
	private String exceptionText;
	private String execUserId;
	private String procId;

	private String calType;
	private String a;
	private String b;
	private String c;
	private String d;
	private String e;
	private String f;
	private String g;
	private String h;
	private String i;
	private String j;
	private String k;
	private String l;
	private String m;
	private String n;
	private String o;
	private String p;
	private String q;
	private String r;
	private String s;
	private String t;
	private String u;
	private String v;
	private String w;
	private String x;
	private String y;
	private String z;

	private String calTypeActual;

	private String actual;
	private String target;
	private String calTypeScore;
	private String calTypeValue;

	private String kpiApproveUseYn;
	private String actApproveUseYn;

	private String mainMetricId;
	private String mainMetricNm;
	private String mainActYn;
	private String calActYn;
	private String calMetricId;
	private String calMetricNm;

	private String calVal;
	private String calGubun;
	private String calTypeCol;

	private String insertUserId;
	private String insertUserNm;

}
