var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var datagrid;
var EmployeeListCache;
$().ready(function(){
	syncLoadData(function(res){
		EmployeeListCache = res
	}, getContextPath() + '/portal/getEmployeeList',null);
	
	$('#tIndentSource').combobox({
		url : getContextPath() + '/portal/getCustomerService',
		valueField : 'employeeId',
		textField : 'employeeRealName'
	});
	
	
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
							} else if(value == 4){
								return '<span style=color:black; >再次沟通</span>' ;
							}  else if(value == 5){
								return '<span style=color:black; >真实</span>' ;
							} else if(value == 6){
								return '<span style=color:black; >虚假</span>' ;
							} else if(value == 7){
								return '<span style=color:black; >提交</span>' ;
							}
						}
					},{
						field : 'indentSource' ,
						title : '订单来源' ,
						align : 'center' ,
						sortable : true ,
						width : 80,
						sortable : true ,
						formatter : function(value , record , index){
							if( value == 1){
								return '<span style=color:black; >线上-网站</span>' ; 
							} else if( value == 2){
								return '<span style=color:black; >线上-活动</span>' ;
							} else if( value == 3){
								return '<span style=color:black; >线上-新媒体</span>' ;
							}else if( value == 4){
								return '<span style=color:black; >线下-电销</span>' ;
							}else if( value == 5){
								return '<span style=color:black; >线下-直销</span>' ;
							}else if( value == 6){
								return '<span style=color:black; >线下-活动</span>' ;
							}else if( value == 7){
								return '<span style=color:black; >线下-渠道</span>' ;
							}else if( value == 8){
								return '<span style=color:black; >复购</span>' ;
							}else if( value == 9){
								return '<span style=color:black; >线下-400</span>' ;
							}else if( value == 10){
								return '<span style=color:black; >线下-商桥</span>' ;
							}
						}
					},{
						field : 'employeeId' ,
						title : '处理人员' ,
						align : 'center' ,
						width : 80,
						formatter : function(value , record , index){
							for (var int = 0; int < EmployeeListCache.length; int++) {
								var elc = EmployeeListCache[int];
								if(elc.employeeId == value){
									return elc.employeeRealName;
								}
							}
							return '';
						}
					},{
						field : 'realName',
						title : '客户联系人',
						width : 120,
						align : 'center',
						sortable : true 
					},{
						field : 'indent_tele',
						title : '客户电话',
						width : 120,
						align : 'center',
						sortable : true 
					},{
						field : 'userCompany',
						title : '客户公司',
						width : 120,
						align : 'center',
						sortable : true 
					},{
						field : 'indent_recomment',
						title : '订单备注',
						width : 220,
						align : 'center',
						sortable : true 
					},{
						field : 'cSRecomment',
						title : 'CRM备注',
						align : 'center',
						sortable : true
					},{
						field : 'salesmanUniqueId',
						title : '分销ID',
						align : 'center',
						hidden: true
					},{
						field : 'salesmanName',
						title : '分销渠道',
						width : 100,
						align : 'center',
						sortable : true ,
						formatter : function(value,row,index){
							if(row.salesmanName == null || row.salesmanName == ''){
								row.salesmanName = '';
							}
							return '<span style=color:black; >'+ row.salesmanName +'</span>' ;
						},
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
		
		$('#salesmanUnique').combobox({
			url : getContextPath() + '/portal/salesman/all',
			valueField:'uniqueId',
			textField:'salesmanName',
		});	
		$('#salesmanUnique').combobox('setValue',arr[0].salesmanUniqueId);
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
		onOpen : function(event, ui) {
		}
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
function customerServiceFun(){
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.message('最少需要选择一条记录以上才能进行分配!');
	} else {
		var ids = '';
		for(var i = 0 ; i < arr.length ; i++){
			ids += arr[i].id + ',';
		}
		ids = ids.substring(0,ids.length-1);
		$('#FmCustomerService').form('clear');
		$('#dlgCustomerService').dialog({
			modal : true,
			onOpen : function(event, ui) {
				$('#customerService').combobox({
					url : getContextPath() + '/portal/getCustomerService',
					valueField : 'employeeId',
					textField : 'employeeRealName'
				});
			}
		}).dialog('open').dialog('center');
		$('#employeeIds').val(ids);
	}
}

function saveCustomerService(){
	progressLoad();
	var employeeIds = $('#employeeIds').val();
	var customerService = $('#customerService').combobox('getValue');
	$.post(getContextPath() + '/portal/indent/updateCustomerService',
			{
		employeeIds:employeeIds,
		customerService:customerService
			},function(result){
				datagrid.datagrid('clearSelections');
				datagrid.datagrid('reload');
				progressClose();
				$.message('操作成功!');
				$('#dlgCustomerService').dialog('close');
	});
}