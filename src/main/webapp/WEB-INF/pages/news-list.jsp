<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/lib/kindeditor/themes/default/default.css" var="defaultCss" />
<spring:url value="/resources/lib/kindeditor/plugins/code/prettify.css" var="prettifyCss" />
<%-- import JS --%>
<spring:url value="/resources/lib/jquery/jquery.base64.js" var="jquerybase64Js" />
<spring:url value="/resources/js/news-list.js" var="newsListJs" />
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
<script src="${newsListJs }"></script>
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
			<r:permission uri="/portal/news/save">
				<a onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
				<script type="text/javascript">
					$.canSave = true;
				</script>
			</r:permission>
			
			<r:permission uri="/portal/news/update">
				<a onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
				<script type="text/javascript">
					$.canUpdate = true;
				</script>
			</r:permission>
			
			<r:permission uri="/portal/news/delete">
				<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
			</r:permission>
			
			<a onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
		</div>
		
		<div id="dlg" class="easyui-dialog" style="padding:5px 5px;width: 520px;height: 500px;"
            closed="true" buttons="#dlg-buttons" title="新闻信息">
	        <form id="fm" method="post" enctype="multipart/form-data">
	        	<input id="newsId" name="id" type="hidden">
	        	<input type="hidden" name="picLDUrl" />
	            <div class="online">
					<label class="lable l-width">新闻标题</label>
					<input id="title" name="title" class="easyui-textbox" required="true" />
					<label class="lable l-width">是否显示到主页</label>
					<select id="status" name="status" required="true" editable="false" class="easyui-combobox" style="width: 158px;">
						<option value="1" selected>显示</option>
	          			<option value="0" >不显示</option>
					</select>
				</div>
				<br>
				<div class="online">
					<label class="lable l-width">新闻描述</label>
					<input id="discription" name="discription" multiline="true" class="easyui-textbox text-area" required="true" style="height: 100px;width: 82%;"/>
				</div>
				<br>
				<div class="online">
					<label class="lable l-width">新闻标签</label>
					<input id="tags" name="tags" class="easyui-textbox" style="width: 82%"/>
				</div>
				<br>
				<div class="online">
					<label class="lable l-width">新闻封面</label>
					<input type="file" id="picLDUrlFile" name="picLDUrlFile"/>
					
				</div>
				<br>
				<div class="online">
					<label class="lable l-width">新闻类别</label>
					<select id="category" name="category" required="true" editable="false" class="easyui-combobox" style="width: 158px;">
						<option value="0" selected>最热咨询</option>
	          			<option value="1" >案例分享</option>
	          			<option value="2" >企业活动</option>
	          			<option value="3" >行业资讯</option>
	          			<option value="4" >人物访谈</option>
					</select>
				</div>
				<br>
				<div class="textarea-position">
					<div class="lable l-width">新闻内容</div>
					<input name="content" class="ta-content" required="true" />
				</div>
	            
	        </form>
	    </div>
	    <div id="dlg-buttons">
	    
	    	<r:mulparampermission uri2="/portal/news/save" uri="/portal/news/update">
		        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()" >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
	    
</body>
</html>