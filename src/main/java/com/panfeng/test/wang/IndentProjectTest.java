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

import com.panfeng.domain.GlobalConstant;
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
		ip.setUserType(GlobalConstant.ROLE_EMPLOYEE);
		ip.setUserId(36);
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

	// public Map<String,CellStyle> createCellStyle(Workbook wb,List<StationMap>
	// colorArr){
	// final Map<String,CellStyle> styles = new HashMap<String, CellStyle>();
	// final CellStyle style = wb.createCellStyle();
	// style.setTopBorderColor(IndexedColors.BLACK.index);
	// style.setBottomBorderColor(IndexedColors.BLACK.index);
	// style.setLeftBorderColor(IndexedColors.BLACK.index);
	// style.setBorderTop(CellStyle.BORDER_THIN);
	// style.setBorderBottom(CellStyle.BORDER_THIN);
	// style.setBorderLeft(CellStyle.BORDER_THIN);
	// style.setBorderRight(CellStyle.BORDER_THIN);
	// styles.put("normar", style); // 未渲染
	//
	// for(StationMap sm : colorArr){
	// final CellStyle colorStyle = wb.createCellStyle();
	// XSSFCellStyle styleTemp = (XSSFCellStyle) colorStyle;
	// styleTemp.setTopBorderColor(IndexedColors.BLACK.index);
	// styleTemp.setBottomBorderColor(IndexedColors.BLACK.index);
	// styleTemp.setLeftBorderColor(IndexedColors.BLACK.index);
	// styleTemp.setBorderTop(CellStyle.BORDER_THIN);
	// styleTemp.setBorderBottom(CellStyle.BORDER_THIN);
	// styleTemp.setBorderLeft(CellStyle.BORDER_THIN);
	// styleTemp.setBorderRight(CellStyle.BORDER_THIN);
	// styleTemp.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
	// final String color = sm.getColor();
	// if(color.indexOf("(") > -1 && color.indexOf("") > -1){ // 不为空
	// String[] str = color.split("\\(")[1].split("\\)")[0].split(",");
	// styleTemp.setFillForegroundColor(new XSSFColor(new
	// Color(Integer.valueOf(str[0].trim()),Integer.valueOf(str[1].trim()),Integer.valueOf(str[2].trim()))));
	// styles.put(String.valueOf(sm.getPrice()), styleTemp);
	// }
	// }
	// return styles;
	// }
	@Test
	public void getProjectCount(){
		System.out.println(indentProjectMapper2.getProjectCount());
	}
	@Test
	public void getid(){
		System.out.println(indentProjectService.getProjectSerialID(1L));
	}
}
