package com.panfeng.service.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.poi.util.PoiReportUtils;
import com.paipianwang.pat.facade.indent.entity.IndentSource;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.paipianwang.pat.workflow.entity.PmsProjectFlow;
import com.paipianwang.pat.workflow.entity.PmsProjectMessage;
import com.paipianwang.pat.workflow.entity.PmsProjectResource;
import com.paipianwang.pat.workflow.entity.PmsProjectSynergy;
import com.paipianwang.pat.workflow.entity.PmsProjectTeam;
import com.paipianwang.pat.workflow.entity.PmsProjectUser;
import com.paipianwang.pat.workflow.enums.ProjectRoleType;
import com.paipianwang.pat.workflow.enums.ProjectTeamType;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectMessageFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectSynergyFacade;
import com.panfeng.domain.BaseMsg;
import com.panfeng.mq.service.ProjectSynergyChangeMQService;
import com.panfeng.service.ProjectFlowService;
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
	@Autowired
	private PmsProjectMessageFacade pmsProjectMessageFacade;


	/**
	 * 修改项目协同人
	 */
	@Override
	public void updateProjectSynergy(HttpServletRequest request, BaseMsg result,SessionInfo sessionInfo) {
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
			String roleName=null;
			if(role.equals(ProjectRoleType.sale)){
				roleName="负责人";
			}else{
				roleName=ProjectRoleType.getEnum(role.getId()).getText();
			}
		
			
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
					insertDetailOperationLog(projectId, "为项目添加了\""+roleName+"\"协同人:"+employee.getEmployeeRealName(), sessionInfo.getReqiureId(), sessionInfo.getRealName());
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
							Map<String,Object> metaData=new HashMap<>();
							metaData.put("principal", synergy.getEmployeeId());
							metaData.put("principalName",synergy.getEmployeeName());
							pmsProjectFlowFacade.update(metaData, projectId);
						}
						insertDetailOperationLog(projectId, "修改项目\""+roleName+"\"为:"+employee.getEmployeeRealName(), sessionInfo.getReqiureId(), sessionInfo.getRealName());
					}
				}
			}
		}
		projectSynergyChangeMQService.sendMessage(projectId);
	}
	
	public void insertDetailOperationLog(String projectId, String content,
			Long fromId, String fromName) {
		PmsProjectMessage message = new PmsProjectMessage();
		message.setFromId("employee_"+fromId);
		message.setFromGroup("manager");//管理员
		message.setProjectId(projectId);
		message.setContent(content);
		message.setMessageType(PmsProjectMessage.TYPE_LOG);
		message.setFromName(fromName);
		pmsProjectMessageFacade.insert(message);
	}
	
	/**
	 * 流程数据导出
	 */
	@Override
	public void exportProjectFlow(List<PmsProjectFlow> list, OutputStream os, SessionInfo sessionInfo) {
		String header = "项目ID,项目名称,项目状态,项目阶段,评级,产品线,等级,配置" 
				+ ",负责人,项目来源,项目预算,预估价格,对标影片网址,项目描述,项目周期,创建时间,更新时间"
				+ ",客户名称 ,客户联系人,联系人电话,客户邮箱,客户评级,客户启动函约定付款时间,客户启动函项目交付时间"
				+ ",策划供应商名称,策划供应商联系人,策划供应商联系人电话,策划供应商预算价格,策划供应商支付价格,对接人姓名,对接人电话,供应商策划内容,项目策划交付时间,策划供应商分配时间"
				+ ",制作供应商名称,制作供应商联系人,制作供应商联系人电话,制作供应商预算价格,制作供应商支付价格,发票带头,供应商制作内容,项目制作交付时间,制作供应商分配时间"
				+ ",团队信息,文件信息";
		
		
		// 创建文档
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
		// 创建一个新的页
		XSSFSheet sheet = xssfWorkbook.createSheet("供应商列表信息");
		// 生成头部信息
		PoiReportUtils.generateHeader(new ArrayList<String>(Arrays.asList(header.split(","))), xssfWorkbook, sheet);

		//冻结头
		sheet.createFreezePane(0, 1, 0, 1);
		
		// 生成数据信息
		this.generateFlowContent(list, xssfWorkbook, sheet);

		
		// 写出响应
		try {
			xssfWorkbook.write(os);
			xssfWorkbook.close();
			Log.error("project list export success ...", sessionInfo);
		} catch (Exception e) {
			Log.error("project list export error:"+e.getMessage(), sessionInfo);
		}
	}

	/**
	 * 导出各行数据
	 * @param list
	 * @param workbook
	 * @param sheet
	 */
	private void generateFlowContent(List<PmsProjectFlow> list, XSSFWorkbook workbook, XSSFSheet sheet) {
		if(ValidateUtil.isValid(list)){
			for(int i=0;i<list.size();i++){
				int j=0;
				PmsProjectFlow flow=list.get(i);
				
				
				//----------值处理-----------
				// 项目状态
				String projectStatus = flow.getProjectStatus();
				if ("finished".equals(projectStatus)) {
					projectStatus= "已完成";
				} else if ("cancel".equals(projectStatus)) {
					projectStatus="已取消";
				} else if ("suspend".equals(projectStatus)) {
					projectStatus= "挂起";
				} else {
					projectStatus= "进行中";
				}
				// 项目阶段
				String projectStage = flow.getProjectStage()+"";
				if ("1".equals(projectStage)) {
					projectStage = "沟通阶段";
				} else if ("2".equals(projectStage)) {
					projectStage = "方案阶段";
				} else if ("3".equals(projectStage)) {
					projectStage = "商务阶段";
				} else if ("4".equals(projectStage)) {
					projectStage = "制作阶段";
				} else if ("5".equals(projectStage)) {
					projectStage = "交付阶段";
				}

				// 项目评级
				String projectGrade = flow.getProjectGrade();
				switch (projectGrade) {
				case "5":
					projectGrade="S";
					break;
				case "4":
					projectGrade="A";
					break;
				case "3":
					projectGrade="B";
					break;
				case "2":
					projectGrade="C";
					break;
				case "1":
					projectGrade= "D";
					break;
				case "0":
					projectGrade="E";
					break;
				default:
					projectGrade= "";
					break;
				}
				// 项目配置
				String lengthName = flow.getProductConfigLengthName() == null ? ""
						: flow.getProductConfigLengthName();
				String addiName = flow.getProductConfigAdditionalPackageName() == null ? ""
						: flow.getProductConfigAdditionalPackageName();

				String productConfigName= (ValidateUtil.isValid(lengthName) && ValidateUtil.isValid(addiName))
						? lengthName + "+" + addiName : lengthName + addiName;
				// 项目来源
				String projectSource="";
				if (flow.getProjectSource() != null) {
					projectSource=IndentSource.enumOf(Integer.parseInt((String) flow.getProjectSource())).getName();
				}
				
				
				//单元格生成、数据填充
				
				XSSFRow xssfRow=sheet.createRow(i+1);
				// 样式
				XSSFCellStyle cs = PoiReportUtils.getLeftCellStyle(workbook);
				
				//项目信息
				XSSFCell xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(flow.getProjectId());
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(flow.getProjectName());
						
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(projectStatus);
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(projectStage);
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(projectGrade);
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(flow.getProductName());
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(flow.getProductConfigLevelName());
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(productConfigName);
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(flow.getPrincipalName());
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(projectSource);
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(flow.getProjectBudget()!=null){
					xssfCell.setCellValue(flow.getProjectBudget());
				}
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(flow.getEstimatedPrice()!=null){
					xssfCell.setCellValue(flow.getEstimatedPrice());
				}
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(flow.getFilmDestPath());
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(flow.getProjectDescription());
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(flow.getProjectCycle()!=null){
					xssfCell.setCellValue(flow.getProjectCycle());
				}
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(flow.getCreateDate());
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(flow.getUpdateDate());
				
				//客户信息
				PmsProjectUser user=flow.getUser();
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(user.getUserName());
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(user.getLinkman());
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(user.getTelephone());
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(user.getEmail());
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(user.getUserLevel());
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(user.getAppointedTime());
				
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(user.getDeliveryTime());
				
				//供应商信息
				List<PmsProjectTeam> teams=flow.getTeamList();
				PmsProjectTeam scheme=null;
				PmsProjectTeam produce=null;
				if(ValidateUtil.isValid(teams)){
					for(PmsProjectTeam team:teams){
						if(ProjectTeamType.scheme.getCode().equals(team.getTeamType())){
							scheme=team;
						}else if(ProjectTeamType.produce.getCode().equals(team.getTeamType())){
							produce=team;
						}
					}
				}
				//策划
				if(scheme==null){
					scheme=new PmsProjectTeam();
					scheme.setTeamType(ProjectTeamType.scheme.getCode());
				}
				j=generateTeamCell(scheme, xssfRow, cs, j);
				//制作
				if(produce==null){
					produce=new PmsProjectTeam();
					produce.setTeamType(ProjectTeamType.produce.getCode());
				}
				j=generateTeamCell(produce, xssfRow, cs, j);
				
				
				// 团队信息
				List<PmsProjectSynergy> synergyList = flow.getSynergyList();
				StringBuilder synergyInfo = new StringBuilder();
				for (PmsProjectSynergy synergy:synergyList) {
					synergyInfo.append(",").append(synergy.getEmployeeName()).append("(")
							.append(ProjectRoleType.getEnum(synergy.getEmployeeGroup()).getText()).append(")");
				}
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(ValidateUtil.isValid(synergyList)){
					xssfCell.setCellValue(synergyInfo.toString().substring(1));
				}
				// 文件信息
				List<PmsProjectResource> resourceList =flow.getResourceList();
				StringBuilder resourceInfo = new StringBuilder();
				for (PmsProjectResource resource:resourceList) {
						resourceInfo.append(",").append(resource.getResourceName()).append("(")
								.append(resource.getUploaderName()).append(")");
				}
				xssfCell=xssfRow.createCell(j++);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(ValidateUtil.isValid(resourceList)){
					xssfCell.setCellValue(resourceInfo.toString().substring(1));
				}
			}
			
		}
		
	}
	
	/**
	 * 生成各行供应商数据
	 * @param team
	 * @param xssfRow
	 * @param cs
	 * @param j
	 * @return
	 */
	private int generateTeamCell(PmsProjectTeam team,XSSFRow xssfRow,XSSFCellStyle cs,int j){
		
		XSSFCell xssfCell=xssfRow.createCell(j++);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		xssfCell.setCellValue(team.getTeamName());
		
		xssfCell=xssfRow.createCell(j++);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		xssfCell.setCellValue(team.getLinkman());
		
		xssfCell=xssfRow.createCell(j++);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		xssfCell.setCellValue(team.getTelephone());
		
		xssfCell=xssfRow.createCell(j++);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		if(team.getBudget()!=null){
			xssfCell.setCellValue(team.getBudget());
		}
		
		xssfCell=xssfRow.createCell(j++);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		if(team.getActualPrice()!=null){
			xssfCell.setCellValue(team.getActualPrice());
		}
		
		//邮件相关信息-策划、制作
		if(ProjectTeamType.scheme.getCode().equals(team.getTeamType())){
			xssfCell=xssfRow.createCell(j++);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
			xssfCell.setCellValue(team.getAccessMan());
			
			xssfCell=xssfRow.createCell(j++);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
			xssfCell.setCellValue(team.getAccessManTelephone());
			
			xssfCell=xssfRow.createCell(j++);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
			xssfCell.setCellValue(team.getPlanContent());
			
			xssfCell=xssfRow.createCell(j++);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
			xssfCell.setCellValue(team.getPlanTime());
		}else if(ProjectTeamType.produce.getCode().equals(team.getTeamType())){
			xssfCell=xssfRow.createCell(j++);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
			xssfCell.setCellValue(team.getInvoiceHead());
			
			xssfCell=xssfRow.createCell(j++);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
			xssfCell.setCellValue(team.getMakeContent());
			
			xssfCell=xssfRow.createCell(j++);
			xssfCell.setCellStyle(cs);
			xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
			xssfCell.setCellValue(team.getMakeTime());
		}
		
		xssfCell=xssfRow.createCell(j++);
		xssfCell.setCellStyle(cs);
		xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		xssfCell.setCellValue(team.getCreateDate());
		return j;
	}

}
