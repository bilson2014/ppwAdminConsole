var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var datagrid;
$().ready(function(){
	
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/indent/list',
		idField : 'indentId' ,
		title : '订单列表' , 
		fitColumns : true ,
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
						width : 200,
						align : 'center' ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : true , 
								missingMessage : '请填写订单名称!'
							}
						}
					},{
						field : 'indentId',
						title : '订单编号',
						width : 150,
						align : 'center'
					},{
						field : 'indentPrice',
						title : '订单金额',
						width : 150,
						align : 'center',
						sortable : true ,
						formatter : function(value,row,index){
							
							return thousandCount(row.indentPrice) + '<span style=color:#999; > 元</span>' ;
						},
						editor : {
							type : 'numberbox' ,
							options : {
								required:true ,
								min:0 ,
								max:99999999 ,
								precision:2,
								missingMessage : '请填写订单金额!'
							}
						}
					},{
						field : 'indent_tele',
						title : '联系人电话',
						width : 150,
						align : 'center',
						editor : {
							type : 'numberbox' ,
							options : {
								required:true ,
								missingMessage : '请填写联系人电话!'
							}
						}
					},{
						field : 'second',
						title : '影片长度',
						width : 110,
						align : 'center',
						formatter : function(value,row,index){
							return thousandCount(row.second) + '<span style=color:#999; > 秒</span>' ;
						},
						editor : {
							type : 'numberbox' ,
							options : {
								required:true ,
								min:0 ,
								max:99999999 ,
								precision:0,
								missingMessage : '请填写订单影片长度!'
							}
						}
					},{
						field : 'indentType' ,
						title : '订单类型' ,
						align : 'center' ,
						width : 100,
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
						},
						editor:{
							type:'combobox' , 
							options:{
								data:[{id:0 , val:'新订单'},{id:1 , val:'处理中'},{id:2 , val:'完成'},{id:3 , val:'停滞'}] ,
								valueField:'id' , 
								textField:'val' ,
								required:false , 
								editable : false
							}
						}
					},{
						field : 'orderDate',
						title : '下单时间',
						width : 150,
						align : 'center',
						editor : {
							type : 'datebox' ,
							options : {
								required : false ,
								missingMessage : '请选择下单时间!'
							}
						}
					},{
						field : 'indent_recomment',
						title : '订单备注',
						width : 150,
						align : 'center',
						editor : {
							type : 'validatebox' ,
							options : {
								required:false ,
								missingMessage : '请填写订单备注!'
							}
						}
					},{
						field : 'indent_description',
						title : 'CRM备注',
						width : 150,
						align : 'center',
						editor : {
							type : 'validatebox' ,
							options : {
								required:false ,
								missingMessage : '请填写订单备注!'
							}
						}
					},{
						field : 'service_name',
						title : '服务名称',
						width : 120,
						align : 'center'
					},{
						field : 'service_price',
						title : '服务价格',
						width : 80,
						align : 'center'
					},{
						field : 'service_discount',
						title : '折扣',
						width : 60,
						align : 'center'
					},{
						field : 'service_realPrice',
						title : '实付价格',
						width : 80,
						align : 'center'
					},{
						field : 'product_name',
						title : '产品名称',
						width : 120,
						align : 'center'
					},{
						field : 'team_name',
						title : '团队名称',
						width : 120,
						align : 'center'
					},{
						field : 'user_name',
						title : '团队名称',
						width : 120,
						align : 'center'
					},{
						field : 'user_email',
						title : '用户邮箱',
						width : 150,
						align : 'center'
					},{
						field : 'salesmanUniqueId',
						title : '分销人',
						width : 150,
						align : 'center',
						formatter : function(value,row,index){
							if(row.salesmanName == null || row.salesmanName == ''){
								row.salesmanName = '';
							}
							return '<span style=color:black; >'+ row.salesmanName +'</span>' ;
						},
						editor : {
							type : 'combobox' ,
							options : {
								url : getContextPath() + '/portal/salesman/all',
								valueField:'uniqueId',
								textField:'salesmanName',
								required : false ,
								missingMessage : '请选择分销人!'
							}
						}
					}]],
		pagination: true ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar',
		onAfterEdit:function(index , record){
			delete record.service;
			delete record.user;
			delete record.salesman;
			$.post(flag =='add' ? getContextPath() + '/portal/indent/save' : getContextPath() + '/portal/indent/update', record , function(result){
				datagrid.datagrid('clearSelections');
				datagrid.datagrid('reload');
				$.message('操作成功!');
			});
		}
	});
	
});

//修改
function editFuc(){
	var arr = datagrid.datagrid('getSelections');
	if(arr.length != 1){
		$.message('只能选择一条记录进行修改!');
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
					ids += arr[i].indentId + ',';
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
	datagrid.datagrid('load', $.serializeObject($('#searchForm')));
}

//清除
function cleanFun() {
	$('#searchForm').form('clear');
	datagrid.datagrid('load', {});
}

