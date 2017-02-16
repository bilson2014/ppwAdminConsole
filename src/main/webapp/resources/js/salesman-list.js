var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var formUrl;
var datagrid;
$().ready(function(){
		
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
				title : '姓名',
				width : 100,
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
				field : 'salesmanURL' ,
				title : '分销产品地址' ,
				width : 200,
				align : 'center',
				formatter : function(value,row,index){
					return '<span style=color:orange; >'+ getServerName() + '/salesman/' + row.uniqueId +'</span>' ;
				}
			},{
				field : 'orderAction' ,
				title : '操作',
				width : 120,
				align : 'center',
				formatter : function(value, row, index) {
					var str = '&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="orderView(\'{0}\');" >查看</a> | ', row.uniqueId);
						str += $.formatString('<a href="javascript:void(0)" onclick="orderDownLoad(\'{0}\',\'{1}\');" >下载</a>', row.uniqueId,row.salesmanName);
					return str;
				}
			},{
				field : 'salesmanOrderURL' ,
				title : '分销下单地址' ,
				align : 'center',
				width : 200,
				formatter : function(value,row,index){
					return '<span style=color:black; >'+ getServerName() + '/salesman/order/' + row.uniqueId +'</span>' ;
				}
			},{
				field : 'action' ,
				title : '操作',
				width : 120,
				align : 'center',
				formatter : function(value, row, index) {
					var str = '&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="view(\'{0}\');" >查看</a> | ', row.uniqueId);
						str += $.formatString('<a href="javascript:void(0)" onclick="downLoad(\'{0}\',\'{1}\');" >下载</a>', row.uniqueId,row.salesmanName);
					return str;
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
function getServerName(){
	
	//return 'http://192.168.0.143:8080';
	return 'http://www.apaipian.com';
	//return 'http://test.apaipian.com:8080';
}

// 查看直接下单二维码
function view(uniqueId){
	var url = 'http://qr.liantu.com/api.php?text=' + getServerName() + '/salesman/order/' + uniqueId;
	$('#qrCode').attr('src',url);
	$('#qrCode').removeClass('hide');
	$('#qrCode-condition').removeClass('hide');
	
	$('#p-cancel').on('click',function(){
		$('#qrCode-condition').addClass('hide');
		$('#qrCode').attr('src','');
	});
}

// 下载直接下单二维码
function downLoad(uniqueId,salesmanName){
	download(getContextPath() + '/portal/salesman/download/code', $.toJSON({
		uniqueId : uniqueId,
		salesmanName : salesmanName
	}));
}

// 查看产品页二维码
function orderView(uniqueId){
	var url = 'http://qr.liantu.com/api.php?text=' + getServerName() + '/salesman/' + uniqueId;
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