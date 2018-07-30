<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>
<spring:url value="/resources/css/production-resources-list.css" var="productListCss" />
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery.base64.js" var="jquerybase64Js" />
<spring:url value="/resources/js/production/production-base.js" var="productionBaseJs" />
<spring:url value="/resources/js/production/production-costume-list.js" var="costumeListJs" />
<spring:url value="/resources/lib/My97DatePicker/WdatePicker.js" var="WdatePickerJs" />
<spring:url value="/resources/lib/jcrop/jquery.Jcrop.min.js" var="jcropJs"/>
<spring:url value="/resources/lib/jcrop/jquery.color.js" var="jcropColorJs"/>
<spring:url value="/resources/lib/jcrop/jquery.Jcrop.min.css" var="jcropCss"/>
<spring:url value="/resources/js/cutphoto-common.js" var="cutphoto"></spring:url>
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
<link rel="stylesheet" href="${jcropCss }">
<script src="${jquerybase64Js }"></script>
<script src="${productionBaseJs }"></script>
<script src="${costumeListJs }"></script>
<script src="${WdatePickerJs }" ></script>
<script src="${jcropJs }" ></script>
<script src="${jcropColorJs }" ></script>
<script src="${cutphoto }"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<input type="hidden" id="storage_node" value="${file_locate_storage_path }" />
	<input type="hidden" id="default_referrer" value="${referrer }">
	<input type="hidden" id="default_referrer_name" value="${referrer_name }">
	<input type="hidden" id="statusList" value='${statusList }'>
	<input type="hidden" id="nature" value='${nature}'>
	<input type="hidden" id="natureName" value="${natureName }">
	<input type="hidden" id="clothingTypeList" value='${clothingTypeList }'>
	<input type="hidden" id="accreditList" value='${accreditList }'>
	<input type="hidden" id="propsTypeList" value='${propsTypeList }'>
	
	<div data-options="region:'north',border:false" style="height: 80px; overflow: hidden;background-color: #fff">
		<form id="searchForm">
			<table>
				<tr>
					<th>姓名：</th>
					<td>
						<input type="hidden" id="dataLevel" name="dataLevel" value="${dataLevel }">
						<input id="search-name" name="name" class="easyui-textbox"/>
					</td>
					<th>标准化分级：</th>
					<td>
						<input id="search-typeId" name="typeId" class="easyui-combotree" style="width:156px" />
					</td>
					<th>报价：</th>
					<td>
						<input name="beginPrice" class="easyui-numberbox" style="width: 76px;"/>~
						<input name="endPrice" class="easyui-numberbox" style="width: 76px;"/>
					</td>
					
					<th>${natureName }类别：</th>
					<td>
						<input id="search-type" name="type" class="easyui-combobox" style="width:156px" />
					</td>
					<th>授权方式：</th>
					<td>
						<input id="search-accredit" name="accredit" class="easyui-combobox" style="width:156px" />
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
			<r:permission uri="/portal/production/costume-${nature}/save">
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
				<script type="text/javascript">
					$.canSave = true;
				</script>
			</r:permission>
			
			<r:mulparampermission uri2="/portal/production/costume-${nature}/update" uri="/portal/product/costume-${nature}/update">
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
				<script type="text/javascript">
					$.canUpdate = true;
				</script>
			</r:mulparampermission>
			
			<r:permission uri="/portal/production/costume-${nature}/delete">
				<a onclick="delFuca();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
			</r:permission>
	
		</div>
		
		<div id="dlg" class="easyui-dialog" style="padding:5px 5px;width: 500px;height: 400px;"
            closed="true" buttons="#dlg-buttons" title="${positionName }信息">
	        <form id="fm" method="post" enctype="multipart/form-data">
	        	<input id="id" name="id" type="hidden" />
	        	
	        	<div class="online">
					<div class="lable l-width">姓名</div>
					<div class="d-float f-width">
						<input id="name" name="name" class="easyui-textbox" required="true" />
					</div>
					
					<div class="lable l-width">类别</div>
					<div class="d-float f-width1">
						<input id="type" name="type" class="easyui-combobox" required="true" />
					</div>
					
				</div>
				<div class="online">
					<div class="lable l-width">授权方式</div>
					<div class="d-float f-width">
						<input id="accredit" name="accredit" class="easyui-combobox" required="true" />
					</div>
					
					<div class="lable l-width">库存(套)</div>
					<div class="d-float f-width1">
						<input id="stockNumber" name="stockNumber" class="easyui-numberbox" required="true" />
					</div>
					
				</div>
				<div class="online">
					<div class="lable l-width" id="priceLabel">报价(元/天)</div>
					<div class="d-float f-width">
						<input id="price" name="price" class="easyui-numberbox" required="true" />
					</div>
					<div class="lable l-width">所在城市</div>
					<div class="d-float f-width1">
					<input id="city" name="city"  class="easyui-combobox" required="true"/>
					</div>
				</div>
				
				<div class="online">
					<div class="lable l-width">标准化分级</div>
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
				备注  ：请完善${positionName }尺寸、用途
				</div>			
				<div class="textarea-position">
					<input name="remark" class="easyui-textbox" data-options="prompt:'请完善${positionName }尺寸、用途等信息...',multiline:true" style="height: 100px;width: 500px">
				</div>
							
				<div class="online">
					<div class="lable l-width">照片</div>
					<div class="d-float f-width"  style="margin-top:0px !important;">
					
						<div id="uploadDiv" class="easyui-linkbutton">选择文件</div>  
						<div ></div> 
					</div>
				</div>
				<div class="online disPlay" id="imgDisplay">				
				</div>
				<input id="photo" name="photo" type="hidden"/>	
				<input id="delImg" name="delImg" type="hidden">
	            
	        </form>
	    </div>
	    <div id="dlg-buttons">	    
	    	<r:mulparampermission uri2="/portal/production/costume-${nature}/save" uri="/portal/production/costume-${nature}/update">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
</body>
</html>