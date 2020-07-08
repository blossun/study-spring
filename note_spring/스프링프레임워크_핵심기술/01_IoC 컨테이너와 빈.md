# IoC 컨테이너와 빈

## 1부 : 스프링 IoC 컨테이너와 빈

**Inversion of Control**

의존 관계 주입(Dependency Injection)이라고도 하며, 어떤 객체가
사용하는 의존 객체를 직접 만들어 사용하는게 아니라, 주입 받아 사용하는 방법을 말 함.

**스프링 IoC 컨테이너**
	● BeanFactory
	● 애플리케이션 컴포넌트의 중앙 저장소.
	● 빈 설정 소스로 부터 빈 정의를 읽어들이고, 빈을 구성하고 제공한다.
**빈**
	● 스프링 IoC 컨테이너가 관리 하는 객체.
	● 장점
		○ 의존성 관리
		○ 스코프
			■ 싱글톤: 하나만 만들어서 사용 (default)
			■ 프로포토 타입: 매번 다른 객체를 만들어 사용
		○ 라이프사이클 인터페이스



### Inversion of Control

직접 만들어 사용하는 예

```java
BookRepository repository = new BookRepository();
BookService service = new BookService(repository);
```



IoC를 사용한 예1) `@Autowired` 어노테이션 사용

```java
@Autowired
BookRepository repository;

BookService service = new BookService(repository);
```



IoC를 사용한 예2) 생성자 주입 (1번 예시보다 권장되는 방식)

```java
BookRepository repository;

public BookService(BookRepository repository) {
  this.repository = repositoy;
}
```



> IoC 컨테이너를 사용하는 이유?

여러 개발자들이 스프링 커뮤니티에서 논의해서 만들어낸 여러가지 DI 방법과 Best Practices들과 노하우가 쌓여있는 프레임워크이기 때문이다.



컨테이너 안에 들어있는 객체들을 `빈`이라고 한다.

컨테이너라고 부르는 이유는 IoC 기능을 제공하는 빈들을 담고 있기 때문에 `컨테이너`라고 하는 것이다. 우리는 그런 `빈`들을 컨테이너로부터 가져와서 사용할 수 있는 것이다.



스프링 초기에는 XML로 설정하는 것이 대세였지만, 구글 주스(Google Guice)[^1]가 선보인 `어노테이션`기반의 DI를 지원하기 시작했다. 그래서 오늘 날에는 `@Service` 를 사용해서 포조 객체(일반적인 객체)를 빈으로 등록할 수 있고, `@Autowired`로 빈에 등록이 되어있는 객체를 손쉽게 주입을 받아서 사용할 수 있는 것이다.



[^1]:스프링이 해주는 역할 중에 하나인 의존성 주입을 해주는 프레임 워크



### BeanFactory

[문서](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/BeanFactory.html)

스프링의 가장 최상위에 있는 인터페이스는 `BeanFactory`라는 인터페이스이다. 

IoC의 핵심적인 클래스이다.

Bean factory 구현은 가능한 표준 Bean라이프 사이클 인터페이스를 지원해야한다. 전체 초기화 방법과 표준 순서는 다음과 같다. (인터페이스명)

1. BeanNameAware's `setBeanName`
2. BeanClassLoaderAware's `setBeanClassLoader`
3. BeanFactoryAware's `setBeanFactory`
4. EnvironmentAware's `setEnvironment`
5. EmbeddedValueResolverAware's `setEmbeddedValueResolver`
6. ResourceLoaderAware's `setResourceLoader` (only applicable when running in an application context)
7. ApplicationEventPublisherAware's `setApplicationEventPublisher` (only applicable when running in an application context)
8. MessageSourceAware's `setMessageSource` (only applicable when running in an application context)
9. ApplicationContextAware's `setApplicationContext` (only applicable when running in an application context)
10. ServletContextAware's `setServletContext` (only applicable when running in a web application context)
11. `postProcessBeforeInitialization` methods of BeanPostProcessors
12. InitializingBean's `afterPropertiesSet`
13. a custom init-method definition
14. `postProcessAfterInitialization` methods of BeanPostProcessors



### ApplicationContext

[문서](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationContext.html)

* 가장 많이 사용하게될 빈 팩토리
* BeanFactory를 상속받은 인터페이스
* BeanFactory 가 가진 기능에 추가적으로 ApplicationEventPublisher, EnvironmentCapable 등과 같이 다양한 기능을 가지고 있다.



이러한 라이프 사이클로 스프링이 더 여러가지 기능을 제공해줄 수 있는 것이다. (컨테이너 안의 빈들을 가공하는 것이 가능하기 때문에)



> 특정 클래스를 왜 빈으로 등록해서 IoC 컨테이너가 관리하게 할까?

1. 의존성 주입때문에

   의존성 주입을 받으려면 빈으로 들어가 있어야 받을 수 있다.

2. 빈의 scope 때문에

   싱글톤으로 객체를 만들어서 관리하고 싶을 때

   스프링 IoC 컨테이너에 등록되는 빈들은 기본적으로 (별도의 어노테이션을 지정하지 않는다면) 싱글톤 스코프로 빈이 등록된다.

   * 싱글톤 패턴 : 하나의 애플리케이션에서 하나의 인스턴스만 생성되는 것을 보장하고, 생성된 객체를 어디에서든지 참조할 수 있도록 하는 패턴 
   * 싱글톤으로 사용하고 싶지 않다면? `프로토 타입`이 있다.
   * 프로토 타입 : 매번 다른 객체를 사용하는 것

   따라서 애플리케이션 전반에서 우리가 스프링 IoC 컨테이너로 부터 받아서 사용한다면 그 인스턴스들은 항상 같은 객체이다.

   메모리 면에서도 효율적이고, 미리 만들어 놓은 하나의 객체를 가져와 사용하기 때문에 런타임 시 성능 최적화에도 유리하다. (매번 만들어 사용하는 프로토 타입에 비해서)

   특히, DB와 일을 하는 xxxRepository 와 같은 객체는  만드는데 비용이 비쌀 텐데, 이런 것들을 싱글톤으로 쉽게 사용한다면 큰 장점이 될 수 있다.

3. `라이프사이클 인터페이스`를 지원해준다.

   스프링 IoC 컨테이너에 등록된 빈에 국한된 이야기이다.

   어떤 빈이 만들어 졌을 때, 추가적인 작업을 하고 싶다. 여러가지 다양한 라이프 사이클 인터페이스를 사용해서 부가적인 작업을 할 수 있고, 스프링(부트) 자체에서 이런 라이프사이클 콜백을 이용해서 부가적인 기능을 만들어 낼 수 있다.

   예시 ) `@PostConstruct` 로 해당 빈이 생성될 때, 실행할 작업을 추가할 수 있다. 

   ![라이프사이클 인터페이스](https://i.imgur.com/G0a5Rgs.png)



우리가 직접 IoC 기능을 하는 코드를 작성할 수도 있지만, 위의 장점들 때문에 빈으로 등록해서 스프링이 제공해주는 IoC 컨테이너 기능을 사용하는 이유이다.



### 의존성 문제

다음 BookService를 테스트하는 코드를 보자

```java
public class BookServiceTest {
  @Test
  public void save() {
    Book book = new Book();
    
    BookRepository bookRepository = new BookRepository();
    BookService bookService = new BookService(bookRepository);
    
    Book result = bookService.save(book);
    
    assertThat(book.getCreated()).isNotNull();
    assertThat(book.getBookStatus()).isEqualTo(BookStatus.DRAFT);
    assertThat(result).isNotNull();
  }
}
```



현재 BookRepository의 save 메서드는 null 만 리턴하고 있기 때문에 무조건 테스트에 실패할 수 밖에 없다. 하지만 성공하는 테스트 코드를 만들고 싶다면?



BookService가 BookRepository에 의존성을 가지고 있어서, BookRepository를 구현하지 않고서는 BookService만을 테스트할 수 없는 상황이다.

의존성을 가진 BookService의 단위테스트를 만들기 힘들다.

 

현재 생성자를 통해서 빈주입을 받고있다.

```java
@Service
public class BookService {
  BookRepository repository;

  public BookService(BookRepository repository) {
    this.repository = repositoy;
  }
}
```



하지만 이 코드를 다음과 같이 직접 생성해서 넣어준다면 의존성을 바꿔줄 수 없는 코드이므로 더 테스트하기 힘들어진다.

```java
@Service
public class BookService {
  BookRepository repository = new BookRepository();
}
```



하지만 우리는 의존성 주입을 받을 수 있도록 BookService 코드를 작성했기 때문에 테스트 시에 `가짜 객체(Mock객체)`를 만들어서 테스트할 수 있는 것이다.

```java
public class BookServiceTest {
  
  @Mock
  BookRepository bookRepository;
  
  @Test
  public void save() {
    Book book = new Book();
    
    //save 메서드를 호출할 때, 매개변수로 넘긴 book 인스턴스를 그대로 리턴하게 한다.
    // 즉, 동일한 book 인스턴스를 반환하게 된다.
    when(bookReposiroty.save(book)).thenReturn(book);
    BookService bookService = new BookService(bookRepository);
    
    //result가 null이 아니므로 테스트가 정상적으로 통과하게 된다.
    Book result = bookService.save(book); 
    
    assertThat(book.getCreated()).isNotNull();
    assertThat(book.getBookStatus()).isEqualTo(BookStatus.DRAFT);
    assertThat(result).isNotNull();
  }
}
```



다음 시간에, 스프링 부트가 없을 때, 어떤식으로 XML 설정과 자바 설정을 사용할 수 있었는지 확인해보자



---

## ApplicationContext와 다양한 빈 설정 방법

![컨테이너](https://i.imgur.com/XXzSHgo.png)

스프링 IoC 컨테이너의 역할
	● 빈 인스턴스 생성
	● 의존 관계 설정
	● 빈 제공
AppcliationContext
	● ClassPathXmlApplicationContext (XML)
	● AnnotationConfigApplicationContext (Java)
빈 설정
	● 빈 명세서
	● 빈에 대한 정의를 담고 있다.
		○ 이름
		○ 클래스
		○ 스코프
		○ 생성자 아규먼트 (constructor)
		○ 프로퍼트 (setter)
		○ ..
컴포넌트 스캔
	● 설정 방법
		○ XML 설정에서는 context:component-scan
		○ 자바 설정에서 @ComponentScan
● 특정 패키지 이하의 모든 클래스 중에 @Component 애노테이션을 사용한 클래스를 빈으로 자동으로 등록 해 줌.



### 1. xml 파일의 bean 태그로 직접 빈 설정

고전적인 방식으로 현재 거의 쓰지 않는 방식이다.

1. 프로젝트 생성

   [Spring Initializr] > [Dependencies] : `web` 선택

   Spring Boot 프로젝트로 생성해서 `spring-boot-starter-web` 의존성만 추가하면, 우리가 학습하는데 필요한 대부분의 의존성이 추가된다.

   이 3개가 우리가 주로 사용하는 모듈들이다.

   ```
   Maven: org.springframework:spring-beans:5.2.7.RELEASE
   Maven: org.springframework:spring-context:5.2.7.RELEASE
   Maven: org.springframework:spring-core:5.2.7.RELEASE
   ```

2. BookService와 BookRepository 클래스 생성

3. `Application Context` 를 이용해서 BookService와 BookRepository를 빈으로 등록

4. main 클래스의 `@SpringBootApplication` 삭제, main 메서드 안의 run 도 삭제

5. 고전적인 방식으로 스프링 빈 설정 파일을 만들어보자

   스프링 IoC 컨테이너는 빈 설정파일이 있어야 한다.

   [resources] > New : [XML Configuration File] > [Spring Config] > `application.xml` 파일 생성

6. BookService와 BookRepository를 빈으로 직접 등록

   ```java
   <bean id="bookService" class="dev.solar.springapplicationcontext.BookService"/>
   <bean id="bookRepository" class="dev.solar.springapplicationcontext.BookRepository"/>
   ```

   `<bean>` 태그로 빈을 생성

   id 네이밍 컨벤션 : 소문자로 시작하는 카멜케이스

   class : bean의 타입을 지정

   scope : 스코프를 지정

   	* prototype : 매번 객체를 생성
   	* request : request 당 객체를 생성
   	* session : session 당 객체를 생성
   	* singleton : 하나의 객체만 생성 (default)

   autowire : 모드를 지정

   	* default : 기본값 (뒤에서 자세히 설명)
   	* byType
   	* byName
   	* constructor
   	* no

   지금은 빈을 등록만한 상태이기 때문에 BookService에서 BookRepository를 주입받지 못한다.

   따라서 빈설정 파일에서 주입시켜줘야 한다.

 7.  BookService에 BookRepository를 주입

    `<property>` 태그 사용

    name : BookRepository의 setter에서 이름을 가져옴

    ref : 다른 빈을 참조하도록 `해당 빈의 id`를 값으로 설정

    ```java
    <bean id="bookService" class="dev.solar.springapplicationcontext.BookService"/>
      <property name="bookRepository" ref="bookRepository" />
    </bean>
    ```

8. 앞서 만든 빈을 사용하는 ApplicationContext를 만들어서 사용

   `ClassPathXmlApplicationContext` 로 ApplicationContext를 생성

   ```java
   public class SpringapplicationcontextApplication {
      private static final Logger log = LoggerFactory.getLogger(SpringapplicationcontextApplication.class);
     
     public static void main(String[] args) {
       ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
       String[] beanDefinitionNames = context.getBeanDefinitionNames();
   		log.debug("생성된 빈의 이름 : {}", Arrays.toString(beanDefinitionNames));
     }
   }
   ```

   

   출력결과

   - 빈으로 등록되어있는 빈들의 이름을 확인

   ![bean등록](https://i.imgur.com/ibPNgxg.png)

   

   `getBean()` 으로 빈을 가져올 수 있다. Object 타입으로 반환되므로 형변환 필요

   ```java
   BookService bookService = (BookService) context.getBean("bookService");//Type cast
   ```

   

   null이 아니면 의존성 주입이 잘 된 것이므로 출력해서 확인해보자

   ```java
   log.debug("의존성 주입이 되었는지 확인 : {}",bookService.bookRepository != null);//null이 아닌지 확인 -> true : 빈주입 성공
   ```

   

##### 단점

* 일일이 빈으로 등록하고 주입을 해줘야 한다.

  그래서 등장한 것이  `context:component-scan`이다.



### 2. xml 파일의 Context:component-scan 으로 빈 등록

1. application.xml 파일에 context:component-scan 태그로 빈 스캔

   base-package : 스캐닝 대상이 될 패키지명 등록 (메인 패키지명 입력)

   ```xml
   <context:component-scan base-package="dev.solar.springapplicationcontext"/>
   ```

   

   application.xml 파일

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
       <context:component-scan base-package="dev.solar.springapplicationcontext"/>
   </beans>
   
   ```

2. 어노테이션을 이용해서 빈 등록

   기본적으로 `@Component` 어노테이션을 사용해서 빈으로 등록할 수 있다.

   뿐만 아니라 `@Component` 어노테이션을 상속받은 `@Service` , `@Repository` 등의 어노테이션으로도 빈등록이 가능

   ![service어노테이션](https://i.imgur.com/HNrZui5.png)

   

   ```java
   @Service
   public class BookService { ... }
   ```

   ```java
   @Repository
   public class BookRepository {}
   ```

   

   이렇게만 하면 빈이 등록만 되고, 의존성 주입은 되지 않는다.

3. 빈 주입

   `@Autowired` 또는 `@Inject` 어노테이션으로 빈을 주입받을 수 있다.

   `@Inject` 어노테이션은 별도의 의존성이 추가로 필요하므로 `@Autowired` 로 주입받자

   ```java
   @Service
   public class BookService {
   
       @Autowired
       BookRepository bookRepository;
   
       public void setBookRepository(BookRepository bookRepository) {
           this.bookRepository = bookRepository;
       }
   }
   ```

   

   다시 실행해보면 빈이 생성되고, 주입도 성공한 것을 확인할 수 있다.

   ![빈생성 확인](https://i.imgur.com/BvILPso.png)



application.xml 파일을 읽어들이긴 하지만 xml에 들어있는 `component-scan` 기능을 사용해서 설정한 패키지 하위에 존재하는 어노테이션들을 스캐닝하여 클래스들을 빈으로 생성해준다.



### 3. 자바 코드를 이용한 빈 설정

1. 자바 설정 파일임을 알려주는 `@Configuration` 어노테이션을 사용하여 빈 설정파일을 생성

   메소드명 : bean의 id

   반환타입 : bean의 타입

   

2. BookService에 직접 BookRepository 의존성을 주입할 수 있다.

   * **setter을 이용해서 필요한 빈을 주입**

   (1) 의존성 주입에 필요한 인스턴스는 메서드로 호출해서 가져와 넘김

   ```java
   @Configuration
   public class ApplicationConfig {
   
     @Bean
     public BookRepository bookRepository() {
       return new BookRepository();
     }
   
     @Bean
     public BookService bookService() {
       BookService bookService = new BookService();
       bookService.setBookRepository(bookRepository()); //직접 의존성 주입
       return bookService;
     }
   }
   ```

   

   (2) 메서드 파라미터로 넘겨받아서 의존성 주입

   ```java
   @Bean
   public BookService bookService(BookRepository bookRepository) {
     BookService bookService = new BookService();
     bookService.setBookRepository(bookRepository);
     return bookService;
   }
   ```

   

   (3) `@Autowired` 어노테이션으로 의존성 주입

   빈으로 등록만 하면 @Autowired 어노테이션 적용이 가능하다.

   

   직접 의존성 주입을 하던 코드 삭제

   ```java
   // ApplicationConfig.java
   @Bean
   public BookService bookService(BookRepository bookRepository) {
     return new BookService();
   }
   ```

   

   `@Autowired` 어노테이션 추가

   ```java
   public class BookService {
       @Autowired
       BookRepository bookRepository;
   }
   ```

   

   * **생성자를 통한 빈 주입**

   ```java
   @Configuration
   public class ApplicationConfig {
   
     @Bean
     public BookRepository bookRepository() {
       return new BookRepository();
     }
   
     @Bean
     public BookService bookService(BookRepository bookRepository) {
       return new BookService(bookRepository);
     }
   }
   ```

   ```java
   public class BookService {
   
     BookRepository bookRepository;
   
     public BookService(BookRepository bookRepository) {
       this.bookRepository = bookRepository;
     }
   }
   ```

   

3. 앞서 만든 빈을 사용하는 ApplicationContext를 만들어서 사용

   `AnnotationConfigApplicationContext` 로 ApplicationContext를 생성

   지정한 클래스를 빈설정 파일로 사용한다.

   ```java
   ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContext.class);
   ```

   

   빈설정에서 `@Bean`으로 빈 정의를 읽어서 빈으로 등록하고 정의해준 대로 의존성 주입을 한다.

   

   <전체코드>

   ```java
   public class SpringapplicationcontextApplication {
       private static final Logger log = LoggerFactory.getLogger(SpringapplicationcontextApplication.class);
   
       public static void main(String[] args) {
           ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContext.class);
           String[] beanDefinitionNames = context.getBeanDefinitionNames();
           log.debug("생성된 빈의 이름 : {}", Arrays.toString(beanDefinitionNames));
           BookService bookService = (BookService) context.getBean("bookService");//Type cast
           log.debug("의존성 주입이 되었는지 확인 : {}",bookService.bookRepository != null);//null이 아닌지 확인 -> true : 빈주입 성공
       }
   
   }
   ```

   ![자바코드로 빈주입](https://i.imgur.com/iF6pfa7.png)



##### 단점

하지만 여전히 `@Bean` 어노테이션으로 일일이 지정하여 만드는 불편함이 있다.

xml에서 component scanning을 했던 것처럼 자바코드로도 스캐닝을 할 수 있다.



### 4. ComponentScan 어노테이션을 이용한 빈 설정

```java
@ComponentScan(basePackageClasses = SpringapplicationcontextApplication.class)
```

* basePackages = "path/of/package/class"

  문자열로 입력할 수 있다. (IDE가 좋으면 자동완성을 지원하긴 하지만 Type Safety[^2]하지 않다.)

* basePackageClasses = 클래스명

  **좀 더 Type Safety한 방법**

  이 어플리케이션(클래스)이 위치한 곳부터 Component Scanning을 진행한다.

  특정 어노테이션이 설정된 클래스들을 스캐닝해서 빈으로 알아서 등록해준다.

[^2]:어떠한 오퍼레이션(또는 연산)도 정의되지 않은 결과를 내놓지 않는것, 즉, 예측불가능한 결과를 내지 않는것을 뜻한다.

```java
@Configuration
@ComponentScan(basePackageClasses = SpringapplicationcontextApplication.class)
public class ApplicationConfig {
}
```

```java
@Repository
public class BookRepository {}
```

```java
@Service
public class BookService {...}
```



이 방식이 스프링부트를 이용해 빈을 설정하는 방식에 가장 가까운 방법이다.

물론 스프링부트 프로젝트에서 `ApplicationContext`를 직접 만들어서 사용하고 있진 않다. 이것 또한 스프링이 알아서 생성해준다.



### 5. 스프링 부트에서의 빈 설정

`@SpringBootApplication` 을 붙여주면 스프링이 ApplicationContext를 알아서 생성해준다.

```java
@SpringBootApplication
public class SpringapplicationcontextApplication {
  public static void main(String[] args) {
  }
}
```



@SpringBootApplication을 확인해보면 이미 `@ComponentScan` 어노테이션을 상속받고 있고, (@SpringBootConfiguration → ) `@Configuration` 이 붙어있는 것이다.

사실상 위 코드의 클래스가 빈 설정파일이 되는 것이다. (따라서 별도로 만들었던 ApplicationConfig.java 파일은 불필요하다.)



---

## @Autowired

필요한 의존 객체의 “타입"에 해당하는 빈을 찾아 주입한다.

@Autowired

​	● required: 기본값은 true (따라서 못 찾으면 애플리케이션 구동 실패)

사용할 수 있는 위치

​	● 생성자 (스프링 4.3 부터는 생략 가능)
​	● 세터
​	● 필드

경우의 수

​	● 해당 타입의 빈이 없는 경우
​	● 해당 타입의 빈이 한 개인 경우
​	● 해당 타입의 빈이 여러 개인 경우
​		○ 빈 이름으로 시도,
​			■ 같은 이름의 빈 찾으면 해당 빈 사용
​			■ 같은 이름 못 찾으면 실패

같은 타입의 빈이 여러개 일 때

​	● @Primary
​	● 해당 타입의 빈 모두 주입 받기
​	● @Qualifier (빈 이름으로 주입)

동작 원리

​	● 첫시간에 잠깐 언급했던 빈 라이프사이클 기억하세요?
​	● BeanPostProcessor
​		○ 새로 만든 빈 인스턴스를 수정할 수 있는 라이프 사이클 인터페이스
​	● AutowiredAnnotationBeanPostProcessor extends BeanPostProcessor
​		○ 스프링이 제공하는 @Autowired와 @Value 애노테이션 그리고 JSR-330의
​				@Inject 애노테이션을 지원하는 애노테이션 처리기.



1. 프로젝트 생성

[Spring Initializr] > [Dependencies] : `web` 선택

BookService와 BookRepository 클래스를 생성 후, BookService만 @Service 로 빈으로 만들고 BookRepository는 빈으로 등록하지 않은 상태에서 BookService에서 BookRepository 의존성 주입을 해보자



### @Autowired 사용할 수 있는 위치

##### 1. 생성자로 주입

IDE에서 BookRepository로 등록된 빈이 없어서 알려주고 있지만 의도한 코드이므로 무시하고 넘어간다.

![생성자 빈주입](https://i.imgur.com/rQa0Y58.png)



애플리케이션을 실행하여 오류를 확인

⇒ 오류 내용 : BookService 생성자의 0번째 파라미터에 해당하는 BookRepository 빈이 없다.

⇒ 해결 방안 : BookRepository에 해당하는 빈을 정의해라

```
***************************
APPLICATION FAILED TO START
***************************

Description:

Parameter 0 of constructor in dev.solar.demospring51.BookService required a bean of type 'dev.solar.demospring51.BookRepository' that could not be found.


Action:

Consider defining a bean of type 'dev.solar.demospring51.BookRepository' in your configuration.


Process finished with exit code 1
```



BookRepository에 @Repository 를 붙여주자

@Component 어노트에션을 붙일 수도 있지만 Repository로 활용할 클래스이기 때문에 @Repository를 붙여주는 것이 좋다. 특정한 기능을 실행시킬 수도 있고, AOP에서도 사용하기 더 좋기 때문에 구분해서 쓰는 것이 더 좋다.

다시 실행하면 에러 없이 잘 주입이 되는 것을 확인할 수 있다.



##### 2. Setter로 주입

![setter 주입](https://i.imgur.com/14G3QSS.png)



BookRepository에 @Repository를 붙이지 않고 실행하면 동일하게 오류가 나는 것을 확인할 수 있다.



> 생성자로 주입한 경우와 Setter로 주입한 경우에 발생한 에러에서 알 수 있는 점

생성자로 주입한 경우 `BookService`를 만드려고 할 때 부터 에러가 나는 것은 분명하다.

Setter로 주입하는 경우, Setter를 호출할 때 해당하는 빈이 없어서 오류가 나는 것은 맞지만, BookService 인스턴스 자체는 만들 수 있는 것 아닌가?

맞는 말이지만, `@Autowired` 라는 어노테이션이 붙어있기 때문에 BookService `빈을 생성할 때` 의존성을 주입하려고 시도한다. 따라서 실행 시 오류가 발생하는 것이다.



`@Autowired` 어노테이션이 필수가 아니라 옵셔널한 사항이라면 `@Autowired(required = false)` 로  설정해서 실행하면 에러가 발생하지 않는다.

```java
@Service
public class BookService {

  BookRepository bookRepository;

  @Autowired(required = false)
  public void setBookRepository(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }
}
```



##### 3. 필드로 주입

필드 주입 시에도 @Autowired 설정을 required = false로 설정해서 옵셔널하게 지정하면 에러 없이 빈 생성이 가능하다.

```java
@Service
public class BookService {

  @Autowired(required = false)
  BookRepository bookRepository;

}
```



* 생성자 인젝션과 Setter, 필드 인젝션의 차이

  Setter와 필드 의존성 주입시에는 옵셔널하게 설정해서 해당하는 빈이 없이도 인스턴스 자체는 만들도록 할 수 있다.



### 해당 타입의 빈이 여러 개인 경우































