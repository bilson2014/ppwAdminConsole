<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/salesman-list.css" var="salesmanListCss"/>
<spring:url value="/resources/js/salesman-list.js" var="salesmanListJs"/>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
	<jsp:include page="common.jsp" />
	<link rel="stylesheet" href="${salesmanListCss }">
	<script src="${salesmanListJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<input type="hidden" id='sourceCombobox' value='${sourceCombobox }'> 
	<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
		<form id="searchForm">
			<table>
				<tr>
					<th>名称:</th>
					<td><input id="search-name" name="salesmanName" class="easyui-textbox" placeholder="分销人名称"/></td>
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
		<r:permission uri="/portal/salesman/save">
			<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
		</r:permission>
		
		<r:permission uri="/portal/salesman/update">
			<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
		</r:permission>
		
		<r:permission uri="/portal/salesman/delete">
			<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
		</r:permission>
		
		<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
	</div>
	
	<!-- qrCode show content begin-->
		<div id="qrCode-condition" class="picture-condition hide">
			<div class="picture-modalDialog">
				<div class="picture-condition-body">
					<div class="operation-panel">
						<img id="qrCode" src="" style="height: 200px;width: 200px;" class="hide" >
						<div class="p-label">
							<a href="#" class="button p-submit" id="p-cancel">取消</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	<!-- qrCode list show content end-->
	
	<div id="dlg" class="easyui-dialog" style="width:350px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons" title="分销信息">
	        <form id="fm" method="post">
	        	<input type="hidden" name="salesmanId" id="salesmanId"> 
	        	<div class="fitem">
	                <label>名称:</label>
	                <input name="salesmanName" class="easyui-textbox" required="true">
	            </div>
	            <div class="fitem">
	                <label>作用范围:</label>
	                <select name="belongs" class="easyui-combobox" editable="true" required="true">
							<option value="网站" selected>网站</option>
							<option value="活动">活动</option>
							<option value="分销">分销</option>
							<option value="视频名片">视频名片</option>
						</select>
	            </div>
	            <div class="fitem">
	                <label>受众平台:</label>
	                <select name="platform" class="easyui-combobox" editable="true" required="true">
						<option value="PC" selected>PC</option>
						<option value="移动端">移动端</option>
						<option value="今日头条">今日头条</option>
						<option value="微信">微信</option>
					</select>
	            </div>
	           
	            <div class="fitem">
	                <label>唯一标识:</label>
	                <input name="uniqueId" id="uniqueId" class="easyui-textbox" required="true">
	            </div>
	             <div class="fitem">
	                <label>网站类型:</label>
	                <select name="type" class="easyui-combobox" editable="true" required="true">
						<option value="0" selected>PC</option>
						<option value="1">移动端</option>
					</select>
	            </div>
	             <div class="fitem">
	                <label>产品地址:</label>
					<input name="accessurl" id="accessurl" class="easyui-textbox" >
	            </div>
	             <div class="fitem">
	                <label>下单地址:</label>
					<input name="orderUrl" id="orderUrl" class="easyui-textbox" >
	            </div>
	            <div class="fitem">
            	 	<label>订单来源:</label>
            	 	<select id="indentSource" name="indentSource" class="easyui-combobox" style="width: 46%;">
                	</select>
           		</div>
	            <div class="fitem">
	                <label>备注:</label>
	                <input name="salesmanDescription" class="easyui-textbox" >
	            </div>
	        </form>
	    </div>
	    <div id="dlg-buttons">
	    	<r:mulparampermission uri2="/portal/salesman/save" uri="/portal/salesman/update">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveFuc()" >保存</a>
	    	</r:mulparampermission>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
</body>
</html>