<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib"%>

<spring:url value="/resources/css/flowTemptale.css"
	var="flowTemptaleCss" />
<spring:url value="/resources/js/flow-template.js" var="flowTemplateJs" />
<spring:url value="/resources/lib/jquery.easyui/datagrid-dnd.js"
	var="datagridDndJs" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js"
	var="jsonJs" />
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<jsp:include page="common.jsp" />
<link rel="stylesheet" href="${flowTemptaleCss }">
<script type="text/javascript" src="${jsonJs }"></script>
<script type="text/javascript" src="${datagridDndJs }"></script>
<script src="${flowTemplateJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false"
		style="height: 40px; overflow: hidden; background-color: #fff">
		<form id="searchForm">
			<table>
				<tr>
					<th>姓名:</th>
					<td><input name="roleName" placeholder="请输入模板名称" /></td>
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
		<a onclick="addFuc();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'icon-add'">添加</a> <a
			onclick="editFuc();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'icon-edit'">修改</a>
	</div>


	<!-- image show content begin-->
	<div id="picture-condition" class="picture-condition hide">
		<div class="picture-modalDialog">
			<div class="picture-condition-body">
				<div class="operation-panel">
					<img id="productPicture" src="" style="width: 640px; height: 60px;"
						class="hide">
					<div class="p-label">
						<a href="#" class="button p-submit" id="p-cancel">取消</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- edit template show content begin-->
	<div id="dlg" class="easyui-dialog"
		style="width: 780px; height: 480px; padding: 10px 20px" closed="true"
		buttons="#dlg-buttons" title="人员信息">
		<form id="templateform"
			style="width: 700px; height: 400px; padding: 10px 20px">
			<div id="toolbar2">
				<a onclick="addFuc2();" href="javascript:void(0);"
					class="easyui-linkbutton"
					data-options="plain:true,iconCls:'icon-add'">添加行</a> <a
					onclick="editFuc2();" href="javascript:void(0);"
					class="easyui-linkbutton"
					data-options="plain:true,iconCls:'icon-edit'">修改行</a> <a
					onclick="delFuc2();" href="javascript:void(0);"
					class="easyui-linkbutton"
					data-options="plain:true,iconCls:'icon-remove'">删除行</a> <a
					onclick="saveFuc2();" href="javascript:void(0);"
					class="easyui-linkbutton"
					data-options="plain:true,iconCls:'icon-save'">保存行</a> <a
					onclick="cancelFuc2();" href="javascript:void(0);"
					class="easyui-linkbutton"
					data-options="plain:true,iconCls:'icon-cancel'">取消操作</a> | <a
					onclick="checkData();" href="javascript:void(0);"
					class="easyui-linkbutton"
					data-options="plain:true,iconCls:'icon-save'">保存模板</a> <a
					onclick="exitFuc2();" href="javascript:void(0);"
					class="easyui-linkbutton"
					data-options="plain:true,iconCls:'icon-undo'">退出编辑</a>
			</div>
			模板ID： <input type="text" id="templateId" class="easyui-textbox">
			模板名： <input type="text" id="templateName" class="easyui-textbox">
			<input type="hidden" id="d_id">
			<table id="editTable" data-options="fit:true,border:false"></table>
		</form>
	</div>
</body>
</html>