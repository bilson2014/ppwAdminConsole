package com.panfeng.resource.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.paipianwang.pat.common.entity.BaseEntity;
import com.paipianwang.pat.common.util.ValidateUtil;

public class IndentFlow extends BaseEntity {

	private static final long serialVersionUID = 927916387241744169L;
	public static String defaultDate = "1970-01-01";

	public final static String FLOWENABLE = "Y";
	public final static String FLOWDISABLE = "N";

	private long ifId;
	private long ifIndentId;
	private String ifFlowId;
	private String ifState;

	public long getIfId() {
		return ifId;
	}

	public void setIfId(long ifId) {
		this.ifId = ifId;
	}

	public long getIfIndentId() {
		return ifIndentId;
	}

	public void setIfIndentId(long ifIndentId) {
		this.ifIndentId = ifIndentId;
	}

	public String getIfFlowId() {
		return ifFlowId;
	}

	public void setIfFlowId(String ifFlowId) {
		this.ifFlowId = ifFlowId;
	}

	public String getIfState() {
		return ifState;
	}

	public void setIfState(String ifState) {
		this.ifState = ifState;
	}

	public IndentFlow(long ifId, long ifIndentId, String ifFlowId, String ifState) {
		super();
		this.ifId = ifId;
		this.ifIndentId = ifIndentId;
		this.ifFlowId = ifFlowId;
		this.ifState = ifState;
	}

	public IndentFlow() {
		super();
	}

	// -------------冗余字段----------------
	private long fdId;
	private String fdFlowId;
	private String fdStartTime;
	private String fdTaskId;

	public long getFdId() {
		return fdId;
	}

	public void setFdId(long fdId) {
		this.fdId = fdId;
	}

	public String getFdFlowId() {
		return fdFlowId;
	}

	public void setFdFlowId(String fdFlowId) {
		this.fdFlowId = fdFlowId;
	}

	public String getFdStartTime() {
		return fdStartTime;
	}

	public void setFdStartTime(String fdStartTime) {
		this.fdStartTime = fdStartTime;
	}

	public String getFdTaskId() {
		return fdTaskId;
	}

	public void setFdTaskId(String fdTaskId) {
		this.fdTaskId = fdTaskId;
	}

	public static List<FlowDate> getFlowDates(List<IndentFlow> flows) {
		List<FlowDate> lDates = new ArrayList<FlowDate>();
		for (IndentFlow indentFlow : flows) {
			lDates.add(new FlowDate(indentFlow.getFdId(), indentFlow.getFdFlowId(), indentFlow.getFdStartTime(),
					indentFlow.getFdTaskId()));
		}
		return lDates;
	}

	/**
	 * 修改lDates内对象数据 同步到IndentProject <br>
	 * form IndentProject to List<FlowDate>
	 * 
	 * @param indentProject
	 * @param lDates
	 */
	public static void updateFlowDates(IndentProject indentProject, List<FlowDate> lDates) {
		Map<String, String> time = indentProject.getTime();
		for (FlowDate flowDate : lDates) {
			// update time
			String dateStr = time.get(flowDate.getFdTaskId()) != null && "".equals(time.get(flowDate.getFdTaskId()))
					? defaultDate : time.get(flowDate.getFdTaskId());
			flowDate.setFdStartTime(dateStr);
		}
	}

	/**
	 * 时间对象转换 query db date == '1970-01-01' 则时间对象为空（默认值） “” <br>
	 * form List<FlowDate> to IndentProject
	 * 
	 * @param indentProject
	 * @param indentFlows
	 */
	public static void indentProjectFillDate(IndentProject indentProject, List<IndentFlow> indentFlows) {
		Map<String, String> time = new HashMap<>();
		if (null != indentFlows && indentFlows.size() > 0) {
			for (IndentFlow flowDate : indentFlows) {
				final String firstDate = flowDate.getFdStartTime();
				if (ValidateUtil.isValid(firstDate)) {

					String dateStr = flowDate.getFdStartTime().equals(defaultDate) ? "" : flowDate.getFdStartTime();
					time.put(flowDate.getFdTaskId(), dateStr);
				} else {
					time.put(flowDate.getFdTaskId(), "");
				}
			}
		}
		indentProject.setTime(time);
	}

	/**
	 * 创建流程时间节点
	 * 
	 * @param flowId
	 * @param indentProject
	 * @param nodes
	 * @return
	 */
	public static List<FlowDate> createFlowDates(String flowId, IndentProject indentProject, List<ActivitiTask> nodes) {
		List<FlowDate> dates = new ArrayList<>();
		FlowDate flowDate;
		for (ActivitiTask activitiTask : nodes) {
			flowDate = new FlowDate();
			flowDate.setFdFlowId(flowId);
			flowDate.setFdTaskId(activitiTask.getTaskDefinitionKey());
			dates.add(flowDate);
		}
		updateFlowDates(indentProject, dates);
		return dates;
	}
}
