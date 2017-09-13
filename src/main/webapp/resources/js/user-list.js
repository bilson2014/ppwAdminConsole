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
							} else if( value == 4){
								return '<span style=color:orange; >D</span>' ;
							}else{
								return '<span style=color:orange; >未分级</span>' ;
							}
						},
						editor:{
							type:'combobox' , 
							options:{
								data:[{id:'-1' , val:'未分级'},{id:3 , val:'S'},{id:0 , val:'A'},{id:1 , val:'B'},{id:2 , val:'C'},{id:4 , val:'D'}] ,
								valueField:'id' , 
								textField:'val' ,
								required:true , 
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
						field : 'customerType' ,
						title : '客户来源' ,
						align : 'center' ,
						width : 100,
						sortable : true ,
						formatter : function(value , record , index){
							switch (value) {
							case 1:
								return '<span style=color:blue; >渠道</span>' ;
							case 2:
								return '<span style=color:blue; >推广</span>' ;
							case 3:
								return '<span style=color:blue; >直客</span>' ;
							case 4:
								return '<span style=color:blue; >活动</span>' ;
							case 5:
								return '<span style=color:blue; >推荐</span>' ;
							case 6:
								return '<span style=color:blue; >电销</span>' ;
							case 7:
								return '<span style=color:blue; >新媒体</span>' ;
							case 8:
								return '<span style=color:blue; >线下拓展</span>' ;
							case 9:
								return '<span style=color:blue; >市场活动</span>' ;
							case 10:
								return '<span style=color:blue; >社区运营</span>' ;
							case 11:
								return '<span style=color:blue; >复购</span>' ;
							case 12:
								return '<span style=color:blue; >4A有策划</span>' ;
							case 13:
								return '<span style=color:blue; >4A无策划</span>' ;
							case 14:
								return '<span style=color:blue; >直客-线上-网站</span>' ;
							case 15:
								return '<span style=color:blue; >直客-线上-活动</span>' ;
							case 16:
								return '<span style=color:blue; >直客-线上-新媒体</span>' ;
							case 17:
								return '<span style=color:blue; >直客-线上-电销</span>' ;
							case 18:
								return '<span style=color:blue; >直客-线下-直销</span>' ;
							case 19:
								return '<span style=color:blue; >直客-线下-活动</span>' ;
							case 20:
								return '<span style=color:blue; >直客-线下-渠道</span>' ;
							case 21:
								return '<span style=color:blue; >直客-线上-400</span>' ;
							case 22:
								return '<span style=color:blue; >直客-线上-商桥</span>' ;
								
							case 23:
								return '<span style=color:blue; >直客-线上-PC-首页banner</span>' ;
							case 24:
								return '<span style=color:blue; >直客-线上-PC-直接下单</span>' ;
							case 25:
								return '<span style=color:blue; >直客-线上-PC-成本计算器</span>' ;
							case 26:
								return '<span style=color:blue; >直客-线上-PC-供应商首页</span>' ;
							case 27:
								return '<span style=color:blue; >直客-线上-PC-作品</span>' ;
								
							case 28:
								return '<span style=color:blue; >直客-线上-移动-首页banner</span>' ;
							case 29:
								return '<span style=color:blue; >直客-线上-移动-成本计算器</span>' ;
							case 30:
								return '<span style=color:blue; >直客-线上-移动-作品</span>' ;
								
							case 31:
								return '<span style=color:blue; >直客-线上-公众号-成本计算器</span>' ;
							case 32:
								return '<span style=color:blue; >直客-线上-公众号-直接下单</span>' ;
							case 33:
								return '<span style=color:blue; >直客-线上-公众号-作品</span>' ;
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
						field : 'imgUrl' ,
						title : '用户头像地址',
						width : 110,
						align : 'center' ,
						hidden : true
					},{
						field : 'officialSite' ,
						title : '网址',
						width : 110,
						align : 'center',
						formatter : function(value , record , index){
							if(value != '' && value != null && value != undefined)
								return value.trim();
							return value;
						}
					},{
						field : 'position' ,
						title : '职位',
						width : 110,
						align : 'center' ,
						formatter : function(value , record , index){
							switch (value) {
							case 0:
								return '<span style=color:blue; >创始团队：决策人</span>' ;
							case 1:
								return '<span style=color:blue; >高层：影响决策人</span>' ;
							case 2:
								return '<span style=color:blue; >中层：执行层/建议权</span>' ;
							case 3:
								return '<span style=color:blue; >基层：获取合作信息</span>' ;
							case 4:
								return '<span style=color:blue; >其他：非关联部门</span>' ;
							}
						}
					},{
						field : 'purchaseFrequency' ,
						title : '购买频次',
						width : 110,
						align : 'center' ,
						formatter : function(value , record , index){
							switch (value) {
							case 0:
								return '<span style=color:blue; >周复购</span>' ;
							case 1:
								return '<span style=color:blue; >月度复购</span>' ;
							case 2:
								return '<span style=color:blue; >季度复购</span>' ;
							case 3:
								return '<span style=color:blue; >年度复购</span>' ;
							}
						}
					},{
						field : 'purchasePrice' ,
						title : '购买价格',
						width : 110,
						align : 'center' ,
						formatter : function(value , record , index){
							switch (value) {
							case 0:
								return '<span style=color:blue; >均价采买价格10万以上</span>' ;
							case 1:
								return '<span style=color:blue; >均价采买价格5-10万</span>' ;
							case 2:
								return '<span style=color:blue; >均价采买价格3-5万</span>' ;
							case 3:
								return '<span style=color:blue; >均价采买价格1-3万</span>' ;
							case 4:
								return '<span style=color:blue; >均价采买价格1万内</span>' ;
							}
						}
					},{
						field : 'customerSize' ,
						title : '客户规模',
						width : 110,
						align : 'center' ,
						formatter : function(value , record , index){
							switch (value) {
							case 0:
								return '<span style=color:blue; >上市公司及同等规模</span>' ;
							case 1:
								return '<span style=color:blue; >b轮以上及同等规模</span>' ;
							case 2:
								return '<span style=color:blue; >天使轮以上及同等规模</span>' ;
							case 3:
								return '<span style=color:blue; >小微企业／个体</span>' ;
							}
						}
					},{
						field : 'endorse' ,
						title : '高层背书',
						width : 110,
						align : 'center' ,
						formatter : function(value , record , index){
							switch (value) {
							case 0:
								return '<span style=color:blue; >有高层背书</span>' ;
							case 1:
								return '<span style=color:blue; >无高层背书</span>' ;
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
					}
					]],
		pagination: true ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar'
	});
	initRating();
});

//增加
function addFuc(){
	$('#fm').form('clear');
	openDialog(null);
	$('.referrer').hide();
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
		if(rows[0].customerType == 18)//OFFLINE_DIRECT_SELLING
			$('.referrer').show();
		else
			$('.referrer').hide();
		
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
        		if(value.trim() != originalPhoneNumber.trim()){
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

function initRating(){
	$('#referrerId').combobox({
		url : getContextPath() + '/portal/getEmployeeList',
		valueField : 'employeeId',
		textField : 'employeeRealName'
	});
	
	loadData(function(res){
		$('#position').combobox({
			data : res.result.position,
			valueField : 'id',
			textField : 'text'
		});
		$('#customerType').combobox({
			data : res.result.customerType,
			valueField : 'id',
			textField : 'text',
			onSelect : function(record){
				if(record.text == '直客-线下-直销'){
					$('.referrer').show();
				}else{
					$('.referrer').hide();
				}
			}
		});
		$('#tCustomerType').combobox({
			data : res.result.customerType,
			valueField : 'id',
			textField : 'text'
		});
		$('#purchaseFrequency').combobox({
			data : res.result.purchaseFrequency,
			valueField : 'id',
			textField : 'text'
		});
		$('#tPurchaseFrequency').combobox({
			data : res.result.purchaseFrequency,
			valueField : 'id',
			textField : 'text'
		});
		$('#purchasePrice').combobox({
			data : res.result.purchasePrice,
			valueField : 'id',
			textField : 'text'
		});
		$('#tPurchasePrice').combobox({
			data : res.result.purchasePrice,
			valueField : 'id',
			textField : 'text'
		});
		$('#customerSize').combobox({
			data : res.result.customerSize,
			valueField : 'id',
			textField : 'text'
		});
		$('#tCustomerSize').combobox({
			data : res.result.customerSize,
			valueField : 'id',
			textField : 'text'
		});
		$('#endorse').combobox({
			data : res.result.endorse,
			valueField : 'id',
			textField : 'text'
		});
		$('#tEndorse').combobox({
			data : res.result.endorse,
			valueField : 'id',
			textField : 'text'
		});
		$('#searchForm').form('clear');
		$('#clientLevel').combobox('setValue',-1);
	}, getContextPath() + '/portal/user/option', null);
}
