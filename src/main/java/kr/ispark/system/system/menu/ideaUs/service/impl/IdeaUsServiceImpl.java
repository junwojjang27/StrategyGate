package kr.ispark.system.system.menu.ideaUs.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
import kr.ispark.bsc.system.system.notice.service.NoticeVO;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.util.CustomEgovFileMngUtil;
import kr.ispark.common.util.SessionUtil;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.menu.ideaUs.service.IdeaUsVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class IdeaUsServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;

	@Resource
	private IdeaUsDAO ideaUsDAO;

	@Resource(name="CustomEgovFileMngUtil")
	private CustomEgovFileMngUtil fileUtil;

	@Resource(name="EgovFileMngService")
	private EgovFileMngService fileMngService;

	/**
	 * 혁신 IDEA+ 목록 조회
	 * @param	IdeaUsVO searchVO
	 * @return	List<IdeaUsVO>
	 * @throws	Exception
	 */
	public List<IdeaUsVO> selectList(IdeaUsVO searchVO) throws Exception {
		return ideaUsDAO.selectList(searchVO);
	}

	/**
	 * 혁신 IDEA+ 상세 조회
	 * @param	IdeaUsVO searchVO
	 * @return	IdeaUsVO
	 * @throws	Exception
	 */
	public IdeaUsVO selectDetail(IdeaUsVO searchVO) throws Exception {
		return ideaUsDAO.selectDetail(searchVO);
	}

	/**
	 * 혁신 IDEA+ 정렬순서저장
	 * @param	IdeaUsVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(IdeaUsVO dataVO) throws Exception {
		int resultCnt = 0;
		for(IdeaUsVO paramVO : dataVO.getGridDataList()) {
			resultCnt += ideaUsDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	/**
	 * 혁신 IDEA+ 삭제
	 * @param	IdeaUsVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteIdeaUs(IdeaUsVO dataVO) throws Exception {
		return ideaUsDAO.deleteIdeaUs(dataVO);
	}

	/**
	 * 혁신 IDEA+ 저장
	 * @param	IdeaUsVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String insertData(IdeaUsVO dataVO, List<FileVO> fileList1) throws Exception {
		if(!CommonUtil.isEmpty(fileList1)) {
			fileMngService.insertFileInfs(fileList1);
		}
		dataVO.setAtchFileId(fileUtil.getAtchFileId(fileList1));
		dataVO.setIdeaCd(idgenService.selectNextSeq("IDEA_INFO", "S", 6, "0"));

		dataVO.setState("001");
		dataVO.setIdeaGbnCd("002");

		return ideaUsDAO.insertData(dataVO);
	}

	/**
	 * 혁신제안 수정
	 * - 처리순서
	 * 	1) 업로드한 파일 정보를 DB에 저장
	 * 	2) 게시물 정보 update
	 *  3) 삭제 체크한 파일이 있으면 DB에서 delete하고 물리적으로 삭제할 파일 목록 생성
	 *  4) 삭제할 파일 목록을 리턴 (controller에서 삭제 처리)
	 */
	public List<FileVO> updateData(IdeaUsVO dataVO, List<FileVO> fileList1) throws Exception {
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
		ideaUsDAO.updateData(dataVO);

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

}
