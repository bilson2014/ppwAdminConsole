var formUrl;
var datagrid;
$().ready(function(){
	
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/service/list',
		idField : 'serviceId' ,
		title : '服务管理列表' , 
		fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'serviceName',
						title : '服务名称',
						width : 200,
						align : 'center' ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : true , 
								missingMessage : '请填写服务名称!'
							}
						}
					},{
						field : 'mcoms',
						title : '影片时长',
						width : 110,
						align : 'center' ,
						formatter : function(value,row,index){
							return '<span style=color:red; >'+ row.mcoms +' 秒</span>' ;
						},
					},{
						field : 'servicePrice',
						title : '价格',
						width : 110,
						align : 'center' ,
						sortable : true ,
					},{
						field : 'serviceDiscount' ,
						title : '折扣' ,
						align : 'center' ,
						width : 70,
						sortable : true ,
					},{
						field : 'serviceRealPrice',
						title : '真实价格',
						width : 110,
						align : 'center'
					},{
						field : 'serviceOd',
						title : '排序',
						width : 70,
						align : 'center' ,
					},{
						field : 'productId',
						title : '所属项目',
						width : 170,
						align : 'center' ,
						formatter : function(value,row,index){
							return '<span style=color:red; >'+ row.productName +'</span>' ;
						},
					},{
						field : 'serviceDescription',
						title : '描述',
						width : 170,
						align : 'center' ,
					}]],
		pagination: true ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar'
	});
	service.dataInit();
});

var service = {
	dataInit : function(){
		$('#search-name').combobox({
			url : getContextPath() + '/portal/service/productSelect',
			valueField : 'productName',
			textField : 'productName',
			filter: function(q, row){
				if(row.productName == null)
					return false;
				return row.productName.indexOf(q) >= 0;
			}
		});
	}
}

//增加
function addFuc(){
	$('#fm').form('clear');
	openDialog('dlg',null);
	formUrl = getContextPath() + '/portal/service/save';
	$('input[name="serviceId"]').val(0);
}
//修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		openDialog('dlg',rows[0]);
		$('#fm').form('load',rows[0]);
		
		formUrl = getContextPath() + '/portal/service/update';
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
					ids += arr[i].serviceId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/service/delete', {ids:ids},function(result){
					
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
		onSubmit : function(param) {
			var flag = $(this).form('validate');
			if(!flag){
				progressClose();
				$.message('请将信息填写完整!');
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
//打开dialog
function openDialog(id,data){
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {
			// 加载角色树
			$('#priceDetail').combotree({
				url : getContextPath() + '/portal/module/list',
				parentField : 'pid',
				lines : true,
			    idField : 'id',
				treeField : 'text',
				multiple: true
			});
			$("#productId").combobox({
				url : getContextPath() + '/portal/service/productSelect',
				valueField:'productId',
				textField:'productName',
				filter: function(q, row){
					if(row.productName == null)
						return false;
					return row.productName.indexOf(q) >= 0;
				}
			});
			
		}
	}).dialog('open').dialog('center');
}