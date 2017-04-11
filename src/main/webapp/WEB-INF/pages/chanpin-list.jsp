<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/flowTemptale.css" var="flowTemptaleCss" />
<spring:url value="/resources/lib/zyUpload/css/zyUpload.css" var="zyUploadCss" />
<spring:url value="/resources/js/chanpin-list.js" var="chanpinJs" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js"
	var="jsonJs" />
<spring:url value="/resources/lib/zyUpload/js/zyUpload.js"
	var="zyUploadJs" />
<spring:url value="/resources/lib/zyUpload/js/zyFile.js"
	var="zyFileJs" />
<spring:url value="/resources/lib/jquery.easyui/datagrid-dnd.js" var="datagridDndJs" />
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
<link rel="stylesheet" href="${zyUploadCss }">
<script type="text/javascript" src="${chanpinJs }"></script>
<script type="text/javascript" src="${zyUploadJs }"></script>
<script type="text/javascript" src="${zyFileJs }"></script>
<script type="text/javascript" src="${datagridDndJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
		<input type="hidden" id="storage_node" value="${file_locate_storage_path }" />
		<!-- <div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
			<form id="searchForm">
				<table>
					<tr>
						<th>姓名:</th>
						<td><input name="activityName" id="searchFormactivityName" placeholder="请输入活动名称"/></td>
						<td>
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
						</td>
					</tr>
				</table>
			</form>
		</div> -->
		
		<div data-options="region:'center',border:true" >
			<table id="gride" data-options="fit:true,border:false"></table>
		</div>

		<div id="toolbar" style="display: none;">
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">新增产品</a>
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改产品</a>
				<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除产品</a>
				<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
				<a onclick="showPicImg();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'">设置封面</a>
				<a onclick="showFeature();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'">设置特性</a>
		</div>
	    
		<div id="dlg" class="easyui-dialog" style="width:380px; height:300px;"
            closed="true" buttons="#dlg-buttons" title="人员信息">
            <form id="fm" method="post" enctype="multipart/form-data">
            	<input type="hidden" name="chanpinId" id="chanpinId">
            	<input id="chanpinFeature" type="hidden" name="chanpinFeature" >
            	<div class="fitem">
	                <label>产品名称：</label>
	                <input id="chanpinName" name="chanpinName" class="easyui-textbox" required="true">
	            </div>
	            <div class="fitem">
	                <label>场景：</label>
	            </div>
	             <div class="sceneTag">
	            	
	            </div>
	            <div>
	            	<label>产品描述：</label><br/>
	                <textarea class="easyui-textbox" id="chanpinDescription" name="chanpinDescription" multiline="true" style="width:256px;height: 50px;"></textarea>
	            </div>
				<div id="dlg-buttons">
					<r:mulparampermission uri2="/portal/employee/save" uri="/portal/employee/update">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
					</r:mulparampermission>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
				</div>
	        </form>
	    </div>
	    
	    <!-- 产品封面相关   ---  begin -->
		    <div id="PicDlg" class="easyui-dialog" style="width:646px; height:560px;"
	            closed="true"  title="人员信息">
	           <div id="PicD" style="width:100%; height:100%;">
	           		<table id="imgList" data-options="fit:true,border:false"></table>
	           </div>
	           <div id="PicDlgBody" style="display: none;">
	           </div>
		    </div>
		    <div id="toolbar2" style="display: none;">
					<a onclick="subImg();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">上传封面</a>
					<a onclick="saveOd();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">保存顺序</a>
					<a onclick="back();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
			</div>
		<!-- 产品封面相关   ---  end -->
		
		<!-- 产品特性相关   --- begin  -->
		<div id="featureDlg" class="easyui-dialog" style="width:646px; height:560px;"
            closed="true"  title="人员信息">
			<table id="featureBody" data-options="fit:true,border:false"></table>
			
			 <div id="toolbar3" style="display: none;">
					<a onclick="addFeature();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加特性</a>
					<a onclick="updateFeature();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改特性</a>
					<a onclick="saveFeatureOd();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">保存顺序</a>
					<a onclick="back();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
			</div>
	    </div>
	    
	    <div id="dlgFeatureForm" class="easyui-dialog" style="width:380px; height:300px;"
            closed="true" buttons="#dlg-buttons1" title="人员信息">
            <form id="fmFeature" method="post" enctype="multipart/form-data">
            	<input type="hidden" name="chanpinId" id="chanpinId-Feature">
            	<input type="hidden" name="fId" id="fId">
            	<div class="fitem">
	                <label>特性名称：</label>
	                <input id="name" name="name" class="easyui-textbox" required="true">
	            </div>
	            <div class="fitem">
	                <label>封面图片：</label>
	                <input id="scenenPicLDUrlFile" type="file" name="fileList"  required="true">
	            </div>
	            <div>
	            	<label>特性描述：</label><br/>
	                <textarea class="easyui-textbox" id="description" name="description" multiline="true" style="width:256px;height: 50px;"></textarea>
	            </div>
				 <div id="dlg-buttons1">
					<r:mulparampermission uri2="/portal/employee/save" uri="/portal/employee/update">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveFeature()" >保存</a>
					</r:mulparampermission>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgFeatureForm').dialog('close')" >取消</a>
				</div> 
	        </form>
	    </div>
		<!-- 产品特性相关   --- end  -->
		
		<!-- image/video show content begin-->
		<div id="picture-condition" class="picture-condition hide">
			<div class="picture-modalDialog">
				<div style="position: absolute; z-index: 9999999;border: 1px solid #CCC;background-color: #ffffff;box-shadow: 1px 2px 4px 2px rgba(0, 0, 0, 0.3);border-radius: 3px;">
					<div class="" >
						<img id="productPicture" src=""  style="width: 100%!important;height: 365px!important;" class="hide" >
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