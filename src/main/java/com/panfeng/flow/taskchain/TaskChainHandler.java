package com.panfeng.flow.taskchain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.panfeng.persist.TaskChainMapper;
import com.panfeng.resource.model.NodesEvent;
import com.panfeng.resource.model.TaskChain;

/**
 * 处理任务链（每个节点拥有有个）
 * 
 * @author wang
 *
 */
@Component
public class TaskChainHandler {
	@Autowired
	private TaskChainMapper taskChainMapper;

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 查询数据库获取当前节点的任务链
	 * 
	 * @return
	 */
	private TaskChain loadNodeTasks(Long taskChainId) {
		return taskChainMapper.findTaskChainByTaskChainId(taskChainId);
	}

	public JsonArray checkTaskStatus(Long taskChainId) {
		TaskChain tasks = loadNodeTasks(taskChainId);
		List<NodesEvent> events = tasks.getNodesEvents();
		JsonArray jsonArray = new JsonArray();
		if (events.size() > 0) {
			// List<NodesEvent> autoEvents = new ArrayList<>();
			List<NodesEvent> eaunEvents = new ArrayList<>();
			for (NodesEvent nodesEvent : events) {
				if (nodesEvent.getNodesEventModel() == NodesEvent.AUTOMODEL) {
					// autoEvents.add(nodesEvent);
				} else {
					eaunEvents.add(nodesEvent);
				}
			}

			// 首先检测 手动完成的任务
			// 然后检测自动执行的程序（触发自动执行）
			boolean finish = true;
			for (NodesEvent nodesEvent : eaunEvents) {
				EventBase taskBase = getBean(nodesEvent.getNodesEventClassName());
				TaskStatus taskStatus = taskBase.checkStatus();
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty(nodesEvent.getNodesEventName(), taskStatus.name());
				jsonArray.add(jsonObject);
				if (!taskStatus.equals(TaskStatus.Finish)) {
					finish = false;
				}
			}
			if (finish) {
				jsonArray = null; // 检测通过不返回结果返回null
				// execute(autoEvents, sessionInfo, processId);
			}
		}
		return jsonArray;
	}

	// 触发当前节点的所有自动完成任务
	public void execute(Long taskChainId, SessionInfo sessionInfo, String processId) {
		// 开启异步处理自动任务，请求即刻返回
		new Thread(() -> {
			TaskChain tasks = loadNodeTasks(taskChainId);
			List<NodesEvent> events = tasks.getNodesEvents();
			if (events.size() > 0) {
				List<NodesEvent> autoEvents = new ArrayList<>();
				for (NodesEvent nodesEvent : events) {
					if (nodesEvent.getNodesEventModel() == NodesEvent.AUTOMODEL) {
						autoEvents.add(nodesEvent);
					}
				}
				if (ValidateUtil.isValid(autoEvents)) {
					for (NodesEvent nodesEvent : autoEvents) {
						String Vclass = nodesEvent.getNodesEventClassName();
						EventBase taskBase = getBean(Vclass);
						if (taskBase == null) {
							System.out.println("Vclass 异常 自动跳过");
							continue;
						} else
							taskBase.execute(nodesEvent, sessionInfo, processId);
					}
				}
			}
		}).start();
	}

	/**
	 * 构造class 实例
	 * @param Vclass
	 * @return
	 */
	private EventBase getBean(String Vclass) {
		Object obj = applicationContext.getBean(Vclass);
		if (obj instanceof EventBase) {
			return (EventBase) obj;
		} else {
			System.out.println("任务是假的！");
		}
		return null;
	}
}
