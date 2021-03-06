var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var formUrl;
var datagrid;
$().ready(function(){
	var invoiceTeamList = {
			init:function(){
				//初始化搜索 条件
				this.searchInit();
				//初始化DataGrid
				this.table();
			},
			searchInit:function(){
				$('#search-projectId').combobox({
					url : getContextPath() + '/project/all',
					valueField : 'projectId',
					textField : 'projectName',
					filter: function(q, row){
						if(row.projectName == null)
							return false;
						return row.projectName.indexOf(q) >= 0;
					}
				});
			},
			table:function(){
				datagrid = $('#gride').datagrid({
					url : getContextPath()+ '/portal/invoice/team/list',
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
									title : '发票号',
									align : 'center'
								},{
									field : 'invoiceType' ,
									title : '发票类型' ,
									align : 'center',
									formatter : function(value,row,index){
										if(value == 1){
											return '<span style=color:red; >增值税专用发票</span>' ;
										} else if(value == 2){
											return '<span style=color:black; >增值税普通发票</span>' ;
										} else if(value == 3) {
											return '<span style=color:green; >通用机打发票</span>' ;
										}
									}
								},{
									field : 'invoiceContent',
									title : '发票内容',
									align : 'left'
								},{
									field : 'invoiceTotal',
									title : '价税合计',
									align : 'center',
									sortable : true 
								},{
									field : 'invoiceRadio',
									title : '税率',
									align : 'center',
									sortable : true ,
									formatter : function(value,row,index){
										return '<span style=color:red; >'+ (value * 100) +'%</span>' ;
									}
								},{
									field : 'invoiceStampTime',
									title : '开票日期',
									sortable : true ,
									align : 'center',
									formatter : function(value,row,index){
										var time = new Date(value); 
										return time.Format("yyyy-MM-dd"); 
									}
								},{
									field : 'invoiceTeamTime',
									title : '付款日期',
									sortable : true ,
									align : 'center',
									formatter : function(value,row,index){
										var time = new Date(value); 
										return time.Format("yyyy-MM-dd"); 
									}
								},{
									field : 'teamName',
									title : '供应商名称',
									align : 'left',
									width : 120
								},{
									field : 'projectName',
									title : '项目名称',
									align : 'left',
									width : 120
								},{
									field : 'invoiceEmployeeName',
									title : '提供人',
									align : 'center'
								},{
									field : 'invoiceNotice',
									title : '备注',
									align : 'left',
									width : 220
								},{
									field : 'invoiceStatus',
									title : '审批状态',
									align : 'center',
									sortable : true ,
									formatter : function(value,row,index){
										if(value == 0){
											return '<span style=color:black; >未审核</span>' ;
										} else if(value == 1){
											return '<span style=color:green; >审核通过</span>' ;
										}else if(value == 2){
											return '<span style=color:red; >审核未通过</span>' ;
										}
									}
								},{
									field : 'reason',
									title : '原因',
									align : 'left'
								
								}]],
					pagination: true ,
					pageSize : 20,
					pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
					showFooter : true,
					toolbar : '#toolbar'
				});
			}
	}
	invoiceTeamList.init();	
});


// 增加
function addFuc(){
	$('#fm').form('clear');
	openDialog('dlg',null);
	formUrl = getContextPath() + '/portal/invoice/team/save';
	$('input[name="invoiceId"]').val(0);
}

// 修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		$('#fm').form('load',rows[0]);
		openDialog('dlg',rows[0]);
		formUrl = getContextPath() + '/portal/invoice/team/update';
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}

//报表导出
function exportFun(){
	$('#searchForm').form('submit',{
		url : getContextPath() + '/portal/invoice/team/export',
		onSubmit : function() {
			// $.growlUI('报表输出中…', '正在为您输出报表，请稍等。。。');
		},
		success : function(result) {
			
		}
	});
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
				$.post(getContextPath() + '/portal/invoice/team/delete', {ids:ids},function(result){
					
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

//审批
function auditFuc(data){
	var operate = $(data).attr("data-flag");
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.message('请选择进行审批操作!');
		return false;
	}
	if(operate == "ok"){//审批通过
		$.messager.confirm('提示信息' , '确认通过审批?' , function(r){
			if(r){
				var ids = '';
				for(var i = 0 ; i < arr.length ; i++){
					ids += arr[i].invoiceId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/invoice/team/auditing', {
					ids:ids,
					invoiceStatus:1
				},function(result){
					// 刷新数据
					datagrid.datagrid('clearSelections');
					datagrid.datagrid('reload');
					$.message('操作成功!');
				});
			} else {
				 return ;
			}
		});
	} else if(operate == "no"){
		if(arr.length == 1 ){
			$('#invoicedlg').dialog({
				modal : true,
				onOpen : function(){
					var rows = datagrid.datagrid('getSelections');
					$('#invoicedlgfm').form('clear');
					$('#invoicedlgfm').form('load',rows[0]);
					$('#invoicefm_invoiceStatus').val(2);
				}
			}).dialog('open').dialog('center');
		}else{
			$.message('只能选择一条进行审批未通过!');
			return false;
		}
		
	}
}
function saveInvoiceReason(){
	$('#invoicedlgfm').form('submit',{
		url : getContextPath() + '/portal/invoice/team/auditing',
		onSubmit : function() {
			var flag = $(this).form('validate');
			var msg = "请填写原因!"
			if(!flag){
				$.message(msg);
			}
			return flag;
		},
		success : function(result) {
			$('#invoicedlg').dialog('close');
			datagrid.datagrid('reload');
			datagrid.datagrid('clearSelections');
			$.message('操作成功!');
		}
	});
}
// 确认事件
function save(){
	
	progressLoad();
	$('#fm').form('submit',{
		url : formUrl,
		onSubmit : function() {
			var flag = $(this).form('validate');
			var msg = "存在未填项!"
			if(flag){
				// 判断 起至日期是否为空
				var stampTime = $('input[name="invoiceStampTime"]').val();
				if(stampTime == null || stampTime == undefined || stampTime == ''){
					flag = false;
					msg="请填写开票时间后再提交!"
				}
				var teamTime = $('input[name="invoiceTeamTime"]').val();
				if(teamTime == null || teamTime == undefined || teamTime == ''){
					flag = false;
					msg="请填写提供发票时间后再提交!"
				}
			}
			if(!flag){
				$.message(msg);
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
				textField : 'projectName',
				filter: function(q, row){
					if(row.projectName == null)
						return false;
					return row.projectName.indexOf(q) >= 0;
				},
				onSelect : function(record){
					var id = $('#invoiceProjectId').combobox('getValue');
					
					if(id<20170000){
						loadData(function(flag){
							var teamId = flag.teamId;
							$('#invoiceTeamId').combobox('setValue',teamId);
						}, getContextPath() + '/project/get-projectInfo', $.toJSON({
							id : id
						}));
					}else{
						loadData(function(flag){
							if(null!=flag && undefined!=flag && flag.length>0){
								var teamId = $('#invoiceTeamId').combobox('getValue');
								var rewrite=true;
								for(var i=0;i<flag.length;i++){
									if(teamId==flag[i].teamId){
										rewrite=false;
										break;
									}
								}
								if(rewrite){
									$('#invoiceTeamId').combobox('setValue',flag[0].teamId);
								}
							}
							
						}, getContextPath() + '/portal/projectflow/team', $.toJSON({
							projectId : id
						}));
					}
					
				}
			});
			
			$('#invoiceTeamId').combobox({
				url : getContextPath() + '/project/getAllTeam',
				valueField : 'teamId',
				textField : 'teamName',
				filter: function(q, row){
					// 修改过滤器增加模糊搜索
					if(row.teamName == null)
						return false;
					return row.teamName.indexOf(q) >= 0;
				}
			});
			$('#invoiceEmployeeId').combobox({
				url : getContextPath() + '/portal/employee/findProvider',
				valueField : 'employeeId',
				textField : 'employeeRealName',
				filter: function(q, row){
					// 修改过滤器增加模糊搜索
					if(row.employeeRealName == null)
						return false;
					return row.employeeRealName.indexOf(q) >= 0;
				}
			});
			
			if(data != null && data != undefined && data != ''){
				var invoiceTeamId = data.invoiceTeamId;
				var projectId = data.invoiceProjectId;
				var employeeId = data.invoiceEmployeeId;
				if(invoiceTeamId != null && invoiceTeamId != undefined && invoiceTeamId != ''){
					$('#invoiceTeamId').combobox('setValue',invoiceTeamId);
				}else {
					$('#invoiceTeamId').combobox('setValue','');
				}
				
				if(projectId != null && projectId != undefined && projectId != ''){
					$('#invoiceProjectId').combobox('setValue',projectId);
				}else {
					$('#invoiceProjectId').combobox('setValue','');
				}
				if(employeeId != null && employeeId != undefined && employeeId != ''){
					$('#invoiceEmployeeId').combobox('setValue',employeeId);
				}else {
					$('#invoiceEmployeeId').combobox('setValue','');
				}
				if(data.invoiceStatus==1){//审核通过的单子不能修改
					$("#saveInvoice").hide();
				}else{
					$("#saveInvoice").show();
				}
			}
		}
	}).dialog('open').dialog('center');
}

//查询
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