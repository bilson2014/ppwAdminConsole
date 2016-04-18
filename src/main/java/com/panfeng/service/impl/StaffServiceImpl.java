package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.persist.StaffMapper;
import com.panfeng.resource.model.Staff;
import com.panfeng.resource.view.StaffView;
import com.panfeng.service.StaffService;
import com.panfeng.util.ValidateUtil;

@Service
public class StaffServiceImpl implements StaffService {

	@Autowired
	private final StaffMapper mapper = null;
	
	public List<Staff> listWithPagination(final StaffView view) {
		
		final List<Staff> list = mapper.listWithPagination(view);
		return list;
	}

	public long maxSize(final StaffView view) {
		
		final long max = mapper.maxSize(view);
		return max;
	}

	public long save(final Staff staff) {
		
		final long ret = mapper.save(staff);
		return ret;
	}

	public long updateImagePath(final Staff staff) {
		
		final long ret = mapper.updateImagePath(staff);
		return ret;
	}

	public Staff findStaffById(final long staffId) {
		
		final Staff staff = mapper.findStaffById(staffId);
		return staff;
	}

	public long update(final Staff staff) {
		
		final long ret = mapper.update(staff);
		return ret;
	}

	public long delete(final long staffId) {
		
		final long ret = mapper.delete(staffId);
		return ret;
	}

	public List<Staff> findStaffsByArray(final long[] ids) {
		
		final List<Staff> list = mapper.findStaffsByArray(ids);
		return list;
	}

	@Transactional
	public long deleteByArray(final long[] ids) {
		
		if(ValidateUtil.isValid(ids)){
			mapper.deleteByArray(ids);
		}
		return 0l;
	}

	public List<Staff> getAll() {
		
		List<Staff> list = mapper.getAll();
		return list;
	}

}
