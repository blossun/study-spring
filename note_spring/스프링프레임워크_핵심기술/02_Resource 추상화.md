# Resource 추상화

**org.springframework.core.io.Resource**

특징
	● java.net.URL을 추상화 한 것.
	● 스프링 내부에서 많이 사용하는 인터페이스.

추상화 한 이유
	● 클래스패스 기준으로 리소스 읽어오는 기능 부재
	● ServletContext를 기준으로 상대 경로로 읽어오는 기능 부재
	● 새로운 핸들러를 등록하여 특별한 URL 접미사를 만들어 사용할 수는 있지만 구현이 복잡하고 편의성 메소드가 부족하다.

인터페이스 둘러보기
	● 상속 받은 인터페이스
	● 주요 메소드
			○ getInputStream()
			○ exitst()
			○ isOpen()
			○ getDescription(): 전체 경로 포함한 파일 이름 또는 실제 URL

구현체
	● UrlResource: java.net.URL 참고, 기본으로 지원하는 프로토콜 http, https, ftp, file, jar.
	● ClassPathResource: 지원하는 접두어 classpath:
	● FileSystemResource
	● ServletContextResource: 웹 애플리케이션 루트에서 상대 경로로 리소스 찾는다.
	● ...

리소스 읽어오기
	● Resource의 타입은 location 문자열과 ApplicationContext의 타입에 따라 결정 된다.
			○ ClassPathXmlApplicationContext -> ClassPathResource
			○ FileSystemXmlApplicationContext -> FileSystemResource
			○ WebApplicationContext -> ServletContextResource
	● ApplicationContext의 타입에 상관없이 리소스 타입을 강제하려면 java.net.URL 접두어(+ classpath:)중 하나를 사용할 수 있다.
			○ classpath:me/whiteship/config.xml -> ClassPathResource
			○ file:///some/resource/path/config.xml -> FileSystemResource

---

## org.springframework.core.io.Resource

java.net.URL을 추상화하여 `org.springframework.core.io.Resource` 라는 클래스로 감싸서 실제 로우 레벨에 있는 리소스에 접근하는 기능을 만든 것이다.

### 추상화 한 이유

* 클래스패스 기준으로 리소스 읽어오는 기능 부재

  URL이 여러 prefix(http, ftp, https, etc.)를 지원하긴 하지만, 클래스패쓰로 부터 가져오고 싶었다.

  클래스패쓰로 부터 가져오기위해서 다른 방식을 썼었어야하는데, Resource를 가져온다는 점에서 동일한 방법으로 통일시킨것이다.

* ServletContext를 기준으로 상대 경로로 읽어오는 기능 부재

* 새로운 핸들러를 등록하여 특별한 URL 접미사를 만들어 사용할 수는 있지만 구현이 복잡하고 편의성 메소드가 부족하다.



Resource 인터페이스를 보면 여러가지 핵심적인 메서드들이 있다. (스프링에서 매우 많이 사용된다.)

초기에 XML파일로 빈을 등록할 때, XML파일을 `ClassPathXmlApplicationContext` 를 이용해서 만들었다.

```java
ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
```

"application.xml"이라는 문자열이 내부적으로 Resource로 변환이 된다.

그리고 ResourceLoader의 getResource() 메서드 파라미터로 전달되는 location에 들어간다.

```java
Resource resource = resourceLoader.getResource("application.xml");
```

즉, 실질적으로 내부에서 Resource를 쓰고 있는 것이다.



**※ [참고]** 파일시스템 경로에서 리소스를 가져오기

```java
FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext("xxx.xml");
```

⇒ 내부적으로는 둘이 다른 구현체를 쓰게 된다.



### 주요 메서드

* getInputStream()
* exists() : 리소스가 항상 존재한다는 가정을 하지 않기 때문에, 리소스 존재여부를 항상 확인해준다.
* isReadable() : 읽을 수 있는지
* isOpen() : 열려 있는지
* isFile() : 파일인지 디렉토리인지
* getFile() : 파일로 가져올 수 있는 경우는 제한적이다. 항상 모든 리소스를 파일로 가져올 수는 없다.
* getDescription(): 전체 경로 포함한 파일 이름 또는 실제 URL



### 구현체

Resource를 구현한 다양한 구현체들이 있다.

* UrlResource: java.net.URL 참고, 기본으로 지원하는 프로토콜 http, https, ftp, file, jar.
* ClassPathResource: 지원하는 접두어 classpath:
* FileSystemResource
* ServletContextResource: **웹 애플리케이션 루트에서** 상대 경로로 리소스 찾는다. (가장 많이 쓰게되는 구현체)



### 리소스 읽어오기

*  Resource의 타입은 location 문자열과 **ApplicationContext의 타입**에 따라 결정 된다.
  * (ApplicationContext) → (구현체, Resource를 찾는 기준 위치)
  * ClassPathXmlApplicationContext → ClassPathResource
  * FileSystemXmlApplicationContext → FileSystemResource
  * WebApplicationContext -> ServletContextResource
    * WebApplicationContext 이하로는 전부다 ServletContextResource를 쓰게된다. 즉, 웹 애플리케이션 루트에서부터 리소스를 찾는다.
    * WebApplicationContext는 인터페이스이고, 이를 구현한 구현체들도 다양하다. 그중 `GenericWebApplicationContext`를 많이 사용한다.
  * ApplicationContext 타입에 따라서 구현체가 결정되므로 리소스를 찾는 기준 위치에 따라서 알아서 찾아준다.
*  **ApplicationContext의 타입에 상관없이 리소스 타입을 강제하려면 java.net.URL 접두어(+ classpath:)중 하나를 사용할 수 있다.** (★ 이 방식 추천)
  * **classpath:**me/whiteship/config.xml -> ClassPathResource
  * **file://**/some/resource/path/config.xml -> FileSystemResource



#### 접두어를 사용하여 리소스 타입을 강제하는 방식 추천

ApplicationContext는 대부분의 경우 WebApplicationContext를 사용하기때문에 ServletContextResource을 사용하겠지만, 그 외의 Resource 구현체를 알고 쓰는 개발자들이 대부분 없다. 따라서 이 리소스가 어디서 오는 것인지 코드만 봐서는 알기가 없다. 하지만 접두어를 쓰면 좀 더 명시적이다. 따라서 명시적인 프로그래밍을 권고한다.



**※ [실습]**

접두어를 지정하지 않으면 ApplicationContext 이므로 기본적으로 ServletContextResource 타입이어야 한다.

ServletContextResource 은 리소스를 context path `웹 애플리케이션 루트` 에서부터 찾게된다.

스프링부트가 띄워주는 내장형 톰캣에는 context path가 지정되어있지 않기 때문에 리소스를 찾을 수 없어서 exists()를 하면 false가 뜬다.

```java
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext resourceLoader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(resourceLoader.getClass()); //WebApplicationContext 중 하나

        Resource resource = resourceLoader.getResource("classpath:test.txt");
        System.out.println(resource.getClass()); //classpath prefix를 썼기때문에 ClassPathResource 타입

        Resource resource02 = resourceLoader.getResource("text.txt");
        System.out.println(resource02.getClass()); //기본 타입 ServletContextResource

        System.out.println(resource.exists());
        System.out.println(resource.getDescription()); //전체 경로
        System.out.println(resource02.exists());
        System.out.println(resource02.getDescription()); //전체 경로
    }
}
```

```
결과
class org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext
class org.springframework.core.io.ClassPathResource
class org.springframework.web.context.support.ServletContextResource
true
class path resource [test.txt]
false
ServletContext resource [/text.txt]
```



스프링 부트 기반의 애플리케이션을 작성할 때는 보통 (특히 JSP를 쓰지않는 경우, thymeleaf를 사용하는 경우 또한) classpath 기준으로 많은 리소스를 사용하기 때문에, classpath 접두어를 사용하는 것을 추천한다.



또한 리소스를 빈으로 등록할 때도 value에 문자열만 쓰면 빈 설정을 읽어들이는 ApplicationContext에 따라서 타입이 달라진다. 따라서 항상 명시해주는 것이 좋다.

```java
<bean id="myBean" class="...">
  <property name="template" value="some/resource/path/myTemplate.txt"/>
</bean>
```

```java
<property name="template" value="classpath:some/resource/path/myTemplate.txt">
<property name="template" value="file:/some/resource/path/myTemplate.txt"/>
```

참고 : [Resources as dependencies](https://docs.spring.io/spring/docs/3.0.0.M3/spring-framework-reference/html/ch05s06.html)



* 경로에 와일드 카드도 사용할 수 있다.

* classpath* 도 사용가능

  ⇒ 레퍼런스를 참고