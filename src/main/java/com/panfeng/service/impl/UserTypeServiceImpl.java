package com.panfeng.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.persist.TeamMapper;
import com.panfeng.persist.UserMapper;
import com.panfeng.persist.VersionManagerMapper;
import com.panfeng.resource.model.Team;
import com.panfeng.resource.model.User;
import com.panfeng.resource.model.UserViewModel;
import com.panfeng.resource.model.VersionManager;
import com.panfeng.service.UserTempService;

@Service
public class UserTypeServiceImpl implements UserTempService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private TeamMapper teamMapper;
	@Autowired
	private VersionManagerMapper versionManagerMapper;

	// 创建对象
	public UserViewModel buildObject(String userType, long userId) {
		UserViewModel userViewModel = new UserViewModel();
		switch (userType) {
		case GlobalConstant.ROLE_MANAGER:
			// 视频管家
			VersionManager versionManager = versionManagerMapper
					.findManagerById(userId);
			userViewModel
					.setUserName(versionManager.getManagerRealName() == null
							|| versionManager.getManagerRealName().equals("") ? "视频管家"
							: versionManager.getManagerRealName());
			userViewModel.setImgUrl("/resources/img/flow/guanhead.png");
			userViewModel.setUserType("视频管家");
			userViewModel.setOrgName("");
			break;
		case GlobalConstant.ROLE_PROVIDER:
			// 供应商
			Team team = teamMapper.findTeamById(userId);
			userViewModel.setUserName(team.getLoginName() == null
					|| team.getLoginName().equals("") ? "供应商" : team
					.getLoginName());
			String teamimageUrl=team.getTeamPhotoUrl();
			if(teamimageUrl!=null&&!"".equals(teamimageUrl)){
				String filename=teamimageUrl.substring(teamimageUrl.lastIndexOf('/'), teamimageUrl.length());
				userViewModel.setImgUrl("/team/img/"+filename);
			}else{
				userViewModel.setImgUrl("/resources/img/flow/gonghead.png");	
			}
			userViewModel.setUserType("供应商");
			userViewModel.setOrgName(team.getTeamName());
			break;
		case GlobalConstant.ROLE_CUSTOMER:
			// 客户
			User user = userMapper.findUserById(userId);
			userViewModel.setUserName(user.getRealName() == null
					|| user.getRealName().equals("") ? "客户" : user
					.getRealName());
			userViewModel.setUserType("客户");
			String userImageUrl=user.getImgUrl();
			if(userImageUrl!=null&&!"".equals(userImageUrl)){
				String filename=userImageUrl.substring(userImageUrl.lastIndexOf('/'), userImageUrl.length());
				userViewModel.setImgUrl("/user/img/"+filename);
			}else{
				userViewModel.setImgUrl("/resources/img/flow/kehead.png");
			}
			userViewModel.setOrgName(user.getUserName());
			break;
		case GlobalConstant.ROLE_SYSTEM:
			userViewModel.setImgUrl("/resources/img/flow/xitong.png");
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
