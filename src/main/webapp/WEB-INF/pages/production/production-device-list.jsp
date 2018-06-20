<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>
<spring:url value="/resources/css/production-resources-list.css" var="productListCss" />
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery.base64.js" var="jquerybase64Js" />
<spring:url value="/resources/js/production/production-base.js" var="productionBaseJs" />
<spring:url value="/resources/js/production/production-device-list.js" var="deviceListJs" />
<spring:url value="/resources/lib/My97DatePicker/WdatePicker.js" var="WdatePickerJs" />

<%-- <spring:url value="/resources/lib/normalize/normalize.css"
	var="normalizeCss" />
<spring:url value="/resources/lib/h5bp/h5bp.css" var="h5bpCss" />
<spring:url value="/resources/css/common.css" var="commonCss" />
<spring:url
	value="/resources/lib/jquery.easyui/themes/default/easyui.css"
	var="easyuiCss" />
<spring:url value="/resources/lib/jquery.easyui/themes/icon.css"
	var="iconCss" /> --%>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<jsp:include page="../common.jsp" />
<link rel="stylesheet" href="${productListCss }">
<script src="${jquerybase64Js }"></script>
<script src="${productionBaseJs }"></script>
<script src="${deviceListJs }"></script>
<script src="${WdatePickerJs }" ></script>

<%-- <link rel="stylesheet" href="${normalizeCss }">
<link rel="stylesheet" href="${h5bpCss }">
<link rel="stylesheet" href="${commonCss }">
<link rel="stylesheet" href="${easyuiCss }">
<link rel="stylesheet" href="${iconCss }"> --%>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<input type="hidden" id="storage_node" value="${file_locate_storage_path }" />
	<input type="hidden" id="default_referrer" value="${referrer }">
	<input type="hidden" id="statusList" value='${statusList }'>
	<input type="hidden" id="typeList" value='${typeList }'>
	<div data-options="region:'north',border:false" style="height: 80px; overflow: hidden;background-color: #fff">
		<form id="searchForm">
			<table>
				<tr>
					<th>类型：</th>
					<td>
						<input id="search-type" name="type" class="easyui-combobox" />
					</td>
					<th>名称：</th>
					<td>
						<input id="search-typeId" name="typeId" class="easyui-combotree" style="width:156px" />
					</td>

					<th>报价(元/天)：</th>
					<td>
						<input name="beginPrice" class="easyui-numberbox" style="width: 76px;"/>~
						<input name="endPrice" class="easyui-numberbox" style="width: 76px;"/>
					</td>
					<th>城市：</th>
					<td>
						<input id="search-city" name="city" class="easyui-combobox" />
					</td>
				</tr>
				<tr>
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
						<input id="search-status" name="status" class="easyui-combobox" />
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
			<r:permission uri="/portal/production/device/save">
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
				<script type="text/javascript">
					$.canSave = true;
				</script>
			</r:permission>
			
			<r:mulparampermission uri2="/portal/production/device/update" uri="/portal/product/device/update">
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
				<script type="text/javascript">
					$.canUpdate = true;
				</script>
			</r:mulparampermission>
			
			<r:permission uri="/portal/production/device/delete">
				<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
			</r:permission>
	
		</div>
		
		<div id="dlg" class="easyui-dialog" style="padding:5px 5px;width: 500px;height: 400px;"
            closed="true" buttons="#dlg-buttons" title="演员信息">
	        <form id="fm" method="post" enctype="multipart/form-data">
	        	<input id="id" name="id" type="hidden" />
	        	<div class="online">
					<div class="lable l-width">类型</div>
					<div class="d-float f-width">
					<input id="type" name="type" class="easyui-combobox" required="true" />
					</div>
					
					<div class="lable l-width">名称</div>
					<div class="d-float f-width1">
					<input id="typeId" name="typeId" class="easyui-combotree" style="width:156px" required="true"/>
					</div>
									
				</div>
				<div class="online">
					
					<div class="lable l-width">数量</div>
					<div class="d-float f-width">
						<input id="quantity" name="quantity" class="easyui-numberbox" required="true"/>
					</div>
					
					<div class="lable l-width">报价(元/天)</div>
					<div class="d-float f-width1">
						<input id="price" name="price" class="easyui-numberbox" required="true" />
					</div>
				</div>		
				<div class="online">
					<div class="lable l-width">所在城市</div>
					<div class="d-float f-width">
					<input id="city" name="city" class="easyui-combobox" required="true"/>
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
					备注
				</div>
				<div class="textarea-position">
					
					<input name="remark" class="easyui-textbox" data-options="prompt:'请完善设备详细信息...',multiline:true" style="height: 100px;width: 500px">
				</div>
	            
	        </form>
	    </div>
	    <div id="dlg-buttons">	    
	    	<r:mulparampermission uri2="/portal/production/device/save" uri="/portal/production/device/update">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
	    
	    <div id="dlgCut" class="easyui-dialog" style="padding:5px 5px;width: 500px;height: 400px;"closed="cutTrue" buttons="#dlgCut-buttons" title="演员信息">
	    	       <div class="" style="height:300px;width: 214px;">
	    	            <img >
	    	       </div>     
	    </div>

 	
</body>
</html>