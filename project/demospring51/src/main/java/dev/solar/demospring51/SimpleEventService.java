package dev.solar.demospring51;

import org.springframework.stereotype.Service;

@Service
public class SimpleEventService implements EventService{ //Real Subject
    @Override
    public void createEvent() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Created an event");
    }

    @Override
    public void publishEvent() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Published an event");
    }

    // Aspect 적용 제외 대상
    @Override
    public void deleteEvent() {
        System.out.println("Deleted an event");
    }
}
