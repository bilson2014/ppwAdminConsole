var treegrid;
$().ready(function() {
	$('#startbtn').on('click', function() {
		loadData(function(msg) {
			if(msg.errorCode == 200){
				$.message('新建成功！');
				$('#processId').val(msg.result);
				refresh();
			}else if(msg.errorCode == 500){
				$.message('新建失败！');
			}
		}, '/flow/startProcess', $.toJSON({
			id : $('#templateId').val().trim()
		}));
	});
	$('#nextbtn').on('click',function(){
		$.ajax({  
		     type:'post',  
		     url:'/flow/completeTask',  
		     data:"processId="+$('#processId').val(),  
		     cache:false,  
		     success:function(data){
		    	 if(data.errorCode == 200){
		    		 refresh();
		    	 }else{
		    		 if(data.result != null){
		    			 $('#error').text(data.result);
		    		 }else{
		    			 $('#error').text(data.errorMsg);
		    		 }
		    	 }
		     },  
		     error:function(){}  
		}); 
	});
	$('#prevbtn').on('click',function(){
		$.ajax({  
			type:'post',  
			url:'/flow/prevTask',  
			data:"processId="+$('#processId').val(),  
			cache:false,  
			success:function(data){
				if(data.errorCode == 200){
					refresh();
				}else{
					if(data.result != null){
						$('#error').text(data.result);
					}else{
						$('#error').text(data.errorMsg);
					}
				}
			},  
			error:function(){}  
		}); 
	});
	

	treegrid = $('#treeGrid').treegrid({
		url : getContextPath() + '/portal/right/list',
		idField : 'rightId',
		treeField : 'rightName',
		parentField : 'pId',
		fit : true,
		fitColumns : false,
		border : false,
		frozenColumns : [[{
			title : '编号',
			field : 'rightId',
			width : 40
		}]],
		columns : [ [ {
			field : 'rightName',
			title : '资源名称',
			width : 200
		}, {
			field : 'url',
			title : '资源路径',
			width : 200
		}, {
			field : 'seq',
			title : '排序',
			width : 40
		}, {
			field : 'icon',
			title : '图标',
			width : 100
		}, {
			field : 'resourceType',
			title : '资源类型',
			width : 80,
			formatter : function(value, row, index) {
				switch (value) {
				case 0:
					return '<span style="color:red;" >菜单</span>' ;
				case 1:
					return '<span style="color:black;" >按钮</span>' ;
				}
			}
		}, {
			field : 'pId',
			title : '上级资源ID',
			width : 150,
			hidden : true
		}, {
			field : 'status',
			title : '状态',
			width : 40,
			formatter : function(value, row, index) {
				switch (value) {
				case 0:
					return '<span style="color:red;" >停用</span>' ;
				case 1:
					return '<span style="color:green;" >正常</span>';
				}
			}
		},{
			field : 'isCommon',
			title : '是否公用',
			width : 60,
			formatter : function(value, row, index) {
				if (value) 
					return '<span style="color:red;" >公用</span>';
				else
					return '<span style="color:black;" >非公用</span>' ;
			}
		}]],
		toolbar : '#toolbar'
	});
	
});
function refresh(){
	var id = $('#processId').val();
	if(id == ''){
		alert('必须填写 process id');
		return;
	}
	var path = '/get/processimage?processId='+id+'&time='+Date.parse(new Date());
	var div = $('#flow');
	var img = $('<img src="'+path+'"/>');
	div.html('');
	div.append(img);
}
function pay(){
	$.ajax({  
	     type:'post',  
	     url:'/test/pay',  
	     data:"processId="+$('#processId').val(),  
	     cache:false,  
	     success:function(data){
	    	 $('#errorpay').text(data.result);
	     },  
	     error:function(){}  
	}); 
}
