package dev.solar.demospringmonitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class DemospringmonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemospringmonitorApplication.class, args);
    }

}
