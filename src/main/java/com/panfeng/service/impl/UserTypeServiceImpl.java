package com.panfeng.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.panfeng.persist.TeamMapper;
import com.panfeng.persist.UserMapper;
import com.panfeng.resource.model.Team;
import com.panfeng.resource.model.User;
import com.panfeng.resource.model.UserViewModel;
import com.panfeng.service.UserTempService;

@Service
public class UserTypeServiceImpl implements UserTempService {
	@Autowired
	private final UserMapper userMapper = null;
	
	@Autowired
	private final TeamMapper teamMapper = null;
	
	@Autowired
	private final PmsEmployeeFacade pmsEmployeeFacade = null;

	// 创建对象
	public UserViewModel buildObject(String userType, long userId) {
		UserViewModel userViewModel = new UserViewModel();
		switch (userType) {
		case PmsConstant.ROLE_EMPLOYEE:
			// 视频管家
			PmsEmployee employee = pmsEmployeeFacade.findEmployeeById(userId);
			// modify by wanglc 2016-7-18 13:59:19 视频管家为空 begin
			if (null != employee) {
				userViewModel
						.setUserName(employee.getEmployeeRealName() == null || employee.getEmployeeRealName().equals("")
								? "内部员工" : employee.getEmployeeRealName());
				String imgUrl = employee.getEmployeeImg();
				if (imgUrl != null && !"".equals(imgUrl)) {
					userViewModel.setImgUrl(imgUrl);
					//修改为fdfs路径end
				} else
					userViewModel.setImgUrl("/resources/images/flow/guanhead.png");
			} else
				userViewModel.setImgUrl("/resources/images/flow/guanhead.png");
			// modify by wanglc 2016-7-18 13:59:19 视频管家为空 end
			userViewModel.setUserType("内部员工");
			userViewModel.setOrgName("");
			break;
		case PmsConstant.ROLE_PROVIDER:
			// 供应商
			Team team = teamMapper.findTeamById(userId);
			userViewModel.setUserName(
					team.getLoginName() == null || team.getLoginName().equals("") ? "供应商" : team.getLoginName());
			String teamimageUrl = team.getTeamPhotoUrl();
			if (teamimageUrl != null && !"".equals(teamimageUrl)) {
				userViewModel.setImgUrl(teamimageUrl);
			} else {
				userViewModel.setImgUrl("/resources/images/flow/gonghead.png");
			}
			userViewModel.setUserType("供应商");
			userViewModel.setOrgName(team.getTeamName());
			break;
		case PmsConstant.ROLE_CUSTOMER:
			// 客户
			User user = userMapper.findUserById(userId);
			userViewModel.setUserName(
					user.getRealName() == null || user.getRealName().equals("") ? "客户" : user.getRealName());
			userViewModel.setUserType("客户");
			String userImageUrl = user.getImgUrl();
			if (userImageUrl != null && !"".equals(userImageUrl)) {
				userViewModel.setImgUrl(userImageUrl);
			} else {
				userViewModel.setImgUrl("/resources/images/flow/kehead.png");
			}
			userViewModel.setOrgName(user.getUserName());
			break;
		case PmsConstant.ROLE_SYSTEM:
			userViewModel.setImgUrl("/resources/images/flow/xitong.png");
			userViewModel.setUserName("系统");
			userViewModel.setUserType("系统");
			userViewModel.setOrgName("");
			break;
		}
		return userViewModel;
	}

	@Override
	public UserViewModel getInfo(String userType, long userId) {
		return buildObject(userType, userId);
	}

}
