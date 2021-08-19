/*************************************************************************
* CLASS 명	: DeployDataController
* 작 업 자	: 현걸욱
* 작 업 일	: 2018-07-09
* 기	능	: 고객사별 전년데이터 일괄적용 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	현걸욱		2018-07-09
**************************************************************************/
package kr.ispark.superMng.superMng.superMng.deployData.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.let.utl.fcc.service.EgovStringUtil;
import kr.ispark.superMng.superMng.superMng.deployData.service.impl.DeployDataServiceImpl;
import kr.ispark.superMng.superMng.superMng.deployData.service.DeployDataVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;

@Controller
public class DeployDataController extends BaseController {
	@Autowired
	private DeployDataServiceImpl deployDataService;
	
	/**
	 * 고객사별 전년데이터 일괄적용 목록 화면
	 * @param	DeployDataVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/deployData/deployDataList.do")
	public String deployDataList(@ModelAttribute("searchVO") DeployDataVO searchVO, Model model) throws Exception {
		return "/superMng/superMng/superMng/deployData/deployDataList." + searchVO.getLayout();
	}
	
	/**
	 * 고객사별 전년데이터 일괄적용 그리드 조회(json)
	 * @param	DeployDataVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/deployData/deployDataList_json.do")
	public ModelAndView deployDataList_json(@ModelAttribute("searchVO") DeployDataVO searchVO) throws Exception {
		List<DeployDataVO> dataList = deployDataService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}
	
	//excelDownload
	/**
	 * 엑셀양식다운로드
	 * @param	DeployDataVO searchVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/deployData/excelDownload.do")
	public String excelDownload(@ModelAttribute("searchVO") DeployDataVO searchVO, Model model) throws Exception {
		
		/*
		 * 현재페이지 화면에 맞게 반드시 수정할 것.
		 */
		
		List<DeployDataVO> dataList = deployDataService.selectList(searchVO);
		
		// 타이틀
		model.addAttribute("title", egovMessageSource.getMessage("word.deployDataManage"));
		
		// 검색조건
		model.addAttribute("year", egovMessageSource.getMessage("word.year"));
		model.addAttribute("findYear", searchVO.getFindYear());
		
		// header
		model.addAttribute("deployDataNm", egovMessageSource.getMessage("word.deployDataNm"));	// 고객사별 전년데이터 일괄적용
		model.addAttribute("sortOrder", egovMessageSource.getMessage("word.sortOrder"));			// 정렬순서
		
		// 조직 데이터
		model.addAttribute("dataList", dataList);
		
		// 시트명
		model.addAttribute("sheetName", egovMessageSource.getMessage("word.deployDataNm"));
		
		model.addAttribute("destJxlsFileName", egovMessageSource.getMessage("word.deployDataNm") + "_" + EgovStringUtil.getTimeStamp()+".xlsx");
		model.addAttribute("templateJxlsFileName", "deployDataList.xlsx");

		return "excelDownloadView";
	}

	/**
	 * 고객사별 전년데이터 일괄적용 저장
	 * @param	DeployDataVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/superMng/superMng/superMng/deployData/saveDeployData.do")
	public ModelAndView saveDeployData(@ModelAttribute("dataVO") DeployDataVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		/*
		beanValidator.validate(dataVO, bindingResult);
		if(bindingResult.hasErrors()){
			return makeFailJsonData(getListErrorMsg(bindingResult));
		}
		*/

		return makeJsonDataByResultCnt(deployDataService.saveData(dataVO));
	}
}

