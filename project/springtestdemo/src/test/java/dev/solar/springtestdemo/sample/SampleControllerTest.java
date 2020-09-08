package dev.solar.springtestdemo.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleControllerTest {

    @Autowired
    WebTestClient webTestClient; //WebClient와 비슷하게 동작하는 비동기 처리방식의 API

    //controller와 연관있는 service를 MockBean으로 만듬
    //ApplicationContext에 들어있는 빈을 Mock으로 만든 객체로 교체
    @MockBean
    SampleService mockSampleService;

    @Test
    public void hello() throws Exception {
        //mocking
        when(mockSampleService.getName()).thenReturn("Solar");

        webTestClient.get().uri("/hello")
                .exchange().expectStatus().isOk()
                .expectBody(String.class).isEqualTo("hello Solar");
    }
}
