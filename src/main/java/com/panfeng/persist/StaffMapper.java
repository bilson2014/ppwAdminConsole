package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Staff;
import com.panfeng.resource.view.StaffView;

public interface StaffMapper {

	public List<Staff> listWithPagination(final StaffView view);

	public long maxSize(final StaffView view);

	public long save(final Staff staff);

	public long updateImagePath(final Staff staff);

	public Staff findStaffById(@Param("staffId") final long staffId);

	public long update(final Staff staff);

	public long delete(@Param("staffId") final long staffId);
	
	public long deleteByArray(@Param("ids") final long[] ids);

	public List<Staff> findStaffsByArray(@Param("ids") final long[] ids);

	public List<Staff> getAll();

}
