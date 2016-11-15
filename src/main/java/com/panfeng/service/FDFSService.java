package com.panfeng.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FDFSService {

	public String upload(final File file,final String fileName);
	
	public void download(final String fileId, final String destFilePath) throws IOException;
	
	public int delete(final String fileId);

	public String upload(final MultipartFile file);

	InputStream download(final String fileId) throws IOException;

	public String upload(final InputStream inputStream, final String fileName);
	
	// 获取最优文件地址服务器
	public String locateFileStoragePath(final String fileId);
}
