package com.panfeng.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.IndentResource;

public interface IndentResourceService {

	public List<IndentResource> findIndentList(IndentProject indentProject);

	public List<IndentResource> findIndentListByTaskId(ActivitiTask activitiTask);

	public boolean addResource(IndentProject indentProject, MultipartFile multipartFile);

	public void removeIndentResource(IndentProject indentProject);

	public IndentResource findIndentResource(IndentResource indent_Resource);

	public void removeResource(IndentResource indent_Resource);

	public void updateResource(IndentResource indent_Resource);

	byte[] getBytes(IndentResource indent_Resource);

	InputStream getInputStream(IndentResource indent_Resource);

	File getFile(IndentResource indent_Resource);
	
	List<File> getPDFFileList();
	
	List<String> getTags();

}