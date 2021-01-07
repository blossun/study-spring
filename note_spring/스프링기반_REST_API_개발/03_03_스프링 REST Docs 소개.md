# 스프링 REST Docs 소개

https://docs.spring.io/spring-restdocs/docs/2.0.2.RELEASE/reference/html5/

REST Docs 코딩

* andDo(document(“doc-name”, snippets))
* snippets
  * **links()**
  * requestParameters() + parameterWithName()
  * pathParameters() + parametersWithName()
  * requestParts() + partWithname()
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





