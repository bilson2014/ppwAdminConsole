var teamList=[];
var referrerList=[];
var citys=[];
var upload_Video;


function init(type){
	
	loadData(function(res) {
		citys = res;
		
		loadData(function(res) {
			typeIdList = res;	
			
			searchFun();
			
			if(type!='device'){
				$('#typeId').combotree(
						{
							data :typeIdList,
							lines : true,
							cascadeCheck : false,
							parentField : 'pid',
							idField : 'id',
							treeField : 'text',
//							editable :true,//后期加上搜索
//							filter:{}
							onBeforeSelect: function(node) {  //只能选择叶子节点
					            if (!$(this).tree('isLeaf', node.target)) {  
					                return false;  
					            }  
					        }
				});
				
				$('#search-typeId').combotree(
						{
							data :typeIdList,
							lines : true,
							cascadeCheck : false,
							parentField : 'pid',
							idField : 'id',
							treeField : 'text',
//							editable :true,//后期加上搜索
//							filter:{}
							onBeforeSelect: function(node) {  //只能选择叶子节点
					            if (!$(this).tree('isLeaf', node.target)) {  
					                return false;  
					            }  
					        }
				});
			}
			
		}, getContextPath() + '/portal/quotationtype/production/select?productionType='+type, null);
		

		$('#city').combobox({
			data : citys,
			valueField : 'cityID',
			textField : 'city'
		});
		$('#search-city').combobox({
			data : citys,
			valueField : 'cityID',
			textField : 'city'
		});
		
	}, getContextPath() + '/portal/all/citys', null);
	
	

		
	
	//statusList=[{'value':2,'text':'审核中'},{'value':1,'text':'审核通过'},{'value':0,'text':'审核未通过'}];
	loadData(function(res) {
		teamList = res;
		$('#teamId').combobox({
			data : teamList,
			valueField : 'teamId',
			textField : 'teamName',
			filter: function(q, row){
				if(row.teamName == null)
					return false;
				return row.teamName.indexOf(q) >= 0;
			}
		});
		
		$('#search-teamId').combobox({
			data : teamList,
			valueField : 'teamId',
			textField : 'teamName',
			filter: function(q, row){
				if(row.teamName == null)
					return false;
				return row.teamName.indexOf(q) >= 0;
			}
		});
	}, getContextPath() + '/portal/team/listByName', $.toJSON({
		teamName : ''
	}));
	
	loadData(function(res) {
		referrerList = res;
		
		$('#referrer').combobox({
			data :referrerList,
			valueField:'employeeId',
			textField:'employeeRealName'
		}); 
		
		var dataLevel=$('#dataLevel').val();
		
		
		if(dataLevel!=undefined && dataLevel!=null && dataLevel=='1'){
			var s_referrerList=JSON.parse(JSON.stringify(referrerList));
			s_referrerList.unshift({'employeeId':'-1','employeeRealName':'无推荐人'});
			
			$('#search-referrer').combobox({
				data :s_referrerList,
				valueField:'employeeId',
				textField:'employeeRealName'
			});
		}else{
			$('#search-referrer').combobox({
				data :[{'employeeId':'-1','employeeRealName':'无推荐人'},
					{'employeeId':$('#default_referrer').val(),'employeeRealName':$('#default_referrer_name').val()}],
				valueField:'employeeId',
				textField:'employeeRealName'
			}); 
		}
		
		
		
	}, getContextPath() + '/portal/getEmployeeList', null);
	
	
	$('#status').combobox({
		data : statusList,
		valueField : 'value',
		textField : 'text'
	});
	
	var s_statusList=JSON.parse(JSON.stringify(statusList));
	s_statusList.unshift({'value':'','text':'--请选择--'});
	$('#search-status').combobox({
		data : s_statusList,
		valueField : 'value',
		textField : 'text'
	});
	
	if(type!='device'){		
		initCutPhoto('uploadDiv');
	}
		
}

//审核通过必有推荐人，默认当前用户
function setRef(){
	var status = $('#status').combobox('getValue');
	var referrer = $("#referrer").combobox("getValue");
	if(status==1 && referrer==''){
		$("#referrer").combobox("setValue", $('#default_referrer').val());
	}
}

