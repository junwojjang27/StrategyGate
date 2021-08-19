/*************************************************************************
* CLASS 명	: CompareMetricGrpController
* 작 업 자	: kimyh
* 작 업 일	: 2018-04-27
* 기	능	: 지표POOL별 비교 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018-04-27
**************************************************************************/
package kr.ispark.bsc.mon.compare.compareMetricGrp.web;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.ispark.bsc.mon.compare.compareMetricGrp.service.CompareMetricGrpVO;
import kr.ispark.bsc.mon.compare.compareMetricGrp.service.impl.CompareMetricGrpServiceImpl;
import kr.ispark.common.system.service.impl.CommonServiceImpl;
import kr.ispark.common.web.BaseController;

@Controller
public class CompareMetricGrpController extends BaseController {
	@Autowired
	private CommonServiceImpl commmonService;

	@Autowired
	private CompareMetricGrpServiceImpl compareMetricGrpService;

	/**
	 * 지표POOL별 비교 목록 화면
	 * @param	CompareMetricGrpVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/compare/compareMetricGrp/compareMetricGrpList.do")
	public String compareMetricGrpList(@ModelAttribute("searchVO") CompareMetricGrpVO searchVO, Model model) throws Exception {
		// 지표POOL
		List<CompareMetricGrpVO> metricGrpList = compareMetricGrpService.selectMetricGrpList(searchVO);

		model.addAttribute("searchVO", searchVO);
		model.addAttribute("metricGrpList", metricGrpList);
		return "/bsc/mon/compare/compareMetricGrp/compareMetricGrpList." + searchVO.getLayout();
	}

	/**
	 * 지표POOL 목록
	 * @param	CompareMetricGrpVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/compare/compareMetricGrp/metricGrpList.do")
	public ModelAndView metricGrpList(@ModelAttribute("searchVO") CompareMetricGrpVO searchVO, Model model) throws Exception {
		List<CompareMetricGrpVO> metricGrpList = compareMetricGrpService.selectMetricGrpList(searchVO);
		return makeJsonListData(metricGrpList);
	}

	/**
	 * 평가군별 비교 그리드 조회(json)
	 * @param	CompareMetricGrpVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/compare/compareMetricGrp/compareMetricGrpList_json.do")
	public ModelAndView compareMetricGrpList_json(@ModelAttribute("searchVO") CompareMetricGrpVO searchVO) throws Exception {
		searchVO.setFindMonitoringRootScDeptId(commmonService.selectMonitoringRootScDeptIdForSearch(searchVO));

		List<CompareMetricGrpVO> dataList = compareMetricGrpService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 평가군별 비교 상세 조회
	 * @param	CompareMetricGrpVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/compare/compareMetricGrp/compareMetricGrpDetail.do")
	public ModelAndView compareMetricGrpDetail(@ModelAttribute("searchVO") CompareMetricGrpVO searchVO) throws Exception {
		searchVO.setFindMonitoringRootScDeptId(commmonService.selectMonitoringRootScDeptIdForSearch(searchVO));

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("scoreAnalysis", compareMetricGrpService.selectScoreAnalysis(searchVO));
		resultMap.put("monthlyTrend", compareMetricGrpService.selectMonthlyTrend(searchVO));
		return new ModelAndView("jsonView", resultMap);
	}
}
