package com.panfeng.flow.taskchain.event;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.panfeng.domain.BaseMsg;
import com.panfeng.domain.SessionInfo;
import com.panfeng.flow.taskchain.EventBase;
import com.panfeng.flow.taskchain.TaskStatus;
import com.panfeng.resource.model.NodesEvent;

@Component
public class Log extends EventBase {

	public TaskStatus checkStatus() {
		return TaskStatus.Finish;
	}

	public void execute(SessionInfo sessionInfo,String processId) {
		System.out.println("输出了一条日志。。。");
	}

	public void closeTask() {
		System.out.println("停止了输出一条日志。。。");
	}

	public BaseMsg getResult() {
		System.out.println("返回了日志结果");
		return new BaseMsg(BaseMsg.NORMAL, "", "");
	}

	public EventBase getInfo() {
		return this;
	}

	@Override
	public void execute(NodesEvent autoEvent, SessionInfo sessionInfo, String processId) {
		// TODO Auto-generated method stub
		
	}
	
	

}
