package kr.ispark.system.system.menu.ideaUs.web;

import java.sql.SQLException;
import java.util.List;

import egovframework.com.cmm.service.FileVO;
import egovframework.com.utl.sim.service.EgovFileTool;
import kr.ispark.bsc.system.system.notice.service.NoticeVO;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.CustomEgovFileMngUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.system.system.menu.ideaUs.service.impl.IdeaUsServiceImpl;
import kr.ispark.system.system.menu.ideaUs.service.IdeaUsVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IdeaUsController extends BaseController {
	@Autowired
	private IdeaUsServiceImpl ideaUsService;

	@Resource(name = "CustomEgovFileMngUtil")
	private CustomEgovFileMngUtil fileUtil;

	/**
	 * 혁신 IDEA+ 목록 화면
	 *
	 * @param    IdeaUsVO searchVO
	 * @param    Model model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping("/system/system/menu/ideaUs/ideaUsList.do")
	public String ideaUsList(@ModelAttribute("searchVO") IdeaUsVO searchVO, Model model) throws Exception {
		return "/system/system/menu/ideaUs/ideaUsList." + searchVO.getLayout();
	}

	/**
	 * 혁신 IDEA+ 그리드 조회(json)
	 *
	 * @param    IdeaUsVO searchVO
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/menu/ideaUs/ideaUsList_json.do")
	public ModelAndView ideaUsList_json(@ModelAttribute("searchVO") IdeaUsVO searchVO) throws Exception {
		List<IdeaUsVO> dataList = ideaUsService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 혁신 IDEA+ 조회
	 *
	 * @param    IdeaUsVO searchVO
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/menu/ideaUs/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") IdeaUsVO searchVO) throws Exception {
		return makeJsonData(ideaUsService.selectDetail(searchVO));
	}

	/**
	 * 혁신 IDEA+ 정렬순서저장
	 *
	 * @param    IdeaUsVO dataVO
	 * @param    Model model
	 * @param    BindingResult bindingResult
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/menu/ideaUs/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") IdeaUsVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if (bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		return makeJsonDataByResultCnt(ideaUsService.updateSortOrder(dataVO));
	}

	/**
	 * 혁신 IDEA+ 삭제
	 *
	 * @param    IdeaUsVO dataVO
	 * @param    Model model
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/system/system/menu/ideaUs/deleteIdeaUs.do")
	public ModelAndView deleteIdeaUs(@ModelAttribute("dataVO") IdeaUsVO dataVO, Model model) throws Exception {
		int resultCnt = ideaUsService.deleteIdeaUs(dataVO);
		if(resultCnt == 0) {
			return makeFailJsonData();
		}
		return makeSuccessJsonData();
		//return makeJsonDataByResultCnt(ideaUsService.deleteIdeaUs(dataVO));
	}


	/**
	 * 혁신 IDEA+ 저장
	 * @param	IdeaUsVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */

	@RequestMapping("/system/system/menu/ideaUs/saveIdeaUs.do")
	public void saveIdeaUs (
			final MultipartHttpServletRequest multiRequest,
			HttpServletResponse response,
			@ModelAttribute("dataVO") IdeaUsVO dataVO,
			Model model, BindingResult bindingResult) throws Exception {

		// 유효성 체크
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			resultHandling(false, multiRequest, response, dataVO, getListErrorMsg(bindingResult));
			return;
		}

		// 수정
		if(!CommonUtil.isEmpty(dataVO.getIdeaCd())) {
			IdeaUsVO attachVO = ideaUsService.selectDetail(dataVO);
			dataVO.setAtchFileId(attachVO.getAtchFileKey());
		}

		// 첨부파일 유효성 체크
		String errMsg;
		errMsg = fileUtil.validation(multiRequest, "upFile1", dataVO.getAtchFileId(), (long)5242880, 5, PropertyUtil.getProperty("fileUpload.allowFileExts").split(","), dataVO.getChkAttachFiles());
		if(errMsg != null) {
			resultHandling(false, multiRequest, response, dataVO, errMsg);
			return;
		}

		// 첨부파일을 List로 변환
		List<FileVO> fileList1 = fileUtil.parseFileInf(multiRequest, "upFile1", "example", dataVO.getAtchFileId());

		try {
			// 등록
			if(!CommonUtil.isEmpty(dataVO.getIdeaCd())) {
				// 수정
				List<FileVO> deleteFileList = ideaUsService.updateData(dataVO, fileList1);
				try {
					for(FileVO fileVO : deleteFileList) {
						EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
					}
				} catch(RuntimeException re) {
					log.error("error : "+re.getCause());
				} catch(Exception e) {
					log.error("error : "+e.getCause());
				}
			} else {
				dataVO.setUserId(SessionUtil.getUserId());
				ideaUsService.insertData(dataVO, fileList1);
			}
		} catch(SQLException sqe) {
			log.error("error : "+sqe.getCause());
			// 업로드한 파일들 물리적으로 삭제
			for(FileVO fileVO : fileList1) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
			resultHandling(false, multiRequest, response, dataVO);
		} catch(Exception e) {
			log.error(e.getMessage());
			// 업로드한 파일들 물리적으로 삭제
			for(FileVO fileVO : fileList1) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
		}


		resultHandling(true, multiRequest, response, dataVO);
	}

	@RequestMapping("/system/system/menu/ideaUs/ideaUsDetail.do")
	public String getAtchFileForm(@ModelAttribute("searchVO") IdeaUsVO ideaUsVO,  Model model) {

		//model.addAttribute("searchVO",IdeaUsVO);
		return "/system/system/menu/ideaUs/ideaUsAtchFileForm";
	}
}