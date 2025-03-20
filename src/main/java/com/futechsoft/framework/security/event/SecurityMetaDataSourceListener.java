package com.futechsoft.framework.security.event;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Component;

@Component("framework.security.SecurityMetaDataSourceListener")
public class SecurityMetaDataSourceListener implements ApplicationListener<SecurityMetaDataSourceEvent> {

	private LinkedHashMap<String, List<ConfigAttribute>> requestMap;

	public LinkedHashMap<String, List<ConfigAttribute>> getRequestMap() {
		return requestMap;
	}

	@Override
	public void onApplicationEvent(SecurityMetaDataSourceEvent event) {
		this.requestMap = event.getRequestMap();
	}

}
