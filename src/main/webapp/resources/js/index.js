var StepTool;
var stepListJson;
$().ready(function() {
	loadprojecctlist(false);
	$(".flowbtn").on("click", function() {
		var key = getCurrentProject();
		loadData(function(msg) {
			loadflowdata();
		}, getContextPath() + '/completeTask', $.toJSON({
			id : key
		}));
	});
	$(".upload-file-btn").on("click", function() {
		$("#addfile").click();
	});
	$("#addfile").on("change", function() {
		uploadfile();
	});
	$(".more-file-btn").on("click", function() {
		loadfiledata(true);
		$(".more-file-btn").hide();
	});
	$(".comment-btn").on("click", function() {
		submitcomment();
	});
	$(".more-comment").on("click", function() {
		loadcommentdata(true);
		$(".more-comment").hide();
	});

	$(".newBtn").on("click", function() {
		window.location.href = getContextPath() + "/add-view";
	});

});

function submitcomment() {
	var key = getCurrentProject();
	var comment = $(".comment").val();
	if (comment == null || comment == '') {
		alert("不能为空")
		return;
	}
	loadData(function(msg) {
		$(".comment").val("");
		loadcommentdata(false);
	}, getContextPath() + '/addComment', $.toJSON({
		icContent : comment,
		icIndentId : key,
		icUserType : 1,
		icUserId : 2
	}));
}
var currentIndex;
function loadflowdata() {
	var key = getCurrentProject();
	loadData(
			function(msg) {
				stepListJson = new Array(msg.length);
				for (var i = 0; i < msg.length; i++) {
					stepListJson[i] = {
						StepNum : (i + 1),
						StepText : msg[i].name,
						StepDescription : msg[i].description
					};
				}
				// 当前进行到第几步
				var currentStep = 1;
				StepTool = new Step_Tool_dc("test", "mycall");
				// 使用工具对页面绘制相关流程步骤图形显示
				StepTool.drawStep(currentStep, stepListJson);
				loadData(
						function(msg) {
							$(".description-text").text(msg.description);
							var num;
							$(".test ul li").each(function(index) {
								num = jQuery(this).attr("data-value");
								var text = jQuery(this).attr("data-text");
								if (text.trim() == msg.name.trim()) {
									StepTool.drawStep(num, stepListJson);
									currentIndex = num;
									return;
								}
							});
							if (msg.name.trim() == '任务不存在') {
								StepTool.drawStep(num, stepListJson);
								currentIndex = num;
								return;
							}

							$(".drop-content a").each(function(index) {
								$(this).css({
									'animation-delay' : (index / 10) + 's'
								});
							});

							(function() {
								var init, isMobile, setupExamples, setupHero, _Drop;

								_Drop = Drop.createContext({
									classPrefix : 'drop'
								});

								init = function() {
									return setupExamples();
								};

								setupExamples = function() {
									return $('.test')
											.each(
									function() {
										var $example, $target, content, drop, openOn, theme;
										$example = $(this);
										theme = $example
												.data('theme');
										openOn = $example
												.data('open-on')
												|| 'click';
										$target = $example
												.find('.drop-target');
										$target.addClass(theme);
										content = function() {
											return $('.drop-content').html();
										};
										for (var int = 0; int < $target.length; int++) {
											drop = new _Drop(
													{
														target : $target[int],
														classes : theme,
														position : 'bottom right',
														constrainToWindow : true,
														constrainToScrollParent : false,
														openOn : openOn,
														content : content
													});
											drop.on("open",function() {
													var text = jQuery(this.target).attr("data-description");
													$(".description-c").html("<p>"+ text+ "</p>");
											});
										}
									});
								};
								init();
							}).call(this);
						}, getContextPath() + '/getCurrectTask', $.toJSON({
							id : key
						}));
			}, getContextPath() + '/getnodes', $.toJSON({
				id : key
			}));
}
/**
 * 上传文件
 */
function uploadfile() {
	var key = getCurrentProject();
	$.ajaxFileUpload({
		url : getContextPath() + '/addResource',
		secureuri : true,
		fileElementId : [ 'addfile' ],
		dataType : 'text/html',
		timeout : 0,
		data : {
			id : key
		},
		success : function(data) {
			alert(data);
			loadfiledata(false);
		},
		error : function(data, status, e) {
			alert(data);
		}
	});

}
function loadfiledata(more) {
	var key = getCurrentProject();
	loadData(
			function(msg) {
				var tab = $(".file-table");
				tab.html("");
				for (var i = 0; i < msg.length; i++) {
					var tr = $("<tr></tr>");
					var td1 = $("<td class=\"file-icon\"> <img src=\"/pat4/resources/images/index/file.png\"/> </td>");
					var td2 = $("<td class=\"file-name\">"
							+ msg[i].irOriginalName + "</td>");
					var td3 = $("<td class=\"file-createtime\">"
							+ msg[i].irCreateDate + "</td>");
					var td_qr = $("<td class=\"qrcode-td\">"
							+ "</td>");
					var img_qr=$("<img class=\"qrcode-img\" data-url=\""+msg[i].irId+"\" src=\"/pat4/resources/images/index/qrcode.png\">");
					var td4 = $("<td><a href="
							+ getContextPath()
							+ '/getFile?indentResourceId='
							+ msg[i].irId
							+ "><img src=\"/pat4/resources/images/index/download.png\"/></a></td>");

					$(img_qr).on("click", function() {
						
						var fileId=jQuery(this).attr("data-url");
						loadData(function(msg) {
							var url="/portal/project/doc/"+msg.url;
							window.location.href = url;
						}, getContextPath() + '/getDocView', $.toJSON({
							irId : fileId
						}));
					});
					td_qr.append(img_qr);
					tr.append(td1);
					tr.append(td2);
					tr.append(td3);
					tr.append(td_qr);
					tr.append(td4);
					tab.append(tr);
					if (!more && i == 2) {
						break;
					}
				}
			}, getContextPath() + '/getResourceList', $.toJSON({
				id : key
			}));

}
function loadcommentdata(more) {
	var key = getCurrentProject();
	loadData(
			function(msg) {
				var tab = $(".message-table");
				tab.html("");
				for (var i = 0; i < msg.length; i++) {
					var tr = $("<tr></tr>");
					var td1 = $("<td class=\"message-portrait\" rowspan=\"2\"><img class=\"message-portrait-img\""
							+ " src=\"/pat4/resources/images/index/first-desc.png\"></td>");

					var x = msg[i].icUserType;
					var text = "小王，小王";
					if (x == '4') {
						text = "系统";
					}
					var td2 = $("<td><label class=\"msg-comm-name\">"
							+ text
							+ "</label><label class=\"msg-comm-time\">1个小时前</label></td>");
					tr.append(td1);
					tr.append(td2);
					tab.append(tr);

					var tr2 = $("<tr></tr>");
					var td2_1 = ("<td>" + msg[i].icContent + "</tf>");
					tr2.append(td2_1);

					tab.append(tr2);

					if (!more && i == 2)
						break;
				}
			}, getContextPath() + '/getAllComment', $.toJSON({
				id : key
			}));
}
function loadprojecctlist(more) {
	loadData(function(msg) {
		var tab = $(".indentlist");
		tab.html("");
		var currentprojectkey = '';
		for (var i = 0; i < msg.length; i++) {
			var tr = $("<tr></tr>");
			var td = $("<td ></td>");
			if (i == 0 && !more && getCurrentProject() == null) {
				putCurrentProject(msg[i].id);
				currentprojectkey = msg[i].id + '';
			}
			var a = $("<a class=\"indent-a\" data-value=" + msg[i].id + ">");
			$(a).on("click", function() {
				var key = $(this).attr("data-value");
				putCurrentProject(key);
				// ///////////////////////////////////////////////////////////
				loadprojecctlist(false);
			});
			a.append("&nbsp;&nbsp;");
			a.append(msg[i].projectName);
			td.append(a);
			tr.append(td);
			tab.append(tr);
			if (!more && i == 10) {
				break;
			}
		}
		// load more component
		loadflowdata();
		loadfiledata(false);
		loadcommentdata(false);
		loadIndentInfo();
		updateProject();
	}, getContextPath() + '/project/all-project', $.toJSON({
		userType : 1,
		userId : 2
	}));

}

function updateProject() {
	$(".indentlist tr td a")
			.each(
					function(index) {
						num = jQuery(this).attr("data-value");
						var key = getCurrentProject();
						if (num == key) {
							jQuery(this).attr("class", "indent-selected");

							var editImg = $("<img class='indent-ednt-btn' src=\"/pat4/resources/images/index/edit.png\"/>");
							$(editImg).on(
									"click",
									function() {
										window.location.href = getContextPath()
												+ "/project/upadte-view";
									});
							jQuery(this).append("&nbsp;&nbsp;&nbsp;&nbsp;");
							jQuery(this).append(editImg);
						} else {
							jQuery(this).attr("class", "indent-a");
							jQuery(this).next().remove();
						}
					});
}
function loadIndentInfo() {
	var key = getCurrentProject();
	loadData(function(msg) {
		// get
		var projectId = $(".projectId");
		var projectName = $(".projectName");
		var userName = $(".userName");
		var teamName = $(".teamName");
		var userContact = $(".userContact");
		var teamContact = $(".teamContact");
		var userPhone = $(".userPhone");
		var teamPhone = $(".teamPhone");
		var viedoPrice = $(".viedoPrice");
		// clear
		$(projectId).text("");
		$(projectName).text("");
		$(userName).text("");
		$(teamName).text("");
		$(teamContact).text("");
		$(userContact).text("");
		$(userPhone).text("");
		$(teamPhone).text("");
		$(viedoPrice).text("");
		// assignment
		$(projectId).text(msg.id);
		$(projectName).text(msg.projectName);
		$(userName).text(msg.userName);
		$(teamName).text(msg.teamName);
		$(teamContact).text(msg.teamContact);
		$(userContact).text(msg.userContact);
		$(userPhone).text(msg.userPhone);
		$(teamPhone).text(msg.teamPhone);
		$(viedoPrice).text(msg.price + '万元');

	}, getContextPath() + '/project/get-projectInfo', $.toJSON({
		id : key
	}));
}
function getCurrentProject() {
	return $.cookie('currentproject');
}
function putCurrentProject(key) {
	$.cookie("currentproject", key + '');
}
/**
 * 
 * 流程相关回掉实现
 */
// 点击
function mycall(restult) {
	// alert("mycall"+result.value+":"+result.text);
	StepTool.drawStep(result.value, stepListJson);
}
// 移入
function mouseenter(restult) {
	console.log("in" + restult.value);
}
// 移出
function mouseleave(restult) {
	console.log("out" + restult.value);
}
// 获取当前时间
function getCurrentTime() {
	var myDate = new Date();
	var year = myDate.getFullYear();
	var month = myDate.getMonth() + 1;
	var day = myDate.getDate();
	return year + '-' + month + '-' + day;
}
