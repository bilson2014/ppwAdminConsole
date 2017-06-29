<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib"%>

<%-- import JS --%>
<spring:url value="/resources/js/sms-list.js" var="smsListJs" />
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<jsp:include page="common.jsp" />
<script src="${smsListJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

	<div data-options="region:'north',border:false"
		style="height: 40px; overflow: hidden; background-color: #fff">
		<form id="searchForm">
			<table>
				<!-- <tr>
					<th>上传时间:</th>
					<td>
						<input name="beginTime" style="width: 76px;" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly" required="true" />~
					</td>
					<td>
						<input name="endTime" style="width: 76px;" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly" required="true" />
					</td>
					<td>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
					</td>
				</tr> -->
			</table>
		</form>
	</div>

	<div data-options="region:'center',border:true">
		<table id="gride" data-options="fit:true,border:false"></table>
	</div>

	<div id="toolbar" style="display: none;">
		<r:permission uri="/portal/sms/save">
			<a onclick="addFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-add'">添加</a>
			<script type="text/javascript">
					$.canSave = true;
				</script>
		</r:permission>

		<r:permission uri="/portal/sms/update">
			<a onclick="editFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-edit'">修改</a>
			<script type="text/javascript">
					$.canUpdate = true;
				</script>
		</r:permission>

		<r:permission uri="/portal/sms/delete">
			<a onclick="delFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-remove'">删除</a>
		</r:permission>
	</div>

	<div id="dlg" class="easyui-dialog"
		style="padding: 5px 5px; width: 520px; height: 500px;" closed="true"
		buttons="#dlg-buttons" title="作品信息">
		<form id="fm" method="post" enctype="multipart/form-data">
			<input id="smsId" name="id" type="hidden">
			<div class="online">
				<div class="lable l-width">短信平台模板ID</div>
				<div class="d-float f-width">
					<input id="tempId" name="tempId" class="easyui-textbox"
						required="true" />
				</div>
			</div>
			<div class="online">
				<div class="lable l-width">模板标题</div>
				<div class="d-float f-width">
					<input id="tempTitle" name="tempTitle" class="easyui-textbox"
						required="true" />
				</div>
			</div>
			<div class="online">
				<div class="lable l-width">模板内容</div>
				<div class="d-float f-width">
					<input required="true" class="easyui-textbox text-area"
						id="tempContent" name="tempContent" multiline="true"
						style="height: 100px; width: 92%;" prompt="在此填写对短信模板" />
				</div>
			</div>
		</form>
	</div>
	<div id="dlg-buttons">

		<r:mulparampermission uri2="/portal/sms/save" uri="/portal/sms/update">
			<a href="javascript:void(0)" class="easyui-linkbutton c6"
				iconCls="icon-ok" onclick="save()">保存</a>
		</r:mulparampermission>

		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>

</body>
</html>