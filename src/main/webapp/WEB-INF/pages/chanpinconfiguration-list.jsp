<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/flowTemptale.css" var="flowTemptaleCss" />
<spring:url value="/resources/js/chanpinconfiguration-list.js" var="chanpinconfigurationJs" />
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
<script type="text/javascript" src="${chanpinconfigurationJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
		<!-- <div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
			<form id="searchForm">
				<table>
					<tr>
						<th>姓名:</th>
						<td><input name="activityName" id="searchFormactivityName" placeholder="请输入活动名称"/></td>
						<td>
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
						</td>
					</tr>
				</table>
			</form>
		</div> -->
		
		<div data-options="region:'center',border:true" >
			<table id="gride" data-options="fit:true,border:false"></table>
		</div>

		<div id="toolbar" style="display: none;">
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">新增配置</a>
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改配置</a>
				<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除配置</a>
				<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
				<a onclick="setPic();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'">设置封面</a>
		</div>
	    
		<div id="dlg" class="easyui-dialog" style="width:580px; height:500px"
            closed="true" buttons="#dlg-buttons" title="配置/价格信息">
            <form id="fm" method="post">
            	<input type="hidden" name="chanpinconfigurationId" id="chanpinconfigurationId">
            	<div class="fitem">
	                <label>配置名称：</label>
	                <input id="chanpinconfigurationName" name="chanpinconfigurationName" class="easyui-textbox" required="true">
	            </div>
	            <div class="fitem">
	                <label>选择产品：</label>
	                <select id="chanpinId" name="chanpinId" style="width: 150px" class="easyui-combobox" required="true">
	                </select>
	            </div>
	            <div class="fitem">
	                <label>标签：</label>
	                <input id="tags" name="tags" class="easyui-textbox" required="true">
	            </div>
	            <div class="fitem">
	                <label>起步价格：</label>
	                <input id="initiatePrice" name="initiatePrice" class="easyui-textbox">
	            </div>
	            <div>
	            	<label>配置描述：</label><br/>
	                <textarea class="easyui-textbox" id="chanpinconfigurationDescription" name="chanpinconfigurationDescription" multiline="true" style="width:256px;height: 50px;"></textarea>
	            </div>
				<div id="dlg-buttons">
					<r:mulparampermission uri2="/portal/config/save" uri="/portal/config/update">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
					</r:mulparampermission>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
				</div>
				<hr/>
				
				<a onclick="addBaseModule()" id="add-Module" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加基础模块</a>
				<div class="basemodule">
				</div>
				<hr/>
				<a onclick="addAdditiveModule()" id="add-Module" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加可选模块</a>
				<div class="additivemodule">
				</div>
				<hr/>
				<a onclick="addDimensionModule()" id="add-Module" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加维度</a>
				<div class="dimensionmodule">
				</div>
				 
	        </form>
	    </div>
	    
	    
	    <!-- 配置封面   --- begin  -->
	    <div id="dlgPicForm" class="easyui-dialog" style="width:380px; height:300px;"
            closed="true" buttons="#dlg-buttons1" title="配置封面">
            <form id="pic" method="post" enctype="multipart/form-data">
            	<input type="hidden" name="chanpinconfigurationId" id="chanpinconfigurationId">
            	<div class="fitem">
	                <label>特性名称：</label>
	                <input id="picFile" name="picFile" type="file">
	            </div>
				 <div id="dlg-buttons1">
					<r:mulparampermission uri2="/portal/config/save" uri="/portal/config/update">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="savePic()" >保存</a>
					</r:mulparampermission>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgPicForm').dialog('close')" >取消</a>
				</div> 
	        </form>
	    </div>
		<!-- 配置封面   --- end  -->
</body>
</html>