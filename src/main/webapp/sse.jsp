<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>
	<%@ include file="/WEB-INF/jsp/framework/_includes/includeScript.jspf" %>

    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<input id="input"/>
<button id="send">send</button>
<pre id="messages"></pre>
<script>



    const eventSource = new EventSource("/api/subscribe");
	//const eventSource = new EventSource("/api/test");

   	eventSource.onmessage = function(e) {
	   console.log(e);
	};

	eventSource.onerror = function(e) {
	   console.log(e);
	};

	eventSource.onmessage = function(e) {
	    document.querySelector("#messages").appendChild(document.createTextNode(e.data + "\n"));
	};
/*
    document.querySelector("#send").addEventListener("click", () => {
        fetch("/api/publish?message="+document.querySelector("#input").value);
    });
    */
</script>
</body>
</html>


