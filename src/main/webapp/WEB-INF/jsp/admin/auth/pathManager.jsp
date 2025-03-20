<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>

<script type="text/javascript">//<![CDATA[




	$( document ).ready(function() {

		createPathTree();



		 $("#up").on("click",function(e) {
			 e.preventDefault();
		  	var selected = $('#sortOrd option:selected');
		    if(selected[0].previousElementSibling == null) return;
		    $(selected).each(function(index, obj){
		          $(obj).insertBefore($(obj).prev());
		    });

		    updateOrd();
       });


	 	$("#down").on("click",function(e) {
	 		 e.preventDefault();
			var selected = $('#sortOrd option:selected');
	       	if(selected.last().next().length == 0) return;
	      	 $(selected.get().reverse()).each(function(index, obj){
				$(obj).insertAfter($(obj).next());
	       	});

	      	updateOrd();
       });




	 	$("#new").on("click",function(e) {
	 		 e.preventDefault();

	 		$("#path").focus();

	 		if($("#upPathSeq").val() !='-1'){
	 			 alert("상위경로를 선택하세요");
	 			 return;
	 		 }


	 		$("#save").show();
	 		$("#delete").hide();
	 		$("#pathAuthView").hide();

	 		$("#upPath").val($("#path").val());

	    	$("#upPathSeq").val($("#pathSeq").val());


	    	$("#mode").val("new");

	    	$("#allAuth").hide();
	    	$("#savePathAuth").hide();


	    	$("#pathForm").show();

	    	 init();

       });


	 	$("#save").on("click",function(e) {


	 		 e.preventDefault();

	 		if(!confirm("저장하시겠습니까?")) return;

	 		var params = $("#aform").serialize();

	 		$.ajax({
			    url:'/admin/auth/savePath',
			    type:'post',
			    contentType:"application/x-www-form-urlencoded; charset=UTF-8",
			    data: params,
			    success: function(data) {
			    	if(data.isSuccess){

			    		$("#pathTree").jstree("refresh_node", $("#upPathSeq").val());
			  		    $("#pathTree").jstree("open_node", $("#upPathSeq").val());


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



	 		if($("#pathSeq").val()==''){
	 			alert("삭제할 항목을 선택하세요.");
	 			return;

	 		}

	 		if($("#upPathSeq").val()==-1){
	 			alert("최상위 항목을 삭제할수 없습니다.");
	 			return;
	 		}


	 		if(!confirm("삭제하시겠습니까?")) return;

	 		var params = $("#aform").serialize();

	 		$.ajax({
			    url:'/admin/auth/deletePath',
			    type:'post',
			    contentType:"application/x-www-form-urlencoded; charset=UTF-8",
			    data: params,
			    success: function(data) {
			    	if(data.isSuccess){

			    		$("#pathTree").jstree("refresh_node", $("#upPathSeq").val());
			  		    $("#pathTree").jstree("open_node", $("#upPathSeq").val());


			    	}else{
			    		alert(data.msg);
			    		return;
			    	}
			    },
			    error: function(err) {
			    }
			});

     });




	 	$("#savePathAuth").on("click",function(e) {


		 	e.preventDefault();

    	 	$("#pathAuthView").hide();

    	 	savePathAuth();




      });



		$("#pathAuthView").on("click",function(e) {


 		 	e.preventDefault();


 		 	$("#allAuth").show();
 			$("#pathForm").hide();
	 		$("#save").hide();
	 		$("#delete").hide();
	 		$("#savePathAuth").show();
 		 	$("#pathAuthView").hide();

     	});


	});




	function updateOrd(){


		var pathOrd = [];
		$('#sortOrd option').each(function(){
			pathOrd.push($(this).val());
		});

		$.ajax({
		    url:'/admin/auth/updatePathOrd',
		    type:'post',
		    contentType:"application/x-www-form-urlencoded; charset=UTF-8",
		    data:{path_seqs : pathOrd},
		    success: function(data) {
		    	if(data.isSuccess){
	    		  	$("#pathTree").jstree("refresh_node", $("#pathSeq").val());
		  		    $("#pathTree").jstree("open_node", $("#pathSeq").val());
		    	}
		    },
		    error: function(err) {
		    }
		});

	}




	function createPathTree() {
        $('#pathTree').jstree({
        	 'core': {

        	      'data': {
	        	        "url" :  "/admin/auth/getPathList/?lazy",

	       	        	 "data": function (node) {
	       	        	 		 return { "pathSeq" : node.id};
	                      }
        	      } ,
        	      "check_callback" : true,
        	    },

        	    "types" : {

    	    	    "030" : {
    	    	      "icon" : "/images/framework/icon_doc.gif",
    	    	      "valid_children" : []
    	    	    }

    	    	  },


        	    "plugins" : [
        	    	"types"
        	    	//   "wholerow"
        	      ]

        });


        $('#pathTree').on("changed.jstree", function (e, data) {

        });

        $('#pathTree').on('open_node.jstree',function (e, data) {
        	showChildren(data);
        });


        $('#pathTree').on("select_node.jstree", function (e, data) {
        	showChildren(data);

        	createPathAuthTree();
        });


   	 }




	function showChildren(data) {


		$("#save").show();
		$("#delete").show();
		$("#mode").val("update");

		$("#path").val(data.node.text);
		$("#pathNm").val(data.node.original.pathNm);


    	$("#upPathSeq").val(data.node.original.upPathSeq);
    	$("#pathSeq").val(data.node.original.id);
    	$("#upPath").val(data.node.original.upPath);


    	 if(data.node.original.id =='0'){
    		 $("#pathAuthView").hide();
    		 $("#delete").hide();
    	 }else{
    		 $("#pathAuthView").show();
    		 $("#delete").show();
    	 }



   		 if($('#savePathAuth').is(':visible') ){
   			 $("#save").hide();
   			$("#delete").hide();
   		 	$("#pathAuthView").hide();
   		 }else{
   			 $("#save").show();
   			$("#delete").show();
   		 	$("#pathAuthView").show();
   		 }



   		var node = $("#pathTree").jstree("get_node",data.node.id);

   		var selected = $('#sortOrd option:selected');
       	$('#sortOrd').children('option').remove();
       	for (var i = 0; i < node.children.length; i++) {
       		var temp_node = $("#pathTree").jstree("get_node",node.children[i]);
            	 $('#sortOrd').append($('<option/>', {
                    value: temp_node.original.id,
                    text : temp_node.original.text
                }));

        }
       	$("#sortOrd").val(selected.val());


	}


	function init() {
		$("#path").val('');
		$("#pathNm").val('');
    	$("#pathSeq").val('');
	}



	function createPathAuthTree() {

		$('#authTree').jstree("destroy");

		var pathSeq=$("#pathSeq").val();
 		if(pathSeq == "") pathSeq=-1

 		 $.ajax({
             async: true,
             type: "POST",
             url: "/admin/auth/getPathAuthList",
             data : {  "useYn" : "Y",  "pathSeq" : pathSeq },
             dataType: "json",
             success: function (jsondata) {
            	    $('#authTree').jstree({

            	    	 'core': {
                             'data': jsondata
                         },

            	    	'checkbox' : {
	           	        	keep_selected_style : true,
	            	        three_state : true, // to avoid that fact that checking a node also check others
	            	         //whole_node : false,  // to avoid checking the box just clicking the node
	            	         //tie_selection : false // for checking without selecting and selecting without checking
	            	      },

	            	      "types" : {

	            	    	  "030" : {
	              	    	      "icon" : "/images/framework/icon_opi.gif",
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




        $('#authTree').on('ready.jstree', function (e, data) {
        	 $("#authTree").jstree("open_all");
        })


        $('#authTree').on("changed.jstree", function (e, data) {

        });

        $('#authTree').on("select_node.jstree", function (e, data) {

        });



   	 }

	function savePathAuth(){

		if(!confirm("저장하시겠습니까?")) return;



		var sendData ={};
		var pathSeq=$("#pathSeq").val();
		if(pathSeq == "") pathSeq=-1

		sendData.pathSeq = pathSeq;
		sendData.auth_seqs= $('#authTree').jstree(true).get_selected();

		$.ajax({
		    url:'/admin/auth/savePathAuth',
		    type:'post',
		    contentType:"application/json; charset=UTF-8",

		    data: JSON.stringify(sendData),

		    success: function(data) {
		    	if(data.isSuccess){
		    		 $("#authTree").jstree("open_all");
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


	//]]>
</script>

<div class="rightcolumn">
	<form name="aform" id="aform">
		<input type="hidden" name="upPathSeq" id="upPathSeq">
		<input type="hidden" name="pathSeq" id="pathSeq" value="-1">
		<input type="hidden" name="mode" id="mode">
		<jsp:include page="/WEB-INF/jsp/framework/_includes/includePageParam.jsp" flush="true" />
		<div class="contents">

			<div style="float: left; width: 40%;">
				PATH
				<div id="pathTree" style="border: 2px solid black; width: 100%; height: 315px; overflow-x: visible; overflow-y: scroll;"></div>
			</div>

			<div id="pathDetail" style="float: left">
				<div id="pathForm">
					<table border="1" cellpadding="0" cellspacing="0">
						<colgroup>
							<col width="30%" />
							<col width="70%" />
						</colgroup>



						<tr>
							<td>상위경로</td>
							<td>
								<input type="text" id="upPath" name="upPath" value="" maxlength="100" />
							</td>
						</tr>


						<tr>
							<td>경로</td>
							<td>
								<input type="text" id="path" name="path" value="" maxlength="100" />
							</td>
						</tr>
						<tr>
							<td>경로명</td>
							<td>
								<input type="text" id="pathNm" name="pathNm" value="" maxlength="100" />
							</td>
						</tr>

						<tr>
							<td>정렬순서</td>
							<td>
								<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td width="90%">
											<select id="sortOrd" name="sortOrd" multiple style="width: 100%; height: 400px;">

											</select>
										</td>
										<td width="10%" align="center">
											<button id="up">up</button>
											<br />
											<br /> <br />
											<button id="down">down</button>
										</td>
									</tr>
								</table>
							</td>
						</tr>


					</table>
				</div>

				<div>
					<button id="new">신규</button>
					<button id="save" style="display: none">저장</button>
					<button id="delete" style="display: none">삭제</button>
					<button id="pathAuthView">경로별 권한지정</button>
					<button id="savePathAuth" style="display: none">경로별 권한저장</button>
				</div>

				<div id="allAuth" style="display: none;">
					<div id="authTree"></div>
				</div>

			</div>



		</div>

	</form>
	<!--   <button id="sam">create node</button>-->
</div>
