<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/employee-list.css" var="employeeListCss" />
<spring:url value="/resources/lib/cripto/aes.js" var="aesJs"/>
<spring:url value="/resources/lib/cripto/pad-zeropadding.js" var="padJs"/>
<spring:url value="/resources/js/employee-list.js" var="employeeListJs" />

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>

<jsp:include page="common.jsp" />
<link rel="stylesheet" href="${employeeListCss }">
<script src="${aesJs }"></script>
<script src="${padJs }"></script>
<script src="${employeeListJs }"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
			<form id="searchForm">
				<table>
					<tr>
						<th>姓名:</th>
						<td><input name="employeeRealName" placeholder="请输入用户姓名"/></td>
						
						<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a></td>
					</tr>
				</table>
			</form>
		</div>
		
		<div data-options="region:'center',border:true" >
			<table id="gride" data-options="fit:true,border:false"></table>
		</div>

		<div id="toolbar" style="display: none;">
			<r:permission uri="/portal/employee/save">
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
			</r:permission>
			
			<r:permission uri="/portal/employee/update">
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
			</r:permission>
			
			<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
		</div>
		
		<div id="dlg" class="easyui-dialog" style="width:360px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons" title="人员信息">
	        <form id="fm" method="post" enctype="multipart/form-data">
	        	<input name="employeeId" type="hidden">
	            <div class="fitem">
	                <label>登录名:</label>
	                <input name="employeeLoginName" class="easyui-textbox" required="true">
	            </div>
	            <div class="fitem">
	                <label>姓名:</label>
	                <input name="employeeRealName" class="easyui-textbox" required="true">
	            </div>
	            <div class="fitem">
	                <label>密码:</label>
	                <input name="employeePassword" type="password" class="easyui-textbox" prompt="Password" iconCls="icon-lock">
	            </div>
	            <div class="fitem">
	                <label>手机号码:</label>
	                <input name="phoneNumber" id="phoneNumber" class="easyui-validatebox textbox" validtype="mobile" required="true" />
	            </div>
	            <div class="fitem">
	                <label>邮箱:</label>
	                <input name="email" id="email" class="easyui-validatebox textbox" validType="email" required="true" />
	            </div>
	            <div class="fitem">
	                <label>在职状态:</label>
	                <select name="dimissionStatus" class="easyui-combobox" id="dimissionStatus" style="width: 160px;" editable="false" required="true" >
	               		<option value="0">在职</option>
	                	<option value="1">离职</option>
	                	<option value="2">测试</option>
	                </select>
	            </div>
	            <div class="fitem">
	                <label>角色:</label>
	                <select name="roleIds" id="roleIds" style="width: 160px;">
	                </select>
	            </div>
	            <div class="fitem">
	                <label>头像:</label>
	                <input name="employeeImage"  type="file" style="width: 160px;"/>
	            </div>
	            <div class="fitem">
	                <label>描述:</label>
	                <input class="easyui-textbox" name="employeeDescription" multiline="true" style="height: 100px;"/>
	            </div>
	        </form>
	    </div>
	    <div id="dlg-buttons">
	    
	    	<r:mulparampermission uri2="/portal/employee/save" uri="/portal/employee/update">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
</body>
</html>