<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib"%>

<spring:url value="/resources/css/indent-list.css" var="indentListCss" />
<spring:url value="/resources/js/indent-list.js" var="indentListJs" />
<spring:url value="/resources/lib/My97DatePicker/WdatePicker.js"
	var="WdatePickerJs" />

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<jsp:include page="common.jsp" />
<link rel="stylesheet" href="${indentListCss }">
<script src="${indentListJs }"></script>
<script src="${WdatePickerJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false"
		style="height: 40px; overflow: hidden; background-color: #fff">
		<form id="searchForm" method="post">
			<table>
				<tr>
					<th>订单编号:</th>
					<td><input id="search-indentNum" name="id"
						placeholder="请输入订单编号" /></td>
					<th>联系电话:</th>
					<td><input id="search-phone" name="indent_tele"
						placeholder="请输入联系电话" /></td>
					<th>上传时间:</th>
					<td><input name="beginTime" style="width: 76px;"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
						readonly="readonly" required="true" />~ <input name="endTime"
						style="width: 76px;"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
						readonly="readonly" required="true" /></td>
					<th>订单来源:</th>
					<td>
						<select name="indentSource" style="width: 110px;">
							<option value="" selected="selected">-- 请选择 --</option>
		               	    <option value="1">线上-网站</option>
						    <option value="2">线上-活动</option>
						    <option value="3">线上-新媒体</option>
						    <option value="4">线下-电销</option>
						    <option value="5">线下-直销</option>
						    <option value="6">线下-活动</option>
						    <option value="7">线下-渠道</option>
						    <option value="8">复购</option>
		                </select>
					</td>
					<th>处理人员:</th>
					<td>
						<select name="employeeId" class="easyui-combobox" id="tIndentSource" style="width: 110px;">
		                </select>
					</td>	
					<td><a href="javascript:void(0);" class="easyui-linkbutton"
						data-options="iconCls:'icon-search',plain:true"
						onclick="searchFun();">查询</a><a href="javascript:void(0);"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-cancel',plain:true"
						onclick="cleanFun();">清空</a></td>
				</tr>
			</table>
		</form>
	</div>

	<div data-options="region:'center',border:true">
		<table id="gride" data-options="fit:true,border:false"></table>
	</div>

	<div id="toolbar" style="display: none;">
		<r:permission uri="/portal/indent/update">
			<a onclick="editFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-edit'">修改</a>
		</r:permission>

		<r:permission uri="/portal/indent/delete">
			<a onclick="delFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-remove'">删除</a>
		</r:permission>

		<r:permission uri="/portal/indent/update">
			<a onclick="saveFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-save'">保存</a>
		</r:permission>

		<r:permission uri="/portal/indent/modifyType">
			<a onclick="changeIndentsTypeFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-save'">修改状态</a>
		</r:permission>
		<r:permission uri="/portal/indent/export">
			<a href="javascript:void(0);" class="easyui-linkbutton"
				data-options="iconCls:'icon-search',plain:true"
				onclick="exportFun();">报表导出</a>
		</r:permission>
		
		<r:permission uri="/portal/indent/updateCustomerService">
			<a href="javascript:void(0);" class="easyui-linkbutton"
				data-options="iconCls:'icon-search',plain:true"
				onclick="customerServiceFun();">分配客服</a>
		</r:permission>
		
		<a onclick="cancelFuc();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
	</div>

	<div id="dlg" class="easyui-dialog"
		style="padding: 5px 5px; width: 320px; height: 130px;" closed="true"
		buttons="#dlg-buttons" title="项目信息">
		<form id="fm" method="post">
			<table id="table_info" style="width: 98%;">
				<tr>
					<th>项目状态</th>
					<td><select id="indentType" name="indentType"
						class="easyui-combobox" editable="false" style="width: 90%;">
							<option value="0" selected>新订单</option>
							<option value="1">处理中</option>
							<option value="2">完成</option>
							<option value="3">停滞</option>
							<option value="4">再次沟通</option>
							<option value="5">真实</option>
							<option value="6">虚假</option>
							<option value="7">提交</option>
					</select></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<r:permission uri="/portal/indent/modifyType">
			<a href="javascript:void(0)" class="easyui-linkbutton c6"
				iconCls="icon-ok" onclick="change()">保存</a>
		</r:permission>

		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>

	<!-------------------------------------------------------------->

	<div id="dlg2" class="easyui-dialog"
		style="padding: 5px 5px; width: 350px; height: 360px;" closed="true"
		buttons="#dlg-buttons2" title="订单信息">
		<form id="fm2" method="post">
			<input type="hidden" name="id">
			<div class="fitem">
                <label>订单名称:</label>
                <input name="indentName" class="easyui-textbox" required="true">
	        </div>
	        <div class="fitem">
                <label>下单时间:</label>
                <input name="orderDate" class="easyui-datebox" required="true">
	        </div>
	        <div class="fitem">
                <label>订单金额:</label>
                <input name="indentPrice" class="easyui-textbox">
	        </div>
	        <div class="fitem">
            	 <label>订单来源:</label>
            	 <select name="indentSource" class="easyui-combobox" required="true" style="width: 46%;">
               	    <option value="1">线上-网站</option>
				    <option value="2">线上-活动</option>
				    <option value="3">线上-新媒体</option>
				    <option value="4">线下-电销</option>
				    <option value="5">线下-直销</option>
				    <option value="6">线下-活动</option>
				    <option value="7">线下-渠道</option>
				    <option value="8">复购</option>
                </select>
            </div>
	        <div class="fitem">
            	 <label>订单状态:</label>
            	 <select name="indentType" class="easyui-combobox" required="true" style="width: 46%;">
                	<option value="0">新订单</option>
					<option value="1">处理中</option>
					<option value="2">完成</option>
					<option value="3">停滞</option>
					<option value="4">再次沟通</option>
					<option value="5">真实</option>
					<option value="6">虚假</option>
					<option value="7">提交</option>
                </select>
            </div>
	        <div class="fitem">
                <label>客户电话:</label>
                <input name="indent_tele" class="easyui-textbox" required="true">
	        </div>
	        <div class="fitem">
                <label>订单备注:</label>
                <input name="indent_recomment" class="easyui-textbox">
	        </div>
	        <div class="fitem">
                <label>CRM备注:</label>
                <input name="cSRecomment" class="easyui-textbox">
	        </div>
	        <div class="fitem">
                <label>分销渠道:</label>
                <input name="salesmanUniqueId" id='salesmanUnique' class="easyui-combobox">
	        </div>
		</form>
	</div>
	<div id="dlg-buttons2">
		<r:permission uri="/portal/indent/update">
			<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveFun()">保存</a>
		</r:permission>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg2').dialog('close')">取消</a>
	</div>
	
	
		<!-- 分配客服相关   --- begin  -->
		 <div id="dlgCustomerService" class="easyui-dialog" style="width:380px; height:150px;"
            closed="true" buttons="#dlg-buttons1" title="分配客服">
            <form id="FmCustomerService" method="post">
            	<input type="hidden" name="employeeIds" id="employeeIds">
	            <div class="fitem">
            	 <label>客服:</label>
            	 <select name="customerService" id="customerService" class="easyui-combobox" required="true" style="width: 46%;">
                </select>
            	</div>
				 <div id="dlg-buttons1">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveCustomerService()" >保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgCustomerService').dialog('close')" >取消</a>
				</div> 
	        </form>
	    </div>
		<!-- 分配客服相关   --- end  -->
</body>
</html>