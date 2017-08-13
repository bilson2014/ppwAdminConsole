<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/lib/kindeditor/themes/default/default.css" var="defaultCss" />
<spring:url value="/resources/lib/kindeditor/plugins/code/prettify.css" var="prettifyCss" />
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery.base64.js" var="jquerybase64Js" />
<spring:url value="/resources/js/mail-list.js" var="mailListJs" />
<spring:url value="/resources/lib/kindeditor/kindeditor-all-min.js" var="kindeditorJs" />
<spring:url value="/resources/lib/kindeditor/plugins/code/prettify.js" var="prettifyJs" />
<spring:url value="/resources/lib/kindeditor/lang/zh_CN.js" var="kindeditorzhJs" />
<spring:url value="/resources/lib/My97DatePicker/WdatePicker.js" var="WdatePickerJs" />
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
<script src="${jquerybase64Js }"></script>
<script src="${kindeditorJs }"></script>
<script src="${prettifyJs }"></script>
<script src="${kindeditorzhJs }"></script>
<script src="${mailListJs }"></script>
<script src="${WdatePickerJs }" ></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<input type="hidden" id="storage_node" value="${file_locate_storage_path }" />
	<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
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
	
	<div data-options="region:'center',border:true" >
			<table id="gride" data-options="fit:true,border:false"></table>
		</div>

		<div id="toolbar" style="display: none;">
			<r:permission uri="/portal/mail/save">
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
				<script type="text/javascript">
					$.canSave = true;
				</script>
			</r:permission>
			
			<r:permission uri="/portal/mail/update">
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
				<script type="text/javascript">
					$.canUpdate = true;
				</script>
			</r:permission>
			
			<r:permission uri="/portal/mail/delete">
				<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
			</r:permission>
			
			<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
		</div>
		
		<div id="dlg" class="easyui-dialog" style="padding:5px 5px;width: 520px;height: 500px;"
            closed="true" buttons="#dlg-buttons" title="作品信息">
	        <form id="fm" method="post">
	        	<input id="mailId" name="id" type="hidden">
	            <div class="online">
					<div class="lable l-width">邮件标题</div>
					<div class="d-float f-width">
						<input id="subject" name="subject" class="easyui-textbox" required="true" />
					</div>
				</div>
				<div class="online">
					<div class="lable l-width">邮件类型</div>
					<div class="d-float f-width">
						<input id="mailType" name="mailType" class="easyui-textbox" required="true" />
					</div>
				</div>
				<div>
					<div >收件人</div>
					<div >
						<input id="receiver" name="receiver" class="easyui-textbox" style="width: 283px;"/> &
						<select name="receiverRole" id="receiverRole"  class="easyui-combobox" style="width: 110px;">
							<!-- <option value="1">项目客户</option>
							<option value="2">项目策划供应商</option>
							<option value="3">项目制作供应商</option> -->
						</select>
					</div>
				</div>
				<div class="online">
					<div class="lable l-width">抄送人</div>
					<div class="d-float f-width">
						<input id="bcc" name="bcc" placeholder="多个抄送人以','分隔" class="easyui-textbox" style="width: 283px;"/>&
						<select name="bccRole" id="bccRole" style="width: 283px;"  class="easyui-combobox"></select>
					</div>
				</div>
				<div class="online">
					<div class="lable l-width">发送人</div>
					<div class="d-float f-width">
						<input id="sender" name="sender" class="easyui-textbox" style="width: 155px;" validType="email" /> or
						<select name="senderRole" id="senderRole" style="width: 110px;"  class="easyui-combobox"></select>
					</div>
				</div>
				<div class="online">
					<div class="lable l-width">附件</div>
					<div class="d-float f-width">
						 <select name="mailFile" id="mailFile" style="width: 283px;"  class="easyui-combobox" data-options="multiple:true">
							<!-- <option value="1">项目简报</option>
							<option value="2">项目排期</option>
							<option value="3">策划方案</option>
							<option value="4">报价单</option>
							<option value="5">PPM</option>
							<option value="6">影片修改表</option>
							<option value="7">客户回复</option> -->
						</select> 
					</div>
				</div>
				<div class="textarea-position">
					<div class="lable l-width">邮件内容</div>
					<input name="content" class="ta-content" required="true" />
				</div>
	            
	        </form>
	    </div>
	    <div id="dlg-buttons">
	    
	    	<r:mulparampermission uri2="/portal/mail/save" uri="/portal/mail/update">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
	    
</body>
</html>