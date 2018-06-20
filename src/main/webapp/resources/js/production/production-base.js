var imgNo=1; 
var teamList;
var referrerList;
var citys;



function init(type){
	
	syncLoadData(function(res) {
		citys = res;
	}, getContextPath() + '/portal/all/citys', null);
	
	syncLoadData(function(res) {
		typeIdList = res;	
	}, getContextPath() + '/portal/quotationtype/production/select?productionType='+type, null);
		
	
	//statusList=[{'value':2,'text':'审核中'},{'value':1,'text':'审核通过'},{'value':0,'text':'审核未通过'}];
	loadData(function(res) {
		teamList = res;
		$('#teamId').combobox({
			data : teamList,
			valueField : 'teamId',
			textField : 'teamName',
			filter: function(q, row){
				if(row.teamName == null)
					return false;
				return row.teamName.indexOf(q) >= 0;
			}
		});
		
		$('#search-teamId').combobox({
			data : teamList,
			valueField : 'teamId',
			textField : 'teamName',
			filter: function(q, row){
				if(row.teamName == null)
					return false;
				return row.teamName.indexOf(q) >= 0;
			}
		});
	}, getContextPath() + '/portal/product/init', null);
	
	loadData(function(res) {
		referrerList = res;
		
		$('#referrer').combobox({
			data :referrerList,
			valueField:'employeeId',
			textField:'employeeRealName'
		}); 
		
		
		$('#search-referrer').combobox({
			data :referrerList,
			valueField:'employeeId',
			textField:'employeeRealName'
		}); 
	}, getContextPath() + '/portal/employee/getAll', null);
	
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
	
	$('#status').combobox({
		data : statusList,
		valueField : 'value',
		textField : 'text'
	});
	
	var s_statusList=JSON.parse(JSON.stringify(statusList));
	s_statusList.unshift({'value':'','text':'--请选择--'});
	$('#search-status').combobox({
		data : s_statusList,
		valueField : 'value',
		textField : 'text'
	});
	
	if(type!='device'){
		$('#typeId').combotree(
				{
					data :typeIdList,
					lines : true,
					cascadeCheck : false,
					parentField : 'pid',
					idField : 'id',
					treeField : 'text',
//					editable :true,//后期加上搜索
//					filter:{}
					onBeforeSelect: function(node) {  //只能选择叶子节点
			            if (!$(this).tree('isLeaf', node.target)) {  
			                return false;  
			            }  
			        }
		});
		
		$('#search-typeId').combotree(
				{
					data :typeIdList,
					lines : true,
					cascadeCheck : false,
					parentField : 'pid',
					idField : 'id',
					treeField : 'text',
//					editable :true,//后期加上搜索
//					filter:{}
					onBeforeSelect: function(node) {  //只能选择叶子节点
			            if (!$(this).tree('isLeaf', node.target)) {  
			                return false;  
			            }  
			        }
		});
	}
	
	
	
	 $("#uploadDiv").on("click",function(){  
	     var uploadFile = '<input type="file" id="videoFile" style="width:100%" name="uploadFiles" class="p-file" multiple="multiple" onchange="addImg(this)" accept="image/gif,image/jpeg,image/jpg,image/png"/>';  
	     $("#fileDiv").append($(uploadFile));  
	     
	     $("#videoFile").click();          
	 }); 
}

function validatePhoto(){
	//function validatePhoto(min,max){
	var imgCount=$('.aptimg').length;
	if(imgCount<min){
		$.message('至少选择'+min+'张照片！');
		return false;
	}
	if(imgCount>max){
		$.message('最多选择'+max+'张照片！');
		return false;
	}
	return true;
}



function addImg(obj) {
    var windowURL = window.URL || window.webkitURL;

    for(var i=0;i<obj.files.length;i++){
    	//校验：是否是图片 大小是否满足
    	//
    	/*
		var type = file.type.split('/')[0];
		if (type !='image') {
			alert('请上传图片');
			return;
		}*/
    	/*var file = obj.files[i];
		var size = Math.floor(file.size / 1024 /1024);
		if (size > 3) {
			$.message('图片大小不得超过3M');
		}*/
    	
    	var loadImg = windowURL.createObjectURL(obj.files[i]);
        imgNo=imgNo+1;
        displayImg(loadImg,obj.files[i].name,1);
    }
    $(obj).removeAttr("id"); 
    
    validatePhoto();
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
		/*if(type==2){
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

		}*/	
	}
	//移除预览
	$('#displayImg'+no).remove();
	$('#displayRemove'+no).remove();
	
}
