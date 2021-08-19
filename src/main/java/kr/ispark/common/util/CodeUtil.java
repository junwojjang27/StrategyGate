/*************************************************************************
* CLASS 명  	: CodeUtil
* 작 업 자  	: 박재현
* 작 업 일  	: 2009.07.15
* 기    능  	: 코드유틸
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박재현		 2009.07.15			  최 초 작 업
*   2    하윤식		 2012.06.25			  년도구분 작업추가
**************************************************************************/
package kr.ispark.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.util.service.CodeVO;
import kr.ispark.common.util.service.impl.CodeUtilServiceImpl;

@Component
public class CodeUtil {
	private static HashMap<String, HashMap<String, ArrayList<CodeVO>>> codeHash = null;

	@Autowired
	private CodeUtilServiceImpl codeUtilService;

	private static CodeUtilServiceImpl staticCodeUtilService;

	@PostConstruct
    public void init() {
		staticCodeUtilService = codeUtilService;
    }

	/**
	 * 공통코드 상세 리스트 조회
	 * @param codeGrpId
	 * @return ArrayList
	*/
	public static List<CodeVO> getCodeList(String codeGrpId) {
		EgovMap emap = new EgovMap();
		emap.put("codeGrpId", codeGrpId);
		emap.put("year", "9999");
		return staticCodeUtilService.selectDBCodeList(emap);
	}

	/**
	 * 공통코드 상세 리스트 조회(년도별)
	 * @param codeGrpId, selected
	 * @return String
	*/
	public static List<CodeVO> getCodeList(String codeGrpId, String year){
		
		boolean yearYn = getYearYn(codeGrpId);
		if(!yearYn) year = "9999";
		
		EgovMap emap = new EgovMap();
		emap.put("codeGrpId", codeGrpId);
		emap.put("year", year);
		return staticCodeUtilService.selectDBCodeList(emap);
	}

	/**
	 * 코드그룹명 조회
	 * @param codeGrpId
	 * @return String
	*/
	public static String getCodeGrpName(String codeGrpId){
		EgovMap emap = new EgovMap();
		emap.put("codeGrpId", codeGrpId);
		emap.put("codeGrpCol", "codeGrpNm");
		return staticCodeUtilService.selectDBCodeGrpDetail(emap);
	}

	/**
	 * 코드그룹 년도별 관리여부 조회
	 * 년도별 관리시 true 반환
	 * @param codeGrpId
	 * @return boolean
	*/
	public static boolean getYearYn(String codeGrpId){
		EgovMap emap = new EgovMap();
		emap.put("codeGrpId", codeGrpId);
		emap.put("codeGrpCol", "yearYn");
		
		if("Y".equals(CommonUtil.nullToBlank(staticCodeUtilService.selectDBCodeGrpDetail(emap)))){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 코드명 조회
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeName(String codeGrpId, String codeId){
		
		boolean yearYn = getYearYn(codeGrpId);
		if(yearYn) return "";
		
		EgovMap emap = new EgovMap();
		emap.put("codeGrpId", codeGrpId);
		emap.put("codeId", codeId);
		emap.put("year", "9999");
		emap.put("codeCol", "codeNm");
		return staticCodeUtilService.selectDBCodeDetail(emap);
	}
	
	/**
	 * etc1 조회
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeEtc1(String codeGrpId, String codeId){
		boolean yearYn = getYearYn(codeGrpId);
		if(yearYn) return "";
		
		EgovMap emap = new EgovMap();
		emap.put("codeGrpId", codeGrpId);
		emap.put("codeId", codeId);
		emap.put("year", "9999");
		emap.put("codeCol", "etc1");
		return staticCodeUtilService.selectDBCodeDetail(emap);
	}
	
	/**
	 * etc2 조회
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeEtc2(String codeGrpId, String codeId){
		boolean yearYn = getYearYn(codeGrpId);
		if(yearYn) return "";
		
		EgovMap emap = new EgovMap();
		emap.put("codeGrpId", codeGrpId);
		emap.put("codeId", codeId);
		emap.put("year", "9999");
		emap.put("codeCol", "etc2");
		return staticCodeUtilService.selectDBCodeDetail(emap);
	}
	
	/**
	 * 비고 조회
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeContent(String codeGrpId, String codeId){
		boolean yearYn = getYearYn(codeGrpId);
		if(yearYn) return "";
		
		EgovMap emap = new EgovMap();
		emap.put("codeGrpId", codeGrpId);
		emap.put("codeId", codeId);
		emap.put("year", "9999");
		emap.put("codeCol", "content");
		return staticCodeUtilService.selectDBCodeDetail(emap);
	}
	
	/**
	 * 코드명 조회(년도별)
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeName(String codeGrpId, String codeId, String year){
		
		boolean yearYn = getYearYn(codeGrpId);
		if(!yearYn) year = "9999";
		
		EgovMap emap = new EgovMap();
		emap.put("codeGrpId", codeGrpId);
		emap.put("codeId", codeId);
		emap.put("year", year);
		emap.put("codeCol", "codeNm");
		return staticCodeUtilService.selectDBCodeDetail(emap);

	}

	/**
	 * etc1 조회(년도별)
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeEtc1(String codeGrpId, String codeId, String year){
		boolean yearYn = getYearYn(codeGrpId);
		if(!yearYn) year = "9999";
		
		EgovMap emap = new EgovMap();
		emap.put("codeGrpId", codeGrpId);
		emap.put("codeId", codeId);
		emap.put("year", year);
		emap.put("codeCol", "etc1");
		return staticCodeUtilService.selectDBCodeDetail(emap);
	}

	/**
	 * etc2 조회(년도별)
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeEtc2(String codeGrpId, String codeId, String year){
		boolean yearYn = getYearYn(codeGrpId);
		if(!yearYn) year = "9999";
		
		EgovMap emap = new EgovMap();
		emap.put("codeGrpId", codeGrpId);
		emap.put("codeId", codeId);
		emap.put("year", year);
		emap.put("codeCol", "etc2");
		return staticCodeUtilService.selectDBCodeDetail(emap);
	}

	/**
	 * 비고 조회(년도별)
	 * @param codeGrpId, codeId
	 * @return String
	*/
	public static String getCodeContent(String codeGrpId, String codeId, String year){
		boolean yearYn = getYearYn(codeGrpId);
		if(!yearYn) year = "9999";
		
		EgovMap emap = new EgovMap();
		emap.put("codeGrpId", codeGrpId);
		emap.put("codeId", codeId);
		emap.put("year", year);
		emap.put("codeCol", "content");
		return staticCodeUtilService.selectDBCodeDetail(emap);
	}

	/*
	 * Map을 value순으로 정렬
	 */
	public static List<String> sortMapByValue(final Map map){
		List<String> list = new ArrayList();
		list.addAll(map.keySet());
		 
		Collections.sort(list,new Comparator(){
			public int compare(Object o1,Object o2){
				Object v1 = map.get(o1);
				Object v2 = map.get(o2);
				 
				return ((Comparable) v1).compareTo(v2);
			}
		});
		return list;
	}
	
	/*
	 * 국가코드 조회
	 */
	public static ArrayList<CodeVO> getCountryList() {
		Locale locale = LocaleContextHolder.getLocale();
		String[] locales = locale.getISOCountries();
		HashMap<String, String> countryMap = new HashMap<String, String>();
		for (String countryCode : locales) {
			Locale obj = new Locale("", countryCode);
			countryMap.put(obj.getISO3Country(), obj.getDisplayCountry(locale));
		}

		// 한글 locale에서 남수단이 번역이 되지 않아서 직접 처리 
		if(CommonUtil.nullToBlank(locale.toString()).equals("ko")) {
			countryMap.put("SSD", "남수단");
		}
		
		List<String> keyList = sortMapByValue(countryMap);

		ArrayList<CodeVO> countryList = new ArrayList<CodeVO>();
		for(String key : keyList) {
			CodeVO vo = new CodeVO();
			vo.setCodeId(key);
			vo.setCodeNm(countryMap.get(key));
			countryList.add(vo);
		}
		return countryList;
	}
}
