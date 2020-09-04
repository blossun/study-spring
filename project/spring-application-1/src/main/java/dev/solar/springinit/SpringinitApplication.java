package dev.solar.springinit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringinitApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringinitApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE); //WebApplicationType 지정
        app.run(args);
    }
}
