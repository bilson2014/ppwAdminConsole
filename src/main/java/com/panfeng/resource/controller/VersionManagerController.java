package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Role;
import com.panfeng.resource.model.VersionManager;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.resource.view.VersionManagerView;
import com.panfeng.service.RightService;
import com.panfeng.service.RoleService;
import com.panfeng.service.SessionInfoService;
import com.panfeng.service.VersionManagerService;
import com.panfeng.util.AESUtil;
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

	@Autowired
	private final VersionManagerService service = null;
	
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
	@RequestMapping("/manager-list")
	public ModelAndView view(){
		
		return new ModelAndView("manager-list");
	}
	
	/**
	 * 分页查询
	 * @return 视频管家列表
	 */
	@RequestMapping(value = "/manager/list",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
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
	}
	
	/**
	 * 更新
	 */
	@RequestMapping(value = "/manager/update",method = RequestMethod.POST)
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
		
	}
	
	/**
	 * 新增
	 * @param versionManager
	 */
	@RequestMapping(value = "/manager/save",method = RequestMethod.POST)
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
		
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/manager/delete")
	public long delete(final long[] ids){
		
		final long ret = service.delete(ids);
		return ret;
	}
	
	@RequestMapping("/manager/getAll")
	public List<VersionManager> getAll(){
		
		final List<VersionManager> list = service.all();
		return list;
	}
	
	// --------------------  前端方法 ----------------
	
	/**
	 * 前端登陆验证
	 */
	@RequestMapping("/manager/static/encipherment")
	public boolean doLogin(final HttpServletRequest request,@RequestBody final VersionManager manager){
		
		if(manager != null){
			final VersionManager vManager = service.doLogin(manager.getManagerLoginName(),manager.getManagerPassword());
			if(vManager != null){
				// 登陆成功
				// 设置权限
				infoService.removeSession(request);
				return initSessionInfo(vManager,request);
			}
		}
		
		return false;
	}
	
	/**
	 * 更新 视频管家基础信息
	 */
	@RequestMapping("/mamager/static/updateInfo")
	public boolean updateInformation(final HttpServletRequest request,@RequestBody final VersionManager manager){
		
		if(manager != null){
			manager.setManagerPassword(null);
			final long ret = service.update(manager);
			if(ret > 0)
				return true;
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
	public boolean editPassword(final HttpServletRequest request,@RequestBody final VersionManager manager){
		
		if(manager != null){
			final long ret = service.editPassword(manager.getPhoneNumber(),manager.getManagerPassword());
			if(ret > 0)
				return true;
		}
		return false;
	}
	
	/**
	 * 初始化 sessionInfo 信息
	 */
	public boolean initSessionInfo(final VersionManager manager,final HttpServletRequest request){
		// 存入session中
		final String sessionId = request.getSession().getId();
		final SessionInfo info = new SessionInfo();
		info.setLoginName(manager.getManagerLoginName());
		info.setRealName(manager.getManagerRealName());
		info.setSessionType(GlobalConstant.ROLE_MANAGER);
		info.setSuperAdmin(false);
		info.setToken(DataUtil.md5(sessionId));
		info.setReqiureId(manager.getManagerId());
		
		final Role role = roleService.findRoleById(9l); // 获取用户角色
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		manager.setRoles(roles);
		// 计算权限码总和
		final long maxPos = rightService.getMaxPos();
		final long[] rightSum = new long[(int) (maxPos+ 1)];
		manager.setRightSum(rightSum);
		manager.calculateRightSum();
		info.setSum(manager.getRightSum());
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put(GlobalConstant.SESSION_INFO, info);
		return infoService.addSession(request, map);
	}
}
