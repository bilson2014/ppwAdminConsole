var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var datagrid;
//项目来源信息
var indent_resource_array = [
	{"name" : "推广"},
	{"name" : "活动"},
	{"name" : "新媒体"},
	{"name" : "渠道"},
	{"name" : "线下拓展"},
	{"name" : "市场活动"},
	{"name" : "社区运营"},
	{"name" : "自主开发"},
	{"name" : "电销"},
	{"name" : "复购"},
	{"name" : "推荐"}
];
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
						field : 'serial',
						title : '项目序列号',
						align : 'center'
					},{
						field : 'projectName',
						title : '项目名称',
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
						field : 'stage' ,
						title : '项目阶段' ,
						align : 'center'
					},{
						field : 'createTime' ,
						title : '创建时间' ,
						align : 'center',
						sortable : true 
					},{
						field : 'updateTime' ,
						title : '更新时间' ,
						align : 'center',
						sortable : true 
					},{
						field : 'employeeRealName' ,
						title : '视频管家' ,
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
						field : 'clientLevel',
						title : '客户级别',
						align : 'center',
						formatter : function(value , record , index){
							if(value == 0){
								return '<span style=color:red; >A</span>' ;
							} else if( value == 1){
								return '<span style=color:blue; >B</span>' ; 
							} else if( value == 2){
								return '<span style=color:green; >C</span>' ;
							} else if( value == 3){
								return '<span style=color:black; >S</span>' ;
							} else if( value == 4){
								return '<span style=color:orange; >D</span>' ;
							}else{
								return '<span style=color:orange; >E</span>' ;//未分级
							}
						}
					},{
						field : 'source',
						title : '项目来源',
						align : 'center'
					},{
						field : 'referrerName',
						title : '项目推荐人',
						align : 'center'
					},{
						field : 'teamName',
						title : '供应商',
						align : 'center'
					},
					{
						field : 'price',
						title : '项目预算额度',
						align : 'center',
						formatter : function(value , row , index){
							return '<span style=color:orange; >'+ row.priceFirst + ' 元 ~ ' + row.priceLast +' 元</span>' ;
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
						title : '支付供应商金额',
						align : 'center'
					},{
						field : 'description',
						title : '项目描述',
						align : 'center',
						width: 100
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
						title : '项目推荐人ID' ,
						align : 'center' ,
						hidden: true
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
		
		$('#search-userId').combobox({
			url : getContextPath() + '/portal/employee/findSynergy',
			valueField : 'employeeId',
			textField : 'employeeRealName',
			//视频管家选中后 开始是否作为协同人
			onSelect : function(record){
				$("#isSynergy").combobox('enable');
			}
		});
		
		$('#search-customerId').combobox({
			url : getContextPath() + '/portal/user/all',
			valueField : 'id',
			textField : 'userName',
			filter: function(q, row){
				if(row.userName == null)
					return false;
				return row.userName.indexOf(q) >= 0;
			}
		});
		
		$('#search-projectId').combobox({
			url : getContextPath() + '/project/getAllProject',
			valueField : 'projectName',
			textField : 'projectName'
		});
		
		$('#search-source').combobox({
			data : indent_resource_array,
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
	//add by wanglc 2016-7-11 13:25:46 只有选择视频管家才能选择是否协同,之前让协同disable兼容性有问题,改在这里提示 begin
	var employee = $('#search-userId').combobox('getValue');
	var isSynergy = $('#isSynergy').combobox('getValue');
	if((null==employee||''==employee||undefined==employee) && isSynergy==1){
		$.messager.show({
			title:'消息提示',
			msg:'请先选择视频管家',
			timeout:5000,
			showType:'slide'
		});
	return false;
	}
	//add by wanglc 2016-7-11 13:25:46 只有选择视频管家才能选择是否协同,之前让协同disable兼容性有问题,改在这里提示 end
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

//文件列表
function loadResourceFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#picture-condition').removeClass('hide');
		$('#p-cancel').unbind('click');
		$('#p-cancel').bind('click',resourceCancelFuc);
		var data = rows[0];
		var projectId = data.id;
		loadData(function(rList){
			$('#resource-table').empty();
			var tBody = '';
			if(rList != null && rList.length > 0){
				// 有文件列表
				tBody += '<tr>';
				tBody += '<th class="th-name">文件名称</th>';
				tBody += '<th class="th-type">阶段</th>';
				tBody += '<th class="th-time">上传时间</th>';
				tBody += '<th class="th-operation">操作</th>';
				tBody += '</tr>';
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
					tBody += '<a href="'+ getContextPath() + '/getDFSFile/' + n.irId +'">下载</a>';
					tBody += '</td>';
					tBody += '</tr>';
				});
			}else {
				tBody += '<tr>';
				tBody += '<td colspan="4">此项目还没有上传文件</td>';
				tBody += '</tr>';
			}
			
			$('#resource-table').append(tBody);
		}, getContextPath() + '/getResourceList', $.toJSON({
			id : projectId
		}))
	} else {
		$.message('只能选择一条记录进行查看!');
	}
}
//日志列表
function loadLogFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#log-condition').removeClass('hide');
		$('#log-cancel').unbind('click');
		$('#log-cancel').bind('click',logCancelFuc);
		var data = rows[0];
		var projectId = data.id;
		loadData(function(rList){
			$("#log-container").html("");
			var content = '';
			$.each(rList,function(i,n){
				content += '<div style="font-size:16px">在' +n.icCreateDate + " 时间    " + n.icContent + '</div>';
			});
			$("#log-container").append(content);
		}, getContextPath() + '/getAllComment', $.toJSON({
			id : projectId
		}))
	} else {
		$.message('只能选择一条记录进行查看!');
	}
}
//取消日志列表
function logCancelFuc(){
	$('#log-condition').addClass('hide');
}

// 取消文件列表
function resourceCancelFuc(){
	$('#picture-condition').addClass('hide');
	// 清除列表内容
	$('#resource-table').empty();
}