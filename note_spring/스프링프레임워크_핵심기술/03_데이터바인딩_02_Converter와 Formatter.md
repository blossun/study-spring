# 데이터 바인딩 추상화: Converter와 Formatter

[Converter](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/convert/converter/Converter.html)
	● S 타입을 T 타입으로 변환할 수 있는 매우 일반적인 변환기.
	● 상태 정보 없음 == Stateless == 쓰레드세이프
	● [ConverterRegistry](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/convert/converter/ConverterRegistry.html)에 등록해서 사용

```java
public class StringToEventConverter implements Converter<String, Event> {
  @Override
  public Event convert(String source) {
    Event event = new Event();
    event.setId(Integer.parseInt(source));
    return event;
  }
}
```

[Formatter](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/format/Formatter.html)
	● PropertyEditor 대체제
	● Object와 String 간의 변환을 담당한다.
	● 문자열을 Locale에 따라 다국화하는 기능도 제공한다. (optional)
	● [FormatterRegistry](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/format/FormatterRegistry.html)에 등록해서 사용

```java
public class EventFormatter implements Formatter<Event> {
  @Override
  public Event parse(String text, Locale locale) throws ParseException {
    Event event = new Event();
    int id = Integer.parseInt(text);
    event.setId(id);
    return event;
  }
  @Override
  public String print(Event object, Locale locale) {
    return object.getId().toString();
  }
}
```

[ConversionService](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/convert/ConversionService.html)
	● 실제 변환 작업은 이 인터페이스를 통해서 쓰레드-세이프하게 사용할 수 있음.
	● **스프링 MVC**, 빈 (value) 설정, SpEL에서 사용한다.
	● DefaultFormattingConversionService
	● FormatterRegistry
	● ConversionService
	● 여러 기본 컴버터와 포매터 등록 해 줌.



<img src="https://i.imgur.com/5dbVAu6.png" alt="converter formatter" style="zoom:50%;" />

스프링 부트 

​	● 웹 애플리케이션인 경우에 DefaultFormattingConversionSerivce를 상속하여 만든 **WebConversionService**를 빈으로 등록해 준다. 
​	● Formatter와 Converter 빈을 찾아 자동으로 등록해 준다

---

## Converter

Property가 가지고 있던 단점때문에 Converter가 생겼다.

Object와 String 간의 변환만할 수 있었던 Property와 달리 서로 다른 타입간의 변환이 가능하다.

Property가 가지고있던 상태정보를 Converter는 가지고 있지 않다.

#### 1. 컨버터 구현

source와 target을 매개변수로 Converter를 구현하면 된다. 

converter라는 메서드만 구현해주면 된다.

앞서 만든 PropertyEditor와 같은 기능을 한다. 단지, 상태정보가 없기 때문에 얼마든지 빈으로 등록해서 사용해도 상관없다.

```java
public class EventConverter {

    //String -> Event 타입변환
    @Component
    public static class StringToEventConverter implements Converter<String, Event> {
        @Override
        public Event convert(String source) {
            return new Event(Integer.parseInt(source));
        }
    }

    //Evnet -> String 타입변환
    @Component
    public static class EventToStringConverter implements Converter<Event, String> {
        @Override
        public String convert(Event source) {
            return source.getId().toString();
        }
    }
}
```



### 2. 빈등록

ConverterRegistry에 등록해서 사용하면 된다. 우리가 ConverterRegistry를 직접 쓸 일은 거의 없다. 

* 스프링부트없이 스프링 MVC를 쓴다면, WebConfig를 쓴다고 가정
  Web용 Configuration을 만든 코드에서 `addFormatters()`메서드를 오버라이딩해서 `registry.addConverter`로 컨버터를 레지스터리에 등록해서 사용하면 된다.

  ```java
  @Configuration
  public class WebConfig implements WebMvcConfigurer {

      @Override
      public void addFormatters(FormatterRegistry registry) {
          registry.addConverter(컨버터 등록); //<--
      }
  }
  ```



EventController에서 PropertyEditor를 사용하던 코드 삭제 후, 테스트 코드 실행

* EventController.java

  ```java
  @RestController
  public class EventController {
  
      @GetMapping("/event/{event}")
      public String getEvent(@PathVariable Event event) {
          System.out.println(event);
          return event.getId().toString();
      }
  }
  ```

* EventControllerTest.java

  ```java
  @RunWith(SpringRunner.class)
  @WebMvcTest
  public class EventControllerTest {
  
      @Autowired
      MockMvc mockMvc;
  
      @Test
      public void getTest() throws Exception {
          mockMvc.perform(MockMvcRequestBuilders.get("/event/1"))
                  .andExpect(status().isOk())
                  .andExpect(content().string("1"));
      }
  }
  ```

테스트 통과

```
Event{id=1, title='null'}
```

⇒ 컨트롤러에서 `/event/1`로 넘어온 url에서 `1`값을 String → Event 타입으로 형변환 하여 받을 수 있는 것이다.



Integer 타입같은 경우, 기본적으로 등록이 되어있는 컨버터나 포메터들이 자동을 변환을 해준다. 모든 타입을 컨버터를 만들지 않아도 된다.

따라서 스프링에 기본적으로 등록되어있지 않은 컨버터만 만들어서 사용하면 된다.

컨버터는 재너럴하다. 웹의 경우 거의 대부분의 경우 사용자 입력값이 문자열로 들어오고, 객체들을 문자로 내보내는 경우가 많다. 그런 문자들을 MessageSource(i18n)를 사용해서 다국화를 지원한다. (스프링에서 이 기능에 특화된 Formatter 인터페이스를 제공)



## Formatter

스프링이 조금 더 웹에 특화되어있는 인터페이스를 만들어서 제공한다. 





























