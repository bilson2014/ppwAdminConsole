var formUrl;
var datagrid;
var pmCache;
$().ready(function() {
	syncLoadData(function (res){
		pmCache = res;
	},  getContextPath() + '/portal/employee/findSynergy', null);
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/require/list',
		idField : 'requireId',
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
			field : 'employeeId',
			title : '项目经理',
			width : 30,
			align : 'center',
			formatter : function(value,row,index){
				if(value != null && value != undefined && pmCache !=null && pmCache.length >0){
					for (var int = 0; int < pmCache.length; int++) {
						var item = pmCache[int];
						if(item.employeeId == value){
							return '<span style="color:red">'+item.employeeRealName+'</span>';
						}
					}
				}
				return '';
			}
		},{
			field : 'requireJson',
			title : '需求表',
			width : 120,
			align : 'center',
			formatter : function(value,row,index){
				if(value != null && value != undefined){
					var json = $.evalJSON(value);
					var jsonHtml = '';
					jsonHtml += "适用场景："+json.scene + '  ';
					jsonHtml += "受众："+json.audience+ '  ';
					jsonHtml += "核心信息："+json.coreMessage+ '  ';
					jsonHtml += "时长："+json.time+ '  ';
					jsonHtml += "预算："+json.budget+ '  ';
					jsonHtml += "是否有样片："+json.sample+ '  ';
					jsonHtml += "调性："+json.tonal;
					return jsonHtml;
				}
				return '';
			}
		},{
			field : 'wechat',
			title : '客户微信',
			width : 30,
			align : 'center',
			hidden: true
		}, {
			field : 'createTime',
			title : '创建时间',
			align : 'center',
			width : 30
		}, {
			field : 'updateTime',
			title : '更新时间',
			align : 'center',
			width : 30
		}] ],
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
			ids += arr[i].requireId + ',';
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
		$('#requireIds').val(ids);
	}
}

function pmsave(){
	progressLoad();
	var requireIds = $('#requireIds').val();
	var employeeId = $('#employeeId').combobox('getValue');
	$.post(getContextPath() + '/portal/require/updatepm'
		,{
		requireIds:requireIds,
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

function delFuc(){
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.message('请选择进行删除操作!');
	} else {
		$.messager.confirm('提示信息' , '确认删除?' , function(r){
			if(r){
				var ids = '';
				for(var i = 0 ; i < arr.length ; i++){
					ids += arr[i].requireId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/require/delete', {requireIds:ids},function(result){
					// 刷新数据
					datagrid.datagrid('clearSelections');
					datagrid.datagrid('reload');
					$.message('操作成功!');
				});
			} else {
				 return "";
			}
		});
	}
}