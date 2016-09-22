package com.panfeng.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.persist.UserMapper;
import com.panfeng.resource.model.ThirdBind;
import com.panfeng.resource.model.User;
import com.panfeng.resource.view.UserView;
import com.panfeng.service.UserService;
import com.panfeng.util.DataUtil;
import com.panfeng.util.ValidateUtil;

@Transactional
@Service
public class UserServiceImpl implements UserService {

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

		if (ids.length > 0) {

			for (final long id : ids) {
				mapper.delete(id);
			}
		} else {
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
	public int validationPhone(final String telephone, final String loginName) {
		// modify by lw

		final int count = mapper.validationPhone(telephone, loginName);
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

	@Override
	public long findUnlevelUsers() {
		final long count = mapper.findUnlevelUsers();
		return count;
	}

	@Override
	public User threeLoginPhone(String telephone) {
		final User user = mapper.findUserByPhone(telephone);
		return user;
	}

	/**
	 * /** 三方用户不存在 1.手机号没注册过 phoneStatus 2.手机号注册过,但是未绑定第三方 thirdStatus
	 * 3.手机号注册过,且绑定了第三方 三方用户已经存在 ,但是未绑定手机 4.手机号没注册过 5.手机号注册过,但是未绑定第三方
	 * 6.手机号注册了,也绑定了第三方
	 */
	@Override
	public Map<String, Object> bindThird(ThirdBind bind) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "1");
		User user = new User();
		if (bind.getCode() == 0) {// 用户不存在
			if (bind.getPhoneStatus().equals("noregister")) {// 注册新用户,绑定第三方
				user.setUserName(bind.getUserName());
				user.setImgUrl(bind.getImgUrl());
				user.setPassword(DataUtil.md5("123456"));
				user.setTelephone(bind.getTelephone());
				if (bind.getType().equals("qq")) {
					user.setQqUnique(bind.getUnique());
				} else if (bind.getType().equals("wechat")) {
					user.setWechatUnique(bind.getUnique());
				} else if (bind.getType().equals("wb")) {
					user.setWbUnique(bind.getUnique());
				}
				mapper.saveByThirdLogin(user);
				map.put("user", user);
				map.put("msg", "绑定成功");
			}
			if (bind.getPhoneStatus().equals("register") && bind.getThirdStatus() == 0) {// 更新phone的账号,绑定第三方
				user.setTelephone(bind.getTelephone());
				user = mapper.findUserByAttr(user);
				if (null == user.getUserName() || "".equals(user.getUserName())) {
					user.setUserName(bind.getUserName());
				}
				if (null == user.getImgUrl() || "".equals(user.getImgUrl())) {
					user.setImgUrl(bind.getImgUrl());
				}
				if (bind.getType().equals("qq")) {
					user.setQqUnique(bind.getUnique());
				} else if (bind.getType().equals("wechat")) {
					user.setWechatUnique(bind.getUnique());
				} else if (bind.getType().equals("wb")) {
					user.setWbUnique(bind.getUnique());
				}
				mapper.update(user);
				map.put("user", user);
				map.put("msg", "绑定成功");
			}
			if (bind.getPhoneStatus().equals("register") && bind.getThirdStatus() == 1) {// 提示手机号被占用
				map.put("code", "0");
				map.put("msg", "手机号被占用");
			}
		} else if (bind.getCode() == 1) {// 第三方用户存在
			if (bind.getPhoneStatus().equals("noregister")) {// 手机无注册,更新手机到第三方账户
				user.setUniqueId(bind.getUnique());
				user = mapper.verificationUserExistByThirdLogin(user).get(0);
				user.setTelephone(bind.getTelephone());
				mapper.update(user);
				map.put("msg", "绑定成功");
				map.put("user", user);
			}
			if (bind.getPhoneStatus().equals("register") && bind.getThirdStatus() == 0) {// 删除第三方账户,更新phone用户
				user.setUniqueId(bind.getUnique());
				user = mapper.verificationUserExistByThirdLogin(user).get(0);
				mapper.delete(user.getId());
				user = new User();
				user.setTelephone(bind.getTelephone());
				user = mapper.findUserByAttr(user);
				if (bind.getType().equals("qq")) {
					user.setQqUnique(bind.getUnique());
				} else if (bind.getType().equals("wechat")) {
					user.setWechatUnique(bind.getUnique());
				} else if (bind.getType().equals("wb")) {
					user.setWbUnique(bind.getUnique());
				}
				mapper.update(user);
				map.put("msg", "绑定成功");
				map.put("user", user);
			}
			if (bind.getPhoneStatus().equals("register") && bind.getThirdStatus() == 1) {// 提示手机号被占用
				map.put("code", "0");
				map.put("msg", "手机号被占用");
			}
		}
		return map;
	}

	// 根据用户名和密码查询用户
	@Override
	public User findUserByLoginNameAndPwd(User user) {
		return mapper.findUserByLoginNameAndPwd(user);
	}

	@Override
	public long modifyUserLoginName(User user) {
		return mapper.modifyUserLoginName(user);
	}

	//查询第三方绑定状态
	@Override
	public Map<String, Object> thirdStatus(User u) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("qq", "0");
		map.put("wechat","0");
		map.put("wb", "0");
		User user = mapper.findUserById(u.getId());
		if(null!=user){
			if(ValidateUtil.isValid(user.getQqUnique())){
				map.put("qq", "1");
			}
			if(ValidateUtil.isValid(user.getWechatUnique())){
				map.put("wechat", "1");
			}
			if(ValidateUtil.isValid(user.getWbUnique())){
				map.put("wb", "1");
			}
		}
		return map;
	}

	/**
	 * 用户个人资料页面绑定第三方
	 */
	@Override
	public boolean userInfoBind(User u) {
		//查询第三方是不是存在绑定
		List<User> list = mapper.verificationUserExistByThirdLogin(u);
		if(list.size()>0){
			return false;//已经存在绑定
		}else{
			User user = mapper.findUserById(u.getId());
			if(u.getlType().equals("qq")){
				user.setQqUnique(u.getUniqueId());
			}else if(u.getlType().equals("weibo")){
				user.setWbUnique(u.getUniqueId());
			}else if(u.getlType().equals("wechat")){
				user.setWechatUnique(u.getUniqueId());
			}
			if(!ValidateUtil.isValid(user.getUserName())){
				user.setUserName(u.getUserName());
			}
			mapper.update(user);
			return true;
		}
	}
	/**
	 * 用户个人资料页面解除绑定第三方
	 */
	@Override
	public boolean userInfoUnBind(User u) {
		mapper.unBindThird(u);
		return true;
	}

	@Override
	public boolean uniqueUserName(User user) {
		if(null!=user){
			List<User> list = mapper.findUserByUserName(user);
			if(list.size()==0){
				return true;
			}
			if(list.size()==1){
				User u = list.get(0);
				if(null == u || u.getId()==user.getId()){//是自身
					return true;
				}
			}
			return false;
		}return false;
	}
}
