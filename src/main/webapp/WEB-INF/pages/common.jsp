<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%-- import CSS --%>
<spring:url value="/resources/lib/normalize/normalize.css" var="normalizeCss" />
<spring:url value="/resources/lib/h5bp/h5bp.css" var="h5bpCss" />
<spring:url value="/resources/lib/jquery.easyui/themes/gray/easyui.css" var="easyuiCss" />
<spring:url value="/resources/lib/jquery.easyui/themes/icon.css" var="iconCss" />
<spring:url value="/resources/css/common.css" var="commonCss" />
<%-- import JS --%>
<spring:url value="/resources/lib/html5shiv/html5shiv.js" var="html5shivJs" />
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js" var="jqueryJs" />
<spring:url value="/resources/lib/jquery/plugins.js" var="pluginJs" />
<spring:url value="/resources/lib/jquery/ajaxfileupload.js" var="ajaxfileuploadJs" />
<spring:url value="/resources/lib/jquery.blockui/jquery.blockUI.js" var="blockUIJs" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js" var="jsonJs" />
<spring:url value="/resources/lib/jquery.easyui/jquery.easyui.min.js" var="easyuiJs" />
<spring:url value="/resources/lib/jquery.easyui/locale/easyui-lang-zh_CN.js" var="zhJs" />
<spring:url value="/resources/js/common.js" var="commonJs" />

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=9,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="${normalizeCss }">
<link rel="stylesheet" href="${h5bpCss }">
<link rel="stylesheet" href="${easyuiCss }">
<link rel="stylesheet" href="${iconCss }">
<link rel="stylesheet" href="${commonCss }">
<!--[if lt IE 9]>
		<script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
	<![endif]-->
<script src="${jqueryJs }"></script>
<script src="${pluginJs }"></script>
<script src="${ajaxfileuploadJs }"></script>
<script src="${blockUIJs }"></script>
<script src="${jsonJs }"></script>
<script src="${easyuiJs }"></script>
<script src="${zhJs }"></script>
<script src="${commonJs }"></script>
