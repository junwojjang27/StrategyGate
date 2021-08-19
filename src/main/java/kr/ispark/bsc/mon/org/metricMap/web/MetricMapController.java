/*************************************************************************
* CLASS 명	: MetricMapController
* 작 업 자	: kimyh
* 작 업 일	: 2018. 05. 08.
* 기	능	: 지표연계도 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2018. 05. 08.			최 초 작 업
**************************************************************************/
package kr.ispark.bsc.mon.org.metricMap.web;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.bsc.mon.org.metricMap.service.impl.MetricMapServiceImpl;
import kr.ispark.common.CommonVO;
import kr.ispark.common.web.BaseController;

@Controller
public class MetricMapController extends BaseController {
	
	@Autowired
	private MetricMapServiceImpl metricMapService;
	
	/**
	 * 지표연계도 화면
	 * @param	CommonVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/org/metricMap/metricMap.do")
	public String metricMap(@ModelAttribute("searchVO") CommonVO searchVO, Model model) throws Exception {
		return "/bsc/mon/org/metricMap/metricMap." + searchVO.getLayout();
	}
	
	/**
	 * 지표연계도 조회
	 * @param	CommonVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/org/metricMap/metricMapList.do")
	public ModelAndView metricMapList(@ModelAttribute("searchVO") CommonVO searchVO, Model model) throws Exception {
		HashMap<String, List<EgovMap>> resultMap = new HashMap<String, List<EgovMap>> ();
		resultMap.put("perspectiveList", metricMapService.selectPerspectiveList(searchVO));
		resultMap.put("strategyList", metricMapService.selectStrategyList(searchVO));
		resultMap.put("metricList", metricMapService.selectMetricList(searchVO));
		return new ModelAndView("jsonView", resultMap);
	}
}
