package dev.solar.springbootrest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/hello")
    private String hello() throws InterruptedException {
        Thread.sleep(5000l);
        return "hello";
    }

    @GetMapping("/world")
    private String world() throws InterruptedException {
        Thread.sleep(3000l);
        return "world";
    }
}
