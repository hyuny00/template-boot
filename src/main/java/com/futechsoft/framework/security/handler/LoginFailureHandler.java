package com.futechsoft.framework.security.handler;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.futechsoft.admin.user.vo.User;
import com.futechsoft.framework.exception.ErrorCode;
import com.futechsoft.framework.security.service.SecurityService;

public class LoginFailureHandler implements AuthenticationFailureHandler {

	@Resource(name = "framework.security.service.SecurityService")
	private SecurityService securityService;

	private String loginId;
	private String loginPwd;
	private String errorMsg;
	private String defaultFailureUrl;

	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

		String userId = request.getParameter(loginId);
		String password = request.getParameter(loginPwd);
		String errorMsg = "";

		if (exception instanceof BadCredentialsException) {
			errorMsg =ErrorCode.BAD_CREDENTIALS.getMessage();

			try {
				// securityService.plusFailCnt(userId);
				User user = securityService.getUserInfo(userId);

				if (user != null) {

					if (user.getLoginFailCnt() >= 3) {
						try {
							securityService.disabledUser(userId);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}else {
			errorMsg =exception.getMessage();
		}


		request.setAttribute(loginId, userId);
		request.setAttribute(loginPwd, password);
		request.setAttribute("errorMsg", errorMsg);

		request.getRequestDispatcher(defaultFailureUrl).forward(request, response);

	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getDefaultFailureUrl() {
		return defaultFailureUrl;
	}

	public void setDefaultFailureUrl(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}

}
