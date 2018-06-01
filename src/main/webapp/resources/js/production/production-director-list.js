var formUrl;
var datagrid;
var imgNo=1; 
var storage_node;
var citys;
var specialtyList;
var min=1,max=1;
var statusList;
var typeIdList;

$().ready(function(){
	storage_node=$('#storage_node').val();
	statusList=JSON.parse($('#statusList').val());
	
	syncLoadData(function(res) {
		citys = res;
	}, getContextPath() + '/portal/all/citys', null);
	
	//擅长领域
	specialtyList=[{'value':1,'text':'宣传片'},{'value':2,'text':'广告'},
		{'value':3,'text':'MV'},{'value':4,'text':'剧情微电影'},
		{'value':5,'text':'短视频'},{'value':6,'text':'剧情长篇'},
		{'value':7,'text':'纪录片'},{'value':8,'text':'栏目（节目）'},
		{'value':9,'text':'话剧（舞台）'},{'value':10,'text':'演出（活动）'}];
	
	init('director');
	
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/production/director/list',
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
						field : 'specialty' ,
						title : '擅长领域' ,
						align : 'center' ,
						width : 200,
						formatter : function(value,row,index){
							if(value!=null && value!=''){
								var values=value.split(',');
								var text='';
								for(var each in values){
									for(var i=0;i<specialtyList.length;i++){
										if(each==specialtyList[i].value){
											text+=specialtyList[i].text+',';
										}
									}
								}
								return text;
							}
							
						}
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
	
	$('#city').combobox({
		data : citys,
		valueField : 'cityID',
		textField : 'city'
	});
	$('#search-city').combobox({
		data : citys,
		valueField : 'cityID',
		textField : 'city'
	});
	$('#specialty').combobox({
		data : specialtyList,
		valueField : 'value',
		textField : 'text',
		multiple:true
	});
	
	var s_specialtyList=JSON.parse(JSON.stringify(specialtyList));
	s_specialtyList.unshift({'value':'','text':'--请选择--'});
	$('#search-specialty').combobox({
		data : s_specialtyList,
		valueField : 'value',
		textField : 'text'
	});
	
	/*$('#teamId').combobox({
		url : getContextPath() + '/portal/product/init',
		valueField : 'teamId',
		textField : 'teamName',
		filter: function(q, row){
			if(row.teamName == null)
				return false;
			return row.teamName.indexOf(q) >= 0;
		}
	});
	
	
	 $("#uploadDiv").on("click",function(){  
	     var uploadFile = '<input type="file" id="videoFile" style="width:100%" name="uploadFiles" class="p-file" multiple="multiple" onchange="addImg(this)" accept="image/gif,image/jpeg,image/jpg,image/png"/>';  
	     $("#fileDiv").append($(uploadFile));  
	     
	     $("#videoFile").click();          
	 }); */
	
});


//增加
function addFuc(){
	imgNo=1;
	delImg='';
	$('#fm').form('clear');
	$("#imgDisplay").empty();
	$("#fileDiv").empty();
	
	//默认推荐人
	$("#referrer").combobox("setValue", $('#default_referrer').val());
	//默认审核通过
	$('#status').combobox('setValue',1);
	
	formUrl = getContextPath() + '/portal/production/director/save';
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
		$("#fileDiv").empty();
		
		$('#fm').form('load',rows[0]);
		
		if(rows[0].photo!=undefined && rows[0].photo!=null){
			var photos=rows[0].photo.split(";");
			for(var i=0;i<photos.length;i++){
				if(photos[i]!=null && photos[i]!=''){
					displayImg(storage_node+photos[i],photos[i],2);
					imgNo++;
				}	
			}
		}
		formUrl = getContextPath() + '/portal/production/director/update';
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
				$.post(getContextPath() + '/portal/production/director/delete', {ids:ids},function(result){
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
		title : '导演信息',
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



/*function addImg(obj) {
    var windowURL = window.URL || window.webkitURL;
    
    for(var i=0;i<obj.files.length;i++){
    	
    	var loadImg = windowURL.createObjectURL(obj.files[i]);
        imgNo=imgNo+1;
        displayImg(loadImg,obj.files[i].name,1);
    }
    $(obj).removeAttr("id"); 
} 

function displayImg(loadImg,url,type){ 
	var html = '<div><img src='+loadImg+' id="displayImg'+imgNo+'" class="aptimg">'+
	'<a onclick="delFuc('+imgNo+',\''+url+'\','+type+');" href="javascript:void(0);" id="displayRemove'+imgNo+'" class="easyui-linkbutton aptDel" '+
	'data-options="plain:true,iconCls:\'icon-cancel\'"></a></div>';
	
	var newImg = $(html).appendTo("#imgDisplay");
	 //渲染easyUI
	 $.parser.parse($(newImg));
}

var delImg="";

function delFuc(no,url,type){
	
	if(url!=undefined && url != null && url!=''){
		delImg=delImg+";"+url;
		$('#delImg').val(delImg);
	}
	//移除预览
	$('#displayImg'+no).remove();
	$('#displayRemove'+no).remove();
	
}*/
