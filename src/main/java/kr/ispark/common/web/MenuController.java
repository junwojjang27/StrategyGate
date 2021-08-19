package kr.ispark.common.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;

import kr.ispark.bsc.system.system.notice.service.impl.NoticeServiceImpl;
import kr.ispark.common.CommonVO;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.MenuVO;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.system.service.impl.MenuServiceImpl;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.PropertyUtil;
import kr.ispark.common.util.SessionUtil;
import sun.util.logging.resources.logging;

@Controller
public class MenuController {
	@Autowired
	private MenuServiceImpl menuService;

	@Autowired
	private NoticeServiceImpl noticeService;

	@Autowired
	private CommonServiceImpl commonService;

	/**
	 * 메뉴 이동
	 * @param	MenuVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/main.do")
	public String main(MenuVO searchVO, Model model) throws Exception {
		String pgmId = searchVO.getPgmId();
		String url = null;
		UserVO userVO = SessionUtil.getUserVO();

		if(userVO == null) {
			return "forward:/login.do";
		}

		// 로그인 후 첫 화면
		if(CommonUtil.isEmpty(pgmId)) {
			/*
			if(CommonUtil.removeNull(userVO.getAlertPwChangeYn()).equals("Y")) {
				model.addAttribute("alertPwChangeYn", "Y");
			}
			*/
			// 로그인 후 비밀번호 변경 알림을 한 번만 보여주기 위한 처리
			//userVO.setAlertPwChangeYn("N");

			// 도움말 목록
			model.addAttribute("guideCommentList", menuService.selectGuideCommentList(searchVO));
			
			// 프로세스 목록
			/*
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(menuService.selectProcessList(searchVO));
			model.addAttribute("processList", jsonData);
			*/

			// 테마
			model.addAttribute("theme", CommonUtil.removeNull(commonService.selectTheme(searchVO), PropertyUtil.getProperty("default.theme")));
			
			//메뉴얼 이미지 파일 목록 가져오기
			ArrayList<String> manualImgNmList = new ArrayList<String>();
			String imgPath = PropertyUtil.getProperty("Globals.menualPath");
			File files = new File(imgPath);
			if(files.isDirectory() && files.length() > 0){
				for(File file : files.listFiles()){
					if(!"".equals(CommonUtil.nullToBlank(file.getName()))){
						manualImgNmList.add(file.getName().substring(0, file.getName().lastIndexOf(".")));
					}
				}
			}
			model.addAttribute("manualImgNmList", manualImgNmList);

			// 팝업 공지사항
			if(CommonUtil.removeNull(userVO.getShowPopNotice()).equals("Y")) {
				model.addAttribute("popNoticeList", noticeService.selectPopNoticeList(searchVO));
				userVO.setShowPopNotice("N");
			}

			return "/common/main.main";
		} else {
			HashMap<String, String> menuMap = (HashMap<String, String>)SessionUtil.getAttribute("menuMap");
			if(menuMap == null) {
				return "/common/main.simple";
			}

			url = menuMap.get(pgmId);

			// 메뉴 접근 로그 저장
			if(userVO != null) {
				MenuVO dataVO = new MenuVO();
				dataVO.setPgmId(pgmId);
				dataVO.setUserId(userVO.getUserId());
				dataVO.setScDeptId(userVO.getScDeptId());
				dataVO.setIp(userVO.getIp());
				dataVO.setUrl(url);
				menuService.insertMenuAccessLog(dataVO);
			}

			return "forward:" + url;
		}
	}

	/**
	 * IE9용 파일 업로드 콜백 페이지
	 * @param	MenuVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping(value="/common/fileUploadCallback.do")
	public String fileUploadCallback(CommonVO dataVO, Model model) throws Exception {
		model.addAttribute("dataVO", dataVO);
		return "/common/fileUploadCallback.simple";
	}
}
