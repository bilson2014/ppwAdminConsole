<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%-- import CSS --%>
<spring:url value="/resources/lib/normalize/normalize.css"
	var="normalizeCss" />
<spring:url value="/resources/lib/h5bp/h5bp.css" var="h5bpCss" />
<spring:url value="/resources/css/common.css" var="commonCss" />
<spring:url
	value="/resources/lib/jquery.easyui/themes/default/easyui.css"
	var="easyuiCss" />
<spring:url value="/resources/lib/jquery.easyui/themes/icon.css"
	var="iconCss" />
<spring:url value="/resources/lib/dist/css/drop-theme-basic.css"
	var="dropTheme" />
<%-- import JS --%>
<spring:url value="/resources/lib/html5shiv/html5shiv.js"
	var="html5shivJs" />
<spring:url value="/resources/lib/jquery/jquery-2.0.3.min.js"
	var="jqueryJs" />
<spring:url value="/resources/lib/jquery/plugins.js" var="pluginJs" />
<spring:url value="/resources/lib/jquery.blockui/jquery.blockUI.js"
	var="blockUIJs" />
<spring:url value="/resources/lib/jquery.json/jquery.json-2.4.min.js"
	var="jsonJs" />
<spring:url value="/resources/lib/jquery.cookie/jquery.cookie.js"
	var="cookiejs" />
<spring:url value="/resources/lib/jquery.easyui/jquery.easyui.min.js"
	var="easyuiJs" />
<spring:url
	value="/resources/lib/jquery.easyui/locale/easyui-lang-zh_CN.js"
	var="zhJs" />
<spring:url value="/resources/js/common.js" var="commonJs" />
<spring:url value="/resources/lib/My97DatePicker/WdatePicker.js"
	var="WdatePicker" />
<spring:url value="/resources/css/flow/flow.css" var="index" />
<spring:url value="/resources/css/flow/step-dc-style1.css" var="stepdcstyle" />
<spring:url value="/resources/js/flow/step-jquery-dc.js" var="stepjquery" />
<spring:url value="/resources/js/flow/flow.js" var="indexjs" />
<spring:url value="/resources/lib/dist/tether.min.js" var="tetherjs" />
<spring:url value="/resources/lib/dist/js/drop.min.js" var="dropjs" />
<spring:url value="/resources/lib/jquery/ajaxfileupload.js"
	var="ajaxfileuploadJs" />
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=9,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="${normalizeCss }">
<link rel="stylesheet" href="${h5bpCss }">
<link rel="stylesheet" href="${commonCss }">
<link rel="stylesheet" href="${easyuiCss }">
<link rel="stylesheet" href="${iconCss }">
<link rel="stylesheet" href="${userListCss }">

<link rel="stylesheet" href="${index }">
<link rel="stylesheet" href="${stepdcstyle }">
<link rel="stylesheet" href="${dropTheme }">
<!--[if lt IE 9]>
		<script>window.html5 || document.write('<script src="html5shivJs"><\/script>')</script>
	<![endif]-->
<script src="${jqueryJs }"></script>
<script src="${pluginJs }"></script>
<script src="${blockUIJs }"></script>
<script src="${jsonJs }"></script>
<script src="${easyuiJs }"></script>
<script src="${zhJs }"></script>
<script src="${commonJs }"></script>
<script src="${WdatePicker }"></script>
<script src="${stepjquery }"></script>
<script src="${cookiejs }"></script>
<script src="${indexjs }"></script>
<script src="${tetherjs }"></script>
<script src="${dropjs }"></script>


<script type="text/javascript" src="${ajaxfileuploadJs}"></script>
<style>
.drop-content{
	background-color: #fff;
	width:160px;
	position:absolute;
	left: -170px;
}
.drop-content a{
display: inline-block;
width: 100%;
-webkit-animation-duration: 0.8s;
animation-duration: 0.8s;
-webkit-animation-fill-mode: both;
animation-fill-mode: both;
-webkit-animation-name: bounceIn;
animation-name: bounceIn;
}			
@-webkit-keyframes bounceIn {
0% {
  opacity: 0;
  -webkit-transform: scale3d(.3, .3, .3);
  transform: scale3d(.3, .3, .3);
}

20% {
  -webkit-transform: scale3d(1.1, 1.1, 1.1);
  transform: scale3d(1.1, 1.1, 1.1);
}

40% {
  -webkit-transform: scale3d(.9, .9, .9);
  transform: scale3d(.9, .9, .9);
}

60% {
  opacity: 1;
  -webkit-transform: scale3d(1.03, 1.03, 1.03);
  transform: scale3d(1.03, 1.03, 1.03);
}

80% {
  -webkit-transform: scale3d(.97, .97, .97);
  transform: scale3d(.97, .97, .97);
}

to {
  opacity: 1;
  -webkit-transform: scale3d(1, 1, 1);
  transform: scale3d(1, 1, 1);
}
}

@keyframes bounceIn {
  0% {
    opacity: 0;
    -webkit-transform: scale3d(.3, .3, .3);
    transform: scale3d(.3, .3, .3);
  }

  20% {
    -webkit-transform: scale3d(1.1, 1.1, 1.1);
    transform: scale3d(1.1, 1.1, 1.1);
  }

  40% {
    -webkit-transform: scale3d(.9, .9, .9);
    transform: scale3d(.9, .9, .9);
  }

  60% {
    opacity: 1;
    -webkit-transform: scale3d(1.03, 1.03, 1.03);
    transform: scale3d(1.03, 1.03, 1.03);
  }

  80% {
    -webkit-transform: scale3d(.97, .97, .97);
    transform: scale3d(.97, .97, .97);
  }

  to {
    opacity: 1;
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }
}
</style>

</head>
<body>
	<div class="page">
		<div class="left-page">
			<div class="left-title">
				<label class="left-title-text">所有项目</label>
			</div>
			<div class="newBtn">
				<img src="/resources/img/flow/add.png" class="newBtn-img">
			</div>
			<div class="indentdiv">
				<table class="indentlist">
				</table>
			</div>
		</div>
		<div class="right-page">
			<div class="flowblock">
				<div class="flow-title">
					<label class="flow-title-text">项目流程</label>
				</div>
				<div class="step_context test" data-open-on="hover">
				</div>
					<div class="drop-content" style="display: none;">
						<a href="#" class="drop-a"><img src="/resources/img/flow/point.png"/></a>
						<a href="#" class="drop-a"><img src="/resources/img/flow/point.png"/></a>
						
			            <a href="#" class="description-c"></a>
			            point.png
					</div>
				<div class="descriptiondiv">
					<label class="description-title-text">方案描述：</label><br /> <br />
					<label class="description-text">在该阶段中，拍片网会给用户提供方案。客户可以下载查看。</label>
				</div>
				<div class="flowbtndiv">
					<button class="flowbtn">确认完成，进入下一步</button>
				</div>
			</div>
			<div class="indentinfo">
				<div class="indentinfo-title">
					<label class="indentinfo-title-text">项目详细信息</label>
				</div>
				<div class="indentinfo-table-div">
					<table class="indentinfo-table">
						<tr>
							<td class="indent-title">项目编号</td>
							<td class="indent-content projectId">项目A</td>
							<td class="indent-title">项目名称</td>
							<td class="indent-content projectName">无疆精灵产品宣传片</td>
						</tr>
						<tr>
							<td class="indent-title">客户名称</td>
							<td class="indent-content userName">王石</td>
							<td class="indent-title ">导演名称</td>
							<td class="indent-content teamName">何欣</td>
						</tr>
						<tr>
							<td class="indent-title">客户联系人</td>
							<td class="indent-content userContact">王石</td>
							<td class="indent-title">导演联系人</td>
							<td class="indent-content teamContact">何欣</td>
						</tr>
						<tr>
							<td class="indent-title">客户电话</td>
							<td class="indent-content userPhone">1884547851</td>
							<td class="indent-title">导演电话</td>
							<td class="indent-content teamPhone">1884547851</td>
						</tr>

						<tr>
							<td class="indent-title">影片价格</td>
							<td class="indent-content viedoPrice"></td>
							<td class="indent-title"></td>
							<td class="indent-content"></td>
						</tr>

					</table>
				</div>
			</div>

			<div class="indentfile">
				<div class="indentfile-title">
					<label class="indentfile-title-text">项目文件</label>
					<button class="upload-file-btn">上传文件</button>
					<input type="file" name="addfile" id="addfile" />
				</div>
				<table class="file-table">
				</table>

				<div class="more-file">
					<button class="more-file-btn">更多文件</button>
				</div>
			</div>

			<div class="message-div">
				<div class="message-title">
					<label class="message-title-text">留言板</label>
				</div>
				<div class="message-table-div">
					<textarea class="comment"></textarea>

					<button class="comment-btn">提交</button>
					<table class="message-table">
					</table>
					<button class="more-comment">更多评论</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>