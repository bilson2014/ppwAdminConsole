var imgNo=1; 
var teamList;
var referrerList;
var citys;
var upload_Video;
var image_max_size = 1024*1024; // 250KB
var image_err_msg = '图片大小超出1MB上限,请重新上传!';
var jcrop_api;



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
	     var uploadFile = '<input type="file" id="videoFile" style="width:100%" name="file" class="p-file"  onchange="addImg(this)" accept="image/gif,image/jpeg,image/jpg,image/png"/>';  
	     $("#fileDiv").append($(uploadFile));  	     
	     $("#videoFile").click();
		// addImg();
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
	
	$('#dlgCut').dialog({
		title : '裁剪图片',
		modal : true,
		width : 700,
		height : 700,
	}).dialog('open').dialog('center');
	
	$(obj).addClass('ss');
	
	 var windowURL = window.URL || window.webkitURL;
	 var loadImg = windowURL.createObjectURL(obj.files[0]);
	 document.getElementById('setFile').setAttribute('src',loadImg);
	 initImgSize();


	 
	 
	 $('#uploadConfirmBt').bind('click',function(){
			if(x == 0 && y == 0 && x2 ==0 && y2 ==0){
				return;
			}
		     $("#fileDiv").append('<input type="hidden" name="x" value="'+x+'" />');
		     $("#fileDiv").append('<input type="hidden" name="y" value="'+y+'" />'); 
		     $("#fileDiv").append('<input type="hidden" name="x2" value="'+x2+'" />'); 
		     $("#fileDiv").append('<input type="hidden" name="y2" value="'+y2+'" />'); 
		     $("#fileDiv").append('<input type="hidden" name="width" value="'+w+'" />'); 
		     $("#fileDiv").append('<input type="hidden" name="height" value="'+h+'" />');
		     $("#fileDiv").append('<input type="hidden" name="originalWidth" value="'+$("#setFile").width()+'" />'); 
		     $("#fileDiv").append('<input type="hidden" name="originalHeight" value="'+$("#setFile").height()+'" />'); 
		     
		     
		     $('#fileDiv').form('submit',{
		 		onSubmit : function() {
		 			var flag = $(this).form('validate');
		 			
		 			return flag;
		 		},
		 		success : function(result) {

		 			$.message('操作成功!');
		 		}
		 	});

	 });
	 
	
	
  /*  var windowURL = window.URL || window.webkitURL;

    for(var i=0;i<obj.files.length;i++){
    	//校验：是否是图片 大小是否满足
    	
		var type = file.type.split('/')[0];
		if (type !='image') {
			alert('请上传图片');
			return;
		}
    	var file = obj.files[i];
		var size = Math.floor(file.size / 1024 /1024);
		if (size > 3) {
			$.message('图片大小不得超过3M');
		}
    	
    	var loadImg = windowURL.createObjectURL(obj.files[i]);
        imgNo=imgNo+1;
        displayImg(loadImg,obj.files[i].name,1);
        
    }
    
    $(obj).removeAttr("id");     
    validatePhoto();*/
    
} 

function convertImgToBase64(url, callback, outputFormat){
    var canvas = document.createElement('CANVAS'),
        ctx = canvas.getContext('2d'),
        img = new Image;
    img.crossOrigin = 'Anonymous';
    img.onload = function(){
        canvas.height = img.height;
        canvas.width = img.width;
        ctx.drawImage(img,0,0);
        var dataURL = canvas.toDataURL(outputFormat || 'image/png');
        callback.call(this, dataURL);
        canvas = null; 
    };
    img.src = url;
}
  

function displayImg(loadImg,url,type){ 
	var html = '<div><img src='+loadImg+' id="displayImg'+imgNo+'" class="aptimg">'+
	'<a onclick="delFuc('+imgNo+',\''+url+'\','+type+');" href="javascript:void(0);" id="displayRemove'+imgNo+'" class="easyui-linkbutton aptDel" '+
	'data-options="plain:true,iconCls:\'icon-cancel\'"></a></div>';
	
	var newImg = $(html).appendTo("#imgDisplay");
	 //渲染easyUIxc
	 $.parser.parse($(newImg));
}

function initImgSize(){
	var needWidth = $('.imgDivSize').css('width');
	var needHeight = $('.imgDivSize').css('height');	
	var changeImg = $('#setFile');
	changeImg.load(function(){
			var realHeight = $(this).height();
			var realWidth  = $(this).width();			
				if(realWidth/realHeight < (16/9)){
					$(this).css('height',needHeight).css('width','auto');
				}else{
					$(this).css('height','auto').css('width',needWidth);
				}
			JcropFunction();
		});
    
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



function JcropFunction(){
	x=0;
	y=0;
	x2=0;
	y2=0;
	h=0;
	w=0;
	// 初始化Jcrop
	jcrop_api = $.Jcrop('#setFile',{
		bgOpacity : 0.2,
		aspectRatio : 162/216,//选框宽高比。说明：width/height
//		aspectRatio : 248/140,//选框宽高比。说明：width/height
		onSelect : updateCoords // 当选择完成时执行的函数
	});
}

function updateCoords(coords){
	x=coords.x;//坐标位置x的开始位置
	y=coords.y;//坐标位置y的开始位置
	x2=coords.x2;//坐标x结束位置
	y2=coords.y2;//坐标y结束位置
	w=coords.w;//实际宽
	h=coords.h;//实际高
	if(parseInt(coords.w) > 0){
		//计算预览区域图片缩放的比例，通过计算显示区域的宽度(与高度)与剪裁的宽度(与高度)之比得到 
		var rx = $("#showImgSize").width() / coords.w;
		var ry = $("#showImgSize").height() / coords.h;
		$("#showImg").attr('src',$('#setFile').attr('src'));
		//通过比例值控制图片的样式与显示 
		$("#showImg").css({
			width:Math.round(rx * $("#setFile").width()) + "px", //预览图片宽度为计算比例值与原图片宽度的乘积 
			height:Math.round(ry * $("#setFile").height()) + "px", //预览图片高度为计算比例值与原图片高度的乘积 
			marginLeft:"-" + Math.round(rx * coords.x) + "px",
			marginTop:"-" + Math.round(ry * coords.y) + "px",
			opacity:1,
		});
	}
}
