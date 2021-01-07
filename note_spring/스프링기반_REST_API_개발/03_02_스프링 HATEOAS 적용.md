# 스프링 HATEOAS 적용

EvnetResource 만들기

* extends ResourceSupport의 문제
  * @JsonUnwrapped로 해결
  * extends Resource<T>로 해결

테스트 할 것

> * 응답에 HATEOA와 profile 관련 링크가 있는지 확인.
>   * self (view)
>   * update (만든 사람은 수정할 수 있으니까)
>   * events (목록으로 가는 링크)

---

