/*************************************************************************
* CLASS 명	: ExcelParser
* 작 업 자	: kimyh
* 작 업 일	: 2017. 12. 21.
* 기	능	: 엑셀 파싱 유틸. 오류 발생시 ExcelParsingException을 발생
* ---------------------------- 변 경 이 력 --------------------------------
* 번호	작 업 자		작	업	일			변 경 내 용				비고
* ----	---------	----------------	---------------------	-----------
*	1	kimyh		2017. 12. 21.		최 초 작 업
**************************************************************************/
package kr.ispark.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.ispark.common.exception.ExcelParsingException;

@Component
public class ExcelParser {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource(name="egovMessageSource")
	public EgovMessageSource egovMessageSource;

	public static final String FILE_NOT_FOUND = "error1";
	public static final String INVALID_EXCEL_FORMAT = "error2";
	public static final String NO_DATA = "error3";
	public static final String METHOD_NOT_FOUND = "error4";
	public static final String VALIDATION_COUNT_ERROR = "error5";

	public static final String VALIDATION_ERROR = "valiedationError1";
	public static final String VALIDATION_ERROR_TYPE = "valiedationError2";
	public static final String VALIDATION_ERROR_NULL = "valiedationError3";

	public static final int FILE_TYPE_XLS = 0;
	public static final int FILE_TYPE_XLSX = 1;
	public static final int FILE_TYPE_ETC = -1;

	public static final int CELL_TYPE_NUMBER = 0;
	public static final int CELL_TYPE_STRING = 1;
	public static final int CELL_TYPE_FORMULA = 2;
	public static final int CELL_TYPE_NUMBER_NOTNULL = 10;
	public static final int CELL_TYPE_STRING_NOTNULL = 11;
	public static final int CELL_TYPE_FORMULA_NOTNULL = 12;

	public int fileType = FILE_TYPE_ETC;

	@SuppressWarnings("rawtypes")
	public ArrayList excelToList(InputStream fis, int sheetIdx, int startRowIdx, Class cls, String[] names, int[] validations) throws Exception {
		
		if(fis != null){
			Object vo = null;
			String val;
			Double numVal = null;
			Cell cell;
			Method[] m = new Method[names.length];
			Class[] types = new Class[names.length];
			StringBuffer logSb;
			EgovMap map = null;
			
			String classType = "";
			if(cls != null){
				if(cls.getName().contains("VO")){
					classType = "bean";
				}else if(cls.isInstance(new EgovMap())){
					classType = "emap";
				}
			}
			
			System.out.println("###cls.getName() : "+cls.getName());
			System.out.println("###classType : "+classType);

			if(names != null && validations != null
					&& names.length != validations.length) {
				throw new ExcelParsingException(VALIDATION_COUNT_ERROR);
			}
			
			// setter 메소드를 찾아서 Method 배열에 넣음
			for(int i=0, iLen=names.length; i<iLen; i++) {
				if(names[i] == null) continue;
				
				if("bean".equals(classType)){
					m[i] = CommonUtil.findSetMethod(cls, names[i]);
					if(m[i] == null) {
						throw new ExcelParsingException(METHOD_NOT_FOUND);
					}
				}
				types[i] = CommonUtil.getFieldType(cls, names[i]);
			}

			ArrayList list = new ArrayList();
			InputStream is = null;
			try{
				is = fis;
				Workbook wb = WorkbookFactory.create(is);

				if(wb instanceof XSSFWorkbook) {
					fileType = FILE_TYPE_XLSX;
				} else if(wb instanceof HSSFWorkbook) {
					fileType = FILE_TYPE_XLS;
				} else {
					throw new ExcelParsingException(INVALID_EXCEL_FORMAT);
				}

				Sheet sheet = wb.getSheetAt(sheetIdx);
				if(sheet == null || sheetIdx > wb.getNumberOfSheets()-1) {
					throw new ExcelParsingException(NO_DATA);
				}

				log.debug("\n\n\n#### ExcelParser.excelToList ###");

				for(int i=startRowIdx, iLen=sheet.getPhysicalNumberOfRows(); i<iLen; i++) {
					
					if("bean".equals(classType)){
						vo = cls.newInstance();
					}else{
						map = new EgovMap();
					}
					
					
					logSb = new StringBuffer();

					// 비어있는 row인지 체크하여 비어있으면 loop 종료
					if(sheet.getRow(i) == null) break;

					int cellCnt = sheet.getRow(i).getPhysicalNumberOfCells();
					if(names != null && cellCnt > names.length) {
						cellCnt = names.length;
					}

					int emptyCellCnt = 0;
					logSb.append((i - startRowIdx + 1) + " ) ");

					for(int j=0; j<cellCnt; j++) {
						cell = sheet.getRow(i).getCell(j);
						logSb.append(" | " + cell);
						if(cell == null
								|| cell.getCellTypeEnum() == CellType.FORMULA
								|| CommonUtil.removeNull(cell.toString()).trim().equals("")
							) {
							emptyCellCnt++;
						}
					}
					logSb.append(" - (" + emptyCellCnt + " / " + cellCnt + ")");

					// 비어있는 행이면 loop 종료
					if(cellCnt == emptyCellCnt) {
						break;
					}

					//vo = cls.newInstance();
					for(int k=0, kLen=names.length; k<kLen; k++) {
						cell = sheet.getRow(i).getCell(k);
						
						if(cell == null) {
							val = "";
							cell = sheet.getRow(i).createCell(k);
							cell.setCellType(CellType.BLANK);
						} else {
							val = CommonUtil.removeNull(cell.toString()).trim();
						}

						if(validations != null && validations[k] != -1) {
							if((validations[k] == CELL_TYPE_NUMBER_NOTNULL
									|| validations[k] == CELL_TYPE_STRING_NOTNULL
									|| validations[k] == CELL_TYPE_FORMULA_NOTNULL)
									&& val.equals("")) {
								throw new ExcelParsingException(VALIDATION_ERROR_NULL, i);
							}

							try {
								switch(validations[k]) {
									case CELL_TYPE_NUMBER:
									case CELL_TYPE_NUMBER_NOTNULL:
										try {
											if(cell.getCellTypeEnum() != CellType.BLANK) {
												numVal = cell.getNumericCellValue();
											} else {
												numVal = null;
											}
										} catch(RuntimeException re) {
											// 셀 서식이 문자열로 되어있는 경우를 고려해서 처리
											val = cell.getRichStringCellValue().getString();

											if(NumberUtils.isNumber(val)) {
												numVal = Double.parseDouble(val);
											} else {
												throw new ExcelParsingException(VALIDATION_ERROR_TYPE, i);
											}
										} catch(Exception e) {
											// 셀 서식이 문자열로 되어있는 경우를 고려해서 처리
											val = cell.getRichStringCellValue().getString();

											if(NumberUtils.isNumber(val)) {
												numVal = Double.parseDouble(val);
											} else {
												throw new ExcelParsingException(VALIDATION_ERROR_TYPE, i);
											}
										}
										break;
									case CELL_TYPE_STRING:
									case CELL_TYPE_STRING_NOTNULL:
										val = CommonUtil.removeNull(cell.getRichStringCellValue().getString()).trim();
										break;
									case CELL_TYPE_FORMULA:
									case CELL_TYPE_FORMULA_NOTNULL:
										if(cell.getCachedFormulaResultTypeEnum() == CellType.NUMERIC) {
											val = CommonUtil.doubleToString(cell.getNumericCellValue());
										} else if(cell.getCachedFormulaResultTypeEnum() == CellType.STRING) {
											val = cell.getRichStringCellValue().getString();
										}
										break;
									default:
										break;
								}
							} catch(ExcelParsingException epe) {
								throw new ExcelParsingException(VALIDATION_ERROR_TYPE, i);
							} catch(Exception e) {
								throw new ExcelParsingException(VALIDATION_ERROR_TYPE, i);
							}
						}
						
						if("bean".equals(classType)){
							if(m[k] != null) {
								if(validations[k] == CELL_TYPE_NUMBER || validations[k] == CELL_TYPE_NUMBER_NOTNULL) {
									if(types[k] == BigDecimal.class) {
										m[k].invoke(vo, new BigDecimal(numVal));
									} else if(types[k] == String.class) {
										if(numVal != null) {
											m[k].invoke(vo, CommonUtil.doubleToString(numVal));
										}
									} else if(types[k] == Integer.class || types[k] == int.class) {
										m[k].invoke(vo, numVal.intValue());
									} else if(types[k] == Float.class || types[k] == float.class) {
										m[k].invoke(vo, numVal.floatValue());
									} else if(types[k] == Long.class || types[k] == long.class) {
										m[k].invoke(vo, numVal.longValue());
									} else {
										m[k].invoke(vo, numVal);
									}
								} else if(validations[k] == CELL_TYPE_FORMULA || validations[k] == CELL_TYPE_FORMULA_NOTNULL) {
									if(types[k] == BigDecimal.class) {
										m[k].invoke(vo, new BigDecimal(val));
									} else if(types[k] == Double.class || types[k] == double.class) {
										m[k].invoke(vo, Double.parseDouble(val));
									} else if(types[k] == Integer.class || types[k] == int.class) {
										m[k].invoke(vo, Integer.parseInt(val));
									} else if(types[k] == Float.class || types[k] == float.class) {
										m[k].invoke(vo, Float.parseFloat(val));
									} else if(types[k] == Long.class || types[k] == long.class) {
										m[k].invoke(vo, Long.parseLong(val));
									} else {
										m[k].invoke(vo, val);
									}
								} else {
									m[k].invoke(vo, val);
								}
							}
						}else if("emap".equals(classType)){
							if(validations[k] == CELL_TYPE_NUMBER || validations[k] == CELL_TYPE_NUMBER_NOTNULL) {
								if(types[k] == BigDecimal.class) {
									map.put(names[k],new BigDecimal(numVal));
								} else if(types[k] == String.class) {
									if(numVal != null) {
										map.put(names[k],CommonUtil.doubleToString(numVal));
									}
								} else if(types[k] == Integer.class || types[k] == int.class) {
									map.put(names[k],numVal.intValue());
								} else if(types[k] == Float.class || types[k] == float.class) {
									map.put(names[k],numVal.floatValue());
								} else if(types[k] == Long.class || types[k] == long.class) {
									map.put(names[k],numVal.longValue());
								} else {
									map.put(names[k],numVal);
								}
							} else if(validations[k] == CELL_TYPE_FORMULA || validations[k] == CELL_TYPE_FORMULA_NOTNULL) {
								if(types[k] == BigDecimal.class) {
									map.put(names[k],new BigDecimal(val));
								} else if(types[k] == Double.class || types[k] == double.class) {
									map.put(names[k],Double.parseDouble(val));
								} else if(types[k] == Integer.class || types[k] == int.class) {
									map.put(names[k],Integer.parseInt(val));
								} else if(types[k] == Float.class || types[k] == float.class) {
									map.put(names[k],Float.parseFloat(val));
								} else if(types[k] == Long.class || types[k] == long.class) {
									map.put(names[k],Long.parseLong(val));
								} else {
									m[k].invoke(vo, val);
									map.put(names[k],val);
								}
							} else {
								map.put(names[k],val);
							}
						}
						
					}
					
					if("bean".equals(classType)){
						list.add(vo);
					}else{
						list.add(map);
					}
					
					//list.add(vo);
					log.debug(logSb.toString());
				}
				log.debug("#######################\n");

				return list;
			} catch(ExcelParsingException e) {
				String msgCd = "errors.processing";	// 데이터 처리 중 오류가 발생하였습니다.

				/*
				 * ExcelParsing 중 에러에 따른 메시지 설정
				 * - 사용자 화면에 표시될 내용이 아닌 것은 log.error()로 출력
				 */
				switch(e.getMessage()) {
					case FILE_NOT_FOUND:
						log.error("FILE_NOT_FOUND");
						break;
					case INVALID_EXCEL_FORMAT:
						msgCd = "errors.excelFileOnly";	// 엑셀파일만 등록가능합니다.
						break;
					case NO_DATA:
						log.error("NO_DATA - 데이터가 없음");
						break;
					case METHOD_NOT_FOUND:
						log.error("METHOD_NOT_FOUND - vo에 해당 method가 없음");
						break;
					case VALIDATION_COUNT_ERROR:
						log.error("VALIDATION_COUNT_ERROR - validation 설정 수과 열의 수가 다름");
						break;
					case VALIDATION_ERROR:
						msgCd = "errors.excel.validation.error";	// 입력값이 유효하지 않습니다.
						break;
					case VALIDATION_ERROR_TYPE:
						msgCd = "errors.excel.validation.invalidType";	// 입력값의 형식이 잘못되었습니다.
						break;
					case VALIDATION_ERROR_NULL:
						msgCd = "errors.excel.validation.required";	// 필수 입력 항목이 누락되었습니다.
						break;
				}
				throw new ExcelParsingException(egovMessageSource.getMessage(msgCd)
						+ (e.getLineNum() > -1 ?  " (Row : " + (e.getLineNum()+1) + ")": ""));
			} catch(Exception e) {
				log.error("error : "+e.getCause());
				throw e;
			} finally{
				try{
					is.close();
					fis.close();
				}catch(IOException ioe){
					log.error("error : "+ioe.getCause());
				}
			}
		}else{
			throw new FileNotFoundException();
		}
	}

	@SuppressWarnings("rawtypes")
	public ArrayList excelToList(String fileName, int startRowIdx, Class cls, String[] names, int[] validations) throws Exception {
		return excelToList(new FileInputStream(fileName), 0, startRowIdx, cls, names, validations);
	}

	@SuppressWarnings("rawtypes")
	public ArrayList excelToList(String fileName, int sheetIdx, int startRowIdx, Class cls, String[] names, int[] validations) throws Exception {
		return excelToList(new FileInputStream(fileName), sheetIdx, startRowIdx, cls, names, validations);
	}

	@SuppressWarnings("rawtypes")
	public ArrayList excelToList(File f, int startRowIdx, Class cls, String[] names, int[] validations) throws Exception {
		return excelToList(new FileInputStream(f), 0, startRowIdx, cls, names, validations);
	}

	@SuppressWarnings("rawtypes")
	public ArrayList excelToList(File f, int sheetIdx, int startRowIdx, Class cls, String[] names, int[] validations) throws Exception {
		return excelToList(new FileInputStream(f), sheetIdx, startRowIdx, cls, names, validations);
	}

	@SuppressWarnings("rawtypes")
	public ArrayList excelToList(InputStream fis, int startRowIdx, Class cls, String[] names, int[] validations) throws Exception {
		return excelToList(fis, 0, startRowIdx, cls, names, validations);
	}
}
