package com.panfeng.resource.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletOutputStream;
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
import com.panfeng.service.IndentResourceService;

@RestController
public class ResourceController extends BaseController {
	@Autowired
	IndentResourceService indentResourceService;

	@RequestMapping(value = "/addResource")
	public String addResource(@RequestParam final MultipartFile addfile,
			final IndentProject indentProject) {
		return indentResourceService.addResource(indentProject, addfile) + "";
	}

	@RequestMapping(value = "/getResourceList", method = RequestMethod.POST)
	public List<IndentResource> getResourceList(@RequestBody final IndentProject indentProject) {
		return indentResourceService.findIndentList(indentProject);
	}

	@RequestMapping("/getFile/{id}")
	public void getFile(@PathVariable final long id,
			final HttpServletResponse response) {
		IndentResource indentResource=new IndentResource();
		indentResource.setIrId(id);
		indentResource=indentResourceService.findIndentResource(indentResource);
		try {
			byte[] datas = indentResourceService.getBytes(indentResource);
			response.reset();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
			response.setContentLength(datas.length);
			String filename=URLEncoder.encode(indentResource.getIrOriginalName(), "UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+  filename+ "\"\r\n");
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(datas, 0, datas.length);
			ouputStream.flush();
			ouputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping("/getTags")
	public List<String> getTags(){
		return indentResourceService.getTags();
	}
}
