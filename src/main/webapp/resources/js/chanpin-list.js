var editing; // 判断用户是否处于编辑状态
var flag; // 判断新增和修改方法
var formUrl;
var featureFormUrl;
var datagrid;
var datagrid2;
var datagrid3;
$().ready(function() {
	// 初始化DataGrid
	datagrid = $('#gride').datagrid({
		url : getContextPath() + '/portal/chanpin/list',
		idField : 'chanpinId',
		title : '产品列表',
		fitColumns : true,
		striped : true,
		loadMsg : '数据正在加载,请耐心的等待...',
		rownumbers : true,
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		} ] ],
		columns : [ [ {
			field : 'chanpinId',
			title : 'ID',
			width : 60,
			align : 'center'
		}, {
			field : 'chanpinName',
			title : '名称',
			width : 60,
			align : 'center'
		}
		, {
			field : 'chanpinDescription',
			title : '描述',
			width : 60,
			align : 'center'
		}, {
			field : 'chanpinCreateTime',
			title : '创建时间',
			align : 'center',
			width : 60
		}, {
			field : 'chanpinUpdateTime',
			title : '更新时间',
			align : 'center',
			width : 60
		}] ],
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
			if(data != null){
				initScene(data.chanpinId);
			}else{
				// 0设定成特殊值，为新建对象时所用
				initScene(0); 
			}
		}
	}).dialog('open').dialog('center');
}

// 增加
function addFuc() {
	$('#fm').form('clear');
	openDialog('dlg', null);
	formUrl =getContextPath()+'/portal/chanpin/save';
}
// 修改
function editFuc() {
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#fm').form('clear');
		$('#fm').form('load',rows[0]);
		formUrl = getContextPath() + '/portal/chanpin/update';
		openDialog('dlg',rows[0]);
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}
function delFuc(){
	var arr = datagrid.datagrid('getSelections');
	if(arr.length <= 0 ){
		$.message('请选择进行删除操作!');
	} else {
		$.messager.confirm('提示信息' , '确认删除?' , function(r){
			if(r){
				var ids = '';
				for(var i = 0 ; i < arr.length ; i++){
					ids += arr[i].chanpinId + ',';
				}
				ids = ids.substring(0,ids.length-1);
				$.post(getContextPath() + '/portal/chanpin/delete', {ids:ids},function(result){
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

function save(){
	progressLoad();
	$('#fm').form('submit',{
		url : formUrl,
		success : function(result) {
			var res = $.evalJSON(result);
			progressClose();
			$('#dlg').dialog('close');
			datagrid.datagrid('clearSelections');
			datagrid.datagrid('reload');
			var msg = res.result == 1 ||  res.result == '1' ? '操作成功':'操作失败';
			$.message(msg);
		}
	});
}
function searchFun(){
	datagrid.datagrid('load', $.serializeObject($('#searchForm')));
}
function cleanFun(){
	$('#searchFormactivityName').val('');
}
function initScene(id) {
	var root = $(".sceneTag");
	root.html("");
	getData(function(res){
		if(res != null && res != undefined){
			var array = res.result.rows;
			for (var int = 0; int < array.length; int++) {
				var checked ="";
				var a= array[int];
				if(a.checked){
					checked = 'checked="checked"';
				}
				var ele = '<input type="checkbox" name="sceneTag"  '+checked+'  value="'+a.sceneId+'">' + a.sceneName;
				root.append(ele);
			}
		}
	}, getContextPath()+"/portal/chanpin/scene/list/"+id);
}
/////////////////////////////////封面////////////////////////////////////
/**
 * 显示封面编辑弹出框
 */
function showBannerImg(){
	$('#BannerD').show();
	$('#BannerDlgBody').hide();
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#BannerDlg').dialog({
			modal : true,
			onOpen : function(event, ui) {
				var imgList = $('#imgList');
					datagrid2 = $('#imgList').datagrid({
						url : getContextPath()+'/portal/chanpin/bannerImgs?chanpinId='+rows[0].chanpinId,
						fitColumns : true,
						striped : true,
						loadMsg : '数据正在加载,请耐心的等待...',
						rownumbers : true,
						columns : [ [ {
							field : 'path',
							title : 'ID',
							width : 60,
							align : 'center',
							formatter : function(value,row,index){
								if(row != undefined && row != '' && row != null){
									return '<img style="height:100px;" src="'+getDfsHostName()+row+'">';
								}
								return '';
							}
						},{
							field : 'option',
							title : '更新时间',
							align : 'center',
							width : 60,
							formatter : function(value,row,index){
								if(row != undefined && row != '' && row != null){
									return '<button data-id='+row+' onclick="deleteImg(this);">删除</button>';
								}
								return '';
							}
						}] ],
						pagination : false,
						pageSize : 20,
						pageList : [20],
						showFooter : false,
						toolbar : '#toolbar2',
						onLoadSuccess : function(){
							$('#imgList').datagrid('enableDnd');
						}
					});
			}
		}).dialog('open').dialog('center');
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}
/**
 * 初始化批量上传图片组件
 */
function initSubPicImgView() {
	$('#BannerDlgBody').html('');
	var rows = datagrid.datagrid('getSelections');
	var param = {};
	param['chanpinId'] = rows[0].chanpinId;
	$("#BannerDlgBody").zyUpload({
		width            :   "640px",                 // 宽度
		height           :   "400px",                 // 宽度
		itemWidth        :   "120px",                 // 文件项的宽度
		itemHeight       :   "100px",                 // 文件项的高度
		url              :   "/portal/chanpin/upload",  // 上传文件的路径
		multiple         :   true,                    // 是否可以多个文件上传
		dragDrop         :   false,                   // 是否可以拖动上传文件
		del              :   true,                    // 是否可以删除文件
		finishDel        :   false,  				  // 是否在上传文件完成后删除预览
		
		fromData         :   param,
		
		/* 外部获得的回调接口 */
		onSelect: function(files, allFiles){                    // 选择文件的回调方法
			console.info("当前选择了以下文件：");
			console.info(files);
			console.info("之前没上传的文件：");
			console.info(allFiles);
		},
		onDelete: function(file, surplusFiles){                     // 删除一个文件的回调方法
			console.info("当前删除了此文件：");
			console.info(file);
			console.info("当前剩余的文件：");
			console.info(surplusFiles);
		},
		onSuccess: function(file){                    // 文件上传成功的回调方法
			console.info("此文件上传成功：");
			console.info(file);
		},
		onFailure: function(file){                    // 文件上传失败的回调方法
			console.info("此文件上传失败：");
			console.info(file);
		},
		onComplete: function(responseInfo){           // 上传完成的回调方法
			console.info("文件上传完成");
			console.info(responseInfo);
		}
	});
}
/**
 * 前台UI切换方法
 */
function subImg() {
	$('#BannerD').hide();
	$('#BannerDlgBody').show();
	initSubPicImgView();
}
/**
 * 封面图片排序方法
 */
function saveOd(){
	var arr=datagrid2.datagrid('getData');
	var rows = datagrid.datagrid('getSelections');
	 $.ajax({
        type: "POST",
        url: getContextPath()+'/portal/chanpin/saveod',
        data: {"chanpinId":rows[0].chanpinId,"paths":$.toJSON(arr.rows)},
        success: function(data){
       	 datagrid2.datagrid('clearSelections');
       	 datagrid2.datagrid('reload');
        }
    });
}
/**
 * 删除封面图片
 * @param self
 */
function deleteImg(self){
	var id = $(self).attr('data-id');
	var rows = datagrid.datagrid('getSelections');
	 $.ajax({
         type: "GET",
         url: getContextPath()+'/portal/chanpin/delete/img',
         data: {"chanpinId":rows[0].chanpinId,"path":id},
         success: function(data){
        	 datagrid2.datagrid('clearSelections');
        	 datagrid2.datagrid('reload');
         }
     });
}
function back(){
	$('#BannerDlg').dialog('close');
	$('#featureDlg').dialog('close');
}
////////////////////////////////////////特性相关//////////////////////////////////////
function showFeature(){
	var rows = datagrid.datagrid('getSelections');
	if(rows.length == 1){
		$('#featureDlg').dialog({
			modal : true,
			onOpen : function(event, ui) {
				datagrid3 = $('#featureBody').datagrid({
					url : getContextPath()+'/portal/chanpin/feature?chanpinId='+rows[0].chanpinId,
					fitColumns : true,
					striped : true,
					loadMsg : '数据正在加载,请耐心的等待...',
					rownumbers : true,
					idField : 'fId',
					frozenColumns : [ [ {
						field : 'ck',
						checkbox : true
					} ] ],
					columns : [ [ {
						field : 'name',
						title : '特性名称',
						width : 60,
						align : 'center'
					}
					,{
						field : 'description',
						title : '特性描述',
						width : 60,
						align : 'center'
					}
					,{
						field : 'picHDUrl',
						title : '封面',
						align : 'center',
						width : 60,
						formatter : function(value,row,index){
							if(row != undefined && row != '' && row != null){
								return '<span >双击查看封面</span>';
							}
							return '';
						}
					}
					,{
						field : 'option',
						title : '操作',
						align : 'center',
						width : 60,
						formatter : function(value,row,index){
							if(row != undefined && row != '' && row != null){
								return '<button data-id='+row.fId+' onclick="deleteFeature(this);">删除</button>';
							}
							return '';
						}
					}] ],
					pagination : false,
					pageSize : 20,
					pageList : [20],
					showFooter : false,
					toolbar : '#toolbar3',
					onLoadSuccess : function(){
						$('#featureBody').datagrid('enableDnd');
					},
					onDblClickCell:function(index,field,value){
						if(field == 'picHDUrl'){
							$('#picture-condition').removeClass('hide');
							$('#productPicture').removeClass('hide');
							$('#productVideo').addClass('hide');
							$('#productVideo').hide('fast');
							var imgPath = getDfsHostName() + value;
							$('#productPicture').attr('src',imgPath);
							$('#p-cancel').on('click',function(){
								$('#picture-condition').addClass('hide');
								$('#productVideo').attr('src','');
								$('#productPicture').attr('src','');
								$('#youku-player').hide('fast');
							});
						}
					}
				});
			}
		}).dialog('open').dialog('center');
	} else {
		$.message('只能选择一条记录进行修改!');
	}
}

function addFeature() {
	$('#dlgFeatureForm').dialog({
		modal : true,
		onOpen : function(event, ui) {
			$('#fmFeature').form('clear');
			var rows = datagrid.datagrid('getSelections');
			$('#chanpinId-Feature').val(rows[0].chanpinId);
			featureFormUrl = getContextPath() +'/portal/chanpin/save/feature';
		}
	}).dialog('open').dialog('center');
}
function updateFeature() {
	$('#dlgFeatureForm').dialog({
		modal : true,
		onOpen : function(event, ui) {
			var rows = datagrid.datagrid('getSelections');
			var rows3 = datagrid3.datagrid('getSelections');
			$('#fmFeature').form('clear');
			$('#fmFeature').form('load',rows3[0]);
			$('#chanpinId-Feature').val(rows[0].chanpinId);
			featureFormUrl = getContextPath() +'/portal/chanpin/update/feature';
		}
	}).dialog('open').dialog('center');
}
function saveFeature(){
	progressLoad();
	$('#fmFeature').form('submit',{
		url : featureFormUrl,
		success : function(result) {
			var res = $.evalJSON(result);
			progressClose();
			$('#dlgFeatureForm').dialog('close');
			datagrid3.datagrid('clearSelections');
			datagrid3.datagrid('reload');
			progressClose();
			$.message(res.result);
		}
	});
}
function deleteFeature(self){
	var fId = $(self).attr('data-id');
	var rows = datagrid.datagrid('getSelections');
	 $.ajax({
         type: "GET",
         url: getContextPath()+'/portal/chanpin/delete/feature',
         data: {"chanpinId":rows[0].chanpinId,"fId":fId},
         success: function(data){
        	 datagrid3.datagrid('clearSelections');
        	 datagrid3.datagrid('reload');
        	 $.message(data.result);
         }
     });
}
function saveFeatureOd(){
	var arr=datagrid3.datagrid('getData');
	var rows = datagrid.datagrid('getSelections');
	 $.ajax({
        type: "POST",
        url: getContextPath()+'/portal/chanpin/saveFeatureOd',
        data: {"chanpinId":rows[0].chanpinId,"features":$.toJSON(arr.rows)},
        success: function(data){
       	 datagrid3.datagrid('clearSelections');
       	 datagrid3.datagrid('reload');
       	 $.message(data.result);
        }
    });
}
