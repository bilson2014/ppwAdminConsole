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
	<div data-options="region:'north',border:false" style="height: 66px; overflow: hidden;background-color: #fff">
		<form id="searchForm" method="post">
			<input type="hidden" name="payment" value="malfunction"/>
			<table>
				<tr>
					<th>项目名称:</th>
					<td>
					<%--modify by wanglc 2016-7-4 16:02:04 项目名称改为模糊查询 begin --%>
					<%--<input id="search-projectId" name="projectId" class="easyui-combobox"  placeholder="请输入项目名称" style="width: 136px;" /> --%>
					<input id="search-projectId" name="projectName" class="easyui-combobox"  placeholder="请输入项目名称" style="width: 136px;" />
					<%--modify by wanglc 2016-7-4 16:02:04 项目名称改为模糊查询 end --%> 
					</td>
					<th>项目状态:</th>
					<td>
					<select id="search-state" name="state" editable="false" class="easyui-combobox" style="width: 100px;">
							<option value="" selected></option>
							<option value="0" >正常</option>
            				<option value="1" >取消</option>
            				<option value="3" >暂停</option>
            				<option value="2" >完成</option>
						</select>
					</td>
					<th>视频管家</th>
					<td>
					<input id="search-userId" name="userId"  class="easyui-combobox" placeholder="请输入视频管家名称" style="width: 100px;"/>
					</td>
					<!-- modify by wanglc 2016-6-28 17:46:35 增加协同人搜索的维度 begin-->
					<%-- <th>供应商</th>
					<td>
					<input name="teamId" id="search-teamId" class="easyui-combobox" placeholder="请输入供应商名称" style="width: 136px;"/>
					</td>
					<th>项目来源</th>
					<td>
					<input name="source" id="search-source"  class="easyui-combobox" placeholder="请选择项目来源" editable="false" style="width: 100px;"/>
					</td> 
					 <td>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
						<r:permission uri="/project/export">
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="exportFun();">报表导出</a>
						</r:permission>
					</td>  --%>
					<!-- modify by wanglc 2016-6-28 17:46:35 增加协同人搜索的维度  end-->
				</tr>
				<!-- add by wanglc 2016-6-28 17:48:32 增加协同人搜索的维度 begin-->
				<tr>
					<th>供应商</th>
					<td>
					<%--modify by wanglc 2016-7-4 16:04:26供应商修改为模糊查询 begin --%>
					<%--<input name="teamId" id="search-teamId" class="easyui-combobox" placeholder="请输入供应商名称" style="width: 136px;"/>--%>
					<input name="teamName" id="search-teamId" class="easyui-combobox" placeholder="请输入供应商名称" style="width: 136px;"/>
					<%--modify by wanglc 2016-7-4 16:04:26供应商修改为模糊查询 begin --%>
					</td>
					<th>项目来源</th>
					<td>
					<input name="source" id="search-source"  class="easyui-combobox" placeholder="请选择项目来源" editable="false" style="width: 100px;"/>
					</td>
					<th>是否作为协同人</th>
					<td>
						<select disabled="disabled" id="isSynergy" name="isSynergy" editable="false" class="easyui-combobox" style="width: 100px;">
							<option value="0" >否</option>
            				<option value="1" >是</option>
						</select>
					</td>
					<td>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
						<r:permission uri="/project/export">
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="exportFun();">报表导出</a>
						</r:permission>
					</td>
				</tr>
				<!-- add by wanglc 2016-6-28 17:48:32 增加协同人搜索的维度end-->
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
		
		<a onclick="loadResourceFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">文件列表</a>
		
		<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="padding:5px 5px;width: 520px;height: 430px;"
            closed="true" buttons="#dlg-buttons" title="项目信息">
	        <form id="fm" method="post">
	        	<input id="projectId" name="id" type="hidden">
	        	<input id="userType" name="userType" type="hidden">
	        	
	        	<table id="table_info" style="width: 98%;">
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
	            				<option value="3" >暂停</option>
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
	        			<th>客户实付金额</th>
	        			<td>
	        				<input id="customerPayment" name="customerPayment" class="easyui-numberbox" required="false" precision="2"/>
	        			</td>
	        			
	        			<th>供应商实付金额</th>
	        			<td>
	        				<input id="providerPayment" name="providerPayment" class="easyui-numberbox" required="false" precision="2"/>
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
					<%--add by wanglc,2016-06-23 23:00 添加协同人 begin--%>
	        		<tr id="synergy-content">
	        			<th>协同人
	        				<a onclick="addSynergy()" id="add-synergy" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'"></a>
	        			</th>
	        			<td colspan="3" id="synergy-errorInfo">
	        				<span class="ratio-error hide" style="color:red"></span>
	        			</td>
	        		</tr>
	        		<!-- <tr>
	        			<th></th>
	        			<td colspan="3">
	        				<input name="user_name" class="easyui-combobox" editable="false" required="true"/>
	        				<input name="ratio" class="easyui-textbox" required="true" style="width: 30%;"/>%
	        				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'"></a>
	        			</td>
	        		</tr> -->
	        		<%--add by wanglc,2016-06-23 23:00 end--%>
	        	</table>
	        </form>
	    </div>
	    
	     <div id="dlg-buttons">
	    
	    	<r:mulparampermission uri2="/project/save" uri="/project/updateInfo">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
	    
	    <!-- file list show content begin-->
		<div id="picture-condition" class="picture-condition hide">
			<div class="picture-modalDialog">
				<div class="picture-condition-body">
					<div class="operation-panel" >
						<table class="resource-table" id="resource-table">
							<tr>
								<th class="th-name">文件名称</th>
								<th class="th-type">阶段</th>
								<th class="th-time">上传时间</th>
								<th class="th-operation">操作</th>
							</tr>
						</table>
					</div>
					<div class="p-label">
						<a href="javascript:void(0);" class="button p-submit" id="p-cancel">取消</a>
					</div>
				</div>
			</div>
		</div>
		<!-- file list show content end-->
</body>
</html>