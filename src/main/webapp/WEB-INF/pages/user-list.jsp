<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/user-list.css" var="userListCss"/>
<spring:url value="/resources/lib/My97DatePicker/WdatePicker.js" var="WdatePickerJs" />
<spring:url value="/resources/js/user-list.js" var="userListJs"/>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
	<jsp:include page="common.jsp" />
	<link rel="stylesheet" href="${userListCss }">
	<script src="${WdatePickerJs }"></script>
	<script src="${userListJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
		<form id="searchForm">
			<table>
				<tr>
					<th>用户名称:</th>
					<td><input name="userName" placeholder="请输入用户名称"/></td>
					<th>客户分级:</th>
					<td>
						<select name="clientLevel" id="clientLevel" class="easyui-combobox" editable="false">
							<option value="-1" selected>-- 请选择 --</option>
							<option value="">未分级</option>
							<option value="3">S</option>
							<option value="0">A</option>
							<option value="1">B</option>
							<option value="2">C</option>
						</select>
					</td>
					<th>跟进时间:</th>
					<td>
						<input name="beginTime" style="width: 76px;" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly" required="true" />~
					</td>
					<td>
						<input name="endTime" style="width: 76px;" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly" required="true" />
					</td>
					<th>联系电话:</th>
					<td>
						<input name="telephone"   placeholder="请输入联系电话"/>
					</td>
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
		<r:permission uri="/portal/user/save">
			<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
		</r:permission>
		
		<r:permission uri="/portal/user/update">
			<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
		</r:permission>
		
		<r:permission uri="/portal/user/delete">
			<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
		</r:permission>
		
		<r:mulparampermission uri2="/portal/user/save" uri="/portal/user/update">
			<a onclick="saveFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">保存</a>
		</r:mulparampermission>
		
		<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
	</div>
	
	
	
		<div id="dlg" class="easyui-dialog" style="padding:5px 5px;width: 520px;height: 430px;"
            closed="true" buttons="#dlg-buttons" title="项目信息">
	        <form id="fm" method="post">
	        	<input type="hidden" name='id' id="userId">
	        	<table id="table_info" style="width: 98%;">
	        		<tr>
	        			<th>昵称</th>
	        			<td>
	        				<input id="userName" name="userName" data-options="validType:'vuNickName'" class="easyui-textbox" required="true"/>
	        			</td>
	        			<th>真实姓名</th>
	        			<td>
	        				<input id="realName" name="realName" class="easyui-textbox" required="true"/>
	        			</td>
	        		</tr>
	        		<tr>
	        			
	        			
	        			<th>客户级别</th>
	        			<td>
							<select style="width:155px" name="clientLevel"  class="easyui-combobox" required="true" editable="false">
								<option value="-1">未分级</option>
								<option value="0" selected>A</option>
								<option value="1">B</option>
								<option value="2">C</option>
								<option value="3">S</option>
							</select>
	        			</td>
	        			<th>客户意向度</th>
	        			<td>
							<select style="width:155px" name="preference" id='preference' class="easyui-combobox"  editable="false">
								<option value="0" selected>A</option>
								<option value="1">B</option>
								<option value="2">C</option>
								<option value="3">D</option>
							</select>
	        			</td>
	        		</tr>
	        		
	        		<tr>
	        			<th>客户公司</th>
	        			<td>
	        				<input id="userCompany" name="userCompany" class="easyui-textbox"/>
	        			</td>
	        			<th>性别</th>
	        			<td>
	        				<select style="width:155px" name="sex" id='sex' class="easyui-combobox" required="required" editable="false">
								<option value="0" selected>男</option>
								<option value="1">女</option>
								<option value="2">保密</option>
							</select>
	        			</td>
	        		</tr>
	        		
	        		<tr>
	        			<th>联系电话</th>
	        			<td>
	        				<input id="telephone" name="telephone" class="easyui-textbox" data-options="validType:['mobile','vuPhoneNumber']"  required="required" />
	        			</td>
	        			<th>电子邮件</th>
	        			<td>
	        				<input id="email" name="email" class="easyui-textbox" data-options="validType:'email'"/>
	        			</td>
	        		</tr>
	        		
	        		<tr>
	        			<th>QQ</th>
	        			<td>
	        				<input id="qq" name="qq" class="easyui-textbox" data-options="validType:'length[6,20]'"/>
	        			</td>
	        			<th>微信</th>
	        			<td>
	        				<input id="weChat" name="weChat" class="easyui-textbox" data-options="validType:'length[6,20]'"/>
	        			</td>
	        		</tr>
	        		<tr>
	        			<th>客户来源</th>
	        			<td>
	        				<select style="width:155px" name="customerSource" id='customerSource' class="easyui-combobox" required="required" editable="false">
								<option value="1">渠道</option>
								<option value="2">推广</option>
								<option value="3">自主开发</option>
								<option value="4">活动</option>
								<option value="5">推荐</option>
								<option value="6">电销</option>
							</select>
	        			</td>
	        			<th>跟进日期</th>
	        			<td>
	        				<input id="followTime" name="followTime" class="easyui-datebox" required="required"/>
	        			</td>
	        		</tr>
	        		<tr>
	        			<th>是否推送</th>
	        			<td>
	        				<select style="width:155px" name="kindlySend" id='kindlySend' class="easyui-combobox" required="required">
								<option value="1">推送</option>
								<option value="0">不推送</option>
							</select>
	        			</td>
	        		</tr>
	        		<tr>
	        			<th>备注信息</th>
	        			<td>
	        				<input class="easyui-textbox text-area" id="note" name="note" multiline="true" style="height: 100px;width: 92%;" prompt="在此填写备注信息" />
	        			</td>
	        		</tr>
	        	</table>
	        </form>
	    </div>
	    
	     <div id="dlg-buttons">
	    
	    	<r:mulparampermission uri2="/portal/user/save" uri="/portal/user/update">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
	
	
	
	
	
</body>
</html>