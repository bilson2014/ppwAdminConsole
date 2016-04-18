package com.panfeng.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.panfeng.resource.model.Right;
import com.panfeng.resource.model.Tree;
import com.panfeng.resource.view.RightView;

public interface RightService {

	public List<Right> all();
	
	public List<Right> listWithPagination(final RightView view);
	
	public Right findRightById(final long rightId);
	
	public long save(final Right right);
	
	public long update(final Right right);
	
	public long delete(final long[] ids);
	
	public long maxSize(final RightView view);
	
	public List<Tree> resourceTree();

	public Map<String, Right> getRights();

	public long getMaxPos();

	public List<Tree> menu(final HttpServletRequest request);
}
