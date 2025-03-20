<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>

<script type="text/javascript">//<![CDATA[

	$( document ).ready(function() {

		createAuthTree();

	 	$("#new").on("click",function(e) {
	 		 e.preventDefault();

	 		$("#authNm").focus();

	 		 if($("#upAuthSeq").val() ==''){
	 			 alert("상위권한을 선택하세요");
	 			 return;
	 		 }

	 		 if($("#authTypeCd").val() =='030'){
	 			 alert("상위권한을 선택하세요");
	 			 return;
	 		 }


	 		$("#save").show();
	 		$("#delete").hide();
	 		$("#authMenuView").hide();

	 		$("#upAuthNm").val($("#authNm").val());
	    	$("#upAuthSeq").val($("#authSeq").val());


    	 	if($("#upAuthSeq").val() =='0'){
 				$("#new").hide();
 		 	}


	    	if($("#upAuthSeq").val() =='0'){
	    		$("#authTypeCd").val("010").attr("selected", "selected");
	 		 }else{
	 			 if($("#authTypeCd").val()=='010'){
	 				$("#authTypeCd").val("020").attr("selected", "selected");
	 			 }

	 		 }


	    	$("#mode").val("new");

	    	$("#allMenu").hide();
	    	$("#saveAuthMenu").hide();

	    	$("#authForm").show();



	    	 var selected = $('#authTypeCd').val();

	    	 if(selected =='010' || selected =='020'){
	    		$( "#authCd" ).val("");
	 			$( "#authCd" ).prop( "readonly", true );
	 		 }
	    	 if(selected =='030'){
				 $( "#authCd" ).prop( "readonly", false );
	 		 }


   	 		init();

       });


	 	$("#save").on("click",function(e) {


 		 	e.preventDefault();

 			 if($("#upAuthSeq").val() ==''){
	 			 alert("상위권한을 선택하세요");
	 			 return;
	 		 }

	 		if(!confirm("저장하시겠습니까?")) return;

	 		var params = $("#aform").serialize();


	 		$.ajax({
			    url:'/admin/auth/saveAuth',
			    type:'post',
			    contentType:"application/x-www-form-urlencoded; charset=UTF-8",
			    data: params,
			    success: function(data) {
			    	if(data.isSuccess){

			    		$("#authTree").jstree("refresh_node", $("#upAuthSeq").val());
			  		    $("#authTree").jstree("open_node", $("#upAuthSeq").val());

			    	}else{
			    		alert("저장에 실패했습니다.");
			    	}
			    },
			    error: function(err) {
			    }
			});

      });



		 $("#delete").on("click",function(e) {


		 		 e.preventDefault();


		 		if($("#authSeq").val()==''){
		 			alert("삭제할 항목을 선택하세요.");
		 			return;

		 		}

		 		if($("#upAuthSeq").val()==-1){
		 			alert("최상위 항목을 삭제할수 없습니다.");
		 			return;
		 		}


		 		if(!confirm("삭제하시겠습니까?")) return;


		 		var params = $("#aform").serialize();

		 		$.ajax({
				    url:'/admin/auth/deleteAuth',
				    type:'post',
				    contentType:"application/x-www-form-urlencoded; charset=UTF-8",
				    data: params,
				    success: function(data) {
				    	if(data.isSuccess){

				    		$("#authTree").jstree("refresh_node", $("#upAuthSeq").val());
				  		    $("#authTree").jstree("open_node", $("#upAuthSeq").val());


				    	}else{
				    		alert(data.msg);
				    		return;
				    	}
				    },
				    error: function(err) {
				    }
				});

	     });

	 	$("#saveAuthMenu").on("click",function(e) {

		 	e.preventDefault();
    	 	$("#authMenuView").hide();
    	 	saveAuthMenu();

      });



	 	$("#authTypeCd").change(function() {

	 		 var selected = $('#authTypeCd').val();

	 		 if(selected =='010' || selected =='020'){
	 			$( "#authCd" ).val("");
	 			$( "#authCd" ).prop( "readonly", true );
	 		 }
			 if(selected =='030'){
				 $( "#authCd" ).prop( "readonly", false );
	 		 }

	 	});

		$("#authMenuView").on("click",function(e) {


 		 	e.preventDefault();


 		 	$("#allMenu").show();
 			$("#authForm").hide();
	 		$("#save").hide();
	 		$("#delete").hide();
	 		$("#saveAuthMenu").show();
 		 	$("#authMenuView").hide();

     	});


	});






	function createAuthTree() {


		var schUseYn = $("#schUseYn").val();

		$('#authTree').jstree("destroy");

        $('#authTree').jstree({
        	 'core': {

        	      'data': {
	        	        "url" :  "/admin/auth/getAuthList/?lazy",

	       	        	 "data": function (node) {
	       	        	 		 return {"useYn" :schUseYn, "authSeq" : node.id};
	                      }
        	      } ,
        	      "check_callback" : true,
        	    },

        	    "types" : {

    	    	    "030" : {
    	    	      "icon" : "/images/framework/icon_opi.gif",
    	    	      "valid_children" : []
    	    	    }

    	    	  },


        	    "plugins" : [
        	    	"types"
        	    	//   "wholerow"
        	      ]

        });

        $('#authTree').on("loaded.jstree", function (e, data) {
        	 $("#authTree").jstree("open_all");
        });


        $('#authTree').on("changed.jstree", function (e, data) {
        });

        $('#authTree').on('open_node.jstree',function (e, data) {
        	showChildren(data);



        });


        $('#authTree').on("select_node.jstree", function (e, data) {
        	showChildren(data);
        	createAuthMenuTree();
        });




   	 }




	function createAuthMenuTree() {

		$('#menuTree').jstree("destroy");

		 $.ajax({
             async: true,
             type: "POST",
             url: "/admin/menu/getAuthMenuList",
             data : {  "useYn" : "Y", "authSeq" : $("#authSeq").val() },
             dataType: "json",
             success: function (jsondata) {
            	    $('#menuTree').jstree({

            	    	 'core': {
                             'data': jsondata
                         },

            	    	'checkbox' : {
	           	        	keep_selected_style : false,
	            	        three_state : true, // to avoid that fact that checking a node also check others
	            	        // whole_node : false,  // to avoid checking the box just clicking the node
	            	         //tie_selection : false // for checking without selecting and selecting without checking
	            	      },

	            	      "types" : {

            	    	  	"last" : {
	               	    	      "icon" : "/images/filemanager/icon_doc.gif",
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


        $('#menuTree').on('ready.jstree', function (e, data) {
           $("#menuTree").jstree("close_all");
           $("#menuTree").jstree("open_node",0);
        })


        $('#menuTree').on("changed.jstree", function (e, data) {


        });

        $('#menuTree').on("select_node.jstree", function (e, data) {

        });


   	 }


	function saveAuthMenu(){

		if(!confirm("저장하시겠습니까?")) return;

		var authCd=$("#authCd").val();

		if(authCd ==''){
			alert("권한을 선택하세요.");
			return;
		}

		var sendData ={};
		var authSeq=$("#authSeq").val();
		if(authSeq == "") authSeq=-1

		sendData.authSeq = authSeq;
		sendData.menu_seqs= $('#menuTree').jstree(true).get_selected();

		$.ajax({
		    url:'/admin/auth/saveAuthMenu',
		    type:'post',
		    contentType:"application/json; charset=UTF-8",

		    data: JSON.stringify(sendData),

		    success: function(data) {
		    	if(data.isSuccess){
		    		 $("#menuTree").jstree("open_all");
		    		 alert("저장되었습니다.");
		    	}else{
		    		alert("저장에 실패했습니다.");
		    	}
		    },
		    error: function(err) {
		    	alert("저장에 실패했습니다.");
		    }

		});

	}



	function showChildren(data) {

		$("#new").show();
		$("#save").show();
		$("#delete").show();

		$("#mode").val("update");

		$("#authNm").val(data.node.text);

		if(data.node.original.authTypeCd=='030'){
			$("#authCd").val(data.node.original.authCd);
		}else{
			 $('#saveAuthMenu').hide();
		}

    	$("#upAuthNm").val(data.node.original.upAuthNm);
    	$("#upAuthSeq").val(data.node.original.upAuthSeq);
    	$("#authSeq").val(data.node.original.id);


    	$("#useYn").val(data.node.original.useYn).attr("selected", "selected");

    	$("#authTypeCd").val(data.node.original.authTypeCd).attr("selected", "selected");




    	 if(data.node.original.id =='0'){
    		 $("#authMenuView").hide();
    		 $("#delete").hide();
    	 }else{
    		 $("#authMenuView").show();
    		 $("#delete").show();
    	 }



   		 if($('#saveAuthMenu').is(':visible') ){

 			 	$("#save").hide();
 	   			$("#delete").hide();
 	   		 	$("#authMenuView").hide();

   		 }else{
  			 	$("#save").show();
  	   			$("#delete").show();
  	   		 	$("#authMenuView").show();
   		 }

   		if($('#allMenu').is(':visible') ){
   			$("#save").hide();
   		}else{
   			$("#save").show();
   		}



    	 var selected = $('#authTypeCd').val();

    	 if(selected =='010' || selected =='020'){
    		$( "#authCd" ).val("");
 			$( "#authCd" ).prop( "readonly", true );
 		 }
    	 if(selected =='030'){
			 $( "#authCd" ).prop( "readonly", false );
 		 }

	}


	function init() {

		$("#authNm").val('');
    	$("#authCd").val('');
    	$("#authSeq").val('');

    	$("#useYn option:eq(0)").attr("selected", "selected");
	}


	//]]>
</script>



<div class="rightcolumn">
	<form name="aform" id="aform">
		<input type="hidden" name="upAuthSeq" id="upAuthSeq">
		<input type="hidden" name="authSeq" id="authSeq" value="-1">
		<input type="hidden" name="mode" id="mode">
		<jsp:include page="/WEB-INF/jsp/framework/_includes/includePageParam.jsp" flush="true" />
		<div class="contents">

			<div id="auth" style="float: left; width: 40%;">
				권한 사용여부
				<select id="schUseYn" name="schUseYn" onChange="javascript:createAuthTree();">
				    <option value="">전체</option>
					<option value="Y" selected>Yes</option>
					<option value="N">No</option>
				</select>

				<div id="authTree" style="border: 2px solid black; width: 100%; height: 315px; overflow-x: visible; overflow-y: scroll;"></div>
			</div>

			<div id="authDetail" style="float: left">
				<div id="authForm">
					<table border="1" cellpadding="0" cellspacing="0">
						<colgroup>
							<col width="30%" />
							<col width="?" />
						</colgroup>

						<tr>
							<td>상위권한</td>
							<td>
								<input type="text" id="upAuthNm" name="upAuthNm" value="" readonly maxlength="100" />
							</td>
						</tr>

						<tr>
							<td>메뉴구분</td>
							<td>
								<select id="authTypeCd" name="authTypeCd">
									<option value="010">시스템</option>
									<option value="020">폴더</option>
									<option value="030">권한</option>
								</select>

							</td>
						</tr>

						<tr>
							<td>권한명</td>
							<td>
								<input type="text" id="authNm" name="authNm" value="" maxlength="100" />
							</td>
						</tr>
						<tr>
							<td>권한코드</td>
							<td>
								<input type="text" id="authCd" name="authCd" value="" maxlength="100" />
							</td>
						</tr>


						<tr>
							<td>사용여부</td>
							<td>
								<select id="useYn" name="useYn">
									<option value="Y">Yes</option>
									<option value="N">No</option>
								</select>
							</td>
						</tr>

					</table>
				</div>

				<div>
					<button id="new">신규</button>
					<button id="save" style="display: none">권한저장</button>
					<button id="delete" style="display: none">삭제</button>
					<button id="authMenuView">메뉴권한지정</button>
					<button id="saveAuthMenu" style="display: none">권한별 메뉴저장</button>
				</div>

				<div id="allMenu" style="display: none;">
					메뉴 선택
					<div id="menuTree" style="border: 2px solid black; width: 100%; height: 400px; overflow-x: visible; overflow-y: scroll;"></div>
				</div>

			</div>



		</div>

	</form>
	<!--   <button id="sam">create node</button>-->
</div>
