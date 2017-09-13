package com.panfeng.persist;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.ActivitiUser;

public interface ActivitiUserMapper {

	long save(ActivitiUser activitiUser);

	long delete(@Param("id") String id);
}
