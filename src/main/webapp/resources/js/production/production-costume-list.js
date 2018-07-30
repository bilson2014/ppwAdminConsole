var formUrl;
var datagrid;
var storage_node;
var min=0,max=1;
var statusList=[];
var typeIdList=[];
var typeList=[];
var accreditList=[];
var imgRatio=248/140;
var displayWidth=124;
var displayHeight=70;

var nature;
var natureName;


$().ready(function(){
	storage_node=$('#storage_node').val();
	statusList=JSON.parse($('#statusList').val());	
	
	nature=$('#nature').val();
	natureName=$('#natureName').val();
	
	
	accreditList=JSON.parse($('#accreditList').val());
	
	if(nature=='clothing'){
		typeList=JSON.parse($('#clothingTypeList').val());
	}else{
		typeList=JSON.parse($('#propsTypeList').val());
	}
	
	
	
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/production/costume-'+nature+'/list',
		idField : 'id' ,
		title : natureName+'列表' ,
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
						field : 'type' ,
						title : natureName+'类别' ,
						align : 'center' ,
						width : 200,
						formatter : function(value,row,index){	
							for(var i=0;i<typeList.length;i++){
								if(typeList[i].value==value){
									return typeList[i].text;
								}
							}
						}
					},{
						field : 'accredit' ,
						title : '授权方式' ,
						align : 'center' ,
						width : 200,
						formatter : function(value,row,index){
							for(var i=0;i<accreditList.length;i++){
								if(accreditList[i].value==value){
									return accreditList[i].text;
								}
							}							
						}
					},{
						field : 'stockNumber',
						title : '库存(套)',
						width : 150,
						align : 'center' ,
					},{
						field : 'price',
						title : '报价(元)',
						width : 150,
						align : 'center' ,
						formatter : function(value,row,index){
							if(row.accredit==2){
								return value+'/天';
							}
							return value;
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
		toolbar : '#toolbar',
		onBeforeLoad: function (param) {
            var firstLoad = $(this).attr("firstLoad");
            if (firstLoad == "false" || typeof (firstLoad) == "undefined")
            {
                $(this).attr("firstLoad","true");
                return false;
            }
            return true;
		}
	});
	
	init(nature);
	
	$('#type').combobox({
		data : typeList,
		valueField : 'value',
		textField : 'text'
	});
	
	var s_typeList=JSON.parse(JSON.stringify(typeList));
	s_typeList.unshift({'value':'','text':'--请选择--'});
	$('#search-type').combobox({
		data : s_typeList,
		valueField : 'value',
		textField : 'text'
	});
	
	$('#accredit').combobox({
		data : accreditList,
		valueField : 'value',
		textField : 'text',
		onSelect: function(record){
			if(record.value==2){
				$('#priceLabel').html("报价(元/天)");
			}else{
				$('#priceLabel').html("报价(元)");
			}
		}
	});
	
	var s_accreditList=JSON.parse(JSON.stringify(accreditList));
	s_accreditList.unshift({'value':'','text':'--请选择--'});
	$('#search-accredit').combobox({
		data : s_accreditList,
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
	
	$('#priceLabel').html("报价(元/天)");//默认报价标签
	
	formUrl = getContextPath() + '/portal/production/costume-'+nature+'/save';
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
		
		if(rows[0].accredit==2){
			$('#priceLabel').html("报价(元/天)");
		}else{
			$('#priceLabel').html("报价(元)");
		}
		
		if(rows[0].photo!=undefined && rows[0].photo!=null){
			var photos=rows[0].photo.split(";");
			for(var i=0;i<photos.length;i++){
				if(photos[i]!=null && photos[i]!=''){
					displayImg(photos[i],2);
				}	
			}
		}
		formUrl = getContextPath() + '/portal/production/costume-'+nature+'/update';
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
				$.post(getContextPath() + '/portal/production/costume-'+nature+'/delete', {ids:ids},function(result){
					console.log(result);
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
			
			setRef();
			
			return flag;
		},
		success : function(result) {
			
			console.log(result);
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
		title : natureName+'信息',
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
