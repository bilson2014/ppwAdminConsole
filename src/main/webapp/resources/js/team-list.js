var editing ; //判断用户是否处于编辑状态 
var flag  ;	  //判断新增和修改方法
var formUrl;
var datagrid;
var recommend_datagrid;
var productLineCache;
// 验证
var isadd = false;
var originalLoginName = '';
var originalPhoneNumber = '';

var teamNatureData=[{'id':0,'text':'公司'},{'id':1,text:'工作室'}];

var list = new Array();
var idList = new Array();
var skillList=new Array();
var skillIdList=new Array();
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
						field : 'recommendation',
						title : '审核意见',
						width : 200,
						align : 'center'
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
						field : 'certificateUrl',
						title : '公司营业执照/身份证',
						width : 80,
						align : 'center'
					},{
						field : 'idCardfrontUrl',
						title : '法人手持身份证正面',
						width : 80,
						align : 'center'
					},{
						field : 'idCardbackUrl',
						title : '法人手持身份证背面',
						width : 80,
						align : 'center'
					},{
//						field : 'recommendation',
//						title : '审核意见',
//						width : 80,
//						align : 'center',
//						formatter : function(value , record , index){
//							if(value == 'null'){
//								return '' ;
//							}
//						},
//						hidden : true
//					},{
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
					},{
						field : 'skill',
						title : '业务技能',
						width : 80,
						align : 'center',
						hidden : true
					},{
						field : 'productLine',
						title : '产品线',
						width : 80,
						align : 'center',
						hidden : true
					},{
						field : 'teamNature',
						title : '公司性质',
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
			if(field == 'teamPhotoUrl' ||field == 'certificateUrl' ||field == 'idCardfrontUrl' ||field == 'idCardbackUrl'){
				$('#picture-condition').removeClass('hide');
				$('#teamPicture').attr('src',getDfsHostName() + value);
				
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
		
		$('#search-provinceID').combobox({
			url : getContextPath() + '/portal/get/provinces',
			valueField : 'provinceID',
			textField : 'provinceName',
			onSelect : function(record){
				$('#search-cityID').combobox('reload',getContextPath() + '/portal/get/citys/' + record.provinceID);
			},
			filter : function(q,row) {
				var word = q.trim();
				if(word == '' || word == null || word == undefined) {
					// 输入为空，那么则重新加载市数据
					$('#search-cityID').combobox('reload',getContextPath() + '/portal/all/citys');
					return ;
				} else {
					return row.provinceName.indexOf(word) >= 0;
				}
			},
			onChange : function(n,o) {
				var city = $('#search-cityID').combobox('getValue');
				if(city != '' && city != null) {
					// 清空
					$('#search-cityID').combobox('setValue','');
				}
			}
		});
		
		$('#search-cityID').combobox({
			url : getContextPath() + '/portal/all/citys',
			valueField : 'cityID',
			textField : 'city',
			filter : function(q,row) {
				if(row.city == null)
					return false;
				return row.city.indexOf(q) >= 0;
			}
		});
		
		syncLoadData(function(res) {
			productLineCache = res;
		
			$("#search-productLine").combotree({
			    idField : 'id',
			    treeField : 'text',
				data:productLineCache,
				onBeforeSelect: function(node) {  //只能选择叶子节点
		            if (!$(this).tree('isLeaf', node.target)) {  
		                return false;  
		            }  
		        },  
		        onClick: function(node) {
		        	var snode =node;
		            if (!$(this).tree('isLeaf', node.target)) {  
		                $('#search-productLine').combo('showPanel'); 
		                
		                snode = $('#search-productLine').combotree('tree').tree('getSelected'); 
		            }
		            if(snode!=null){
		            	 $('#search-productLine').combo('setText', snode.parentText+'_'+snode.text);
		            } 
		        }
		        
		 });
		}, getContextPath() + "/portal/config/listTree", null);
		
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
			$('#search-business').combo('setValue', list).combo('setText', list).combo('hidePanel');
		});
		
		//业务技能
		$('#search-skill').combo({
			editable:false
		});
		$('#sp-skill').appendTo($('#search-skill').combo('panel'));
		skillList = new Array();
		skillIdList=new Array();
		$('#sp-skill input').click(function(){
			var v = $(this).val();
			var s = $(this).next('span').text();
			// 判断选中状态
			if(this.checked == true){
				// 选中则添加
				skillList.push(s);
				skillIdList.push(v);
			}else {
				// 取消则删除
				$.each(skillIdList,function(i,n){
					if(n == v){
						skillList.splice(i,1);
						skillIdList.splice(i,1);
					}
				});
			}
			$('#search-skill').combo('setValue', skillIdList).combo('setText', skillList).combo('hidePanel');
		});
	}
}

function addProductLine(lId){
	var module = $(".productLineModule");
	 //添加模板
	var html = createBaseModuleView(lId);
	var newModule = $(html).appendTo(".productLineModule");
	 //渲染easyUI
	 $.parser.parse($(newModule));

	 var box =  $(".productLine").length - 1;
	 $(".productLine:eq("+box+")").combotree({
		    idField : 'id',
		    treeField : 'text',
			data:productLineCache,
			onBeforeSelect: function(node) {  
	            if (!$(this).tree('isLeaf', node.target)) {  
	                return false;  
	            }  
	        },  
	        onClick: function(node) { 
	        	var snode =node;
	            if (!$(this).tree('isLeaf', node.target)) {  
	            	$(".productLine:eq("+box+')').combo('showPanel');
	            	 
		            snode = $(".productLine:eq("+box+')').combotree('tree').tree('getSelected'); 
	            }
	            
	            if(snode!=null){
	            	$(".productLine:eq("+box+')').combo('setText', snode.parentText+'_'+snode.text);
	            }
	        } ,
	        onLoadSuccess: function () { //数据加载完毕事件
				if(lId != null && lId != undefined){
					$(".productLine:eq("+box+')').combotree('setValue', lId);
					
					for(var i=0;i<productLineCache.length;i++){
						var nodes=productLineCache[i].children;
						if(nodes!=null){
							for(var j=0;j<nodes.length;j++){
								var node=nodes[j];
								if(node!=null && node!=undefined && node.id==lId){
									$(".productLine:eq("+box+')').combo('setText', node.parentText+'_'+node.text);
								}
							}
						}
					}
				}		
         	}
	 });
	 delProductLine();
}

function createBaseModuleView(cpmId){
	var $body=['<div class="moduleBlock">',
	'<select id="productLine" name="productLine" style="width: 260px" class=" productLine easyui-combotree" required="true"></select>',
	'<a href="javascript:void(0);" class="easyui-linkbutton productLine-del" data-options="plain:true,iconCls:\'icon-remove\'"></a>',
    '</div>',
    ''].join('');
	return $body;
}

function delProductLine(){
	$(".productLine-del").unbind('click').on("click",function(){
		$(this).parent().remove();
	});
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
	$('#teamNature').combobox({
		valueField : 'id',
		textField : 'text',
		data:teamNatureData	
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
				$('#dlg input[name="business"]').each(function(){
					if(this.value == arr[i])
						this.checked = 'checked';
				});
			}
		}
		//数据回显--业务技能
		var skill = rows[0].skill;
		if(skill != null && skill != '' && skill != undefined){
			var arr = skill.split(',');
			for(var i = 0;i < arr.length;i ++){
				// 遍历checkbox
				$('#dlg input[name="skill"]').each(function(){
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
		//数据回显-公司性质
		$('#teamNature').combobox({
			valueField : 'id',
			textField : 'text',
			data:teamNatureData,
			onLoadSuccess: function(){
				console.log(1);
				$('#teamNature').combobox('setValue',rows[0].teamNature);
			}
		});
		
		openDialog('dlg');
		
		formUrl = getContextPath() + '/portal/team/update';
		
		//数据回显--产品线
		var productLine=rows[0].productLine;
		if(productLine!=null && productLine!='' && productLine!=undefined){
			var arr=productLine.split(',');
			for(var i=0;i<arr.length;i++){
				addProductLine(arr[i]);
			}
		}
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

// 添加用户名重复验证
$.extend($.fn.validatebox.defaults.rules, {  
    vLoginName : {
        validator : function(value, param) {
        	var url = getContextPath() + '/portal/team/isExist';
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

// 添加手机重复验证
$.extend($.fn.validatebox.defaults.rules, {
    vPhoneNumber : {
        validator : function(value, param) {
        	var url = getContextPath() + '/portal/team/isExist';
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
        				phoneNumber : $('#phoneNumber').val()
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
			$(".productLineModule").html('');
		}
	}).dialog('open').dialog('center');
}

// 查询
function searchFun(){
	//清空点击表的排序操作,例如按时间排序等
	$('#gride').datagrid('options').sortName = null;
	$('#gride').datagrid('options').sortOrder = null;
	
	// 验证 省 市 筛选数据
	var pId = $('#search-provinceID').combobox('getValue');
	var cId = $('#search-cityID').combobox('getValue');
	if(pId != '' && pId != null) {
		if(!checkNumber(pId)) {
			alert('请选择省');
			return ;
		}
	}
	
	if(cId != '' && cId != null) {
		if(!checkNumber(cId)) {
			alert('请选择城市');
			return ;
		}
	}
	datagrid.datagrid('clearSelections');
	datagrid.datagrid('load', $.serializeObject($('#searchForm')));
}

// 清除
function cleanFun() {
	$('#searchForm').form('clear');
	list = new Array();
	idList = new Array();
	skillList=new Array();
	skillIdList=new Array();
	$('#sp').find('input').attr('checked',false);
	$('#sp-skill').find('input').attr('checked',false);
	datagrid.datagrid('load', {});
}
function recommendFuc(){
	openDialog('recommend-dlg');
	// 初始化DataGrid
	recommend_datagrid = $('#recommend-gride').datagrid({
		url : getContextPath() + '/portal/team/list',
		idField : 'teamId' ,
		title : '供应商管理列表' , 
		queryParams:{recommend:true},
		fit: true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		//frozenColumns : [[
		//		{field : 'ck' , checkbox:true}
		//]],
		columns:[[
					{
						field : 'teamName',
						title : '公司名称',
						width : 260,
						align : 'center'
					},
					{
						field : 'recommendSort',
						width : 160,
						title : '操作',
						align : 'center',
						formatter : function(value,row,index){
							var all = "";
							var totalCount = $('#recommend-gride').datagrid('getData').total;
							var up  = '<a class="sort" data-target="up" data-id="'+row.teamId+'" href="javascript:void(0)">上移</a>';
							var down = '<a class="sort" data-target="down" data-id="'+row.teamId+'" href="javascript:void(0)">下移</a>';
							var del = '<a class="sort" data-target="del" data-id="'+row.teamId+'" href="javascript:void(0)">移除</a>';
							if(totalCount<=1){
								return del;
							}
							if(value==1 && value!=totalCount){
								return down+" "+del;
							}
							if(value>1 && value<totalCount){
								return up+" "+down+" "+del;
							}
							if(value == totalCount){
								return up+" "+del;
							}
						}
					}
				]] ,
		pagination: true,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		onLoadSuccess:function(){
			sort();//排序操作
			add();//添加操作
		}
	});
	//初始化团队信息
	$('#search-recommend-teamName').combobox({
		url : getContextPath() + '/portal/team/all/norecommend',
		valueField : 'teamId',
		textField : 'teamName',
		filter: function(q, row){
			if(row.teamName == null)
				return false;
			return row.teamName.indexOf(q) >= 0;
		}
	});
}
function getProductsFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		openDialog('product-dlg');
		$('#product-gride').datagrid({  
	        url:getContextPath() + '/portal/product/listByTeam',
	        idField : 'productId',
	        queryParams:{teamId:rows[0].teamId},
	        striped : true ,
			loadMsg : '数据正在加载,请耐心的等待...' ,
			rownumbers : true , 
			pagination: true ,
			pageSize : 20,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			showFooter : false,
	        columns:[[
	        	{
					field : 'productName',
					title : '作品标题',
					width : 150,
					align : 'center' ,
					editor : {
						type : 'validatebox' ,
						options : {
							required : true , 
							missingMessage : '请填写项目名称!'
						}
					}
				},{
					field : 'flag' ,
					title : '审核状态' ,
					align : 'center' ,
					width : 100,
					sortable : true ,
					formatter : function(value , record , index){
						if(value == 0){
							return '<span style=color:blue; >审核中</span>' ;
						} else if( value == 1){
							return '<span style=color:green; >审核通过</span>' ;
						} else if( value == 2){
							return '<span style=color:red; >未通过审核</span>' ;
						}
					},
					editor:{
						type:'combobox' , 
						options:{
							data:[{id:0 , val:'审核中'},{id:1 , val:'审核通过'},{id:2 , val:'未通过审核'}] ,
							valueField:'id' , 
							textField:'val' ,
							required:false , 
							editable : false
						}
					}
				}, {
					field : 'visible' ,
					title : '是否可见 ',
					align : 'center' ,
					width : 60,
					sortable : true ,
					formatter : function(value , record , index){
						if(value == 0){
							return '<span style=color:green; >可见</span>' ;
						} else if( value == 1){
							return '<span style=color:red; >不可见</span>' ; 
						}
					}
				},{
					field : 'tags' ,
					title : '标签' ,
					align : 'center' ,
					width : 152,
					sortable : true ,
					editor : {
						type : 'validatebox' ,
						options : {
							required : false 
						}
					}
				} ,{
					field : 'teamId' ,
					title : '作品链接' ,
					align : 'center' ,
					width : 60,
					sortable : true ,
					formatter : function(value , record , index){
						return "<a href='http://www.apaipian.com/play/"+value+"_"+record.productId+".html' target='_blank'>链接</a>";
					}
				}
	        ]] ,
	        onDblClickCell:function(index,field,value){
				if(field == 'productName'){
					var row = $('#product-gride').datagrid('getData').rows[index];	
					$('#video-condition').removeClass('hide');
					$('#productVideo').removeClass('hide');
					$('#productVideo').show('fast');
					
					var videoPath = getDfsHostName() +  row.videoUrl;
					$('#productVideo').attr('src',videoPath);
					
					$('#v-cancel').on('click',function(){
						$('#video-condition').addClass('hide');
						$('#productVideo').attr('src','');
					});
				}
	        }
	    });  
	}else{
		$.message('只能查看一条记录的产品列表!');
	}
}
function sort(){
	$(".sort").off("click").on("click",function(){
		var action = $(this).attr("data-target");
		var teamId = $(this).attr("data-id");
		//TODO 移除提示
		$.ajax({
			url : getContextPath() + '/portal/team/recommend/sort',
			type : 'POST',
			data : {
				'action' : action,
				'teamId' : teamId,
			},
			success : function(data){
				if(data){
					recommend_datagrid.datagrid('clearSelections');
					recommend_datagrid.datagrid('load', {recommend:true});
					if(action=='del'){//刷新上方选择供应商
						$('#search-recommend-teamName').combobox('clear');
						$('#search-recommend-teamName').combobox('reload');
					}
				}
			}
		});
		
	})
}
function add(){
	$("#add-recommend").off("click").on("click",function(){
		var teamId = $('#search-recommend-teamName').combobox('getValue');
		if(teamId!=''){
			$.ajax({
				url : getContextPath() + '/portal/team/addrecommend',
				type : 'POST',
				data : {
					'teamId' : teamId,
				},
				success : function(data){
					if(data){
						$('#search-recommend-teamName').combobox('clear');
						$('#search-recommend-teamName').combobox('reload');
						recommend_datagrid.datagrid('load', {recommend:true});
					}
				}
			});
		}else{
			$.message('请选择要添加的供应商!');
			return false;
		}
	})
}

//报表导出
function exportFun(){
	$('#searchForm').form('submit',{
		url : getContextPath() + '/portal/team/export',
		onSubmit : function() {
			$.growlUI('报表输出中…', '正在为您输出报表，请稍等。。。');
		},
		success : function(result) {
			
		}
	});
}

/*
 * 验证 数字
 */
function checkNumber(str){
	reg = /^[1-9]+[0-9]*]*$/;
	if(str.match(reg))
		return true;
	else
		return false;
}