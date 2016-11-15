<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/service-list.css" var="serviceListCss"/>
<spring:url value="/resources/js/service-list.js" var="serviceListJs"/>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
	<jsp:include page="common.jsp" />
	<link rel="stylesheet" href="${serviceListCss }">
	<script src="${serviceListJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
		<form id="searchForm">
			<table>
				<tr>
					<th>所属项目:</th>
					<%--modify by wanglc 2016-7-4 15:11:31 项目修改为模糊查询 begin--%>
					<%-- <td><input id="search-name" name="productId" placeholder="请输入项目名"/></td>--%>
					<td><input id="search-name" name="productName" placeholder="请输入项目名"/></td>
					<%--modify by wanglc 2016-7-4 15:11:31 项目修改为模糊查询end--%>
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
		<r:permission uri="/portal/service/save">
			<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
		</r:permission>
		
		<r:permission uri="/portal/service/update">
			<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
		</r:permission>
		
		<r:permission uri="/portal/service/delete">
			<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
		</r:permission>
		
		<r:mulparampermission uri2="/portal/service/save" uri="/portal/service/update">
			<a onclick="saveFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">保存</a>
		</r:mulparampermission>
		
		<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
	</div>
	
		<div id="dlg" class="easyui-dialog" style="width:360px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons" title="价格信息">
	        <form id="fm" method="post" enctype="multipart/form-data">
	        	<input name="serviceId" type="hidden">
	            <div class="fitem">
	                <label>服务名称:</label>
	                <input name="serviceName" class="easyui-textbox" required="true">
	            </div>
	            <div class="fitem">
	                <label>影片时长:</label>
	                <input name="mcoms" class="easyui-numberbox" required="true" max="999999999" size="8" maxlength="8">
	            </div>
	            <div class="fitem">
	                <label>价格:</label>
	                <input name="servicePrice"  class="easyui-numberbox" required="true" precision="2" max="999999999" size="8" maxlength="8">
	            </div>
	            <div class="fitem">
	                <label>价格详情:</label>
	                <input name="priceDetail" id='priceDetail' class="easyui-textbox"/>
	            </div>
	            <div class="fitem">
	                <label>折扣:</label>
	                <input name="serviceDiscount"  class="easyui-numberbox" required="true" min='0' max="1" precision="2"/>
	            </div>
	             <div class="fitem">
	                <label>排序:</label>
	                <input name="serviceOd"  class="easyui-numberbox"  min='0' max="999999"/>
	            </div>
	             <div class="fitem">
	                <label>所属项目:</label>
	                <input name="productId" id='productId' class="easyui-textbox" required="true"/>
	            </div>
	            <div class="fitem">
	                <label>描述:</label>
	                <input class="easyui-textbox" name="serviceDescription" multiline="true" style="height: 100px;"/>
	            </div>
	        </form>
	    </div>
	    <div id="dlg-buttons">
	    
	    	<r:mulparampermission uri2="/portal/service/save" uri="/portal/service/update">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveFuc()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
	
	
	
	
	
	
	
	
	
</body>
</html>