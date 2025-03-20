<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>


<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title></title>
		<meta name="viewport" content="width=device-width" />
		<style>
		html, body { background:#ebebeb; font-size:10px; font-family:Verdana; margin:0; padding:0; }
		#container { min-width:320px; margin:0px auto 0 auto; background:white; border-radius:0px; padding:0px; overflow:hidden; }
		#tree { float:left; min-width:319px; border-right:1px solid silver; overflow:auto; padding:0px 0; }
		#data { margin-left:320px; }
		#data textarea { margin:0; padding:0; height:100%; width:100%; border:0; background:white; display:block; line-height:18px; resize:none; }
		#data, #code { font: normal normal normal 12px/18px 'Consolas', monospace !important; }

		</style>
	</head>
	<body>
		<div id="container" role="main">
		    <div id="search">조직검색 <input type="text" id="schOrgNm" name="schOrgNm"/>   <a href="javascript:searchOrg()">검색</a>
		    
		    <a href="javascript:createOrgTree('', '', 'Y')">조직도보기</a>
		    </div>
		    
		    <div id="orgList"></div>
		    
			<div id="tree"></div>
			<div id="data">
				
			</div>
		</div>

		<script>
		
		$(function () {
			$(window).resize(function () {
				var h = Math.max($(window).height() - 0, 420);
				$('#container, #data, #tree, #data .content').height(h);
			}).resize();
			
			createOrgTree('','', 'N');

		});
		
		
		
		
		
		function createOrgTree(baseDn, type, orgView){
			
			if(orgView =='N'){
				
				var url;
				
				if(type == "dept") {
					url = "<c:url value='/framework/ldap/findOrgByDn' />";
				}
					
				if(type == "user") {
					url = "<c:url value='/framework/ldap/findUserByDn' />";
				}
				
				var data={};
	        	data.dn=baseDn;
	        	$('#data').html('');
	        	$('#data').append($('<div>').load(url, data, function() {
	        	}));
				
			}
		
			
			if(orgView=='Y'){
				$('#orgList').html('');
			}
			
			if(baseDn==''){
				baseDn="<c:out value='${param.baseDn}'/>";
			}
			
			$('#tree').jstree("destroy");
			
			
			$('#tree')
			.jstree({
				'core' : {
					'data' : {
						'url' : '<c:url value="/framework/ldap/getDeptManageSublist"/>',		
						'data' : function (node) {
							if(node.id == '#')
								node.dn=baseDn;
							else 
								node.dn= node.id;
							
							return { 'dn' : node.dn };
						}
					},
					
					'themes' : {
						'responsive' : false,
						'variant' : 'small',
						'stripes' : true
					}
				},
				
	
				'types' : {
					   "user" : {
		    	    	      "icon" : "/images/framework/icon_opi.gif",
		    	    	      "valid_children" : []
	    	    	    }
				},
				
				'plugins' : ['types']
			})
			
			.on('changed.jstree', function (e, data) {
				
				
				if(data && data.selected && data.selected.length) {
					var dn = data.selected.join(':');
					
					var url;
					
					// 선택이 부서일 경우
					if(data.node.original.type == "dept") {
						url = "<c:url value='/framework/ldap/findOrgByDn' />";
					}
						
					if(data.node.original.type == "user") {
						url = "<c:url value='/framework/ldap/findUserByDn' />";
					}

					var data={};
		        	data.dn=dn;
		        	$('#data').html('');
		        	$('#data').append($('<div>').load(url, data, function() {
		        	}));
					
				} else {
					$('#data').hide();
				}
			});
			
		}
		
		function searchOrg(){
			
			$('#tree').jstree("destroy");
			
			var schOrgNm = $("#schOrgNm").val();
			
			if(schOrgNm==''){
				alert("검섹어를 입력하세요");
				return;
			}
			
			var data={};
        	data.schOrgNm=schOrgNm;
        	$('#orgList').html('');
        	$('#orgList').append($('<div>').load("${basePath}/framework/ldap/findOrgByName", data, function() {
        	}));
        	
        
		}
		
		</script>
	</body>
</html>