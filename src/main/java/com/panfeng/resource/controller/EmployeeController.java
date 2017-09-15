package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.alibaba.fastjson.JSONArray;
import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.facade.indent.entity.IndentSource;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.entity.PmsRole;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.paipianwang.pat.facade.right.service.PmsRoleFacade;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;
import com.panfeng.persist.ActivitiMemberShipMapper;
import com.panfeng.persist.ActivitiUserMapper;
import com.panfeng.resource.model.ActivitiMember;
import com.panfeng.resource.model.ActivitiUser;
import com.panfeng.resource.model.BizBean;
import com.panfeng.resource.view.EmployeeView;
import com.panfeng.util.AESSecurityUtil;
import com.panfeng.util.DataUtil;
import com.panfeng.util.Log;

/**
 * 权限用户管理 -- 超级管理员
 * 
 * @author Jack
 *
 */

@RestController
@RequestMapping("/portal")
public class EmployeeController extends BaseController {

	@Autowired
	private final PmsEmployeeFacade employeeFacade = null;

	@Autowired
	private PmsRoleFacade pmsRoleFacade;
	@Autowired
	private ActivitiUserMapper activitiUserMapper;
	@Autowired
	private ActivitiMemberShipMapper activitiMemberShipMapper;

	/**
	 * 跳转
	 * 
	 * @return
	 */
	@RequestMapping("/employee-list")
	public ModelAndView view() {

		return new ModelAndView("employee-list");
	}

	@RequestMapping("/editEmployeePwd")
	public ModelAndView editPwdView() {

		return new ModelAndView("editEmployeePassword");
	}

	@RequestMapping("/editEmpwd")
	public boolean editPassword(final HttpServletRequest request, final String oldPwd, final String pwd) {

		final SessionInfo info = (SessionInfo) request.getSession().getAttribute(PmsConstant.SESSION_INFO);

		if (info != null) {
			// 验证password
			if (ValidateUtil.isValid(oldPwd)) {
				try {
					final String ops = AESSecurityUtil.Decrypt(oldPwd, PmsConstant.UNIQUE_KEY);
					final PmsEmployee employee = employeeFacade.doLogin(info.getLoginName(), DataUtil.md5(ops));
					if (employee != null) {
						// 验证通过
						// 更新密码
						if (ValidateUtil.isValid(pwd)) {
							final String nps = AESSecurityUtil.Decrypt(pwd, PmsConstant.UNIQUE_KEY);
							employee.setEmployeePassword(DataUtil.md5(nps));
							employeeFacade.updatePwdById(employee);
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
	 * 
	 * @return 用户列表
	 */
	@RequestMapping(value = "/employee/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<PmsEmployee> list(final EmployeeView view, final PageParam param) {

		final long page = param.getPage();
		final long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);

		final DataGrid<PmsEmployee> dataGrid = employeeFacade.listWithPagination(JsonUtil.objectToMap(view), param);
		return dataGrid;
	}

	/**
	 * 更新权限用户 -- 超级管理员
	 */
	@RequestMapping(value = "/employee/update", method = RequestMethod.POST)
	public void update(final HttpServletRequest request, HttpServletResponse response,
			@RequestParam final MultipartFile employeeImage, final PmsEmployee employee,@RequestParam final String groupId) {
		response.setContentType("text/html;charset=UTF-8");

		// 判断密码是否为空
		final String password = employee.getEmployeePassword();
		if (ValidateUtil.isValid(password)) {
			try {
				final String ps = AESSecurityUtil.Decrypt(password, PmsConstant.UNIQUE_KEY);
				employee.setEmployeePassword(DataUtil.md5(ps));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 获取employee，删除之前的文件
		final PmsEmployee e = employeeFacade.findEmployeeById(employee.getEmployeeId());
		

		employeeFacade.updateWidthRelation(employee);
		
		String actid="employee_"+employee.getEmployeeId();
		//更新用户角色
		if(!ValidateUtil.isValid(groupId)){
			//删除
			activitiMemberShipMapper.delete(actid);
			activitiUserMapper.delete(actid);
		}else{
			List<ActivitiMember> roles=activitiMemberShipMapper.findByUserId(actid);
			if(ValidateUtil.isValid(roles)){
				//更新
				ActivitiMember activitiMember=new ActivitiMember();
				activitiMember.setGroupId(groupId);
				activitiMember.setId(actid);
				activitiMemberShipMapper.update(activitiMember);
			}else{
				//添加
				if(!ValidateUtil.isValid(employee.getEmployeeImg())){
					employee.setEmployeeImg(e.getEmployeeImg());
				}
				saveProjectRole(employee, groupId);
			}
		}
		
		
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update Employee info ...", sessionInfo);
		processFile(employeeImage, employee,e);
	}

	/**
	 * 增加一个权限用户 -- 超级管理员
	 * 
	 * @param employee
	 */
	@RequestMapping(value = "/employee/save", method = RequestMethod.POST)
	public void save(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam final MultipartFile employeeImage, final PmsEmployee employee,@RequestParam final String groupId) {
		response.setContentType("text/html;charset=UTF-8");

		// 判断密码是否为空
		final String password = employee.getEmployeePassword();
		if (ValidateUtil.isValid(password)) {
			try {
				final String ps = AESSecurityUtil.Decrypt(password, PmsConstant.UNIQUE_KEY);
				employee.setEmployeePassword(DataUtil.md5(ps));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long employeeId=employeeFacade.save(employee);
		employee.setEmployeeId(employeeId);
		// 处理图像
		processFile(employeeImage, employee,null);
		//添加角色
		if(ValidateUtil.isValid(groupId)){
			saveProjectRole(employee, groupId);
		}
	}
	
	private void saveProjectRole(PmsEmployee employee,String groupId){
		String actid="employee_"+employee.getEmployeeId();
		ActivitiUser activitiUser=new ActivitiUser();
		activitiUser.setEmail(employee.getEmail());
		activitiUser.setFirst(employee.getEmployeeRealName());
		activitiUser.setId(actid);
		activitiUser.setLast(employee.getEmployeeLoginName());
		activitiUser.setPictureId(employee.getEmployeeImg());
		activitiUser.setPwd("000000");
		activitiUser.setRev(1);
		activitiUserMapper.save(activitiUser);
		ActivitiMember activitiMember=new ActivitiMember();
		activitiMember.setGroupId(groupId);
		activitiMember.setId(actid);
		activitiMemberShipMapper.save(activitiMember);
	}

	public void processFile(final MultipartFile employeeImage, final PmsEmployee employee, PmsEmployee old) {
		// modify by wlc 2016-11-4 12:42:16
		// 修改为dfs上传begin
		if (!employeeImage.isEmpty()) {
			if (old!=null && ValidateUtil.isValid(old.getEmployeeImg())) {
				FastDFSClient.deleteFile(old.getEmployeeImg());
			}
			String fileId = FastDFSClient.uploadFile(employeeImage);
			employee.setEmployeeImg(fileId);
			employeeFacade.updateImagePath(employee);
		}
		// 修改为dfs上传end
	}

	/**
	 * 获取内部员工
	 * @param bizBean
	 * @return
	 */
	@RequestMapping(value = "/search/employee/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public List<PmsEmployee> searchEmployee(@RequestBody BizBean bizBean) {
		if (bizBean != null && bizBean.getName() != null && !"".equals(bizBean.getName())) {
			// 根据名字获取 在职状态下的所有员工
			return employeeFacade.findEmployeeByRealNameWithinVersionManager(bizBean.getName());
		}
		return null;
	}

	/**
	 * 获取内部员工（除admin、测试账号外）
	 * 
	 * @return
	 */
	@RequestMapping("/getEmployeeList")
	public List<PmsEmployee> getEmployeeList() {

		final List<PmsEmployee> list = employeeFacade.findEmployeeList();
		return list;
	}

	@RequestMapping("/employee/getAll")
	public List<PmsEmployee> getAll() {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("flag", 1);
		final List<PmsEmployee> list = employeeFacade.findEmployeeByCondition(paramMap);
		return list;
	}
	
	/**
	 * 获取客服
	 * 
	 * @return
	 */
	@RequestMapping("/getCustomerService")
	public List<PmsEmployee> getCustomerService() {
		final List<PmsEmployee> list = employeeFacade.findEmployeeList();
		List<PmsEmployee> rrr = new ArrayList<>();
		if (ValidateUtil.isValid(list)) {
			for (PmsEmployee pmsEmployee : list) {
				List<PmsRole> roles = pmsRoleFacade.findRolesByEmployId(pmsEmployee.getEmployeeId());
				if (ValidateUtil.isValid(roles)) {
					for (PmsRole pmsRole : roles) {
						if (pmsRole.getRoleName().equals("BD专员")) {
							rrr.add(pmsEmployee);
							break;
						}
					}
				}
			}
		}
		return rrr;
	}

	/**
	 * 获取项目协同人 目前业务规则:协同人身份为视频管家和视频管家指导
	 * 
	 * @return employeeList
	 */
	@RequestMapping("/employee/findSynergy")
	public List<PmsEmployee> findEmployeeToSynergy() {
		final List<PmsEmployee> list = employeeFacade.findEmployeeToSynergy();
		return list;
	}

	/**
	 * 获取角色为供应商管家的员工
	 * 
	 * @return
	 */
	@RequestMapping("/employee/findProvider")
	public List<PmsEmployee> findEmployeeToProvider() {
		List<Long> roleId = new ArrayList<Long>();
		// 查找角色为“供应商管家”的员工
		roleId.add(4l);
		final List<PmsEmployee> list = employeeFacade.findEmployeeByRoleId(roleId);
		return list;
	}

	/**
	 * 获取角色为销售的员工
	 * 
	 * @return
	 */
	@RequestMapping("/employee/findSale")
	public List<PmsEmployee> findEmployeeToSale() {
		List<Long> roleId = new ArrayList<Long>();
		// 查找角色为“销售”的员工
		roleId.add(9l);
		final List<PmsEmployee> list = employeeFacade.findEmployeeByRoleId(roleId);
		return list;
	}
}
