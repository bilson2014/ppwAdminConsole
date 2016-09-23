var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var formUrl;
var datagrid;
// 验证
var isadd = false;
var originalLoginName = '';
var originalPhoneNumber = '';

var list = new Array();
var idList = new Array();
$().ready(function(){
	
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/team/list',
		idField : 'teamId' ,
		title : '供应商管理列表' , 
		//fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'teamName',
						title : '公司名称',
						width : 160,
						align : 'center'
					},{
						field : 'loginName',
						title : '登录名',
						width : 100,
						align : 'center'
					},{
						field : 'flag' ,
						title : '审核状态' ,
						align : 'center' ,
						width : 80,
						sortable : true ,
						formatter : function(value , record , index){
							if(value == 0){
								return '<span style=color:blue; >审核中</span>' ;
							} else if( value == 1){
								return '<span style=color:green; >审核通过</span>' ; 
							} else if( value == 2){
								return '<span style=color:red; >未通过审核</span>' ;
							} else if( value == 4){
								return '<span style=color:black; >幽灵模式</span>' ;
							} 
						}
					},{
						field : 'teamProvinceName',
						title : '所在省',
						width : 100,
						align : 'center'
					},{
						field : 'teamCityName',
						title : '所在城市',
						width : 100,
						align : 'center'
					},{
						field : 'linkman',
						title : '联系人',
						width : 100,
						align : 'center'
					},{
						field : 'updateDate' ,
						title : '更新时间' ,
						align : 'center' ,
						width : 150,
						sortable : true 
					},{
						field : 'createDate' ,
						title : '创建时间' ,
						align : 'center' ,
						width : 150,
						sortable : true 
					},{
						field : 'phoneNumber',
						title : '手机号码',
						width : 100,
						align : 'center'
					},{
						field : 'webchat',
						title : '微信号',
						width : 100,
						align : 'center'
					},{
						field : 'qq',
						title : 'QQ',
						width : 100,
						align : 'center'
					},{
						field : 'email',
						title : '邮箱',
						width : 150,
						align : 'center'
					},{
						field : 'officialSite',
						title : '官网地址',
						width : 150,
						align : 'center',
						formatter : function(value , record , index){
							if(value != null && value != undefined && value != ''){
								return '<a href="'+ value +'" target="_blank">'+ value +'</a>' ;
							}
						}
					},{
						field : 'address',
						title : '公司地址',
						width : 150,
						align : 'center'
					},{
						field : 'teamPhotoUrl',
						title : 'LOGO',
						width : 150,
						align : 'center'
					},{
						field : 'teamDescription' ,
						title : '公司介绍' ,
						align : 'center' ,
						width : 200,
						align : 'center'
					},{
						field : 'establishDate' ,
						title : '成立时间' ,
						align : 'center' ,
						width : 100
					},{
						field : 'priceRange' ,
						title : '价格区间' ,
						align : 'center' ,
						width : 100 ,
						formatter : function(value , record , index){
							if(value == 0){
								return '<span style=color:red; >看情况</span>' ;
							} else if( value == 1){
								return '<span style=color:red; > >= 1W</span>' ; 
							} else if( value == 2){
								return '<span style=color:red; > >= 2W</span>' ;
							} else if( value == 3){
								return '<span style=color:red; > >= 3W</span>' ;
							} else if(value == 4){
								return '<span style=color:red; > >= 5W</span>' ;
							} else if(value == 5){
								return '<span style=color:red; > >= 10W</span>' ;
							}
						}
					},{
						field : 'recommendation',
						title : '审核意见',
						width : 80,
						align : 'center',
						formatter : function(value , record , index){
							if(value == 'null'){
								return '' ;
							}
						},
						hidden : true
					},{
						field : 'scale',
						title : '公司规模',
						width : 80,
						align : 'center',
						hidden : true
					},{
						field : 'business',
						title : '业务范围',
						width : 80,
						align : 'center',
						hidden : true
					},{
						field : 'businessDesc',
						title : '主要客户',
						width : 80,
						align : 'center',
						hidden : true
					},{
						field : 'demand',
						title : '对客户的要求',
						width : 80,
						align : 'center',
						hidden : true
					},{
						field : 'infoResource',
						title : '获知渠道',
						width : 80,
						align : 'center',
						hidden : true
					},{
						field : 'description',
						title : '备注',
						width : 80,
						align : 'center',
						hidden : true
					}]] ,
		pagination: true ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar',
		onDblClickCell:function(index,field,value){
			if(field == 'teamPhotoUrl'){
				$('#picture-condition').removeClass('hide');
				
				var imgName = getFileName(value);
				var imgPath = getHostName() + '/team/img/' + imgName;
				
				$('#teamPicture').attr('src',imgPath);
				
				$('#p-cancel').on('click',function(){
					$('#picture-condition').addClass('hide');
					$('#teamPicture').attr('src','');
				});
			}
		}
	});
	
	team.dataInit();
	team.initCombox();
});

var team = {
	dataInit : function(){ // 初始化控件数据
		$('#search-teamName').combobox({
			url : getContextPath() + '/portal/product/init',
			valueField : 'teamName',
			textField : 'teamName',
			filter: function(q, row){
				if(row.teamName == null)
					return false;
				return row.teamName.indexOf(q) >= 0;
			}
		});
		$('#search-phoneNumber').combobox({
			url : getContextPath() + '/portal/product/init',
			valueField : 'phoneNumber',
			textField : 'phoneNumber',
			filter: function(q, row){
				if(row.phoneNumber == null)
					return false;
				return row.phoneNumber.indexOf(q) >= 0;
			}
		});
	},
	initCombox : function(){
		$('#search-business').combo({
			editable:false
		});
		$('#sp').appendTo($('#search-business').combo('panel'));
		list = new Array();
		idList = new Array();
		$('#sp input').click(function(){
			var v = $(this).val();
			var s = $(this).next('span').text();
			// 判断选中状态
			if(this.checked == true){
				// 选中则添加
				idList.push(v);
				list.push(s);
			}else {
				// 取消则删除
				$.each(list,function(i,n){
					if(n == s){
						list.splice(i,1);
						idList.splice(i,1);
					}
				});
			}
			$('#search-business').combo('setValue', idList).combo('setText', list).combo('hidePanel');
		});
	}
}

function addFuc(){ // 注册 增加按钮
	$('#fm').form('clear');
	isadd = true;
	$('#teamProvince').combobox({
		url : getContextPath() + '/portal/get/provinces',
		valueField : 'provinceID',
		textField : 'provinceName',
		onSelect : function(record){
			$('#teamCity').combobox('clear');
			var id = $('#teamProvince').combobox('getValue');
			$('#teamCity').combobox({
				url : getContextPath() + '/portal/get/citys/'+id,
				valueField : 'cityID',
				textField : 'city'
			});
		}
		,onLoadSuccess: function(record){
			var id = $('#teamProvince').combobox('getValue');
			$('#teamCity').combobox({
				url : getContextPath() + '/portal/get/citys/'+id,
				valueField : 'cityID',
				textField : 'city'
			});
		}
	});
	openDialog('dlg');
	formUrl = getContextPath() + '/portal/team/save';
	$('input[name="teamId"]').val(0);
}

function editFuc(){ // 注册 修改 按钮
	var rows = datagrid.datagrid('getSelections');
	isadd = false;
	if(rows.length == 1){
		$('#fm').form('clear');
		originalLoginName = '';
		originalPhoneNumber = '';
		originalLoginName = rows[0].loginName;
		originalPhoneNumber = rows[0].phoneNumber;
		$('#fm').form('load',rows[0]);
		// 数据回显 -- 业务范围
		var business = rows[0].business;
		if(business != null && business != '' && business != undefined){
			var arr = business.split(',');
			for(var i = 0;i < arr.length;i ++){
				// 遍历checkbox
				$('input[name="business"]').each(function(){
					if(this.value == arr[i])
						this.checked = 'checked';
				});
			}
		}
		var first =true;
		$('#teamProvince').combobox({
			url : getContextPath() + '/portal/get/provinces',
			valueField : 'provinceID',
			textField : 'provinceName',
			onSelect : function(record){
				$('#teamCity').combobox('clear');
				var id = $('#teamProvince').combobox('getValue');
				$('#teamCity').combobox({
					url : getContextPath() + '/portal/get/citys/'+id,
					valueField : 'cityID',
					textField : 'city'
				});
			}
			,onLoadSuccess: function(record){
				var id = $('#teamProvince').combobox('getValue');
				$('#teamCity').combobox({
					url : getContextPath() + '/portal/get/citys/'+rows[0].teamProvince,
					valueField : 'cityID',
					textField : 'city',
					onLoadSuccess :function (){
						if(first){
							$('#teamCity').combobox('setValue',rows[0].teamCity);
							first = false;
						}
					}
				});
				$('#teamProvince').combobox('setValue',rows[0].teamProvince);
			}
		});
		openDialog('dlg');
		formUrl = getContextPath() + '/portal/team/update';
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}

function delFuc(){ // 删除
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.message('请选择进行删除操作!');
	} else {
		$.messager.confirm('提示信息' , '确认删除?' , function(r){
			if(r){
				var ids = '';
				for(var i = 0 ; i < arr.length ; i++){
					ids += arr[i].teamId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/team/delete', {ids:ids},function(result){
					
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

function cancelFuc(){ // 注册 取消按钮
	//回滚数据 
	datagrid.datagrid('rejectChanges');
	editing = undefined;
}
$.extend($.fn.validatebox.defaults.rules, {  
    vLoginName : {
        validator : function(value, param) {
        	var url = '/portal/team/static/checkIsExist';
			var isok = false;
        	if(isadd){
        		// 验证登录名
    			syncLoadData(function (res) {
    				isok = res;
    			}, url, $.toJSON({
    				loginName : $('#loginName').val()
    			}));
    			return isok;
        	}else{
        		if(value != originalLoginName){
        			// 验证登录名
        			syncLoadData(function (res) {
        				isok = res;
        			}, url, $.toJSON({
        				loginName : $('#loginName').val()
        			}));
        			return isok;
        		}
        	}
        	return true;
        },
        message : '用户名已经重复！'  
    }  
});
$.extend($.fn.validatebox.defaults.rules, {  
    vPhoneNumber : {
        validator : function(value, param) {
        	var url = '/portal/team/static/checkIsExist';
			var isok = false;
        	if(isadd){
        		// 验证登录名
    			syncLoadData(function (res) {
    				isok = res;
    			}, url, $.toJSON({
    				phoneNumber : $('#phoneNumber').val()
    			}));
    			return isok;
        	}else{
        		if(value != originalPhoneNumber){
        			// 验证手机
        			syncLoadData(function (res) {
        				isok = res;
        			}, url, $.toJSON({
        				loginName : $('#loginName').val()
        			}));
        			return isok;
        		}
        	}
        	return true;
        },
        message : '手机号已经重复！'  
    }  
});
function saveFuc(){ // 注册 保存按钮
	
	progressLoad();
	$('#fm').form('submit',{
		url : formUrl,
		onSubmit : function() {
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
			var obj = $.parseJSON(result);
			$.message(obj.errorMsg);
		}
	});
}

//打开dialog
function openDialog(id){
	modal : true,
	$('#' + id).dialog({
		onOpen : function(event, ui) {
			
		}
	}).dialog('open').dialog('center');
}

// 查询
function searchFun(){
	//清空点击表的排序操作,例如按时间排序等
	$('#gride').datagrid('options').sortName = null;
	$('#gride').datagrid('options').sortOrder = null;
	datagrid.datagrid('load', $.serializeObject($('#searchForm')));
}

// 清除
function cleanFun() {
	$('#searchForm').form('clear');
	list = new Array();
	idList = new Array();
	$('#sp').find('input').attr('checked',false);
	datagrid.datagrid('load', {});
}