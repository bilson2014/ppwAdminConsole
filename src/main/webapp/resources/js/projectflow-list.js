var datagrid;
var sourceCombobox;
var chanpincache;
var chanpinConfig;
var members;
$().ready(function() {
	sourceCombobox=JSON.parse($('#sourceCombobox').val());
	$('#sourceCombobox').remove();
	initData();
	
	
	
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/projectflow/list',
		idField : 'projectId',
		title : '项目列表',
		fitColumns : true,
		striped : true,
		loadMsg : '数据正在加载,请耐心的等待...',
		rownumbers : true,
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		} ] ],
		columns : [ [ {
			field : 'projectId',
			title : '项目ID',
			width : 80,
			align : 'center'
		}, {
			field : 'projectName',
			title : '项目名称',
			width : 100,
			align : 'center'
		}, {
			field : 'projectStatus',
			title : '项目状态',
			width : 40,
			align : 'center',
			formatter : function(value,row,index){
				if(value=='' || value== undefined){
					return '进行中';
				}else if(value=='finished'){
					return '已完成';
				}else if(value=='cancel'){
					return '取消';
				}else if(value=='suspend'){
					return '挂起';
				}
			}
		}, {
			field : 'projectStage',
			title : '项目阶段',
			width : 40,
			align : 'center',
			formatter : function(value,row,index){
				if(value=='1'){
					return '沟通阶段';
				}else if(value=='2'){
					return '方案阶段';
				}else if(value=='3'){
					return '商务阶段';
				}else if(value=='4'){
					return '制作阶段';
				}else if(value=='5'){
					return '交付阶段';
				}
			}
		}, {
			field : 'projectGrade',
			title : '评级',
			width : 40,
			align : 'center',
			formatter:function(value,row,index){
				var grade='';
				if(value=='5'){
					grade= 'S';
				}else if(value=='4'){
					grade= 'A';
				}else if(value=='3'){
					grade= 'B';
				}else if(value=='2'){
					grade= 'C';
				}else if(value=='1'){
					grade= 'D';
				}else if(value=='0'){
					grade= 'E';
				}
				if(row.user!='' && row.user!=undefined){
					grade+= row.user.userLevel;
				}
				return grade;
			}	
		}, {
			field : 'productName',
			title : '产品线',
			align : 'center',
			width : 60
		}, {
			field : 'productConfigLevelName',
			title : '等级',
			align : 'center',
			width : 60
		}, {
			field : 'productConfigLengthName',
			title : '配置',
			align : 'center',
			width : 60,
			formatter:function(value,row,index){
				if(row.productConfigLengthName!=undefined){
					if(row.productConfigAdditionalPackageName!=undefined){
						return row.productConfigLengthName+'+'+row.productConfigAdditionalPackageName;
					}else{
						return row.productConfigLengthName;
					}
				}else{
					return row.productConfigAdditionalPackageName;
				}
			}
		} , {
			field : 'principalName',
			title : '负责人',
			align : 'center',
			sortable : true ,
			width : 40
		}, {
			field : 'scheme',
			title : '策划',
			align : 'center',
			width : 40,
			sortable : true ,
			formatter : function(value,row,index){
				if(row.synergyList!='' && row.synergyList!=undefined){
					var employees=row.synergyList;
					
					for(var i=0;i<employees.length;i++){
						if(employees[i].employeeGroup=='scheme'){
							return employees[i].employeeName;
						}
					}
				}
				return "";
			}
		}, {
			field : 'supervise',
			title : '监制',
			align : 'center',
			width : 40,
			sortable : true ,
			formatter : function(value,row,index){
				if(row.synergyList!='' && row.synergyList!=undefined){
					var employees=row.synergyList;
					
					for(var i=0;i<employees.length;i++){
						if(employees[i].employeeGroup=='supervise'){
							return employees[i].employeeName;
						}
					}
				}
				return "";
			}
		}, {
			field : 'd',
			title : '客户名称',
			align : 'center',
			width : 60,
			formatter : function(value,row,index){
				if(row.user!='' && row.user!=undefined){
					return row.user.userName;
				}
				return "";
			}
		}, {
			field : 'e',
			title : '制作供应商名称',
			align : 'center',
			width : 60,
			formatter : function(value,row,index){
				if(row.teamList!='' && row.teamList!=undefined){
					for(var i=0;i<row.teamList.length;i++){
						if(row.teamList[i].teamType=='produce'){
							return row.teamList[i].teamName;
						}
					}
				}
				return "";
			}
		}, {
			field : 'projectSource',
			title : '项目来源',
			align : 'center',
			width : 60,
			formatter : function(value , record , index){
				for(var i=0;i<sourceCombobox.length;i++){
					if(value==sourceCombobox[i].value){
						return '<span style=color:black; >'+sourceCombobox[i].text+'</span>' ;
					}
				}
			}
		}, {
			field : 'estimatedPrice',
			title : '项目预算',
			align : 'center',
			sortable : true ,
			width : 60
		}, {
			field : 'projectBudget',
			title : '客户预算',
			align : 'center',
			sortable : true ,
			width : 60
		}, {
			field : 'createDate',
			title : '创建时间',
			align : 'center',
			sortable : true ,
			width : 80
		}, {
			field : 'updateDate',
			title : '更新时间',
			align : 'center',
			sortable : true ,
			width : 80
		}, {
			field : 'teamList',
			title : '供应商信息',
			align : 'center',
			width : 60,
			hidden : true
		}, {
			field : 'user',
			title : '客户',
			align : 'center',
			width : 60,
			hidden : true
		}, {
			field : 'synergyList',
			title : '团队信息',
			align : 'center',
			width : 60,
			hidden : true
		}] ],
		pagination : true,
		pageSize : 50,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
		toolbar : '#toolbar'
	});
	
	
	$('#search_projectSource').combobox({
		data : sourceCombobox,
		valueField : 'value',
		textField : 'text'
	});
	//产品线
	$('#search_productId').combobox({
		valueField : 'chanpinId',
		textField : 'chanpinName',
		data:chanpincache,
		onSelect : function(record){
			$('#search_productConfigLevelId').combobox('clear');
			var id = $('#search_productId').combobox('getValue');
			$('#search_productConfigLevelId').combobox({
				url : getContextPath() + '/portal/config/chanpin',
				onBeforeLoad: function (param) {  
                    param.chanpinId = id;  
                },
				valueField : 'chanpinconfigurationId',
				textField : 'chanpinconfigurationName'
			});
		}
	});
	$('#search_productConfigLevelId').combobox({
		data : chanpinConfig,
		valueField : 'chanpinconfigurationId',
		textField : 'chanpinconfigurationName'
	});
	
});

function initData(){
	syncLoadData(function(res) {
		chanpincache=[];
		//全部
		var all={chanpinName:'-----全部----'}
		chanpincache.push(all);
		//添加-非标准化产品 
		var noChanpin={chanpinName:'非标准化产品',chanpinId:'0'};
		chanpincache.push(noChanpin);
		for(var item in res.rows)
        { 
			chanpincache.push(res.rows[item]);
        }
		
	}, getContextPath() + "/portal/chanpin/list", null);
	syncLoadData(function(res) {
		chanpinConfig = res.rows;
	}, getContextPath() + "/portal/config/list", null);
	syncLoadData(function(res) {
		members = res;
	}, getContextPath() + "/portal/project/member-pull", null);
}

// 供应商信息
function teamDetail() {
	var rows = datagrid.datagrid('getSelections');
	var data=null;
	if(rows.length == 1){
		data=rows[0];
	} else {
		$.message('只能选择一条记录进行查看!');
		return ;
	}
	
	$('#teamf').form('clear');
	$('#teamTabel tbody').html("");//.empty();
	
	$('#teamDlg').dialog({
		modal : true,
		onOpen : function(event, ui) {
		}
	}).dialog('open').dialog('center');
	
	var tab=document.getElementById("teamTabel");
    //添加行
	var teams=rows[0].teamList;
	var productFlag=true;
	var schemeFlag=true;
	for(var i=0;i<teams.length;i++){
		if(teams[i].teamType=='produce' && productFlag){
			tab.insertRow(tab.rows.length).innerHTML='<tr><th colspan="4">制作供应商</th></tr>';
			productFlag=false;
		}else if(teams[i].teamType=='scheme' && schemeFlag){
			tab.insertRow(tab.rows.length).innerHTML='<tr><th colspan="4">策划供应商</th></tr>';
			schemeFlag=false;
		}
		
		tab.insertRow(tab.rows.length).innerHTML='<tr>'+
			'<th>名称</th>'+
			'<td><input name="teamName" id="'+teams[i].teamType+'_teamName_'+i+'" class="easyui-textbox" value="'+teams[i].teamName+'"></td>'+
			'<th>联系人</th>'+
			'<td><input name="linkman" id="'+teams[i].teamType+'_linkman_'+i+'" class="easyui-textbox" value="'+teams[i].linkman+'"></td>'+
			'<hr/>'+
		'</tr>';
		tab.insertRow(tab.rows.length).innerHTML='<tr>'+
			'<th>电话</th>'+
			'<td><input name="telephone" id="'+teams[i].teamType+'_telephone_'+i+'" class="easyui-textbox" value="'+teams[i].telephone+'"></td>'+
			'<th>金额</th>'+
			'<td><input name="actualPrice" id="'+teams[i].teamType+'_actualPrice_'+i+'" class="easyui-textbox" value="'+teams[i].actualPrice+'"></td>'+
			'<hr/>'+
		'</tr>';
		if(teams[i].teamType=='produce'){
			tab.insertRow(tab.rows.length).innerHTML='<tr>'+
			'<th>状态</th>'+
			'<td><select id="'+teams[i].teamType+'_flag_'+i+'" name="flag" style="width: 155px;"  class="easyui-combobox" disabled="disabled" value="'+teams[i].flag+'">'+
				'<option value="0">正常</option>'+
				'<option value="1">已删除</option>'+
			'</select></td></td>'+
			'</tr>';
			tab.insertRow(tab.rows.length).innerHTML='<tr><td colspan="4"><hr/></td></tr>';
		}
		
	}
	
	if(teams.length==0){
		tab.insertRow(tab.rows.length).innerHTML='<tr><th colspan="4">未分配供应商</th></tr>';
	}
    
	$('#teamf').find("input").each(function () {
        $(this).attr("disabled",true)
    });
	$.parser.parse($(tab));
}
//客户信息
function userDetail() {
	var rows = datagrid.datagrid('getSelections');
	var data=null;
	if(rows.length == 1){
		data=rows[0];
	} else {
		$.message('只能选择一条记录进行查看!');
		return ;
	}
	
	$('#userf').form('clear');
	$('#userf').form('load',rows[0].user);

	$('#userDlg').dialog({
		modal : true,
		onOpen : function(event, ui) {
			$('#userDlg').find("input").each(function () {
                $(this).attr("disabled",true)
            });
		}
	}).dialog('open').dialog('center');
}
//团队信息
function employeeDetail() {
	var rows = datagrid.datagrid('getSelections');
	var data=null;
	if(rows.length == 1){
		data=rows[0];
	} else {
		$.message('只能选择一条记录进行查看!');
		return ;
	}
	$('#employeef').form('clear');
	$('#employeef').form('load',rows[0]);
	
	var synergies=rows[0].synergyList;
	
	for(var key in members){
//		var dis=true;
		$("#"+key).combobox({
			data : members[key],
			valueField : 'id',
			textField : 'first',
			onLoadSuccess : function(record) {			
				for(var i=0;i<synergies.length;i++){
					if(key==synergies[i].employeeGroup){
//						dis=false;
						$("#"+key).combobox('setValue',
								"employee_"+synergies[i]['employeeId']);
						break;
					}
				}
			}
		});
		//只能修改不能设置
//		if(dis){
//			$("#"+key).combobox({disabled:true});
//		}
	}

//	$("#sale").combobox({
//		data : members.sale,
//		valueField : 'id',
//		textField : 'first',
//		onLoadSuccess : function(record) {
//			$("#sale").combobox('setValue',"employee_"+rows[0].principal);
//		}
//	});

	$('#employeeDlg').dialog({
		modal : true,
		onOpen : function(event, ui) {
			
		}
	}).dialog('open').dialog('center');
}
//项目信息
function projectDetail() {
	var rows = datagrid.datagrid('getSelections');
	if(rows.length != 1){
		$.message('只能选择一条记录进行查看!');
		return ;
	}
	
	$('#projectf').form('clear');
	$('#projectSource').combobox({
		data : sourceCombobox,
		valueField : 'value',
		textField : 'text',
		onLoadSuccess : function(record) {
			$('#projectSource').combobox('setValue',
					rows[0].projectSource);
		}
	});
	$('#projectf').form('load',rows[0]);

	$('#projectDlg').dialog({
		modal : true,
		onOpen : function(event, ui) {	
			$('#projectDlg').find("input").each(function () {
                $(this).attr("disabled",true)
            });
			$("#projectSource").combobox({disabled:true});
		}
	}).dialog('open').dialog('center');
}
//文件列表
function fileDetail(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length != 1){
		$.message('只能选择一条记录进行查看!');
		return ;
	}
	$('#fileDlg').dialog({
		modal : true,
		onOpen : function(event, ui) {		
		}
	}).dialog('open').dialog('center');
	
	var filegrid=$('#file-gride').datagrid({  
        url:getContextPath() + '/portal/project-resource/list',
        idField : 'projectResourceId',
        queryParams:{projectId:rows[0].projectId},
        striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true , 
		pagination: false ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
        columns:[[
        	{
				field : 'resourceType',
				title : '文件类型',
				width : 70,
				align : 'center',
				formatter : function(value , record , index){
					if(value == 'projectBrief'){
						return '项目简报' ;
					} else if( value == 'projectPlan'){
						return '项目排期' ;
					} else if( value == 'planning'){
						return '策划方案' ;
					} else if( value == 'ppm'){
						return 'PPM' ;
					} else if( value == 'demoUpdate'){
						return '影片修改表' ;
					} else if( value == 'customerReply'){
						return '客户验收函回复截图' ;
					} else if( value == 'priceSheet'){
						return '报价单' ;
					} else if( value == 'watermarkFile'){
						return '水印样片' ;
					} else if( value == 'shootingScript'){
						return '分镜头脚本' ;
					}
				}
			},{
				field : 'resourceName' ,
				title : '名称' ,
				align : 'center' ,
				width : 260,
				sortable : true 
			}, {
				field : 'uploaderName' ,
				title : '上传人 ',
				align : 'center' ,
				width : 60,
				sortable : true 
			},{
				field : 'createDate' ,
				title : '上传时间' ,
				align : 'center' ,
				width : 140,
				sortable : true 
			} ,{
				field : 'flag' ,
				title : '最终版' ,
				align : 'center' ,
				width : 50,
				sortable : true,
				formatter : function(value , record , index){
					if(value==1){
						return "<input type='radio' checked>";
					}
				}
			},{
				field : 'resourcePath' ,
				title : '操作' ,
				align : 'center' ,
				width : 50,
				sortable : true,
				formatter : function(value , record , index){
					if(value!=undefined && value!='' ){
						return "<a href='/portal/project/getDFSFile/"+record.projectResourceId+"'>下载</a>";
					}
				}
			}
        ]] 
        ,
        onLoadSuccess:function(){
        	var datas=filegrid.datagrid("getRows");
        	var RowCount = datas.length;
        	var begin=0;
        	var span=1;
        	for(var i=0;i<RowCount;i++){
        		if(i!=RowCount-1 && datas[i].resourceType==datas[i+1].resourceType){
        			span++;
        		}else{
        			if(span>1){
        				$('#file-gride').datagrid('mergeCells',{
            				index:begin,
            				field:'resourceType',
            				rowspan:span
            			});
        			}
        			begin=i+1;
        			span=1;
        		}
        	}
        

        }
        
    });  
}
//项目日志
function logDetail(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length != 1){
		$.message('只能选择一条记录进行查看!');
		return ;
	}
	$('#logDlg').dialog({
		modal : true,
		onOpen : function(event, ui) {		
		}
	}).dialog('open').dialog('center');
	
	var loggrid=$('#log-gride').datagrid({  
        url:getContextPath() + '/portal/project-message/list',
        idField : 'projectMessageId',
        queryParams:{projectId:rows[0].projectId},
        striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true , 
		pagination: true ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
        columns:[[
        	{
				field : 'createDate',
				title : '操作时间',
				width : 150,
				align : 'center'
			},{
				field : 'fromName' ,
				title : '操作人' ,
				align : 'center' ,
				width : 100,
				sortable : true 
			}, {
				field : 'content' ,
				title : '事件 ',
				align : 'left' ,
				width : 360,
				sortable : true 
			}
        ]]    
    });  
}
//财务信息
function financeDetail() {
	var rows = datagrid.datagrid('getSelections');
	if(rows.length != 1){
		$.message('只能选择一条记录进行查看!');
		return ;
	}
	
	$('#financef').form('clear');
	//var html=" <table style='width: 98%;'><tr><th>付给供应商金额</th><th>供应商制作内容</th></tr>";
	var data=[];
	syncLoadData(function(res) {
		$('#financef').form('load',res);
		var finances=res.providerFinances;
		if(finances!=null && finances!='' && finances!=undefined){
			for(var i=0;i<finances.length;i++){
				var finance={};
				for(var j=0;j<rows[0].teamList.length;j++){
					if(rows[0].teamList[j].teamType=='produce' && rows[0].teamList[j].projectTeamId==finances[i].userId){
						finance.makeContent=rows[0].teamList[j].makeContent;
						finance.invoiceHead=rows[0].teamList[j].invoiceHead;
						break;
					}
				}
				
				finance.payPrice=finances[i].payPrice;
				finance.projectTeamId=finances[i].userId;
				finance.userName=finances[i].userName;
				finance.payTime=finances[i].payTime;
				data.push(finance);
			}
		}
		
	}, getContextPath() + "/portal/project-finance/"+rows[0].projectId,null);

	
	var loggrid=$('#finalce-gride').datagrid({  
        idField : 'projectTeamId',
        data:data,
        striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true , 
		pagination: false ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : false,
        columns:[[
        	{
				field : 'userName' ,
				title : '供应商名称' ,
				align : 'center' ,
				width : 80
			},{
				field : 'makeContent' ,
				title : '供应商制作内容' ,
				align : 'center' ,
				width : 100
			},
        	{
				field : 'payPrice',
				title : '付给供应商金额',
				width : 100,
				align : 'center'
			},{
				field : 'invoiceHead' ,
				title : '发票抬头' ,
				align : 'center' ,
				width : 100
			},{
				field : 'payTime' ,
				title : '付款时间' ,
				align : 'center' ,
				width : 100
			}
        ]]    
    });  
	

	$('#financeDlg').dialog({
		modal : true,
		onOpen : function(event, ui) {		
			$('#financeDlg').find("input").each(function () {
                $(this).attr("disabled",true)
            });
		}
	}).dialog('open').dialog('center');
}

//导出
function exportInfo(){
	$('#searchForm').form('submit',{
		url : getContextPath() + '/portal/project/export',
		onSubmit : function() {
			$.growlUI('报表输出中…', '正在为您输出报表，请稍等。。。');
		},
		success : function(result) {
			
		}
	});
}

function delFuc(){
	var rows = datagrid.datagrid('getSelections');
	
	if(rows.length <= 0 ){
		$.message('请选择进行删除操作!');
	} else {
		$.messager.confirm('提示信息' , '确认删除?' , function(r){
			if(r){
				var ids = '';
				for(var i = 0 ; i < rows.length ; i++){
					ids += rows[i].projectId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/projectflow/delete', {projectIds:ids},function(result){
					if(result.result){
						// 刷新数据
						datagrid.datagrid('clearSelections');
						datagrid.datagrid('reload');
						$.message('操作成功!');
					}else{
						$.message(result.err);
					}
					
				});
			} else {
				 return ;
			}
		});
	}
}


function searchFun(){
	datagrid.datagrid('load', $.serializeObject($('#searchForm')));
	datagrid.datagrid('clearSelections');
}
function cleanFun(){
	$('#searchForm').form('clear');
	datagrid.datagrid('load', {});
	datagrid.datagrid('clearSelections');
}
function updateEmployeeFuc(){
	progressLoad();
	$('#employeef').form('submit',{
		url : getContextPath() + '/portal/project-synergy/update',
		onSubmit : function() {
			
		},
		success : function(result) {
			var obj = $.parseJSON(result);
			if(obj.errorCode==200){
				$('#employeeDlg').dialog('close');
				datagrid.datagrid('clearSelections');
				datagrid.datagrid('reload');
			}
			
			progressClose();
			$.message(obj.errorMsg);
		}
	});
}
