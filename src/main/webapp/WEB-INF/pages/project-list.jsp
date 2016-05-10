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
					<th>项目名称:</th>
					<td>
						<input id="search-projectId" name="projectId" class="easyui-combobox"  placeholder="请输入项目名称"/>
					</td>
					<th>项目状态:</th>
					<td>
						<select id="search-state" name="state" editable="false" class="easyui-combobox" style="width: 70px;">
							<option value="" selected>--  请选择 --</option>
							<option value="0" >正常</option>
            				<option value="1" >取消</option>
            				<option value="2" >完成</option>
						</select>
					</td>
					<th>视频管家</th>
					<td>
						<input id="search-userId" name="userId" class="easyui-combobox" placeholder="请输入视频管家名称"/>
					</td>
					<th>供应商</th>
					<td>
						<input id="search-teamId" name="teamId" class="easyui-combobox" placeholder="请输入供应商名称"/>
					</td>
					<th>项目来源</th>
					<td>
						<input id="search-source" name="source" class="easyui-combobox" placeholder="请选择项目来源" editable="false"/>
					</td>
					<td>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
						<r:permission uri="/project/export">
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="exportFun();">报表导出</a>
						</r:permission>
					</td>
				</tr>
			</table>
		</form>
	</div>
	
	<div data-options="region:'center',border:true" >
		<table id="gride" data-options="fit:true,border:false"></table>
	</div>

	<div id="toolbar" style="display: none;">
		<r:permission uri="/project/saveInfo">
			<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
		</r:permission>
		
		<r:permission uri="/project/updateInfo">
			<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
		</r:permission>
		
		<r:permission uri="/project/delete">
			<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">取消流程</a>
		</r:permission>
		
		<r:mulparampermission uri2="/project/saveInfo" uri="/project/updateInfo">
			<a onclick="saveFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">保存</a>
		</r:mulparampermission>
		
		<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="padding:5px 5px;width: 520px;height: 430px;"
            closed="true" buttons="#dlg-buttons" title="项目信息">
	        <form id="fm" method="post">
	        	<input id="projectId" name="id" type="hidden">
	        	<input id="userType" name="userType" type="hidden">
	        	
	        	<table style="width: 98%;">
	        		<tr>
	        			<th>项目编号</th>
	        			<td>
	        				<input id="serial" name="serial" class="easyui-textbox" required="true" editable="false" readonly="readonly"/>
	        			</td>
	        			
	        			<th>项目状态</th>
	        			<td>
	        				<select id="state" name="state" class="easyui-combobox" editable="false" style="width: 90%;">
								<option value="0" selected>正常</option>
	            				<option value="1" >取消</option>
	            				<option value="2" >完成</option>
							</select>
	        			</td>
	        		</tr>
	        		
	        		<tr>
	        			<th>项目名称</th>
	        			<td>
	        				<input id="projectName" name="projectName" class="easyui-textbox" required="true"/>
	        			</td>
	        			
	        			<th>项目来源</th>
	        			<td>
							<input id="source" name="source" class="easyui-combobox" editable="false" />
	        			</td>
	        		</tr>
	        		
	        		<tr>
	        			<th>视频管家</th>
	        			<td>
	        				<input id="userId" name="userId" class="easyui-combobox" editable="false"  />
	        			</td>
	        			
	        			<th>客户名称</th>
	        			<td>
	        				<input id="customerId" name="customerId" class="easyui-combobox" required="true"/>
	        			</td>
	        		</tr>
	        		
	        		<tr>
	        			<th>客户联系人</th>
	        			<td>
	        				<input id="userContact" name="userContact" class="easyui-textbox" required="true" />
	        			</td>
	        			
	        			<th>客户手机</th>
	        			<td>
	        				<input id="userPhone" name="userPhone" class="easyui-textbox" required="true"/>
	        			</td>
	        		</tr>
	        		
	        		<tr>
	        			<th>供应商名称</th>
	        			<td>
	        				<input id="teamId" name="teamId" class="easyui-combobox" editable="false" />
	        			</td>
	        			
	        			<th>供应商联系人</th>
	        			<td>
	        				<input id="teamContact" name="teamContact" class="easyui-textbox" required="true"/>
	        			</td>
	        		</tr>
	        		
	        		<tr>
	        			<th>供应商电话</th>
	        			<td>
	        				<input id="teamPhone" name="teamPhone" class="easyui-textbox" required="true" />
	        			</td>
	        			<th>最终价格(元)</th>
	        			<td>
	        				<input id="priceFinish" name="priceFinish" class="easyui-numberbox" precision="0"/>
	        			</td>
	        		</tr>
	        		
	        		<tr>
	        			<th>预期最小值(元)</th>
	        			<td>
	        				<input id="priceFirst" name="priceFirst" class="easyui-numberbox" required="true" precision="0"/>
	        			</td>
	        			
	        			<th>预期最大值(元)</th>
	        			<td>
	        				<input id="priceLast" name="priceLast" class="easyui-numberbox" required="true" precision="0"/>
	        			</td>
	        		</tr>
	        		
	        		<tr>
	        			<th>项目描述</th>
	        			<td colspan="3">
	        				<input class="easyui-textbox text-area" id="description" name="description" multiline="true" style="height: 100px;width: 92%;" prompt="在此填写对项目的描述" />
	        			</td>
	        		</tr>
	        		
	        		<tr id="referrer-tr" class="hide">
						<th>推荐人</th>
						<td colspan="2">
							<input id="referrerId" name="referrerId" class="easyui-combobox" style="width: 180px;"/>
						</td>
					</tr>
	        	</table>
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