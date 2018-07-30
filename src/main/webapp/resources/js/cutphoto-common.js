var imgNo=1; 
var image_max_size = 1024*1024; // 250KB
var image_err_msg = '图片大小超出1MB上限,请重新上传!';
var jcrop_api;

//需要封装一下：参数传入 初始化后页面加入dlg  如果需要预览，传入预览位置
//uploadDiv imgDisplay  min max  imgNo
//#photo 所有图片 #delImg 待删除图片

function initCutPhoto(uploadBtn){
	//剪切提交表单
	 var cutForm=' <form  method="post" action="/portal/production/cutPhoto" enctype="multipart/form-data" id="fileDiv" style="display:none" >'+
     			'<input type="hidden" id="x" name="x" />'+
     			'<input type="hidden" id="y" name="y"  />'+ 
     			'<input type="hidden" id="x2" name="x2" /> '+
     			'<input type="hidden" id="y2" name="y2" /> '+
     			'<input type="hidden" id="width" name="width" />'+ 
     			'<input type="hidden" id="height" name="height"  />'+
     			'<input type="hidden" id="originalWidth" name="originalWidth"  />'+ 
     			'<input type="hidden" id="originalHeight" name="originalHeight" /> '+
     		'</form>';
	
	//剪切弹出框
	var cutDlg='<div id="dlgCut" class="easyui-dialog" style="padding:5px 5px;width: 350px;height: 700px;" closed="true" buttons="#dlgCut-buttons" title="裁剪图片">'+
	       			'<div class="imgDivSize" style="height:300px;width:300px;background:#eee;overflow: hidden;text-align:center;position:relative;margin-top: 5px;margin-left:10px;display: none;">'+//
	       		//   ' <!-- <img id="setFile" style="width:100%;height:auto"> -->'+
	       			'</div>'+
	       			'<div id="showImgSize" style="width:'+displayWidth+'px;height:'+displayHeight+'px;overflow:hidden;'+
	       				'margin-top: 10px;margin-left:10px;" class="preview">'+
	       			'   <img id="showImg">  '+
	       			'</div>'+
	       			cutForm+
	       		'</div>'+

	       		'<div id="dlgCut-buttons" style="display: none;">'+
	       			'<a href="javascript:void(0)" class="easyui-linkbutton c6"  iconCls="icon-ok" onclick="cutImg()" >确定</a>'+
	       		'</div> ';
	/* $("body").append($(cutDlg)); 
	 
	 //渲染easyUIxc
	 $.parser.parse($(cutDlg));*/
	 
	 var newDlg = $(cutDlg).appendTo("body");
	 //渲染easyUIxc
	 $.parser.parse($(newDlg));
	 
	
//	 $("#uploadDiv").on("click",function(){ 
	 $("#"+uploadBtn).on("click",function(){  
		 if(!validatePhoto(true)){
			 return ;
		 }
		 $("div").remove(".p-file");
		 
	     var uploadFile = '<div class="p-file"><input type="file" id="videoFile" style="width:100%" name="uploadFile" class="p-file"  onchange="addImg(this)" accept="image/gif,image/jpeg,image/jpg,image/png"/></div>';  
	     $("#fileDiv").append($(uploadFile));  	     
	     $("#videoFile").click();
	     //$('#videoFile').removeAttr('id');
		// addImg();
	 }); 
	 
	// $('#dlgCut').dialog('close');
}

function validatePhoto(init){
	//function validatePhoto(min,max){
	var imgCount=$('.aptimg').length;
	
	if(init){
		if(max!=undefined && imgCount>=max){
			$.message('最多选择'+max+'张照片！');
			return false;
		}
	}else{
		if(min!=undefined && imgCount<min){
			$.message('至少选择'+min+'张照片！');
			return false;
		}
		if(max!=undefined && imgCount>max){
			$.message('最多选择'+max+'张照片！');
			return false;
		}
	}
	return true;
}

function addImg(obj) {
	
	//销毁
	if(jcrop_api!= undefined){
		jcrop_api.destroy();
	}
	
	$('.imgDivSize').empty();
	 var setFile = ' <img id="setFile" style="width:100%;height:auto">';  //如果不每次清除，setFile会加载两次，而剪切时取得其width是真实图片大小　１５０
     $(".imgDivSize").append($(setFile));
     $('#showImg').removeAttr('src');
     
    
	
	$('#dlgCut').dialog({
		title : '裁剪图片',
		modal : true,
		width : 335,
		height : 500,
	}).dialog('open').dialog('center');
	
	 $('.imgDivSize')[0].style.display="block";
	 $('#dlgCut-buttons')[0].style.display="block";
		
	 var windowURL = window.URL || window.webkitURL;
	 var loadImg = windowURL.createObjectURL(obj.files[0]);
	 document.getElementById('setFile').setAttribute('src',loadImg);
	 initImgSize();
	 console.log(obj.files[0]);
	
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


function cutImg(){

	if(x == 0 && y == 0 && x2 ==0 && y2 ==0){
		return;
	}
	
	$('#x').val(x);
	$('#y').val(y);
	$('#x2').val(x2);
	$('#y2').val(y2);
	$('#width').val(w);
	$('#height').val(h);
	$('#originalWidth').val($("#setFile").width());
	$('#originalHeight').val($("#setFile").height());
     
     $('#fileDiv').form('submit',{
 		onSubmit : function() {
 			var flag = $(this).form('validate');
 			
 			return flag;
 		},
 		success : function(result) {
 			
 			var msg=$.parseJSON( result );
 			
 			if(msg.code=='200'){
 				//预览 
 				$('#photo').val($('#photo').val()+msg.result+";");//如果是单张图片，可以直接替换	
 				displayImg(msg.result,1);
 				
 				$('#dlgCut').dialog('close');
 				
 				progressClose();
 				$.message('操作成功!');
 			}else{
 				progressClose();
 				$.message('剪切失败!');
 			}
 			
 			 $('.imgDivSize')[0].style.display="none";
 			 $('#dlgCut-buttons')[0].style.display="none";
 		}
 	});
}

function displayImg(url,type){ 
	var html = '<div><img src='+storage_node+url+' id="displayImg'+imgNo+'" style="margin-left:10px;width: '+displayWidth+'px;height: '+displayHeight+'px;" class="aptimg">'+
	'<a onclick="delFuc('+imgNo+',\''+url+'\','+type+');" href="javascript:void(0);" id="displayRemove'+imgNo+'" style="margin-left:-20px;margin-top: '+(displayHeight-10)+'px;" class="easyui-linkbutton aptDel" '+
	'data-options="plain:true,iconCls:\'icon-cancel\'"></a></div>';
	
	var newImg = $(html).appendTo("#imgDisplay");
	 //渲染easyUIxc
	 $.parser.parse($(newImg));
	 
	 imgNo++;
}

function initImgSize(){
	var needWidth = $('.imgDivSize').css('width');
	var needHeight = $('.imgDivSize').css('height');	
	var changeImg = $('#setFile');
	changeImg.load(function(){
			var realHeight = $(this).height();
			var realWidth  = $(this).width();			
				if(realWidth/realHeight < 1){
					$(this).css('height',needHeight).css('width','auto');
				}else{
					$(this).css('height','auto').css('width',needWidth);
				}	
				
			JcropFunction();
		});
}

var delImg="";

function delFuc(no,url,type){//TODO type可以去掉了

	if(url!=undefined && url != null && url!=''){
//		delImg=delImg+";"+url;
		$('#delImg').val($('#delImg').val()+";"+url);	
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
		aspectRatio : imgRatio,//选框宽高比。说明：width/height
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

