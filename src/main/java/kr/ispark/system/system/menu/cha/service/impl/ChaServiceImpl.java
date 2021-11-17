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
import kr.ispark.system.system.menu.ideaEvalItem.service.IdeaEvalItemVO;
import kr.ispark.system.system.menu.ideaUs.service.IdeaUsVO;
import kr.ispark.system.system.survey.survReg.service.SurvRegVO;
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
	 * 문화재청 성과목표 목록 조회
	 * @param	ChaVO searchVO
	 * @return	List<ChaVO>
	 * @throws	Exception
	 */
	public List<ChaVO> selectList2(ChaVO searchVO) throws Exception {
		return chaDAO.selectList2(searchVO);
	}

	/**
	 * 문화재청 전략목표 목록 조회
	 * @param	ChaVO searchVO
	 * @return	List<ChaVO>
	 * @throws	Exception
	 */
	public List<ChaVO> selectList3(ChaVO searchVO) throws Exception {
		return chaDAO.selectList3(searchVO);
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
		System.out.println("삭제 서비스 dataVO : " + dataVO);
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
	public int saveData3(ChaVO dataVO, List<FileVO> fileList2) throws Exception {
		System.out.println("서비스");
		int resultCnt = 0;

		if(!CommonUtil.isEmpty(fileList2)) {
			fileMngService.insertFileInfs(fileList2);
		}

		if(CommonUtil.isEmpty(dataVO.getStraTgtId())) {
			dataVO.setAtchFileId(fileUtil.getAtchFileId(fileList2));
			dataVO.setStraTgtId(idgenService.selectNextSeq("BSC_SELF_STRATEGY", "A", 6, "0"));
			return chaDAO.insertData3(dataVO);
//			for(int i= 0 ; i < dataVO.getGridDataList().size();i++){
//				ChaVO chaVO = new ChaVO();
//				chaVO = dataVO.getGridDataList().get(i);
//				chaVO.setKpiId(idgenService.selectNextSeq("BSC_SELF_KPI", "B", 6, "0"));
//				chaVO.setKpiGbnId("01");
//				chaVO.setYear(dataVO.getYear());
//				chaVO.setStraTgtId(dataVO.getStraTgtId());
//				resultCnt += chaDAO.insertData33(chaVO);
//				return resultCnt;
//			}

		} else { //여기 구현해야함
			System.out.println("수정은 아직 구현 놉");
			return chaDAO.updateData3(dataVO);
//			dataVO.setMatchFileId(fileUtil.getAtchFileId(fileList2));
//			return chaDAO.updateData3(dataVO);
		}
	}

	/**
	 * 문화재청 저장 (전락목표 아래부분)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData33(ChaVO dataVO) throws Exception {
		List<ChaVO> gridDataList = dataVO.getGridDataList();
		int resultCnt = 0;
		String key = "";

		if(gridDataList != null && 0 < gridDataList.size()){
			for(ChaVO vo: gridDataList ){

				if(true) {
					vo.setKpiId(idgenService.selectNextSeq("BSC_SELF_KPI", "B", 6, "0"));
					vo.setKpiGbnId("01");
					vo.setStraNo(dataVO.getStraNo());
					vo.setStraTgtNm(dataVO.getStraTgtNm());
					//vo.setYear(dataVO.getYear());

					resultCnt +=  chaDAO.insertData33(vo);
				} else { //수정부분은 따로 구현해야함.
					resultCnt += chaDAO.updateData(vo);
				}

			}
		}

		return resultCnt;
	}

	/**
	 * 문화재청 저장 (성과목표 윗부분)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData4(ChaVO dataVO, List<FileVO> fileList2) throws Exception {
		System.out.println("서비스");
		System.out.println("서비스 dataVO : " + dataVO);
		int resultCnt = 0;

		if(!CommonUtil.isEmpty(fileList2)) {
			fileMngService.insertFileInfs(fileList2);
		}

		if(CommonUtil.isEmpty(dataVO.getResultTgtId())) {
			dataVO.setAtchFileId(fileUtil.getAtchFileId(fileList2));
			dataVO.setResultTgtId(idgenService.selectNextSeq("BSC_SELF_STRATEGY", "A", 6, "0"));
			return chaDAO.insertData4(dataVO);
//			for(int i= 0 ; i < dataVO.getGridDataList().size();i++){
//				ChaVO chaVO = new ChaVO();
//				chaVO = dataVO.getGridDataList().get(i);
//				chaVO.setKpiId(idgenService.selectNextSeq("BSC_SELF_KPI", "B", 6, "0"));
//				chaVO.setKpiGbnId("01");
//				chaVO.setYear(dataVO.getYear());
//				chaVO.setStraTgtId(dataVO.getStraTgtId());
//				resultCnt += chaDAO.insertData33(chaVO);
//				return resultCnt;
//			}

		} else { //여기 구현해야함
			System.out.println("수정은 아직 구현 놉");
			return chaDAO.updateData4(dataVO);
//			dataVO.setMatchFileId(fileUtil.getAtchFileId(fileList2));
//			return chaDAO.updateData3(dataVO);
		}
	}

	/**
	 * 문화재청 저장 (전락목표 아래부분)
	 * @param	ChaVO searchVO
	 * @return	int
	 * @throws	Exception
	 */
	public int saveData44(ChaVO dataVO) throws Exception {
		List<ChaVO> gridDataList = dataVO.getGridDataList();
		int resultCnt = 0;
		String key = "";

		if(gridDataList != null && 0 < gridDataList.size()){
			for(ChaVO vo: gridDataList ){

				if(true) {
					vo.setKpiId(idgenService.selectNextSeq("BSC_SELF_KPI", "B", 6, "0"));
					vo.setKpiGbnId("02");
					vo.setResultTgtNo(dataVO.getResultTgtNo());
					vo.setResultTgtNm(dataVO.getResultTgtNm());
					vo.setStraTgtId(dataVO.getStraTgtNm2());
					//vo.setYear(dataVO.getYear());

					resultCnt +=  chaDAO.insertData44(vo);
				} else { //수정부분은 따로 구현해야함.
					resultCnt += chaDAO.updateData(vo);
				}

			}
		}

		return resultCnt;
	}

	/**
	 * 콤보박스용..
	 */
	public List<ChaVO> selectList9(ChaVO searchVO) throws Exception {
		System.out.println("안되나?22222222222");
		return chaDAO.selectList9(searchVO);
	}

	/**
	 * 성과목표 상세 조회
	 * @param	ChaVO searchVO
	 * @return	ChaVO
	 * @throws	Exception
	 */
	public ChaVO selectDetail7(ChaVO searchVO) throws Exception {
		return chaDAO.selectDetail7(searchVO);
	}

	/**
	 * 성과목표 상세 조회
	 * @param	ChaVO searchVO
	 * @return	ChaVO
	 * @throws	Exception
	 */
	public ChaVO selectDetail77(ChaVO searchVO) throws Exception {
		return chaDAO.selectDetail77(searchVO);
	}
}

