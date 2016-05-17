package com.panfeng.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.IndentResource;

public interface IndentResourceService {

	List<IndentResource> findIndentList(IndentProject indentProject);

	List<IndentResource> findIndentListByTaskId(ActivitiTask activitiTask);

	boolean addResource(IndentProject indentProject, MultipartFile multipartFile);

	void removeIndentResource(IndentProject indentProject);

	IndentResource findIndentResource(IndentResource indent_Resource);

	void removeResource(IndentResource indent_Resource);

	void updateResource(IndentResource indent_Resource);

	byte[] getBytes(IndentResource indent_Resource);

	InputStream getInputStream(IndentResource indent_Resource);

	File getFile(IndentResource indent_Resource);

	List<File> getPDFFileList();

	List<String> getTags();
	
	void saveResourceState(final IndentResource indentResource, final String state);
	
	void deleteResource(IndentResource indentResource);

}