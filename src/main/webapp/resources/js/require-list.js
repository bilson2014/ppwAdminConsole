var formUrl;
var datagrid;
var pmCache;
$().ready(function() {
	syncLoadData(function (res){
		pmCache = res;
	},  getContextPath() + '/portal/employee/findSynergy', null);
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/list/require',
		idField : 'id',
		title : '产品列表',
		fitColumns : true,
		striped : true,
		loadMsg : '数据正在加载,请耐心的等待...',
		rownumbers : true,
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		} ] ],
		columns : [ [{
			field : 'indentName',
			title : '订单名称',
			width : 30,
			align : 'center'
		},{
			field : 'realName',
			title : '用户，ing',
			width : 30,
			align : 'center'
		},{
			field : 'indent_tele',
			title : '客户电话',
			width : 30,
			align : 'center',
			hidden: true
		}, {
			field : 'userCompany',
			title : '客户公司',
			align : 'center',
			width : 30
		}, {
			field : 'wechat',
			title : '客户微信',
			align : 'center',
			width : 30
		}, {
			field : 'orderDate',
			title : '下单时间',
			align : 'center',
			width : 30
		}, {
			field : 'indent_recomment',
			title : '备注',
			align : 'center',
			width : 30
		}
		
		] ],
		pagination : true,
		pageSize : 50,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar'
	});
});

//打开dialog
function updatePMDialog(id, data) {
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.message('最少需要选择一条记录以上才能进行分配!');
	} else {
		var ids = '';
		for(var i = 0 ; i < arr.length ; i++){
			ids += arr[i].id + ',';
		}
		ids = ids.substring(0,ids.length-1);
		$('#pmFm').form('clear');
		$('#pmDlg').dialog({
			modal : true,
			onOpen : function(event, ui) {
				$('#employeeId').combobox({
		            url : getContextPath() + '/portal/employee/findSynergy',
		            valueField : 'employeeId',
		            textField : 'employeeRealName'
		        });
			}
		}).dialog('open').dialog('center');
		$('#indentIds').val(ids);
	}
}
// 分配项目经理
function pmsave(){
	progressLoad();
	var indentIds = $('#indentIds').val();
	var employeeId = $('#employeeId').combobox('getValue');
	$.post(getContextPath() + '/portal/require/updatepm'
		,{
		indentIds:indentIds,
		employeeId:employeeId
		},
		function(result){
				$('#pmDlg').dialog('close');
				datagrid.datagrid('clearSelections');
				datagrid.datagrid('reload');
				progressClose();
				$.message('操作成功!');
		});
}
