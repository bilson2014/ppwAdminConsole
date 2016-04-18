var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
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
						editor : {
							type : 'numberbox' ,
							options : {
								required : true , 
								min:0 ,
								max:9999999 ,
								precision:0,
								missingMessage : '请填写影片时长!'
							}
						}
					},{
						field : 'servicePrice',
						title : '价格',
						width : 110,
						align : 'center' ,
						editor : {
							type : 'numberbox' ,
							options : {
								required:true ,
								min:0 ,
								max:999999999 ,
								precision:2,
								missingMessage : '请填写服务价格!'
							}
						}
					},{
						field : 'serviceDiscount' ,
						title : '折扣' ,
						align : 'center' ,
						width : 70,
						sortable : true ,
						editor : {
							type : 'numberbox' ,
							options : {
								required:true ,
								min:0 , 
								max:1 ,
								precision:2,
								missingMessage : '请填写折扣!'
							}
						}
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
						editor : {
							type : 'numberbox' ,
							options : {
								required : false ,
								min:0 ,
								max:9999,
								missingMessage : '请填写排序!'
							}
						}
					},{
						field : 'productId',
						title : '所属项目',
						width : 170,
						align : 'center' ,
						formatter : function(value,row,index){
							return '<span style=color:red; >'+ row.productName +'</span>' ;
						},
						editor : {
							type : 'combobox' ,
							options : {
								url : getContextPath() + '/portal/service/productSelect',
								valueField:'productId',
								textField:'productName',
								required : true ,
								missingMessage : '请选择所属项目!'
							}
						}
					},{
						field : 'serviceDescription',
						title : '描述',
						width : 320,
						align : 'center' ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false , 
								missingMessage : '请填写服务名称!'
							}
						}
					}]],
		pagination: true ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar',
		onAfterEdit:function(index , record){
			delete record.product;
			$.post(flag =='add' ? getContextPath() + '/portal/service/save' : getContextPath() + '/portal/service/update', record , function(result){
				datagrid.datagrid('clearSelections');
				datagrid.datagrid('reload');
				$.message('操作成功!');
			});
		}
	});
	
	service.dataInit();
});

var service = {
	dataInit : function(){
		$('#search-name').combobox({
			url : getContextPath() + '/portal/service/productSelect',
			valueField : 'productId',
			textField : 'productName'
		});
	}
}

//增加
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