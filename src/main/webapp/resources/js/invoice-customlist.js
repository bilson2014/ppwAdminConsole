var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var formUrl;
var datagrid;
$().ready(function(){
		
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath()+ '/portal/invoice/custom/list',
		idField : 'invoiceId' ,
		title : '客户发票管理列表' , 
		//fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'invoiceCode',
						title : '发票编号',
						align : 'center'
					},{
						field : 'invoiceName' ,
						title : '发票名称' ,
						align : 'center'
					},{
						field : 'invoicePrice',
						title : '发票金额',
						align : 'center'
					},{
						field : 'invoiceRadio',
						title : '税率',
						align : 'center',
						formatter : function(value,row,index){
							return '<span style=color:red; >'+ (value * 100) +'%</span>' ;
						}
					},{
						field : 'projectName',
						title : '项目名称',
						align : 'center'
					},{
						field : 'userName',
						title : '客户名称',
						align : 'center'
					},{
						field : 'invoiceFlag',
						title : '视频管家是否领票',
						align : 'center',
						formatter : function(value,row,index){
							if(value == 0){
								return '<span style=color:red; >未领</span>' ;
							} else if(value == 1){
								return '<span style=color:black; >已领</span>' ;
							}
						}
					},{
						field : 'invoiceDraw',
						title : '对方是否领票',
						align : 'center',
						formatter : function(value,row,index){
							if(value == 0){
								return '<span style=color:red; >未领</span>' ;
							} else if(value == 1){
								return '<span style=color:black; >已领</span>' ;
							}
						}
					},{
						field : 'invoiceNotice',
						title : '备注',
						align : 'center'
					},{
						field : 'createDate',
						title : '创建时间',
						align : 'center'
					},{
						field : 'invoiceProjectId',
						title : '项目唯一编号',
						align : 'center',
						hidden : true
					},{
						field : 'invoiceUserId',
						title : '用户唯一编号',
						align : 'center',
						hidden : true
					},{
						field : 'invoiceProviderId',
						title : '供应商唯一编号',
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
	formUrl = getContextPath() + '/portal/invoice/save';
	$('input[name="invoiceId"]').val(0);
	$('input[name="invoiceType"]').val(0);
	$('input[name="invoiceProviderId"]').val(0);
}

// 修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		$('#fm').form('load',rows[0]);
		$('input[name="invoiceType"]').val(0);
		$('input[name="invoiceProviderId"]').val(0);
		openDialog('dlg',rows[0]);
		formUrl = getContextPath() + '/portal/invoice/update';
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
					ids += arr[i].invoiceId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/invoice/delete', {ids:ids},function(result){
					
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
			$('#invoiceProjectId').combobox({
				url : getContextPath() + '/project/all',
				valueField : 'projectId',
				textField : 'projectName'
			});
			
			$('#invoiceUserId').combobox({
				url : getContextPath() + '/portal/user/all',
				valueField : 'userId',
				textField : 'userName'
			});
			
			if(data != null && data != undefined && data != ''){
				var userId = data.invoiceUserId;
				var projectId = data.invoiceProjectId;
				
				if(userId != null && userId != undefined && userId != ''){
					$('#invoiceUserId').combobox('setValue',userId);
				}else {
					$('#invoiceUserId').combobox('setValue','');
				}
				
				if(projectId != null && projectId != undefined && projectId != ''){
					$('#invoiceProjectId').combobox('setValue',projectId);
				}else {
					$('#invoiceProjectId').combobox('setValue','');
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