var treegrid;
var formUrl;
$().ready(function(){
	treegrid = $('#treeGrid').treegrid({
		url : getContextPath() + '/portal/right/list',
		idField : 'rightId',
		treeField : 'rightName',
		parentField : 'pId',
		fit : true,
		fitColumns : false,
		border : false,
		frozenColumns : [[{
			title : '编号',
			field : 'rightId',
			width : 40
		}]],
		columns : [ [ {
			field : 'rightName',
			title : '资源名称',
			width : 200
		}, {
			field : 'url',
			title : '资源路径',
			width : 200
		}, {
			field : 'seq',
			title : '排序',
			width : 40
		}, {
			field : 'icon',
			title : '图标',
			width : 100
		}, {
			field : 'resourceType',
			title : '资源类型',
			width : 80,
			formatter : function(value, row, index) {
				switch (value) {
				case 0:
					return '<span style="color:red;" >菜单</span>' ;
				case 1:
					return '<span style="color:black;" >按钮</span>' ;
				}
			}
		}, {
			field : 'pId',
			title : '上级资源ID',
			width : 150,
			hidden : true
		}, {
			field : 'status',
			title : '状态',
			width : 40,
			formatter : function(value, row, index) {
				switch (value) {
				case 0:
					return '<span style="color:red;" >停用</span>' ;
				case 1:
					return '<span style="color:green;" >正常</span>';
				}
			}
		},{
			field : 'isCommon',
			title : '是否公用',
			width : 60,
			formatter : function(value, row, index) {
				if (value) 
					return '<span style="color:red;" >公用</span>';
				else
					return '<span style="color:black;" >非公用</span>' ;
			}
		}]],
		toolbar : '#toolbar'
	});
	
});

// 增加
function addFun(){
	openDialog('dlg');
	$('#fm').form('clear');
	formUrl = getContextPath() + '/portal/right/save';
}

// 修改
function editFun() {
	var node = treegrid.treegrid('getSelected');
	if (node) {
		openDialog('dlg');
		$('#fm').form('clear');
		$('#fm').form('load',node);
		formUrl = getContextPath() + '/portal/right/update';
	}else {
		$.message('只能选择一条记录进行修改!');
	}
}

// 删除
function delFun(){
	var node = treegrid.treegrid('getSelected');
	if(node){
		$.messager.confirm('提示信息' , '确认删除?' , function(r){
			if(r){
				var ids = node.rightId;
				$.post(getContextPath() + '/portal/right/delete', {ids:ids},function(result){
					
					// 刷新数据
					treegrid.treegrid('clearSelections');
					treegrid.treegrid('reload');
					$.message('操作成功!');
				});
			} else {
				 return ;
			}
		});
	}else {
		$.message('只能选择一条记录进行删除!');
	}
}

// 保存 操作
function saveFun(){
	var condition = $.toJSON({
		rightId : $("input[name='rightId']").val(),
		rightName : $("input[name='rightName']").val().trim(),
		url : $("input[name='url']").val().trim(),
		seq : $("input[name='seq']").val(),
		icon : $("input[name='icon']").val(),
		status : $("input[name='status']").val(),
		pId : $("input[name='pId']").val(),
		isCommon : $("input[name='isCommon']").val(),
		resourceType : $('input[name="resourceType"]').val()
	});
	
	if($('#fm').form('validate')){
		loadData(function(){
			$('#dlg').dialog('close');
			treegrid.treegrid('reload');
		}, formUrl, condition);
	}
}

function openDialog(id){
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {
			$('#pId').combotree({
			    url: getContextPath() + '/portal/right/tree',
			    parentField : 'pid',
				lines : true,
			    idField : 'id',
				treeField : 'text',
				cascadeCheck : false
			});
			
		}
	}).dialog('open').dialog('center');
}