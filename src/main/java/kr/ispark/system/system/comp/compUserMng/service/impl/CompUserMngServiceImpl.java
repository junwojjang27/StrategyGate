/*************************************************************************
* CLASS 명	: CompUserMngServiceIpml
* 작 업 자	: 박정현
* 작 업 일	: 2018-07-02
* 기	능	: 사용자관리 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-07-02
**************************************************************************/
package kr.ispark.system.system.comp.compUserMng.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.common.exception.CustomException;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CodeUtil;
import kr.ispark.common.util.ExcelParser;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.system.system.comp.compUserMng.service.CompUserMngVO;

@Service
public class CompUserMngServiceImpl extends EgovAbstractServiceImpl {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource(name="egovMessageSource")
	public EgovMessageSource egovMessageSource;

	@Resource
	private IdGenServiceImpl idgenService;

	@Resource
	private CompUserMngDAO compUserMngDAO;

	@Autowired
	private ExcelParser excelParser;

	@Autowired
	StandardPasswordEncoder passwordEncoder;

	/**
	 * 사용자관리 목록 조회
	 * @param	CompUserMngVO searchVO
	 * @return	List<CompUserMngVO>
	 * @throws	Exception
	 */
	public List<CompUserMngVO> selectList(CompUserMngVO searchVO) throws Exception {
		return compUserMngDAO.selectList(searchVO);
	}

	/**
	 * 사용자관리 상세 조회
	 * @param	CompUserMngVO searchVO
	 * @return	CompUserMngVO
	 * @throws	Exception
	 */
	public CompUserMngVO selectDetail(CompUserMngVO searchVO) throws Exception {
		return compUserMngDAO.selectDetail(searchVO);
	}

	/**
	 * 사용자관리 삭제
	 * @param	CompUserMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteCompUserMng(CompUserMngVO dataVO) throws Exception {
		return compUserMngDAO.deleteCompUserMng(dataVO);
	}

	/**
	 * 사용자관리 저장
	 * @param	CompUserMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(CompUserMngVO dataVO) throws Exception {

		if("N".equals(dataVO.getUpdateYn())) {
			dataVO.setPasswd(passwordEncoder.encode(dataVO.getUserId()));
			return compUserMngDAO.insertData(dataVO);
		} else {
			return compUserMngDAO.updateData(dataVO);
		}
	}

	/**
	 * 패스워드 초기화
	 * @param	CompUserMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updatePasswordReset(CompUserMngVO dataVO) throws Exception {
		dataVO.setPasswd(passwordEncoder.encode(dataVO.getUserId()));
		return compUserMngDAO.updatePasswordReset(dataVO);
	}

	/**
	 * 패스워드 변경
	 * @param	CompUserMngVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updatePassword(CompUserMngVO dataVO) throws Exception {
		dataVO.setUserId(SessionUtil.getUserId());
		dataVO.setPasswd(passwordEncoder.encode(dataVO.getPasswd()));
		return compUserMngDAO.updatePassword(dataVO);
	}

	/**
	 * ID 중복 체크
	 * @param	CompUserMngVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int selectIdCnt(CompUserMngVO searchVO) throws Exception {
		return compUserMngDAO.selectIdCnt(searchVO);
	}

	/**
	 * 사용자 언어 설정 수정
	 * @param	UserVO dataVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateUserLang(UserVO dataVO) throws Exception{
		return compUserMngDAO.updateUserLang(dataVO);
	}

	/**
	 * 사용자관리 목록 조회 (엑셀 양식용)
	 * @param	CompUserMngVO searchVO
	 * @return	List<CompUserMngVO>
	 * @throws	Exception
	 */
	public List<CompUserMngVO> selectListForExcelForm(CompUserMngVO searchVO) throws Exception {
		return compUserMngDAO.selectListForExcelForm(searchVO);
	}


	/**
	 * 사용자관리 엑셀 업로드
	 * @param	CompUserMngVO searchVO
	 * @param	InputStream f
	 * @return	int
	 * @throws	Exception
	 */
	public int excelUploadProcess(CompUserMngVO dataVO, InputStream f) throws Exception {
		int resultCnt = 0;
		
		if(f != null){
			try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				/*
				 * excelParser에서 InputStream을 읽은 뒤 close 하기 때문에
				 * 시트를 여러개 읽으려면 InputStream을 ByteArrayOutputStream에 넣어두고 사용해야 함
				*/
				byte[] buffer = new byte[1024];
				int len;
				while ((len = f.read(buffer)) > -1 ) {
					baos.write(buffer, 0, len);
				}
				baos.flush();

				f = new ByteArrayInputStream(baos.toByteArray());

				// 수정
				List<CompUserMngVO> updateList
					= excelParser.excelToList(f, 2,
							2,	// 몇 번째 row부터 읽을 것인가. 0부터 시작.
							CompUserMngVO.class,	// row를 파싱할 class 타입
							new String[] {	// excel의 각 열을 매칭할 vo의 변수명 (열 순서대로)
								"userId",	"userNm",	"posId",
								"jikgubId",	"jobId",	"deptId",
								"email",	"beingYn"
							},
							new int[] {	// 각 열 별 속성. 뒤에 _NOTNULL이 있으면 유효성 검사시 필수값 체크를 수행함
								ExcelParser.CELL_TYPE_STRING_NOTNULL,	ExcelParser.CELL_TYPE_STRING_NOTNULL,	ExcelParser.CELL_TYPE_STRING_NOTNULL,
								ExcelParser.CELL_TYPE_STRING_NOTNULL,	ExcelParser.CELL_TYPE_STRING_NOTNULL,	ExcelParser.CELL_TYPE_STRING_NOTNULL,
								ExcelParser.CELL_TYPE_STRING,			ExcelParser.CELL_TYPE_STRING_NOTNULL
							}
					);

				// id 중복 체크
				Set<String> updateIdSet = new HashSet<String>();
				for(CompUserMngVO vo : updateList) {
					if(updateIdSet.contains(vo.getUserId())) {
						throw new CustomException(egovMessageSource.getMessage("bsc.system.userMng.excelUpload.error") + " (" + vo.getUserId() + ")");
					} else {
						updateIdSet.add(vo.getUserId());
					}
				}

				for(CompUserMngVO row : updateList) {
					row.setPosId(row.getPosId().split(":")[0]);
					row.setPosNm(CodeUtil.getCodeName("344", row.getPosId()));
					row.setJikgubId(row.getJikgubId().split(":")[0]);
					row.setJikgubNm(CodeUtil.getCodeName("345", row.getJikgubId()));
					row.setJobId(row.getJobId().split(":")[0]);
					row.setJobNm(CodeUtil.getCodeName("343", row.getJobId()));
					row.setBeingYn(row.getBeingYn().split(":")[0]);

					row.setFindYear(dataVO.getFindYear());

					resultCnt += compUserMngDAO.updateExcelData(row);
				}


				f = new ByteArrayInputStream(baos.toByteArray());

				// 등록
				List<CompUserMngVO> insertList
					= excelParser.excelToList(f, 1,
							2,	// 몇 번째 row부터 읽을 것인가. 0부터 시작.
							CompUserMngVO.class,	// row를 파싱할 class 타입
							new String[] {	// excel의 각 열을 매칭할 vo의 변수명 (열 순서대로)
								"userId",	"userNm",	"posId",
								"jikgubId",	"jobId",	"deptId",
								"email",	"beingYn"
							},
							new int[] {	// 각 열 별 속성. 뒤에 _NOTNULL이 있으면 유효성 검사시 필수값 체크를 수행함
								ExcelParser.CELL_TYPE_STRING_NOTNULL,	ExcelParser.CELL_TYPE_STRING_NOTNULL,	ExcelParser.CELL_TYPE_STRING_NOTNULL,
								ExcelParser.CELL_TYPE_STRING_NOTNULL,	ExcelParser.CELL_TYPE_STRING_NOTNULL,	ExcelParser.CELL_TYPE_STRING_NOTNULL,
								ExcelParser.CELL_TYPE_STRING,			ExcelParser.CELL_TYPE_STRING_NOTNULL
							}
					);

				// id 중복 체크
				List<CompUserMngVO> userList = compUserMngDAO.selectListForExcelForm(dataVO);
				updateIdSet = new HashSet<String>();
				for(CompUserMngVO vo : userList) {
					updateIdSet.add(vo.getUserId());
				}

				Set<String> insertIdSet = new HashSet<String>();
				for(CompUserMngVO vo : insertList) {
					if(updateIdSet.contains(vo.getUserId()) || insertIdSet.contains(vo.getUserId())) {
						throw new CustomException(egovMessageSource.getMessage("bsc.system.userMng.excelUpload.error") + " (" + vo.getUserId() + ")");
					} else {
						insertIdSet.add(vo.getUserId());
					}
				}

				for(CompUserMngVO row : insertList) {
					row.setPosId(row.getPosId().split(":")[0]);
					row.setPosNm(CodeUtil.getCodeName("344", row.getPosId()));
					row.setJikgubId(row.getJikgubId().split(":")[0]);
					row.setJikgubNm(CodeUtil.getCodeName("345", row.getJikgubId()));
					row.setJobId(row.getJobId().split(":")[0]);
					row.setJobNm(CodeUtil.getCodeName("343", row.getJobId()));
					row.setBeingYn(row.getBeingYn().split(":")[0]);

					row.setPasswd(passwordEncoder.encode(row.getUserId()));

					row.setFindYear(dataVO.getFindYear());

					resultCnt += compUserMngDAO.insertExcelData(row);
				}
			} catch(IOException ie) {
				log.error("error : "+ie.getCause());
				throw ie;	
			} catch(Exception e) {
				log.error("error : "+e.getCause());
				throw e;
			}
		}
		
		return resultCnt;
	}
}
