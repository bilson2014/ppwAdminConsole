var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var datagrid;
$().ready(function(){
	
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/project/list',
		idField : 'id' ,
		title : '项目管理列表' , 
		fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'projectName',
						title : '项目名称',
						width : 200,
						align : 'center' ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : true , 
								missingMessage : '请填写项目名称!'
							}
						}
					},{
						field : 'serial',
						title : '项目序列号',
						width : 110,
						align : 'center',
						editor : {
							type : 'validatebox' ,
							options : {
								required : true , 
								missingMessage : '请填写项目名称!'
							}
						}
					},{
						field : 'state',
						title : '项目状态',
						align : 'center' ,
						formatter : function(value,row,index){
							if(value == 0){
								return '<span style=color:green; >正常</span>' ;
							} else if( value == 1){
								return '<span style=color:red; >取消</span>' ; 
							} else if( value == 2){
								return '<span style=color:black; >完成</span>' ;
							}
						},
						editor:{
							type:'combobox' , 
							options:{
								data:[{id:0 , val:'正常'},{id:1 , val:'取消'},{id:2 , val:'完成'}] ,
								valueField:'id' , 
								textField:'val' ,
								required:true , 
								editable : false
							}
						}
					},{
						field : 'userId' ,
						title : '视频管家' ,
						align : 'center' ,
						formatter : function(value,row,index){
							return '<span>'+ row.managerRealName +'</span>' ;
						},
						editor : {
							type : 'combobox' ,
							options : {
								url : getContextPath() + '/project/getAllVersionManager',
								valueField:'userId',
								textField:'managerRealName',
								required : true ,
								missingMessage : '请选择视频管家!'
							}
						}
					},{
						field : 'userName',
						title : '客户公司',
						width : 110,
						align : 'center'
					},{
						field : 'customerId' ,
						title : '客户' ,
						align : 'center' ,
						width : 70,
						formatter : function(value,row,index){
							return '<span>'+ row.userContact +'</span>' ;
						},
						editor : {
							type : 'combobox' ,
							options : {
								url : getContextPath() + '/project/getAllUser',
								valueField:'customerId',
								textField:'userName',
								required : true ,
								missingMessage : '请选择客户!'
							}
						}
					},{
						field : 'userPhone',
						title : '客户手机',
						width : 110,
						align : 'center'
					},{
						field : 'teamId',
						title : '供应商名称',
						width : 70,
						align : 'center' ,
						formatter : function(value,row,index){
							return '<span>'+ row.teamName +'</span>' ;
						},
						editor : {
							type : 'combobox' ,
							options : {
								url : getContextPath() + '/project/getAllTeam',
								valueField:'teamId',
								textField:'teamName',
								required : true ,
								missingMessage : '请选择供应商信息!'
							}
						}
					},{
						field : 'teamContact',
						title : '供应商联系人',
						align : 'center'
					},{
						field : 'teamPhone',
						title : '供应商联系人手机号',
						align : 'center'
					},{
						field : 'source',
						title : '项目来源',
						align : 'center',
						editor:{
							type:'combobox' , 
							options:{
								data:[{id:0 , val:'网站下单'},{id:1 , val:'友情推荐'},{id:2 , val:'活动下单'},{id:3 , val:'渠道优惠'},{id:4 , val:'团购下单'},{id:5 , val:'媒体推广'}] ,
								valueField:'val' , 
								textField:'val' ,
								required:true , 
								editable : false
							}
						}
					},{
						field : 'price',
						title : '项目额度',
						align : 'center'
					}]],
		pagination: true ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar',
		onAfterEdit:function(index , record){
			delete record.time;
			delete record.task;
			delete record.userViewModel;
			$.post(flag =='add' ? getContextPath() + '/project/save' : getContextPath() + '/project/updateInfo', record , function(result){
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
					ids += arr[i].id + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/project/delete', {ids:ids},function(result){
					
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