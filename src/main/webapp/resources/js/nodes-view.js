var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var resourceTree;
var datagrid;
$().ready(function(){
		
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/get/nodes',
		idField : 'taskChainId' ,
		title : '流程模板列表' , 
		fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'taskChainId',
						title : '节点ID',
						width : 120,
						align : 'center'
					},{
						field : 'name' ,
						title : '节点名' ,
						width : 200,
						align : 'center'
					},{
						field : 'description' ,
						title : '节点描述' ,
						width : 200,
						align : 'center'
					},{
						field : 'orderAction' ,
						title : '操作',
						width : 120,
						align : 'center',
						formatter : function(value, row, index) {
							var str = '&nbsp;';
								str += $.formatString('<a href="javascript:void(0);" onclick="reviewImages('+row.d_deployment_id+')" >查看</a>');
							return str;
						}
					}]],
		pagination: true ,
		pageSize : 50,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar',
		onAfterEdit:function(index , record){
			delete record.rights;
			delete record.employees;
			$.post(flag =='add' ? getContextPath() + '/portal/role/add' : getContextPath() + '/portal/role/update', record , function(result){
				
				// 刷新数据
				datagrid.datagrid('clearSelections');
				datagrid.datagrid('reload');
				$.message('操作成功!');
			});
		}
	});
});
//打开dialog
function openDialog(id,f){
	var rows = datagrid.datagrid('getSelections');
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {
			loadData(function(msg){
				var data = formatData(msg);
				// 加载角色树
				$('#nodesEvents').combotree({
				    multiple: true,
				    required: true,
				    panelHeight : 'auto'
				});
				$('#nodesEvents').combotree('loadData',data);
				if(rows[0] != undefined){
					// id 不同填充数据
					var nodesEvents = rows[0].nodesEvents;
					if(nodesEvents.length !=null){
						var ids = new Array();
						for (var int = 0; int < nodesEvents.length; int++) {
							ids.push(nodesEvents[int].nodesEventId);
						}
						$('#nodesEvents').combotree('setValues',ids);
					}
				}
			}, '/get/events', null);
		}
	}).dialog('open').dialog('center');
}
var formUrl;
//修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		$('#fm').form('load',rows[0]);
		$('#taskChainId').val(rows[0].taskChainId);
		openDialog('dlg',null);
		formUrl = getContextPath() + '/put/node';
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}
// 添加
function addFuc(){ 
	$('#fm').form('clear');
	openDialog('dlg',null);
	formUrl = getContextPath() + '/post/nodes';
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
					ids += arr[i].nodesEventId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/delete/events', {eventIds:ids},function(result){
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
function formatData(src){
	var newarray =new Array();
	if(src!=null && src.length > 0){
		for (var i in src) {
			newarray.push(new tree(src[i].nodesEventId,src[i].nodesEventName));
		}
	}
	return newarray;
}
function tree(id,text){
	this.id = id;
	this.text= text;
}
function save(){
	var nodesEvents =  $('#nodesEvents').combotree("getValues")
	var nodes = new Array();
	for ( var i in nodesEvents) {
		nodes.push({nodesEventId:nodesEvents[i]});
	}
	loadData(function(msg){
		$('#dlg').dialog('close');
		datagrid.datagrid('clearSelections');
		datagrid.datagrid('reload');
		$.message('操作成功!');
	}, formUrl, $.toJSON({
		taskChainId : $('#taskChainId').val(),
		name : $('#name').val().trim(),
		description : $('#description').val().trim(),
		nodesEvents : nodes
	}));
}
