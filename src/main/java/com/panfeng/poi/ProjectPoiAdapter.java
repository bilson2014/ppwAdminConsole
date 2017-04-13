package com.panfeng.poi;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.paipianwang.pat.common.util.ValidateUtil;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.resource.model.Synergy;
import com.panfeng.util.DateUtils;

public class ProjectPoiAdapter extends PoiBaseAdapter<IndentProject> {

	List<IndentProject> indentList = new ArrayList<IndentProject>();

	@Override
	public int createHead(XSSFSheet sheet, XSSFWorkbook workbook) {
		// 设置合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 24));//拍片网项目统筹表
		sheet.addMergedRegion(new CellRangeAddress(2, 4, 0, 0));//项目编号
		sheet.addMergedRegion(new CellRangeAddress(2, 4, 1, 1));//项目名称
		sheet.addMergedRegion(new CellRangeAddress(2, 4, 2, 2));//项目来源
		sheet.addMergedRegion(new CellRangeAddress(2, 4, 3, 3));//个人信息下单来源
		sheet.addMergedRegion(new CellRangeAddress(2, 4, 4, 4));//项目负责人
		sheet.addMergedRegion(new CellRangeAddress(2, 4, 5, 5));//协同人及比例

		sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 15));//项目进展
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 6, 6));//阶段
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 7, 7));//状态
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 8, 8));//解决方法及下阶段时间→//立项时间
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 9, 9));//立项时间→交付时间
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 10, 10));//交付时间→最后更新时间
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 11, 11));//剩余时间
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 12, 12));//延期交付时间
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 13, 13));//周期
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 14, 14));//预算金额
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 15, 15));//项目金额

		sheet.addMergedRegion(new CellRangeAddress(2, 2, 16, 24));//基础信息
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 16, 20));//客户信息及负责人
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 21, 24));//制作团队导演

		// 填充数据
		// 第一行 -- 二行
		XSSFRow xssfRow = sheet.createRow(0);
		xssfRow.setHeightInPoints(30);
		XSSFCell xssfCell = xssfRow.createCell(0);
		xssfCell.setCellValue("拍片网项目统筹表");
		xssfCell.setCellStyle(PoiUtils.getDefaultTitleStyle(workbook));
		// 三行 --》一列
		xssfRow = sheet.createRow(2);
		xssfRow.setHeightInPoints(20);
		XSSFCellStyle cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(0);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("项目编号");
		// 三行 --》二列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(1);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("项目名称");
		// 三行 --》三列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(2);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("项目来源");
		// 三行 --》四列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(3);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("个人信息下单来源");
		// 三行 --》五列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(4);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("项目负责人");

		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(5);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("协同人及比例");

		// 三行 --》六列 -- 十五列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(6);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("项目进展");
		// 三行 --》六列 -- 十六列
		//cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		//xssfCell = xssfRow.createCell(16);
		//xssfCell.setCellStyle(cellStyle);
		//xssfCell.setCellValue("说明（特殊情况）");
		// 三行 --》十七列 -- 二十六列
		// 三行 --》六列 -- 十六列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		//xssfCell = xssfRow.createCell(17);
		xssfCell = xssfRow.createCell(16);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("基础信息");

		// 四行 --》六列
		xssfRow = sheet.createRow(3);
		xssfRow.setHeightInPoints(20);
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(6);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("阶段");
		// 四行 --》七列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(7);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("状态");

		// 四行 --》八列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(8);
		xssfCell.setCellStyle(cellStyle);
		//xssfCell.setCellValue("解决方法及下阶段时间点");
		xssfCell.setCellValue("立项时间");

		// 四行 --》九列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(9);
		xssfCell.setCellStyle(cellStyle);
		//xssfCell.setCellValue("立项时间");
		xssfCell.setCellValue("交付时间");

		// 四行 --》十列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(10);
		xssfCell.setCellStyle(cellStyle);
		//xssfCell.setCellValue("交付时间");
		xssfCell.setCellValue("最后更改时间");

		// 四行 --》十一列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(11);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("剩余时间");
		// 四行 --》十二列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(12);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("延期交付时间");
		// 四行 --》十三列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(13);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("周期（天）");

		// 四行 --》十四列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(14);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("预算金额（元）");
		// 四行 --》十五列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(15);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("项目金额（元）");

		// 四行 --》十七列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(16);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("客户信息及负责人");

		// 四行 --》二十一列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(21);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("制作团队信息及导演");

		// 四行 --》二十五列
		/*cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(26);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("当月应回款");

		// 四行 --》二十六列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(27);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("次月应回款");*/

		// 五行 --》十七列
		xssfRow = sheet.createRow(4);
		xssfRow.setHeightInPoints(20);
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(16);
	//	xssfCell = xssfRow.createCell(17);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("客户名称");
		// 五行 --》十八列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(17);
		//xssfCell = xssfRow.createCell(18);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("负责人");
		// 五行 --》十九列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		//xssfCell = xssfRow.createCell(19);
		xssfCell = xssfRow.createCell(18);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("联系方式");
		// 五行 --》二十列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		//xssfCell = xssfRow.createCell(20);
		xssfCell = xssfRow.createCell(19);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("实付金额");

		// 五行 --》二十一列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		//xssfCell = xssfRow.createCell(21);
		xssfCell = xssfRow.createCell(20);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("客户评级");

		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		//xssfCell = xssfRow.createCell(22);
		xssfCell = xssfRow.createCell(21);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("供应商名称");
		// 五行 --》二十二列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		//xssfCell = xssfRow.createCell(23);
		xssfCell = xssfRow.createCell(22);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("负责人（导演）");
		// 五行 --》二十三列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		//xssfCell = xssfRow.createCell(24);
		xssfCell = xssfRow.createCell(23);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("联系方式");
		// 五行 --》二十四列
		cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		//xssfCell = xssfRow.createCell(25);
		xssfCell = xssfRow.createCell(24);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("实付金额");

		// 设置行宽
		sheet.setColumnWidth(0, 14 * 256);
		sheet.setColumnWidth(1, 14 * 256);
		sheet.setColumnWidth(2, 15 * 256);
		sheet.setColumnWidth(3, 16 * 256);
		sheet.setColumnWidth(4, 16 * 256);
		sheet.setColumnWidth(5, 16 * 256);
		sheet.setColumnWidth(6, 20 * 256);
		sheet.setColumnWidth(7, 23 * 256);
		sheet.setColumnWidth(8, 20 * 256);
		sheet.setColumnWidth(9, 14 * 256);
		sheet.setColumnWidth(10, 14 * 256);
		sheet.setColumnWidth(11, 14 * 256);
		sheet.setColumnWidth(12, 14 * 256);
		sheet.setColumnWidth(13, 14 * 256);
		sheet.setColumnWidth(14, 14 * 256);
		sheet.setColumnWidth(15, 23 * 256);
		sheet.setColumnWidth(16, 25 * 256);
		sheet.setColumnWidth(17, 14 * 256);
		sheet.setColumnWidth(18, 18 * 256);
		sheet.setColumnWidth(19, 14 * 256);
		sheet.setColumnWidth(20, 25 * 256);
		sheet.setColumnWidth(21, 14 * 256);
		sheet.setColumnWidth(22, 18 * 256);
		sheet.setColumnWidth(23, 14 * 256);
		sheet.setColumnWidth(24, 14 * 256);
		sheet.setColumnWidth(25, 14 * 256);
		return 5;
	}

	@Override
	public void getItemView(XSSFSheet sheet, XSSFWorkbook workbook, IndentProject entity, int itemId) {
		XSSFRow xssfRow = sheet.createRow(itemId);
		xssfRow.createCell(0).setCellValue(entity.getSerial());
		xssfRow.createCell(1).setCellValue(entity.getProjectName());
		xssfRow.createCell(2).setCellValue(entity.getSource());
		if (ValidateUtil.isValid(entity.getSource()) && entity.getSource().equals("个人信息下单")) {
			xssfRow.createCell(3).setCellValue(entity.getReferrerName());
		}
		xssfRow.createCell(4).setCellValue(entity.getEmployeeRealName());

		// 协同人
		StringBuffer stringBuffer = new StringBuffer();
		if (entity.getSynergys() != null) {
			for (Synergy synergy : entity.getSynergys()) {
				stringBuffer.append(synergy.getUserName());
				stringBuffer.append("(");
				stringBuffer.append(synergy.getRatio());
				stringBuffer.append("%");
				stringBuffer.append(") ");
			}
		}
		xssfRow.createCell(5).setCellValue(stringBuffer.toString());
		
		xssfRow.createCell(6).setCellValue(entity.getStage());
		// 状态
		String state = "";
		XSSFCell xssfCell = xssfRow.createCell(7);
		XSSFCellStyle xssfCellStyle = null;
		switch (entity.getState()) {
		case IndentProject.PROJECT_NORMAL:
			state = "正常";
			xssfCellStyle = PoiUtils.getCustomColorsCellStyle(workbook, Color.green);
			break;
		case IndentProject.PROJECT_CANCEL:
			state = "取消";
			xssfCellStyle = PoiUtils.getCustomColorsCellStyle(workbook, Color.red);
			break;
		case IndentProject.PROJECT_FINISH:
			xssfCellStyle = PoiUtils.getCustomColorsCellStyle(workbook, Color.black);
			state = "完成";
			break;
		case IndentProject.PROJECT_SUSPEND:
			xssfCellStyle = PoiUtils.getCustomColorsCellStyle(workbook, Color.orange);
			state = "暂停";
			break;
		}
		if (xssfCell != null)
			xssfCell.setCellStyle(xssfCellStyle);
		xssfCell.setCellValue(state);
		// 立项时间
		xssfRow.createCell(8).setCellValue(DateUtils.getDateByFormat2(entity.getCreateTime(), "yyyy-MM-dd"));
		//-->去除ActivitiTask,从sql中获取,加快速度
		String jfDate = entity.getFdStartTime();
		if (ValidateUtil.isValid(jfDate)) {
			// 交付时间
			xssfRow.createCell(9).setCellValue(jfDate);
			if (null!=entity.getState() && entity.getState() != 2) {//未完成
				// 最后更新时间
				xssfRow.createCell(10).setCellValue(DateUtils.getDateByFormat2(entity.getUpdateTime(), "yyyy-MM-dd"));
				// 剩余时间
				Date date1 = DateUtils.getDateByFormat(jfDate, "yyyy-MM-dd");
				Date date2 = DateUtils.getDateByFormat(new Date(), "yyyy-MM-dd");
				int c = DateUtils.dateInterval(date1.getTime(), date2.getTime());
				xssfCellStyle = PoiUtils.getDefaultErrorCellStyle(workbook);
				xssfCell = xssfRow.createCell(10 + 1);
				xssfCell.setCellStyle(xssfCellStyle);
				if (c <= 0)
					xssfCell.setCellValue("已过期");
				else
					xssfCell.setCellValue("剩余" + c + "天");

				date2 = DateUtils.getDateByFormat(entity.getCreateTime(), "yyyy-MM-dd");
				c = DateUtils.dateInterval(date1.getTime(), date2.getTime());
				// 包含结束天
				c++;
				// 周期（天）
				xssfRow.createCell(13).setCellValue(c + "天");
			}
		}
		// 延期交付时间
		xssfRow.createCell(12).setCellValue("");
		// 预算金额（元）
		xssfRow.createCell(14).setCellValue(entity.getPriceFirst() + "元");
		// 项目金额（元）
		xssfRow.createCell(15).setCellValue(entity.getPriceFinish() + "元");
		// 说明（特殊情况）
		//xssfRow.createCell(16).setCellValue("");
		// 客户名称
		xssfRow.createCell(16).setCellValue(entity.getUserName());
		// 客户负责人
		xssfRow.createCell(17).setCellValue(entity.getUserContact());
		// 联系方式
		xssfRow.createCell(18).setCellValue(entity.getUserPhone());
		// 实付金额
		xssfRow.createCell(19).setCellValue(entity.getCustomerPayment() + "元");

		switch (entity.getClientLevel()) {
		case 0:
			xssfRow.createCell(20).setCellValue("A");
			break;
		case 1:
			xssfRow.createCell(20).setCellValue("B");
			break;
		case 2:
			xssfRow.createCell(20).setCellValue("C");
			break;
		default:
			xssfRow.createCell(20).setCellValue("");
			break;
		}

		// 供应商名称
		xssfRow.createCell(21).setCellValue(entity.getTeamName());
		// 负责人（导演）
		xssfRow.createCell(22).setCellValue(entity.getTeamContact());
		// 联系方式
		xssfRow.createCell(23).setCellValue(entity.getTeamPhone());
		// 实付金额
		xssfRow.createCell(24).setCellValue(entity.getProviderPayment() + "元");
	}

	@Override
	public List<IndentProject> getData() {
		return indentList;
	}
	@Override
	public void setData(List<IndentProject> indentList) {
		this.indentList = indentList;
	}
	
}
