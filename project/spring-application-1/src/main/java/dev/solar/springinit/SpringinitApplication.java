package dev.solar.springinit;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

@SpringBootApplication
public class SpringinitApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(SpringinitApplication.class)
                .banner(new Banner() {
                    @Override
                    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
                        out.println("======================================");
                        out.println("SOLAR'S SPRING PROJECT !!!! ");
                        out.println("======================================");
                    }
                })
                .run(args);
    }
}
