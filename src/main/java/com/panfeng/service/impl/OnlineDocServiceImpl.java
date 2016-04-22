package com.panfeng.service.impl;

import java.io.File;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.resource.model.IndentResource;
import com.panfeng.service.FileStatusService;
import com.panfeng.service.IndentResourceService;
import com.panfeng.service.OnlineDocService;
import com.panfeng.util.Constants;
import com.panfeng.util.FileUtils;
import com.panfeng.util.HttpUtil;
import com.panfeng.util.VerifyFileUtils;

@Service
public class OnlineDocServiceImpl implements OnlineDocService {
	@Autowired
	IndentResourceService indentResourceService;
	@Autowired
	FileStatusService fileStatusService;
	String pdf2html = Constants.PDF2HTML;

	public static final String TRANSFORMATION = "transformation";
	public static final String FINISH = "finish";
	public static final String FAIL = "fail";

	public String convertFile(IndentResource indentResource) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println(indentResource.getIrIndentId()+"~"+indentResource.getIrId());
				fileStatusService.save(FileUtils.getRedisKey(indentResource),
						indentResource.getIrId() + "", TRANSFORMATION);
				String fileName = indentResource.getIrFormatName();
				String extName = FileUtils.getExtName(fileName, ".");
				boolean isDoc = VerifyFileUtils.verifyDocFile(extName);
				if (isDoc) {
					File file = indentResourceService.getFile(indentResource);
					String name = fileName.substring(0, fileName.indexOf('.'));
					File output = new File(Constants.FILE_PROFIX
							+ Constants.PROJECT_DOC, name + ".html");
					MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
							.create();
					multipartEntityBuilder.addBinaryBody("file", file);
					HttpUtil.httpPostFileForm(GlobalConstant.CONVERIONHSOT
							+ "/FileConversion/convert",
							multipartEntityBuilder, output.getAbsolutePath());
				}
				System.out.println(indentResource.getIrIndentId()+"~"+indentResource.getIrId());
				fileStatusService.save(FileUtils.getRedisKey(indentResource),
						indentResource.getIrId() + "", FINISH);
			}
		}).start();

		return "";
	}

	public String getFile(IndentResource indentResource) {
		indentResource = indentResourceService
				.findIndentResource(indentResource);
		String fileName = indentResource.getIrFormatName();

		String Name = indentResource.getIrFormatName();
		String extName = FileUtils.getExtName(Name, ".");
		boolean isDoc = VerifyFileUtils.verifyDocFile(extName);
		if (isDoc) {
			String name = fileName.substring(0, fileName.indexOf('.'));
			return name + ".html";
		} else {
			File output = new File(Constants.FILE_PROFIX
					+ Constants.PROJECT_DOC);
			File file = new File(output.getAbsolutePath(), fileName);
			if (!file.exists()) {
				File file1 = indentResourceService.getFile(indentResource);
				file1.renameTo(file);
			}
			return fileName;
		}
	}

}
