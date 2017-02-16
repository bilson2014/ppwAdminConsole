var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var datagrid;
$().ready(function(){
	
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/indent/list',
		idField : 'id' ,
		title : '订单列表' , 
		//fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'indentName',
						title : '订单名称',
						width : 160,
						align : 'center' ,
						sortable : true
					},{
						field : 'id',
						title : '订单编号',
						width : 100,
						align : 'center',
						sortable : true 
					},{
						field : 'orderDate',
						title : '下单时间',
						align : 'center',
						sortable : true
					},{
						field : 'indentPrice',
						title : '订单金额',
						align : 'center',
						sortable : true ,
						formatter : function(value,row,index){
							return thousandCount(row.indentPrice) + '<span style=color:#999; > 元</span>' ;
						}
					},{
						field : 'indentType' ,
						title : '订单状态' ,
						align : 'center' ,
						sortable : true ,
						width : 80,
						sortable : true ,
						formatter : function(value , record , index){
							if(value == 0){
								return '<span style=color:red; >新订单</span>' ;
							} else if( value == 1){
								return '<span style=color:green; >处理中</span>' ; 
							} else if( value == 2){
								return '<span style=color:blue; >完成</span>' ;
							} else if( value == 3){
								return '<span style=color:black; >停滞</span>' ;
							}
						}
					},{
						field : 'indent_tele',
						title : '客户电话',
						width : 120,
						align : 'center',
						sortable : true 
					},{
						field : 'indent_recomment',
						title : '订单备注',
						align : 'center',
						sortable : true 
					},{
						field : 'indent_description',
						title : 'CRM备注',
						align : 'center',
						sortable : true
					},{
						field : 'salesmanUniqueId',
						title : '分销渠道',
						width : 100,
						align : 'center',
						sortable : true 
						/*formatter : function(value,row,index){
							if(row.salesmanName == null || row.salesmanName == ''){
								row.salesmanName = '';
							}
							//return '<span style=color:black; >'+ row.salesmanName +'</span>' ;
							//TODO
							return '<span style=color:black; >'+ row.salesmanUniqueId +'</span>' ;
						},*/
					}]],
		pagination: true ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar'
	});
	
});

//修改
function editFuc(){
	var arr = datagrid.datagrid('getSelections');
	if(arr.length != 1){
		$.message('只能选择一条记录进行修改!');
	} else {
		$('#fm2').form('clear');
		$('#fm2').form('load',arr[0]);
		openDialog('dlg2');
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
					ids += arr[i].id + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/indent/delete', {ids:ids},function(result){
					
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
function changeIndentsTypeFuc(){
	
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.message('请选择进行批量修改操作!');
	} else {
		$('#dlg').dialog({
			modal : true,
			onOpen : function(event, ui) {
			},
		}).dialog('open').dialog('center');
	}
}

function change(){	
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.message('请选择进行批量修改操作!');
	} else {
		$.messager.confirm('提示信息' , '确认修改?' , function(r){
			if(r){
				var ids = '';
				for(var i = 0 ; i < arr.length ; i++){
					ids += arr[i].id + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/indent/modifyType',
					{
						ids:ids,
						indentType:$("#indentType").combobox("getValue")
					},function(result){
						$('#dlg').dialog('close');
						datagrid.datagrid('clearSelections');
						datagrid.datagrid('reload');
						progressClose();
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

// 查询
function searchFun(){
	//清空点击表的排序操作,例如按时间排序等
	$('#gride').datagrid('options').sortName = null;
	$('#gride').datagrid('options').sortOrder = null;
	datagrid.datagrid('load', $.serializeObject($('#searchForm')));
}

//清除
function cleanFun() {
	$('#searchForm').form('clear');
	datagrid.datagrid('load', {});
}
//baobiao
function exportFun() {
	$('#searchForm').form('submit',{
		url : getContextPath() + '/portal/indent/export',
		onSubmit : function() {
			$.growlUI('报表输出中…', '正在为您输出报表，请稍等。。。');
		},
		success : function(result) {
			
		}
	});
}

function openDialog(id){
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {}
	}).dialog('open').dialog('center');
}
function saveFun(){
	progressLoad();
	$('#fm2').form('submit',{
		url : getContextPath() + '/portal/indent/update',
		success : function(result) {
			$('#dlg2').dialog('close');
			datagrid.datagrid('clearSelections');
			datagrid.datagrid('reload');
			progressClose();
			$.message('操作成功!');
		}
	});
}
