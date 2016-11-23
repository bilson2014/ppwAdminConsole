package com.panfeng.flow.taskchain.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.panfeng.domain.SessionInfo;
import com.panfeng.flow.taskchain.EventBase;
import com.panfeng.flow.taskchain.EventType;
import com.panfeng.flow.taskchain.TaskStatus;
import com.panfeng.mq.service.MailMQService;
import com.panfeng.resource.model.NodesEvent;
import com.panfeng.service.TemplateDataManage;
import com.panfeng.util.ValidateUtil;

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
			List<String> data = new LinkedList<>();
			String[] fieldarray = fields.split("\\,");
			String[] rparray = relevantPersons.split("\\,");
			if (fieldarray != null && fieldarray.length > 0) {
				for (int i = 0; i < fieldarray.length; i++) {
					data.add(fieldarray[i]);
				}
			}
			if (rparray != null && rparray.length > 0) {
				// 转换用户信息，相对于发邮件来说此时用户存在的意义是接收和发送邮件
				for (int i = 0; i < rparray.length; i++) {
					data.add(rparray[i]);
				}
			}
			Object fillData = templateDataManage.fillData(data, autoEvent.getDataFiller(), processId, EventType.MAIL);
			if (fillData != null) {
				List<String> addressee = new ArrayList<>();
				LinkedList<String> res = (LinkedList<String>) fillData;
				for (int i = 0; i < rparray.length; i++) {
					String pollLast = res.pollLast();
					String[] ra = pollLast.split("\\,");
					if (ra != null && ra.length > 0) {
						for (int j = 0; j < ra.length; j++) {
							addressee.add(ra[j]);
						}
					}
				}
				if (ValidateUtil.isValid(addressee)) {
					for (String addr : addressee) {
						Map<String, String[]> map = new HashMap<String, String[]>();
						map.put(addr, res.toArray(new String[res.size()]));
						mailMQService.sendMailsByType(autoEvent.getTemplateId(), map);
					}
				}

			}

		}
	}

}
