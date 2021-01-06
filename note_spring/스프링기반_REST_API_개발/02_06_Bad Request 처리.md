# Event 생성 API 구현: Bad Request 처리하기

@Valid와 BindingResult (또는 Errors)

* BindingResult는 항상 @Valid 바로 다음 인자로 사용해야 함. (스프링 MVC)
* @NotNull, @NotEmpty, @Min, @Max, ... 사용해서 입력값 바인딩할 때 에러 확인할 수 있음

도메인 Validator 만들기

* [Validator](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/validation/Validator.html) 인터페이스 없이 만들어도 상관없음

테스트 설명 용 애노테이션 만들기

* @Target, @Retention

테스트 할 것

> * 입력 데이터가 이상한 경우 Bad_Request로 응답
>   * **입력값이 이상한 경우 에러**
>   * **비즈니스 로직으로 검사할 수 있는 에러**
>   * 에러 응답 메시지에 에러에 대한 정보가 있어야 한다.

---







