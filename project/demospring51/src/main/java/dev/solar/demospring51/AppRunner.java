package dev.solar.demospring51;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    MessageSource messageSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        while(true) {
            System.out.println(messageSource.getMessage("greeting", new String[]{"Solar"}, Locale.getDefault()));
            System.out.println(messageSource.getMessage("greeting", new String[]{"Solar"}, Locale.KOREA));
            System.out.println(messageSource.getMessage("greeting", new String[]{"Solar"}, Locale.ENGLISH));
            Thread.sleep(10001); //1초마다 콘솔에 찍으면서 값이 변경되는지 확인
        }
    }
}

