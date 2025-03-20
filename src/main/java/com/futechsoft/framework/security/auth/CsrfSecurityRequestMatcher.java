package com.futechsoft.framework.security.auth;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;


public class CsrfSecurityRequestMatcher implements RequestMatcher {

	private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");


	/*
	private RequestMatcher hessianMatcher = new RegexRequestMatcher("/remote/secure/client-cert/hessian/.*", null,true);

	private RequestMatcher wsMatcher = new RegexRequestMatcher("/ws/.*", null, true);

	private RequestMatcher wgetMatcher = new RegexRequestMatcher("/wget.*", null, true);

	private RequestMatcher orMatcher = new OrRequestMatcher(hessianMatcher, wsMatcher, wgetMatcher);
*/

//	private RequestMatcher unprotectedMatcher  = new RegexRequestMatcher("/sample.*", null, true);

	private RequestMatcher unprotectedMatcher  = new RegexRequestMatcher("/logout", null, true);


	@Override
	public boolean matches(HttpServletRequest request) {

		if (allowedMethods.matcher(request.getMethod()).matches()) {

			return false;

		}
		return !unprotectedMatcher.matches(request);
		//return true;

	}

}
