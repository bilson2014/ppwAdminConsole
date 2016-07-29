var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var formUrl;
var datagrid;
$().ready(function(){
	
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath()+ '/portal/finance-offline/list',
		idField : 'dealId' ,
		title : '线下财务管理列表' , 
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
						align : 'center'
					},{
						field : 'projectName',
						title : '项目名称',
						align : 'center'
					},{
						field : 'payTime',
						title : '交易时间',
						align : 'center'
					},{
						field : 'dealLogSource',
						title : '交易方式',
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
						formatter : function(value,row,index){
							if(value == 0){
								return '<span style=color:green; >入账</span>' ;
							} else if( value == 1){
								return '<span style=color:red; >出账</span>' ; 
							}
						}
					},{
						field : 'billNo',
						title : '订单编号',
						align : 'center'
					},{
						field : 'userName' ,
						title : '交易方' ,
						align : 'center'
					},{
						field : 'payPrice',
						title : '交易金额',
						align : 'center',
						formatter : function(value,row,index){
							return thousandCount(value) + '<span style=color:#999; > 元</span>'; 
						}
					},{
						field : 'description',
						title : '描述',
						align : 'center'
					},{
						field : 'userId',
						title : '客户唯一编号',
						align : 'center',
						hidden : true
					},{
						field : 'projectId',
						title : '项目唯一编号',
						align : 'center',
						hidden : true
					}]],
		pagination: true ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : true,
		toolbar : '#toolbar'
	});
		
});


// 增加
function addFuc(){
	$('#fm').form('clear');
	openDialog('dlg',null);
	formUrl = getContextPath() + '/portal/finance/save';
	$('input[name="dealId"]').val(0);
	$('input[name="productName"]').val('');
	$('input[name="userName"]').val('');
}

// 修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		$('#fm').form('load',rows[0]);
		openDialog('dlg',rows[0]);
		formUrl = getContextPath() + '/portal/finance/update';
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}

// 删除
function delFuc(){
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.message('请选择进行删除操作!');
	} else {
		$.messager.confirm('提示信息' , '确认删除?' , function(r){
			if(r){
				var ids = '';
				for(var i = 0 ; i < arr.length ; i++){
					ids += arr[i].dealId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/finance/delete', {ids:ids},function(result){
					
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
			var flag = $(this).form('validate');
			if(flag){
				// 判断 起至日期是否为空
				var payTime = $('input[name="payTime"]').val();
				if(payTime == null || payTime == undefined || payTime == ''){
					flag = false;
				}
			}
			
			if(!flag){
				$.message('请填写交易时间后再提交!');
				progressClose();
			}
			return flag;
		},
		success : function(result) {
			$('#dlg').dialog('close');
			datagrid.datagrid('reload');
			datagrid.datagrid('clearSelections');
			progressClose();
			$.message('操作成功!');
		}
	});
}

// 打开dialog
function openDialog(id,data){
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event,ui){
			$('#projectId').combobox({
				url : getContextPath() + '/project/all',
				valueField : 'projectId',
				textField : 'projectName',
				onSelect : function(record){
					$('#projectName').val(record.projectName);
					var id = $('#projectId').combobox('getValue');
					loadData(function(flag){
						var userId = flag.customerId;
						$('#userId').combobox('setValue',userId);
						var userName = $('#userId').combobox('getText');
						$('#userName').val(userName);
					}, getContextPath() + '/project/get-projectInfo', $.toJSON({
						id : id
					}));
				},
				filter: function(q, row){
					if(row.projectName == null)
						return false;
					return row.projectName.indexOf(q) >= 0;
				}
			});
			
			$('#userId').combobox({
				url : getContextPath() + '/portal/user/all',
				valueField : 'userId',
				textField : 'userName',
				onSelect : function(record){
					$('#userName').val(record.userName);
				},
				filter: function(q, row){
					// 修改过滤器增加模糊搜索
					if(row.userName == null)
						return false;
					return row.userName.indexOf(q) >= 0;
				}
			});
			
			if(data != null && data != undefined && data != ''){
				var userId = data.userId;
				var projectId = data.projectId;
				 
				if(userId != null && userId != undefined && userId != ''){
					$('#userId').combobox('setValue',userId);
					var userName = $('#userId').combobox('getText');
					$('#userName').val(userName);
				}else {
					$('#userId').combobox('setValue','');
				}
				
				if(projectId != null && projectId != undefined && projectId != ''){
					$('#projectId').combobox('setValue',projectId);
				}else {
					$('#projectId').combobox('setValue','');
				}
			}
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