var treegrid;
var formUrl;

var imgNo=1; 
var storage_node;
var min=0,max=1;
var imgRatio=248/140;
var displayWidth=124;
var displayHeight=70;

$().ready(
		
				function() {
					storage_node=$('#storage_node').val();
					initCutPhoto('uploadDiv');
					
					$('#grade').combobox(
						{
							onSelect : function(record) {
								$('#parentId').combotree('clear');
								$('#parentId').combotree('reload',
												getContextPath()+ '/portal/quotationtype/selectlist/'+ record.value);
						}
					});

					treegrid = $("#treeGride").treegrid({
						url : getContextPath() + '/portal/quotationtype/list',
						idField : 'typeId',
						treeField : 'typeName',
						parentField : 'parentId',
						fit : true,
						fitColumns : false,
						border : false,
						onLoadSuccess : function(data) {
							// treegrid.treegrid('collapseAll');
						},
						columns : [ [ {
							field : 'typeName',
							title : '名称',
							width : 300
						}, {
							field : 'unitPrice',
							title : '单价',
							width : 100
						}, {
							field : 'costPrice',
							title : '成本价',
							width : 100
						}, {
							field : 'description',
							title : '描述',
							width : 400
						},{
							field : 'fullJob',
							title : '是否整包',
							width : 80,
							formatter : function(value , record , index){
								if(value == 0){
									return '' ;
								} else if( value == 1){
									return '整包' ; 
								}
							}
						},{
							field : 'status',
							title : '是否禁用',
							width : 80,
							formatter : function(value , record , index){
								if(value == 0){
									return '禁用' ;
								} else if( value == 1){
									return '' ; 
								}
							}
						} , {
							field : 'updateDate',
							title : '维护时间',
							width : 200
						}, {
							field : 'grade',
							title : '类型级别',
							width : 200,
							hidden : true
						}] ],
						toolbar : '#toolbar',
					});
				});

// 添加页
function addFun() {
	imgNo=1;
	//document.getElementById('displayFileImg').setAttribute('src',"/resources/img/portal/user/default.png");
	var node = treegrid.treegrid('getSelected');
	$('#fm').form('clear');
	$("#imgDisplay").empty();
	
	if (node && node.grade<3) {
		//如果选择非叶子类型，默认父类型为选中类型
		openDialog('dlg', node.grade+1);
		$('#parentId').combotree('setValue',node.typeId);
		$('#grade').combobox('setValue',node.grade+1);
	}else{
		openDialog('dlg', null);
	}
	
	formUrl = getContextPath() + '/portal/quotationtype/save';
}
// 修改页
function editFun() {
	imgNo=1;
	var node = treegrid.treegrid('getSelected');
	if (node) {
		openDialog('dlg', node.grade);
		$('#fm').form('clear');
		$("#imgDisplay").empty();
		$('#fm').form('load', node);
		if(node.parentId==null || node.parentId==undefined || node.parentId==""){
			$('#parentId').combotree("clear");
		}
		if(node.photo==undefined || node.photo=='' || node.photo==null){
//			document.getElementById('displayFileImg').setAttribute('src',"/resources/img/portal/user/default.png");
		}else{
//			document.getElementById('displayFileImg').setAttribute('src',getDfsHostName()+node.photo);
			displayImg(node.photo,2);
			$('#photo').val(node.photo+";");
		}
		formUrl = getContextPath() + '/portal/quotationtype/update';
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}
// 添加
function saveFun() {
	progressLoad();
	$('#fm').form('submit', {
		url : formUrl,
		onSubmit : function() {
			var flag = $(this).form('validate');
			if (!flag) {
				progressClose();
				return flag;
			}
			if(!validate()){
				progressClose();
				return false;
			}
			return flag;
		},
		success : function(result) {
			$('#dlg').dialog('close');
			treegrid.treegrid('reload');
			progressClose();

			$.messager.alert('消息提示',JSON.parse(result).msg,'info');
		}
	});
}
// 修改
// 删除
function delFun() {
	var node = treegrid.treegrid('getSelected');
	if (node) {
		/*if(node.grade==1){
			$.message('大类不可删除!');
			return;
		}*/
		var msg='确认删除?';
		if(node.grade==1 || node.grade==2){
			msg='将删除 "'+node.typeName+'"下所有类型，确认删除?';
		}
		$.messager.confirm('提示信息', msg, function(r) {
			if (r) {
				var ids = node.typeId;
				$.post(getContextPath() + '/portal/quotationtype/delete', {
					ids : ids
				}, function(result) {
					// 刷新数据
					treegrid.treegrid('clearSelections');
					treegrid.treegrid('reload');
					$.message('操作成功!');
				});
			} else {
				return;
			}
		});
	}
}
// 数据校验
function validate(){
	var typeName=$("input[name='typeName']").val() ;//$('#typeName').val();
	if (typeName == null || typeName == '') {
		$.message('请输入名称!');
		return false;
	}
	var grade =$('#grade').combobox('getValue') ;
	if (grade == null || grade == '') {
		$.message('请选择等级!');
		return false;
	}
	if(grade>1){
		//子类/项目
		var parentId =$('#parentId').combobox('getValue') ;
		if (parentId == null || parentId == '') {
			$.message('请选择直属上级!');
			return false;
		}
	}
	if(grade==3){
		var unitPrice=$("input[name='unitPrice']").val() ;//$('#unitPrice').val();
		if (unitPrice == null || unitPrice == '') {
			$.message('请输入单价!');
			return false;
		}
	}
	return true;
}
// 打开Dlg
function openDialog(id, grade) {
	$('#' + id).dialog(
			{
				modal : true,
				width : 400,
				height : 590,
				onOpen : function(event, ui) {
					if(grade>1){
						$('#parentId').combotree(
								{
									url : getContextPath()
											+ '/portal/quotationtype/selectlist/'
											+ grade,
									lines : true,
									cascadeCheck : false,
									parentField : 'pid',
									idField : 'id',
									treeField : 'text',
//									editable :true,//后期加上搜索
//									filter:{}
									onBeforeSelect: function(node) {  //只能选择叶子节点
							            if (!$(this).tree('isLeaf', node.target)) {  
							                return false;  
							            }  
							        }
						});
					}else{
						$('#parentId').combotree(
								{
									lines : true,
									cascadeCheck : false,
									parentField : 'pid',
									idField : 'id',
									treeField : 'text'
						});
					}
				}
			}).dialog('open').dialog('center');
}

/*function changeImg(obj) {
    var windowURL = window.URL || window.webkitURL;
    var loadImg = windowURL.createObjectURL(obj.files[0]);
    document.getElementById('displayFileImg').setAttribute('src',loadImg);
} */

/*function removeFileFuc(obj){
	$('#displayFileImg').attr('src','/resources/img/portal/user/default.png');
	$('#uploadFile').val('');
	$('#photo').val('');
}*/