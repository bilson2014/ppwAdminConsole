<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/flowTemptale.css" var="flowTemptaleCss" />
<spring:url value="/resources/lib/zyUpload/css/zyUpload.css" var="zyUploadCss" />
<spring:url value="/resources/js/product-case-list.js" var="chanpinJs" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js"
	var="jsonJs" />
<spring:url value="/resources/lib/zyUpload/js/zyUpload.js"
	var="zyUploadJs" />
<spring:url value="/resources/lib/zyUpload/js/zyFile.js"
	var="zyFileJs" />
<spring:url value="/resources/lib/jquery.easyui/datagrid-dnd.js" var="datagridDndJs" />
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
<link rel="stylesheet" href="${zyUploadCss }">
<script type="text/javascript" src="${chanpinJs }"></script>
<script type="text/javascript" src="${zyUploadJs }"></script>
<script type="text/javascript" src="${zyFileJs }"></script>
<script type="text/javascript" src="${datagridDndJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
		<input type="hidden" id="storage_node" value="${file_locate_storage_path }" />
		<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
			<form id="searchForm">
				<table>
					<tr>
						<th>案例名称:</th>
						<td><input name="pName" id="searchpName" placeholder="请输入活动名称"/></td>
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
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">新增案例</a>
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改案例</a>
				<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除案例</a>
				<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
		</div>
		
		<div id="dlg" class="easyui-dialog" style="width:380px; height:400px;"
            closed="true" buttons="#dlg-buttons" title="案例信息">
            <form id="fm" method="post">
            	<input type="hidden" name="pId" id="pId">
            	<div class="fitem">
	                <label>案例名称：</label>
	                <input id="pName" name="pName" class="easyui-textbox" required="true">
	            </div>
            	<div class="fitem">
	                <label>案例周期：</label>
	                <input id="pProductionCycle" name="pProductionCycle" class="easyui-textbox" required="true">
	            </div>
            	<div class="fitem">
	                <label>关联产品：</label>
	                 <select id="chanpinId" name="chanpinId" style="width: 150px" class="easyui-combobox" required="true">
	                </select>
	            </div>
            	<div class="fitem">
	                <label>选择配置：</label>
	                 <select id="configurationId" name="configurationId"  class="easyui-combobox" style="width: 130px"></select>
	            </div>
            	<div class="fitem">
	                <label>关联作品：</label>
	                 <select id="productId" name="productId" style="width: 150px" class="easyui-combobox" required="true">
	                </select>
	            </div>
	            <div class="fitem">
	                <label>客户：</label>
	                <select id="userId" name="userId" style="width: 150px" class="easyui-combobox" required="true" ></select>
	            </div>
	            <div class="fitem">
	                <label>案例时长：</label>
	                <input id="mcoms" name="mcoms" class="easyui-textbox" required="true" >
	            </div>
	            <div class="fitem">
	                <label>场景：</label>
	            </div>
	             <div class="sceneTag">
	            	
	            </div>
	            <div>
	            	<label>客户证言：</label><br/>
	                <textarea class="easyui-textbox" id="customerRestimonial" name="customerRestimonial" multiline="true" style="width:256px;height: 50px;"></textarea>
	            </div>
	            <div>
	            	<label>案例描述：</label><br/>
	                <textarea class="easyui-textbox" id="pDescription" name="pDescription" multiline="true" style="width:256px;height: 50px;"></textarea>
	            </div>
				<div id="dlg-buttons">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
				</div>
	        </form>
	    </div>
	</body>
</html>