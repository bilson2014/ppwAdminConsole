package com.panfeng.flow.taskchain.event;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.panfeng.domain.BaseMsg;
import com.panfeng.domain.SessionInfo;
import com.panfeng.flow.taskchain.EventBase;
import com.panfeng.flow.taskchain.TaskStatus;
import com.panfeng.resource.model.NodesEvent;

@Component
public class Pay extends EventBase {

	public static String status = "0"; // 用静态变量模拟 数据库储存数据。无论对象是否存活，静态对象一直在

	@Override
	public TaskStatus checkStatus() {
		return status.equals("1") ? TaskStatus.Finish : TaskStatus.Fail;
	}

	public void execute(SessionInfo sessionInfo, String processId) {
		System.out.println("发起支付。。。processId:"+processId +".....getRealName:"+sessionInfo.getRealName());
		status = "1";
	}

	@Override
	public void closeTask() {
		System.out.println("停止支付。。。");
	}

	@Override
	public BaseMsg getResult() {
		System.out.println("返回了支付结果");
		return new BaseMsg(BaseMsg.NORMAL, "", "");
	}

	@Override
	public <Pay> Pay getInfo() {
		return null;
	}

	@Override
	public void execute(NodesEvent autoEvent, SessionInfo sessionInfo, String processId) {
		// TODO Auto-generated method stub
		
	}

}
