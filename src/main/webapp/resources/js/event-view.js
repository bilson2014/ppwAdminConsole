var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var resourceTree;
var datagrid;
var formUrl;
$().ready(function(){
		
	//  初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/get/events',
		idField : 'nodesEventId' ,
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
						field : 'nodesEventId',
						title : '事件ID',
						width : 120,
						align : 'center'
					},{
						field : 'nodesEventName' ,
						title : '事件名' ,
						width : 200,
						align : 'center'
					},{
						field : 'nodesEventDescription' ,
						title : '事件描述' ,
						width : 200,
						align : 'center'
					},{
						field : 'nodesEventClassName' ,
						title : '事件类路径' ,
						width : 200,
						align : 'center'
					},{
						field : 'nodesEventModel' ,
						title : '事件类型' ,
						width : 200,
						align : 'center',
						formatter : function(value,row,index){
							return '<span>'+ ((parseInt(value) == 1) ? '手动':'自动') +'</span>' ;
						}
					},{
						field : 'templateId' ,
						title : '模板' ,
						width : 200,
						align : 'center'
					},{
						field : 'dataFiller' ,
						title : '数据装载器' ,
						width : 200,
						align : 'center'
					},{
						field : 'dataFields' ,
						title : '相关属性' ,
						width : 200,
						align : 'center'
					},{
						field : 'relevantPerson' ,
						title : '涉及人物' ,
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
function openDialog(id,data){
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {
			$('#templateId').combotree({
				url : getContextPath() + '/get/template/tree',
				parentField : 'pid',
				lines : true,
			    idField : 'id',
				treeField : 'text',
				cascadeCheck : false,
				onlyLeafCheck:true,
				multiple:true
			});
			$('#nodesEventClassName').combobox({
				url : getContextPath() + '/get/eventHandles',
				valueField : "id",
				textField  : "text",
				onLoadSuccess:function(){
					
				}
			});
			$('#dataFiller').combobox({
				url : getContextPath() + '/get/datatemplates',
				valueField : "id",
				textField  : "text",
				onSelect:function(row){
					if(row != null){
						var id = row.id;
						$.ajax({
					        type: 'POST',
					        dataType : 'json',
					        url: getContextPath() + '/get/fields',   
					        data: {"templateDataKey":id},   
					        success: function(json){
					        	$('#dataFields').combotree({
									checkbox : true,
									multiple: true
					        	});
					        	$('#dataFields').combotree('loadData',json);
					        }
					    });
						$.ajax({
							type: 'POST',
							dataType : 'json',
							url: getContextPath() + '/get/personnel',   
							data: {"templateDataKey":id},   
							success: function(json){
								$('#relevantPerson').combotree({
									checkbox : true,
									multiple: true
								});
								$('#relevantPerson').combotree('loadData',json);
							}
						});
					}
				},onLoadSuccess:function(){
					if(data != null){
						var id = data.dataFiller;
						$.ajax({
					        type: 'POST',
					        dataType : 'json',
					        url: getContextPath() + '/get/fields',   
					        data: {"templateDataKey":id},   
					        success: function(json){
					        	$('#dataFields').combotree({
									checkbox : true,
									multiple: true
					        	});
					        	$('#dataFields').combotree('loadData',json);
					        	$('#dataFields').combotree('setValues',data.dataFields);
					        }
					    });
						$.ajax({
							type: 'POST',
							dataType : 'json',
							url: getContextPath() + '/get/personnel',   
							data: {"templateDataKey":id},   
							success: function(json){
								$('#relevantPerson').combotree({
									checkbox : true,
									multiple: true
								});
								$('#relevantPerson').combotree('loadData',json);
								$('#relevantPerson').combotree('setValues',data.relevantPerson);
							}
						});
					}
				}
			});
			
			
		}
	}).dialog('open').dialog('center');
}
//修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		openDialog('dlg',rows[0]);
		$('#fm').form('load',rows[0]);
		formUrl = getContextPath() + '/put/events';
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}
//增加按钮
function addFuc(){ 
	$('#fm').form('clear');
	openDialog('dlg',null);
	formUrl = getContextPath() + '/post/events';
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
function cancelFuc(){ // 注册 取消按钮
	//回滚数据 
	datagrid.datagrid('rejectChanges');
	editing = undefined;
}

function save(){
	var nodesEventId = $("#nodesEventId").val();
	var nodesEventName = $('#nodesEventName').val();
	var nodesEventDescription = $('#nodesEventDescription').val();
	var nodesEventClassName = $('#nodesEventClassName').combobox("getValue");
	var nodesEventModel = $('#nodesEventModel').combobox("getValue");
	
	var template = $("#templateId").combotree('tree');  
	var row_ = template.tree('getChecked');  
	var templateId;
	var templateType;
	if(row_.length != 1){
		$.message('只能选择一个模板');
		return;
	}
	if(row_[0].pid == "null"){
		$.message('请选择模板，不是类型！');
		return;
	}else{
		templateId = row_[0].id;
		templateType = row_[0].pid;
	}
	
	var dataFiller = $('#dataFiller').combobox("getValue");
	var dataFields = format($('#dataFields').combobox("getValues"));
	var relevantPerson = format($('#relevantPerson').combobox("getValues"));
	// 同步刷新ui
	syncLoadData(function(){
		$('#dlg').dialog('close');
		datagrid.datagrid('clearSelections');
		datagrid.datagrid('reload');
		$.message('操作成功!');
	}, formUrl, $.toJSON({
		nodesEventId : nodesEventId,
		nodesEventName : nodesEventName,
		nodesEventDescription : nodesEventDescription,
		nodesEventClassName : nodesEventClassName,
		nodesEventModel : nodesEventModel,
		templateId : templateId,
		templateType : templateType,
		dataFiller : dataFiller,
		dataFields :dataFields,
		relevantPerson : relevantPerson
	}));
}
function format(arr){
	var res = "";
	if(arr != null && arr != undefined && arr.length > 0){
		if(arr.length == 1){
			res = arr[0];
		}
		else{
			for (var int = 0; int < arr.length; int++) {
				if(int == 0)
					res+=arr[int];
				else
					res+=','+arr[int];
			}
		}
	}
	return res;
}
