<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
	<body>
	<form name='form1' id='form1'>
	<input type='hidden' name='dn' id='dn'>			
	<table border='1px' style='border:1px solid #E7E7E7'>
		<tr id='line1'>
			<td style='background-color: #F6F6F6'>
				ID
			</td>
			<td>
				<input type='text' name='cn' id='cn' value="<c:out value='${result.cn}'/>" style='border:1px solid #ffffff' readonly>
			</td>
			<!-- 이름 -->
			<td style='background-color: #F6F6F6'>
				이름
			</td>
			<td>
				<input type='text' name='displayName' id='displayName' value="<c:out value='${result.displayName}'/>"  style='border:1px solid #ffffff'>	
			</td>
		</tr>
		<tr id='line2'>
			<!-- 소속부서 -->
			<td>
				소속부서
			</td>
			<td style='background-color: #F6F6F6'>
				<input type='text' name='ou' id='ou' value="<c:out value='${result.ou}'/>" style='border:1px solid #ffffff'>
			</td>
			<!-- 부서코드 -->
			<td>
				부서코드
			</td>
			<td style='background-color: #F6F6F6'>
				<input type='text' name='ouCode' id='ouCode'  value="<c:out value='${result.ouCode}'/>" style='border:1px solid #ffffff'>
			</td>
		</tr>
		<tr id='line3'>
			<!-- 소속기관 -->
			<td style='background-color: #F6F6F6'>
				소속기관
			</td>
			<td>
				<input type='text' name='companyName' id='companyName' value="<c:out value='${result.companyName}'/>" style='border:1px solid #ffffff'>
			</td>
			<!-- 기관코드 -->
			<td style='background-color: #F6F6F6'>
				기관코드
			</td>
			<td>
				<input type='text' name='topouCode' id='topouCode' value="<c:out value='${result.topouCode}'/>" style='border:1px solid #ffffff'>
			</td>
		</tr>
		
		<tr id='line3'>
			<!-- 회사전화번호 -->
			<td style='background-color: #F6F6F6'>
				회사전화번호
			</td>
			<td>
				<input type='text' name='telephoneNumber' id='telephoneNumber' value="<c:out value='${result.telephoneNumber}'/>" style='border:1px solid #ffffff'>
			</td>
			<!-- 메일 -->
			<td style='background-color: #F6F6F6'>
				메일
			</td>
			<td>
				<input type='text' name='mail' id='mail' value="<c:out value='${result.mail}'/>" style='border:1px solid #ffffff'>
			</td>
		</tr>
		
		<tr id='line4'>
			<!-- 사용자전체이름 -->
			<td>
				사용자전체이름
			</td>
			<td colspan='3'  style='background-color: #F6F6F6'>
				<input type='text' name='userFullName' width='100%' id='userFullName'  value="<c:out value='${result.userFullName}'/>"  style='border:1px solid #ffffff;width:100%'>
			</td>
		</tr>		
		<tr>	
			<!-- 조직전체이름 -->	
			<td style='background-color: #F6F6F6'>
				조직전체이름	
			</td>
			<td colspan='3'>
				<input type='text' name='ucOrgFullName' width='100%' id='ucOrgFullName' value="<c:out value='${result.ucOrgFullName}'/>"  style='border:1px solid #ffffff;width:100%'>
			</td>
		
		</tr>
	</table>
	</form>
	
	</body>
</html>