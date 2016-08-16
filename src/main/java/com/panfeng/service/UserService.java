package com.panfeng.service;

import java.util.List;
import java.util.Map;

import com.panfeng.resource.model.ThirdBind;
import com.panfeng.resource.model.User;
import com.panfeng.resource.view.UserView;

public interface UserService {

	public List<User> all();

	public List<User> listWithPagination(final UserView view);

	public long maxSize(final UserView view);

	/**
	 * 根据 服务编号 删除 service
	 * 
	 * @param serviceId
	 *            service 编号
	 */
	public long delete(final long[] ids);

	public long save(final User user);

	public long update(final User user);

	/**
	 * 根据用户ID获取用户
	 * 
	 * @param id
	 *            用户唯一编号
	 * @return 用户信息
	 */
	public User findUserById(final long id);

	/**
	 * 密码相同的前提下，通过用户名称获取用户 根据用户名匹配手机号或者邮件
	 * 
	 * @param user
	 *            用户密码 以及 用户名称
	 * @return 用户信息
	 */
	public User findUserByAttr(final User user);

	/**
	 * 检查手机号是否存在
	 * 
	 * @param telephone
	 *            手机号码
	 * @return 存在个数
	 */
	public int validationPhone(final String telephone, String loginName);

	/**
	 * 密码重置
	 */
	public long recover(final User user);

	/**
	 * 注册用户
	 */
	public User register(final User user);

	/**
	 * 修改 用户基本信息(昵称、性别、真实姓名、电子邮件、QQ)
	 * 
	 * @param (昵称、性别、真实姓名、电子邮件、QQ、用户ID)
	 * @return 修改个数
	 */
	public long modifyUserInfo(final User user);

	/**
	 * 修改 用户密码
	 */
	public long modifyUserPassword(final User user);

	/**
	 * 修改 用户手机号码
	 */
	public long modifyUserPhone(final User user);

	/**
	 * 修改 用户头像
	 */
	public long modifyUserPhoto(final User user);

	/**
	 * 验证 通过第三方登录的用户是否存在
	 */
	public List<User> verificationUserExistByThirdLogin(final User user);

	/**
	 * 保存通过第三方登录的用户信息
	 */
	public long saveByThirdLogin(final User createUser);

	public List<User> findUserByName(final User user);

	public long simpleSave(final User user);

	/**
	 * 获取新注册的用户数量，用来提示客服还有多少新注册用户未完成分级
	 */
	public long findUnlevelUsers();

	public User threeLoginPhone(String telephone);

	public Map<String, Object> bindThird(ThirdBind bind);

	/**
	 * 根据loginName和密码查询用户
	 */
	public User findUserByLoginNameAndPwd(User user);

	public long modifyUserLoginName(User user);

	/**
	 * 查询第三方绑定状态
	 */
	public Map<String, Object> thirdStatus(User user);

	/**
	 * 用户个人资料页面绑定第三方
	 */
	public boolean userInfoBind(User user);

	/**
	 * 用户个人资料页面解除绑定第三方
	 */
	public boolean userInfoUnBind(User user);

	/**
	 * 验证用户名昵称唯一性
	 * true 可用
	 * false 不可用
	 */
	public boolean uniqueUserName(User user);

}
