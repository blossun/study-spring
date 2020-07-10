package dev.solar.demospring51;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MyEventHandler {
    @EventListener
    @Async
    public void handle(MyEvent event) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("이벤트 전달 받음!!!! 데이터는 : " + event.getData());
    }

    @EventListener
    @Async
    public void handle(ContextRefreshedEvent event) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("ContextRefreshedEvent");
        ApplicationContext applicationContext = event.getApplicationContext(); //직접 ApplicationContext를 꺼내서 사용할 수도 있다.
        System.out.println("==>>> " + applicationContext.getClass());
    }

    @EventListener
    @Async
    public void handle(ContextClosedEvent event) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("ContextClosedEvent");
    }
}
