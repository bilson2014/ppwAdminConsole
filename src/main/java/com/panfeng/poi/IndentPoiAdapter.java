package com.panfeng.poi;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.paipianwang.pat.common.web.poi.util.PoiReportUtils;

public class IndentPoiAdapter extends PoiBaseAdapter<Map<String, Object>> {

	private List<Map<String, Object>> pmsIndents=null; 
	
	@Override
	public int createHead(XSSFSheet sheet, XSSFWorkbook workbook) {
		String displayColNames = "订单名称,订单编号,下单时间,订单金额,订单状态,客户电话,订单备注,CRM备注,分销渠道,订单来源"
				+ ",处理人员,客户联系人,客户公司,推荐人,视频分钟数,项目经理,微信号,职位";
		XSSFRow xssfRow = sheet.createRow(0);
		xssfRow.setHeightInPoints(20);
		
		XSSFCellStyle titleStyle=PoiReportUtils.getCenterCellStyle(workbook);
		String[] titleNames=displayColNames.split(",");
		for(int i=0;i<titleNames.length;i++){
			String title=titleNames[i];
			XSSFCell xssfCell = xssfRow.createCell(i);
			xssfCell.setCellValue(title);
			xssfCell.setCellStyle(titleStyle);
			
			// 设置行宽
			sheet.setColumnWidth(i, 14 * 256);
		}
		
		//head冻结
		sheet.createFreezePane(0, 1, 0, 1);
		
		return 1;
	}

	@Override
	public void getItemView(XSSFSheet sheet, XSSFWorkbook workbook, Map<String, Object> indent, int itemId) {
		// 样式
		XSSFCellStyle left = PoiReportUtils.getLeftCellStyle(workbook);
		
		XSSFRow xssfRow=sheet.createRow(itemId);
		
		String matchColNames = "indentName,id,orderDate,indentPrice,indentTypeName,indent_tele,indent_recomment,cSRecomment,salesmanUniqueId,indentSourceName"
				+ ",employeeRealName,realName,userCompany,referrerRealName,second,pMRealName,wechat,position";
		
		String[] colNames=matchColNames.split(",");
		
		for(int i=0;i<colNames.length;i++){
			XSSFCell xssfCell=xssfRow.createCell(i);
			xssfCell.setCellStyle(left);
			xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if(indent.get(colNames[i])==null ){
				xssfCell.setCellValue("");
			}else{
				xssfCell.setCellValue(indent.get(colNames[i])+"");
			}
			
		}
	}

	@Override
	public List<Map<String, Object>> getData() {
		return pmsIndents;
	}


	@Override
	public void setData(List<Map<String, Object>> list) {
		this.pmsIndents=list;
	}
}
