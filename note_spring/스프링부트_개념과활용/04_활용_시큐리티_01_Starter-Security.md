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



#### (실습)

* `/hello`, `/my`에 대한 요청을 처리하는 컨트롤러 추가
* index.html, hello.html, my.html 페이지 추가
* 컨트롤러 테스트 추가

* Thymleaf로 뷰 렌더링

**목표**

* /hello : 모든 사용자가 접근 가능
* /my : 로그인한 사용자만 접근 가능









