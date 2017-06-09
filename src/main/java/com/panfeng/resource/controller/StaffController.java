package com.panfeng.resource.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.facade.employee.entity.PmsStaff;
import com.paipianwang.pat.facade.employee.service.PmsStaffFacade;
import com.panfeng.resource.view.StaffView;
import com.panfeng.util.Log;

@RestController
@RequestMapping("/portal")
public class StaffController extends BaseController {

	@Autowired
	private final PmsStaffFacade pmsStaffFacade = null;
	
	@RequestMapping("/staff-list")
	public ModelAndView view() {

		return new ModelAndView("staff-list");
	}

	@RequestMapping("/staff/list")
	public DataGrid<PmsStaff> list(final StaffView view, final PageParam param) {

		final long page = param.getPage();
		final long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);

		final DataGrid<PmsStaff> dataGrid = pmsStaffFacade.listWithPagination(param,JsonUtil.objectToMap(view));
		return dataGrid;
	}

	@RequestMapping("/staff/save")
	public void save(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam final MultipartFile staffImage, final PmsStaff staff) {
		response.setContentType("text/html;charset=UTF-8");

		pmsStaffFacade.save(staff);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("save staff ...", sessionInfo);
		processFile(staffImage, staff);

	}

	@RequestMapping("/staff/update")
	public void update(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam final MultipartFile staffImage, final PmsStaff staff) {

		response.setContentType("text/html;charset=UTF-8");
		pmsStaffFacade.update(staff);
		
		final PmsStaff s = pmsStaffFacade.findStaffById(staff.getStaffId());
		final String imagePath = s.getStaffImageUrl();
		if (!staffImage.isEmpty()) {
			// dfs删除文件
			FastDFSClient.deleteFile(imagePath);
			processFile(staffImage, staff);
		}
		
	}

	@RequestMapping("/staff/delete")
	public long delete(final long[] ids, HttpServletRequest request) {

		if (ids != null && ids.length > 1) {
			List<PmsStaff> list = pmsStaffFacade.findStaffsByArray(ids);
			if (ValidateUtil.isValid(list)) {
				for (final PmsStaff staff : list) {
					final String imageUrl = staff.getStaffImageUrl();
					if (ValidateUtil.isValid(imageUrl)) {
						FastDFSClient.deleteFile(imageUrl);
					}
				}
			}
		}

		final long ret = pmsStaffFacade.deleteByArray(ids);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("delete staff ... ids:" + ids.toString(), sessionInfo);
		return ret;
	}

	public void processFile(final MultipartFile staffImage, final PmsStaff staff) {
		if (!staffImage.isEmpty()) {
			// 修改为DFS上传
			String path = FastDFSClient.uploadFile(staffImage);
			staff.setStaffImageUrl(path);
			pmsStaffFacade.updateImagePath(staff);
		}
	}

	// ------------------------- 前台数据接口 ------------------------

	/**
	 * 获取所有人员信息
	 */
	@RequestMapping("/staff/static/list")
	public List<PmsStaff> list(final HttpServletRequest request) {
		List<PmsStaff> list = pmsStaffFacade.getAll();
		return list;
	}
}
