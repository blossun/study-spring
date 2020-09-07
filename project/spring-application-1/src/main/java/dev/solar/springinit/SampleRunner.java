package dev.solar.springinit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SampleRunner implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(SampleRunner.class);

    @Autowired
    private PersonProperties personProperties;

    @Autowired
    private String hello;

    @Override
    public void run(ApplicationArguments args) {
        logger.debug("===============================");
        logger.debug(hello);
        logger.debug(personProperties.getName());
        logger.debug(personProperties.getFullName());
        logger.debug("===============================");
    }
}
