# SpEL (스프링 Expression Language)

[스프링 EL](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions)이란?
	● 객체 그래프를 조회하고 조작하는 기능을 제공한다.
	● [Unified EL](https://docs.oracle.com/javaee/5/tutorial/doc/bnahq.html)과 비슷하지만, 메소드 호출을 지원하며, 문자열 템플릿 기능도 제공한다.
	● OGNL, MVEL, JBOss EL 등 자바에서 사용할 수 있는 여러 EL이 있지만, SpEL은 모든 스프링 프로젝트 전반에 걸쳐 사용할 EL로 만들었다.
	● 스프링 3.0 부터 지원.

SpEL 구성
	● ExpressionParser parser = new SpelExpressionParser()
	● StandardEvaluationContext context = new StandardEvaluationContext(bean)
	● Expression expression = parser.parseExpression(“SpEL 표현식”)
	● String value = expression.getvalue(context, String.class)

문법
	● #{“표현식"}
	● ${“프로퍼티"}
	● 표현식은 프로퍼티를 가질 수 있지만, 반대는 안 됨.
			○ #{${my.data} + 1}
	● [레퍼런스](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions-language-ref) 참고

실제로 어디서 쓰나?
	● @Value 애노테이션
	● @ConditionalOnExpression 애노테이션
	● [스프링 시큐리티](https://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html)
			○ 메소드 시큐리티, @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter
			○ XML 인터셉터 URL 설정
			○ ...
	● [스프링 데이터](https://spring.io/blog/2014/07/15/spel-support-in-spring-data-jpa-query-definitions)
			○ @Query 애노테이션
	● [Thymeleaf](https://blog.outsider.ne.kr/997)
	● ..

---

## 문법

#### 1. 표현식 사용 #{}

`@Value` 어노테이션에 `#{}` 를 사용하면 Expression(표현식)을 사용할 수 있다.

```java
// 표현식 : 1 + 1 결과값을 value 변수에 저장
@Value("#{1 + 1}")
int value;
```



빈이 만들어질 때, @Value 안에 `#{}`을 쓰면 이를 표현식으로 인식하고, 사용한 표현식 값을 SpEL로 파싱해서 평가해서 결과값을 변수에 넣어준다.

@Value 어노테이션에 값(value)만 넣어도 된다.



#### 2. 프로퍼티 사용 ${}

프로퍼티를 참고하는 방식

스프링부트 애플리케이션의 경우, `application.properties`파일에 내용을 적어준다.

```properties
#application.properties
my.value = 100
```

```java
//사용
@Value("${my.value}")
int myValue;
```



★ 주의사항

* 표현식 안에는 프로퍼티 참조 방식을 사용할 수 있지만, 프로퍼티 내에서는 표현식을 사용할 수 없다.

* 표현식 내에서 프로퍼티를 감싸서 가지고 있는 것은 가능

```java
@Value("#{${my.value} eq 100}") //표현식 내에서 프로퍼티를 감싸서 가지고 있는 것은 가능
boolean isMyValue100;
```



ApplicationRunner를 생성해서 확인

```java
@Component
public class AppRunner implements ApplicationRunner {

    @Value("#{1 +1}")
    int value;

    @Value("#{'hello ' + 'world'}") //문자열은 싱글쿼터(') 사용
    String greeting;

    @Value("#{1 eq 1}")
    boolean trueOrFalse;

    @Value("haha")
    String haha;

    @Value("${my.value}") //프로퍼티 사용
    int myValue;

    @Value("#{${my.value} eq 100}") //표현식 내에서 프로퍼티를 감싸서 가지고 있는 것은 가능
    boolean isMyValue100;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(value);
        System.out.println(greeting);
        System.out.println(trueOrFalse);
        System.out.println(haha);
        System.out.println(myValue);
        System.out.println(isMyValue100);
    }
}
```

```
2
hello world
true
haha
100
true
```



#### 그 외 레퍼런스 참고

* [레퍼런스](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions-language-ref) 참고

* Literal Expressions
  표현식에 문자열이 그대로 값으로 대입

  ```java
  @Value("#{'문자열 그대로'}")
  String text;
  ```

* Properties, Arrays, List, Maps, Indexers 지원

  ```java
  // evals to 1856
  int year = (Integer) parser.parseExpression("Birthdate.Year + 1900").getValue(context);
  
  String city = (String) parser.parseExpression("placeOfBirth.City").getValue(context);
  ```

  

#### 3. 빈에 있는 정보 참고

1. sample 빈 생성

   ```java
   @Component
   public class Sample {
       private int data = 200;
     // ... getter, setter
   }
   ```

   

2. 가져와서 사용

   ```java
   @Value("#{sample.data}")
   int sampleData;
   ```

   

##### 메서드 호출하는 기능

* 레퍼런스 참고

---

## SpEL 기능이 쓰이는 곳

* @Value 애노테이션
* @ConditionalOnExpression 애노테이션
*  [스프링 시큐리티](https://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html)
  * 메소드 시큐리티, @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter
  * XML 인터셉터 URL 설정
*  [스프링 데이터](https://spring.io/blog/2014/07/15/spel-support-in-spring-data-jpa-query-definitions)
  		○ @Query 애노테이션
*  [Thymeleaf](https://blog.outsider.ne.kr/997)



#### @ConditionalOnExpression

@ConditionalOn 어노테이션은 선택적으로 빈을 등록하거나 빈설정파일을 읽어들일 때 사용

SpEL을 기반으로 선별적으로 빈을 등록할 수 있다.



#### 스프링 시큐리티

xml의 경우 `hasRole()`, `hasIpAddress()`로 사용할 수 있다.

 `hasRole()`, `hasIpAddress()`함수들은 `EvaluationContext`에서 오는 것이다. 

EvaluationContext로 빈을 만들어주면 해당 빈이 제공하는 함수들을 쓸 수 있다.

```xml
<http use-expressions="true">
  <intercept-url pattern="/admin*"
                 access="hasRole('admin') and hasIpAddress('192.168.1.0/24')"/>
  ...
</http>
```

`@PreAuthorize` 어노테이션으로 쓸 수 있다.

```java
@PreAuthorize("hasRole('ROLE_USER')")
public void create(Contact contact);
```



#### 스프링 데이터

`@Query` 어노테이션

파라미터 값을 메서드 인자로 받은 도메인의 필드 값을 참고해서 쓸 수 있다.

```java
@Query("select u from User u where u.firstname = :#{#customer.firstname}")
List<User> findUsersByCustomersFirstname(@Param("customer") Customer customer);
```



인덱스(위치)기반으로도 사용 가능

```java
@Query("select u from User u where u.age = ?#{[0]}")
List<User> findUsersByAge(int age);
```



SpEL의 가장 일반적인 시나리오는 보안 제한 조건의 정의입니다. 따라서 현재 인증 된 사용자와 관련된 결과 만 반환하도록 쿼리를 제한 할 수 있다면 좋을 것입니다

```java
@Query("select u from User u where u.emailAddress = ?#{principal.emailAddress}")
List<User> findCurrentUserWithCustomQuery();
```

---

## ExpressionParser

필요하다면 ExpressionParser를 코딩으로 사용할 수 있다.

`parser.parseExpression()`가 이미 표현식이므로 인자값으로 `#{}`안의 내용만 넣어주면 된다. 

```java
@Component
public class AppRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("2 + 100"); //표현식 정의
        Integer value = expression.getValue(Integer.class);//표현식으로 가져올 데이터 타입
        System.out.println("value = " + value);
    }
}
```

```
value = 102
```



SpEL도 해당하는 타입으로 형변환을 위해 `ConversionService`을 사용하는 것이다.

```java
Integer value = expression.getValue(Integer.class);
```

