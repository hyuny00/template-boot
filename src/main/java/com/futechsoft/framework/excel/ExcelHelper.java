package com.futechsoft.framework.excel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.futechsoft.framework.exception.ErrorCode;
import com.futechsoft.framework.exception.FileDownloadException;
import com.futechsoft.framework.util.FtMap;

@Component
public class ExcelHelper {


	private final int ROW_ACCESS_WINDOW_SIZE = 100;
	private SXSSFWorkbook sxssfWorkbook = null;


	public void excelDownload(HttpServletResponse response, File templateFileName, List<FtMap> list, ExcelColumn excelColumn ) throws IOException, InvalidFormatException {



		ZipSecureFile.setMinInflateRatio(0);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(templateFileName);

	    Sheet originSheet = xssfWorkbook.getSheetAt(0);
	    int rowNo = originSheet.getLastRowNum();

	    // SXSSF 생성
	    sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook, ROW_ACCESS_WINDOW_SIZE);
	    Sheet sheet = sxssfWorkbook.getSheetAt(0);

	    CellStyle  defaultStyle =null;
	    CellStyle  cellStyleContent =null;
	    CellStyle style = null;
	    CellVo cellVos[]= excelColumn.getCellVos();

	    List<CellStyle> cellStyleList = new ArrayList<CellStyle>();

		for(CellVo cellVo  : excelColumn.getCellVos()) {
			defaultStyle = getDefaultStyle();
			cellStyleContent = getCellStyleContent( defaultStyle);

		    if(cellVo.getFormat()!= null) {
				  DataFormat dataformat = sxssfWorkbook.createDataFormat();
				  cellStyleContent.setDataFormat(dataformat.getFormat(cellVo.getFormat()));
			 }

		    style = setAign(cellStyleContent, cellVo);

			cellStyleList.add(style);
		}


	    for(FtMap ftMap : list) {


	        Row row = sheet.createRow(++rowNo);

	        int colIndex=0;
	        Cell cell;
			for(CellVo cellVo : cellVos) {


				style=cellStyleList.get(colIndex);
				cell=row.createCell(colIndex++);
				cell.setCellStyle(style);

				if(cellVo.getCellType() == CellVo.CELL_NUMBER) {
					  cell.setCellValue(ftMap.getDouble(cellVo.getCellColumn()));
					  cell.setCellStyle(style);

				}else if(cellVo.getCellType() == CellVo.CELL_CURRENCY) {
					  cell.setCellValue(ftMap.getDouble(cellVo.getCellColumn()));
					  cell.setCellStyle(style);

				}else if(cellVo.getCellType() == CellVo.CELL_STRING) {
					if(cellVo.getCodeMap()!=null) {
						cell.setCellValue( cellVo.getCodeMap().getString(ftMap.getString(cellVo.getCellColumn())));
					}else {
						cell.setCellValue(ftMap.getString(cellVo.getCellColumn()));
					}
					cell.setCellStyle(style);
				}else {
					cell.setCellValue(ftMap.getString(cellVo.getCellColumn()));
					cell.setCellStyle(style);
				}


			}
	    }

	    ((SXSSFSheet)sheet).flushRows(list.size());

	    response.setHeader("Set-Cookie", "fileDownload=true; path=/");

		response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode( templateFileName.getName(), "utf-8") + "\";");

		try (OutputStream out = response.getOutputStream()) {
			sxssfWorkbook.write(out);
			sxssfWorkbook.dispose();
		} catch (Exception e) {
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			e.printStackTrace();
			throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
		}finally {
			if (sxssfWorkbook != null) {
				sxssfWorkbook.close();
				sxssfWorkbook.dispose();
			}
		}


	}



	public void excelDownload(HttpServletResponse response, File templateFileName, List<FtMap> list, String[] columnValue) throws IOException, InvalidFormatException {
		ZipSecureFile.setMinInflateRatio(0);
	    XSSFWorkbook xssfWorkbook = new XSSFWorkbook(templateFileName);

	    Sheet originSheet = xssfWorkbook.getSheetAt(0);
	    int rowNo = originSheet.getLastRowNum();

	    // SXSSF 생성
	    sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook, ROW_ACCESS_WINDOW_SIZE);
	    Sheet sheet = sxssfWorkbook.getSheetAt(0);

	    CellStyle defaultStyle = getDefaultStyle();
	    CellStyle cellStyleContent = getCellStyleContent( defaultStyle);




	    for(FtMap ftMap : list) {
	        Row row = sheet.createRow(++rowNo);

	        int colIndex=0;
			for(String value : columnValue) {
				Cell cell=row.createCell(colIndex++);
				cell.setCellValue(ftMap.getString(value));
				cell.setCellStyle(cellStyleContent);
			}
	    }

	    ((SXSSFSheet)sheet).flushRows(list.size());

	    response.setHeader("Set-Cookie", "fileDownload=true; path=/");

		response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode( templateFileName.getName(), "utf-8") + "\";");

		try (OutputStream out = response.getOutputStream()) {
			sxssfWorkbook.write(out);
			sxssfWorkbook.dispose();
		} catch (Exception e) {
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			e.printStackTrace();
			throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
		}finally {
			if (sxssfWorkbook != null) {
				sxssfWorkbook.close();
				sxssfWorkbook.dispose();
			}
		}


	}


	private CellStyle getDefaultStyle() {

		 CellStyle defaultStyle = sxssfWorkbook.createCellStyle();
		 defaultStyle.setWrapText(true);
		 return defaultStyle;
	}

	private CellStyle getCellStyleContent(CellStyle cellStyle) {

/*
		Font font = sxssfWorkbook.createFont();
		font.setFontHeightInPoints((short) 9);
		font.setBold(Boolean.TRUE);
		font.setFontName("맑은고딕");

		// 제목 스타일에 폰트 적용, 정렬
		styleHd.setFont(font);

*/
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);

		cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

		 return cellStyle;
	}

	private CellStyle setAign(CellStyle cellStyle,CellVo cellVo) {

		if(cellVo.getAlign() ==CellVo.ALIGN_CENTER ) {
			cellStyle.setAlignment(HorizontalAlignment.CENTER);

		}else if (cellVo.getAlign() ==CellVo.ALIGN_LEFT) {
			cellStyle.setAlignment(HorizontalAlignment.LEFT);

		}else if (cellVo.getAlign() ==CellVo.ALIGN_RIGHT) {
			cellStyle.setAlignment(HorizontalAlignment.RIGHT);

		}else {
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
		}

		return cellStyle;
	}



	public LargeExcel preparedLargeExcel(File templateFileName) throws InvalidFormatException, IOException {
		 ZipSecureFile.setMinInflateRatio(0);
		 XSSFWorkbook xssfWorkbook = new XSSFWorkbook(templateFileName);

		 Sheet originSheet = xssfWorkbook.getSheetAt(0);
		 int rowNo = originSheet.getLastRowNum();

		 SXSSFWorkbook workbook;
		 SXSSFSheet sheet ;

		 workbook =  new SXSSFWorkbook(xssfWorkbook,ROW_ACCESS_WINDOW_SIZE);
		 workbook.setCompressTempFiles(true);

		 sheet = workbook.getSheetAt(0);

		 LargeExcel largeExcel =  new LargeExcel(workbook, sheet, rowNo);
		 return largeExcel;
	}

	public void endLargeExcel(HttpServletResponse response, SXSSFWorkbook workbook, String fileNm) throws Exception {

		response.reset();
		response.setHeader("Set-Cookie", "fileDownload=true; path=/");

		response.setHeader("Content-Disposition", "attachment; filename=\"" +  java.net.URLEncoder.encode( fileNm, "utf-8") + "\";");

		try (OutputStream out = response.getOutputStream()) {
			workbook.write(out);
			workbook.dispose();
		} catch (Exception e) {
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			e.printStackTrace();
			throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
		}finally {
			if (workbook!= null) {
				workbook.close();
				workbook.dispose();
			}
		}
	}

}
