package com.panfeng.persist;

import java.util.Map;

/**
 * 项目流程清除
 */
public interface ActivitiCleanMapper {

	public long deleteAll(Map<String, Object> paramMap);
}
