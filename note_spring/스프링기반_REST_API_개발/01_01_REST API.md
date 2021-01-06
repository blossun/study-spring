# REST API

API

* **A**pplication **P**rogramming **I**nterface

REST

* **RE**presentational **S**tate **T**ransfer
* 인터넷 상의 시스템 간의 상호 운용성(interoperability)을 제공하는 방법중 하나
* 시스템 제각각의 **독립적인** **진화**를 보장하기 위한 방법
* REST API: REST 아키텍처 스타일을 따르는 API

REST 아키텍처 스타일 ([발표 영상 ](https://www.youtube.com/watch?v=RP_f5dMoHFc)11분)

* Client-Server
* Stateless
* Cache
* **Uniform Interface**
* Layered SystemCode-On-Demand (optional)

**Uniform Interface (발표 영상 11분 40초)**

* Identification of resources
* manipulation of resources through represenations
* **self-descrive messages**
* **hypermedia as the engine of appliaction state (HATEOAS)**

두 문제를 좀 더 자세히 살펴보자. (발표 영상 37분 50초)

* Self-descriptive message
  * 메시지 스스로 메시지에 대한 설명이 가능해야 한다.
  * 서버가 변해서 메시지가 변해도 클라이언트는 그 메시지를 보고 해석이 가능하다.
  * **확장 가능한 커뮤니케이션**
* HATEOAS
  * 하이퍼미디어(링크)를 통해 애플리케이션 상태 변화가 가능해야 한다.
  * **링크 정보를 동적으로 바꿀 수 있다.** (Versioning 할 필요 없이!)

Self-descriptive message 해결 방법 

* 방법 1: 미디어 타입을 정의하고 IANA에 등록하고 그 미디어 타입을 리소스 리턴할 때 Content-Type으로 사용한다.
* **방법 2: profile 링크 헤더를 추가한다. (발표 영상 41분 50초)**
  * [브라우저들이 아직 스팩 지원을 잘 안해](http://test.greenbytes.de/tech/tc/httplink/)
  * 대안으로 (본문에) [HAL](http://stateless.co/hal_specification.html)의 링크 데이터에 [profile 링크](https://tools.ietf.org/html/draft-wilde-profile-link-04) 추가

HATEOAS 해결 방법 

* 방법1: 데이터에 링크 제공**링크를 어떻게 정의할 것인가? HAL**
* 방법2: 링크 헤더나 Location을 제공

---

## API

* Java의 Interface, 웹으로 접근할 수 있는 Rest API

## [그런 REST API로 괜찮은가](https://www.youtube.com/watch?v=RP_f5dMoHFc)

* 현재의 API는 REST API가 아니다

* REST 아키텍처 스타일을 따라야 하는데, 그 중 **Uniform Interface** 스타일을 따르지 못하고 있다.

* Uniform Interface는 **self-descrive messages** 와 **hypermedia as the engine of appliaction state (HATEOAS)** 를 만족해야 하는데 이를 만족하지 못하고 있다.

  ⇒ 만족하지 못하기 때문에 필요없이 API에 버저닝을 하는 일이 벌어진다.

* 따라서 이 문제로 "시스템 제각각의 **독립적인** **진화**" 를 보장하지 못하고 있다.

* **self-descrive messages** 와 **hypermedia as the engine of appliaction state (HATEOAS)** 는 클라이언트와 서버 모두에게 해당하는 목표이다. 서로 간에 어떤 정보를 가지고 어떻게 소통할지에 대한 이야기이다.



HATEOAS

* Response에 상태 변화가 가능한 링크정보가 들어가 있어야 한다.

* 클라이언트는 하이퍼미디어를 통해서 다른 상태로 전이할 수 있어야한다.

* 클라이언트와 미리 URL을 약속해놓고 "~url에 보내면 ~한 일을하겠다." 이렇게 약속을 정해놓고 소통하는 것이 아니라, 유투버나 웹사이트에서 사용하듯이 

  응답을 받은 다음에 다음 애플리케이션 상태로 전이하려면 응답에 들어있는 ***링크*** 정보를 사용해서 이동해야한다는 의미

  서버가 링크를 변경하더라도, 클라이언트가 링크의 의미만 파악해서 이동한다면 실제 URI가 변경되더라도 상관이 없다. 

  ⇒ 버저닝을 할 필요가 없어진다.

  ⇒ 독립적인 진화 가능



> 오늘날 REST API라고 불리우는 것들은 과연 **Self-descriptive message** 를 달성하는가?

Naver와 Kakao API를 보면서 확인해보자



### Naver API 예제

#### 예제 1 ) [Naver Papago 언어감지 API Reference](https://developers.naver.com/docs/detectLangs/reference/)

응답예시 

```json
{ "langCode" : "ko" }
```

⇒ 이 자체로 무엇을 뜻하는지 알 수가 없다. 절대 REST API라고 할 수 없다.

langCode가 무엇을 뜻하는지 의미를 알 수 없음. → `Self-descriptive` 하지 않음

다음 상태로 전이할 수 있는 링크들이 없음 → `HATEOAS` 를 만족하지 않음



#### 예제 2 ) [Naver Clova 얼굴 감지 API](https://developers.naver.com/docs/clova/api/CFR/API_Guide.md#ErrorCode)

얼굴 감지 API 요청에 대한 응답 예

```json
// 1개의 얼굴을 감지한 경우
{
 "info": {
   "size": {
     "width": 900,
     "height": 1349
   },
   "faceCount": 1
 },
 "faces": [{
   "roi": {
     "x": 235,
     "y": 227,
     "width": 326,
     "height": 326
   },
   "landmark": {
     "leftEye": {
       "x": 311,
       "y": 289
     },
     "rightEye": {
       "x": 425,
       "y": 287
     },
   },
   "gender": {
     "value": "male",
     "confidence": 0.91465
   },
   "age": {
     "value": "22~26",
     "confidence": 0.742265
   },
 }]
}

// 감지한 얼굴이 없을 경우
{
 	"info": {
 		"size": {
 			"width": 700,
 			"height": 800
 		},
 		"faceCount": 0
 	},
 	"faces": []
 }
```

⇒ REST API라고 할 수 없다.

* 데이터가 무엇을 뜻하는지 알 수가 없다. → `Self-descriptive` 하지 않음. 문서를 봐야지만 알 수 있다. 문서를 볼 수 있는 링크 정보가 Response에 들어있지도 않다.

  응답만 가지고는 응답에 대한 의미를 해석할 수가 없다. 유추를 할 뿐... 정확한 내용을 보려면 문서를 참조해야한다.

  **Self-descriptive**해지려면 문서에 대한 링크(profile)를 본문에 담아주면 된다. 

* 다음 상태로 전이할 수 있는 링크들이 없음 → `HATEOAS` 를 만족하지 않음



#### 예제 3 ) [Naver 서비스 API > 검색 > 쇼핑](https://developers.naver.com/docs/search/shopping/)

#### 응답

```http
< HTTP/1.1 200 OK
< Server: nginx
< Date: Mon, 26 Sep 2016 09:04:44 GMT
< Content-Type: text/xml;charset=utf-8
< Transfer-Encoding: chunked
< Connection: keep-alive
< Keep-Alive: timeout=5
< Vary: Accept-Encoding
< X-Powered-By: Naver
< Cache-Control: no-cache, no-store, must-revalidate
< Pragma: no-cache
<
<rss version="2.0">
    <channel>
        <title>Naver Open API - shop ::'가방'</title>
        <link>http://search.naver.com</link>
        <description>Naver Search Result</description>
        <lastBuildDate>Tue, 04 Oct 2016 13:23:58 +0900</lastBuildDate>
        <total>17161390</total>
        <start>1</start>
        <display>10</display>
        <item>
            <title>허니트립 보스턴백</title>
            <link>http://openapi.naver.com/l?AAABWLsQ7CIBRFv+Z1JLzSShkYqLajRmPcG6TQRCgiNunfizdnODnJfX9N2iUMCnoKHYWh/4sSlUtmli7nCExBPRY+bo1xCZaEaTOJ6NWXaKdsSHAB2Lg8gZ2QMmybA0cuqiyx4W0ZZR2KrvJyx2CPV3RQ95fbnDT3r+Fh2kbfz5su7x8wIs7ZjgAAAA==</link>
            <image>http://shopping.phinf.naver.net/main_1031546/10315467179.jpg</image>
            <lprice>6700</lprice>
            <hprice>0</hprice>
            <mallName>허니트립</mallName>
            <productId>10315467179</productId>
            <productType>2</productType>
            <brand></brand>
            <maker>허니트립</maker>
            <category1>패션잡화</category1>
            <category2>여행용가방/소품</category2>
            <category3>보스턴백</category3>
            <category4></category4>
        </item>
        ...
    </channel>
</rss>
```

⇒ ~~REST API 이다.~~ REST API 일 뻔 함.

* Content-Type : `application/rss+xml` rss용 컨텐츠 타입이 있는데 `text/xml` 라고 들어가 있다.

  → `Self-descriptive` 하지 않음. 

  text/xml 은 제너럴한 타입이기 때문에 rss 태그들을 해석하지 못한다. 똑똑한 브라우저라면 xml로 넘어왔더라도 rss로 판단해서 rss로 알아서 파싱하겠지만, 좀 더 정확하게 하려면 `Content-Type : application/rss+xml` 으로 줬어야 본문을 해석 가능하게 된다. → `Self-descriptive message` 를 충족하게 됨

* 아래 예제에서 *title*에 해당하는 내용을 보고 싶다면 *link* 헤더를 보고 해당 값으로 이동하면된다.

  *link* 의 값이 어떤 것이든 수정되든 상관없이 "주어진 url로 이동하면 된다"는 의미만 파악해서 이동할 수 있으면 된다.

  ```xml
  <item>
    <title>허니트립 보스턴백</title>
    <link>http://openapi.naver.com/l?AAA/link></link>
  </item>
  ```

  → `HATEOAS` 하다.



#### 예제 4 ) [Github Issues API](https://docs.github.com/en/free-pro-team@latest/rest/reference/issues)

* `Accept: application/vnd.github.v3+json`  깃헙은 Media Type을 IANA에 모두 등록해놨다.

  →  `Self-descriptive` 하다.

  응답에 대한 미디어타입을 정의한 후, 미디어타입에 응답에 들어올 메시지를 다 정의해 놓는다. 이러한 방식으로  `Self-descriptive` 을 충족시킬 수 있다.

* HAL이라는 스펙을 따르지는 않지만, 응답을 받은 후 다음 상태로 전이할 수 있는 링크들이 많다. → `HATEOAS` 하다.

  ```json
  [
    {
      "id": 1,
      "node_id": "MDU6SXNzdWUx",
      "url": "https://api.github.com/repos/octocat/Hello-World/issues/1347",
      "repository_url": "https://api.github.com/repos/octocat/Hello-World",
      "labels_url": "https://api.github.com/repos/octocat/Hello-World/issues/1347/labels{/name}",
      "state": "open",
      "title": "Found a bug",
      "body": "I'm having a problem with this.",
      "user": {
        "login": "octocat",
        "id": 1,
        "node_id": "MDQ6VXNlcjE=",
        "avatar_url": "https://github.com/images/error/octocat_happy.gif",
        "gravatar_id": "",
        "url": "https://api.github.com/users/octocat",
        "html_url": "https://github.com/octocat",
        "followers_url": "https://api.github.com/users/octocat/followers",
        "following_url": "https://api.github.com/users/octocat/following{/other_user}",
        "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
      },
      "labels": [
        {
          "id": 208045946,
          "node_id": "MDU6TGFiZWwyMDgwNDU5NDY=",
          "url": "https://api.github.com/repos/octocat/Hello-World/labels/bug",
          "name": "bug",
          "description": "Something isn't working",
          "color": "f29513",
          "default": true
        }
      ],
      "assignee": {
        "login": "octocat",
        "id": 1,
        "node_id": "MDQ6VXNlcjE=",
        "avatar_url": "https://github.com/images/error/octocat_happy.gif",
        "gravatar_id": "",
        "url": "https://api.github.com/users/octocat",
  ```

  → 이 REST API를 소비하는 클라이언트는 링크 URL 템플릿을 외워서 작성할 필요가 없다. 

  ​	ex) `labels.url` 값을 읽어서 이동하면 된다.



⇒ 강좌에서는 **self-descrive messages** 와 **HATEOAS** 두가지 문제를 **`HAL(Hypertext Application Language)`** 라는 스팩을 이용해서 링크를 제공하는 방법 사용. 

Self-descriptive message 해결 방법 

* **방법 2: profile 링크 헤더를 추가한다.**
  * **본문에** [HAL](http://stateless.co/hal_specification.html)의 링크 데이터에 [profile 링크](https://tools.ietf.org/html/draft-wilde-profile-link-04) 추가

HATEOAS 해결 방법 

* 방법1: 데이터에 링크 제공**링크를 어떻게 정의할 것인가? HAL**