package egovframework.com.cmm.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.FileVO;
import egovframework.rte.psl.dataaccess.EgovAbstractMapper;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.PropertyUtil;

/**
 * @Class Name : EgovFileMngDAO.java
 * @Description : 파일정보 관리를 위한 데이터 처리 클래스
 * @Modification Information
 *
 *	수정일			수정자		수정내용
 *	-------			-------	-------------------
 *	2009. 3. 25.	이삼섭	최초생성
 *	2017.12. 21.	kimyh	기능 추가
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 25.
 * @version
 * @see
 *
 */
@Repository("FileManageDAO")
public class FileManageDAO extends EgovAbstractMapper {

	/**
	* 여러 개의 파일에 대한 정보(속성 및 상세)를 등록한다.
	*
	* @param fileList
	* @return
	* @throws Exception
	*/
	public String insertFileInfs(List<?> fileList) throws Exception {
		FileVO vo = (FileVO) fileList.get(0);
		String atchFileId = vo.getAtchFileId();

		insert("file.insertFileMaster", vo);

		Iterator<?> iter = fileList.iterator();
		while (iter.hasNext()) {
			vo = (FileVO) iter.next();

			insert("file.insertFileDetail", vo);
		}

		return atchFileId;
	}

	/**
	* 하나의 파일에 대한 정보(속성 및 상세)를 등록한다.
	*
	* @param vo
	* @throws Exception
	*/
	public void insertFileInf(FileVO vo) throws Exception {
		insert("file.insertFileMaster", vo);
		insert("file.insertFileDetail", vo);
	}

	/**
	* 여러 개의 파일에 대한 정보(속성 및 상세)를 수정한다.
	*
	* @param fileList
	* @throws Exception
	*/
	public void updateFileInfs(List<?> fileList) throws Exception {
		FileVO vo;
		Iterator<?> iter = fileList.iterator();
		while (iter.hasNext()) {
			vo = (FileVO) iter.next();

			insert("file.insertFileDetail", vo);
		}
	}

	/**
	* 여러 개의 파일을 삭제한다.
	*
	* @param fileList
	* @throws Exception
	*/
	public void deleteFileInfs(List<?> fileList) throws Exception {
		Iterator<?> iter = fileList.iterator();
		FileVO vo;
		while (iter.hasNext()) {
			vo = (FileVO) iter.next();

			delete("file.deleteFileDetail", vo);
		}
	}

	/**
	* 하나의 파일을 삭제한다.
	*
	* @param vo
	* @throws Exception
	*/
	public void deleteFileInf(FileVO vo) throws Exception {
		delete("file.deleteFileDetail", vo);
	}

	/**
	* 파일에 대한 목록을 조회한다.
	*
	* @param vo
	* @return
	* @throws Exception
	*/
	public List<FileVO> selectFileInfs(FileVO vo) throws Exception {
		
		return selectList("file.selectFileList", vo);
	}

	/**
	* 파일 구분자에 대한 최대값을 구한다.
	*
	* @param vo
	* @return
	* @throws Exception
	*/
	public int getMaxFileSN(FileVO vo) throws Exception {
		return (Integer) selectOne("file.getMaxFileSN", vo);
	}

	/**
	* 파일에 대한 상세정보를 조회한다.
	*
	* @param vo
	* @return
	* @throws Exception
	*/
	public FileVO selectFileInf(FileVO vo) throws Exception {
		
		return (FileVO)selectOne("file.selectFileInf", vo);
	}

	/**
	* 전체 파일을 삭제한다.
	*
	* @param vo
	* @throws Exception
	*/
	public void deleteAllFileInf(FileVO vo) throws Exception {
		update("file.deleteCOMTNFILE", vo);
	}

	/**
	* 파일명 검색에 대한 목록을 조회한다.
	*
	* @param vo
	* @return
	* @throws Exception
	*/
	public List<FileVO> selectFileListByFileNm(FileVO vo) throws Exception {
		return selectList("file.selectFileListByFileNm", vo);
	}

	/**
	* 파일명 검색에 대한 목록 전체 건수를 조회한다.
	*
	* @param vo
	* @return
	* @throws Exception
	*/
	public int selectFileListCntByFileNm(FileVO vo) throws Exception {
		return (Integer) selectOne("file.selectFileListCntByFileNm", vo);
	}

	/**
	* 이미지 파일에 대한 목록을 조회한다.
	*
	* @param vo
	* @return
	* @throws Exception
	*/
	public List<FileVO> selectImageFileList(FileVO vo) throws Exception {
		return selectList("file.selectImageFileList", vo);
	}

	public List<FileVO> selectFileInfsByCheckbox(FileVO vo) {
		return selectList("file.selectFileInfsByCheckbox", vo);
	}

	public void deleteFileInfsByAtchFileId(FileVO vo) {
		delete("file.deleteFileInfsByAtchFileId", vo);
	}

	public void deleteFileInfsByCheckbox(FileVO vo) {
		delete("file.deleteFileInfsByCheckbox", vo);
	}

	public void updateFileTrash(FileVO vo) {
		update("file.updateFileTrash", vo);
	}

	public List<FileVO> selectFileInfsByFileTrash(FileVO vo) throws Exception {
		return selectList("file.selectFileListByFileTrash", vo);
	}

	public void deleteFileInfsByFileTrash(FileVO vo) {
		delete("file.deleteFileInfsByFileTrash", vo);
	}

	public void updateFileTrashRestore(FileVO vo) {
		delete("file.updateFileTrashRestore", vo);
	}
}
