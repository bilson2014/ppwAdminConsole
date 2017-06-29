<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib"%>

<spring:url value="/resources/css/flowTemptale.css"
	var="flowTemptaleCss" />
<spring:url value="/resources/js/test.js" var="TestJs" />
<spring:url value="/resources/lib/jquery.easyui/datagrid-dnd.js"
	var="datagridDndJs" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js"
	var="jsonJs" />
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<jsp:include page="common.jsp" />
<link rel="stylesheet" href="${flowTemptaleCss }">
<script type="text/javascript" src="${jsonJs }"></script>
<script type="text/javascript" src="${datagridDndJs }"></script>
<script src="${TestJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	flow Template id:
	<input type="text" class="easyui-textbox" id="templateId"
		required="required" /> process Id:
	<input type="text" style="border-radius: 6px; border: 1px solid #333;"
		placeholder="自动填写" id="processId">
	<div id="flow" style="margin: 10px"></div>
	<div id="flowbtn">
		<button id="startbtn">启动新的流程</button>
		<button id="nextbtn">下一步</button>
		<button id=prevbtn>上一步</button>
		<button id="refresh" onclick="refresh()">刷新</button>
	</div>
	<br />
	<br />
	<label id="error"></label>
	<br />
	<br />
	<br />
	<br />

	<table id="flowList">

	</table>

	<br />
	<br />
	<div>
		<button onclick="pay()">模拟完成支付任务</button>
		res :<label id="errorpay"></label>
	</div>
</body>
</html>