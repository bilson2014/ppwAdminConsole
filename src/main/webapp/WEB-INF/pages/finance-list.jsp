<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/project-list.css" var="projectListCss"/>
<spring:url value="/resources/js/finance-list.js" var="financeListJs"/>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
	<jsp:include page="common.jsp" />
	<link rel="stylesheet" href="${projectListCss }">
	<script src="${financeListJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 36px;overflow: hidden;background-color: #fff">
		<form id="searchForm"  method="post">
			<table>
				<tr>
					<th>入出类型:</th>
					<td>
						<select id="search-type"  name="logType" editable="false" class="easyui-combobox" style="width: 76px;">
							<option value=""></option>
							<option value="0" >入账</option>
            				<option value="1" >出账</option>
						</select>
					</td>
					
					<th>交易方式:</th>
					<td>
						<select id="search-resource"  name="dealLogSource" editable="false" class="easyui-combobox" style="width: 76px;">
							<option value=""></option>
							<option value="0" >线上支付</option>
            				<option value="1" >线下支付</option>
						</select>
					</td>
					
					<td>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	
	<div data-options="region:'center',border:true" >
		<table id="gride" data-options="fit:true,border:false"></table>
	</div>

</body>
</html>