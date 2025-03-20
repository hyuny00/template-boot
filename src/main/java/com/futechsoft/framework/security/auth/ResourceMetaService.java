package com.futechsoft.framework.security.auth;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Component;

import com.futechsoft.admin.auth.vo.Path;
import com.futechsoft.admin.auth.vo.PathAuth;
import com.futechsoft.framework.common.constant.AuthConstant;
import com.futechsoft.framework.security.event.SecurityMetaDataSourceEvent;
import com.futechsoft.framework.security.service.SecurityService;

@Component("framework.security.ResourceMetaService")
public class ResourceMetaService {

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	SecurityService securityService;

	private LinkedHashMap<String, List<ConfigAttribute>> requestMap;

	public void createSecurityMetaDataSource() throws Exception {

		List<Path> pathList = securityService.getAllPathList();
		requestMap = new LinkedHashMap<String, List<ConfigAttribute>>();

		for (Path path : pathList) {

			List<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();

			List<PathAuth> pathAuthList = securityService.getPathAuthList(path.getPathSeq());
			for (PathAuth pathAuth : pathAuthList) {
				if(!pathAuth.getAuthCd().startsWith(AuthConstant.ROLE_PREFIX)){
					atts.add(new SecurityConfig(AuthConstant.ROLE_PREFIX + pathAuth.getAuthCd()));
				}else {
					atts.add(new SecurityConfig( pathAuth.getAuthCd()));
				}

			}

			requestMap.put(path.getPath(), atts);
		}

		publisher.publishEvent(new SecurityMetaDataSourceEvent(this, requestMap));
	}
}
