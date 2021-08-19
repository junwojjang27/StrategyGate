/*************************************************************************
* CLASS 명	: DashboardController
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-08
* 기	능	: dashboard Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-08
**************************************************************************/
package kr.ispark.bsc.mon.dashboard.dashboard.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.ispark.bsc.mon.dashboard.dashboard.service.DashboardVO;
import kr.ispark.bsc.mon.dashboard.dashboard.service.impl.DashboardServiceImpl;
import kr.ispark.common.security.service.UserVO;
import kr.ispark.common.system.service.ScDeptVO;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.util.CodeUtil;
import kr.ispark.common.util.CommonUtil;
import kr.ispark.common.util.SessionUtil;
import kr.ispark.common.util.service.CodeVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Controller
public class DashboardController extends BaseController {
	@Autowired
	private DashboardServiceImpl dashboardService;
	
	@Autowired
	private CommonServiceImpl commonService;
	
	/**
	 * dashboard 목록 화면
	 * @param	DashboardVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/dashboard/dashboard/dashboardList.do")
	public String dashboardList(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		
		if("".equals(CommonUtil.nullToBlank(searchVO.getFindAnalCycle()))){
			searchVO.setFindAnalCycle("Y");
		}
		
		UserVO uvo = SessionUtil.getUserVO();
		searchVO.setLoginUserId(uvo!=null?uvo.getUserId():null);
		
		List<DashboardVO> itemList = dashboardService.selectItemList(searchVO);
		List<DashboardVO> itemUserList = dashboardService.selectItemUserList(searchVO);
		
		model.addAttribute("itemList", itemList);
		model.addAttribute("itemUserList", itemUserList);
		
		return "/bsc/mon/dashboard/dashboard/dashboardList." + searchVO.getLayout();
	}
	
	/**
	 * dashboard 목록 화면
	 * @param	DashboardVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/dashboard/dashboard/popItemList.do")
	public String popItemList(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		return "/bsc/mon/dashboard/dashboard/popItemList." + searchVO.getLayout();
	}
	
	/**
	 * dashboard 목록 화면
	 * @param	DashboardVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/dashboard/dashboard/popItemList_json.do")
	public ModelAndView popItemList_json(@ModelAttribute("searchVO") DashboardVO searchVO) throws Exception {
		
		UserVO uvo = SessionUtil.getUserVO();
		searchVO.setLoginUserId(uvo != null?uvo.getUserId():null);
		
		List<DashboardVO> itemList = dashboardService.selectItemList(searchVO);
		
		return makeGridJsonData(itemList, itemList.size(), searchVO);
	}
	
	/**
	 * 조직성과상세 목록 화면
	 * @param	ScDeptDetailVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/dashboard/dashboard/mon_itemA.do")
	public String mon_itemA(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		
		String userScDeptId = searchVO.getFindScDeptId();
		EgovMap userScDeptMap = (EgovMap) SessionUtil.getAttribute("userScDeptMap");
		if(userScDeptMap != null){
//			userScDeptId = (String)userScDeptMap.get(searchVO.getFindYear());
			ScDeptVO svo = commonService.getScDeptInfo(searchVO.getFindYear(), userScDeptId);
			if(svo != null){
				searchVO.setUserScDeptId(userScDeptId);
				model.addAttribute("scDeptNm", svo.getScDeptNm());
			}else{
				ScDeptVO tvo = commonService.selectTopScDeptInfo(searchVO.getFindYear());
				if(tvo != null){
					searchVO.setUserScDeptId(tvo.getScDeptId());
					model.addAttribute("scDeptNm", tvo.getScDeptNm());
				}
			}
			
		}
		
		DashboardVO deptScore = dashboardService.selectOrgScore(searchVO);
		
		if(deptScore != null){
			model.addAttribute("deptScore", deptScore);
		}
		
		List<DashboardVO> perspectiveList = dashboardService.selectPerspectiveScoreList(searchVO);
		model.addAttribute("perspectiveList", perspectiveList);
		
		return "/bsc/mon/dashboard/dashboard/mon_itemA." + searchVO.getLayout();
	}
	
	@RequestMapping("/bsc/mon/dashboard/dashboard/mon_itemB.do")
	public String mon_itemB(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		
		String userScDeptId = searchVO.getFindScDeptId();
		EgovMap userScDeptMap = (EgovMap) SessionUtil.getAttribute("userScDeptMap");
		if(userScDeptMap != null){
//			userScDeptId = (String)userScDeptMap.get(searchVO.getFindYear());
			ScDeptVO svo = commonService.getScDeptInfo(searchVO.getFindYear(), userScDeptId);
			if(svo != null){
				searchVO.setUserScDeptId(userScDeptId);
				model.addAttribute("scDeptNm", svo.getScDeptNm());
			}else{
				ScDeptVO tvo = commonService.selectTopScDeptInfo(searchVO.getFindYear());
				if(tvo != null){
					searchVO.setUserScDeptId(tvo.getScDeptId());
					model.addAttribute("scDeptNm", tvo.getScDeptNm());
				}
			}
			
		}
		
		
		List<DashboardVO> metricList = dashboardService.selectMetricTop5List(searchVO);
		List<DashboardVO> metricTopList = new ArrayList<DashboardVO>();
		if(metricList != null && metricList.size() > 0){
			DashboardVO dvo;
			int rankNum = 5;
			if(metricList.size() < rankNum ){
				rankNum = metricList.size();
			}
			for(int i=0 ; i<rankNum ; i++){
				dvo = (DashboardVO)metricList.get(i);
				metricTopList.add(dvo);
			}
		}
		
		model.addAttribute("metricTopList", metricTopList);
		
		return "/bsc/mon/dashboard/dashboard/mon_itemB." + searchVO.getLayout();
	}
	
	@RequestMapping("/bsc/mon/dashboard/dashboard/mon_itemC.do")
	public String mon_itemC(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		
		String userScDeptId = "";
		EgovMap userScDeptMap = (EgovMap) SessionUtil.getAttribute("userScDeptMap");
		if(userScDeptMap != null){
			userScDeptId = (String)userScDeptMap.get(searchVO.getFindYear());
			ScDeptVO svo = commonService.getScDeptInfo(searchVO.getFindYear(), userScDeptId);
			if(svo != null){
				searchVO.setUserScDeptId(userScDeptId);
				model.addAttribute("scDeptNm", svo.getScDeptNm());
			}else{
				ScDeptVO tvo = commonService.selectTopScDeptInfo(searchVO.getFindYear());
				if(tvo != null){
					searchVO.setUserScDeptId(tvo.getScDeptId());
					model.addAttribute("scDeptNm", tvo.getScDeptNm());
				}
			}
			
		}
		
		UserVO uvo = SessionUtil.getUserVO();
		searchVO.setLoginUserId(uvo != null?uvo.getUserId():null);
		
		List<DashboardVO> mainMetricList = dashboardService.selectMainMetricList(searchVO);
		model.addAttribute("mainMetricList", mainMetricList);
		
		return "/bsc/mon/dashboard/dashboard/mon_itemC." + searchVO.getLayout();
	}
	
	/**
	 * dashboard 목록 화면
	 * @param	DashboardVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/dashboard/dashboard/mon_itemC_json.do")
	public ModelAndView mon_itemC_json(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		UserVO uvo = SessionUtil.getUserVO();
		searchVO.setLoginUserId(uvo != null?uvo.getUserId():null);
		
		List<DashboardVO> mainMetricList = dashboardService.selectMainMetricList(searchVO);
		return makeJsonListData(mainMetricList);
	}
	
	/**
	 * dashboard 목록 화면
	 * @param	DashboardVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/dashboard/dashboard/popItemCMetricList.do")
	public String popMon_itemC(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		return "/bsc/mon/dashboard/dashboard/popItemCMetricList." + searchVO.getLayout();
	}
	
	/**
	 * dashboard 저장
	 * @param	DashboardVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/dashboard/dashboard/saveItemCData.do")
	public ModelAndView saveItemCData(@ModelAttribute("dataVO") DashboardVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		
		UserVO uvo = SessionUtil.getUserVO();
		dataVO.setLoginUserId(uvo.getUserId());
		
		return makeJsonDataByResultCnt(dashboardService.saveItemCData(dataVO));
	}
	
	/**
	 * dashboard 목록 화면
	 * @param	DashboardVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/dashboard/dashboard/popItemCMetricList_json.do")
	public ModelAndView popItemCMetricList_json(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		
		String userScDeptId = "";
		EgovMap userScDeptMap = (EgovMap) SessionUtil.getAttribute("userScDeptMap");
		if(userScDeptMap != null){
			userScDeptId = (String)userScDeptMap.get(searchVO.getFindYear());
			ScDeptVO svo = commonService.getScDeptInfo(searchVO.getFindYear(), userScDeptId);
			if(svo != null){
				searchVO.setUserScDeptId(userScDeptId);
				model.addAttribute("scDeptNm", svo.getScDeptNm());
			}else{
				ScDeptVO tvo = commonService.selectTopScDeptInfo(searchVO.getFindYear());
				if(tvo != null){
					searchVO.setUserScDeptId(tvo.getScDeptId());
					model.addAttribute("scDeptNm", tvo.getScDeptNm());
				}
			}
			
		}
		
		UserVO uvo = SessionUtil.getUserVO();
		searchVO.setLoginUserId(uvo!=null?uvo.getUserId():null);
		
		List<DashboardVO> dataList = dashboardService.selectUserMetricList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	@RequestMapping("/bsc/mon/dashboard/dashboard/mon_itemD.do")
	public String mon_itemD(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		
		String userScDeptId = searchVO.getFindScDeptId();
		EgovMap userScDeptMap = (EgovMap) SessionUtil.getAttribute("userScDeptMap");
		if(userScDeptMap != null){
//			userScDeptId = (String)userScDeptMap.get(searchVO.getFindYear());
			ScDeptVO svo = commonService.getScDeptInfo(searchVO.getFindYear(), userScDeptId);
			if(svo != null){
				searchVO.setUserScDeptId(userScDeptId);
				model.addAttribute("scDeptNm", svo.getScDeptNm());
			}else{
				ScDeptVO tvo = commonService.selectTopScDeptInfo(searchVO.getFindYear());
				if(tvo != null){
					searchVO.setUserScDeptId(tvo.getScDeptId());
					model.addAttribute("scDeptNm", tvo.getScDeptNm());
				}
			}
			
		}
		
		List<CodeVO> scDeptGrpList = CodeUtil.getCodeList("003", searchVO.getFindYear());
		model.addAttribute("scDeptGrpList", scDeptGrpList);
		
		if("".equals(CommonUtil.nullToBlank(searchVO.getFindScDeptGrpId()))){
			if(scDeptGrpList != null && scDeptGrpList.size() > 0){
				CodeVO cvo = (CodeVO)scDeptGrpList.get(0);
				searchVO.setFindScDeptGrpId(cvo.getCodeId());
			}
		}
		
		
		List<DashboardVO> evalGrpAllList = dashboardService.selectEvalGrpList(searchVO);
		List<DashboardVO> evalGrpList = new ArrayList<DashboardVO>(0);
		if(evalGrpAllList != null && evalGrpAllList.size() > 0){
			DashboardVO dvo;
			int rankNum = 5;
			if(evalGrpAllList.size() < rankNum ){
				rankNum = evalGrpAllList.size();
			}
			for(int i=0 ; i<rankNum ; i++){
				dvo = (DashboardVO)evalGrpAllList.get(i);
				evalGrpList.add(dvo);
			}
		}
		model.addAttribute("evalGrpList", evalGrpList);
		
		
		return "/bsc/mon/dashboard/dashboard/mon_itemD." + searchVO.getLayout();
	}
	
	@RequestMapping("/bsc/mon/dashboard/dashboard/evalGrpScoreList_json.do")
	public ModelAndView evalGrpScoreList_json(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		
		String userScDeptId = searchVO.getFindScDeptId();
		EgovMap userScDeptMap = (EgovMap) SessionUtil.getAttribute("userScDeptMap");
		if(userScDeptMap != null){
//			userScDeptId = (String)userScDeptMap.get(searchVO.getFindYear());
			ScDeptVO svo = commonService.getScDeptInfo(searchVO.getFindYear(), userScDeptId);
			if(svo != null){
				searchVO.setUserScDeptId(userScDeptId);
				model.addAttribute("scDeptNm", svo.getScDeptNm());
			}else{
				ScDeptVO tvo = commonService.selectTopScDeptInfo(searchVO.getFindYear());
				if(tvo != null){
					searchVO.setUserScDeptId(tvo.getScDeptId());
					model.addAttribute("scDeptNm", tvo.getScDeptNm());
				}
			}
		}
		
		List<DashboardVO> evalGrpAllList = dashboardService.selectEvalGrpList(searchVO);
		List<DashboardVO> evalGrpList = new ArrayList<DashboardVO>(0);
		if(evalGrpAllList != null && evalGrpAllList.size() > 0){
			DashboardVO dvo;
			int rankNum = 5;
			if(evalGrpAllList.size() < rankNum ){
				rankNum = evalGrpAllList.size();
			}
			for(int i=0 ; i<rankNum ; i++){
				dvo = (DashboardVO)evalGrpAllList.get(i);
				evalGrpList.add(dvo);
			}
		}
		
		return makeJsonListData(evalGrpList);
	}
	
	@RequestMapping("/bsc/mon/dashboard/dashboard/mon_itemE.do")
	public String mon_itemE(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		
		List<DashboardVO> govList = dashboardService.selectGovList(searchVO);
		model.addAttribute("govList", govList);
		
		return "/bsc/mon/dashboard/dashboard/mon_itemE." + searchVO.getLayout();
	}
	
	@RequestMapping("/bsc/mon/dashboard/dashboard/govScoreList_json.do")
	public ModelAndView govScoreList_json(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		List<DashboardVO> govList = dashboardService.selectGovList(searchVO);
		return makeJsonListData(govList);
	}
	
	@RequestMapping("/bsc/mon/dashboard/dashboard/mon_itemF.do")
	public String mon_itemF(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		
		String userScDeptId = searchVO.getFindScDeptId();
		EgovMap userScDeptMap = (EgovMap) SessionUtil.getAttribute("userScDeptMap");
		if(userScDeptMap != null){
//			userScDeptId = (String)userScDeptMap.get(searchVO.getFindYear());
			ScDeptVO svo = commonService.getScDeptInfo(searchVO.getFindYear(), userScDeptId);
			if(svo != null){
				searchVO.setUserScDeptId(userScDeptId);
				model.addAttribute("scDeptNm", svo.getScDeptNm());
			}else{
				ScDeptVO tvo = commonService.selectTopScDeptInfo(searchVO.getFindYear());
				if(tvo != null){
					searchVO.setUserScDeptId(tvo.getScDeptId());
					model.addAttribute("scDeptNm", tvo.getScDeptNm());
				}
			}
		}
		
		List<DashboardVO> metricList = dashboardService.selectMetricList(searchVO);
		model.addAttribute("metricList", metricList);
		
		if(metricList != null && metricList.size() > 0){
			DashboardVO vo = (DashboardVO)metricList.get(0);
			if(vo != null){
				searchVO.setFindMetricId(vo.getMetricId());
			}
		}
		
		/*
		DashboardVO detail = dashboardService.selectMetricDetail(searchVO);
		if(detail != null){
			model.addAttribute("detail", detail);
		}
		*/
		
		/*
		DashboardVO target = dashboardService.selectChartTarget(searchVO);
		DashboardVO actual = dashboardService.selectChartActual(searchVO);
		DashboardVO score = dashboardService.selectChartScore(searchVO);
		
		model.addAttribute("target", target);
		model.addAttribute("actual", actual);
		model.addAttribute("score", score);
		*/
		
		return "/bsc/mon/dashboard/dashboard/mon_itemF." + searchVO.getLayout();
	}
	
	/**
	 * 조직성과상세 목록 화면
	 * @param	ScDeptDetailVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/dashboard/dashboard/actualDetailChart_json.do")
	public ModelAndView actualDetailChart_xml(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		
		DashboardVO detail = dashboardService.selectMetricDetail(searchVO);
		DashboardVO target = dashboardService.selectChartTarget(searchVO);
		DashboardVO actual = dashboardService.selectChartActual(searchVO);
		DashboardVO score = dashboardService.selectChartScore(searchVO);
		
		HashMap<String,Object> resurltMap = new HashMap<String,Object>();
		
		resurltMap.put("detail", detail);
		resurltMap.put("target", target);
		resurltMap.put("actual", actual);
		resurltMap.put("score", score);
		
		return new ModelAndView("jsonView",resurltMap);
	}
	
	@RequestMapping("/bsc/mon/dashboard/dashboard/mon_itemG.do")
	public String mon_itemG(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		
		String userScDeptId = searchVO.getFindScDeptId();
		EgovMap userScDeptMap = (EgovMap) SessionUtil.getAttribute("userScDeptMap");
		if(userScDeptMap != null){
//			userScDeptId = (String)userScDeptMap.get(searchVO.getFindYear());
			ScDeptVO svo = commonService.getScDeptInfo(searchVO.getFindYear(), userScDeptId);
			if(svo != null){
				searchVO.setUserScDeptId(userScDeptId);
				model.addAttribute("scDeptNm", svo.getScDeptNm());
			}else{
				ScDeptVO tvo = commonService.selectTopScDeptInfo(searchVO.getFindYear());
				if(tvo != null){
					searchVO.setUserScDeptId(tvo.getScDeptId());
					model.addAttribute("scDeptNm", tvo.getScDeptNm());
				}
			}
			
		}
		
		DashboardVO dvo = dashboardService.selectScoreAnalysis(searchVO);
		model.addAttribute("scDeptGrpDetail", dvo);
		
		return "/bsc/mon/dashboard/dashboard/mon_itemG." + searchVO.getLayout();
	}
	
	/**
	 * dashboard 그리드 조회(json)
	 * @param	DashboardVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/dashboard/dashboard/dashboardList_json.do")
	public ModelAndView dashboardList_json(@ModelAttribute("searchVO") DashboardVO searchVO) throws Exception {
		List<DashboardVO> dataList = dashboardService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	//excelDownload
	/**
	 * 엑셀양식다운로드
	 * @param	DashboardVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/dashboard/dashboard/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") DashboardVO searchVO, Model model) throws Exception {
		
		/*
		 * 현재페이지 화면에 맞게 반드시 수정할 것.
		 */
		
		List<DashboardVO> dataList = dashboardService.selectList(searchVO);
		
		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.dashboardManage"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// header
		model.addAttribute("dashboardNm", egovMessageSource.getMessage("word.dashboardNm"));	// dashboard
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서
		
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.dashboardNm"));
		
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.dashboardNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "dashboardList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * dashboard 조회
	 * @param	DashboardVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/dashboard/dashboard/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") DashboardVO searchVO) throws Exception {
		return makeJsonData(dashboardService.selectDetail(searchVO));
	}
	
	/**
	 * dashboard 정렬순서저장
	 * @param	DashboardVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/dashboard/dashboard/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") DashboardVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		return makeJsonDataByResultCnt(dashboardService.updateSortOrder(dataVO));
	}
	
	/**
	 * dashboard 삭제
	 * @param	DashboardVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/dashboard/dashboard/deleteDashboard.do")
	public ModelAndView deleteDashboard(@ModelAttribute("dataVO") DashboardVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(dashboardService.deleteDashboard(dataVO));
	}
	
	/**
	 * dashboard 저장
	 * @param	DashboardVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/dashboard/dashboard/saveDashboard.do")
	public ModelAndView saveDashboard(@ModelAttribute("dataVO") DashboardVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		
		UserVO uvo = SessionUtil.getUserVO();
		dataVO.setLoginUserId(uvo != null?uvo.getUserId():null);
		
		
		return makeJsonDataByResultCnt(dashboardService.saveData(dataVO));
	}
}

