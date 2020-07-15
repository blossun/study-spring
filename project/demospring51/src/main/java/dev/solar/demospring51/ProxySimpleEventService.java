package dev.solar.demospring51;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class ProxySimpleEventService implements EventService{

    @Autowired
    SimpleEventService simpleEventService; // 방법1. 타입을 명시적으로 선언
//    EventService simpleEventService; // 방법2. 빈의 이름을 기반으로 해서 SimpleEventService 타입을 주입받음

    @Override
    public void createEvent() {
        long begin = System.currentTimeMillis();
        simpleEventService.createEvent(); //Delegate
        System.out.println("실행시간 : " + (System.currentTimeMillis() - begin));
    }

    @Override
    public void publishEvent() {
        long begin = System.currentTimeMillis();
        simpleEventService.publishEvent(); //Delegate
        System.out.println("실행시간 : " + (System.currentTimeMillis() - begin));
    }

    @Override
    public void deleteEvent() {
        simpleEventService.deleteEvent();
    }
}
