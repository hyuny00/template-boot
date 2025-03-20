package com.futechsoft.framework.security.auth;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.futechsoft.admin.user.vo.User;
import com.futechsoft.admin.user.vo.UserAuth;
import com.futechsoft.framework.exception.ErrorCode;
import com.futechsoft.framework.security.service.SecurityService;
import com.futechsoft.framework.security.vo.CustomUserDetails;
import com.futechsoft.framework.util.CommonUtil;
import com.futechsoft.framework.util.ConvertUtil;
public class CustomUserDetailsService implements UserDetailsService {

	@Resource(name = "framework.security.service.SecurityService")
	SecurityService securityService;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

		User user = null;
		try {
			user = securityService.getUserInfo(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		if (user == null) {
			throw new InternalAuthenticationServiceException(ErrorCode.BAD_CREDENTIALS.getMessage());
		}

		if (!CommonUtil.isTrue(user.getIsEnabled())) {
			throw new DisabledException(ErrorCode.DISABLED_USER.getMessage());
		}

		if (!CommonUtil.isTrue(user.getIsAccountNonExpired())) {
			throw new AccountExpiredException(ErrorCode.ACCOUNT_EXPIRED_USER.getMessage());
		}

		if (!CommonUtil.isTrue(user.getIsCredentialsNonExpired())) {
			throw new CredentialsExpiredException( ErrorCode.CREDENTIALS_EXPIRED_USER.getMessage());
		}

		if (!CommonUtil.isTrue(user.getIsAccountNonLocked())) {
			throw new LockedException( ErrorCode.LOCKED_USER.getMessage());
		}

		CustomUserDetails userDetails = new CustomUserDetails();
		ConvertUtil.copyProperties(user, userDetails);

		userDetails.setEnabled(CommonUtil.isTrue(user.getIsEnabled()));
		userDetails.setAccountNonExpired(CommonUtil.isTrue(user.getIsAccountNonExpired()));
		userDetails.setAccountNonLocked(CommonUtil.isTrue(user.getIsAccountNonLocked()));
		userDetails.setCredentialsNonExpired(CommonUtil.isTrue(user.getIsCredentialsNonExpired()));

		List<UserAuth> userAuthList = null;
		try {
			userAuthList = securityService.getUserAuthList(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		userDetails.setUserAuthList(userAuthList);

		return userDetails;
	}

}
