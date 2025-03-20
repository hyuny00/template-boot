package com.futechsoft.framework.test.event;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.futechsoft.framework.util.FtMap;

@Component
public class TestEventHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestEventHandler.class);

	private static final Map<String, SseEmitter> CLIENTS = new ConcurrentHashMap<>();


	public  Map<String, SseEmitter> getClients() {
		return CLIENTS;
	}




	@Async
    @EventListener
    public void onTestEvent(final TestEvent testEvent) {

		Map<String, SseEmitter> CLIENTS= getClients() ;
    	Set<String> deadIds = new HashSet<>();

         CLIENTS.forEach((id, emitter) -> {
             try {
            	 FtMap ftMap= testEvent.getFtMap();
            	 ftMap.put("id", id);

                 emitter.send( ftMap, MediaType.APPLICATION_JSON);
             } catch (Exception e) {
                 deadIds.add(id);

                 LOGGER.debug("disconnect..."+id);
             }
         });

         deadIds.forEach(CLIENTS::remove);
    }


}
