<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>

    <script type="text/javaScript">
	    function goPage(pageNo){
	    	var formName= $("#paging").closest('form').attr('name');
	    	document[formName].pageNo.value = pageNo;
	       	document[formName].submit();
	    }

	    function changePageSize(){
	    	var formName= $("#paging").closest('form').attr('name');

	    	document[formName].pageSize.value=document[formName].listPageSize.value;
	    	document[formName].pageNo.value = 1;
	       	document[formName].submit();
	    }
    </script>

<div id="paging" align="center">
		<c:if test="${pageObject.prev}">
			<a href="javascript:goPage(1)">first</a>
		    <a href="javascript:goPage(${pageObject.beginPage-1})">prev</a>
		</c:if>
		<c:forEach begin="${pageObject.beginPage}" end="${pageObject.endPage}" step="1" var="index">
		    <c:choose>
		        <c:when test="${pageObject.pageNo==index}">
		            ${index}
		        </c:when>
		        <c:otherwise>
		            <a href="javascript:goPage(${index})">${index}</a>
		        </c:otherwise>
		    </c:choose>
		</c:forEach>
		<c:if test="${pageObject.next}">
		    <a href="javascript:goPage(${pageObject.endPage+1})">next</a>
		    <a href="javascript:goPage(${pageObject.totalPage})">last</a>
		</c:if>

		<select name="listPageSize" id="listPageSize" onChange="javascript:changePageSize()">
			<option value="10" <c:if test="${pageObject.pageSize eq '10'}">selected</c:if> >10</option>
			<option value="20" <c:if test="${pageObject.pageSize eq '20'}">selected</c:if>>20</option>
			<option value="30" <c:if test="${pageObject.pageSize eq '30'}">selected</c:if>>30</option>
		</select>
		${pageObject.pageNo}/${pageObject.totalPage} 총${pageObject.totalCount}건
</div>
