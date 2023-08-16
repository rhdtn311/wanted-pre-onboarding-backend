# wanted-pre-onboarding-backend 과제 (공태현)
## 애플리케이션 실행 방법
1. `application.properties`에 다음과 같이 설정 정보를 작성합니다.
```groovy
spring.datasource.url=jdbc:mysql://localhost:3306/wanted?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC
spring.datasource.username=[mysql_id]
spring.datasource.password=[mysql_password]
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

spring.jpa.database=mysql
spring.jpa.hibernate.ddl-auto=create 
```
2. `WantedPreOnboardingBackendApplication`를 실행합니다.

### 앤드포인트 호출 방법
1. **회원가입 (POST: /users)**

HTTP Body에 application/json 형식으로 다음과 같이 요청합니다.
```json
{
    "email": {
        "address": "email@wanted.com"
    },
    "password": {
        "password": "password"
    }
}
```
2. **로그인 (POST: /users/login)**

HTTP Body에 application/json 형식으로 다음과 같이 요청합니다.
```json
{
    "email": {
        "address": "email@naver.com"
    },
    "password": {
        "password": "password!"
    }
}
```
3. **게시글 생성 (POST: /posts)**

HTTP Body에 application/json 형식으로 다음과 같이 요청합니다.
```json
{
    "title": "제목",
    "content": "내용"
}
```

4. **게시글 목록 조회 (GET: /posts)**

HTTP 요청 파라미터에 다음과 같이 요청합니다.
``` http request
?page=1&size=10
```

5. **게시글 단일 조회 (GET: /posts/{postId})**

HTTP 요청 URI에 다음과 같이 요청합니다.
``` http request
/posts/1
```
6. **게시글 수정 (PUT: /posts/{postId})**

   HTTP 요청 URI에 다음과 같이 요청합니다.
``` http request
/posts/1
```
HTTP Body에 application/json 형식으로 다음과 같이 요청합니다.
```json
{
    "title": "수정할 제목",
    "content": "수정할 내용"
}
```

7. **게시글 삭제 (DELETE: /posts/{postId})**

HTTP 요청 URI에 다음과 같이 요청합니다.
``` http request
/posts/1
```

## 데이터베이스 테이블 구조
![image](https://github.com/JionisGenius/wanted-pre-onboarding-backend/assets/68289543/89edefeb-0014-4907-a87f-370af3f07e5a)

## API 동작 영상 링크

## 구현 방법 및 이유
- 비밀번호 암호화 
  - 암호화 방식으로 rainbow table attack을 방지할 수 있는 Bcrypt 암호화 방식을 사용했습니다.
  - 코드 수정 없이 다른 암호화 방식으로 변경할 수 있도록 `PasswordEncoder` 인터페이스를 구현하여 암호화 역할을 하는 구현체를 만들었습니다.
- JWT
  - JWT를 구현하기 위해 간단하게 사용할 수 있는 jjwt 라이브러리를 사용했습니다.
  - `JwtTokenManager`에서 토큰을 생성하고 유효성을 검증, 토큰에서 사용자 정보 파싱하는 책임을 가집니다.
  - 토큰을 생성할 때 필요한 `Header`와 `Payload`의 값을 각각 `JwtHeader`, `JwtPayload` 객체에서 관리하도록 했습니다.
  - JWT 토큰을 생성할 때 필요한 키값은 따로 관리해야 하지만 과제 특성상 임의로 `JwtTokenManager`에 필드 값으로 두었습니다.
- `ArgumentResolver`
  - JWT 토큰에서 사용자의 이메일 정보를 가져오는 로직은 필요한 API를 호출할 때마다 중복되어야 합니다.
  - 이러한 코드 중복을 해결하기 위해 `@LoginCheck` 어노테이션이 포함되어 있는 핸들러의 파라미터는 HTTP 요청 헤더에서 토큰 값을 얻어와서 사용자 Email 정보를 파싱하는 `ArgumentResolver`를 커스텀하여 등록했습니다.
- DTO
  - 요청, 응답 값을 전달하는 DTO는 모두 Record 타입으로 생성했습니다.
  - 코드의 간결성과 불변객체로 관리하기 위함입니다.
- Email, Password를 객체로 관리
  - 사용자의 이메일과 비밀번호를 `String`이 아닌 객체로 생성하여 관리하였습니다.
  - 이렇게 관리함으로써 여러 곳에서 중복되는 이메일과 비밀번호 검증 로직을 하나의 클래스에서 공통적으로 관리할 수 있습니다.
- 공통 예외 처리
  - 예외를 공통적으로 `ErrorResponse`라는 객체에서 관리하도록 합니다.
  - `GlobalExceptionHandler`에서 발생하는 모든 예외를 잡아 일관된 `ErrorResponse` 형식으로 반환합니다.
  - 클라이언트 입장에서 일관된 로직으로 예외를 처리할 수 있습니다.

# API 명세

## 1. **회원가입 (POST: /users)**

### **HTTP Request Body**
```json
{
    "email": {
        "address": "email@wanted.com"
    },
    "password": {
        "password": "password"
    }
}
```
### 성공(201)
#### HTTP Response Header
```http request
Location: /users/1
```

<br>

### 실패: 이메일 중복(400)
#### HTTP Response Body
```json
{
    "code": "INTERNAL_SERVER_ERROR",
    "message": "이미 존재하는 이메일입니다.",
    "inputErrors": null
}
```
### 실패: 검증 에러(400)
#### HTTP Response Body
```json
{
    "code": "INVALID_INPUT_ERROR",
    "message": "입력값 검증 오류입니다.",
    "inputErrors": [
        {
            "message": "잘못된 이메일 형식입니다.",
            "field": "email"
        },
        {
            "message": "잘못된 비밀번호 형식입니다.",
            "field": "password"
        }
    ]
}
```
<br>

## 2. 로그인 (POST: /users/login)
### **HTTP Request Body**
```json
{
    "email": {
        "address": "email@naver.com"
    },
    "password": {
        "password": "password!"
    }
}
```
### 성공(200)
#### HTTP Response Body
```json
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyaGR0bjMxMUBuYXZlci5jb20iLCJpc3MiOiJodHRwczovL3dhbnRlZC5jb20vYXV0aCIsImp0aSI6ImQwOWI4ZjMyLTc5YjYtNGRiYi04NmFkLTZhYzgwYjY0MzBhNSIsImVtYWlsIjoicmhkdG4zMTFAbmF2ZXIuY29tIiwiZXhwIjoxNjkxOTM3NjA4fQ.HEEDRN_g81pfbbLD-DDnQDkONNbeQAWjlo1ssYWLFAI
```
<br>

### 실패: 존재하지 않는 사용자(400)
#### HTTP Response Body
```json
{
   "code": "INTERNAL_SERVER_ERROR",
   "message": "존재하지 않는 사용자입니다.",
   "inputErrors": null
}
```
### 실패: 검증 에러(400)
#### HTTP Response Body
```json
{
    "code": "INVALID_INPUT_ERROR",
    "message": "입력값 검증 오류입니다.",
    "inputErrors": [
        {
            "message": "잘못된 이메일 형식입니다.",
            "field": "email"
        },
        {
            "message": "잘못된 비밀번호 형식입니다.",
            "field": "password"
        }
    ]
}
```
<br>

## 3. **게시글 생성 (POST: /posts)**
### **HTTP Request Body**
```json
{
    "title": "제목",
    "content": "내용"
}
```
### 성공(201)
#### HTTP Response Header
```http request
Location: /posts/1
```
<br>

### 실패: 인증 헤더가 존재하지 않는 경우(409)
#### HTTP Response Body
```json
{
   "code": "INTERNAL_SERVER_ERROR",
   "message": "인증 헤더가 존재하지 않습니다.",
   "inputErrors": null
}
```
<br>

## 4. **게시글 목록 조회 (GET: /posts)**
### **HTTP Request Parameter**
``` http request
?page=1&size=10
```
### 성공(200)
#### HTTP Response Body
```json
{
    "postResponses": [
        {
            "id": 3,
            "title": "게시글2",
            "content": "2",
            "email": {
                "address": "rhdtn311@naver.com"
            }
        },
        {
            "id": 2,
            "title": "게시글1",
            "content": "1",
            "email": {
                "address": "rhdtn311@naver.com"
            }
        }
    ]
}
```
<br>

## 5. **게시글 단일 조회 (GET: /posts/{postId})**

### HTTP URI Path
``` http request
/posts/1
```
### 성공(200)
#### HTTP Response Body
```json
{
   "id": 2,
   "title": "게시글1",
   "content": "1",
   "email": {
      "address": "rhdtn311@naver.com"
   }
}
```
<br>

### 실패: 게시글이 존재하지 않는 경우(409)
#### HTTP Response Body
```json
{
    "code": "INTERNAL_SERVER_ERROR",
    "message": "존재하지 않는 게시글입니다.",
    "inputErrors": null
}
```
<br>

## 6. **게시글 수정 (PUT: /posts/{postId})**
### HTTP URI Path
``` http request
/posts/1
```
### HTTP Request Body
```json
{
    "title": "수정할 제목",
    "content": "수정할 내용"
}
```
### 성공(204)
#### HTTP Response Body
```json
(Blank)
```
<br>

### 실패: 게시글이 존재하지 않는 경우(409)
#### HTTP Response Body
```json
{
    "code": "INTERNAL_SERVER_ERROR",
    "message": "존재하지 않는 게시글입니다.",
    "inputErrors": null
}
```
### 실패: 게시글 수정 권한이 없는 경우(409)
#### HTTP Response Body
```json
{
    "code": "INTERNAL_SERVER_ERROR",
    "message": "게시글 접근 권한이 없는 사용자입니다.",
    "inputErrors": null
}
```
<br>

## 7. **게시글 삭제 (DELETE: /posts/{postId})**
### HTTP URI Path
``` http request
/posts/1
```
### 성공(204)
#### HTTP Response Body
```json
(Blank)
```
<br>

### 실패: 게시글이 존재하지 않는 경우(409)
#### HTTP Response Body
```json
{
    "code": "INTERNAL_SERVER_ERROR",
    "message": "존재하지 않는 게시글입니다.",
    "inputErrors": null
}
```
### 실패: 게시글 수정 권한이 없는 경우(409)
#### HTTP Response Body
```json
{
    "code": "INTERNAL_SERVER_ERROR",
    "message": "게시글 접근 권한이 없는 사용자입니다.",
    "inputErrors": null
}
```
<br>