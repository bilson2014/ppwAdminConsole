var editing ; //判断用户是否处于编辑状态
var formUrl;
var datagrid;
var sessionId;
var editor;
var roleTree;
var fileItems;

$.base64.utf8encode = true;
editorBeReady("content");
$().ready(function(){
	fileItems=[{value:'projectBrief',text:'项目简报'},
		{value:'projectPlan',text:'项目排期'},
		{value:'planning',text:'策划方案'},
		{value:'planningDemo',text:'策划方案模板'},
		{value:'ppm',text:'PPM'},
		{value:'vedioRevise',text:'客户影片修改表'},
		{value:'demoUpdate',text:'会审影片修改表'},
		{value:'customerReply',text:'客户验收函回复截图'},
		{value:'priceSheet',text:'报价单'},
		{value:'autoProjectSheet',text:'项目制作单'},
		{value:'autoCpsl',text:'客户项目服务函'},
		{value:'watermark',text:'拍片网专用水印'}];
	
	roleTree=[{value:'customer',text:'项目客户'},
		{value:'teamPlan',text:'项目策划供应商'},
		{value:'teamProduct',text:'项目制作供应商'},
		{value:'sale',text:'销售（负责人）'},
		
		{value:'saleDirector',text:'销售总监'},
		{value:'creativityDirector',text:'创意总监'},
		{value:'superviseDirector',text:'监制总监'},
		{value:'teamProvider',text:'供应商管家'},
		{value:'teamPurchase',text:'供应商采购'},
		{value:'scheme',text:'策划'},
		{value:'finance',text:'财务'},
		{value:'supervise',text:'监制'},
		
		{value:'teamDirector',text:'供应商总监'},
		{value:'financeDirector',text:'财务总监'},
		{value:'customerDirector',text:'客服总监'},
		{value:'crm',text:'CRM'},
		];
	
//	syncLoadData(function(msg){
//		roleTree = msg;
//	}, getContextPath() + '/portal/role/tree2',null);
	
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
						field : 'mailType',
						title : '邮件类型',
						width : 150,
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
		items : [ 'source','undo','redo','plainpaste','wordpaste','indent','outdent','fontname', 'fontsize', 'formatblock','|', 'forecolor', 'hilitecolor',
					'bold', 'italic', 'underline', 'removeformat', '|','lineheight',
					'justifyleft', 'justifycenter', 'justifyright',
					'insertorderedlist', 'insertunorderedlist', '|',
					'emoticons', 'image', 'link', 'unlink', 'fullscreen',
					'table',   'preview' ]
	});
	
	var iframe = editor.edit.iframe.get(); //此时为iframe对象
	var iframe_body = iframe.contentWindow.document.body;
	KindEditor.ctrl(iframe_body, 'S', function() {
		var mailId=$('#mailId').val().trim();
		$.base64.utf8encode = true;
		var content= $.base64.btoa(editor.html());
		/*loadData(function(){
			ls = datagrid.datagrid('getSelections');
			ls[0].content = content;
		}, getContextPath() + '/portal/product/saveContent', $.toJSON({
			'mailId' : mailId,
			'content' : content	
		}));*/
	});
}

//增加
function addFuc(){
	$('#fm').form('clear');
	formUrl = getContextPath() + '/portal/mail/save';
	$('#receiverRole').combobox({
		data : roleTree,
		valueField : 'value',
		textField : 'text'
	});
	$('#bccRole').combobox({
		data : roleTree,
		valueField : 'value',
		textField : 'text',
		multiple:true
	});
	$('#senderRole').combobox({
		data : roleTree,
		valueField : 'value',
		textField : 'text'
	});
	$('#mailFile').combobox({
		data : fileItems,
		valueField : 'value',
		textField : 'text'
	});
	console.log(fileItems);
	openDialog(null);
	$('#mailId').val(0);
}

// 修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		if(rows[0].bccRole==null){
			rows[0].bccRole='';
		}
		if(rows[0].mailFile==null){
			rows[0].mailFile='';
		}
		$('#fm').form('load',rows[0]);
		// 回显编辑器
		$.base64.utf8encode = true;
		var html=$.trim($.base64.atob($.trim(rows[0].content),true));
		editor.html(html);
		formUrl = getContextPath() + '/portal/mail/update';
		
		$('#receiverRole').combobox({
			data : roleTree,
			valueField : 'value',
			textField : 'text',
			onLoadSuccess : function() {
				$('#receiverRole').combobox('setValue', rows[0].receiverRole);
			}
		});
		$('#bccRole').combobox({
			data : roleTree,
			valueField : 'value',
			textField : 'text',
			multiple:true
		});
		$('#senderRole').combobox({
			data : roleTree,
			valueField : 'value',
			textField : 'text',
			onLoadSuccess : function() {
				$('#senderRole').combobox('setValue', rows[0].senderRole);
			}
		});
		$('#mailFile').combobox({
			data : fileItems,
			valueField : 'value',
			textField : 'text',
			multiple:true
		});
		if(rows[0].bccRole!=null && rows[0].bccRole!=''){
			$('#bccRole').combobox('setValues',rows[0].bccRole.split(','));
		}
		if(rows[0].mailFile!=null && rows[0].mailFile!=''){
			$('#mailFile').combobox('setValues',rows[0].mailFile.split(','));
		}
		
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
					ids += arr[i].id + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/mail/delete', {ids:ids},function(result){
					
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
	var content= $.base64.btoa(editor.html());
	$('input[name="content"]').val(content);
	$('#fm').form('submit',{
		url : formUrl,
		onSubmit : function() {
			var flag = $(this).form('validate');
			if(!flag){
				progressClose();
				return flag;
			}
			if(!validate()){
				progressClose();
				return false;
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

function validate(){
	var receiver=$('#receiver').val();
	if(receiver!='' && receiver!=null){
		var receiverArray=receiver.split(',');
		var checkReceiver=true;
		for(var i = 0;i < receiverArray.length;i ++){
			checkReceiver=checkEmail(receiverArray[i]);
			if(!checkReceiver){
				$.message('请输入正确格式的抄送人：邮箱,邮箱!');
				return false;
			}
		}
	}
	var mailBcc=$('#bcc').val();
	if(mailBcc!='' && mailBcc!=null){
		var mailBccArray=mailBcc.split(',');
		var checkBcc=true;
		for(var i = 0;i < mailBccArray.length;i ++){
			checkBcc=checkEmail(mailBccArray[i]);
			if(!checkBcc){
				$.message('请输入正确格式的抄送人：邮箱,邮箱!');
				return false;
			}
		}
	}
	return true;
}

function openDialog(data){
	$('#dlg').dialog({
		title : '邮件信息',
		modal : true,
		width : 700,
		height : 500,
		onOpen : function(event, ui) {
			KindEditor.remove('input[name="content"]');
			// 打开Dialog后创建编辑器
			createEditor('input[name="content"]');
		},
		onBeforeClose: function (event, ui) {
			// 关闭Dialog前移除编辑器
			KindEditor.remove('input[name="content"]');
		}
	}).dialog('open').dialog('center');
}

function checkEmail(str){
	reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	if(str.match(reg))
		return true; 
	else
		return false;
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
