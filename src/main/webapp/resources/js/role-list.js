var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var resourceTree;
var datagrid;
$().ready(function(){
		
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/role/list',
		idField : 'roleId' ,
		title : '角色列表' , 
		fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'roleName',
						title : '角色名',
						width : 120,
						align : 'center',
						editor : {
							type : 'validatebox' ,
							options : {
								required : true , 
								missingMessage : '请填写角色名!'
							}
						}
					},{
						field : 'updateDate' ,
						title : '更新时间' ,
						width : 200,
						align : 'center'
					},{
						field : 'roleDescription',
						title : '描述',
						width : 100,
						align : 'center',
						editor : {
							type : 'validatebox' ,
							options : {
								required : false
							}
						}
					},{
						field : 'action',
						title : '操作',
						width : 120,
						formatter : function(value, row, index) {
							var str = '&nbsp;';
								str += $.formatString('<a href="javascript:void(0)" onclick="grantFun(\'{0}\');" >授权</a>', row.roleId);
							return str;
						}
					}]],
		pagination: true ,
		pageSize : 50,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar',
		onAfterEdit:function(index , record){
			delete record.rights;
			delete record.employees;
			$.post(flag =='add' ? getContextPath() + '/portal/role/add' : getContextPath() + '/portal/role/update', record , function(result){
				
				// 刷新数据
				datagrid.datagrid('clearSelections');
				datagrid.datagrid('reload');
				$.message('操作成功!');
			});
		}
	});
});


// 增加
function addFuc(){
	if(editing == undefined){
		flag = 'add';
		//1 先取消所有的选中状态
		datagrid.datagrid('unselectAll');
		//2追加一行
		datagrid.datagrid('appendRow',{});
		//3获取当前页的行号
		editing = datagrid.datagrid('getRows').length -1;
		//4开启编辑状态
		datagrid.datagrid('beginEdit', editing);
	}
}

// 修改
function editFuc(){
	var arr = datagrid.datagrid('getSelections');
	if(arr.length != 1){
		$.messager.show({
			title:'提示信息',
			msg:'只能选择一条记录进行修改!'
		});
	} else {
		if(editing == undefined){
			flag = 'edit';
			//根据行记录对象获取该行的索引位置
			editing = datagrid.datagrid('getRowIndex' , arr[0]);
			//开启编辑状态
			datagrid.datagrid('beginEdit',editing);
		}
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
					ids += arr[i].roleId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/role/delete', {ids:ids},function(result){
					// 加载数据
					// 获取当前页码
					datagrid.datagrid('reload');
					
					$.messager.show({
						title:'提示信息',
						msg:'操作成功!'
					});
				});
			} else {
				 return ;
			}
		});
	}
}

// 保存
function saveFuc(){
	//保存之前进行数据的校验 , 然后结束编辑并释放编辑状态字段 
	datagrid.datagrid('beginEdit', editing);
	if(datagrid.datagrid('validateRow',editing)){
		datagrid.datagrid('endEdit', editing);
		editing = undefined;
	}
}

// 取消
function cancelFuc(){
	//回滚数据 
	datagrid.datagrid('rejectChanges');
	editing = undefined;
}

// 授权
function grantFun(id){
	$('#roleGrantForm').form('clear');
	$('#role-id').val(id);
	openDialog('dlg',id);
}

// 打开dialog
function openDialog(id,roleId){
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {
			resourceTree = $('#resourceTree').tree({
				url : getContextPath() + '/portal/right/tree',
				parentField : 'pid',
				lines : true,
				checkbox : true,
				panelHeight : '200',
				onClick : function(node) {
				},
				onLoadSuccess : function(node, data) {
					
					if(roleId == undefined){
						var rows = dataGrid.datagrid('getSelections');
						roleId = rows[0].roleId;
					}
					
					loadData(function(list){
						$.each(list,function(i,rightId){
							if(rightId != null && rightId != '' && rightId != undefined){
								if (resourceTree.tree('find', rightId)) {
									resourceTree.tree('check', resourceTree.tree('find', rightId).target);
								}
							}
						});
					}, getContextPath() + '/portal/role/getRights/' + roleId, null);
					
				},
				cascadeCheck : false
			});
			
		}
	}).dialog('open').dialog('center');
}

//全选
function checkAll(){
	var nodes = resourceTree.tree('getChecked', 'unchecked');
	if (nodes && nodes.length > 0) {
		for ( var i = 0; i < nodes.length; i++) {
			resourceTree.tree('check', nodes[i].target);
		}
	}
}

// 反选
function checkInverse() {
	var unchecknodes = resourceTree.tree('getChecked', 'unchecked');
	var checknodes = resourceTree.tree('getChecked');
	if (unchecknodes && unchecknodes.length > 0) {
		for ( var i = 0; i < unchecknodes.length; i++) {
			resourceTree.tree('check', unchecknodes[i].target);
		}
	}
	if (checknodes && checknodes.length > 0) {
		for ( var i = 0; i < checknodes.length; i++) {
			resourceTree.tree('uncheck', checknodes[i].target);
		}
	}
}

// 取消
function uncheckAll() {
	var nodes = resourceTree.tree('getChecked');
	if (nodes && nodes.length > 0) {
		for ( var i = 0; i < nodes.length; i++) {
			resourceTree.tree('uncheck', nodes[i].target);
		}
	}
}

// 授权
function grantRight(){
	
	$('#roleGrantForm').form('submit',{
		url : getContextPath() + '/portal/role/grant',
		onSubmit : function() {
			var isValid = $(this).form('validate');
			var checknodes = resourceTree.tree('getChecked');
			var ids = [];
			if (checknodes && checknodes.length > 0) {
				for ( var i = 0; i < checknodes.length; i++) {
					ids.push(checknodes[i].id);
				}
			}
			$('#resourceIds').val(ids);
			return isValid;
		},
		success : function(result) {
			$('#dlg').dialog('close');
			datagrid.datagrid('reload');
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
