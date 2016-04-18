package com.panfeng.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GenerateExcel {

	private int row = 0;

	@SuppressWarnings("unchecked")
	public File generate(@SuppressWarnings("rawtypes") PoiBaseAdapter adapter) {
		String filepath = "test.xlsx";
		try {
			OutputStream outputStream = new FileOutputStream(filepath);

			// 创建文档
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
			// 创建一个新的页
			XSSFSheet sheet = xssfWorkbook.createSheet("项目");
			// 创建文件头部样式
			row = adapter.createHead(sheet, xssfWorkbook);

			List<?> data = adapter.getData();
			for (int i = 0; i < data.size(); i++) {
				adapter.getItemView(sheet, xssfWorkbook, data.get(i), (i + row));
			}
			xssfWorkbook.write(outputStream);
			xssfWorkbook.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return new File("filepath");
	}
}
