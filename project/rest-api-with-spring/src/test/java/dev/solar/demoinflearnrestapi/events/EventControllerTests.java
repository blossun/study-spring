package dev.solar.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;

    @Autowired
    private WebApplicationContext ctx;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();
    }

    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2021, 1, 8, 13, 2, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 1, 9, 13, 2, 21))
                .beginEventDateTime(LocalDateTime.of(2021, 1, 10, 13, 2, 21))
                .endEventDateTime(LocalDateTime.of(2021, 1, 11, 13, 2, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        // stubbing
        // evnetRepository의 save()가 호출될 때 할 행동을 정의
        event.setId(10);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/") // 요청
                .contentType(MediaType.APPLICATION_JSON) // Request body 데이터 형태
                .accept(MediaTypes.HAL_JSON) // Response 데이터 타입
                .content(objectMapper.writeValueAsString(event))) // 헤더 정보에 맞게 본문을 JSON으로 변환해서 넘겨줘야한다.
                .andDo(print()) // http 본문 출력
                .andExpect(status().isCreated()) // 201 응답코드
        .andExpect(jsonPath("id").exists())
        .andExpect(header().exists(HttpHeaders.LOCATION))
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE.concat(";charset=UTF-8")));
        ;
    }
}
