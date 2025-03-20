<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>
<style>

</style>
<div class="leftcolumn">
 	<ul>
		<c:forEach items="${subMenuList}" var="subMenuList" >

				<c:if test="${ subMenuList.menuTypeCd eq '020'   or subMenuList.menuTypeCd eq '030' }">
					<li>
						<c:set var="loop_flag" value="false" />
						<c:if test="${ subMenuList.subMenuCnt eq '0'  }">
								<c:forEach items="${authMenuList}" var="authMenuList">
									<c:if test="${not loop_flag}">
										<c:if test="${subMenuList.menuSeq eq authMenuList.menuSeq}">
											<a href="${subMenuList.menuUrl}?menuSeq=${subMenuList.menuSeq}&topMenuSeq=${param.topMenuSeq}">${subMenuList.menuNm}</a>
											<c:set var="loop_flag" value="true" />
										</c:if>
									</c:if>
								</c:forEach>
						</c:if>

						<c:set var="loop_flag" value="false" />
						<c:forEach items="${authMenuList}" var="authMenuList">
							<c:if test="${not loop_flag}">
									<c:if test="${subMenuList.menuSeq eq authMenuList.upMenuSeq}">
										<a href="${subMenuList.menuUrl}?menuSeq=${subMenuList.menuSeq}&topMenuSeq=${param.topMenuSeq}">${subMenuList.menuNm}</a>
										<c:set var="loop_flag" value="true" />
									</c:if>
							</c:if>
						</c:forEach>

						<c:forEach items="${authMenuList}" var="authMenuList">
							<c:if test="${subMenuList.menuSeq eq authMenuList.upMenuSeq}">
							<li>--<a href="${basePath}${authMenuList.menuUrl}?menuSeq=${authMenuList.menuSeq}&topMenuSeq=${param.topMenuSeq}">${authMenuList.menuNm}</a></li>
							</c:if>
						</c:forEach>
				   </li>
				</c:if>
		</c:forEach>
  	</ul>
</div>