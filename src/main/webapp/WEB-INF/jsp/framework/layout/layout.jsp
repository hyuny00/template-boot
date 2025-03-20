<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<%@ include file="/WEB-INF/jsp/framework/_includes/includeCss.jspf" %>
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



