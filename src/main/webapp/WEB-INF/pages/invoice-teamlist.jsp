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
					<th>发票名称:</th>
					<td>
						<input type="text" name="invoiceName" class="easyui-textbox" placeholder="请输入发票名称"/>
					</td>
					<th>是否最终领票:</th>
					<td>
						<select id="search-type"  name="invoiceDraw" editable="false" class="easyui-combobox" style="width: 76px;">
							<option value=""></option>
							<option value="0" >未领取</option>
            				<option value="1" >已领取</option>
						</select>
					</td>
					<td>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
					</td>
				</tr>
			</table>
			</form>
		</div>
		
		<div data-options="region:'center',border:true" >
			<table id="gride" data-options="fit:true,border:false"></table>
		</div>

		<div id="toolbar" style="display: none;">
			<r:permission uri="/portal/invoice/save">
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
			</r:permission>
			
			<r:permission uri="/portal/invoice/update">
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
			</r:permission>
			
			<r:permission uri="/portal/invoice/delete">
				<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
			</r:permission>
			
			<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
		</div>
		
		<div id="dlg" class="easyui-dialog" style="width:360px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons" title="发票信息">
	        <form id="fm" method="post" enctype="multipart/form-data">
	        	<input name="invoiceId" type="hidden" />
	        	<input name="invoiceType" type="hidden" />
	        	<input name="invoiceUserId" type="hidden" />
	            <div class="fitem">
	                <label>发票编号:</label>
	                <input name="invoiceCode" class="easyui-textbox" required="true" />
	            </div>
	            <div class="fitem">
	                <label>发票名称:</label>
	                <input name="invoiceName" class="easyui-textbox" required="true" />
	            </div>
	            <div class="fitem">
	                <label>发票金额:</label>
	               	<input name="invoicePrice" class="easyui-numberbox" required="true" precision="2" />
	            </div>
	            <div class="fitem">
	                <label>发票税率:</label>
	               	<input name="invoiceRadio" class="easyui-numberbox" required="true" precision="2" />
	            </div>
	            <div class="fitem">
	                <label>交易时间:</label>
	                <input name="payTime" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" required="true" />
	            </div>
	            <div class="fitem">
	                <label>项目名称:</label>
	               	<input id="invoiceProjectId" name="invoiceProjectId" class="easyui-combobox" required="true" />
	            </div>
	            <div class="fitem">
	                <label>供应商名称:</label>
	               	<input id="invoiceProviderId" name="invoiceProviderId" class="easyui-combobox" required="true" />
	            </div>
	            <div class="fitem">
	                <label>视频管家是否领取发票:</label>
	               	<select name="invoiceFlag" class="easyui-combobox" editable="false" required="true" >
						<option value="0" selected>未领</option>
           				<option value="1" >已领</option>
					</select>
	            </div>
	            <div class="fitem">
	                <label>客户是否领取发票:</label>
	                <select name="invoiceDraw" class="easyui-combobox" editable="false" required="true" >
						<option value="0" selected>未领</option>
           				<option value="1" >已领</option>
					</select>
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <input class="easyui-textbox text-area" name="invoiceNotice" multiline="true" style="height: 100px;width: 92%;" prompt="在此填写对发票记录的备注" />
	            </div>
	        </form>
	    </div>
	    <div id="dlg-buttons">
	    
	    	<r:mulparampermission uri2="/portal/invoice/save" uri="/portal/invoice/update">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
</body>
</html>