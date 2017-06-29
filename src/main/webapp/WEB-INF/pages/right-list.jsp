<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>
<%-- import CSS --%>
<spring:url value="/resources/lib/normalize/normalize.css" var="normalizeCss" />
<spring:url value="/resources/lib/h5bp/h5bp.css" var="h5bpCss" />
<spring:url value="/resources/css/common.css" var="commonCss" />
<spring:url value="/resources/lib/jquery.easyui/themes/default/easyui.css" var="easyuiCss" />
<spring:url value="/resources/lib/jquery.easyui/themes/icon.css" var="iconCss" />
<%-- <spring:url value="/resources/css/role-list.css" var="roleListCss" /> --%>
<%-- import JS --%>
<spring:url value="/resources/lib/html5shiv/html5shiv.js" var="html5shivJs" />
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs" />
<spring:url value="/resources/lib/jquery/plugins.js" var="pluginJs" />
<spring:url value="/resources/lib/jquery/ajaxfileupload.js" var="ajaxfileuploadJs" />
<spring:url value="/resources/lib/jquery.blockui/jquery.blockUI.js" var="blockUIJs" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs" />
<spring:url value="/resources/lib/jquery.easyui/jquery.easyui.min.js" var="easyuiJs" />
<spring:url value="/resources/lib/jquery.easyui/locale/easyui-lang-zh_CN.js" var="zhJs" />
<spring:url value="/resources/js/common.js" var="commonJs" />
<spring:url value="/resources/js/right-list.js" var="rightListJs" />
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
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
<script src="${ajaxfileuploadJs }"></script>
<script src="${blockUIJs }"></script>
<script src="${jsonJs }"></script>
<script src="${easyuiJs }"></script>
<script src="${zhJs }"></script>
<script src="${commonJs }"></script>
<script src="${rightListJs }"></script>
</head>
<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',border:false"  style="overflow: hidden;">
			<table id="treeGrid"></table>
		</div>
	</div>
	
	<div id="toolbar" style="display: none;">
		
		<r:permission uri="/portal/right/save">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
		</r:permission>
		
		<r:permission uri="/portal/right/update">
			<a onclick="editFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
		</r:permission>
		
		<r:permission uri="/portal/right/delete">
			<a onclick="delFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
		</r:permission>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:400px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons" title="资源信息">
	        <form id="fm" method="post" novalidate>
	        	<input name="rightId" type="hidden" >
	            <div class="fitem">
	                <label>资源名:</label>
	                <input name="rightName" class="easyui-textbox" required="true">
	            </div>
	            <div class="fitem">
	                <label>资源路径:</label>
	                <input name="url" class="easyui-textbox" >
	            </div>
	            <div class="fitem">
	                <label>排序:</label>
	                <input name="seq" class="easyui-numberspinner" required="true">
	            </div>
	            <div class="fitem">
	                <label>图标:</label>
	                <select name="icon" class="easyui-combobox" data-options="editable:false,panelHeight:'auto'">
						<option value="icon-company">根节点</option>
						<option value="icon-folder">菜单</option>
						<option value="icon-btn">功能</option>
					</select>
	            </div>
	            <div class="fitem">
	                <label>类型:</label>
	                <select name="resourceType" class="easyui-combobox" data-options="editable:false,panelHeight:'auto'">
						<option value="1">按钮</option>
						<option value="0">菜单</option>
					</select>
	            </div>
	            <div class="fitem">
	                <label>状态:</label>
	              	<select name="status" class="easyui-combobox" data-options="editable:false,panelHeight:'auto'" style="width: 50%;">
						<option value="1" >正常</option>
						<option value="0">停用</option>
					</select>
	            </div>
	            <div class="fitem">
	                <label>是否公共:</label>
	              	<select name="isCommon" class="easyui-combobox" data-options="editable:false,panelHeight:'auto'" style="width: 50%;">
						<option value="false">false</option>
						<option value="true" >true</option>
					</select>
	            </div>
	            <div class="fitem">
	                <label>上级资源:</label>
	              	<select id="pId" name="pId"  style="width: 70%;"></select>
	              	<a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#pId').combotree('clear');" >清空</a>
	            </div>
	        </form>
	    </div>
	    <div id="dlg-buttons">
	    	
	    	<r:mulparampermission uri2="/portal/right/update" uri="/portal/right/save">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveFun()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
</html>