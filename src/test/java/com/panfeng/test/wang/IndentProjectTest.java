package com.panfeng.test.wang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.activiti.engine.history.HistoricTaskInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.paipianwang.pat.common.constant.PmsConstant;
import com.panfeng.persist.IndentFlowMapper;
import com.panfeng.persist.IndentProjectMapper;
import com.panfeng.poi.GenerateExcel;
import com.panfeng.poi.ProjectPoiAdapter;
import com.panfeng.resource.model.ActivitiTask;
import com.panfeng.resource.model.IndentFlow;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.Synergy;
import com.panfeng.resource.model.UserViewModel;
import com.panfeng.service.IndentActivitiService;
import com.panfeng.service.IndentProjectService;
import com.panfeng.service.SynergyService;
import com.panfeng.service.UserTempService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext.xml" })
public class IndentProjectTest {
	@Autowired
	IndentProjectMapper indentProjectMapper;
	@Autowired
	IndentFlowMapper indentFlowMapper;
	@Autowired
	IndentActivitiService indentActivitiService;
	@Autowired
	ApplicationContext applicationContext;
	@Autowired
	IndentProjectMapper indentProjectMapper2;
	@Autowired
	IndentProjectService indentProjectService;
	@Autowired
	SynergyService synergyService;
	
	@Test
	public void test2() {
		ProjectPoiAdapter projectPoiAdapter = new ProjectPoiAdapter();
		GenerateExcel ge = new GenerateExcel();
		IndentProject ip = new IndentProject();
		ip.setUserType(PmsConstant.ROLE_EMPLOYEE);
		ip.setUserId(36L);
		List<IndentProject> list = indentProjectMapper.findProjectList(ip);
		UserTempService userTempService=applicationContext.getBean(UserTempService.class);
		for (IndentProject indentProject2 : list) {
			List<IndentFlow> listDates = indentFlowMapper
					.findFlowDateByIndentId(indentProject2);
			IndentFlow.indentProjectFillDate(indentProject2, listDates);
			ActivitiTask at = indentActivitiService
					.getCurrentTask(indentProject2);
			if (at.getId().equals("")) {
				List<HistoricTaskInstance> listHistoricTaskInstances = indentActivitiService
						.getHistoryProcessTask_O(indentProject2);
				HistoricTaskInstance historicTaskInstance = listHistoricTaskInstances
						.get(listHistoricTaskInstances.size() - 1);
				at.setId("");
				at.setName("已完成");
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
				at.setCreateTime(simpleDateFormat.format(historicTaskInstance.getEndTime()));
			}
			indentProject2.setTask(at);
			//填充管家
			UserViewModel userViewModel=userTempService.getInfo(indentProject2.getUserType(), indentProject2.getUserId());
			indentProject2.setUserViewModel(userViewModel);
			indentProject2.setEmployeeRealName(userViewModel.getUserName());
			projectPoiAdapter.getData().add(indentProject2);
		}
		if (list != null) {
			for (IndentProject indentProject2 : list) {
				List<Synergy> Synergys = synergyService.findSynergyByProjectId(indentProject2.getId());
				indentProject2.setSynergys(Synergys);
			}
		}
		try {
			OutputStream outputStream=new FileOutputStream(new File("F:\\test.xlsx"));
			ge.generate(projectPoiAdapter,outputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void getProjectCount(){
		System.out.println(indentProjectMapper2.getProjectCount());
	}
	@Test
	public void getid(){
		System.out.println(indentProjectService.getProjectSerialID(1L));
	}
}
