<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>
<script type="text/javascript" src="${basePath}/js/polyfill/polyfill.min.js"></script>
<script type="text/javascript" src="${basePath}/js/jspdf/jspdf.min.js"></script>
<script type="text/javascript" src="${basePath}/js/html2canvas/html2canvas.min.js"></script>
<script type="text/javascript" src="${basePath}/js/html2Pdf/html2pdf.bundle.js"></script>
<script type="text/javascript" src="${basePath}/js/multiselect.js"></script>
    <script type="text/javaScript"  >

	    $(document).ready(function(){
	    	// select2 초기화
	    	$('#test').select2();


	    	//$('#test2').select2();
	    	$('#test2').select2({
	    		placeholder: '선택'
	    	});


	    	$('#test3').select2({
	    		//placeholder: '선택'
	    	});


/*
	    	$('#test3').select2({
	    		ajax: {
	    			placeholder: "select...",
	    		    url: '/sample/selectCode',
	    		    processResults: function (data) {
	    		      // Transforms the top-level key of the response object from 'items' to 'results'
	    		      return {

	    		        results: data
	    		      };
	    		    }
	    		  }
	    	});
*/

	    	  $("#btn-excel").on("click", function () {

	              var $preparingFileModal = $("#preparing-file-modal");
	              $preparingFileModal.dialog({ modal: true });
	              $("#progressbar").progressbar({value: false});
	              $.fileDownload("/file/excelDown", {
	                  successCallback: function (url) {
	                      $preparingFileModal.dialog('close');
	                  },
	                  failCallback: function (responseHtml, url) {
	                      $preparingFileModal.dialog('close');
	                      $("#error-modal").dialog({ modal: true });
	                  }
	              });
	              // 버튼의 원래 클릭 이벤트를 중지 시키기 위해 필요합니다.
	              return false;
	          });

	    	  $('#undo_redo').multiselect();

	    });


	    function selectCode(id) {


	    	$.ajax({
	    		url: '/sample/selectCode',
			    type:'post',
			    contentType:"application/x-www-form-urlencoded; charset=UTF-8",
			    data: {"test": id},
			    success: function(data) {

			    	if(data){

			    	//	data.unshift({id: "", text:"선택"});
			    		$('#test3').val('');
			    		$('#test3').trigger('change');

			    		$('#test3').select2({
				    		//placeholder: '선택1',
				    		//allowClear: true,
				    		data: data
				    	});

			    	}


			    	//$('#test3').val('2');
			    	//$('#test3').trigger('change');
			    },
			    error: function(err) {
			    }
			});


	    }


        /* 글 수정 화면 function */
        function sampleDetail(id) {
        	document.listForm.id.value = id;
           	document.listForm.action = "${basePath}/sample/selectSample";
           	document.listForm.submit();
        }

        /* 글 등록 화면 function */
        function sampleForm() {
           	document.listForm.action = "${basePath}/sample/sampleForm";
           	document.listForm.submit();
        }

        /* 글 목록 화면 function */
        function sampleList() {
        	document.listForm.pageNo.value = 1;
        	document.listForm.action = "${basePath}/sample/selectSampleList";
           	document.listForm.submit();
        }


        function popupSample(id){

        	var data={};
        	data.view="sample/samplePopup";

        	//SampleController 호출

        	$("#popLayer").load("${basePath}/common/view", data , function() {
				//호출완료시 스크립트 실행
        		//fn_test();
        	});

        	$("#popLayer").fadeIn();
        	$("#popLayer").center();
        	$("#popLayer").draggable();

        }


        function append(id){

        	var data={};
        	data.view="sample/samplePopup";

        	$('#appendLayer').append($('<div>').load("${basePath}/sample/samplePopup", data, function() {
    			//호출완료시 스크립트 실행
        		//fn_test();
        	}));

        }




        function popupClose(){
        	$("#popLayer").fadeOut();
        }

        function test1(){
    		alert("안녕하세요1");
    	}

        function test1(){
        	 var $preparingFileModal = $("#preparing-file-modal");
             $preparingFileModal.dialog({ modal: true });
             $("#progressbar").progressbar({value: false});
             $.fileDownload("/file/excelDown", {
                 successCallback: function (url) {
                     $preparingFileModal.dialog('close');
                 },
                 failCallback: function (responseHtml, url) {
                     $preparingFileModal.dialog('close');
                     $("#error-modal").dialog({ modal: true });
                 }
             });
             // 버튼의 원래 클릭 이벤트를 중지 시키기 위해 필요합니다.
             return false;
    	}


        function screenshot1() {


			$("#doc1").css("height", "");

			$('html,body').scrollTop(0); // Take your <div> / html at top position before calling html2canvas

   		 	html2canvas( $("#page1"), {
         		useCORS: true,
         		dpi: 300,

          		onrendered:function(canvas) {
		        	  $("#doc1").css("height", "300px");
		        	  $("#doc1").hide();
						//document.body.appendChild(canvas);



						var doc = new jsPDF('p', 'mm', 'a4');
						var imgData = canvas.toDataURL('image/png');
						var imgWidth = 210;
						var pageHeight = 295;
						var imgHeight = canvas.height * imgWidth / canvas.width;
						var heightLeft = imgHeight;
						var position = 0;

						doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);


						heightLeft -= pageHeight;


						var i=0;
						while (heightLeft >= 0) {

							position = heightLeft - imgHeight;
							doc.addPage();

							doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
							heightLeft -= pageHeight;
						}
						 var file = doc.output('blob');
			              var fd = new FormData();     // To carry on your data
			              fd.append('pdf',file);


		              /*
		              var dataURL = canvas.toDataURL("image/png");
		              var contentType = 'image/png';


		              var pdfData = new jsPDF ('p', 'mm', 'a4');

		              var width = pdfData.internal.pageSize.getWidth();
		              var height = pdfData.internal.pageSize.getHeight();

              		  pdfData.addImage(dataURL, 'PNG', 0, 0, width, height);

		              var file = pdfData.output('blob');
		              var fd = new FormData();     // To carry on your data
		              fd.append('pdf',file);
					*/

		              $.ajax({
		              	   url: '/createPdf1',   //here is also a problem, depends on your
		              	   data: fd,           //backend language, it may looks like '/model/send.php'
		              	   dataType: 'text',
		              	   processData: false,
		              	   contentType: false,
		              	   type: 'POST',
		              	   success: function (response) {
		              	     alert('Exit to send request');
		              	   },
		              	   error: function (jqXHR) {
		              		   alert('Failure to send request');
		              	   }
	              		});

          }
        });



/*
        html2canvas($('#doc1')
        ).then(function(canvas) {
            var dataURL = canvas.toDataURL();
            //var pdf = new jsPDF('p','mm',[297, 210]);
            var pdfData = new jsPDF ('p', 'mm', 'a4');

            var width = pdfData.internal.pageSize.getWidth();
            var height = pdfData.internal.pageSize.getHeight();

             pdfData.addImage(dataURL, 'PNG', 0, 0, width, height);


            var file = pdfData.output('blob');
            var fd = new FormData();     // To carry on your data
            fd.append('fd',file);


            //this.pdfData.save(filename);

            $.ajax({
            	   url: '/createPdf1',   //here is also a problem, depends on your
            	   data: fd,           //backend language, it may looks like '/model/send.php'
            	   dataType: 'text',
            	   processData: false,
            	   contentType: false,
            	   type: 'POST',
            	   success: function (response) {
            	     alert('Exit to send request');
            	   },
            	   error: function (jqXHR) {
            		   alert('Failure to send request');
            	   }
            	});
        });
*/

    }
/*
    function screenshot() {


        html2canvas($('#doc1')[0], {useCORS: true, scrollY: -window.scrollY}).then(function(canvas) {
            var dataURL = canvas.toDataURL();
           // window.open(dataURL);
            $.ajax({
                url: '/createPdf',
                type: 'post',
                data: {
                    image: dataURL
                },
                dataType: 'json',
                success: function(response) {
                    if(response.success) {
                       $.post("yourlinkuploadserver", response.data.link);
                    }
                }
            });
        });
    }
*/

	function screenshot() {


		$("#doc1").css("height", "");
		$('html,body').scrollTop(0); // Take your <div> / html at top position before calling html2canvas

		 html2canvas( $("#page1"), {
	     	useCORS: true,
     		dpi: 200,

	      	onrendered:function(canvas) {
    	  		$("#doc1").hide();
	    	  	$("#doc1").css("height", "300px");

	          	var dataURL = canvas.toDataURL("image/png");
		        $.ajax({
		            url: '/createPdf',
		            type: 'post',
		            data: {
		                image: dataURL
		            },
		            dataType: 'json',
		            success: function(response) {
		                if(response.success) {
		                   $.post("yourlinkuploadserver", response.data.link);
		                }
		            }
			      });
		      }
	     });

	}


	function screenshot2() {


	    var element = document.getElementById('pdf1').innerHTML;


	     var opt = {
	                        margin:       [50, 72, 50, 72], //top, left, buttom, right
	                        //filename:    name + '.pdf',
	                        image:        { type: 'jpeg', quality: 0.98 },
	                        html2canvas:  { dpi: 300, scale: 2, letterRendering: true},
	                        jsPDF:        { unit: 'pt', format: 'a4', orientation: 'portrait'},
	                        pageBreak: { mode: 'css', after:'.break-page'}
                   };
/*
 *
 html2pdf().from(element).set(opt).save();

 */

	    var worker = html2pdf()
	        .set(opt)
	        .from(element)
	        .toPdf()
	        .get('pdf')
	        .then(function(doc) {

        	 	var file = doc.output('blob');
              	var fd = new FormData();     // To carry on your data
              	fd.append('pdf',file);


              	$.ajax({
              	   	url: '/createPdf1',   //here is also a problem, depends on your
              	   	data: fd,           //backend language, it may looks like '/model/send.php'
              	   	dataType: 'text',
              	   	processData: false,
              	   	contentType: false,
              	   	type: 'POST',
              	   	success: function (response) {
              	     	alert('Exit to send request');
              	   	},
              	   	error: function (jqXHR) {
              		   	alert('Failure to send request');
              	   	}
       			});


              	/*
	            var totalPages = doc.internal.getNumberOfPages();


	            for(var i = 1; i <= totalPages; i++ ) {

	                if (i == 1) {
	                    doc.setPage(i);

	                    //doc.addImage('/img/logo.png', 'PNG', 0, 0, 600, 850);
	                     //  doc.setPage(i);
	                    doc.setFontType("bold");
	                    doc.text(330, 40, 'Matter: ');
	                    doc.setFontType("normal");
	                    doc.text(382, 40,  'No onno nono nononono');

	                    doc.setFontType("bold");
	                    doc.text(330, 60, 'Activity: ');
	                    doc.setFontType("normal");
	                    doc.text(392, 60, 'Nonnono onno nonono');
	                    doc.setFontSize(12);
	                    doc.setFontType("normal");
	                     doc.setPage(i);


	                }
	            }
				*/


	        })


	}

    </script>


    <form  id="listForm" name="listForm"  method="post" action="${basePath}/sample/selectSampleList">
    	<!-- 메뉴, 페이징 파라메터-->
		<jsp:include page="/WEB-INF/jsp/framework/_includes/includePageParam.jsp" flush="true"/>
        <input type="hidden" name="id" value="">
        	<!-- // 타이틀 -->
        	<div id="search">
        		<ul>
        			<li>
        				<select name="schCondition" >
        					<option value="1" <c:if test="${param.schCondition eq '1'}">selected </c:if> >Name</option>
        					<option value="0" <c:if test="${param.schCondition eq '0'}">selected </c:if> >ID</option>
        				</select>
        			</li>
        			<li>
                        <input type="text"  name="schKeyword" value="${param.schKeyword}" />
                    </li>
        			<li>
        	            <span class="btn_blue_l">
        	                <a href="javascript:sampleList();">search</a>
        	            </span>
        	        </li>

        	        <li>
        				<select name="test" id="test" style="width:300px" onchange="selectCode(this.value);">
      							<option >전체</option>
      							<option value="1">가나다라</option>
								<option value="2">abcs</option>
								<option value="3">1234</option>
								<option value="4">테스트</option>
        				</select>

        				<select name="test2" id="test2" style="width:300px" multiple="multiple">
        						<option>가나다라</option>
								<option>abcs</option>
								<option>1234</option>
								<option>테스트</option>
								<option>홍길동</option>
								<option>자연</option>
								<option>캐다나</option>
								<option>아프리카</option>
								<option>등록</option>
        				</select>


        				<select name="test3" id="test3" style="width:300px">
<option >전체</option>

        				</select>
        			</li>


                </ul>
        	</div>
        	<!-- List -->
        	<div id="table">
        		<table width="80%" border="0" cellpadding="0" cellspacing="0" summary="카테고리ID, 케테고리명, 사용여부, Description, 등록자 표시하는 테이블">
        			<colgroup>
        				<col width="10%"/>
        				<col width="20%"/>
        				<col width="15%"/>
        				<col width="15%"/>
        				<col width="15%"/>
        				<col width="15%"/>
        			</colgroup>
        			<tr>
        				<th align="center">id</th>
        				<th align="center">name</th>
        				<th align="center">useYn</th>
        				<th align="center">description</th>
        				<th align="center">regUser</th>
        				<th align="center">구분</th>
        			</tr>
        			<c:forEach var="result" items="${list}" varStatus="status">
            			<tr>
            				<td align="center" class="listtd"><a href="javascript:sampleDetail('${result.id}')">${result.id}</a></td>
            				<td align="left" class="listtd">${result.name}</td>
            				<td align="center" class="listtd">${result.useYn}</td>
            				<td align="center" class="listtd">${result.description}</td>
            				<td align="center" class="listtd">${result.regUser}</td>
            				<td align="center" class="listtd">${etcCode[result.etcCode]}</td>
            			</tr>
        			</c:forEach>
        		</table>
        	</div>



        	<div >

        	</div>
        	<!-- /List -->

        	<jsp:include page="/WEB-INF/jsp/framework/_includes/paging.jsp" flush="true"/>

        	<div id="sysbtn">
        	  <ul>
        	      <li>
        	          <span class="btn_blue_l">
        	              	<a href="javascript:sampleForm();">create</a>
       	               		<a href="javascript:popupSample('testId3');">popup sample</a>
       	               		<a href="javascript:append('testId3');">ajax append sample</a>
                      </span>
                      <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_USER')">
						   [권한 체크]
					  </sec:authorize>
						<button id="btn-excel">엑셀 다운로드</button>
						 <a href="javascript:screenshot2();"> screenshot()</a>
                  </li>
              </ul>


        	</div>



    </form>

	<div id="popLayer" style="display:none;width:500px;height:400px;border:4px solid #ddd;background:#fff;"> </div>
	<div id="appendLayer" > </div>
	<div id="doc1" style="display:none;OVERFLOW-Y:auto; width: 750px !important; height: 300px !important;background:#fff; "></div>

	<DIV>
        	  <div class="col-xs-5" style="float:left">
					<select name="from" STYLE="width:100px" id="undo_redo" class="form-control" size="13" multiple="multiple">
						<option value="1">C++</option>
						<option value="2">C#</option>
						<option value="3">Haskell</option>
						<option value="4">Java</option>
						<option value="5">JavaScript</option>
						<option value="6">Lisp</option>
						<option value="7">Lua</option>
						<option value="8">MATLAB</option>
						<option value="9">NewLISP</option>
						<option value="10">PHP</option>
						<option value="11">Perl</option>
						<option value="12">SQL</option>
						<option value="13">Unix shell</option>
					</select>
				</div>
				 <div style="float:left">

						<button type="button" id="undo_redo_undo" >undo</button>
						<button type="button" id="undo_redo_rightAll" > >> </button>
						<button type="button" id="undo_redo_rightSelected" >></button>
						<button type="button" id="undo_redo_leftSelected" ><</button>
						<button type="button" id="undo_redo_leftAll" ><<</button>
						<button type="button" id="undo_redo_redo" >redo</button>

				</div>
 				<div class="col-xs-5" style="float:left">
					<select name="to" STYLE="width:100px" id="undo_redo_to" class="form-control" size="13" multiple="multiple"></select>
				</div>



        	</DIV>
