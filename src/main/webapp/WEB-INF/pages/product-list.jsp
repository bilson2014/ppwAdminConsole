<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/product-list.css" var="productListCss" />
<spring:url value="/resources/lib/kindeditor/themes/default/default.css" var="defaultCss" />
<spring:url value="/resources/lib/kindeditor/plugins/code/prettify.css" var="prettifyCss" />
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery.base64.js" var="jquerybase64Js" />
<spring:url value="/resources/js/youku-player.js" var="youkuJs" />
<spring:url value="/resources/js/product-list.js" var="productListJs" />
<spring:url value="/resources/lib/kindeditor/kindeditor-all-min.js" var="kindeditorJs" />
<spring:url value="/resources/lib/kindeditor/plugins/code/prettify.js" var="prettifyJs" />
<spring:url value="/resources/lib/kindeditor/lang/zh_CN.js" var="kindeditorzhJs" />

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
<link rel="stylesheet" href="${productListCss }">
<script type="text/javascript" src="http://player.youku.com/jsapi"></script>
<script src="${jquerybase64Js }"></script>
<script src="${kindeditorJs }"></script>
<script src="${prettifyJs }"></script>
<script src="${kindeditorzhJs }"></script>
<script src="${youkuJs }"></script>
<script src="${productListJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	
	<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
		<form id="searchForm">
			<table>
				<tr>
					<th>所属团队:</th>
					<!-- <td><input id="search-teamName" name="teamId" placeholder="请输入团队名称"/></td> -->
					<%--modify by wanglc 2016-6-30 12:53:52 团队搜索模糊查询 begin --%>
					<td><input id="search-teamName" name="teamName" placeholder="请输入团队名称"/></td>
					<%--modify by wanglc 2016-6-30 12:53:57 团队搜索模糊查询 end --%>
					<th>项目名称:</th>
					<td><input id="search-productName" name="productName" class="easyui-textbox" placeholder="请填写作品名称" /></td>
					<th>审核状态：</th>
					<td>
						<select name="flag" class="easyui-combobox" editable="false">
							<option value="" selected>-- 请选择 --</option>
							<option value="0">审核中</option>
							<option value="1">审核通过</option>
							<option value="2">未审核通过</option>
						</select>
					</td>
					<th>是否挂接外链：</th>
					<td>
						<select name="hret" class="easyui-combobox" editable="false">
							<option value="" selected>-- 请选择 --</option>
							<option value="0">是</option>
							<option value="1">否</option>
						</select>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	
	<div data-options="region:'center',border:true" >
			<table id="gride" data-options="fit:true,border:false"></table>
		</div>

		<div id="toolbar" style="display: none;">
			<r:permission uri="/portal/product/save">
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
				<script type="text/javascript">
					$.canSave = true;
				</script>
			</r:permission>
			
			<r:mulparampermission uri2="/portal/product/update" uri="/portal/product/part/update">
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
				<script type="text/javascript">
					$.canUpdate = true;
				</script>
			</r:mulparampermission>
			
			<r:permission uri="/portal/product/delete">
				<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
			</r:permission>
			
			<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
		</div>
		
		<div id="dlg" class="easyui-dialog" style="padding:5px 5px;width: 520px;height: 500px;"
            closed="true" buttons="#dlg-buttons" title="作品信息">
	        <form id="fm" method="post" enctype="multipart/form-data">
	        	<input id="productId" name="productId" type="hidden">
	        	<input id="sessionId" name="sessionId" type="hidden">
	        	
	            <div class="online">
					<div class="lable l-width">项目名称</div>
					<div class="d-float f-width">
						<input id="productName" name="productName" class="easyui-textbox" required="true" />
					</div>
				</div>
				
				<div class="online">
					<div class="lable l-width">项目类型</div>
					<div class="d-float f-width1">
						<input id="productType" name="productType" class="p-textbox-small" style="width: 144px;height: 30px;"/>
					</div>
					<div class="lable-right l-width">所属团队</div>
					<div class="d-float f-width1">
						<input id="teamId" name="teamId" class="p-textbox-small" style="width: 144px;height: 30px;"/>
					</div>
				</div>
				
				<div class="textarea-position">
					<div class="lable l-width">项目描述</div>
					<textarea class="easyui-textbox" id="pDescription" name="pDescription" multiline="true" style="height: 50px;"></textarea>
				</div>
				
				<div class="online">
					<div class="lable l-width">展示图文</div>
					<div class="d-float f-width1">
						<select name="showType" class="easyui-combobox" editable="false">
							<option value="1" selected>展示</option>
							<option value="0">不展示</option>
						</select>
					</div>
					<div class="lable-right l-width">视频长度</div>
					<div class="d-float f-width1">
						<input id="videoLength" name="videoLength" class="easyui-numberbox" required="true" precision="0"/>
					</div>
				</div>
				
				<div class="online">
					<div class="lable l-width">推荐值</div>
					<div class="d-float f-width1">
						<input id="recommend" name="recommend" class="easyui-numberbox" required="true" precision="0" />
					</div>
					<div class="lable-right l-width">赞值</div>
					<div class="d-float f-width1">
						<input id="supportCount" name="supportCount" class="easyui-numberbox" required="true" precision="0" />
					</div>
				</div>
				
				<div class="online">
					<div class="lable l-width">视频文件</div>
					<div class="d-float f-width">
						<input type="file" id="videoFile" name="uploadFiles" class="p-file" />
					</div>
				</div>
				
				
				<div class="online">
					<div class="lable l-width">缩略图</div>
					<div class="d-float f-width">
						<input type="file" id="picHDFile" name="uploadFiles" class="p-file" />
					</div>
				</div>
				
				<div class="online">
					<div class="lable l-width">封面图片</div>
					<div class="d-float f-width">
						<input type="file" id="picLDFile" name="uploadFiles" class="p-file" />
					</div>
				</div>
				
				<div class="online">
					<div class="lable l-width">标签</div>
					<div class="d-float f-width">
						<input type="text" id="tags" name="tags" class="easyui-textbox" />
					</div>
				</div>
				
				<div class="online">
					<div class="lable l-width">外链</div>
					<div class="d-float f-width">
						<input type="text" id="hret" name="hret" class="easyui-textbox" />
					</div>
				</div>
				
				<div class="online">
					<div class="lable l-width">审核状态</div>
					<div class="d-float f-width">
						<select id="flag" name="flag" class="easyui-combobox" editable="false" required="true">
							<option value="0">审核中</option>
							<option value="1" selected>审核通过</option>
							<option value="2">未通过审核</option>
						</select>
					</div>
				</div>
				
				<div class="textarea-position">
					<div class="lable l-width">视频描述</div>
					<!-- <textarea class="ta-content" id="videoDescription" name="videoDescription" ></textarea> -->
					<input name="videoDescription" class="ta-content" />
				</div>
	            
	        </form>
	    </div>
	    <div id="dlg-buttons">
	    
	    	<r:mulparampermission uri2="/portal/product/save" uri="/portal/product/update">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
	    
	    <!-- image/video show content begin-->
		<div id="picture-condition" class="picture-condition hide">
			<div class="picture-modalDialog">
				<div class="picture-condition-body">
					<div class="operation-panel">
						<img id="productPicture" src="" style="height: 360px;width: 640px;" class="hide" >
						<video style="height: 360px;width: 640px" id="productVideo" class="hide" controls="controls" src=""></video>
						<div id="youku-player" style="height: 360px;width: 640px;display: none;"></div>
						<div class="p-label">
							<a href="#" class="button p-submit" id="p-cancel">取消</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- image/video show content end-->
</body>
</html>