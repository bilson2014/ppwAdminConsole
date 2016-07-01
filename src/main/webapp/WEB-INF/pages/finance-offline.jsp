<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/lib/My97DatePicker/WdatePicker.js" var="WdatePickerJs" />
<spring:url value="/resources/js/finance-offline.js" var="financeOfflineJs" />

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
<script src="${financeOfflineJs }"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
			<form id="searchForm">
				<table>
				<tr>
					<th>入出类型:</th>
					<td>
						<select id="search-type"  name="logType" editable="false" class="easyui-combobox" style="width: 76px;">
							<option value=""></option>
							<option value="0" >入账</option>
            				<option value="1" >出账</option>
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
			<r:permission uri="/portal/finance/save">
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
			</r:permission>
			
			<r:permission uri="/portal/finance/update">
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
			</r:permission>
			
			<r:permission uri="/portal/finance/delete">
				<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
			</r:permission>
			
			<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
		</div>
		
		<div id="dlg" class="easyui-dialog" style="width:360px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons" title="人员信息">
	        <form id="fm" method="post" enctype="multipart/form-data">
	        	<input name="dealId" type="hidden">
	        	<input id="projectName" name="projectName" type="hidden">
	        	<input id="userName" name="userName" type="hidden">
	            <div class="fitem">
	                <label>交易流水号:</label>
	                <input name="unOrderId" class="easyui-textbox" required="true" />
	            </div>
	            <div class="fitem">
	                <label>交易方:</label>
	                <input id="userId" name="userId" class="easyui-combobox" required="true" />
	            </div>
	            <div class="fitem">
	                <label>交易时间:</label>
	                <input name="payTime" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" required="true" />
	            </div>
	            <div class="fitem">
	                <label>交易类型:</label>
	               	<select id="logType" name="logType" class="easyui-combobox" editable="false" required="true" >
						<option value="0" selected>入账</option>
           				<option value="1" >出账</option>
					</select>
	            </div>
	            <div class="fitem">
	                <label>项目名称:</label>
	               	<input id="projectId" name="projectId" class="easyui-combobox" required="true" />
	            </div>
	            <div class="fitem">
	                <label>交易金额:</label>
	               	<input name="payPrice" class="easyui-numberbox" required="true" precision="2" />
	            </div>
	            <div class="fitem">
	                <label>描述:</label>
	                <input class="easyui-textbox text-area" id="description" name="description" multiline="true" style="height: 100px;width: 92%;" prompt="在此填写对交易记录的描述" />
	            </div>
	        </form>
	    </div>
	    <div id="dlg-buttons">
	    
	    	<r:mulparampermission uri2="/portal/finance/save" uri="/portal/finance/update">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
</body>
</html>