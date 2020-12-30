# 스프링 웹 MVC 7부: Thymeleaf

스프링 부트가 자동 설정을 지원하는 템플릿 엔진

* FreeMarker
* Groovy
* **Thymeleaf**
* Mustache

JSP를 권장하지 않는 이유

* JAR 패키징 할 때는 동작하지 않고, WAR 패키징 해야 함.
* Undertow는 JSP를 지원하지 않음.
* https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-jsp-limitations

Thymeleaf 사용하기

* https://www.thymeleaf.org/
* https://www.thymeleaf.org/doc/articles/standarddialect5minutes.html
* 의존성 추가: spring-boot-starter-thymeleaf
* 템플릿 파일 위치: /src/main/resources/**template/**
* 예제: https://github.com/thymeleaf/thymeleafexamples-stsm/blob/3.0-master/src/main/webapp/WEB-INF/templates/seedstartermng.html

---

템플릿 엔진

* 주로 뷰를 만드는 데 사용
  * 동적 컨텐츠 생성

* 뷰 만드는 것 이외에 여러가지 용도로 사용 가능
  * Code Generation, Email Template, 

> 뷰를 만들 때 템플릿 엔진을 쓰는 이유?

기본적인 템플릿은 같은데 안에 들어가는 값들만 경우에 따라서 조금씩 다를 때, 정적인 컨텐츠를 사용할 수 없고 동적으로 컨텐츠를 생성해서 응답으로 보낼 때 사용할 수 있다.



## 스프링 부트가 자동 설정을 지원하는 템플릿 엔진

* FreeMarker
* Groovy
* **Thymeleaf**
* Mustache

JSP를 권장하지 않는 이유

* JSP는 자동 설정을 지원하지 않음

* SpringBoot가 지향하는 바와 충돌이 있다.

* 스프링 부트는 독립적으로 실행가능한 embedded tomcat으로 애플리케이션을 빠르고 쉽게 만들어서 배포하고자 한다.

  JSP를 사용하게되면 JAR로 패키징한 파일을 사용하지못하고, WAR로 패키징한 파일을 사용해야 한다.

  (물론, WAR로 패키징하더라도 `java -jar`로 embedded tomcat 으로 실행할 수 있긴 하다. 다른 서블릿 엔진으로 배포할 수도 있다.)

  하지만 최근에 만들어진 Undertow라는 서블릿 엔진은 JSP를 지원하지 않음

  그렇기 때문에 이런 제약사항들이 있기 때문에 요즘에는 잘 쓰지 않는다.



## Thymeleaf 사용하기

* 의존성 추가: spring-boot-starter-thymeleaf

  ```xml
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
  </dependency>
  ```

* 템플릿 파일 위치: /src/main/resources/**template/**

  * 자동 설정이 완료되면 모든 동적으로 생성되는 뷰는 템플릿 파일 위치에서 찾게된다.

* 예제: https://github.com/thymeleaf/thymeleafexamples-stsm/blob/3.0-master/src/main/webapp/WEB-INF/templates/seedstartermng.html































