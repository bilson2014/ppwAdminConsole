var formUrl;
var datagrid;
var storage_node;
var typeList;
var min=1,max=4;
var statusList;
var typeIdList;


$().ready(function(){
	storage_node=$('#storage_node').val();
	statusList=JSON.parse($('#statusList').val());
	
	/*typeList=[{'value':1,'text':'摄影设备'},{'value':2,'text':'摄影辅助'},
		{'value':3,'text':'镜头设备'},{'value':4,'text':'灯光设备'},
		{'value':5,'text':'灯光附件'},{'value':6,'text':'录音设备'},
		{'value':7,'text':'特种设备'},{'value':8,'text':'其他设备'}];*/
//	typeList=JSON.parse($('#typeList').val());
	
	init('device');
	
	
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/production/device/list',
		idField : 'id' ,
		title : '设备列表' ,
		fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'typeId' ,
						title : '名称' ,
						align : 'center' ,
						width : 200,
						formatter : function(value,row,index){
							for(var i=0;i<typeIdList.length;i++){
								if(typeIdList[i].id==value){
									return typeIdList[i].text;
								}
							}							
						}
					},{
						field : 'type' ,
						title : '类型' ,
						align : 'center' ,
						width : 200,
						formatter : function(value,row,index){
							for(var i=0;i<typeIdList.length;i++){
							if(typeIdList[i].id==value){
								return typeIdList[i].text;
							}}
						}
					},{
						field : 'quantity',
						title : '数量',
						width : 150,
						align : 'center' ,
					},{
						field : 'price',
						title : '报价(元/天)',
						width : 150,
						align : 'center' ,
					},{
						field : 'city' ,
						title : '城市' ,
						align : 'center' ,
						width : 200,
						formatter : function(value,row,index){
							for(var i=0;i<citys.length;i++){
								if(citys[i].cityID==value){
									return citys[i].city;
								}
							}
						}
					},{
						field : 'teamName' ,
						title : '供应商' ,
						align : 'center' ,
						width : 200
					},{
						field : 'referrerName' ,
						title : '推荐人' ,
						align : 'center' ,
						width : 200
					},{
						field : 'status' ,
						title : '审核状态' ,
						align : 'center' ,
						width : 200,
						formatter : function(value,row,index){
							for(var i=0;i<statusList.length;i++){
								if(statusList[i].value==value){
									return statusList[i].text;
								}
								
							}							
						}
					}
					]] ,
		pagination: true ,
		pageSize : 50,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar'
	});
	

	$('#search-typeId').combobox({
		data : [],
		valueField : 'key',
		textField : 'value'
	});	
	$('#typeId').combobox({
		data : [],
		valueField : 'key',
		textField : 'value'
	});	
	
	loadData(function(res) {
		typeList = res;
		
		var s_typeList=JSON.parse(JSON.stringify(typeList));
		s_typeList.unshift({'key':'','name':'--请选择--'});
		$('#search-type').combobox({
			data : s_typeList,
			valueField : 'key',
			textField : 'value',
			onChange : function(n,o) {
				var type = $('#search-typeId').combobox('getValue');
				if(type != '' && type != null) {
					// 清空
					$('#search-typeId').combobox('setValue','');
				}
				$('#search-typeId').combobox('reload',getContextPath()
						+ '/portal/quotationtype/production/children?typeId='+n);
			}
		});
		 
	}, getContextPath() + '/portal/quotationtype/production/children?productionType=device', null);
	
	

});


//增加
function addFuc(){
	$('#fm').form('clear');
	//默认推荐人
	$("#referrer").combobox("setValue", $('#default_referrer').val());
	
	$('#type').combobox({
		data : typeList,
		valueField : 'key',
		textField : 'value',
		onChange : function(n,o) {
			var type = $('#typeId').combobox('getValue');
			if(type != '' && type != null) {
				// 清空
				$('#typeId').combobox('setValue','');
			}
			$('#typeId').combobox('reload',getContextPath()
					+ '/portal/quotationtype/production/children?typeId='+n);
		}
	});
	//默认审核通过
	$('#status').combobox('setValue',1);
	
	formUrl = getContextPath() + '/portal/production/device/save';
	openDialog(null);
	$('#id').val(0);
}

// 修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');	
		$('#fm').form('load',rows[0]);
		
		$('#type').combobox({
			data : typeList,
			valueField : 'key',
			textField : 'value',
			onChange : function(n,o) {
				var type = $('#typeId').combobox('getValue');
				if(type != '' && type != null) {
					// 清空
					$('#typeId').combobox('setValue','');
				}
				$('#typeId').combobox('reload',getContextPath()
						+ '/portal/quotationtype/production/children?typeId='+n);
			}
		});
		$('#type').combobox('setValue',
				rows[0].type);
		$('#typeId').combobox('setValue',
				rows[0].typeId);
		
		formUrl = getContextPath() + '/portal/production/device/update';
		openDialog(rows[0]);
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
		$.messager.confirm('提示信息' , '确认删除?' , function(r){
			if(r){
				var ids = '';
				for(var i = 0 ; i < arr.length ; i++){
					ids += arr[i].id + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/production/device/delete', {ids:ids},function(result){
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

// 确认事件
function save(){
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
			datagrid.datagrid('clearSelections');
			datagrid.datagrid('reload');
			progressClose();
			$.message('操作成功!');
		}
	});
}

function openDialog(data){
	$('#dlg').dialog({
		title : '设备信息',
		modal : true,
		width : 530,
		height : 400,
		onOpen : function(event, ui) {
			
		},
		onBeforeClose: function (event, ui) {
		
		}
	}).dialog('open').dialog('center');
}

//查询
function searchFun(){
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

