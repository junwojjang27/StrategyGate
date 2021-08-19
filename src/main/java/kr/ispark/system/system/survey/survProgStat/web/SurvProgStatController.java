/*************************************************************************
* CLASS 명	: SurvProgStatController
* 작 업 자	: 최학룡
* 작 업 일	: 2018-10-18
* 기	능	: 설문진행현황 Controller
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	최학룡		2018-10-18
**************************************************************************/
package kr.ispark.system.system.survey.survProgStat.web;

import java.util.List;

import kr.ispark.common.CommonVO;
/*공통 컨트롤러*/
import kr.ispark.common.web.BaseController;
import kr.ispark.system.system.survey.survProgStat.service.SurvProgStatVO;
import kr.ispark.system.system.survey.survProgStat.service.impl.SurvProgStatServiceImpl;
import kr.ispark.system.system.survey.survReg.service.SurvRegVO;
import kr.ispark.system.system.survey.survReg.service.impl.SurvRegServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SurvProgStatController extends BaseController {
	@Autowired
	private SurvProgStatServiceImpl survProgStatService;

	@Autowired
	private SurvRegServiceImpl survRegService;

	/**
	 * 설문진행현황 목록 화면
	 * @param	SurvProgStatVO searchVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survProgStat/survProgStatList.do")
	public String survProgStatList(@ModelAttribute("searchVO") SurvProgStatVO searchVO, Model model) throws Exception {
		SurvRegVO vo = new SurvRegVO();
		vo.setFindUseYn("Y");
		List<SurvRegVO> surveyList = survRegService.selectList(vo);
		model.addAttribute("surveyList", surveyList);
		return "/system/system/survey/survProgStat/survProgStatList." + searchVO.getLayout();
	}

	/**
	 * 설문진행현황 그리드 조회(json)
	 * @param	SurvProgStatVO searchVO
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survProgStat/survProgStatList_json.do")
	public ModelAndView survProgStatList_json(@ModelAttribute("searchVO") SurvProgStatVO searchVO) throws Exception {
		List<SurvProgStatVO> dataList = survProgStatService.selectList(searchVO);
		return makeGridJsonData(dataList, dataList.size(), searchVO);
	}

	/**
	 * 메일발송 popup
	 * @param	SurvProgStatVO dataVO
	 * @param	Model model
	 * @return	String
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survProgStat/survPopSendMail.do")
	public String survPopSendMail(@ModelAttribute("dataVO") SurvProgStatVO dataVO, Model model) throws Exception {
		return "/system/system/survey/survProgStat/survPopSendMail." + dataVO.getLayout();
	}
	
	/**
	 * 설문진행현황 메일 발송
	 * @param	SurvProgStatVO dataVO
	 * @param	Model model
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survProgStat/selectSendMail.do")
	public ModelAndView selectSendMail(@ModelAttribute("dataVO") SurvProgStatVO dataVO, Model model) throws Exception {
		return makeJsonDataByResultCnt(survProgStatService.selectSendMail(dataVO.getTitle(), dataVO.getContents(), dataVO));
	}

	/**
	 * 설문진행현황 마감
	 * @param	SurvProgStatVO dataVO
	 * @param	Model model
	 * @param	BindingResult bindingResult
	 * @return	ModelAndView
	 * @throws	Exception
	 */
	@RequestMapping("/system/system/survey/survProgStat/saveSurvProgStat.do")
	public ModelAndView saveSurvProgStat(@ModelAttribute("dataVO") SurvProgStatVO dataVO, Model model, BindingResult bindingResult) throws Exception {
		return makeJsonDataByResultCnt(survProgStatService.saveData(dataVO));
	}
}

