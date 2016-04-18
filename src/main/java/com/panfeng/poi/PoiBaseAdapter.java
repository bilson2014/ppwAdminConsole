package com.panfeng.poi;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class PoiBaseAdapter<T> {

	public abstract int createHead(XSSFSheet sheet,XSSFWorkbook workbook);

	public abstract void getItemView(XSSFSheet sheet,XSSFWorkbook workbook,T entity,int itemId);

	public abstract List<T> getData();

	
	
	
}
