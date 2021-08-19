/*************************************************************************
* CLASS 명	: StrategyDiagController
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-05-03
* 기	능	: 전략연계도 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-05-03
**************************************************************************/
package kr.ispark.bsc.mon.orgOutput.strategyDiag.web;

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
import kr.ispark.bsc.mon.orgOutput.strategyDiag.service.impl.StrategyDiagServiceImpl;
import kr.ispark.bsc.mon.orgOutput.strategyDiag.service.StrategyDiagVO;
import kr.ispark.common.util.CommonUtil;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class StrategyDiagController extends BaseController {
	@Autowired
	private StrategyDiagServiceImpl strategyDiagService;
	
	/**
	 * 전략연계도 목록 화면
	 * @param	StrategyDiagVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/strategyDiag/strategyDiagList.do")
	public String strategyDiagList(@ModelAttribute("searchVO") StrategyDiagVO searchVO, Model model) throws Exception {
		
		if("".equals(CommonUtil.nullToBlank(searchVO.getFindAnalCycle()))){
			searchVO.setFindAnalCycle("Y");
		}
		
		List<StrategyDiagVO> strategyList = strategyDiagService.selectStrategyList(searchVO);
		model.addAttribute("strategyList", strategyList);
		
		if("".equals(CommonUtil.nullToBlank(searchVO.getFindStrategyId()))){
			if(strategyList != null && strategyList.size() > 0){
				StrategyDiagVO vo = (StrategyDiagVO)strategyList.get(0);
				searchVO.setFindStrategyId(vo.getStrategyId());
			}
		}
		
		return "/bsc/mon/orgOutput/strategyDiag/strategyDiagList." + searchVO.getLayout();
	}
	
	/**
	 * 전략연계도 그리드 조회(json)
	 * @param	StrategyDiagVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/strategyDiag/strategyDiagList_json.do")
	public ModelAndView strategyDiagList_json(@ModelAttribute("searchVO") StrategyDiagVO searchVO) throws Exception {
		List<StrategyDiagVO> dataList = strategyDiagService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	/**
     * 전략연계도 데이터 조회(json)
     */
    @RequestMapping(value="/bsc/mon/orgOutput/strategyDiag/strategyDiagList_xml")
    public String strategyDiagList_xml(@ModelAttribute("searchVO") StrategyDiagVO searchVO,Model model
    	  )throws Exception {

		List<StrategyDiagVO> list = strategyDiagService.selectList(searchVO);
		if(list != null && list.size() > 0){
			for(StrategyDiagVO pvo : list){pvo.setScore(CommonUtil.getNumberFormat(pvo.getScore()));}
		}
		list = setNodeRemoveCnt(list);

        model.addAttribute("list", list);

        List<StrategyDiagVO> signalList=strategyDiagService.selectSignalList(searchVO);
        EgovMap sinalMap = new EgovMap();
        if(0<signalList.size()){
        	for(int idx_sig = 0 ; idx_sig<signalList.size() ; idx_sig++){
        		StrategyDiagVO svo = (StrategyDiagVO)signalList.get(idx_sig);
        		sinalMap.put(svo.getCodeId().toString(),svo.getColor());
        	}
        }
        model.addAttribute("sinalMap", sinalMap);

        return "/bsc/mon/orgOutput/strategyDiag/strategyDiag_xml";

    }
    
    /**
	 * 전략연계도 그리드 조회(json)
	 * @param	StrategyDiagVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/strategyDiag/strategyList_json.do")
	public ModelAndView strategyList_json(@ModelAttribute("searchVO") StrategyDiagVO searchVO) throws Exception {
		List<StrategyDiagVO> strategyList = strategyDiagService.selectStrategyList(searchVO);
		return makeJsonListData(strategyList);
	}
    
    /**
	 * XML 노드 종료 개수 설정
	 * @param list
	 * @return
	 */
	private  List setNodeRemoveCnt(List list) {
		String beforeFullScDeptId = "";		// 이전 조직 전체코드
		String beforeUpScDeptId = "D";		// 이전 상위 조직코드
		int node_cnt = 0;	//노드 생성 개수
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				StrategyDiagVO vo = (StrategyDiagVO) list.get(i);

				int node_remove_cnt = 0;		// </node> 작성 개수
				node_cnt++;						// 노드 생성

				//이전 조직 전체코드를 현재 조직 전체코드와 비교를 위해 문자열 자르기
				String[] before_full_sc_dept_id_list = beforeFullScDeptId.split(">");
				String[] now_full_sc_dept_id_list = vo.getFullScDeptId().split(">");

				if (beforeUpScDeptId != null && beforeUpScDeptId.equals(vo.getUpScDeptId())) {
					//이전 상위 조직코드와 현재 상위 조직코드가 동일하면 </node> 작성
					node_remove_cnt++;		// 노드 종료
					node_cnt--;		// 노드 종료
				} else {
					// 몇번째 상위 조직코드가 동일한지 비교
					int before_length = before_full_sc_dept_id_list.length;
					int now_length = now_full_sc_dept_id_list.length;
					int length = (before_length > now_length)? now_length:before_length;

					int equal_cnt = 0;
					if (before_length > 0 && now_length > 0) {
						for (int j = 0; j < length; j++) {
							if (now_full_sc_dept_id_list[j].equals(before_full_sc_dept_id_list[j])) {
								equal_cnt = j+1;		// 마지막 동일한 상위 조직 코드 인덱스 번호 가져오기
							}
						}
					}
					// 현재 작성된 노드 개수와 마지막 동일한 상위조직 노드의 차이만큼 노드 종료
					int max_cnt = node_cnt-equal_cnt;
					for (int j = 0; j < max_cnt; j++) {
						node_remove_cnt++;		//노드 종료
						node_cnt--;		//노드 종료
					}
				}

				beforeFullScDeptId = vo.getFullScDeptId();
				beforeUpScDeptId = vo.getUpScDeptId();

				vo.setNodeRemoveCnt(node_remove_cnt);		//노드 종료가 필요한 수
			}
		}

		return list;
	}
	
	//excelDownload
	/**
	 * 엑셀양식다운로드
	 * @param	StrategyDiagVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/strategyDiag/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") StrategyDiagVO searchVO, Model model) throws Exception {
		
		/*
		 * 현재페이지 화면에 맞게 반드시 수정할 것.
		 */
		
		List<StrategyDiagVO> dataList = strategyDiagService.selectList(searchVO);
		
		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.strategyDiagManage"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// header
		model.addAttribute("strategyDiagNm", egovMessageSource.getMessage("word.strategyDiagNm"));	// 전략연계도
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서
		
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.strategyDiagNm"));
		
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.strategyDiagNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "strategyDiagList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * 전략연계도 조회
	 * @param	StrategyDiagVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/strategyDiag/selectDetail.do")
	public ModelAndView selectDetail(@ModelAttribute("searchVO") StrategyDiagVO searchVO) throws Exception {
		return makeJsonData(strategyDiagService.selectDetail(searchVO));
	}
	
	/**
	 * 전략연계도 정렬순서저장
	 * @param	StrategyDiagVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/strategyDiag/saveSortOrder.do")
	public ModelAndView saveSortOrder(@ModelAttribute("dataVO") StrategyDiagVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		// list 유효성 체크
		validateList(dataVO.getGridDataList(), bindingResult);
		if(bindingResult.hasErrors()) {
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		
		return makeJsonDataByResultCnt(strategyDiagService.updateSortOrder(dataVO));
	}
	
	/**
	 * 전략연계도 삭제
	 * @param	StrategyDiagVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/strategyDiag/deleteStrategyDiag.do")
	public ModelAndView deleteStrategyDiag(@ModelAttribute("dataVO") StrategyDiagVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(strategyDiagService.deleteStrategyDiag(dataVO));
	}
	
	/**
	 * 전략연계도 저장
	 * @param	StrategyDiagVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/bsc/mon/orgOutput/strategyDiag/saveStrategyDiag.do")
	public ModelAndView saveStrategyDiag(@ModelAttribute("dataVO") StrategyDiagVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}

		return makeJsonDataByResultCnt(strategyDiagService.saveData(dataVO));
	}
}

