<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib"%>

<spring:url value="/resources/lib/My97DatePicker/WdatePicker.js"
	var="WdatePickerJs" />
<spring:url value="/resources/js/job-list.js" var="jobListJs" />

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>

<jsp:include page="common.jsp" />
<script src="${WdatePickerJs }"></script>
<script src="${jobListJs }"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false"
		style="height: 40px; overflow: hidden; background-color: #fff">
		<form id="searchForm">
			<table>
				<tr>
					<th>招聘岗位:</th>
					<td><input name="jobName" placeholder="请输入招聘岗位" /></td>

					<td><a href="javascript:void(0);" class="easyui-linkbutton"
						data-options="iconCls:'icon-search',plain:true"
						onclick="searchFun();">查询</a><a href="javascript:void(0);"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-cancel',plain:true"
						onclick="cleanFun();">清空</a></td>
				</tr>
			</table>
		</form>
	</div>

	<div data-options="region:'center',border:true">
		<table id="gride" data-options="fit:true,border:false"></table>
	</div>

	<div id="toolbar" style="display: none;">
		<r:permission uri="/portal/job/save">
			<a onclick="addFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-add'">添加</a>
		</r:permission>

		<r:permission uri="/portal/job/update">
			<a onclick="editFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-edit'">修改</a>
		</r:permission>

		<r:permission uri="/portal/job/delete">
			<a onclick="delFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-remove'">删除</a>
		</r:permission>

		<a onclick="cancelFuc();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
	</div>

	<div id="dlg" class="easyui-dialog"
		style="width: 360px; padding: 10px 20px" closed="true"
		buttons="#dlg-buttons" title="人员信息">
		<form id="fm" method="post" enctype="multipart/form-data">
			<input name="jobId" type="hidden">
			<div class="fitem">
				<label>岗位名称:</label> <input name="jobName" class="easyui-textbox"
					required="true">
			</div>
			<div class="fitem">
				<label>岗位简介:</label> <input name="jobDescription"
					class="easyui-textbox" required="true" multiline="true"
					style="height: 100px;">
			</div>
			<div class="fitem">
				<label>招聘要求:</label> <input name="demand" class="easyui-textbox"
					required="true" multiline="true" style="height: 100px;">
			</div>
			<div class="fitem">
				<label>起始时间:</label> <input name="startDate"
					onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					readonly="readonly" required="true" />
			</div>
			<div class="fitem">
				<label>结束时间:</label> <input name="endDate"
					onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					readonly="readonly" required="true" />
			</div>
		</form>
	</div>
	<div id="dlg-buttons">

		<r:mulparampermission uri2="/portal/job/save" uri="/portal/job/update">
			<a href="javascript:void(0)" class="easyui-linkbutton c6"
				iconCls="icon-ok" onclick="save()">保存</a>
		</r:mulparampermission>

		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>
</body>
</html>