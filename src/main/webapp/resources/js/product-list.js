var editing ; //判断用户是否处于编辑状态
var formUrl;
var datagrid;
var recommend_datagrid;
var editor;
$.base64.utf8encode = true;
editorBeReady("videoDescription");
$().ready(function(){
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/product/list',
		idField : 'productId' ,
		title : '项目管理列表' ,
		//fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'productName',
						title : '标题',
						width : 150,
						align : 'center' ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : true , 
								missingMessage : '请填写项目名称!'
							}
						}
					},{
						field : 'checkDetails' ,
						title : '审核详情' ,
						align : 'center' ,
						width : 100,
						hidden : true
					},{
						field : 'flag' ,
						title : '审核状态' ,
						align : 'center' ,
						width : 100,
						sortable : true ,
						formatter : function(value , record , index){
							if(value == 0){
								return '<span style=color:blue; >审核中</span>' ;
							} else if( value == 1){
								return '<span style=color:green; >审核通过</span>' ;
							} else if( value == 2){
								return '<span style=color:red; >未通过审核</span>' ;
							}
						},
						editor:{
							type:'combobox' , 
							options:{
								data:[{id:0 , val:'审核中'},{id:1 , val:'审核通过'},{id:2 , val:'未通过审核'}] ,
								valueField:'id' , 
								textField:'val' ,
								required:false , 
								editable : false
							}
						}
					},{
						field : 'visible' ,
						title : '是否可见 ',
						align : 'center' ,
						width : 60,
						sortable : true ,
						formatter : function(value , record , index){
							if(value == 0){
								return '<span style=color:green; >可见</span>' ;
							} else if( value == 1){
								return '<span style=color:red; >不可见</span>' ; 
							}
						}
					},{
						field : 'tags' ,
						title : '标签' ,
						align : 'center' ,
						width : 120,
						sortable : true ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false 
							}
						}
					},{
						field : 'teamName' ,
						title : '团队名称' ,
						align : 'center' ,
						width : 200,
						sortable : true ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false 
							}
						}
					},{
						field : 'recommend' ,
						title : '推荐值' ,
						align : 'center' ,
						width : 150,
						sortable : true ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false 
							}
						},
						formatter : function(value , record , index){
							if(value == 0){
								return '<span>未推荐</span>' ;
							} else if( value == 1){
								return '<span style=color:red; >热门爆款</span>' ; 
							} else if(value == 2){
								return '<span style=color:green; >精品案例</span>' ; 
							}else if(value > 2){
								return '<span style=color:blue; >推荐视频</span>' ; 
							}
						}
					},{
						field : 'supportCount' ,
						title : '赞值' ,
						align : 'center' ,
						width : 60,
						sortable : true ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false 
							}
						}
					},{
						field : 'hret' ,
						title : '视频外链' ,
						align : 'center' ,
						width : 60,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false 
							}
						},
						formatter : function(value , record , index){
							if(value == '' || value == undefined || value == null){
								return '<span style=color:red;>无</span>' ;
							} else{
								return '<span style=color:blue;>有</span>' ; 
							}
						}
					},{
						field : 'uploadDate' ,
						title : '上传时间' ,
						align : 'center' ,
						width : 150,
						sortable : true ,
					},{
						field : 'creationTime' ,
						title : '创作时间' ,
						align : 'center' ,
						width : 150
					},{
						field : 'submitTime' ,
						title : '提交审核时间' ,
						align : 'center' ,
						width : 150,
						sortable : true ,
					},{
						field : 'picLDUrl' ,
						title : '封面' ,
						align : 'center' ,
						width : 180,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false 
							}
						}
					},{
						field : 'pDescription' ,
						title : '描述' ,
						align : 'center' ,
						width : 200,
						hidden : true,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false 
							}
						}
					},{
						field : 'videoUrl' ,
						title : '视频连接' ,
						align : 'center' ,
						width : 180,
						hidden : true,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false 
							}
						}
					},{
						field : 'videoLength' ,
						title : '视频长度' ,
						align : 'center' ,
						width : 80,
						hidden : true,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false 
							}
						}
					},{
						field : 'teamId' ,
						title : '团队编号' ,
						align : 'center' ,
						width : 80,
						hidden : true ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false 
							}
						}
					},{
						field : 'productId' ,
						title : '作品链接' ,
						align : 'center' ,
						width : 280,
						hidden : true,
						formatter : function(value , record , index){
							//http://www.apaipian.com/play/16_659.html
							return 'http://www.apaipian.com/play/'+record.teamId + '_' + record.productId+'.html';
						}
					},{
						field : 'masterWork' ,
						title : '代表作' ,
						align : 'center' ,
						width : 100,
						formatter : function(value , record , index){
							if(value == 0){
								return '<span style=color:bule; >否</span>' ; 
							} else if( value == 1){
								return '<span style=color:green; >是</span>' ; 
							} 
						}
					}]],
		pagination: true ,
		pageSize : 50,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar',
		onDblClickCell:function(index,field,value){
			if(field == 'productName'){
				var row = $('#gride').datagrid('getData').rows[index];
				// video
				$('#picture-condition').removeClass('hide');
				$('#productVideo').removeClass('hide');
				$('#productPicture').addClass('hide');
				$('#youku-player').hide();
				$('#productVideo').show('fast');
				var videoPath = getDfsHostName() +  row.videoUrl;
				destroyPlayer('youku-player');// 销毁优酷播放器
				$('#productVideo').attr('src',videoPath);
				$('#p-cancel').on('click',function(){
					$('#picture-condition').addClass('hide');
					$('#productVideo').attr('src','');
					$('#productPicture').attr('src','');
					$('#youku-player').hide('fast');
				});
			}else if(field == 'picLDUrl'){
				$('#picture-condition').removeClass('hide');
				$('#productPicture').removeClass('hide');
				$('#productVideo').addClass('hide');
				$('#youku-player').hide('fast');
				$('#productVideo').hide('fast');
				var imgPath = getDfsHostName() + value;
				destroyPlayer('youku-player');// 销毁优酷播放器
				$('#productPicture').attr('src',imgPath);
				
				$('#p-cancel').on('click',function(){
					$('#picture-condition').addClass('hide');
					$('#productVideo').attr('src','');
					$('#productPicture').attr('src','');
					$('#youku-player').hide('fast');
				});
			} else if(field == 'hret'){
				// 外链
				$('#picture-condition').removeClass('hide');
				$('#productVideo').removeClass('hide');
				$('#productPicture').addClass('hide');
				$('#productVideo').hide('fast');
				$('#youku-player').show('fast');
				destroyPlayer('youku-player');// 销毁优酷播放器
				makePlayer('youku-player',value);
				$('#p-cancel').on('click',function(){
					$('#picture-condition').addClass('hide');
					$('#productVideo').attr('src','');
					$('#productPicture').attr('src','');
					$('#youku-player').hide('fast');
				});
			}
		}
	});
	product.initData();
});

var product = {
	initData : function(){
		$('#search-teamName').combobox({
			url : getContextPath() + '/portal/product/init',
			//valueField : 'teamId',
			valueField : 'teamName',
			textField : 'teamName',
			filter: function(q, row){
				if(row.teamName == null)
					return false;
				return row.teamName.indexOf(q) >= 0;
			}
		});
	}
}

function editorBeReady(valueName){
	var name='input[name="'+valueName+'"]';
	KindEditor.ready(function(K) {
		createEditor(name);
	});
}

function createEditor(name){
	editor = KindEditor.create(name, {
		cssPath : getContextPath() + '/resources/lib/kindeditor/plugins/code/prettify.css',
		uploadJson : getContextPath() + '/kindeditor/uploadImage',
		zIndex : 999999,
		width : '520px',
		height : '350px',
		resizeType:0,
		allowImageUpload : true,
		items : [ 'undo','redo','plainpaste','wordpaste','indent','outdent','fontname', 'fontsize', 'formatblock','|', 'forecolor', 'hilitecolor',
					'bold', 'italic', 'underline', 'removeformat', '|',
					'justifyleft', 'justifycenter', 'justifyright',
					'insertorderedlist', 'insertunorderedlist', '|',
					'emoticons', 'image', 'link', 'unlink', 'fullscreen',
					'table',   'preview' ]
	});
	
	var iframe = editor.edit.iframe.get(); //此时为iframe对象
	var iframe_body = iframe.contentWindow.document.body;
	KindEditor.ctrl(iframe_body, 'S', function() {
		var productId=$('#productId').val().trim();
		$.base64.utf8encode = true;
		var videoDescription= $.base64.btoa(editor.html());
		loadData(function(){
			ls = datagrid.datagrid('getSelections');
			ls[0].videoDescription = videoDescription;
		}, getContextPath() + '/portal/product/saveVideoDescription', $.toJSON({
			'productId' : productId,
			'videoDescription' : videoDescription
		}));
	});
}

//增加
function addFuc(){
	$('#fm').form('clear');
	formUrl = getContextPath() + '/portal/product/save';
	openDialog(null);
	$('input[name="productId"]').val(0);
	$('input[name="visible"]').val(0);
	$('#showType').combobox('setValue','1');
}

// 修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		$('#fm').form('load',rows[0]);
		// 回显编辑器
		$.base64.utf8encode = true;
		var html=$.trim($.base64.atob($.trim(rows[0].videoDescription),true));
		editor.html(html);
		
		formUrl = getContextPath() + '/portal/product/update';
		
		openDialog(rows[0]);
		
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}

// 删除
function delFuc(){
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.message('请选择进行删除操作!');
	} else {
		$.messager.confirm('提示信息' , '确认删除?' , function(r){
			if(r){
				var ids = '';
				for(var i = 0 ; i < arr.length ; i++){
					ids += arr[i].productId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/product/delete', {ids:ids},function(result){
					
					// 刷新数据
					datagrid.datagrid('clearSelections');
					datagrid.datagrid('reload');
					$.message('操作成功!');
				});
			} else {
				 return ;
			}
		});
	}
}

// 取消
function cancelFuc(){
	//回滚数据 
	datagrid.datagrid('rejectChanges');
	editing = undefined;
}
// 确认事件
function save(){
	progressLoad();
	$.base64.utf8encode = true;
	var videoDescription= $.base64.btoa(editor.html());
	$('input[name="videoDescription"]').val(videoDescription);
	
	$('#fm').form('submit',{
		url : formUrl,
		onSubmit : function() {
			if($("#videoUrl").val()=='' && $("#videoFile").val()==''){
				$.message('请上传视频文件!');
				progressClose();
				return false;
			}
			if($("#picLDUrl").val()=='' && $("#picLDFile").val()==''){
				$.message('请上传封面!');
				progressClose();
				return false;
			}
			var flag = $(this).form('validate');
			if(!flag){
				progressClose();
			}
			return flag;
		},
		success : function(result) {
			$('#dlg').dialog('close');
			datagrid.datagrid('clearSelections');
			datagrid.datagrid('reload');
			progressClose();
			$.message('操作成功!');
		}
	});
}

function openDialog(data){
	$('#dlg').dialog({
		title : '作品信息',
		modal : true,
		width : 620,
		height : 500,
		onOpen : function(event, ui) {
			
			$('#teamId').combobox({
				url : getContextPath() + '/portal/product/init',
				valueField : 'teamId',
				textField : 'teamName'
			});
			if(data == null){
				$('#teamId').combobox('setValue','');
				$('#videoLength').textbox('setValue','0');
				$('#recommend').textbox('setValue','0');
				$('#supportCount').textbox('setValue','0');
				$('#flag').combobox('setValue','1');
			}else {
				$('#teamId').combobox('setValue',data.teamId);
				$('#videoUrl').val(data.videoUrl);
				$('#picLDUrl').val(data.picLDUrl);
			}
			
			KindEditor.remove('input[name="videoDescription"]');
			// 打开Dialog后创建编辑器
			createEditor('input[name="videoDescription"]');
		},
		onBeforeClose: function (event, ui) {
			// 关闭Dialog前移除编辑器
			KindEditor.remove('input[name="videoDescription"]');
		}
	}).dialog('open').dialog('center');
}

//查询
function searchFun(){
	//清空点击表的排序操作,例如按时间排序等
	$('#gride').datagrid('options').sortName = null;
	$('#gride').datagrid('options').sortOrder = null;
	datagrid.datagrid('load', $.serializeObject($('#searchForm')));
}

// 清除
function cleanFun() {
	$('#searchForm').form('clear');
	datagrid.datagrid('load', {});
}
//设置代表作
function setMaster(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		var productId  = $.trim(rows[0].productId);
		var teamId  = $.trim(rows[0].teamId);
		loadData(function(){
			datagrid.datagrid('clearSelections');
			datagrid.datagrid('reload');
			progressClose();
			$.message('操作成功!');
		}, getContextPath() + '/portal/set/masterWork',$.toJSON({
			productId : productId,
			teamId:teamId,
			masterWork:'1'
		}));
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}
function toPlayHtml(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length != 0){
		for(var i=0;i<rows.length;i++){
			var teamId = rows[i].teamId;
			var productId = rows[i].productId;
			window.open('http://www.apaipian.com/play/'+teamId+'_'+productId+'.html');
		}
	}else{
		$.message('请先选择作品!');
	}
}
function recommendFuc(){
	$('#recommend-dlg').dialog({
		title : '作品推荐',
		modal : true,
		width : 950,
		height : 500,
		onOpen : function(event, ui) {
		},
	}).dialog('open').dialog('center');
	// 初始化DataGrid
	recommend_datagrid = $('#recommend-gride').datagrid({
		url : getContextPath() + '/portal/product/recommend/list',
		idField : 'productId',
		title : '首页作品推荐' , 
		fit: true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		columns:[[
					{
						field : 'productName',
						title : '作品名称',
						width : 260,
						align : 'center'
					},
					{
						field : 'teamName',
						title : '供应商名称',
						width : 260,
						align : 'center'
					},
					{
						field : 'recommend',
						title : '推荐值',
						width : 180,
						align : 'center',
						formatter:function(value,row,index){
							if(row.recommend==1){
								return '<span style=color:blue; >热门爆款</span>' ;
							}
							if(row.recommend==2){
								return '<span style=color:green; >精品案例</span>' ;
							}else{
								return '<span style=color:black; >推荐视频</span>' ;
							}
						},
						editor:{
							type:'combobox' , 
							options:{
								data:[{id:1 , val:'热门爆款'},{id:2 , val:'精品案例'},{id:3 , val:'推荐视频'}] ,
								valueField:'id' , 
								textField:'val' ,
								required:true , 
								editable : false
							}
						}
					},
					{
						field : 'recommendDeal',
						width : 160,
						title : '操作',
						align : 'center',
						formatter : function(value,row,index){
							var all = "";
							var totalCount = $('#recommend-gride').datagrid('getData').total;
							var modify  = '<a class="modify-recommend" data-index="'+index+'" data-id="'+row.productId+'" href="javascript:void(0)">修改</a>';
							var save  = '<a class="save-recommend hide" data-index="'+index+'" data-id="'+row.productId+'" href="javascript:void(0)">保存</a>';
							var del = '<a class="del-recommend" data-id="'+row.productId+'" href="javascript:void(0)">移除</a>';
							return modify+" "+save+" "+del;
						}
					}
				]] ,
		pagination: true,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		onLoadSuccess:function(){
			modifyCommend();
			delCommend();
		},
		onAfterEdit:function(index , record){
			$.post(getContextPath() + '/portal/product/updateRecommend', record ,function(result){
				recommend_datagrid.datagrid('clearSelections');
				recommend_datagrid.datagrid('reload');
				$.message('操作成功!');
			});
		}
	});
}
function modifyCommend(){
	$(".modify-recommend").off("click").on("click",function(){
		var index = $(this).attr("data-index");
		//开启编辑状态
		recommend_datagrid.datagrid('beginEdit',index);
		$(this).addClass("hide")
		$(this).next().removeClass("hide");
		saveCommend();
	})
}
function saveCommend(){
	$(".save-recommend").off("click").on("click",function(){
		var index = $(this).attr("data-index");
		//保存之前进行数据的校验 , 然后结束编辑并释放编辑状态字段 
		recommend_datagrid.datagrid('beginEdit', index);
		if(recommend_datagrid.datagrid('validateRow',index)){
			recommend_datagrid.datagrid('endEdit', index);
		}
	})
}
function delCommend(){
	$(".del-recommend").off("click").on("click",function(){
		var productId = $(this).attr("data-id");
		$.messager.confirm('提示信息' , '确认删除?' , function(r){
			if(r){
				$.post(getContextPath() + '/portal/product/updateRecommend', {
					productId:productId,
					recommend:0
				} ,function(result){
					recommend_datagrid.datagrid('reload');
					$.message('操作成功!');
				});
			}
		});
	})
}