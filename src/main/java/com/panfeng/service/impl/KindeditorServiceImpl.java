package com.panfeng.service.impl;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;
import com.panfeng.service.KindeditorService;
import com.panfeng.util.Constants;
import com.panfeng.util.FileUtils;
import com.panfeng.util.PathFormatUtils;
import com.panfeng.util.VerifyFileUtils;

@Service
public class KindeditorServiceImpl implements KindeditorService {

	// 文件保存目录路径
	final static String SAVE_PATH = Constants.FILE_PROFIX
			+ Constants.PRODUCT_DESCRIPTION_IMAGE_PATH;

	// 获取图片URL
	final static String SAVE_URL = Constants.PRODUCT_DESCRIPTION_IMAGE_URL;

	@Override
	public String getImageUrl(String filename) {
		//return SAVE_URL + filename;
		return filename;
	}

	@Override
	public String saveImage(MultipartFile multipartFile, String sessionid) {
		String fileName = PathFormatUtils.parse("{time}{rand:4}.")
				+ FileUtils.getExtName(multipartFile.getOriginalFilename(),
						null);
		try {
			String path = SAVE_PATH + File.separator + sessionid;
			File dir = new File(path);
			if (!dir.exists())
				dir.mkdirs();
			boolean res = FileUtils.saveFileByInputStream(
					multipartFile.getInputStream(), path +File.separator +fileName);
			if (!res)
				return Constants.FAIL;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sessionid+"/"+fileName;
	}

	@Override
	public String verifyImage(MultipartFile multipartFile, String type) {
		return VerifyFileUtils.verifyFile(multipartFile, type);
	}

	@Override
	public String createMsg(String message, int state) {
		JsonObject jo = new JsonObject();
		jo.addProperty("error", state);
		if (state == Constants.MSG_FAIL)
			jo.addProperty("message", message);
		else
			jo.addProperty("url", message);

		return jo.toString();
	}

	@Override
	public void deleteImageDir(String path) {
		path = SAVE_PATH +File.separator+ path;
		FileUtils.deleteFolderFile(path, true);
	}

}
