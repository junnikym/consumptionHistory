# 소비내역 기록/관리 API

** 제출 기간이 지났지만 README 는 작성하는 것이 좋을 것 같아 기간 이후 README 만 업로드 하였습니다.

## 사용한 언어 및 라이브러리

- Java 1.8 / Spring Boot
- JPA & MySQL 5.7
- Spring Security
- JUnit

## 실행

``` shell
./gradlew build
docker-compose up -d

// 종료 시 
docker-compose down
```

## 구현
 - <b>회원인증</b>

jjwt를 통해 발행된 JWT로 토큰 기반 인증 시스템을 구현하였습니다. Spring Security 에 JWT 인증 필터를 추가하여 로그인이 되지 않을 시 ``` 401 Unauthorized``` 에러가 발생합니다.

다른 사용자의 소비내역을 조회, 수정, 삭제 시 JWT 에 등록된 사용자 정보와 DB에 등록된 사용자 정보와 일치하는지 확인 후 에러가 발생합니다.

403 Forbidden 가 발생할 경우 사용자가 의도치 않게 남의 데이터를 방문하거나 특정 데이터가 존제하는지 조회하기 위한 목적으로 사용할 수 있겠다 생각하여 위와 같은 경우는 ```404 Not Found``` 가 발생하도록 구현하였습니다.

 - <b>가게부 관리</b>

가게부 조회는 목록으로 조회 시 자세한 내용이 필요없다 생각하여 DTO 를 요약본인 SummaryDTO 와 자세하게 출력되는 DetailDTO 2가지로 나누어 List 형으로 반환 될 경우 SummaryDTO 를 하나만 출력 될 경우 DetailDTO 형태로 반환 하였습니다.

삭제/복구 기능은 기록 테이블인 History 중 isDeleted 컬럼을 통해 삭제 여부를 정한 뒤 삭제된 데이터를 복구하고 싶을 때 해당 컬럼의 값을 변경하여 복구합니다

## API 구성

### 회원관리 관련 API

회원가입
```
[POST] /api/v1/auth/signup
{
    "email" : < email 값 >,
    "password" : < password 값 >
}
```

로그인
```
[POST] /api/v1/auth/login
{
    "email" : < email 값 >,
    "password" : < password 값 >
}
```

### 소비내역 관련 API

소비내역 관련 API는 JWT가 필요합니다. 로그인을 통해 발행된 토큰을 아래와 같이 해더에 포함시켜야 정상작동됩니다. 
```
Authorization: Bearer < JWT 값 >
```

소비내역 생성
```
[POST] /api/v1/history
{
    "amount" : < 가격 >,
    "summaryMemo" : < 요약 메모 >,
    "detailMemo" : < 자세한 메모 >
}
```

소비내역(요약) 리스트 조회
```
[GET] /api/v1/history
```

소비내역(자세히) 조회
```
[GET] /api/v1/history/{id}
```

소비내역 수정
```
[PUT] /api/v1/history/{id}
{
    "amount" : < 가격 >,
    "summaryMemo" : < 요약 메모 >,
    "detailMemo" : < 자세한 메모 >
}

 * 수정하고싶은 항목만 포함
```

소비내역 삭제 (단일)
```
[DELETE] /api/v1/history/{id}
```

소비내역 삭제 (여러개)
```
[DELETE] /api/v1/history?id=<소비내역 UUID 1>&id=<소비내역 UUID 2>
```

삭제된 소비내역 조회
```
[GET] /api/v1/history/delete
```

소비내역 복구 (단일)
```
[PATCH] /api/v1/history/recover/{id}
```

소비내역 복구 (여러개)
```
[PATCH] /api/v1/history/recover?id=<소비내역 UUID 1>&id=<소비내역 UUID 2>
```