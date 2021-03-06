var formUrl;
var datagrid;
var storage_node;
var min=1,max=4;
var statusList=[];
var typeIdList=[];
var imgRatio=248/140;
var displayWidth=124;
var displayHeight=70;


$().ready(function(){
	storage_node=$('#storage_node').val();
	statusList=JSON.parse($('#statusList').val());
	
	
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/production/studio/list',
		idField : 'id' ,
		title : '场地列表' ,
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
						title : '名称',
						width : 150,
						align : 'center' ,
					},{
						field : 'type',
						title : '类型',
						width : 150,
						align : 'center' ,
						formatter : function(value,row,index){
							if(value=="1"){
								return "内景";
							}else if(value=="2"){
								return "外景";
							}
						}
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
						field : 'area' ,
						title : '面积' ,
						align : 'center' ,
						width : 200
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
	
	init('studio');
	
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
	
	formUrl = getContextPath() + '/portal/production/studio/save';
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
		formUrl = getContextPath() + '/portal/production/studio/update';
		openDialog(rows[0]);
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}

// 删除
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
				$.post(getContextPath() + '/portal/production/studio/delete', {ids:ids},function(result){
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
		title : '场地信息',
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
	
}
*/