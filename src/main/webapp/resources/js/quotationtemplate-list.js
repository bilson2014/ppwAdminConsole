var flag  ;	  //判断新增和修改方法
var datagrid;
var itemgrid;
var formUrl;
var quotationTypeCache;
var productLineCache;
var lastIndex;//编辑行

//验证
var isadd = false;

$().ready(function(){
	
	syncLoadData(function(res) {
		productLineCache=res;
	}, getContextPath() + "/portal/config/listTree", null);
	
	syncLoadData(function(res) {
		quotationTypeCache=res;
	}, getContextPath() + "/portal/quotationtype/list", null);

	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/quotationtemplate/list',
		idField : 'templateId' ,
		title : '报价单模板列表' , 
		// fitColumns : true ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		frozenColumns : [[
				{field : 'ck' , checkbox:true}
		]],
		columns:[[
					{
						field : 'templateName',
						title : '模板名称',
						width : 250,
						align : 'center'
					},{
						field : 'type',
						title : '模板类型',
						width : 150,
						align : 'center' ,
						formatter : function(value , record , index){
							if(value == 0){
								return '个人' ;
							} else if( value == 1){
								return '产品线' ; 
							}
						}
					},{
						field : 'chanpinId',
						title : '产品线',
						width : 150,
						align : 'center' ,
						formatter : function(value , record , index){
							if(productLineCache!=null){
								for(var i=0;i<productLineCache.length;i++){
									if(productLineCache[i].children!=null){
										for(var j=0;j<productLineCache[i].children.length;j++){
											if(record.chanpinconfigId==productLineCache[i].children[j].id){
												return productLineCache[i].children[j].parentText;
											}
										}
									}
								}
							}
							
						}
					},{
						field : 'chanpinconfigId',
						title : '配置',
						width : 150,
						align : 'center' ,
						formatter : function(value , record , index){
							if(productLineCache!=null){
								for(var i=0;i<productLineCache.length;i++){
									if(productLineCache[i].children!=null){
										for(var j=0;j<productLineCache[i].children.length;j++){
											if(value==productLineCache[i].children[j].id){
												return productLineCache[i].children[j].text;
											}
										}
									}
									
								}
							}
							
						}
					},{
						field : 'total',
						title : '最终价格',
						width : 150,
						align : 'center' 
					},{
						field : 'updateDate',
						title : '维护时间',
						width : 150,
						align : 'center' 
					}
					]],
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
	$('#fm-1').form('clear');
	$('#fm-2').form('clear');
	openDialog(null);
	initCombobox();
	initItem(null);
	isadd = true;
	$('#saveBtn').show();
	
	formUrl = getContextPath() + '/portal/quotationtemplate/save';
}

//修改
function editFuc(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		isadd = false;
		$('#fm').form('clear');
		$('#fm').form('load',rows[0]);
		$('#fm-1').form('clear');
		$('#fm-2').form('clear');
		$('#fm-2').form('load',rows[0]);
		openDialog('dlg',rows[0]);
		initCombobox();
		initItem(rows[0]);
		if(rows[0].chanpinconfigId!=null){
			$('#chanpinconfigId').combotree('setValue', rows[0].chanpinconfigId); 
		}
		if(rows[0].type==0){
			$('#saveBtn').hide();
		}else{
			$('#saveBtn').show();
		}
		
		formUrl = getContextPath() + '/portal/quotationtemplate/update';
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
					ids += arr[i].templateId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/quotationtemplate/delete', {ids:ids},function(result){
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
	//校验
	var templateName=$('#templateName').val();
	var chanpinconfigId=$('#chanpinconfigId').combotree('getValue');
	var taxRate= $('#taxRate').val();
	var discount=$('#discount').val();
	var total= $('#total').val();
	var subTotal = itemgrid.datagrid('getFooterRows')[0].sum;
	var items=itemgrid.datagrid("getRows");
	
	$.each(items,function(index,item){
        if(item.days=="整包"){
        	item.days="-1";
        } 
        if(item.quantity=="整包"){
        	item.quantity="-1";
        }
	});
	
	progressLoad();
	syncLoadData(function(res) {
		progressClose();
		if(res.result){
			$('#dlg').dialog('close');
			datagrid.datagrid('clearSelections');
			datagrid.datagrid('reload');
			$.message("保存成功");
		}else{
			$.message(res.err);
		}
	}, formUrl, $.toJSON({
		items : items,
		templateId : $('#templateId').val(),
		templateName : templateName,
		chanpinconfigId:chanpinconfigId,
		taxRate: taxRate,
		discount:discount,
		subTotal:subTotal,
        total: total	
	}));
}

// 取消
function cancelFuc(){
	//回滚数据 
	datagrid.datagrid('rejectChanges');
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

//添加报价单项
function add(){
	var type = $('#typeId').combobox('getValue');

	var detail = $('#itemId').combotree('tree').tree('getSelected');	
	
	//校验
	if(type==null || type==undefined){
		$.message('请选择收费类');
		return;
	}
	if(detail==null || detail==undefined){
		$.message('请选择收费项');
		return;
	}
	//是否添加过
	
	//value
	var row=new Object();
	row.quantity=$("#quantity").val();
	row.days=$("#days").val();
	
	for(var i=0;i<quotationTypeCache.length;i++){
		if(detail.id==quotationTypeCache[i].typeId){
			row.detailId=quotationTypeCache[i].typeId;
			row.detailName=quotationTypeCache[i].typeName;
			row.description=quotationTypeCache[i].description;
			row.unitPrice=quotationTypeCache[i].unitPrice;
			row.fullJob=quotationTypeCache[i].fullJob;
			//整包
			if(quotationTypeCache[i].fullJob==1){
				row.quantity="整包";
				row.days="整包";
			}
		}
		if(type==quotationTypeCache[i].typeId){
			row.typeId=quotationTypeCache[i].typeId;
			row.typeName=quotationTypeCache[i].typeName;
		}
		if(detail.pid==quotationTypeCache[i].typeId){
			row.itemId=quotationTypeCache[i].typeId;
			row.itemName=quotationTypeCache[i].typeName;
		}
	}
	//index-type或item（优先）相同的下面
	var rows = itemgrid.datagrid("getRows");
	var newIndex=rows.length;
	for(var i=newIndex-1;i>=0;i--){
		if(rows[i].typeId==row.typeId){
			newIndex=i+1;
			if(rows[i].itemId==row.itemId){
				break;
			}	
		}
	}
	
	itemgrid.datagrid('insertRow',{  
        index:newIndex,  
        row:row
    }); 
	
	computeRowSum(newIndex);
	itemgrid.datagrid('refreshRow', newIndex);
	mergeItemGrid();
	itemgrid.datagrid('statistics');
	computeTotal();
}

//打开dialog
function openDialog(data){
	$('#dlg').dialog({
		modal : true,
		onOpen : function(event, ui) {
			$(".fullJob").hide();
		},
	}).dialog('open').dialog('center');
}

function initCombobox(){
	$("#chanpinconfigId").combotree({
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
	        	$('#chanpinconfigId').combo('showPanel'); 
	        	snode = $('#chanpinconfigId').combotree('tree').tree('getSelected'); 
	        }
	        if(snode!=null){
	        	$('#chanpinconfigId').combo('setText', snode.parentText+'_'+snode.text);
	        	
	        	if($("#templateName").val()==""){
	        		$("#templateName").textbox("setValue", snode.parentText+'_'+snode.text);
	        	}
	        	
	        } 
	    }       
	 });
	
	$('#typeId').combobox({
		url : getContextPath() + '/portal/quotationtype/select',
		valueField : 'id',
		textField : 'text',
		onSelect : function(record){
			$('#itemId').combotree('clear');
			initItemIdSelect();
			$("#days").textbox("setValue", "");
			$("#quantity").textbox("setValue", "");
		}
		,onLoadSuccess: function(record){
			initItemIdSelect();
		}
	});
}

function initItemIdSelect(){
	var id = $('#typeId').combobox('getValue');
	if(id!=undefined && id!=null && id!=""){
		$('#itemId').combotree({
			url : getContextPath() + '/portal/quotationtype/select?typeId='+id,
			idField : 'id',
		    treeField : 'text',
		    parentField:'pid',
		    onClick: function(node) {
		    	for(var i=0;i<quotationTypeCache.length;i++){
		    		if(node.id==quotationTypeCache[i].typeId){
		    			$('#unitPrice').html(quotationTypeCache[i].unitPrice);
		    			$('#description').html(quotationTypeCache[i].description);
		    			if(quotationTypeCache[i].fullJob==1){
		    				$(".fullJob").show();
		    				$("#days").numberbox("disable",true);
		    				$("#quantity").numberbox("disable",true);
		    			}else{
		    				$(".fullJob").hide();
		    				$("#days").numberbox("enable",true);
		    				$("#quantity").numberbox("enable",true);
		    			}
		    			$("#days").numberbox("setValue", "");
		    			$("#quantity").numberbox("setValue", "");
		    			break;
		    		}
		    	}
		    }
		});
	}
}



function initItem(data){
	var templateId;
	if(data==null){
		templateId=null;
	}else{
		templateId=data.templateId;
	}
	
	itemgrid=$('#item-gride').datagrid({  
        url:getContextPath() + '/portal/quotationtemplate/get',
        idField : 'detailId',
        queryParams:{templateId:templateId},
        striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true , 
		pagination: false ,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		showFooter : true,
        columns:[[
        	{
				field : 'typeId',
				title : '类别id',
				width : 10,
				align : 'center',
				hidden : true
			},
        	{
				field : 'typeName',
				title : '类别名称',
				width : 110,
				align : 'center'
			},{
				field : 'itemId' ,
				title : '项目id' ,
				align : 'center' ,
				width : 10,
				hidden : true
			}, {
				field : 'itemName' ,
				title : '项目名称 ',
				align : 'center' ,
				width : 110
			}, {
				field : 'detailName' ,
				title : '明细名称 ',
				align : 'center' ,
				width : 110
			}, {
				field : 'description' ,
				title : '描述 ',
				align : 'center' ,
				width : 160
			}, {
				field : 'unitPrice' ,
				title : '单价 ',
				align : 'center' ,
				width : 100,
				editor: {  
	                type: 'numberbox',  
	                options: {  
	                    precision: 2,
	                    min:0,
						required:true , 
	                    missingMessage:'单价必填!'
	                }  
	            }
			}, {
				field : 'quantity' ,
				title : '数量 ',
				align : 'center' ,
				width : 50,
				formatter:function(value,row,index){
					if(value=="-1" || value==-1){
						return "整包";
					}
					return value;
				}
			}, {
				field : 'days' ,
				title : '天数',
				align : 'center' ,
				width : 50,
				formatter:function(value,row,index){
					if(value=="-1" || value==-1){
						return "整包";
					}
					return value;
				}
			}, {
				field : 'sum' ,
				title : '总价',
				align : 'center' ,
				width : 100
			},{field:'action',title:'操作',width:80,align:'center',
                formatter:function(value,row,index){
                	if(!row.footer){
                		if (row.editing){
                            var s = '<a href="javascript:void(0)" onclick="saverow(this)">Save</a> ';
                            var c = '<a href="javascript:void(0)" onclick="cancelrow(this)">Cancel</a>';
                            return s+c;
                        } else {
                            var e = '<a href="javascript:void(0)" onclick="editrow(this)">Edit</a> ';
                            var d = '<a href="javascript:void(0)" onclick="deleterow(this)">Delete</a>';
                            return e+d;
                        }
                	}
                }
            }
        ]]  ,
        onBeforeEdit:function(index,row){
            row.editing = true;
            itemgrid.datagrid('refreshRow', index);
            mergeItemGrid();
        },
        onAfterEdit:function(index,row){
            row.editing = false;
            itemgrid.datagrid('refreshRow', index);
            mergeItemGrid();
        },
        onCancelEdit:function(index,row){
            row.editing = false;
            itemgrid.datagrid('refreshRow', index);
            mergeItemGrid();
        },
        onLoadSuccess:function(){
        	//合并-类别、名称
        	mergeItemGrid();
        	
        	itemgrid.datagrid('statistics'); //合计
        }
    });  
	
}

function getRowIndex(target){
    var tr = $(target).closest('tr.datagrid-row');
    return parseInt(tr.attr('datagrid-row-index'));
}
function editrow(target) {
	var rowIndex = getRowIndex(target);

	if (lastIndex != rowIndex) {
		itemgrid.datagrid('endEdit', lastIndex);
	}

	var row = itemgrid.datagrid('getRows')[rowIndex];
	if (row.fullJob == 0) {
		// 数量、天数添加编辑框
		itemgrid.datagrid('addEditor', [ // 添加cardNo列editor
		{
			field : 'quantity',
			editor : {
				type : 'numberbox',
				options : {
					precision : 0,
					min:1,
					required:true , 
                    missingMessage:'数量必填!'
				}
			}
		}, {
			field : 'days',
			editor : {
				type : 'numberbox',
				options : {
					precision : 0,
					min:1,
					required:true , 
                    missingMessage:'天数必填!'
				}
			}
		} ]);
	}else{
		itemgrid.datagrid('removeEditor',['quantity','days']);
	}

	itemgrid.datagrid('beginEdit', rowIndex);
	lastIndex = rowIndex;
}

function deleterow(target){
    $.messager.confirm('删除','确认删除?',function(r){
        if (r){
        	itemgrid.datagrid('deleteRow', getRowIndex(target));
        	mergeItemGrid();
        	itemgrid.datagrid('statistics'); //合计
        	//计算总价格
        	computeTotal();
        }
    });
}
function saverow(target){
	//计算总价
	var rowIndex=getRowIndex(target);
	computeRowSum(rowIndex)
	itemgrid.datagrid('endEdit', rowIndex);
	itemgrid.datagrid('statistics'); //合计
	//计算总价格
	computeTotal();
}
function cancelrow(target){
	itemgrid.datagrid('cancelEdit', getRowIndex(target));
}
//计算一行合计
function computeRowSum(rowIndex){
	var row=itemgrid.datagrid('getRows')[rowIndex];
	var sum=Number(row.unitPrice);
	if(row.fullJob==0){
		sum=Number(row.unitPrice)*Number(row.quantity)*Number(row.days);
	}
	
	row.sum = sum;  
}
//计算最终价格
function computeTotal(){
	if(undefined==itemgrid || null==itemgrid){
		return ;
	}
	var sum = itemgrid.datagrid('getFooterRows')[0].sum;
	var tax=Number($('#taxRate').val())/100;
	var total=(1+tax)*sum-Number($('#discount').val());
	
	$("#total").textbox("setValue", total.toFixed(2));
}
//合并单元格
function mergeItemGrid(){
	var datas=itemgrid.datagrid("getRows");
	var RowCount = datas.length;
	var begin=0;
	var span=1;
	
	var itemBegin=0;
	var itemSpan=1;
	
	for(var i=0;i<RowCount;i++){
		if(i!=RowCount-1 && datas[i].typeName==datas[i+1].typeName){
			span++;
		}else{
			if(span>1){
				itemgrid.datagrid('mergeCells',{
    				index:begin,
    				field:'typeName',
    				rowspan:span
    			});
			}
			begin=i+1;
			span=1;
		}
		
		if(i!=RowCount-1 && datas[i].itemName==datas[i+1].itemName){
			itemSpan++;
		}else{
			if(itemSpan>1){
				itemgrid.datagrid('mergeCells',{
    				index:itemBegin,
    				field:'itemName',
    				rowspan:itemSpan
    			});
			}
			itemBegin=i+1;
			itemSpan=1;
		}
	}
}
//合计footer
$.extend($.fn.datagrid.methods, {
    statistics: function(jq) {
    	var rows = $(jq).datagrid("getRows");
    	var footerArray=[];
    	var footerObj=new Object();
    	
    	footerObj.sum=sum();
    	footerObj.typeName="不含税价格：";
    	footerObj.footer=true;
    	
    	footerArray.push(footerObj);
    	$(jq).datagrid('reloadFooter', footerArray);
 
        function sum() {
            var sumNum = 0;
            var str = "";
            for (var i = 0; i < rows.length; i++) {
                var num = rows[i].sum;
                sumNum += Number(num);
            }
            return sumNum.toFixed(2);
        }
    },
    addEditor : function(jq, param) { 
    	if (param instanceof Array) { 
    		$.each(param, function(index, item) { 
    			var e = $(jq).datagrid('getColumnOption', item.field); 
    			e.editor = item.editor; 
    		}); 
    	} else { 
    		var e = $(jq).datagrid('getColumnOption', param.field); 
    		e.editor = param.editor; 
    	} 
	}, 
	removeEditor : function(jq, param) { 
		if (param instanceof Array) { 
			$.each(param, function(index, item) { 
				var e = $(jq).datagrid('getColumnOption', item); 
				e.editor = {}; 
			}); 
		} else { 
			var e = $(jq).datagrid('getColumnOption', param);
			e.editor = {}; 
		} 
	}
});

