package com.futechsoft.framework.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.futechsoft.framework.exception.ErrorCode;
import com.futechsoft.framework.security.vo.CustomUserDetails;
//@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder; 

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();

		CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
		if (!matchPassword(password, user.getPassword())) {
			throw new BadCredentialsException(ErrorCode.BAD_CREDENTIALS.getMessage());
		}

		UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());

		return upat;
	}

	public boolean supports(Class<?> authentication) {
		return true;
	}

	private boolean matchPassword(String loginPwd, String password) {
		return (passwordEncoder.matches(loginPwd, password));
	}

}
