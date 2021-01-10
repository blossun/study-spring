# Event 생성 API 구현: Bad Request 처리하기

@Valid와 BindingResult (또는 Errors)

* BindingResult는 항상 @Valid 바로 다음 인자로 사용해야 함. (스프링 MVC)
* @NotNull, @NotEmpty, @Min, @Max, ... 사용해서 입력값 바인딩할 때 에러 확인할 수 있음

도메인 Validator 만들기

* [Validator](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/validation/Validator.html) 인터페이스 없이 만들어도 상관없음

테스트 설명 용 애노테이션 만들기

* @Target, @Retention

테스트 할 것

> * 입력 데이터가 이상한 경우 Bad_Request로 응답
>   * **입력값이 이상한 경우 에러**
>   * **비즈니스 로직으로 검사할 수 있는 에러**
>   * 에러 응답 메시지에 에러에 대한 정보가 있어야 한다.

---

## 테스트 1 - 입력값이 이상한 경우

* 요청에 맞는 필드를 보내지만 값은 비어있는 경우 Bad Request응답이 와야한다.

```java
@Test
@DisplayName("입력 데이터가 이상한 경우")
public void createEvent_Bad_Request_Empty_Input() throws Exception {
    EventDto eventDto = EventDto.builder().build();

    this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
            .andExpect(status().isBadRequest());
}
```

→ (테스트실패) 201로 처리된다.

```
MockHttpServletResponse:
           Status = 201
    Error message = null
          Headers = [Location:"http://localhost/api/events/1", Content-Type:"application/hal+json;charset=UTF-8"]
     Content type = application/hal+json;charset=UTF-8
             Body = {"id":1,"name":null,"description":null,"beginEnrollmentDateTime":null,"closeEnrollmentDateTime":null,"beginEventDateTime":null,"endEventDateTime":null,"location":null,"basePrice":0,"maxPrice":0,"limitOfEnrollment":0,"offline":false,"free":false,"eventStatus":"DRAFT"}
    Forwarded URL = null
   Redirected URL = http://localhost/api/events/1
          Cookies = []

java.lang.AssertionError: Status 
Expected :400
Actual   :201
```



## Bad Request 처리하기 - @Valid

@Valid와 BindingResult (또는 Errors)

* BindingResult는 항상 @Valid 바로 다음 인자로 사용해야 함. (스프링 MVC)
* @NotNull, @NotEmpty, @Min, @Max, ... 사용해서 입력값 바인딩할 때 에러 확인할 수 있음



* 스프링부트 2.3부터 의존성 추가 필요

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
      <version>2.3.7.RELEASE</version>
  </dependency>
  <dependency>
      <groupId>javax.validation</groupId>
      <artifactId>validation-api</artifactId>
  </dependency>
  ```



@Valid 를 쓰면 컨트롤러에서 Request 값들을 엔티티에 바인딩할 때 검증을 수행할 수 있다. JSR-303애노테이션을 사용

검증 결과를 @Valid를 붙인 인자 바로 다음에 오는 Errors 객체에 담아준다.

```java
@PostMapping()
public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
    if (errors.hasErrors()) { // 에러가 발생한 경우 Bad Request 반환
        return ResponseEntity.badRequest().build();
    }
    Event event = modelmapper.map(eventDto, Event.class);
    Event newEvent = this.eventRepository.save(event);
    URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri(); // DB에 저장된 ID 값
    return ResponseEntity.created(createdUri).body(newEvent); //저장된 Event 정보 반환
}
```

바인딩할 엔티티에 @NotNull, @NotEmpty, @Min, @Max, ... 사용해서 입력값 바인딩할 때 에러 확인할 수 있음

```java
public class EventDto {
    @NotEmpty
    private String name;
    @NotNull
    private LocalDateTime beginEnrollmentDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    @Min(0)
    private int basePrice; // (optional)
}
```



## 테스트 2 - 비즈니스 로직으로 검사할 수 있는 에러

* maxPrice는 basePrice보다 높아야함. 시작날짜는 종료날짜보다 먼저여야 한다.

```java
@Test
@DisplayName("잘못된 데이터 입력 시 Bac Request")
public void createEvent_Bad_Request_Wrong_Input() throws Exception {
  EventDto eventDto = EventDto.builder()
    .name("Spring")
    .description("REST API Development with Spring")
    .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
    .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
    .beginEventDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
    .endEventDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
    .basePrice(10000)
    .maxPrice(200)
    .limitOfEnrollment(100)
    .location("강남역 D2 스타텁 팩토리")
    .build();

  this.mockMvc.perform(post("/api/events")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(this.objectMapper.writeValueAsString(eventDto)))
    .andExpect(status().isBadRequest());
}
```

→ (테스트실패) 201로 처리된다.



## Bad Request 처리하기 - Validator 구현

* validator를 bean으로 등록해서 입력값을 검증

```java
@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        // 비즈니스 로직에 위배되는 경
        // 무제한 경매가 아닌데 basePrice > maxPrice인 경우 잘못됨
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong."); //에러를 담아줌
            errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong."); //에러를 담아줌
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
                endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
        }

        // TODO BeginEventDateTime
        // TODO CloseEnrollmentDateTime
    }
}
```



```java
@PostMapping()
public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
    if (errors.hasErrors()) {
        return ResponseEntity.badRequest().build();
    }

    eventValidator.validate(eventDto, errors);
    if (errors.hasErrors()) {
        return ResponseEntity.badRequest().build();
    }
```



---

#### 질문

> 스프링부트 버전 2.3.0에서 starter web에 디펜던시로 spring-boot-starter-validation가 제외된 이슈와 관련하여
>
> 직접 "javax.validation"을 의존성 설정을 해줌으로써 @NotEmpty와 같은 어노테이션을 사용할 수 있었습니다.
>
> 
>
> 하지만 추가적인 문제가 발생했는데요.
>
> EventDto class의 특정 필드에 @NotEmpty, @NotNull과 같은 어노테이션을 설정했음에도 불구하고 유효성 검사가 정상적으로 이루어지지 않고 있습니다.(동작하지 않는 것 같습니다.)
>
> 
>
> "javax.validation"외 추가적인 의존성 설정이 필요한 것인지 궁금합니다.
>
> 
>
> [참고자료]
>
> https://stackoverflow.com/questions/8756768/annotations-from-javax-validation-constraints-not-working
>
> 위 참고자료를 보면,
>
> 저 같이 "javax.validation"이 아닌 "spring-boot-starter-validation"에 대해서 의존성 설정을 추가하라는 제안이 있었는데요.
>
> 
>
> 실제로 유효성 검증을 처리하여 Errors(또는 BindingResult)에 결과를 넘겨주는 라이브러리가 "spring-boot-starter-validation"에는 추가로 존재하는 것일까요?

스프링 부트 2.3부터 validation이 별도의 모듈로 분리가 되서 그런데요. spring-boot-starter-validation만 추가하시면 JSR-303애노테이션을 쓸 수 있고, 그와 관련 자동 설정은 여전히 스프링 부트 코어에서 제공하기 때문에 validation 처리가 되는게 맞습니다.

@Valid를 사용하지 않으셨거나, 에러처리를 안한건 아닌지 다시 살펴보시기 바랍니다.

스프링부트 버전 2.3.0부터는 starter web에 디펜던시로 spring-boot-starter-validation  이 제외되서, 별도로 추가해야만 했던 문제였습니다.

참고 링크 : https://github.com/spring-projects/spring-boot/issues/19550

참고 영상 : https://www.youtube.com/watch?v=cP8TwMV4LjE

> 제가 기존에 알고있던 validator를 구현하는 방식은 Validator 인터페이스를 구현한 클래스를 생성한 후 WebMvcConfigurer.getValidator 메소드를 오버라이딩 하는 방법과 controller 클래스에 @InitBinder 에노테이션이 선언된 메소드에서 WebDataBinder.addValidator 호출 하는 방법 두가지를 알고 있습니다.
>
> ```java
> // global validator 등록
> class MvcConfig implement WebMvcConfigure {
>    @Override
>    public Validator getValidator() {
>       return new EventValidator();
>    }
> }
> 
> // local validator 등록
> @RestController
> class EventController {
>    @InitBinder
>    public void initBinder(WebDataBinder binder) {
>       binder.addValidator(new EventValidator());
>    }
> }
> ```
>
> 하지만 첫번째 방법의 경우 다른 커맨드 객체를 검증하기 어려운 단점이 있고 두번째 방법은 요청 메소드가 호출될때마다 validator가 매번 생성되는 것으로 알고 있습니다.
>
> 혹시 강사님은 custom validation을 구현할때 3가지 방식 중 어떤 방식을 선호하고 왜 선호하는지 알 수 있을까요?

보통은 validator가 특정 전역적으로 쓰이기 보다는 특정 도메인 관련해서 쓰이는 경우가 많아서 저는 여기서 보여드린대로 빈으로 등록해서 필요할 곳에서 주입 받아서 직접 쓰거나, 아니면 특정 컨트롤러 내부에서 initBinder를 통해 등록해서 쓰는데요. 가능하면 initBinder에 묶어서 쓰는게 더 편하더라구요. 아무래도 직접 validator를 호출해야 하는 코드도 줄어드니까요.

> Errors 와 BindingResult 중 무엇을 쓰는게 더 좋을까요?

Errors가 더 상위 인터페이스이고 대부분의 경우엔 Errors를 써도 무방합니다.

BindingResult API를 읽어보시고 그안에 있는 메소드가 필요한 경우에만 BindingResult를 쓰는게 좋겠습니다.
 https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/validation/BindingResult.html

