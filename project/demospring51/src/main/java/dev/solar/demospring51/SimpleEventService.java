package dev.solar.demospring51;

import org.springframework.stereotype.Service;

@Service
public class SimpleEventService implements EventService{ //Real Subject
    @Override
    public void createEvent() {
        long begin = System.currentTimeMillis();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Created an event");

        System.out.println("수행시간 : " + (System.currentTimeMillis() - begin));
    }

    @Override
    public void publishEvent() {
        long begin = System.currentTimeMillis();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Published an event");

        System.out.println("수행시간 : " + (System.currentTimeMillis() - begin));
    }

    // Aspect 적용 제외 대상
    @Override
    public void deleteEvent() {
        System.out.println("Deleted an event");
    }
}
