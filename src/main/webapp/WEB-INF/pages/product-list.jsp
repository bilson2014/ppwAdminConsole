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
<link rel="stylesheet" href="${productListCss }">
<script type="text/javascript" src="http://player.youku.com/jsapi"></script>
<script src="${jquerybase64Js }"></script>
<script src="${kindeditorJs }"></script>
<script src="${prettifyJs }"></script>
<script src="${kindeditorzhJs }"></script>
<script src="${youkuJs }"></script>
<script src="${productListJs }"></script>
<script src="${WdatePickerJs }" ></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<input type="hidden" id="storage_node" value="${file_locate_storage_path }" />
	<div data-options="region:'north',border:false" style="height: 66px; overflow: hidden;background-color: #fff">
		<form id="searchForm">
			<table>
				<tr>
					<th>所属团队:</th>
					<td><input id="search-teamName" name="teamName" placeholder="请输入团队名称"/></td>
					<th>项目名称:</th>
					<td><input id="search-productName" name="productName" class="easyui-textbox" placeholder="请填写作品名称" /></td>
					<th>上传时间:</th>
					<td>
						<input name="beginTime" style="width: 76px;" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly" required="true" />~
						<input name="endTime" style="width: 76px;" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly" required="true" />
					</td>
					<th>审核状态：</th>
					<td>
						<select name="flag" class="easyui-combobox" editable="false">
							<option value="" selected>-- 请选择 --</option>
							<option value="0">审核中</option>
							<option value="1">审核通过</option>
							<option value="2">未审核通过</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>是否可见：</th>
					<td>
						<select name="visible" class="easyui-combobox" editable="false">
							<option value="" selected>-- 请选择 --</option>
							<option value="0">可见</option>
							<option value="1">不可见</option>
						</select>
					</td>
					<th>是否挂接外链：</th>
					<td>
						<select name="hret" class="easyui-combobox" editable="false">
							<option value="" selected>-- 请选择 --</option>
							<option value="0">是</option>
							<option value="1">否</option>
						</select>
					</td>
					<th>推荐值：</th>
					<td>
						<select name="recommend" class="easyui-combobox" editable="false">
							<option value="" selected>-- 请选择 --</option>
							<option value="0">未推荐</option>
							<option value="1">热门爆款</option>
							<option value="2">精品案例</option>
							<option value="3">推荐视频</option>
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
			
			<a onclick="setMaster();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-ok'">设为代表作</a>
			<a onclick="recommendFuc()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'">首页推荐</a>
			
			<a onclick="toPlayHtml()" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'">作品链接</a>
		</div>
		
		<div id="dlg" class="easyui-dialog" style="padding:5px 5px;width: 6500px;height: 500px;"
            closed="true" buttons="#dlg-buttons" title="作品信息">
	        <form id="fm" method="post" enctype="multipart/form-data">
	        	<input id="productId" name="productId" type="hidden" />
	        	<input id="visible" name="visible" type="hidden"/>
				<div class="online">
					<div class="lable l-width">项目名称</div>
					<div class="d-float f-width">
						<input id="productName" name="productName" class="easyui-textbox" required="true" />
					</div>
					<div class="lable l-width">所属团队</div>
					<div class="d-float f-width1">
						<input id="teamId" name="teamId" required="true" class="p-textbox-small" />
					</div>
				</div>
				
				<div class="textarea-position">
					<div class="lable l-width">项目描述</div>
					<textarea class="easyui-textbox" id="pDescription" name="pDescription" multiline="true" style="width:156px;height: 50px;"></textarea>
				</div>
				
				<div class="online">
					<div class="lable l-width">展示图文</div>
					<div class="d-float f-width1">
						<select name="showType" id='showType' class="easyui-combobox" style="width:156px" editable="false">
							<option value="1" selected>展示</option>
							<option value="0">不展示</option>
						</select>
					</div>
					<div class="lable-right l-width">视频长度</div>
					<div class="d-float f-width1">
						<input id="videoLength" name="videoLength" class="easyui-numberbox"  precision="0"/>
					</div>
				</div>
				
				<div class="online">
					<div class="lable l-width">推荐值</div>
					<div class="d-float f-width1">
						<input id="recommend" name="recommend" required="true" class="easyui-numberbox" style="width: 156px"  precision="0" />
					</div>
					<div class="lable-right l-width">创作时间</div>
					<div class="d-float f-width1">
						<input class="textbox" name="creationTime" style="width:156px" id = "creationTime" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly"/>
					</div>
				</div>
				<input id="videoUrl"  type="hidden"/>
				<input id="picLDUrl"  type="hidden"/>
				<div class="online">
					<div class="lable l-width" >视频文件</div>
					<div class="d-float f-width" style="margin-top:0px !important;">
						<input type="file" id="videoFile" style="width:100%" name="uploadFiles" class="p-file" />
					</div>
				</div>
				<div class="online">
					<div class="lable l-width">封面图片</div>
					<div class="d-float f-width" style="margin-top:0px !important;">
						<input type="file" id="picLDFile" style="width:100%" class="p-file" />
					</div>
				</div>
				
				<div class="online">
					<div class="lable l-width">标签</div>
					<div class="d-float f-width1">
						<input type="text" id="tags" name="tags" style="width:156px" required="true" class="easyui-textbox" />
					</div>
					
					<div class="lable-right l-width">赞值</div>
					<div class="d-float f-width1">
						<input id="supportCount" required="true" name="supportCount" class="easyui-numberbox" precision="0" />
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
						<select id="flag" name="flag" class="easyui-combobox"  editable="false" required="true">
							<option value="0">审核中</option>
							<option value="1" selected>审核通过</option>
							<option value="2">未通过审核</option>
						</select>
					</div>
				</div>
				<div class="textarea-position">
					<div class="lable l-width">审核详情</div>
					<textarea class="easyui-textbox" id="checkDetails" name="checkDetails" multiline="true" style="height: 50px;width: 422px"></textarea>
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
						<video autoplay="autoplay" style="height: 360px;width: 640px" id="productVideo" class="hide" controls="controls" src=""></video>
						<div id="youku-player" style="height: 360px;width: 640px;display: none;"></div>
						<div class="p-label">
							<a href="#" class="button p-submit" id="p-cancel">取消</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- image/video show content end-->
		
		<div id="recommend-dlg" class="easyui-dialog" style="width:430px; height:480px;padding:10px 20px"
           closed="true" buttons="#recommend-dlg-buttons" title="首页推荐">
			<table id="recommend-gride" data-options="fit:true,border:false"></table>
    </div>
     <div id="recommend-dlg-buttons">
    </div>
</body>
</html>