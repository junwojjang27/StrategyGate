package kr.ispark.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import net.sf.jxls.transformer.XLSTransformer;

public class ExcelDownloadView extends AbstractExcelView {
	public final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		XLSTransformer transformer = new XLSTransformer();
		String templateFileName = (String)model.get("templateJxlsFileName");
		String destFileName = (String)model.get("destJxlsFileName");
		//현재 문제는 없는데 다른 환경에서 문제가 생긴다면 global.properties에서 설정하여 절대경로로 할것을 추천함
		String templateFilePath = request.getSession().getServletContext().getRealPath("/WEB-INF/jxls/");
		Workbook resultWorkbook = transformer.transformXLS(new FileInputStream(templateFilePath + "/" + templateFileName), model);
		//Workbook resultWorkbook = transformer.transform(new FileInputStream(templateFilePath+templateFileName), model);

		String sheetName = (String)model.get("sheetName");
		if(sheetName != null && !sheetName.equals("")) {
			resultWorkbook.setSheetName(0, sheetName);
		}
		Object sheetNames = model.get("sheetNames");
		if(sheetNames != null) {
			if(sheetNames instanceof String[]) {
				String[] names = (String[])sheetNames;
				for(int i=0, iLen=Math.min(resultWorkbook.getNumberOfSheets(), names.length); i<iLen; i++) {
					resultWorkbook.setSheetName(i, names[i]);
				}
			} else if(sheetNames instanceof List) {
				List names = (List)sheetNames;
				for(int i=0, iLen=Math.min(resultWorkbook.getNumberOfSheets(), names.size()); i<iLen; i++) {
					resultWorkbook.setSheetName(i, (String)names.get(i));
				}
			}
		}

		// 열 숨김
		try {
			Object hideCols = model.get("hideCols");
			if(hideCols != null) {
				Sheet sheet = resultWorkbook.getSheetAt(0);
				if(hideCols instanceof Integer[]) {
					Integer[] cols = (Integer[])hideCols;
					for(int i=0, iLen=cols.length; i<iLen; i++) {
						sheet.setColumnHidden(cols[i], true);
					}
				} else if(hideCols instanceof Integer) {
					sheet.setColumnHidden((Integer)hideCols, true);
				} else if(hideCols instanceof List) {
					for(Integer idx : (List<Integer>)hideCols) {
						sheet.setColumnHidden(idx, true);
					}
				}
			}
		} catch(NumberFormatException nfe) {
			log.error("error : "+nfe.getCause());
		} catch(Exception e) {
			log.error("error : "+e.getCause());
		}
		
		// 개행문자 처리
		try {
			Object newLineCols = model.get("newLineCols");
			if(newLineCols != null) {
				CellStyle cs = resultWorkbook.createCellStyle();
				Sheet sheet = resultWorkbook.getSheetAt(0);
				Row row;
				Cell cell;
				for(int i=0, iLen=sheet.getPhysicalNumberOfRows(); i<iLen; i++) {
					row = sheet.getRow(i);
					if(row == null) continue;
					
					if(newLineCols instanceof Integer[]) {
						Integer[] cols = (Integer[])newLineCols;
						for(int j=0, jLen=cols.length; j<jLen; j++) {
							cell = row.getCell(cols[i]);
							if(cell != null) {
								cs = cell.getCellStyle();
								cs.setWrapText(true);
							}
						}
					} else if(newLineCols instanceof Integer) {
						cell = row.getCell((Integer)newLineCols);
						if(cell != null) {
							cs = cell.getCellStyle();
							cs.setWrapText(true);
						}
					} else if(newLineCols instanceof List) {
						for(Integer idx : (List<Integer>)newLineCols) {
							cell = row.getCell(idx);
							if(cell != null) {
								cs = cell.getCellStyle();
								cs.setWrapText(true);
							}
						}
					}
				}
			}
		} catch(NumberFormatException nfe) {
			log.error("error : "+nfe.getCause());
		} catch(Exception e) {
			log.error("error : "+e.getCause());
		}

		setDisposition(destFileName, request, response);
		response.setContentType("application/x-msexcel");
		resultWorkbook.write(response.getOutputStream());
	}

	/**
     * 브라우저 구분 얻기.
     *
     * @param request
     * @return
     */
    private String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        } else if (header.indexOf("Trident") > -1) {	// IE11 문자열 깨짐 방지
        	return "Trident";
        }
        return "Firefox";
    }

	/**
     * Disposition 지정하기.
     *
     * @param filename
     * @param request
     * @param response
     * @throws Exception
     */
    private void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String browser = getBrowser(request);

		String dispositionPrefix = "attachment; filename=";
		String encodedFilename = null;

		if (browser.equals("MSIE")) {
		    encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		} else if (browser.equals("Firefox")) {
		    encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Opera")) {
		    encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Chrome")) {
		    StringBuffer sb = new StringBuffer();
		    for (int i = 0; i < filename.length(); i++) {
			char c = filename.charAt(i);
			if (c > '~') {
			    sb.append(URLEncoder.encode("" + c, "UTF-8"));
			} else {
			    sb.append(c);
			}
		    }
		    encodedFilename = sb.toString();
		} else if (browser.equals("Trident")) {		// IE11 문자열 깨짐 방지
	    	encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		} else {
		    //throw new RuntimeException("Not supported browser");
		    throw new IOException("Not supported browser");
		}

		response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);

		if ("Opera".equals(browser)){
		    response.setContentType("application/octet-stream;charset=UTF-8");
		}
    }
}

