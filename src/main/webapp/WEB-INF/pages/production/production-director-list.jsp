<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>
<spring:url value="/resources/css/production-resources-list.css" var="productListCss" />
<%-- <spring:url value="/resources/lib/kindeditor/themes/default/default.css" var="defaultCss" />
<spring:url value="/resources/lib/kindeditor/plugins/code/prettify.css" var="prettifyCss" /> --%>
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery.base64.js" var="jquerybase64Js" />
<%-- <spring:url value="/resources/js/youku-player.js" var="youkuJs" /> --%>
<spring:url value="/resources/js/production/production-base.js" var="productionBaseJs" />
<spring:url value="/resources/js/production/production-director-list.js" var="directorListJs" />
<%-- <spring:url value="/resources/lib/kindeditor/kindeditor-all-min.js" var="kindeditorJs" />
<spring:url value="/resources/lib/kindeditor/plugins/code/prettify.js" var="prettifyJs" />
<spring:url value="/resources/lib/kindeditor/lang/zh_CN.js" var="kindeditorzhJs" /> --%>
<spring:url value="/resources/lib/My97DatePicker/WdatePicker.js" var="WdatePickerJs" />
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<jsp:include page="../common.jsp" />
<%-- <link rel="stylesheet" href="${defaultCss }">
<link rel="stylesheet" href="${prettifyCss }"> --%>
<link rel="stylesheet" href="${productListCss }">
<!-- <script type="text/javascript" src="http://player.youku.com/jsapi"></script> -->
<script src="${jquerybase64Js }"></script>
<%-- <script src="${kindeditorJs }"></script>
<script src="${prettifyJs }"></script>
<script src="${kindeditorzhJs }"></script>
<script src="${youkuJs }"></script> --%>
<script src="${productionBaseJs }"></script>
<script src="${directorListJs }"></script>
<script src="${WdatePickerJs }" ></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<input type="hidden" id="storage_node" value="${file_locate_storage_path }" />
	<input type="hidden" id="default_referrer" value="${referrer }">
	<input type="hidden" id="statusList" value='${statusList }'>
	<div data-options="region:'north',border:false" style="height: 80px; overflow: hidden;background-color: #fff">
		<form id="searchForm">
			<table>
				<tr>
					<th>姓名：</th>
					<td>
						<input id="search-name" name="name" class="easyui-textbox"/>
					</td>
					<th>标准化元素：</th>
					<td>
						<input id="search-typeId" name="typeId" class="easyui-combotree" style="width:156px" />
					</td>
					<th>报价(元/天)：</th>
					<td>
						<input name="beginPrice" class="easyui-numberbox" style="width: 76px;"/>~
						<input name="endPrice" class="easyui-numberbox" style="width: 76px;"/>
					</td>
					<th>擅长领域：</th>
					<td>
						<input id="search-specialty" name="specialty" class="easyui-combobox" />						
					</td>
				</tr>
				<tr>
					<th>城市：</th>
					<td>
						<input id="search-city" name="city" class="easyui-combobox" />
					</td>
					<th>供应商：</th>
					<td>
						<input id="search-teamId" name="teamId" class="easyui-combobox" />
					</td>
					<th>推荐人：</th>
					<td>
						<input id="search-referrer" name="referrer" class="easyui-combobox" />
					</td>
					<th>审核状态：</th>
					<td>
						<input id="search-status" name="status" class="easyui-combobox"/>
					</td>
					
					
					<th></th>
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

		<div id="toolbar" style="display: none;">
			<r:permission uri="/portal/production/director/save">
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
				<script type="text/javascript">
					$.canSave = true;
				</script>
			</r:permission>
			
			<r:mulparampermission uri2="/portal/production/director/update" uri="/portal/product/director/update">
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
				<script type="text/javascript">
					$.canUpdate = true;
				</script>
			</r:mulparampermission>
			
			<r:permission uri="/portal/production/director/delete">
				<a onclick="delFuca();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
			</r:permission>
	
		</div>
		
		<div id="dlg" class="easyui-dialog" style="padding:5px 5px;width: 500px;height: 400px;"
            closed="true" buttons="#dlg-buttons" title="导演信息">
	        <form id="fm" method="post" enctype="multipart/form-data">
	        	<input id="id" name="id" type="hidden" />
	        	<div class="online">
					<div class="lable l-width">姓名</div>
					<div class="d-float f-width">
						<input id="name" name="name" class="easyui-textbox" required="true" />
					</div>
					<div class="lable l-width">报价(元/天)</div>
					<div class="d-float f-width1">
						<input id="price" name="price" class="easyui-numberbox" required="true" />
					</div>
				</div>
				
				<div class="online">
					<div class="lable l-width">擅长领域</div>
					<div class="d-float f-width">
						<input id="specialty" name="specialty"  class="easyui-combobox" />
					</div>
					<div class="lable l-width">所在城市</div>
					<div class="d-float f-width1">
					<input id="city" name="city"  class="easyui-combobox" required="true"/>
					</div>
				</div>
				<div class="online">
					<div class="lable l-width">标准化元素</div>
					<div class="d-float f-width">
					<input id="typeId" name="typeId" class="easyui-combotree" style="width:156px" required="true"/>
					</div>
					<div class="lable l-width">供应商</div>
					<div class="d-float f-width1">
						<input id="teamId" name="teamId" class="easyui-combobox" required="true"/>
					</div>
					
				</div>
				<div class="online">
					<div class="lable l-width">推荐人</div>
					<div class="d-float f-width">
						<input id="referrer" name="referrer" class="easyui-combobox"/>
					</div>
					<div class="lable l-width">审核状态</div>
					<div class="d-float f-width1">
					<input id="status" name="status" class="easyui-combobox" required="true" />
					</div>
				</div>
				<div class="online">
				备注  ：请完善导演简历及作品
				</div>			
				<div class="textarea-position">
					<input name="remark" class="easyui-textbox" data-options="prompt:'请完善导演简历及作品等...',multiline:true" style="height: 100px;width: 500px">
				</div>
							
				<div class="online">
					<div class="lable l-width">照片</div>
					<div class="d-float f-width"  style="margin-top:0px !important;">
					
						<div id="uploadDiv" class="easyui-linkbutton">选择文件</div>  
						<div id="fileDiv" style="display: none"></div> 
						
						<!-- <input type="file" id="videoFile" style="width:100%" name="uploadFiles" class="p-file" multiple="multiple" onchange="addImg(this)" accept="image/gif,image/jpeg,image/jpg,image/png"/> -->
					</div>
				</div>
				<div class="online disPlay" id="imgDisplay">				
				</div>
				<input id="photo" name="photo" type="hidden"/>	
				<input id="delImg" name="delImg" type="hidden">
	            
	        </form>
	    </div>
	    <div id="dlg-buttons">	    
	    	<r:mulparampermission uri2="/portal/production/director/save" uri="/portal/production/director/update">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>

 	
</body>
</html>