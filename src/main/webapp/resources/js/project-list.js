var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var datagrid;
$().ready(function(){
	
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/project/list',
		idField : 'id' ,
		title : '项目管理列表' , 
		//fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'serial',
						title : '项目序列号',
						width : 90,
						align : 'center'
					},{
						field : 'projectName',
						title : '项目名称',
						width : 200,
						align : 'center'
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
							} else if( value == 3){
								return '<span style=color:black; >暂停</span>' ;
							}
						}
					},{
						field : 'employeeRealName' ,
						title : '视频管家' ,
						align : 'center'
					},{
						field : 'userName',
						title : '客户公司',
						align : 'center'
					},{
						field : 'clientLevel',
						title : '客户级别',
						align : 'center',
						formatter : function(value , record , index){
							if(value == 0){
								return '<span style=color:red; >A</span>' ;
							} else if( value == 1){
								return '<span style=color:blue; >B</span>' ; 
							} else if( value == 2){
								return '<span style=color:black; >C</span>' ;
							}
						}
					},{
						field : 'userContact' ,
						title : '客户联系人' ,
						align : 'center' 
					},{
						field : 'userPhone',
						title : '客户手机',
						align : 'center'
					},{
						field : 'teamName',
						title : '供应商名称',
						align : 'center'
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
						align : 'center'
					},{
						field : 'referrerName',
						title : '项目来源人',
						align : 'center'
					},{
						field : 'price',
						title : '项目预算金额',
						align : 'center',
						formatter : function(value , row , index){
							var pBegin = 0;
							var pEnd = 0;
							if(row.priceFirst != null && row.priceFirst != '' && row.priceFirst != undefined){
								pBegin = row.priceFirst;
							}
							
							if(row.priceLast != null && row.priceLast != '' && row.priceLast != undefined){
								pEnd = row.priceLast;
							}
							if(pBegin == 0 && pEnd == 0){
								return '<span style=color:orange; >0 元</span>' ;
							}
							return '<span style=color:orange; >'+ pBegin + ' 元 ~ ' + pEnd +' 元</span>' ;
						}
					},{
						field : 'priceFinish',
						title : '最终额度',
						align : 'center'
					},{
						field : 'customerPayment',
						title : '客户实付金额',
						align : 'center'
					},{
						field : 'providerPayment',
						title : '供应商实付金额',
						align : 'center'
					},{
						field : 'synergys',
						title : '协同人',
						align : 'center',
						formatter : function(value , row , index){
							var info = '';
							if(value != null && value != '' && value != undefined){
								// 有项目协同人
								for(var i = 0 ;i < value.length;i ++){
									info += value[i].userName + '(' + value[i].ratio + '%)';
									if(i != value.length - 1){
										info += ' ,';
									}
								}
							}
							return '<span style=color:orange; >'+ info +'</span>' ;
						}
					},{
						field : 'customerId' ,
						title : '客户ID' ,
						align : 'center' ,
						hidden: true
					},{
						field : 'teamId' ,
						title : '供应商ID' ,
						align : 'center' ,
						hidden: true
					},{
						field : 'userId' ,
						title : '视频管家ID' ,
						align : 'center' ,
						hidden: true
					},{
						field : 'referrerId' ,
						title : '项目来源人ID' ,
						align : 'center' ,
						hidden: true
					},{
						field : 'priceFirst',
						title : '项目额度最小值',
						align : 'center',
						hidden : true
					},{
						field : 'priceLast',
						title : '项目额度最大值',
						align : 'center',
						hidden : true
					},{
						field : 'userType' ,
						title : '创建类型' ,
						align : 'center' ,
						hidden: true
					}]],
			pagination: true ,
			pageSize : 20,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			showFooter : false,
			toolbar : '#toolbar'
	});
	
	project.initData();
});

var project = {
	initData : function(){
		$('#search-teamId').combobox({
			url : getContextPath() + '/project/getAllTeam',
			valueField : 'teamId',
			textField : 'teamName'
		});
		
		$('#search-userId').combobox({
			url : getContextPath() + '/project/getAllVersionManager',
			valueField : 'userId',
			textField : 'employeeRealName'
		});
		
		$('#search-projectId').combobox({
			url : getContextPath() + '/project/getAllProject',
			valueField : 'id',
			textField : 'projectName'
		});
		
		$('#search-source').combobox({
			url : getContextPath() + '/project/getProjectTags',
			valueField : 'name',
			textField : 'name'
		});
		
	}
}


//增加
function addFuc(){
	$('#fm').form('clear');
	openDialog('dlg',null);
	formUrl = getContextPath() + '/project/saveInfo';
	$('#projectId').val(0);
	$('#userType').val('role_manager');
}

// 修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		$('#fm').form('load',rows[0]);
		openDialog('dlg',rows[0]);
		formUrl = getContextPath() + '/project/updateInfo';
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
		$.messager.confirm('提示信息' , '确认取消流程吗?' , function(r){
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
			
			if(!flag){
				progressClose();
				$.message('请将信息填写完整!');
			}
			return flag;
		},
		success : function(result) {
			$('#dlg').dialog('close');
			datagrid.datagrid('reload');
			progressClose();
			$.message('操作成功!');
		}
	});
}

// 打开dialog
function openDialog(id,data){
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {
			
			if(data == null){
				// 新增，则生成项目序号
				loadData(function(pro){
					var serial = pro.serial;
					if(serial != null && serial != undefined && serial != ''){
						$('#serial').textbox('setValue',serial);
					}
				}, getContextPath() + '/project/get/SerialID', null);
			}
			
			$('#customerId').combobox({
				url : getContextPath() + '/project/getAllUser',
				valueField : 'customerId',
				textField : 'userName',
				onSelect : function(record){
					$('#userContact').textbox('setValue',record.userContact);
					$('#userPhone').textbox('setValue',record.userPhone);
				}
			});
			
			$('#teamId').combobox({
				url : getContextPath() + '/project/getAllTeam',
				valueField : 'teamId',
				textField : 'teamName',
				onSelect : function(record){
					$('#teamContact').textbox('setValue',record.teamContact);
					$('#teamPhone').textbox('setValue',record.teamPhone);
				}
			});
			
			$('#referrerId').combobox({
				url : getContextPath() + '/portal/getEmployeeList',
				valueField : 'employeeId',
				textField : 'employeeRealName'
			});
			
			$('#source').combobox({
				url : getContextPath() + '/project/getProjectTags',
				valueField : 'name',
				textField : 'name',
				onSelect : function(record){
					if(record.name == '个人信息下单'){
						$('#referrer-tr').show();
					}else {
						$('#referrer-tr').hide();
						$('#referrerId').combobox('setValue','0');
					}
				}
			});
			
			$('#userId').combobox({
				url : getContextPath() + '/project/getAllVersionManager',
				valueField : 'userId',
				textField : 'employeeRealName'
			});
			
			if(data != null && data != undefined && data != ''){
				var userId = data.userId;
				if(userId != null && userId != undefined && userId != ''){
					$('#userId').combobox('setValue',userId);
				}else {
					$('#userId').combobox('setValue','');
				}
				
				var teamId = data.teamId;
				if(teamId != null && teamId != undefined && teamId != ''){
					$('#teamId').combobox('setValue',teamId);
				}else {
					$('#teamId').combobox('setValue','');
				}
				
				var customerId = data.customerId;
				if(customerId != null && customerId != undefined && customerId != ''){
					$('#customerId').combobox('setValue',customerId);
				}else {
					$('#customerId').combobox('setValue','');
				}
				
				var sourceName = data.source;
				if(sourceName != null && sourceName != undefined && sourceName != ''){
					$('#source').combobox('setValue',sourceName);
					if(sourceName == '个人信息下单'){
						// 显示推荐人
						$('#referrer-tr').show();
						$('#referrerId').combobox('setValue',data.referrerId);
					}else {
						$('#referrer-tr').hide();
						$('#referrerId').combobox('setValue','0');
					}
				}else {
					$('#source').combobox('setValue','');
				}
				
			}
		},
	}).dialog('open').dialog('center');
}

// 查询
function searchFun(){
	datagrid.datagrid('load', $.serializeObject($('#searchForm')));
}

// 清除
function cleanFun() {
	$('#searchForm').form('clear');
	datagrid.datagrid('load', {});
}

// 报表导出
function exportFun(){
	
	$('#searchForm').form('submit',{
		url : getContextPath() + '/project/export',
		onSubmit : function() {
			$.growlUI('报表输出中…', '正在为您输出报表，请稍等。。。');
		},
		success : function(result) {
			
		}
	});
}

// 文件列表
function loadResourceFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#picture-condition').removeClass('hide');
		$('#p-cancel').unbind('click');
		$('#p-cancel').bind('click',resourceCancelFuc);
		var data = rows[0];
		var projectId = data.id;
		loadData(function(rList){
			$('#resource-list').empty();
			var tBody = '';
			if(rList != null && rList.length > 0){
				// 有文件列表
				$.each(rList,function(i,n){
					if(i % 2 == 0){
						tBody += '<tr class="tr-even">';
					}else {
						tBody += '<tr class="tr-single">';
					}
					tBody += '<td>' + n.irOriginalName + '</td>';
					tBody += '<td>' + n.irtype + '</td>';
					tBody += '<td>'+ n.irCreateDate +'</td>';
					tBody += '<td>';
					tBody += '<a href="'+ getContextPath() + '/getFile/' + n.irId +'">下载</a>';
					tBody += '</td>';
					tBody += '</tr>';
				});
			}
			
			$('#resource-list').append(tBody);
		}, getContextPath() + '/getResourceList', $.toJSON({
			id : projectId
		}))
	} else {
		$.message('只能选择一条记录进行查看!');
	}
}

// 取消文件列表
function resourceCancelFuc(){
	$('#picture-condition').addClass('hide');
	// TODO 清除列表内容
}