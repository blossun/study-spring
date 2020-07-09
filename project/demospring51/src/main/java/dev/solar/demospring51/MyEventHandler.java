package dev.solar.demospring51;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MyEventHandler implements ApplicationListener<MyEvent> {
    @Override
    public void onApplicationEvent(MyEvent event) {
        //전달받은 event로 원하는 작업을 하면 된다.
        System.out.println("이벤트 전달 받음!!!! 데이터는 : " + event.getData());
    }
}
