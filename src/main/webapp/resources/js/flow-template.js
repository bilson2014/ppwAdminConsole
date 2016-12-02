var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var resourceTree;
var datagrid;
var createTemplateDefaultJson=$.parseJSON('[{"id":"startEvent","name":"Start","nodeType":null,"index":-1,"allowSkip"'+
		':false,"description":"startEvent"},{"id":"endEvent","name":"End","nodeType":null,"index":-2,"allowSkip":false,"description":"endEvent"}]');
$().ready(function(){
		
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/all/template',
		idField : 'id' ,
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
						field : 'id',
						title : '流程ID',
						width : 120,
						align : 'center'
					},{
						field : 'name' ,
						title : '流程名' ,
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
function reviewImages(d_deployment_id){
	var imgPath = getHostName() + '/get/image/' + d_deployment_id;
	$('#picture-condition').removeClass('hide');
	$('#productPicture').removeClass('hide');
	$('#productPicture').attr('src',imgPath);
	$('#p-cancel').on('click',function(){
		$('#picture-condition').addClass('hide');
		$('#productPicture').attr('src','');
	});
}
//增加
function addFuc(){
	 $('#templateId').textbox('textbox').attr('readonly',false);
	 $('#templateform').form('clear');
	 createFlowNodeView();
	 $('#editTable').datagrid("loadData",createTemplateDefaultJson);
	 openDialog('dlg');
}
// 修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	$('#templateId').textbox('textbox').attr('readonly',true);
	$('#templateform').form('clear');
	if(rows.length == 1){
		var templateId = rows[0].d_id+'';
		syncLoadData(function(res){
			 createFlowNodeView();
			 $('#editTable').datagrid("loadData",res.flowNodes);
			 $('#editTable').datagrid('enableDnd');
			 openDialog('dlg');
		}, '/get/template', $.toJSON({
			"d_id" : templateId
		}));
	    $("#templateId").textbox('setValue',rows[0].id);
	    $("#templateName").textbox('setValue',rows[0].name);
	    $("#d_id").val(templateId);
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}

function createFlowNodeView(){
	$('#editTemplate').removeClass('hide');
	// cache treeView data
	var tree;
	var tree2;
	syncLoadData(function(msg){
		tree = msg;
	}, getContextPath() + '/get/event/tree',null);
	syncLoadData(function(msg){
		tree2 = msg;
	}, getContextPath() + '/portal/role/tree2',null);
		$('#editTable').datagrid({
			rownumbers : true ,
			fitColumns : true ,
			striped : true ,
			frozenColumns : [[
			  				{field : 'ck' , checkbox:true}
			  		]],
			columns:[[
						{
							field : 'id',
							title : '节点ID',
							width : 120,
							align : 'center',
							editor : {
								type : 'validatebox' ,
								options : {
									required : true , 
									missingMessage : '请填写角色名!',
									disabled:true
								}
							}
						},{
							field : 'name' ,
							title : '节点名' ,
							width : 200,
							align : 'center',
							editor : {
								type : 'validatebox' ,
								options : {
									required : true , 
									missingMessage : '请填写角色名!'
								}
							}
						},{ 
							field : 'events' ,
							title : '任务链' ,
							width : 200,
							align : 'center',
							editor : {
								type : 'combotree' ,
								options : {
									valueField: 'id', textField: 'text',
									data : tree,
								    multiple: true,
								    required: true,
								    panelHeight : 'auto',
								    value : null,
									required : true 
								}
							},
							formatter : function(value, row, index) {
								if( row.events != undefined){
									var ids = row.events.split(',');
									var str = '';
									var index = 0;
										if(tree!=null && tree.length >0){
											for (var int = 0; int < ids.length; int++) {
												for ( var role in tree) {
													if(ids[int] == tree[role].id){
														if(index ==0)
															str+=tree[role].text;
														else
															str+=','+tree[role].text;
														index++;
													}
												}
											}
										}
								}
								return str;
							}
						},{ // TODO
							field : 'allowSkip' ,
							title : '允许跳过' ,
							width : 200,
							align : 'center',
							editor : {
								type : 'combobox' ,
								options : {
									required : true , 
									missingMessage : '请填写角色名!',
									panelHeight: 'auto',
									valueField : 'id',
									textField : 'text',
									data : [{'id':'true','text':'true'},{'id':'false','text':'false'}]
								}
							}
						},{
							field : 'assignee' ,
							title : '控制人' ,
							width : 200,
							align : 'center',
							editor : {
								type : 'combotree' ,
								options : {
									valueField: 'id', textField: 'text',
									data : tree2,
								    multiple: true,
								    panelHeight : 'auto',
								    value : null
								}
							},formatter : function(value, row, index) {
								var str = '';
								var ids;
								var index = 0;
								var assignee = '';
								if(row.assignee!=undefined){
									assignee =  row.assignee;
									if(assignee != ''){
										ids = assignee.split(',');
											if(tree2.length > 0){
												for (var int = 0; int < ids.length; int++) {
													for ( var role in tree2) {
														if(ids[int] == tree2[role].id){
															if(index ==0)
																str+=tree2[role].text;
															else
																str+=','+tree2[role].text;
															index++;
														}
													}
												}
											}
										return str;
									}
								}
								return '';
							}
						},{ // TODO
							field : 'description' ,
							title : '描述' ,
							width : 200,
							align : 'center',
							editor : {
								type : 'textbox' ,
								options : {
									required : true , 
									missingMessage : '请填写描述!',
									panelHeight: 'auto'
								}
							}
						}]]
					});
	
}
function openDialog(id){
	$('#' + id).dialog({
		onOpen : function(event, ui) {
			
		},
		onClose:function(){
			var item = $('#editTable').datagrid('getRows'); 
			if (item) { 
				for (var i = item.length - 1; i >= 0; i--) { 
					var index = $('#editTable').datagrid('getRowIndex', item[i]); 
					$('#editTable').datagrid('deleteRow', index); 
				} 
			} 
		}
	}).dialog('open').dialog('center');
}
///////////////////////////// 弹出框  ///////////////////////////////////
//增加
function addFuc2(){
	if(editing == undefined){
		flag = 'add';
		//1 先取消所有的选中状态
		$('#editTable').datagrid('unselectAll');
		//2追加一行
		$('#editTable').datagrid('insertRow',{
			// 在最后一行之前插入
			index: $('#editTable').datagrid('getRows').length -1,
		    row: {}
		});
		//3获取当前页的行号
		editing = $('#editTable').datagrid('getRows').length -2;
		//4开启编辑状态
		$('#editTable').datagrid('beginEdit', editing);
	}
}
//保存
function saveFuc2(){
	//保存之前进行数据的校验 , 然后结束编辑并释放编辑状态字段 
	$('#editTable').datagrid('beginEdit', editing);
	if($('#editTable').datagrid('validateRow',editing)){
		$('#editTable').datagrid('endEdit', editing);
		 $('#editTable').datagrid('enableDnd');
		editing = undefined;
	}
}
//取消	
function cancelFuc2(){
	//回滚数据 
	$('#editTable').datagrid('rejectChanges');
	editing = undefined;
}
// 删除
function delFuc2(){
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.messager.show({
			title:'提示信息',
			msg:'请选择进行删除操作!'
		});
	} else {
		$.messager.confirm('提示信息' , '确认删除?' , function(r){
			if(r){
				var row = $('#editTable').datagrid('getSelections');
				for (var int = 0; int < row.length; int++) {
					var index = $('#editTable').datagrid('getRowIndex',row[int]);
			         $('#editTable').datagrid('deleteRow',index); 
				}
			} else {
				 return ;
			}
		});
	}
}
//修改
function editFuc2(){
	var arr = $('#editTable').datagrid('getSelections');
	if(arr.length != 1){
		$.messager.show({
			title:'提示信息',
			msg:'只能选择一条记录进行修改!'
		});
	} else {
		if(editing == undefined){
			flag = 'edit';
			//根据行记录对象获取该行的索引位置
			editing = $('#editTable').datagrid('getRowIndex' , arr[0]);
			//开启编辑状态
			$('#editTable').datagrid('beginEdit',editing);
			var id = $("#editTable").datagrid('getEditor',{index:0,field:'id'});
			var name = $("#editTable").datagrid('getEditor',{index:0,field:'name'});
			if(id.oldHtml == 'startEvent' || id.oldHtml == 'endEvent'){
				id.target.attr("readonly","readonly");
				name.target.attr("readonly","readonly");
			}
		}
	}
}
function exitFuc2() {
	$('#dlg').dialog('close');
	editing = undefined;
}

function checkData(){
	var res = build();
	if(res!=null){
		loadData(function(msg){
			if(msg.errorCode == 200){
				$('#dlg').dialog('close');
				editing = undefined;
				datagrid.datagrid('reload');
				$.messager.show({
					title:'提示信息',
					msg: msg.errorMsg
				});
			}else{
				$.messager.show({
					title:'提示信息',
					msg:'更新失败！'
				});
			}
		}, getContextPath()+'/put/template', $.toJSON(res));	
	}
}

function build(){ //TODO
	var arr=$('#editTable').datagrid('getData').rows;
	if(arr.length > 0){
		var ft =new FlowTemplate();
		ft.setId($('#templateId').textbox('getValue'));
		ft.setName($('#templateName').textbox('getValue'));
		ft.setDId($("#d_id").val());
		var flowNodesarray = new Array();
		for ( var i in arr) { // 重新索引
			var taskChainId = arr[i].taskChainId;
			var allowSkip = arr[i].allowSkip;
			var assignee =  arr[i].assignee;
			var nid=  arr[i].id;
			var nname = arr[i].name;
			var events = arr[i].events;
			var description = arr[i].description;
			var flowNodes = new FlowNodes(nid,nname,i, taskChainId,allowSkip,assignee,events,description);
			flowNodesarray.push(flowNodes);
		}		
		ft.setflowNodes(flowNodesarray);
		return ft;
	}
	return null;
}
// ------------------------实体映射---------------------
// 模板实体
function FlowTemplate(){
	this.id='';
	this.d_id=d_id;
	this.name='';
	this.flowNodes=null;
}
FlowTemplate.prototype ={
	setId:function(id){
		this.id=id;
	},
	setName:function(name){
		this.name=name;
	},
	setflowNodes:function(flowNodes){
		this.flowNodes=flowNodes;
	},
	setDId:function(d_id){
		this.d_id=d_id;
	}
}
// 节点实体
function FlowNodes(id,name,index,taskChainId,allowSkip,assignee,events,description){
	this.id =id;
	this.name =name;
	this.index =index;
	this.taskChainId = taskChainId;
	this.allowSkip = allowSkip;
	this.assignee = assignee;
	this.events = events;
	this.description = description;
}