<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/nodesView.css" var="nodesViewCss" />
<spring:url value="/resources/js/nodes-view.js" var="nodesViewJs" />
<spring:url value="/resources/js/common.js" var="commonJs" />
<spring:url value="/resources/lib/jquery.easyui/datagrid-dnd.js" var="datagridDndJs" />
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
<link rel="stylesheet" href="${nodesViewCss }">
<script type="text/javascript" src="${jsonJs }"></script>
<script type="text/javascript" src="${datagridDndJs }"></script>
<script src="${nodesViewJs }"></script>
<script src="${commonJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
			<form id="searchForm">
				<table>
					<tr>
						<th>姓名:</th>
						<td><input name="roleName" placeholder="请输入模板名称"/></td>
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
			<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
			<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
			<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
		</div>
		
		<!-- edit template show content begin-->
		<div id="dlg" class="easyui-dialog" style="width:400px; height:280px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons" title="人员信息">
            <form id="fm" method="post">
            	<input type="hidden" name="taskChainId" id="taskChainId" >
	            <div class="fitem">
	                <label>节点名称：</label>
	                <input name="name" id="name" class="easyui-textbox" required="true">
	            </div>
	            <div class="fitem">
	                <label>节点描述:</label>
	                <input name="description" id = "description" class="easyui-textbox" required="true">
	            </div>
	            <div class="fitem">
	                <label>节点事件链:</label>
	                <select name="nodesEvents" id="nodesEvents" style="width: 160px;">
	                </select>
	            </div>
	        </form>
	    </div>
	     <div id="dlg-buttons">
		    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
</body>
</html>