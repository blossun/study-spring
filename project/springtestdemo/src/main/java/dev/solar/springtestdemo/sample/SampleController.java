package dev.solar.springtestdemo.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
    
    private static final Logger log = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    private SampleService sampleService;

    @GetMapping("/hello")
    public String hello() {
        log.info("nunnu");
        System.out.println("skip"); //이렇게 쓰는 것은 비추
        return "hello " + sampleService.getName();
    }
}
