<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/js/staff-list.js" var="staffListJs" />

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>

<jsp:include page="common.jsp" />
<script src="${staffListJs }"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
			<form id="searchForm">
				<table>
					<tr>
						<th>人员名称:</th>
						<td><input name="staffName" placeholder="请输入人员名称"/></td>
						
						<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a></td>
					</tr>
				</table>
			</form>
		</div>
		
		<div data-options="region:'center',border:true" >
			<table id="gride" data-options="fit:true,border:false"></table>
		</div>

		<div id="toolbar" style="display: none;">
			<r:permission uri="/portal/staff/save">
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
			</r:permission>
			
			<r:permission uri="/portal/staff/update">
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
			</r:permission>
			
			<r:permission uri="/portal/staff/delete">
				<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
			</r:permission>
			
			<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
		</div>
		
		<div id="dlg" class="easyui-dialog" style="width:360px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons" title="人员信息">
	        <form id="fm" method="post" enctype="multipart/form-data">
	        	<input name="staffId" type="hidden">
	            <div class="fitem">
	                <label>姓名:</label>
	                <input name="staffName" class="easyui-textbox" required="true">
	            </div>
	            <div class="fitem">
	                <label>职位:</label>
	                <input name="staffPosition" class="easyui-textbox" required="true" >
	            </div>
	            <div class="fitem">
	                <label>学历:</label>
	                <input name="staffEducation" class="easyui-textbox" required="true" >
	            </div>
	             <div class="fitem">
	                <label>照片文件:</label>
	                <input name="staffImage" type="file"/>
	            </div>
	            <div class="fitem">
	                <label>排序:</label>
	                <input name="od" class="easyui-numberbox" required="true" precision="0" value="0">
	            </div>
	            <div class="fitem">
	                <label>人员简介:</label>
	                <input name="staffDescription" class="easyui-textbox" required="true" multiline="true" style="height: 100px;">
	            </div>
	        </form>
	    </div>
	    <div id="dlg-buttons">
	    
	    	<r:mulparampermission uri2="/portal/staff/save" uri="/portal/staff/update">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
</body>
</html>