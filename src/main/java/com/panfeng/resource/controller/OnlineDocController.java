package com.panfeng.resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.panfeng.resource.model.IndentResource;
import com.panfeng.service.OnlineDocService;

@RestController
public class OnlineDocController extends BaseController {
	@Autowired
	OnlineDocService onlineDocService;

	@RequestMapping("/getDocView")
	public String getViewUrl(@RequestBody final IndentResource indentResource) {
		String filename=onlineDocService.getFile(indentResource);
		return "{\"url\":\""+filename+"\"}";
	}
}
