/*************************************************************************
* CLASS 명	: ExampleBoardServiceImpl
* 작 업 자	: kimyh
* 작 업 일	: 2017. 12. 26.
* 기	능	: 게시판 (파일 첨부) 예제용 Service
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2017. 12. 26.		최 초 작 업
**************************************************************************/

package kr.ispark.example.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.CustomEgovFileMngUtil;
import kr.ispark.example.service.ExampleBoardVO;

@Service
public class ExampleBoardServiceImpl extends EgovAbstractServiceImpl {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IdGenServiceImpl idgenService;
	
	@Resource(name="CustomEgovFileMngUtil")
	private CustomEgovFileMngUtil fileUtil;
	
	@Resource(name="EgovFileMngService")
	private EgovFileMngService fileMngService;
	
	@Resource
	private ExampleBoardDAO exampleBoardDAO;
	
	/**
	 * 게시판 목록 조회
	 * @param	ExampleBoardVO searchVO
	 * @return	List<ExampleBoardVO>
	 * @throws	Exception
	 */
	public List<ExampleBoardVO> selectList(ExampleBoardVO searchVO) throws Exception {
		return exampleBoardDAO.selectList(searchVO);
	}

	/**
	 * 게시판 목록수 조회
	 */
	public int selectListCount(ExampleBoardVO searchVO) throws Exception {
		return exampleBoardDAO.selectListCount(searchVO);
	}
	
	/**
	 * 게시물 조회
	 */
	public ExampleBoardVO selectBoard(ExampleBoardVO searchVO) throws Exception {
		return exampleBoardDAO.selectBoard(searchVO);
	}
	
	/**
	 * 게시판 등록
	 */
	public String insertData(ExampleBoardVO dataVO, List<FileVO> fileList1, List<FileVO> fileList2) throws Exception {
		if(!CommonUtil.isEmpty(fileList1)) {
    		fileMngService.insertFileInfs(fileList1);
    	}
		if(!CommonUtil.isEmpty(fileList2)) {
    		fileMngService.insertFileInfs(fileList2);
    	}
    	dataVO.setAtchFileId(fileUtil.getAtchFileId(fileList1));
		dataVO.setAtchFileId2(fileUtil.getAtchFileId(fileList2));
		dataVO.setId(idgenService.selectNextSeq("EXAMPLE_BOARD", 7, null));
		
		return exampleBoardDAO.insertData(dataVO);
	}

	/**
	 * 게시판 수정
	 * - 처리순서
	 * 	1) 업로드한 파일 정보를 DB에 저장
	 * 	2) 게시물 정보 update
	 *  3) 삭제 체크한 파일이 있으면 DB에서 delete하고 물리적으로 삭제할 파일 목록 생성
	 *  4) 삭제할 파일 목록을 리턴 (controller에서 삭제 처리)
	 */
	public List<FileVO> updateData(ExampleBoardVO dataVO, List<FileVO> fileList1, List<FileVO> fileList2) throws Exception {
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
		
		// 첨부파일2에 업로드한 파일이 있으면
		if(!CommonUtil.isEmpty(fileList2)) {
			if(CommonUtil.isEmpty(dataVO.getAtchFileId2())) {
				dataVO.setAtchFileId2(fileMngService.insertFileInfs(fileList2));
			} else {
				fileMngService.updateFileInfs(fileList2);
			}
    	}
		
		// 게시물 정보 update
		exampleBoardDAO.updateData(dataVO);
		
		List<FileVO> deleteFileList = new ArrayList<FileVO>();
		// 삭제 체크한 파일들 DB에서 삭제
		FileVO fvo = new FileVO();
		if(!CommonUtil.isEmpty(dataVO.getChkAttachFiles())) {
			fvo.setAtchFileId(dataVO.getAtchFileId());
			fvo.setChkAttachFiles(dataVO.getChkAttachFiles());
			deleteFileList.addAll(fileMngService.deleteFileInfsAndDisk(fvo));
		}
		// 첨부파일2 처리
		if(!CommonUtil.isEmpty(dataVO.getChkAttachFiles2())) {
			fvo.setAtchFileId(dataVO.getAtchFileId2());
			fvo.setChkAttachFiles(dataVO.getChkAttachFiles2());
			deleteFileList.addAll(fileMngService.deleteFileInfsAndDisk(fvo));
		}
		
		// 삭제할 파일 목록을 controller에서 삭제 처리 (DB transaction 이후)
		return deleteFileList;
	}
	
	/**
	 * 게시판 삭제
	 */
	public List<FileVO> deleteData(ExampleBoardVO dataVO) throws Exception {
		// 삭제할 파일ID 정보
		List<String> fileIdList = exampleBoardDAO.selectAtchFileIdListForDelete(dataVO);
		List<FileVO> fileList = new ArrayList<FileVO>();
		
		// 게시물 삭제
		exampleBoardDAO.deleteData(dataVO);
		
		// DB에서 파일 정보 삭제 & 물리적으로 삭제할 파일 목록 병합
		if(!CommonUtil.isEmpty(fileIdList)) {
			for(String atchFileId : fileIdList) {
				fileList.addAll(fileMngService.deleteFileInfsAndDiskByAtchFileId(atchFileId));
			}
		}
		
		return fileList;
	}
}
