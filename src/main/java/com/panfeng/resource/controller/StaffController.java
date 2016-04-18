package com.panfeng.resource.controller;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.resource.model.Staff;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.resource.view.StaffView;
import com.panfeng.service.StaffService;
import com.panfeng.util.FileUtils;
import com.panfeng.util.ValidateUtil;

@RestController
@RequestMapping("/portal")
public class StaffController extends BaseController {

	@Autowired
	private final StaffService service = null;
	
	@RequestMapping("/staff-list")
	public ModelAndView view(){
		
		return new ModelAndView("staff-list");
	}
	
	@RequestMapping("/staff/list")
	public DataGrid<Staff> list(final StaffView view,final PageFilter pf){
		
		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);
		
		DataGrid<Staff> dataGrid = new DataGrid<Staff>();
		
		final List<Staff> list = service.listWithPagination(view);
		
		dataGrid.setRows(list);
		final long total = service.maxSize(view);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	@RequestMapping("/staff/save")
	public void save(final HttpServletRequest request,final HttpServletResponse response,
					@RequestParam final MultipartFile staffImage,
					final Staff staff){
		response.setContentType("text/html;charset=UTF-8");
		
		service.save(staff);
		
		processFile(staffImage,staff);
		
	}
	
	@RequestMapping("/staff/update")
	public void update(final HttpServletRequest request,final HttpServletResponse response,
			@RequestParam final MultipartFile staffImage,
			final Staff staff){
		
		response.setContentType("text/html;charset=UTF-8");
		
		final Staff s = service.findStaffById(staff.getStaffId());
		final String imagePath = s.getStaffImageUrl();
		if(ValidateUtil.isValid(imagePath)){
			FileUtils.deleteFile(imagePath);
		}
		
		service.update(staff);
		
		processFile(staffImage, staff);
	}
	
	@RequestMapping("/staff/delete")
	public long delete(final long[] ids){
		
		if(ids != null && ids.length > 1){
			List<Staff> list = service.findStaffsByArray(ids);
			if(ValidateUtil.isValid(list)){
				for (final Staff staff : list) {
					final String imageUrl = staff.getStaffImageUrl();
					if(ValidateUtil.isValid(imageUrl)){
						FileUtils.deleteFile(GlobalConstant.FILE_PROFIX + File.separator + imageUrl);
					}
				}
			}
		}
		
		final long ret = service.deleteByArray(ids);
		return ret;
	}
	
	public void processFile(final MultipartFile staffImage,final Staff staff){
		if(!staffImage.isEmpty()){
			final String imagePath = GlobalConstant.FILE_PROFIX + GlobalConstant.STAFF_IMAGE_PATH;
			
			File image = new File(imagePath);
			if(!image.exists())
				image.mkdir();
			
			final String extName = FileUtils.getExtName(staffImage.getOriginalFilename(), ".");
			final StringBuffer fileName = new StringBuffer();
			fileName.append("staff" + staff.getStaffId());
			fileName.append("-");
			final Calendar calendar = new GregorianCalendar();
			fileName.append(calendar.get(Calendar.YEAR));
			fileName.append((calendar.get(Calendar.MONTH) + 1) < 10 ? "0"
					+ (calendar.get(Calendar.MONTH) + 1)
					: (calendar.get(Calendar.MONTH) + 1));
			fileName.append(calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
					+ calendar.get(Calendar.DAY_OF_MONTH)
					: calendar.get(Calendar.DAY_OF_MONTH));
			fileName.append(calendar.get(Calendar.HOUR_OF_DAY));
			fileName.append(calendar.get(Calendar.MINUTE));
			fileName.append(calendar.get(Calendar.SECOND));
			fileName.append(calendar.get(Calendar.MILLISECOND));
			fileName.append(".");
			fileName.append(extName);
			final String path = imagePath + File.separator + fileName.toString();
			
			File dest = new File(path);
			try {
				staffImage.transferTo(dest);
				staff.setStaffImageUrl(GlobalConstant.STAFF_IMAGE_PATH + File.separator + fileName.toString());
				service.updateImagePath(staff);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// ------------------------- 前台数据接口 ------------------------
	
	/**
	 * 获取所有人员信息
	 */
	@RequestMapping("/staff/static/list")
	public List<Staff> list(final HttpServletRequest request){
		List<Staff> list = service.getAll();
		return list;
	}
}
