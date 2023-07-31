# 토이프로젝트 저장소
## 주제 선정 이유

개발자 준비를 하면서 여러사람들의 개인 프로젝트, 토이 프로젝트를 참고하거나 구경하려면 구글링을 통해 하나하나 찾는방법 밖에 없었습니다. 

이러한 방법은 시간적인 소요가 너무 많이 들었고 이러한 개인프로젝트, 토이프로젝트가 한 곳에 모여있으면 어떨까하는 생각을 해봤습니다. 

따라서 사람들이 이를 포스팅할 수 있는 플랫폼을 구상하게 되었고 해당 프로젝트를 진행하게 되었습니다.

## 개발환경
- IntelliJ
- Postman
- GitHub
- SourceTree
- H2
- Visual Studio Code

## 사용기술

### “Backend”

-   Spring Boot 3.0.7, Java17
    -   스프링부터3버전 이상부터는 Java17버전이상만 가능하다.
-   Spring Security, OAuth2, JWT
-   Spring Data JPA
-   H2, MariaDB, Redis

### “Frontend”

-   HTML, CSS, Javascript, JQuery, Tailwind CSS
-   Toast UI, Tagify

### “Infra”

-   AWS EC2, S3, CodeDeploy, Elastic Cache, GithubAction

## 시스템 아키텍쳐

![image](https://github.com/StartDeveloperKim/ToyProject-Storage/assets/97887047/98c2f1cf-7ab1-47db-9c65-6f79e6ddf771)

## 주요기능
- [Github OAuth + JWT(AccessToken+RefreshToken)을 활용한 로그인](https://github.com/StartDeveloperKim/ToyProject-Storage#github-oauth--jwtaccesstokenrefreshtoken%EC%9D%84-%ED%99%9C%EC%9A%A9%ED%95%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EA%B5%AC%ED%98%84)
- [게시글 CRUD](https://github.com/StartDeveloperKim/ToyProject-Storage#%EA%B2%8C%EC%8B%9C%EA%B8%80-crud)
- [S3를 이미지 저장소로 사용](https://github.com/StartDeveloperKim/ToyProject-Storage#s3%EB%A5%BC-%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%A0%80%EC%9E%A5%EC%86%8C%EB%A1%9C-%EC%82%AC%EC%9A%A9)
- [태그 등록](https://github.com/StartDeveloperKim/ToyProject-Storage#%ED%83%9C%EA%B7%B8)
    - [태그 이름 반정규화를 통한 조회 성능 향상](https://github.com/StartDeveloperKim/ToyProject-Storage#%ED%83%9C%EA%B7%B8-%EC%9D%B4%EB%A6%84-%EB%B0%98%EC%A0%95%EA%B7%9C%ED%99%94%EB%A5%BC-%ED%86%B5%ED%95%9C-%EC%A1%B0%ED%9A%8C-%EC%84%B1%EB%8A%A5-%ED%96%A5%EC%83%81)
- [깃허브 댓글 기능 활용](https://github.com/StartDeveloperKim/ToyProject-Storage#%EB%8C%93%EA%B8%80)
- [좋아요 기능](https://github.com/StartDeveloperKim/ToyProject-Storage#%EC%A2%8B%EC%95%84%EC%9A%94)
    - [좋아요 개수 반정규화를 통한 성능 향상](https://github.com/StartDeveloperKim/ToyProject-Storage#%EC%A2%8B%EC%95%84%EC%9A%94-%EA%B0%9C%EC%88%98-db%EB%B0%98%EC%A0%95%EA%B7%9C%ED%99%94%EC%99%80-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8A%A4%EC%BC%80%EC%A5%B4%EB%9F%AC%EB%A5%BC-%ED%86%B5%ED%95%9C-%EC%84%B1%EB%8A%A5-%ED%96%A5%EC%83%81)
- [무한스크롤](https://github.com/StartDeveloperKim/ToyProject-Storage#%EB%AC%B4%ED%95%9C%EC%8A%A4%ED%81%AC%EB%A1%A4)
- [검색](https://github.com/StartDeveloperKim/ToyProject-Storage#%EA%B2%80%EC%83%89)
    - [태그기반 검색](https://github.com/StartDeveloperKim/ToyProject-Storage#%ED%83%9C%EA%B7%B8%EA%B8%B0%EB%B0%98-%EA%B2%80%EC%83%89)
    - [검색어 자동완성](https://github.com/StartDeveloperKim/ToyProject-Storage#%EA%B2%80%EC%83%89%EC%96%B4-%EC%9E%90%EB%8F%99%EC%99%84%EC%84%B1)
- [조건별 정렬](https://github.com/StartDeveloperKim/ToyProject-Storage#%EC%B5%9C%EC%8B%A0-%EC%88%9C-%EC%A2%8B%EC%95%84%EC%9A%94-%EC%88%9C-%EC%A0%95%EB%A0%AC)
- [CI/CD 배포자동화](https://github.com/StartDeveloperKim/ToyProject-Storage#cicd-%EB%B0%B0%ED%8F%AC%EC%9E%90%EB%8F%99%ED%99%94)
- [Spring Rest Docs를 활용한 API문서 관리]

## 주요기능 설명 및 동작화면
### Github OAuth + JWT(AccessToken+RefreshToken)을 활용한 로그인 구현
AWS 프리티어 사용자인 현 상황을 고려하여 OAuth와 JWT를 활용하여 로그인을 구현했습니다. 

블로그 프로젝트에서는 AccessToken만을 활용했지만 보안을 고려하여 RefreshToken도 함께 구현하였습니다.

프론트엔드가 SSR이기 때문에 쿠키로서 클라이언트에게 전달했고 HttpOnly 설정을 활성화해서 자바스크립트를 통한 쿠키 탈취문제를 예방했습니다.

![로그인](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/18a014ca-4463-4ba4-a0a3-c27b06e27264)

--------

### 게시글 CRUD
게시글 작성은 Toast Editor를 사용해서 마크다운형식으로 작성할 수 있도록 했습니다.

![게시글](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/d161245d-eae7-4681-ad8a-5ed219c31970)

--------

### S3를 이미지 저장소로 사용
이미지를 서버내에 저장하면 서버 자원을 효율적으로 사용하지 못할 것이기에 이미지 저장서버를 분리했습니다.

썸네일 또는 게시글에 포함되는 이미지를 AWS의 S3에 저장하도록하여 DB에는 이미지의 URL만 저장되도록 구성하였습니다.

![이미지](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/98f89a25-3c21-452e-abfa-e5ea9d948d15)

#### [Toast Editor 이미지 저장 문제]
토스트 에디터의 경우 이미지를 게시글에 함께 첨부해버리면 이미지가 Base64의 형태로 인코딩 되어버려 게시글의 사이즈가 엄청커지기에 문제가 발생합니다.

따라서 Toast Editor의 Hook 기능을 활용하여 이미지 첨부하면 AJAX를 활용하여 이미지를 multipart/form-data 형식으로 전달하여 S3에 저장한 후 
해당 이미지의 링크를 전송하는 방식으로 해당 문제를 해결했습니다.

--------

### 태그
프론트엔드 단에서는 Tagify를 사용하여 태그를 등록할 수 있도록하였습니다.

![2023-07-11 14;01;17](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/ec3a2fcd-114e-4974-bbe4-9335cb983386)


#### [태그 이름 반정규화를 통한 조회 성능 향상]
태그와 게시글은 다대다 관계로서 데이터의 중복을 줄이고자 BoardTag 테이블을 두었습니다.

이 때 게시글을 하나 조회하면 게시글과 BoardTag와의 Join 그리고 BoardTag와 Tag와의 Join 총 2번의 Join이 필요했습니다.

그래서 태그의 수가 많을 수록 조회의 성능을 떨어질 것이라고 판단했습니다.

게시글 테이블 컬럼에 태그리스트를 ','로 구분하여 문자열 형태로 삽입하는 반정규화를 진행하였고 조회성능을 2.9ms에서 1.0ms로 약 2.9배의 성능향상을 이뤄냈습니다.

--------

### 댓글
깃허브 이슈를 기반으로 댓글을 달 수 있게 만들어주는 깃허브 앱이 Utterances를 활용하여 구현하였습니다. User는 깃허브 계정이 있다면 댓글을 작성할 수 있습니다.

![2023-07-11 14;00;22](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/c07ead2d-a8c0-4285-9875-2f93f46adc6a)

--------

### 좋아요
게시글에 좋아요 추가 및 취소를 가능하게 했습니다. 

 좋아요 테이블을 따로 두어 사용자가 좋아요를 재등록 및 취소할 수도 있도록했습니다.

![2023-07-11 14;02;04](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/c19eb156-2eb8-4e43-a359-059d07fe4489)

#### [좋아요 개수 DB반정규화와 스프링 스케쥴러를 통한 성능 향상]

좋아요개수를 게시글을 조회할 때 마다 count하는 것은 성능상 문제가 있다고 생각했습니다. 그래서 좋아요개수 컬럼을 두는 반정규화를 통해 성능향상을 했습니다.

이 떄 JPA의 변경감지로 인해 발생하는 업데이트 무시가 때문에 두 데이터간의 동기화가 필요했습니다. 

그래서 스프링 스케쥴러를 사용해 특정 시간마다 두 데이터간의 정합성을 맞추는 작업을 했습니다.

---------

### 무한스크롤
제이쿼리의 scroll()을 통한 무한스크롤을 구현했습니다. 

메인 페이지에서는 스크롤 위치에 따라 4개의 게시글을 읽어오며 4개미만의 게시글이 조회되면 모든 글을 읽어왔기에 무한스크롤이 종료됩니다.

![2023-07-11 14;07;09](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/bec37e32-bffd-4c5f-a2b0-fc87e6582c8f)

--------

### 검색
#### 태그기반 검색
게시글에 등록되어있는 태그를 기반으로 검색이 가능하도록 했습니다.

![2023-07-11 14;08;02](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/70d50866-7fce-4c79-a8ad-9f2d8101a69e)

#### 검색어 자동완성
검색창에 문자를 입력하면 해당 문자를 접두사로 가진 단어를 dropdown 방식으로 보여줍니다.  

RDB에서 Like 문법을 통해 검색을 하면 오버헤드가 심하다고 판단되어 Redis를 사용했습니다.

sorted set 자료구조를 활용하여 입력단어와 prefix가 일치하는 데이터가 존재하면 해당 데이터의 완성형 단어를 응답하도록 구성했습니다.

또한 특정 시간이되면 Redis의 자동완성을 위한 데이터셋을 갱신하는 로직이 동작되도록 했습니다.

![2023-07-29 22;07;49](https://github.com/StartDeveloperKim/ToyProject-Storage/assets/97887047/142ded77-6fc4-4f75-93b5-a8fd00df60bf)

--------

### 최신 순, 좋아요 순 정렬
게시글을 최신 순, 좋아요 순으로 정렬하여 조회할 수 있습니다.

![2023-07-18 18;31;37](https://github.com/StartDeveloperKim/ToyProject-Storage/assets/97887047/b07ab3ff-24a0-47ff-bc06-927f9c50336a)

--------

### CI/CD 배포자동화
Github Action, S3, CodeDeploy를 활용하여 배포자동화를 구축하였습니다. 

개발자가 코드를 Push하면 Github Action은 빌드와 테스트 후 S3로 코드들의 압축파일을 전송합니다.

그리고 CodeDeploy에게 배포 요청을 하여 S3에 있는 압축파일을 EC2 인스턴스로 전달한 후 배포 스크립트를 동작시켜 기존의 프로세스를 종료한 후 새로운 빌드파일(jar)를 실행합니다.

![image](https://github.com/StartDeveloperKim/ToyProject-Storage/assets/97887047/d4750b5f-a27f-434a-bbbb-6eaa934e4761)

현재는 배포 중에 사이트가 중단되는 문제가 존재하여 추후에 nginx를 사용하여 무중단 배포를 구현할 계획에 있습니다.

---------

### Spring Rest Docs를 활용한 API 문서관리
처음에는 Swagger를 고려하였지만 아래의 문제점과 장점에 따라 Spring Rest Docs를 사용하여 API문서를 관리하기로 결정했습니다.

- Swagger는 API문서를 작성하기 위해 핵심로직에 Swagger관련 로직을 작성해야합니다. 따라서 문서관리 툴을 변경하겠다고 하면 핵심로직에 있는 어노테이션등을 모두 수정해야하는 불편함이 존재합니다.
- Spring Rest Docs는 테스트코드를 통해 API 문서를 작성하기 때문에 좀 더 안정적인 코드작성이 가능하다고 판단했습니다.

[Spring Rest Docs index.doc](https://github.com/StartDeveloperKim/ToyProject-Storage/blob/main/src/docs/asciidoc/index.adoc)
[API문서 index.html](https://github.com/StartDeveloperKim/ToyProject-Storage/blob/main/src/main/resources/static/docs/index.html)
