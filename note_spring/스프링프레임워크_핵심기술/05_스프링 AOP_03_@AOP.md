# 스프링 AOP: @AOP

애노테이션 기반의 스프링 @AOP

의존성 추가

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

애스팩트 정의
	● @Aspect
	● 빈으로 등록해야 하니까 (컴포넌트 스캔을 사용한다면) @Component도 추가.

포인트컷 정의
	● @Pointcut(표현식)
	● 주요 표현식
			○ execution
			○ @annotation
			○ bean
	● 포인트컷 조합
			○ &&, ||, !

어드바이스 정의
	● @Before
	● @AfterReturning
	● @AfterThrowing
	● @Around
참고
	● https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#aoppointcuts

---

## 애노테이션 기반의 스프링 @AOP

#### 1. 의존성 추가

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```



#### 2. 애스팩트 정의

*  @Aspect
* 빈으로 등록해야 하니까 (컴포넌트 스캔을 사용한다면) @Component도 추가.

```java
@Component
@Aspect
public class PerAspect { ... }
```



두가지 정보가 필요하다. `Advice(해야할 일)`, `Pointcut(해야할일을 적용할 곳)`



#### 3. 어드바이스 정의

* ProceedingJoinPoint : Advice가 적용되는 대상 - SimpleEventService.createEvent(), SimpleEventService.publishEvent()

* `proceed()` : 메서드를 실행하는 것 (java reflection api)

  메서드가 실행되면서 에러가 발생할 수 있으니 `throws Throwable` 추가

타겟에 해당하는 메서드를 호출하고, 그 결과값을 리턴

단순히 메서드 수행 시간만 측정해주는 코드를 추가

```java
public Object logPerf(ProceedingJoinPoint pjp) throws Throwable {
  long begin = System.currentTimeMillis();
  Object retVal = pjp.proceed(); //메서드 실행
  System.out.println("수행시간 : " + (System.currentTimeMillis() - begin));
  return retVal;
}
```



## 포인트컷 정의 - (1) execution으로 포인트컷 정의

* 정의한 Advice를 어떻게 적용할 것인지 어노테이션으로 지정
* value값으로 포인트컷을 직접 주거나 포인트컷을 정의할 수 있다.

* `execution()` : 포인트컷 표현식. 어디에 적용할지 정의할 수 있다.

* 포인트 컷을 직접 적용

  포인트 컷을 다른 어드바이스에서 재사용하지 않을 것이라면 직접 적용해서 쓰면 된다.

  ex) dev.solar.demospring51 패키지 하위에 존재하는 EventService의 모든 메서드에 Advice를 적용

```java
@Around("execution(* dev.solar..*.EventService.*(..))")
```



* 포인트 컷을 지정하면 IDE에서 이 Advice가 어디에 적용되는지 보여준다.

  ![포인트컷지정](https://i.imgur.com/k16lC0t.png)
  
  
  
  적용되는 메서드로 가보면, 어떤 Advice가 적용되는지 쉽게 확인할 수 있다.
  
  ![포인트컷대상](https://i.imgur.com/q2DRWz4.png)



* @Around
  * 메서드를 감싸는 형태로 적용된다.
  * 그 메서드 호출 자체를 로직으로 감싸고 있기 때문에, 해당 메서드 호출 전/후에 로직을 처리할 수 있다.
  * 해당 메서드 호출 시 발생한 에러를 잡아서 에러 발생 시 특정 로직을 수행하게 할 수 있다.
* @Before
  * 메서드가 실행되기 전에 적용



전체 Aspect 코드

```java
@Component
@Aspect
public class PerAspect {

    @Around("execution(* dev.solar..*.EventService.*(..))")
    public Object logPerf(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.currentTimeMillis();
        Object retVal = pjp.proceed(); //메서드 실행
        System.out.println("수행시간 : " + (System.currentTimeMillis() - begin));
        return retVal;
    }
}
```

실행 결과

```
Created an event
수행시간 : 1015
Published an event
수행시간 : 2001
Deleted an event
수행시간 : 0
```



## 포인트컷 정의 - (2) @annotation 으로 포인트컷 정의

* 적용대상을 쉽게 바뀔 수 있다.

ex) 이 패키지의 모든 클래스의 모든 메서드에 적용

```java
@Around("execution(* dev.solar..*.*(..))")
```



> EventService.deleteEvent() 메서드에는 적용하고 싶지 않다면?

`execution`보다 애노테이션으로 적용하면 편리하다.

애노테이션으로 적용하기 위해서는 약간의 코드 변경 필요



#### 1. 애노테이션 생성

*  `@Retention`

★ 주의사항 ★

* RetentionPolicy를 줄때, `CLASS`이상으로 지정해야 한다.
* 기본값이 CLASS이므로 따로 지정하지 않고 사용해도 괜찮지만, 잘 모를 수 있기 때문에 명시적으로 써주는 것도 좋음
* RetentionPolicy
  * 이 애노테이션 정보를 얼마나 유지할 것인가.
  * default 설정 - CLASS
  * CLASS
    * class 파일까지 유지하겠다.
    * 즉, 컴파일하여 나온 `.class`파일에도 이 애노테이션 정보가 남아있다는 의미
  * SOURCE
    * 컴파일하고 나면 사라진다.
  * RUNTIME
    * (지금 굳이 runtime까지 유지할 필요는 없음)



*  `@Target` : Pointcut 대상 정도는 알려줘도 좋다.

*  `@Documented`  : Java Doc 문서를 만들 때 Doc이 되도록 어노테이션 추가해도 좋음

```java
@Documented
@Target(ElementType.METHOD) //메서드 대상
@Retention(RetentionPolicy.CLASS)
public @interface PerLogging { }
```



#### 2. 성능을 측정하고자 하는 메서드에 애노테이션 추가

기본적으로 이 애노테이션을 사용하면, 바이트코드까지 남아있게 된다.

```java
@PerLogging // <-- 추가
@Override
public void createEvent() { ... }

@PerLogging // <-- 추가
@Override
public void publishEvent() { ... }
```



#### 3. Aspect에 Pointcut 정의

* `@annotation()`

ex ) `PerLogging` 애노테이션이 붙어있는 메서드를 대상으로 이 Advice를 적용해라

```java
@Around("@annotation(PerLogging)")
```



실행 결과

⇒ createEvent()와 publishEvent()에는 적용되고, deleteEvent()에는 적용되지 않아서 수행시간이 출력되지 않는다.

```
Created an event
수행시간 : 1013
Published an event
수행시간 : 2004
Deleted an event
```



### @annotation 방식 장점

* execution은 포인트컷 조합 (&&, ||, !)을 할 수는 있지만 불편
  * execution 어드바이스를 2개 만들고, 중복되는 코드를 extract 메서드로 빼내는 방법
* 대상이 여러 곳에 흩어질 때는 각 대상에 애노테이션을 붙이는 것이 편리하다.
* IDE 툴의 지원을 받아서 어떤 Advice가 적용되는지 알 수 있지만, 툴의 지원을 못받는 경우 애노테이션을 통해서 Advice를 짐작할 수 있기 때문

```java
/*
 * 이 애노테이션을 사용하면 성능을 로깅해줍니다.
 */
@Documented
@Target(ElementType.METHOD) //메서드 대상
@Retention(RetentionPolicy.CLASS)
public @interface PerLogging {
}
```



## 포인트컷 정의 - (3) bean

* 지정한 bean이 가지고 있는 모든 public 메서드에 적용된다.

```java
@Around("bean(simpleEventService)")
public Object logPerf(ProceedingJoinPoint pjp) throws Throwable { ... }
```

```
Created an event
수행시간 : 1013
Published an event
수행시간 : 2004
Deleted an event
수행시간 : 0
```



* @Before

  메서드 실행 전에 hello 출력

```java
@Before("bean(simpleEventService)")
public void hello() {
  System.out.println("hello");
}
```

```
hello
Created an event
수행시간 : 1014
hello
Published an event
수행시간 : 2003
hello
Deleted an event
수행시간 : 0
```











