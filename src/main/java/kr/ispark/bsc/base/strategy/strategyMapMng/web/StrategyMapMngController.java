/*************************************************************************
* CLASS 명	: StrategyMapMngController
* 작 업 자	: kimyh
* 작 업 일	: 2018. 2. 2.
* 기	능	: 전략체계도 관리 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 2. 2.			최 초 작 업
**************************************************************************/
package kr.ispark.bsc.base.strategy.strategyMapMng.web;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.utl.sim.service.EgovFileTool;
import kr.ispark.bsc.base.strategy.strategyMapMng.service.StrategyMapVO;
import kr.ispark.bsc.base.strategy.strategyMapMng.service.impl.StrategyMapMngServiceImpl;
import kr.ispark.common.util.CustomEgovFileMngUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.common.web.BaseController;

@Controller
public class StrategyMapMngController extends BaseController {

	@Autowired
	private StrategyMapMngServiceImpl strategyMapMngServiceImpl;

	@Resource(name="CustomEgovFileMngUtil")
	private CustomEgovFileMngUtil fileUtil;

	/**
	 * 전략체계도 관리 화면
	 * @param	StrategyMapVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/base/strategy/strategyMapMng/strategyMapMng.do")
	public String scDeptDiagramList(@ModelAttribute("searchVO") StrategyMapVO searchVO, Model model) throws Exception {
		return "/bsc/base/strategy/strategyMapMng/strategyMapMng." + searchVO.getLayout();
	}

	/**
	 * 비전 & 미션 정보 조회
	 * @param	ScDeptVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/base/strategy/strategyMapMng/getData.do")
	public ModelAndView getData(@ModelAttribute("searchVO") StrategyMapVO searchVO) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("info", strategyMapMngServiceImpl.selectVisionMission(searchVO));
		return new ModelAndView("jsonView", resultMap);
	}

	/**
	 * 전략체계도 데이터 조회
	 */
	@RequestMapping(value="/bsc/base/strategy/strategyMapMng/strategyMapMng_xml.do")
	public String strategyMapMng_xml(@ModelAttribute("searchVO") StrategyMapVO searchVO, Model model) throws Exception {
		List<StrategyMapVO> perspectiveList = strategyMapMngServiceImpl.selectPerspectiveList(searchVO);
		List<StrategyMapVO> strategyList = strategyMapMngServiceImpl.selectList(searchVO);
		List<StrategyMapVO> metricList = strategyMapMngServiceImpl.selectMetricList(searchVO);
		List<StrategyMapVO> arrowList = strategyMapMngServiceImpl.selectArrowList(searchVO);
		String showMetricYn = strategyMapMngServiceImpl.selectShowMetricYn(searchVO);

		String bgImgNm = "svgChartBG_" + searchVO.getFindScDeptId() + "_" + searchVO.getFindYear() + ".jpg";
		if(!new File(PropertyUtil.getProperty("FILE_BACKGROUND_ROOT_PATH") + "/" + bgImgNm).exists()) {
			bgImgNm = "";
		}

		model.addAttribute("bgImgNm", bgImgNm);
		model.addAttribute("showMetricYn", showMetricYn);
		model.addAttribute("perspectiveList", perspectiveList);
		model.addAttribute("strategyList", strategyList);
		model.addAttribute("metricList", metricList);
		model.addAttribute("arrowList", arrowList);

		return "/bsc/base/strategy/strategyMapMng/strategyMapMng_xml.xml";
	}

	/**
	 * 전략체계도 저장
	 * @param	StrategyMapVO dataVO
	 * @param	BindingResult bindingResult
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/base/strategy/strategyMapMng/saveStrategyMap.do")
	public ModelAndView saveStrategyMap (
			@ModelAttribute("dataVO") StrategyMapVO dataVO,
			BindingResult bindingResult,
			Model model) throws Exception {
		try {
			strategyMapMngServiceImpl.saveData(dataVO);
		} catch(SQLException sqe) {
			log.error("error : "+sqe.getCause());
			return makeFailJsonData();
		} catch(Exception e) {
			log.error("error : "+e.getCause());
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
	}

	/**
	 * 배경 업로드
	 * @param	StrategyMapVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/base/strategy/strategyMapMng/popUploadBG.do")
	public String popUploadBG(@ModelAttribute("searchVO") StrategyMapVO searchVO, Model model) throws Exception {
		return "/bsc/base/strategy/strategyMapMng/popUploadBG." + searchVO.getLayout();
	}

	/**
	 * 배경 업로드 처리
	 * @param multiRequest
	 * @param response
	 * @param searchVO
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/bsc/base/strategy/strategyMapMng/popUploadBGProcess.do")
	public void popUploadBGProcess(
			final MultipartHttpServletRequest multiRequest,
			HttpServletResponse response,
			@ModelAttribute("dataVO") StrategyMapVO dataVO, Model model) throws Exception {

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
				String fileNm = "svgChartBG_" + dataVO.getFindScDeptId() + "_" + dataVO.getFindYear() + ".jpg";
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
	@RequestMapping("/bsc/base/strategy/strategyMapMng/deleteBGProcess.do")
	public ModelAndView deleteBGProcess(@ModelAttribute("dataVO") StrategyMapVO dataVO, Model model) throws Exception {
		try {
			String fileNm = "svgChartBG_" + dataVO.getFindScDeptId() + "_" + dataVO.getFindYear() + ".jpg";
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
