package com.futechsoft.framework.security.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.futechsoft.admin.auth.vo.Path;
import com.futechsoft.admin.auth.vo.PathAuth;
import com.futechsoft.admin.user.vo.User;
import com.futechsoft.admin.user.vo.UserAuth;
import com.futechsoft.framework.security.mapper.SecurityMapper;

@Service("framework.security.service.SecurityService")
public class SecurityService {

	@Resource(name = "framework.security.mapper.SecurityMapper")
	private SecurityMapper securityMapper;

	public User getUserInfo(String userId) throws Exception {
		return securityMapper.getUserInfo(userId);
	}

	public List<UserAuth> getUserAuthList(String userId) throws Exception {
		return securityMapper.getUserAuthList(userId);
	}

	public void resetFailCnt(String userId) throws Exception {
		securityMapper.resetFailCnt(userId);
	}

	public void plusFailCnt(String userId) throws Exception {
		securityMapper.plusFailCnt(userId);
	}

	public void disabledUser(String userId) throws Exception {
		securityMapper.disabledUser(userId);
	}

	public List<PathAuth> getPathAuthList(long pathSeq) throws Exception {
		return securityMapper.getPathAuthList(pathSeq);
	}

	public List<Path> getAllPathList() throws Exception {
		return securityMapper.getAllPathList();
	}

}
