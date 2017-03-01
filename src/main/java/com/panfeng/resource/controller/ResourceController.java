package com.panfeng.resource.controller;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.IndentResource;
import com.panfeng.service.FDFSService;
import com.panfeng.service.IndentResourceService;
import com.panfeng.util.HttpUtil;

@RestController
public class ResourceController extends BaseController {
	@Autowired
	private IndentResourceService indentResourceService = null;
	@Autowired
	private FDFSService fdfsService = null;

	@RequestMapping(value = "/addResource")
	public String addResource(@RequestParam final MultipartFile addfile, final IndentProject indentProject) {
		return indentResourceService.addResource(indentProject, addfile) + "";
	}

	@RequestMapping(value = "/getResourceList", method = RequestMethod.POST)
	public List<IndentResource> getResourceList(@RequestBody final IndentProject indentProject) {
		return indentResourceService.findIndentList(indentProject);
	}

	@RequestMapping("/getTags")
	public List<String> getTags() {
		return indentResourceService.getTags();
	}

	@RequestMapping("/getIndentResource/{id}")
	public IndentResource getIndentResource(@PathVariable final long id, final HttpServletResponse response) {
		IndentResource indentResource = new IndentResource();
		indentResource.setIrId(id);
		indentResource = indentResourceService.findIndentResource(indentResource);
		return indentResource;
	}

	@RequestMapping("/getDFSFile/{id}")
	public void getDFSFile(@PathVariable final long id, final HttpServletResponse response,
			final HttpServletRequest request) {
		IndentResource indentResource = new IndentResource();
		indentResource.setIrId(id);
		indentResource = indentResourceService.findIndentResource(indentResource);
		try {
			InputStream in = fdfsService.download(indentResource.getIrFormatName());
			// 此处设置文件大小
			// System.err.println(indentResource.getIrOriginalName() + " 文件大小为:
			// " + in.available());
			// response.setContentLength(in.available());
			ServletOutputStream ouputStream = response.getOutputStream();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
			String filename = URLEncoder.encode(indentResource.getIrOriginalName(), "UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"\r\n");
			// send file
			HttpUtil.saveTo(in, ouputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
