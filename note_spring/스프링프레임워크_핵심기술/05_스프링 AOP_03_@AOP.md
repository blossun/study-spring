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



#### 4. 포인트컷 정의

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



#### 5. Aspect 대상 지정

* 적용대상을 쉽게 바뀔 수 있다.

ex) 이 패키지의 모든 클래스의 모든 메서드에 적용

```java
@Around("execution(* dev.solar..*.*(..))")
```



* EventService.deleteEvent() 메서드에는 적용하고 싶지 않다면?

  























