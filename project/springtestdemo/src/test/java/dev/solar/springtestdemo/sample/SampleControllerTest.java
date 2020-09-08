package dev.solar.springtestdemo.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate; //테스트용 TestTemplate

    //controller와 연관있는 service를 MockBean으로 만듬
    //ApplicationContext에 들어있는 빈을 Mock으로 만든 객체로 교체
    @MockBean
    SampleService mockSampleService;

    @Test
    public void hello() throws Exception {
        //mocking
        when(mockSampleService.getName()).thenReturn("Holari"); //테스트를 위해 실제 SampleService.getName()이 리턴하는 값과 다르게 주었음

        String result = testRestTemplate.getForObject("/hello", String.class);
        assertThat(result).isEqualTo("hello Holari");
    }
}
