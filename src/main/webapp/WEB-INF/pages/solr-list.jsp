<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url value="/resources/css/solr-list.css" var="solrListCss"/>
<spring:url value="/resources/js/solr-list.js" var="solrListJs"/>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
	<jsp:include page="common.jsp" />
	<link rel="stylesheet" href="${solrListCss }">
	<script src="${solrListJs }"></script>
</head>
<body>
	<div class="content-wrap">
		<div class="search-content">
			<input id="solrInput" name="solrContent" type="text" placeholder="键入搜索信息"/>
			<ul id="shelper">
				
			</ul>
		</div>
		价格:<select id="solr-price" name="solrPrice" >
			<option value="[0 TO *]">-- 全部 --</option>
			<option value="[0 TO 20000]">0~2万</option>
			<option value="[20000 TO 100000]">2~10万</option>
			<option value="[100000 TO *]">10以上</option>
		</select>
		时长:<select id="solr-length" name="solrLength" >
			<option value="[0 TO *]">-- 全部 --</option>
			<option value="[0 TO 30]">0~30秒</option>
			<option value="[30 TO 60]">30~60秒</option>
			<option value="[60 TO *]">60秒以上</option>
		</select>
		视频类型:<select id="solr-item" name="solrItem">
			<option value="*">-- 全部 --</option>
		</select>
		<input type="button" value="搜索" id="search-btn">
	</div>
	<div class="detail-content">
		<div id="gride" class="gride-content"></div>
	</div>
</body>
</html>