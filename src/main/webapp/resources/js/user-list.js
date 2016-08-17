var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var datagrid;
$().ready(function(){

	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/user/list',
		idField : 'id' ,
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
						field : 'userName',
						title : '昵称',
						width : 150,
						align : 'center' ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : true , 
								missingMessage : '请填写昵称!',
							},
						}
					},{
						field : 'realName',
						title : '真实姓名',
						width : 150,
						align : 'center' ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false , 
								missingMessage : '请填写真实姓名!'
							}
						}
					},{
						field : 'clientLevel',
						title : '客户级别',
						width : 150,
						align : 'center' ,
						formatter : function(value , record , index){
							if(value == 0){
								return '<span style=color:red; >A</span>' ;
							} else if( value == 1){
								return '<span style=color:blue; >B</span>' ; 
							} else if( value == 2){
								return '<span style=color:green; >C</span>' ;
							} else if( value == 3){
								return '<span style=color:black; >S</span>' ;
							} else {
								return '<span style=color:orange; >未分级</span>' ;
							}
						},
						editor:{
							type:'combobox' , 
							options:{
								data:[{id:3 , val:'S'},{id:0 , val:'A'},{id:1 , val:'B'},{id:2 , val:'C'}] ,
								valueField:'id' , 
								textField:'val' ,
								required:false , 
								editable : false
							}
						}
					},{
						field : 'userCompany',
						title : '客户公司',
						width : 150,
						align : 'center' ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false , 
								missingMessage : '请填写客户公司!'
							}
						}
					},{
						field : 'sex' ,
						title : '性别' ,
						align : 'center' ,
						width : 60,
						formatter : function(value , record , index){
							if(value == 0){
								return '<span style=color:blue; >男</span>' ;
							} else if( value == 1){
								return '<span style=color:red; >女</span>' ; 
							} else if( value == 2){
								return '<span style=color:green; >保密</span>' ;
							}
						},
						editor:{
							type:'combobox' , 
							options:{
								data:[{id:0 , val:'男'},{id:1 , val:'女'},{id:2 , val:'保密'}] ,
								valueField:'id' , 
								textField:'val' ,
								required:true , 
								editable : false
							}
						}
					},{
						field : 'password',
						title : '密码',
						width : 110,
						align : 'center' ,
						hidden : true
					},{
						field : 'telephone' ,
						title : '联系电话' ,
						align : 'center' ,
						width : 100,
						sortable : true ,
						editor : {
							type : 'validatebox' ,
							options : {
								required:true ,
								missingMessage : '请填写联系电话!'
							}
						}
					},{
						field : 'email',
						title : '电子邮件',
						width : 120,
						align : 'center',
						editor : {
							type : 'validatebox' ,
							options : {
								required : false ,
								missingMessage : '请填写电子邮件!'
							}
						}
					},{
						field : 'qq',
						title : 'QQ',
						width : 80,
						align : 'center',
						editor : {
							type : 'validatebox' ,
							options : {
								required : false ,
								missingMessage : '请填写QQ号码!'
							}
						}
					},{
						field : 'customerSource' ,
						title : '客户来源' ,
						align : 'center' ,
						width : 60,
						formatter : function(value , record , index){
							switch (value) {
							case 1:
								return '<span style=color:blue; >渠道</span>' ;
							case 2:
								return '<span style=color:blue; >推广</span>' ;
							case 3:
								return '<span style=color:blue; >自主开发</span>' ;
							case 4:
								return '<span style=color:blue; >活动</span>' ;
							case 5:
								return '<span style=color:blue; >推荐</span>' ;
							}
						},
						editor:{
							type:'combobox' , 
							options:{
								data:[{id:1 , val:'渠道'},{id:2 , val:'推广'},{id:3 , val:'自主开发'},{id:4 , val:'活动'},{id:5 , val:'推荐'}] ,
								valueField:'id' , 
								textField:'val' ,
								required:true , 
								editable : false
							}
						}
					},{
						field : 'createDate',
						title : '注册日期',
						width : 120,
						align : 'center'
					},{
						field : 'birthday',
						title : '出生日期',
						width : 120,
						align : 'center' ,
						editor : {
							type : 'datebox' ,
							options : {
								required : false ,
								missingMessage : '请选择出生日期!'
							}
						}
					},{
						field : 'note',
						title : '备注信息',
						width : 120,
						align : 'center' ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false , 
								missingMessage : '请填写备注信息!'
							}
						}
					},{
						field : 'imgUrl' ,
						title : '用户头像地址',
						width : 110,
						align : 'center' ,
						hidden : true
					}]],
		pagination: true ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar',
		onAfterEdit:function(index , record){
			delete record.roles;
			//$.post(flag =='add' ? getContextPath() + '/portal/user/save' : getContextPath() + '/portal/user/update', record , function(result){
			//	datagrid.datagrid('clearSelections');
			///	datagrid.datagrid('reload');
			//	$.message('操作成功!');
			//});
			//modify by wanglc 添加用户名唯一性 验证 begin
			loadData(function(data){
				if(data){
					$.post(flag =='add' ? getContextPath() + '/portal/user/save' : getContextPath() + '/portal/user/update', record , function(result){
						datagrid.datagrid('clearSelections');
						datagrid.datagrid('reload');
						$.message('操作成功!');
					});
				}else{
					datagrid.datagrid('beginEdit', index);
					$.message('用户名已经存在!');
				}
			}, getContextPath() + '/portal/user/unique/username',$.toJSON({
				userName : record.userName,
				id:record.id
			}));
			//modify by wanglc 添加用户名唯一性 验证 end
		},
	});
	
});

//增加
function addFuc(){
	if(editing == undefined){
		flag = 'add';
		//1 先取消所有的选中状态
		datagrid.datagrid('unselectAll');
		//2追加一行
		datagrid.datagrid('appendRow',{birthday : getCurrentTime()});
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
				$.post(getContextPath() + '/portal/user/delete', {ids:ids},function(result){
					
					// 刷新状态
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
	datagrid.datagrid('load', {clientLevel:-1});
}