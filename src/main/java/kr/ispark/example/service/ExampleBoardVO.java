/*************************************************************************
* CLASS 명	: ExampleBoardVO
* 작 업 자	: kimyh
* 작 업 일	: 2017. 12. 22.
* 기	능	: 파일첨부 예제 VO
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2017. 12. 22.		최 초 작 업
**************************************************************************/
package kr.ispark.example.service;

import java.util.List;

import kr.ispark.common.CommonVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExampleBoardVO extends CommonVO {
	private static final long serialVersionUID = -7059455004697348240L;
	private String id;	// 게시물 ID
	private String subject;	// 제목
	private String content;	// 내용
	private String userId;		// 작성자 ID
	private String userNm;		// 작성자명
	private String atchFileId;		// 첨부파일 ID
	private String atchFileId2;		// 첨부파일2 ID
	
	private List<String> chkAttachFiles2;
}
