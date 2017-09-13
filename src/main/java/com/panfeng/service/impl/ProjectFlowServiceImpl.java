package com.panfeng.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.indent.entity.IndentSource;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectSynergyFacade;
import com.panfeng.domain.BaseMsg;
import com.panfeng.mq.service.ProjectSynergyChangeMQService;
import com.panfeng.service.ProjectFlowService;
import com.panfeng.util.CsvWriter;
import com.panfeng.util.Log;

@Service
public class ProjectFlowServiceImpl implements ProjectFlowService {
	@Autowired
	private PmsProjectSynergyFacade pmsProjectSynergyFacade;
	@Autowired
	private PmsEmployeeFacade pmsEmployeeFacade;
	@Autowired
	private PmsProjectFlowFacade pmsProjectFlowFacade;
	@Autowired
	private ProjectSynergyChangeMQService projectSynergyChangeMQService;
	/**
	 * 流程数据导出
	 */
	@Override
	public void exportProjectFlow(List<PmsProjectFlow> list, HttpServletResponse response, SessionInfo sessionInfo) {
		// 完成数据csv文件的封装
		String displayColNames = "项目ID,项目名称,项目状态,项目阶段,评级,产品线,等级,配置" 
				+ ",负责人,项目来源,项目预算,预估价格,对标影片网址,项目描述,项目周期,创建时间,更新时间"
				+ ",客户名称 ,客户联系人,联系人电话,客户邮箱,客户评级,客户启动函约定付款时间,客户启动函项目交付时间"
				+ ",策划供应商名称,策划供应商联系人,策划供应商联系人电话,策划供应商预算价格,策划供应商支付价格,对接人姓名,对接人电话,供应商策划内容,项目策划交付时间,策划供应商分配时间"
				+ ",制作供应商名称,制作供应商联系人,制作供应商联系人电话,制作供应商预算价格,制作供应商支付价格,发票带头,供应商制作内容,项目制作交付时间,制作供应商分配时间"
				+ ",团队信息,文件信息";
		String matchColNames = "projectId,projectName,projectStatus,projectStage,projectGrade,productName,productConfigLevelName,productConfigName"
				+ ",principalName,projectSource,projectBudget,estimatedPrice,filmDestPath,projectDescription,projectCycle,createDate,updateDate"
				+ ",userName,linkman,telephone,email,userLevel,appointedTime,deliveryTime"
				+ ",scheme_teamName,scheme_linkman,scheme_telephone,scheme_budget,scheme_actualPrice,scheme_accessMan,scheme_accessManTelephone,scheme_planContent,scheme_planTime,scheme_createDate"
				+ ",produce_teamName,produce_linkman,produce_telephone,produce_budget,produce_actualPrice,produce_invoiceHead,produce_makeContent,produce_makeTime,produce_createDate"
				+ ",synergyInfo,resourceInfo";
		List<Map<String, Object>> datas = JsonUtil.getValueListMap(list);
		// 数据处理
		for (Map<String, Object> data : datas) {
			// 客户信息
			String user = (String) data.get("user");
			if (user != null) {
				Map<String, Object> userMap = JSON.parseObject(user);
				data.putAll(userMap);
			}

			// 供应商信息
			String teamList = (String) data.get("teamList");
			JSONArray teamArray = JSONArray.parseArray(teamList);
			for (int i = 0; i < teamArray.size(); i++) {
				JSONObject team = teamArray.getJSONObject(i);
				Iterator<String> iterator = team.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					data.put(team.get("teamType") + "_" + key, team.get(key));
				}
			}
			// 团队信息
			String synergyList = (String) data.get("synergyList");
			JSONArray synergyArray = JSONArray.parseArray(synergyList);
			StringBuilder synergyInfo = new StringBuilder();
			for (int i = 0; i < synergyArray.size(); i++) {
				JSONObject synergy = synergyArray.getJSONObject(i);
				synergyInfo.append(",").append(synergy.getString("employeeName")).append("(")
						.append(ProjectRoleType.getEnum(synergy.getString("employeeGroup")).getText()).append(")");
			}
			data.put("synergyInfo", synergyInfo.toString().substring(1));
			// 文件信息
			String resourceList = (String) data.get("resourceList");
			JSONArray resourceArray = JSONArray.parseArray(resourceList);
			if (resourceArray != null && resourceArray.size() > 0) {
				StringBuilder resourceInfo = new StringBuilder();
				for (int i = 0; i < resourceArray.size(); i++) {
					JSONObject resource = resourceArray.getJSONObject(i);
					resourceInfo.append(",").append(resource.getString("resourceName")).append("(")
							.append(resource.getString("uploaderName")).append(")");
				}
				data.put("resourceInfo", resourceInfo.toString().substring(1));
			}

			// ------------显示值处理------------------
			// 项目状态
			String projectStatus = (String) data.get("projectStatus");
			if ("finished".equals(projectStatus)) {
				data.put("projectStatus", "已完成");
			} else if ("cancel".equals(projectStatus)) {
				data.put("projectStatus", "已取消");
			} else if ("suspend".equals(projectStatus)) {
				data.put("projectStatus", "挂起");
			} else {
				data.put("projectStatus", "进行中");
			}
			// 项目评级
			String projectGrade = (String) data.get("projectGrade");
			switch (projectGrade) {
			case "5":
				data.put("projectGrade", "S");
				break;
			case "4":
				data.put("projectGrade", "A");
				break;
			case "3":
				data.put("projectGrade", "B");
				break;
			case "2":
				data.put("projectGrade", "C");
				break;
			case "1":
				data.put("projectGrade", "D");
				break;
			case "0":
				data.put("projectGrade", "E");
				break;
			default:
				data.put("projectGrade", "");
				break;
			}
			// 项目配置
			String lengthName = data.get("productConfigLengthName") == null ? ""
					: (String) data.get("productConfigLengthName");
			String addiName = data.get("productConfigAdditionalPackageName") == null ? ""
					: (String) data.get("productConfigAdditionalPackageName");

			data.put("productConfigName", (ValidateUtil.isValid(lengthName) && ValidateUtil.isValid(addiName))
					? lengthName + "+" + addiName : lengthName + addiName);
			// 项目来源
			if (data.get("projectSource") != null) {
				data.put("projectSource",
						IndentSource.enumOf(Integer.parseInt((String) data.get("projectSource"))).getName());
			}
			// 项目阶段
			String projectStage = (String) data.get("projectStage");
			if (projectStage == "1") {
				projectStage = "沟通阶段";
			} else if (projectStage == "2") {
				projectStage = "方案阶段";
			} else if (projectStage == "3") {
				projectStage = "商务阶段";
			} else if (projectStage == "4") {
				projectStage = "制作阶段";
			} else if (projectStage == "5") {
				projectStage = "交付阶段";
			}
			data.put("projectStage", projectStage);
		}

		String fileName = "project_report_";
		String content = CsvWriter.formatCsvData(datas, displayColNames, matchColNames);
		try {
			Log.error("project list export success ...", sessionInfo);
			CsvWriter.exportCsv(fileName, content, response);

		} catch (IOException e) {
			Log.error("project list export error ...", sessionInfo);
		}
	}

	/**
	 * 修改项目协同人
	 */
	@Override
	public void updateProjectSynergy(HttpServletRequest request, BaseMsg result) {
//		Map m=request.getParameterMap();
		
		String projectId=request.getParameter("projectId");
		if(!ValidateUtil.isValid(projectId)){
			result.setCode(BaseMsg.ERROR);
			result.setErrorMsg("数据错误，修改失败");
			return ;
		}
		
		Map<String,PmsProjectSynergy> synergyAll=pmsProjectSynergyFacade.getSynergysByProjectId(projectId);
		
		for (ProjectRoleType role : ProjectRoleType.values()) {
			String select=request.getParameter(role.getId());
			if(ValidateUtil.isValid(select)){
				String id=select.split("_")[1];
				//变更则修改
				PmsProjectSynergy synergy=synergyAll.get(role.getId());
				if(synergy==null){
					//添加
					PmsEmployee employee=pmsEmployeeFacade.findEmployeeById(Long.parseLong(id));
					synergy=new PmsProjectSynergy();
					synergy.setEmployeeGroup(role.getId());
					synergy.setEmployeeId(Integer.parseInt(id));
					synergy.setEmployeeName(employee.getEmployeeRealName());
					synergy.setImgUrl(employee.getEmployeeImg());
					synergy.setProjectId(projectId);
					synergy.setTelephone(employee.getPhoneNumber());
					pmsProjectSynergyFacade.insert(synergy);
				}else{
					//比较 变更则修改
					if(!synergy.getEmployeeId().equals(Integer.parseInt(id))){
						PmsEmployee employee=pmsEmployeeFacade.findEmployeeById(Long.parseLong(id));
						synergy.setEmployeeId(Integer.parseInt(id));
						synergy.setEmployeeName(employee.getEmployeeRealName());
						synergy.setImgUrl(employee.getEmployeeImg());
						synergy.setTelephone(employee.getPhoneNumber());
						pmsProjectSynergyFacade.update(synergy);
						//负责人
						if(ProjectRoleType.sale.getId().equals(role.getId())){
							Map<String,Object> metaData=new HashMap();
							metaData.put("principal", synergy.getEmployeeId());
							metaData.put("principalName",synergy.getEmployeeName());
							pmsProjectFlowFacade.update(metaData, projectId);
						}
					}
				}
			}
		}
		projectSynergyChangeMQService.sendMessage(projectId);
	}

}
