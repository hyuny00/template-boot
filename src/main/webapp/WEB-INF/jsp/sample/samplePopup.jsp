<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>

<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeCss.jspf" %>

<script type="text/javascript">

	function fn_test(){
		alert("안녕하세요");
	}

</script>

<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>


		<div id="aa">
               contents : ${contents}
      </div>

  		<a href="javascript:popupClose();">close</a>
    	<a href="javascript:fn_test();">test</a>
		<a href="javascript:test1();">test1</a>



</body>
</html>