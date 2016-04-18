package com.panfeng.persist;

import java.util.List;

import com.panfeng.resource.model.IndentComment;
import com.panfeng.resource.model.IndentProject;

public interface IndentCommentMapper {

	long save(final IndentComment indentComment);

	List<IndentComment> findCommentByIndentId(IndentProject indentProject);

	long delete(final IndentComment indentComment);

	long update(final IndentComment indentComment);

	long removeIndentCommentList(IndentProject indentProject);

}
