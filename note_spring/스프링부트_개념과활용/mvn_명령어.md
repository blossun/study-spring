**mvn compile**

 \- 컴파일 수행

 \- 컴파일 된 결과는 target/classes에 생성된다.



**mvn test**

 \- 테스트 클래스 실행

 \- 테스트 코드를 컴파일한 뒤 테스트 코드를 실행한다.

 \- 테스트 클래스들은 target/test-classes 디렉터리에 생성된다.

 \- 테스트 결과 리포트는 target/surefire-reports에 생성된다.



**mvn package**

 \- 컴파일된 결과물을 패키지 파일로 생성

 \- 컴파일, 테스트, 빌드를 수행하여 패키지 파일을 생성한다.

 \- 프로텍트 이름, 버전, 패키징 옵션에 맞게 파일이 생성된다.

 \- pom에서 아래와 같이 설정하면 결과 파일은 test-1.0-SNAPSHOT.war 로 생성된다.

```xml
<artifactId>test</artifactId> 
<version>1.0-SNAPSHOT</version> 
<packaging>war</packaging>
```



**mvn install**

 \- 패키징한 파일을 로컬 저장소에 배포



**mvn deploy**

 \- 패키징한 파일을 원격 저장소에 배포 (nexus 혹은 maven central 저장소)



**mvn clean**

 \- 메이븐 빌드를 통하여 생성된 모든 산출물을 삭제한다.



**자주사용하는 옵션**

**1) -Dmaven.test.skip=true**

 . 테스트를 건너뛴다.

 . 예) mvn package -Dmaven.test.skip=true : 테스트를 건너뛰고 패키징 수행



**2) mvn -P[profile명] [goals]**

 . 환경에 따라 다르게 설정을 관리할 수 있는 기능아다. profile(프로파일)이라고 부름.

 . pom에서 설정할수 있다.

 . 예) mvn -Pdevelopment -Dmaven.test.skip=true package

   : development라고 정의한 profile 설정으로 테스트를 건너뛰고 패키징 수행



---

### MVN 설치

* [[Maven] - Mac OS에 Maven 설치하기](https://pangsblog.tistory.com/92)

#### zsh 설정

zsh은 Terminal이 시작할 때 ~/.zshrc가 실행됩니다. 그래서 .zshrc 파일을 만들고 아래처럼 .bash_profile이 실행되도록 추가했습니다.

```zshrc
if [ -f ~/.bash_profile ]; then
  . ~/.bash_profile
fi
```

이제 Terminal을 열때마다 source .bash_profile 명령을 안해줘도 됩니다.

또는 .zshrc 파일안에 .bash_profile의 내용을 옮겨줘도 됩니다.









