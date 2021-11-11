/*************************************************************************
* CLASS 명	: ChaServiceIpml
* 작 업 자	: 하성준
* 작 업 일	: 2021-11-09
* 기	능	: 문화재청 ServiceIpml
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	하성준		2021-11-09
**************************************************************************/
package kr.ispark.system.system.menu.cha.service.impl;

import java.util.List;

import javax.annotation.Resource;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
import kr.ispark.common.util.CustomEgovFileMngUtil;
import kr.ispark.system.system.menu.ideaUs.service.IdeaUsVO;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.ispark.system.system.menu.cha.service.ChaVO;
import kr.ispark.common.system.service.impl.IdGenServiceImpl;
import kr.ispark.common.util.CommonUtil;

@Service
public class ChaServiceImpl extends EgovAbstractServiceImpl {

	@Resource
	private IdGenServiceImpl idgenService;
	
	@Resource
	private ChaDAO chaDAO;


	@Resource(name="CustomEgovFileMngUtil")
	private CustomEgovFileMngUtil fileUtil;

	@Resource(name="EgovFileMngService")
	private EgovFileMngService fileMngService;

	/**
	 * 문화재청 목록 조회
	 * @param	ChaVO searchVO
	 * @return	List<ChaVO>
	 * @throws	Exception
	 */
	public List<ChaVO> selectList(ChaVO searchVO) throws Exception {
		return chaDAO.selectList(searchVO);
	}
	
	/**
	 * 문화재청 상세 조회
	 * @param	ChaVO searchVO
	 * @return	ChaVO
	 * @throws	Exception
	 */
	public ChaVO selectDetail(ChaVO searchVO) throws Exception {
		return chaDAO.selectDetail(searchVO);
	}
	
	/**
	 * 문화재청 정렬순서저장
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int updateSortOrder(ChaVO dataVO) throws Exception {
		int resultCnt = 0;
		for(ChaVO paramVO : dataVO.getGridDataList()) {
			resultCnt += chaDAO.updateSortOrder(paramVO);
		}
		return resultCnt;
	}

	/**
	 * 문화재청 삭제
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int deleteCha(ChaVO dataVO) throws Exception {
		return chaDAO.deleteCha(dataVO);
	}
	
	/**
	 * 문화재청 저장 (임무)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData(ChaVO dataVO) throws Exception {
		System.out.println("서비스");

		if(CommonUtil.isEmpty(dataVO.getVision()) && CommonUtil.isEmpty(dataVO.getMatchFileId()) && CommonUtil.isEmpty(dataVO.getVatchFileId())) {
			return chaDAO.insertData(dataVO);
		} else {
			return chaDAO.updateData(dataVO);
		}
	}

	/**
	 * 문화재청 저장 (임무 첨부파일)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String saveData11(ChaVO dataVO, List<FileVO> fileList1) throws Exception {
		System.out.println("서비스");
		if(!CommonUtil.isEmpty(fileList1)) {
			fileMngService.insertFileInfs(fileList1);
		}

		if(CommonUtil.isEmpty(dataVO.getMission()) && CommonUtil.isEmpty(dataVO.getVision()) && CommonUtil.isEmpty(dataVO.getMatchFileId()) && CommonUtil.isEmpty(dataVO.getVatchFileId())) {
			dataVO.setMatchFileId(fileUtil.getAtchFileId(fileList1));
			return chaDAO.insertData11(dataVO);
		} else {
			dataVO.setMatchFileId(fileUtil.getAtchFileId(fileList1));
			return chaDAO.updateData11(dataVO);
		}
	}

	/**
	 * 문화재청 저장 (비전)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData2(ChaVO dataVO) throws Exception {
		System.out.println("서비스");
		if(CommonUtil.isEmpty(dataVO.getMission()) && CommonUtil.isEmpty(dataVO.getMatchFileId()) && CommonUtil.isEmpty(dataVO.getVatchFileId())) {
			return chaDAO.insertData2(dataVO);
		} else {
			return chaDAO.updateData2(dataVO);
		}
	}

	/**
	 * 문화재청 저장 (비전 첨부파일)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String saveData22(ChaVO dataVO, List<FileVO> fileList1) throws Exception {
		System.out.println("서비스");
		if(!CommonUtil.isEmpty(fileList1)) {
			fileMngService.insertFileInfs(fileList1);
		}

		if(CommonUtil.isEmpty(dataVO.getMission()) && CommonUtil.isEmpty(dataVO.getVision()) && CommonUtil.isEmpty(dataVO.getMatchFileId()) && CommonUtil.isEmpty(dataVO.getVatchFileId())) {
			dataVO.setVatchFileId(fileUtil.getAtchFileId(fileList1));
			return chaDAO.insertData22(dataVO);
		} else {
			dataVO.setVatchFileId(fileUtil.getAtchFileId(fileList1));
			return chaDAO.updateData22(dataVO);
		}
	}

	/**
	 * 문화재청 저장 (전락목표 윗부분)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public String saveData3(ChaVO dataVO, List<FileVO> fileList2) throws Exception {
		System.out.println("서비스");
		if(!CommonUtil.isEmpty(fileList2)) {
			fileMngService.insertFileInfs(fileList2);
		}

		if(true) {
			dataVO.setAtchFileId(fileUtil.getAtchFileId(fileList2));
			dataVO.setStraTgtId(idgenService.selectNextSeq("BSC_SELF_STRATEGY", "S", 6, "0"));
			return chaDAO.insertData3(dataVO);
		} else { //여기 구현해야함
			System.out.println("수정은 아직 구현 놉");
			return "asfsdfdfsfafdffda";
//			dataVO.setMatchFileId(fileUtil.getAtchFileId(fileList2));
//			return chaDAO.updateData3(dataVO);
		}
	}
}

