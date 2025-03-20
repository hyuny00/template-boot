<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>

<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeCss.jspf" %>



<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>



<c:forEach var="result" items="${list}" varStatus="status">
			<a href="javascript:createOrgTree('<c:out value="${result.dn}"/>','<c:out value="${result.type}"/>', 'N')"><c:out value="${result.ou}"/></a> <br>
</c:forEach>


		
</body>
</html>