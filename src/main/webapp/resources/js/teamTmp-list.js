$().ready(function(){
	var teamTmp_list  = {
		init : function(){
			this.initTable();
		},
		initTable:function(){
			var _this =this;
			datagrid = $('#gride').datagrid({
				url : getContextPath() + '/portal/teamTmp/list',
				idField : 'id' ,
				title : '供应商审核列表' ,
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
								width : 150,
								align : 'center' ,
							},{
								field : 'linkMan',
								title : '联系人',
								width : 80,
								align : 'center' ,
							},{
								field : 'webchat' ,
								title : '微信' ,
								align : 'center' ,
								width : 80,
							},{
								field : 'qq' ,
								title : 'QQ' ,
								align : 'center' ,
								width : 80,
							},{
								field : 'email' ,
								title : '邮箱' ,
								align : 'center' ,
								width : 150,
							},{
								field : 'address' ,
								title : '地址' ,
								align : 'center' ,
								width : 150,
							},{
								field : 'teamProvinceName' ,
								title : '省份' ,
								align : 'center' ,
								width : 80,
							},{
								field : 'teamCityName' ,
								title : '城市' ,
								align : 'center' ,
								width : 80,
							},{
								field : 'priceRange' ,
								title : '价格范围' ,
								align : 'center' ,
								width : 80,
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
								field : 'infoResource' ,
								title : '获知渠道' ,
								align : 'center' ,
								width : 80,
								formatter : function(value , record , index){
									if(value == 0){
										return '<span style=color:red; >友情推荐</span>' ;
									} else if( value == 1){
										return '<span style=color:red; >网络搜索</span>' ; 
									} else if( value == 2){
										return '<span style=color:red; >拍片帮</span>' ;
									} else if( value == 3){
										return '<span style=color:red; >拍片网</span>' ;
									} else if(value == 4){
										return '<span style=color:red; >电销</span>' ;
									}
								}
							},{
								field : 'business' ,
								title : '业务范围' ,
								align : 'center' ,
								width : 80
							},{
								field : 'teamDescription' ,
								title : '公司简介' ,
								align : 'center' ,
								width : 150,
							},{
								field : 'scale' ,
								title : '规模' ,
								align : 'center' ,
								width : 150,
							},{
								field : 'demand' ,
								title : '客户要求' ,
								align : 'center' ,
								width : 100,
							},{
								field : 'createTime' ,
								title : '创建时间' ,
								align : 'center' ,
								width : 100,
							},{
								field : 'checkStatus' ,
								title : '审核状态' ,
								align : 'center' ,
								width : 50,
								formatter : function(value , record , index){
									if(value == 0){
										return '<span style=color:blue; >审核中</span>' ;
									} else if( value == 1){
										return '<span style=color:green; >审核通过</span>' ; 
									} else if( value == 2){
										return '<span style=color:red; >未通过审核</span>' ;
									} 
								}
							},{
								field : 'checkDetails' ,
								title : '审核详情' ,
								align : 'center' ,
								width : 100,
							},{
								field : 'establishDate' ,
								title : '成立时间' ,
								align : 'center' ,
								width : 100,
							},{
								field : 'officialSite' ,
								title : '官网' ,
								align : 'center' ,
								width : 150,
								formatter : function(value , record , index){
									if(value != null && value != undefined && value != ''){
										return '<a href="'+ value +'" target="_blank">'+ value +'</a>' ;
									}
								}
							},{
								field : 'businessDescription' ,
								title : '客户描述' ,
								align : 'center' ,
								width : 150,
							}]] ,
				pagination: true ,
				pageSize : 50,
				pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
				showFooter : false,
				toolbar : '#toolbar',
			});
		}
	}
	teamTmp_list.init();
});

// 修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		$('#fm').form('load',rows[0]);
		loadData(function(data){
			$("#diff_container").empty().html(juicer(teamTmp_tpl.diffList,{list:data}));
		}, getContextPath() + '/portal/teamTmp/find/diff/'+rows[0].teamId,null);
		
		formUrl = getContextPath() + '/portal/teamTmp/update';
		$('input[name="id"]').val(rows[0].id);
		openDialog();
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}

// 确认事件
function save(){
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
			$.message('操作成功!');
		}
	});
}

function openDialog(data){
	$('#dlg').dialog({
		title : '供应商信息审核详情',
		modal : true,
		width : 520,
		height : 500,
		onOpen : function(event, ui) {
		},
	}).dialog('open').dialog('center');
}
var teamTmp_tpl = {
		diffList:[
				'{@each list as item}',
				'<div>',
				'	<h4 style="margin-bottom: 5px !important">${item.propertyName}:</h4>',
				'{@if item.propertyName != "公司Logo"}',
				'	<span>&nbsp;&nbsp;${item.oldValue == ""?"空值":item.oldValue}</span><b>&nbsp;&rarr;&nbsp;</b><span>${item.newValue == ""?"空值":item.newValue}</span>',
				'{@/if}',
				'{@if item.propertyName == "公司Logo"}',
				'&nbsp;&nbsp;<a target="blank" href='+getDfsHostName()+'${item.oldValue == ""?"空值":item.oldValue}>旧公司LOGO</a><b>&nbsp;&rarr;&nbsp;</b><a target="blank" href='+getDfsHostName()+'${item.newValue == ""?"空值":item.newValue}>新公司LOGO</a>',
				'{@/if}',
				'</div>',
				'{@/each}'	 	   
		 ].join("")
}
