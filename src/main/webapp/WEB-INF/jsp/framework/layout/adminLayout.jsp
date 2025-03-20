<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<%@ include file="/WEB-INF/jsp/framework/_includes/includeCss.jspf" %>
	<link href="${basePath}/css/test.css" rel="stylesheet" />
	<link href="${basePath}/js/jquery/jquery-ui-1.12.1/jquery-ui.min.css" rel="stylesheet" />
	<link href="${basePath}/js/select2-4.0.13/dist/css/select2.min.css" rel="stylesheet" />
	<link href="${basePath}/js/jstree/themes/default/style.min.css" rel="stylesheet"/>
	<link href="data:image/x-icon;," type="image/x-icon" rel="shortcut icon">
	<%@ include file="/WEB-INF/jsp/framework/_includes/includeScript.jspf" %>
</head>
<body>
	<tiles:insertAttribute name="header"/>
	<div class="row">
		<tiles:insertAttribute name="leftMenu"/>
		<tiles:insertAttribute name="body"/>
	</div>


	<tiles:insertAttribute name="footer"/>
</body>
</html>



