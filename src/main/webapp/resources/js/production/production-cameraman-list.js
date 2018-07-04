var formUrl;
var datagrid;
var storage_node;
var specialtyList;
var min=1,max=1;
var statusList;
var typeIdList;
var imgRatio=162/216;
var displayWidth=81;
var displayHeight=108;

$().ready(function(){
	storage_node=$('#storage_node').val();
	statusList=JSON.parse($('#statusList').val());	
	
	//特殊技能
	specialSkillList=[{'value':1,'text':'水下拍摄'},{'value':2,'text':'航拍'}];
	
	init('cameraman');
	
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/production/cameraman/list',
		idField : 'id' ,
		title : '导演列表' ,
		fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'name',
						title : '姓名',
						width : 150,
						align : 'center' ,
					},{
						field : 'typeId' ,
						title : '标准化分级' ,
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
						field : 'price',
						title : '报价(元/天)',
						width : 150,
						align : 'center' ,
					},{
						field : 'specialSkill' ,
						title : '特殊技能' ,
						align : 'center' ,
						width : 200,
						formatter : function(value,row,index){
							for(var i=0;i<specialSkillList.length;i++){
								if(value==specialSkillList[i].value){										
									return specialSkillList[i].text;
								}
							}
								
						return "";
						}
					},{
						field : 'city' ,
						title : '城市' ,
						align : 'center' ,
						width : 200,
						formatter : function(value,row,index){
							if(citys==undefined || citys==null){
								return "";
							}
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
	
	
	$('#specialSkill').combobox({
		data : specialSkillList,
		valueField : 'value',
		textField : 'text'
	});
	
	var s_specialSkillList=JSON.parse(JSON.stringify(specialSkillList));
	s_specialSkillList.unshift({'value':'','text':'--请选择--'});
	$('#search-specialSkill').combobox({
		data : s_specialSkillList,
		valueField : 'value',
		textField : 'text'
	});
	
});

//增加
function addFuc(){
	imgNo=1;
	delImg='';
	$('#fm').form('clear');
	$("#imgDisplay").empty();
	
	//默认推荐人
	$("#referrer").combobox("setValue", $('#default_referrer').val());
	//默认审核通过
	$('#status').combobox('setValue',1);
	
	formUrl = getContextPath() + '/portal/production/cameraman/save';
	openDialog(null);
	$('#id').val(0);
}

// 修改
function editFuc(){
	imgNo=1;
	delImg='';
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		$("#imgDisplay").empty();
		$('#fm').form('load',rows[0]);
		
		if(rows[0].typeId==null || rows[0].typeId==undefined || rows[0].typeId==""){
			$('#typeId').combotree("clear");
		}
		
		if(rows[0].photo!=undefined && rows[0].photo!=null){
			var photos=rows[0].photo.split(";");
			for(var i=0;i<photos.length;i++){
				if(photos[i]!=null && photos[i]!=''){
					displayImg(photos[i],2);
				}	
			}
		}
		formUrl = getContextPath() + '/portal/production/cameraman/update';
		openDialog(rows[0]);
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}

function delFuca(){
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
				$.post(getContextPath() + '/portal/production/cameraman/delete', {ids:ids},function(result){
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
			
			var photoFlag=true;
			if(flag){
				photoFlag=validatePhoto();
			}
		
			if(!flag || !photoFlag){
				progressClose();
				return false;
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
		title : '摄影师信息',
		modal : true,
		width : 530,
		height : 530,
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
