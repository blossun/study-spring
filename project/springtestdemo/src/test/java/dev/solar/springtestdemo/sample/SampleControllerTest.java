package dev.solar.springtestdemo.sample;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(SampleController.class) //이 컨트롤러 하나만 테스트
public class SampleControllerTest {

    @Rule
    public OutputCaptureRule output = new OutputCaptureRule();

    @Autowired
    MockMvc mockMvc; //@WebMvcTest는 `MockMvc로 테스트해야한다.

    //controller와 연관있는 service를 MockBean으로 만듬
    //ApplicationContext에 들어있는 빈을 Mock으로 만든 객체로 교체
    @MockBean
    SampleService mockSampleService;

    @Test
    public void hello() throws Exception {

        //mocking
        when(mockSampleService.getName()).thenReturn("Solar");

        mockMvc.perform(get("/hello"))
                .andExpect(content().string("hello Solar"));

        assertThat(output)
                .contains("nunnu")
                .contains("skip");
    }
}
