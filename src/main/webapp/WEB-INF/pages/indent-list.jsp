<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/indent-list.css" var="indentListCss"/>
<spring:url value="/resources/js/indent-list.js" var="indentListJs"/>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
	<jsp:include page="common.jsp" />
	<link rel="stylesheet" href="${indentListCss }">
	<script src="${indentListJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
		<form id="searchForm">
			<table>
				<tr>
					<th>订单编号:</th>
					<td><input id="search-indentNum" name="indentId" placeholder="请输入订单编号"/></td>
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
		<r:permission uri="/portal/indent/update">
			<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
		</r:permission>
		
		<r:permission uri="/portal/indent/delete">
			<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
		</r:permission>
		
		<r:permission uri="/portal/indent/update">
			<a onclick="saveFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">保存</a>
		</r:permission>
		
		<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
	</div>
	
</body>
</html>