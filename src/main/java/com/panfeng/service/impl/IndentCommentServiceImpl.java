package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.persist.IndentCommentMapper;
import com.panfeng.resource.model.IndentComment;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.UserViewModel;
import com.panfeng.service.IndentCommentService;
import com.panfeng.service.UserTempService;

@Service
public class IndentCommentServiceImpl implements IndentCommentService {
	@Autowired
	private IndentCommentMapper indent_CommentMapper;
	@Autowired
	private UserTempService userTempService;

	@Override
	public long addComment(IndentComment ic) {
		return indent_CommentMapper.save(ic);
	}
	@Override
	public List<IndentComment> getIndentCommentList(IndentProject indentProject) {
		List<IndentComment> list = indent_CommentMapper
				.findCommentByIndentId(indentProject);
		for (IndentComment indentComment : list) {
			indentComment
					.setUserViewModel(userTempService.getInfo(
							indentComment.getIcUserType(),
							indentComment.getIcUserId()));
		}
		return list;
	}

	@Override
	public long removeComment(IndentComment indent_Comment) {
		return indent_CommentMapper.delete(indent_Comment);
	}

	@Override
	public long modifyComment(IndentComment indent_Comment) {

		return indent_CommentMapper.update(indent_Comment);
	}

	@Override
	public long removeIndentCommentList(IndentProject indentProject) {
		return indent_CommentMapper.removeIndentCommentList(indentProject);
	}
	@Override
	public void createSystemMsg(String msg, IndentProject indentProject) {
		// 创建系统消息
		IndentComment indentComment = new IndentComment();
		indentComment.setIcUserId(888);
		indentComment.setIcUserType(GlobalConstant.ROLE_SYSTEM);
		UserViewModel userViewModel = userTempService.getInfo(
				indentProject.getUserType(), indentProject.getUserId());
		indentComment.setIcContent(userViewModel.getUserName() + "，" + msg);
		indentComment.setIcIndentId(indentProject.getId());
		indent_CommentMapper.save(indentComment);
	}

}
