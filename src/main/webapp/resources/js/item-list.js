var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var datagrid;
var formUrl;
$().ready(function(){

	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/item/list',
		idField : 'itemId' ,
		title : '项目类型列表' , 
		fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'itemName',
						title : '类型名称',
						width : 150,
						align : 'center' ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : true , 
								missingMessage : '请填写类型名称!'
							}
						}
					},{
						field : 'od',
						title : '排序',
						width : 40,
						align : 'center' ,
						sortable : true ,
						editor : {
							type : 'validatebox' ,
							options : {
								required:false ,
								missingMessage : '请填写联系电话!'
							}
						}
					},{
						field : 'isActive' ,
						title : '活动类型' ,
						align : 'center' ,
						width : 60,
						sortable : true ,
						formatter : function(value , record , index){
							if(value == 0){
								return '<span style=color:blue; >否</span>' ;
							} else if( value == 1){
								return '<span style=color:red; >是</span>' ; 
							}
						},
						editor:{
							type:'combobox' , 
							options:{
								data:[{id:0 , val:'否'},{id:1 , val:'是'}] ,
								valueField:'id' , 
								textField:'val' ,
								required:true , 
								editable : false
							}
						}
					},{
						field : 'itemDescription' ,
						title : '描述' ,
						align : 'center' ,
						width : 200,
						sortable : true ,
						editor : {
							type : 'validatebox' ,
							options : {
								required:false ,
								missingMessage : '请填写联系电话!'
							}
						}
					}]],
		pagination: true ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar'
	});
	
	item.dataInit();
});

var item = {
	dataInit : function(){
		$('#search-itemName').combobox({
			url : getContextPath() + '/portal/item/itemSelect',
			valueField : 'itemId',
			textField : 'itemName'
		});
	}
}

//增加
function addFuc(){
	openDialog('dlg');
	$('#fm').form('clear');
	formUrl = getContextPath() + '/portal/item/save';
}

//修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		$('#fm').form('load',rows[0]);
		formUrl = getContextPath() + '/portal/item/update';
		openDialog('dlg');
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}

//删除
function delFuc(){
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.message('请选择进行删除操作!');
	} else {
		$.messager.confirm('提示信息' , '确认删除?' , function(r){
			if(r){
				var ids = '';
				for(var i = 0 ; i < arr.length ; i++){
					ids += arr[i].itemId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/item/delete', {ids:ids},function(result){
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

//保存
function saveFuc(){
	progressLoad();
	$('#fm').form('submit',{
		url : formUrl,
		success : function(result) {
			$('#dlg').dialog('close');
			datagrid.datagrid('clearSelections');
			datagrid.datagrid('reload');
			progressClose();
			$.message('操作成功!');
		}
	});
}

// 取消
function cancelFuc(){
	//回滚数据 
	datagrid.datagrid('rejectChanges');
	editing = undefined;
}

// 查询
function searchFun(){
	datagrid.datagrid('load', $.serializeObject($('#searchForm')));
}

//清除
function cleanFun() {
	$('#searchForm').form('clear');
	datagrid.datagrid('load', {});
}

function openDialog(id){
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {}
	}).dialog('open').dialog('center');
}