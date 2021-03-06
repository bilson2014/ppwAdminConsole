<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/project-list.css" var="projectListCss"/>
<spring:url value="/resources/js/project-meeting.js" var="projectMeetingJs"/>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
	<jsp:include page="common.jsp" />
	<link rel="stylesheet" href="${projectListCss }">
	<script src="${projectMeetingJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 66px; overflow: hidden;background-color: #fff">
		<form id="searchForm"  method="post">
			<input type="hidden" name="payment" value="malfunction"/>
			<table>
				<tr>
					<th>项目名称:</th>
					<td>
						<input id="search-projectId" name="projectName" class="easyui-combobox"  placeholder="请输入项目名称" style="width: 136px;"/>
					</td>
					<th>项目状态:</th>
					<td>
						<select id="search-state"  name="state" editable="false" class="easyui-combobox" style="width: 100px;">
							<option value="" selected></option>
							<option value="0" >正常</option>
            				<option value="1" >取消</option>
            				<option value="3" >暂停</option>
            				<option value="2" >完成</option>
						</select>
					</td>
					<th>视频管家:</th>
					<td>
						<input id="search-userId" name="userId" class="easyui-combobox" placeholder="请输入视频管家名称" style="width: 100px;"/>
					</td>
					<th>项目阶段:</th>
					<td>
						<select id="stage" name="stage" editable="false" class="easyui-combobox" style="width: 100px;">
							<option value="" >全部</option>
							<option value="沟通" >沟通</option>
            				<option value="方案" >方案</option>
            				<option value="商务" >商务</option>
            				<option value="制作" >制作</option>
            				<option value="交付" >交付</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>是否协同:</th>
					<td>
						<select id="isSynergy" name="isSynergy" editable="false" class="easyui-combobox" style="width: 100px;">
							<option value="0" >否</option>
            				<option value="1" >是</option>
						</select>
					</td>
					<th>项目来源:</th>
					<td>
						<input id="search-source" name="source" class="easyui-combobox" placeholder="请选择项目来源" editable="false" style="width: 100px;"/>
					</td>
					<th>客户:</th>
					<td>
						<input id="search-customerId" name="customerId"  class="easyui-combobox" placeholder="请输入视频管家名称" style="width: 100px;"/>
					</td>
					<th></th>
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
		<a onclick="loadResourceFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">文件列表</a>
		
		<a onclick="loadLogFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">日志列表</a>
		
		<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
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
		
		<div id="log-condition" class="log-condition hide">
			<div class="log-modalDialog">
				<div class="log-condition-body">
					<div class="operation-panel" id="log-container">
					</div>
					<div class="log-label">
						<a href="javascript:void(0);" class="button log-submit" id="log-cancel">取消</a>
					</div>
				</div>
			</div>
		</div>
</body>
</html>