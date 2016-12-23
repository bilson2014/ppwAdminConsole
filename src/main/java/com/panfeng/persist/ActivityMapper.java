package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Activity;
import com.panfeng.resource.view.ActivityView;

public interface ActivityMapper {

	long save(Activity activity);

	long update(Activity activity);

	long delete(@Param("ids") Long[] ids);

	Activity findActivityById(@Param("activityId") Long activityId);

	List<Activity> findAll();

	List<Activity> listWithPagination(ActivityView view);

	long maxSize(ActivityView view);
	
}
