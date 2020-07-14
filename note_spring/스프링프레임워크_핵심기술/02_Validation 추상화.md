#  Validation 추상화

[org.springframework.validation.Validator](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/validation/Validator.html)

애플리케이션에서 사용하는 객체 검증용 인터페이스.

특징
	● 어떤한 계층과도 관계가 없다. => 모든 계층(웹, 서비스, 데이터)에서 사용해도 좋다.
	● 구현체 중 하나로, JSR-303(Bean Validation 1.0)과 JSR-349(Bean Validation 1.1)을 지원한다. ([LocalValidatorFactoryBean](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/validation/Validator.html))
	● DataBinder에 들어가 바인딩 할 때 같이 사용되기도 한다.

인터페이스
	● boolean supports(Class clazz): 어떤 타입의 객체를 검증할 때 사용할 것인지 결정함
	● void validate(Object obj, Errors e): 실제 검증 로직을 이 안에서 구현
			○ 구현할 때 ValidationUtils 사용하며 편리 함.

스프링 부트 2.0.5 이상 버전을 사용할 때
	● LocalValidatorFactoryBean 빈으로 자동 등록
	● JSR-380(Bean Validation 2.0.1) 구현체로 hibernate-validator 사용.
	● https://beanvalidation.org/

---

## Validator

스프링에서 사용하라고 만들었지만(?) 웹 계층에서만 사용하라고 만든 웹 계층 전용 Validator 개념은 아니다.

애플리케이션이 계층형 아키텍쳐를 사용하고 있다면 웹이든 서비스든 데이터레이어든 어떠한 계층이든 상관없이 모두 사용할 수 있는 일반적인 인터페이스이다.

Bean Validation 1.0, 1.1, 2.0.1 버전 까지도 지원한다.

Bean Validation이 제공하는 여러 Validation용 어노테이션을 사용해서 어떤 객체의 데이터를 검증할 수 있다.

Bean Validation은 자바 EE 표준 스팩이다. 

스프링이 제공하는 `Validator`인터페이스는 2가지 메서드를 구현해야 한다.

* **boolean supports(Class clazz)**

  * 인자로 넘어온 (검증하려는 인스턴스의) 클래스가 이 Validator가 검증을 할 수 있는 지원하는 Class인지 확인하는 메서드를 구현

* **void validate(Object target, Errors errors)**

  * 실제 검증이 이루어지는 로직 구현

  * ValidationUtils 사용



## 직접 Validator를 만들어서 검증 구현

* 원시적인 방식
* 복잡한 검증 로직 직접 구현 가능

**※ [실습]**

##### 1. 검증 대상이 될 클래스 생성

```java
public class Event {
    Integer id;
    String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
```



##### 2. Event 클래스를 검증할 Validator를 구현한 클래스 생성

* supports, validate 메서드 구현

1. supports

   파라미터로 넘겨받은 클래스가 Event 클래스인지 확인

2. validate

   Event의 title 필드값이 비어있거나 공백이 있는지 확인

   `ValidationUtils.rejectIfEmptyOrWhitespace(errors, filed, errorCode, defaultMessage );`

   ```java
   ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "notempty", );
   ```

   * errorCode : 메세지 리졸버

   ApplicationContext가 어떤 키값에 해당하는 메시지를 가져오는 인터페이스 역할도 한다.

   그러한 기능을 사용해서 실제 메시지를 가져올 수 있는 Message의 Key값에 해당하는 에러코드이다.

   * errorCode로 "notempty.title" 과 같이 `도트(.)`를 쓰지 않는 이유?
   * defaultMessage : 에러코드로 메시지를 찾지 못한 경우 사용할 메시지

```java
package dev.solar.demospring51;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class EventValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Event.class.equals(clazz); //파라미터로 넘겨받은 클래스가 Event 클래스인지 확인
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "notempty", "Empty title is not allowed!!");
    }
}
```



##### 3. Runner 구현

Errors를 BeanPropertyBindingResult 구현체로 생성

* target : 어떤 객체를 검사할지 대상
* objectName : 이름

이러한 과정들은 스프링 MVC에서 사용한다면 이런 값들은 자동으로 넘어가기 때문에 우리가 직접 이러한 클래스를 사용할 일은 거의 없다. 하지만 이 인터페이스는 자주보게 될 것이다.

Errors의 구현체인 `BeanPropertyBindingResult`라는 클래스는 스프링 MVC가 자동으로 생성해서 파라미터에 Errors 타입으로 전달해줄 것이기 때문에, 지금은 테스트를 위해서 직접 사용하지만 실제 사용할 일은 거의 없다.

```java
@Component
public class AppRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Event event = new Event();
        EventValidator eventValidator = new EventValidator();

        //Errors 필요
        Errors errors = new BeanPropertyBindingResult(event, "event");

        // 검증
        eventValidator.validate(event, errors);

        // error가 있는지 확인
        System.out.println(errors.hasErrors());
        // 모든 에러를 가져와서 에러를 순차적으로 순회하면서 에러코드를 출력
        errors.getAllErrors().forEach(e -> {
            System.out.println("===== error code =====");
            Arrays.stream(e.getCodes()).forEach(System.out::println);
            System.out.println(e.getDefaultMessage());
        });
    }
}
```

```
true
===== error code =====
notempty.event.title
notempty.title
notempty.java.lang.String
notempty				<== 내가 만든 에러메시지
Empty title is not allowed!!
```



생성한 event에 title 값을 넣지 않았기 때문에, 검증을 하면 에러가 날 것이다.

내가 만든 errorCode이외에 3가지가 더 추가적으로 생성된다. (notempty.event.title, notempty.title, 
notempty.java.lang.String)

그래서 `notempty.title`과 같이 만들지 않은 것이다.



#### Errors로 validate 구현

* 반드시 ValidationUtils를 이용해야되는 것은 아니다.

* 조건을 넣을 수 있다.

  1. Event 타입으로 형변환 한다.

  2. title이 null이면 errors.reject()로 직접 errorCode랑 Message를 담으면 된다.

     특정한 Field값에 해당하는 에러이면 reject() 인자로 Field를 적어준다.

     여러 Field값에 해당하는 에러이면 그냥 reject()를 적어준다. (객체 전반적인 대상의 에러인 경우)

```java
@Override
public void validate(Object target, Errors errors) {
  Event event = (Event)target;
  if (event.getTitle() == null) {
    errors.reject("notempty", "Empty title is not allowed!!");
  }
}
```

```
true
===== error code =====
notempty.event
notempty
Empty title is not allowed!!
```



## 어노테이션을 이용한 검증 구현

* 스프링 부트 2.0.5 이상 버전에서 기능 제공

  최근에는 Validator를 직접 사용하는 것이 아니라 (특히나 스프링 부트를 쓰고 있다면) 기본적으로 `스프링`이 제공해주는 `LocalValidatorFactoryBean`을 자동으로 빈으로 등록해준다.

* **LocalValidatorFactoryBean**

  * SR-380(**Bean Validation** 2.0.1) 구현체
  * Bean Validation 어노테이션들을 지원하는 Validator이다.
  * 이 Validator를 바로 사용하면 된다.



**※ [실습]**

##### 1. 의존성 추가

```xml
<dependency>
  <groupId>org.hibernate.validator</groupId>
  <artifactId>hibernate-validator</artifactId>
  <version>6.0.7.Final</version>
</dependency>
```



##### 2. 검증 대상이 되는 필드에 어노테이션 추가

* @NotNull
* @NotEmpty

* @Size : 컬렉션의 사이즈 검증
* @Min, @Max : 데이터 값 검증
* @Email : email 형식 검증

* defaultMessage도 Validator에서 기본적으로 제공해준다.

```java
import javax.validation.constraints.*;

public class Event {

    Integer id;

    @NotEmpty //<- 검증을 위한 어노테이션 추가
    String title;

    @NotNull @Min(0) @Max(100)
    Integer limit;

    @Email
    String email;

  	//... getter, setter
}
```



##### 3. Runner 구현

AppRunner에서 기존에 만든 EventValidator를 사용하지 않고, org.springframework.validation.Validator를 주입받아서 사용한다.

```java
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    Validator validator;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("validator : " + validator.getClass()); //어떠한 validator가 주입되는지 확인

        Event event = new Event();
        event.setLimit(-1); // 0이상 이어야 error 안 남
        event.setEmail("hahaha"); // email 형식에 맞지 않는 문자열

        Errors errors = new BeanPropertyBindingResult(event, "event");

        // 검증
        validator.validate(event, errors);

        // error가 있는지 확인
        System.out.println(errors.hasErrors());
        // 모든 에러를 가져와서 에러를 순차적으로 순회하면서 에러코드를 출력
        errors.getAllErrors().forEach(e -> {
            System.out.println("===== error code =====");
            Arrays.stream(e.getCodes()).forEach(System.out::println);
            System.out.println(e.getDefaultMessage());
        });
    }
}
```

```shell
validator : class org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
true
===== error code =====
Email.event.email
Email.email
Email.java.lang.String
Email
이메일 주소가 유효하지 않습니다. <-- Validator가 제공하는 default 메시지
===== error code =====
Min.event.limit
Min.limit
Min.java.lang.Integer
Min
반드시 0보다 같거나 커야 합니다.
===== error code =====
NotEmpty.event.title
NotEmpty.title
NotEmpty.java.lang.String
NotEmpty
반드시 값이 존재하고 길이 혹은 크기가 0보다 커야 합니다.
```



복잡한 비즈니스 검증 로직이 필요하다면 어노테이션이 아니라 직접 Validator를 구현해서 사용하면 된다.



⇒ 실습 코드를 깃헙에 업로드했더니 org.hibernate.validator가 취약하다고 알림이 왔다.

내용을 찾아보고 보안 업데이트된 라이브러리를 써야할 듯...!!

![org.hibernate.validator취약점](https://i.imgur.com/QUuuEYz.png)