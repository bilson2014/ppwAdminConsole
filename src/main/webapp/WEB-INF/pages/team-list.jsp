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
	
	<div data-options="region:'north',border:false" style="height: 100px; overflow: hidden;background-color: #fff">
			<form id="searchForm" method="post">
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
						<th>价格区间:</th>
						<td>
							<select id="search-price" name="priceRange">
								<option value="" selected="selected">-- 请选择 --</option>
	            				<option value="8" >5千以内</option>
	            				<option value="7" >5千-1万</option>
	            				<option value="6" >1-3万</option>
	            				<option value="5" >3-5万</option>
	            				<option value="4" >5-10万</option>
	            				<option value="3" >10-20万</option>
	            				<option value="2" >20-50万</option>
	            				<option value="1" >50万以上</option>
							</select>
						</td>
					
					</tr>
					<tr>
						<th>联系人:</th>
						<td><input id="search-linkman" name="linkman" placeholder="请输入联系人"/></td>
						<th>所在省：</th>
						<td><input id="search-provinceID" name="provinceID" placeholder="请输入省"/></td>
						
						<th>所在市：</th>
						<td><input id="search-cityID" name="cityID" placeholder="请输入市"/></td>
						
						<th>业务范围:</th>
						<td>
							<select id="search-business" name="business" style="width:180px"></select>
							<div id="sp">
								<div style="padding:10px">
									<input type="checkbox" name="business" value="宣传片"/> <span>宣传片</span>
									<input type="checkbox" name="business" value="活动视频"/> <span>活动视频</span>
									<input type="checkbox" name="business" value="病毒视频"/> <span>病毒视频</span>
									<input type="checkbox" name="business" value="微电影"/> <span>微电影</span>
									<input type="checkbox" name="business" value="MG动画"/> <span>MG动画</span>
									<input type="checkbox" name="business" value="广告TVC" /> <span>广告TVC</span>
			            			<input type="checkbox" name="business" value="真人秀节目"/> <span>真人秀节目</span>
			            			<input type="checkbox" name="business" value="VR视频"/> <span>VR视频</span>
			            			<input type="checkbox" name="business" value="教学视频"/> <span>教学视频</span>
			            			<input type="checkbox" name="business" value="网络节目"/> <span>网络节目</span>
			            			<input type="checkbox" name="business" value="网络剧"/> <span>网络剧</span>
			            			<input type="checkbox" name="business" value="网络电影"/> <span>网络电影</span>
			            			<input type="checkbox" name="business" value="mv"/> <span>mv</span>
			            			<input type="checkbox" name="business" value="实验影片"/> <span>实验影片</span>
			            			<input type="checkbox" name="business" value="电影预告片"/> <span>电影预告片</span>
			            			<input type="checkbox" name="business" value="花絮"/> <span>花絮</span>
			            			<input type="checkbox" name="business" value="演播室栏目"/> <span>演播室栏目</span>
			            			<input type="checkbox" name="business" value="真人秀节目"/> <span>真人秀节目</span>
			            			<input type="checkbox" name="business" value="综艺节目"/> <span>综艺节目</span>
			            			<input type="checkbox" name="business" value="访谈"/> <span>访谈</span>
			            			<input type="checkbox" name="business" value="三维建筑漫游动画"/> <span>三维建筑漫游动画</span>
			            			<input type="checkbox" name="business" value="三维产品动画"/> <span>三维产品动画</span>
			            			<input type="checkbox" name="business" value="三维角色动画"/> <span>三维角色动画</span>
			            			<input type="checkbox" name="business" value="三维特效影片"/> <span>三维特效影片</span>
			            			<input type="checkbox" name="business" value="Flash角色动画"/> <span>Flash角色动画</span>
			            			<input type="checkbox" name="business" value="定格动画"/> <span>定格动画</span>
			            			<input type="checkbox" name="business" value="二维逐帧动画"/> <span>二维逐帧动画</span>
			            			<input type="checkbox" name="business" value="博物馆视频"/> <span>博物馆视频</span>
			            			<input type="checkbox" name="business" value="展览视频"/> <span>展览视频</span>
			            			<input type="checkbox" name="business" value="体验馆视频"/> <span>体验馆视频</span>
			            			<input type="checkbox" name="business" value="异形投影视频"/> <span>异形投影视频</span>
			            			<input type="checkbox" name="business" value="AR视频"/> <span>AR视频</span>		
								</div>
							</div>
									
						</td>
						
					</tr>
					<tr>
						<th>业务技能:</th>
						<td>
						<select id="search-skill" name="skill" style="width:180px"></select>
							<div id="sp-skill">
								<div style="padding:10px">
									<input type="checkbox" name="skill" value="解说词" /> <span>解说词</span>
									<input type="checkbox" name="skill" value="广告语" /> <span>广告语</span>
									<input type="checkbox" name="skill" value="故事" /> <span>故事</span>
									<input type="checkbox" name="skill" value="贴图分镜" /> <span>贴图分镜</span>
									<input type="checkbox" name="skill" value="手绘分镜" /> <span>手绘分镜</span>
									<input type="checkbox" name="skill" value="美术设计" /> <span>美术设计</span>
									<input type="checkbox" name="skill" value="影片策略" /> <span>影片策略</span>
									<input type="checkbox" name="skill" value="导演" /> <span>导演</span>
									<input type="checkbox" name="skill" value="制片" /> <span>制片</span>
									<input type="checkbox" name="skill" value="摄影" /> <span>摄影</span>
									<input type="checkbox" name="skill" value="剪辑" /> <span>剪辑</span>
									<input type="checkbox" name="skill" value="包装" /> <span>包装</span>
									<input type="checkbox" name="skill" value="调色" /> <span>调色</span>
								</div>
							</div></td>
							<th>产品线:</th>
							<td>
							<select id="search-productLine" name="productLine" style="width: 180px" class="easyui-combotree" ></select>
							</td>
							<th>公司性质：</th>
						<td>
							 <select id="search-teamNature" name="teamNature" >
								<option value="" selected="selected">-- 请选择 --</option>
								<option value="0">公司</option>
								<option value="1">工作室</option>
							</select>
						</td>
						<th></th>
						<td>
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a>
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a>
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="exportFun();">报表导出</a>
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
			
			<r:permission uri="/portal/product/listByTeam">
				<a  id="listproduct-btn" onclick="getProductsFuc();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'">作品列表</a>
			</r:permission>
			
			<a  id="upload-btn" onclick="uploadFile();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">上传资质</a>
			
		</div>
		
		<div id="dlg" class="easyui-dialog" style="width:520px; height:480px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons" title="人员信息">
	        <form id="fm" method="post" enctype="multipart/form-data">
	        	<input name="teamId" type="hidden">
	            
	            <table style="width: 98%;">
	            	<tr>
	            		<th >公司名称</th>
	            		<td><input name="teamName" class="easyui-textbox" required="true"></td>
	            		<th>公司性质</th>
	            		<td>
	            			<select id="teamNature" name="teamNature" class="easyui-combobox" style="width: 150px;height:22px;line-height: 28px;" required="true" editable="false">
							</select>
	            		</td>
	            	</tr>
	            	<tr>
	            		<th >登录名</th>
	            		<td><input name="loginName" validtype="vLoginName" class="easyui-validatebox easyui-textbox" id="loginName"></td>
	            		<!-- validtype="vLoginName" -->
	            		<th>审核状态</th>
	            		<td>
	            			<select id="flag" name="flag" class="easyui-combobox" style="width: 150px;height:22px;line-height: 28px;" required="true" editable="false">
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
	            		<td><input class="easyui-validatebox easyui-textbox" data-options="validType:['mobile','vPhoneNumber']"  name="phoneNumber" id="phoneNumber" required="true" /></td>
	            		<!-- data-options="validType:['mobile','vPhoneNumber']" -->
	            	</tr>
	            	
	            	<tr>
	            		<th>微信</th>
	            		<td><input name="webchat" class="easyui-textbox"/></td>
	            		<th>QQ</th>
	            		<td><input name="qq" class="easyui-validatebox easyui-textbox" validtype="qq" /></td>
	            	</tr>
	            	
	         <!--    	 <tr>
	            		<th colspan="2">公司LOGO</th>
	                	<td colspan="2"><input name="file" id="file" type="file" prompt="选择一个照片" required="true"/></td>
	            	</tr>  -->
	            	
	            	<tr>
	            		<th>邮箱</th>
	            		<td><input class="easyui-validatebox easyui-textbox" validType="email" name="email" required="true"/></td>
	            		<th>官网网址</th>
	            		<td><input class="easyui-validatebox easyui-textbox" validType="url" name="officialSite" invalidMessage="url格式不正确[http://www.example.com]"/></td>
	            	</tr>
	            	
	            	<tr>
	            		<th>详细地址</th>
	            		<td><input class="easyui-textbox" name="address" required="true"/></td>
	            		<th>从业时间</th>
	            		<td><input  name="establishDate" type="text" class="easyui-datebox" required="true"/></td>
	            		
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
	            		<th >座机</th>
	            		<td ><input class="easyui-textbox"  name="telNumber"/></td>
	            	</tr>
	            	<!-- <tr>
	            	<th id="certName" colspan="2">营业执照</th>
	            	<td colspan="2"><input name="certificateFile" id="certificateFile" type="file" prompt="选择一个照片" /></td>
	            	</tr>
	            	<tr>
	            	<th colspan="2">法人持身份证正面</th>
	            	<td colspan="2"><input name="idCardfrontFile" id="idCardfrontFile" type="file" prompt="选择一个照片" /></td>
	            	</tr>
	            	<tr>
	            	<th colspan="2">法人持身份证背面</th>
	            	<td colspan="2"><input name="idCardbackFile" id="idCardbackFile" type="file" prompt="选择一个照片" /></td>
	            	</tr> -->
	            	
	            	<tr>
	            		<th colspan="4">业务范围</th>
	            	</tr>
	            	
	            	<tr>
	            		<td colspan="4">
	            			<input type="checkbox" name="business" value="宣传片"/> 宣传片
							<input type="checkbox" name="business" value="活动视频"/> 活动视频
							<input type="checkbox" name="business" value="病毒视频"/> 病毒视频
							<input type="checkbox" name="business" value="微电影"/> 微电影
							<input type="checkbox" name="business" value="MG动画"/> MG动画
							<input type="checkbox" name="business" value="广告TVC" /> 广告TVC
			            	<input type="checkbox" name="business" value="真人秀节目"/> 真人秀节目
			            	<input type="checkbox" name="business" value="VR视频"/> VR视频
			            	<input type="checkbox" name="business" value="教学视频"/> 教学视频
			            	<input type="checkbox" name="business" value="网络节目"/> 网络节目
			            	<input type="checkbox" name="business" value="网络剧"/> 网络剧
			            	<input type="checkbox" name="business" value="网络电影"/> 网络电影
			            	<input type="checkbox" name="business" value="mv"/> mv
			            	<input type="checkbox" name="business" value="实验影片"/> 实验影片
			            	<input type="checkbox" name="business" value="电影预告片"/> 电影预告片
			            	<input type="checkbox" name="business" value="花絮"/> 花絮
			            	<input type="checkbox" name="business" value="演播室栏目"/> 演播室栏目
			            	<input type="checkbox" name="business" value="真人秀节目"/> 真人秀节目
			            	<input type="checkbox" name="business" value="综艺节目"/> 综艺节目
			            	<input type="checkbox" name="business" value="访谈"/> 访谈
			            	<input type="checkbox" name="business" value="三维建筑漫游动画"/> 三维建筑漫游动画
			            	<input type="checkbox" name="business" value="三维产品动画"/> 三维产品动画
			            	<input type="checkbox" name="business" value="三维角色动画"/> 三维角色动画
			            	<input type="checkbox" name="business" value="三维特效影片"/> 三维特效影片
			            	<input type="checkbox" name="business" value="Flash角色动画"/> Flash角色动画
			            	<input type="checkbox" name="business" value="定格动画"/> 定格动画
			            	<input type="checkbox" name="business" value="二维逐帧动画"/> 二维逐帧动画
			            	<input type="checkbox" name="business" value="博物馆视频"/> 博物馆视频
			            	<input type="checkbox" name="business" value="展览视频"/> 展览视频
			            	<input type="checkbox" name="business" value="体验馆视频"/> 体验馆视频
			            	<input type="checkbox" name="business" value="异形投影视频"/> 异形投影视频
			            	<input type="checkbox" name="business" value="AR视频"/> AR视频
	            		</td>
	            	</tr>
	            	<tr>
	            		<th colspan="2">业务技能</th><td colspan="2" id="skillMsg"></td>
	            	</tr>
	            	
	            	<tr>
	            		<td colspan="4">
	            			创意策划:
	            			<input type="checkbox" name="skill" value="解说词"/> 解说词
	            			<input type="checkbox" name="skill" value="广告语" /> 广告语
	            			<input type="checkbox" name="skill" value="故事" /> 故事
	            			<input type="checkbox" name="skill" value="贴图分镜" /> 贴图分镜
	            			<input type="checkbox" name="skill" value="手绘分镜" /> 手绘分镜
	            			<input type="checkbox" name="skill" value="美术设计" /> 美术设计
	            			<input type="checkbox" name="skill" value="影片策略" /> 影片策略	
	            			<br>		

	            			创作团队:
	            			<input type="checkbox" name="skill" value="导演" /> 导演
	            			<input type="checkbox" name="skill" value="制片" /> 制片
	            			<input type="checkbox" name="skill" value="摄影" /> 摄影
	            			<br>		

	            			后期制作:
	            			<input type="checkbox" name="skill" value="剪辑" /> 剪辑
	            			<input type="checkbox" name="skill" value="包装" /> 包装
	            			<input type="checkbox" name="skill" value="调色" /> 调色		
	            		</td>
	            	 </tr>
	            	 <tr>
	            	 <th colspan="4"><a onclick="addProductLine()" id="add-Module" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加产品线</a></th>
	            	 </tr>
	            	 <tr>
	            	 
	            	 <td colspan="4">
	            	 	<div class="productLineModule"> 					
						</div>
					<hr/>
	            	 </td>
	            	 
				
				</tr>
	            	
	            	<tr> 
	            		<th>价格区间</th>
	            		<td>
	            			<select name="priceRange" class="easyui-combobox" editable="false" required="true">
	            				<option value="8" >5千以内</option>
	            				<option value="7" >5千-1万</option>
	            				<option value="6" >1-3万</option>
	            				<option value="5" >3-5万</option>
	            				<option value="4" >5-10万</option>
	            				<option value="3" >10-20万</option>
	            				<option value="2" >20-50万</option>
	            				<option value="1" >50万以上</option>
	            			</select>
	            		</td>
	            		
	            		<!-- <th>信息来源</th>
	            		<td>
	            			<select name="infoResource" class="easyui-combobox" editable="false" required="true">
	            				<option value="0" selected>友情推荐</option>
	            				<option value="1" >网络搜索</option>
	            				<option value="2" >拍片帮</option>
	            				<option value="3" >拍片网</option>
	            				<option value="4" >电销</option>
	            			</select>
	            		</td> -->
	            		<th>规模</th>
	            		<td><select class="easyui-combobox" name="scale" editable="false" required="true">
	            				<option value="0">5人以下</option>
								<option value="1">5-10人</option>
								<option value="2">11-50人</option>
								<option value="3">51-100人</option>
								<option value="4">101-500人</option>
								<option value="5">500人以上</option>
	            			</select></td>
	            	</tr>
	            	
	            	<!-- <tr>
	            		<th colspan="1">公司规模</th>
	            		<td><select class="easyui-combobox" name="scale" editable="false" required="true">
	            				<option value="0">5人以下</option>
								<option value="1">5-10人</option>
								<option value="2">11-50人</option>
								<option value="3">51-100人</option>
								<option value="4">101-500人</option>
								<option value="5">500人以上</option>
	            			</select></td>
	            	</tr> -->
	           
	            	
	            	<!-- <tr>
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
	            	</tr> -->
	            	
	            	<tr>
	            		<th colspan="4">公司简介</th>
	            	</tr>
	            	
	            	<tr>
	            		<td colspan="4">
							<input class="easyui-textbox text-area" name="teamDescription" multiline="true" style="height: 100px;width: 92%;" prompt="在此填写对公司的描述" required="true"/>
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
		<%-- image/video show content begin 
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
		 image/video show content end --%>
		
		
	<div id="recommend-dlg" class="easyui-dialog" style="width:520px; height:480px;padding:10px 20px"
           closed="true" buttons="#recommend-dlg-buttons" title="首页推荐">
           
        	<input id="search-recommend-teamName" name="recommend-teamName" placeholder="请输入供应商名称"/>
        	<a href="javascript:void(0);" id='add-recommend' class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="">添加</a>
			<table id="recommend-gride" data-options="fit:true,border:false"></table>
    </div>
    <div id="product-dlg" class="easyui-dialog" style="width:620px; height:530px;padding:10px 20px"
           closed="true" title="作品列表">
			<table id="product-gride" data-options="fit:true,border:false"></table>
			
			<!-- video show content begin-->
		<div id="video-condition" class="picture-condition hide">
			<div class="video-modalDialog">
				<div class="video-condition-body">
					<div class="video-operation-panel">					
						<video autoplay="autoplay" style="height: 270px;width: 480px" id="productVideo" preload="auto" class="hide" controls="controls" src=""></video>						
						<div class="video-p-label">
							<a href="#" class="button p-submit" id="v-cancel">取消</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- video show content end-->
    </div>


	<div id="upload-dlg" class="easyui-dialog"
		style="width: 620px; height: 530px; padding: 10px 20px" closed="true"
		buttons="#apt-buttons" title="上传资质">
		<form id="af" method="post" enctype="multipart/form-data">
			<input name="teamId" type="hidden">
			<table style="width: 100%;"  border="0" cellspacing="1" >
				<tr>
					<th >公司LOGO</th>
					<td><img src="" id="fileImg" class="aptimg"></td>
					
					<td><input name="file" id="file" type="file"
						onchange="changeImg(this)" /></td>

				</tr>
				<tr>
					<th id="certName" >营业执照</th>
					<td><img src="" id="certificateFileImg" class="aptimg"></td>
					
					<td><input name="certificateFile" id="certificateFile"
						type="file" onchange="changeImg(this)" /></td>
				</tr>
				<tr>
					<th >法人持身份证正面</th>
					<td><img src="" id="idCardfrontFileImg" class="aptimg"></td>
					
					<td><input name="idCardfrontFile" id="idCardfrontFile"
						type="file" onchange="changeImg(this)" /></td>
				</tr>
				<tr>
					<th >法人持身份证背面</th>
					<td><img src="" id="idCardbackFileImg" class="aptimg"></td>
					
					<td><input name="idCardbackFile" id="idCardbackFile"
						type="file" onchange="changeImg(this)" /></td>
				</tr>
				
			</table>
		</form>

		<div id="picture-condition" class="picture-condition hide">
			<div class="picture-modalDialog">
				<div class="picture-condition-body">
					<div class="operation-panel">
						<img id="teamPicture" src=""
							style="height: 350px; width: 500px">
						<div class="p-label">
							<a href="#" class="button p-submit" id="p-cancel">取消</a>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<div id="apt-buttons">

		<a id="saveapt-btn" onclick="saveAptFuc();" href="javascript:void(0)"
			class="easyui-linkbutton c6" iconCls="icon-ok">保存</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel"
			onclick="javascript:$('#upload-dlg').dialog('close')">取消</a>
	</div>

	<div id="recommend-dlg-buttons"></div>
</body>
</html>