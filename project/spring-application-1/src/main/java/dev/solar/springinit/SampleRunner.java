package dev.solar.springinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SampleRunner implements ApplicationRunner {

    @Autowired
    PersonProperties personProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("=============================");
        System.out.println(personProperties.getFullName());
        System.out.println(personProperties.getAge());
        System.out.println("=============================");
    }
}
