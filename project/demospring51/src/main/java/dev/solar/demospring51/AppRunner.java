package dev.solar.demospring51;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

    @Value("#{1 +1}")
    int value;

    @Value("#{'hello ' + 'world'}") //문자열은 싱글쿼터(') 사용
    String greeting;

    @Value("#{1 eq 1}")
    boolean trueOrFalse;

    @Value("haha")
    String haha;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("================= ");
        System.out.println(value);
        System.out.println(greeting);
        System.out.println(trueOrFalse);
        System.out.println(haha);
    }
}
