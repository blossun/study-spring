# 스프링 REST Docs: 링크, (Req, Res) 필드와 헤더 문서화

요청 필드 문서화

* requestFields() + fieldWithPath()
* responseFields() + fieldWithPath()
* requestHeaders() + headerWithName()
* responseHedaers() + headerWithName()
* links() + linkWithRel()

테스트 할 것

> * API 문서 만들기
>   * **요청 본문 문서화**
>   * **응답 본문 문서화**
>   * **링크 문서화**
>     * **self**
>     * **query-events**
>     * **update-event**
>     * profile 링크 추가
>   * **요청 헤더 문서화**
>   * **요청 필드 문서화**
>   * **응답 헤더 문서화**
>   * **응답 필드 문서화**

Relaxed 접두어

* 장점: 문서 일부분만 테스트 할 수 있다.
* 단점: 정확한 문서를 생성하지 못한다.

---







---

# 스프링 REST Docs: 문서 빌드

스프링 REST Docs

* https://docs.spring.io/spring-restdocs/docs/2.0.2.RELEASE/reference/html5/
* pom.xml에 메이븐 플러그인 설정

```xml
<plugin>
  <groupId>org.asciidoctor</groupId>
  <artifactId>asciidoctor-maven-plugin</artifactId>
  <version>1.5.3</version>
  <executions>
    <execution>
      <id>generate-docs</id>
      <phase>prepare-package</phase>
      <goals>
        <goal>process-asciidoc</goal>
      </goals>
      <configuration>
        <backend>html</backend>
        <doctype>book</doctype>
      </configuration>
    </execution>
  </executions>
  <dependencies>
    <dependency>
      <groupId>org.springframework.restdocs</groupId>
      <artifactId>spring-restdocs-asciidoctor</artifactId>
      <version>2.0.2.RELEASE</version>
    </dependency>
  </dependencies>
</plugin>
<plugin>
  <artifactId>maven-resources-plugin</artifactId>
  <version>2.7</version>
  <executions>
    <execution>
      <id>copy-resources</id>
      <phase>prepare-package</phase>
      <goals>
        <goal>copy-resources</goal>
      </goals>
      <configuration>
        <outputDirectory>
          ${project.build.outputDirectory}/static/docs
        </outputDirectory>
        <resources>
          <resource>
            <directory>
              ${project.build.directory}/generated-docs
            </directory>
          </resource>
        </resources>
      </configuration>
    </execution>
  </executions>
</plugin>
```

* 템플릿 파일 추가
  * src/main/asciidoc/index.adoc

문서 생성하기

* mvn package
  * test
  * prepare-package :: process-asciidoc
  * prepare-package :: copy-resources
* 문서 확인
  * /docs/index.html

테스트 할 것

> * API 문서 만들기
>   * **요청 본문 문서화**
>   * **응답 본문 문서화**
>   * **링크 문서화**
>     * **self**
>     * **query-events**
>     * **update-event**
>     * profile 링크 추가
>   * **요청 헤더 문서화**
>   * **요청 필드 문서화**
>   * **응답 헤더 문서화**
>   * **응답 필드 문서화**

---













