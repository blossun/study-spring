# Null-safety

스프링 프레임워크 5에 추가된 Null 관련 애노테이션
	● @NonNull
	● @Nullable
	● @NonNullApi (패키지 레벨 설정)
	● @NonNullFields (패키지 레벨 설정)

목적
	● (툴의 지원을 받아) 컴파일 시점에 최대한 NullPointerException을 방지하는 것

---



**※ [실습]**

Null 체크를 하려는 곳에 `@NonNull` 애노테이션 추가

```java
@Service
public class EventService {

    @NonNull //리턴값을 체크. null 허용하지 않음
    public String createEvent(@NonNull String name) { //인자값을 체크. null 허용하지 않음
        return "hello " + name;
    }
}
```



null을 허용하지 않는 필드에 null을 넣어서 체크하는지 확인해보자

```java
@Component
public class AppRunner implements ApplicationRunner {
    @Autowired
    EventService eventService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        eventService.createEvent(null); //<-- null을 허용하지 않으므로 에러가 나야 한다.
    }
}
```



⇒ 실행하면 별다른 오류가 나지 않는다. `@NonNull` 했는데 ??



Compiler 설정을 확인해보면 Spring 관련 애노테이션이 없다. 이를 추가해줘야 한다.

![기본애노테이션확인](https://i.imgur.com/tFgcjSh.png)



스프링 애노테이션 추가

* org.springframework.lang.Nullable
* org.springframework.lang.NonNull

![스프링 애노테이션 추가](https://i.imgur.com/FCofxxK.png)



설정 추가 후, 재빌드만 하면 적용 안 됨. IntelliJ를 재시작



![null 체크](https://i.imgur.com/PjZc08j.png)

⇒ "`@NotNull`이 붙은 곳에 null을 넣으려고 하고있다."라고 알려준다.















