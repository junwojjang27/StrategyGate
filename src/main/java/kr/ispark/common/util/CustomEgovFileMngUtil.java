package kr.ispark.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.utl.sim.service.EgovFileTool;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;

/**
 * @Class Name	: CustomEgovFileMngUtil.java
 * @Description : 파일 처리 관련 유틸리티
 * @Modification Information
 *
 *	수정일		수정자					수정내용
 *	-------		--------	---------------------------
 *	2009.02.13	이삼섭		최초 생성
 *	2011.08.31	JJY			경량환경 템플릿 커스터마이징버전 생성
 *	2017.12.22	kimyh		다중 업로드용 메소드 추가
 *							소스 정리
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 02. 13
 * @version 1.0
 * @see
 *
 */
@Component("CustomEgovFileMngUtil")
public class CustomEgovFileMngUtil {

	public static final int BUFF_SIZE = 2048;

	@Autowired
	private IdGenServiceImpl idgenService;

	@Resource(name="EgovFileMngService")
	private EgovFileMngService fileMngService;

	@Resource(name="egovMessageSource")
	public EgovMessageSource egovMessageSource;

	private static final Logger logger = LoggerFactory.getLogger(CustomEgovFileMngUtil.class);

	/**
	 * 업로드 파일 유효성 체크
	 * @param	MultipartHttpServletRequest multiRequest
	 * @param	String inputFileName	form의 input name
	 * @param	String atchFileId		파일 id
	 * @param	Long maxTotalSize		최대 첨부 가능 용량, null인 경우 globals.properties의 fileUpload.defaultMaxSize 값 적용
	 * @param	Integer maxFileCnt		최대 첨부 파일 수, null인 경우 globals.properties의 fileUpload.defaultMaxCount 값 적용
	 * @param	String[] allowFileExts	허용 하는 확장자명(배열), null인 경우 globals.properties의 fileUpload.allowFileExts 값 적용
	 * @param	List<String> chkAttachFiles	삭제할 파일 sn 목록
	 * @return	오류 메시지 (오류가 없으면 null)
	 */
	public String validation(MultipartHttpServletRequest multiRequest, String inputFileName, String atchFileId, Long maxTotalSize, Integer maxFileCnt, String[] allowFileExts) {
		return validation(multiRequest, inputFileName, atchFileId, maxTotalSize, maxFileCnt, allowFileExts, null);
	}
	public String validation(MultipartHttpServletRequest multiRequest, String inputFileName, String atchFileId, Long maxTotalSize, Integer maxFileCnt, String[] allowFileExts, List<String> chkAttachFiles) {
		MultiValueMap<String, MultipartFile> fileMap = multiRequest.getMultiFileMap();
		if(maxTotalSize == null) {
			maxTotalSize = Long.valueOf(PropertyUtil.getProperty("fileUpload.defaultMaxSize"));
		}
		if(maxFileCnt == null) {
			maxFileCnt = Integer.valueOf(PropertyUtil.getProperty("fileUpload.defaultMaxCount"));
		}
		if(allowFileExts == null) {
			allowFileExts = PropertyUtil.getProperty("fileUpload.allowFileExts").split(",");
		}
		List<String> allowFileExtList = Arrays.asList(allowFileExts);
		// 허용 확장자를 전부 소문자로 변경
		for(int i=0, iLen=allowFileExtList.size(); i<iLen; i++) {
			allowFileExtList.set(i, allowFileExtList.get(i).toLowerCase());
		}

		int fileCnt = 0;
		int totalSize = 0;

		// 기존에 업로드된 파일들이 있으면 조회해서 파일수와 용량을 구함
		if(atchFileId != null) {
			FileVO fileVO = new FileVO();
			fileVO.setAtchFileId(atchFileId);
			try {
				List<FileVO> fileList = fileMngService.selectFileInfs(fileVO);
				for(FileVO file : fileList) {
					// 삭제할 파일은 카운트 하지 않음
					if(chkAttachFiles != null && chkAttachFiles.contains(file.getFileSn())) {
						continue;
					}
					fileCnt++;
					totalSize += file.getFileSize();
				}
			} catch (SQLException sqe) {
				sqe.getCause();
				return egovMessageSource.getMessage("fail.request.msg");
			} catch (Exception e) {
				e.getCause();
				return egovMessageSource.getMessage("fail.request.msg");
			}
		}

		// 업로드한 파일들 체크
		List<MultipartFile> fileList = fileMap.get(inputFileName);
		if(fileList != null) {
			for(MultipartFile file : fileList) {
				if(!file.isEmpty()) {
					String orginFileName = file.getOriginalFilename();

					// 파일명이 없는 경우 처리 (첨부가 되지 않은 input file type)
					if ("".equals(orginFileName)) {
						continue;
					}

					// 확장자 체크
					if(allowFileExtList != null && allowFileExtList.size() > 0) {
						int index = orginFileName.lastIndexOf(".");
						String fileExt = orginFileName.substring(index + 1).toLowerCase();
						if(!allowFileExtList.contains(fileExt)) {
							return egovMessageSource.getMessage("errors.nowAllowedFormat2");
						}
					}

					fileCnt++;
					totalSize += file.getSize();
				}
			}
		}

		if(fileCnt > maxFileCnt) {
			return egovMessageSource.getMessage("errors.exceededMaximumFiles", String.valueOf(maxFileCnt));
		}

		if(totalSize > maxTotalSize) {
			return egovMessageSource.getMessage("errors.exceededMaximumFileSize", String.valueOf(maxTotalSize));
		}

		return null;
	}

	/**
	 * 업로드한 파일들을 List<FileVO>로 변환 & file id 채번 & 파일 저장
	 * @param fileMap
	 * @param inputFileName
	 * @param fileNamePrefix
	 * @param atchFileId
	 * @return
	 * @throws Exception
	 */
	public List<FileVO> parseFileInf(MultipartHttpServletRequest multiRequest, String inputFileName, String fileNamePrefix, String atchFileId) throws Exception {
		MultiValueMap<String, MultipartFile> fileMap = multiRequest.getMultiFileMap();
		String atchFileIdString = "";
		String storePathString = PropertyUtil.getProperty("Globals.fileStorePath") + File.separator + fileNamePrefix + File.separator + EgovStringUtil.getTimeStamp().substring(0, 6) + File.separator;

		int fileSn = 0;
		if(CommonUtil.removeNull(atchFileId).equals("")) {
			atchFileIdString = idgenService.selectNextSeq("COMTNFILE", "FILE_", 15, "0");
		} else {
			atchFileIdString = atchFileId;
			FileVO fvo = new FileVO();
			fvo.setAtchFileId(atchFileIdString);
			fileSn = fileMngService.getMaxFileSN(fvo);
		}

		File saveFolder = new File(storePathString);

		if(!saveFolder.exists() || saveFolder.isFile()) {
			saveFolder.mkdirs();
		}

		List<FileVO> result	= new ArrayList<FileVO>();
		FileVO fvo;
		String filePath = "";
		List<MultipartFile> fileList = fileMap.get(inputFileName);
		if(fileList != null) {
			for(MultipartFile file : fileList) {
				if(!file.isEmpty()) {
					String orginFileName = file.getOriginalFilename();

					// 파일명이 없는 경우 처리 (첨부가 되지 않은 input file type)
					if ("".equals(orginFileName)) {
						continue;
					}

					int index = orginFileName.lastIndexOf(".");
					String fileExt = orginFileName.substring(index + 1);
					String newName = fileNamePrefix + EgovStringUtil.getTimeStamp() + fileSn;
					long _size = file.getSize();

					if (!"".equals(orginFileName)) {
						filePath = storePathString + File.separator + newName;
						file.transferTo(new File(filePath));
					}
					fvo = new FileVO();
					fvo.setFileExtsn(fileExt);
					fvo.setFileStreCours(storePathString);
					fvo.setFileSize(_size);
					fvo.setOrignlFileNm(orginFileName);
					fvo.setStreFileNm(newName);
					fvo.setAtchFileId(atchFileIdString);
					fvo.setFileSn(String.valueOf(fileSn));
					fvo.setFormNm(inputFileName);
					result.add(fvo);

					fileSn++;
				}
			}
		}

		return result;
	}

	/**
	 * File List에서 atchFileId를 리턴
	 * @param	fileList
	 * @return	String
	 * @throws	Exception
	 */
	public String getAtchFileId(List<FileVO> fileList) throws Exception {
		if(CommonUtil.isEmpty(fileList)) {
			return null;
		} else {
			FileVO fileVO = fileList.get(0);
			return fileVO.getAtchFileId();
		}
	}

	/**
	 * 첨부파일에 대한 목록 정보를 취득한다.
	 *
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public List<FileVO> parseFileInf(Map<String, MultipartFile> files, String KeyStr, int fileKeyParam, String atchFileId, String storePath) throws Exception {
		int fileKey = fileKeyParam;

		String storePathString = "";
		String atchFileIdString = "";

		if ("".equals(storePath) || storePath == null) {
			storePathString = PropertyUtil.getProperty("Globals.fileStorePath");
		} else {
			storePathString = PropertyUtil.getProperty(storePath);
		}

		if ("".equals(atchFileId) || atchFileId == null) {
			atchFileIdString = idgenService.selectNextSeq("COMTNFILE", "FILE_", 15, "0");
		} else {
			atchFileIdString = atchFileId;
		}

		File saveFolder = new File(storePathString);

		if (!saveFolder.exists() || saveFolder.isFile()) {
			saveFolder.mkdirs();
		}

		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		MultipartFile file;
		String filePath = "";
		List<FileVO> result	= new ArrayList<FileVO>();
		FileVO fvo;

		while (itr.hasNext()) {
			Entry<String, MultipartFile> entry = itr.next();

			file = entry.getValue();
			String orginFileName = file.getOriginalFilename();

			// 파일명이 없는 경우 처리 (첨부가 되지 않은 input file type)
			if ("".equals(orginFileName)) {
				continue;
			}

			int index = orginFileName.lastIndexOf(".");
			String fileExt = orginFileName.substring(index + 1);
			String newName = KeyStr + EgovStringUtil.getTimeStamp() + fileKey;
			long _size = file.getSize();

			if (!"".equals(orginFileName)) {
			filePath = storePathString + File.separator + newName;
			file.transferTo(new File(filePath));
			}
			fvo = new FileVO();
			fvo.setFileExtsn(fileExt);
			fvo.setFileStreCours(storePathString);
			fvo.setFileSize(_size);
			fvo.setOrignlFileNm(orginFileName);
			fvo.setStreFileNm(newName);
			fvo.setAtchFileId(atchFileIdString);
			fvo.setFileSn(String.valueOf(fileKey));
			result.add(fvo);

			fileKey++;
		}

		return result;
	}

	/**
	 * 첨부파일을 서버에 저장한다.
	 *
	 * @param file
	 * @param newName
	 * @param stordFilePath
	 * @throws Exception
	 */
	public void writeUploadedFile(MultipartFile file, String newName, String stordFilePath) throws Exception {
		InputStream stream = null;
		OutputStream bos = null;
		String stordFilePathReal = (stordFilePath==null?"":stordFilePath).replaceAll("\\.\\.","");
		try {
			stream = file.getInputStream();
			File cFile = new File(stordFilePathReal);

			if (!cFile.isDirectory()) {
				boolean _flag = cFile.mkdir();
				if (!_flag) {
					throw new IOException("Directory creation Failed ");
				}
			}

			bos = new FileOutputStream(stordFilePathReal + File.separator + newName);

			int bytesRead = 0;
			byte[] buffer = new byte[BUFF_SIZE];

			while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
		} catch (FileNotFoundException fnfe) {
			logger.debug("error : "+fnfe.getCause());
			throw fnfe;
		} catch (IOException ioe) {
			logger.debug("error : "+ioe.getCause());
			throw ioe;
		} catch (Exception e) {
			logger.debug("error : "+e.getCause());
			throw e;
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException ioe) {
					logger.debug("error : "+ioe.getCause());
				} catch (Exception ignore) {
					logger.debug("error : "+ignore.getCause());
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException ioe) {
					logger.debug("error : "+ioe.getCause());
				} catch (Exception ignore) {
					logger.debug("error : "+ignore.getCause());
				}
			}
		}
	}

	/**
	 * 서버의 파일을 다운로드한다.
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void downFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String downFileName = EgovStringUtil.isNullToString(request.getAttribute("downFile")).replaceAll("..","");
		String orgFileName = EgovStringUtil.isNullToString(request.getAttribute("orgFileName")).replaceAll("..","");

		File file = new File(downFileName);

		if (!file.exists()) {
			throw new FileNotFoundException(downFileName);
		}

		if (!file.isFile()) {
			throw new FileNotFoundException(downFileName);
		}

		byte[] b = new byte[BUFF_SIZE]; //buffer size 2K.
		String fName = (new String(orgFileName.getBytes(), "UTF-8")).replaceAll("\r\n","");
		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Disposition:", "attachment; filename=" + fName);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");

		BufferedInputStream fin = null;
		BufferedOutputStream outs = null;

		try {
			fin = new BufferedInputStream(new FileInputStream(file));
			outs = new BufferedOutputStream(response.getOutputStream());
			int read = 0;

			while ((read = fin.read(b)) != -1) {
				outs.write(b, 0, read);
			}
		} finally {
			if (outs != null) {
				try {
					outs.close();
				} catch (IOException ioe) {
					logger.debug("error : "+ioe.getCause());
				} catch (Exception ignore) {
					logger.debug("error : "+ignore.getCause());
				}
			}
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException ioe) {
					logger.debug("error : "+ioe.getCause());
				} catch (Exception ignore) {
					logger.debug("error : "+ignore.getCause());
				}
			}
		}
	}

	/**
	 * 파일을 실제 물리적인 경로에 생성한다.
	 *
	 * @param file
	 * @param newName
	 * @param stordFilePath
	 * @throws Exception
	 */
	protected static void writeFile(MultipartFile file, String newName, String stordFilePath) throws Exception {
		InputStream stream = null;
		OutputStream bos = null;
		newName = EgovStringUtil.isNullToString(newName).replaceAll("..", "");
		stordFilePath = EgovStringUtil.isNullToString(stordFilePath).replaceAll("..", "");
		try {
			stream = file.getInputStream();
			File cFile = new File(stordFilePath);

			if (!cFile.isDirectory())
			cFile.mkdir();

			bos = new FileOutputStream(stordFilePath + File.separator + newName);

			int bytesRead = 0;
			byte[] buffer = new byte[BUFF_SIZE];

			while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
			bos.write(buffer, 0, bytesRead);
			}
		} catch (FileNotFoundException fnfe) {
			logger.debug("error : "+fnfe.getCause());
		} catch (IOException ioe) {
			logger.debug("error : "+ioe.getCause());
		} catch (Exception e) {
			logger.debug("error : "+e.getCause());
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException ioe) {
					logger.debug("error : "+ioe.getCause());
				} catch (Exception ignore) {
					logger.debug("error : "+ignore.getCause());
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException ioe) {
					logger.debug("error : "+ioe.getCause());
				} catch (Exception ignore) {
					logger.debug("error : "+ignore.getCause());
				}
			}	
		}
	}

	/**
	 * 서버 파일에 대하여 다운로드를 처리한다.
	 *
	 * @param response
	 * @param streFileNm
	 *			: 파일저장 경로가 포함된 형태
	 * @param orignFileNm
	 * @throws Exception
	 */
	public void downFile(HttpServletResponse response, String streFileNm, String orignFileNm) throws Exception {
		String downFileName = EgovStringUtil.isNullToString(streFileNm).replaceAll("..","");
		String orgFileName = EgovStringUtil.isNullToString(orignFileNm).replaceAll("..","");

		File file = new File(downFileName);

		if (!file.exists()) {
			throw new FileNotFoundException(downFileName);
		}

		if (!file.isFile()) {
			throw new FileNotFoundException(downFileName);
		}

		//byte[] b = new byte[BUFF_SIZE]; //buffer size 2K.
		int fSize = (int)file.length();
		if (fSize > 0) {
			BufferedInputStream in = null;

			try {
				in = new BufferedInputStream(new FileInputStream(file));

				String mimetype = "text/html"; //"application/x-msdownload"

				response.setBufferSize(fSize);
				response.setContentType(mimetype);
				response.setHeader("Content-Disposition:", "attachment; filename=" + orgFileName);
				response.setContentLength(fSize);
				FileCopyUtils.copy(in, response.getOutputStream());
			} finally {
				if (in != null) {
					try {
					in.close();
					} catch (IOException ioe) {
						logger.debug("error : "+ioe.getCause());
					} catch (Exception ignore) {
						logger.debug("error : "+ignore.getCause());
					}
				}
			}
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}

	/**
	 * MultipartHttpServletRequest에서 파일 목록을 추출
	 * @param	multiRequest
	 * @return	List<MultipartFile>
	 */
	public List<MultipartFile> getFileList(MultipartHttpServletRequest multiRequest) {
		List<MultipartFile> fileList = new ArrayList<MultipartFile>();
		final Map<String, MultipartFile> files = multiRequest.getFileMap();
		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		MultipartFile file;
		Entry<String, MultipartFile> entry;
		try {
			while(itr.hasNext()) {
				entry = itr.next();
				file = entry.getValue();
				if(file != null) {
					fileList.add(file);
				}
			}
		} catch(RuntimeException re) {
			logger.debug("error : "+re.getCause());
		} catch(Exception e) {
			logger.debug("error : "+e.getCause());
		}

		return fileList;
	}

	/**
	 * 파일을 복사하고 새로 생성된 atchFileId를 리턴한다.
	 * @param	String atchFileId
	 * @return	String
	 * @throws	Exception
	 */
	public String copyFiles(String atchFileId) throws Exception {
		String newAtchFileId = idgenService.selectNextSeq("COMTNFILE", "FILE_", 15, "0");

		FileVO paramVO = new FileVO();
		paramVO.setAtchFileId(atchFileId);
		List<FileVO> fileList = fileMngService.selectFileInfs(paramVO);

		File orgFile;
		File newFile;
		String newFileNm;
		List<String> copiedFileList = new ArrayList<String>();

		try {
			for(FileVO fileVO : fileList) {
				orgFile = new File(fileVO.getFileStreCours() + fileVO.getStreFileNm());
				if(orgFile.exists()) {
					newFileNm = "COPY" + EgovStringUtil.getTimeStamp() + fileVO.getFileSn();
					newFile = new File(fileVO.getFileStreCours() + newFileNm);
					FileCopyUtils.copy(orgFile, newFile);

					copiedFileList.add(fileVO.getFileStreCours() + newFileNm);
					fileVO.setStreFileNm(newFileNm);
				}
				fileVO.setAtchFileId(newAtchFileId);
			}

			fileMngService.insertFileInfs(fileList);
		} catch (IOException ie) {
			logger.debug("error : "+ie.getCause());
			try {
				// 에러 발생시 복사한 파일 삭제
				for(String fileNm : copiedFileList) {
					EgovFileTool.deleteFile(fileNm);
				}
			} catch (RuntimeException e2) {
				logger.debug("error : "+e2.getCause());
			} catch (Exception e3) {
				logger.debug("error : "+e3.getCause());
			}

			return null;
		} catch (Exception e) {
			logger.debug("error : "+e.getCause());
			try {
				// 에러 발생시 복사한 파일 삭제
				for(String fileNm : copiedFileList) {
					EgovFileTool.deleteFile(fileNm);
				}
			} catch (RuntimeException e2) {
				logger.debug("error : "+e2.getCause());
			} catch (Exception e3) {
				logger.debug("error : "+e3.getCause());
			}

			return null;
		}

		return newAtchFileId;
	}
}
