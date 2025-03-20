package com.futechsoft.framework.test.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.futechsoft.framework.test.event.TestEvent;
import com.futechsoft.framework.util.FtMap;

@Service("test.service.TestService")
public class TestService {

	@Autowired
	private  ApplicationEventPublisher applicationEventPublisher;



	public void getData(final FtMap param) {

		param.put("MESSAGE", "MESSAGE :" +UUID.randomUUID().toString());
		applicationEventPublisher.publishEvent(new TestEvent(param));

	}


}
