<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%-- import CSS --%>
<spring:url value="/resources/lib/normalize/normalize.css"
	var="normalizeCss" />
<spring:url
	value="/resources/lib/jquery.easyui/themes/default/easyui.css"
	var="easyuiCss" />
<spring:url value="/resources/lib/jquery.easyui/themes/icon.css"
	var="iconCss" />
<spring:url value="/resources/css/common.css" var="commonCss" />
<spring:url value="/resources/css/login.css" var="loginCss" />

<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js"
	var="jqueryJs" />
<spring:url value="/resources/lib/jquery/plugins.js" var="pluginJs" />
<spring:url value="/resources/lib/jquery.easyui/jquery.easyui.min.js"
	var="easyuiJs" />
<spring:url value="/resources/lib/cripto/aes.js" var="aesJs" />
<spring:url value="/resources/lib/cripto/pad-zeropadding.js" var="padJs" />
<spring:url value="/resources/js/common.js" var="commonJs" />
<spring:url value="/resources/js/login.js" var="loginJs" />
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
<link rel="stylesheet" href="${easyuiCss }">
<link rel="stylesheet" href="${iconCss }">
<link rel="stylesheet" href="${commonCss }">
<link rel="stylesheet" href="${loginCss }">
<!--[if lt IE 9]>
		<script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
	<![endif]-->
<script src="${jqueryJs }"></script>
<script src="${pluginJs }"></script>
<script src="${easyuiJs }"></script>
<script src="${aesJs }"></script>
<script src="${padJs }"></script>
<script src="${commonJs }"></script>
<script src="${loginJs }"></script>

</head>
<body>
	<div class="login">
		<form method="post" id="loginform">
			<div class="logo"></div>
			<div class="login_form">
				<div class="user">
					<input class="text_value" type="text" name="employeeLoginName"
						data-options="required:true"></input> <input class="text_value"
						type="password" id="password" data-options="required:true"></input>
					<input class="text_value" type="hidden" name="employeePassword"></input>
				</div>
				<button class="button" type="button" id="login-btn">登录</button>
			</div>

			<div id="tip"></div>
		</form>
	</div>
</body>
</html>