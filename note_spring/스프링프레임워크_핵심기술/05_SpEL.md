# SpEL (스프링 Expression Language)

스프링 EL이란?
	● 객체 그래프를 조회하고 조작하는 기능을 제공한다.
	● Unified EL과 비슷하지만, 메소드 호출을 지원하며, 문자열 템플릿 기능도 제공한다.
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
	● 레퍼런스 참고

실제로 어디서 쓰나?
	● @Value 애노테이션
	● @ConditionalOnExpression 애노테이션
	● 스프링 시큐리티
			○ 메소드 시큐리티, @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter
			○ XML 인터셉터 URL 설정
			○ ...
	● 스프링 데이터
			○ @Query 애노테이션
	● Thymeleaf
	● ..

---

### 1. 표현식 사용 #{}

**※ [실습]**

`@Value` 어노테이션에 `#{}` 를 사용하면 Expression(표현식)을 사용할 수 있다.

```java
// 표현식 : 1 + 1 결과값을 value 변수에 저장
@Value("#{1 + 1}")
int value;
```



빈이 만들어질 때, @Value 안에 사용한 표현식 값을 SpEL로 파싱해서 평가해서 결과값을 변수에 넣어준다.

`#{}`을 쓰면 이를 표현식으로 인식하고, 표현식을 평가한 다음에 실행해서 결과값을 프로퍼티에 넣어준다.

@Value 어노테이션에 값(value)만 넣어도 된다.



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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("================= ");
        System.out.println(value);
        System.out.println(greeting);
        System.out.println(trueOrFalse);
        System.out.println(haha);
    }
}
```

```
================= 
2
hello world
true
haha
```



### 2. 프로퍼티 사용 ${}

**※ [실습]**

프로퍼티를 참고하는 방식

스프링부트 애플리케이션의 경우, 



























