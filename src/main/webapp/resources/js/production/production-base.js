var teamList;
var referrerList;
var citys;
var upload_Video;


function init(type){
	
	syncLoadData(function(res) {
		citys = res;
	}, getContextPath() + '/portal/all/citys', null);
	
	syncLoadData(function(res) {
		typeIdList = res;	
	}, getContextPath() + '/portal/quotationtype/production/select?productionType='+type, null);

		
	
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
		
		
		$('#search-referrer').combobox({
			data :referrerList,
			valueField:'employeeId',
			textField:'employeeRealName'
		}); 
	}, getContextPath() + '/portal/getEmployeeList', null);
	
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
		$('#typeId').combotree(
				{
					data :typeIdList,
					lines : true,
					cascadeCheck : false,
					parentField : 'pid',
					idField : 'id',
					treeField : 'text',
//					editable :true,//后期加上搜索
//					filter:{}
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
//					editable :true,//后期加上搜索
//					filter:{}
					onBeforeSelect: function(node) {  //只能选择叶子节点
			            if (!$(this).tree('isLeaf', node.target)) {  
			                return false;  
			            }  
			        }
		});
		
		initCutPhoto('uploadDiv');
	}
	
	
	
}

