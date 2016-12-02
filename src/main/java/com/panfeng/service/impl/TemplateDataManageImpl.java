package com.panfeng.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.panfeng.flow.data.FillerParam;
import com.panfeng.flow.data.TemplateDateInterface;
import com.panfeng.flow.taskchain.EventBase;
import com.panfeng.flow.taskchain.EventType;
import com.panfeng.resource.model.Tree;
import com.panfeng.service.TemplateDataManage;
import com.panfeng.util.ValidateUtil;

@Service
public class TemplateDataManageImpl implements ApplicationListener<ApplicationEvent>, TemplateDataManage {

	@Autowired
	private ApplicationContext applicationContext;

	@SuppressWarnings("rawtypes")
	private static Map<String, TemplateDateInterface> templateData;

	private static Map<String, EventBase> eventHandle;

	private static boolean isStart = false;

	/**
	 * 包扫描加载模板数据实现类
	 */
	@Override
	public synchronized void onApplicationEvent(ApplicationEvent event) {
		if (!isStart) {
			isStart = true;
			templateData = applicationContext.getBeansOfType(TemplateDateInterface.class);
			eventHandle = applicationContext.getBeansOfType(EventBase.class);
		}
	}

	/**
	 * 获取数据模板列表
	 * 
	 * @return
	 */
	public List<Tree> getDataList() {
		List<Tree> tree = new ArrayList<>();
		if (ValidateUtil.isValid(templateData)) {
			Set<String> keySet = templateData.keySet();
			for (String string : keySet) {
				Tree t = new Tree();
				t.setId(string);
				t.setText(string);
				tree.add(t);
			}
		}
		return tree;
	}

	/**
	 * 获取所有事件类
	 */
	public List<Tree> getEventHandle() {
		List<Tree> tree = new ArrayList<>();
		if (ValidateUtil.isValid(eventHandle)) {
			Set<String> keySet = eventHandle.keySet();
			for (String string : keySet) {
				Tree t = new Tree();
				t.setId(string);
				t.setText(string);
				tree.add(t);
			}
		}
		return tree;
	}

	/**
	 * 填充已知模板
	 * 
	 * @param list
	 * @param templateDataKey
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <F, T> F fillData(FillerParam fillerParam, String templateDataKey, T t,EventType eventType) {
		if (ValidateUtil.isValid(templateDataKey)) {
			TemplateDateInterface<F, T> td = templateData.get(templateDataKey);
			return td.fillData(fillerParam, t,eventType);
		}
		return null;
	}

	/**
	 * 获取模板字段
	 * 
	 * @param templateDataKey
	 * @return
	 */
	public List<Tree> optionalFields(String templateDataKey) {
		if (ValidateUtil.isValid(templateDataKey)) {
			TemplateDateInterface<?, ?> tdi = templateData.get(templateDataKey);
			List<Tree> treeList = new ArrayList<>();
			if (tdi != null && ValidateUtil.isValid(tdi.optionalFields())) {
				for (String tree : tdi.optionalFields()) {
					Tree t = new Tree();
					t.setId(tree);
					t.setText(tree);
					treeList.add(t);
				}
			}
			return treeList;
		}
		return null;
	}

	/**
	 * 获取模板相关人员
	 * 
	 * @param templateDataKey
	 * @return
	 */
	public List<Tree> personnel(String templateDataKey) {
		if (ValidateUtil.isValid(templateDataKey)) {
			TemplateDateInterface<?, ?> tdi = templateData.get(templateDataKey);
			List<Tree> treeList = new ArrayList<>();
			Map<String, String> personnel = tdi.personnel();
			if (tdi != null && ValidateUtil.isValid(personnel)) {
				Set<String> keySet = personnel.keySet();
				for (String key : keySet) {
					Tree t = new Tree();
					t.setId(key);
					t.setText(personnel.get(key));
					treeList.add(t);
				}
			}
			return treeList;
		}
		return null;
	}

}
