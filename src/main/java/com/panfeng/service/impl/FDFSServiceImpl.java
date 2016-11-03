package com.panfeng.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.panfeng.service.FDFSService;
import com.panfeng.util.FastDFSClient;

@Service
public class FDFSServiceImpl implements FDFSService {
	
	@Autowired
	private final FastDFSClient client = null;

	@Override
	public String upload(File file, String fileName) {
		
		final String fileId = client.uploadFile(file, fileName);
		return fileId;
	}
	@Override
	public String upload(MultipartFile file) {
		final String fileId = client.uploadFile(file);
		return fileId;
	}
	@Override
	public String upload(InputStream inputStream, String fileName) {
		final String fileId = client.uploadFile(inputStream,fileName);
		return fileId;
	}
	@Override
	public void download(final String fileId, final String destFilePath) throws IOException {
		
		final InputStream stream = client.downloadFile(fileId);
		final File destFile = new File(destFilePath);
		FileUtils.copyInputStreamToFile(stream, destFile);
	}
	@Override
	public InputStream download(final String fileId) throws IOException {
		return  client.downloadFile(fileId);
	}

	@Override
	public int delete(final String fileId) {
		
		final int result = client.deleteFile(fileId);
		return result;
	}
}
