package com.panfeng.flow.data;

import java.util.List;
import java.util.Map;

import com.panfeng.flow.taskchain.EventType;

public interface TemplateDateInterface<F, T> {

	/**
	 * 返回可选字段
	 * 
	 * @return
	 */
	List<String> optionalFields();

	/**
	 * 根据属性名，创建实际生产数据 保持进入参数顺序返回结果
	 * 
	 * @param list
	 * @return
	 */
	F fillData(FillerParam fillerParam, T t, EventType eventType);

	Map<String, String> personnel();
	

}
