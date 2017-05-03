<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="r" uri="/mytaglib" %>

<spring:url value="/resources/css/team-list.css" var="teamListCss" />

<spring:url value="/resources/lib/My97DatePicker/WdatePicker.js" var="WdatePickerJs" />
<spring:url value="/resources/js/team-list.js" var="teamListJs" />


<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<jsp:include page="common.jsp" />
<link rel="stylesheet" href="${teamListCss }">
<script src="${WdatePickerJs }"></script>
<script src="${teamListJs }"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<input type="hidden" id="storage_node" value="${file_locate_storage_path }" />
	<div data-options="region:'north',border:false" style="height: 40px; overflow: hidden;background-color: #fff">
			<form id="searchForm">
				<table>
					<tr>
						<th>团队名称：</th>
						<td><input id="search-teamName" name="teamName" placeholder="请输入供应商名称"/></td>
						<th>手机号：</th>
						<td><input id="search-phoneNumber" name="phoneNumber" placeholder="请输入手机号码"/></td>
						<th>审核状态：</th>
						<td>
							<select id="search-flag" name="flag">
								<option value="" selected="selected">-- 请选择 --</option>
								<option value="0">审核中</option>
								<option value="1">审核通过</option>
								<option value="2">未通过审核</option>
							</select>
						</td>
						<th>价格区间</th>
						<td>
							<select id="search-price" name="priceRange">
								<option value="" selected="selected">-- 请选择 --</option>
								<option value="0" >看情况</option>
	            				<option value="1" >1万元及以上</option>
	            				<option value="2" >2万元及以上</option>
	            				<option value="3" >3万元及以上</option>
	            				<option value="4" >5万元及以上</option>
	            				<option value="5" >10万元及以上</option>
							</select>
						</td>
						
						<th>业务范围</th>
						<td>
							<select id="search-business" name="business" style="width:180px"></select>
							<div id="sp">
								<div style="padding:10px">
									<input type="checkbox" name="business" value="0" /> <span>广告</span>
			            			<input type="checkbox" name="business" value="1"/> <span>宣传片</span>
			            			<input type="checkbox" name="business" value="2"/> <span>真人秀</span>
			            			<input type="checkbox" name="business" value="3"/> <span>纪录片</span>
			            			<input type="checkbox" name="business" value="4"/> <span>病毒视频</span>
			            			<input type="checkbox" name="business" value="5"/> <span>电视栏目</span>
			            			<input type="checkbox" name="business" value="6"/> <span>三维动画</span>
			            			<input type="checkbox" name="business" value="7"/> <span>MG动画</span>
			            			<input type="checkbox" name="business" value="8"/> <span>体育赛事</span>
			            			<input type="checkbox" name="business" value="9"/> <span>专题片</span>
			            			<input type="checkbox" name="business" value="10"/> <span>VR拍摄</span>
			            			<input type="checkbox" name="business" value="11"/> <span>产品拍摄</span>
			            			<input type="checkbox" name="business" value="12"/> <span>微电影</span>
			            			<input type="checkbox" name="business" value="13"/> <span>航拍</span>
			            			<input type="checkbox" name="business" value="14"/> <span>活动视频</span>
			            			<input type="checkbox" name="business" value="15"/> <span>后期制作</span>
								</div>
							</div>
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
			<r:permission uri="/portal/team/save">
				<a id="add-btn" onclick="addFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
			</r:permission>
			
			<r:permission uri="/portal/team/update">
				<a id="edit-btn" onclick="editFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改</a>
			</r:permission>
			
			<r:permission uri="/portal/team/delete">
				<a id="del-btn" onclick="delFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除</a>
			</r:permission>
			
			<a  id="cancel-btn" onclick="cancelFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">取消操作</a>
			
			
			<a  id="cancel-btn" onclick="recommendFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'">首页推荐</a>
		</div>
		
		<div id="dlg" class="easyui-dialog" style="width:520px; height:480px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons" title="人员信息">
	        <form id="fm" method="post" enctype="multipart/form-data">
	        	<input name="teamId" type="hidden">
	            
	            <table style="width: 98%;">
	            	<tr>
	            		<th >公司名称</th>
	            		<td><input name="teamName" class="easyui-textbox" required="true"></td>
	            	</tr>
	            	<tr>
	            		<th >登录名</th>
	            		<td><input name="loginName" validtype="vLoginName" class="easyui-validatebox textbox" id="loginName"></td>
	            		<th>审核状态</th>
	            		<td>
	            			<select id="flag" name="flag" class="easyui-combobox" style="width: 150px;height:28px;line-height: 28px;" required="true" editable="false">
								<option value="0">审核中</option>
								<option value="1" >审核通过</option>
								<option value="2">未通过审核</option>
							</select>
	            		</td>
	            	</tr>
	            	
	            	<tr>
	            		<th>联系人姓名</th>
	            		<td><input name="linkman" class="easyui-textbox" required="true"/></td>
	            		<th>手机号码</th>
	            		<td><input class="easyui-validatebox textbox" data-options="validType:['mobile','vPhoneNumber']"  name="phoneNumber" id="phoneNumber" required="true" /></td>
	            	</tr>
	            	
	            	<tr>
	            		<th>微信号</th>
	            		<td><input name="webchat" class="easyui-textbox" required="true"/></td>
	            		<th>QQ</th>
	            		<td><input name="qq" class="easyui-validatebox textbox" validtype="qq" /></td>
	            	</tr>
	            	
	            	<tr>
	            		<th colspan="2">公司LOGO</th>
	                	<td colspan="2"><input name="file" id="file" type="file" prompt="选择一个照片" /></td>
	            	</tr>
	            	
	            	<tr>
	            		<th>邮箱</th>
	            		<td><input class="easyui-validatebox textbox" validType="email" name="email" required="true"/></td>
	            		<th>官方网站</th>
	            		<td><input class="easyui-validatebox textbox" validType="url" name="officialSite" invalidMessage="url格式不正确[http://www.example.com]"/></td>
	            	</tr>
	            	
	            	<tr>
	            		<th>公司地址</th>
	            		<td><input class="easyui-textbox" name="address" required="true"/></td>
	            		<th>成立时间</th>
	            		<td><input class="textbox" name="establishDate" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly"/></td>
	            	</tr>
	            	
	            	<tr>
	            	<th >所在省</th>
	            		<td>
	            			<select name="teamProvince" id="teamProvince" class="easyui-combobox" required="true" editable="false" style="width:90%;">
	            				
	            			</select>
	            		</td>
            		<th >所在城市</th>
	            		<td>
	            			<select name="teamCity" id="teamCity" class="easyui-combobox" required="true" editable="false" style="width:90%;">
	            				
	            			</select>
	            		</td>
	            	</tr>
	            	
	            	<tr>
	            		<th colspan="4">业务范围</th>
	            	</tr>
	            	
	            	<tr>
	            		<td colspan="4">
	            			<input type="checkbox" name="business" value="0" /> 广告
	            			<input type="checkbox" name="business" value="16" /> TVC
	            			<input type="checkbox" name="business" value="1"/> 宣传片
	            			<input type="checkbox" name="business" value="2"/> 真人秀
	            			<input type="checkbox" name="business" value="3"/> 纪录片
	            			<input type="checkbox" name="business" value="4"/> 病毒视频
	            			<input type="checkbox" name="business" value="5"/> 电视栏目
	            			<input type="checkbox" name="business" value="17"/> MV
	            			<input type="checkbox" name="business" value="6"/> 三维动画
	            			<input type="checkbox" name="business" value="7"/> MG动画
	            			<input type="checkbox" name="business" value="8"/> 体育赛事
	            			<input type="checkbox" name="business" value="9"/> 专题片
	            			<input type="checkbox" name="business" value="10"/> VR拍摄
	            			<input type="checkbox" name="business" value="11"/> 产品拍摄
	            			<input type="checkbox" name="business" value="12"/> 微电影
	            			<input type="checkbox" name="business" value="13"/> 航拍
	            			<input type="checkbox" name="business" value="14"/> 活动视频
	            			<input type="checkbox" name="business" value="15"/> 后期制作
	            			<input type="checkbox" name="business" value="18"/> 包装
	            		</td>
	            	</tr>
	            	
	            	<tr>
	            		<th>价格区间</th>
	            		<td>
	            			<select name="priceRange" class="easyui-combobox" editable="false" required="true">
	            				<option value="0" selected>看情况</option>
	            				<option value="1" >1万元及以上</option>
	            				<option value="2" >2万元及以上</option>
	            				<option value="3" >3万元及以上</option>
	            				<option value="4" >5万元及以上</option>
	            				<option value="5" >10万元及以上</option>
	            			</select>
	            		</td>
	            		
	            		<th>信息来源</th>
	            		<td>
	            			<select name="infoResource" class="easyui-combobox" editable="false" required="true">
	            				<option value="0" selected>友情推荐</option>
	            				<option value="1" >网络搜索</option>
	            				<option value="2" >拍片帮</option>
	            				<option value="3" >拍片网</option>
	            				<option value="4" >电销</option>
	            			</select>
	            		</td>
	            	</tr>
	            	
	            	<tr>
	            		<th colspan="4">公司规模</th>
	            	</tr>
	            	
	            	<tr>
	            		<td colspan="4">
	            			<input class="easyui-textbox" name="scale" multiline="true" style="height: 100px;width: 92%;" prompt="坐班人数及坐班导演或合作导演,坐班后期" required="true"/>
	            		</td>
	            	</tr>
	            	
	            	<tr>
	            		<th colspan="4">主要客户/作品及价格</th>
	            	</tr>
	            	
	            	<tr>
	            		<td colspan="4">
	            			<input class="easyui-textbox text-area" name="businessDesc" multiline="true" style="height: 150px;width: 92%;" prompt="请填写贵公司主要客户/作品以及价格" />
	            		</td>
	            	</tr>
	            	
	            	<tr>
	            		<th colspan="4">对客户的需求</th>
	            	</tr>
	            	
	            	<tr>
	            		<td colspan="4">
	            			<input class="easyui-textbox text-area" name="demand" multiline="true" style="height: 100px;width: 92%;" prompt="在此填写对客户的要求" />
	            		</td>
	            	</tr>
	            	
	            	<tr>
	            		<th colspan="4">描述</th>
	            	</tr>
	            	
	            	<tr>
	            		<td colspan="4">
							<input class="easyui-textbox text-area" name="teamDescription" multiline="true" style="height: 100px;width: 92%;" prompt="在此填写对公司的描述"/>
	            		</td>
	            	</tr>
	            	
	            	<tr>
	            		<th colspan="4">备注</th>
	            	</tr>
	            	
	            	<tr>
	            		<td colspan="4">
	            			<input class="easyui-textbox text-area" name="description" multiline="true" style="height: 100px;width: 92%;" prompt="在此填写备注信息"/>
	            		</td>
	            	</tr>
	            	
	            	<tr>
	            		<th colspan="4">审核意见</th>
	            	</tr>
	            	
	            	<tr>
	            		<td colspan="4">
	            			<input class="easyui-textbox text-area" name="recommendation" multiline="true" style="height: 100px;width: 92%;" prompt="在此填写审核意见"/>
	            		</td>
	            	</tr>
	            	
	            </table>
	        </form>
	    </div>
	    
	    <div id="dlg-buttons">
	    
	    	<r:mulparampermission uri2="/portal/team/save" uri="/portal/team/update">
		        <a id="save-btn" onclick="saveFuc();" href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok"  >保存</a>
	    	</r:mulparampermission>
	    	
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" >取消</a>
	    </div>
		<%-- image/video show content begin --%>
		<div id="picture-condition" class="picture-condition hide">
			<div class="picture-modalDialog">
				<div class="picture-condition-body">
					<div class="operation-panel">
						<img id="teamPicture" src="" style="height: 350px;width: 500px" >
						<div class="p-label">
							<a href="#" class="button p-submit" id="p-cancel">取消</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<%-- image/video show content end --%>
		
		
	<div id="recommend-dlg" class="easyui-dialog" style="width:520px; height:480px;padding:10px 20px"
           closed="true" buttons="#recommend-dlg-buttons" title="首页推荐">
           
        	<input id="search-recommend-teamName" name="recommend-teamName" placeholder="请输入供应商名称"/>
        	<a href="javascript:void(0);" id='add-recommend' class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="">添加</a>
			<table id="recommend-gride" data-options="fit:true,border:false"></table>
    </div>
     <div id="recommend-dlg-buttons">
    </div>
</body>
</html>