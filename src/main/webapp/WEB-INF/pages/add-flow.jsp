<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%-- import CSS --%>
<spring:url value="/resources/lib/normalize/normalize.css"
	var="normalizeCss" />
<spring:url value="/resources/lib/h5bp/h5bp.css" var="h5bpCss" />
<spring:url value="/resources/css/common.css" var="commonCss" />
<spring:url value="/resources/css/user-list.css" var="userListCss" />
<spring:url
	value="/resources/lib/jquery.easyui/themes/default/easyui.css"
	var="easyuiCss" />
<spring:url value="/resources/lib/jquery.easyui/themes/icon.css"
	var="iconCss" />
<%-- import JS --%>
<spring:url value="/resources/lib/html5shiv/html5shiv.js"
	var="html5shivJs" />
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js"
	var="jqueryJs" />
<spring:url value="/resources/lib/jquery/plugins.js" var="pluginJs" />
<spring:url value="/resources/lib/jquery.blockui/jquery.blockUI.js"
	var="blockUIJs" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js"
	var="jsonJs" />
<spring:url value="/resources/lib/jquery.easyui/jquery.easyui.min.js"
	var="easyuiJs" />
<spring:url
	value="/resources/lib/jquery.easyui/locale/easyui-lang-zh_CN.js"
	var="zhJs" />
<spring:url value="/resources/js/common.js" var="commonJs" />

<spring:url value="/resources/lib/My97DatePicker/WdatePicker.js"
	var="WdatePicker" />
<spring:url value="/resources/lib/jquery.cookie/jquery.cookie.js"
	var="cookiejs" />


<spring:url value="/resources/css/flow/addflow.css" var="addflowcss" />
<spring:url value="/resources/js/flow/addflow.js" var="addflowjs" />


<spring:url value="/resources/lib/jquery/ajaxfileupload.js"
	var="ajaxfileuploadJs" />
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=9,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="${normalizeCss }">
<link rel="stylesheet" href="${h5bpCss }">
<link rel="stylesheet" href="${commonCss }">
<link rel="stylesheet" href="${easyuiCss }">
<link rel="stylesheet" href="${iconCss }">

<link rel="stylesheet" href="${addflowcss }">
<!--[if lt IE 9]>
		<script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
	<![endif]-->
<script src="${jqueryJs }"></script>
<script src="${pluginJs }"></script>
<script src="${blockUIJs }"></script>
<script src="${jsonJs }"></script>
<script src="${easyuiJs }"></script>
<script src="${zhJs }"></script>
<script src="${commonJs }"></script>
<script src="${WdatePicker }"></script>
<script src="${cookiejs }"></script>
<script type="text/javascript" src="${addflowjs }"></script>

<script type="text/javascript" src="${ajaxfileuploadJs}"></script>
</head>
<body>
	<div class="page">
		<label class="state">${state }</label>
		<div class="page-title">
			<label class="page-title-title">项目信息添加</label>
		</div>

		<div class="infobody">
			<div class="baseinfo">
				<div class="baseinfo-left">
					<div class="baseinfo-left-text-div">
						<label class="baseinfo-left-text">基础信息</label>
					</div>
				</div>
				<div class="baseinfo-right">
					<table class="baseinfo-table">
						<tr>
							<td class="baseinfo-table-key">项目编号</td>
							<td class="baseinfo-table-key">项目名称</td>
							<td class="baseinfo-table-key">项目价格</td>
						</tr>
						<tr>
							<td class="baseinfo-table-value td-addbottom-border"><input
								type="text" class="tableinput-baseinfo projectId"></td>
							<td class="baseinfo-table-value td-addbottom-border"><input
								type="text" class="tableinput-baseinfo projectName"></td>
							<td class="baseinfo-table-value td-addbottom-border">
								<div class="pirce-div mleft">
									<input type="text" class="pirce-input firstinput">~<input
										type="text" class="pirce-input lastinput">
									&nbsp;&nbsp;&nbsp;
									<button class="pirce-btn">确认</button>
								</div>
							</td>
						</tr>

						<tr>
							<td class="baseinfo-table-key">客户名称</td>
							<td class="baseinfo-table-key">客户联系人</td>
							<td class="baseinfo-table-key">客户电话</td>
						</tr>
						<tr>
							<td class="baseinfo-table-value td-addbottom-border"><input
								type="text" class="tableinput-baseinfo userName"></td>
							<td class="baseinfo-table-value td-addbottom-border"><input
								type="text" class="tableinput-baseinfo userContact"></td>
							<td class="baseinfo-table-value td-addbottom-border"><input
								type="text" class="tableinput-baseinfo userPhone"></td>
						</tr>

						<tr>
							<td class="baseinfo-table-key">供应商名称</td>
							<td class="baseinfo-table-key">供应商联系人</td>
							<td class="baseinfo-table-key">供应商电话</td>
						</tr>
						<tr>
							<td class="baseinfo-table-value td-addbottom-border"><input
								type="text" class="tableinput-baseinfo teamName"></td>
							<td class="baseinfo-table-value td-addbottom-border"><input
								type="text" class="tableinput-baseinfo teamContact"></td>
							<td class="baseinfo-table-value td-addbottom-border"><input
								type="text" class="tableinput-baseinfo teamPhone"></td>
						</tr>

						<tr>
							<td class="baseinfo-table-key">阶段描述</td>
						</tr>
						<tr>
							<td colspan="3"><textarea rows="3" cols="74"
									class="textarea-baseinfo description"></textarea></td>
						</tr>

					</table>
				</div>
			</div>
		</div>

		<div class="indent-time">
			<div class="indent-time-left">
				<div class="indent-time-left-text-div">
					<label class="indent-time-left-text">预计时间表</label>
				</div>
			</div>
			<div class="indent-time-right">
				<div class="table-border">
					<table class="indent-time-table">
						<tr>
							<td>阶段</td>
							<td>沟通</td>
							<td>方案</td>
							<td>商务</td>
							<td>制作</td>
							<td>交付</td>
						</tr>
						<tr class="indent-time-table-border-tr">
							<td>预计时间</td>
							<td><input type="text" class="tableinput gtstarttime"
								id="gtstarttime"></td>
							<td><input type="text" class="tableinput fastarttime"
								id="gtstarttime"></td>
							<td><input type="text" class="tableinput swstarttime"
								id="gtstarttime"></td>
							<td><input type="text" class="tableinput zzstarttime"
								id="gtstarttime"></td>
							<td><input type="text" class="tableinput jfstarttime"
								id="gtstarttime"></td>
						</tr>
					</table>
				</div>
			</div>
		</div>

		<div class="indent-btn-div">
			<button class="indent-btn">确认</button>
		</div>
	</div>

</body>
</html>