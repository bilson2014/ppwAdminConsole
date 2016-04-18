package com.panfeng.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ServletContextAware;

import com.panfeng.dao.RightDao;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.persist.RightMapper;
import com.panfeng.resource.model.Right;
import com.panfeng.resource.model.Tree;
import com.panfeng.resource.view.RightView;
import com.panfeng.service.RightService;
import com.panfeng.service.SessionInfoService;
import com.panfeng.util.ListSort;
import com.panfeng.util.ValidateUtil;

@Service
public class RightServiceImpl implements RightService,ServletContextAware {

	@Autowired
	private final RightMapper mapper = null;
	
	@Autowired
	private final RightDao dao = null;
	
	@Autowired
	private final SessionInfoService sessionService = null;
	
	public ServletContext sc = null;
	
	public List<Right> all() {
		
		final List<Right> list = mapper.all();
		return list;
	}

	public List<Right> listWithPagination(final RightView view) {
		
		final List<Right> list = mapper.listWithPagination(view);
		return list;
	}

	public Right findRightById(final long rightId) {
		
		final Right right = mapper.findRightById(rightId);
		return right;
	}

	@Transactional
	public long save(final Right right) {
		int pos = 0;
		long code = 0;
		final Integer maxPos = mapper.findMaxPos();
		
		if(maxPos == null){
			pos = 0;
			code = 1;
		} else {
			Long maxCode = mapper.findMaxCodeByPos(maxPos);
			if(maxCode >= (1L << 60)){
				pos = maxPos + 1;
				code = 1;
			}else {
				pos = maxPos;
				code = maxCode << 1;
			}
		}
		right.setPos(pos);
		right.setCode(code);
		final long ret = mapper.save(right);
		
		// 更新上下文资源
		dao.addRightByRedis(right);
		
		return ret;
	}

	public long update(final Right right) {
		
		final long ret = mapper.update(right);
		
		// 更新上下文资源
		Map<String,Right> map = mapper.getRights();
		dao.resetRightFromRedis(map);
		
		return ret;
	}

	@Transactional
	public long delete(long[] ids) {
		
		if(ids != null && ids.length > 0){
			for (final long id : ids) {
				mapper.deleteRightRoleLink(id);
				mapper.delete(id);
			}
			
			// 更新上下文资源
			Map<String,Right> map = mapper.getRights();
			dao.resetRightFromRedis(map);
			
			return 1l;
		}
		return 0l;
	}

	public long maxSize(RightView view) {
		
		final long size = mapper.maxSize(view);
		return size;
	}

	public List<Tree> resourceTree() {
		
		final List<Right> rights = mapper.all();
		final List<Tree> trees = new ArrayList<Tree>();
		
		for (final Right right : rights) {
			final Tree tree = new Tree();
			tree.setId(right.getRightId() + "");
			tree.setPid(right.getpId() + "");
			tree.setText(right.getRightName());
			Map<String, Object> attr = new HashMap<String, Object>();
			attr.put("url", right.getUrl());
			tree.setAttributes(attr);
			tree.setIconCls(right.getIcon());
			trees.add(tree);
		}
		return trees;
	}

	/**
	 * 获取权限集合
	 */
	public Map<String, Right> getRights() {
		
		final Map<String,Right> map = mapper.getRights();
		return map;
	}

	public void setServletContext(ServletContext sc) {
		this.sc = sc;
		
	}

	public long getMaxPos() {
		
		final long maxPos = mapper.findMaxPos();
		return maxPos;
	}

	public List<Tree> menu(final HttpServletRequest request) {
		
		// 从session缓存中获取登录信息
		final SessionInfo info = (SessionInfo) sessionService.getSessionWithField(request, GlobalConstant.SESSION_INFO);
		final List<Tree> treeList = new ArrayList<Tree>();
		
		if(info != null){
			if(!info.isSuperAdmin()){
				
				final Set<Long> pSet = new HashSet<Long>();
				
				// 从redis 中获取map
				Map<String,Right> map = dao.getRightsFromRedis();
				
				// 遍历权限集合
				for(Map.Entry<String,Right> entity : map.entrySet()){
					final Right right = entity.getValue();
					if(right != null){
						if(right.getResourceType() == 0){ // 菜单
							if(right.getIsCommon()){
								// 公共资源加入
								final Long pId = right.getpId();
								Tree tree = new Tree();
								tree.setIconCls(right.getIcon());
								tree.setId(right.getRightId() + "");
								tree.setPid(pId.toString());
								tree.setText(right.getRightName());
								tree.setOd(right.getSeq() + "");
								
								Map<String,String> attr = new HashMap<String, String>();
								attr.put("url", right.getUrl());
								tree.setAttributes(attr);
								
								treeList.add(tree);
								if(pId != null){
									pSet.add(pId);
								}
							}else {
								// 非公共资源
								if(info.hasRight(right)){
									// 有权限则加入
									final Long pId = right.getpId();
									Tree tree = new Tree();
									tree.setIconCls(right.getIcon());
									tree.setId(right.getRightId() + "");
									tree.setPid(pId.toString());
									tree.setText(right.getRightName());
									tree.setOd(right.getSeq() + "");
									
									Map<String,String> attr = new HashMap<String, String>();
									attr.put("url", right.getUrl());
									tree.setAttributes(attr);
									
									treeList.add(tree);
									if(pId != null){
										pSet.add(pId);
									}
								}
							}
						}
					}
				}
				
				// 查询没有url的根节点
				if(ValidateUtil.isValid(pSet)){
					// 获取根节点
					final List<Right> list = mapper.findRightByPid(pSet);
					if(ValidateUtil.isValid(list)){
						for (final Right right : list) {
							final Tree tree = new Tree();
							tree.setIconCls(right.getIcon());
							tree.setId(right.getRightId() + "");
							tree.setPid(right.getpId() == null ? null : right.getpId().toString());
							tree.setText(right.getRightName());
							tree.setOd(right.getSeq() + "");
							
							Map<String,String> attr = new HashMap<String, String>();
							attr.put("url", right.getUrl());
							tree.setAttributes(attr);
							
							treeList.add(tree);
						}
						
					}
				}
			
			}else {
				final List<Right> rights = mapper.getAllRightsWithMenu();
				for (final Right right : rights) {
					final Tree tree = new Tree();
					tree.setIconCls(right.getIcon());
					tree.setId(right.getRightId() + "");
					tree.setPid(right.getpId() == null ? null : right.getpId().toString());
					tree.setText(right.getRightName());
					tree.setOd(right.getSeq() + "");
					
					Map<String,String> attr = new HashMap<String, String>();
					attr.put("url", right.getUrl());
					tree.setAttributes(attr);
					
					treeList.add(tree);
				}
			}
		}
		ListSort<Tree> listSort = new ListSort<Tree>();
		listSort.Sort(treeList, "getOd", "asc");
		return treeList;
	}
}
