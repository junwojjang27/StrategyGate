/*************************************************************************
* CLASS 명	: ScDeptDiagMngController
* 작 업 자	: kimyh
* 작 업 일	: 2018. 1. 31.
* 기	능	: 성과조직도 관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 1. 31.		최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.scDept.scDeptDiagMng.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.utl.sim.service.EgovFileTool;
import kr.ispark.bsc.base.scDept.scDeptDiagMng.service.ScDeptDiagMngVO;
import kr.ispark.bsc.base.scDept.scDeptDiagMng.service.impl.ScDeptDiagMngServiceImpl;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.util.CustomEgovFileMngUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.common.web.BaseController;

@Controller
public class ScDeptDiagMngController extends BaseController {

	@Autowired
	private ScDeptDiagMngServiceImpl scDeptDiagMngService;

	@Autowired
	private CommonServiceImpl commmonService;

	@Resource(name="CustomEgovFileMngUtil")
	private CustomEgovFileMngUtil fileUtil;

	/**
	 * 성과조직도 관리 화면
	 * @param	ScDeptDiagMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/base/scDept/scDeptDiagMng/scDeptDiagMng.do")
	public String scDeptDiagramList(@ModelAttribute("searchVO") ScDeptDiagMngVO searchVO, Model model) throws Exception {
		return "/bsc/base/scDept/scDeptDiagMng/scDeptDiagMng." + searchVO.getLayout();
	}

	/**
	 * 성과조직도 조회
	 * @param	ScDeptDiagMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/base/scDept/scDeptDiagMng/scDeptDiagMng_xml.do")
	public String scDeptDiagMng_xml(@ModelAttribute("searchVO") ScDeptDiagMngVO searchVO, Model model) throws Exception {
		String autoYn = "N";
		String monitoringRootScDeptId = commmonService.selectMonitoringRootScDeptId(searchVO);
		searchVO.setFindScDeptId(monitoringRootScDeptId);

		List<ScDeptDiagMngVO> deptList = scDeptDiagMngService.getList(searchVO);
		model.addAttribute("deptList", deptList);

		ArrayList<ScDeptDiagMngVO> list1 = new ArrayList<ScDeptDiagMngVO>(0);
		ArrayList<ScDeptDiagMngVO> list2 = new ArrayList<ScDeptDiagMngVO>(0);
		ArrayList<ScDeptDiagMngVO> list3 = new ArrayList<ScDeptDiagMngVO>(0);
		ArrayList<ScDeptDiagMngVO> list4 = new ArrayList<ScDeptDiagMngVO>(0);
		ArrayList<ScDeptDiagMngVO> list5 = new ArrayList<ScDeptDiagMngVO>(0);

		if(deptList.size() > 0){
			ScDeptDiagMngVO vo;
			int minLevelId = -1;
			for(int idx=0 ; idx<deptList.size() ; idx++) {
				vo = deptList.get(idx);
				if(idx==0 || minLevelId > vo.getLevelId()) {
					minLevelId = vo.getLevelId();
				}
			}
			minLevelId--;

			for(int idx=0 ; idx<deptList.size() ; idx++) {
				vo = deptList.get(idx);
				vo.setLevelId(vo.getLevelId()-minLevelId);
				if(1 == vo.getLevelId()){
					list1.add(vo);
				} else if(2 == vo.getLevelId()) {
					list2.add(vo);
				} else if(3 == vo.getLevelId()) {
					list3.add(vo);
				} else if(4 == vo.getLevelId()) {
					list4.add(vo);
				} else if(5 == vo.getLevelId()) {
					list5.add(vo);
				}
			}
		}

		model.addAttribute("autoYn", autoYn);
		model.addAttribute("list1", list1);
		model.addAttribute("list2", list2);
		model.addAttribute("list3", list3);
		model.addAttribute("list4", list4);
		model.addAttribute("list5", list5);

		return "/bsc/base/scDept/scDeptDiagMng/scDeptDiagMng_xml.xml";
	}

	/**
	 * 신호등 조회
	 * @param	ScDeptDiagMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/base/scDept/scDeptDiagMng/trafficSignal_xml.do")
	public String trafficSignal_xml(@ModelAttribute("searchVO") ScDeptDiagMngVO searchVO, Model model) throws Exception {
		List<ScDeptDiagMngVO> list = scDeptDiagMngService.getSignal(searchVO);
		model.addAttribute("signalList", list);
		return "/bsc/base/scDept/scDeptDiagMng/trafficSignal_xml.xml";
	}

	/**
	 * 성과조직도 저장
	 * @param	ScDeptDiagMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/base/scDept/scDeptDiagMng/saveScDeptDiag.do")
	public ModelAndView saveScDeptDiag(@ModelAttribute("dataVO") ScDeptDiagMngVO dataVO, Model model) throws Exception {
		scDeptDiagMngService.insertData(dataVO);
		return makeSuccessJsonData();
	}

	/**
	 * 배경 업로드
	 * @param	ScDeptDiagMngVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/base/scDept/scDeptDiagMng/popUploadBG.do")
	public String popUploadBG(@ModelAttribute("searchVO") ScDeptDiagMngVO searchVO, Model model) throws Exception {
		return "/bsc/base/scDept/scDeptDiagMng/popUploadBG." + searchVO.getLayout();
	}

	/**
	 * 배경 업로드 처리
	 * @param multiRequest
	 * @param response
	 * @param searchVO
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/bsc/base/scDept/scDeptDiagMng/popUploadBGProcess.do")
	public void popUploadBGProcess(
			final MultipartHttpServletRequest multiRequest,
			HttpServletResponse response,
			@ModelAttribute("dataVO") ScDeptDiagMngVO dataVO, Model model) throws Exception {

		// 첨부파일 유효성 체크
		String errMsg;
		errMsg = fileUtil.validation(multiRequest, "fileNm", null, Long.valueOf(PropertyUtil.getProperty("fileUpload.defaultImgMaxSize")), 1, PropertyUtil.getProperty("fileUpload.allowImgFileExts").split(","));
		if(errMsg != null) {
			resultHandling(false, multiRequest, response, dataVO, errMsg);
			return;
		}

		List<MultipartFile> fileList = fileUtil.getFileList(multiRequest);
		if(fileList.size() == 0) {
			resultHandling(false, multiRequest, response, dataVO);
		} else {
			try {
				MultipartFile file = fileList.get(0);
				String fileNm = "svgOrgBG_" + dataVO.getFindYear() + ".jpg";
				fileUtil.writeUploadedFile(file, fileNm, PropertyUtil.getProperty("FILE_BACKGROUND_ROOT_PATH"));
				resultHandling(true, multiRequest, response, dataVO);
			} catch(RuntimeException re) {
				log.error("error : "+re.getCause());
				resultHandling(false, multiRequest, response, dataVO);
			} catch(Exception e) {
				log.error("error : "+e.getCause());
				resultHandling(false, multiRequest, response, dataVO);
			}
		}
	}

	/**
	 * 배경 삭제 처리
	 * @param dataVO
	 * @param model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/base/scDept/scDeptDiagMng/deleteBGProcess.do")
	public ModelAndView deleteBGProcess(@ModelAttribute("dataVO") ScDeptDiagMngVO dataVO, Model model) throws Exception {
		try {
			String fileNm = "svgOrgBG_" + dataVO.getFindYear() + ".jpg";
			EgovFileTool.deletePath(PropertyUtil.getProperty("FILE_BACKGROUND_ROOT_PATH") + File.separator + fileNm);
			return makeSuccessJsonData();
		} catch(RuntimeException re) {
			log.error("error : "+re.getCause());
			return makeFailJsonData();
		} catch(Exception e) {
			log.error("error : "+e.getCause());
			return makeFailJsonData();
		}
	}
}
