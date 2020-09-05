package dev.solar.springinit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringinitApplicationTest {

    @Autowired
    Environment environment;

    @Test
    public void contextLoads() {
        assertThat(environment.getProperty("person.name"))
                .isEqualTo("solar");
    }

    @Test
    public void contextLoads2() {
        assertThat(environment.getProperty("person.name"))
                .isEqualTo("holari");
    }
}
