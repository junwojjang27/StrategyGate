package kr.ispark.bsc.mon.common.metricComment.service;

import org.codehaus.jackson.annotate.JsonIgnore;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MetricCommentVO extends CommonVO {
	private static final long serialVersionUID = -2574845875731384480L;
	
	private String metricId;
	private Integer seq = -1;	// seq가 null로 전달되는 경우에 대한 처리
	
	@JsonIgnore
	private int upSeq = -1;
	private int groupSeq = -1;
	private int groupOrder = 0;
	private int groupLevel = 0;
	private int replyCnt = 0;
	private String contents;
	@JsonIgnore
	private String editContents;	// 수정용
	private String insertUserId;
	private String insertUserIp;
	
	public int getSeq() {
		if(seq == null) {
			return -1;
		} else {
			return seq;
		}
	}
}
