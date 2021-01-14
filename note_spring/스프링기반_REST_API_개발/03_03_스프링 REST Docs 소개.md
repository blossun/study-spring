# 스프링 REST Docs 소개

https://docs.spring.io/spring-restdocs/docs/2.0.2.RELEASE/reference/html5/

REST Docs 코딩

* andDo(document(“doc-name”, snippets))
* snippets
  * **links()**
  * requestParameters() + parameterWithName()
  * pathParameters() + parametersWithName()
    * pathParameter : url에 들어온 패턴 정보 ex) {id}
  * requestParts() + partWithname()
    * parts request인 경우 ex) 파일 다운로드
  * requestPartBody()
  * requestPartFields()
  * requestHeaders() + headerWithName()
  * **requestFields()** + fieldWithPath()
  * responseHeaders() + headerWithName()
  * **responseFields()** + fieldWithPath()
  * ...
* Relaxed*
* Processor
  * preprocessRequest(prettyPrint())
  * preprocessResponse(prettyPrint())
  * ...

Constraint

* https://github.com/spring-projects/spring-restdocs/blob/v2.0.2.RELEASE/samples/rest-notes-spring-hateoas/src/test/java/com/example/notes/ApiDocumentation.java

---

Spring MVC Test를 사용해서 REST API 문서의 일부분을 생성하는데 유용한 라이브러리

테스트를 실행하면서 테스트를 실행할 때 사용한 요청과 응답, 응답의 헤더 etc.. 를 이용해서 문서 조각을 만들 수 있다.

문서 조각을 모아서 REST API Docs를 만들 수 있다.

REST Docs는 아스키 도커를 사용한다. 아스키 도커는 plain text로 작성한 문서를 Asciidoc(.adoc) 문법을 사용해 만든 스니펫을 조합해서 HTML문서로 만들어준다.



## 스니펫 생성

MockMvc, WebTestClient 등을 사용해 만든 테스트들을 Spring REST Docs와 연동해서 스니펫(문서조각)을 만들어낼 수 있다.

만들어진 스니펫은 (src>main>)`asciidoc>index.adoc` 템플릿에서 사용한다.



[최소 요구사항](https://docs.spring.io/spring-restdocs/docs/2.0.2.RELEASE/reference/html5/#getting-started-requirements)

- Java 8
- Spring Framework 5 (5.0.2 or later)



예제 ) MockMvc와 연동 - JUnit4 기준

documentationConfiguration()을 이용해서 restDocumentation를 끼워서 갖고 있다.

```java
private MockMvc mockMvc;

@Autowired
private WebApplicationContext context;

@Rule
public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

@Before
public void setUp() {
	this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
			.apply(documentationConfiguration(this.restDocumentation)) 
			.build();
}
```

* ~~스프링부트에서는 `@AutoConfigureRestDocs` 애노테이션만 붙이면 설정 끝~~이라고 했는데 안돼서 위의 코드 추가함



예제 ) 하이퍼미디어 API 제공

```java
this.mockMvc.perform(get("/").accept(MediaType.APPLICATION_JSON))
	.andExpect(status().isOk())
	.andDo(document("index", links( 
			linkWithRel("alpha").description("Link to the alpha resource"), 
			linkWithRel("bravo").description("Link to the bravo resource")))); 
```

`.andDo()` 테스트 실행 후, 결과를 document(`스니펫을 만들 디렉토리`, `링크정보`) 로 지정한 디렉토리 하위에 스니펫을 만들어준다.

links() : 링크정보를 documentation하겠다.

documentation이름 다음 인자로는 여러개의 스니펫을 랜덤하게 줄 수 있다. (가변인자로 스니펫을 제공)

스니펫 안에 정의할 수 있는 내용은 [문서](https://docs.spring.io/spring-restdocs/docs/2.0.2.RELEASE/reference/html5/#documenting-your-api) 참고



⇒ API 변경사항과 문서의 업데이트 불일치를 개발단계에서 미연에 방지할 수 있다.

API 변경으로 테스트 코드가 변경되면 문서의 내용도 자동으로 변경되고, 테스트를 실패한 부분에 대해서 테스트를 통과하도록 강제할 수 있다.

(Swagger 보다 추천)