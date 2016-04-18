package com.panfeng.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件验证工具
 * 
 * @author WangLM
 *
 */
public final class VerifyFileUtils {
	// 定义允许上传的文件扩展名
	private final static HashMap<String, String> extMap = new HashMap<String, String>();
	private final static List<String> extDocList = new ArrayList<>();
	static {
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");

		extDocList.add("doc");
		extDocList.add("docx");
		extDocList.add("xls");
		extDocList.add("xlsx");
		extDocList.add("ppt");
		extDocList.add("pptx");
		extDocList.add("pdf");
	}

	private final static long DEFAULT_FILE_MAX_SIZE = 250 * 1024;

	public static boolean verifyDocFile(String extName) {
		extName = extName.toLowerCase();
		return extDocList.contains(extName);
	}

	/**
	 * 验证文件
	 * 
	 * @param multipartFile
	 *            上传包装文件
	 * @param type
	 *            文件类型
	 * @param size
	 *            文件大小
	 * @return 返回结果“”为正常
	 */
	public static String verifyFile(MultipartFile multipartFile, String type,
			long size) {
		if (multipartFile == null)
			return "请选择上传文件。";
		if (multipartFile.getSize() > size)
			return "上传文件超出大小。";
		String originalExtName = FileUtils.getExtName(
				multipartFile.getOriginalFilename(), null);
		String ExtName = extMap.get(type);
		if (!ExtName.contains(originalExtName))
			return "不允许上传此类型文件。";

		return "";
	}

	/**
	 * 验证文件 默认文件大小250KB
	 * 
	 * @param multipartFile
	 *            上传包装文件
	 * @param type
	 *            文件类型
	 * @return 返回结果“”为正常
	 */
	public static String verifyFile(MultipartFile multipartFile, String type) {
		return verifyFile(multipartFile, type, DEFAULT_FILE_MAX_SIZE);
	}
}
