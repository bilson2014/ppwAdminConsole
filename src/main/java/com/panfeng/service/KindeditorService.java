package com.panfeng.service;

import org.springframework.web.multipart.MultipartFile;

public interface KindeditorService {

	public String getImageUrl(final String filename);

	public String saveImage(final MultipartFile multipartFile,final String sessionid);

	public String verifyImage(final MultipartFile multipartFile, final String type);

	public String createMsg(final String message,final int state);
	
	public void deleteImageDir(final String path);

}
