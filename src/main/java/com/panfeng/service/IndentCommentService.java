package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.IndentComment;
import com.panfeng.resource.model.IndentProject;

public interface IndentCommentService {

	long addComment(IndentComment pc);

	List<IndentComment> getIndentCommentList(IndentProject indentProject);

	long removeIndentCommentList(IndentProject indentProject);

	long removeComment(IndentComment indent_Comment);

	long modifyComment(IndentComment indent_Comment);

	void createSystemMsg(String msg, IndentProject indentProject);
}
