<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/project-list.css" var="projectListCss"/>
<spring:url value="/resources/js/project-list.js" var="projectListJs"/>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
	<jsp:include page="common.jsp" />
	<link rel="stylesheet" href="${projectListCss }">
	<script src="${projectListJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
		<form id="searchForm">
			<table>
				<tr>
					<th>所属项目:</th>
					<td><input id="search-name" name="productId" placeholder="请输入项目名"/></td>
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
		<r:permission uri="/project/save">
			<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
		</r:permission>
		
		<r:permission uri="/project/updateInfo">
			<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
		</r:permission>
		
		<r:permission uri="/project/delete">
			<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
		</r:permission>
		
		<r:mulparampermission uri2="/project/save" uri="/project/updateInfo">
			<a onclick="saveFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">保存</a>
		</r:mulparampermission>
		
		<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="padding:5px 5px;width: 520px;height: 500px;"
            closed="true" buttons="#dlg-buttons" title="项目信息">
	        <form id="fm" method="post">
	        	<input id="projectId" name="id" type="hidden">
	        	
	            <div class="online">
					<div class="lable l-width">项目编号</div>
					<div class="d-float f-width">
						<input id="serial" name="serial" class="easyui-textbox" required="true" />
					</div>
					
					<div class="lable l-width">项目状态</div>
					<div class="d-float f-width">
						<select id="state" name="state" class="easyui-combobox" editable="false">
							<option value="0" selected>正常</option>
            				<option value="1" >取消</option>
            				<option value="2" >完成</option>
						</select>
					</div>
				</div>
				
				<div class="online">
					<div class="lable l-width">项目名称</div>
					<div class="d-float f-width1">
						<input id="projectName" name="projectName" class="easyui-textbox" required="true"/>
					</div>
					<div class="lable-right l-width">项目来源</div>
					<div class="d-float f-width1">
						<select id="source" name="source" class="easyui-combobox" editable="false">
							<option value="网站下单" selected>网站下单</option>
            				<option value="友情推荐" >友情推荐</option>
            				<option value="活动下单" >活动下单</option>
            				<option value="渠道优惠" >渠道优惠</option>
            				<option value="团购下单" >团购下单</option>
            				<option value="媒体推广" >媒体推广</option>
						</select>
					</div>
				</div>
				
				<div class="online">
					<div class="lable l-width">视频管家</div>
					<div class="d-float f-width1">
						<input id="userId" name="userId" class="easyui-combobox" editable="false"  />
					</div>
					
					<div class="lable l-width">客户名称</div>
					<div class="d-float f-width1">
						<input id="customerId" name="customerId" class="easyui-combobox" required="true"/>
					</div>
				</div>
				
				<div class="online">
					
					<div class="lable-right l-width">客户联系人</div>
					<div class="d-float f-width1">
						<input id="userContact" name="userContact" class="easyui-textbox" required="true" />
					</div>
					
					<div class="lable l-width">客户手机</div>
					<div class="d-float f-width1">
						<input id="userPhone" name="userPhone" class="easyui-textbox" required="true"/>
					</div>
				</div>
				
				<div class="online">
					
					<div class="lable-right l-width">供应商名称</div>
					<div class="d-float f-width1">
						<input id="teamId" name="teamId" class="easyui-combobox" editable="false" />
					</div>
					
					<div class="lable l-width">供应商联系人</div>
					<div class="d-float f-width1">
						<input id="teamContact" name="teamContact" class="easyui-textbox" required="true"/>
					</div>
				</div>
				
				<div class="online">
					
					<div class="lable-right l-width">供应商电话</div>
					<div class="d-float f-width1">
						<input id="teamPhone" name="teamPhone" class="easyui-textbox" required="true" />
					</div>
					
					<div class="lable l-width">项目价格</div>
					<div class="d-float f-width1">
						<input id="price" name="price" class="easyui-textbox" required="true"/>
					</div>
				</div>
				
				<div class="textarea-position">
					<div class="lable l-width">项目描述</div>
					<textarea class="easyui-textbox" id="description" name="description" multiline="true" style="height: 50px;"></textarea>
				</div>
	        </form>
	    </div>
	    
	     <div id="dlg-buttons">
	    
	    	<r:mulparampermission uri2="/project/save" uri="/project/updateInfo">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
</body>
</html>