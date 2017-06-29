package com.panfeng.service;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.panfeng.domain.BaseMsg;
import com.panfeng.resource.model.Team;

public interface TeamService {

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
	 * 更新供应商审核状态为 审核中
	 * 
	 * @param teamId
	 *            供应商唯一编号
	 * @return 数据受影响个数
	 */
	public long updateTeamStatus(final long id);

	public List<Team> findTeamByName(final Team team);

	public List<Team> verificationTeamExist(final Team team);

	public BaseMsg bind(final Team provider);

	/**
	 * 根据用户名和密码登录供应商
	 * 
	 * @param original
	 * @return
	 */
	public Team findTeamByLoginNameAndPwd(Team original);

	/**
	 * 查询第三方绑定状态
	 */
	public Map<String, Object> thirdStatus(Team team);

	/**
	 * 基本信息页面绑定第三方
	 */
	public boolean teamInfoBind(Team team);

	/**
	 * 供应商报表导出
	 * @param list
	 * @param os
	 */
	public void generateReport(List<PmsTeam> list, OutputStream os);
}
