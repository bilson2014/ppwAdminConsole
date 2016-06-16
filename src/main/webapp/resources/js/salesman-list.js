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
		fitColumns : true ,
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
				align : 'center',
				editor : {
					type : 'validatebox' ,
					options : {
						required : true , 
						missingMessage : '请填写分销人姓名!'
					}
				}
			},{
				field : 'uniqueId',
				title : '唯一标识',
				width : 120,
				align : 'center'
			},{
				field : 'salesmanDescription' ,
				title : '备注' ,
				width : 200,
				align : 'center',
				editor : {
					type : 'validatebox' ,
					options : {
						required : false , 
						missingMessage : '请填写分销人姓名!'
					}
				}
			},{
				field : 'salesmanURL' ,
				title : '分销产品地址' ,
				align : 'center',
				formatter : function(value,row,index){
					return '<span style=color:orange; >'+ getServerName() + '/phone/salesman/' + row.uniqueId +'</span>' ;
				}
			},{
				field : 'orderAction' ,
				title : '操作',
				width : 120,
				align : 'center',
				formatter : function(value, row, index) {
					var str = '&nbsp;';
						str += $.formatString('<a href="javascript:void(0)" onclick="orderView(\'{0}\');" >查看</a> | ', row.uniqueId);
						str += $.formatString('<a href="javascript:void(0)" onclick="orderdownLoad(\'{0}\',\'{1}\');" >下载</a>', row.uniqueId,row.salesmanName);
					return str;
				}
			},{
				field : 'salesmanOrderURL' ,
				title : '分销下单地址' ,
				align : 'center',
				formatter : function(value,row,index){
					return '<span style=color:black; >'+ getServerName() + '/phone/salesman/order/' + row.uniqueId +'</span>' ;
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
				align : 'center'
			},{
				field : 'sumPrice' ,
				title : '分销总额',
				align : 'center'
			}]],
		pagination: true ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar',
		onAfterEdit:function(index , record){
			$.post(flag =='add' ? getContextPath() + '/portal/salesman/save' : getContextPath() + '/portal/salesman/update', record , function(result){
				datagrid.datagrid('clearSelections');
				datagrid.datagrid('reload');
				$.message('操作成功!');
			});
		}
	});
		
});


//增加
function addFuc(){
	if(editing == undefined){
		flag = 'add';
		//1 先取消所有的选中状态
		datagrid.datagrid('unselectAll');
		//2追加一行
		datagrid.datagrid('appendRow',{});
		//3获取当前页的行号
		editing = datagrid.datagrid('getRows').length -1;
		//4开启编辑状态
		datagrid.datagrid('beginEdit', editing);
	}
}

//修改
function editFuc(){
	var arr = datagrid.datagrid('getSelections');
	if(arr.length != 1){
		$.message('只能选择一条记录进行修改!');
	} else {
		if(editing == undefined){
			flag = 'edit';
			//根据行记录对象获取该行的索引位置
			editing = datagrid.datagrid('getRowIndex' , arr[0]);
			//开启编辑状态
			datagrid.datagrid('beginEdit',editing);
		}
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
	//保存之前进行数据的校验 , 然后结束编辑并释放编辑状态字段 
	datagrid.datagrid('beginEdit', editing);
	if(datagrid.datagrid('validateRow',editing)){
		datagrid.datagrid('endEdit', editing);
		editing = undefined;
	}
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
	
	//return 'http://192.168.1.119:8080';
	return 'http://www.apaipian.com';
	//return 'http://test.apaipian.com:8080';
}

// 查看直接下单二维码
function view(uniqueId){
	var url = 'http://qr.liantu.com/api.php?text=' + getServerName() + '/phone/salesman/order/' + uniqueId;
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
	var url = 'http://qr.liantu.com/api.php?text=' + getServerName() + '/phone/salesman/' + uniqueId;
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