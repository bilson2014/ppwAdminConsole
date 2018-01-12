<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/projectflow-list.css" var="flowTemptaleCss" />
<spring:url value="/resources/js/projectflow-list.js" var="activityJs" />
<spring:url value="/resources/lib/jquery.easyui/datagrid-dnd.js" var="datagridDndJs" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js"
	var="jsonJs" />
<spring:url value="/resources/lib/jquery/jquery.base64.js" var="jquerybase64Js" />
<spring:url value="/resources/lib/My97DatePicker/WdatePicker.js"
	var="WdatePickerJs" />
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
<script type="text/javascript" src="${jsonJs }"></script>
<script type="text/javascript" src="${datagridDndJs }"></script>
<script src="${activityJs }"></script>
<script src="${jquerybase64Js }"></script>
<script src="${WdatePickerJs }"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<input type="hidden" id='sourceCombobox' value='${sourceCombobox }'> 
		<div data-options="region:'north',border:false" style="height: 100px; overflow: hidden;background-color: #fff">
			<form id="searchForm" method="post">
				<table>
					<tr>
						<th>项目名称:</th>
						<td><input name="projectName" id="searchProjectName" placeholder="请输入项目名称"/></td>
						<th>项目状态:</th>
						<td><select id="search-projectStatus" name="projectStatus" style="width: 135px;"  class="easyui-combobox">
								<option value="">---请选择---</option>
								<option value="running">进行中</option>
								<option value="suspend">挂起</option>
								<option value="cancel">取消</option>
								<option value="finished">已完成</option>
							</select></td>
						<th>负责人:</th>
						<td><input name="principalName" id="searchPrincipalName" placeholder="请输入项目负责人"/></td>
						<th>策划:</th>
						<td><input name="schemeName" id="search_schemeName" placeholder="请输入策划人名称"/></td>
					</tr>
					<tr>	
						<th>监制:</th>
						<td><input name="superviseName" id="search_superviseName" placeholder="请输入监制名称"/></td>
						<th>客户名称:</th>
						<td><input name="userName" id="search_userName" placeholder="请输入客户名称"/></td>
						<th>供应商名称:</th>
						<td><input name="teamName" id="search_teamName" placeholder="请输入供应商名称"/></td>
						<th>项目来源:</th>
						<td><select name="projectSource" id="search_projectSource" style="width: 135px;"  class="easyui-combobox"></select></td>
					</tr>
					<tr>	
						<th>项目阶段:</th>
						<td><select id="search-projectStage" name="projectStage" style="width: 135px;"  class="easyui-combobox">
								<option value="">---请选择---</option>
								<option value="1">沟通阶段</option>
								<option value="2">方案阶段</option>
								<option value="3">商务阶段</option>
								<option value="4">制作阶段</option>
								<option value="5">交付阶段</option>
							</select></td>
						<th>产品线:</th>
						<td><input name="productId" id="search_productId" placeholder="请输入项目名称"/></td>
						<th>等级:</th>
						<td><input name="productConfigLevelId" id="search_productConfigLevelId" placeholder="请输入项目名称"/></td>
						<th>创建时间:</th>
						<td>
						<input name="beginTime" style="width: 135px;" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly" required="true" />~
						<input name="endTime" style="width: 135px;" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly" required="true" />
						</td>
						<td>
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a>
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
							<a onclick="exportInfo();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">报表导出</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
		
		<div data-options="region:'center',border:true" >
			<table id="gride" data-options="fit:true,border:false"></table>
		</div>

		<div id="toolbar" style="display: none;">
				<a onclick="teamDetail();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">供应商信息</a>
				<a onclick="userDetail();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">客户信息</a>
				<a onclick="employeeDetail();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">团队信息</a>
				<a onclick="projectDetail();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">项目信息</a>
				<a onclick="fileDetail();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">文件列表</a>
				<a onclick="logDetail();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">项目日志</a>
				<a onclick="financeDetail();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">财务信息</a>
				<r:permission uri="/portal/projectflow/delete">
					<a onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
				</r:permission>
		</div>
	    
		<div id="teamDlg" class="easyui-dialog" style="width:500px; height:300px;padding:10px 20px"
            closed="true" buttons="#team-buttons" title="供应商信息">
            <form id="teamf" method="post" enctype="multipart/form-data">
            	<input type="hidden" name="projectId" id="projectId">
	            
	            <table style="width: 98%;" id="teamTabel">
	            	<!-- <tr><th colspan="4">策划供应商</th></tr>
	            	<tr>
	            		<th>名称</th>
	            		<td><input name="scheme_teamName" id="scheme_teamName" class="easyui-textbox" ></td>
	            		<th>联系人</th>
	            		<td><input name="linkman" id="scheme_linkman" class="easyui-textbox" ></td>
	            	</tr>
	            	<tr>
	            		<th>电话</th>
	            		<td><input name="telephone" id="scheme_telephone" class="easyui-textbox" ></td>
	            		<th>金额</th>
	            		<td><input name="actualPrice" id="scheme_actualPrice" class="easyui-textbox" ></td>
	            	</tr>
	            	<tr><th colspan="4">制作供应商</th></tr> 
	            	 <tr>
	            		<th>名称</th>
	            		<td><input name="teamName" id="produce_teamName" class="easyui-textbox" ></td>
	            		<th>联系人</th>
	            		<td><input name="linkman" id="produce_linkman" class="easyui-textbox" ></td>
	            	</tr>
	            	<tr>
	            		<th>电话</th>
	            		<td><input name="telephone" id="produce_telephone" class="easyui-textbox" ></td>
	            		<th>金额</th>
	            		<td><input name="actualPrice" id="produce_actualPrice" class="easyui-textbox" ></td>
	            	</tr> -->
	            	<!-- <tr>
	            	 
	            	 <td colspan="4">
	            	 	<div class="otherTeam"> 					
						</div>
					<hr/>
	            	 </td>
	            	</tr> -->
	            </table>
	            
				<div id="team-buttons">
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#teamDlg').dialog('close')" >取消</a>
				</div>
	        </form>
	    </div>
	    
	    <div id="employeeDlg" class="easyui-dialog" style="width:500px; height:400px;padding:10px 20px"
            closed="true" buttons="#employee-buttons" title="团队信息">
            <form id="employeef" method="post" enctype="multipart/form-data">
            	<input type="hidden" name="projectId" id="projectId">
            	<input type="hidden" name="synergyId" id="synergyId">
	            <!-- <div class="employeeContent">
	            </div> -->
	           <table style="width: 98%;">
	           <tr>
	           		<th>负责人</th>
	            	<td><input name="sale" id="sale" style="width: 135px;"  class="easyui-combobox" required="required" editable="false"></td>
	            	<th>供应商管家</th>
	            	<td><input name="teamProvider" id="teamProvider" style="width: 135px;"  class="easyui-combobox" editable="false"></td>
	           </tr>
	           <tr>
	           		<th>创意总监</th>
	            	<td><input name="creativityDirector" id="creativityDirector" style="width: 135px;"  class="easyui-combobox" editable="false"></td>
	            	<th>项目助理</th>
	            	<td><input name="customerDirector" id="customerDirector" style="width: 135px;"  class="easyui-combobox" disabled="disabled" ></td>
	           </tr>
	           <tr>
	           		<th>销售总监</th>
	            	<td><input name="saleDirector" id="saleDirector" style="width: 135px;"  class="easyui-combobox" editable="false"></td>
	            	<!-- <th>财务主管</th>
	            	<td><input name="financeDirector" id="financeDirector" style="width: 135px;"  class="easyui-combobox" disabled="disabled" ></td> -->
	          
	           		<th>供应商总监</th>
	            	<td><input name="teamDirector" id="teamDirector" style="width: 135px;"  class="easyui-combobox" disabled="disabled" ></td>
	            </tr>
	            <tr>
	            	<th>监制总监</th>
	            	<td><input name="superviseDirector" id="superviseDirector" style="width: 135px;"  class="easyui-combobox" editable="false"></td>
	           
	           		<th>财务</th>
	            	<td><input name="finance" id="finance" style="width: 135px;"  class="easyui-combobox" editable="false"></td>
	           </tr>
	           <tr>
	            	<th>策划</th>
	            	<td><input name="scheme" id="scheme" style="width: 135px;"  class="easyui-combobox" editable="false"></td>
	           		<th>监制</th>
	            	<td><input name="supervise" id="supervise" style="width: 135px;"  class="easyui-combobox" editable="false"></td>
	           </tr>
	           </table>
				<div id="employee-buttons">
					<r:permission uri="/portal/project-synergy/update">
						<a href="javascript:void(0)" class="easyui-linkbutton c6"  iconCls="icon-ok" onclick="updateEmployeeFuc();">保存</a>
					</r:permission>
			
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#employeeDlg').dialog('close')" >取消</a>
				</div>
	        </form>
	    </div>
	    
	    <div id="userDlg" class="easyui-dialog" style="width:500px; height:200px;padding:10px 20px"
            closed="true" buttons="#user-buttons" title="客户信息">
            <form id="userf" method="post" enctype="multipart/form-data">
            	<input type="hidden" name="projectId" id="projectId">
            	<input type="hidden" name="projectUserId" id="projectUserId">
	            
	            <table style="width: 98%;">
	            	<tr>
	            		<th>名称</th>
	            		<td><input name="userName" class="easyui-textbox" ></td>
	            		<th>联系人</th>
	            		<td><input name="linkman" class="easyui-textbox" ></td>
	            	</tr>
	            	<tr>
	            		<th>电话</th>
	            		<td><input name="telephone" class="easyui-textbox" ></td>
	            		<th>评级</th>
	            		<td><input name="userLevel" class="easyui-textbox" ></td>
	            	</tr>
	            	<tr>
	            		<th>邮箱</th>
	            		<td><input name="email" class="easyui-textbox" ></td>
	            	</tr>
	            </table>
	            
				<div id="user-buttons">
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#userDlg').dialog('close')" >取消</a>
				</div>
	        </form>
	    </div>
	    <div id="projectDlg" class="easyui-dialog" style="width:500px; height:400px;padding:10px 20px"
            closed="true" buttons="#project-buttons" title="项目信息">
            <form id="projectf" method="post" enctype="multipart/form-data">
            	<input type="hidden" name="projectId" id="projectId">
	            
	            <table style="width: 98%;">
	            	<tr>
	            		<th>名称</th>
	            		<td><input name="projectName" class="easyui-textbox" ></td>
	            		<th>评级</th>
	            		<td>
	            		<select id="projectGrade" name="projectGrade" style="width: 155px;"  class="easyui-combobox" disabled="disabled">
								<option value="5">S</option>
								<option value="4">A</option>
								<option value="5">B</option>
								<option value="2">C</option>
								<option value="1">D</option>
								<option value="0">E</option>
							</select></td>
	            	</tr>
	            	<tr>
	            		<th>来源</th>
	            		<td><select name="projectSource" id="projectSource" style="width: 155px;"  class="easyui-combobox"></select></td>
	            		<th>产品线</th>
	            		<td><input name="productName" class="easyui-textbox" ></td>
	            	</tr>
	            	<tr>
	            		<th>等级</th>
	            		<td><input name="productConfigLevelName" class="easyui-textbox" ></td>
	            		<th>配置</th>
	            		<td><input name="productConfigLengthName" class="easyui-textbox" ></td>
	            	</tr>
	            	<tr>
	            		<th>项目周期</th>
	            		<td><input name="projectCycle" class="easyui-textbox" ></td>
	            		<th>对标影片</th>
	            		<td><input name="filmDestPath" class="easyui-textbox" ></td>
	            	</tr>
	            	<tr>
	            		<th >项目预算</th>
	            		<td ><input name="projectBudget" class="easyui-textbox" ></td>
	            	</tr>
	            	<tr><th colspan="4">项目描述</th></tr>
	            	<tr>
	            	<td colspan="4">
	            		<input class="easyui-textbox text-area" name="projectDescription" multiline="true" style="height: 100px;width: 98%;" disabled="disabled"/>
	            	</td>
	            	</tr>
	            </table>
	            
				<div id="project-buttons">
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#projectDlg').dialog('close')" >取消</a>
				</div>
	        </form>
	    </div>
	     <div id="fileDlg" class="easyui-dialog" style="width:710px; height:400px;padding:10px 20px"
            closed="true" title="文件列表">
				<table id="file-gride" data-options="fit:true,border:false"></table>
	    </div>
	     <div id="logDlg" class="easyui-dialog" style="width:700px; height:400px;padding:10px 20px"
            closed="true" title="项目日志">
				<table id="log-gride" data-options="fit:true,border:false"></table>
	    </div>
	<div id="financeDlg" class="easyui-dialog"
		style="width: 560px; height: 400px; padding: 10px 20px" closed="true"
		buttons="#finance-buttons" title="财务信息">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="height: 50px">
				<form id="financef" method="post" enctype="multipart/form-data">
					<input type="hidden" name="projectId" id="projectId">

					<table style="width: 98%;">
						<tr>
							<th>客户实付金额</th>
							<td><input name="customerAmount" class="easyui-textbox"></td>
							<!-- <th>付给供应商金额</th>
	            		<td><input name="providerAmount" class="easyui-textbox" ></td> -->
						</tr>
					</table>

				</form>
			</div>
			<div data-options="region:'center',border:false">
				<table id="finalce-gride" data-options="fit:true,border:false"></table>
			</div>
		</div>
		<div id="finance-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel"
				onclick="javascript:$('#financeDlg').dialog('close')">取消</a>
		</div>
	</div>
</body>
</html>