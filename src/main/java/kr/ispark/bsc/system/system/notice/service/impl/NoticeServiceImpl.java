/*************************************************************************
* CLASS 명	: NoticeServiceIpml
* 작 업 자	: 박정현
* 작 업 일	: 2018-03-29
* 기	능	: 공지사항 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-03-29
**************************************************************************/
package kr.ispark.bsc.system.system.notice.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.bsc.system.system.notice.service.NoticeVO;
import kr.ispark.common.CommonVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.CustomEgovFileMngUtil;

@Service
public class NoticeServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;

	@Resource
	private NoticeDAO noticeDAO;

	@Resource(name="CustomEgovFileMngUtil")
	private CustomEgovFileMngUtil fileUtil;

	@Resource(name="EgovFileMngService")
	private EgovFileMngService fileMngService;
	/**
	 * 공지사항 목록 조회
	 * @param	NoticeVO searchVO
	 * @return	List<NoticeVO>
	 * @throws	Exception
	 */
	public List<NoticeVO> selectList(NoticeVO searchVO) throws Exception {
		return noticeDAO.selectList(searchVO);
	}

	/**
	 * 공지사항 상세 조회
	 * @param	NoticeVO searchVO
	 * @return	NoticeVO
	 * @throws	Exception
	 */
	public NoticeVO selectDetail(NoticeVO searchVO) throws Exception {
		return noticeDAO.selectDetail(searchVO);
	}

	/**
	 * 공지사항 삭제
	 * @param	NoticeVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public List<FileVO> deleteNotice(NoticeVO dataVO) throws Exception {
		// 삭제할 파일ID 정보
		List<String> fileIdList = noticeDAO.selectAtchFileIdListForDelete(dataVO);
		List<FileVO> fileList = new ArrayList<FileVO>();

		// 게시물 삭제
		noticeDAO.deleteNotice(dataVO);

		// DB에서 파일 정보 삭제 & 물리적으로 삭제할 파일 목록 병합
		if(!CommonUtil.isEmpty(fileIdList)) {
			for(String atchFileId : fileIdList) {
				fileList.addAll(fileMngService.deleteFileInfsAndDiskByAtchFileId(atchFileId));
			}
		}

		return fileList;
	}

	/**
	 * 공지사항 저장
	 * @param	NoticeVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String insertData(NoticeVO dataVO, List<FileVO> fileList1) throws Exception {
		if(!CommonUtil.isEmpty(fileList1)) {
			fileMngService.insertFileInfs(fileList1);
		}
		dataVO.setAtchFileId(fileUtil.getAtchFileId(fileList1));
		dataVO.setId(idgenService.selectNextSeq("COM_NOTICE", 0));

		return noticeDAO.insertData(dataVO);
	}

	/**
	 * 게시판 수정
	 * - 처리순서
	 * 	1) 업로드한 파일 정보를 DB에 저장
	 * 	2) 게시물 정보 update
	 *  3) 삭제 체크한 파일이 있으면 DB에서 delete하고 물리적으로 삭제할 파일 목록 생성
	 *  4) 삭제할 파일 목록을 리턴 (controller에서 삭제 처리)
	 */
	public List<FileVO> updateData(NoticeVO dataVO, List<FileVO> fileList1) throws Exception {
		// 업로드한 파일이 있으면
		if(!CommonUtil.isEmpty(fileList1)) {
			// 새로 업로드 한 경우에는 atchFileId 새로 채번
			if(CommonUtil.isEmpty(dataVO.getAtchFileId())) {
				dataVO.setAtchFileId(fileMngService.insertFileInfs(fileList1));
			} else {
				// 추가로 업로드 한 경우
				fileMngService.updateFileInfs(fileList1);
			}
		}

		// 게시물 정보 update
		noticeDAO.updateData(dataVO);

		List<FileVO> deleteFileList = new ArrayList<FileVO>();
		// 삭제 체크한 파일들 DB에서 삭제
		FileVO fvo = new FileVO();
		if(!CommonUtil.isEmpty(dataVO.getChkAttachFiles())) {
			fvo.setAtchFileId(dataVO.getAtchFileId());
			fvo.setChkAttachFiles(dataVO.getChkAttachFiles());
			deleteFileList.addAll(fileMngService.deleteFileInfsAndDisk(fvo));
		}

		// 삭제할 파일 목록을 controller에서 삭제 처리 (DB transaction 이후)
		return deleteFileList;
	}

	/**
	 * 공지사항 목록 조회 (로그인 화면용)
	 * @param	NoticeVO searchVO
	 * @return	List<NoticeVO>
	 * @throws	Exception
	 */
	public List<NoticeVO> selectListForAll(CommonVO searchVO) throws Exception {
		return noticeDAO.selectListForAll(searchVO);
	}

	/**
	 * 전체 공지사항 상세 조회 (로그인 화면용)
	 * @param	NoticeVO searchVO
	 * @return	NoticeVO
	 * @throws	Exception
	 */
	public NoticeVO selectDetailForAll(NoticeVO searchVO) throws Exception {
		return noticeDAO.selectDetailForAll(searchVO);
	}

	/**
	 * 팝업 공지사항 목록 조회
	 * @param	CommonVO searchVO
	 * @return	List<NoticeVO>
	 * @throws	Exception
	 */
	public List<NoticeVO> selectPopNoticeList(CommonVO searchVO) throws Exception {
		return noticeDAO.selectPopNoticeList(searchVO);
	}
}
