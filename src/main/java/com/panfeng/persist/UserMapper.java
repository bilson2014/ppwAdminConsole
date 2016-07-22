package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.User;
import com.panfeng.resource.view.UserView;

public interface UserMapper {

	public List<User> all();

	public List<User> listWithPagination(final UserView view);

	public long maxSize(final UserView view);

	public long delete(@Param("id") final long id);

	public long save(final User user);

	public long update(final User user);

	/**
	 * 根据用户ID获取用户
	 * 
	 * @param id
	 *            用户唯一编号
	 * @return 用户信息
	 */
	public User findUserById(@Param("id") final long id);

	/**
	 * 密码相同的前提下，通过用户名称获取用户
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
	public int validationPhone(@Param("telephone") final String telephone, @Param("loginName") final String loginName);

	/**
	 * 密码重置
	 */
	public long recover(final User user);

	/**
	 * 修改 用户基本信息(昵称、性别、真实姓名、电子邮件、QQ)
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
	public long saveByThirdLogin(final User user);

	public List<User> findUserByNameOrRealName(final User user);

	public long simpleSave(final User user);

	/**
	 * 获取新注册的用户数量，用来提示客服还有多少新注册用户未完成分级
	 * 
	 * @return
	 */
	public long findUnlevelUsers();

	public User findUserByPhone(String telephone);

	// add by wanglc 2016-7-13 14:35:49 begin
	/**
	 * 根据用户名和密码查询用户
	 * 
	 * @param user
	 */
	public User findUserByLoginNameAndPwd(User user);
	// add by wanglc 2016-7-13 14:35:49 begin

	// 修改用户名和密码
	public long modifyUserLoginName(User user);
}
