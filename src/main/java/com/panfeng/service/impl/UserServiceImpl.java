package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.persist.UserMapper;
import com.panfeng.resource.model.User;
import com.panfeng.resource.view.UserView;
import com.panfeng.service.UserService;
import com.panfeng.util.DataUtil;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private final UserMapper mapper = null;
	
	public List<User> all() {
		
		final List<User> list = mapper.all();
		return list;
	}

	@Override
	public List<User> listWithPagination(final UserView view) {
		
		final List<User> list = mapper.listWithPagination(view);
		return list;
	}

	@Override
	public long maxSize(final UserView view) {
		
		final long total = mapper.maxSize(view);
		return total;
	}

	@Override
	public long delete(final long[] ids) {
		
		if(ids.length > 0){
			
			for (final long id : ids) {
				mapper.delete(id);
			}
		}else {
			throw new RuntimeException("Delete User error ...");
		}
		return 0l;
	}

	@Override
	public long save(final User user) {
		
		final long ret = mapper.save(user);
		return ret;
	}

	@Override
	public long update(final User user) {
		
		final long ret = mapper.update(user);
		return ret;
	}

	@Override
	public User findUserById(final long id) {
		
		final User user = mapper.findUserById(id);
		return user;
	}

	@Override
	public User findUserByAttr(final User user) {
		
		final User u = mapper.findUserByAttr(user);
		return u;
	}

	@Override
	public int validationPhone(final String telephone) {
		
		final int count = mapper.validationPhone(telephone);
		return count;
	}

	@Override
	public long recover(final User user) {
		
		final long ret = mapper.recover(user);
		return ret;
	}

	
	@Override
	public User register(final User user) {
		
		mapper.save(user);
		user.setVerification_code("");
		return user;
	}

	@Override
	public long modifyUserInfo(final User user) {
		
		final long ret = mapper.modifyUserInfo(user);
		return ret;
	}

	@Override
	public long modifyUserPassword(final User user) {
		
		final long ret = mapper.modifyUserPassword(user);
		return ret;
	}

	@Override
	public long modifyUserPhone(final User user) {
		
		final long ret = mapper.modifyUserPhone(user);
		return ret;
	}

	@Override
	public long modifyUserPhoto(final User user) {
		
		final long ret = mapper.modifyUserPhoto(user);
		return ret;
	}

	@Override
	public List<User> verificationUserExistByThirdLogin(final User user) {
		
		final List<User> users = mapper.verificationUserExistByThirdLogin(user);
		return users;
	}

	@Override
	public long saveByThirdLogin(final User user) {

		final long ret = mapper.saveByThirdLogin(user);
		return ret;
	}

	@Override
	public List<User> findUserByName(final User user) {
		return mapper.findUserByNameOrRealName(user);
	}

	@Override
	public long simpleSave(final User user) {
		
		user.setPassword(DataUtil.md5(GlobalConstant.PROJECT_USER_INIT_PASSWORD));
		final long ret = mapper.simpleSave(user);
		return ret;
	}

}
