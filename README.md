# kuembang-be-assignment 프로젝트

## 프로젝트 설명

- 본 프로젝트는 금방주식회사 백엔드 입사과제의 <a href="https://github.com/DaniHoon/Entry-BE-Assignment">레포지토리</a>를 참고하여 Java/Spring Boot로 구현한 프로젝트 입니다

## 프로젝트 구조

이 프로젝트는 두 개의 서버로 나누어져 있습니다

### 1. keumbang-auth (인증서버)

- **역할**: 인증 및 권한 부여 처리, JWT 발급 및 검증, gRPC 서버 구현

### 2. keumbang-resource (자원서버)

- **역할**: 자원 관리 및 주문 처리, gRPC 클라이언트 사용

## 사용 기술

![Java](https://img.shields.io/badge/Java-17-blue?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.3-brightgreen?style=flat-square)
![JPA](https://img.shields.io/badge/JPA-ORM-orange?style=flat-square)
![gRPC](https://img.shields.io/badge/gRPC-1.66.0-yellow?style=flat-square)
![MariaDB](https://img.shields.io/badge/MariaDB-5F2E88?style=flat-square)

## 설계

### keumbang-auth ERD
<img width="787" alt="keumbang-auth ERD" src="https://github.com/user-attachments/assets/d3152641-171f-4d48-a32b-9a18b0497320">

### keumbang-resource ERD
<img width="981" alt="keumbang-resource ERD" src="https://github.com/user-attachments/assets/98f5a6a0-a3a7-4c1c-ab88-adb4df4c87db">

## Quick Start

본 가이드를 따라 keumbang-auth 과 keumbang-resource 서버를 빠르게 시작할 수 있습니다. <br>
Git, Java, MariaDB는 설치되어 있다고 가정합니다.

### 1. 프로젝트 클론

원하는 위치에서 프로젝트를 clone 합니다.

```bash
$ git clone https://github.com/sung-silver/keumbang-be-assignment.git
$ cd keumbang-be-assignment
```

### 2. application.properties 파일 및 data.sql 추가

google drive에 첨부된 auth-application.properties, resource-application.properties, auth-data.sql, resource-data.sql의 내용을 복사하여 다음 경로에 파일을 생성합니다<br>
MariaDB가 각각 존재할 경우 application.properties를 생성하는 시점에서 DB_URL을 맞추어 변경해줍니다<br>
그렇지 않은 경우 MariaDB를 실행하여 keumbang-auth, keumbang-resource 스키마를 각각 생성하여 사용합니다<br>

```bash
# auth 서버 설정 파일 생성
$ cd keumbang-auth/src/main/resources/
# vim 편집기에서 auth-application.properties의 내용을 복사 붙여넣기 한 후 저장합니다 (:wq)
$ vim application.properties
# vim 편집기에서 auth-data.sql의 내용을 복사 붙여넣기 한 후 저장합니다 (:wq)
$ vim data.sql

## 프로젝트 루트로 이동합니다
$ cd ../../../../

# resource 서버 설정 파일 생성
$ cd keumbang-resource/src/main/resources/
# vim 편집기에서 resource-application.properties의 내용을 복사 붙여넣기 한 후 저장합니다 (:wq)
$ vim application.properties
# vim 편집기에서 resource-data.sql의 내용을 복사 붙여넣기 한 후 저장합니다 (:wq)
$ vim data.sql

## 프로젝트 루트로 이동합니다
$ cd ../../../../
```

### 3. 프로젝트 빌드

각각의 서버 디렉토리에서 다음 명령어를 실행하여 프로젝트를 빌드합니다

```bash
# auth 프로젝트 빌드
$ cd keumbang-auth
$ ./gradlew build -x test

# 프로젝트 루트로 이동합니다
$ cd ../../../../

# resource 프로젝트 빌드
$ cd keumbang-resource
$ ./gradlew build -x test

# 프로젝트 루트로 이동합니다
$ cd ../../../../
```

### 4-1. keumbang-auth 서버 실행

keumbang-auth 서버 디렉토리로 이동한 후, 다음 명령어로 서버를 실행합니다.

```bash
$ cd keumbang-auth/build/libs
$ java -jar -Dspring.profiles.active=auth auth-0.0.1-SNAPSHOT.jar

## 프로젝트 루트로 이동합니다
$ cd ../../../../
```

### 4-2. keumbang-resource 서버 실행

keumbang-resource 서버 디렉토리로 이동한 후, 다음 명령어로 서버를 실행합니다.

```bash
$ cd keumbang-resource/build/libs
$ java -jar -Dspring.profiles.active=resource resource-0.0.1-SNAPSHOT.jar

## 프로젝트 루트로 이동합니다
$ cd ../../../../
```

## API 명세서

### Swagger

두 개의 서버가 모두 실행이 되면, Swagger로 API 명세를 확인할 수 있습니다.

- <a href="http://localhost:8888/swagger-ui/index.html#/">keumbang-auth Swagger 바로가기</a>
- <a href="http://localhost:9999/swagger-ui/index.html#/">keumbang-resource Swagger 바로가기</a>

### Postman

실행이 되면, Postman으로 API를 호출할 수 있습니다.<br>
Postman에서는 각각 서버에 대한 요청을 HEADER([AUTH], [RESOURCE])로 구분합니다<br>
자원 서버 호출 시 인증 서버에서 회원 가입을 진행하고 발급받은 AccessToken을 사용하여 API를 호출합니다

- <a href="https://documenter.getpostman.com/view/22245127/2sAXqmB5ZH">kuembang-be-assignment Postman 바로가기</a>
