package dev.solar.demospring51;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AnotherHandler {

    @EventListener
    public void handle(MyEvent myEvent) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("Another Handler !!! 데이터는 : " + myEvent.getData() );
    }
}
