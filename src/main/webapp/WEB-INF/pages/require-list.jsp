<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/flowTemptale.css" var="flowTemptaleCss" />
<spring:url value="/resources/lib/zyUpload/css/zyUpload.css" var="zyUploadCss" />
<spring:url value="/resources/js/require-list.js" var="chanpinJs" />
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
				<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除需求</a>
				<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
				<a onclick="updatePMDialog();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'">分配产品经理</a>
		</div>
	    
		<div id="pmDlg" class="easyui-dialog" style="width:380px; height:300px;"
            closed="true" buttons="#dlg-buttons" title="产品信息">
            <form id="pmFm" method="post" >
            	<input type="hidden" name="requireIds" id="requireIds">
	            <div class="fitem">
	            	 <label>产品经理:</label>
	            	 <select name="employeeId" id="employeeId" class="easyui-combobox" required="true" style="width: 46%;">
	                 </select>
            	</div>
				<div id="dlg-buttons">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="pmsave()" >保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#pmDlg').dialog('close')" >取消</a>
				</div>
	        </form>
	   </div>
		
</body>
</html>