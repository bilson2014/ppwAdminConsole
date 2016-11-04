package com.panfeng.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FDFSService {

	public String upload(final File file,final String fileName);
	
	public void download(final String fileId, final String destFilePath) throws IOException;
	
	public int delete(final String fileId);

	public String upload(MultipartFile file);

	InputStream download(String fileId) throws IOException;

	public String upload(InputStream inputStream, String fileName);
}
