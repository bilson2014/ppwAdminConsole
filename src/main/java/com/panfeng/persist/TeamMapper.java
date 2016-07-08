package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Team;
import com.panfeng.resource.view.TeamView;

public interface TeamMapper {

	public List<Team> listWithPagination(final TeamView view);

	public long save(final Team team);
	
	public long saveTeamPhotoUrl(final Team team);

	public long update(final Team team);

	public long delete(@Param("teamId") final long teamId);

	public long maxSize(final TeamView view);

	/**
	 * 根据 ID数组 获取 team列表
	 * 
	 * @param ids
	 *            ID数组
	 * @return team列表
	 */
	public List<Team> findTeamByArray(@Param("ids") final long[] ids);

	public Team findTeamById(@Param("teamId") final long teamId);

	/**
	 * 获取全部公司名称不为空的公司列表
	 * 
	 * @return 公司列表
	 */
	public List<Team> getAll();

	/**
	 * 检查供应商是否存在
	 * 
	 * @param original
	 *            供应商信息（需包含登录名和密码(已加密)）
	 * @return 供应商信息
	 */
	public Team checkTeam(@Param("phoneNumber") String phoneNumber);

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
	 * @return数据受影响个数
	 */
	public long updatePasswordByLoginName(final Team team);

	/**
	 * 更新供应商审核状态为 审核中
	 * 
	 * @param teamId
	 *            供应商唯一编号
	 * @return 数据受影响个数
	 */
	public long updateTeamStatus(@Param("teamId") final long teamId);

	/**
	 * 检测 手机号或登录名是否存在
	 * 
	 * @param team
	 *            包含 手机号 或者 登录名
	 * @return 存在个数
	 */
	public long checkExist(final TeamView view);

	public List<Team> findTeamByNameOrContact(final Team team);

	public List<Team> verificationTeamExist(final Team team);

	public long updateUniqueId(final Team provider);

}
