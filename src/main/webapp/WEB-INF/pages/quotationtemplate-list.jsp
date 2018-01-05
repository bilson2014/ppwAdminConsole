<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib"%>

<spring:url value="/resources/lib/My97DatePicker/WdatePicker.js"
	var="WdatePickerJs" />
<spring:url value="/resources/css/quotationtemplate-list.css" var="qtemplateListCss" />
<spring:url value="/resources/js/quotationtemplate-list.js"
	var="quotationtemplateListJs" />

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<jsp:include page="common.jsp" />
<link rel="stylesheet" href="${qtemplateListCss }">
<script src="${WdatePickerJs }"></script>
<script src="${quotationtemplateListJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false"
		style="height: 36px; overflow: hidden; background-color: #fff">
		<form id="searchForm" method="post">
			<table>
				<tr>
					<th>模板名称:</th>
					<td><input id="search-templateName" name="templateName"
						placeholder="请输入报价单模板名称" style="width: 136px;" /></td>

					<td><a href="javascript:void(0);" class="easyui-linkbutton"
						data-options="iconCls:'icon-search',plain:true"
						onclick="searchFun();">查询</a> <a href="javascript:void(0);"
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
		<r:permission uri="/portal/quotationtemplate/save">
			<a onclick="addFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-add'">添加</a>
		</r:permission>

		<r:permission uri="/portal/quotationtemplate/update">
			<a onclick="editFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-edit'">修改</a>
		</r:permission>

		<r:permission uri="/portal/quotationtemplate/delete">
			<a onclick="delFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-remove'">删除</a>
		</r:permission>

		<a onclick="cancelFuc();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
	</div>

	<div id="dlg" class="easyui-dialog"
		style="padding: 10px 20px; width: 1000px; height: 650px;" closed="true"
		buttons="#dlg-buttons" title="模板信息">
		
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="padding:10px;height: 50px" >
				<form id="fm" method="post" enctype="multipart/form-data">
					<input type="hidden" name="templateId" id="templateId">

					<table style="width: 98%;">
						<tr>
							<th>产品线</th>
							<td>
							<select name="chanpinconfigId" id="chanpinconfigId" style="width: 200px" class="easyui-combotree" required="true"></select>
							</td>
							<th>模板名称</th>
							<td><input name="templateName" id="templateName"  style="width: 200px" class="easyui-textbox" required="true"></td>
							
						</tr>
					</table>

				</form>
			</div>
			<div data-options="region:'center',border:false">
				<div data-options="region:'north',border:false" style="padding:10px;height: 50px" >
					<form id="fm-1" method="post" enctype="multipart/form-data">

					<table style="width: 98%;">
						<tr>
							<th>收费类</th>
							<td><input name="typeId" id="typeId" class="easyui-combobox"></td>
							<th>收费项</th>
							<td><select name="itemId" id="itemId" style="width: 180px" class="easyui-combotree"></select></td>
							
							<th>天数</th>
							<td><input name="days" id="days" style="width: 60px" class="easyui-numberbox" data-options="min:0,precision:0"></td>
							<th>数量</th>
							<td><input name="quantity" id="quantity" style="width: 60px" class="easyui-numberbox" data-options="min:0,precision:0"></td>
							
							<th class="fullJob">整包</th>
							
							<td>
								<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="add()" >添加</a>
							</td>
							
						</tr>
						<tr>
							<th>单价：</th>
							<td id="unitPrice"></td>
							<th style="width: 100px">收费项描述：</th>
							<td colspan="5" id="description"></td>
						</tr>
					</table>
				</form>
				</div>
				<div data-options="region:'center',border:false" style="height: 400px">
					<table id="item-gride" data-options="fit:true,border:false"></table>
				</div>
				<div data-options="region:'south',border:false" style="height: 40px">
					<form id="fm-2" method="post" enctype="multipart/form-data">
					<table style="width: 98%;">
						<tr>
							<th>税率(%)</th>
							<td><input name="taxRate" id="taxRate" class="easyui-numberbox" required="true" data-options="min:0,precision:2, onChange: function (a, b) {computeTotal(); }"></td>
							<th>优惠(元)</th>
							<td><input name="discount" id="discount" class="easyui-numberbox" data-options="min:0,precision:2, onChange: function (a, b) {computeTotal(); }" ></td>
							<th>含税总价格</th>
							<td><input name="total" id="total" class="easyui-textbox" required="true" editable="false"></td>
						</tr>
					</table>
					</form>
				</div>
				
			</div>
			
		</div>
	</div>
	
	<div id="dlg-buttons">
	    	<r:mulparampermission uri2="/portal/quotationtemplate/save" uri="/portal/quotationtemplate/update">
		        <a id="saveBtn" href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	    	</r:mulparampermission>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	</div>
</body>
</html>