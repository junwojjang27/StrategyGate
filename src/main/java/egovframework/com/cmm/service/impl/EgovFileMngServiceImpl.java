package egovframework.com.cmm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.utl.sim.service.EgovFileTool;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;

/**
 * @Class Name : EgovFileMngServiceImpl.java
 * @Description : 파일정보의 관리를 위한 구현 클래스
 * @Modification Information
 *
 *	수정일	   수정자		 수정내용
 *	-------		-------	 -------------------
 *	2009. 3. 25.	 이삼섭	최초생성
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 25.
 * @version
 * @see
 *
 */
@Service("EgovFileMngService")
public class EgovFileMngServiceImpl extends EgovAbstractServiceImpl implements EgovFileMngService {

	@Resource(name = "FileManageDAO")
	private FileManageDAO fileMngDAO;

	@Autowired
	private IdGenServiceImpl idgenService;
	
	/**
	 * 여러 개의 파일을 삭제한다.
	 *
	 * @see egovframework.com.cmm.service.EgovFileMngService#deleteFileInfs(java.util.List)
	 */
	@Override
	public void deleteFileInfs(List<?> fvoList) throws Exception {
		fileMngDAO.deleteFileInfs(fvoList);
	}

	/**
	 * 하나의 파일에 대한 정보(속성 및 상세)를 등록한다.
	 *
	 * @see egovframework.com.cmm.service.EgovFileMngService#insertFileInf(egovframework.com.cmm.service.FileVO)
	 */
	@Override
	public String insertFileInf(FileVO fvo) throws Exception {
		String atchFileId = fvo.getAtchFileId();
	
		fileMngDAO.insertFileInf(fvo);
	
		return atchFileId;
	}

	/**
	 * 여러 개의 파일에 대한 정보(속성 및 상세)를 등록한다.
	 *
	 * @see egovframework.com.cmm.service.EgovFileMngService#insertFileInfs(java.util.List)
	 */
	@Override
	public String insertFileInfs(List<?> fvoList) throws Exception {
		String atchFileId = "";
	
		if (fvoList.size() != 0) {
			atchFileId = fileMngDAO.insertFileInfs(fvoList);
		}
		if(atchFileId == ""){
			atchFileId = null;
		}
		return atchFileId;
	}

	/**
	 * 파일에 대한 목록을 조회한다.
	 *
	 * @see egovframework.com.cmm.service.EgovFileMngService#selectFileInfs(egovframework.com.cmm.service.FileVO)
	 */
	@Override
	public List<FileVO> selectFileInfs(FileVO fvo) throws Exception {
		return fileMngDAO.selectFileInfs(fvo);
	}

	/**
	 * 여러 개의 파일에 대한 정보(속성 및 상세)를 수정한다.
	 *
	 * @see egovframework.com.cmm.service.EgovFileMngService#updateFileInfs(java.util.List)
	 */
	@Override
	public void updateFileInfs(List<?> fvoList) throws Exception {
		//Delete & Insert
		fileMngDAO.updateFileInfs(fvoList);
	}

	/**
	 * 하나의 파일을 삭제한다.
	 *
	 * @see egovframework.com.cmm.service.EgovFileMngService#deleteFileInf(egovframework.com.cmm.service.FileVO)
	 */
	@Override
	public void deleteFileInf(FileVO fvo) throws Exception {
		fileMngDAO.deleteFileInf(fvo);
	}

	/**
	 * 파일에 대한 상세정보를 조회한다.
	 *
	 * @see egovframework.com.cmm.service.EgovFileMngService#selectFileInf(egovframework.com.cmm.service.FileVO)
	 */
	@Override
	public FileVO selectFileInf(FileVO fvo) throws Exception {
		return fileMngDAO.selectFileInf(fvo);
	}

	/**
	 * 파일 구분자에 대한 최대값을 구한다.
	 *
	 * @see egovframework.com.cmm.service.EgovFileMngService#getMaxFileSN(egovframework.com.cmm.service.FileVO)
	 */
	@Override
	public int getMaxFileSN(FileVO fvo) throws Exception {
		return fileMngDAO.getMaxFileSN(fvo);
	}

	/**
	 * 전체 파일을 삭제한다.
	 *
	 * @see egovframework.com.cmm.service.EgovFileMngService#deleteAllFileInf(egovframework.com.cmm.service.FileVO)
	 */
	@Override
	public void deleteAllFileInf(FileVO fvo) throws Exception {
		fileMngDAO.deleteAllFileInf(fvo);
	}

	/**
	 * 파일명 검색에 대한 목록을 조회한다.
	 *
	 * @see egovframework.com.cmm.service.EgovFileMngService#selectFileListByFileNm(egovframework.com.cmm.service.FileVO)
	 */
	@Override
	public Map<String, Object> selectFileListByFileNm(FileVO fvo) throws Exception {
		List<FileVO> result = fileMngDAO.selectFileListByFileNm(fvo);
		int cnt = fileMngDAO.selectFileListCntByFileNm(fvo);
	
		Map<String, Object> map = new HashMap<String, Object>();
	
		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));
	
		return map;
	}

	/**
	 * 이미지 파일에 대한 목록을 조회한다.
	 *
	 * @see egovframework.com.cmm.service.EgovFileMngService#selectImageFileList(egovframework.com.cmm.service.FileVO)
	 */
	@Override
	public List<FileVO> selectImageFileList(FileVO vo) throws Exception {
		return fileMngDAO.selectImageFileList(vo);
	}
	
	/**
	 * 파일 삭제를 위한 휴지통 ID발급처리
	 * @see egovframework.com.cmm.service.EgovFileMngService#toTrash(java.lang.String, java.lang.String)
	 */
	public String updateFileTrash(FileVO vo) throws Exception {
		//휴지통 ID가 입력되었는지 확인하여 입력되지 않았다면 휴지통 ID를 생성한다.
		String fileTrashId = vo.getFileTrashId();
		if ("".equals(fileTrashId) || fileTrashId == null) {
			fileTrashId = idgenService.selectNextSeq("FILE_TRASH", "TRASH_", 14, "0");
			vo.setFileTrashId(fileTrashId);
		}
		fileMngDAO.updateFileTrash(vo);

		return fileTrashId;
	}

	/**
	 * 휴지통에 있는 파일 삭제
	 * @see egovframework.com.cmm.service.EgovFileMngService#cleanTrash(egovframework.com.cmm.service.FileVO)
	 */
	public void deleteFileTrash(FileVO fvo) throws Exception {
		//휴지통 ID에 해당하는 파일목록을 가져온다.
		List<FileVO> fileList = fileMngDAO.selectFileInfsByFileTrash(fvo);

		// 파일 삭제
		if(fileList.size()>0){
			fileMngDAO.deleteFileInfsByFileTrash(fvo);

			//물리적 파일 삭제
			for(FileVO fileVO : fileList){
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
		}

	}

	/**
	 * 입력받은 FILE_TRASH_ID를 NULL로 바꾼다.
	 */
	public void updateFileTrashRestore(FileVO fvo) throws Exception {
		//휴지통 trash 정보 초기화
		fileMngDAO.updateFileTrashRestore(fvo);
	}

	/**
	 * atchFileId로 DB에서 첨부파일 정보를 삭제
	 * DB 작업과 파일 삭제 작업을 분리하기 위해 삭제할 파일들의 List를 리턴함
	 * @see egovframework.com.cmm.service.EgovFileMngService#deleteFileInfs(com.lexken.bsc.common.BscDefaultVO)
	 */
	public List<FileVO> deleteFileInfsAndDiskByAtchFileId(String atchFileId) throws Exception {
		if(atchFileId == null || "".equals(atchFileId)) return null;

		FileVO fvo = new FileVO();
		fvo.setAtchFileId(atchFileId);
		List<FileVO> fileList = fileMngDAO.selectFileInfs(fvo);

		// deleteCOMTNFILE 을 실행함 다만 실제 delete가 아닌 상태값 update임
		deleteAllFileInf(fvo);

		// 데이터 삭제
		if(fileList.size() > 0) {
			// 데이터 delete
			fileMngDAO.deleteFileInfsByAtchFileId(fvo);
		}

		return fileList;
	}

	/**
	 * 입력받은 atchFileId와 chkAttachFiles[]를 참조하여 파일목록을 가져와서 첨부파일 정보와 물리적 파일을 삭제한다.
	 * @see egovframework.com.cmm.service.EgovFileMngService#deleteFileInfs(com.lexken.bsc.common.BscDefaultVO)
	 */
	public List<FileVO> deleteFileInfsAndDisk(FileVO fvo) throws Exception {
		// atchFileId와 delAttachFiles[]에 의한 파일 목록 조회
		if(fvo.getChkAttachFiles() == null) return null;
		List<FileVO> fileList = fileMngDAO.selectFileInfsByCheckbox(fvo);

		// 데이터 삭제
		if(fileList.size() > 0) {
			fileMngDAO.deleteFileInfsByCheckbox(fvo);
		}

		return fileList;
	}
}
