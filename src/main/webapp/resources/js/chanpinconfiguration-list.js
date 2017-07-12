var chanpincache = '';
var modulecache = '';
var flag; // 判断新增和修改方法
var formUrl;
var datagrid;
$().ready(function() {
	initChanPinCache();
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/config/list',
		idField : 'chanpinconfigurationId',
		title : '配置/价格列表',
		fitColumns : true,
		striped : true,
		loadMsg : '数据正在加载,请耐心的等待...',
		rownumbers : true,
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		} ] ],
		columns : [ [ {
			field : 'chanpinconfigurationId',
			title : 'ID',
			width : 60,
			align : 'center'
		}, {
			field : 'chanpinconfigurationName',
			title : '名称',
			width : 60,
			align : 'center'
		}, {
			field : 'chanpinId',
			title : '产品名称',
			width : 60,
			align : 'center',
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
			field : 'chanpinconfigurationDescription',
			title : '描述',
			width : 60,
			align : 'center'
		}, {
			field : 'price',
			title : '配置总价',
			width : 60,
			align : 'center',
			formatter : function(value,row,index){
				return '---';
			}
		}, {
			field : 'tags',
			title : '标签',
			width : 60,
			align : 'center'
		}, {
			field : 'chanpinconfigurationCreateTime',
			title : '创建时间',
			align : 'center',
			width : 60
		}, {
			field : 'chanpinconfigurationUpdateTime',
			title : '更新时间',
			align : 'center',
			width : 60
		} ] ],
		pagination : true,
		pageSize : 50,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar'
	});
});
// 打开dialog
function openDialog(id, data) {
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {
			$(".basemodule").html('');
			$(".additivemodule").html('');
			$(".dimensionmodule").html('');
			$('#chanpinId').combobox({
				valueField : 'chanpinId',
				textField : 'chanpinName',
				data:chanpincache,
				onSelect : function(){
				}
			});
		}
	}).dialog('open').dialog('center');
}

// 增加
function addFuc() {
	$('#fm').form('clear');
	openDialog('dlg', null);
	formUrl = getContextPath() + '/portal/config/save';
}
// 修改
function editFuc() {
	var rows = datagrid.datagrid('getSelections');
	if (rows.length == 1) {
		$('#fm').form('clear');
		formUrl = getContextPath() + '/portal/config/update';
		var val ;
		syncLoadData(function (res){
			val = res.result;
		}, getContextPath() +'/portal/config/info/'+rows[0].chanpinconfigurationId, null);
		
		openDialog('dlg', val);
		$('#fm').form('load', val);
		initModule(val);
	} else {
		$.message('只能选择一条记录进行修改!');
	}

}
function delFuc() {
	var arr = datagrid.datagrid('getSelections');
	if (arr.length <= 0) {
		$.message('请选择进行删除操作!');
	} else {
		$.messager.confirm('提示信息', '确认删除?', function(r) {
			if (r) {
				var ids = '';
				for (var i = 0; i < arr.length; i++) {
					ids += arr[i].chanpinconfigurationId + ',';
				}
				ids = ids.substring(0, ids.length - 1);
				$.post(getContextPath() + '/portal/config/delete', {
					ids : ids
				}, function(result) {
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

function save() {
	progressLoad();
	loadData(function(res){
		progressClose();
		$('#dlg').dialog('close');
		datagrid.datagrid('clearSelections');
		datagrid.datagrid('reload');
		progressClose();
		$.message(res.result);
	}, formUrl , $.toJSON({
		"chanpinconfigurationName":$('#chanpinconfigurationName').val(),
		"chanpinId":$('#chanpinId').combobox('getValue'),
		"chanpinconfigurationDescription":$('#chanpinconfigurationDescription').val(),
		"tags":$('#tags').val(),
		"pmsProductModule":buildModuleParam(),
		"pmsDimensions":buildDimensionParam(),
		"chanpinconfigurationId":$('#chanpinconfigurationId').val()
	}));
}
function searchFun() {
	datagrid.datagrid('load', $.serializeObject($('#searchForm')));
}
function cleanFun() {
	$('#searchFormactivityName').val('');
}

function initChanPinCache() {
	syncLoadData(function(res) {
		chanpincache = res.rows;
	}, getContextPath() + "/portal/chanpin/list", null);
	syncLoadData(function(res) {
		modulecache = res;
	}, getContextPath() + "/portal/module/list", null);
}

function addBaseModule(mId,cpmId,price,close){
	var module = $(".basemodule");
	if(price == undefined)
		price = 0;
	if(cpmId == undefined)
		cpmId = '';
	 //添加模板
	var html = createBaseModuleView(cpmId,price);
	var newModule = $(html).appendTo(".basemodule");
	 //渲染easyUI
	 $.parser.parse($(newModule));
	 var box =  $(".moduleName").length - 1;
	 $(".moduleName:eq("+box+")").combotree({
		    idField : 'productModuleId',
		    treeField : 'moduleName',
		    parentField:'pid',
			data:modulecache,
			onLoadSuccess: function () { //数据加载完毕事件
				if(mId != null && mId != undefined)
				$(".moduleName:eq("+box+')').combotree('setValue', mId);
         	}
	 });
	 //if(close == undefined)
		 //$(".moduleName:eq("+box+")").combotree('tree').tree("collapseAll"); 
	 delBaseModule();
}

function addAdditiveModule(mId,cpmId,price,close){
	var module = $(".additivemodule");
	if(price == undefined)
		price = 0;
	if(cpmId == undefined)
		cpmId = '';
	//添加模板
	var html = createAdditiveModuleView(cpmId,price);
	var newModule = $(html).appendTo(".additivemodule");
	//渲染easyUI
	$.parser.parse($(newModule));
	var box =  $(".additiveModuleName").length - 1;
	$(".additiveModuleName:eq("+box+")").combotree({
		idField : 'productModuleId',
	    treeField : 'moduleName',
	    parentField:'pid',
		data:modulecache,
		onLoadSuccess: function () { //数据加载完毕事件
			if(mId != null && mId != undefined)
			$(".additiveModuleName:eq("+box+')').combotree('setValue', mId);
		}
	});
	//if(close == undefined)
		//$(".additiveModuleName:eq("+box+")").combotree('tree').tree("collapseAll"); 
	delAdditiveModule();
}

function addDimensionModule(dimensionId,name,value,computeType,dimensionNameId){
	if(name == undefined){
		name = "0";
	}
	
	var module = $(".dimensionmodule");
	if(value == undefined)
		value = 0;
	if(dimensionId == undefined)
		dimensionId = '';
	//添加模板
	var html = createDimensionModuleView(dimensionId,value,name);
	var newModule = $(html).appendTo(".dimensionmodule");
	//渲染easyUI
	$.parser.parse($(newModule));
	var box =  $(".dimensionModuleName").length - 1;
	$(".dimensionModuleName:eq("+box+')').combobox('select', dimensionNameId);
	var box =  $(".computeType").length - 1;
	$(".computeType:eq("+box+')').combobox('select', computeType);
	delDimension();
}


function createBaseModuleView(cpmId,price){
	var $body=['<div class="moduleBlock">',
	'<input type="hidden" id="cpmId" value="'+cpmId+'">',
	'模块：<select id="moduleName" style="width: 260px" class=" moduleName easyui-combotree" required="true"></select>',
	'价格： <input id="modulePrice" class="easyui-textbox" style="width: 50px" value="'+price+'" required="true">',
	'<a href="javascript:void(0);" class="easyui-linkbutton basemodule-del" data-options="plain:true,iconCls:\'icon-remove\'"></a>',
    '</div>',
    '<br/>'].join('');
	return $body;
}
function createAdditiveModuleView(cpmId,price){
	var $body=['<div class="moduleAdditiveBlock">',
	           '<input type="hidden" id="additiveCpmId" value="'+cpmId+'">',
	           '模块：<select id="additiveModuleName" style="width: 260px" class=" additiveModuleName easyui-combotree" required="true"></select>',
	           '价格： <input id="additiveModulePrice" class="easyui-textbox" style="width: 50px" value="'+price+'" required="true">',
	           '<a href="javascript:void(0);" class="easyui-linkbutton additivemodule-del" data-options="plain:true,iconCls:\'icon-remove\'"></a>',
	           '</div>',
	           '<br/>'].join('');
	return $body;
}

function createDimensionModuleView(dimensionId,value,name){
	var dimension = ['<option value="0">时长</option>'
	                 ].join('');
	var computeType = [
		               	'<option value="0">乘</option>',
		           		'<option value="1">加</option>',
		           		'<option value="2">减</option>'
	                  ].join('');
	var $body=['<div class="moduleDimensionBlock">',
	           		'<input type="hidden" id="dimensionId" value="'+dimensionId+'">',
		           '维度：<select id="dimensionModuleName" style="width: 80px" class=" dimensionModuleName easyui-combobox" required="true">',
		           dimension,
		           '</select>',
		           '计算模式：<select id="computeType" style="width: 40px" class="computeType easyui-combobox" required="true">',
		           computeType,
		           '</select>',
		           '维度列：<input id="dimensionRowName" class="easyui-textbox dimensionRowName" style="width: 50px" value="'+name+'" required="true">',
		           '维度值： <input id="dimensionRowValue" class="easyui-textbox" style="width: 50px" value="'+value+'" required="true">',
		           '<a href="javascript:void(0);" class="easyui-linkbutton dimension-del" data-options="plain:true,iconCls:\'icon-remove\'"></a>',
	           '</div>',
	           '<br/>'].join('');
	return $body;
}
/**
 * 构造维度列名,存在基础的提示信息
 * @param index
 */
function rowNameArray(index){
	var html = '';
	switch (index) {
	case 0:
		html = [
		        '<option>1分钟</option>',
		        '<option>2分钟</option>',
		        '<option>3分钟</option>',
		        '<option>4分钟</option>',
		        '<option>5分钟</option>',
		        '<option>10分钟</option>'
		       ];
		break;
	case 1:
		html = [
		        '<option>1分钟</option>',
		        '<option>2分钟</option>',
		        '<option>3分钟</option>',
		        '<option>4分钟</option>',
		        '<option>5分钟</option>',
		        '<option>10分钟</option>'
		       ];
		break;
	}
	html.join('');
	return html;
}

/**
 * 内部实体
 */
function ModuleEntity_ (cpmId,cpmModulePrice,cpmModuleType){
	this.cpmModulePrice = cpmModulePrice;
	this.cpmModuleType = cpmModuleType;
	this.cpmId = cpmId;
}

/**
 * 实体
 */
function ModuleEntity (productModuleId,m){
	this.productModuleId = productModuleId;
	this.pinConfiguration_ProductModule = m;
}

function DimensionEntity(dimensionNameId,computeType,rowName,rowValue,dimensionId,cfId){
	this.dimensionNameId = dimensionNameId;
	this.computeType = computeType;
	this.rowName = rowName;
	this.rowValue = rowValue;
	
	this.dimensionId = dimensionId;
	this.cfId = cfId;
}


/**
 * 获取页面参数并转换成后台数据结构
 */
function buildModuleParam(){
	var rootView = $('.moduleBlock');
	var rootAdditiveView = $('.moduleAdditiveBlock');
	var param = new Array();
	if(rootView != null){
		for (var int = 0; int < rootView.length; int++) {
			var view = $(rootView[int]);
			var cpmModulePrice = view.find('#modulePrice').val();
			var cpmModuleType = 0;
			var productModuleId = view.find('#moduleName').combotree('getValue');
			var cpmId = view.find('#cpmId').val();
			var moduleEntity_ = new  ModuleEntity_(cpmId,cpmModulePrice,cpmModuleType);
			var moduleEntity = new ModuleEntity(productModuleId,moduleEntity_);
			param.push(moduleEntity);
		}
	}
	if(rootAdditiveView != null){
		for (var int = 0; int < rootAdditiveView.length; int++) {
			var view = $(rootAdditiveView[int]);
			var cpmModulePrice = view.find('#additiveModulePrice').val();
			var cpmModuleType = 1;
			var productModuleId = view.find('#additiveModuleName').combotree('getValue');
			var cpmId = view.find('#additiveCpmId').val();
			var moduleEntity_ = new  ModuleEntity_(cpmId,cpmModulePrice,cpmModuleType);
			var moduleEntity = new ModuleEntity(productModuleId,moduleEntity_);
			param.push(moduleEntity);
		}
	}
	return param;
}
/**
 * 获取页面参数并转换成后台数据结构
 */
function buildDimensionParam(){
	var rootDimensionView = $('.moduleDimensionBlock');
	var param = new Array();
	if(rootDimensionView != null){
		for (var int = 0; int < rootDimensionView.length; int++) {
			var view = $(rootDimensionView[int]);
			var dimensionNameId = view.find('#dimensionModuleName').combobox('getValue');
			var computeType = view.find('#computeType').combobox('getValue');
			var rowName = view.find('#dimensionRowName').val();
			var rowValue = view.find('#dimensionRowValue').val();
			var dimensionId = view.find('#dimensionId').val();
			var cfId = $('#chanpinconfigurationId').val();
			// TODO: function DimensionEntity(dimensionNameId,computeType,rowName,rowValue,dimensionId,cfId){
			var dimensionEntity = new DimensionEntity(dimensionNameId,computeType,rowName,rowValue,dimensionId,cfId);
			param.push(dimensionEntity);
		}
	}
	return param;
}

function savePic(){
	progressLoad();
	$('#pic').form('submit',{
		url : '/portal/config/saveimg',
		success : function(result) {
			var res = $.evalJSON(result);
			progressClose();
			$('#dlgPicForm').dialog('close');
			datagrid.datagrid('clearSelections');
			datagrid.datagrid('reload');
			$.message(res.errorMsg);
		}
	});
}
function setPic(){
	var rows = datagrid.datagrid('getSelections');
	if (rows.length == 1) {
		$('#pic').form('clear');
		$('#dlgPicForm').dialog({
			modal : true
		}).dialog('open').dialog('center');
		$('#pic').form('load', rows[0]);
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}

function initModule(obj){
	var mod = obj.pmsProductModule;
	if(mod!=null){
		for (var int = 0; int < mod.length; int++) {
			var x = mod[int];
			var xx= x.pinConfiguration_ProductModule.cpmModuleType;
			if(xx == 0){
				addBaseModule(x.productModuleId,x.pinConfiguration_ProductModule.cpmId,x.pinConfiguration_ProductModule.cpmModulePrice,false);
			}else{
				addAdditiveModule(x.productModuleId,x.pinConfiguration_ProductModule.cpmId,x.pinConfiguration_ProductModule.cpmModulePrice,false);
			}
		}
	}
	var dimensions = obj.pmsDimensions;
	if(dimensions!=null){
		for (var int = 0; int < dimensions.length; int++) {
			var x = dimensions[int];
			addDimensionModule(x.dimensionId,x.rowName,x.rowValue,x.computeType,x.dimensionNameId);
		}
	}
}

function delBaseModule(){
	$(".basemodule-del").unbind('click').on("click",function(){
		$(this).parent().next('br').remove();
		$(this).parent().remove();
	});
}
function delAdditiveModule(){
	$(".additivemodule-del").unbind('click').on("click",function(){
		$(this).parent().next('br').remove();
		$(this).parent().remove();
	});
}
function delDimension(){
	$(".dimension-del").unbind('click').on("click",function(){
		$(this).parent().next('br').remove();
		$(this).parent().remove();
	});
}
