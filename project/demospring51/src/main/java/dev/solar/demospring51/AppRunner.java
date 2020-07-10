package dev.solar.demospring51;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext resourceLoader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(resourceLoader.getClass()); //WebApplicationContext 중 하나

        Resource resource = resourceLoader.getResource("classpath:test.txt");
        System.out.println(resource.getClass()); //classpath prefix를 썼기때문에 ClassPathResource 타입

        Resource resource02 = resourceLoader.getResource("text.txt");
        System.out.println(resource02.getClass()); //기본 타입 ServletContextResource

        System.out.println(resource.exists());
        System.out.println(resource.getDescription()); //전체 경로
        System.out.println(resource02.exists());
        System.out.println(resource02.getDescription()); //전체 경로
    }
}
