/*************************************************************************
* CLASS 명	: StrategyMapController
* 작 업 자	: kimyh
* 작 업 일	: 2018. 2. 2.
* 기	능	: 전략체계도 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 2. 2.			최 초 작 업
**************************************************************************/
package kr.ispark.bsc.mon.org.strategyMap.web;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.ispark.bsc.base.scDept.scDeptDiagMng.service.ScDeptDiagMngVO;
import kr.ispark.bsc.base.scDept.scDeptDiagMng.service.impl.ScDeptDiagMngServiceImpl;
import kr.ispark.bsc.base.strategy.strategyMapMng.service.StrategyMapVO;
import kr.ispark.bsc.base.strategy.strategyMapMng.service.impl.StrategyMapMngServiceImpl;
import kr.ispark.bsc.mon.org.scDeptDiag.service.ScDeptDiagVO;
import kr.ispark.bsc.mon.org.scDeptDiag.service.impl.ScDeptDiagServiceImpl;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.web.BaseController;

@Controller
public class StrategyMapController extends BaseController {
	@Autowired
	private CommonServiceImpl commmonService;

	@Autowired
	private ScDeptDiagMngServiceImpl scDeptDiagMngService;

	@Autowired
	private StrategyMapMngServiceImpl strategyMapMngServiceImpl;

	@Autowired
	private ScDeptDiagServiceImpl scDeptDiagService;

	/**
	 * 전략체계도 화면
	 * @param	StrategyMapVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/org/strategyMap/strategyMap.do")
	public String strategyMap(@ModelAttribute("searchVO") StrategyMapVO searchVO, Model model) throws Exception {
		return "/bsc/mon/org/strategyMap/strategyMap." + searchVO.getLayout();
	}

	/**
	 * 비전 & 미션 정보 조회
	 * @param	ScDeptVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/org/strategyMap/getData.do")
	public ModelAndView getData(@ModelAttribute("searchVO") StrategyMapVO searchVO) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("info", strategyMapMngServiceImpl.selectVisionMission(searchVO));
		return new ModelAndView("jsonView", resultMap);
	}

	/**
	 * 전략체계도 데이터 조회
	 */
	@RequestMapping(value="/bsc/mon/org/strategyMap/strategyMap_xml.do")
	public String strategyMapMng_xml(@ModelAttribute("searchVO") StrategyMapVO searchVO, Model model) throws Exception {
		searchVO.setFindMonitoringRootScDeptId(commmonService.selectMonitoringRootScDeptIdForSearch(searchVO));

		List<StrategyMapVO> perspectiveList = strategyMapMngServiceImpl.selectPerspectiveList(searchVO);
		List<StrategyMapVO> strategyList = strategyMapMngServiceImpl.selectList(searchVO);
		List<StrategyMapVO> metricList = strategyMapMngServiceImpl.selectMetricList(searchVO);
		List<StrategyMapVO> arrowList = strategyMapMngServiceImpl.selectArrowList(searchVO);
		String showMetricYn = strategyMapMngServiceImpl.selectShowMetricYn(searchVO);

		model.addAttribute("showMetricYn", showMetricYn);
		model.addAttribute("perspectiveList", perspectiveList);
		model.addAttribute("strategyList", strategyList);
		model.addAttribute("metricList", metricList);
		model.addAttribute("arrowList", arrowList);

		return "/bsc/base/strategy/strategyMapMng/strategyMapMng_xml.xml";
	}

	/**
	 * 신호등 조회
	 * @param	ScDeptDiagVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/org/strategyMap/trafficSignal_xml.do")
	public String trafficSignal_xml(@ModelAttribute("searchVO") ScDeptDiagMngVO searchVO, Model model) throws Exception {
		List<ScDeptDiagMngVO> list = scDeptDiagMngService.getSignal(searchVO);
		model.addAttribute("signalList", list);
		return "/bsc/base/scDept/scDeptDiagMng/trafficSignal_xml.xml";
	}

	/**
	 * 지표 목록 조회
	 * @param	ScDeptDiagVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/bsc/mon/org/strategyMap/selectList.do")
	public ModelAndView selectList(@ModelAttribute("searchVO") ScDeptDiagVO searchVO) throws Exception {
		searchVO.setFindMonitoringRootScDeptId(commmonService.selectMonitoringRootScDeptIdForSearch(searchVO));
		return makeGridJsonData(scDeptDiagService.selectList(searchVO));
	}

	/**
	 * 차트 조회
	 * @param	ScDeptDiagVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping(value="/bsc/mon/org/strategyMap/selectChartData.do")
	public ModelAndView selectChartData(@ModelAttribute("searchVO") ScDeptDiagVO searchVO) throws Exception {
		searchVO.setFindMonitoringRootScDeptId(commmonService.selectMonitoringRootScDeptIdForSearch(searchVO));
		return makeJsonListData(scDeptDiagService.selectChartData(searchVO));
	}
}
