<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib"%>

<spring:url value="/resources/css/item-list.css" var="itemListCss" />
<spring:url value="/resources/js/item-list.js" var="itemListJs" />

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<jsp:include page="common.jsp" />
<link rel="stylesheet" href="${itemListCss }">
<script src="${itemListJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false"
		style="height: 40px; overflow: hidden; background-color: #fff">
		<form id="searchForm">
			<table>
				<tr>
					<th>项目类型:</th>
					<td><input id="search-itemName" name="itemId"
						placeholder="请输入项目类型" /></td>
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


	<div id="dlg" class="easyui-dialog"
		style="width: 400px; padding: 10px 20px" closed="true"
		buttons="#dlg-buttons" title="资源信息">
		<form id="fm" method="post">
			<input type="hidden" name="itemId" id="itemId">
			<div class="fitem">
				<label>分类名称:</label> <input name="itemName" class="easyui-textbox"
					required="true">
			</div>
			<div class="fitem">
				<label>排序:</label> <input name="od" class="easyui-textbox"
					required="true">
			</div>
			<div class="fitem">
				<label>活动类型:</label> <select name="isActive" class="easyui-combobox"
					required="true" style="width: 46%;">
					<option value="0">否</option>
					<option value="1">是</option>
				</select>
			</div>
			<div class="fitem">
				<label>描述:</label>
				<textarea class="easyui-textbox" id="itemDescription"
					name="itemDescription" multiline="true"
					style="width: 156px; height: 50px;"></textarea>
			</div>
		</form>
	</div>
	<div id="dlg-buttons">

		<r:mulparampermission uri2="/portal/item/save"
			uri="/portal/item/update">
			<a href="javascript:void(0)" class="easyui-linkbutton c6"
				iconCls="icon-ok" onclick="saveFuc()">保存</a>
		</r:mulparampermission>

		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>


	<div id="toolbar" style="display: none;">
		<r:permission uri="/portal/item/save">
			<a onclick="addFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-add'">添加</a>
		</r:permission>

		<r:permission uri="/portal/item/update">
			<a onclick="editFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-edit'">修改</a>
		</r:permission>

		<r:permission uri="/portal/item/delete">
			<a onclick="delFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-remove'">删除</a>
		</r:permission>

		<a onclick="cancelFuc();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
	</div>

</body>
</html>