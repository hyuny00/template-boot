<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>사용자 목록</title>
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/sample.css'/>"/>
    <script type="text/javaScript" language="javascript" defer="defer">
        <!--
        
       
        
        function saveUser(){
        	document.aform.action="/user/saveUser";
        	document.aform.submit();
        }
      
        //-->
    </script>
</head>


<body style="text-align:center; margin:0 auto; display:inline; padding-top:100px;">
   <div id="content_pop">
		   <form id="listForm" name="aform" method="post">
		   
		   사용자 id <input type="text" name="userId" value="admin">
		   사용자 이름 <input type="text" name="userNm">
		  비밀번호 <input type="text" name="password" value="edmsUser"> 
		  이메일<input type="text" name="userEmail"> 
		  
		   <div>
	       		 <a href="javascript:saveUser()">저장</a>
	        </div>
		   
		   </form>
		   
	 </div>
 </body>