package com.panfeng.flow.taskchain.event;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.panfeng.flow.data.FillerParam;
import com.panfeng.flow.taskchain.EventBase;
import com.panfeng.flow.taskchain.EventType;
import com.panfeng.flow.taskchain.TaskStatus;
import com.panfeng.mq.service.MailMQService;
import com.panfeng.resource.model.NodesEvent;
import com.panfeng.service.TemplateDataManage;

@Component
public class MailEvent extends EventBase {

	@Autowired
	private TemplateDataManage templateDataManage;

	@Autowired
	private MailMQService mailMQService;

	@Override
	public TaskStatus checkStatus() {
		return TaskStatus.Finish;
	}

	public void execute(SessionInfo sessionInfo, String processId) {
		System.out.println("发送邮件中");
	}

	@Override
	public void closeTask() {
		System.out.println("关闭了右键发送程序");
	}

	@Override
	public <BaseMsg> BaseMsg getResult() {
		return null;
	}

	@Override
	public <Mail> Mail getInfo() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(NodesEvent autoEvent, SessionInfo sessionInfo, String processId) {
		String fields = autoEvent.getDataFields();
		String relevantPersons = autoEvent.getRelevantPerson();
		if (ValidateUtil.isValid(fields) && ValidateUtil.isValid(relevantPersons)) {
			String[] fieldarray = fields.split("\\,");
			String[] rparray = relevantPersons.split("\\,");
			FillerParam fillerParam = new FillerParam();
			if (fieldarray != null && fieldarray.length > 0) {
				LinkedList<String> field = new LinkedList<>();
				for (int i = 0; i < fieldarray.length; i++) {
					field.add(fieldarray[i]);
				}
				fillerParam.setFields(field);
			}
			if (rparray != null && rparray.length > 0) {
				// 转换用户信息，相对于发邮件来说此时用户存在的意义是接收和发送邮件
				LinkedList<String> relevantPersonsList = new LinkedList<>();
				for (int i = 0; i < rparray.length; i++) {
					relevantPersonsList.add(rparray[i]);
				}
				fillerParam.setRelevantPersons(relevantPersonsList);
			}

			Object fillData = templateDataManage.fillData(fillerParam, autoEvent.getDataFiller(), processId,
					EventType.MAIL);
			if (fillData != null) {
				Map<String, String[]> res = (Map<String, String[]>) fillData;
				if (ValidateUtil.isValid(res)) {
					Set<String> key = res.keySet();
					for (String keyString : key) {
						Map<String, String[]> map = new HashMap<>();
						String[] value = res.get(keyString);
						map.put(keyString, value);
						mailMQService.sendMailsByType(autoEvent.getTemplateId(), map);
					}
				}
			}
		}
	}

}
