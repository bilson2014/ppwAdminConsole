var editing ; //判断用户是否处于编辑状态
var formUrl;
var datagrid;
var sessionId;
var editor;
$.base64.utf8encode = true;
editorBeReady("videoDescription");
$().ready(function(){
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/mail/list',
		idField : 'id' ,
		title : '邮件管理列表' ,
		//fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'subject',
						title : '邮件标题',
						width : 150,
						align : 'center' ,
					},{
						field : 'type',
						title : '邮件类型',
						width : 60,
						align : 'center' ,
					},{
						field : 'createTime' ,
						title : '创建时间' ,
						align : 'center' ,
						width : 200,
						sortable : true ,
					},{
						field : 'updateTime' ,
						title : '更新时间' ,
						align : 'center' ,
						width : 200,
					}]] ,
		pagination: true ,
		pageSize : 50,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar',
	});
	product.initData();
});
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
	// 新增时，首先获取SESSIONID
	loadData(function(pData){
		
		sessionId = pData.sessionId;
		$('input[name="sessionId"]').val(sessionId);
		
		openDialog(null);
		
	}, getContextPath() + '/portal/product/sessionId', null);
	
	$('input[name="productId"]').val(0);
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
		width : 520,
		height : 500,
		onOpen : function(event, ui) {
			
			$('#teamId').combobox({
				url : getContextPath() + '/portal/product/init',
				valueField : 'teamId',
				textField : 'teamName'
			});
			
			$('#productType').combobox({
				url : getContextPath() + '/portal/item/itemSelect',
				valueField : 'itemId',
				textField : 'itemName'
			});
			
			if(data == null){
				$('#productType').combobox('setValue','');
				$('#teamId').combobox('setValue','');
			}else {
				$('#productType').combobox('setValue',data.productType);
				$('#teamId').combobox('setValue',data.teamId);
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
