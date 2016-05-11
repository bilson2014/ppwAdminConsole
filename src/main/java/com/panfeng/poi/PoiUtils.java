package com.panfeng.poi;

import java.awt.Color;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PoiUtils {
	public static XSSFCellStyle getDefaultTitleStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		XSSFFont xssfFont = workbook.createFont();
		xssfFont.setBold(true);
		xssfFont.setColor(new XSSFColor(Color.black));
		xssfFont.setFontHeight(15);
		cellStyle.setFont(xssfFont);
		cellStyle.setBottomBorderColor(new XSSFColor(Color.black));
		return cellStyle;
	}

	public static XSSFCellStyle getDefaultCenterCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBottomBorderColor(new XSSFColor(Color.black));
		return cellStyle;
	}

	public static XSSFCellStyle getDefaultErrorCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		XSSFFont xssfFont = workbook.createFont();
		xssfFont.setColor(new XSSFColor(Color.black));
		cellStyle.setFont(xssfFont);
		cellStyle.setFillBackgroundColor(new XSSFColor(Color.red));
		return cellStyle;
	}

}
