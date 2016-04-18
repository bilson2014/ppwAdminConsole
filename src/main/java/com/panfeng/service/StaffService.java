package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.Staff;
import com.panfeng.resource.view.StaffView;

public interface StaffService {

	public List<Staff> listWithPagination(final StaffView view);

	public long maxSize(final StaffView view);

	public long save(final Staff staff);

	public long updateImagePath(final Staff staff);

	public Staff findStaffById(final long staffId);

	public long update(final Staff staff);

	public long delete(final long staffId);

	public List<Staff> findStaffsByArray(final long[] ids);

	public long deleteByArray(final long[] ids);

	public List<Staff> getAll();
	
}
