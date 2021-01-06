# Event 생성 API 구현: Bad Request 응답 본문 만들기

커스텀 JSON Serializer 만들기

* extends JsonSerializer<T> (Jackson JSON 제공)
* @JsonComponent (스프링 부트 제공)

BindingError

* FieldError 와 GlobalError (ObjectError)가 있음
* objectName
* defaultMessage
* codefield
* rejectedValue

테스트 할 것

> * 입력 데이터가 이상한 경우 Bad_Request로 응답
>   * 입력값이 이상한 경우 에러
>   * 비즈니스 로직으로 검사할 수 있는 에러
>   * **에러 응답 메시지에 에러에 대한 정보가 있어야 한다.**

---









