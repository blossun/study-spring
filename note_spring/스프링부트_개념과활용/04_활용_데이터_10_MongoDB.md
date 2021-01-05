스프링 데이터 10부: MongoDB

[MongoDB](https://www.mongodb.com/)는 JSON 기반의 도큐먼트 데이터베이스입니다.

의존성 추가

* spring-boot-starter-data-mongodb

MongoDB 설치 및 실행 (도커)

```sh
docker run -p 27017:27017 --name mongo_boot -d mongo
docker exec -i -t mongo_boot bash
mongo
```

스프링 데이터 몽고DB

* MongoTemplate
* MongoRepository
* 내장형 MongoDB (테스트용)
  * de.flapdoodle.embed:de.flapdoodle.embed.mongo
* @DataMongoTest

---

