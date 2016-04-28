package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.persist.VersionManagerMapper;
import com.panfeng.resource.model.VersionManager;
import com.panfeng.resource.view.VersionManagerView;
import com.panfeng.service.VersionManagerService;
import com.panfeng.util.ValidateUtil;

@Service
public class VersionManagerServiceImpl implements VersionManagerService {
	
	@Autowired
	private VersionManagerMapper mapper = null;

	public List<VersionManager> listWithPagination(final VersionManagerView view) {
		
		final List<VersionManager> list = mapper.listWithPagination(view);
		return list;
	}

	public long maxSize(final VersionManagerView view) {
		
		final long total = mapper.maxSize(view);
		return total;
	}

	public long update(final VersionManager manager) {
		
		final long ret = mapper.update(manager);
		return ret;
	}

	public long save(final VersionManager manager) {
		
		final long ret = mapper.save(manager);
		return ret;
	}

	@Transactional
	public long delete(final long[] ids) {
		
		if(ValidateUtil.isValid(ids)){
			for (final long id : ids) {
				mapper.delete(id);
			}
			return 1l;
		}
		
		return 0l;
	}

	public VersionManager doLogin(final String loginName,final String password) {
		 
		final VersionManager manager = mapper.doLogin(loginName, password);
		return manager;
	}

	public long editPassword(final String phoneNumber,final String pwd) {
		
		final long ret = mapper.editPassword(phoneNumber, pwd);
		return ret;
	}

	public long checkPhoneNumber(final String phoneNumber) {
		
		final long ret = mapper.checkPhoneNumber(phoneNumber);
		return ret;
	}

	public List<VersionManager> all() {
		
		final List<VersionManager> list = mapper.all();
		return list;
	}

}
