var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var formUrl;
var datagrid;
$().ready(function(){
		
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath()+ '/portal/manager/list',
		idField : 'managerId' ,
		title : '用户列表' , 
		fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'managerLoginName',
						title : '登录名',
						align : 'center'
					},{
						field : 'managerRealName',
						title : '姓名',
						align : 'center'
					},{
						field : 'phoneNumber' ,
						title : '手机号',
						align : 'center'
					},{
						field : 'updateDate' ,
						title : '更新时间' ,
						align : 'center'
					},{
						field : 'managerEmail' ,
						title : '邮箱',
						align : 'center'
					},{
						field : 'managerDescription' ,
						title : '人员简介',
						align : 'center',
						width: 200
					}]],
		pagination: true ,
		pageSize : 50,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar'
	});
		
});


// 增加
function addFuc(){
	$('#fm').form('clear');
	openDialog('dlg');
	formUrl = getContextPath() + '/portal/manager/save';
	$('input[name="managerId"]').val(0);
}

// 修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		$('#fm').form('load',rows[0]);
		openDialog('dlg');
		formUrl = getContextPath() + '/portal/manager/update';
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}

// 删除
function delFuc(){
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.messager.show({
			title:'提示信息',
			msg:'请选择进行删除操作!'
		});
	} else {
		$.messager.confirm('提示信息' , '确认删除?' , function(r){
			if(r){
				var ids = '';
				for(var i = 0 ; i < arr.length ; i++){
					ids += arr[i].managerId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/manager/delete', {ids:ids},function(result){
					
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
	
	$('#fm').form('submit',{
		url : formUrl,
		onSubmit : function() {
			
			var ps = $('input[name="managerPassword"]').val().trim();
			if(ps != null && ps != '' && ps != undefined){
				
				$('input[name="managerPassword"]').val(Encrypt(ps));
			}
			
			var flag = $(this).form('validate');
			if(!flag){
				progressClose();
			}
			return flag;
		},
		success : function(result) {
			$('#dlg').dialog('close');
			datagrid.datagrid('reload');
			progressClose();
			$.message('操作成功!');
		}
	});
}

//查询
function searchFun(){
	datagrid.datagrid('load', $.serializeObject($('#searchForm')));
}

// 清除
function cleanFun() {
	$('#searchForm').form('clear');
	datagrid.datagrid('load', {});
}

function openDialog(id){
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {
			// do something here
		}
	}).dialog('open').dialog('center');
}