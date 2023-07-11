# 토이프로젝트 저장소
## 주제 선정 이유

개발자 준비를 하면서 여러사람들의 개인 프로젝트, 토이 프로젝트를 참고하거나 구경하려면 구글링을 통해 하나하나 찾는방법 밖에 없었다. 이러한 방법은 시간적인 소요가 너무 많이 들었다. 그래서 이러한 개인프로젝트, 토이프로젝트가 한 곳에 모여있으면 어떨까하는 생각을 해보았고 사람들이 이를 포스팅할 수 있는 플랫폼이 있으면 어떨까하는 생각에 이러한 주제를 선정하게 되었다.

## 개발환경
- IntelliJ
-   Postman
-   GitHub
-   SourceTree
-   H2
-   Visual Studio Code

## 사용기술

### “Backend”

-   Spring Boot 3.0.7, Java17
    -   스프링부터3버전 이상부터는 Java17버전이상만 가능하다.
-   Spring Security, JWT, OAuth2
-   SpringData JPA,
-   H2, MariaDB

### “Frontend”

-   HTML, CSS, Javascript, JQuery, Tailwind CSS
-   Toast Editor, Tagify

### “Infra”

-   AWS EC2, S3, CodeDeploy, GithubAction
## 주요기능
#### Github OAuth + JWT(AccessToken+RefreshToken)을 활용한 로그인 구현
AWS 프리티어 사용자인 현 상황을 고려하여 OAuth와 JWT를 활용하여 로그인을 구현했습니다. 이전 블로그 프로젝트에서는 AccessToken만을 활용했는데 보안을 고려하여 RefreshToken도 함께 구현하였습니다.

프론트엔드가 SSR이기 때문에 쿠키로서 클라이언트에게 전달했습니다. 또한 HttpOnly 설정을 활성화해서 자바스크립트를 통한 쿠키 탈취문제를 예방했습니다.

![로그인](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/18a014ca-4463-4ba4-a0a3-c27b06e27264)

--------

#### 게시글 CRUD
게시글 작성은 Toast Editor를 사용해서 마크다운형식으로 작성할 수 있도록 했습니다.

![게시글](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/d161245d-eae7-4681-ad8a-5ed219c31970)

--------

#### S3를 이미지 저장소로 사용
썸네일 또는 게시글에 포함되는 이미지를 AWS의 S3 서버에 저장하도록하여 DB에는 이미지의 URL만 저장되도록 구성하였습니다.

![이미지](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/98f89a25-3c21-452e-abfa-e5ea9d948d15)

##### [Toast Editor 이미지 저장 문제]
토스트 에디터의 경우 이미지를 게시글에 함께 첨부해버리면 이미지가 Base64의 형태로 인코딩 되어버려 게시글의 사이즈가 엄청커지기에 문제가 발생한다.
따라서 Toast Editor의 Hook 기능을 활용하여 이미지 첨부하면 AJAX를 활용하여 이미지를 multipart/form-data 형식으로 전달하여 S3에 저장한 후 
해당 이미지의 링크를 전송하는 방식으로 해당 문제를 해결하였다.

--------

#### 태그
프론트엔드 단에서는 Tagify를 사용하여 태그를 등록할 수 있도록하였고 지정된 태그가 아니면 사용자는 임의로 태그를 등록할 수 없습니다.

![2023-07-11 14;01;17](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/ec3a2fcd-114e-4974-bbe4-9335cb983386)


##### [태그 이름 반정규화를 통한 조회 성능 향상]
태그와 게시글은 다대다 관계로서 데이터의 중복을 줄이고자 BoardTag 테이블을 두었다. 이 때 게시글을 하나 조회하면 게시글과 BoardTag와의 Join
그리고 BoardTag와 Tag와의 Join 총 2번의 Join이 필요했다. 그래서 태그의 수가 많을 수록 조회의 성능을 떨어질 것이라고 판단했다.
게시글 테이블 컬럼에 태그리스트를 ','로 구분하여 문자열 형태로 삽입하는 반정규화를 진행하였고 조회성능을 2.9ms에서 1.0ms로 약 2.9배의 성능향상을 이뤄냈다.

--------

#### 댓글
깃허브 이슈를 기반으로 댓글을 달 수 있게 만들어주는 깃허브 앱이 Utterances를 활용하여 구현하였습니다. User는 깃허브 계정이 있다면 댓글을 작성할 수 있습니다.

![2023-07-11 14;00;22](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/c07ead2d-a8c0-4285-9875-2f93f46adc6a)

--------

#### 좋아요
게시글에 좋아요 추가 및 취소를 가능하게 했습니다. 

![2023-07-11 14;02;04](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/c19eb156-2eb8-4e43-a359-059d07fe4489)

##### [좋아요 개수 DB반정규화와 스프링 스케쥴러를 통한 성능 향상]
메인페이지에 좋아요개수를 나타낼 필요가 있는데 이를 매번 count하면 성능에 악영향을 끼칠 수 있다고 생각했습니다. 그래서 게시글 테이블에 좋아요개수 컬럼을 두고 이를 보여주는 방식으로 성능 향상을 이끌어 냈습니다. 하지만 이 경우 데이터 정합성 문제가 발생할 수도 있기에 스프링 스케쥴러를 사용하여 사용자가 적은 시간에 그 둘의 데이터를 동기화하는 작업을 자동으로 실행하도록 했습니다.

--------

#### 무한스크롤
제이쿼리의 scroll()을 통한 무한스크롤을 구현했습니다. 메인 페이지에서는 스크롤 위치에 따라 4개의 게시글을 읽어오며 4개이하의 게시글이 조회되면 모든 글을 읽어왔기에 무한스크롤이 종료됩니다.

![2023-07-11 14;07;09](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/bec37e32-bffd-4c5f-a2b0-fc87e6582c8f)

--------

#### 태그기반 검색
게시글에 등록되어있는 태그를 기반으로 검색이 가능하도록 했습니다.

![2023-07-11 14;08;02](https://github.com/StartDeveloperKim/Developer-SmallRoom/assets/97887047/70d50866-7fce-4c79-a8ad-9f2d8101a69e)

--------

