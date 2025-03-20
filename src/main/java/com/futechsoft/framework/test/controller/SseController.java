package com.futechsoft.framework.test.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.futechsoft.framework.test.event.TestEventHandler;

@RestController
public class SseController {
   // private static final Map<String, SseEmitter> CLIENTS = new ConcurrentHashMap<>();

	@Autowired
	private TestEventHandler testEventHandler;

    @RequestMapping("/api/subscribe")
    public SseEmitter subscribe() {
    	 SseEmitter emitter = new SseEmitter((long) (60000 * 5));

    	Map<String, SseEmitter> CLIENTS = testEventHandler.getClients();

    	String id= UUID.randomUUID().toString();
        CLIENTS.put(id, emitter);

        emitter.onTimeout(() -> CLIENTS.remove(id));
        emitter.onCompletion(() -> CLIENTS.remove(id));
        return emitter;
    }

    @RequestMapping("/api/publish")
    public void publish(String message) {
        Set<String> deadIds = new HashSet<>();
        Map<String, SseEmitter> CLIENTS = testEventHandler.getClients();

        CLIENTS.forEach((id, emitter) -> {
            try {
                emitter.send(message, MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                deadIds.add(id);
                System.out.println("disconnected id : {}"+id);
            }
        });

        deadIds.forEach(CLIENTS::remove);
    }


    /*
    @Async
    @EventListener
    public void onTestEvent(final TestEvent testEvent) {
    	 System.out.println("hhhhhhhhhh............"+testEvent.getData()+":"+ CLIENTS.size());
    	 Set<String> deadIds = new HashSet<>();

         CLIENTS.forEach((id, emitter) -> {
             try {
                 emitter.send( testEvent.getFtMap(), MediaType.APPLICATION_JSON);
             } catch (Exception e) {
                 deadIds.add(id);
                 System.out.println("disconnected id : {}"+id);
             }
         });

         deadIds.forEach(CLIENTS::remove);
    }
*/



    @GetMapping("/api/test")
    public SseEmitter streamSseMvc() {
        SseEmitter emitter = new SseEmitter();
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                for (int i = 0; true; i++) {
                	System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFF");
                   /*
                	SseEventBuilder event = SseEmitter.event()
                      .data("SSE MVC - " + LocalTime.now().toString())
                      .id(String.valueOf(i))
                      .name("sse event - mvc");
                       emitter.send(event);
                      */
                	 emitter.send("fffff", MediaType.APPLICATION_JSON);
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }

}
