package dev.solar.demospring51;

import dev.solar.out.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;

import java.util.function.Supplier;

@SpringBootApplication
public class Demospring51Application {

    @Autowired
    MyService myService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Demospring51Application.class);
        app.addInitializers(new ApplicationContextInitializer<GenericApplicationContext>() {
            @Override
            public void initialize(GenericApplicationContext ctx) {
                ctx.registerBean(MyService.class);
            }
        });
        app.run(args);
    }

}
