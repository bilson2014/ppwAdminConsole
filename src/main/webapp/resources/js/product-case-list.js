var formUrl;
var chanpincache;
var productcache;
var configcache;
var userCache;
$().ready(function() {
	initChanPinCache();
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/case/list',
		idField : 'pId',
		title : '案例列表',
		fitColumns : true,
		striped : true,
		loadMsg : '数据正在加载,请耐心的等待...',
		rownumbers : true,
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		} ] ],
		columns : [ [ {
			field : 'pId',
			title : 'ID',
			width : 60,
			align : 'center'
		}, {
			field : 'pName',
			title : '案例名称',
			width : 60,
			align : 'center'
		}, {
			field : 'pProductionCycle',
			title : '案例周期',
			width : 60,
			align : 'center'
		}, {
			field : 'mcoms',
			title : '案例时长',
			width : 60,
			align : 'center'
		}, {
			field : 'userId',
			title : '关联客户',
			width : 60,
			align : 'center',
			formatter : function(value,row,index){
				if(value != undefined ){
					var html= "";
					for (var int = 0; int < userCache.length; int++) {
						var user = userCache[int];
						if(user.userId == value)
							return user.userName;
					}
					return html;
				}
			}
		}, {
			field : 'pDescription',
			title : '案例描述',
			align : 'center',
			width : 60
		}, {
			field : 'chanpinId',
			title : '关联产品',
			align : 'center',
			width : 60,
			formatter : function(value,row,index){
				if(value != undefined ){
					var chanpinId  = row.chanpinId;
					for (var int = 0; int < chanpincache.length; int++) {
						var chanpin = chanpincache[int];
						if(chanpin.chanpinId == chanpinId)
							return '<span>'+chanpin.chanpinName+'</span>';
					}
					return chanpinId;
				}
			}
		}, {
			field : 'productId',
			title : '关联作品',
			align : 'center',
			width : 60,
			formatter : function(value,row,index){
				if(value != undefined ){
					var productId  = row.productId;
					for (var int = 0; int < productcache.length; int++) {
						var product = productcache[int];
						if(product.productId == productId)
							return '<span>'+product.productName+'</span>';
					}
					return ""+productId;
				}
			}
		}, {
			field : 'pScene',
			title : '关联场景',
			align : 'center',
			width : 60
		}, {
			field : 'configurationId',
			title : '关联配置',
			align : 'center',
			width : 60,
			formatter : function(value,row,index){
				if(value != undefined ){
					var html= "";
					syncLoadData(function(res){
						if(res != null && res.result !=null) 
						html = res.result.chanpinconfigurationName;
					}, getContextPath() +'/portal/config/info/'+value, null);
					return html;
				}
			}
		}, {
			field : 'customerRestimonial',
			title : '客户证言',
			align : 'center',
			width : 60
		}, {
			field : 'createTime',
			title : '创建时间',
			align : 'center',
			width : 60
		}, {
			field : 'updateTime',
			title : '更新时间',
			align : 'center',
			width : 60
		}] ],
		pagination : true,
		pageSize : 50,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar'
	});
});
function addFuc() {
	$('#fm').form('clear');
	openDialog('dlg', null);
	formUrl =getContextPath()+'/portal/case/save';
}
function editFuc() {
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		formUrl = getContextPath() + '/portal/case/update';
		openDialog('dlg',rows[0]);
		$('#fm').form('load',rows[0]);
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}
function delFuc() {
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.message('请选择进行删除操作!');
	} else {
		$.messager.confirm('提示信息' , '确认删除?' , function(r){
			if(r){
				var ids = '';
				for(var i = 0 ; i < arr.length ; i++){
					ids += arr[i].pId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/case/delete', {ids:ids},function(result){
					// 刷新数据
					datagrid.datagrid('clearSelections');
					datagrid.datagrid('reload');
					$.message('操作成功!');
				});
			} else {
				 return "";
			}
		});
	}
}
//打开dialog
function openDialog(id, data) {
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {
			$('#chanpinId').combobox({
				valueField : 'chanpinId',
				textField : 'chanpinName',
				data:chanpincache,
				onSelect : function(){
					var cId = $('#chanpinId').combobox('getValue');
					$('#configurationId').combobox({
						valueField : 'chanpinconfigurationId',
						textField : 'chanpinconfigurationName',
						url : getContextPath() + '/portal/config/chanpin?chanpinId='+cId
					});
				}
			});
			$("#productId").combobox({
				data:productcache,
				valueField:'productId',
				textField:'productName',
				filter: function(q, row){
					if(row.productName == null)
						return false;
					return row.productName.indexOf(q) >= 0;
				}
			});
			
			$('#userId').combobox({
				data:userCache,
				valueField : 'id',
				textField : 'userName',
				filter: function(q, row){
					if(row.userName == null)
						return false;
					return row.userName.indexOf(q) >= 0;
				}
			});
			
			if(data != null){
				initScene(data.pId);
				$('#configurationId').combobox({
					valueField : 'chanpinconfigurationId',
					textField : 'chanpinconfigurationName',
					url : getContextPath() + '/portal/config/chanpin?chanpinId='+data.chanpinId
				});
			}else{
				// 0设定成特殊值，为新建对象时所用
				initScene(0); 
			}
		}
	}).dialog('open').dialog('center');
}
function initChanPinCache() {
	loadData(function(res) {
		chanpincache = res.rows;
		console.info('chanpincache',chanpincache);
	}, getContextPath() + "/portal/chanpin/list", null);
	loadData(function(res) {
		productcache = res;
		console.info('productcache',res);
	}, getContextPath() + '/portal/service/productSelect', null);
	loadData(function(res) {
		configcache = res;
		console.info('configcache',res);
	}, getContextPath() + '/portal/service/productSelect', null);
	loadData(function(res) {
		userCache = res;
		console.info('userCache',res);
	}, getContextPath() + '/portal/user/all', null);
}
function initScene(id) {
	var root = $(".sceneTag");
	root.html("");
	getData(function(res){
		if(res != null && res != undefined){
			var array = res.result.rows;
			for (var int = 0; int < array.length; int++) {
				var checked ="";
				var a= array[int];
				if(a.checked){
					checked = 'checked="checked"';
				}
				var ele = '<input type="checkbox" name="sceneTag"  '+checked+'  value="'+a.sceneName+'">' + a.sceneName;
				root.append(ele);
			}
		}
	}, getContextPath()+"/portal/case/scene/list?caseId="+id);
}
function save(){
	progressLoad();
	$('#fm').form('submit',{
		url : formUrl,
		success : function(result) {
			var res = $.evalJSON(result);
			progressClose();
			$('#dlg').dialog('close');
			datagrid.datagrid('clearSelections');
			datagrid.datagrid('reload');
			var msg = res.result == 1 ||  res.result == '1' ? '操作成功':'操作失败';
			$.message(res.errorMsg);
		}
	});
}
//查询
function searchFun(){
	//清空点击表的排序操作,例如按时间排序等
	$('#gride').datagrid('options').sortName = null;
	$('#gride').datagrid('options').sortOrder = null;
	datagrid.datagrid('load', $.serializeObject($('#searchForm')));
}
function cleanFun(){
	$('#searchpName').val('');
}
//取消
function cancelFuc(){
	//回滚数据 
	datagrid.datagrid('rejectChanges');
	editing = undefined;
}