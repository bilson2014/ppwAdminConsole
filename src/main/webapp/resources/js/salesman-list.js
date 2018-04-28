var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var formUrl;
var datagrid;
$().ready(function(){
	var sourceCombobox=JSON.parse($('#sourceCombobox').val());
	$('#sourceCombobox').remove();
	$('#indentSource').combobox({
		data:sourceCombobox,
		valueField:'value',
		textField:'text'
	});
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath()+ '/portal/salesman/list',
		idField : 'salesmanId' ,
		title : '分销人员列表' , 
		//fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
			{field : 'ck' , checkbox:true}
		]],
		columns:[[
			{
				field : 'salesmanName',
				title : '名称',
				width : 100,
				align : 'center'
			},{
				field : 'belongs',
				title : '作用范围',
				width : 60,
				align : 'center'
			},{
				field : 'platform',
				title : '受众平台',
				width : 60,
				align : 'center'
			},{
				field : 'uniqueId',
				title : '唯一标识',
				width : 120,
				align : 'center'
			},{
				field : 'salesmanDescription' ,
				title : '备注' ,
				width : 200,
				align : 'center'
			},{
				field : 'indentSource' ,
				title : '订单来源' ,
				align : 'center' ,
				sortable : true ,
				width : 80,
				sortable : true ,
				formatter : function(value , record , index){
					for(var i=0;i<sourceCombobox.length;i++){
						if(value==sourceCombobox[i].value){
							return '<span style=color:black; >'+sourceCombobox[i].text+'</span>' ;
						}
					}
				}
			},{
				field : 'accessurl' ,
				title : '访问地址' ,
				width : 200,
				//hidden : true,
				align : 'center',
				formatter : function(value,row,index){
					if (row.accessurl != null && row.accessurl != '' && row.accessurl != undefined)
						return '<span style=color:orange; >'+getFullUrl(row.type,row.accessurl,row.uniqueId)+'</span>' ; 
//						return '<span style=color:orange; >'+ row.accessurl +'</span>' ; 
//					return '<span style=color:orange; >'+ getServerName(row.platform) + '/salesman/' + row.uniqueId +'</span>' ;
				}
		/*	},{
				field : 'salesmanURL' ,
				title : '分销产品地址' ,
				width : 200,
				align : 'center',
				formatter : function(value,row,index){
					if (row.accessurl != null && row.accessurl != '' && row.accessurl != undefined)
						return '<span style=color:orange; >'+ row.accessurl +'</span>' ; 
					return '<span style=color:orange; >'+ getServerName(row.platform) + '/salesman/' + row.uniqueId +'</span>' ;
				}*/
			},{
				field : 'orderAction' ,
				title : '操作',
				width : 120,
				align : 'center',
				formatter : function(value, row, index) {
					var salesmanURL;
					if (row.accessurl != null && row.accessurl != '' && row.accessurl != undefined){
						salesmanURL=getFullUrl(row.type,row.accessurl,row.uniqueId); 
						var str = '&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="view(\'{0}\');" >查看</a> | ', salesmanURL);
						str += $.formatString('<a href="javascript:void(0)" onclick="downLoad(\'{0}\',\'{1}\');" >下载</a>', salesmanURL,row.salesmanName);
						return str;
					}
					
				}
			},{
				field : 'orderUrl' ,
				title : '分销下单地址' ,
				align : 'center',
				width : 200,
				formatter : function(value,row,index){
//					if(row.orderAddress=='1'){
//						return '<span style=color:black; >'+ getServerName(row.platform) + '/salesman/order/' + row.uniqueId +'</span>' ;
//					}
					if (row.orderUrl != null && row.orderUrl != '' && row.orderUrl != undefined)
						return '<span style=color:black; >'+getFullUrl(row.type,row.orderUrl,row.uniqueId)+'</span>' ; 
				}
			},{
				field : 'action' ,
				title : '操作',
				width : 120,
				align : 'center',
				formatter : function(value, row, index) {
					if(row.orderUrl != null && row.orderUrl != '' && row.orderUrl != undefined){
						var salesmanOrderURL=getFullUrl(row.type,row.orderUrl,row.uniqueId);
						var str = '&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="view(\'{0}\');" >查看</a> | ', salesmanOrderURL);
						str += $.formatString('<a href="javascript:void(0)" onclick="downLoad(\'{0}\',\'{1}\');" >下载</a>', salesmanOrderURL,row.salesmanName);
						return str;
					}
					
				}
			},{
				field : 'total' ,
				title : '分销总单数',
				width : 200,
				align : 'center'
					
			},{
				field : 'sumPrice' ,
				title : '分销总额',
				width : 200,
				align : 'center'
			},{
				field : 'realTotal' ,
				title : '有效分销单数',
				width : 200,
				align : 'center'
					
			},{
				field : 'realSumPrice' ,
				title : '有效分销总额',
				width : 200,
				align : 'center'
			}]],
		pagination: true ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar'
	});
		
});


//增加
function addFuc(){
	openDialog('dlg');
	$('#fm').form('clear');
	formUrl = getContextPath() + '/portal/salesman/save';
}

//修改
function editFuc(){
	var arr = datagrid.datagrid('getSelections');
	if(arr.length != 1){
		$.message('只能选择一条记录进行修改!');
	} else {
		$('#fm').form('clear');
		$('#fm').form('load',arr[0]);
		formUrl = getContextPath() + '/portal/salesman/update';
		openDialog('dlg');
	}
}

//删除
function delFuc(){
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.message('请选择进行删除操作!');
	} else {
		$.messager.confirm('提示信息' , '确认删除?' , function(r){
			if(r){
				var ids = '';
				for(var i = 0 ; i < arr.length ; i++){
					ids += arr[i].salesmanId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/salesman/delete', {ids:ids},function(result){
					
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

//保存
function saveFuc(){
	progressLoad();
	$('#fm').form('submit',{
		url : formUrl,
		onSubmit : function() {
			var accessurl = $("#accessurl").val();
			/*if(accessurl == '' || accessurl == null){
				accessurl = '/salesman/';
			}*/
			
			if(accessurl != '' && accessurl != null){
				if(accessurl.charAt(0) != '/' && accessurl.indexOf("http")!=0) {
					accessurl = '/' + accessurl;
				}
				
				/*if(accessurl.charAt(accessurl.length-1) != '/') {
					accessurl += '/';
				}*/
			}
			
			
			$('input[name="accessurl"]').val(accessurl);
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

// 取消
function cancelFuc(){
	//回滚数据 
	datagrid.datagrid('rejectChanges');
	editing = undefined;
}

// 查询
function searchFun(){
	datagrid.datagrid('load', $.serializeObject($('#searchForm')));
}

//清除
function cleanFun() {
	$('#searchForm').form('clear');
	datagrid.datagrid('load', {});
}

// 获取服务地址
function getServerName(type){
	if(type=='1'){
		return 'https://m.apaipian.com';
//		return 'https://test.apaipian.com:8088';
	}else{
		return 'https://www.apaipian.com';
//		return 'https://test.apaipian.com';
	}
	//return 'http://192.168.0.143:8080';
	//return 'http://test.apaipian.com:8080';
}

function getFullUrl(type,url,uniqueId){
	if(url!=null && url!=''){
		if(url.charAt(0) == '/'){
			url=getServerName(type)+url;
		}
		if(url.indexOf("{uniqueId}")>-1){
			url=url.replace(/{uniqueId}/,uniqueId)
		}
		return url;
	}
}

// 查看直接下单二维码
function view(accessurl){
	var url = 'http://qr.liantu.com/api.php?text=' + accessurl;
	$('#qrCode').attr('src',url);
	$('#qrCode').removeClass('hide');
	$('#qrCode-condition').removeClass('hide');
	
	$('#p-cancel').on('click',function(){
		$('#qrCode-condition').addClass('hide');
		$('#qrCode').attr('src','');
	});
}

// 下载直接下单二维码
function downLoad(accessurl,salesmanName){
	download(getContextPath() + '/portal/salesman/download/code', $.toJSON({
		accessurl : accessurl,
		salesmanName : salesmanName
	}));
}

// 查看产品页二维码
function orderView(uniqueId,accessurl){
	var url;
	if(accessurl != null && accessurl != '' && accessurl != undefined) {
		// 如果访问路径没有配置，则默认为分销地址
		url = 'http://qr.liantu.com/api.php?text=' + getServerName() + '/salesman/' + uniqueId;
	}else {
		// 如果配置了访问路径，则使用该访问路径
		url = 'http://qr.liantu.com/api.php?text=' + getServerName() + accessurl + uniqueId;
	}
	$('#qrCode').attr('src',url);
	$('#qrCode').removeClass('hide');
	$('#qrCode-condition').removeClass('hide');
	
	$('#p-cancel').on('click',function(){
		$('#qrCode-condition').addClass('hide');
		$('#qrCode').attr('src','');
	});
}

// 下载产品页二维码
function orderDownLoad(uniqueId,salesmanName){
	download(getContextPath() + '/portal/salesman/download/order/code', $.toJSON({
		uniqueId : uniqueId,
		salesmanName : salesmanName
	}));
}

function openDialog(id){
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {}
	}).dialog('open').dialog('center');
}