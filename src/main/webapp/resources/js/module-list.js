var treegrid;
var formUrl;
$().ready(function(){
	treegrid = $('#treeGrid').treegrid({
		url : getContextPath() + '/portal/module/list',
		idField : 'productModuleId',
		treeField : 'moduleName',
		parentField : 'pid',
		fit : true,
		fitColumns : false,
		border : false,
		frozenColumns : [[{
			title : '编号',
			field : 'productModuleId',
			width : 40
		}]],
		columns : [ [ {
			field : 'moduleName',
			title : '资源名称',
			width : 200
		}, {
			field : 'pic',
			title : '图片路径',
			width : 200
		}, {
			field : 'moduleLevel',
			title : '等级',
			width : 200,
			formatter : function(value,row,index){
				if(value==3)return "初级";
				if(value==4)return "中级";
				if(value==5)return "高级";
			},
		}, {
			field : 'description',
			title : '描述',
			width : 200
		}, {
			field : 'modulePrice',
			title : '价格',
			width : 200
		}, {
			field : 'sortIndex',
			title : '排序',
			width : 40,
			hidden : true
		}, {
			field : 'pId',
			title : '上级资源ID',
			width : 150,
			hidden : true
		}]],
		toolbar : '#toolbar',
		onDblClickCell:function(index,field,value){
			if(index == 'pic' && null != field.pic){
				$('#dlg-showImg').dialog({
					modal : true,
					onOpen : function(event, ui) {
						$('#show-img').attr("src",getDfsHostName() + field.pic);
					}
				}).dialog('open').dialog('center');
			}
		}
	});
	
});

// 增加
function addFun(){
	openDialog('dlg');
	$('#fm').form('clear');
	formUrl = getContextPath() + '/portal/module/save';
}

// 修改
function editFun() {
	var node = treegrid.treegrid('getSelected');
	if (node) {
		openDialog('dlg');
		$('#fm').form('clear');
		$('#fm').form('load',node);
		formUrl = getContextPath() + '/portal/module/update';
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
				var ids = node.id;
				$.post(getContextPath() + '/portal/module/delete', {ids:ids},function(result){
					
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

//确认事件
function saveFun(){
	progressLoad();
	$('#fm').form('submit',{
		url : formUrl,
		onSubmit : function() {
			var flag = $(this).form('validate');
			if(!flag){
				progressClose();
			}
			return flag;
		},
		success : function(result) {
			$('#dlg').dialog('close');
			treegrid.treegrid('reload');
			progressClose();
			$.message('操作成功!');
		}
	});
}
/*// 保存 操作
function saveFun(){
	var condition = $.toJSON({
		id : $("input[name='id']").val().trim(),
		moduleName : $("input[name='moduleName']").val().trim(),
		modulePrice : $("input[name='modulePrice']").val(),
		description : $("input[name='description']").val(),
		pid : $("input[name='pid']").val(),
	});
	
	if($('#fm').form('validate')){
		loadData(function(){
			$('#dlg').dialog('close');
			treegrid.treegrid('reload');
		}, formUrl, condition);
	}
}*/

function openDialog(id){
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {
			$('#pid').combotree({
			    url: getContextPath() + '/portal/module/list',
			    parentField : 'pid',
				lines : true,
			    idField : 'id',
				treeField : 'text',
				cascadeCheck : false
			});
			
		}
	}).dialog('open').dialog('center');
}