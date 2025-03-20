<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>



<script type="text/javascript">//<![CDATA[


	$( document ).ready(function() {
		 createMenuTree();


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


	 		$("#menuNm").focus();

	 		 var menuTypeCd=$("#menuTypeCd").val();

	 		 if( $("#upMenuSeq").val() =='' || menuTypeCd =='030'  ){
	 			 alert("상위메뉴를 선택하세요");
	 			 return;
	 		 }

	 		$("#save").show();
	 		$("#delete").hide();

	 		$("#upMenuNm").val($("#menuNm").val());
	    	$("#upMenuSeq").val($("#menuSeq").val());




	    	if($("#upMenuSeq").val() =='0'){
	    		$("#menuTypeCd").val("010").attr("selected", "selected");
	 		 }else{



	 			 if(menuTypeCd=='010'){
	 				$("#menuTypeCd").val("020").attr("selected", "selected");
	 			 }

	 			if(menuTypeCd=='020'){
	 				$("#menuTypeCd").val("030").attr("selected", "selected");
	 				//$( "#menuUrl" ).prop( "readonly", false );
	 			 }

	 		 }




	    	$("#mode").val("new");

	    	 init();

       });


	 	$("#save").on("click",function(e) {


	 		 e.preventDefault();

	 		if( $("#upMenuNm").val() =='' ){
	 			 alert("상위메뉴를 선택하세요");
	 			 return;
	 		 }

	 		if(!confirm("저장하시겠습니까?")) return;

	 	//	$("#menuOrd").val($('#sortOrd option').length+1);
	 		var params = $("#aform").serialize();


	 		$.ajax({
			    url:'/admin/menu/saveMenu',
			    type:'post',
			    contentType:"application/x-www-form-urlencoded; charset=UTF-8",
			    data: params,
			    success: function(data) {
			    	if(data.isSuccess){
						if( $("#upMenuSeq").val() == -1){
							 $("#menuTree").jstree(true).refresh();
						}else{
							$("#menuTree").jstree("refresh_node", $("#upMenuSeq").val());
				  		    $("#menuTree").jstree("open_node", $("#upMenuSeq").val());
						}

			    	}
			    },
			    error: function(err) {
			    }
			});

      });


	 	$("#delete").on("click",function(e) {

	 		e.preventDefault();



	 		if($("#menuSeq").val()==''){
	 			alert("삭제할 항목을 선택하세요.");
	 			return;

	 		}

	 		if($("#upMenuSeq").val()==-1){
	 			alert("최상위 항목을 삭제할수 없습니다.");
	 			return;
	 		}

	 		if(!confirm("삭제하시겠습니까?")) return;

	 		var params = $("#aform").serialize();

	 		$.ajax({
			    url:'/admin/menu/deleteMenu',
			    type:'post',
			    contentType:"application/x-www-form-urlencoded; charset=UTF-8",
			    data: params,
			    success: function(data) {
			    	if(data.isSuccess){

		    		  	$("#menuTree").jstree("refresh_node", $("#upMenuSeq").val());
			  		    $("#menuTree").jstree("open_node", $("#upMenuSeq").val());
			    	}else{
			    		alert(data.msg);
			    		return;
			    	}
			    },
			    error: function(err) {


			    }
			});


      });


	 	$("#menuTypeCd").change(function() {

	 		 var selected = $('#menuTypeCd').val();


	 		var selectedMenuTypeCd = $("#selectedMenuTypeCd").val();

	 		if(selectedMenuTypeCd =='010' && selected=='030'){
	 			alert("변경할수 없습니다");
	 			return;
	 		}

	 		if(selectedMenuTypeCd =='010' && selected=='010'){
	 			alert("변경할수 없습니다");
	 			return;
	 		}


	 		 if(selected =='010' ){
	 			//$( "#menuUrl" ).val("");
	 			//$( "#menuUrl" ).prop( "readonly", true );
	 		 }
			 if(selected =='020' || selected =='030'){
				 $( "#menuUrl" ).prop( "readonly", false );
	 		 }

			 /*
	 		 if(selected !='010' ){
	 			 if($("#upMenuSeq").val()==0){
	 				alert("상위메뉴를 선택하세요");
	 				$("#menuTypeCd").val("010").attr("selected", "selected");
	 				return;
	 			 }
	 		 }
	 		*/

	 	});

	});


	function updateOrd(){


		var menuOrd = [];
		$('#sortOrd option').each(function(){
			menuOrd.push($(this).val());
		});



		$.ajax({
		    url:'/admin/menu/updateMenuOrd',
		    type:'post',
		    contentType:"application/x-www-form-urlencoded; charset=UTF-8",
		    data:{menu_seqs : menuOrd},
		    success: function(data) {
		    	if(data.isSuccess){
	    		  	$("#menuTree").jstree("refresh_node", $("#menuSeq").val());
		  		    $("#menuTree").jstree("open_node", $("#menuSeq").val());
		    	}
		    },
		    error: function(err) {
		    }
		});

	}



	function createMenuTree() {
        $('#menuTree').jstree({
        	 'core': {

        	      'data': {
	        	        "url" :  "/admin/menu/getMenuList/?lazy",


	       	        	 "data": function (node) {
	       	        	 		 return { "menuSeq" : node.id, "useYn":"Y"};
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
        $('#menuTree').on("changed.jstree", function (e, data) {
        });

        $('#menuTree').on('open_node.jstree',function (e, data) {
        	showChildren(data)
        });


        $('#menuTree').on("select_node.jstree", function (e, data) {
        	showChildren(data)
        });


   	 }




	function showChildren(data) {


		$("#save").show();
		$("#delete").show();
		$("#mode").val("update");

		$("#menuNm").val(data.node.text);
    	$("#menuUrl").val(data.node.original.menuUrl);
    	$("#upMenuNm").val(data.node.original.upMenuNm);
    	$("#upMenuSeq").val(data.node.original.upMenuSeq);
    	$("#menuSeq").val(data.node.original.id);
    	$("#menuSeqView").val(data.node.original.id);



    	$("#etc").val(data.node.original.etc);

    	$("#useYn").val(data.node.original.useYn).attr("selected", "selected");
    	//$("#openYn").val(data.node.original.openYn).attr("selected", "selected");


    	$("#menuTypeCd").val(data.node.original.menuTypeCd).attr("selected", "selected");


    	$("#selectedMenuTypeCd").val(data.node.original.menuTypeCd);


    	if($("#menuTypeCd").val()=='010'){
    		$("#topMenuSeq").val(data.node.original.id);
    	}
    	if($("#menuTypeCd").val()=='020'){
    		$("#topMenuSeq").val(data.node.original.topMenuSeq);
    	}

    	 if(data.node.original.id =='0'){
    		// $("#save").hide();
    		 $("#delete").hide();
    	 }else{
    		 $("#save").show();
    		 $("#delete").show();
    	 }

    	 var selected = $('#menuTypeCd').val();

 		 if(selected =='010' ){
    		//$( "#menuUrl" ).val("");
 			//$( "#menuUrl" ).prop( "readonly", true );
 		 }
		 if(selected =='020' || selected =='030'){
			 $( "#menuUrl" ).prop( "readonly", false );
 		 }



		var node = $("#menuTree").jstree("get_node",data.node.id);


		var selected = $('#sortOrd option:selected');

    	$('#sortOrd').children('option').remove();

    	for (var i = 0; i < node.children.length; i++) {
    		var temp_node = $("#menuTree").jstree("get_node",node.children[i]);

         	 $('#sortOrd').append($('<option/>', {
                 value: temp_node.original.id,
                 text : temp_node.original.text
             }));
        }

    	$("#sortOrd").val(selected.val());

	}

	function init() {

		$("#menuNm").val('');
    	$("#menuUrl").val('');
    	$("#menuSeq").val(0);

    	$("#etc").val('');

    	$("#useYn option:eq(0)").attr("selected", "selected");

    	//$("#openYn option:eq(1)").attr("selected", "selected");


	}





	//]]>
</script>


<div class="rightcolumn">
	<form name="aform" id="aform">
		<jsp:include page="/WEB-INF/jsp/framework/_includes/includePageParam.jsp" flush="true" />
		<input type="hidden" name="upMenuSeq" id="upMenuSeq">
		<input type="hidden" name="menuOrd" id="menuOrd">
		<input type="hidden" name="mode" id="mode" value="new">
		<input type="hidden" name="selectedMenuTypeCd" id="selectedMenuTypeCd">

		<div class="contents">
			<div id="menuTree" style="float: left; width: 40%; height: 500px; overflow-x: visible; overflow-y: scroll;"></div>

			<div id="menuDetail" style="float: left">
				<table border="1" cellpadding="0" cellspacing="0">
					<colgroup>
						<col width="30%" />
						<col width="?" />
					</colgroup>

					<tr>
						<td>상위 메뉴명</td>
						<td colspan="3">
							<input type="text" id="upMenuNm" name="upMenuNm" value="" readonly />
						</td>
					</tr>

					<tr>
						<td>메뉴구분</td>
						<td colspan="3">
							<select id="menuTypeCd" name="menuTypeCd">
								<option value="010">시스템</option>
								<option value="020">폴더</option>
								<option value="030">메뉴</option>
							</select>

						</td>
					</tr>

					<tr>
						<td>메뉴명</td>
						<td>
							<input type="text" id="menuNm" name="menuNm" value="" maxlength="100" />
						</td>
						<td>메뉴일련번호</td>
						<td>
							<input type="text" id="menuSeqView" name="menuSeqView" value="" readonly maxlength="100" />
						</td>
					</tr>
					<tr>
						<td>메뉴URL</td>
						<td colspan="3">
							<input type="text" id="menuUrl" width="100%" name="menuUrl" value="" maxlength="100" />
						</td>
					</tr>



					<tr style="display: none">
						<td>사용여부</td>
						<td colspan="3">
							<select id="useYn" name="useYn">
								<option value="Y">Yes</option>
								<option value="N">No</option>
							</select>
						</td>
					</tr>
					<!--
				    		<tr>
			    				<td>
			        				공개여부
			        			</td>
				    			<td >
				    				<select id="openYn" name="openYn" >
				    					<option value="Y" >모두공개</option>
				    					<option value="N">로그인 사용자공개</option>
				    				</select>
				    			</td>
				    		</tr>

 -->

					<tr>
						<td>정렬순서</td>
						<td colspan="3">
							<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width="90%">
										<select id="sortOrd" name="sortOrd" multiple style="width: 100%; height: 82px;">

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



					<tr>
						<td>비고</td>
						<td colspan="3">
							<textarea name="etc" id="etc" rows="5" cols="58"></textarea>
						</td>
					</tr>

				</table>
				<div id="menu_button" style="display: block">
					<button id="new">new</button>
					<button id="delete">delete</button>
					<button id="save" style="display: none">save</button>
				</div>
			</div>

		</div>

	</form>
	<!--   <button id="sam">create node</button>-->
</div>
