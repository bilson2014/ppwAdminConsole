var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var formUrl;
var datagrid;
$().ready(function(){
		
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath()+ '/portal/employee/list',
		idField : 'employeeId' ,
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
						field : 'employeeLoginName',
						title : '登录名',
						width : 60,
						align : 'center'
					},{
						field : 'employeeRealName',
						title : '姓名',
						width : 80,
						align : 'center'
					},{
						field : 'phoneNumber' ,
						title : '手机号码',
						width : 120,
						align : 'center'
					},{
						field : 'email' ,
						title : '邮箱',
						width : 120,
						align : 'center'
					},{
						field : 'employeeDescription' ,
						title : '人员简介',
						width : 200,
						align : 'center'
					},{
						field : 'roleIds' ,
						title : '人员列表',
						align : 'center',
						hidden : true
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
	openDialog('dlg',null);
	formUrl = getContextPath() + '/portal/employee/save';
	$('input[name="employeeId"]').val(0);
}

// 修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		$('#fm').form('load',rows[0]);
		openDialog('dlg',rows[0].roleIds);
		formUrl = getContextPath() + '/portal/employee/update';
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
					ids += arr[i].employeeId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/employee/delete', {ids:ids},function(result){
					
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
			
			var ps = $('input[name="employeePassword"]').val().trim();
			if(ps != null && ps != '' && ps != undefined){
				
				$('input[name="employeePassword"]').val(Encrypt(ps));
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

// 打开dialog
function openDialog(id,data){
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {
			// 加载角色树
			$('#roleIds').combotree({
			    url: getContextPath() + '/portal/role/tree',
			    multiple: true,
			    required: true,
			    panelHeight : 'auto',
			    value : data
			});
			
		}
	}).dialog('open').dialog('center');
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