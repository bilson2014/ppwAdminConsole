<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/lib/My97DatePicker/WdatePicker.js" var="WdatePickerJs" />
<spring:url value="/resources/js/invoice-teamlist.js" var="invoiceTeamListJs" />

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>

<jsp:include page="common.jsp" />
<script src="${WdatePickerJs }"></script>
<script src="${invoiceTeamListJs }"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
			<form id="searchForm">
				<table>
					<tr>
						<th>发票类型:</th>
						<td>
							<select id="search-type"  name="invoiceType" editable="false" class="easyui-combobox" style="width: 120px;">
								<option selected value="">-- 请选择 --</option>
								<option value="1" >增值税专用发票</option>
	           					<option value="2" >增值税普通发票</option>
	           					<option value="3" >通用机打发票</option>
							</select>
						</td>
						<th>发票号:</th>
						<td>
							<input id="search-code" name="invoiceCode" class="easyui-textbox" style="width: 120px;"/>
						</td>
						<th>项目名称:</th>
						<td>
							<select id="search-projectId"  name="invoiceProjectId" editable="true" class="easyui-combobox" style="width: 120px;">
								
							</select>
						</td>
						<td>
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a>
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
							<r:permission uri="/portal/invoice/team/export">
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="exportFun();">报表导出</a>
							</r:permission>
						
						</td>
					</tr>
				</table>
			</form>
		</div>
		
		<div data-options="region:'center',border:true" >
			<table id="gride" data-options="fit:true,border:false"></table>
		</div>

		<div id="toolbar" style="display: none;">
			<r:permission uri="/portal/invoice/team/save">
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
			</r:permission>
			
			<r:permission uri="/portal/invoice/team/update">
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
			</r:permission>
			
			<r:permission uri="/portal/invoice/team/delete">
				<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
			</r:permission>
			
			<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
			<r:permission uri="/portal/invoice/team/auditing">
				<a onclick="auditFuc(this);" href="javascript:void(0);" class="easyui-linkbutton" data-flag="ok" data-options="plain:true,iconCls:'icon-ok'">审批通过</a>
				<a onclick="auditFuc(this);" href="javascript:void(0);" class="easyui-linkbutton" data-flag="no" data-options="plain:true,iconCls:'icon-no'">审批未通过</a>
			</r:permission>
		</div>
		
		<div id="dlg" class="easyui-dialog" style="width:360px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons" title="发票信息">
            
             <form id="fm" method="post" enctype="multipart/form-data">
	        	<input name="invoiceId" type="hidden" />
	        	<input name="invoiceStatus" type="hidden" />
	        	 <div class="fitem">
	                <label>发票类型:</label>
	               	<select name="invoiceType" class="easyui-combobox" editable="false" required="true">
						<option value="1" selected>增值税专用发票</option>
           				<option value="2" >增值税普通发票</option>
           				<option value="3" >通用机打发票</option>
					</select>
	            </div>
	            <div class="fitem">
	                <label>发票编号:</label>
	                <input name="invoiceCode" class="easyui-textbox" required="true" />
	            </div>
	            <div class="fitem">
	                <label>发票内容:</label>
	                <input name="invoiceContent" class="easyui-textbox" required="true" />
	            </div>
	            <div class="fitem">
	                <label>价税合计:</label>
	               	<input name="invoiceTotal" class="easyui-numberbox" required="true" precision="6" />
	            </div>
	            <div class="fitem">
	                <label>发票税率:</label>
	               	<input name="invoiceRadio" class="easyui-numberbox" required="true" precision="2"  max="1.00" />(填写对应小数)
	            </div>
	            <div class="fitem">
	                <label>开票时间:</label>
	                <input name="invoiceStampTime"  onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly" required="true" />
	            </div>
	            <div class="fitem">
	                <label>付款日期:</label>
	                <input name="invoiceTeamTime" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly" required="true" />
	            </div>
	            <div class="fitem">
	                <label>项目名称:</label>
	               	<input id="invoiceProjectId" name="invoiceProjectId" class="easyui-combobox" required="true" />
	            </div>
	            <div class="fitem">
	                <label>供应商名称:</label>
	               	<input id="invoiceTeamId" name="invoiceTeamId" class="easyui-combobox" required="true" />
	            </div>
	            <div class="fitem">
	                <label>提供人:</label>
	               	<input id="invoiceEmployeeId" name="invoiceEmployeeId" class="easyui-combobox" required="true" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <input class="easyui-textbox text-area" name="invoiceNotice" multiline="true" style="height: 100px;width: 92%;" prompt="在此填写对发票记录的备注" />
	            </div>
	        </form>
	    </div>
	    <div id="dlg-buttons">
	    
	    	<r:mulparampermission uri2="/portal/invoice/save" uri="/portal/invoice/update">
		        <a href="javascript:void(0)" id="saveInvoice" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
	    
	    
	  <div id="invoicedlg" class="easyui-dialog" style="width:360px;padding:10px 20px"
		closed="true" buttons="#invoicedlg-buttons" title="未通过原因">
		<form id="invoicedlgfm" method="post" enctype="multipart/form-data">
			<input name="invoiceId" type="hidden" />
			<input name="invoiceStatus" id="invoicefm_invoiceStatus" type="hidden" value="2"/>
			<div class="fitem">
				<label>请填写未通过原因:</label>
				<input class="easyui-textbox text-area" name="reason"  required="true" multiline="true" style="height: 100px;width: 92%;" />
			</div>
		</form>
		</div>
		<div id="invoicedlg-buttons">
	    	<r:permission uri="/portal/invoice/team/auditing">
		        <a href="javascript:void(0)" id="saveInvoice" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveInvoiceReason()" >保存</a>
	    	</r:permission>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#invoicedlg').dialog('close')" >取消</a>
	    </div>
</body>
</html>