var curCount;
var count = 120; // 2分钟执行一次
var index_tabs;

$().ready(function(){
	
	// 根据权限查询菜单
	$("#menu").tree({
		url : getContextPath() + "/portal/right/menu",
		method : "get",
		parentField : 'pid',
		animate : true,
		lines : true,
		onClick : function(node){
			if (node.attributes && node.attributes.url) {
				var url = getContextPath() + node.attributes.url;
				addTab({
					url : url,
					title : node.text,
					iconCls : node.iconCls
				});
			}
		}
	});
	
	index_tabs = $('#index_tabs').tabs({
		fit : true,
		border : false,
		tools : [{
			iconCls : 'icon-home',
			handler : function() {
				index_tabs.tabs('select', 0);
			}
		}, {
			iconCls : 'icon-refresh',
			handler : function() {
				var index = index_tabs.tabs('getTabIndex', index_tabs.tabs('getSelected'));
				//index_tabs.tabs('getTab', index).panel('open').panel('refresh');
				var current_tab = index_tabs.tabs('getTab', index);
				
				index_tabs.tabs('update',{
					tab : current_tab,
					options : {
						content : current_tab.panel('options').content
					}
				});
			}
		}, {
			iconCls : 'icon-del',
			handler : function() {
				var index = index_tabs.tabs('getTabIndex', index_tabs.tabs('getSelected'));
				var tab = index_tabs.tabs('getTab', index);
				if (tab.panel('options').closable) {
					index_tabs.tabs('close', index);
				}
			}
		} ]
	});
	
	// 每2分钟检测 订单状态，如果有 新订单 则弹出提示
	/*checkIndentStatus();*/
	
});

// 查看 订单状态
function checkIndentStatus(){
	window.setInterval(loadStatus, 120000);
	
	function loadStatus(){
		loadData(function(count){
			if(count > 0){
				$('.l-btn').click(); // 先清除所有的 alert 弹框
				// 在menu出添加 徽章
				var li_list = $('#menu').find('.tree-node');
				console.log(li_list);
				$.each(li_list,function(i,n){
					if(n.text == '订单管理'){
						$(this).find('.badge').remove();
						var $span = '<span class="badge">new-';
						$span += count;
						$span += '</span>';
						$(this).append($span);
					}
				});
				$.messager.alert('Warning','您有'+ count +'条订单未处理,请及时处理!');
			} else {
				$('.l-btn').click(); // 先清除所有的 alert 弹框
				$('#menu').find('.tree-node').find('.badge').remove(); // 清除 徽章 效果
			}
		}, getContextPath() + '/portal/indent/checkStatus/new', null);
	}
}

// 登出
function logout(){
	
	getData(function(){
		window.location.href= getContextPath() + '/login';
	}, getContextPath() + '/portal/logout');
}

function addTab(params) {
	var iframe = '<iframe src="' + params.url + '" frameborder="0" style="border:0;width:100%;height:99.6%;"></iframe>';
	var t = $('#index_tabs');
	var opts = {
		title : params.title,
		closable : true,
		iconCls : params.iconCls,
		content : iframe,
		border : false,
		fit : true
	};
	if (t.tabs('exists', opts.title)) {
		t.tabs('select', opts.title);
	} else {
		t.tabs('add', opts);
	}
}

// 修改密码
function editUserPwd(){
	parent.$.modalDialog({
		title : '修改密码',
		width : 300,
		height : 250,
		href : getContextPath() + '/portal/editEmployeePwd',
		buttons : [{
			text : '修改',
			handler : function() {
				var f = parent.$.modalDialog.handler.find('#editUserPwdForm');
				f.submit();
			}
		}]
	});
}