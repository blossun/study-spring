# SpringApplication 2부

* [Spring Docs - events and listeners](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-spring-applicatio n.html#boot-features-application-events-and-listeners)

* ApplicationEvent 등록
  * ApplicationContext를 만들기 전에 사용하는 리스너는 @Bean으로 등록할 수 없다.
    * SpringApplication.addListners()

* WebApplicationType 설정 
* 애플리케이션 아규먼트 사용하기
  * ApplicationArguments를 빈으로 등록해 주니까 가져다 쓰면 됨. 
* 애플리케이션 실행한 뒤 뭔가 실행하고 싶을 때
  * ApplicationRunner (추천) 또는 CommandLineRunner
  * 순서 지정 가능 @Order

---

스프링부트가 기본적으로 제공해주는 ApplicationEvent들이 있고, 이벤트가 실행되는 시점이 다양하게 있다. 어플리케이션이 실행될 때, Application Context를 만들었을 때, Application Context가 refresh 됐을 때, Application이 잘 구동되었을 때, 실패했을 때 등등...

그 중에 주의해야할 것이 있다.

1. ApplicationEvent 생성





















