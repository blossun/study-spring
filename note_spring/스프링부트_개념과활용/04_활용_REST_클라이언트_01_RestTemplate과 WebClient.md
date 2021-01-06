# 스프링 REST 클라이언트 1부: RestTemplate과 WebClient

RestTemplate

* Blocking I/O 기반의 Synchronous API 
* RestTemplateAutoConfiguration
* 프로젝트에 spring-web 모듈이 있다면 RestTemplate**Builder**를 빈으로 등록해 줍니다.
* https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#rest-client-access

WebClient

* Non-Blocking I/O 기반의 Asynchronous API
* WebClientAutoConfiguration
* 프로젝트에 spring-webflux 모듈이 있다면 WebClient.**Builder**를 빈으로 등록해 줍니다.
* https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-client

※ 스프링이 제공하는 기능으로 스프링부트는 빈으로 등록해주기만 한다. RestTemplate과 WebClient를 빈으로 등록해주는 것이 아니라 **Builder**를 등록해주는 것. 따라서 빌더를 주입 받아서 필요할 때마다 빌드를 해서 인스턴스를 생성해서 사용해야한다.

* RestTemplate과 WebClient 둘을 동시에 사용해도 된다.

---

## RestTemplate

* Blocking I/O 기반의 Synchronous API 

(예제)

```java
@RestController
public class SampleController {

    @GetMapping("/hello")
    private String hello() throws InterruptedException {
        Thread.sleep(5000l); //5초 슬립
        return "hello";
    }

    @GetMapping("/world")
    private String world() throws InterruptedException {
        Thread.sleep(3000l); //3초 슬립
        return "world";
    }
}
```

```java
@Component
public class RestRunner implements ApplicationRunner {

    @Autowired
    RestTemplateBuilder restTemplateBuilder;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        RestTemplate restTemplate = restTemplateBuilder.build();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String helloResult = restTemplate.getForObject("http://localhost:8080/hello", String.class); //이 요청이 끝날 때까지 다음 라인으로 넘어가지 않는다. // 5초 슬립이 걸려있다.
        System.out.println(helloResult);

        String worldResult = restTemplate.getForObject("http://localhost:8080/world", String.class); //이 요청이 끝날 때까지 다음 라인으로 넘어가지 않는다. // 3초 슬립이 걸려있다.
        System.out.println(worldResult);

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }
}
```

→ 한 라인의 명령이 끝날 때까지 기다리면서 다음라인을 순차적으로 진행한다. 

결과 : 총 8초의 시간이 걸림

```
2021-01-06 11:42:21.907  INFO 13385 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 0 ms
hello
world
StopWatch '': running time = 8103483462 ns //<-- 총 8초의 시간이 걸림
---------------------------------------------
ns         %     Task name
---------------------------------------------
8103483462  100%  
```



## WebClient

* Non-Blocking I/O 기반의 Asynchronous API















