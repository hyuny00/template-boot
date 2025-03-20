<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
		<html>
		<body>
		<form name='form1' id='form1' method='post'>
		<input type='hidden' name='dn' id='dn'>			
		<table border='1px' style='border:1px solid #E7E7E7'>
			<tr id='line1'>
				<!-- 부서명 -->
				<td style='background-color: #F6F6F6'>
					부서명
				</td>
				<td>
					<input type='text' name='ou' id='ou' value="<c:out value='${result.ou}'/>" style='border:1px solid #ffffff' readonly>
				</td>
				<!-- 부서코드 -->
				<td style='background-color: #F6F6F6'>
					부서코드
				</td>
				<td>
					<input type='text' name='ouCode' id='ouCode'  value="<c:out value='${result.ouCode}'/>"  style='border:1px solid #ffffff'>	
				</td>
			</tr>
			<tr id='line2'>
				<!-- 부서장 -->
				<td>
					부서장
				</td>
				<td style='background-color: #F6F6F6'>
					<input type='text' name='ucChieftitle' id='ucChieftitle'  value="<c:out value='${result.ucChieftitle}'/>" style='border:1px solid #ffffff'>
				</td>
				<!-- 조직명(전체)
				<td>
					조직명(전체)
				</td>
				<td style='background-color: #F6F6F6'>
					<input type='text' name='ucOrgFullName' id='ucOrgFullName'   value="<c:out value='${result.ucOrgFullName}'/>"  style='border:1px solid #ffffff'>
				</td>
				
				<!-- 조직이름 -->	
				<td style='background-color: #F6F6F6'>
					소속기관	
				</td>
				<td colspan='3'>
					<input type='text' name='ucOrganizationalUnitName' width='100%' id='ucOrganizationalUnitName' value="<c:out value='${result.ucOrganizationalUnitName}'/>"  style='border:1px solid #ffffff;width:100%'>
				</td>
			</tr>
			<tr id='line3'>
				<!-- 상위부서코드 -->
				<td style='background-color: #F6F6F6'>
					상위부서코드
				</td>
				<td>
					<input type='text' name='parentouCode' id='parentouCode'  value="<c:out value='${result.parentouCode}'/>"  style='border:1px solid #ffffff'>
				</td>
				<!-- 기관코드 -->
				<td style='background-color: #F6F6F6'>
					기관코드
				</td>
				<td>
					<input type='text' name='repouCode' id='repouCode'  value="<c:out value='${result.repouCode}'/>"    style='border:1px solid #ffffff'>
				</td>
			</tr>
			<tr id='line4'>
				<!--문서수신여부 -->
				<td>
					문서수신여부
				</td>
				<td style='background-color: #F6F6F6'>
					<input type='text' name='ouReceiveDocumentYN' id='ouReceiveDocumentYN'  value="<c:out value='${result.ouReceiveDocumentYN}'/>"    style='border:1px solid #ffffff'>
				</td>
				<!-- 관인부서여부 -->
				<td>
					관인부서여부
				</td>
				<td style='background-color: #F6F6F6'>
					<input type='text' name='ouSendOutDocumentYN' id='ouSendOutDocumentYN' value="<c:out value='${result.ouSendOutDocumentYN}'/>"   style='border:1px solid #ffffff'>
				</td>
			</tr>
		</table>
		</form>
		
		</body>
		</html>
