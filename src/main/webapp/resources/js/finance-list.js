var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var datagrid;
$().ready(function(){
	
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/finance/list',
		idField : 'dealId' ,
		title : '财务管理列表' , 
		//fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'unOrderId',
						title : '交易流水号',
						width : 150,
						align : 'center'
					},{
						field : 'userName' ,
						title : '交易方' ,
						width : 150,
						align : 'center'
					},{
						field : 'payTime',
						title : '支付时间',
						width : 150,
						align : 'center',
						sortable : true 
					},{
						field : 'billNo',
						title : '订单编号',
						width : 150,
						align : 'center'
					},{
						field : 'dealLogSource',
						title : '交易方式',
						width : 150,
						align : 'center',
						formatter : function(value,row,index){
							if(value == 0){
								return '<span style=color:green; >线上支付</span>' ;
							} else if( value == 1){
								return '<span style=color:red; >线下支付</span>' ; 
							}
						}
					},{
						field : 'logType',
						title : '交易类型',
						align : 'center',
						width : 150,
						formatter : function(value,row,index){
							if(value == 0){
								return '<span style=color:green; >入账</span>' ;
							} else if( value == 1){
								return '<span style=color:red; >出账</span>' ; 
							}
						}
					},{
						field : 'projectName',
						title : '项目名称',
						width : 150,
						align : 'center'
					},{
						field : 'payPrice',
						title : '交易价格',
						width : 150,
						align : 'center',
						sortable : true ,
						formatter : function(value,row,index){
							return thousandCount(value) + '<span style=color:#999; > 元</span>'; 
						}
					},{
						field : 'description',
						title : '描述',
						width : 150,
						align : 'center'
					}]],
			pagination: true ,
			pageSize : 20,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			showFooter : false
	});
	
});

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

// 清除
function cleanFun() {
	$('#searchForm').form('clear');
	datagrid.datagrid('load', {});
}
