package com.panfeng.test.wang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.panfeng.poi.GenerateExcel;
import com.panfeng.poi.ProjectPoiAdapter;

public class ReportsHeaderTest {
	@Test
	public void test() {
		ProjectPoiAdapter projectPoiAdapter = new ProjectPoiAdapter();
		GenerateExcel ge = new GenerateExcel();
		try {
			OutputStream outputStream=new FileOutputStream(new File("I:\\wangliming\\2016-05-12 周四\\资源\\test.xlsx"));
			ge.generate(projectPoiAdapter,outputStream);
			if(outputStream!=null)
				outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
