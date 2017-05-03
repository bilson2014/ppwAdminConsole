var editing; // 判断用户是否处于编辑状态
var flag; // 判断新增和修改方法
var formUrl;
var datagrid;
$().ready(function() {
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/scene/list',
		idField : 'sceneId',
		title : '场景列表',
		fitColumns : true,
		striped : true,
		loadMsg : '数据正在加载,请耐心的等待...',
		rownumbers : true,
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		} ] ],
		columns : [ [ {
			field : 'sceneId',
			title : 'ID',
			width : 60,
			align : 'center'
		}, {
			field : 'sceneName',
			title : '名称',
			width : 60,
			align : 'center'
		}, {
			field : 'sceneDescription',
			title : '描述',
			width : 60,
			align : 'center'
		}, {
			field : 'createTime',
			title : '创建时间',
			align : 'center',
			width : 60
		}, {
			field : 'updateTime',
			title : '更新时间',
			align : 'center',
			width : 60
		}] ],
		pagination : true,
		pageSize : 50,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar'
	});
});
// 打开dialog
function openDialog(id, data) {
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {
		}
	}).dialog('open').dialog('center');
}

// 增加
function addFuc() {
	$('#fm').form('clear');
	openDialog('dlg', null);
	formUrl =getContextPath()+'/portal/scene/save';
}
// 修改
function editFuc() {
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		$('#fm').form('load',rows[0]);
		formUrl = getContextPath() + '/portal/scene/update';
		openDialog('dlg',rows[0]);
	} else {
		$.message('只能选择一条记录进行修改!');
	}
	
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
					ids += arr[i].sceneId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/scene/delete', {ids:ids},function(result){
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

function save(){
	progressLoad();
	$('#fm').form('submit',{
		url : formUrl,
		success : function(result) {
			progressClose();
			$('#dlg').dialog('close');
			datagrid.datagrid('clearSelections');
			datagrid.datagrid('reload');
			progressClose();
			$.message(result.result);
		}
	});
}
function searchFun(){
	datagrid.datagrid('load', $.serializeObject($('#searchForm')));
}
function cleanFun(){
	$('#searchFormactivityName').val('');
}
