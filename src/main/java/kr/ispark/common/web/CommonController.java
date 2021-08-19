/*************************************************************************
* CLASS 명	: CommonController
* 작 업 자	: kimyh
* 작 업 일	: 2018. 1. 24.
* 기	능	: 공통 모듈 Controller
* 주의사항	: 이곳에 선언된 url은 권한 상관 없이 접근할 수 있으므로 권한 체크가 필요하면 수동으로 할 것
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 1. 24.		최 초 작 업
**************************************************************************/
package kr.ispark.common.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.bsc.system.system.notice.service.NoticeVO;
import kr.ispark.bsc.system.system.notice.service.impl.NoticeServiceImpl;
import kr.ispark.common.CommonVO;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.DeptVO;
import kr.ispark.common.system.service.MenuVO;
import kr.ispark.common.system.service.ScDeptVO;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.util.CodeUtil;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.RoutingDataSource;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.common.util.service.CodeVO;
import kr.ispark.system.system.batch.pastYearCopy.service.PastYearCopyVO;
import kr.ispark.system.system.batch.pastYearCopy.service.impl.PastYearCopyServiceImpl;

@Controller
public class CommonController extends BaseController {
	@Autowired
	private CommonServiceImpl commonService;

	@Autowired
	private NoticeServiceImpl noticeService;

	@Autowired
	private PastYearCopyServiceImpl pastYearCopyService;

	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileService;

	/**
	 * 로그인
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/login.do")
	public String login(Model model, CommonVO searchVO) throws Exception {
		model.addAttribute("noticeList", noticeService.selectListForAll(searchVO));
		model.addAttribute("popNoticeList", noticeService.selectPopNoticeList(searchVO));
		return "/common/login.simple";
	}
	
	/**
	 * 로그인
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loginDenied.do")
	public String loginDenied(Model model, CommonVO searchVO) throws Exception {
		
		String test = "test";
		
		return "/common/loginDenied.simple";
	}

	/**
	 * 코드 목록 조회 (json)
	 */
	@RequestMapping("/common/codeList_json.do")
	public ModelAndView codeList_json(@ModelAttribute("searchVO") CodeVO searchVO) throws Exception {
		
		List<CodeVO> codeList;
		if(CommonUtil.isEmpty(searchVO.getFindYear())) {
			codeList = CodeUtil.getCodeList(searchVO.getCodeGrpId());
		} else {
			codeList = CodeUtil.getCodeList(searchVO.getCodeGrpId(), searchVO.getFindYear());
		}

		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(isAuthenticated) {
			return makeJsonListData(codeList);
		} else {
			return makeFailJsonData(egovMessageSource.getMessage("errors.secureException"));
		}
	}

	/**
	 * 성과조직 목록 조회 (json)
	 * @param	ScDeptVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/common/scDeptList_json.do")
	public ModelAndView scDeptList_json(@ModelAttribute("searchVO") ScDeptVO searchVO) throws Exception {
		
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(isAuthenticated) {
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("monitoringRootScDeptId", commonService.selectMonitoringRootScDeptId(searchVO));
			resultMap.put("scDeptList", commonService.selectScDeptList(searchVO));
			resultMap.put("deptList", commonService.selectDeptList(searchVO));
			return new ModelAndView("jsonView", resultMap);
		} else {
			return makeFailJsonData(egovMessageSource.getMessage("errors.secureException"));
		}
	}

	/**
	 * 성과조직 목록 조회
	 * @param	ScDeptVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/common/popScDeptList.do")
	public String popScDeptList(@ModelAttribute("searchVO") ScDeptVO searchVO, Model model) throws Exception {
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(isAuthenticated) {
			model.addAttribute("scDeptList", commonService.selectScDeptList(searchVO));
			return "/common/popScDeptList." + searchVO.getLayout();
		} else {
			return "forward:/error/accessDenied.do";
		}
	}

	/**
	 * 조직 목록 조회 (json)
	 * @param	DeptVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/common/deptList_json.do")
	public ModelAndView deptList_json(@ModelAttribute("searchVO") DeptVO searchVO) throws Exception {
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(isAuthenticated) {
			return makeJsonListData(commonService.selectDeptList(searchVO));
		} else {
			return makeFailJsonData(egovMessageSource.getMessage("errors.secureException"));
		}
	}

	/**
	 * 조직 목록 조회
	 * @param	DeptVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/common/popDeptList.do")
	public String popDeptList(@ModelAttribute("searchVO") DeptVO searchVO, Model model) throws Exception {
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(isAuthenticated) {
			model.addAttribute("deptList", commonService.selectDeptList(searchVO));
			return "/common/popDeptList." + searchVO.getLayout();
		} else {
			return "forward:/error/accessDenied.do";
		}
	}

	/**
	 * 사용자 조회 팝업
	 * @param	UserVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/common/popSearchUser.do")
	public String popDeptList(@ModelAttribute("searchVO") UserVO searchVO, Model model) throws Exception {
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(isAuthenticated) {
			model.addAttribute("deptList", commonService.selectDeptList(searchVO));
			return "/common/popSearchUser." + searchVO.getLayout();
		} else {
			return "forward:/error/accessDenied.do";
		}
	}

	/**
	 * 사용자 목록 조회 (json)
	 * @param	UserVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/common/userList_json.do")
	public ModelAndView userList_json(@ModelAttribute("searchVO") UserVO searchVO) throws Exception {
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(isAuthenticated) {
			return makeJsonListData(commonService.selectUserList(searchVO));
		} else {
			return makeFailJsonData(egovMessageSource.getMessage("errors.secureException"));
		}
	}

	/**
	 * 즐겨찾기 목록 조회
	 * @param	MenuVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/common/bookmarkList.do")
	public ModelAndView bookmarkList(@ModelAttribute("searchVO") MenuVO searchVO, Model model) throws Exception {
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(isAuthenticated) {
			return makeJsonListData(commonService.selectBookmarkList(searchVO));
		} else {
			return makeFailJsonData(egovMessageSource.getMessage("errors.secureException"));
		}
	}

	/**
	 * 즐겨찾기 삭제
	 * @param	MenuVO dataVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/common/deleteBookmark.do")
	public ModelAndView deleteBookmark(@ModelAttribute("dataVO") MenuVO dataVO, Model model) throws Exception {
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(isAuthenticated) {
			int result = commonService.deleteBookmark(dataVO);
			if(result > 0) {
				return makeSuccessJsonData();
			} else {
				return makeFailJsonData();
			}
		} else {
			return makeFailJsonData(egovMessageSource.getMessage("errors.secureException"));
		}
	}

	/**
	 * 즐겨찾기 등록
	 * @param	MenuVO dataVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/common/insertBookmark.do")
	public ModelAndView insertBookmark(@ModelAttribute("dataVO") MenuVO dataVO, Model model) throws Exception {
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(isAuthenticated) {
			int result = commonService.insertBookmark(dataVO);
			if(result > 0) {
				return makeSuccessJsonData();
			} else {
				return makeFailJsonData();
			}
		} else {
			return makeFailJsonData(egovMessageSource.getMessage("errors.secureException"));
		}
	}

	/**
	 * 첨부파일에 대한 목록을 조회한다.
	 * @param fileVO
	 * @param atchFileId
	 * @param sessionVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/common/fileList.do")
	public String fileList(@ModelAttribute("searchVO") FileVO fileVO, @RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
		String atchFileId = (String)commandMap.get("param_atchFileId");

		fileVO.setAtchFileId(atchFileId);
		List<FileVO> fileList = fileService.selectFileInfs(fileVO);

		model.addAttribute("fileList", fileList);
		model.addAttribute("updateFlag", "N");
		model.addAttribute("fileListCnt", fileList.size());
		model.addAttribute("atchFileId", atchFileId);

		return "/common/fileList.script";
	}

	/**
	 * 첨부파일에 대한 목록을 조회한다. (공개용)
	 * @param fileVO
	 * @param atchFileId
	 * @param sessionVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/common/publicFileList.do")
	public String publicFileList(@ModelAttribute("searchVO") FileVO fileVO, @RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
		String atchFileId = (String)commandMap.get("param_atchFileId");

		fileVO.setIsPublic("Y");
		fileVO.setAtchFileId(atchFileId);
		List<FileVO> fileList = fileService.selectFileInfs(fileVO);

		model.addAttribute("fileList", fileList);
		model.addAttribute("updateFlag", "N");
		model.addAttribute("fileListCnt", fileList.size());
		model.addAttribute("atchFileId", atchFileId);

		return "/common/fileList.script";
	}

	/**
	 * 사용자의 성과조직 정보 조회
	 * @param	CommonVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/common/scDeptInfoByUser.do")
	public ModelAndView scDeptInfoByUser(CommonVO searchVO) throws Exception {
		return makeJsonData(commonService.selectScDeptByUser(searchVO));
	}

	/**
	 * 팝업 공지사항
	 * @param	NoticeVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/common/popNoticeDetail.do")
	public String popNoticeDetail(@ModelAttribute("searchVO") NoticeVO searchVO, Model model) throws Exception {
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(isAuthenticated) {
			NoticeVO dataVO = noticeService.selectDetail(searchVO);
			model.addAttribute("dataVO", dataVO);
			model.addAttribute("showCloseOption", "Y");
			return "/bsc/system/system/notice/popNoticeForm." + searchVO.getLayout();
		} else {
			return "forward:/error/accessDenied.do";
		}
	}

	/**
	 * 메일발송 popup
	 * @param	CommonVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/common/popSendMail.do")
	public String popSendMail(@ModelAttribute("searchVO") CommonVO searchVO, Model model) throws Exception {
		return "/common/popSendMail." + searchVO.getLayout();
	}

	/**
	 * 메일발송
	 * @param	CommonVO dataVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/common/sendMail.do")
	public ModelAndView sendMail(@ModelAttribute("searchVO") CommonVO dataVO)throws Exception {
		int resultCnt = commonService.sendMail(dataVO.getTitle(), dataVO.getContents(), dataVO.getUserIdList());
		return makeJsonDataByResultCnt(resultCnt);
	}

	/**
	 * 전년도 데이터 복사
	 * @param	PastYearCopyVO dataVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/common/copyPastData.do")
	public ModelAndView copyPastData(@ModelAttribute("dataVO") PastYearCopyVO dataVO)throws Exception {
		if(!SessionUtil.hasAuth("01")) {
			return makeFailJsonData(egovMessageSource.getMessage("info.noAuth.msg"));
		}

		int resultCnt = pastYearCopyService.copyDataFromLastYear(dataVO);
		return makeJsonDataByResultCnt(resultCnt);
	}
	
	/**
	 * 성과조직 목록 조회 (json)
	 * @param	ScDeptVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/common/processList_json.do")
	public ModelAndView processList_json(@ModelAttribute("searchVO") MenuVO searchVO) throws Exception {
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(isAuthenticated) {
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("processList", commonService.selectProcessList(searchVO));
			return new ModelAndView("jsonView", resultMap);
		} else {
			return makeFailJsonData(egovMessageSource.getMessage("errors.secureException"));
		}
	}
	
	/**
	 * 즐겨찾기 등록
	 * @param	MenuVO dataVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/common/processCompleteData.do")
	public ModelAndView processCompleteData(@ModelAttribute("dataVO") MenuVO dataVO, Model model) throws Exception {
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if(isAuthenticated) {
			int result = commonService.updateProcessData(dataVO);
			if(result > 0) {
				return makeSuccessJsonData();
			} else {
				return makeFailJsonData();
			}
		} else {
			return makeFailJsonData(egovMessageSource.getMessage("errors.secureException"));
		}
	}
	
	/**
	 * 즐겨찾기 등록
	 * @param	MenuVO dataVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/connectionReload.do")
	public ModelAndView connectionReload() throws Exception {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		EgovMap emap = new EgovMap();
		emap.put("param", PropertyUtil.getProperty("Globals.Master.db"));
		List<CommonVO> paramList = commonService.selectDbResetList(emap);
		if(paramList != null && paramList.size() > 0){
			//routingDataSource.makeTargetDataSourcesFile(paramList);
			//routingDataSource.resetTargetDataSources();
			resultMap.put("msg", "success");
		}else{
			resultMap.put("msg", "fail");
		}
		return new ModelAndView("jsonView", resultMap);
		
	}
}
