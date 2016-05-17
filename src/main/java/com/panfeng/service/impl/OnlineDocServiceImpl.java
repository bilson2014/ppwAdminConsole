package com.panfeng.service.impl;

import java.io.File;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.resource.model.IndentResource;
import com.panfeng.service.IndentResourceService;
import com.panfeng.service.OnlineDocService;
import com.panfeng.util.Constants;
import com.panfeng.util.FileUtils;
import com.panfeng.util.HttpUtil;
import com.panfeng.util.VerifyFileUtils;

@Service
public class OnlineDocServiceImpl implements OnlineDocService {
	@Autowired
	private IndentResourceService indentResourceService;
	String pdf2html = Constants.PDF2HTML;
	//add by laowang 2016.5.17 12:05 begin
	//-->添加转换文件url
	static String CONVERSION_URL=GlobalConstant.CONVERIONHSOT+ "/FileConversion/convert";
	//add by laowang 2016.5.17 12:05 end
	
	public String convertFile(IndentResource indentResource) {
		//modify by laowang 2016.5.17 12:10 begin
		//-->修改操作redis方法
		indentResourceService.saveResourceState(indentResource, OnlineDocService.TRANSFORMATION);
		//modify by laowang 2016.5.17 12:10 end
		new Thread(new Runnable() {
			@Override
			public void run() {
				String fileName = indentResource.getIrFormatName();
				String extName = FileUtils.getExtName(fileName, ".");
				boolean isDoc = VerifyFileUtils.verifyDocFile(extName);
				if (isDoc) {
					File file = indentResourceService.getFile(indentResource);
					String name = fileName.substring(0, fileName.indexOf('.'));
					File output = new File(Constants.FILE_PROFIX
							+ Constants.PROJECT_DOC, name + ".html");
					//构建模拟表单
					MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
							.create();
					multipartEntityBuilder.addBinaryBody("file", file);
				//modify by laowang 2016.5.17 12:10 begin
				//-->修改操作redis方法,增加文件转换请求状态
					boolean res=HttpUtil.httpPostFileForm(CONVERSION_URL,multipartEntityBuilder, output.getAbsolutePath());
					// 添加文件转换失败状态
					if(!res){
						indentResourceService.saveResourceState(indentResource, OnlineDocService.FAIL);
						return ;
					}
					
				}
				indentResourceService.saveResourceState(indentResource, OnlineDocService.FINISH);
				//modify by laowang 2016.5.17 12:15 end
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
				FileUtils.copyFile(file1, file);
			}
			return fileName;
		}
	}

}
