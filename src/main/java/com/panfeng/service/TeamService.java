package com.panfeng.service;

import java.util.List;
import java.util.Map;

import com.panfeng.domain.BaseMsg;
import com.panfeng.resource.model.Team;
import com.panfeng.resource.view.TeamView;

public interface TeamService {

	/**
	 * 分页检索 team
	 * 
	 * @param team
	 * @return list of team
	 */
	public List<Team> listWithPagination(final TeamView view);

	public long save(final Team team);

	/**
	 * 保存 图片路径
	 */
	public long saveTeamPhotoUrl(final Team team);

	/**
	 * 删除后返回信息列表，便于删除文件
	 * 
	 * @param ids
	 *            编号数组
	 * @return 团队列表
	 */
	public List<Team> delete(final long[] ids);

	public long update(final Team team);

	/**
	 * 获取team总个数
	 * 
	 * @return team的总个数
	 */
	public long maxSize(final TeamView view);

	/**
	 * 根据 ID 获取 team 信息
	 * 
	 * @param id
	 *            team ID
	 * @return team
	 */
	public Team findTeamById(final long id);

	/**
	 * 获取所有 team 信息
	 * 
	 * @return
	 */
	public List<Team> getAll();

	/**
	 * 供应商登录
	 * 
	 * @param original
	 *            包含(登录名和密码(已加密))
	 * @return
	 */
	public Team doLogin(final String phoneNumber);

	/**
	 * 检查手机号唯一性 如果没有,则返回0,有则返回存在的条目数
	 * 
	 * @param phoneNumber
	 *            电话号码
	 * @return 数据个数
	 */
	public long checkExist(final Team original);

	/**
	 * 供应商注册
	 * 
	 * @param original
	 *            供应商信息
	 * @return 新增数据个数
	 */
	public Team register(final Team original);

	/**
	 * 密码重置
	 * 
	 * @param original
	 *            包含 手机号及新密码
	 * @return 数据受影响个数
	 */
	public long recover(final Team original);

	/**
	 * 供应商 基础信息更新(供应商名称、简介、地址、邮箱等)
	 * 
	 * @return 数据受影响个数
	 */
	public long updateTeamInfomation(final Team team);

	/**
	 * 根据登录名修改密码
	 * 
	 * @param team
	 *            (包含 登录名和密码)
	 * @return 数据受影响个数
	 */
	public long updatePasswordByLoginName(final Team team);

	/**
	 * 更新供应商审核状态为 审核中
	 * 
	 * @param teamId
	 *            供应商唯一编号
	 * @return 数据受影响个数
	 */
	public long updateTeamStatus(final long id);

	public List<Team> findTeamByName(final Team team);

	public List<Team> verificationTeamExist(final Team team);

	/**
	 * 绑定第三方登录账号
	 * 
	 * @param provider
	 * @return
	 */
	public long updateUniqueId(final Team provider);

	public BaseMsg bind(final Team provider);

	/**
	 * 根据用户名和密码登录供应商
	 * 
	 * @param original
	 * @return
	 */
	public Team findTeamByLoginNameAndPwd(Team original);

	public long updateTeamAccount(Team original);

	/**
	 * 查询第三方绑定状态
	 */
	public Map<String, Object> thirdStatus(Team team);

	/**
	 * 基本信息页面绑定第三方
	 */
	public boolean teamInfoBind(Team team);

	/**
	 * 基本信息解除绑定第三方
	 */
	public boolean teamInfoUnBind(Team team);

	public long modifyTeamPhone(Team team);

}
