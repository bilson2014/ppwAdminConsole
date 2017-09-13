var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var formUrl;
var datagrid;
$().ready(function(){
		
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath()+ '/portal/employee/list',
		idField : 'employeeId' ,
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
						field : 'employeeLoginName',
						title : '登录名',
						width : 60,
						align : 'center'
					},{
						field : 'employeeRealName',
						title : '姓名',
						width : 80,
						align : 'center'
					},{
						field : 'hireDate',
						title : '入职日期',
						align : 'center',
						formatter : function(value,row,index){
							var time = new Date(value); 
							return time.Format("yyyy-MM-dd"); 
						}
					},{
						field : 'phoneNumber' ,
						title : '手机号码',
						align : 'left'
					},{
						field : 'email' ,
						title : '邮箱',
						align : 'left'
					},{
						field : 'roleNameGroup' ,
						title : '角色',
						align : 'left'
					},{
						field : 'roleIds' ,
						title : '人员列表',
						align : 'center',
						hidden : true
					},{
						field : 'dimissionStatus' ,
						title : '离职状态',
						align : 'center',
						formatter : function(value , record , index){
							if(value == 0){
								return '<span>在职</span>' ;
							} else if( value == 1){
								return '<span>离职</span>' ; 
							} else if( value == 2){
								return '<span>测试</span>' ; 
							} 
						}
					},{
						field : 'employeeDescription' ,
						title : '备注',
						width : 160,
						align : 'center'
					}]],
		pagination: true ,
		pageSize : 50,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar'
	});
	employee.initData();
});

var employee = {
	initData : function() {
		$('#roleId').combobox({
		    url: getContextPath() + '/portal/role/findAll',
			valueField : 'roleId',
			textField : 'roleName'
		});
	}
		
}

// 增加
function addFuc(){
	$('#fm').form('clear');
	openDialog('dlg',null,null);
	formUrl = getContextPath() + '/portal/employee/save';
	$('input[name="employeeId"]').val(0);
}

// 修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		$('#fm').form('load',rows[0]);
		openDialog('dlg',rows[0].roleIds,rows[0].employeeId);
		formUrl = getContextPath() + '/portal/employee/update';
	} else {
		$.message('只能选择一条记录进行修改!');
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
			
			if(flag) {
				
				var ps = $('input[name="employeePassword"]').val().trim();
				if(ps != null && ps != '' && ps != undefined){
					$('input[name="employeePassword"]').val(Encrypt(ps));
				}
				
				// 判断 入职日期是否为空
				var hireDate = $('input[name="hireDate"]').val();
				if(hireDate == null || hireDate == undefined || hireDate == ''){
					flag = false;
					$.message('请选择入职时间!');
				}
			}
			
			if(!flag){
				progressClose();
			}
			return flag;
		},
		success : function(result) {
			$('#dlg').dialog('close');
			datagrid.datagrid('clearSelections');
			datagrid.datagrid('reload');
			progressClose();
			$.message('操作成功!');
		}
	});
}

// 打开dialog
function openDialog(id,data,employeeId){
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {
			// 加载角色树
			$('#roleIds').combotree({
			    url: getContextPath() + '/portal/role/tree',
			    multiple: true,
			    required: true,
			    panelHeight : 200,
			    value : data,
			    onLoadSuccess : function() {
			    	createRoleInfo();
			    },
				onCheck : function(node,checked){
					// 改变选择后，触发功能
					createRoleInfo();
				}
			});
			//流程角色
			$('#groupId').combobox({
			    url: getContextPath() + '/portal/project/groups',
				valueField : 'value',
				textField : 'text',
				onLoadSuccess : function() {
					//获取用户流程角色
					if(employeeId!=null){
						syncLoadData(function(res) {
							
							$("#groupId").combobox('setValue',res.groupId);
							console.log(res.groupId);
//							console.log($("#groupId").combobox('getValue'));
						}, getContextPath() + "/portal/project/userGroup/"+employeeId, null);
					}
				}
			});
			
		}
	}).dialog('open').dialog('center');
}

function createRoleInfo() {
	$('#roleDesc').empty();
	var $roleDescBody = '';
	
	$.each($("#roleIds").combotree('getValues'), function(i,n) {
		var node = $('#roleIds').combotree('tree').tree('find',n);
		$roleDescBody += '<div class="fitem">';
		$roleDescBody += '<label>'+ node.text +':</label>';
		$roleDescBody += '<label style="width:70%;">'+ node.desc +'</label>';
		$roleDescBody += '</div>';
	});
	$('#roleDesc').append($roleDescBody);
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