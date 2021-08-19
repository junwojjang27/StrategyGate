package kr.ispark.system.system.board.service;

import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardVO extends CommonVO {
	private static final long serialVersionUID = 945273040008132492L;
	
	private String boardId;
	private String boardNm;
	private int seq = -1;
	@JsonIgnore private int upSeq = -1;
	private int groupSeq = -1;
	private int groupOrder = 0;
	private int groupLevel = 0;
	private String title;
	private String contents;
	private String insertUserId;
	@JsonIgnore private String insertUserIp;
	@JsonIgnore private String upInsertUserId;			// 최상위글 작성자
	private int hit = 0;
	private int replyCnt = 0;
	private int commentCnt = 0;
	private String createDt;
	
	private int commentSeq = 0;
	private String deptNm;

	@JsonIgnore private String useAtchFileYn = "N";		// 파일첨부 가능 여부
	@JsonIgnore private int maxUploadCnt = 1;			// 최대 업로드 수
	@JsonIgnore private long maxUploadSize = 5242880;	// 최대 업로드 용량 (5MB)
	@JsonIgnore private String useCommentYn = "N";		// 댓글 사용 여부
	@JsonIgnore private String useReplyYn = "N";		// 답글 사용 여부
	@JsonIgnore private String privateYn = "N";			// 본인글만 노출 여부
	@JsonIgnore private String writeAuth = "01";		// 글 작성 권한 : ","로 권한 구분
	@JsonIgnore private String replyAuth = "01";		// 답글 작성 권한 : ","로 권한 구분
	@JsonIgnore private int duplLimitMin = 1;			// 중복 게시 방지 시간(분)
	
	private boolean isReadable = true;			// 글 조회 가능 여부
	private boolean isReplyWritable = false;	// 답글 작성 가능 여부
	@JsonIgnore private boolean isWritable = false;			// 글 작성 가능 여부
	@JsonIgnore private boolean isEditable = false;			// 글 수정 가능 여부
	@JsonIgnore private boolean isDeletable = false;		// 글 삭제 가능 여부
	
	public List<String> getWriteAuthList() {
		return Arrays.asList(writeAuth.split(","));
	}
	
	public List<String> getReplyAuthList() {
		return Arrays.asList(replyAuth.split(","));
	}
}
