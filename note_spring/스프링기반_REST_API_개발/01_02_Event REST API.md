# “Event” REST API

이벤트 등록, 조회 및 수정 API

#### **GET /api/events**

이벤트 목록 조회 REST API (로그인 안 한 상태)

* 응답에 보여줘야 할 데이터
  * 이벤트 목록
  * 링크
    * self
    * profile: 이벤트 목록 조회 API **문서**로 링크
    * get-an-event: 이벤트 하나 조회하는 API 링크
    * next: 다음 페이지 (optional)
    * prev: 이전 페이지 (optional)
* 문서?
  * 스프링 REST Docs로 만들 예정

이벤트 목록 조회 REST API (로그인 한 상태)

* 응답에 보여줘야 할 데이터
  * 이벤트 목록
  * 링크
    * self
    * profile: 이벤트 목록 조회 API **문서**로 링크 → Self-descriptive message 충족
    * get-an-event: 이벤트 하나 조회하는 API 링크
    * **create-new-event: 이벤트를 생성할 수 있는 API 링크** ★ 
    * next: 다음 페이지 (optional)
    * prev: 이전 페이지 (optional)
* 로그인 한 상태???? (stateless라며..)
* 아니, 사실은 Bearer 헤더에 유효한 AccessToken이 들어있는 경우!
* 세션 정보를 유지하는 것은 아님

⇒ 하이퍼미디어 정보는 현재 상태에 따라 달라진다. 그래서 HATEOAS를 구현할 때는 현재 상태에 적합한 링크 정보들을 줘야한다.

로그인은 안 한 상태에서 이벤트를 생성할 수 없을 때, *이벤트 생성할 수 있는 API* 링크를 넘겨주고 해당 링크로 이동해오면 로그인 요청 페이지로 redirect 시킬 수 있지만 애초에 *이벤트 생성할 수 있는 API* 링크를 넘겨주지 않는 것이 좋은 UX이다.

#### **POST /api/events**

* 이벤트 생성

#### **GET /api/events/{id}**

* 이벤트 하나 조회

#### **PUT /api/events/{id}**

* 이벤트 수정

---

