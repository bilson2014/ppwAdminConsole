var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var datagrid;
var formUrl;
//验证
var isadd = false;
var originalLoginName = '';
var originalPhoneNumber = '';
$().ready(function(){

	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/user/list',
		idField : 'id' ,
		title : '用户列表' , 
		// fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'userName',
						title : '昵称',
						width : 150,
						align : 'center' ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : true , 
								missingMessage : '请填写昵称!'
							},
						}
					},{
						field : 'realName',
						title : '真实姓名',
						width : 150,
						align : 'center' ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false , 
								missingMessage : '请填写真实姓名!'
							}
						}
					},{
						field : 'clientLevel',
						title : '客户级别',
						width : 80,
						align : 'center' ,
						sortable : true ,
						formatter : function(value , record , index){
							if(value == 0){
								return '<span style=color:red; >A</span>' ;
							} else if( value == 1){
								return '<span style=color:blue; >B</span>' ; 
							} else if( value == 2){
								return '<span style=color:green; >C</span>' ;
							} else if( value == 3){
								return '<span style=color:black; >S</span>' ;
							} else {
								return '<span style=color:orange; >未分级</span>' ;
							}
						},
						editor:{
							type:'combobox' , 
							options:{
								data:[{id:'-1' , val:'未分级'},{id:3 , val:'S'},{id:0 , val:'A'},{id:1 , val:'B'},{id:2 , val:'C'}] ,
								valueField:'id' , 
								textField:'val' ,
								required:true , 
								editable : false
							}
						}
					},{
						field : 'preference',
						title : '客户意向度',
						width : 80,
						align : 'center' ,
						sortable : true ,
						formatter : function(value , record , index){
							if(value == 0){
								return '<span style=color:red; >A</span>' ;
							} else if( value == 1){
								return '<span style=color:blue; >B</span>' ; 
							} else if( value == 2){
								return '<span style=color:green; >C</span>' ;
							} else if( value == 3){
								return '<span style=color:black; >D</span>' ;
							}
						},
						editor:{
							type:'combobox' , 
							options:{
								data:[{id:0 , val:'A'},{id:1 , val:'B'},{id:2 , val:'C'},{id:3 , val:'D'}] ,
								valueField:'id' , 
								textField:'val' ,
								required:false , 
								editable : false
							}
						}
					},{
						field : 'userCompany',
						title : '客户公司',
						width : 150,
						align : 'center' ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false , 
								missingMessage : '请填写客户公司!'
							}
						}
					},{
						field : 'sex' ,
						title : '性别' ,
						align : 'center' ,
						width : 60,
						formatter : function(value , record , index){
							if(value == 0){
								return '<span style=color:blue; >男</span>' ;
							} else if( value == 1){
								return '<span style=color:red; >女</span>' ; 
							} else if( value == 2){
								return '<span style=color:green; >保密</span>' ;
							}
						},
						editor:{
							type:'combobox' , 
							options:{
								data:[{id:0 , val:'男'},{id:1 , val:'女'},{id:2 , val:'保密'}] ,
								valueField:'id' , 
								textField:'val' ,
								required:true , 
								editable : false
							}
						}
					},{
						field : 'password',
						title : '密码',
						width : 110,
						align : 'center' ,
						hidden : true
					},{
						field : 'telephone' ,
						title : '联系电话' ,
						align : 'center' ,
						width : 100,
						editor : {
							type : 'validatebox' ,
							options : {
								required:true ,
								missingMessage : '请填写联系电话!',
								validType:['mobile','vuPhoneNumber']
							}
						}
					},{
						field : 'email',
						title : '电子邮件',
						width : 120,
						align : 'center',
						editor : {
							type : 'validatebox' ,
							options : {
								required : false ,
								missingMessage : '请填写电子邮件!',
								validType:'email',
								invalidMessage : '邮箱格式不正确'
							}
						}
					},{
						field : 'qq',
						title : 'QQ',
						width : 80,
						align : 'center',
						editor : {
							type : 'validatebox' ,
							options : {
								required : false ,
								missingMessage : '请填写QQ号码!'
							}
						}
					},{
						field : 'weChat',
						title : '微信',
						width : 80,
						align : 'center',
						editor : {
							type : 'validatebox' ,
							options : {
								required : false ,
								missingMessage : '请填写微信号!'
							}
						}
					},{
						field : 'customerSource' ,
						title : '客户来源' ,
						align : 'center' ,
						width : 60,
						sortable : true ,
						formatter : function(value , record , index){
							switch (value) {
							case 1:
								return '<span style=color:blue; >渠道</span>' ;
							case 2:
								return '<span style=color:blue; >推广</span>' ;
							case 3:
								return '<span style=color:blue; >自主开发</span>' ;
							case 4:
								return '<span style=color:blue; >活动</span>' ;
							case 5:
								return '<span style=color:blue; >推荐</span>' ;
							case 6:
								return '<span style=color:blue; >电销</span>' ;
							}
						},
						editor:{
							type:'combobox' , 
							options:{
								data:[{id:1 , val:'渠道'},{id:2 , val:'推广'},{id:3 , val:'自主开发'},{id:4 , val:'活动'},{id:5 , val:'推荐'},{id:6 , val:'电销'}] ,
								valueField:'id' , 
								textField:'val' ,
								required:true , 
								editable : false
							}
						}
					},{
						field : 'followTime',
						title : '跟进日期',
						width : 120,
						align : 'center',
						sortable : true,
						editor : {
							type : 'datebox' ,
							options:{
									required:true , 
									missingMessage : '请填写跟进日期!'
								}
						}
					},{
						field : 'kindlySend',
						title : '是否推送',
						width : 80,
						align : 'center',
						formatter : function(value , record , index){
							if(value){
								return '<span style=color:blue; >推送</span>' ;
							}else{
								return '<span style=color:red; >不推送</span>' ;
							}
						},
						editor:{
							type:'combobox' , 
							options:{
								data:[{id:1 , val:'推送'},{id:0 , val:'不推送'}] ,
								valueField:'id' , 
								textField:'val' ,
								required:true , 
								editable : false
							}
						}
					},{
						field : 'createDate',
						title : '注册日期',
						width : 120,
						align : 'center',
						sortable : true 
					},{
						field : 'updateTime',
						title : '更新日期',
						width : 120,
						align : 'center',
						sortable : true 
					},{
						field : 'birthday',
						title : '出生日期',
						width : 120,
						align : 'center' ,
						hidden : true,
						editor : {
							type : 'datebox' ,
							options : {
								required : false ,
								missingMessage : '请选择出生日期!'
							}
						}
					},{
						field : 'note',
						title : '备注信息',
						width : 120,
						align : 'center' ,
						editor : {
							type : 'validatebox' ,
							options : {
								required : false , 
								missingMessage : '请填写备注信息!'
							}
						}
					},{
						field : 'imgUrl' ,
						title : '用户头像地址',
						width : 110,
						align : 'center' ,
						hidden : true
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
	$('#fm').form('clear');
	openDialog(null);
	isadd = true;
	$("#userId").val(0);
	formUrl = getContextPath() + '/portal/user/save';
}

//修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		isadd = false;
		originalPhoneNumber = rows[0].telephone;
		$('#fm').form('clear');
		$('#fm').form('load',rows[0]);
		openDialog('dlg',rows[0]);
		formUrl = getContextPath() + '/portal/user/update';
	} else {
		$.message('只能选择一条记录进行修改!');
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
					ids += arr[i].id + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/user/delete', {ids:ids},function(result){
					
					// 刷新状态
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
function save(){
	progressLoad();
	$('#fm').form('submit',{
		url : formUrl,
		onSubmit : function(param) {
			var flag = $(this).form('validate');
			if(!flag){
				progressClose();
				$.message('请将信息填写完整!');
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
	//清空点击表的排序操作,例如按时间排序等
	$('#gride').datagrid('options').sortName = null;
	$('#gride').datagrid('options').sortOrder = null;
	datagrid.datagrid('load', $.serializeObject($('#searchForm')));
}

//清除
function cleanFun() {
	$('#searchForm').form('clear');
	$('#clientLevel').combobox('setValue',-1);
	datagrid.datagrid('load', {clientLevel:-1});
}

$.extend($.fn.validatebox.defaults.rules, {
    vuPhoneNumber : {
        validator : function(value, param) {
        	var url = getContextPath() + '/portal/user/valication/phone/'+value;
			var isok = false;
        	if(isadd){
    			syncLoadData(function (res) {
    				isok = !res;
    			}, url, null);
    			return isok;
        	}else{
        		if(value != originalPhoneNumber){
        			// 验证手机
        			syncLoadData(function (res) {
        				isok = !res;
        			}, url, null);
        			return isok;
        		}
        	}
        	return true;
        },
        message : '手机号已经重复！'  
    }  
});
$.extend($.fn.validatebox.defaults.rules, {
    vuNickName : {
        validator : function(value, param) {
        	var url = getContextPath() + '/portal/user/unique/username';
			var isok = false;
			syncLoadData(function (res) {
				isok = res;
			}, url, $.toJSON({
				'userName' : value,
				'id' : $("#userId").val()
			}));
			return isok;
        },
        message : '昵称被占用！'  
    }  
});
//打开dialog
function openDialog(data){
	$('#dlg').dialog({
		modal : true,
		onOpen : function(event, ui) {
			
		},
	}).dialog('open').dialog('center');
}
