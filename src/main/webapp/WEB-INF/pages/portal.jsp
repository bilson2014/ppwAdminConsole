<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib"%>

<spring:url value="/resources/css/portal.css" var="portalCss" />
<spring:url value="/resources/js/portal.js" var="portalJs" />

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<jsp:include page="common.jsp" />
<link rel="stylesheet" href="${portalCss }">
<script src="${portalJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

	<div data-options="region:'north',border:false"
		style="overflow: hidden;">
		<div class="title">
			<div class="header-logo">
				<font class="title-font" color="#0000CC">拍片网后台管理平台</font>
			</div>
			<div class="header-right-content">
				<div class="header-right-userDest">
					<r:identity role="employee">
							欢迎   <b><r:outName /></b>
						<a href="javascript:void(0)" onclick="editUserPwd()"
							style="color: #fff">修改密码</a>
					</r:identity>
					<a href="javascript:void(0)" onclick="logout()" style="color: #fff">安全退出</a>
				</div>
			</div>
		</div>
	</div>
	<div data-options="region:'west',split:true" title="主导航"
		style="width: 160px; overflow: hidden; overflow-y: auto;">
		<div class="well well-small" style="padding: 5px 5px 5px 5px;">
			<ul id="menu"></ul>
		</div>
	</div>
	<div data-options="region:'center'"
		style="overflow: hidden; height: 100%;">
		<div id="index_tabs" style="overflow: hidden;">
			<div title="首页" data-options="border:false" style="overflow: hidden;">
				<div style="padding: 10px">
					<r:identity role="employee">
						<h2>系统介绍</h2>
						<div class="light-info">
							<div class="light-tip icon-tip"></div>
							<div>欢迎使用拍片网后台管理系统。</div>
							<div id="order"></div>
							<div id="new_user"></div>
						</div>
					</r:identity>
				</div>
			</div>
		</div>
	</div>
	<div data-options="region:'south',border:false"
		style="height: 26px; line-height: 26px; overflow: hidden; text-align: center; background-color: #eee">版权所有@拍片网
	</div>
</body>
</html>