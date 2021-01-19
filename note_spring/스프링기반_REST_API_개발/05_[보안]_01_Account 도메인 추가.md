# Account 도메인 추가

OAuth2로 인증을 하려면 일단 Account 부터

* id
* email
* password
* roels

AccountRoles

* ADMIN, USER

JPA 맵핑

* @Table(“Users”)

JPA enumeration collection mapping

```java
@ElementCollection(fetch = FetchType.EAGER)
@Enumerated(EnumType.STRING)
private Set<AccountRole> roles;
```

Event에 owner 추가

```java
@ManyToOne
Account manager;
```

---

현재 인증과 관련된 부분이 하나도 없다. 지금은 아무나 POST 요청을 보내면 이벤트를 생성, 수정이 가능

이벤트 생성

* 인증된 사용자

이벤트 수정

* 이벤트를 만든 사람
* 이벤트와 연관된 계정

SpringSecurity OAuth2를 도입한다.

여러가지 인증 방법(GrantType)을 제공해주는데, 그 중에서 Password라는 GrantType을 사용해서 Spring OAuth를 사용할 것이다.



## 계정과 관련된 Accout 엔티티를 생성

※ DB에서 `user`는 키워드로 사용되기 때문에 user 테이블을 만들 수 없다. 따라서 User가 아닌 `Account` 도메인을 추가

※ user 클래스명을 사용하려면 @Table 애노테이션을 사용해서 다른 테이블명을 설정해줄 수 있다. ex) `@Table("Users")` 로 맵핑



### JPA enumeration collection mapping

하나의 enum이 아니라 여러개의 enum을 가질 수 있으므로 `@ElementCollection`로 맵핑해줘야한다.

그리고, 기본적으로 모든 Set, List는 FetchType이 Lazy인데, 이 경우에는 가져올 role이 굉장히 적고, 거의 매번 Account 정보를 가져올 때마다 필요한 정보라서 `EAGER` 모드로 패치하도록 지정

```java
@ElementCollection(fetch = FetchType.EAGER)
@Enumerated(EnumType.STRING)
private Set<AccountRole> roles;
```



## 연관관계 맵핑 - Event → Account 단방향

즉, Event에서만 owner를 참조할 수 있도록 참조를 걸어준다.

* Event에 owner 추가

```java
@ManyToOne
Account manager; //관리자
```



> Event 도메인 테스트와 마찬가지로 builder와 javaBean으로 만들 수 있는지 테스트를 작성할 수 있지만 이 테스트는 롬복을 학습하기위한 학습용 테스트에 가깝기 때문에,
>
> 이정도 도메인으로 테스트를 진행하는 것은 스킵

