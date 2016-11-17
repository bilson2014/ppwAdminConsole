package com.panfeng.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.flow.taskchain.EventType;
import com.panfeng.persist.NodesEventMapper;
import com.panfeng.persist.TaskChainMapper;
import com.panfeng.resource.model.Mail;
import com.panfeng.resource.model.NodesEvent;
import com.panfeng.resource.model.Sms;
import com.panfeng.resource.model.TaskChain;
import com.panfeng.resource.model.TaskChainNodesEventLink;
import com.panfeng.resource.model.Tree;
import com.panfeng.service.MailService;
import com.panfeng.service.SMSTemplateService;
import com.panfeng.service.TaskChainService;
import com.panfeng.util.ValidateUtil;

@Service
public class TaskChainServiceImpl implements TaskChainService {
	@Autowired
	private TaskChainMapper taskChainMapper;
	@Autowired
	private NodesEventMapper nodesEventMapper;
	@Autowired
	private SMSTemplateService smsTemplateService;
	@Autowired
	private MailService mailService;

	@Override
	public List<TaskChain> getAll() {
		return taskChainMapper.getAll();
	}

	@Override
	public List<NodesEvent> getAllEvents() {
		return nodesEventMapper.getAll();
	}

	@Override
	public List<Tree> getEventTree() {
		List<NodesEvent> list = nodesEventMapper.getAll();
		List<Tree> tree = new LinkedList<Tree>();
		if (ValidateUtil.isValid(list)) {
			for (NodesEvent event : list) {
				Tree t = new Tree();
				t.setId(event.getNodesEventId().toString());
				t.setText(event.getNodesEventName());
				tree.add(t);
			}
		}
		return tree;
	}

	@Override
	public long updateEvent(NodesEvent nodesEvent) {
		return nodesEventMapper.update(nodesEvent);
	}

	@Override
	public long addEvent(NodesEvent nodesEvent) {
		return nodesEventMapper.save(nodesEvent);
	}

	@Override
	public long deleteEvent(Long eventId) {
		return nodesEventMapper.delete(eventId);
	}

	@Override
	@Transactional
	public long updateNodes(TaskChain taskChain) {
		List<TaskChainNodesEventLink> links = taskChainMapper.findLinkByTaskChainId(taskChain.getTaskChainId());
		List<NodesEvent> events = taskChain.getNodesEvents();
		Iterator<NodesEvent> iterator = events.iterator();
		while (iterator.hasNext()) {
			Iterator<TaskChainNodesEventLink> linkite = links.iterator();
			NodesEvent ne = iterator.next();
			while (linkite.hasNext()) {
				TaskChainNodesEventLink tce = linkite.next();
				if (tce.getNodeEventId() == ne.getNodesEventId()) {
					linkite.remove();
					iterator.remove();
					break;
				}
			}
		}
		if (links.size() > 0) {
			for (TaskChainNodesEventLink chainNodesEventLink : links) {
				taskChainMapper.deleteLink(chainNodesEventLink.getTaskChainNodesEventLinkId());
			}
		}
		if (events.size() > 0) {
			for (NodesEvent nodesEvent : events) {
				taskChainMapper.saveLink(
						new TaskChainNodesEventLink(taskChain.getTaskChainId(), nodesEvent.getNodesEventId()));
			}
		}

		return taskChainMapper.update(taskChain);

	}

	public long addNodes(TaskChain taskChain) {
		long res = taskChainMapper.save(taskChain);
		List<NodesEvent> events = taskChain.getNodesEvents();
		if (events.size() > 0) {
			for (NodesEvent nodesEvent : events) {
				taskChainMapper.saveLink(
						new TaskChainNodesEventLink(taskChain.getTaskChainId(), nodesEvent.getNodesEventId()));
			}
		}
		return res;
	}

	/**
	 * 事件模板
	 * 
	 * @return
	 */
	public List<Tree> contentTemplateTree() {
		final List<Tree> tree = new ArrayList<Tree>();
		Tree tr = new Tree();
		EventType smsenum = EventType.SMS;
		tr.setId(smsenum.getId() + "");
		tr.setPid("null");
		tr.setText(smsenum.getName() + "模板");
		tr.setState("closed");
		tree.add(tr);
		// 添加短信模板
		List<Sms> smsList = smsTemplateService.getAll();
		if (ValidateUtil.isValid(smsList)) {
			for (Sms sms : smsList) {
				Tree t = new Tree();
				t.setId(sms.getTempId());
				t.setPid("0");
				t.setText(sms.getTempTitle());
				tree.add(t);
			}
		}
		tr = new Tree();
		EventType mailenum = EventType.MAIL;
		tr.setId(mailenum.getId() + "");
		tr.setPid("null");
		tr.setText(mailenum.getName() + "模板");
		tr.setState("closed");
		tree.add(tr);
		// 添加邮件模板
		List<Mail> mails = mailService.getAll();
		if (ValidateUtil.isValid(mails)) {
			for (Mail mail : mails) {
				Tree t = new Tree();
				t.setId(mail.getMailType());
				t.setPid("1");
				t.setText(mail.getSubject());
				tree.add(t);
			}
		}
		return tree;
	}
}
