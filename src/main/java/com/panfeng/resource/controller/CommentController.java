package com.panfeng.resource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.panfeng.resource.model.IndentComment;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.service.IndentCommentService;

@RestController
public class CommentController extends BaseController {
	@Autowired
	IndentCommentService indentCommentService;

	@RequestMapping("/addComment")
	public long addComment(@RequestBody final IndentComment indentComment) {
		return indentCommentService.addComment(indentComment);
	}

	@RequestMapping(value = "/getAllComment", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public List<IndentComment> getAllComment(@RequestBody final IndentProject indentProject) {
		List<IndentComment> list = indentCommentService.getIndentCommentList(indentProject);
		return list;
	}

}
