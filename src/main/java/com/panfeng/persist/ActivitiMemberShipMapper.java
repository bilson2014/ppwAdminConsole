package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.ActivitiMember;

public interface ActivitiMemberShipMapper {
	
	long save(ActivitiMember activitiMember);

	long update(ActivitiMember activitiMember);

	long delete(@Param("id") String id);

	List<ActivitiMember> findByRole(@Param("groupId") String groupId);
	
	List<ActivitiMember> findByUserId(@Param("id") String id);
}
