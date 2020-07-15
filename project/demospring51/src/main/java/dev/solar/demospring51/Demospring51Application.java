package dev.solar.demospring51;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demospring51Application {

    public static void main(String[] args) {
        //Web Application으로 실행하지 않고, Java main 메서드 실행시킴 (서버 모드 OFF)
        SpringApplication app = new SpringApplication(Demospring51Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

}

