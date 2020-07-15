# 스프링 AOP: 프록시 기반 AOP

스프링 AOP 특징
	● **프록시 기반의 AOP** 구현체
	● **스프링 빈에만 AOP를 적용**할 수 있다.
	● 모든 AOP 기능을 제공하는 것이 목적이 아니라, 스프링 IoC와 연동하여 엔터프라이즈 애플리케이션에서 가장 흔한 문제에 대한 해결책을 제공하는 것이 목적.

프록시 패턴
	● 왜? (기존 코드 변경 없이) 접근 제어 또는 부가 기능 추가

​	![프록시패턴](https://i.imgur.com/Y0e5KEp.png)

​	● 기존 코드를 건드리지 않고 성능을 측정해 보자. (프록시 패턴으로)

문제점
	● 매번 프록시 클래스를 작성해야 하는가?
	● 여러 클래스 여러 메소드에 적용하려면?
	● 객체들 관계도 복잡하고...

그래서 등장한 것이 스프링 AOP
	● 스프링 IoC 컨테이너가 제공하는 기반 시설과 Dynamic 프록시를 사용하여 여러 복잡한 문제 해결.
	● 동적 프록시: 동적으로 프록시 객체 생성하는 방법
			○ 자바가 제공하는 방법은 인터페이스 기반 프록시 생성.
			○ CGlib은 클래스 기반 프록시도 지원.
	● 스프링 IoC: 기존 빈을 대체하는 동적 프록시 빈을 만들어 등록 시켜준다.
			○ 클라이언트 코드 변경 없음.
			○ [AbstractAutoProxyCreator](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/aop/framework/autoproxy/AbstractAutoProxyCreator.html) implements [BeanPostProcessor](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/beans/factory/config/BeanPostProcessor.html)

---

## 프록시 패턴

인터페이스가 있고, 클라이언트는 인터페이스 타입으로 프록시 객체를 사용하게 된다. 프록시 객체는 Target객체를 참조하고 있다.  Proxy객체와 Target객체(Real Subject)는 같은 타입, 원래 해야할 일은 Real Subject에 있고, Proxy 객체가 Real Subject를 감싸서 실제 요청을 처리하게 된다.

![프록시패턴](https://i.imgur.com/Y0e5KEp.png)

> 그런 기능을 사용하는 이유?

접근제어, 부가 기능 추가의 용도



## 기존 코드를 건드리지 않고 성능을 측정해 보자. (프록시 패턴으로)

**※ [실습]**

#### 1. <interface>  Subject 생성

* EventServcie 인터페이스 생성

```java
public interface EventService { //Interface Subject
    void createEvent();
    void publishEvent();
}
```



#### 2. Real Subject 생성

* EventServcie 인터페이스 구현체인 SimpleEventService 생성

사용하기 위해서 빈으로 등록

```java
@Service
public class SimpleEventService implements EventService{ //Real Subject
    @Override
    public void createEvent() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Created an event");
    }

    @Override
    public void publishEvent() {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("Published an event");
    }

    @Override
    public void deleteEvent() {
      System.out.println("Deleted an event");
    }
}
```



#### 3. Client 코드 구현

* AppRunner로 프록시 패턴 사용

★ 인터페이스를 사용하는 경우에는 `인터페이스 타입으로 주입`받는 것을 권장한다. ★

```java
@Component
public class AppRunner implements ApplicationRunner { //Client
    @Autowired
    EventService eventService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        eventService.createEvent();
        eventService.publishEvent();
        eventService.deleteEvent();
    }
}
```

실행결과

```
2020-07-15 12:20:23.992  INFO 28893 --- [           main] d.s.d.Demospring51Application            : Started Demospring51Application in 2.25 seconds (JVM running for 2.939)
Created an event
Published an event
Deleted an event
```



#### 4. Crosscutting Concerns (흩어진 관심사) 코드 추가

Real Subject의 메서드에서 직접 실행시간을 측정하는 코드를 추가할 수 있다.

```java
@Service
public class SimpleEventService implements EventService{ //Real Subject
    @Override
    public void createEvent() {
        long begin = System.currentTimeMillis(); // <-- 중복 코드(1) 
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Created an event");

        System.out.println("수행시간 : " + (System.currentTimeMillis() - begin)); // <-- 중복 코드(2)
    }

    @Override
    public void publishEvent() {
        long begin = System.currentTimeMillis(); // <-- 중복 코드(1)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Published an event");

        System.out.println("수행시간 : " + (System.currentTimeMillis() - begin)); // <-- 중복 코드(2)
    }
  
    // Aspect 적용 제외 대상
    @Override
    public void deleteEvent() {
        System.out.println("Deleted an event");
    }
}
```

```
Created an event
수행시간 : 1004
Published an event
수행시간 : 2004
Deleted an event
```



#### 5. 프록시 적용

* Real Subject와 Client 코드를 수정하지 않고 Real Subject의 createEvent(), publishEvent() 메서드(deleteEvent() 제외)의 실행시간을 측정하는 기능 추가

(1) Proxy 클래스 생성

* 클래스의 타입이 Subject와 동일해야 한다. 즉 같은 인터페이스를 구현해야 한다.

* `@Autowired`로 빈 등록

* `@Primary` 빈으로 등록. 동일한 타입의 빈이 다수개 있다면 우선순위가 높도록 지정

* (이론적으로는) `인터페이스 타입의 빈`을 주입받는 것을 권장하지만,

  프록시 같은 경우는 `Real Subject`빈을 주입받아서 사용해야한다.

  ```java
  // 방법1. 타입을 명시적으로 선언
  @Autowired
  SimpleEventService simpleEventService;
  // 방법2. 빈의 이름을 기반으로 해서 SimpleEventService 타입을 주입받음
  @Autowired
  EventService simpleEventService;
  ```

  

* 기본 동작코드는 위임(Delegation) 하도록 한다.

```java
@Primary
@Service
public class ProxySimpleEventService implements EventService{

    @Autowired
    SimpleEventService simpleEventService; // 방법1. 타입을 명시적으로 선언
//    EventService simpleEventService; // 방법2. 빈의 이름을 기반으로 해서 SimpleEventService 타입을 주입받음

    @Override
    public void createEvent() {
        long begin = System.currentTimeMillis();
        simpleEventService.createEvent(); //Delegate
        System.out.println("실행시간 : " + (System.currentTimeMillis() - begin));
    }

    @Override
    public void publishEvent() {
        long begin = System.currentTimeMillis();
        simpleEventService.publishEvent(); //Delegate
        System.out.println("실행시간 : " + (System.currentTimeMillis() - begin));
    }

    @Override
    public void deleteEvent() {
        simpleEventService.deleteEvent();
    }
}
```



Client에서 EventService를 주입받지만 `ProxySimpleEventService`에 `@Primary`로 우선순위를 높게 지정했기 때문에 프록시빈을 가져다 쓰게 되는 것이다.

```java
@Component
public class AppRunner implements ApplicationRunner { //Client
    @Autowired
    EventService eventService; //<-- ProxySimpleEventService 빈 주입

    @Override
    public void run(ApplicationArguments args) throws Exception {
        eventService.createEvent();
        eventService.publishEvent();
        eventService.deleteEvent();
    }
}
```



실행결과

```
Created an event
실행시간 : 1003
Published an event
실행시간 : 2004
Deleted an event
```



## 스프링 부트에서 Non 서버 모드로 애플리케이션 실행하기

SpringApplication을 기본적으로 Web Application으로 띄워주는데, Web Application으로 띄우지 않고, 서버 모드가 아닌 그냥 Java main 메서드 실행하듯이 앱을 실행시키도록 코드 수정

→ 이전 프로젝트 모두 이런 방식으로 실행시키는 것이 이상적이다. 굳이 웹서를 띄울 필요가 없기 때문에...!  속도가 더 빠름

```java
@SpringBootApplication
public class Demospring51Application {

    public static void main(String[] args) {
        //Web Application으로 실행하지 않고, Java main 메서드 실행시킴 (서버 모드 OFF)
        SpringApplication app = new SpringApplication(Demospring51Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
//        SpringApplication.run(Demospring51Application.class, args);
    }
}
```



## 프록시 적용 코드 문제점

* 프록시 클래스 내에서도 중복코드가 생긴다.

* 이러한 프록시 클래스를 만드는데 드는 비용이 적지 않다. 기본 메서드를 위임(delegation)해줘야한다.

* 프록시에서 추가한 기능(메서드 수행 시간 측정 기능)을 (EventService가 아닌) 다른 클래스에도 적용해줘야 한다면?

  적용 대상 클래스마다 프록시 클래스를 모두 만들고, 그 프록시에 중복된 코드를 심어야 하는 거 아닌가.... 비효율적

* 줄일 수 있는 방법

  * ----- 개선 ---→  다이나믹 프록시 적용 -----개선 ---→ 스프링의 Auto Proxy 팩토리빈 적용 ---- 개선 ---→ 스프링 AOP
  * (이부분 너무 깊이있게 들어가지 않고 말로만 설명)



프록시를 클래스로 만들어서 컴파일해서 썼다. 동적으로 프록시 객체를 만드는 방법이 있다.

여기서 `동적`은 런타임 즉, 애플리케이션이 동작하는 중에 동적으로 어떤 객체를 감싸는 프록시 객체를 만드는 방법이 있다.

그 방법을 기반으로 스프링 IoC 컨테이너가 제공하는 방법과 혼합해서 사용해서 이 문제를 심플하게 해결한다.

"프록시를 클래스마다 만들어야된다.", "프록시내에 중복코드 발생"이라는 단점들을 해결한다. 이것이 스프링 AOP이다.



## 스프링 AOP

* 스프링 IoC 컨테이너가 제공하는 기반 시설과 Dynamic 프록시를 사용하여 여러 복잡한 문제 해결.
* 동적 프록시: 동적으로 프록시 객체 생성하는 방법
  * 자바가 제공하는 방법은 인터페이스 기반 프록시 생성.
  * CGlib은 클래스 기반 프록시도 지원.
* 스프링 IoC: 기존 빈을 대체하는 동적 프록시 빈을 만들어 등록 시켜준다.
  * 클라이언트 코드 변경 없음.
  * [AbstractAutoProxyCreator](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/aop/framework/autoproxy/AbstractAutoProxyCreator.html) implements [BeanPostProcessor](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/beans/factory/config/BeanPostProcessor.html)



스프링 IoC에는 `BeanPostProcessor`가 있다. 어떤 빈이 등록이 되면 그 빈을 가공할 수 있는 라이프사이클 인터페이스이다.

이 경우 스프링 AOP가 적용되는 로직은 SimpleEventService가 빈으로 등록이 되면 스프링이 `AbstractAutoProxyCreator`라는 

`BeanPostProcessor`로 이 SimpleEventService라는 빈을 감싸는 프록시빈을 생성해서 그 빈을 SimpleEventService빈 대신에 등록해준다.

깊이있는 공부를 하고 싶다면, 토비의 스프링3에 자세히 설명되어있다.



**※ BeanPostProcessor**

새로운 빈 인스턴스를 조작할 수 있는 기능 제공

[참고](/Users/ssun/Develop/Project/SpringProjects/study-spring/note_spring/스프링프레임워크_핵심기술/01_IoC 컨테이너와 빈_04.md)

