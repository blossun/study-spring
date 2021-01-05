# 스프링 시큐리티 1부: spring-boot-starter-security

스프링 시큐리티

* 웹 시큐리티
* 메소드 시큐리티
* 다양한 인증 방법 지원
  * LDAP, 폼 인증, Basic 인증, OAuth, ...

스프링 부트 시큐리티 자동 설정

* SecurityAutoConfiguration
* UserDetailsServiceAutoConfiguration
* spring-boot-starter-security
  * 스프링 시큐리티 5.* 의존성 추가
* 모든 요청에 인증이 필요함.
* 기본 사용자 생성
  * Username: user
  * Password: 애플리케이션을 실행할 때 마다 랜덤 값 생성 (콘솔에 출력 됨.)
  * spring.security.user.name
  * spring.security.user.password
* 인증 관련 각종 이벤트 발생
  * DefaultAuthenticationEventPublisher 빈 등록
  * 다양한 인증 에러 핸들러 등록 가능

스프링 부트 시큐리티 테스트

* https://docs.spring.io/spring-security/site/docs/current/reference/html/test-method.html

---

스프링부트 프로젝트에 `spring-boot-starter-security` 의존성만 추가하면 스프링 시큐리티를 사용할 수 있다.

※ 프로젝트 : springbootsecurity - 의존성:web



**(실습 준비)**

* `/hello`, `/my`에 대한 요청을 처리하는 컨트롤러 추가
* index.html, hello.html, my.html 페이지 추가
* 컨트롤러 테스트 추가

* Thymleaf로 뷰 렌더링

#### 목표

* /hello : 모든 사용자가 접근 가능
* /my : 로그인한 사용자만 접근 가능



#### 실습

스프링 시큐리티 의존성 추가

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```



테스트 코드

```java
@Test
public void hello() throws Exception {
    mockMvc.perform(get("/hello")
                .accept(MediaType.TEXT_HTML))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("hello"));
}

@Test
public void my() throws Exception {
    mockMvc.perform(get("/my"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("my"));
}
```



⇒ 시큐리티를 추가하면 테스트가 깨진다.

* "/my" ⇒ 401 Unauthorized 응답 : *"Basic 인증"*

  ```
  MockHttpServletResponse:
             Status = 401
      Error message = Unauthorized
            Headers = [WWW-Authenticate:"Basic realm="Realm"", X-Content-Type-Options:"nosniff", X-XSS-Protection:"1; mode=block", Cache-Control:"no-cache, no-store, max-age=0, must-revalidate", Pragma:"no-cache", Expires:"0", X-Frame-Options:"DENY"]
  ```

  Accect 헤더에 별도로 값을 지정하지 않았기 때문에 서버는 클라이언트에게 Basic인증을 요청하게 된다.



* "/hello" ⇒ 302 Redirect 응답 : *"폼 인증"*

  ```
  MockHttpServletResponse:
             Status = 302
      Error message = null
            Headers = [X-Content-Type-Options:"nosniff", X-XSS-Protection:"1; mode=block", Cache-Control:"no-cache, no-store, max-age=0, must-revalidate", Pragma:"no-cache", Expires:"0", X-Frame-Options:"DENY", Location:"http://localhost/login"]
       Content type = null
               Body = 
      Forwarded URL = null
     Redirected URL = http://localhost/login
            Cookies = []
  ```

  스프링부트가 기본으로 만들어주는 로그인 폼 인증화면으로 넘어간다.



> why??

스프링부트가 제공하는 시큐리티 자동설정 중 하나이다.

* 모든 요청이 스프링 시큐리티로 인해 `인증`이 필요하게 된다.

* Basic Authentication과 Form 인증이 둘 다 적용된다.



Basic Authentication는 Accept 헤더에 따라 달라진다. (* Accept 헤더 : 요청이 원하는 응답의 형태)

우리는 따로 Accept 헤더에 `text/html`을 담아서 보내지 않았기 때문에, Basic 인증을 응답으로 보낸다.

브라우저가 `Basic Authentication` 응답을 받으면 브러우저의 내장된 Basic Authentication Form을 띄우게 된다. 

보통 브라우저에서 요청하는 HTTP Accept 헤더에는 `text/html`이 들어간다. 그런 경우 폼 인증으로 넘어간다.

(⇒ ***폼인증 vs Basic 인증 vs 다이제스트 인증*** 의 차이점과 보안 취약점을 확인해보자)





### Basic 인증 vs 폼 인증

※ Basic 인증

![image-20210105145200626](images/image-20210105145200626.png)

![image-20210105145244183](images/image-20210105145244183.png)



※ 폼 인증

![image-20210105145435620](images/image-20210105145435620.png)

![image-20210105144307849](images/image-20210105144307849.png)

---

※ Basic 인증 보안 주의

​	[HTTP 인증 Docs](https://developer.mozilla.org/ko/docs/Web/HTTP/Authentication)

​	[[보안\] 폼인증 vs Basic 인증 vs 다이제스트 인증](https://jamie95.tistory.com/entry/보안-다이제스트-인증)







