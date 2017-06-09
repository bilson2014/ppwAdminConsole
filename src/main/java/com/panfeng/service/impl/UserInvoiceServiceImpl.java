package com.panfeng.service.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.paipianwang.pat.common.enums.AuditStatus;
import com.paipianwang.pat.common.enums.InvoiceType;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.poi.util.PoiReportUtils;
import com.paipianwang.pat.facade.finance.entity.PmsUserInvoice;
import com.panfeng.service.UserInvoiceService;

@Service
public class UserInvoiceServiceImpl implements UserInvoiceService {

	private static String[] header = { "发票号", "发票类型", "发票内容", "价税合计", "税率", "开票日期", "管家领取日期", "客户名称", "项目名称", "领取人",
			"备注", "审批状态", "原因" };

	@Override
	public void generateReport(List<PmsUserInvoice> list, OutputStream os) {

		// 创建文档
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
		// 创建一个新的页
		XSSFSheet sheet = xssfWorkbook.createSheet("客户发票清单");
		// 生成头部信息
		PoiReportUtils.generateHeader(new ArrayList<String>(Arrays.asList(header)), xssfWorkbook, sheet);

		// 生成数据信息
		this.generateTeamInvoiceContent(list, xssfWorkbook, sheet);

		try {
			xssfWorkbook.write(os);
			xssfWorkbook.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 生成 供应商发票的 数据部分
	 * 
	 * @param list
	 * @param xssfWorkbook
	 * @param sheet
	 */
	public void generateTeamInvoiceContent(final List<PmsUserInvoice> list, final XSSFWorkbook workbook,
			XSSFSheet sheet) {
		if (ValidateUtil.isValid(list)) {
			for (int i = 0; i < list.size(); i++) {
				PmsUserInvoice ui = list.get(i);
				XSSFRow xssfRow = sheet.createRow(i + 1);

				// 样式
				XSSFCellStyle cs = PoiReportUtils.getLeftCellStyle(workbook);

				// 编号
				xssfRow.createCell(0).setCellStyle(cs);
				xssfRow.createCell(0).setCellValue(ui.getInvoiceCode());

				// 发票类型
				xssfRow.createCell(1).setCellStyle(cs);
				xssfRow.createCell(1).setCellType(XSSFCell.CELL_TYPE_STRING);
				switch (ui.getInvoiceType()) {
				case 1:
					// 增值税专用发票
					xssfRow.createCell(1).setCellValue(InvoiceType.added_value_specially.getDesc());
					break;
				case 2:
					// 增值税普通发票
					xssfRow.createCell(1).setCellValue(InvoiceType.added_value_normal.getDesc());
					break;
				case 3:
					// 通用机打发票
					xssfRow.createCell(1).setCellValue(InvoiceType.general_machine.getDesc());
					break;
				}

				// 发票内容
				xssfRow.createCell(2).setCellStyle(cs);
				xssfRow.createCell(2).setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfRow.createCell(2).setCellValue(ui.getInvoiceContent());

				// 价税合计
				xssfRow.createCell(3).setCellStyle(cs);
				xssfRow.createCell(3).setCellType(XSSFCell.CELL_TYPE_NUMERIC);
				xssfRow.createCell(3).setCellValue(ui.getInvoiceTotal());

				// 税率
				xssfRow.createCell(4).setCellStyle(cs);
				xssfRow.createCell(4).setCellValue((ui.getInvoiceRadio() * 100) + "%");

				// 开票日期
				xssfRow.createCell(5).setCellStyle(cs);
				xssfRow.createCell(5).setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfRow.createCell(5).setCellValue(ui.getInvoiceStampTime());

				// 管家领取日期
				xssfRow.createCell(6).setCellStyle(cs);
				xssfRow.createCell(6).setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfRow.createCell(6).setCellValue(ui.getInvoiceReceiveTime());

				// 客户名称
				xssfRow.createCell(7).setCellStyle(cs);
				xssfRow.createCell(7).setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfRow.createCell(7).setCellValue(ui.getUserName());

				// 项目名称
				xssfRow.createCell(8).setCellStyle(cs);
				xssfRow.createCell(8).setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfRow.createCell(8).setCellValue(ui.getProjectName());

				// 领取人
				xssfRow.createCell(9).setCellStyle(cs);
				xssfRow.createCell(9).setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfRow.createCell(9).setCellValue(ui.getInvoiceEmployeeName());

				// 备注
				xssfRow.createCell(10).setCellStyle(cs);
				xssfRow.createCell(10).setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfRow.createCell(10).setCellValue(ui.getInvoiceNotice());

				// 审批状态
				xssfRow.createCell(11).setCellStyle(cs);
				xssfRow.createCell(11).setCellType(XSSFCell.CELL_TYPE_STRING);
				switch (ui.getInvoiceStatus()) {
				case 0:
					xssfRow.createCell(11).setCellValue(AuditStatus.NOAUDIT.getDesc());
					break;
				case 1:
					xssfRow.createCell(11).setCellValue(AuditStatus.PASS.getDesc());
					break;
				case 2:
					xssfRow.createCell(11).setCellValue(AuditStatus.UNPASS.getDesc());
					break;

				}

				// 原因
				xssfRow.createCell(12).setCellStyle(cs);
				xssfRow.createCell(12).setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfRow.createCell(12).setCellValue(ui.getReason());
			}
		}
	}

}