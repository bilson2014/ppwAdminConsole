package com.panfeng.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.fs.impl.LocalResourceImpl;
import com.panfeng.persist.IndentResourceMapper;
import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.IndentComment;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.IndentResource;
import com.panfeng.service.FileStatusService;
import com.panfeng.service.IndentActivitiService;
import com.panfeng.service.IndentCommentService;
import com.panfeng.service.IndentResourceService;
import com.panfeng.service.OnlineDocService;
import com.panfeng.service.UserTempService;
import com.panfeng.util.Constants;
import com.panfeng.util.FileUtils;
import com.panfeng.util.ResourcesType;

/**
 * 订单资源存储服务
 * 
 * @author Wang,LM
 *
 */
@Service
public class IndentResourceServiceImpl implements IndentResourceService {
	@Autowired
	private IndentResourceMapper indent_ResourceMapper;

	@Autowired
	private LocalResourceImpl localResourceImpl;

	@Autowired
	private IndentActivitiService indentActivitiService;
	@Autowired
	private IndentCommentService indentCommentService;

	@Autowired
	private OnlineDocService onlineDocService;

	@Autowired
	private UserTempService userTempService;

	private static ResourcesType resourcesIndentMedia = ResourcesType.INDENT_MEDIA;
	@Autowired
	private FileStatusService fileStatusService;

	@Override
	public List<IndentResource> findIndentList(IndentProject indentProject) {
		List<IndentResource> list = indent_ResourceMapper
				.findResourcetListByIndentId(indentProject);
		// 获取多例的服务
		List<String> key = new ArrayList<>();
		// 获取所有资源文件ID集合
		for (IndentResource indentResource : list) {
			key.add(Long.toString(indentResource.getIrId()));
		}
		// 获取redis 内文件状态集合 -->System.arraycopy();
		
		String[] keyarray = key.toArray(new String[key.size()]);
		List<String> states = fileStatusService.find(
				"r_" + indentProject.getId(), keyarray);
		IndentResource indentResource;
		for (int i = 0; i < list.size(); i++) {
			indentResource=list.get(i);
			// 添加用户信息
			indentResource.setUserViewModel(userTempService.getInfo(
					indentResource.getIrUserType(),
					indentResource.getIrUserId()));
			indentResource.setState(states.get(i));
		}
		return list;
	};

	@Override
	public boolean addResource(IndentProject indentProject,
			MultipartFile multipartFile) {
		try {
			InputStream inputStream = multipartFile.getInputStream();
			String filename = resourcesIndentMedia.getName()
					+ "."
					+ FileUtils.getExtName(multipartFile.getOriginalFilename(),
							".");
			String filepath = formatPath(indentProject, resourcesIndentMedia);
			boolean write = localResourceImpl.writeFile(inputStream, filepath,
					filename);
			if (write) {
				// 添加系统评论
				IndentComment indentComment = new IndentComment();
				indentComment.setIcContent("上传了文件："
						+ multipartFile.getOriginalFilename());
				indentComment.setIcUserType(GlobalConstant.ROLE_SYSTEM);
				indentComment.setIcUserId(888);
				indentComment.setIcIndentId(indentProject.getId());
				indentCommentService.addComment(indentComment);
				// 添加资源信息
				IndentResource resource = new IndentResource();
				resource.setIrOriginalName(multipartFile.getOriginalFilename());
				resource.setIrFormatName(filename);
				resource.setIrIndentId(indentProject.getId());
				resource.setIrtype(indentProject.getTag());
				resource.setIrUserType(indentProject.getUserType());
				resource.setIrUserId(indentProject.getUserId());
				ActivitiTask at = indentActivitiService
						.getCurrentTask(indentProject);
				resource.setIrTaskId(at.getTaskDefinitionKey());
				resource.setIrProcessInstanceId(at.getProcessInstanceId());

				// TODO 添加文件过滤
				indent_ResourceMapper.save(resource);
				// 转换文件
				onlineDocService.convertFile(resource);

				// onlineDocService.convertFile(resource);
			}

			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void removeIndentResource(IndentProject indentProject) {
		long l = indent_ResourceMapper.deleteIndentResourceList(indentProject);
		if (l > 0) {
			localResourceImpl.removeDir(
					formatPath(indentProject, resourcesIndentMedia), true);
		}
	}

	@Override
	public IndentResource findIndentResource(IndentResource indent_Resource) {
		return indent_ResourceMapper.findResourceById(indent_Resource);
	}

	@Override
	public void removeResource(IndentResource indent_Resource) {
		indent_ResourceMapper.delete(indent_Resource);
	}

	@Override
	public void updateResource(IndentResource indent_Resource) {
		indent_ResourceMapper.update(indent_Resource);
	}

	// ///////////////////////////////////////////////////////////////////
	private String formatPath(IndentProject indentProject,
			ResourcesType resourcesType) {
		return resourcesType.getPath() + indentProject.getId() + File.separator;
	}

	private String formatPath(IndentResource indentResource,
			ResourcesType resourcesType) {
		return resourcesType.getPath() + indentResource.getIrIndentId()
				+ File.separator;
	}

	@Override
	public byte[] getBytes(IndentResource indent_Resource) {
		String filepath = formatPath(indent_Resource, resourcesIndentMedia);
		return localResourceImpl.getBytes(filepath,
				indent_Resource.getIrFormatName());
	}

	@Override
	public InputStream getInputStream(IndentResource indent_Resource) {
		String filepath = formatPath(indent_Resource, resourcesIndentMedia);
		return localResourceImpl.getInputStream(filepath,
				indent_Resource.getIrFormatName());
	}

	@Override
	public File getFile(IndentResource indent_Resource) {
		String filepath = formatPath(indent_Resource, resourcesIndentMedia);
		return localResourceImpl.getFile(filepath,
				indent_Resource.getIrFormatName());
	}

	@Override
	public List<IndentResource> findIndentListByTaskId(ActivitiTask activitiTask) {
		return indent_ResourceMapper.findIndentListByTaskId(activitiTask);
	}

	@Override
	public List<File> getPDFFileList() {
		return localResourceImpl.getFileList(Constants.PROJECT_PDF);
	}

	@Override
	public List<String> getTags() {
		List<String> tags = new ArrayList<String>();
		tags.add("需求文档");
		tags.add("Q&A文档");
		tags.add("排期表");
		tags.add("策划方案");
		tags.add("制作导演信息");
		tags.add("分镜头脚本");
		tags.add("花絮");
		tags.add("成片");
		return tags;
	}

}
