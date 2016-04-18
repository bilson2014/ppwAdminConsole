package com.panfeng.persist;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Right;
import com.panfeng.resource.view.RightView;

public interface RightMapper {

	public List<Right> all();

	public List<Right> listWithPagination(final RightView view);

	public Right findRightById(@Param("rightId") final long rightId);

	public long save(final Right right);

	public long update(final Right right);

	public void deleteRightRoleLink(@Param("rightId") final long rightId);

	public void delete(@Param("rightId") final long rightId);

	public long maxSize(final RightView view);

	public Integer findMaxPos();
	
	public Long findMaxCodeByPos(@Param("pos") final long pos);

	@MapKey(value = "url")
	public Map<String, Right> getRights();

	public List<Right> findRightByPid(@Param("pList") final Set<Long> pList);
	
	public List<Right> getAllRightsWithMenu();
	
}
