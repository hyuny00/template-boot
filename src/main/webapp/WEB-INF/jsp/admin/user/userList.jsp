<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>


    <script type="text/javaScript" >
        <!--
        /* 글 수정 화면 function */
        function userDetail(userNo) {
        	document.listForm.userNo.value = userNo;
           	document.listForm.action = "/admin/user/userDetail";
           	document.listForm.submit();
        }

        /* 글 등록 화면 function */
        function userForm() {
           	document.listForm.action = "/admin/user/userForm";
           	document.listForm.submit();
        }

        /* 글 목록 화면 function */
        function userList() {
        	document.listForm.pageNo.value = 1;
        	document.listForm.action = "/admin/user/userList";
           	document.listForm.submit();
        }



        /* pagination 페이지 링크 function */
        function goPage(pageNo){
        	document.listForm.pageNo.value = pageNo;
        	document.listForm.action = "${basePath}/admin/user/userList";
           	document.listForm.submit();
        }

        function changePageSize(){
        	document.listForm.pageSize.value=document.listForm.listPageSize.value;
        	document.listForm.action =  "${basePath}/admin/user/userList";
           	document.listForm.submit();
        }

        //-->
    </script>


<form id="listForm" name="listForm" action="/admin/user/userList" method="post">
	<!--  메뉴, 페이징 파라메터-->
	<jsp:include page="/WEB-INF/jsp/framework/_includes/includePageParam.jsp" flush="true" />
	<input type="hidden" name="userNo" value="">
	<!-- // 타이틀 -->
	<div id="search">
		<ul>

			<li>
				사용자 이름
				<input type="text" name="schUserNm" value="${param.schUserNm}" value="" />
			</li>
			<li>
				<span class="btn_blue_l">
					<a href="javascript:userList();">search</a>
				</span>
			</li>
		</ul>
	</div>
	<!-- List -->
	<div id="table">
		<table width="80%" border="0" cellpadding="0" cellspacing="0">
			<colgroup>
				<col width="10%" />
				<col width="20%" />
				<col width="20%" />
				<col width="20%" />
				<col width="20%" />
			</colgroup>
			<tr>
				<th align="center">사용자번호</th>
				<th align="center">아이디</th>
				<th align="center">사용자명</th>
				<th align="center">이메일</th>
				<th align="center">전화번호</th>
			</tr>
			<c:forEach var="result" items="${list}" varStatus="status">
				<tr>
					<td align="center" class="listtd">
						<a href="javascript:userDetail('<c:out value="${result.userNo}"/>')">
							<c:out value="${result.userNo}" />
						</a>
					</td>
					<td align="left" class="listtd">
						<c:out value="${result.userId}" />
						&nbsp;
					</td>
					<td align="center" class="listtd">
						<c:out value="${result.userNm}" />
						&nbsp;
					</td>
					<td align="center" class="listtd">
						<c:out value="${result.userEmail}" />
						&nbsp;
					</td>
					<td align="center" class="listtd">
						<c:out value="${result.telNo}" />
						&nbsp;
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>

	<div></div>
	<!-- /List -->
	<jsp:include page="/WEB-INF/jsp/framework/_includes/paging.jsp" flush="true" />
	<div id="sysbtn">
		<ul>
			<li>
				<span class="btn_blue_l">
					<a href="javascript:userForm();">create</a>
				</span>
			</li>
		</ul>
	</div>
</form>

