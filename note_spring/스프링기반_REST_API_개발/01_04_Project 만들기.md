# 스프링 부트 프로젝트 만들기

추가할 의존성

* Web
* JPA
* HATEOAS
* REST Docs
* H2 → test scope로 변경 "테스트용 DB로 사용"
* PostgreSQL → runtime scope 삭제 (compile scope) "서비스 DB로 사용"
* Lombok → optional : true 다른 프로젝트에서 이 프로젝트를 참조할 때 이 의존성이 같이 들어가지 않는다.

자바 버전 11로 시작

* [자바는 여전히 무료다.](https://itnext.io/java-is-still-free-c02aef8c9e04)

스프링 부트 핵심 원리

* 의존성 설정 (pom.xml)
* 자동 설정 (@EnableAutoConfiguration)
* 내장 웹 서버 (의존성과 자동 설정의 일부)
* 독립적으로 실행 가능한 JAR (pom.xml의 플러그인)

---



프로젝트 셋팅 후, 애플리케이션이 잘 동작하는지 확인하기 위해 잠시 h2의 test scope을 주석처리하고 실행해보자

H2 test scope을 셋팅한 채로 실행하면 h2는 애플리케이션을 실행할 때 사용되지 않으므로 postgreSQL을 사용하게 되는데 별다른 DB 설정을 해주지 않아서 실행되지 않는다.

H2를 test scope으로 두지 않으면 두 가지 DB 설정이 있을 때, postreSQL의 설정이 없다면 인메모리 DB인 H2를 사용해서 실행하게되므로 정상적으로 띄워진다.



* JAR 로 실행

  1. 패키징

     ※ 로컬의 java 버전을 11로 맞춰줘야함

     ```sh
     mvn package
     ```

     → `/target` 에 jar파일 생성됨

  2. jar 파일 실행

     ```sh
     java -jar target/demo-inflearn-rest-api-0.0.1-SNAPSHOT.jar 
     ```

     

---

* 로컬의 java 버전을 11로 변경

```sh
 java -version 
openjdk version "1.8.0_232"
OpenJDK Runtime Environment (Zulu 8.42.0.23-CA-macosx) (build 1.8.0_232-b18)
OpenJDK 64-Bit Server VM (Zulu 8.42.0.23-CA-macosx) (build 25.232-b18, mixed mode)
 jenv versions 
  system
  1.8
  1.8.0.232
  11.0
  11.0.6
  openjdk64-13.0.1
  oracle64-9
* zulu64-1.8.0.232 (set by /Users/ssun/.jenv/version)
  zulu64-11.0.6
 jenv local zulu64-11.0.6 
 java -version           
openjdk version "11.0.6" 2020-01-14 LTS
OpenJDK Runtime Environment Zulu11.37+17-CA (build 11.0.6+10-LTS)
OpenJDK 64-Bit Server VM Zulu11.37+17-CA (build 11.0.6+10-LTS, mixed mode)

```

