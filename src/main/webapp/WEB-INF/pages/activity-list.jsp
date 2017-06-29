<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/flowTemptale.css" var="flowTemptaleCss" />
<spring:url value="/resources/js/activity-list.js" var="activityJs" />
<spring:url value="/resources/lib/jquery.easyui/datagrid-dnd.js" var="datagridDndJs" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js"
	var="jsonJs" />
<spring:url value="/resources/lib/jquery/jquery.base64.js" var="jquerybase64Js" />
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
<script src="${activityJs }"></script>
<script src="${jquerybase64Js }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
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
		</div>
		
		<div data-options="region:'center',border:true" >
			<table id="gride" data-options="fit:true,border:false"></table>
		</div>

		<div id="toolbar" style="display: none;">
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加行</a>
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改行</a>
				<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除行</a>
				<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
		</div>
	    
		<div id="dlg" class="easyui-dialog" style="width:380px; height:500px;"
            closed="true" buttons="#dlg-buttons" title="人员信息">
            <form id="fm">
            	<input type="hidden" name="activityId" id="activityId">
            	<div class="fitem">
	                <label>活动名称：</label>
	                <input id="activityName" name="activityName" class="easyui-textbox" required="true">
	            </div>
            	<div class="fitem">
	                <label>类型：</label>
	                <select id="acticityTempleteType" name="acticityTempleteType" class="easyui-combobox" required="true">
	                	<option value="0">短信</option>
	                	<option value="1">邮件</option>
	                </select>
	            </div>
            	<div class="fitem">
	                <label>模板：</label>
	                <input id="acticityTempleteId" name="acticityTempleteId" class="easyui-combobox" required="true">
	            </div>
	            <div>
	            	<label>模板内容：</label><br/>
	                <span id="templateValue"></span>
	            </div>
            	<div class="fitem">
	                <label>参数列表：</label>
	                <a id="addSystemParam" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加系统参数</a>
	                <a id="addCustomParam" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加自定义参数</a><br/>
	                -----------------------------------------------------
	                <div id="paramList">
	                	
	                </div>
	                -----------------------------------------------------
	            </div>
            	<div class="fitem">
	                <label>启动时间：</label>
	                <input id="activityStartTime" name="activityStartTime" class="easyui-datetimebox" required="true">
	            </div>
	            
	            <div class="fitem">
	                <label>相关人员：</label>
	                 <select id="actitityRelevantPersons" name="actitityRelevantPersons" class="easyui-combobox" required="true">
	                	<option value="0">所有供应商</option>
	                	<option value="1">所有有客户</option>
	                	<option value="2">所有工作人员</option>
	                </select>
	            </div>
	            
				<div id="dlg-buttons">
					<r:mulparampermission uri2="/portal/employee/save" uri="/portal/employee/update">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
					</r:mulparampermission>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
				</div>
	        </form>
	    </div>
</body>
</html>