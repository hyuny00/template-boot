package com.futechsoft.framework.task;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.futechsoft.framework.test.service.TestService;
import com.futechsoft.framework.util.FtMap;

@Component
public class Task {


	private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);


	@Resource(name = "test.service.TestService")
	private TestService testService;

	@PostConstruct
	public void init() {

		LOGGER.debug("init().................");
	}

	//@Scheduled(cron="0/10 * * * * * ")
	@Scheduled(fixedDelay=10000)
	public void task() {
		//LOGGER.debug("SSSSSSSSSSS..............."+new Date());
	}


	@Scheduled(fixedDelay = 2000)
	public void testEvent() {
		testService.getData(new FtMap());
	}



}
