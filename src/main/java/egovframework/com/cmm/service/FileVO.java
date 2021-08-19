/**
 * @Class Name : FileVO.java
 * @Description : 파일정보 처리를 위한 VO 클래스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 25.     이삼섭
 *	  2017.12.21		kimyh		변수 추가, extends 추가
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 25.
 * @version
 * @see
 *
 */
package egovframework.com.cmm.service;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

import kr.ispark.common.CommonVO;
import kr.ispark.common.secure.service.SecureService;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FileVO extends CommonVO {
	/**
	 *	serialVersion UID
	 */
	private static final long serialVersionUID = -287950405903719128L;

	@JsonIgnore private String isPublic = "N";

	/**
	 * 첨부파일 아이디
	 */
	public String atchFileId = "";

	// 복사할 첨부파일 ID
	@JsonIgnore public String newAtchFileId = "";

	/**
	 * 생성일자
	 */
	public String creatDt = "";

	/**
	 * 파일내용
	 */
	public String fileCn = "";
	/**
	 * 파일확장자
	 */
	public String fileExtsn = "";

	/**
	 * 파일크기
	 */
	public long fileSize = 0;

	/**
	 * 파일연번
	 */
	public String fileSn = "";
	/**
	 * 파일저장경로
	 */
	public String fileStreCours = "";

	/**
	 * 원파일명
	 */
	public String orignlFileNm = "";

	/**
	 * 저장파일명
	 */
	public String streFileNm = "";

	/**
	 * 폼에서 넘어온 input form명칭
	 */
	public String formNm = "";

	/**
	 * 파일휴지통ID
	 */
	public String fileTrashId = "";

	/**
     * 파일크기
     */
    public String fileMg = "";

	@JsonIgnore private String searchCnd = "";

	/**
	 * toString 메소드를 대치한다.
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	// 암호화된 atchFileId
	public String getEncAtchFileId() {
		try {
			return SecureService.encryptStr(atchFileId);
		} catch (NullPointerException npe) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}
