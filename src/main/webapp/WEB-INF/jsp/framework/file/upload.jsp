<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>

<style>


.file_list {
  	min-height:80px;
  	width:95%;
  	border-width:1px;
  	border-style: solid;
  	border-color:#dcdfe2;

  	/*overflow : auto;*/
 }


 /*
.file_progress {
  	display:none;
  	background-color:#ddd;
  	color:#4CAF50;
  	padding :0px 0px;
  	margin :2px 0;
  	border :1px inset #446;
  	border-radius:5px;
  	width:95%
 }
 */

.button {
    background-color: black;
    border: none;
    color: white;
    padding: 3px 10px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 12px;
    margin: 4px 2px;
    cursor: pointer;
    font-weight :bold;
}

.button1 {background-color: #E66454;}
.button2 {background-color: red;} /* Red */
.button3 {background-color: green; }
.button4 {background-color: blue; }

.file_table{
	margin-top:5px;
	border : 1px solid #dfdfdf;
	background-color: #edf0f1;
	width:95%;
	text-align: center;
}


.file_header{
width:95%;
	background-color: #edf0f1;
	font-size :12px;
	text-align: center;
}


 progress {
        position: relative;
        text-align: center;
        height: 2em;
        width: 100%;
        border-radius: 50px;
        -ms-border-radius: 50px;
        -webkit-border-radius: 50px;
        border: 1px solid #ddd;
        background-color: #ddd;
        color: #000;
        width:95%;
        /*-webkit-appearance: none;*/
    }



    progress:after {
        display: inline-block;
        position: absolute;
        top: 20%;
        content: attr(data-label);
        font-size: 0.8em;
        color: #000;
    }

    progress::-webkit-progress-bar {
        border-radius: 50px;
        background-color: #ddd;
    }
    progress::-webkit-progress-value {
        background-color: lightskyblue;
        border-radius: 50px;
    }

    progress::-ms-fill {
        background-color: lightskyblue;
    }

    progress::-moz-progress-value {
        background-color: lightskyblue;
    }
    .counts {position: absolute; z-index: 1;}

</style>

<div style="width:95%">

	<c:if test="${param.mode ne 'view'}">
		 <label class="button" for="file_input_${uploadFormId}" data-file-add>UPLOAD</label>
		 <span id="delButton_${uploadFormId}" style="display:none">
			 <span class="button button1"  onClick="uploadModule.deleteFile('${uploadFormId}')">선택 삭제</span>
			 <span class="button button2"  onClick="uploadModule.deleteAllFile('${uploadFormId}')">전체 삭제</span>
		 </span>
		 <span id="sortButton_${uploadFormId}" style="float:right;display:none">
		 	<c:if test="${ empty param.docId}">
		 		<span class="button button3" onClick="uploadModule.sort('${uploadFormId}','up')">&#8593</span>
				<span class="button button4" onClick="uploadModule.sort('${uploadFormId}','down')">&#8595</span>
			</c:if>
	  	</span>
	</c:if>
	<c:if test="${param.mode eq 'view'}">
	 	<span class="button button3" onClick="uploadModule.download('${uploadFormId}')">ZIP</span>
 	</c:if>
   	<input type="file"  id="file_input_${uploadFormId}" accept="${accept}" multiple  onChange="uploadModule.upload('${uploadFormId}');" style="display:none"/>
</div>

<div data-file-list  data-doc-id="${param.docId}"  data-thumbnail-yn="${param.thumbnailYn}"   data-upload-form-id="${uploadFormId}"  data-mode="${param.mode}"  data-ref-doc-id="${param.refDocId}" data-base-path="${basePath}" >

 	<progress id="progressBar_${uploadFormId}"   style="display:none"  max="100" data-label=""></progress>

   <div class="file_table">
	 <table class="file_header">
	  <thead><tr><th width="30px"><input type="checkbox"  onClick="uploadModule.checkAll('${uploadFormId}')"  name="checkAll" id="checkAll_${uploadFormId}"></th> <th>파일명</th></tr></thead>
	 </table>
	</div>

   <div id="fileList_${uploadFormId}"   data-thumbnail-yn="${param.thumbnailYn}"  data-max-file-size="${param.maxFileSize}"  data-accept-file="${accept}"  data-single-file="${param.singleFile}"  data-max-file-cnt="${param.maxFileCnt}"  class="file_list" >
     	 <c:if test="${param.mode ne 'view'}">파일을 드래그 해주세요</c:if>
   </div>

   <label id="progressLabel_${uploadFormId}" <c:if test="${param.mode eq 'view'}">style="display:none" </c:if> >No file selected</label>
   <input type="hidden" name="fileInfoList"  id="fileInfoList_${uploadFormId}">


   <input type="hidden" name="${param.refDocId}" id="docId_${uploadFormId}"  value="${param.docId}">

   <c:if test="${param.mode ne 'view'}">
  	 <input type="hidden" name="refDocId"  id="refDocId_${uploadFormId}" value="${param.refDocId}">
   </c:if>
   <c:if test="${param.mode eq 'view'}">
  	 <input type="hidden" name="refDocId"  id="refDocId_${uploadFormId}" value="NONE">
   </c:if>
</div>
