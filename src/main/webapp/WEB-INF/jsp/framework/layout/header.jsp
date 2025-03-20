<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf"%>

<script type="text/javascript">
	function logout() {
		document.logoutForm.submit();
	}
</script>
<div class="header">
	<h1>HEADER</h1>

	<form id="logoutForm" name="logoutForm" action="${basePath}/logout" method="POST">
		<input type="hidden" name="${ _csrf.parameterName }" value="${ _csrf.token }">
		<sec:authorize access="isAnonymous()">
			<a href="${basePath}/login/loginPage">로그인</a>
		</sec:authorize>
		<sec:authorize access="isAuthenticated()">
			<sec:authentication var="user" property="principal" />
		${user.userNm}님
		<a href="javascript:logout();">로그아웃</a>
		</sec:authorize>
	</form>

</div>

<div class="topnav">

	<c:forEach items="${menuList.topMenuList}" var="topMenuList">

		<c:forEach items="${topMenuSeqList}" var="topMenuSeqList">
			<c:if test="${topMenuSeqList eq topMenuList.menuSeq }">
				<c:if test="${topMenuSeqList eq topMenuList.menuSeq and  topMenuList.menuSeq ne '12'}">
					<c:if test="${empty topMenuList.menuUrl }">
					  	<c:set var="andChar" value="?" />
					</c:if>
					<c:if test="${not empty topMenuList.menuUrl }">
					  	<c:set var="andChar" value="&" />
					</c:if>
					<a href="${basePath}${topMenuList.menuUrl}${andChar}topMenuSeq=${topMenuList.menuSeq}">${topMenuList.menuNm}</a>
				</c:if>
			</c:if>
		</c:forEach>
	</c:forEach>

</div>






<%--

<c:set var="loop_flag" value="false" />
	<c:forEach items="${menuList.topMenuList}" var="topMenuList" >

		<c:if test="${not loop_flag }">
		    <c:forEach items="${authMenuList}" var="authMenuList">
		    	<c:if test="${authMenuList.topMenuSeq eq menuList.menuSeq }">
		    			<c:set var="loop_flag" value="true" />
						<a href="${topMenuList.menuUrl}?topMenuSeq=${topMenuList.menuSeq}">${topMenuList.menuNm}</a>
				</c:if>
		    </c:forEach>
		 </c:if>

	</c:forEach>
	--%>