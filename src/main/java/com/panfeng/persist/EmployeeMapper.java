package com.panfeng.persist;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Employee;
import com.panfeng.resource.view.EmployeeView;

public interface EmployeeMapper {
	
	/**
	 * 根据ID获取员工信息，包含员工拥有的所有角色
	 * @param id 员工ID
	 * @return 员工实体
	 */
	public Employee findEmployeeById(@Param("employeeId") final long employeeId);
	
	/**
	 * 根据输入的员工姓名，搜索员工
	 * @param employeeRealName 员工真实姓名
	 * @return 包含该真实名称的人集合
	 */
	public List<Employee> findEmployeeByRealName(@Param("employeeRealName") final String employeeRealName);
	
	/**
	 * 根据条件分页显示员工信息
	 * @param view 条件
	 * @return 员工列表
	 */
	public List<Employee> listWithPagination(final EmployeeView view);
	
	/**
	 * 根据条件查询总数
	 * @param view 条件
	 * @return 员工总数
	 */
	public long maxSize(final EmployeeView view);
	
	/**
	 * 获取所有员工实体，不包含员工拥有的角色
	 * @return 员工列表
	 */
	public List<Employee> all();
	
	/**
	 * 保存员工信息，不包含员工与角色之间的关系
	 * @param employer 员工实体
	 * @return 员工ID
	 * 获取 员工ID 方式，employer.getEmployerId();
	 */
	public Long save(final Employee employer);
	
	/**
	 * 仅保存员工与角色之间的关系
	 * @param link 员工角色关系实体
	 */
	public long saveRelativity(final Employee employee);
	
	public long update(final Employee employee);
	
	public long delete(@Param("employeeId") final long employeeId);

	public long deleteEmployeeRoleLink(@Param("employeeId") long employeeId);

	public Employee doLogin(@Param("loginName") final String loginName,@Param("password") String password);

	/**
	 * 更新照片路径
	 * @param path
	 * @return
	 */
	public long updateImagePath(final Employee e);

	/**
	 * 修改密码
	 */
	public long editPassword(final Employee employee);

	/**
	 * 检测数据唯一性
	 * @param phoneNumber 手机号码
	 */
	public long checkPhoneNumber(@Param("phoneNumber") final String phoneNumber);

	/**
	 * 获取内部员工（除admin、测试账号外）
	 * @return list
	 */
	public List<Employee> getEmployeeList();

	/**
	 * 获取内部员工（除admin、测试账号外）
	 * @return map
	 */
	@MapKey(value = "employeeId")
	public Map<Long, Employee> getEmployeeMap();
	
}
