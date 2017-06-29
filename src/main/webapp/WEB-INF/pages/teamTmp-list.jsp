<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib"%>

<spring:url value="/resources/lib/kindeditor/themes/default/default.css"
	var="defaultCss" />
<spring:url value="/resources/lib/kindeditor/plugins/code/prettify.css"
	var="prettifyCss" />
<%-- import JS --%>
<spring:url value="/resources/lib/juicer.js" var="juicerJs" />
<spring:url value="/resources/js/teamTmp-list.js" var="TeamTmpListJs" />
<spring:url value="/resources/lib/kindeditor/plugins/code/prettify.js"
	var="prettifyJs" />
<spring:url value="/resources/lib/My97DatePicker/WdatePicker.js"
	var="WdatePickerJs" />
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<jsp:include page="common.jsp" />
<link rel="stylesheet" href="${defaultCss }">
<link rel="stylesheet" href="${prettifyCss }">
<script src="${juicerJs }"></script>
<script src="${prettifyJs }"></script>
<script src="${TeamTmpListJs }"></script>
<script src="${WdatePickerJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<input type="hidden" id="storage_node"
		value="${file_locate_storage_path }" />
	<div data-options="region:'north',border:false"
		style="height: 40px; overflow: hidden; background-color: #fff">
		<form id="searchForm">
			<table>
			</table>
		</form>
	</div>

	<div data-options="region:'center',border:true">
		<table id="gride" data-options="fit:true,border:false"></table>
	</div>

	<div id="toolbar" style="display: none;">
		<r:permission uri="/portal/teamTmp/update">
			<a onclick="editFuc();" href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="plain:true,iconCls:'icon-edit'">修改</a>
			<script type="text/javascript">
					$.canUpdate = true;
				</script>
		</r:permission>
	</div>

	<div id="dlg" class="easyui-dialog"
		style="padding: 5px 5px; width: 520px; height: 500px;" closed="true"
		buttons="#dlg-buttons" title="作品信息">
		<form id="fm" method="post">
			<input type="hidden" name="id" value=''>
			<div id="diff_container"></div>
			<br>
			<br>
			<br>
			<div class="online">
				<div class="lable l-width">审核</div>
				<select name="checkStatus" class="easyui-combobox" editable="false"
					required="true" style="width: 150px;">
					<option value="0" selected="selected">未审核</option>
					<option value="1">审核通过</option>
					<option value="2">审核不通过</option>
				</select>
			</div>
			<div class="online">
				<div class="lable l-width">审核详情</div>
				<input class="easyui-textbox text-area" name="checkDetails"
					multiline="true" style="height: 100px; width: 92%;"
					prompt="再此填写审核详情" />
			</div>
		</form>
	</div>
	<div id="dlg-buttons">

		<r:permission uri="/portal/teamTmp/update">
			<a href="javascript:void(0)" class="easyui-linkbutton c6"
				iconCls="icon-ok" onclick="save()">保存</a>
		</r:permission>

		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>

</body>
</html>