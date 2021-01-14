# 스프링 HATEOAS 소개

스프링 HATEOAS

* https://docs.spring.io/spring-hateoas/docs/current/reference/html/
* 링크 만드는 기능
  * 문자열 가지고 만들기
  * 컨트롤러와 메소드로 만들기
* 리소스 만드는 기능
  * 리소스: 데이터 + 링크
  * 링크 찾아주는 기능
    * Traverson
    * LinkDiscoverers
  * 링크
    * HREF
    * REL
      * self
      * profile
      * update-event
      * query-events

![img](images/tE_PeVBhr56TsyoYIXBpjKwyDA93z5kKxo3T5WgHlF36p120tLAtMaOFLpaUHkbg_fEjLwl23OOirN-ebVaHkER8wk2cT4uPfdFkWafzhhIipR4z9LGX4SLE5avtbsZwijYxcZ_K.png)

---

HATEOAS를 만족하는 REST API를 제공하기위해 편리하게 사용할 수 있는 라이브러리

HATEOAS ?

* [wikipedia](https://en.wikipedia.org/wiki/HATEOAS)

* REST 애플리케이션 아키텍처의 컴포넌트 중 하나
* 하이퍼미디어를 통해서 애플리케이션 서버의 정보를 동적으로 주고받을 수 있는 방법
* (위키피디아 예제 참고)

이러한 요청을 보냈을 때, 

```java
GET /accounts/12345 HTTP/1.1
Host: bank.example.com
Accept: application/vnd.acme.account+json
...
```

응답으로 다음과 같은 정보가 들어가 있다.

`links` : 현재 이 리소스와의 릴레이션으로 어떠한 액션(서버와 클라이언트간 상호작용)을 할 수있는지 & 그 때의 url 주소 설정

클라이언트는 ref(주소 key값)만 보고 서버와 소통한다면 url이 바뀌더라도 상관이 없다.

```java
HTTP/1.1 200 OK
Content-Type: application/vnd.acme.account+json
Content-Length: ...

{
    "account": {
        "account_number": 12345,
        "balance": {
            "currency": "usd",
            "value": 100.00
        },
        "links": {
            "deposit": "/accounts/12345/deposit",
            "withdraw": "/accounts/12345/withdraw",
            "transfer": "/accounts/12345/transfer",
            "close": "/accounts/12345/close"
        }
    }
}
```



마이너스 계좌인 경우 `deposit` (입금)만 할 수 있다.

```java
HTTP/1.1 200 OK
Content-Type: application/vnd.acme.account+json
Content-Length: ...

{
    "account": {
        "account_number": 12345,
        "balance": {
            "currency": "usd",
            "value": -25.00
        },
        "links": {
            "deposit": "/accounts/12345/deposit"
        }
    }
}

```

⇒ 즉, 애플리케이션 상태에 따라 링크정보가 바뀌어야 한다.



클라이언트(HATEOAS를 따르는 API를 소비하는 쪽) 입장에서 어떻게 하면 링크를 보다 쉽게 가져와서 사용할 수 있도록 링크를 찾아주는 기능 (`Traverson`, `LinkDiscoverers`)을 제공해준다.



### 링크에 들어가는 정보

* HREF : uri, url
* REL : 현재 이 리소스와의 관계 표현
  * self : 자기자신 url
  * profile : 현재 응답 본문에 대한 문서
  * update-event : (이벤트 업데이트)
  * query-events : (이벤트 조회)

