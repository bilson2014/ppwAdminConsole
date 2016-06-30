package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Employee;
import com.panfeng.resource.model.Role;
import com.panfeng.service.EmployeeService;
import com.panfeng.service.RightService;
import com.panfeng.service.RoleService;
import com.panfeng.service.SessionInfoService;
import com.panfeng.util.DataUtil;
import com.panfeng.util.ValidateUtil;


/**
 * 视频管家管理 
 * @author GY
 *
 */

@RestController
@RequestMapping("/portal")
public class VersionManagerController extends BaseController{

	private static Logger logger = LoggerFactory.getLogger("error");
	
	@Autowired
	private final EmployeeService service = null;
	
	@Autowired
	private final SessionInfoService infoService = null;
	
	@Autowired
	private final RoleService roleService = null;
	
	@Autowired
	private final RightService rightService = null;
	
	/**
	 * 跳转
	 * @return
	 */
	/*@RequestMapping("/manager-list")
	public ModelAndView view(){
		
		return new ModelAndView("manager-list");
	}*/
	
	/**
	 * 分页查询
	 * @return 视频管家列表
	 */
	/*@RequestMapping(value = "/manager/list",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
	public DataGrid<VersionManager> list(final VersionManagerView view,final PageFilter pf){
		
		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);
		
		DataGrid<VersionManager> dataGrid = new DataGrid<VersionManager>();
		final List<VersionManager> list = service.listWithPagination(view);
		
		dataGrid.setRows(list);
		final long total = service.maxSize(view);
		dataGrid.setTotal(total);
		return dataGrid;
	}*/
	
	/**
	 * 更新
	 */
	/*@RequestMapping(value = "/manager/update",method = RequestMethod.POST)
	public void update(final HttpServletRequest request,HttpServletResponse response,
					   final VersionManager manager){
		response.setContentType("text/html;charset=UTF-8");
		
		// 判断密码是否为空
		final String password = manager.getManagerPassword();
		if(ValidateUtil.isValid(password)){
			try {
				final String ps = AESUtil.Decrypt(password, GlobalConstant.UNIQUE_KEY);
				manager.setManagerPassword(DataUtil.md5(ps));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		service.update(manager);
		
	}*/
	
	/**
	 * 新增
	 * @param versionManager
	 */
	/*@RequestMapping(value = "/manager/save",method = RequestMethod.POST)
	public void save(final HttpServletRequest request,final HttpServletResponse response,
					final VersionManager manager){
		response.setContentType("text/html;charset=UTF-8");
		
		// 判断密码是否为空
		final String password = manager.getManagerPassword();
		if(ValidateUtil.isValid(password)){
			try {
				final String ps = AESUtil.Decrypt(password, GlobalConstant.UNIQUE_KEY);
				manager.setManagerPassword(DataUtil.md5(ps));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		service.save(manager);
		
	}*/
	
	/**
	 * 删除
	 */
	/*@RequestMapping("/manager/delete")
	public long delete(final long[] ids){
		
		final long ret = service.delete(ids);
		return ret;
	}*/
	
	// --------------------  前端方法 ----------------
	
	/**
	 * 前端登陆验证
	 */
	@RequestMapping("/manager/static/encipherment")
	public boolean doLogin(final HttpServletRequest request,@RequestBody final Employee employee){
		
		if(employee != null){
			//final VersionManager vManager = service.doLogin(manager.getManagerLoginName(),manager.getManagerPassword());
			final Employee e = service.doLogin(employee.getEmployeeLoginName(), employee.getEmployeePassword());
			if(e != null){
				// 登陆成功
				// 设置权限
				infoService.removeSession(request);
				return initSessionInfo(e,request);
			}
		}
		
		return false;
	}
	
	@RequestMapping("/manager/static/checkNumber/{phoneNumber}")
	public long isNumberExist(@PathVariable("phoneNumber") final String phoneNumber,final HttpServletRequest request){
		
		if(ValidateUtil.isValid(phoneNumber)){
			final long count = service.checkPhoneNumber(phoneNumber);
			return count;
		}
		
		return 0l;
	}
	
	/**
	 * 修改密码
	 */
	@RequestMapping("/manager/static/editPwd")
	public boolean editPassword(final HttpServletRequest request,@RequestBody final Employee e){
		
		if(e != null){
			if(ValidateUtil.isValid(e.getPhoneNumber())){
				// 在视频管家范围内查找该手机号码的人员
				final List<Employee> list = service.getEmployeesWithVersionManager(e.getPhoneNumber());
				if(ValidateUtil.isValid(list)){
					if(list.size() == 1){
						final Employee originalEmployee = list.get(0);
						originalEmployee.setEmployeePassword(e.getEmployeePassword());
						final long ret = service.editPasswordById(originalEmployee);
						if(ret > 0)
							return true;
					}else {
						logger.error("VersionManagerController method:editPassword() error,becase phoneNumber is not unique ...");
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 初始化 sessionInfo 信息
	 */
	public boolean initSessionInfo(final Employee e,final HttpServletRequest request){
		// 存入session中
		final String sessionId = request.getSession().getId();
		final SessionInfo info = new SessionInfo();
		info.setLoginName(e.getEmployeeLoginName());
		info.setRealName(e.getEmployeeRealName());
		info.setSessionType(GlobalConstant.ROLE_EMPLOYEE);
		info.setSuperAdmin(false);
		info.setToken(DataUtil.md5(sessionId));
		info.setReqiureId(e.getEmployeeId());
		info.setPhoto(e.getEmployeeImg());
		
/*		final Role role = roleService.findRoleById(9l); // 获取用户角色
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		manager.setRoles(roles);
		// 计算权限码总和
		final long maxPos = rightService.getMaxPos();
		final long[] rightSum = new long[(int) (maxPos+ 1)];
		manager.setRightSum(rightSum);
		manager.calculateRightSum();
		info.setSum(manager.getRightSum());*/
		
		// 计算权限码
		// 替换带有权限的角色
		final List<Role> roles = new ArrayList<Role>();
		for (final Role r : e.getRoles()) {
			
			final Role role = roleService.findRoleById(r.getRoleId());
			roles.add(role);
		}
		e.setRoles(roles);

		// 计算权限码总和
		final long maxPos = rightService.getMaxPos();
		final long[] rightSum = new long[(int) (maxPos+ 1)];
		
		e.setRightSum(rightSum);
		e.calculateRightSum();
		long[] sum = e.getRightSum();
		info.setSum(sum);
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put(GlobalConstant.SESSION_INFO, info);
		return infoService.addSession(request, map);
	}
}
