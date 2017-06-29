<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="r" uri="/mytaglib"%>

<spring:url value="/resources/css/employee-list.css"
	var="employeeListCss" />
<spring:url value="/resources/lib/cripto/aes.js" var="aesJs" />
<spring:url value="/resources/lib/cripto/pad-zeropadding.js" var="padJs" />
<spring:url value="/resources/js/editEmployeePassword.js"
	var="editEmployeePasswordJs" />
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>

<jsp:include page="common.jsp" />
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<script src="${aesJs }"></script>
	<script src="${padJs }"></script>
	<script src="${editEmployeePasswordJs }"></script>
	<script type="text/javascript">
$().ready(function(){
	$('#editUserPwdForm').form({
		url : getContextPath() + '/portal/editEmpwd',
		onSubmit : function() {
			progressLoad();
			
			// 加密
			var ps = $('input[name="oldPwd"]').val().trim();
			if(ps != null && ps != '' && ps != undefined){
				
				$('input[name="oldPwd"]').val(Encrypt(ps));
			}
			
			var nps = $('input[name="pwd"]').val().trim();
			if(nps != null && nps != '' && nps != undefined){
				
				$('input[name="pwd"]').val(Encrypt(nps));
			}
			
			var isValid = $(this).form('validate');
			if (!isValid) {
				progressClose();
			}
			return isValid;
		},
		success : function(result) {
			progressClose();
			//parent.$.messager.alert('提示', result.msg, 'info');
			parent.$.modalDialog.handler.dialog('close');
		}
	});
});
</script>
	<div data-options="region:'center',border:true"
		style="overflow: hidden;">
		<form id="editUserPwdForm" method="post">
			<table>
				<tr>
					<th>登录名</th>
					<td><r:outName /></td>
				</tr>
				<tr>
					<th>原密码</th>
					<td><input name="oldPwd" type="password" placeholder="请输入原密码"
						class="easyui-validatebox text-box" data-options="required:true"></td>
				</tr>
				<tr>
					<th>新密码</th>
					<td><input name="pwd" type="password" placeholder="请输入新密码"
						class="easyui-validatebox text-box" data-options="required:true"></td>
				</tr>
				<tr>
					<th>重复密码</th>
					<td><input type="password" placeholder="请再次输入新密码"
						class="easyui-validatebox text-box"
						data-options="required:true,validType:'eqPwd[\'#editUserPwdForm input[name=pwd]\']'"></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>