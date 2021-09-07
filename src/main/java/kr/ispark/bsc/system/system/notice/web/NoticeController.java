/*************************************************************************
* CLASS 명	: NoticeController
* 작 업 자	: 박정현
* 작 업 일	: 2018-03-29
* 기	능	: 공지사항 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	박정현		2018-03-29
**************************************************************************/
package kr.ispark.bsc.system.system.notice.web;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.service.FileVO;
import egovframework.com.utl.sim.service.EgovFileTool;
import kr.ispark.bsc.system.system.notice.service.NoticeVO;
import kr.ispark.bsc.system.system.notice.service.impl.NoticeServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.CustomEgovFileMngUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.SessionUtil;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class NoticeController extends BaseController {
	@Autowired
	private NoticeServiceImpl noticeService;

	@Resource(name="CustomEgovFileMngUtil")
	private CustomEgovFileMngUtil fileUtil;

	/**
	 * 공지사항 목록 화면
	 * @param	NoticeVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/system/system/notice/noticeList.do")
	public String noticeList(@ModelAttribute("searchVO") NoticeVO searchVO, Model model) throws Exception {
		return "/bsc/system/system/notice/noticeList." + searchVO.getLayout();
	}

	/**
	 * 공지사항 그리드 조회(json)
	 * @param	NoticeVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/system/system/notice/noticeList_json.do")
	public ModelAndView noticeList_json(@ModelAttribute("searchVO") NoticeVO searchVO) throws Exception {
		List<NoticeVO> dataList = noticeService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 공지사항 조회
	 * @param	NoticeVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/system/system/notice/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") NoticeVO searchVO,Model model) throws Exception {
		model.addAttribute("dataVO", noticeService.selectDetail(searchVO));

		return makeJsonData(noticeService.selectDetail(searchVO));
	}

	/**
	 * 공지사항 삭제
	 * @param	NoticeVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/bsc/system/system/notice/deleteNotice.do")
	public ModelAndView deleteNotice(@ModelAttribute("dataVO") NoticeVO dataVO, Model model) throws Exception {
		List<FileVO> deleteFileList = noticeService.deleteNotice(dataVO);
		try {
			for(FileVO fileVO : deleteFileList) {
				EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
			}
		} catch(RuntimeException re) {
			log.error("error : "+re.getCause());
		} catch(Exception e) {
			log.error("error : "+e.getCause());
		}

		return makeSuccessJsonData();
	}

	/**
	 * 공지사항 저장 (파일 첨부)
	 * @param multiRequest
	 * @param dataVO
	 * @param model
	 * @param bindingResult
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/bsc/system/system/notice/saveNotice.do")
	public void saveBoard (
			final MultipartHttpServletRequest multiRequest,
			HttpServletResponse response,
			@ModelAttribute("dataVO") NoticeVO dataVO,
			Model model, BindingResult bindingResult) throws Exception {

		// 유효성 체크
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			resultHandling(false, multiRequest, response, dataVO, getListErrorMsg(bindingResult));
			return;
		}

		// 수정
		if(!CommonUtil.isEmpty(dataVO.getId())) {
			NoticeVO attachVO = noticeService.selectDetail(dataVO);
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
			if(CommonUtil.isEmpty(dataVO.getId())) {
				dataVO.setUserId(SessionUtil.getUserId());
				noticeService.insertData(dataVO, fileList1);
			} else {
				// 수정
				List<FileVO> deleteFileList = noticeService.updateData(dataVO, fileList1);
				try {
					for(FileVO fileVO : deleteFileList) {
						EgovFileTool.deletePath(fileVO.getFileStreCours()+fileVO.getStreFileNm());
					}
				} catch(RuntimeException re) {
					log.error("error : "+re.getCause());
				} catch(Exception e) {
					log.error("error : "+e.getCause());
				}
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

	/**
	 * 공지사함 첨부파일 관리
	 * @param	NoticeVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/bsc/system/system/notice/noticeDetail.do")
	public String noticeDetail(@ModelAttribute("searchVO") NoticeVO searchVO, Model model) throws Exception {

		return "/bsc/system/system/notice/noticeDetail.script";
	}

    /**
     * 공지사항 미리보기
     * @param
     * @return String
     * @throws
     */

    @RequestMapping(value="/bsc/system/system/notice/popNoticeForm.do")
    public String noticePopup(
            @ModelAttribute("searchVO") NoticeVO searchVO, Model model)
            throws Exception {
    	NoticeVO dataVO = noticeService.selectDetail(searchVO);
    	model.addAttribute("dataVO", dataVO);
        return "/bsc/system/system/notice/popNoticeForm." + searchVO.getLayout();
    }


    /*popup 조회용 controller*/

    /**
	 * 공지사항 목록 화면
	 * @param	NoticeVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/bsc/system/system/notice/popNoticeList.do")
	public String popNoticeList(@ModelAttribute("searchVO") NoticeVO searchVO, Model model) throws Exception {

		return "/bsc/system/system/notice/popNoticeList." + searchVO.getLayout();
	}

	/**
	 * 공지사항 그리드 조회(json)
	 * @param	NoticeVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/bsc/system/system/notice/popNoticeList_json.do")
	public ModelAndView popNoticeList_json(@ModelAttribute("searchVO") NoticeVO searchVO) throws Exception {
		List<NoticeVO> dataList = noticeService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 공지사항 조회
	 * @param	NoticeVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/bsc/system/system/notice/popSelectNoticeDetail.do")
	public ModelAndView popSelectNoticeDetail(@ModelAttribute("searchVO") NoticeVO searchVO,Model model) throws Exception {
		model.addAttribute("dataVO", noticeService.selectDetail(searchVO));

		return makeJsonData(noticeService.selectDetail(searchVO));
	}

	/**
	 * 공지사함 첨부파일 관리
	 * @param	NoticeVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/comPop/bsc/system/system/notice/popNoticeDetail.do")
	public String popNoticeDetail(@ModelAttribute("searchVO") NoticeVO searchVO, Model model) throws Exception {

		return "/bsc/system/system/notice/noticeDetail.script";
	}

	/**
	 * 전체 공지사항 상세 조회 (로그인 화면용)
	 * @param	NoticeVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws
	 */
	@RequestMapping(value="/comPop/notice/popNoticeDetail.do")
	public String popNoticeDetailForAll(@ModelAttribute("searchVO") NoticeVO searchVO, Model model) throws Exception {
		NoticeVO dataVO = noticeService.selectDetailForAll(searchVO);
		model.addAttribute("dataVO", dataVO);
		model.addAttribute("isPublic", "Y");
		return "/bsc/system/system/notice/popNoticeForm." + searchVO.getLayout();
	}
}