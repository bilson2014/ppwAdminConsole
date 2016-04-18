package com.panfeng.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.resource.model.IndentResource;
import com.panfeng.service.IndentResourceService;
import com.panfeng.service.OnlineDocService;
import com.panfeng.util.Constants;
import com.panfeng.util.FileUtils;
import com.panfeng.util.PDFConverHtml;
import com.panfeng.util.RuntimeUtils;
import com.panfeng.util.VerifyFileUtils;

@Service
public class OnlineDocServiceImpl implements OnlineDocService {
	@Autowired
	IndentResourceService indentResourceService;

	String pdf2html = Constants.PDF2HTML;

	public String convertFile(IndentResource indentResource) {
		String fileName = indentResource.getIrFormatName();
		String extName = FileUtils.getExtName(fileName, ".");
		boolean isDoc = VerifyFileUtils.verifyDocFile(extName);
		if (isDoc) {
			File file = indentResourceService.getFile(indentResource);
			File pdfFile;
			String name = fileName.substring(0, fileName.indexOf('.'));
			String namepdf = name + ".pdf";
			if (!extName.toLowerCase().equals("pdf")) {
				// 将文件转换至pdf
				pdfFile = new File(Constants.FILE_PROFIX
						+ Constants.PROJECT_PDF, namepdf);
				PDFConverHtml pdfConverHtml = new PDFConverHtml(file, pdfFile);
				pdfConverHtml.conver();
			} else {
				pdfFile = file;
			}

			File output = new File(Constants.FILE_PROFIX
					+ Constants.PROJECT_DOC);
			String command = String.format("%s %s --dest-dir %s", pdf2html,
					pdfFile.getAbsolutePath(), output.getAbsolutePath());
			RuntimeUtils ru = new RuntimeUtils(command);
			ru.start();

			return name + ".html";
		}
		return null;
	}

	public String getFile(IndentResource indentResource) {
		indentResource = indentResourceService
				.findIndentResource(indentResource);
		String fileName = indentResource.getIrFormatName();
		String name = fileName.substring(0, fileName.indexOf('.'));
		File output = new File(Constants.FILE_PROFIX + Constants.PROJECT_DOC);
		File file = new File(output.getAbsolutePath(), name + ".html");
		if (!file.exists())
			convertFile(indentResource);
		return name + ".html";
	}

}
