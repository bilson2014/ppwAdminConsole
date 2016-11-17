package com.panfeng.flow.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;

import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.panfeng.resource.model.FlowNode;
import com.panfeng.resource.model.FlowTemplate;
import com.panfeng.util.Log;
import com.panfeng.util.ValidateUtil;

/**
 * activiti 资源深度结合控制类</br>
 * 处理方法</br>
 * 
 * 1.web加载bpmn资源</br>
 * 2.web编辑器编辑修改bpmn图</br>
 * 3.repositoryService 资源服务重新部署新的流程图
 * 
 * @author wang
 *
 */
@Component
public class Resource {

	@Autowired
	private RepositoryService repositoryService;

	public void loadTemplate(String name, File bpmnFile) throws FileNotFoundException {
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		FileInputStream fileInputStream = new FileInputStream(bpmnFile);
		deploymentBuilder.addInputStream(name, fileInputStream);
		deploymentBuilder.enableDuplicateFiltering().name(name);
		deploymentBuilder.deploy();
	}

	public boolean createTemplate(FlowTemplate flowTemplate) {
		BpmnModel model = new BpmnModel();
		Process process = new Process();
		model.addProcess(process);
		final String processid = flowTemplate.getId();
		final String processname = flowTemplate.getName();
		process.setId(processid);
		process.setName(processname);
		if (ValidateUtil.isValid(flowTemplate.getFlowNodes())) {
			parseNode(flowTemplate.getFlowNodes(), process);
			new BpmnAutoLayout(model).execute();
			repositoryService.createDeployment().addBpmnModel(processid + ".bpmn", model)
					.name(processid + "_deployment").deploy();
			return true;
		}
		Log.error("create template fail .....", null);
		return false;
	}

	public void updateTemplate(String name) {

	}

	private void parseNode(List<FlowNode> nodes, Process process) {
		if (ValidateUtil.isValid(nodes)) {
			// 按照顺序存放节点
			nodes.sort(new Comparator<FlowNode>() {
				@Override
				public int compare(FlowNode o1, FlowNode o2) {
					if (o1.getIndex() < o2.getIndex())
						return -1;
					else if (o1.getIndex() == o2.getIndex())
						return 0;
					else
						return 1;
				}
			});

			StartEvent startEvent = Bpmn.createStartEvent();
			process.addFlowElement(startEvent);
			EndEvent endEvent = Bpmn.createEndEvent();
			process.addFlowElement(endEvent);
			FlowNode nextFlowNode = null;
			FlowNode prevFlowNode = null;
			for (int i = 0; i < nodes.size(); i++) {
				FlowNode flowNode = nodes.get(i);
				UserTask userTask = Bpmn.createUserTask(flowNode.getId(), flowNode.getName(), flowNode.getflowOptionsToJson());
				process.addFlowElement(userTask);

				prevFlowNode = nextFlowNode;
				nextFlowNode = flowNode;

				// add sequence
				SequenceFlow sequenceFlow;
				if (i == 0) {
					// 建立第一条连接线
					sequenceFlow = Bpmn.createSequenceFlow("startEvent", nextFlowNode.getId(), "", "");
				} else {
					sequenceFlow = Bpmn.createSequenceFlow(prevFlowNode.getId(), nextFlowNode.getId(), "", "");
				}
				process.addFlowElement(sequenceFlow);

				if (i == (nodes.size() - 1)) {
					// 建立最后一条连接线(补充最后一条连线)
					sequenceFlow = Bpmn.createSequenceFlow(nextFlowNode.getId(), "endEvent", "", "");
					process.addFlowElement(sequenceFlow);
				}
			}
		}
	}

}
