package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Role;
import com.panfeng.resource.model.RoleRightLink;
import com.panfeng.resource.view.RoleView;

public interface RoleMapper {
	
	/**
	 * 根据ID获取角色信息，包含员工
	 * @param id 角色ID
	 * @return 角色实体
	 */
	public Role findRoleById(@Param("roleId") final long roleId);
	
	/**
	 * 获取所有角色实体，不包含员工
	 * @return 角色列表
	 */
	public List<Role> all();
	
	/**
	 * 保存角色信息，不包含员工与角色之间的关系
	 * @param role 角色实体
	 * @return 角色ID
	 * 获取 角色ID 方式，role.getRoleId();
	 */
	public Long save(final Role role);
	
	/**
	 * 仅保存权限与角色之间的关系
	 * @param link 权限角色关系实体
	 */
	public void saveRelativity(final RoleRightLink link);

	/**
	 * 获取符合条件的角色列表
	 * @param view 条件
	 * @return 角色列表
	 */
	public List<Role> listWithPagination(final RoleView view);

	/**
	 * 更新角色实体
	 * @param role 角色实体
	 * @return 更新条目数
	 */
	public long update(final Role role);

	/**
	 * 删除角色实体
	 * @param id 角色ID
	 */
	public long delete(@Param("roleId") final long roleId);

	/**
	 * 删除角色与人之间的关系
	 * @param 角色ID
	 */
	public long deleteRoleRightLink(@Param("roleId") long roleId);

	/**
	 * 获取符合条件的所有条目数
	 * @param view 条件
	 * @return 条目数
	 */
	public long maxSize(final RoleView view);

	/**
	 * 授权
	 * @param roleId 角色ID
	 * @param resourceIds 权限集合
	 */
	public long grant(@Param("roleId") final long roleId,@Param("resourceIds") final long[] resourceIds);

	/**
	 * 
	 * @param roleId
	 * @return
	 */
	public List<Long> getRightsByRole(@Param("roleId") final long roleId);

	public void deleteEmployeeRoleLink(@Param("roleId") final long roleId);
}
