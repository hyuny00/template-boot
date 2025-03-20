<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>



<script type="text/javascript">//<![CDATA[


	$( document ).ready(function() {
		 createCodeTree();


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

	 		$("#dtlCdNm").focus();
	 		 if(  $("#upCdSeq").val() ==''){
	 			 alert("상위메뉴를 선택하세요");
	 			 return;
	 		 }

	 		$("#save").show();
	 		$("#delete").hide();

	 		$("#upDtlCdNm").val($("#dtlCdNm").val());
	    	$("#upCdSeq").val($("#cdSeq").val());

	    	$("#upCdDiv").val($("#cdDiv").val());
	    	$("#cdDiv").val("");

	    	$("#mode").val("new");

	    	 init();

       });


	 	$("#save").on("click",function(e) {


	 		 e.preventDefault();

	 		if($("#upCdSeq").val() =='0'){
	 			alert("상위코드를 선택하세요");
	 			return;
	 		}

	 		if($("#dtlCd") ==''){
	 			alert("코드를 입력하세요");
	 			return;
	 		}

	 		if($("#dtlCdNm") ==''){
	 			alert("코드명을 입력하세요");
	 			return;
	 		}


	 		if(!confirm("저장하시겠습니까?")) return;

	 		var params = $("#aform").serialize();


	 		$.ajax({
			    url:'/admin/code/saveCode',
			    type:'post',
			    contentType:"application/x-www-form-urlencoded; charset=UTF-8",
			    data: params,
			    success: function(data) {
			    	if(data.isSuccess){

		    		  	$("#codeTree").jstree("refresh_node", $("#upCdSeq").val());
			  		    $("#codeTree").jstree("open_node", $("#upCdSeq").val());
			    	}
			    },
			    error: function(err) {
			    }
			});

      });


	 	$("#delete").on("click",function(e) {

	 		e.preventDefault();


	 		if($("#cdSeq").val()==''){
	 			alert("삭제할 항목을 선택하세요.");
	 			return;

	 		}

	 		if($("#upCdSeq").val()=='0'){
	 			alert("최상위 항목을 삭제할수 없습니다.");
	 			return;
	 		}

	 		if(!confirm("삭제하시겠습니까?")) return;

	 		var params = $("#aform").serialize();

	 		$.ajax({
			    url:'/admin/code/deleteCode',
			    type:'post',
			    contentType:"application/x-www-form-urlencoded; charset=UTF-8",
			    data: params,
			    success: function(data) {
			    	if(data.isSuccess){

		    		  	$("#codeTree").jstree("refresh_node", $("#upCdSeq").val());
			  		    $("#codeTree").jstree("open_node", $("#upCdSeq").val());
			    	}else{
			    		alert(data.msg);
			    		return;
			    	}
			    },
			    error: function(err) {


			    }
			});


      });



	});


	function updateOrd(){


		var codeOrd = [];
		$('#sortOrd option').each(function(){
			codeOrd.push($(this).val());
		});



		$.ajax({
		    url:'/admin/code/updateCodeOrd',
		    type:'post',
		    contentType:"application/x-www-form-urlencoded; charset=UTF-8",
		    data:{code_seqs : codeOrd},
		    success: function(data) {
		    	if(data.isSuccess){
	    		  	$("#codeTree").jstree("refresh_node", $("#cdSeq").val());
		  		    $("#codeTree").jstree("open_node", $("#cdSeq").val());
		    	}
		    },
		    error: function(err) {
		    }
		});

	}



	function createCodeTree() {
        $('#codeTree').jstree({
        	 'core': {

        	      'data': {
	        	        "url" :  "/admin/code/getCodeList/?lazy",


	       	        	 "data": function (node) {
	       	        	 		 return { "cdSeq" : node.id, "useYn":"Y"};
	                      }
        	      } ,
        	      "check_callback" : true,
        	    },

  				"types" : {

    	    	    "last" : {
    	    	      "icon" : "/images/filemanager/icon_doc.gif",
    	    	      "valid_children" : []
    	    	    }

    	    	  },

        	    "plugins" : [
        	    	"types"
        	      ]

        });


        // Listen for events - example

        $('#codeTree').on("changed.jstree", function (e, data) {
        });

        $('#codeTree').on('open_node.jstree',function (e, data) {

        	showChildren(data)
        });


        $('#codeTree').on("select_node.jstree", function (e, data) {



        	showChildren(data)
        });


   	 }




	function showChildren(data) {


		$("#save").show();
		$("#delete").show();
		$("#mode").val("update");

		$("#dtlCdNm").val(data.node.original.dtlCdNm);

    	$("#upDtlCdNm").val(data.node.original.upDtlCdNm);

    	$("#shotDtlCdNm").val(data.node.original.shotDtlCdNm);


    	$("#upCdSeq").val(data.node.original.upCdSeq);
    	$("#cdSeq").val(data.node.original.id);

    	$("#upCdDiv").val(data.node.original.upCdDiv);
    	$("#cdDiv").val(data.node.original.cdDiv);
    	$("#dtlCd").val(data.node.original.dtlCd);



    	$("#rmk").val(data.node.original.rmk);

    	$("#useYn").val(data.node.original.useYn).attr("selected", "selected");
    	//$("#openYn").val(data.node.original.openYn).attr("selected", "selected");



    	 if(data.node.original.id =='0'){
    		 $("#save").hide();
    		 $("#delete").hide();
    	 }else{
    		 $("#save").show();
    		 $("#delete").show();
    	 }


		var node = $("#codeTree").jstree("get_node",data.node.id);

		var selected = $('#sortOrd option:selected');
    	$('#sortOrd').children('option').remove();
    	for (var i = 0; i < node.children.length; i++) {

    		var temp_node = $("#codeTree").jstree("get_node",node.children[i]);

         	 $('#sortOrd').append($('<option/>', {
                 value: temp_node.original.id,
                 text : temp_node.original.text
             }));
        }

    	$("#sortOrd").val(selected.val());
	}

	function init() {

		$("#dtlCdNm").val('');
    	$("#cdSeq").val(0);



    	$("#dtlCd").val('');
    	$("#shotDtlCdNm").val('');

    	$("#rmk").val('');

    	$("#useYn option:eq(0)").attr("selected", "selected");

	}





	//]]>
</script>


<div class="rightcolumn">
<form name="aform" id="aform">
	<jsp:include page="/WEB-INF/jsp/framework/_includes/includePageParam.jsp" flush="true"/>
	<input type="hidden" name="codeOrd" id="codeOrd">
	<input type="hidden" name="mode" id="mode" value="new">

	<div class="contents" >
		   <div id="codeTree" style="float:left; width: 40%; height: 500px; overflow-x: visible; overflow-y: scroll;">

		   </div>

		   <div id="codeDetail" style="float:left">
				<table border="1" cellpadding="0" cellspacing="0">
			    		<colgroup>
			    			<col width="30%"/>
			    			<col width="?"/>
			    		</colgroup>

			    			<tr>
			        			<td>
			        				상위 코드명
			        			</td>
			        			<td >
			        				<input type="text" id="upDtlCdNm" name="upDtlCdNm" value=""  readonly  />
			        			</td>
			        		</tr>

			        		<tr>
			        			<td >
			        				상위 코드일련번호
			        			</td>
			        			<td >
			        				<input type="text" id="upCdSeq" name="upCdSeq" value=""  readonly  />
			        			</td>
			        		</tr>

			        		<tr>
			        			<td >
			        				 코드일련번호
			        			</td>
			        			<td >
			        				<input type="text" id="cdSeq" name="cdSeq" value=""  readonly  />
			        			</td>
			        		</tr>

			        		<tr>
			        			<td >
			        				 코드명
			        			</td>
			        			<td >
			        				<input type="text" id="dtlCdNm" name="dtlCdNm" value=""  maxlength="50"   />
			        			</td>
			        		</tr>

			        		<tr>
			        			<td >
			        				단축 코드명
			        			</td>
			        			<td >
			        				<input type="text" id="shotDtlCdNm" name="shotDtlCdNm" value=""  maxlength="25"   />
			        			</td>
			        		</tr>



			        		<tr>
			        			<td >
			        				 코드
			        			</td>
			        			<td >
			        				<input type="text" id="dtlCd" name="dtlCd" value=""  maxlength="3"   />
			        			</td>
			        		</tr>


				    		<tr>
			    				<td>
			        				사용여부
			        			</td>
				    			<td >
				    				<select id="useYn" name="useYn" >
				    					<option value="Y" >Yes</option>
				    					<option value="N">No</option>
				    				</select>
				    			</td>
				    		</tr>

				    		<tr>
			    				<td>
			        				정렬순서
			        			</td>
				    			<td >
				    				<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td width="90%">
												<select id="sortOrd" name="sortOrd"  multiple style="width:100%; height:82px;">

												</select>
											</td>
											<td width="10%" align="center">
												<button id="up">up</button>
												<br/><br/> <br/>
												<button id="down">down</button>
											</td>
										</tr>
									</table>
				    			</td>
				    		</tr>



				    		<tr>
			    				<td>
			        				비고
			        			</td>
				    			<td>
				    				<textarea name="rmk" id="rmk"  rows="5" cols="58" ></textarea>
				    			</td>
				    		</tr>


				    		<tr>
			        			<td>
			        				이전 상위 코드
			        			</td>
			        			<td >
			        				<input type="text" id="upCdDiv" name="upCdDiv" value=""  readonly  />
			        			</td>
			        		</tr>

			        		<tr>
			        			<td >
			        				이전 코드
			        			</td>
			        			<td >
			        				<input type="text" id="cdDiv" name="cdDiv" value=""  readonly  />
			        			</td>
			        		</tr>

			    	</table>
			    	<div id="code_button" style="display:block">
				    	 <button id="new">new</button>
				    	 <button id="delete" >delete</button>
				    	 <button id="save" style="display:none">save</button>
			    	 </div>
		   </div>

	 </div>

</form>
  <!--   <button id="sam">create node</button>-->
</div>
