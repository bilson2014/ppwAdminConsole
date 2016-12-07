var editing; // 判断用户是否处于编辑状态
var flag; // 判断新增和修改方法
var formUrl;
var datagrid;
$().ready(function() {
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/get/activities',
		idField : 'activityId',
		title : '用户列表',
		fitColumns : true,
		striped : true,
		loadMsg : '数据正在加载,请耐心的等待...',
		rownumbers : true,
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		} ] ],
		columns : [ [ {
			field : 'activityId',
			title : 'ID',
			width : 60,
			align : 'center'
		}, {
			field : 'activityName',
			title : '名称',
			width : 60,
			align : 'center'
		}, {
			field : 'activityCreateTime',
			title : '创建时间',
			width : 60,
			align : 'center'
		}, {
			field : 'acticityTempleteType',
			title : '类型',
			width : 60,
			align : 'center'
		}, {
			field : 'acticityTempleteId',
			title : '模板',
			width : 60,
			align : 'center'
		}, {
			field : 'activityParamList',
			title : '参数列表',
			align : 'center',
			hidden : true,
			width : 60
		}, {
			field : 'activityStartTime',
			title : '开始时间',
			align : 'center',
			width : 60
		} ] ],
		pagination : true,
		pageSize : 50,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar'
	});

	$("#acticityTempleteType").combobox({
		onSelect : function(param) {
			if (param != null && param != undefined) {
				var type = param.value;
				var url = getContextPath();
				if (type == 0) {
					url += '/portal/sms/list';
				}else{
					url += '/portal/mail/list';
				}
				$.ajax({
					url : url,
					type : 'POST',
					data : {"page":1,"rows":500},
					dataType : 'json',
					success : function(data){
						if (type == 0) {
							$("#acticityTempleteId").combobox({
								valueField : 'tempId',
								textField : 'tempTitle',
								data:data.rows
							});
						}else{
							$("#acticityTempleteId").combobox({
								valueField : 'id',
								textField : 'subject',
								data:data.rows
							});
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.error('ajax(' + url + ')[' + jqXHR.status + ']' + jqXHR.statusText);
						console.error(jqXHR.responseText);
						console.error('[' + textStatus + ']' + errorThrown);
					}
				});
			}
		}
	});
	
	$('#addSystemParam').on('click',function(){
		addParamBlock('system');
	});
	$('#addCustomParam').on('click',function(){
		addParamBlock('custom');
	});
	
});

// 打开dialog
function openDialog(id, data) {
	$('#' + id).dialog({
		modal : true,
		onOpen : function(event, ui) {
		}
	}).dialog('open').dialog('center');
}

// 增加
function addFuc() {
	initDel();
	$('#fm').form('clear');
	openDialog('dlg', null);
	formUrl =getContextPath()+'/portal/post/activity';
	$('input[name="employeeId"]').val(0);
}
// 修改
function editFuc() {
	$('#fm').form('clear');
	openDialog('dlg', null);
	formUrl = getContextPath() + '/portal/employee/save';
	$('input[name="employeeId"]').val(0);
}

function save(){
	progressLoad();
	loadData(function(msg){
		progressClose();
		// 刷新数据
		datagrid.datagrid('clearSelections');
		datagrid.datagrid('reload');
		$('#dlg').dialog('close')
		$.message('操作成功!');
	}, formUrl, $.toJSON({
		activityName : $('#activityName').val(),
		acticityTempleteType : $('#acticityTempleteType').combobox('getValue'),
		acticityTempleteId : $('#acticityTempleteId').combobox('getValue'),
		paramList : buildParam(),
		activityStartTime : $('#activityStartTime').datetimebox('getValue')
	}));
}



function createParamView(system){
	if(system == 'system'){
		var html = [
		            '<div class="fitem param system">',
		            '	<label>参数选择：</label>',
		            '	<select class="easyui-combobox p1" style="width: 164px" required="true">',
		            '		<option value="0">供应商名称</option>',
		            '		<option value="1">客户名称</option>',
		            '	</select>',
		            '	<a id="addSystemParam" href="#" class="easyui-linkbutton addParamBlock" data-options="iconCls:\'icon-remove\'"></a>',
		            '</div>'
		            ].join('');
		return html;
	}else{
		var html = [
		            '<div class="fitem param custom">',
		            '	<label>参数选择：</label>',
		            '	<input class="easyui-textbox p1" required="true">',
		            '	<a id="addSystemParam" href="#" class="easyui-linkbutton addParamBlock" data-options="iconCls:\'icon-remove\'"></a>',
		            '</div>'
		            ].join('');
		return html;
	}
}

//->  添加
function addParamBlock(type){
	var root = $('#paramList');
	 //添加模板
	var html = createParamView(type);
	var newSynergy = $(html).appendTo("#paramList");
	 //渲染easyUI
	 $.parser.parse($(newSynergy));
	 
	 delParamBlock();
}
function initDel(){
	$('.addParamBlock').parent().remove();
}
// -> 删除
function delParamBlock(){
	$('.addParamBlock').off('click').on('click',function(){
		$(this).parent().remove();
	});
}

function buildParam(){
	var paramElementArray = $('.param');
	var paramArray = new Array();
	if(paramElementArray.length >0){
		for (var int = 0; int < paramElementArray.length; int++) {
			var element = paramElementArray[int];
			if($(element).hasClass('system')){
				// 系统参数
				var selector = $(element).find('.p1');
				var value = $(selector).combobox('getValue');
				var p1 = new param(0,value);
				paramArray.push(p1);
			}else{
				// 自定义参数
				// 系统参数
				var selector = $(element).find('.p1');
				var value = $(selector).textbox('getValue');
				var p1 = new param(1,value);
				paramArray.push(p1);
			}
		}
	}
	return paramArray;
}

function param(type,value){
	this.type = type;
	this.value = value;
	
	this.setType = function(type){
		this.type = type;
	};
	this.setValue = function(value){
		this.value = value;
	};
}