<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/role-list.css" var="roleListCss" />
<spring:url value="/resources/js/role-list.js" var="roleListJs" />

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<jsp:include page="common.jsp" />
<link rel="stylesheet" href="${roleListCss }">
<script src="${roleListJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
			<form id="searchForm">
				<table>
					<tr>
						<th>姓名:</th>
						<td><input name="roleName" placeholder="请输入用户姓名"/></td>
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
			<r:permission uri="/portal/role/add">
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
			</r:permission>
			
			<r:permission uri="/portal/role/update">
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
			</r:permission>
			
			<r:permission uri="/portal/role/delete">
				<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
			</r:permission>
			
			<r:mulparampermission uri2="/portal/role/update" uri="/portal/role/add">
				<a onclick="saveFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">保存</a>
			</r:mulparampermission>
			
			<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
		</div>
		
		<div id="dlg" class="easyui-dialog" 
            closed="true" buttons="#dlg-buttons" title="资源信息" style="width: 500px; height: 400px;padding: 1px;">
	        <div class="layout-left" style="position: absolute;width: 70%;height: 80%;left: 20px;top: 40px;overflow: auto;z-index: 2;">
	        	<form id="roleGrantForm" method="post">
					
					<ul id="resourceTree"></ul>
					<input id="resourceIds" name="resourceIds" type="hidden" />
					<input id="role-id" name="roleId" type="hidden">
				</form>
	        </div>
	        
			<div class="layout-right" style="position: absolute;margin: auto;left: 82%;top: 80px;overflow: hidden;z-index: 2">
				<button class="btn btn-success" onclick="checkAll();">全选</button>
				<br /> <br />
				<button class="btn btn-warning" onclick="checkInverse();">反选</button>
				<br /> <br />
				<button class="btn btn-inverse" onclick="uncheckAll();">取消</button>
			</div>
			
			<div id="dlg-buttons">
				<r:permission uri="/portal/role/grant">
			        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="grantRight()" >授权</a>
				</r:permission>
				
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close');" >取消</a>
		    </div>
	  </div>
	    
</body>
</html>