var formUrl;
var datagrid;
var imgNo=1; 
var storage_node;
var citys;
var zoneList;
var min=1,max=6;
var statusList;

$().ready(function(){
	storage_node=$('#storage_node').val();
	statusList=JSON.parse($('#statusList').val());
	syncLoadData(function(res) {
		citys = res;
	}, getContextPath() + '/portal/all/citys', null);
	

	zoneList=[{'value':1,'text':'中国汉族'},{'value':2,'text':'中国其他少数民族'},
		{'value':3,'text':'印巴裔'},
		{'value':4,'text':'日韩裔'},{'value':5,'text':'东南亚裔'},
		{'value':6,'text':'欧洲裔'},{'value':7,'text':'东欧裔'},
		{'value':8,'text':'北美裔'},{'value':9,'text':'拉丁裔'},
		{'value':10,'text':'非洲裔'},{'value':11,'text':'澳洲裔'}];
	
	
	
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/production/actor/list',
		idField : 'id' ,
		title : '演员列表' ,
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
						field : 'sex' ,
						title : '性别' ,
						align : 'center' ,
						width : 200,
						formatter : function(value,row,index){
							if(value=="1"){
								return "男";
							}else if(value=="2"){
								return "女";
							}
						}
					},{
						field : 'age',
						title : '年龄',
						width : 150,
						align : 'center' ,
					},{
						field : 'price',
						title : '价格',
						width : 150,
						align : 'center' ,
					},{
						field : 'zone' ,
						title : '种族' ,
						align : 'center' ,
						width : 200,
						formatter : function(value,row,index){
							for(var i=0;i<zoneList.length;i++){
								if(zoneList[i].value==value){
									return zoneList[i].text;
								}
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
	$('#search-zone').combobox({
		data : zoneList,
		valueField : 'value',
		textField : 'text'
	});
	$('#zone').combobox({
		data : zoneList,
		valueField : 'value',
		textField : 'text'
	});
	
	init('actor');
	
	
	/* $("#uploadDiv").on("click",function(){  
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
	//默认推荐人
	$("#referrer").combobox("setValue", $('#default_referrer').val());
	//默认审核通过
	$('#status').combobox('setValue',1);
	
	formUrl = getContextPath() + '/portal/production/actor/save';
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
		
		if(rows[0].photo!=undefined && rows[0].photo!=null){
			var photos=rows[0].photo.split(";");
			for(var i=0;i<photos.length;i++){
				if(photos[i]!=null && photos[i]!=''){
					displayImg(storage_node+photos[i],photos[i],2);
					imgNo++;
				}	
			}
		}
		formUrl = getContextPath() + '/portal/production/actor/update';
		openDialog(rows[0]);
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}

function delFuca(){
	
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.message('请选择进行删除操作!');
	}else {
		$.messager.confirm('提示信息' , '确认删除?' , function(r){
			if(r){
				var ids = '';
				for(var i = 0 ; i < arr.length ; i++){
					ids += arr[i].id + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/production/actor/delete', {ids:ids},function(result){
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
// 删除
function delFuc1(){
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
				$.post(getContextPath() + '/portal/production/actor/delete', {ids:ids},function(result){
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
				return false;
			}
			
			var time=$('#birthDay').val();
			var now = new Date();
			var endYear=now.getFullYear(); 
			var beginYear=endYear-100;
			if(time>endYear || time<beginYear){
				$.message('请录入正确年份：'+beginYear+'~'+endYear);
				$('#birthDay').focus();
				progressClose();
				return false;
			}
			
			
			if(!validatePhoto()){
				progressClose();
				return false;
			}

			return true;
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
		title : '演员信息',
		modal : true,
		width : 530,
		height : 570,
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

/*

function addImg(obj) {
    var windowURL = window.URL || window.webkitURL;
    
    for(var i=0;i<obj.files.length;i++){
    	//校验：是否是图片 大小是否满足
    	//
    	var file = obj.files[i];
		var type = file.type.split('/')[0];
		if (type !='image') {
			alert('请上传图片');
			return;
		}
		var size = Math.floor(file.size / 1024 /1024);
		if (size > 3) {
			alert('图片大小不得超过3M');
			return;
		};
    	
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
		if(type==2){
			delImg=delImg+";"+url;
			$('#delImg').val(delImg);
		}else{
			var files=$('#videoFile').get(0).files;
			
			console.log($('#videoFile').get(0));
			
			for(var i=0;i<files.length;i++){
				if(url==files[i].name){
					delete files.i;
					console.log(files);
				}
			}

		}	
	}
	//移除预览
	$('#displayImg'+no).remove();
	$('#displayRemove'+no).remove();
	
}
*/
