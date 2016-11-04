package com.panfeng.resource.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.BizBean;
import com.panfeng.resource.model.Employee;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.EmployeeView;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.service.EmployeeService;
import com.panfeng.service.FDFSService;
import com.panfeng.service.SessionInfoService;
import com.panfeng.util.AESUtil;
import com.panfeng.util.DataUtil;
import com.panfeng.util.FileUtils;
import com.panfeng.util.Log;
import com.panfeng.util.ValidateUtil;


/**
 * 权限用户管理  -- 超级管理员
 * @author Jack
 *
 */

@RestController
@RequestMapping("/portal")
public class EmployeeController extends BaseController{

	@Autowired
	private final EmployeeService service = null;
	
	@Autowired
	private final SessionInfoService infoService = null;
	@Autowired
	private final FDFSService fdfsService = null;
	
	/**
	 * 跳转
	 * @return
	 */
	@RequestMapping("/employee-list")
	public ModelAndView view(){
		
		return new ModelAndView("employee-list");
	}
	
	@RequestMapping("/editEmployeePwd")
	public ModelAndView editPwdView(){
		
		return new ModelAndView("editEmployeePassword");
	}
	
	@RequestMapping("/editEmpwd")
	public boolean editPassword(final HttpServletRequest request,final String oldPwd,final String pwd){
		
		final SessionInfo info = (SessionInfo) infoService.getSessionWithField(request, GlobalConstant.SESSION_INFO);
		
		if(info != null){
			// 验证password
			if(ValidateUtil.isValid(oldPwd)){
				try {
					final String ops = AESUtil.Decrypt(oldPwd, GlobalConstant.UNIQUE_KEY);
					final Employee employee = service.doLogin(info.getLoginName(), DataUtil.md5(ops));
					if(employee != null){
						// 验证通过
						// 更新密码
						if(ValidateUtil.isValid(pwd)){
							final String nps = AESUtil.Decrypt(pwd, GlobalConstant.UNIQUE_KEY);
							employee.setEmployeePassword(DataUtil.md5(nps));
							service.editPasswordById(employee);
							Log.error("update Employee password ...", info);
							return true;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 分页查询所有权限用户-超级用户
	 * @return 用户列表
	 */
	@RequestMapping(value = "/employee/list",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
	public DataGrid<Employee> list(final EmployeeView view,final PageFilter pf){
		
		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);
		
		DataGrid<Employee> dataGrid = new DataGrid<Employee>();
		final List<Employee> list = service.listWithPagination(view);
		
		dataGrid.setRows(list);
		final long total = service.maxSize(view);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	/**
	 * 更新权限用户 -- 超级管理员
	 */
	@RequestMapping(value = "/employee/update",method = RequestMethod.POST)
	public void update(final HttpServletRequest request,HttpServletResponse response,
					   @RequestParam final MultipartFile employeeImage,
					   final Employee employee){
		response.setContentType("text/html;charset=UTF-8");
		
		// 判断密码是否为空
		final String password = employee.getEmployeePassword();
		if(ValidateUtil.isValid(password)){
			try {
				final String ps = AESUtil.Decrypt(password, GlobalConstant.UNIQUE_KEY);
				employee.setEmployeePassword(DataUtil.md5(ps));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 获取employee，删除之前的文件
		final Employee e = service.findEmployerById(employee.getEmployeeId());
		final String imagePath = e.getEmployeeImg();
		if(ValidateUtil.isValid(imagePath)){
			//modify by wlc 2016-11-4 12:38:15
			//修改为dfs删除 begin
			//FileUtils.deleteFile(GlobalConstant.FILE_PROFIX + File.separator + imagePath);
			fdfsService.delete(imagePath);
			//修改为dfs删除 end
		}
				
		service.updateWidthRelation(employee);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update Employee info ...", sessionInfo);
		processFile(employeeImage, employee);
		
	}
	
	/**
	 * 增加一个权限用户 -- 超级管理员
	 * @param employee
	 */
	@RequestMapping(value = "/employee/save",method = RequestMethod.POST)
	public void save(final HttpServletRequest request,final HttpServletResponse response,
					@RequestParam final MultipartFile employeeImage,
					final Employee employee){
		response.setContentType("text/html;charset=UTF-8");
		
		// 判断密码是否为空
		final String password = employee.getEmployeePassword();
		if(ValidateUtil.isValid(password)){
			try {
				final String ps = AESUtil.Decrypt(password, GlobalConstant.UNIQUE_KEY);
				employee.setEmployeePassword(DataUtil.md5(ps));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		service.save(employee);
		
		// 处理图像
		processFile(employeeImage,employee);
		
		
	}
	
	/**
	 * 删除一个权限用户 -- 超级管理员
	 */
	@RequestMapping("/employee/delete")
	public long delete(final long[] ids,HttpServletRequest request){
		
		// 删除文件
		for (final long id : ids) {
			final Employee e = service.findEmployerById(id);
			FileUtils.deleteFile(GlobalConstant.FILE_PROFIX + File.separator + e.getEmployeeImg());
		}
		
		final long ret = service.delete(ids);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("delete employee ...  ids:"+ids.toString(), sessionInfo);
		return ret;
	}
	
	public void processFile(final MultipartFile employeeImage,final Employee employee){
		//modify by wlc 2016-11-4 12:42:16
		//修改为dfs上传begin
		String fileId = fdfsService.upload(employeeImage);
		employee.setEmployeeImg(fileId);
		service.updateImagePath(employee);
		//修改为dfs上传end
		/*if(!employeeImage.isEmpty()){
			final String imagePath = GlobalConstant.FILE_PROFIX + GlobalConstant.EMPLOYEE_IMAGE_PATH;
			
			File image = new File(imagePath);
			if(!image.exists())
				image.mkdir();
			
			final String extName = FileUtils.getExtName(employeeImage.getOriginalFilename(), ".");
			final StringBuffer fileName = new StringBuffer();
			fileName.append("employee" + employee.getEmployeeId());
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
				employeeImage.transferTo(dest);
				employee.setEmployeeImg(GlobalConstant.EMPLOYEE_IMAGE_PATH + File.separator + fileName.toString());
				service.updateImagePath(employee);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}*/
	}
	
	
	@RequestMapping(value = "search/employee/list",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
	public List<Employee> searchEmployee(@RequestBody BizBean bizBean){
		if(bizBean != null && bizBean.getName() != null && ! "".equals(bizBean.getName())){
			return service.searchEmployee(bizBean.getName());
		}
		return new ArrayList<>();
	}
	
	/**
	 * 获取内部员工（除admin、测试账号外）
	 * @return
	 */
	@RequestMapping("/getEmployeeList")
	public List<Employee> getEmployeeList(){
		
		final List<Employee> list = service.getEmployeeList();
		return list;
	}
	
	@RequestMapping("/getEmployeeListByReffer")
	public List<Employee> getEmployeeByNameByReffer(@RequestBody BizBean bizBean){
		if(bizBean != null){
			
			final List<Employee> list = service.findEmployeeByRealNameByReffer(bizBean.getName());
			return list;
		}
		return null;
	}
	/**
	 * 获取项目协同人
	 * 目前业务规则:协同人身份为视频管家和视频管家指导
	 * @return employeeList
	 */
	@RequestMapping("/employee/findSynergy")
	public List<Employee> findEmployeeToSynergy(){
		final List<Employee> list = service.findEmployeeToSynergy();
		return list;
	}
}
