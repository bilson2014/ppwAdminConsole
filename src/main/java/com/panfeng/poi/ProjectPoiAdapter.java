package com.panfeng.poi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.panfeng.resource.model.IndentProject;

public class ProjectPoiAdapter extends PoiBaseAdapter<IndentProject> {

	List<IndentProject> indentList = new ArrayList<IndentProject>();

	@Override
	public int createHead(XSSFSheet sheet, XSSFWorkbook workbook) {
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 21));
		sheet.addMergedRegion(new CellRangeAddress(2, 4, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(2, 4, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(2, 4, 2, 2));
		sheet.addMergedRegion(new CellRangeAddress(2, 4, 3, 3));
		// 阶段预计时间
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 4, 8));
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 4, 4));
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 5, 5));
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 6, 6));
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 7, 7));
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 8, 8));
		// 项目进展
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 9, 12));
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 9, 9));
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 10, 10));
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 11, 11));
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 12, 12));
		// 说明
		sheet.addMergedRegion(new CellRangeAddress(2, 4, 13, 13));
		// 基础信息
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 14, 19));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 14, 16));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 17, 19));
		// 价格 交付时间
		sheet.addMergedRegion(new CellRangeAddress(2, 4, 20, 20));
		sheet.addMergedRegion(new CellRangeAddress(2, 4, 21, 21));

		sheet.setColumnWidth(0, 12 * 256);
		sheet.setColumnWidth(1, 12 * 256);
		sheet.setColumnWidth(2, 12 * 256);
		sheet.setColumnWidth(3, 12 * 256);
		sheet.setColumnWidth(13, 17 * 256);

		XSSFRow xssfRow = sheet.createRow(0);
		XSSFCell xssfCell = xssfRow.createCell(0);
		xssfCell.setCellValue("我是标题");
		xssfCell.setCellStyle(PoiUtils.getDefaultTitleStyle(workbook));
		xssfRow = sheet.createRow(2);

		XSSFCellStyle cellStyle = PoiUtils.getDefaultCenterCellStyle(workbook);
		xssfCell = xssfRow.createCell(0);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("项目编号");
		xssfCell = xssfRow.createCell(1);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("项目名称");
		xssfCell = xssfRow.createCell(2);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("项目来源");
		xssfCell = xssfRow.createCell(3);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("项目负责人");
		xssfCell = xssfRow.createCell(4);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("阶段预计时间");
		xssfCell = xssfRow.createCell(9);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("项目进展");
		xssfCell = xssfRow.createCell(13);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("说明（特殊情况）");
		xssfCell = xssfRow.createCell(14);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("基础信息");

		xssfCell = xssfRow.createCell(20);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("价格（万元）");
		xssfCell = xssfRow.createCell(21);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("交付时间");

		xssfRow = sheet.createRow(3);
		xssfCell = xssfRow.createCell(4);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("沟通");
		xssfCell = xssfRow.createCell(5);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("方案");
		xssfCell = xssfRow.createCell(6);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("商务");
		xssfCell = xssfRow.createCell(7);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("制作");
		xssfCell = xssfRow.createCell(8);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("交付");

		xssfCell = xssfRow.createCell(9);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("节点");
		xssfCell = xssfRow.createCell(10);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("时间点");
		xssfCell = xssfRow.createCell(11);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("状态");
		xssfCell = xssfRow.createCell(12);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("解决方法");
		xssfCell = xssfRow.createCell(14);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("客户信息及负责人");
		xssfCell = xssfRow.createCell(17);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("制作团队信息及导演");

		xssfRow = sheet.createRow(4);
		xssfCell = xssfRow.createCell(14);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("客户名称");
		xssfCell = xssfRow.createCell(15);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("负责人");
		xssfCell = xssfRow.createCell(16);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("联系方式");
		xssfCell = xssfRow.createCell(17);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("供应商名称");
		xssfCell = xssfRow.createCell(18);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("负责人（导演）");
		xssfCell = xssfRow.createCell(19);
		xssfCell.setCellStyle(cellStyle);
		xssfCell.setCellValue("联系方式");

		return 5;
	}

	@Override
	public void getItemView(XSSFSheet sheet, XSSFWorkbook workbook,
			IndentProject entity, int itemId) {
		XSSFRow xssfRow = sheet.createRow(itemId);

		xssfRow.createCell(0).setCellValue(entity.getId());
		xssfRow.createCell(1).setCellValue(entity.getUserName());
		xssfRow.createCell(2).setCellValue(entity.getSource());
		xssfRow.createCell(3).setCellValue("管家");
		/*xssfRow.createCell(4).setCellValue(entity.getGtstarttime());
		xssfRow.createCell(5).setCellValue(entity.getFastarttime());
		xssfRow.createCell(6).setCellValue(entity.getSwstarttime());
		xssfRow.createCell(7).setCellValue(entity.getZzstarttime());
		xssfRow.createCell(8).setCellValue(entity.getJfstarttime());*/

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (!entity.getTask().getId().equals("")) {
			xssfRow.createCell(9).setCellValue(entity.getTask().getName());
			xssfRow.createCell(10).setCellValue(
					simpleDateFormat.format(entity.getTask().getCreateTime()));
			xssfRow.createCell(11).setCellValue("状态");
			xssfRow.createCell(12).setCellValue("解决方法");
			xssfRow.createCell(21).setCellValue("");
		} else {
			xssfRow.createCell(9).setCellValue(entity.getTask().getName());
			xssfRow.createCell(10).setCellValue("~");
			xssfRow.createCell(11).setCellValue("状态");
			xssfRow.createCell(12).setCellValue("解决方法");
			xssfRow.createCell(21).setCellValue(
					simpleDateFormat.format(entity.getTask().getCreateTime()));
		}

		xssfRow.createCell(13).setCellValue("");
		xssfRow.createCell(14).setCellValue(entity.getUserName());
		xssfRow.createCell(15).setCellValue(entity.getUserContact());
		xssfRow.createCell(16).setCellValue(entity.getUserPhone());
		xssfRow.createCell(17).setCellValue(entity.getTeamName());
		xssfRow.createCell(18).setCellValue(entity.getTeamContact());
		xssfRow.createCell(19).setCellValue(entity.getTeamPhone());
		xssfRow.createCell(20).setCellValue(entity.getPrice());
	}

	@Override
	public List<IndentProject> getData() {
		return indentList;
	}

}
