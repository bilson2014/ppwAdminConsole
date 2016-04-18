package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.VersionManager;
import com.panfeng.resource.view.VersionManagerView;

public interface VersionManagerMapper {

	public List<VersionManager> listWithPagination(final VersionManagerView view);

	public long maxSize(final VersionManagerView view);

	public long update(final VersionManager manager);

	public long save(final VersionManager manager);

	public long delete(@Param("managerId") final long managerId);
	
	public VersionManager findManagerById(@Param("managerId") final long managerId);
	
	public VersionManager doLogin(@Param("loginName") final String loginName,@Param("password") final String pwd);
	
	public long editPassword(@Param("managerId") final long managerId,@Param("password") final String pwd);

}
