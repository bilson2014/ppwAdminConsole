<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib"%>

<spring:url value="/resources/css/eventsView.css" var="eventsViewCss" />
<spring:url value="/resources/js/event-view.js" var="eventViewJs" />
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
<link rel="stylesheet" href="${eventsViewCss }">
<script type="text/javascript" src="${jsonJs }"></script>
<script type="text/javascript" src="${datagridDndJs }"></script>
<script src="${eventViewJs }"></script>
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
			data-options="plain:true,iconCls:'icon-edit'">修改</a> <a
			onclick="delFuc();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'icon-remove'">删除</a> <a
			onclick="cancelFuc();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
	</div>

	<!-- edit template show content begin-->
	<div id="dlg" class="easyui-dialog"
		style="width: 400px; height: 380px; padding: 10px 20px" closed="true"
		buttons="#dlg-buttons" title="人员信息">
		<form id="fm" method="post">
			<input type="hidden" name="nodesEventId" id="nodesEventId">
			<div class="fitem">
				<label>事件名称：</label> <input name="nodesEventName"
					id="nodesEventName" class="easyui-textbox" required="true">
			</div>
			<div class="fitem">
				<label>事件描述:</label> <input name="nodesEventDescription"
					id="nodesEventDescription" class="easyui-textbox" required="true">
			</div>
			<div class="fitem">
				<label>事件类路径:</label> <input name="nodesEventClassName"
					id="nodesEventClassName" class="easyui-combobox" required="true">
			</div>
			<div class="fitem">
				<label>事件类型</label> <select name="nodesEventModel"
					id="nodesEventModel" class="easyui-combobox" style="width: 160px;"
					required="true">
					<option value="0">自动</option>
					<option value="1">手动</option>
				</select>
			</div>
			<div class="fitem">
				<label style="display: inline-block;">模板</label> <select
					id="templateId" name="templateId" style="width: 160px"></select>
				<!-- <ul name="templateId" id="templateId"  style="width: 160px;display: inline-block;" required="true">
	                </ul> -->
			</div>
			<div class="fitem">
				<label>数据填充器</label> <select name="dataFiller" id="dataFiller"
					class="easyui-combobox" style="width: 160px;" required="true">
				</select>
			</div>
			<div class="fitem">
				<label>相关属性</label> <select name="dataFields" id="dataFields"
					class="easyui-combobox" style="width: 160px;" required="true"></select>
			</div>
			<div class="fitem">
				<label>涉及人员</label> <select name="relevantPerson"
					id="relevantPerson" class="easyui-combobox" style="width: 160px;"
					required="true"></select>
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton c6"
			iconCls="icon-ok" onclick="save()">保存</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>
</body>
</html>