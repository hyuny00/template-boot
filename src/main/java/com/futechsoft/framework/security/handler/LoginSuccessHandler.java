package com.futechsoft.framework.security.handler;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.futechsoft.framework.security.auth.ResourceMenuService;
import com.futechsoft.framework.security.auth.ResourceMetaService;
import com.futechsoft.framework.security.service.SecurityService;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	private RequestCache requestCache = new HttpSessionRequestCache();
	private RedirectStrategy redirectStratgy = new DefaultRedirectStrategy();

	@Resource(name = "framework.security.service.SecurityService")
	private SecurityService securityService;

	private String loginId;
	private String defaultUrl;
	
	@Resource(name = "framework.menu.service.ResourceMenuService")
	private ResourceMenuService resourceMenuService;
	
	@Resource(name = "framework.security.ResourceMetaService")
	private ResourceMetaService resourceMetaService;

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		clearAuthenticationAttributes(request);

		String userId = request.getParameter(loginId);
		try {
			securityService.resetFailCnt(userId);
			
			resourceMenuService.init();
			resourceMetaService.createSecurityMetaDataSource();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
		resultRedirectStrategy(request, response, authentication);

	}

	protected void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null)
			return;
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

	protected void resultRedirectStrategy(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		String[] ext= {".ico",".css", ".js", ".jpg", ".png", ".gif", ".exe", "/auth"};

		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest != null) {
			String targetUrl = savedRequest.getRedirectUrl();

			if(!StringUtils.endsWithAny(targetUrl, ext)) {
				redirectStratgy.sendRedirect(request, response, targetUrl);
			}else {
				redirectStratgy.sendRedirect(request, response, defaultUrl);
			}

		} else {
			redirectStratgy.sendRedirect(request, response, defaultUrl);
		}

	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}

}
