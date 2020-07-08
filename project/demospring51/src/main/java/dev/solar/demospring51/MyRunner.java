package dev.solar.demospring51;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements ApplicationRunner {

    @Autowired
    AutowiredAnnotationBeanPostProcessor processor;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(processor);
    }
}
