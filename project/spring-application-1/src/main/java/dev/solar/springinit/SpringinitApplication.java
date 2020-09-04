package dev.solar.springinit;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

@SpringBootApplication
public class SpringinitApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringinitApplication.class);
        /* start of 배너를 코딩으로 구현 */
        app.setBanner(new Banner() {
            @Override
            public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
                out.println("======================================");
                out.println("  SOLAR'S SPRING PROJECT !!!!!!!!! ");
                out.println("======================================");
            }
        });
        /* end of 배너를 코딩으로 구현 */
//        app.setBannerMode(Banner.Mode.OFF); //배너 끄기
        app.run(args);
    }
}
