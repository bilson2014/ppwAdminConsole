package com.panfeng.service;

import java.util.Map;

public interface JobParamParser {
	public Map<String, String[]> parser(Long activityId) throws Exception;
}
