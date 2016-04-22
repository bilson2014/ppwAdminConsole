package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.VersionManager;
import com.panfeng.resource.view.VersionManagerView;

public interface VersionManagerService {

	public List<VersionManager> listWithPagination(final VersionManagerView view);

	public long maxSize(final VersionManagerView view);

	public long update(final VersionManager manager);

	public long save(final VersionManager manager);

	public long delete(final long[] ids);

	public VersionManager doLogin(final String loginName,final String password);

	public long editPassword(final String phoneNumber, final String managerPassword);

	public long checkPhoneNumber(final String phoneNumber);

}
