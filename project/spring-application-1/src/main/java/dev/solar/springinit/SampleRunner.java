package dev.solar.springinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SampleRunner implements ApplicationRunner {

    @Autowired
    private PersonProperties personProperties;

    @Autowired
    private String hello;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("=============================");
        System.out.println(hello);
        System.out.println(personProperties.getName());
        System.out.println(personProperties.getFullName());
        System.out.println("=============================");
    }
}
