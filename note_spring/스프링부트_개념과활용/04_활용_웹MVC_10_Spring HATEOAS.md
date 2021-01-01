# 스프링 웹 MVC 10부: Spring HATEOAS

**H**ypermedia **A**s **T**he **E**ngine **O**f **A**pplication **S**tate

* 서버: 현재 리소스와 **연관된 링크 정보**를 클라이언트에게 제공한다.
* 클라이언트: **연관된 링크 정보**를 바탕으로 리소스에 접근한다.
* 연관된 링크 정보
  * **Rel**ation
  * **H**ypertext **Ref**erence)
* spring-boot-starter-hateoas 의존성 추가

HATEOAS 이해하기 (순서대로 링크를 참고해서 읽으면 됨)

* https://spring.io/understanding/HATEOAS
* https://spring.io/guides/gs/rest-hateoas/
* https://docs.spring.io/spring-hateoas/docs/current/reference/html/
* (깃헙 API에 적용되어있음)

ObjectMapper 제공

* spring.jackson.*
* Jackson2ObjectMapperBuilder

LinkDiscovers 제공

* 클라이언트 쪽에서 링크 정보를 Rel 이름으로 찾을때 사용할 수 있는 XPath 확장 클래스

---

## Spring HATEOAS

HATEOAS를 구현하는데 편리한 기능을 제공해주는 라이브러리

> HATEOAS 란?

**H**ypermedia **A**s **T**he **E**ngine **O**f **A**pplication **S**tate

* 서버: 현재 리소스와 **연관된 링크 정보**를 클라이언트에게 제공한다.
* 클라이언트: **연관된 링크 정보**를 바탕으로 리소스에 접근한다.
* 연관된 링크 정보
  * **Rel**ation
  * **H**ypertext **Ref**erence)



#### HATEOAS 원리

ex ) 클라이언트가 리소스를 요청했을 때,  (서버가) 리소스에 대한 정보를 제공할 때, 리소스와 연관되어있는 링크 정보들 까지 같이 제공하고 , (클라이언트) 같이 제공된 연관된 링크 정보를 가지고 리소스에 접근한다.

루트 페이지에 대한 요청했을 때, 루트 페이지에 대한 리소스 정보 - Relation : self(나자신), HRef : 루트 링크

온라인 서점 웹사이트, 북을 조회할 수 있는 페이지가 있을 때, Relation : Books, HRef : /books 

연관된 링크정보들을 넣어주면 클라이언트는 루트를 요청한 다음에 루트와 연관된 리소스들을 파악하고 거기서 Book과 관련된 페이지를 가고 싶다면 Book Relation에 해당하는 HRef를 읽어서 해당 url로 요청을 보내게 된다.



#### 실습

1. spring-boot-starter-hateoas 의존성 추가

   ```xml
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-hateoas</artifactId>
   </dependency>
   ```

   ⇒ 스프링 부트가 여러가지 자동설정을 해준다.

   * ObjectMapper 제공

     우리가 제공하는 리소스를 JSON으로 변환할 때 사용하는 인터페이스

     spring-boot-starter-web 만 의존성을 추가해도 빈으로 등록해준다.

   * LinkDiscovers 제공 (직접 사용하게될 일이 많지 않음)

2. 테스트 코드 작성

   반환하는 Hello에 link정보를 넣지 않았으므로 테스트는 실패한다.



















