<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib"%>
<%-- import CSS --%>
<spring:url value="/resources/lib/normalize/normalize.css"
	var="normalizeCss" />
<spring:url value="/resources/lib/h5bp/h5bp.css" var="h5bpCss" />
<spring:url value="/resources/css/common.css" var="commonCss" />
<spring:url
	value="/resources/lib/jquery.easyui/themes/default/easyui.css"
	var="easyuiCss" />
<spring:url value="/resources/lib/jquery.easyui/themes/icon.css"
	var="iconCss" />
<%-- import JS --%>
<%-- <spring:url value="/resources/lib/html5shiv/html5shiv.js" var="html5shivJs" /> --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js"
	var="jqueryJs" />
<spring:url value="/resources/lib/jquery/plugins.js" var="pluginJs" />
<spring:url value="/resources/lib/jquery.blockui/jquery.blockUI.js"
	var="blockUIJs" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js"
	var="jsonJs" />
<spring:url value="/resources/lib/jquery.easyui/jquery.easyui.min.js"
	var="easyuiJs" />
<spring:url
	value="/resources/lib/jquery.easyui/locale/easyui-lang-zh_CN.js"
	var="zhJs" />
<spring:url value="/resources/js/common.js" var="commonJs" />
<spring:url value="/resources/js/quotationtype-list.js"
	var="quotationtypeJs" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=9,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="${normalizeCss }">
<link rel="stylesheet" href="${h5bpCss }">
<link rel="stylesheet" href="${commonCss }">
<link rel="stylesheet" href="${easyuiCss }">
<link rel="stylesheet" href="${iconCss }">
<!--[if lt IE 9]>
		<script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
	<![endif]-->
<script src="${jqueryJs }"></script>
<script src="${pluginJs }"></script>
<script src="${blockUIJs }"></script>
<script src="${jsonJs }"></script>
<script src="${easyuiJs }"></script>
<script src="${zhJs }"></script>
<script src="${commonJs }"></script>
<script src="${quotationtypeJs }"></script>
</head>
<body>
	<input type="hidden" id="storage_node"
		value="${file_locate_storage_path }" />
	<div id="toolbar" style="display: none;">
		
		<r:permission uri="/portal/quotationtype/save">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
		</r:permission>
		
		<r:permission uri="/portal/quotationtype/update">
			<a onclick="editFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
		</r:permission>
		
		<r:permission uri="/portal/quotationtype/delete">
			<a onclick="delFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
		</r:permission>
	</div>
	
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',border:false"
			style="overflow: hidden;">
			<table id="treeGride"></table>
		</div>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:400px;padding:10px 20px" title="报价单类型信息" closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post" >
			<input name="typeId" type="hidden" >
			<div class="fitem">
				<label>名称</label>
				<input name="typeName" class="easyui-textbox" required="true">
			</div>
			<div class="fitem">
	            <label>级别:</label>
	               <select id="grade" name="grade" class="easyui-combobox" required="true" style="width: 46%;">
	                	<option value="1">类别</option>
	                	<option value="2">子类</option>
	                	<option value="3">项目</option>
	               </select>
	            </div>
			<div class="fitem">
				<label>直属上级</label>
				<select id="parentId" name="parentId"  style="width: 70%;"></select>
			</div>
			<div class="fitem">
				<label>单价</label>
				<input name=unitPrice class="easyui-numberbox" data-options="precision:2">
			</div>
			<div class="fitem">
				<label>成本价</label>
				<input name="costPrice" class="easyui-numberbox" data-options="precision:2">
			</div>
			<div class="fitem">
				<label>描述</label>
				<textarea class="easyui-textbox" id="description" name="description" multiline="true" style="width:256px;height: 160px;"></textarea>
			</div>
		</form>
	</div>
	 <div id="dlg-buttons">
	    	
	    	<r:mulparampermission uri2="/portal/quotationtype/update" uri="/portal/quotationtype/save">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveFun()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
</body>
</html>