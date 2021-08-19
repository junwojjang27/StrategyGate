/*************************************************************************
* CLASS 명	: MetricDiagController
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-03
* 기	능	: 지표연계도 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-03
**************************************************************************/
package kr.ispark.bsc.mon.orgOutput.metricDiag.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.bsc.mon.orgOutput.metricDiag.service.impl.MetricDiagServiceImpl;
import kr.ispark.bsc.mon.orgOutput.strategyDiag.service.StrategyDiagVO;
import kr.ispark.bsc.mon.orgOutput.strategyDiag.service.impl.StrategyDiagServiceImpl;
import kr.ispark.bsc.mon.orgOutput.metricDiag.service.MetricDiagVO;
import kr.ispark.common.util.CommonUtil;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class MetricDiagController extends BaseController {
	@Autowired
	private MetricDiagServiceImpl metricDiagService;
	
	@Autowired
	private StrategyDiagServiceImpl strategyDiagService;
	/**
	 * 지표연계도 목록 화면
	 * @param	MetricDiagVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/metricDiag/metricDiagList.do")
	public String metricDiagList(@ModelAttribute("searchVO") MetricDiagVO searchVO, Model model) throws Exception {
		
		if("".equals(CommonUtil.nullToBlank(searchVO.getFindAnalCycle()))){
			searchVO.setFindAnalCycle("Y");
		}
		
		List<MetricDiagVO> metricList = metricDiagService.selectMetricList(searchVO);
		model.addAttribute("metricList", metricList);
		
		if("".equals(CommonUtil.nullToBlank(searchVO.getFindMetricId()))){
			if(metricList != null && metricList.size() > 0){
				MetricDiagVO vo = (MetricDiagVO)metricList.get(0);
				searchVO.setFindMetricId(vo.getMetricId());
			}
		}
		
		return "/bsc/mon/orgOutput/metricDiag/metricDiagList." + searchVO.getLayout();
	}
	
	/**
	 * 지표연계도 그리드 조회(json)
	 * @param	MetricDiagVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/metricDiag/metricList_json.do")
	public ModelAndView metricList_json(@ModelAttribute("searchVO") MetricDiagVO searchVO) throws Exception {
		List<MetricDiagVO> metricList = metricDiagService.selectMetricList(searchVO);
		return makeJsonListData(metricList);
	}
	
	/**
	 * 지표연계도 그리드 조회(json)
	 * @param	MetricDiagVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/metricDiag/metricDiagList_json.do")
	public ModelAndView metricDiagList_json(@ModelAttribute("searchVO") MetricDiagVO searchVO) throws Exception {
		List<MetricDiagVO> dataList = metricDiagService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
     * 지표연계도 차트데이터 조회(xml)
     */
    @RequestMapping(value="/bsc/mon/orgOutput/metricDiag/metricDiagList_xml.do")
    public String metricDiagList_xml(@ModelAttribute("searchVO") MetricDiagVO searchVO, Model model
    	  )throws Exception {

    	ArrayList list = (ArrayList)metricDiagService.selectList(searchVO);

    	ArrayList list1 = new ArrayList(0);
    	ArrayList list2 = new ArrayList(0);
    	ArrayList list3 = new ArrayList(0);
    	ArrayList list4 = new ArrayList(0);
    	ArrayList list5 = new ArrayList(0);
    	ArrayList list6 = new ArrayList(0);
    	ArrayList list7 = new ArrayList(0);

        if(list.size() > 0){
        	for(int idx=0 ; idx<list.size() ; idx++){
        		MetricDiagVO mvo = (MetricDiagVO) list.get(idx);
        		if(1 == Integer.parseInt(mvo.getLevelNum().toString())){
        			list1.add(mvo);
        		}else if(2 == Integer.parseInt(mvo.getLevelNum().toString())){
        			list2.add(mvo);
        		}else if(3 == Integer.parseInt(mvo.getLevelNum().toString())){
        			list3.add(mvo);
        		}else if(4 == Integer.parseInt(mvo.getLevelNum().toString())){
        			list4.add(mvo);
        		}else if(5 == Integer.parseInt(mvo.getLevelNum().toString())){
        			list5.add(mvo);
        		}else if(6 == Integer.parseInt(mvo.getLevelNum().toString())){
        			list6.add(mvo);
        		}else if(7 == Integer.parseInt(mvo.getLevelNum().toString())){
        			list7.add(mvo);
        		}
        	}
        }

        model.addAttribute("list", list);
        model.addAttribute("list1", list1);
        model.addAttribute("list2", list2);
        model.addAttribute("list3", list3);
        model.addAttribute("list4", list4);
        model.addAttribute("list5", list5);
        model.addAttribute("list6", list6);
        model.addAttribute("list7", list7);

        StrategyDiagVO svo = new StrategyDiagVO();
        svo.setFindYear(searchVO.getFindYear());
        
        ArrayList signalList = (ArrayList)strategyDiagService.selectSignalList(svo);
        EgovMap sinalMap = new EgovMap();
        if(0<signalList.size()){
        	for(int idx_sig = 0 ; idx_sig<signalList.size() ; idx_sig++){
        		StrategyDiagVO vo = (StrategyDiagVO)signalList.get(idx_sig);
        		sinalMap.put(vo.getCodeId().toString(),vo.getColor());
        	}
        }
        model.addAttribute("sinalMap", sinalMap);

        return "/bsc/mon/orgOutput/metricDiag/metricDiag_xml";
    }
	
	//excelDownload
	/**
	 * 엑셀양식다운로드
	 * @param	MetricDiagVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/metricDiag/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") MetricDiagVO searchVO, Model model) throws Exception {
		
		/*
		 * 현재페이지 화면에 맞게 반드시 수정할 것.
		 */
		
		List<MetricDiagVO> dataList = metricDiagService.selectList(searchVO);
		
		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.metricDiagManage"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// header
		model.addAttribute("metricDiagNm", egovMessageSource.getMessage("word.metricDiagNm"));	// 지표연계도
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서
		
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.metricDiagNm"));
		
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.metricDiagNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "metricDiagList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * 지표연계도 조회
	 * @param	MetricDiagVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/metricDiag/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") MetricDiagVO searchVO) throws Exception {
		return makeJsonData(metricDiagService.selectDetail(searchVO));
	}
	
	/**
	 * 지표연계도 정렬순서저장
	 * @param	MetricDiagVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/metricDiag/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") MetricDiagVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		return makeJsonDataByResultCnt(metricDiagService.updateSortOrder(dataVO));
	}
	
	/**
	 * 지표연계도 삭제
	 * @param	MetricDiagVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/metricDiag/deleteMetricDiag.do")
	public ModelAndView deleteMetricDiag(@ModelAttribute("dataVO") MetricDiagVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(metricDiagService.deleteMetricDiag(dataVO));
	}
	
	/**
	 * 지표연계도 저장
	 * @param	MetricDiagVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/metricDiag/saveMetricDiag.do")
	public ModelAndView saveMetricDiag(@ModelAttribute("dataVO") MetricDiagVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		return makeJsonDataByResultCnt(metricDiagService.saveData(dataVO));
	}
}

