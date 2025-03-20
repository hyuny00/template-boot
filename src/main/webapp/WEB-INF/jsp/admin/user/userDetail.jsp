<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>



    <script type="text/javaScript" >



	$( document ).ready(function() {
		createUserAuthTree();
	});






	function createUserAuthTree() {

		var userNo=$("#userNo").val();


		 $.ajax({
             async: true,
             type: "POST",
             url: "/admin/user/getUserAuthList",
             data : {  "useYn" : "Y", "userNo" : userNo },
             dataType: "json",
             success: function (jsondata) {
            	    $('#userAuthTree').jstree({

            	    	 'core': {
                             'data': jsondata
                         },

            	    	'checkbox' : {
	           	        	keep_selected_style : false,
	            	        three_state : true, // to avoid that fact that checking a node also check others
	            	         //whole_node : false,  // to avoid checking the box just clicking the node
	            	         //tie_selection : false // for checking without selecting and selecting without checking
	            	      },

	            	      "types" : {

	          	    	    "030" : {
	          	    	      "icon" : "/images/framework/icon_doc.gif",
	          	    	      "valid_children" : []
	          	    	    }

	          	    	  },

	            	      "plugins" : [
	              	    	 "checkbox", "types" ,"crrm"
	              	      ]

            	    });
             },

             error: function (xhr, ajaxOptions, thrownError) {
            	  if (xhr.status == 401 || xhr.status == 403) {
                  	alert("로그인을 하셔야 합니다.");
                      location.replace('/login/loginPage');
                  } else {
                      alert("예외가 발생했습니다. 관리자에게 문의하세요.");
                  }

             }
         });





        $('#userAuthTree').on('ready.jstree', function (e, data) {
        	 $("#userAuthTree").jstree("open_all");
        })


        $('#userAuthTree').on("changed.jstree", function (e, data) {

        });

        $('#userAuthTree').on("select_node.jstree", function (e, data) {

        });

   	 }




        function userList() {
           	document.detailForm.action = "/admin/user/userList";
           	document.detailForm.submit();
        }

        function deleteUser() {
           	document.detailForm.action = "/admin/user/deleteUser";
           	document.detailForm.submit();
        }

        function saveUser() {

        	if($("#userPwd").val()==''){

        		alert("비밀번호를 입력하세요");
        		return;
        	}


        	frm = document.detailForm;
        	var authSeqs= $('#userAuthTree').jstree(true).get_selected();
        	$("#authSeqs").val(authSeqs);

        	frm.action =  "/admin/user/saveUser";
            frm.submit();

        }

        function updateUser() {

        	if($("#userPwd").val()==''){
        		alert("비밀번호를 입력하세요");
        		return;
        	}


        	frm = document.detailForm;

        	var authSeqs= $('#userAuthTree').jstree(true).get_selected();


        	$("#authSeqs").val(authSeqs);

        	frm.action =  "/admin/user/updateUser";
            frm.submit();

        }


    </script>

<div class="rightcolumn">
	<form id="detailForm" method="post" name="detailForm"  >
		<!-- 검색조건, 메뉴, 페이징 파라메터-->
		<jsp:include page="/WEB-INF/jsp/framework/_includes/includePageParam.jsp" flush="true" />
		<jsp:include page="/WEB-INF/jsp/framework/_includes/includeSchParam.jsp" flush="true" />
		<div id="content_pop">
			<!-- 타이틀 -->
			<div id="title"></div>
			<!-- // 타이틀 -->
			<div id="table">
				<table width="70%" border="1" cellpadding="0" cellspacing="0" style="bordercolor: #D3E2EC; bordercolordark: #FFFFFF; BORDER-TOP: #C2D0DB 2px solid; BORDER-LEFT: #ffffff 1px solid; BORDER-RIGHT: #ffffff 1px solid; BORDER-BOTTOM: #C2D0DB 1px solid; border-collapse: collapse;">
					<colgroup>
						<col width="150" />
						<col width="?" />
					</colgroup>
					<tr>
						<td class="tbtd_content">ID</td>
						<td class="tbtd_content">
							<input type="text" name="userId" value="${result.userId}" cssClass="essentiality" maxlength="10" />
						</td>
					</tr>
					<tr>
						<td class="tbtd_content">NAME</td>
						<td class="tbtd_content">
							<input type="text" name="userNm" value="${result.userNm}" maxlength="30" cssClass="txt" />
						</td>
					</tr>

					<tr>
						<td class="tbtd_caption">email</td>
						<td class="tbtd_content">
							<input type="text" name="userEmail" id="userEmail" value="${result.userEmail}" maxlength="30" cssClass="txt" />
						</td>
					</tr>
					<tr>
						<td class="tbtd_caption">telNo</td>
						<td class="tbtd_content">
							<input type="text" name="officeTelNo" value="${result.officeTelNo}" maxlength="30" cssClass="txt" />
						</td>
					</tr>

					<tr>
						<td class="tbtd_caption">pwd</td>
						<td class="tbtd_content">
							<input type="text" id="userPwd" name="userPwd" value="" maxlength="30" cssClass="txt" />
						</td>
					</tr>

				</table>
			</div>


			<div id="userAuthTree"></div>



			<div id="sysbtn">
				<ul>
					<li>
						<span class="btn_blue_l">
							<a href="javascript:userList();"></a>
							<img src="<c:url value='/images/egovframework/example/btn_bg_r.gif'/>" style="margin-left: 6px;" alt="" />
						</span>
					</li>
					<li>
						<span class="btn_blue_l">
							<a href="javascript:saveUser();"> create </a>
						</span>
					</li>

					<li>
						<span class="btn_blue_l">
							<a href="javascript:updateUser();"> update </a>
						</span>
					</li>
					<li>
						<span class="btn_blue_l">
							<a href="javascript:deleteUser();">delete</a>
						</span>
					</li>

					<li>
						<span class="btn_blue_l">
							<a href="javascript:userList();">list</a>
						</span>
					</li>
					<li>
						<span class="btn_blue_l">
							<a href="javascript:document.detailForm.reset();">reset</a>
						</span>
					</li>
				</ul>
			</div>
		</div>
		<input type="hidden" name="userNo" id="userNo" value="${result.userNo}" />
		<input type="hidden" name="authSeqs" id="authSeqs" value="" />
		<input type="hidden" name="pageNo" value="${param.pageNo}" />
	</form>
</div>
