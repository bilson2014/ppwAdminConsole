var currentPage = 1 ; // 当前页码
var currentPageSize = 10; // 当前页面容量
$().ready(function(){
	
	initData();
	
	// 初始化DataGrid
	$('#gride').datagrid({
		idField : 'productId' ,
		title : '搜索结果' , 
		fitColumns : false ,
		striped : true ,
		loadMsg : '数据正在加载,请耐心的等待...' ,
		rownumbers : true ,
		columns:[[
					{
						field : 'teamName',
						title : '团队名称',
						//width : 150,
						align : 'center'
					},{
						field : 'productName',
						title : '视频名称',
						//width : 150,
						align : 'center'
					},{
						field : 'itemName',
						title : '视频类型',
						//width : 150,
						align : 'center'
					},{
						field : 'tags',
						title : '视频标签',
						//width : 200,
						align : 'center'
					},{
						field : 'pDescription',
						title : '视频描述',
						//width : 200,
						align : 'center'
					},{
						field : 'price',
						title : '折扣价',
						//width : 200,
						align : 'center',
						formatter : function(value,row,index){
							return '<span style=color:red; >'+ row.price +' 元</span>' ;
						}
					},{
						field : 'orignalPrice',
						title : '原价',
						//width : 200,
						align : 'center',
						formatter : function(value,row,index){
							return '<span style=color:red; >'+ row.orignalPrice +' 元</span>' ;
						}
					},{
						field : 'length',
						title : '时长',
						//width : 200,
						align : 'center',
						formatter : function(value,row,index){
							return '<span style=color:red; >'+ row.length +' 秒</span>' ;
						}
					},{
						field : 'picLDUrl',
						title : '海报',
						//width : 200,
						align : 'center'
					}
				]],
		pagination: true
	});
	
	$('#search-btn').on('click',function(){
		
		loadSolrData();
		pagination();
	});
	
	// 注册 suggest 事件
	var oT=document.getElementById('solrInput');
	var oUl=document.getElementById('shelper');
	var now=-1;
	var oldValue='';
	
	oT.onkeyup=function(ev){
		var oEvent=ev || event;
		if (oEvent.keyCode==40 || oEvent.keyCode==38)
		{
			return false;
		}
		
		oldValue=this.value;
		
		var token = $('#solrInput').val().trim();
		if(token != null && token != '' && token != undefined){
			loadData(function(list){
				if(list != null && list != '' && list != undefined) {

					oUl.style.display='block';
					oUl.innerHTML='';
					var arr=list;
					
					for (var i=0; i<arr.length; i++)
					{
						var oLi=document.createElement('li');
						oLi.innerHTML=arr[i].condition;
						oUl.appendChild(oLi);
					}
					
					// 移入移出 点击跳转
					var aLi=oUl.children;
					for (var i=0; i<aLi.length; i++)
					{
						(function (index){
							aLi[i].onmouseover=function (){
								aLi[index].className='on';
							};
							
							aLi[i].onmouseout=function (){
								aLi[index].className='';
							};
							
							aLi[i].onclick=function (){
								$('#solrInput').val(this.innerHTML);
								$('#search-btn').click();
								oUl.style.display='none';
							};
							
						})(i);
					}
				
				}
			}, getContextPath() + '/portal/solr/suggest/' + token, null);
		}
	};
	
	oT.onkeydown=function (ev){
		var aLi=oUl.children;
		
		var oEvent=ev || event;
		
		if (oEvent.keyCode == 40)
		{
			now++;
			if (now == aLi.length)
			{
				now=-1;
			}
			
			for (var i=0; i<aLi.length; i++)
			{
				aLi[i].className='';
			}
			
			if (now == -1)
			{
				oT.value=oldValue;
			}
			else
			{
				aLi[now].className='on';
				oT.value=aLi[now].innerHTML;
			}
			
			document.title=now;
		}
		
	};
});
	
	

function initData(){
	loadData(function(items){
		var $option = '';
		$.each(items,function(i,item){
			$option += '<option value="'+ item.itemId +'">';
			$option += item.itemName;
			$option += '</option>';
		})
		$('#solr-item').append($option);
	}, getContextPath() + "/portal/item/itemSelect", null);
}

//翻页
function pagination(){
	var page = $('#gride').datagrid('getPager');
	$(page).pagination({
		showRefresh : false,
		showPageList : false,
		pageSize: currentPageSize,
		onSelectPage : function(pageNumber,pageSize){
			currentPage = pageNumber;
			
			loadSolrData();
		}
	});
}

function loadSolrData(){
	var condition = $('#solrInput').val().trim();
	
	var price = $('#solr-price option:selected').val();
	var length = $('#solr-length option:selected').val();
	var item = $('#solr-item option:selected').val();
	
	loadData(function(list){
		$('#gride').datagrid('loadData',list);
	}, getContextPath() + '/portal/solr', $.toJSON({
		begin : (currentPage - 1) * currentPageSize,
		limit : currentPageSize,
		condition : condition,
		priceFq:price,
		lengthFq:length,
		itemFq:item
	}));
}