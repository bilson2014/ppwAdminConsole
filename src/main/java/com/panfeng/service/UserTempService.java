package com.panfeng.service;

import com.panfeng.resource.model.UserViewModel;

public interface UserTempService {
	UserViewModel getInfo(String userType,long userId);
}
